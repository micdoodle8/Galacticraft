package micdoodle8.mods.galacticraft.api.item;

import net.minecraft.item.ItemStack;

/**
 * Implement into a key Item class to allow @IKeyable tile entities to get
 * activated while holding this item
 * 
 * Nothing here (yet)
 */
public interface IKeyItem
{
	/**
	 * Gets the tier of this object
	 * 
	 * @return - The item's tier
	 */
	public int getTier(ItemStack stack);
}
