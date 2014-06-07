package micdoodle8.mods.galacticraft.core.entities.player;

import java.lang.ref.WeakReference;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.entities.EntityParachest;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP.EnumModelPacket;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

public class GCPlayerHandler
{
	private ConcurrentHashMap<UUID, GCPlayerStats> playerStatsMap = new ConcurrentHashMap<UUID, GCPlayerStats>();
	
	public ConcurrentHashMap<UUID, GCPlayerStats> getServerStatList()
	{
		return playerStatsMap;
	}
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event)
	{
    	if (event.player instanceof GCEntityPlayerMP)
    	{
    		onPlayerLogin((GCEntityPlayerMP) event.player);
    	}
	}
	
	@SubscribeEvent
	public void onPlayerLogout(PlayerLoggedOutEvent event)
	{
    	if (event.player instanceof GCEntityPlayerMP)
    	{
    		onPlayerLogout((GCEntityPlayerMP) event.player);
    	}
	}

    @SubscribeEvent
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
    	if (event.player instanceof GCEntityPlayerMP)
    	{
            onPlayerRespawn((GCEntityPlayerMP) event.player);
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
		GCPlayerStats oldData = playerStatsMap.remove(player.getPersistentID());
		if (oldData != null)
		{
			oldData.saveNBTData(player.getEntityData());
		}
		
		GCPlayerStats stats = GCPlayerStats.get(player);
	}
	
	private void onPlayerLogout(GCEntityPlayerMP player)
	{
		
	}
	
	private void onPlayerRespawn(GCEntityPlayerMP player)
	{
		GCPlayerStats oldData = playerStatsMap.remove(player.getPersistentID());
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

		if (tick == 10)
		{
			if (SpaceRaceManager.getSpaceRaceFromPlayer(player.getGameProfile().getName()) == null)
			{
				GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_OPEN_SPACE_RACE_GUI, new Object[] { }), player);
			}
		}

		if (player.getPlayerStats().cryogenicChamberCooldown > 0)
		{
			player.getPlayerStats().cryogenicChamberCooldown--;
		}

		if (!player.onGround && player.getPlayerStats().lastOnGround)
		{
			player.getPlayerStats().touchedGround = true;
		}

		if (player.getPlayerStats().teleportCooldown > 0)
		{
			player.getPlayerStats().teleportCooldown--;
		}

		if (player.getPlayerStats().chatCooldown > 0)
		{
			player.getPlayerStats().chatCooldown--;
		}

		if (player.getPlayerStats().openPlanetSelectionGuiCooldown > 0)
		{
			player.getPlayerStats().openPlanetSelectionGuiCooldown--;

			if (player.getPlayerStats().openPlanetSelectionGuiCooldown == 1 && !player.getPlayerStats().hasOpenedPlanetSelectionGui)
			{
				player.sendPlanetList();
				player.getPlayerStats().usingPlanetSelectionGui = true;
				player.getPlayerStats().hasOpenedPlanetSelectionGui = true;
			}
		}

		if (player.getPlayerStats().usingParachute)
		{
			player.fallDistance = 0.0F;
			if (player.onGround)
			{
				player.sendGearUpdatePacket(EnumModelPacket.REMOVE_PARACHUTE.getIndex());
				player.setUsingParachute(false);
			}
		}

		player.checkCurrentItem();

		if (player.getPlayerStats().usingPlanetSelectionGui)
		{
			player.sendPlanetList();
		}

/*		if (player.worldObj.provider instanceof IGalacticraftWorldProvider || player.usingPlanetSelectionGui)
		{
			player.playerNetServerHandler.ticksForFloatKick = 0;
		}	
*/		
		if (player.getPlayerStats().damageCounter > 0)
		{
			player.getPlayerStats().damageCounter--;
		}

		if (tick % 30 == 0 && player.worldObj.provider instanceof IGalacticraftWorldProvider)
		{
			player.sendAirRemainingPacket();
		}

		player.checkGear();

		if (player.getPlayerStats().chestSpawnCooldown > 0)
		{
			player.getPlayerStats().chestSpawnCooldown--;

			if (player.getPlayerStats().chestSpawnCooldown == 180)
			{
				if (player.getPlayerStats().chestSpawnVector != null)
				{
					EntityParachest chest = new EntityParachest(player.worldObj, player.getPlayerStats().rocketStacks, player.getPlayerStats().fuelLevel);

					chest.setPosition(player.getPlayerStats().chestSpawnVector.x, player.getPlayerStats().chestSpawnVector.y, player.getPlayerStats().chestSpawnVector.z);

					if (!player.worldObj.isRemote)
					{
						player.worldObj.spawnEntityInWorld(chest);
					}
				}
			}
		}

		//

		if (player.getPlayerStats().launchAttempts > 0 && player.ridingEntity == null)
		{
			player.getPlayerStats().launchAttempts = 0;
		}

		player.checkOxygen();

		if (player.worldObj.provider instanceof IGalacticraftWorldProvider && (player.getPlayerStats().oxygenSetupValid != player.getPlayerStats().lastOxygenSetupValid || tick % 100 == 0))
		{
			GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_OXYGEN_VALIDITY, new Object[] { player.getPlayerStats().oxygenSetupValid }), player);
		}

		player.throwMeteors();

		player.updateSchematics();

		if (tick % 250 == 0 && player.getPlayerStats().frequencyModuleInSlot == null && !player.getPlayerStats().receivedSoundWarning && player.worldObj.provider instanceof IGalacticraftWorldProvider && player.onGround && tick > 0)
		{
			player.addChatMessage(new ChatComponentText(EnumColor.YELLOW + "I'll probably need a " + EnumColor.AQUA + GCItems.basicItem.getItemStackDisplayName(new ItemStack(GCItems.basicItem, 1, 19)) + EnumColor.YELLOW + " if I want to hear properly here."));
			player.getPlayerStats().receivedSoundWarning = true;
		}

		player.getPlayerStats().lastOxygenSetupValid = player.getPlayerStats().oxygenSetupValid;
		player.getPlayerStats().lastUnlockedSchematics = player.getPlayerStats().unlockedSchematics;

		player.getPlayerStats().lastOnGround = player.onGround;
	}
}
