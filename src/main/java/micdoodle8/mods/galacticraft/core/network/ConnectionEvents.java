package micdoodle8.mods.galacticraft.core.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRace;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.dimension.SpaceStationWorldData;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderSpaceStation;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerClient;
import micdoodle8.mods.galacticraft.core.util.*;
import micdoodle8.mods.galacticraft.core.world.ChunkLoadingCallback;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;

import org.apache.logging.log4j.LogManager;

public class ConnectionEvents
{
    private static boolean clientConnected = false;

    static
    {
        EnumConnectionState.STATES_BY_CLASS.put(PacketSimple.class, EnumConnectionState.PLAY);
        registerPacket(EnumPacketDirection.CLIENTBOUND, PacketSimple.class);
    }

    protected static EnumConnectionState registerPacket(EnumPacketDirection direction, Class<? extends Packet<? extends INetHandler>> packetClass)
    {
        BiMap<Integer, Class<? extends Packet<?>>> bimap = (BiMap<Integer, Class<? extends Packet<?>>>) EnumConnectionState.PLAY.directionMaps.get(direction);

        if (bimap == null)
        {
            bimap = HashBiMap.create();
            EnumConnectionState.PLAY.directionMaps.put(direction, bimap);
        }

        if (bimap.containsValue(packetClass))
        {
            String s = direction + " packet " + packetClass + " is already known to ID " + bimap.inverse().get(packetClass);
            LogManager.getLogger().fatal(s);
            throw new IllegalArgumentException(s);
        }
        else
        {
            bimap.put(Integer.valueOf(bimap.size()), packetClass);
            return EnumConnectionState.PLAY;
        }
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerLoggedOutEvent event)
    {
        ChunkLoadingCallback.onPlayerLogout(event.player);
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event)
    {
        ChunkLoadingCallback.onPlayerLogin(event.player);

        if (event.player instanceof EntityPlayerMP)
        {
            EntityPlayerMP thePlayer = (EntityPlayerMP) event.player;
            GCPlayerStats stats = GCPlayerStats.get(thePlayer);
            SpaceStationWorldData.checkAllStations(thePlayer, stats);
            GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_CLIENT_ID, GCCoreUtil.getDimensionID(thePlayer.world), new Object[] { WorldUtil.spaceStationDataToString(stats.getSpaceStationDimensionData()) }), thePlayer);
            SpaceRace raceForPlayer = SpaceRaceManager.getSpaceRaceFromPlayer(PlayerUtil.getName(thePlayer));
            if (raceForPlayer != null)
            {
                SpaceRaceManager.sendSpaceRaceData(thePlayer.mcServer, thePlayer, raceForPlayer);
            }
        }

        if (event.player.world.provider instanceof WorldProviderSpaceStation && event.player instanceof EntityPlayerMP)
        {
            ((WorldProviderSpaceStation) event.player.world.provider).getSpinManager().sendPackets((EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public void onConnectionReceived(ServerConnectionFromClientEvent event)
    {
        if (ConfigManagerCore.enableDebug)
        {
            Integer[] idList = (Integer[]) WorldUtil.getPlanetList().get(0);
            String ids = "";
            for (int j = 0; j < idList.length; j++)
            {
                ids += idList[j].toString() + " ";
            }
            GCLog.info("Galacticraft server sending dimension IDs to connecting client: " + ids);
        }
        event.getManager().sendPacket(ConnectionPacket.createDimPacket(WorldUtil.getPlanetListInts()));
        event.getManager().sendPacket(ConnectionPacket.createSSPacket(WorldUtil.getSpaceStationListInts()));
        event.getManager().sendPacket(ConnectionPacket.createConfigPacket(ConfigManagerCore.getServerConfigOverride()));
    }

    @SubscribeEvent
    public void onConnectionOpened(ClientConnectedToServerEvent event)
    {
        if (!event.isLocal())
        {
            ConnectionEvents.clientConnected = true;
        }
    }

    @SubscribeEvent
    public void onConnectionClosed(ClientDisconnectionFromServerEvent event)
    {
        TickHandlerClient.menuReset = true;
        if (ConnectionEvents.clientConnected)
        {
            ConnectionEvents.clientConnected = false;
            WorldUtil.unregisterPlanets();
            WorldUtil.unregisterSpaceStations();
            ConfigManagerCore.restoreClientConfigOverrideable();
        }
    }
}
