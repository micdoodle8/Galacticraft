package micdoodle8.mods.galacticraft.API;

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
     * @return - The tier
     */
    public int getTier(ItemStack stack);
}
