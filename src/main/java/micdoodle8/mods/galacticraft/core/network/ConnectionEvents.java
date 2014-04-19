package micdoodle8.mods.galacticraft.core.network;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.world.ChunkLoadingCallback;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;

public class ConnectionEvents 
{
	private static boolean clientConnected = false;
	
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
			FMLLog.info("SENT TO PLAYER ON LOGIN");
			GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_PLANETS_LIST, WorldUtil.getPlanetList()), (EntityPlayerMP) event.player);
			GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_LIST, WorldUtil.getSpaceStationList()), (EntityPlayerMP) event.player);
			GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_CLIENT_ID, new Object[] { ((GCEntityPlayerMP) event.player).getSpaceStationDimensionID() }), (EntityPlayerMP) event.player);
		}
	}
	
	@SubscribeEvent
	public void onConnectionReceived(ServerConnectionFromClientEvent event)
	{
		// TODO See if this is still needed.
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
