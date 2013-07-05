package micdoodle8.mods.galacticraft.API;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Implement into an item class to allow player to hold it above their head
 */
public interface IHoldableItem
{
    /**
     * Self-explanatory.
     * 
     * Use player.inventory.getCurrentItem() to get the itemstack
     * 
     * @param player
     *            the player holding the item
     * @return true if player should hold the item above their head
     */
    public boolean shouldHoldAboveHead(EntityPlayer player);
}
