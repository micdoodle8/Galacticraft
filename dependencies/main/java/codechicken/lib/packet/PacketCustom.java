package codechicken.lib.packet;


import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.vec.BlockCoord;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.network.*;
import cpw.mods.fml.common.network.handshake.NetworkDispatcher;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.FluidStack;

import java.util.EnumMap;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public final class PacketCustom implements MCDataInput, MCDataOutput
{
    public static interface ICustomPacketHandler
    {
    }

    public interface IClientPacketHandler extends ICustomPacketHandler
    {
        public void handlePacket(PacketCustom packetCustom, Minecraft mc, INetHandlerPlayClient handler);
    }

    public interface IServerPacketHandler extends ICustomPacketHandler
    {
        public void handlePacket(PacketCustom packetCustom, EntityPlayerMP sender, INetHandlerPlayServer handler);
    }

    public static AttributeKey<CustomInboundHandler> cclHandler = new AttributeKey<CustomInboundHandler>("ccl:handler");

    @ChannelHandler.Sharable
    public static class CustomInboundHandler extends SimpleChannelInboundHandler<FMLProxyPacket>
    {
        public EnumMap<Side, CustomHandler> handlers = Maps.newEnumMap(Side.class);

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            super.handlerAdded(ctx);
            ctx.channel().attr(cclHandler).set(this);
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, FMLProxyPacket msg) throws Exception {
            handlers.get(ctx.channel().attr(NetworkRegistry.CHANNEL_SOURCE).get())
                    .handle(ctx.channel().attr(NetworkRegistry.NET_HANDLER).get(),
                            ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get(),
                            new PacketCustom(msg.payload()));
        }
    }

    private static interface CustomHandler
    {
        public void handle(INetHandler handler, String channel, PacketCustom packet) throws Exception;
    }

    public static class ClientInboundHandler implements CustomHandler
    {
        private IClientPacketHandler handler;

        public ClientInboundHandler(ICustomPacketHandler handler) {
            this.handler = (IClientPacketHandler) handler;
        }

        @Override
        public void handle(INetHandler netHandler, String channel, PacketCustom packet) throws Exception {
            if (netHandler instanceof INetHandlerPlayClient)
                handler.handlePacket(packet, Minecraft.getMinecraft(), (INetHandlerPlayClient) netHandler);
            else
                System.err.println("Invalid INetHandler for PacketCustom on channel: " + channel);
        }
    }

    public static class ServerInboundHandler implements CustomHandler
    {
        private IServerPacketHandler handler;

        public ServerInboundHandler(ICustomPacketHandler handler) {
            this.handler = (IServerPacketHandler) handler;
        }

        @Override
        public void handle(INetHandler netHandler, String channel, PacketCustom packet) throws Exception {
            if (netHandler instanceof NetHandlerPlayServer)
                handler.handlePacket(packet, ((NetHandlerPlayServer) netHandler).playerEntity, (INetHandlerPlayServer) netHandler);
            else
                System.err.println("Invalid INetHandler for PacketCustom on channel: " + channel);
        }
    }

    public static interface IHandshakeHandler
    {
        public void handshakeRecieved(NetHandlerPlayServer netHandler);
    }

    public static class HandshakeInboundHandler extends ChannelInboundHandlerAdapter
    {
        public IHandshakeHandler handler;

        public HandshakeInboundHandler(IHandshakeHandler handler) {
            this.handler = handler;
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof NetworkHandshakeEstablished) {
                INetHandler netHandler = ((NetworkDispatcher) ctx.channel().attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).get()).getNetHandler();
                if (netHandler instanceof NetHandlerPlayServer)
                    handler.handshakeRecieved((NetHandlerPlayServer) netHandler);
            } else
                ctx.fireUserEventTriggered(evt);
        }
    }

    public static String channelName(Object channelKey) {
        if (channelKey instanceof String)
            return (String) channelKey;
        if (channelKey instanceof ModContainer) {
            String s = ((ModContainer) channelKey).getModId();
            if(s.length() > 20)
                throw new IllegalArgumentException("Mod ID ("+s+") too long for use as channel (20 chars). Use a string identifier");
            return s;
        }

        ModContainer mc = FMLCommonHandler.instance().findContainerFor(channelKey);
        if (mc != null)
            return mc.getModId();

        throw new IllegalArgumentException("Invalid channel: " + channelKey);
    }

    public static FMLEmbeddedChannel getOrCreateChannel(String channelName, Side side) {
        if (!NetworkRegistry.INSTANCE.hasChannel(channelName, side))
            NetworkRegistry.INSTANCE.newChannel(channelName, new CustomInboundHandler());
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

    private ByteBuf byteBuf;
    private String channel;
    private int type;

    public PacketCustom(ByteBuf payload) {
        byteBuf = payload;

        type = byteBuf.readUnsignedByte();
        if (type > 0x80)
            decompress();
        type &= 0x7F;
    }

    public PacketCustom(Object channelKey, int type) {
        if (type <= 0 || type >= 0x80)
            throw new IllegalArgumentException("Packet type: " + type + " is not within required 0 < t < 0x80");

        this.channel = channelName(channelKey);
        this.type = type;
        byteBuf = Unpooled.buffer();
        byteBuf.writeByte(type);
    }

    /**
     * Decompresses the remaining ByteBuf (after type has been read) using Snappy
     */
    private void decompress() {
        Inflater inflater = new Inflater();
        try {
            int len = byteBuf.readInt();
            ByteBuf out = Unpooled.buffer(len);
            inflater.setInput(byteBuf.array(), byteBuf.readerIndex(), byteBuf.readableBytes());
            inflater.inflate(out.array());
            out.writerIndex(len);
            byteBuf = out;
        } catch (Exception e) {
            throw new RuntimeException(e);
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
            byteBuf.readerIndex(1);
            int len = byteBuf.readableBytes();
            deflater.setInput(byteBuf.array(), byteBuf.readerIndex(), len);
            deflater.finish();
            ByteBuf out = Unpooled.buffer(len + 5);
            int clen = deflater.deflate(out.array(), 5, len);
            if (clen >= len - 5 || !deflater.finished())//not worth compressing, gets larger
                return;

            out.setByte(0, type | 0x80);
            out.setInt(1, len);
            out.writerIndex(clen + 5);
            byteBuf = out;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            byteBuf.readerIndex(0);
            deflater.end();
        }
    }

    public boolean incoming() {
        return channel == null;
    }

    public int getType() {
        return type & 0x7F;
    }

    public ByteBuf getByteBuf() {
        return byteBuf;
    }

    public PacketCustom compress() {
        if (incoming())
            throw new IllegalStateException("Tried to compress an incoming packet");
        if ((type & 0x80) != 0)
            throw new IllegalStateException("Packet already compressed");
        type |= 0x80;
        return this;
    }

    public PacketCustom writeBoolean(boolean b) {
        byteBuf.writeBoolean(b);
        return this;
    }

    public PacketCustom writeByte(int b) {
        byteBuf.writeByte(b);
        return this;
    }

    public PacketCustom writeShort(int s) {
        byteBuf.writeShort(s);
        return this;
    }

    public PacketCustom writeInt(int i) {
        byteBuf.writeInt(i);
        return this;
    }

    public PacketCustom writeFloat(float f) {
        byteBuf.writeFloat(f);
        return this;
    }

    public PacketCustom writeDouble(double d) {
        byteBuf.writeDouble(d);
        return this;
    }

    public PacketCustom writeLong(long l) {
        byteBuf.writeLong(l);
        return this;
    }

    @Override
    public PacketCustom writeChar(char c) {
        byteBuf.writeChar(c);
        return this;
    }

    public PacketCustom writeVarInt(int i) {
        ByteBufUtils.writeVarInt(byteBuf, i, 5);
        return this;
    }

    public PacketCustom writeVarShort(int s) {
        ByteBufUtils.writeVarShort(byteBuf, s);
        return this;
    }

    public PacketCustom writeByteArray(byte[] barray) {
        byteBuf.writeBytes(barray);
        return this;
    }

    public PacketCustom writeString(String s) {
        ByteBufUtils.writeUTF8String(byteBuf, s);
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
        writeItemStack(stack, false);
        return this;
    }

    public PacketCustom writeItemStack(ItemStack stack, boolean large) {
        if (stack == null) {
            writeShort(-1);
        } else {
            writeShort(Item.getIdFromItem(stack.getItem()));
            if (large)
                writeInt(stack.stackSize);
            else
                writeByte(stack.stackSize);
            writeShort(stack.getItemDamage());
            writeNBTTagCompound(stack.stackTagCompound);
        }
        return this;
    }

    public PacketCustom writeNBTTagCompound(NBTTagCompound compound) {
        ByteBufUtils.writeTag(byteBuf, compound);
        return this;
    }

    public PacketCustom writeFluidStack(FluidStack fluid) {
        if (fluid == null) {
            writeShort(-1);
        } else {
            writeShort(fluid.fluidID);
            writeVarInt(fluid.amount);
            writeNBTTagCompound(fluid.tag);
        }
        return this;
    }

    public boolean readBoolean() {
        return byteBuf.readBoolean();
    }

    public short readUByte() {
        return byteBuf.readUnsignedByte();
    }

    public int readUShort() {
        return byteBuf.readUnsignedShort();
    }

    public byte readByte() {
        return byteBuf.readByte();
    }

    public short readShort() {
        return byteBuf.readShort();
    }

    public int readInt() {
        return byteBuf.readInt();
    }

    public float readFloat() {
        return byteBuf.readFloat();
    }

    public double readDouble() {
        return byteBuf.readDouble();
    }

    public long readLong() {
        return byteBuf.readLong();
    }

    public char readChar() {
        return byteBuf.readChar();
    }

    @Override
    public int readVarShort() {
        return ByteBufUtils.readVarShort(byteBuf);
    }

    @Override
    public int readVarInt() {
        return ByteBufUtils.readVarInt(byteBuf, 5);
    }

    public BlockCoord readCoord() {
        return new BlockCoord(readInt(), readInt(), readInt());
    }

    public byte[] readByteArray(int length) {
        byte[] barray = new byte[length];
        byteBuf.readBytes(barray, 0, length);
        return barray;
    }

    public String readString() {
        return ByteBufUtils.readUTF8String(byteBuf);
    }

    public ItemStack readItemStack() {
        return readItemStack(false);
    }

    public ItemStack readItemStack(boolean large) {
        ItemStack item = null;
        short itemID = readShort();

        if (itemID >= 0) {
            int stackSize = large ? readInt() : readByte();
            short damage = readShort();
            item = new ItemStack(Item.getItemById(itemID), stackSize, damage);
            item.stackTagCompound = readNBTTagCompound();
        }

        return item;
    }

    public NBTTagCompound readNBTTagCompound() {
        return ByteBufUtils.readTag(byteBuf);
    }

    public FluidStack readFluidStack() {
        FluidStack fluid = null;
        short fluidID = readShort();

        if (fluidID >= 0)
            fluid = new FluidStack(fluidID, readVarInt(), readNBTTagCompound());

        return fluid;
    }

    public FMLProxyPacket toPacket() {
        if (incoming())
            throw new IllegalStateException("Tried to write an incoming packet");

        if (byteBuf.readableBytes() > 32000 || (type & 0x80) != 0)
            do_compress();

        //FML packet impl returns the whole of the backing array, copy used portion of array to another ByteBuf
        return new FMLProxyPacket(byteBuf.copy(), channel);
    }

    public void sendToPlayer(EntityPlayer player) {
        sendToPlayer(toPacket(), player);
    }

    public static void sendToPlayer(Packet packet, EntityPlayer player) {
        if (player == null)
            sendToClients(packet);
        else
            ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(packet);
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
        PlayerManager playerManager = ((WorldServer)world).getPlayerManager();
        for (EntityPlayerMP player : (List<EntityPlayerMP>) MinecraftServer.getServer().getConfigurationManager().playerEntityList)
            if(playerManager.isPlayerWatchingChunk(player, chunkX, chunkZ))
                sendToPlayer(packet, player);

        /* Commented until forge accepts access tranformer request
        PlayerInstance p = ((WorldServer) world).getPlayerManager().getOrCreateChunkWatcher(chunkX, chunkZ, false);
        if (p != null)
            p.sendToAllPlayersWatchingChunk(packet);*/
    }

    public void sendToOps() {
        sendToOps(toPacket());
    }

    public static void sendToOps(Packet packet) {
        for (EntityPlayerMP player : (List<EntityPlayerMP>) MinecraftServer.getServer().getConfigurationManager().playerEntityList)
            if (MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile()))
                sendToPlayer(packet, player);
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
