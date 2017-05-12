package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.items.ItemOilCanister;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
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
        if (CompatibilityManager.fieldBCoilBucket != null)
        {
            try
            {
                if (par1ItemStack.getItem() == (Item) CompatibilityManager.fieldBCoilBucket.get(null))
                {
                    return true;
                }
            }
            catch (Exception ignore)
            {
            }
        }

        return par1ItemStack.getItem() instanceof ItemOilCanister && par1ItemStack.getItemDamage() > 0;

    }
}
