package micdoodle8.mods.galacticraft.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAirLockController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketGuiElement implements IPacket
{
	private int subType;
	private int xCoord;
	private int yCoord;
	private int zCoord;
	private int data;

	public PacketGuiElement(int subType, int xCoord, int yCoord, int zCoord, int data)
	{
		this.subType = subType;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.zCoord = zCoord;
		this.data = data;
	}
	
	public PacketGuiElement(int subType, Vector3 tilePos, int data)
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
		buffer.writeInt(this.data);
	}

	@Override
	public void decodeInto(ChannelHandlerContext context, ByteBuf buffer)
	{
		this.subType = buffer.readInt();
		this.xCoord = buffer.readInt();
		this.yCoord = buffer.readInt();
		this.zCoord = buffer.readInt();
		this.data = buffer.readInt();
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
				launchController.redstoneActivation = this.data == 1;
			}
			break;
		case 1:
			if (tile instanceof TileEntityAirLockController)
			{
				TileEntityAirLockController launchController = (TileEntityAirLockController) tile;
				launchController.playerDistanceActivation = this.data == 1;
			}
			break;
		case 2:
			if (tile instanceof TileEntityAirLockController)
			{
				TileEntityAirLockController launchController = (TileEntityAirLockController) tile;
				launchController.playerDistanceSelection = this.data;
			}
			break;
		case 3:
			if (tile instanceof TileEntityAirLockController)
			{
				TileEntityAirLockController launchController = (TileEntityAirLockController) tile;
				launchController.playerNameMatches = this.data == 1;
			}
			break;
		case 4:
			if (tile instanceof TileEntityAirLockController)
			{
				TileEntityAirLockController launchController = (TileEntityAirLockController) tile;
				launchController.invertSelection = this.data == 1;
			}
			break;
		case 5:
			if (tile instanceof TileEntityAirLockController)
			{
				TileEntityAirLockController launchController = (TileEntityAirLockController) tile;
				launchController.lastHorizontalModeEnabled = launchController.horizontalModeEnabled;
				launchController.horizontalModeEnabled = this.data == 1;
			}
			break;
		default:
			break;
		}
	}
}
