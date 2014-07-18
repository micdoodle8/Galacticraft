package codechicken.nei;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemStackSet
{
    ItemStackMap<Object> underlying = new ItemStackMap<Object>();

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
        return underlying.itemMap.containsKey(item) && underlying.itemMap.get(item).wildcard != null;
    }

    public List<ItemStack> values() {
        return underlying.keys();
    }

    public void clear() {
        underlying.clear();
    }
}
