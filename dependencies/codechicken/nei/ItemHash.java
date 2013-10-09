package codechicken.nei;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemHash implements Comparable<ItemHash>
{
    public ItemHash(int itemID, int itemDamage, NBTTagCompound compound)
    {
        item = (short) itemID;
        damage = (short) itemDamage;
        moreinfo = compound;
    }
    
    public ItemHash(ItemStack stack)
    {
        item = (short) stack.itemID;
        damage = (short) stack.getItemDamage();
        moreinfo = stack.stackTagCompound;
    }
    
    public ItemHash(int itemID)
    {
        this(itemID, -1);
    }
    
    public ItemHash(int itemID, int itemDamage)
    {
        this(itemID, itemDamage, null);
    }

    public boolean equals(Object obj)
    {
        if(obj instanceof ItemHash)
        {
            ItemHash hash = (ItemHash)obj;
            return hash.item == item && 
                    (hash.damage == damage || hash.damage == -1 || damage == -1) && 
                    (moreinfo == hash.moreinfo || (moreinfo != null && moreinfo.equals(hash.moreinfo)));
        }
        return false;
    }
    
    public int hashCode()
    {
        return item;
    }
    
    public int compareTo(ItemHash o)
    {
        if(o.item != item)return Integer.valueOf(item).compareTo((int) o.item);
        if(o.damage != damage)return Integer.valueOf(damage).compareTo((int) o.damage);
        return 0;
    }
    
    public ItemStack toStack()
    {
        ItemStack stack = new ItemStack(item, 1, damage);
        stack.stackTagCompound = moreinfo;
        return stack;
    }
    
    public short item;
    public short damage;
    public NBTTagCompound moreinfo;
}
