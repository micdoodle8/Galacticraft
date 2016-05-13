package micdoodle8.mods.galacticraft.api.item;

import net.minecraft.item.ItemStack;

public interface IItemOxygenSupply
{
    /*
     * Returns the amount of gas that this oxygen item is able to supply
     */
	public float discharge(ItemStack itemStack, float amount);

    public int getOxygenStored(ItemStack theItem);
}
