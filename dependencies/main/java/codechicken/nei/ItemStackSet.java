package codechicken.nei;

import codechicken.lib.item.filtering.IItemFilter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemStackSet extends ItemStackMap<ItemStack> implements IItemFilter {
    public ItemStackSet with(ItemStack... items) {
        for (ItemStack item : items) {
            add(item);
        }
        return this;
    }

    public ItemStackSet with(Item... items) {
        for (Item item : items) {
            add(ItemStackMap.wildcard(item));
        }
        return this;
    }

    public ItemStackSet with(Block... blocks) {
        for (Block block : blocks) {
            add(ItemStackMap.wildcard(Item.getItemFromBlock(block)));
        }
        return this;
    }

    public void add(ItemStack item) {
        put(item, item);
    }

    public boolean contains(ItemStack item) {
        return get(item) != null;
    }

    public boolean containsAll(Item item) {
        return get(ItemStackMap.wildcard(item)) != null;
    }

    @Override
    public boolean matches(ItemStack item) {
        return contains(item);
    }

    public static ItemStackSet of(Block... blocks) {
        return new ItemStackSet().with(blocks);
    }

    public static ItemStackSet of(Item... items) {
        return new ItemStackSet().with(items);
    }

    public static ItemStackSet of(ItemStack... items) {
        return new ItemStackSet().with(items);
    }
}
