package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotRefinery extends Slot
{
    public SlotRefinery(IInventory par1iInventory, int par2, int par3, int par4)
    {
        super(par1iInventory, par2, par3, par4);
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return FluidUtil.isOilContainerAny(stack);
    }
}
