package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.api.transmission.core.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCircuitFabricator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerCircuitFabricator extends Container
{
	private TileEntityCircuitFabricator tileEntity;

	public ContainerCircuitFabricator(InventoryPlayer playerInv, TileEntityCircuitFabricator tileEntity)
	{
		this.tileEntity = tileEntity;

		// Coal slot
		this.addSlotToContainer(new SlotSpecific(tileEntity, 0, 6, 69, IItemElectric.class));

		// Diamond
		this.addSlotToContainer(new SlotSpecific(tileEntity, 1, 15, 17, new ItemStack(Items.diamond, 1, 0)));

		// Silicon
		this.addSlotToContainer(new SlotSpecific(tileEntity, 2, 74, 46, new ItemStack(GCItems.basicItem, 1, 2)));
		this.addSlotToContainer(new SlotSpecific(tileEntity, 3, 74, 64, new ItemStack(GCItems.basicItem, 1, 2)));

		// Redstone
		this.addSlotToContainer(new SlotSpecific(tileEntity, 4, 122, 46, new ItemStack(Items.redstone, 1, 0)));

		// Optional
		ItemStack stacks[] = {new ItemStack(Items.dye, 1, 4),  new ItemStack(Items.repeater, 1, 0), new ItemStack(Blocks.redstone_torch)};
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
				else if (i == Items.diamond)
				{
					if (!this.mergeItemStack(var4, 1, 2, false))
						return null;
				}
				else if (i == GCItems.basicItem && i.getDamage(var4) == 2)
				{
					if (!this.mergeItemStack(var4, 2, 4, false))
						return null;
				}
				else if (i == Items.redstone)
				{
					if (!this.mergeItemStack(var4, 4, 5, false))
						return null;
				}
				else if (i == Items.repeater || i == new ItemStack(Blocks.redstone_torch).getItem() || i == Items.dye && i.getDamage(var4) == 4)
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
