package micdoodle8.mods.galacticraft.api.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;

/**
 * An interface for other mods to access the Galacticraft
 * extended inventory slots.
 * <p>
 * (All normal IInventory methods will work)
 */
public interface IInventoryGC extends IInventory
{
    /**
     * Drop only the Galacticraft items from the player's inventory.
     *
     * @param player
     */
    void dropExtendedItems(PlayerEntity player);

    /**
     * Make the implementing inventory a copy of the specified extended inventory.
     *
     * @param playerInv The inventory to copy
     */
    void copyInventory(IInventoryGC playerInv);
}
