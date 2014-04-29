package micdoodle8.mods.galacticraft.core.network;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.world.ChunkLoadingCallback;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetHandlerPlayServer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;

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
		
		if (event.player instanceof GCEntityPlayerMP)
		{
			GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_CLIENT_ID, new Object[] { ((GCEntityPlayerMP) event.player).getSpaceStationDimensionID() }), (EntityPlayerMP) event.player);
		}
	}
	
	@SubscribeEvent
	public void onConnectionReceived(ServerConnectionFromClientEvent event)
	{
		((NetHandlerPlayServer) event.handler).sendPacket(new PacketSimple(EnumSimplePacket.C_UPDATE_PLANETS_LIST, WorldUtil.getPlanetList()));
		((NetHandlerPlayServer) event.handler).sendPacket(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_LIST, WorldUtil.getSpaceStationList()));
	}
	
	@SubscribeEvent
	public void onConnectionOpened(ClientConnectedToServerEvent event)
	{
		if (!event.isLocal)
		{
			clientConnected = true;
		}
	}
	
	@SubscribeEvent
	public void onConnectionClosed(ClientDisconnectionFromServerEvent event)
	{
		if (clientConnected)
		{
			clientConnected = false;
			WorldUtil.unregisterPlanets();
			WorldUtil.unregisterSpaceStations();
		}
	}
}
