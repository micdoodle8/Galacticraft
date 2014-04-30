package codechicken.lib.inventory;

import com.google.common.base.Objects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import static codechicken.lib.inventory.InventoryUtils.actualDamage;

/**
 * Comparable ItemStack with a hashCode implementation.
 */
public class ItemKey implements Comparable<ItemKey>
{
    public ItemStack stack;
    private int hashcode = 0;

    public ItemKey(ItemStack k) {
        stack = k;
    }

    public ItemKey(Item item, int damage) {
        this(new ItemStack(item, 1, damage));
    }

    public ItemKey(Item item, NBTTagCompound tag) {
        this(item, OreDictionary.WILDCARD_VALUE, tag);
    }

    public ItemKey(Item item, int damage, NBTTagCompound tag) {
        this(item, damage);
        stack.setTagCompound(tag);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemKey))
            return false;

        ItemKey k = (ItemKey) obj;
        return stack.getItem() == k.stack.getItem() &&
                actualDamage(stack) == actualDamage(k.stack) &&
                Objects.equal(stack.stackTagCompound, k.stack.stackTagCompound);
    }

    @Override
    public int hashCode() {
        return hashcode != 0 ? hashcode : (hashcode = Objects.hashCode(stack.getItem(), actualDamage(stack), stack.stackTagCompound));
    }

    public int compareInt(int a, int b) {
        return a == b ? 0 : a < b ? -1 : 1;
    }

    @Override
    public int compareTo(ItemKey o) {
        if (stack.getItem() != o.stack.getItem())
            return compareInt(Item.getIdFromItem(stack.getItem()), Item.getIdFromItem(o.stack.getItem()));
        if (actualDamage(stack) != actualDamage(o.stack))
            return compareInt(actualDamage(stack), actualDamage(o.stack));
        return 0;
    }
}
