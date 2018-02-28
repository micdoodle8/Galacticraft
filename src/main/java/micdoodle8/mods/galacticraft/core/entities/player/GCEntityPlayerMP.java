package micdoodle8.mods.galacticraft.core.entities.player;

import com.mojang.authlib.GameProfile;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.WorldServer;

/**
 * This class provides various hooks which are missing from Forge or don't quite do what we need.
 * </p>
 * Do not reference this or test 'instance of' this in your code:
 * if PlayerAPI is installed, GCEntityPlayerMP will not be used.
 */
public class GCEntityPlayerMP extends EntityPlayerMP
{
    public GCEntityPlayerMP(MinecraftServer server, WorldServer world, GameProfile profile, ItemInWorldManager itemInWorldManager)
    {
        super(server, world, profile, itemInWorldManager);
        if (this.worldObj != world)
        {
            GCPlayerStats.get(this).setStartDimension(WorldUtil.getDimensionName(this.worldObj.provider));
        }
    }

    @Override
    public void updateRidden()
    {
        GalacticraftCore.proxy.player.updateRiddenPre(this);
        super.updateRidden();
        GalacticraftCore.proxy.player.updateRiddenPost(this);
    }

    @Override
    public void mountEntity(Entity par1Entity)
    {
        if (!GalacticraftCore.proxy.player.mountEntity(this, par1Entity))
        {
            super.mountEntity(par1Entity);
        }
    }

    @Override
    public void moveEntity(double par1, double par3, double par5)
    {
        super.moveEntity(par1, par3, par5);
        GalacticraftCore.proxy.player.moveEntity(this, par1, par3, par5);
    }

    @Override
    public void wakeUpPlayer(boolean par1, boolean par2, boolean par3)
    {
        if (GalacticraftCore.proxy.player.wakeUpPlayer(this, par1, par2, par3))
        {
            return;
        }
        super.wakeUpPlayer(par1, par2, par3);
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        par2 = GalacticraftCore.proxy.player.attackEntityFrom(this, par1DamageSource, par2);
        return par2 != -1F && super.attackEntityFrom(par1DamageSource, par2);
    }

    @Override
    public void knockBack(Entity p_70653_1_, float p_70653_2_, double impulseX, double impulseZ)
    {
        GalacticraftCore.proxy.player.knockBack(this, p_70653_1_, p_70653_2_, impulseX, impulseZ);
    }

    @Override
    public boolean isSpectator()
    {
        return GalacticraftCore.proxy.player.isSpectator(this) || super.isSpectator();
    }

    /*@Override
    public void setInPortal()
    {
    	if (!(this.worldObj.provider instanceof IGalacticraftWorldProvider))
    	{
    		super.setInPortal();
    	}
    } TODO Fix disable of portal */
}
