package micdoodle8.mods.galacticraft.core.entities.player;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import micdoodle8.mods.galacticraft.core.event.EventWakePlayer;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
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

import java.util.HashMap;
import java.util.Map.Entry;

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
	
	protected void sendPlanetList()
	{
		HashMap<String, Integer> map = WorldUtil.getArrayOfPossibleDimensions(WorldUtil.getPossibleDimensionsForSpaceshipTier(this.getPlayerStats().spaceshipTier), this);

		String temp = "";
		int count = 0;

		for (Entry<String, Integer> entry : map.entrySet())
		{
			temp = temp.concat(entry.getKey() + (count < map.entrySet().size() - 1 ? "?" : ""));
			count++;
		}

		GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_DIMENSION_LIST, new Object[] { this.getGameProfile().getName(), temp }), this);
	}

	protected void sendAirRemainingPacket()
	{
		final float f1 = Float.valueOf(this.getPlayerStats().tankInSlot1 == null ? 0.0F : this.getPlayerStats().tankInSlot1.getMaxDamage() / 90.0F);
		final float f2 = Float.valueOf(this.getPlayerStats().tankInSlot2 == null ? 0.0F : this.getPlayerStats().tankInSlot2.getMaxDamage() / 90.0F);
		GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_AIR_REMAINING, new Object[] { MathHelper.floor_float(this.getPlayerStats().airRemaining / f1), MathHelper.floor_float(this.getPlayerStats().airRemaining2 / f2), this.getGameProfile().getName() }), this);
	}

	protected void sendThermalLevelPacket()
	{
		GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_THERMAL_LEVEL, new Object[] { this.getPlayerStats().thermalLevel }), this);
	}

	protected void sendGearUpdatePacket(EnumModelPacket gearType)
	{
		this.sendGearUpdatePacket(gearType, -1);
	}

	private void sendGearUpdatePacket(EnumModelPacket gearType, int subtype)
	{
		MinecraftServer theServer = FMLCommonHandler.instance().getMinecraftServerInstance();
		if (theServer != null && PlayerUtil.getPlayerForUsernameVanilla(theServer, this.getGameProfile().getName()) != null)
		{
			GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_UPDATE_GEAR_SLOT, new Object[] { this.getGameProfile().getName(), gearType.ordinal(), subtype }), new TargetPoint(this.worldObj.provider.dimensionId, this.posX, this.posY, this.posZ, 50.0D));
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

	public void setUsingParachute(boolean tf)
	{
		this.getPlayerStats().usingParachute = tf;

		if (tf)
		{
			int subtype = -1;

			if (this.getPlayerStats().parachuteInSlot != null)
			{
				subtype = this.getPlayerStats().parachuteInSlot.getItemDamage();
			}

			this.sendGearUpdatePacket(EnumModelPacket.ADD_PARACHUTE, subtype);
		}
		else
		{
			this.sendGearUpdatePacket(EnumModelPacket.REMOVE_PARACHUTE);
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

	public static enum EnumModelPacket
	{
		ADDMASK, REMOVEMASK, ADDGEAR, REMOVEGEAR, ADDLEFTREDTANK, ADDLEFTORANGETANK, ADDLEFTGREENTANK, REMOVE_LEFT_TANK, ADDRIGHTREDTANK, ADDRIGHTORANGETANK, ADDRIGHTGREENTANK, REMOVE_RIGHT_TANK, ADD_PARACHUTE, REMOVE_PARACHUTE, ADD_FREQUENCY_MODULE, REMOVE_FREQUENCY_MODULE, ADD_THERMAL_HELMET, ADD_THERMAL_CHESTPLATE, ADD_THERMAL_LEGGINGS, ADD_THERMAL_BOOTS, REMOVE_THERMAL_HELMET, REMOVE_THERMAL_CHESTPLATE, REMOVE_THERMAL_LEGGINGS, REMOVE_THERMAL_BOOTS
	}
}
