package micdoodle8.mods.galacticraft.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

/**
 * Implement into tile entities that do something upon being activated by a key
 */
public interface IKeyable
{
    /**
     * @return -1 for any tier, or return tier required for key activated to
     * pass
     */
    int getTierOfKeyRequired();

    /**
     * called when key of correct tier is clicked
     *
     * @param key  the key itemstack
     * @param face the block face clicked
     * @return true if something was done, false if not
     */
    boolean onValidKeyActivated(EntityPlayer player, ItemStack key, EnumFacing face);

    /**
     * called when player is not holding correct tier of key, or any key at all
     *
     * @param player
     * @return true if something was done, false if not
     */
    boolean onActivatedWithoutKey(EntityPlayer player, EnumFacing face);

    boolean canBreak();
}
