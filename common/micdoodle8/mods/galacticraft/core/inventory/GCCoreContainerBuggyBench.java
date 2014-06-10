package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * GCCoreContainerBuggyBench.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreContainerBuggyBench extends Container
{
	public GCCoreInventoryBuggyBench craftMatrix = new GCCoreInventoryBuggyBench(this);
	public IInventory craftResult = new InventoryCraftResult();
	private final World worldObj;

	public GCCoreContainerBuggyBench(InventoryPlayer par1InventoryPlayer, int x, int y, int z)
	{
		final int change = 27;
		this.worldObj = par1InventoryPlayer.player.worldObj;
		this.addSlotToContainer(new GCCoreSlotRocketBenchResult(par1InventoryPlayer.player, this.craftMatrix, this.craftResult, 0, 142, 79 + change));
		int var6;
		int var7;

		// Body
		for (var6 = 0; var6 < 4; ++var6)
		{
			for (var7 = 0; var7 < 3; ++var7)
			{
				this.addSlotToContainer(new GCCoreSlotBuggyBench(this.craftMatrix, var7 * 4 + var6 + 1, 39 + var7 * 18, 14 + var6 * 18 + change, x, y, z, par1InventoryPlayer.player));
			}
		}

		for (var6 = 0; var6 < 2; ++var6)
		{
			for (var7 = 0; var7 < 2; ++var7)
			{
				this.addSlotToContainer(new GCCoreSlotBuggyBench(this.craftMatrix, var7 * 2 + var6 + 13, 21 + var7 * 72, 14 + var6 * 54 + change, x, y, z, par1InventoryPlayer.player));
			}
		}

		// Addons
		for (int var8 = 0; var8 < 3; var8++)
		{
			this.addSlotToContainer(new GCCoreSlotBuggyBench(this.craftMatrix, 17 + var8, 93 + var8 * 26, -15 + change, x, y, z, par1InventoryPlayer.player));
		}

		// Player inv:

		for (var6 = 0; var6 < 3; ++var6)
		{
			for (var7 = 0; var7 < 9; ++var7)
			{
				this.addSlotToContainer(new Slot(par1InventoryPlayer, var7 + var6 * 9 + 9, 8 + var7 * 18, 111 + var6 * 18 + change));
			}
		}

		for (var6 = 0; var6 < 9; ++var6)
		{
			this.addSlotToContainer(new Slot(par1InventoryPlayer, var6, 8 + var6 * 18, 169 + change));
		}

		this.onCraftMatrixChanged(this.craftMatrix);
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);

		if (!this.worldObj.isRemote)
		{
			for (int var2 = 1; var2 < 18; ++var2)
			{
				final ItemStack slot = this.craftMatrix.getStackInSlotOnClosing(var2);

				if (slot != null)
				{
					par1EntityPlayer.dropPlayerItem(slot);
				}
			}
		}
	}

	@Override
	public void onCraftMatrixChanged(IInventory par1IInventory)
	{
		this.craftResult.setInventorySlotContents(0, RecipeUtil.findMatchingBuggy(this.craftMatrix));
	}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{
		return true;
	}

	/**
	 * Called to transfer a stack from one inventory to the other eg. when shift
	 * clicking.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
	{
		ItemStack var2 = null;
		final Slot slot = (Slot) this.inventorySlots.get(par1);
		final int b = this.inventorySlots.size();

		if (slot != null && slot.getHasStack())
		{
			final ItemStack var4 = slot.getStack();
			var2 = var4.copy();

			if (par1 < b - 36)
			{
				if (!this.mergeItemStack(var4, b - 36, b, true))
				{
					return null;
				}

				if (par1 == 0) slot.onSlotChange(var4, var2);
			}
			else
			{
				int i = var4.getItem().itemID;
				if (i == GCCoreItems.heavyPlatingTier1.itemID || i == GCCoreItems.partBuggy.itemID)
				{
					for (int j = 1; j < 20; j ++)
					{
						if (((Slot) this.inventorySlots.get(par1)).isItemValid(var4))
							this.mergeItemStack(var4, j, j+1, false);
					}
				}
				else
				{
					if (par1 < b - 9)
					{
						if (!this.mergeItemStack(var4, b - 9, b, false))
						{
							return null;
						}
					}
					else
					{
						if (!this.mergeItemStack(var4, b - 36, b - 9, false))
						{
							return null;
						}
					}
				}
			}

			if (var4.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (var4.stackSize == var2.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(par1EntityPlayer, var4);
		}

		return var2;
	}
}
