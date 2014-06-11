package micdoodle8.mods.galacticraft.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * GCCoreInventoryExtended.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class InventoryExtended implements IInventory
{
	public ItemStack[] inventoryStacks = new ItemStack[10];

	@Override
	public int getSizeInventory()
	{
		return this.inventoryStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return this.inventoryStacks[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j)
	{
		if (this.inventoryStacks[i] != null)
		{
			ItemStack var3;

			if (this.inventoryStacks[i].stackSize <= j)
			{
				var3 = this.inventoryStacks[i];
				this.inventoryStacks[i] = null;
				return var3;
			}
			else
			{
				var3 = this.inventoryStacks[i].splitStack(j);

				if (this.inventoryStacks[i].stackSize == 0)
				{
					this.inventoryStacks[i] = null;
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
	public ItemStack getStackInSlotOnClosing(int i)
	{
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		this.inventoryStacks[i] = itemstack;

		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
		{
			itemstack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName()
	{
		return "Galacticraft Player Inventory";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return true;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public void markDirty()
	{

	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return true;
	}

	@Override
	public void openInventory()
	{
		;
	}

	@Override
	public void closeInventory()
	{
		;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return false;
	}

	public void dropExtendedItems(EntityPlayer player)
	{
		for (int i = 0; i < this.inventoryStacks.length; i++)
		{
			ItemStack stack = this.inventoryStacks[i];

			if (stack != null)
			{
				player.dropPlayerItemWithRandomChoice(stack, true);
			}

			this.inventoryStacks[i] = null;
		}
	}

	// Backwards compatibility for old inventory
	public void readFromNBTOld(NBTTagList par1NBTTagList)
	{
		this.inventoryStacks = new ItemStack[10];

		for (int i = 0; i < par1NBTTagList.tagCount(); ++i)
		{
			final NBTTagCompound nbttagcompound = (NBTTagCompound) par1NBTTagList.getCompoundTagAt(i);
			final int j = nbttagcompound.getByte("Slot") & 255;
			final ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);

			if (itemstack != null)
			{
				if (j >= 200 && j < this.inventoryStacks.length + 200 - 1)
				{
					this.inventoryStacks[j - 200] = itemstack;
				}
			}
		}
	}

	public void readFromNBT(NBTTagList tagList)
	{
		this.inventoryStacks = new ItemStack[10];

		for (int i = 0; i < tagList.tagCount(); ++i)
		{
			final NBTTagCompound nbttagcompound = (NBTTagCompound) tagList.getCompoundTagAt(i);
			final int j = nbttagcompound.getByte("Slot") & 255;
			final ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);

			if (itemstack != null)
			{
				this.inventoryStacks[j] = itemstack;
			}
		}
	}

	public NBTTagList writeToNBT(NBTTagList tagList)
	{
		NBTTagCompound nbttagcompound;

		for (int i = 0; i < this.inventoryStacks.length; ++i)
		{
			if (this.inventoryStacks[i] != null)
			{
				nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte) i);
				this.inventoryStacks[i].writeToNBT(nbttagcompound);
				tagList.appendTag(nbttagcompound);
			}
		}

		return tagList;
	}

	public void copyInventory(InventoryExtended par1InventoryPlayer)
	{
		for (int i = 0; i < this.inventoryStacks.length; ++i)
		{
			this.inventoryStacks[i] = ItemStack.copyItemStack(par1InventoryPlayer.inventoryStacks[i]);
		}
	}
}
