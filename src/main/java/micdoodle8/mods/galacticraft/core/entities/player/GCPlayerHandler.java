package micdoodle8.mods.galacticraft.core.entities.player;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRace;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderOrbit;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.core.entities.EntityParachest;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP.EnumModelPacket;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP.ThermalArmorEvent;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP.ThermalArmorEvent.ArmorAddResult;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import java.lang.ref.WeakReference;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GCPlayerHandler
{
	private ConcurrentHashMap<UUID, GCPlayerStats> playerStatsMap = new ConcurrentHashMap<UUID, GCPlayerStats>();

	public ConcurrentHashMap<UUID, GCPlayerStats> getServerStatList()
	{
		return this.playerStatsMap;
	}

	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event)
	{
		if (event.player instanceof GCEntityPlayerMP)
		{
			this.onPlayerLogin((GCEntityPlayerMP) event.player);
		}
	}

	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event)
	{
		if (event.player instanceof GCEntityPlayerMP)
		{
			this.onPlayerLogout((GCEntityPlayerMP) event.player);
		}
	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		if (event.player instanceof GCEntityPlayerMP)
		{
			this.onPlayerRespawn((GCEntityPlayerMP) event.player);
		}
	}

	@SubscribeEvent
	public void onEntityConstructing(EntityEvent.EntityConstructing event)
	{
		if (event.entity instanceof GCEntityPlayerMP && GCPlayerStats.get((GCEntityPlayerMP) event.entity) == null)
		{
			GCPlayerStats.register((GCEntityPlayerMP) event.entity);
		}
	}

	private void onPlayerLogin(GCEntityPlayerMP player)
	{
		GCPlayerStats oldData = this.playerStatsMap.remove(player.getPersistentID());
		if (oldData != null)
		{
			oldData.saveNBTData(player.getEntityData());
		}

		GCPlayerStats stats = GCPlayerStats.get(player);

        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_GET_CELESTIAL_BODY_LIST, new Object[] {}), player);
	}

	private void onPlayerLogout(GCEntityPlayerMP player)
	{

	}

	private void onPlayerRespawn(GCEntityPlayerMP player)
	{
		GCPlayerStats oldData = this.playerStatsMap.remove(player.getPersistentID());
		GCPlayerStats stats = GCPlayerStats.get(player);

		if (oldData != null)
		{
			stats.copyFrom(oldData, false);
		}

		stats.player = new WeakReference<GCEntityPlayerMP>(player);
	}

	protected void checkGear(GCEntityPlayerMP player, GCPlayerStats GCPlayer)
	{
		GCPlayer.maskInSlot = GCPlayer.extendedInventory.getStackInSlot(0);
		GCPlayer.gearInSlot = GCPlayer.extendedInventory.getStackInSlot(1);
		GCPlayer.tankInSlot1 = GCPlayer.extendedInventory.getStackInSlot(2);
		GCPlayer.tankInSlot2 = GCPlayer.extendedInventory.getStackInSlot(3);
		GCPlayer.parachuteInSlot = GCPlayer.extendedInventory.getStackInSlot(4);
		GCPlayer.frequencyModuleInSlot = GCPlayer.extendedInventory.getStackInSlot(5);
		GCPlayer.thermalHelmetInSlot = GCPlayer.extendedInventory.getStackInSlot(6);
		GCPlayer.thermalChestplateInSlot = GCPlayer.extendedInventory.getStackInSlot(7);
		GCPlayer.thermalLeggingsInSlot = GCPlayer.extendedInventory.getStackInSlot(8);
		GCPlayer.thermalBootsInSlot = GCPlayer.extendedInventory.getStackInSlot(9);

		//

		if (GCPlayer.frequencyModuleInSlot != GCPlayer.lastFrequencyModuleInSlot)
		{
			if (GCPlayer.frequencyModuleInSlot == null)
			{
				player.sendGearUpdatePacket(EnumModelPacket.REMOVE_FREQUENCY_MODULE);
			}
			else if (GCPlayer.frequencyModuleInSlot.getItem() == GCItems.basicItem && GCPlayer.frequencyModuleInSlot.getItemDamage() == 19 && GCPlayer.lastFrequencyModuleInSlot == null)
			{
				player.sendGearUpdatePacket(EnumModelPacket.ADD_FREQUENCY_MODULE);
			}

			GCPlayer.lastFrequencyModuleInSlot = GCPlayer.frequencyModuleInSlot;
		}

		//

		if (GCPlayer.maskInSlot != GCPlayer.lastMaskInSlot)
		{
			if (GCPlayer.maskInSlot == null)
			{
				player.sendGearUpdatePacket(EnumModelPacket.REMOVEMASK);
			}
			else if (GCPlayer.maskInSlot.getItem() == GCItems.oxMask && GCPlayer.lastMaskInSlot == null)
			{
				player.sendGearUpdatePacket(EnumModelPacket.ADDMASK);
			}

			GCPlayer.lastMaskInSlot = GCPlayer.maskInSlot;
		}

		//

		if (GCPlayer.gearInSlot != GCPlayer.lastGearInSlot)
		{
			if (GCPlayer.gearInSlot == null)
			{
				player.sendGearUpdatePacket(EnumModelPacket.REMOVEGEAR);
			}
			else if (GCPlayer.gearInSlot.getItem() == GCItems.oxygenGear && GCPlayer.lastGearInSlot == null)
			{
				player.sendGearUpdatePacket(EnumModelPacket.ADDGEAR);
			}

			GCPlayer.lastGearInSlot = GCPlayer.gearInSlot;
		}

		//

		if (GCPlayer.tankInSlot1 != GCPlayer.lastTankInSlot1)
		{
			if (GCPlayer.tankInSlot1 == null)
			{
				player.sendGearUpdatePacket(EnumModelPacket.REMOVE_LEFT_TANK);
			}
			else if (GCPlayer.lastTankInSlot1 == null)
			{
				if (GCPlayer.tankInSlot1.getItem() == GCItems.oxTankLight)
				{
					player.sendGearUpdatePacket(EnumModelPacket.ADDLEFTGREENTANK);
				}
				else if (GCPlayer.tankInSlot1.getItem() == GCItems.oxTankMedium)
				{
					player.sendGearUpdatePacket(EnumModelPacket.ADDLEFTORANGETANK);
				}
				else if (GCPlayer.tankInSlot1.getItem() == GCItems.oxTankHeavy)
				{
					player.sendGearUpdatePacket(EnumModelPacket.ADDLEFTREDTANK);
				}
			}
			//if the else is reached then both tankInSlot and lastTankInSlot are non-null
			else if (GCPlayer.tankInSlot1.getItem() != GCPlayer.lastTankInSlot1.getItem())
			{
				if (GCPlayer.tankInSlot1.getItem() == GCItems.oxTankLight)
				{
					player.sendGearUpdatePacket(EnumModelPacket.ADDLEFTGREENTANK);
				}
				else if (GCPlayer.tankInSlot1.getItem() == GCItems.oxTankMedium)
				{
					player.sendGearUpdatePacket(EnumModelPacket.ADDLEFTORANGETANK);
				}
				else if (GCPlayer.tankInSlot1.getItem() == GCItems.oxTankHeavy)
				{
					player.sendGearUpdatePacket(EnumModelPacket.ADDLEFTREDTANK);
				}
			}

			GCPlayer.lastTankInSlot1 = GCPlayer.tankInSlot1;
		}

		//

		if (GCPlayer.tankInSlot2 != GCPlayer.lastTankInSlot2)
		{
			if (GCPlayer.tankInSlot2 == null)
			{
				player.sendGearUpdatePacket(EnumModelPacket.REMOVE_RIGHT_TANK);
			}
			else if (GCPlayer.lastTankInSlot2 == null)
			{
				if (GCPlayer.tankInSlot2.getItem() == GCItems.oxTankLight)
				{
					player.sendGearUpdatePacket(EnumModelPacket.ADDRIGHTGREENTANK);
				}
				else if (GCPlayer.tankInSlot2.getItem() == GCItems.oxTankMedium)
				{
					player.sendGearUpdatePacket(EnumModelPacket.ADDRIGHTORANGETANK);
				}
				else if (GCPlayer.tankInSlot2.getItem() == GCItems.oxTankHeavy)
				{
					player.sendGearUpdatePacket(EnumModelPacket.ADDRIGHTREDTANK);
				}
			}
			//if the else is reached then both tankInSlot and lastTankInSlot are non-null
			else if (GCPlayer.tankInSlot2.getItem() != GCPlayer.lastTankInSlot2.getItem())
			{
				if (GCPlayer.tankInSlot2.getItem() == GCItems.oxTankLight)
				{
					player.sendGearUpdatePacket(EnumModelPacket.ADDRIGHTGREENTANK);
				}
				else if (GCPlayer.tankInSlot2.getItem() == GCItems.oxTankMedium)
				{
					player.sendGearUpdatePacket(EnumModelPacket.ADDRIGHTORANGETANK);
				}
				else if (GCPlayer.tankInSlot2.getItem() == GCItems.oxTankHeavy)
				{
					player.sendGearUpdatePacket(EnumModelPacket.ADDRIGHTREDTANK);
				}
			}

			GCPlayer.lastTankInSlot2 = GCPlayer.tankInSlot2;
		}
		
		//

		if (GCPlayer.parachuteInSlot != GCPlayer.lastParachuteInSlot)
		{
			if (GCPlayer.parachuteInSlot == null)
			{
				if (GCPlayer.usingParachute)
				{
					player.sendGearUpdatePacket(EnumModelPacket.REMOVE_PARACHUTE);
				}
			}
			else if (GCPlayer.lastParachuteInSlot == null)
			{
				if (GCPlayer.usingParachute)
				{
					player.sendGearUpdatePacket(EnumModelPacket.ADD_PARACHUTE);
				}
			}
			else if (GCPlayer.parachuteInSlot.getItemDamage() != GCPlayer.lastParachuteInSlot.getItemDamage())
			{
				player.sendGearUpdatePacket(EnumModelPacket.ADD_PARACHUTE);
			}

			GCPlayer.lastParachuteInSlot = GCPlayer.parachuteInSlot;
		}
		
		//

		if (GCPlayer.thermalHelmetInSlot != GCPlayer.lastThermalHelmetInSlot)
		{
			ThermalArmorEvent armorEvent = new ThermalArmorEvent(0, GCPlayer.thermalHelmetInSlot);
			MinecraftForge.EVENT_BUS.post(armorEvent);

			if (armorEvent.armorResult != ArmorAddResult.NOTHING)
			{
				if (GCPlayer.thermalHelmetInSlot == null || armorEvent.armorResult == ArmorAddResult.REMOVE)
				{
					player.sendGearUpdatePacket(EnumModelPacket.REMOVE_THERMAL_HELMET);
				}
				else if (armorEvent.armorResult == ArmorAddResult.ADD && GCPlayer.lastThermalHelmetInSlot == null)
				{
					player.sendGearUpdatePacket(EnumModelPacket.ADD_THERMAL_HELMET);
				}
			}

			GCPlayer.lastThermalHelmetInSlot = GCPlayer.thermalHelmetInSlot;
		}

		if (GCPlayer.thermalChestplateInSlot != GCPlayer.lastThermalChestplateInSlot)
		{
			ThermalArmorEvent armorEvent = new ThermalArmorEvent(1, GCPlayer.thermalChestplateInSlot);
			MinecraftForge.EVENT_BUS.post(armorEvent);

			if (armorEvent.armorResult != ArmorAddResult.NOTHING)
			{
				if (GCPlayer.thermalChestplateInSlot == null || armorEvent.armorResult == ArmorAddResult.REMOVE)
				{
					player.sendGearUpdatePacket(EnumModelPacket.REMOVE_THERMAL_CHESTPLATE);
				}
				else if (armorEvent.armorResult == ArmorAddResult.ADD && GCPlayer.lastThermalChestplateInSlot == null)
				{
					player.sendGearUpdatePacket(EnumModelPacket.ADD_THERMAL_CHESTPLATE);
				}
			}

			GCPlayer.lastThermalChestplateInSlot = GCPlayer.thermalChestplateInSlot;
		}

		if (GCPlayer.thermalLeggingsInSlot != GCPlayer.lastThermalLeggingsInSlot)
		{
			ThermalArmorEvent armorEvent = new ThermalArmorEvent(2, GCPlayer.thermalLeggingsInSlot);
			MinecraftForge.EVENT_BUS.post(armorEvent);

			if (armorEvent.armorResult != ArmorAddResult.NOTHING)
			{
				if (GCPlayer.thermalLeggingsInSlot == null || armorEvent.armorResult == ArmorAddResult.REMOVE)
				{
					player.sendGearUpdatePacket(EnumModelPacket.REMOVE_THERMAL_LEGGINGS);
				}
				else if (armorEvent.armorResult == ArmorAddResult.ADD && GCPlayer.lastThermalLeggingsInSlot == null)
				{
					player.sendGearUpdatePacket(EnumModelPacket.ADD_THERMAL_LEGGINGS);
				}
			}

			GCPlayer.lastThermalLeggingsInSlot = GCPlayer.thermalLeggingsInSlot;
		}

		if (GCPlayer.thermalBootsInSlot != GCPlayer.lastThermalBootsInSlot)
		{
			ThermalArmorEvent armorEvent = new ThermalArmorEvent(3, GCPlayer.thermalBootsInSlot);
			MinecraftForge.EVENT_BUS.post(armorEvent);

			if (armorEvent.armorResult != ArmorAddResult.NOTHING)
			{
				if (GCPlayer.thermalBootsInSlot == null || armorEvent.armorResult == ArmorAddResult.REMOVE)
				{
					player.sendGearUpdatePacket(EnumModelPacket.REMOVE_THERMAL_BOOTS);
				}
				else if (armorEvent.armorResult == ArmorAddResult.ADD && GCPlayer.lastThermalBootsInSlot == null)
				{
					player.sendGearUpdatePacket(EnumModelPacket.ADD_THERMAL_BOOTS);
				}
			}

			GCPlayer.lastThermalBootsInSlot = GCPlayer.thermalBootsInSlot;
		}
	}
	
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event)
	{
		if (event.entityLiving instanceof GCEntityPlayerMP)
		{
			this.onPlayerUpdate((GCEntityPlayerMP) event.entityLiving);
		}
	}

	private void onPlayerUpdate(GCEntityPlayerMP player)
	{
		int tick = player.ticksExisted - 1;

		if (tick == 25)
		{
            if (!player.openedSpaceRaceManager)
            {
                SpaceRace race = SpaceRaceManager.getSpaceRaceFromPlayer(player.getGameProfile().getName());

                if (race == null || race.getTeamName().equals(SpaceRace.DEFAULT_NAME))
                {
                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_OPEN_SPACE_RACE_GUI, new Object[] {}), player);
                }

                player.openedSpaceRaceManager = true;
            }
		}

		//This will speed things up a little
		final GCPlayerStats GCPlayer = player.getPlayerStats();
		final boolean isInGCDimension = player.worldObj.provider instanceof IGalacticraftWorldProvider;

		if (GCPlayer.cryogenicChamberCooldown > 0)
		{
			GCPlayer.cryogenicChamberCooldown--;
		}

		if (!player.onGround && GCPlayer.lastOnGround)
		{
			GCPlayer.touchedGround = true;
		}

		if (GCPlayer.teleportCooldown > 0)
		{
			GCPlayer.teleportCooldown--;
		}

		if (GCPlayer.chatCooldown > 0)
		{
			GCPlayer.chatCooldown--;
		}

		if (GCPlayer.openPlanetSelectionGuiCooldown > 0)
		{
			GCPlayer.openPlanetSelectionGuiCooldown--;

			if (GCPlayer.openPlanetSelectionGuiCooldown == 1 && !GCPlayer.hasOpenedPlanetSelectionGui)
			{
				player.sendPlanetList();
				GCPlayer.usingPlanetSelectionGui = true;
				GCPlayer.hasOpenedPlanetSelectionGui = true;
			}
		}

		if (GCPlayer.usingParachute)
		{
			if (GCPlayer.lastParachuteInSlot != null)
			{
				player.fallDistance = 0.0F;
			}
			if (player.onGround)
			{
				player.setUsingParachute(false);
			}
		}

		player.checkCurrentItem();

		if (GCPlayer.usingPlanetSelectionGui)
		{
			player.sendPlanetList();
		}

		/*		if (isInGCDimension || player.usingPlanetSelectionGui)
				{
					player.playerNetServerHandler.ticksForFloatKick = 0;
				}	
		*/
		if (GCPlayer.damageCounter > 0)
		{
			GCPlayer.damageCounter--;
		}

		if (isInGCDimension)
		{
			if (tick % 30 == 0)
			{
				player.sendAirRemainingPacket();
				player.sendThermalLevelPacket();		
			}
			
			if (player.ridingEntity instanceof EntityLanderBase)
			{
				GCPlayer.inLander = true;
				GCPlayer.justLanded = false;
			}
			else
			{
				if (GCPlayer.inLander) GCPlayer.justLanded = true;
				GCPlayer.inLander = false;
			}
			
			if (player.onGround && GCPlayer.justLanded)
			{
				GCPlayer.justLanded = false;
				
				//Set spawn point here if just descended from a lander for the first time
				if (player.getBedLocation(player.worldObj.provider.dimensionId) == null)
				{
					int i = 30000000;
					int j = Math.min(i, Math.max(-i, MathHelper.floor_double(player.posX + 0.5D)));
					int k = Math.min(256, Math.max(0, MathHelper.floor_double(player.posY + 1.5D)));
					int l = Math.min(i, Math.max(-i, MathHelper.floor_double(player.posZ + 0.5D)));
					ChunkCoordinates coords = new ChunkCoordinates(j, k, l);
					player.setSpawnChunk(coords, true, player.worldObj.provider.dimensionId);
				}
				
				GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_RESET_THIRD_PERSON, new Object[] {}), player);
			}
			
			if (player.worldObj.provider instanceof WorldProviderOrbit)
				player.fallDistance = 0.0F;
		}

		this.checkGear(player, GCPlayer);

		if (GCPlayer.chestSpawnCooldown > 0)
		{
			GCPlayer.chestSpawnCooldown--;

			if (GCPlayer.chestSpawnCooldown == 180)
			{
				if (GCPlayer.chestSpawnVector != null)
				{
					EntityParachest chest = new EntityParachest(player.worldObj, GCPlayer.rocketStacks, GCPlayer.fuelLevel);

					chest.setPosition(GCPlayer.chestSpawnVector.x, GCPlayer.chestSpawnVector.y, GCPlayer.chestSpawnVector.z);

					if (!player.worldObj.isRemote)
					{
						player.worldObj.spawnEntityInWorld(chest);
					}
				}
			}
		}

		//

		if (GCPlayer.launchAttempts > 0 && player.ridingEntity == null)
		{
			GCPlayer.launchAttempts = 0;
		}

		player.checkThermalStatus();
		player.checkOxygen();

		if (isInGCDimension && (GCPlayer.oxygenSetupValid != GCPlayer.lastOxygenSetupValid || tick % 100 == 0))
		{
			GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_OXYGEN_VALIDITY, new Object[] { GCPlayer.oxygenSetupValid }), player);
		}

		player.throwMeteors();

		player.updateSchematics();

		if (tick % 250 == 0 && GCPlayer.frequencyModuleInSlot == null && !GCPlayer.receivedSoundWarning && isInGCDimension && player.onGround && tick > 0)
		{
			player.addChatMessage(new ChatComponentText(EnumColor.YELLOW + GCCoreUtil.translate("gui.frequencymodule.warning0") + " " + EnumColor.AQUA + GCItems.basicItem.getItemStackDisplayName(new ItemStack(GCItems.basicItem, 1, 19)) + EnumColor.YELLOW + " " + GCCoreUtil.translate("gui.frequencymodule.warning1")));
			GCPlayer.receivedSoundWarning = true;
		}

		GCPlayer.lastOxygenSetupValid = GCPlayer.oxygenSetupValid;
		GCPlayer.lastUnlockedSchematics = GCPlayer.unlockedSchematics;
		GCPlayer.lastOnGround = player.onGround;
	}
}
