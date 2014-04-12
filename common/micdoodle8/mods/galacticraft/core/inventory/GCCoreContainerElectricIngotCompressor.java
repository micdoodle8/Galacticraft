package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.transmission.core.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityElectricIngotCompressor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

/**
 * GCCoreContainerElectricIngotCompressor.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreContainerElectricIngotCompressor extends Container
{
	private GCCoreTileEntityElectricIngotCompressor tileEntity;

	public GCCoreContainerElectricIngotCompressor(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityElectricIngotCompressor tileEntity)
	{
		this.tileEntity = tileEntity;
		tileEntity.compressingCraftMatrix.eventHandler = this;

		for (int x = 0; x < 3; x++)
		{
			for (int y = 0; y < 3; y++)
			{
				this.addSlotToContainer(new Slot(tileEntity.compressingCraftMatrix, y + x * 3, 19 + y * 18, 18 + x * 18));
			}
		}

		// Battery Slot
		this.addSlotToContainer(new SlotSpecific(tileEntity, 0, 55, 75, IItemElectric.class));

		// Smelting result
		this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, tileEntity, 1, 138, 30));
		this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, tileEntity, 2, 138, 48));

		int var3;

		for (var3 = 0; var3 < 3; ++var3)
		{
			for (int var4 = 0; var4 < 9; ++var4)
			{
				this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 117 + var3 * 18));
			}
		}

		for (var3 = 0; var3 < 9; ++var3)
		{
			this.addSlotToContainer(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 175));
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer entityplayer)
	{
		super.onContainerClosed(entityplayer);
	}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{
		return this.tileEntity.isUseableByPlayer(par1EntityPlayer);
	}

	@Override
	public void onCraftMatrixChanged(IInventory par1IInventory)
	{
		super.onCraftMatrixChanged(par1IInventory);
		this.tileEntity.updateInput();
	}

	/**
	 * Called to transfer a stack from one inventory to the other eg. when shift
	 * clicking.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
	{
		ItemStack var2 = null;
		Slot var3 = (Slot) this.inventorySlots.get(par1);

		if (var3 != null && var3.getHasStack())
		{
			ItemStack var4 = var3.getStack();
			var2 = var4.copy();

			if (par1 <= 11)
			{
				if (!this.mergeItemStack(var4, 12, 39, true))
				{
					return null;
				}

				var3.onSlotChange(var4, var2);
			}
			else
			{
				if (var4.getItem() instanceof IItemElectric)
				{
					if (!this.mergeItemStack(var4, 9, 10, false))
					{
						return null;
					}
				}
				else if (par1 < 39)
				{
					if (!this.mergeItemStack(var4, 0, 9, false) && !this.mergeItemStack(var4, 39, 48, false))
					{
						return null;
					}
				}
				else if (par1 >= 39 && par1 < 48 && !this.mergeItemStack(var4, 12, 39, false))
				{
					return null;
				}
			}

			if (var4.stackSize == 0)
			{
				var3.putStack((ItemStack) null);
			}
			else
			{
				var3.onSlotChanged();
			}

			if (var4.stackSize == var2.stackSize)
			{
				return null;
			}

			var3.onPickupFromSlot(par1EntityPlayer, var4);
		}

		return var2;
	}
}
