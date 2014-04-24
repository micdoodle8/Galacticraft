package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.transmission.core.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityElectricBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * GCCoreContainerCargoLoader.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreContainerCargoLoader extends Container
{
	private GCCoreTileEntityElectricBlock tileEntity;

	public GCCoreContainerCargoLoader(InventoryPlayer par1InventoryPlayer, IInventory cargoLoader)
	{
		tileEntity = (GCCoreTileEntityElectricBlock) cargoLoader;
		this.addSlotToContainer(new SlotSpecific(cargoLoader, 0, 10, 27, IItemElectric.class));

		int var6;
		int var7;

		for (var6 = 0; var6 < 2; ++var6)
		{
			for (var7 = 0; var7 < 7; ++var7)
			{
				this.addSlotToContainer(new Slot(cargoLoader, var7 + var6 * 7 + 1, 38 + var7 * 18, 27 + var6 * 18));
			}
		}

		// Player inv:

		for (var6 = 0; var6 < 3; ++var6)
		{
			for (var7 = 0; var7 < 9; ++var7)
			{
				this.addSlotToContainer(new Slot(par1InventoryPlayer, var7 + var6 * 9 + 9, 8 + var7 * 18, 124 + var6 * 18));
			}
		}

		for (var6 = 0; var6 < 9; ++var6)
		{
			this.addSlotToContainer(new Slot(par1InventoryPlayer, var6, 8 + var6 * 18, 66 + 116));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer var1)
	{
		return tileEntity.isUseableByPlayer(var1);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack var3 = null;
		final Slot var4 = (Slot) this.inventorySlots.get(par2);

		if (var4 != null && var4.getHasStack())
		{
			final ItemStack var5 = var4.getStack();
			var3 = var5.copy();

			if (par2 < 36)
			{
				if (!this.mergeItemStack(var5, 36, this.inventorySlots.size(), true))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(var5, 0, 36, false))
			{
				return null;
			}

			if (var5.stackSize == 0)
			{
				var4.putStack((ItemStack) null);
			}
			else
			{
				var4.onSlotChanged();
			}
		}

		return var3;
	}
}
