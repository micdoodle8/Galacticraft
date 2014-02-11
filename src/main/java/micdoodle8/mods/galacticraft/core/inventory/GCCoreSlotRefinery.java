package micdoodle8.mods.galacticraft.core.inventory;

import java.lang.reflect.Field;

import micdoodle8.mods.galacticraft.core.items.GCCoreItemOilCanister;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * GCCoreSlotRefinery.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreSlotRefinery extends Slot
{
	public GCCoreSlotRefinery(IInventory par1iInventory, int par2, int par3, int par4)
	{
		super(par1iInventory, par2, par3, par4);
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		Class<?> buildCraftClass = null;

		try
		{
			if ((buildCraftClass = Class.forName("buildcraft.BuildCraftEnergy")) != null)
			{
				for (final Field f : buildCraftClass.getFields())
				{
					if (f.getName().equals("bucketOil"))
					{
						final Item item = (Item) f.get(null);

						if (par1ItemStack.itemID == item.itemID)
						{
							return true;
						}
					}
				}
			}
		}
		catch (final Throwable cnfe)
		{
		}

		if (par1ItemStack.getItem() instanceof GCCoreItemOilCanister && par1ItemStack.getItemDamage() > 0)
		{
			return true;
		}

		return false;
	}
}
