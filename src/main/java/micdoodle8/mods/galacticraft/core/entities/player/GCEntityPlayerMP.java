package micdoodle8.mods.galacticraft.core.entities.player;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTelemetry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.WorldServer;

import com.mojang.authlib.GameProfile;

public class GCEntityPlayerMP extends EntityPlayerMP
{
    public GCEntityPlayerMP(MinecraftServer server, WorldServer world, GameProfile profile, ItemInWorldManager itemInWorldManager)
    {
        super(server, world, profile, itemInWorldManager);
    }

    //Server-only method
    @Override
    public void clonePlayer(EntityPlayer oldPlayer, boolean keepInv)
    {
        super.clonePlayer(oldPlayer, keepInv);
        GalacticraftCore.proxy.player.clonePlayer(this, oldPlayer, keepInv);
        TileEntityTelemetry.updateLinkedPlayer((EntityPlayerMP) oldPlayer, this);
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
        if (!GalacticraftCore.proxy.player.wakeUpPlayer(this, par1, par2, par3))
        {
            super.wakeUpPlayer(par1, par2, par3);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        par2 = GalacticraftCore.proxy.player.attackEntityFrom(this, par1DamageSource, par2);

        if (par2 == -1)
        {
            return false;
        }

        return super.attackEntityFrom(par1DamageSource, par2);
    }

    @Override
    public void knockBack(Entity p_70653_1_, float p_70653_2_, double impulseX, double impulseZ)
    {
        GalacticraftCore.proxy.player.knockBack(this, p_70653_1_, p_70653_2_, impulseX, impulseZ);
    }
}
