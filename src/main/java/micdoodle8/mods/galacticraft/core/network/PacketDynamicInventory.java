package micdoodle8.mods.galacticraft.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.IInventorySettable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class PacketDynamicInventory implements IPacket
{
	private int type;
	private Object[] data;
	private ItemStack[] stacks;

	public PacketDynamicInventory()
	{
	}

	public PacketDynamicInventory(Entity entity)
	{
		assert entity instanceof IInventory : "Entity does not implement " + IInventory.class.getSimpleName();
		this.type = 0;
		this.data = new Object[] { entity.getEntityId() };
		this.stacks = new ItemStack[((IInventory) entity).getSizeInventory()];

		for (int i = 0; i < this.stacks.length; i++)
		{
			this.stacks[i] = ((IInventory) entity).getStackInSlot(i);
		}
	}

	public PacketDynamicInventory(TileEntity chest)
	{
		assert chest instanceof IInventory : "Tile does not implement " + IInventory.class.getSimpleName();
		this.type = 1;
		this.data = new Object[] { chest.xCoord, chest.yCoord, chest.zCoord };
		this.stacks = new ItemStack[((IInventory) chest).getSizeInventory()];

		for (int i = 0; i < this.stacks.length; i++)
		{
			this.stacks[i] = ((IInventory) chest).getStackInSlot(i);
		}
	}

	@Override
	public void encodeInto(ChannelHandlerContext context, ByteBuf buffer)
	{
		buffer.writeInt(this.type);

		switch (this.type)
		{
		case 0:
			buffer.writeInt((Integer) this.data[0]);
			break;
		case 1:
			buffer.writeInt((Integer) this.data[0]);
			buffer.writeInt((Integer) this.data[1]);
			buffer.writeInt((Integer) this.data[2]);
			break;
		}

		buffer.writeInt(this.stacks.length);

		for (int i = 0; i < this.stacks.length; i++)
		{
			try
			{
				NetworkUtil.writeItemStack(this.stacks[i], buffer);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext context, ByteBuf buffer)
	{
		this.type = buffer.readInt();

		switch (this.type)
		{
		case 0:
			this.data = new Object[1];
			this.data[0] = buffer.readInt();
			break;
		case 1:
			this.data = new Object[3];
			this.data[0] = buffer.readInt();
			this.data[1] = buffer.readInt();
			this.data[2] = buffer.readInt();
			break;
		}

		this.stacks = new ItemStack[buffer.readInt()];

		for (int i = 0; i < this.stacks.length; i++)
		{
			try
			{
				this.stacks[i] = NetworkUtil.readItemStack(buffer);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void handleClientSide(EntityPlayer player)
	{
		if (player.worldObj == null) return;
		
		switch (this.type)
		{
		case 0:
			Entity entity = player.worldObj.getEntityByID((Integer) this.data[0]);

			if (entity instanceof IInventorySettable)
			{
				this.setInventoryStacks((IInventorySettable) entity);
			}

			break;
		case 1:
			TileEntity tile = player.worldObj.getTileEntity((Integer) this.data[0], (Integer) this.data[1], (Integer) this.data[2]);

			if (tile instanceof IInventorySettable)
			{
				this.setInventoryStacks((IInventorySettable) tile);
			}

			break;
		}
	}

	@Override
	public void handleServerSide(EntityPlayer player)
	{
		switch (this.type)
		{
		case 0:
			Entity entity = player.worldObj.getEntityByID((Integer) this.data[0]);

			if (entity instanceof IInventorySettable)
			{
				GalacticraftCore.packetPipeline.sendTo(new PacketDynamicInventory(entity), (EntityPlayerMP) player);
			}

			break;
		case 1:
			TileEntity tile = player.worldObj.getTileEntity((Integer) this.data[0], (Integer) this.data[1], (Integer) this.data[2]);

			if (tile instanceof IInventorySettable)
			{
				GalacticraftCore.packetPipeline.sendTo(new PacketDynamicInventory(tile), (EntityPlayerMP) player);
			}

			break;
		}
	}

	private void setInventoryStacks(IInventorySettable inv)
	{
		inv.setSizeInventory(this.stacks.length);

		for (int i = 0; i < this.stacks.length; i++)
		{
			inv.setInventorySlotContents(i, this.stacks[i]);
		}
	}
}
