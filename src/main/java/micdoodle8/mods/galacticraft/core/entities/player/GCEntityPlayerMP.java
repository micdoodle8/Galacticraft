package micdoodle8.mods.galacticraft.core.entities.player;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.Loader;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import micdoodle8.mods.galacticraft.core.event.EventWakePlayer;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.DamageSourceGC;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.WorldProviderAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemArmorAsteroids;
import micdoodle8.mods.galacticraft.planets.mars.items.ItemArmorMars;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;

public class GCEntityPlayerMP extends EntityPlayerMP
{
    public GCEntityPlayerMP(MinecraftServer server, WorldServer world, GameProfile profile, ItemInWorldManager itemInWorldManager)
	{
		super(server, world, profile, itemInWorldManager);
	}

    private IPlayerServer getClientHandler()
    {
        return new PlayerServer();
    }

	@Override
	public void clonePlayer(EntityPlayer oldPlayer, boolean keepInv)
	{
		super.clonePlayer(oldPlayer, keepInv);
        this.getClientHandler().clonePlayer(this, oldPlayer, keepInv);
	}

    @Override
    public void updateRidden()
    {
        this.getClientHandler().updateRiddenPre(this);
        super.updateRidden();
        this.getClientHandler().updateRiddenPost(this);
    }

    @Override
    public void mountEntity(Entity par1Entity)
    {
        if (!this.getClientHandler().mountEntity(this, par1Entity))
        {
            super.mountEntity(par1Entity);
        }
    }

	@Override
	public void moveEntity(double par1, double par3, double par5)
	{
		super.moveEntity(par1, par3, par5);
        this.getClientHandler().moveEntity(this, par1, par3, par5);
	}

	@Override
	public void wakeUpPlayer(boolean par1, boolean par2, boolean par3)
	{
		if (!this.getClientHandler().wakeUpPlayer(this, par1, par2, par3))
        {
            super.wakeUpPlayer(par1, par2, par3);
        }
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        par2 = this.getClientHandler().attackEntityFrom(this, par1DamageSource, par2);

        if (par2 == -1)
        {
            return false;
        }

        return super.attackEntityFrom(par1DamageSource, par2);
    }

	@Override
    public void knockBack(Entity p_70653_1_, float p_70653_2_, double impulseX, double impulseZ)
    {
        this.getClientHandler().knockBack(this, p_70653_1_, p_70653_2_, impulseX, impulseZ);
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
