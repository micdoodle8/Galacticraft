package micdoodle8.mods.galacticraft.api.item;

import net.minecraft.item.ItemStack;

public interface IItemThermal
{
    /**
     * The thermal strength of this armor. Value for first tier thermal padding is 1.
     */
    int getThermalStrength();

    /**
     * Returns whether the provided itemstack is valid for the armor slot:
     *
     * @param stack
     *  The item to test
     * @param armorSlot
     *  0 - helmet
     *  1 - chestplate
     *  2 - leggings
     *  3 - boots
     */
    boolean isValidForSlot(ItemStack stack, int armorSlot);
}
