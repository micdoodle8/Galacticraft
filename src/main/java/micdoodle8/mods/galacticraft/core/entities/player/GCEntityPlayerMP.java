package micdoodle8.mods.galacticraft.core.entities.player;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.api.event.oxygen.GCCoreOxygenSuffocationEvent;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IAtmosphericGas;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteor;
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
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
		this.updateFeet(par1, par5);
	}

	private void updateFeet(double motionX, double motionZ)
	{
		double motionSqrd = motionX * motionX + motionZ * motionZ;

		// If the player is on the moon, not airbourne and not riding anything
		if (this.worldObj.provider instanceof WorldProviderMoon && motionSqrd > 0.001D && this.ridingEntity == null)
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
					// If it has been long enough since the last step
					if (this.getPlayerStats().distanceSinceLastStep > 0.35D)
					{
						Vector3 pos = new Vector3(this);
						// Set the footprint position to the block below and add random number to stop z-fighting
						pos.y = MathHelper.floor_double(this.posY - 1D) + this.rand.nextFloat() / 100.0F;

						// Adjust footprint to left or right depending on step count
						switch (this.getPlayerStats().lastStep)
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
						this.getPlayerStats().lastStep++;
						this.getPlayerStats().lastStep %= 2;
						this.getPlayerStats().distanceSinceLastStep = 0;
					}
					else
					{
						this.getPlayerStats().distanceSinceLastStep += motionSqrd;
					}
				}
			}
		}
	}

	protected void checkCurrentItem()
	{
		ItemStack theCurrentItem = this.inventory.getCurrentItem();
		boolean noAtmosphericCombustion = this.worldObj.provider instanceof IGalacticraftWorldProvider && (!((IGalacticraftWorldProvider)this.worldObj.provider).isGasPresent(IAtmosphericGas.OXYGEN) || ((IGalacticraftWorldProvider)this.worldObj.provider).isGasPresent(IAtmosphericGas.CO2));
		if (noAtmosphericCombustion && theCurrentItem != null)
		{
			final int var1 = theCurrentItem.stackSize;
			final int var2 = theCurrentItem.getItemDamage();

			if (this.inventory.getCurrentItem().getItem() == Item.getItemFromBlock(Blocks.torch))
			{
				final ItemStack stack = new ItemStack(GCBlocks.unlitTorch, var1, 0);
				this.inventory.mainInventory[this.inventory.currentItem] = stack;
			}
		}
		else if (!noAtmosphericCombustion && theCurrentItem != null)
		{
			final int var1 = theCurrentItem.stackSize;
			final int var2 = theCurrentItem.getItemDamage();

			if (this.inventory.getCurrentItem().getItem() == Item.getItemFromBlock(GCBlocks.unlitTorch))
			{
				final ItemStack stack = new ItemStack(Blocks.torch, var1, 0);
				this.inventory.mainInventory[this.inventory.currentItem] = stack;
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

	public static class ThermalArmorEvent extends cpw.mods.fml.common.eventhandler.Event
	{
		public ArmorAddResult armorResult = ArmorAddResult.NOTHING;
		public final int armorIndex;
		public final ItemStack armorStack;

		public ThermalArmorEvent(int armorIndex, ItemStack armorStack)
		{
			this.armorIndex = armorIndex;
			this.armorStack = armorStack;
		}

		public void setArmorAddResult(ArmorAddResult result)
		{
			this.armorResult = result;
		}

		public enum ArmorAddResult
		{
			ADD, REMOVE, NOTHING
		}
	}

	protected void checkThermalStatus()
	{
		GCPlayerStats playerStats = this.getPlayerStats();
		ItemStack thermalPaddingHelm = playerStats.extendedInventory.getStackInSlot(6);
		ItemStack thermalPaddingChestplate = playerStats.extendedInventory.getStackInSlot(7);
		ItemStack thermalPaddingLeggings = playerStats.extendedInventory.getStackInSlot(8);
		ItemStack thermalPaddingBoots = playerStats.extendedInventory.getStackInSlot(9);

		if (this.worldObj.provider instanceof IGalacticraftWorldProvider && !this.capabilities.isCreativeMode)
		{
			IGalacticraftWorldProvider provider = (IGalacticraftWorldProvider) this.worldObj.provider;
			float thermalLevelMod = provider.getThermalLevelModifier();

			if (thermalLevelMod != 0)
			{
				int thermalLevelCooldownBase = (int) Math.floor(1 / (thermalLevelMod * (thermalLevelMod > 0 ? 1 : -1)) * 200);
				int thermalLevelTickCooldown = thermalLevelCooldownBase;

				if (Loader.isModLoaded(Constants.MOD_ID_PLANETS))
				{
					if (thermalPaddingHelm != null && thermalPaddingChestplate != null && thermalPaddingLeggings != null && thermalPaddingBoots != null)
					{
						int last = playerStats.thermalLevel;

						if (playerStats.thermalLevel < 0)
						{
							playerStats.thermalLevel += 1;
						}
						else if (playerStats.thermalLevel > 0)
						{
							playerStats.thermalLevel -= 1;
						}

						if (playerStats.thermalLevel != last)
						{
							this.sendThermalLevelPacket();
						}

						// Player is wearing all required thermal padding items
						return;
					}

					if (thermalPaddingHelm != null)
					{
						thermalLevelTickCooldown += thermalLevelCooldownBase;
					}

					if (thermalPaddingChestplate != null)
					{
						thermalLevelTickCooldown += thermalLevelCooldownBase;
					}

					if (thermalPaddingLeggings != null)
					{
						thermalLevelTickCooldown += thermalLevelCooldownBase;
					}

					if (thermalPaddingBoots != null)
					{
						thermalLevelTickCooldown += thermalLevelCooldownBase;
					}
				}

				if ((this.ticksExisted - 1) % thermalLevelTickCooldown == 0)
				{
					int last = playerStats.thermalLevel;
					playerStats.thermalLevel = (int) Math.min(Math.max(playerStats.thermalLevel + thermalLevelMod, -22), 22);

					if (playerStats.thermalLevel != last)
					{
						this.sendThermalLevelPacket();
					}

					if (Math.abs(playerStats.thermalLevel) >= 22)
					{
						this.damageEntity(DamageSourceGC.thermal, 1.5F); // TODO New thermal damage source
					}
				}

				if (playerStats.thermalLevel < -15)
				{
					this.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 5, 2, true));
				}

				if (playerStats.thermalLevel > 15)
				{
					this.addPotionEffect(new PotionEffect(Potion.confusion.id, 5, 2, true));

				}
			}
		}
	}

	protected void checkOxygen()
	{
		final ItemStack tankInSlot = this.getPlayerStats().extendedInventory.getStackInSlot(2);
		final ItemStack tankInSlot2 = this.getPlayerStats().extendedInventory.getStackInSlot(3);

		final int drainSpacing = OxygenUtil.getDrainSpacing(tankInSlot, tankInSlot2);

		if (this.worldObj.provider instanceof IGalacticraftWorldProvider && !((IGalacticraftWorldProvider) this.worldObj.provider).hasBreathableAtmosphere() && !this.capabilities.isCreativeMode && !(this.ridingEntity instanceof EntityLanderBase) && !(this.ridingEntity instanceof EntityAutoRocket))
		{
			GCPlayerStats playerStats = this.getPlayerStats();

			if (tankInSlot == null)
			{
				playerStats.airRemaining = 0;
			}

			if (tankInSlot2 == null)
			{
				playerStats.airRemaining2 = 0;
			}

			if (drainSpacing > 0)
			{
				if ((this.ticksExisted - 1) % drainSpacing == 0 && !OxygenUtil.isAABBInBreathableAirBlock(this) && !playerStats.usingPlanetSelectionGui)
				{
					if (tankInSlot != null && tankInSlot.getMaxDamage() - tankInSlot.getItemDamage() > 0)
					{
						tankInSlot.damageItem(1, this);
					}

					if (tankInSlot2 != null && tankInSlot2.getMaxDamage() - tankInSlot2.getItemDamage() > 0)
					{
						tankInSlot2.damageItem(1, this);
					}
				}

				if (tankInSlot != null)
				{
					playerStats.airRemaining = tankInSlot.getMaxDamage() - tankInSlot.getItemDamage();
				}

				if (tankInSlot2 != null)
				{
					playerStats.airRemaining2 = tankInSlot2.getMaxDamage() - tankInSlot2.getItemDamage();
				}
			}
			else
			{
				if ((this.ticksExisted - 1) % 60 == 0)
				{
					if (OxygenUtil.isAABBInBreathableAirBlock(this))
					{
						if (playerStats.airRemaining < 90 && tankInSlot != null)
						{
							playerStats.airRemaining = Math.min(playerStats.airRemaining + 1, tankInSlot.getMaxDamage() - tankInSlot.getItemDamage());
						}

						if (playerStats.airRemaining2 < 90 && tankInSlot2 != null)
						{
							playerStats.airRemaining2 = Math.min(playerStats.airRemaining2 + 1, tankInSlot2.getMaxDamage() - tankInSlot2.getItemDamage());
						}
					}
					else
					{
						if (playerStats.airRemaining > 0)
						{
							playerStats.airRemaining = Math.max(playerStats.airRemaining - 1, 0);
						}

						if (playerStats.airRemaining2 > 0)
						{
							playerStats.airRemaining2 = Math.max(playerStats.airRemaining2 - 1, 0);
						}
					}
				}
			}

			final boolean airEmpty = playerStats.airRemaining <= 0 && playerStats.airRemaining2 <= 0;

			if (this.isOnLadder())
			{
				playerStats.oxygenSetupValid = playerStats.lastOxygenSetupValid;
			}
			else
                playerStats.oxygenSetupValid = !((!OxygenUtil.hasValidOxygenSetup(this) || airEmpty) && !OxygenUtil.isAABBInBreathableAirBlock(this));

			if (!playerStats.oxygenSetupValid && !this.worldObj.isRemote && this.isEntityAlive())
			{
				if (playerStats.damageCounter == 0)
				{
					playerStats.damageCounter = ConfigManagerCore.suffocationCooldown;

					GCCoreOxygenSuffocationEvent suffocationEvent = new GCCoreOxygenSuffocationEvent.Pre(this);
					MinecraftForge.EVENT_BUS.post(suffocationEvent);

					if (!suffocationEvent.isCanceled())
					{
						this.attackEntityFrom(DamageSourceGC.oxygenSuffocation, ConfigManagerCore.suffocationDamage);

						GCCoreOxygenSuffocationEvent suffocationEventPost = new GCCoreOxygenSuffocationEvent.Post(this);
						MinecraftForge.EVENT_BUS.post(suffocationEventPost);
					}
				}
			}
		}
		else if ((this.ticksExisted - 1) % 20 == 0 && !this.capabilities.isCreativeMode && this.getPlayerStats().airRemaining < 90)
		{
			this.getPlayerStats().airRemaining += 1;
			this.getPlayerStats().airRemaining2 += 1;
		}
		else if (this.capabilities.isCreativeMode)
		{
			this.getPlayerStats().airRemaining = 90;
			this.getPlayerStats().airRemaining2 = 90;
		}
		else
		{
			this.getPlayerStats().oxygenSetupValid = true;
		}
	}

	protected void throwMeteors()
	{
		if (this.worldObj.provider instanceof IGalacticraftWorldProvider && FMLCommonHandler.instance().getEffectiveSide() != Side.CLIENT)
		{
			if (((IGalacticraftWorldProvider) this.worldObj.provider).getMeteorFrequency() > 0)
			{
				final int f = (int) (((IGalacticraftWorldProvider) this.worldObj.provider).getMeteorFrequency() * 1000D);

				if (this.worldObj.rand.nextInt(f) == 0)
				{
					final EntityPlayer closestPlayer = this.worldObj.getClosestPlayerToEntity(this, 100);

					if (closestPlayer == null || closestPlayer.getEntityId() <= this.getEntityId())
					{
						int x, y, z;
						double motX, motZ;
						x = this.worldObj.rand.nextInt(20) - 10;
						y = this.worldObj.rand.nextInt(20) + 200;
						z = this.worldObj.rand.nextInt(20) - 10;
						motX = this.worldObj.rand.nextDouble() * 5;
						motZ = this.worldObj.rand.nextDouble() * 5;

						final EntityMeteor meteor = new EntityMeteor(this.worldObj, this.posX + x, this.posY + y, this.posZ + z, motX - 2.5D, 0, motZ - 2.5D, 1);

						if (!this.worldObj.isRemote)
						{
							this.worldObj.spawnEntityInWorld(meteor);
						}
					}
				}

				if (this.worldObj.rand.nextInt(f * 3) == 0)
				{
					final EntityPlayer closestPlayer = this.worldObj.getClosestPlayerToEntity(this, 100);

					if (closestPlayer == null || closestPlayer.getEntityId() <= this.getEntityId())
					{
						int x, y, z;
						double motX, motZ;
						x = this.worldObj.rand.nextInt(20) - 10;
						y = this.worldObj.rand.nextInt(20) + 200;
						z = this.worldObj.rand.nextInt(20) - 10;
						motX = this.worldObj.rand.nextDouble() * 5;
						motZ = this.worldObj.rand.nextDouble() * 5;

						final EntityMeteor meteor = new EntityMeteor(this.worldObj, this.posX + x, this.posY + y, this.posZ + z, motX - 2.5D, 0, motZ - 2.5D, 6);

						if (!this.worldObj.isRemote)
						{
							this.worldObj.spawnEntityInWorld(meteor);
						}
					}
				}
			}
		}
	}

	protected void updateSchematics()
	{
		SchematicRegistry.addUnlockedPage(this, SchematicRegistry.getMatchingRecipeForID(0));
		SchematicRegistry.addUnlockedPage(this, SchematicRegistry.getMatchingRecipeForID(Integer.MAX_VALUE));

		Collections.sort(this.getPlayerStats().unlockedSchematics);

		if (this.playerNetServerHandler != null && (this.getPlayerStats().unlockedSchematics.size() != this.getPlayerStats().lastUnlockedSchematics.size() || (this.ticksExisted - 1) % 100 == 0))
		{
			Integer[] iArray = new Integer[this.getPlayerStats().unlockedSchematics.size()];

			for (int i = 0; i < iArray.length; i++)
			{
				ISchematicPage page = this.getPlayerStats().unlockedSchematics.get(i);
				iArray[i] = page == null ? -2 : page.getPageID();
			}

			List<Object> objList = new ArrayList<Object>();
			objList.add(iArray);

			GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SCHEMATIC_LIST, objList), this);
		}
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
