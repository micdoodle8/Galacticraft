package micdoodle8.mods.galacticraft.api.item;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Implement into an item class to allow player to hold it above their head
 */
public interface IHoldableItem
{
    /**
     * Self-explanatory.
     * <p/>
     * Use player.inventory.getCurrentItem() to get the itemstack
     *
     * @param player the player holding the item
     * @return true if player should hold the item above their head
     */
    boolean shouldHoldLeftHandUp(EntityPlayer player);

    /**
     * Self-explanatory.
     * <p/>
     * Use player.inventory.getCurrentItem() to get the itemstack
     *
     * @param player the player holding the item
     * @return true if player should hold the item above their head
     */
    boolean shouldHoldRightHandUp(EntityPlayer player);

    /**
     * Used to determine if player should crouch while holding this item.
     * <p/>
     * Use player.inventory.getCurrentItem() to get the itemstack
     *
     * @param player the player holding the item
     * @return true if player should hold the item above their head
     */
    boolean shouldCrouch(EntityPlayer player);
}
