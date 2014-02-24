package micdoodle8.mods.galacticraft.mars.inventory;

import micdoodle8.mods.galacticraft.api.transmission.core.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.inventory.SlotSpecific;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntitySlimeling;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * GCMarsContainerSlimeling.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsContainerSlimeling extends Container
{
	private final GCMarsInventorySlimeling slimelingInventory;

	public GCMarsContainerSlimeling(InventoryPlayer playerInventory, GCMarsEntitySlimeling slimeling)
	{
		this.slimelingInventory = slimeling.slimelingInventory;
		this.slimelingInventory.currentContainer = this;

		GCMarsContainerSlimeling.addSlots(this, playerInventory, slimeling);
		GCMarsContainerSlimeling.addAdditionalSlots(this, slimeling, slimeling.getCargoSlot());

		this.slimelingInventory.openChest();
	}

	public static void addSlots(GCMarsContainerSlimeling container, InventoryPlayer playerInventory, GCMarsEntitySlimeling slimeling)
	{
		Slot slot = new SlotSpecific(slimeling.slimelingInventory, 1, 9, 8, new ItemStack(GCMarsItems.marsItemBasic, 1, 4));
		container.addSlotToContainer(slot);

		int var3;

		for (var3 = 0; var3 < 3; ++var3)
		{
			for (int var4 = 0; var4 < 9; ++var4)
			{
				slot = new Slot(playerInventory, var4 + var3 * 9 + 9, 8 + var4 * 18, 107 + var3 * 18);
				container.addSlotToContainer(slot);
			}
		}

		for (var3 = 0; var3 < 9; ++var3)
		{
			slot = new Slot(playerInventory, var3, 8 + var3 * 18, 165);
			container.addSlotToContainer(slot);
		}
	}

	public static void removeSlots(GCMarsContainerSlimeling container)
	{
		container.inventoryItemStacks.clear();
		container.inventorySlots.clear();
	}

	@SuppressWarnings("unchecked")
	public static void addAdditionalSlots(GCMarsContainerSlimeling container, GCMarsEntitySlimeling slimeling, ItemStack stack)
	{
		if (stack != null && stack.getItem().itemID == GCMarsItems.marsItemBasic.itemID && stack.getItemDamage() == 4)
		{
			for (int var3 = 0; var3 < 3; ++var3)
			{
				for (int var4 = 0; var4 < 9; ++var4)
				{
					Slot slot = new Slot(slimeling.slimelingInventory, var4 + var3 * 9 + 2, 8 + var4 * 18, 32 + var3 * 18);
					slot.slotNumber = container.inventorySlots.size();
					container.inventorySlots.add(slot);
					container.inventoryItemStacks.add((Object) null);
				}
			}
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer entityplayer)
	{
		this.slimelingInventory.closeChest();
	}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{
		return this.slimelingInventory.isUseableByPlayer(par1EntityPlayer);
	}

	/**
	 * Called to transfer a stack from one inventory to the other eg. when shift
	 * clicking.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
	{
		ItemStack var2 = null;
		final Slot var3 = (Slot) this.inventorySlots.get(par1);

		if (var3 != null && var3.getHasStack())
		{
			final ItemStack var4 = var3.getStack();
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
				else if (FluidContainerRegistry.isContainer(var4) || FluidContainerRegistry.containsFluid(var4, FluidRegistry.getFluidStack("fuel", 1)))
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
