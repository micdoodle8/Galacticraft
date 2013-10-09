package codechicken.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryCopy implements IInventory
{    
    public boolean[] accessible;
    public ItemStack[] items;
    public IInventory inv;
    
    public InventoryCopy(IInventory inv)
    {
        items = new ItemStack[inv.getSizeInventory()];
        accessible = new boolean[inv.getSizeInventory()];
        this.inv = inv;
        update();
    }
    
    public void update()
    {        
        for(int i = 0; i < items.length; i++)
        {
            ItemStack stack = inv.getStackInSlot(i);
            if(stack != null)
                items[i] = stack.copy();
        }
    }
    
    public InventoryCopy open(InventoryRange access)
    {
        int lslot = access.lastSlot();
        if(lslot > accessible.length)
        {
            boolean[] l_accessable = new boolean[lslot];
            ItemStack[] l_items = new ItemStack[lslot];
            System.arraycopy(accessible, 0, l_accessable, 0, accessible.length);
            System.arraycopy(items, 0, l_items, 0, items.length);
            accessible = l_accessable;
            items = l_items;
        }

        for(int slot : access.slots)
            accessible[slot] = true;
        return this;
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
        onInventoryChanged();
    }

    @Override
    public String getInvName()
    {
        return "copy";
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return true;
    }

    public void openChest()
    {
    }

    @Override
    public void closeChest()
    {
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }
    

    @Override
    public void onInventoryChanged()
    {
    }
    
    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return inv.isItemValidForSlot(i, itemstack);
    }
    
    @Override
    public boolean isInvNameLocalized()
    {
        return true;
    }
}
