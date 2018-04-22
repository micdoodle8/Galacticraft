package buildcraft.api.inventory;

import net.minecraft.item.ItemStack;

import net.minecraftforge.items.IItemHandler;

/** A type of {@link IItemHandler} that has a single valid stack per slot, as specified by {@link #getFilter(int)}. */
public interface IItemHandlerFiltered extends IItemHandler {

    /** @param slot the slot to test
     * @return The filter in that slot. Will be {@link ItemStack#EMPTY} if this is not filtered to a single item (for
     *         example if this will match against a few stacks, or nothing is allowed). Will be equal to
     *         {@link #getStackInSlot(int)} if the slot currently contains an item. */
    default ItemStack getFilter(int slot) {
        return getStackInSlot(slot);
    }
}
