package micdoodle8.mods.galacticraft.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Implement into tile entities that do something upon being activated by a key
 */
public interface IKeyable
{
	/**
	 * @return -1 for any tier, or return tier required for key activated to
	 *         pass
	 */
	public int getTierOfKeyRequired();

	/**
	 * called when key of correct tier is clicked
	 * 
	 * @param key
	 *            the key itemstack
	 * @param face
	 *            the block face clicked
	 * @return true if something was done, false if not
	 */
	public boolean onValidKeyActivated(EntityPlayer player, ItemStack key, int face);

	/**
	 * called when player is not holding correct tier of key, or any key at all
	 * 
	 * @param key
	 *            the key itemstack
	 * @return true if something was done, false if not
	 */
	public boolean onActivatedWithoutKey(EntityPlayer player, int face);

	public boolean canBreak();
}
