package micdoodle8.mods.galacticraft.core.entities.player;

import micdoodle8.mods.galacticraft.core.dimension.WorldProviderZeroGravity;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GCEntityClientPlayerMP extends EntityPlayerSP
{
    public GCEntityClientPlayerMP(Minecraft mcIn, World worldIn, NetHandlerPlayClient netHandler, StatisticsManager statFileWriter)
    {
        super(mcIn, worldIn, netHandler, statFileWriter);
    }

    @Override
    public void wakeUpPlayer(boolean par1, boolean par2, boolean par3)
    {
        if (!ClientProxyCore.playerClientHandler.wakeUpPlayer(this, par1, par2, par3))
        {
            super.wakeUpPlayer(par1, par2, par3);
        }
    }

    @Override
    public boolean isEntityInsideOpaqueBlock()
    {
        return ClientProxyCore.playerClientHandler.isEntityInsideOpaqueBlock(this, super.isEntityInsideOpaqueBlock());
    }

    @Override
    public void onLivingUpdate()
    {
        ClientProxyCore.playerClientHandler.onLivingUpdatePre(this);
        try
        {
            super.onLivingUpdate();
        }
        catch (RuntimeException e)
        {
            FMLLog.severe("A mod has crashed while Minecraft was doing a normal player tick update.  See details below.  GCEntityClientPlayerMP is in this because that is the player class name when Galacticraft is installed.  This is =*NOT*= a bug in Galacticraft, please report it to the mod indicated by the first lines of the crash report.");
            throw (e);
        }
        ClientProxyCore.playerClientHandler.onLivingUpdatePost(this);
    }

    @Override
    public void moveEntity(double par1, double par3, double par5)
    {
        super.moveEntity(par1, par3, par5);
        ClientProxyCore.playerClientHandler.moveEntity(this, par1, par3, par5);
    }

    @Override
    public void onUpdate()
    {
        ClientProxyCore.playerClientHandler.onUpdate(this);
        super.onUpdate();
    }

    @Override
    public boolean isSneaking()
    {
        if (this.worldObj.provider instanceof WorldProviderZeroGravity)
        {
            IStatsClientCapability stats = this.getCapability(CapabilityStatsClientHandler.GC_STATS_CLIENT_CAPABILITY, null);
            if (stats.getFreefallHandler().testFreefall(this))
            {
                return false;
            }
            if (stats.isInFreefall())
            {
                return false;
            }
            if (stats.getLandingTicks() > 0)
            {
                return true;
            }
        }
        return super.isSneaking();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getBedOrientationInDegrees()
    {
        return ClientProxyCore.playerClientHandler.getBedOrientationInDegrees(this, super.getBedOrientationInDegrees());
    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public void setVelocity(double xx, double yy, double zz)
//    {
//    	if (this.worldObj.provider instanceof WorldProviderOrbit)
//    	{
//    		((WorldProviderOrbit)this.worldObj.provider).setVelocityClient(this, xx, yy, zz);	
//    	}
//    	super.setVelocity(xx, yy, zz);
//    }
//

    /*@Override
    public void setInPortal()
    {
    	if (!(this.worldObj.provider instanceof IGalacticraftWorldProvider))
    	{
    		super.setInPortal();
    	}
    } TODO Fix disable of portal */
}
