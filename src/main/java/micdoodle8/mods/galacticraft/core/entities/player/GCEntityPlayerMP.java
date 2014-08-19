package micdoodle8.mods.galacticraft.core.entities.player;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.Loader;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import micdoodle8.mods.galacticraft.core.event.EventWakePlayer;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.util.*;
import micdoodle8.mods.galacticraft.core.wrappers.Footprint;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.WorldProviderAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemArmorAsteroids;
import net.minecraft.entity.Entity;
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
			this.updateFeet(par1, par5);
		}
	}

	private void updateFeet(double motionX, double motionZ)
	{
		double motionSqrd = motionX * motionX + motionZ * motionZ;
		if (motionSqrd > 0.001D)
		{
			int iPosX = MathHelper.floor_double(this.posX);
			int iPosY = MathHelper.floor_double(this.posY) - 1;
			int iPosZ = MathHelper.floor_double(this.posZ);

			// If the block below is the moon block
			if (this.worldObj.getBlock(iPosX, iPosY, iPosZ) == GCBlocks.blockMoon)
			{
				// And is the correct metadata (moon turf)
				if (this.worldObj.getBlockMetadata(iPosX, iPosY, iPosZ) == 5)
				{
					GCPlayerStats playerStats = this.getPlayerStats(); 
					// If it has been long enough since the last step
					if (playerStats.distanceSinceLastStep > 0.35D)
					{
						Vector3 pos = new Vector3(this);
						// Set the footprint position to the block below and add random number to stop z-fighting
						pos.y = MathHelper.floor_double(this.posY - 1D) + this.rand.nextFloat() / 100.0F;

						// Adjust footprint to left or right depending on step count
						switch (playerStats.lastStep)
						{
						case 0:
							float a = (-this.rotationYaw + 90F) / 57.295779513F;
							pos.translate(new Vector3(MathHelper.sin(a) * 0.25F, 0, MathHelper.cos(a) * 0.25F));
							break;
						case 1:
							a = (-this.rotationYaw - 90F) / 57.295779513F;
							pos.translate(new Vector3(MathHelper.sin(a) * 0.25, 0, MathHelper.cos(a) * 0.25));
							break;
						}

						TickHandlerServer.addFootprint(new Footprint(this.worldObj.provider.dimensionId, pos, this.rotationYaw), this.worldObj.provider.dimensionId);

						// Increment and cap step counter at 1
						playerStats.lastStep++;
						playerStats.lastStep %= 2;
						playerStats.distanceSinceLastStep = 0;
					}
					else
					{
						playerStats.distanceSinceLastStep += motionSqrd;
					}
				}
			}
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
}
