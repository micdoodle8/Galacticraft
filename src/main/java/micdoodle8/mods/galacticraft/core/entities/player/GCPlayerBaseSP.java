package micdoodle8.mods.galacticraft.core.entities.player;

import api.player.client.ClientPlayerAPI;
import api.player.client.ClientPlayerBase;
import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.TransformerHooks;
import micdoodle8.mods.galacticraft.core.client.EventHandlerClient;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
        if (this.player.world.provider instanceof IZeroGDimension)
        {
            GCPlayerStatsClient stats = GCPlayerStatsClient.get(this.player);
            if (stats.getLandingTicks() > 0)
            {
                this.player.movementInput.moveStrafe *= 0.5F;
                this.player.movementInput.moveForward *= 0.5F;
            }

            //TODO: equivalent to getEyeHeight() in GCEntityClientPlayerMP

            //TODO: set this.player.flyToggleTimer = 0;
        }
    }

    @Override
    public void afterUpdateEntityActionState()
    {
        if (this.player.world.provider instanceof IZeroGDimension)
        {
            this.player.setJumping(false);
            AxisAlignedBB aABB = this.player.getEntityBoundingBox();
            if ((aABB.minY % 1D) == 0.5D) this.player.setEntityBoundingBox(aABB.offset(0D, 0.00001D, 0D));
        }
    }

    @Override
    public void moveEntity(MoverType moverType, double x, double y, double z)
    {
        super.moveEntity(moverType, x, y, z);
        this.getClientHandler().move(this.player, moverType, x, y, z);
    }

    @Override
    public void afterMoveEntityWithHeading(float paramFloat1, float paramFloat2, float paramFloat3)
    {
        super.afterMoveEntityWithHeading(paramFloat1, paramFloat2, paramFloat3);

        if (CompatibilityManager.isSmartMovingLoaded && !this.player.capabilities.isFlying)
        {
            this.player.motionY += 0.080000000000000002D;
            this.player.motionY -= TransformerHooks.getGravityForEntity(this.player);
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
        if (this.player.world.provider instanceof IZeroGDimension)
    	{
            GCPlayerStatsClient stats = GCPlayerStatsClient.get(this.player);
        	if (stats.getLandingTicks() > 0)
        	{
        	    if (this.lastLandingTicks == 0)
        	        this.lastLandingTicks = stats.getLandingTicks();

        	    return stats.getLandingTicks() < this.lastLandingTicks;
        	}
        	else
        	    this.lastLandingTicks = 0;

        	if (stats.getFreefallHandler().pjumpticks > 0) return true;

        	if (EventHandlerClient.sneakRenderOverride)
        	{
        	    if (stats.getFreefallHandler().testFreefall(this.player)) return false;
        	    if (stats.isInFreefall()) return false;
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

    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender()
    {
        double height = this.player.posY + (double)this.player.getEyeHeight();
        if (height > 255D) height = 255D;
        BlockPos blockpos = new BlockPos(this.player.posX, height, this.player.posZ);
        return this.player.world.isBlockLoaded(blockpos) ? this.player.world.getCombinedLight(blockpos, 0) : 0;
    }
}