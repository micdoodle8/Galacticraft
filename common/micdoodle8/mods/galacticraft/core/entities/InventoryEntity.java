package micdoodle8.mods.galacticraft.core.entities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

/**
 * InventoryEntity.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public abstract class InventoryEntity extends NetworkedEntity implements IInventory
{
	public ItemStack[] containedItems;

	public InventoryEntity(World par1World)
	{
		super(par1World);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt)
	{
		NBTTagList itemList = nbt.getTagList("Items");
		this.containedItems = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < itemList.tagCount(); ++i)
		{
			NBTTagCompound itemTag = (NBTTagCompound) itemList.tagAt(i);
			int slotID = itemTag.getByte("Slot") & 255;

			if (slotID >= 0 && slotID < this.containedItems.length)
			{
				this.containedItems[slotID] = ItemStack.loadItemStackFromNBT(itemTag);
			}
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt)
	{
		NBTTagList itemList = new NBTTagList();

		for (int i = 0; i < this.containedItems.length; ++i)
		{
			if (this.containedItems[i] != null)
			{
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setByte("Slot", (byte) i);
				this.containedItems[i].writeToNBT(itemTag);
				itemList.appendTag(itemTag);
			}
		}

		nbt.setTag("Items", itemList);
	}

	@Override
	public ItemStack getStackInSlot(int var1)
	{
		return this.containedItems[var1];
	}

	@Override
	public ItemStack decrStackSize(int slotIndex, int amount)
	{
		if (this.containedItems[slotIndex] != null)
		{
			ItemStack var3;

			if (this.containedItems[slotIndex].stackSize <= amount)
			{
				var3 = this.containedItems[slotIndex];
				this.containedItems[slotIndex] = null;
				return var3;
			}
			else
			{
				var3 = this.containedItems[slotIndex].splitStack(amount);

				if (this.containedItems[slotIndex].stackSize == 0)
				{
					this.containedItems[slotIndex] = null;
				}

				return var3;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slotIndex)
	{
		if (this.containedItems[slotIndex] != null)
		{
			ItemStack stack = this.containedItems[slotIndex];
			this.containedItems[slotIndex] = null;
			return stack;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slotIndex, ItemStack stack)
	{
		this.containedItems[slotIndex] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public void onInventoryChanged()
	{
		;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return true;
	}

	@Override
	public void openChest()
	{
		;
	}

	@Override
	public void closeChest()
	{
		;
	}
}
