package codechicken.lib.packet;

import codechicken.lib.data.MCDataHandler;
import codechicken.lib.data.MCDataIO;
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
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
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
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public final class PacketCustom extends PacketBuffer implements MCDataHandler {

    public interface ICustomPacketHandler {

    }

    public interface IClientPacketHandler extends ICustomPacketHandler {

        void handlePacket(PacketCustom packetCustom, Minecraft mc, INetHandlerPlayClient handler);
    }

    public interface IServerPacketHandler extends ICustomPacketHandler {

        void handlePacket(PacketCustom packetCustom, EntityPlayerMP sender, INetHandlerPlayServer handler);
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

    private interface CustomHandler {

        void handle(INetHandler handler, String channel, PacketCustom packet);
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
                MinecraftServer mc = FMLCommonHandler.instance().getMinecraftServerInstance();
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

    public interface IHandshakeHandler {

        void handshakeReceived(NetHandlerPlayServer netHandler);
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
                    handler.handshakeReceived((NetHandlerPlayServer) netHandler);
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
            int len = readVarInt();
            byte[] out = new byte[len];
            inflater.setInput(array(), readerIndex(), readableBytes());
            inflater.inflate(out);
            clear();
            writeArray(out);

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
            writeArray(out);
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

    @Override
    public PacketCustom writeBoolean(boolean b) {
        super.writeBoolean(b);
        return this;
    }

    @Override
    public PacketCustom writeByte(int b) {
        super.writeByte(b);
        return this;
    }

    @Override
    public PacketCustom writeShort(int s) {
        super.writeShort(s);
        return this;
    }

    @Override
    public PacketCustom writeInt(int i) {
        super.writeInt(i);
        return this;
    }

    @Override
    public PacketCustom writeFloat(float f) {
        super.writeFloat(f);
        return this;
    }

    @Override
    public PacketCustom writeDouble(double d) {
        super.writeDouble(d);
        return this;
    }

    @Override
    public PacketCustom writeLong(long l) {
        super.writeLong(l);
        return this;
    }

    @Override
    public PacketCustom writeChar(char c) {
        super.writeChar(c);
        return this;
    }

    @Override

    public PacketCustom writeVarInt(int i) {
        writeVarIntToBuffer(i);
        return this;
    }

    @Override
    public PacketCustom writeVarShort(int s) {
        MCDataIO.writeVarShort(this, s);
        return this;
    }

    @Override
    public PacketCustom writeArray(byte[] barray) {
        writeBytes(barray);
        return this;
    }

    @Override
    public PacketCustom writeString(String s) {
        super.writeString(s);
        return this;
    }

    public PacketCustom writeLocation(ResourceLocation loc) {
        writeString(loc.toString());
        return this;
    }

    @Override
    public PacketCustom writePos(BlockPos pos) {
        writeInt(pos.getX());
        writeInt(pos.getY());
        writeInt(pos.getZ());
        return this;
    }

    @Override
    public PacketCustom writeBlockPos(BlockPos pos) {
        return writePos(pos);
    }

    @Override
    public PacketCustom writeItemStack(ItemStack stack) {
        MCDataIO.writeItemStack(this, stack);
        return this;
    }

    @Override
    public PacketCustom writeNBTTagCompound(NBTTagCompound tag) {
        writeNBTTagCompoundToBuffer(tag);
        return this;
    }

    @Override
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

    @Override
    public BlockPos readPos() {
        return new BlockPos(readInt(), readInt(), readInt());
    }

    @Override
    public BlockPos readBlockPos() {
        return readPos();
    }

    @Override
    public byte[] readArray(int length) {
        return readBytes(length).array();
    }

    @Override
    public String readString() {
        return readStringFromBuffer(32767);
    }

    //TODO 1.11 pull to MC data in / out.
    public ResourceLocation readLocation() {
        return new ResourceLocation(readString());
    }

    @Override
    public ItemStack readItemStack() {
        return MCDataIO.readItemStack(this);
    }

    @Override
    public NBTTagCompound readNBTTagCompound() {
        try {
            return readNBTTagCompoundFromBuffer();
        } catch (IOException e) {
            throw new EncoderException(e);
        }
    }

    @Override
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

    public NBTTagCompound toNBTTag(NBTTagCompound tagCompound) {
        tagCompound.setByteArray("CCL:data", array());
        return tagCompound;
    }

    public NBTTagCompound toNBTTag() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setByteArray("CCL:data", array());
        return tagCompound;
    }

    public static PacketCustom fromNBTTag(NBTTagCompound tagCompound) {
        return new PacketCustom(Unpooled.copiedBuffer(tagCompound.getByteArray("CCL:data")));
    }

    public SPacketUpdateTileEntity toTilePacket(BlockPos pos) {
        return new SPacketUpdateTileEntity(pos, 0, toNBTTag());
    }

    @SideOnly (Side.CLIENT)
    public static PacketCustom fromTilePacket(SPacketUpdateTileEntity tilePacket) {
        return fromNBTTag(tilePacket.getNbtCompound());
    }

    public void sendToPlayer(EntityPlayer player) {
        sendToPlayer(toPacket(), player);
    }

    public static void sendToPlayer(Packet packet, EntityPlayer player) {
        if (player == null) {
            sendToClients(packet);
        } else {
            ((EntityPlayerMP) player).connection.sendPacket(packet);
        }
    }

    public void sendToClients() {
        sendToClients(toPacket());
    }

    public static void sendToClients(Packet packet) {
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendPacketToAllPlayers(packet);
    }

    public void sendPacketToAllAround(BlockPos pos, double range, int dim) {
        sendPacketToAllAround(pos.getX(), pos.getY(), pos.getZ(), range, dim);
    }

    public void sendPacketToAllAround(double x, double y, double z, double range, int dim) {
        sendToAllAround(toPacket(), x, y, z, range, dim);
    }

    public static void sendToAllAround(Packet packet, double x, double y, double z, double range, int dim) {
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendToAllNearExcept(null, x, y, z, range, dim, packet);
    }

    public void sendToDimension(int dim) {
        sendToDimension(toPacket(), dim);
    }

    public static void sendToDimension(Packet packet, int dim) {
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendPacketToAllPlayersInDimension(packet, dim);
    }

    public void sendToChunk(TileEntity tile) {
        sendToChunk(tile.getWorld(), tile.getPos().getX() >> 4, tile.getPos().getZ() >> 4);
    }

    public void sendToChunk(World world, int chunkX, int chunkZ) {
        sendToChunk(toPacket(), world, chunkX, chunkZ);
    }

    public static void sendToChunk(Packet packet, World world, int chunkX, int chunkZ) {
        PlayerChunkMapEntry playerInstance = ((WorldServer) world).getPlayerChunkMap().getEntry(chunkX, chunkZ);
        if (playerInstance != null) {
            playerInstance.sendPacket(packet);
        }
    }

    public void sendToOps() {
        sendToOps(toPacket());
    }

    public static void sendToOps(Packet packet) {
        for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerList()) {
            if (FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().canSendCommands(player.getGameProfile())) {
                sendToPlayer(packet, player);
            }
        }
    }

    @SideOnly (Side.CLIENT)
    public void sendToServer() {
        sendToServer(toPacket());
    }

    @SideOnly (Side.CLIENT)
    public static void sendToServer(Packet packet) {
        Minecraft.getMinecraft().getConnection().sendPacket(packet);
    }
}
