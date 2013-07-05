package micdoodle8.mods.galacticraft.core.network;

import java.util.ArrayList;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP;
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

public class GCCoreConnectionHandler implements IConnectionHandler
{
    private static ArrayList<INetworkManager> managers = new ArrayList<INetworkManager>();

    @Override
    public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager)
    {
        if (player instanceof GCCorePlayerMP)
        {
            final GCCorePlayerMP playerMP = (GCCorePlayerMP) player;
            PacketDispatcher.sendPacketToPlayer(GCCorePacketSchematicList.buildSchematicListPacket(playerMP.unlockedSchematics), player);
            PacketDispatcher.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 18, new Object[] { ((GCCorePlayerMP) player).spaceStationDimensionID }), player);
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
    }

    @Override
    public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager)
    {
    }

    @Override
    public void connectionClosed(INetworkManager manager)
    {
        if (GCCoreConnectionHandler.managers.contains(manager))
        {
            GCCoreConnectionHandler.managers.remove(manager);

            if (GCCoreConnectionHandler.managers.size() == 0)
            {
                WorldUtil.unregisterPlanets();
                WorldUtil.unregisterSpaceStations();
            }
        }
    }

    @Override
    public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login)
    {
        if (!GCCoreConnectionHandler.managers.contains(manager))
        {
            GCCoreConnectionHandler.managers.add(manager);
        }
    }
}
