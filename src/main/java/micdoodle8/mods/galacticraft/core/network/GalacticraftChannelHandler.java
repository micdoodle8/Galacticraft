package micdoodle8.mods.galacticraft.core.network;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import gnu.trove.map.hash.TByteObjectHashMap;
import gnu.trove.map.hash.TObjectByteHashMap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.AttributeKey;
import micdoodle8.mods.galacticraft.api.recipe.SchematicPage;
import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import org.apache.logging.log4j.Level;

import java.lang.ref.WeakReference;
import java.util.EnumMap;
import java.util.List;

@ChannelHandler.Sharable
public class GalacticraftChannelHandler extends MessageToMessageCodec<FMLProxyPacket, IPacket>
{
	private EnumMap<Side, FMLEmbeddedChannel> channels;

	private GalacticraftChannelHandler()
	{
		this.addDiscriminator(0, PacketSimple.class);
		this.addDiscriminator(1, PacketRotateRocket.class);
		this.addDiscriminator(2, PacketDynamic.class);
		this.addDiscriminator(3, PacketControllableEntity.class);
		this.addDiscriminator(4, PacketEntityUpdate.class);
		this.addDiscriminator(5, PacketDynamicInventory.class);
	}

	public static GalacticraftChannelHandler init()
	{
		GalacticraftChannelHandler channelHandler = new GalacticraftChannelHandler();
		channelHandler.channels = NetworkRegistry.INSTANCE.newChannel(Constants.MOD_ID_CORE, channelHandler, new GalacticraftPacketHandler());
		return channelHandler;
	}

//	@Override
	public void encodeInto(ChannelHandlerContext ctx, IPacket msg, ByteBuf target) throws Exception
	{
		msg.encodeInto(ctx, target);
	}

//	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, IPacket msg)
	{
		msg.decodeInto(ctx, source);
	}

	/**
	 * Send this message to everyone.
	 * <p/>
	 * Adapted from CPW's code in
	 * cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
	 * 
	 * @param message
	 *            The message to send
	 */
	public void sendToAll(IPacket message)
	{
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		this.channels.get(Side.SERVER).writeOutbound(message);
	}

	/**
	 * Send this message to the specified player.
	 * <p/>
	 * Adapted from CPW's code in
	 * cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
	 * 
	 * @param message
	 *            The message to send
	 * @param player
	 *            The player to send it to
	 */
	public void sendTo(IPacket message, EntityPlayerMP player)
	{
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		this.channels.get(Side.SERVER).writeOutbound(message);
	}

	/**
	 * Send this message to everyone within a certain range of a point.
	 * <p/>
	 * Adapted from CPW's code in
	 * cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
	 * 
	 * @param message
	 *            The message to send
	 * @param point
	 *            The
	 *            {@link cpw.mods.fml.common.network.NetworkRegistry.TargetPoint}
	 *            around which to send
	 */
	public void sendToAllAround(IPacket message, NetworkRegistry.TargetPoint point)
	{
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
		this.channels.get(Side.SERVER).writeOutbound(message);
	}

	/**
	 * Send this message to everyone within the supplied dimension.
	 * <p/>
	 * Adapted from CPW's code in
	 * cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
	 * 
	 * @param message
	 *            The message to send
	 * @param dimensionId
	 *            The dimension id to target
	 */
	public void sendToDimension(IPacket message, int dimensionId)
	{
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
		this.channels.get(Side.SERVER).writeOutbound(message);
	}

	/**
	 * Send this message to the server.
	 * <p/>
	 * Adapted from CPW's code in
	 * cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
	 * 
	 * @param message
	 *            The message to send
	 */
	public void sendToServer(IPacket message)
	{
		this.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		this.channels.get(Side.CLIENT).writeOutbound(message);
	}

    private TByteObjectHashMap<Class<? extends IPacket>> discriminators = new TByteObjectHashMap<Class<? extends IPacket>>();
    private TObjectByteHashMap<Class<? extends IPacket>> types = new TObjectByteHashMap<Class<? extends IPacket>>();

    /**
     * Make this accessible to subclasses
     */

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception
    {
        super.handlerAdded(ctx);
        ctx.attr(FMLIndexedMessageToMessageCodec.INBOUNDPACKETTRACKER).set(new ThreadLocal<WeakReference<FMLProxyPacket>>());
    }

    public void addDiscriminator(int discriminator, Class<? extends IPacket> type)
    {
        discriminators.put((byte)discriminator, type);
        types.put(type, (byte)discriminator);
    }

//    public abstract void encodeInto(ChannelHandlerContext ctx, IPacket msg, ByteBuf target) throws Exception;
    @Override
    protected final void encode(ChannelHandlerContext ctx, IPacket msg, List<Object> out) throws Exception
    {
        ByteBuf buffer = Unpooled.buffer();
        @SuppressWarnings("unchecked") // Stupid unnecessary cast I can't seem to kill
                Class<? extends IPacket> clazz = (Class<? extends IPacket>) msg.getClass();
        byte discriminator = types.get(clazz);
        buffer.writeByte(discriminator);
        encodeInto(ctx, msg, buffer);
        FMLProxyPacket proxy = new FMLProxyPacket(buffer.copy(), ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());
        System.out.println("SENDING PACKET " + clazz.getSimpleName() + " size: " + buffer.array().length);
        if (msg instanceof PacketSimple)
        {
            System.out.println("    SIMPLE PACKET type: " + ((PacketSimple) msg).type);
        }
        System.out.println("    Packet size 2: " + ((C17PacketCustomPayload)proxy.toC17Packet()).func_149558_e().length);
        WeakReference<FMLProxyPacket> ref = ctx.attr(FMLIndexedMessageToMessageCodec.INBOUNDPACKETTRACKER).get().get();
        FMLProxyPacket old = ref == null ? null : ref.get();
        if (old != null)
        {
            proxy.setDispatcher(old.getDispatcher());
        }
        out.add(proxy);
    }

//    public abstract void decodeInto(ChannelHandlerContext ctx, ByteBuf source, IPacket msg);

    @Override
    protected final void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) throws Exception
    {
        testMessageValidity(msg);
        ByteBuf payload = msg.payload();
        byte discriminator = payload.readByte();
        Class<? extends IPacket> clazz = discriminators.get(discriminator);
        if(clazz == null)
        {
            throw new NullPointerException("Undefined message for discriminator " + discriminator + " in channel " + msg.channel());
        }
        IPacket newMsg = clazz.newInstance();
        ctx.attr(FMLIndexedMessageToMessageCodec.INBOUNDPACKETTRACKER).get().set(new WeakReference<FMLProxyPacket>(msg));
        decodeInto(ctx, payload.slice(), newMsg);
        out.add(newMsg);
    }

    /**
     * Called to verify the message received. This can be used to hard disconnect in case of an unexpected packet,
     * say due to a weird protocol mismatch. Use with caution.
     * @param msg
     */
    protected void testMessageValidity(FMLProxyPacket msg)
    {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        FMLLog.log(Level.ERROR, cause, "FMLIndexedMessageCodec exception caught");
        super.exceptionCaught(ctx, cause);
    }
}
