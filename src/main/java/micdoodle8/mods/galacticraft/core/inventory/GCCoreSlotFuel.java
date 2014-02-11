package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.items.GCCoreItemFuelCanister;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * GCCoreSlotFuel.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreSlotFuel extends Slot
{
	public GCCoreSlotFuel(IInventory par1iInventory, int par2, int par3, int par4)
	{
		super(par1iInventory, par2, par3, par4);
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		if (par1ItemStack.getItem() instanceof GCCoreItemFuelCanister && par1ItemStack.getItemDamage() > 0)
		{
			return true;
		}

		return false;
	}
}
