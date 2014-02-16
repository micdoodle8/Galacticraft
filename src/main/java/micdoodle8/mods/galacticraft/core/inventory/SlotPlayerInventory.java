package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.items.ItemOxygenMask;
import micdoodle8.mods.galacticraft.core.items.ItemOxygenTank;
import micdoodle8.mods.galacticraft.core.items.ItemParachute;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * GCCoreSlotPlayer.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class SlotPlayerInventory extends Slot
{
	public SlotPlayerInventory(IInventory par2IInventory, int par3, int par4, int par5)
	{
		super(par2IInventory, par3, par4, par5);
	}

	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}

	@Override
	public boolean isItemValid(ItemStack itemstack)
	{
		switch (this.getSlotIndex())
		{
		case 0:
			return itemstack.getItem() instanceof ItemOxygenMask;
		case 1:
			return itemstack.getItem() == GCItems.oxygenGear;
		case 2:
			return itemstack.getItem() instanceof ItemOxygenTank;
		case 3:
			return itemstack.getItem() instanceof ItemOxygenTank;
		case 4:
			return itemstack.getItem() instanceof ItemParachute;
		case 5:
			return itemstack.getItem() == GCItems.basicItem && itemstack.getItemDamage() == 19;
		}

		return super.isItemValid(itemstack);
	}
}
