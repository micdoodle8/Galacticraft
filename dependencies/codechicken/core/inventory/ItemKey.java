package codechicken.core.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.base.Objects;
import static codechicken.core.inventory.InventoryUtils.*;

public class ItemKey implements Comparable<ItemKey>
{
    public ItemStack item;
    private int hashcode = 0;
    
    public ItemKey(ItemStack k)
    {
        item = k;
    }

    public ItemKey(int id, int damage)
    {
        this(new ItemStack(id, 1, damage));
    }
    
    public ItemKey(int id, int damage, NBTTagCompound compound)
    {
        this(id, damage);
        item.setTagCompound(compound);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof ItemKey))
            return false;
        
        ItemKey k = (ItemKey)obj;
        return item.itemID == k.item.itemID &&
                actualDamage(item) == actualDamage(k.item) &&
                Objects.equal(item.stackTagCompound, k.item.stackTagCompound);
    }
    
    @Override
    public int hashCode()
    {
        return hashcode != 0 ? hashcode : (hashcode = Objects.hashCode(item.itemID, actualDamage(item), item.stackTagCompound));
    }
    
    public int compareInt(int a, int b)
    {
        return a == b ? 0 : a < b ? -1 : 1;
    }

    @Override
    public int compareTo(ItemKey o)
    {
        if(item.itemID != o.item.itemID)
            return compareInt(item.itemID, o.item.itemID);
        if(actualDamage(item) != actualDamage(o.item))
            return compareInt(actualDamage(item), actualDamage(o.item));
        return 0;
    }
}
