package micdoodle8.mods.galacticraft.API;

import net.minecraft.item.ItemStack;

/**
 * Extend this block to allow refining if the item
 */
public interface IRefinableItem
{
	public boolean canSmeltItem(ItemStack originalStack);
	
	public ItemStack getResultItem(ItemStack originalStack);
}
