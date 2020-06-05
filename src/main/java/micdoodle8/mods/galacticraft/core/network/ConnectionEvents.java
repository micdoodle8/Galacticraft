//package micdoodle8.mods.galacticraft.core.network;
//
//import com.google.common.collect.BiMap;
//import com.google.common.collect.HashBiMap;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.dimension.SpaceRace;
//import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
//import micdoodle8.mods.galacticraft.core.dimension.SpaceStationWorldData;
//import micdoodle8.mods.galacticraft.core.dimension.DimensionSpaceStation;
//import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
//import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
//import micdoodle8.mods.galacticraft.core.tick.TickHandlerClient;
//import micdoodle8.mods.galacticraft.core.util.*;
//import micdoodle8.mods.galacticraft.core.world.ChunkLoadingCallback;
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.network.*;
//
//import net.minecraft.network.IPacket;
//import net.minecraftforge.event.entity.player.PlayerEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import org.apache.logging.log4j.LogManager;
//
//public class ConnectionEvents
//{
//    private static boolean clientConnected = false;
//
//    static
//    {
//        ProtocolType.STATES_BY_CLASS.put(PacketSimple.class, ProtocolType.PLAY);
//        registerPacket(PacketDirection.CLIENTBOUND, PacketSimple.class);
//    }
//
//    protected static ProtocolType registerPacket(PacketDirection direction, Class<? extends IPacket<? extends INetHandler>> packetClass)
//    {
//        BiMap<Integer, Class<? extends IPacket<?>>> bimap = (BiMap<Integer, Class<? extends IPacket<?>>>) ProtocolType.PLAY.directionMaps.get(direction);
//
//        if (bimap == null)
//        {
//            bimap = HashBiMap.create();
//            ProtocolType.PLAY.directionMaps.put(direction, bimap);
//        }
//
//        if (bimap.containsValue(packetClass))
//        {
//            String s = direction + " packet " + packetClass + " is already known to ID " + bimap.inverse().get(packetClass);
//            LogManager.getLogger().fatal(s);
//            throw new IllegalArgumentException(s);
//        }
//        else
//        {
//            bimap.put(Integer.valueOf(bimap.size()), packetClass);
//            return ProtocolType.PLAY;
//        }
//    }
//
//    @SubscribeEvent
//    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event)
//    {
//        ChunkLoadingCallback.onPlayerLogout(event.getPlayer());
//    }
//
//    @SubscribeEvent
//    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
//    {
//        ChunkLoadingCallback.onPlayerLogin(event.getPlayer());
//
//        if (event.getPlayer() instanceof ServerPlayerEntity)
//        {
//            ServerPlayerEntity thePlayer = (ServerPlayerEntity) event.getPlayer();
//            GCPlayerStats stats = GCPlayerStats.get(thePlayer);
////            SpaceStationWorldData.checkAllStations(thePlayer, stats); TODO
//            GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_CLIENT_ID, GCCoreUtil.getDimensionID(thePlayer.world), new Object[] { WorldUtil.spaceStationDataToString(stats.getSpaceStationDimensionData()) }), thePlayer);
//            SpaceRace raceForPlayer = SpaceRaceManager.getSpaceRaceFromPlayer(PlayerUtil.getName(thePlayer));
//            if (raceForPlayer != null)
//            {
//                SpaceRaceManager.sendSpaceRaceData(thePlayer.server, thePlayer, raceForPlayer);
//            }
//        }
//
//        if (event.getPlayer().world.getDimension() instanceof DimensionSpaceStation && event.getPlayer() instanceof ServerPlayerEntity)
//        {
//            ((DimensionSpaceStation) event.getPlayer().world.getDimension()).getSpinManager().sendPackets((ServerPlayerEntity) event.getPlayer());
//        }
//    }
//
//    @SubscribeEvent
//    public void onConnectionReceived(PlayerEvent.PlayerLoggedInEvent event)
//    {
//        if (ConfigManagerCore.enableDebug)
//        {
//            Integer[] idList = (Integer[]) WorldUtil.getPlanetList().get(0);
//            String ids = "";
//            for (int j = 0; j < idList.length; j++)
//            {
//                ids += idList[j].toString() + " ";
//            }
//            GCLog.info("Galacticraft server sending dimension IDs to connecting client: " + ids);
//        }
//        NetworkManager networkManager = ((ServerPlayerEntity) event.getPlayer()).connection.netManager;
////        networkManager.sendPacket(ConnectionPacket.createDimPacket(WorldUtil.getPlanetListInts()));
////        networkManager.sendPacket(ConnectionPacket.createSSPacket(WorldUtil.getSpaceStationListInts()));
////        networkManager.sendPacket(ConnectionPacket.createConfigPacket(ConfigManagerCore.getServerConfigOverride()));
//    }
//
//    @SubscribeEvent
//    public void onConnectionOpened(ClientConnectedToServerEvent event)
//    {
//        if (!event.isLocal())
//        {
//            ConnectionEvents.clientConnected = true;
//        }
//    }
//
//    @SubscribeEvent
//    public void onConnectionClosed(ClientDisconnectionFromServerEvent event)
//    {
//        TickHandlerClient.menuReset = true;
//        if (ConnectionEvents.clientConnected)
//        {
//            ConnectionEvents.clientConnected = false;
//            WorldUtil.unregisterPlanets();
//            WorldUtil.unregisterSpaceStations();
//            ConfigManagerCore.restoreClientConfigOverrideable();
//        }
//    }
//}
