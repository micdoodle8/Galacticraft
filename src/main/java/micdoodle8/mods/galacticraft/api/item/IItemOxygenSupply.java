package micdoodle8.mods.galacticraft.api.item;

import net.minecraft.item.ItemStack;

public interface IItemOxygenSupply
{
    /*
     * Returns the amount of gas that this oxygen item is able to supply
     */
	int discharge(ItemStack itemStack, int amount);

    int getOxygenStored(ItemStack theItem);
}
