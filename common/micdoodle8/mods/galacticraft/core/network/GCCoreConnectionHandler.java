package micdoodle8.mods.galacticraft.core.network;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerClient.EnumPacketClient;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

/**
 * GCCoreConnectionHandler.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreConnectionHandler implements IConnectionHandler
{
	private static boolean clientConnected = false;

	@Override
	public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager)
	{
		if (player instanceof GCCorePlayerMP)
		{
			final GCCorePlayerMP playerMP = (GCCorePlayerMP) player;
			PacketDispatcher.sendPacketToPlayer(GCCorePacketSchematicList.buildSchematicListPacket(playerMP.getUnlockedSchematics()), player);
			PacketDispatcher.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketClient.UPDATE_SPACESTATION_CLIENT_ID, new Object[] { ((GCCorePlayerMP) player).getSpaceStationDimensionID() }), player);
		}
	}

	@Override
	public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager)
	{
		manager.addToSendQueue(GCCorePacketDimensionListSpaceStations.buildDimensionListPacket(WorldUtil.registeredSpaceStations));
		manager.addToSendQueue(GCCorePacketDimensionListPlanets.buildDimensionListPacket(WorldUtil.registeredPlanets));
		return null;
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager)
	{
		GCCoreConnectionHandler.clientConnected = true;
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager)
	{
	}

	@Override
	public void connectionClosed(INetworkManager manager)
	{
		if (GCCoreConnectionHandler.clientConnected)
		{
			GCCoreConnectionHandler.clientConnected = false;
			WorldUtil.unregisterPlanets();
			WorldUtil.unregisterSpaceStations();
		}
	}

	@Override
	public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login)
	{
	}
}
