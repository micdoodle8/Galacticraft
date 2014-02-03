package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.transmission.core.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCircuitFabricator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
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
		this.addSlotToContainer(new Slot(tileEntity, 1, 15, 17));

		// Silicon
		this.addSlotToContainer(new Slot(tileEntity, 2, 74, 46));
		this.addSlotToContainer(new Slot(tileEntity, 3, 74, 64));

		// Redstone
		this.addSlotToContainer(new Slot(tileEntity, 4, 122, 46));

		// Optional
		this.addSlotToContainer(new Slot(tileEntity, 5, 145, 20));

		// Smelting result
		this.addSlotToContainer(new SlotFurnace(playerInv.player, tileEntity, 6, 152, 86));

		int var3;

		for (var3 = 0; var3 < 3; ++var3)
		{
			for (int var4 = 0; var4 < 9; ++var4)
			{
				this.addSlotToContainer(new Slot(playerInv, var4 + var3 * 9 + 9, 8 + var4 * 18, 110 + var3 * 18));
			}
		}

		for (var3 = 0; var3 < 9; ++var3)
		{
			this.addSlotToContainer(new Slot(playerInv, var3, 8 + var3 * 18, 168));
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
		Slot var3 = (Slot) this.inventorySlots.get(par1);

		if (var3 != null && var3.getHasStack())
		{
			ItemStack var4 = var3.getStack();
			var2 = var4.copy();

			if (par1 == 2)
			{
				if (!this.mergeItemStack(var4, 3, 39, true))
				{
					return null;
				}

				var3.onSlotChange(var4, var2);
			}
			else if (par1 != 1 && par1 != 0)
			{
				if (var4.getItem() instanceof IItemElectric)
				{
					if (!this.mergeItemStack(var4, 0, 1, false))
					{
						return null;
					}
				}
				else if (FurnaceRecipes.smelting().getSmeltingResult(var4) != null)
				{
					if (!this.mergeItemStack(var4, 1, 2, false))
					{
						return null;
					}
				}
				else if (par1 >= 3 && par1 < 30)
				{
					if (!this.mergeItemStack(var4, 30, 39, false))
					{
						return null;
					}
				}
				else if (par1 >= 30 && par1 < 39 && !this.mergeItemStack(var4, 3, 30, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(var4, 3, 39, false))
			{
				return null;
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
