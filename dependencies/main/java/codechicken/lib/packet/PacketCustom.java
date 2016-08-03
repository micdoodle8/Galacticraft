package codechicken.lib.packet;

import codechicken.lib.data.MCDataIO;
import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.vec.BlockCoord;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.EncoderException;
import io.netty.util.AttributeKey;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerManager.PlayerInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkHandshakeEstablished;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public final class PacketCustom extends PacketBuffer implements MCDataInput, MCDataOutput {
    public static interface ICustomPacketHandler {
    }

    public interface IClientPacketHandler extends ICustomPacketHandler {
        public void handlePacket(PacketCustom packetCustom, Minecraft mc, INetHandlerPlayClient handler);
    }

    public interface IServerPacketHandler extends ICustomPacketHandler {
        public void handlePacket(PacketCustom packetCustom, EntityPlayerMP sender, INetHandlerPlayServer handler);
    }

    public static AttributeKey<CustomInboundHandler> cclHandler = new AttributeKey<CustomInboundHandler>("ccl:handler");

    @ChannelHandler.Sharable
    public static class CustomInboundHandler extends SimpleChannelInboundHandler<FMLProxyPacket> {
        public EnumMap<Side, CustomHandler> handlers = Maps.newEnumMap(Side.class);

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            super.handlerAdded(ctx);
            ctx.channel().attr(cclHandler).set(this);
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, FMLProxyPacket msg) throws Exception {
            handlers.get(ctx.channel().attr(NetworkRegistry.CHANNEL_SOURCE).get()).handle(ctx.channel().attr(NetworkRegistry.NET_HANDLER).get(), ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get(), new PacketCustom(msg.payload()));
        }
    }

    private static interface CustomHandler {
        public void handle(INetHandler handler, String channel, PacketCustom packet);
    }

    public static class ClientInboundHandler implements CustomHandler {
        private IClientPacketHandler handler;

        public ClientInboundHandler(ICustomPacketHandler handler) {
            this.handler = (IClientPacketHandler) handler;
        }

        @Override
        public void handle(final INetHandler netHandler, final String channel, final PacketCustom packet) {
            if (netHandler instanceof INetHandlerPlayClient) {
                Minecraft mc = Minecraft.getMinecraft();
                if (!mc.isCallingFromMinecraftThread()) {
                    mc.addScheduledTask(new Runnable() {
                        public void run() {
                            handle(netHandler, channel, packet);
                        }
                    });
                } else {
                    handler.handlePacket(packet, mc, (INetHandlerPlayClient) netHandler);
                }
            } else {
                System.err.println("Invalid INetHandler for PacketCustom on channel: " + channel);
            }
        }
    }

    public static class ServerInboundHandler implements CustomHandler {
        private IServerPacketHandler handler;

        public ServerInboundHandler(ICustomPacketHandler handler) {
            this.handler = (IServerPacketHandler) handler;
        }

        @Override
        public void handle(final INetHandler netHandler, final String channel, final PacketCustom packet) {
            if (netHandler instanceof NetHandlerPlayServer) {
                MinecraftServer mc = MinecraftServer.getServer();
                if (!mc.isCallingFromMinecraftThread()) {
                    mc.addScheduledTask(new Runnable() {
                        public void run() {
                            handle(netHandler, channel, packet);
                        }
                    });
                } else {
                    handler.handlePacket(packet, ((NetHandlerPlayServer) netHandler).playerEntity, (INetHandlerPlayServer) netHandler);
                }
            } else {
                System.err.println("Invalid INetHandler for PacketCustom on channel: " + channel);
            }
        }
    }

    public static interface IHandshakeHandler {
        public void handshakeRecieved(NetHandlerPlayServer netHandler);
    }

    public static class HandshakeInboundHandler extends ChannelInboundHandlerAdapter {
        public IHandshakeHandler handler;

        public HandshakeInboundHandler(IHandshakeHandler handler) {
            this.handler = handler;
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof NetworkHandshakeEstablished) {
                INetHandler netHandler = ((NetworkDispatcher) ctx.channel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).get()).getNetHandler();
                if (netHandler instanceof NetHandlerPlayServer) {
                    handler.handshakeRecieved((NetHandlerPlayServer) netHandler);
                }
            } else {
                ctx.fireUserEventTriggered(evt);
            }
        }
    }

    public static String channelName(Object channelKey) {
        if (channelKey instanceof String) {
            return (String) channelKey;
        }
        if (channelKey instanceof ModContainer) {
            String s = ((ModContainer) channelKey).getModId();
            if (s.length() > 20) {
                throw new IllegalArgumentException("Mod ID (" + s + ") too long for use as channel (20 chars). Use a string identifier");
            }
            return s;
        }

        ModContainer mc = FMLCommonHandler.instance().findContainerFor(channelKey);
        if (mc != null) {
            return mc.getModId();
        }

        throw new IllegalArgumentException("Invalid channel: " + channelKey);
    }

    public static FMLEmbeddedChannel getOrCreateChannel(String channelName, Side side) {
        if (!NetworkRegistry.INSTANCE.hasChannel(channelName, side)) {
            NetworkRegistry.INSTANCE.newChannel(channelName, new CustomInboundHandler());
        }
        return NetworkRegistry.INSTANCE.getChannel(channelName, side);
    }

    public static void assignHandler(Object channelKey, ICustomPacketHandler handler) {
        String channelName = channelName(channelKey);
        Side side = handler instanceof IServerPacketHandler ? Side.SERVER : Side.CLIENT;
        FMLEmbeddedChannel channel = getOrCreateChannel(channelName, side);
        channel.attr(cclHandler).get().handlers.put(side, side == Side.SERVER ? new ServerInboundHandler(handler) : new ClientInboundHandler(handler));
    }

    public static void assignHandshakeHandler(Object channelKey, IHandshakeHandler handler) {
        FMLEmbeddedChannel channel = getOrCreateChannel(channelName(channelKey), Side.SERVER);
        channel.pipeline().addLast(new HandshakeInboundHandler(handler));
    }

    private String channel;
    private int type;

    public PacketCustom(ByteBuf payload) {
        super(payload);

        type = readUnsignedByte();
        if (type > 0x80) {
            decompress();
        }
        type &= 0x7F;
    }

    public PacketCustom(Object channelKey, int type) {
        super(Unpooled.buffer());
        if (type <= 0 || type >= 0x80) {
            throw new IllegalArgumentException("Packet type: " + type + " is not within required 0 < t < 0x80");
        }

        this.channel = channelName(channelKey);
        this.type = type;
        writeByte(type);
    }

    /**
     * Decompresses the remaining ByteBuf (after type has been read) using Snappy
     */
    private void decompress() {
        Inflater inflater = new Inflater();
        try {
            int len = readInt();
            byte[] out = new byte[len];
            inflater.setInput(array(), readerIndex(), readableBytes());
            inflater.inflate(out);
            clear();
            writeByteArray(out);
        } catch (Exception e) {
            throw new EncoderException(e);
        } finally {
            inflater.end();
        }
    }

    /**
     * Compresses the payload ByteBuf after the type byte
     */
    private void do_compress() {
        Deflater deflater = new Deflater();
        try {
            readerIndex(1);
            int len = readableBytes();
            deflater.setInput(array(), readerIndex(), len);
            deflater.finish();
            byte[] out = new byte[len];
            int clen = deflater.deflate(out);
            if (clen >= len - 5 || !deflater.finished())//not worth compressing, gets larger
            {
                return;
            }
            clear();
            writeByte(type | 0x80);
            writeVarInt(len);
            writeByteArray(out);
        } catch (Exception e) {
            throw new EncoderException(e);
        } finally {
            readerIndex(0);
            deflater.end();
        }
    }

    public boolean incoming() {
        return channel == null;
    }

    public int getType() {
        return type & 0x7F;
    }

    public PacketCustom compress() {
        if (incoming()) {
            throw new IllegalStateException("Tried to compress an incoming packet");
        }
        if ((type & 0x80) != 0) {
            throw new IllegalStateException("Packet already compressed");
        }
        type |= 0x80;
        return this;
    }

    public PacketCustom writeBoolean(boolean b) {
        super.writeBoolean(b);
        return this;
    }

    public PacketCustom writeByte(int b) {
        super.writeByte(b);
        return this;
    }

    public PacketCustom writeShort(int s) {
        super.writeShort(s);
        return this;
    }

    public PacketCustom writeInt(int i) {
        super.writeInt(i);
        return this;
    }

    public PacketCustom writeFloat(float f) {
        super.writeFloat(f);
        return this;
    }

    public PacketCustom writeDouble(double d) {
        super.writeDouble(d);
        return this;
    }

    public PacketCustom writeLong(long l) {
        super.writeLong(l);
        return this;
    }

    @Override
    public PacketCustom writeChar(char c) {
        super.writeChar(c);
        return this;
    }

    public PacketCustom writeVarInt(int i) {
        writeVarIntToBuffer(i);
        return this;
    }

    public PacketCustom writeVarShort(int s) {
        MCDataIO.writeVarShort(this, s);
        return this;
    }

    public PacketCustom writeArray(byte[] barray) {
        writeBytes(barray);
        return this;
    }

    public PacketCustom writeString(String s) {
        super.writeString(s);
        return this;
    }

    public PacketCustom writeCoord(int x, int y, int z) {
        writeInt(x);
        writeInt(y);
        writeInt(z);
        return this;
    }

    public PacketCustom writeCoord(BlockCoord coord) {
        writeInt(coord.x);
        writeInt(coord.y);
        writeInt(coord.z);
        return this;
    }

    public PacketCustom writeItemStack(ItemStack stack) {
        MCDataIO.writeItemStack(this, stack);
        return this;
    }

    public PacketCustom writeNBTTagCompound(NBTTagCompound tag) {
        writeNBTTagCompoundToBuffer(tag);
        return this;
    }

    public PacketCustom writeFluidStack(FluidStack fluid) {
        MCDataIO.writeFluidStack(this, fluid);
        return this;
    }

    public short readUByte() {
        return readUnsignedByte();
    }

    public int readUShort() {
        return readUnsignedShort();
    }

    @Override
    public int readVarShort() {
        return MCDataIO.readVarShort(this);
    }

    @Override
    public int readVarInt() {
        return readVarIntFromBuffer();
    }

    public BlockCoord readCoord() {
        return new BlockCoord(readInt(), readInt(), readInt());
    }

    public byte[] readArray(int length) {
        return readBytes(length).array();
    }

    public String readString() {
        return readStringFromBuffer(32767);
    }

    public ItemStack readItemStack() {
        return MCDataIO.readItemStack(this);
    }

    public NBTTagCompound readNBTTagCompound() {
        try {
            return readNBTTagCompoundFromBuffer();
        } catch (IOException e) {
            throw new EncoderException(e);
        }
    }

    public FluidStack readFluidStack() {
        return MCDataIO.readFluidStack(this);
    }

    public FMLProxyPacket toPacket() {
        if (incoming()) {
            throw new IllegalStateException("Tried to write an incoming packet");
        }

        if (readableBytes() > 32000 || (type & 0x80) != 0) {
            do_compress();
        }

        return new FMLProxyPacket(new PacketBuffer(copy()), channel);
    }

    public void sendToPlayer(EntityPlayer player) {
        sendToPlayer(toPacket(), player);
    }

    public static void sendToPlayer(Packet packet, EntityPlayer player) {
        if (player == null) {
            sendToClients(packet);
        } else {
            ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(packet);
        }
    }

    public void sendToClients() {
        sendToClients(toPacket());
    }

    public static void sendToClients(Packet packet) {
        MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(packet);
    }

    public void sendPacketToAllAround(double x, double y, double z, double range, int dim) {
        sendToAllAround(toPacket(), x, y, z, range, dim);
    }

    public static void sendToAllAround(Packet packet, double x, double y, double z, double range, int dim) {
        MinecraftServer.getServer().getConfigurationManager().sendToAllNear(x, y, z, range, dim, packet);
    }

    public void sendToDimension(int dim) {
        sendToDimension(toPacket(), dim);
    }

    public static void sendToDimension(Packet packet, int dim) {
        MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayersInDimension(packet, dim);
    }

    public void sendToChunk(World world, int chunkX, int chunkZ) {
        sendToChunk(toPacket(), world, chunkX, chunkZ);
    }

    public static void sendToChunk(Packet packet, World world, int chunkX, int chunkZ) {
        PlayerInstance p = ((WorldServer) world).getPlayerManager().getPlayerInstance(chunkX, chunkZ, false);
        if (p != null) {
            p.sendToAllPlayersWatchingChunk(packet);
        }
    }

    public void sendToOps() {
        sendToOps(toPacket());
    }

    public static void sendToOps(Packet packet) {
        for (EntityPlayerMP player : (List<EntityPlayerMP>) MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            if (MinecraftServer.getServer().getConfigurationManager().canSendCommands(player.getGameProfile())) {
                sendToPlayer(packet, player);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void sendToServer() {
        sendToServer(toPacket());
    }

    @SideOnly(Side.CLIENT)
    public static void sendToServer(Packet packet) {
        Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
    }
}
