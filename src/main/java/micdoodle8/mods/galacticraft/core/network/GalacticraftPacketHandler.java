//package micdoodle8.mods.galacticraft.core.network;
//
//import com.google.common.collect.ImmutableMap;
//import com.google.common.collect.Maps;
//import com.google.common.collect.Queues;
//import io.netty.channel.ChannelHandler.Sharable;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.tick.TickHandlerClient;
//import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.network.INetHandler;
//import net.minecraft.network.NetworkManager;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.world.World;
//import net.minecraft.world.dimension.DimensionType;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.network.NetworkRegistry;
//import net.minecraftforge.fml.network.simple.SimpleChannel;
//
//import java.util.Map;
//import java.util.Queue;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Sharable
//public class GalacticraftPacketHandler extends SimpleChannelInboundHandler<IPacket>
//{
//    private final Map<LogicalSide, Map<Integer, Queue<PacketPlayerPair>>> packetMap;
//    private static volatile int livePacketCount = 0;
//
//    public GalacticraftPacketHandler()
//    {
//        Map<LogicalSide, Map<Integer, Queue<PacketPlayerPair>>> map = Maps.newHashMap();
//        for (LogicalSide LogicalSide : LogicalSide.values())
//        {
//            Map<Integer, Queue<PacketPlayerPair>> sideMap = new ConcurrentHashMap<Integer, Queue<PacketPlayerPair>>();
//            map.put(LogicalSide, sideMap);
//        }
//
//        packetMap = ImmutableMap.copyOf(map);
//        if (GCCoreUtil.getEffectiveSide() == LogicalSide.CLIENT)
//        {
//            TickHandlerClient.addPacketHandler(this);
//        }
//        TickHandlerServer.addPacketHandler(this);
//    }
//
//    public void unload(World world)
//    {
//        LogicalSide side = world.isRemote ? LogicalSide.CLIENT : LogicalSide.SERVER;
//        DimensionType dimID = GCCoreUtil.getDimensionID(world);
//        Queue<PacketPlayerPair> queue = getQueue(side, dimId);
//        queue.clear();
//    }
//
//    public void tick(World world)
//    {
//        PacketPlayerPair pair;
//        LogicalSide side = world.isRemote ? LogicalSide.CLIENT : LogicalSide.SERVER;
//        DimensionType dimID = GCCoreUtil.getDimensionType(world);
//        Queue<PacketPlayerPair> queue = getQueue(LogicalSide, dimID);
//        while ((pair = queue.poll()) != null)
//        {
//            switch (side)
//            {
//            case CLIENT:
//                pair.getPacket().handleClientSide(pair.getPlayer());
//                break;
//            case SERVER:
//                pair.getPacket().handleServerSide(pair.getPlayer());
//                break;
//            }
//        }
//    }
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, IPacket msg) throws Exception
//    {
//        PlayerEntity player = GalacticraftCore.proxy.getPlayerFromNetHandler(netHandler);
//
//        if (player == null)
//        {
//            return;
//        }
//
//        if (LogicalSide != null)
//        {
//            getQueue(LogicalSide, msg.getDimensionID()).add(new PacketPlayerPair(msg, player));
//            livePacketCount++;
//        }
//    }
//
//    private Queue<PacketPlayerPair> getQueue(LogicalSide LogicalSide, DimensionType dimID)
//    {
//        Map<Integer, Queue<PacketPlayerPair>> map = packetMap.get(LogicalSide);
//        if (!map.containsKey(dimID))
//        {
//            map.put(dimID, Queues.<PacketPlayerPair>newConcurrentLinkedQueue());
//        }
//        return map.get(dimID);
//    }
//
//    private final class PacketPlayerPair
//    {
//        private IPacket packet;
//        private PlayerEntity player;
//
//        public PacketPlayerPair(IPacket packet, PlayerEntity player)
//        {
//            this.packet = packet;
//            this.player = player;
//        }
//
//        public IPacket getPacket()
//        {
//            return packet;
//        }
//
//        public void setPacket(IPacket packet)
//        {
//            this.packet = packet;
//        }
//
//        public PlayerEntity getPlayer()
//        {
//            return player;
//        }
//
//        public void setPlayer(PlayerEntity player)
//        {
//            this.player = player;
//        }
//    }
//}
