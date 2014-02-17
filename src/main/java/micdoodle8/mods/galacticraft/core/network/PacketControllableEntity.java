package micdoodle8.mods.galacticraft.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import micdoodle8.mods.galacticraft.core.entities.IControllableEntity;
import net.minecraft.entity.player.EntityPlayer;

public class PacketControllableEntity implements IPacket
{
	private int keyPressed;

	public PacketControllableEntity()
	{
	}

	public PacketControllableEntity(int keyPressed)
	{
		this.keyPressed = keyPressed;
	}

	@Override
	public void encodeInto(ChannelHandlerContext context, ByteBuf buffer)
	{
		buffer.writeInt(this.keyPressed);
	}

	@Override
	public void decodeInto(ChannelHandlerContext context, ByteBuf buffer)
	{
		this.keyPressed = buffer.readInt();
	}

	@Override
	public void handleClientSide(EntityPlayer player)
	{
		this.handleKeyPress(player);
	}

	@Override
	public void handleServerSide(EntityPlayer player)
	{
		this.handleKeyPress(player);
	}

	private void handleKeyPress(EntityPlayer player)
	{
		if (player.ridingEntity != null && player.ridingEntity instanceof IControllableEntity)
		{
			((IControllableEntity) player.ridingEntity).pressKey(this.keyPressed);
		}
	}
}
