package micdoodle8.mods.galacticraft.core.inventory;

import java.lang.reflect.Field;

import micdoodle8.mods.galacticraft.core.items.ItemOilCanister;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotRefinery extends Slot
{
	public SlotRefinery(IInventory par1iInventory, int par2, int par3, int par4)
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

						if (par1ItemStack.getItem() == item)
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

        return par1ItemStack.getItem() instanceof ItemOilCanister && par1ItemStack.getItemDamage() > 0;

    }
}
