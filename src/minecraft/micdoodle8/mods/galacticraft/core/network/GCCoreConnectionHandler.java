package micdoodle8.mods.galacticraft.core.network;

import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import universalelectricity.prefab.network.ConnectionHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;

public class GCCoreConnectionHandler extends ConnectionHandler implements IConnectionHandler
{
    private static boolean connected = false;
    
	@Override
	public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager) 
	{
		super.playerLoggedIn(player, netHandler, manager);
	}

	@Override
	public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager)
	{
		super.connectionReceived(netHandler, manager);
		manager.addToSendQueue(GCCorePacketDimensionList.buildDimensionListPacket(WorldUtil.registeredSpaceStations)); // TODO
		return null;
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) 
	{
		super.connectionOpened(netClientHandler, server, port, manager);
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) 
	{
		super.connectionOpened(netClientHandler, server, manager);
	}

	@Override
	public void connectionClosed(INetworkManager manager) 
	{
		super.connectionClosed(manager);

        if (connected)
        {
            WorldUtil.unregisterPlanets();
            WorldUtil.unregisterSpaceStations();
        }

        connected = false;
	}

	@Override
	public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) 
	{
        connected = true;
		super.clientLoggedIn(clientHandler, manager, login);
	}
}
