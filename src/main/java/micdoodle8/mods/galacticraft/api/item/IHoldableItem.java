package micdoodle8.mods.galacticraft.api.item;

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
	public boolean shouldHoldLeftHandUp(EntityPlayer player);

	/**
	 * Self-explanatory.
	 * 
	 * Use player.inventory.getCurrentItem() to get the itemstack
	 * 
	 * @param player
	 *            the player holding the item
	 * @return true if player should hold the item above their head
	 */
	public boolean shouldHoldRightHandUp(EntityPlayer player);

	/**
	 * Used to determine if player should crouch while holding this item.
	 * 
	 * Use player.inventory.getCurrentItem() to get the itemstack
	 * 
	 * @param player
	 *            the player holding the item
	 * @return true if player should hold the item above their head
	 */
	public boolean shouldCrouch(EntityPlayer player);
}
