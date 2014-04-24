package micdoodle8.mods.galacticraft.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * GCCoreContainerParachest.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreContainerParachest extends Container
{
	private IInventory parachestInventory;
	public int numRows;

	public GCCoreContainerParachest(IInventory par1IInventory, IInventory par2IInventory)
	{
		this.parachestInventory = par2IInventory;
		this.numRows = (par2IInventory.getSizeInventory() - 3) / 9;
		par2IInventory.openChest();
		int i = (this.numRows - 4) * 18 + 19;
		int j;
		int k;

		for (j = 0; j < this.numRows; ++j)
		{
			for (k = 0; k < 9; ++k)
			{
				this.addSlotToContainer(new Slot(par2IInventory, k + j * 9, 8 + k * 18, 18 + j * 18));
			}
		}

		this.addSlotToContainer(new Slot(par2IInventory, par2IInventory.getSizeInventory() - 3, 125 + 0 * 18, (this.numRows == 0 ? 24 : 26) + this.numRows * 18));
		this.addSlotToContainer(new Slot(par2IInventory, par2IInventory.getSizeInventory() - 2, 125 + 1 * 18, (this.numRows == 0 ? 24 : 26) + this.numRows * 18));
		this.addSlotToContainer(new Slot(par2IInventory, par2IInventory.getSizeInventory() - 1, 75, (this.numRows == 0 ? 24 : 26) + this.numRows * 18));

		for (j = 0; j < 3; ++j)
		{
			for (k = 0; k < 9; ++k)
			{
				this.addSlotToContainer(new Slot(par1IInventory, k + j * 9 + 9, 8 + k * 18, (this.numRows == 0 ? 116 : 118) + j * 18 + i));
			}
		}

		for (j = 0; j < 9; ++j)
		{
			this.addSlotToContainer(new Slot(par1IInventory, j, 8 + j * 18, (this.numRows == 0 ? 174 : 176) + i));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{
		return this.parachestInventory.isUseableByPlayer(par1EntityPlayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(par2);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2 < this.parachestInventory.getSizeInventory())
			{
				if (!this.mergeItemStack(itemstack1, this.numRows * 9 + 3, this.inventorySlots.size(), true))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 0, this.parachestInventory.getSizeInventory(), false))
			{
				return null;
			}

			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

	/**
	 * Callback for when the crafting gui is closed.
	 */
	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);
		this.parachestInventory.closeChest();
	}

	/**
	 * Return this chest container's lower chest inventory.
	 */
	public IInventory getparachestInventory()
	{
		return this.parachestInventory;
	}
}
