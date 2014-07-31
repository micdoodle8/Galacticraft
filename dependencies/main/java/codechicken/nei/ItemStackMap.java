package codechicken.nei;

import com.google.common.base.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static codechicken.lib.inventory.InventoryUtils.actualDamage;
import static codechicken.lib.inventory.InventoryUtils.newItemStack;
import static net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE;

/**
 * A maplike class for ItemStack keys with wildcard damage/NBT. Optimised for lookup
 */
public class ItemStackMap<T>
{
    public static class StackMetaKey
    {
        public int damage;
        public NBTTagCompound tag;

        public StackMetaKey(int damage, NBTTagCompound tag) {
            this.damage = damage;
            this.tag = tag;
        }

        public StackMetaKey(ItemStack key) {
            this(actualDamage(key), key.stackTagCompound);
        }

        public int hashCode() {
            return Objects.hashCode(damage, tag);
        }

        public boolean equals(Object o) {
            if (!(o instanceof StackMetaKey))
                return false;
            StackMetaKey t = (StackMetaKey) o;
            return damage == t.damage && Objects.equal(tag, t.tag);
        }
    }

    public static class Entry<T>
    {
        public ItemStack key;
        public T value;

        public Entry(ItemStack key, T value) {
            this.key = key;
            this.value = value;
        }
    }

    public class DetailMap
    {
        private boolean hasWildcard;
        private T wildcard;
        private HashMap<Integer, T> damageMap;
        private HashMap<NBTTagCompound, T> tagMap;
        private HashMap<StackMetaKey, T> metaMap;
        private int size;

        public T get(ItemStack key) {
            if (wildcard != null)
                return wildcard;

            if (damageMap != null) {
                T ret = damageMap.get(actualDamage(key));
                if (ret != null) return ret;
            }
            if (tagMap != null) {
                T ret = tagMap.get(key.stackTagCompound);
                if (ret != null) return ret;
            }
            if (metaMap != null)
                return metaMap.get(new StackMetaKey(key));

            return null;
        }

        public T put(ItemStack key, T value) {
            try {
                switch (getKeyType(actualDamage(key), key.stackTagCompound)) {
                    case 0:
                        if (metaMap == null) metaMap = new HashMap<StackMetaKey, T>();
                        return metaMap.put(new StackMetaKey(key), value);
                    case 1:
                        if (tagMap == null) tagMap = new HashMap<NBTTagCompound, T>();
                        return tagMap.put(key.stackTagCompound, value);
                    case 2:
                        if (damageMap == null) damageMap = new HashMap<Integer, T>();
                        return damageMap.put(actualDamage(key), value);
                    case 3:
                        T ret = wildcard;
                        wildcard = value;
                        hasWildcard = true;
                        return ret;
                }
            } finally {
                updateSize();
            }
            return null;
        }

        public T remove(ItemStack key) {
            try {
                switch (getKeyType(actualDamage(key), key.stackTagCompound)) {
                    case 0:
                        return metaMap != null ? metaMap.remove(new StackMetaKey(key)) : null;
                    case 1:
                        return tagMap != null ? tagMap.remove(key.stackTagCompound) : null;
                    case 2:
                        return damageMap != null ? damageMap.remove(actualDamage(key)) : null;
                    case 3:
                        T ret = wildcard;
                        wildcard = null;
                        hasWildcard = false;
                        return ret;
                }
            } finally {
                updateSize();
            }
            return null;
        }

        private void updateSize() {
            int newSize = (hasWildcard ? 1 : 0) +
                    (metaMap != null ? metaMap.size() : 0) +
                    (tagMap != null ? tagMap.size() : 0) +
                    (damageMap != null ? damageMap.size() : 0);

            if(newSize != size) {
                ItemStackMap.this.size += newSize-size;
                size = newSize;
            }
        }

        public void addKeys(Item item, List<ItemStack> list) {
            if (wildcard != null)
                list.add(wildcard(item));
            if (damageMap != null)
                for (int damage : damageMap.keySet())
                    list.add(newItemStack(item, 1, damage, WILDCARD_TAG));
            if (tagMap != null)
                for (NBTTagCompound tag : tagMap.keySet())
                    list.add(newItemStack(item, 1, WILDCARD_VALUE, tag));
            if (metaMap != null)
                for (StackMetaKey key : metaMap.keySet())
                    list.add(newItemStack(item, 1, key.damage, key.tag));
        }

        public void addValues(List<T> list) {
            if (wildcard != null)
                list.add(wildcard);
            if (damageMap != null)
                list.addAll(damageMap.values());
            if (tagMap != null)
                list.addAll(tagMap.values());
            if (metaMap != null)
                list.addAll(metaMap.values());
        }

        public void addEntries(Item item, List<Entry<T>> list) {
            if (wildcard != null)
                list.add(new Entry<T>(newItemStack(item, 1, WILDCARD_VALUE, WILDCARD_TAG), wildcard));
            if (damageMap != null)
                for (Map.Entry<Integer, T> entry : damageMap.entrySet())
                    list.add(new Entry<T>(newItemStack(item, 1, entry.getKey(), WILDCARD_TAG), entry.getValue()));
            if (tagMap != null)
                for (Map.Entry<NBTTagCompound, T> entry : tagMap.entrySet())
                    list.add(new Entry<T>(newItemStack(item, 1, WILDCARD_VALUE, entry.getKey()), entry.getValue()));
            if (metaMap != null)
                for (Map.Entry<StackMetaKey, T> entry : metaMap.entrySet())
                    list.add(new Entry<T>(newItemStack(item, 1, entry.getKey().damage, entry.getKey().tag), entry.getValue()));
        }
    }

    public static int getKeyType(int damage, NBTTagCompound tag) {
             int i = 0;
             if (isWildcard(damage)) i = 1;
             if (isWildcard(tag)) i |= 2;
             return i;
         }

    public static ItemStack wildcard(Item item) {
        return newItemStack(item, 1, WILDCARD_VALUE, WILDCARD_TAG);
    }

    public static boolean isWildcard(int damage) {
        return damage == WILDCARD_VALUE;
    }

    public static boolean isWildcard(NBTTagCompound tag) {
        return tag != null && tag.getBoolean("*");
    }

    public static NBTTagCompound WILDCARD_TAG;

    static {
        WILDCARD_TAG = new NBTTagCompound();
        WILDCARD_TAG.setBoolean("*", true);
    }

    private HashMap<Item, DetailMap> itemMap = new HashMap<Item, DetailMap>();
    private int size;

    public T get(ItemStack key) {
        if (key == null || key.getItem() == null)
            return null;

        DetailMap map = itemMap.get(key.getItem());
        return map == null ? null : map.get(key);
    }

    public void put(ItemStack key, T value) {
        if (key == null || key.getItem() == null)
            return;

        DetailMap map = itemMap.get(key.getItem());
        if (map == null)
            itemMap.put(key.getItem(), map = new DetailMap());
        map.put(key, value);
    }

    public void clear() {
        itemMap.clear();
    }

    public T remove(ItemStack key) {
        if (key == null || key.getItem() == null)
            return null;

        DetailMap map = itemMap.get(key.getItem());
        return map == null ? null : map.remove(key);
    }

    public List<ItemStack> keys() {
        LinkedList<ItemStack> list = new LinkedList<ItemStack>();
        for (Map.Entry<Item, DetailMap> entry : itemMap.entrySet())
            entry.getValue().addKeys(entry.getKey(), list);
        return list;
    }

    public List<T> values() {
        LinkedList<T> list = new LinkedList<T>();
        for (DetailMap map : itemMap.values())
            map.addValues(list);
        return list;
    }

    public List<Entry<T>> entries() {
        LinkedList<Entry<T>> list = new LinkedList<Entry<T>>();
        for (Map.Entry<Item, DetailMap> entry : itemMap.entrySet())
            entry.getValue().addEntries(entry.getKey(), list);
        return list;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
