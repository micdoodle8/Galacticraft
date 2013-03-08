package micdoodle8.mods.galacticraft.core.inventory;

import java.lang.reflect.Field;

import micdoodle8.mods.galacticraft.core.items.GCCoreItemOilCanister;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GCCoreSlotRefinery extends Slot
{
	public GCCoreSlotRefinery(IInventory par1iInventory, int par2, int par3, int par4) 
	{
		super(par1iInventory, par2, par3, par4);
	}
    
    public boolean isItemValid(ItemStack par1ItemStack)
    {
		Class buildCraftClass = null;
		
		try
		{
			if ((buildCraftClass = Class.forName("buildcraft.BuildCraftEnergy")) != null)
			{
				for (Field f : buildCraftClass.getFields())
				{
					if (f.getName().equals("bucketOil"))
					{
						Item item = (Item) f.get(null);
						
						if (par1ItemStack.itemID == item.itemID)
						{
							return true;
						}
					}
				}
			}
		}
		catch (Throwable cnfe)
		{
		}
		
    	if (par1ItemStack.getItem() instanceof GCCoreItemOilCanister && par1ItemStack.getItemDamage() > 0)
    	{
            return true;
    	}

        return false;
    }
}
