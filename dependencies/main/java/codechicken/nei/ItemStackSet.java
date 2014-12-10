package codechicken.nei;

import codechicken.nei.api.ItemFilter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemStackSet implements ItemFilter
{
    ItemStackMap<Object> underlying = new ItemStackMap<Object>();

    public ItemStackSet with(ItemStack... items) {
        for(ItemStack item : items)
            add(item);
        return this;
    }

    public ItemStackSet with(Item... items) {
        for(Item item : items)
            add(ItemStackMap.wildcard(item));
        return this;
    }

    public ItemStackSet with(Block... blocks) {
        for(Block block : blocks)
            add(ItemStackMap.wildcard(Item.getItemFromBlock(block)));
        return this;
    }

    public void add(ItemStack item) {
        underlying.put(item, new Object());
    }

    public boolean remove(ItemStack item) {
        return underlying.remove(item) != null;
    }

    public boolean contains(ItemStack item) {
        return underlying.get(item) != null;
    }

    public boolean containsAll(Item item) {
        return underlying.get(ItemStackMap.wildcard(item)) != null;
    }

    public List<ItemStack> values() {
        return underlying.keys();
    }

    public void clear() {
        underlying.clear();
    }

    @Override
    public boolean matches(ItemStack item) {
        return contains(item);
    }

    public int size() {
        return underlying.size();
    }

    public boolean isEmpty() {
        return underlying.isEmpty();
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
