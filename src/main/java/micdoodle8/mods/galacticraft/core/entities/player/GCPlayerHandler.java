package micdoodle8.mods.galacticraft.core.entities.player;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRace;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.core.entities.EntityParachest;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
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
		}

		player.checkGear();

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
