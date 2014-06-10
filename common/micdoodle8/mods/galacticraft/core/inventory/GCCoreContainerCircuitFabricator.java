package micdoodle8.mods.galacticraft.core.inventory;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.item.GCItems;
import micdoodle8.mods.galacticraft.api.transmission.core.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCircuitFabricator;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

/**
 * GCCoreContainerCircuitFabricator.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreContainerCircuitFabricator extends Container
{
	private GCCoreTileEntityCircuitFabricator tileEntity;

	public GCCoreContainerCircuitFabricator(InventoryPlayer playerInv, GCCoreTileEntityCircuitFabricator tileEntity)
	{
		this.tileEntity = tileEntity;

		// Coal slot
		this.addSlotToContainer(new SlotSpecific(tileEntity, 0, 6, 69, IItemElectric.class));

		// Diamond
		this.addSlotToContainer(new SlotSpecific(tileEntity, 1, 15, 17, new ItemStack(Item.diamond, 1, 0)));

		// Silicon
		this.addSlotToContainer(new SlotSpecific(tileEntity, 2, 74, 46, new ItemStack(GCCoreItems.basicItem, 1, 2)));
		this.addSlotToContainer(new SlotSpecific(tileEntity, 3, 74, 64, new ItemStack(GCCoreItems.basicItem, 1, 2)));

		// Redstone
		this.addSlotToContainer(new SlotSpecific(tileEntity, 4, 122, 46, new ItemStack(Item.redstone, 1, 0)));

		// Optional
		ItemStack stacks[] = {new ItemStack(Item.dyePowder, 1, 4),  new ItemStack(Item.redstoneRepeater.itemID, 1, 0), new ItemStack(Block.torchRedstoneActive)};
		this.addSlotToContainer(new SlotSpecific(tileEntity, 5, 145, 20, stacks));

		// Smelting result
		this.addSlotToContainer(new SlotFurnace(playerInv.player, tileEntity, 6, 152, 86));

		int slot;

		for (slot = 0; slot < 3; ++slot)
		{
			for (int var4 = 0; var4 < 9; ++var4)
			{
				this.addSlotToContainer(new Slot(playerInv, var4 + slot * 9 + 9, 8 + var4 * 18, 110 + slot * 18));
			}
		}

		for (slot = 0; slot < 9; ++slot)
		{
			this.addSlotToContainer(new Slot(playerInv, slot, 8 + slot * 18, 168));
		}
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
	}

	/**
	 * Called to transfer a stack from one inventory to the other eg. when shift
	 * clicking.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
	{
		ItemStack var2 = null;
		Slot slot = (Slot) this.inventorySlots.get(par1);
		final int b = this.inventorySlots.size();

		if (slot != null && slot.getHasStack())
		{
			ItemStack var4 = slot.getStack();
			var2 = var4.copy();

			if (par1 < b - 36)
			{
				if (!this.mergeItemStack(var4, b - 36, b, true))
					return null;

				if (par1 == 6) slot.onSlotChange(var4, var2);
			}
			else
			{
				Item i = var4.getItem();
				if (i instanceof IItemElectric)
				{
					if (!this.mergeItemStack(var4, 0, 1, false))
						return null;
				}
				else if (i.itemID == Item.diamond.itemID)
				{
					if (!this.mergeItemStack(var4, 1, 2, false))
						return null;
				}
				else if (i.itemID == GCCoreItems.basicItem.itemID && i.getDamage(var4) == 2)
				{
					if (!this.mergeItemStack(var4, 2, 4, false))
						return null;
				}
				else if (i.itemID == Item.redstone.itemID)
				{
					if (!this.mergeItemStack(var4, 4, 5, false))
						return null;
				}
				else if (i.itemID == Item.redstoneRepeater.itemID || i.itemID == new ItemStack(Block.torchRedstoneActive).getItem().itemID || i.itemID == Item.dyePowder.itemID && i.getDamage(var4) == 4)
				{
					if (!this.mergeItemStack(var4, 5, 6, false))
						return null;
				}
				else if (par1 < b - 9)
				{
					if (!this.mergeItemStack(var4, b - 9, b, false))
						return null;
				}
				else if (!this.mergeItemStack(var4, b - 36, b - 9, false))
				{
					return null;
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
