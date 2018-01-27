package micdoodle8.mods.galacticraft.api.item;

import net.minecraft.item.ItemStack;

/**
 * Interface version of micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase
 * For use in electrical ItemArmor, or other items which must extend different classes
 */
public interface IItemElectricBase extends IItemElectric
{
    float getMaxTransferGC(ItemStack itemStack);
}
