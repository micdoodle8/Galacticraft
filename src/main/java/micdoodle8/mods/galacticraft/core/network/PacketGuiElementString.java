package micdoodle8.mods.galacticraft.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAirLockController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.ByteBufUtils;

public class PacketGuiElementString implements IPacket
{
	private int subType;
	private int xCoord;
	private int yCoord;
	private int zCoord;
	private String data;

	public PacketGuiElementString(int subType, int xCoord, int yCoord, int zCoord, String data)
	{
		this.subType = subType;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.zCoord = zCoord;
		this.data = data;
	}
	
	public PacketGuiElementString(int subType, Vector3 tilePos, String data)
	{
		this(subType, tilePos.intX(), tilePos.intY(), tilePos.intZ(), data);
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext context, ByteBuf buffer)
	{
		buffer.writeInt(this.subType);
		buffer.writeInt(this.xCoord);
		buffer.writeInt(this.yCoord);
		buffer.writeInt(this.zCoord);
		ByteBufUtils.writeUTF8String(buffer, this.data);
	}

	@Override
	public void decodeInto(ChannelHandlerContext context, ByteBuf buffer)
	{
		this.subType = buffer.readInt();
		this.xCoord = buffer.readInt();
		this.yCoord = buffer.readInt();
		this.zCoord = buffer.readInt();
		this.data = ByteBufUtils.readUTF8String(buffer);
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
		TileEntity tile = world.getTileEntity(this.xCoord, this.yCoord, this.zCoord);
		
		switch (this.subType)
		{
		case 0:
			if (tile instanceof TileEntityAirLockController)
			{
				TileEntityAirLockController launchController = (TileEntityAirLockController) tile;
				launchController.playerToOpenFor = this.data;
			}
			break;
		default:
			break;
		}
	}
}
