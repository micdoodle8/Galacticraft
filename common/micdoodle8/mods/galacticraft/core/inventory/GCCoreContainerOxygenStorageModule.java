package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.transmission.core.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenStorageModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * GCCoreContainerOxygenStorageModule.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreContainerOxygenStorageModule extends Container
{
	private GCCoreTileEntityOxygenStorageModule tileEntity;

	public GCCoreContainerOxygenStorageModule(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityOxygenStorageModule storageModule)
	{
		this.tileEntity = storageModule;

		int var3;

		for (var3 = 0; var3 < 3; ++var3)
		{
			for (int var4 = 0; var4 < 9; ++var4)
			{
				this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}

		for (var3 = 0; var3 < 9; ++var3)
		{
			this.addSlotToContainer(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 142));
		}

		this.tileEntity.playersUsing.add(par1InventoryPlayer.player);
	}

	@Override
	public void onContainerClosed(EntityPlayer entityplayer)
	{
		super.onContainerClosed(entityplayer);
		this.tileEntity.playersUsing.remove(entityplayer);
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
			final ItemStack stack = slot.getStack();
			var2 = stack.copy();

			if (par1 < b - 9)
			{
				if (!this.mergeItemStack(stack, b - 9, b, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(stack, b - 36, b - 9, false))
			{
				return null;
			}

			if (stack.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (stack.stackSize == var2.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(par1EntityPlayer, stack);
		}

		return var2;
	}
}
