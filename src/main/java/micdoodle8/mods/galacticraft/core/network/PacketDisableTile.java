package micdoodle8.mods.galacticraft.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketDisableTile implements IPacket
{
	private int xCoord;
	private int yCoord;
	private int zCoord;
	private boolean disabled;
	private short buttonIndex;

	public PacketDisableTile(int xCoord, int yCoord, int zCoord, boolean disabled)
	{
		this(xCoord, yCoord, zCoord, disabled, (short) 0);
	}
	
	public PacketDisableTile(int xCoord, int yCoord, int zCoord, boolean disabled, short buttonIndex)
	{
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.zCoord = zCoord;
		this.disabled = disabled;
		this.buttonIndex = buttonIndex;
	}
	
	public PacketDisableTile(Vector3 tilePos, boolean disabled)
	{
		this(tilePos.intX(), tilePos.intY(), tilePos.intZ(), disabled, (short) 0);
	}
	
	public PacketDisableTile(Vector3 tilePos, boolean disabled, short buttonIndex)
	{
		this(tilePos.intX(), tilePos.intY(), tilePos.intZ(), disabled, buttonIndex);
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext context, ByteBuf buffer)
	{
		buffer.writeInt(this.xCoord);
		buffer.writeInt(this.yCoord);
		buffer.writeInt(this.zCoord);
		buffer.writeBoolean(this.disabled);
		buffer.writeShort(this.buttonIndex);
	}

	@Override
	public void decodeInto(ChannelHandlerContext context, ByteBuf buffer)
	{
		this.xCoord = buffer.readInt();
		this.yCoord = buffer.readInt();
		this.zCoord = buffer.readInt();
		this.disabled = buffer.readBoolean();
		this.buttonIndex = buffer.readShort();
	}

	@Override
	public void handleClientSide(EntityPlayer player)
	{
		this.setButtonState(player.worldObj);
	}

	@Override
	public void handleServerSide(EntityPlayer player)
	{
		this.setButtonState(player.worldObj);
	}
	
	private void setButtonState(World world)
	{
		TileEntity tile = world.getTileEntity(xCoord, yCoord, zCoord);
		
		if (tile instanceof IDisableableMachine)
		{
			((IDisableableMachine) tile).setDisabled((int) this.buttonIndex, disabled);
		}
	}
}
