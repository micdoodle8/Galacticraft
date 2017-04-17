package micdoodle8.mods.galacticraft.core.entities.player;

import api.player.client.ClientPlayerAPI;
import api.player.client.ClientPlayerBase;
import cpw.mods.fml.common.Loader;
import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;

public class GCPlayerBaseSP extends ClientPlayerBase
{
    boolean lastIsFlying;
    int lastLandingTicks;

    
    public GCPlayerBaseSP(ClientPlayerAPI playerAPI)
    {
        super(playerAPI);
    }

    private IPlayerClient getClientHandler()
    {
        return ClientProxyCore.playerClientHandler;
    }

//    @Override
//    public void wakeUpPlayer(boolean par1, boolean par2, boolean par3)
//    {
//        if (!this.getClientHandler().wakeUpPlayer(this, par1, par2, par3))
//        {
//            super.wakeUpPlayer(par1, par2, par3);
//        }
//    }

    @Override
    public boolean isEntityInsideOpaqueBlock()
    {
        return this.getClientHandler().isEntityInsideOpaqueBlock(this.player, super.isEntityInsideOpaqueBlock());
    }

    @Override
    public void onLivingUpdate()
    {
        this.getClientHandler().onLivingUpdatePre(this.player);
        super.onLivingUpdate();
        this.getClientHandler().onLivingUpdatePost(this.player);
    }
    
    @Override
    public void beforeUpdateEntityActionState()
    {
        if (this.player.worldObj.provider instanceof IZeroGDimension)
        {
            GCPlayerStatsClient stats = GCPlayerStatsClient.get(this.player);
            if (stats.landingTicks > 0)
            {
                this.player.ySize = stats.landingYOffset[stats.landingTicks];
                this.player.movementInput.moveStrafe *= 0.5F;
                this.player.movementInput.moveForward *= 0.5F;
                if (this.player.movementInput.sneak && this.player.ySize < 0.2F)
                {
                    this.player.ySize = 0.2F;
                }
            }
            else if (stats.pjumpticks > 0)
            {
                this.player.ySize = 0.01F * stats.pjumpticks;
            }
            else if (!this.player.onGround || stats.inFreefall)
            {
                this.player.ySize = 0F;
            }
            
            //TODO: set this.player.flyToggleTimer = 0;
        }        
    }

    @Override
    public void afterUpdateEntityActionState()
    {
        if (this.player.worldObj.provider instanceof IZeroGDimension)
        {
            this.player.setJumping(false);
            if ((this.player.boundingBox.minY % 1F) == 0.5F) this.player.boundingBox.minY += 0.00001F;
        }
    }
    
    @Override
    public void moveEntity(double par1, double par3, double par5)
    {
        super.moveEntity(par1, par3, par5);
        this.getClientHandler().moveEntity(this.player, par1, par3, par5);
    }

    @Override
    public void afterMoveEntityWithHeading(float paramFloat1, float paramFloat2)
    {
        super.afterMoveEntityWithHeading(paramFloat1, paramFloat2);

        if (Loader.isModLoaded("SmartMoving") && !this.player.capabilities.isFlying)
        {
            this.player.motionY += 0.080000000000000002D;
            this.player.motionY -= WorldUtil.getGravityForEntity(this.player);
        }
    }

    @Override
    public void onUpdate()
    {
        this.getClientHandler().onUpdate(this.player);
        super.onUpdate();
    }

    @Override
    public boolean isSneaking()
    {
        if (this.player.worldObj.provider instanceof IZeroGDimension)
    	{
            GCPlayerStatsClient stats = GCPlayerStatsClient.get(this.player);
            if (stats.landingTicks > 0)
            {
             if (this.lastLandingTicks == 0)
                 this.lastLandingTicks = stats.landingTicks;
             
             return stats.landingTicks < this.lastLandingTicks;
             }
         else
             this.lastLandingTicks = 0;
         if (stats.pjumpticks > 0) return true;
            if (ClientProxyCore.sneakRenderOverride)
            {
                if (FreefallHandler.testFreefall(this.player)) return false;
                if (stats.inFreefall) return false;
            }
    	}
        return super.isSneaking();
    }

//    @Override
//    @SideOnly(Side.CLIENT)
//    public float getBedOrientationInDegrees()
//    {
//        return this.getClientHandler().getBedOrientationInDegrees(this, super.getBedOrientationInDegrees());
//    }
}
