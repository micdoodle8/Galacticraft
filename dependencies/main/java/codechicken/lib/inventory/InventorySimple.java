package codechicken.lib.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Simple IInventory implementation with an array of items, name and maximum stack size
 */
public class InventorySimple implements IInventory
{
    public ItemStack[] items;
    public int limit;
    public String name;
    
    public InventorySimple(ItemStack[] items, int limit, String name)
    {
        this.items = items;
        this.limit = limit;
        this.name = name;
    }
    
    public InventorySimple(ItemStack[] items, String name)
    {
        this(items, 64, name);
    }
    
    public InventorySimple(ItemStack[] items, int limit)
    {
        this(items, limit, "inv");
    }
    
    public InventorySimple(ItemStack[] items)
    {
        this(items, 64, "inv");
    }

    public InventorySimple(int size, int limit, String name)
    {
        this(new ItemStack[size], limit, name);
    }
    
    public InventorySimple(int size, int limit)
    {
        this(size, limit, "inv");
    }
    
    public InventorySimple(int size, String name)
    {
        this(size, 64, name);
    }
    
    public InventorySimple(int size)
    {
        this(size, 64, "inv");
    }

    @Override
    public int getSizeInventory()
    {
        return items.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return items[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        return InventoryUtils.decrStackSize(this, slot, amount);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        return InventoryUtils.getStackInSlotOnClosing(this, slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        items[slot] = stack;
        markDirty();
    }

    @Override
    public String getInventoryName()
    {
        return name;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return limit;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return true;
    }

    @Override
    public void openInventory()
    {
    }

    @Override
    public void closeInventory()
    {
    }
    
    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return true;
    }
    
    @Override
    public boolean hasCustomInventoryName()
    {
        return true;
    }
    
    @Override
    public void markDirty()
    {
    }
}
