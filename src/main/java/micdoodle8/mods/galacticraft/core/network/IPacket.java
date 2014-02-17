package micdoodle8.mods.galacticraft.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public interface IPacket
{
	public void encodeInto(ChannelHandlerContext context, ByteBuf buffer);

	public void decodeInto(ChannelHandlerContext context, ByteBuf buffer);

	public void handleClientSide(EntityPlayer player);

	public void handleServerSide(EntityPlayer player);
}
