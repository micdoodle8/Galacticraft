package micdoodle8.mods.galacticraft.core.network;

import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;

public class GCCoreConnectionHandler implements IConnectionHandler
{
    private static boolean connected = false;

	@Override
	public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager)
	{
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
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager)
	{
	}

	@Override
	public void connectionClosed(INetworkManager manager)
	{
        if (GCCoreConnectionHandler.connected)
        {
            WorldUtil.unregisterPlanets();
            WorldUtil.unregisterSpaceStations();
        }

        GCCoreConnectionHandler.connected = false;
	}

	@Override
	public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login)
	{
        GCCoreConnectionHandler.connected = true;
	}
}
