package micdoodle8.mods.galacticraft.core.entities.player;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.Loader;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import micdoodle8.mods.galacticraft.core.event.EventWakePlayer;
import micdoodle8.mods.galacticraft.core.util.*;
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
    private boolean updatingRidden = false;
    public boolean openedSpaceRaceManager = false;

	public GCEntityPlayerMP(MinecraftServer server, WorldServer world, GameProfile profile, ItemInWorldManager itemInWorldManager)
	{
		super(server, world, profile, itemInWorldManager);
	}

	@Override
	public void clonePlayer(EntityPlayer oldPlayer, boolean keepInv)
	{
		super.clonePlayer(oldPlayer, keepInv);

		if (oldPlayer instanceof GCEntityPlayerMP)
		{
            this.openedSpaceRaceManager = ((GCEntityPlayerMP) oldPlayer).openedSpaceRaceManager;
			this.getPlayerStats().copyFrom(((GCEntityPlayerMP) oldPlayer).getPlayerStats(), keepInv || this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"));
		}
	}

    @Override
    public void updateRidden()
    {
        updatingRidden = true;
        super.updateRidden();
        updatingRidden = false;
    }

    @Override
    public void mountEntity(Entity par1Entity)
    {
        if (updatingRidden && this.ridingEntity instanceof IIgnoreShift && ((IIgnoreShift) this.ridingEntity).shouldIgnoreShiftExit())
        {
            return;
        }

        super.mountEntity(par1Entity);
    }

	@Override
	public void moveEntity(double par1, double par3, double par5)
	{
		super.moveEntity(par1, par3, par5);

		// If the player is on the moon, not airbourne and not riding anything
		if (this.worldObj.provider instanceof WorldProviderMoon && !this.worldObj.isRemote && this.ridingEntity == null)
		{	
			GCPlayerHandler.updateFeet(this, par1, par5);
		}
	}

	@Override
	public void wakeUpPlayer(boolean par1, boolean par2, boolean par3)
	{
		this.wakeUpPlayer(par1, par2, par3, false);
	}

	public void wakeUpPlayer(boolean par1, boolean par2, boolean par3, boolean bypass)
	{
		ChunkCoordinates c = this.playerLocation;

		if (c != null)
		{
			EventWakePlayer event = new EventWakePlayer(this, c.posX, c.posY, c.posZ, par1, par2, par3, bypass);
			MinecraftForge.EVENT_BUS.post(event);

			if (bypass || event.result == null || event.result == EnumStatus.OK)
			{
				super.wakeUpPlayer(par1, par2, par3);
			}
		}
	}

	public final GCPlayerStats getPlayerStats()
	{
		return GCPlayerStats.get(this);
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
		if (Loader.isModLoaded(Constants.MOD_ID_PLANETS))
		{
			if (par1DamageSource == DamageSource.outOfWorld)
	    	{
	    		if (this.worldObj.provider instanceof WorldProviderAsteroids)
	    		{
	    			if (this.posY > -120D) return false;
	    			if (this.posY > -180D) par2 /= 2;
	    		}
	    	}          	
	    	else if (par1DamageSource == DamageSource.fall || par1DamageSource == DamageSourceGC.spaceshipCrash)
	    	{
	    		int titaniumCount = 0;
                if (this.inventory != null)
                {
                    for (int i = 0; i < 4; i++)
                    {
                        ItemStack armorPiece = this.getCurrentArmor(i);
                        if (armorPiece != null && armorPiece.getItem() instanceof ItemArmorAsteroids)
                        {
                            titaniumCount++;
                        }
                    }
                }
	    		if (titaniumCount == 4) titaniumCount = 5;
	    		par2 *= (1 - 0.15D * titaniumCount);
	    	}
		}

        return super.attackEntityFrom(par1DamageSource, par2);
    }
	
	@Override
    public void knockBack(Entity p_70653_1_, float p_70653_2_, double impulseX, double impulseZ)
    {
		int deshCount = 0;
        if (this.inventory != null && Loader.isModLoaded(Constants.MOD_ID_PLANETS)) 
        {
            for (int i = 0; i < 4; i++)
            {
                ItemStack armorPiece = this.getCurrentArmor(i);
                if (armorPiece != null && armorPiece.getItem() instanceof ItemArmorMars)
                {
                    deshCount++;
                }
            }
		}

        if (this.rand.nextDouble() >= this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue())
        {
            this.isAirBorne = deshCount < 2;
            float f1 = MathHelper.sqrt_double(impulseX * impulseX + impulseZ * impulseZ);
            float f2 = 0.4F - deshCount * 0.05F;
            double d1 = 2.0D - deshCount * 0.15D;
            this.motionX /= d1;
            this.motionY /= d1;
            this.motionZ /= d1;
            this.motionX -= f2 * impulseX / f1;
            this.motionY += f2;
            this.motionZ -= f2 * impulseZ / f1;

            if (this.motionY > 0.4D)
            {
                this.motionY = 0.4D;
            }
        }
    }
}
