package micdoodle8.mods.galacticraft.core.entities.player;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderOrbit;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.Session;
import net.minecraft.world.World;

public class GCEntityClientPlayerMP extends EntityClientPlayerMP
{
    public GCEntityClientPlayerMP(Minecraft minecraft, World world, Session session, NetHandlerPlayClient netHandler, StatFileWriter statFileWriter)
    {
        super(minecraft, world, session, netHandler, statFileWriter);
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
        try {
        	super.onLivingUpdate();
        } catch (RuntimeException e)
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
        if (this.worldObj.provider instanceof WorldProviderOrbit)
    	{
	        if (FreefallHandler.testFreefall(this)) return false;
        	GCPlayerStatsClient stats = GCPlayerStatsClient.get(this);
	    	if (stats.inFreefall) return false;
	    	if (stats.landingTicks > 0) return true;
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

    @Override
    public void setInPortal()
    {
    	if (!(this.worldObj.provider instanceof IGalacticraftWorldProvider))
    	{
    		super.setInPortal();
    	}
    }
}
