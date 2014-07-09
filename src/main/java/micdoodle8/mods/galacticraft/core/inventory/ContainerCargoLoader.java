package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.transmission.core.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCargoLoader extends Container
{
	private TileEntityElectricBlock tileEntity;

	public ContainerCargoLoader(InventoryPlayer par1InventoryPlayer, IInventory cargoLoader)
	{
		this.tileEntity = (TileEntityElectricBlock) cargoLoader;
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
		return this.tileEntity.isUseableByPlayer(var1);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack var3 = null;
		final Slot slot = (Slot) this.inventorySlots.get(par2);

		if (slot != null && slot.getHasStack())
		{
			final ItemStack var5 = slot.getStack();
			var3 = var5.copy();

			if (par2 < 15)
			{
				if (!this.mergeItemStack(var5, 15, 51, true))
				{
					return null;
				}
			}
			else
			{
				if (var5.getItem() instanceof IItemElectric)
				{
					if (!this.mergeItemStack(var5, 0, 1, false))
					{
						return null;
					}
				}
				else if (par2 < 42)
				{
					if (!this.mergeItemStack(var5, 1, 15, false) && !this.mergeItemStack(var5, 42, 51, false))
					{
						return null;
					}
				}
				else if (!this.mergeItemStack(var5, 1, 15, false) && !this.mergeItemStack(var5, 15, 42, false))
				{
					return null;
				}
			}

			if (var5.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}
			
            if (var5.stackSize == var3.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, var5);
		}

		return var3;
	}
}
