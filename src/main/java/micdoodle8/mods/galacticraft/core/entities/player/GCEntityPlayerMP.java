package micdoodle8.mods.galacticraft.core.entities.player;

import com.mojang.authlib.GameProfile;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.WorldServer;

public class GCEntityPlayerMP extends EntityPlayerMP
{
    public GCEntityPlayerMP(MinecraftServer server, WorldServer world, GameProfile profile, ItemInWorldManager itemInWorldManager)
    {
        super(server, world, profile, itemInWorldManager);
    }

    private IPlayerServer getServerHandler()
    {
        return GalacticraftCore.proxy.player;
    }

    @Override
    public void clonePlayer(EntityPlayer oldPlayer, boolean keepInv)
    {
        super.clonePlayer(oldPlayer, keepInv);
        this.getServerHandler().clonePlayer(this, oldPlayer, keepInv);
    }

    @Override
    public void updateRidden()
    {
        this.getServerHandler().updateRiddenPre(this);
        super.updateRidden();
        this.getServerHandler().updateRiddenPost(this);
    }

    @Override
    public void mountEntity(Entity par1Entity)
    {
        if (!this.getServerHandler().mountEntity(this, par1Entity))
        {
            super.mountEntity(par1Entity);
        }
    }

    @Override
    public void moveEntity(double par1, double par3, double par5)
    {
        super.moveEntity(par1, par3, par5);
        this.getServerHandler().moveEntity(this, par1, par3, par5);
    }

    @Override
    public void wakeUpPlayer(boolean par1, boolean par2, boolean par3)
    {
        if (!this.getServerHandler().wakeUpPlayer(this, par1, par2, par3))
        {
            super.wakeUpPlayer(par1, par2, par3);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        par2 = this.getServerHandler().attackEntityFrom(this, par1DamageSource, par2);

        if (par2 == -1)
        {
            return false;
        }

        return super.attackEntityFrom(par1DamageSource, par2);
    }

    @Override
    public void knockBack(Entity p_70653_1_, float p_70653_2_, double impulseX, double impulseZ)
    {
        this.getServerHandler().knockBack(this, p_70653_1_, p_70653_2_, impulseX, impulseZ);
    }

    public final GCPlayerStats getPlayerStats()
    {
        return GCPlayerStats.get(this);
    }

    public static GCPlayerStats getPlayerStats(EntityPlayerMP player)
    {
        return GCPlayerStats.get(player);
    }
}
