package micdoodle8.mods.galacticraft.core.network;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderOrbit;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.world.ChunkLoadingCallback;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetHandlerPlayServer;

public class ConnectionEvents
{
    private static boolean clientConnected = false;

    static
    {
        EnumConnectionState.field_150761_f.put(PacketSimple.class, EnumConnectionState.PLAY);
        EnumConnectionState.PLAY.field_150770_i.put(2515, PacketSimple.class);
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
            GCPlayerStats stats = GCPlayerStats.get((EntityPlayerMP) event.player);
            GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_CLIENT_ID, new Object[] { stats.spaceStationDimensionID }), (EntityPlayerMP) event.player);
        }

        if (event.player.worldObj.provider instanceof WorldProviderOrbit && event.player instanceof EntityPlayerMP)
        {
            ((WorldProviderOrbit) event.player.worldObj.provider).sendPacketsToClient((EntityPlayerMP) event.player);
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
       			ids+=idList[j].toString()+" ";
        	}
        	GCLog.info("Galacticraft server sending dimension IDs to connecting client: "+ ids);
        }
        event.manager.scheduleOutboundPacket(new PacketSimple(EnumSimplePacket.C_UPDATE_PLANETS_LIST, WorldUtil.getPlanetList()));
        event.manager.scheduleOutboundPacket(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_LIST, WorldUtil.getSpaceStationList()));
        event.manager.scheduleOutboundPacket(new PacketSimple(EnumSimplePacket.C_UPDATE_CONFIGS, ConfigManagerCore.getServerConfigOverride()));
    }

    @SubscribeEvent
    public void onConnectionOpened(ClientConnectedToServerEvent event)
    {
        if (!event.isLocal)
        {
            ConnectionEvents.clientConnected = true;
        }
    }

    @SubscribeEvent
    public void onConnectionClosed(ClientDisconnectionFromServerEvent event)
    {
        if (ConnectionEvents.clientConnected)
        {
            ConnectionEvents.clientConnected = false;
            WorldUtil.unregisterPlanets();
            WorldUtil.unregisterSpaceStations();
            ConfigManagerCore.restoreClientConfigOverrideable();
        }
    }
}
