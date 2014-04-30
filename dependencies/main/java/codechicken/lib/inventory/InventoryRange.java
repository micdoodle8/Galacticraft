package codechicken.lib.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

/**
 * Inventory wrapper for unified ISided/IInventory access
 */
public class InventoryRange
{
    public IInventory inv;
    public int side;
    public ISidedInventory sidedInv;
    public int[] slots;
    
    public InventoryRange(IInventory inv, int side)
    {
        this.inv = inv;
        this.side = side;
        if(inv instanceof ISidedInventory)
        {
            sidedInv = (ISidedInventory)inv;
            slots = sidedInv.getAccessibleSlotsFromSide(side);
        }
        else
        {
            slots = new int[inv.getSizeInventory()];
            for(int i = 0; i < slots.length; i++)
                slots[i] = i;
        }
    }

    public InventoryRange(IInventory inv)
    {
        this(inv, 0);
    }
    
    public InventoryRange(IInventory inv, int fslot, int lslot)
    {
        this.inv = inv;
        slots = new int[lslot-fslot];
        for(int i = 0; i < slots.length; i++)
            slots[i] = fslot+i;
    }

    public InventoryRange(IInventory inv, InventoryRange access)
    {
        this.inv = inv;
        this.slots = access.slots;
        this.side = access.side;
        if(inv instanceof ISidedInventory)
            sidedInv = (ISidedInventory) inv;
    }

    public boolean canInsertItem(int slot, ItemStack item)
    {
        return sidedInv == null ? inv.isItemValidForSlot(slot, item) : sidedInv.canInsertItem(slot, item, side);
    }
    
    public boolean canExtractItem(int slot, ItemStack item)
    {
        return sidedInv == null ? inv.isItemValidForSlot(slot, item) : sidedInv.canExtractItem(slot, item, side);
    }

    public int lastSlot()
    {
        int last = 0;
        for(int slot : slots)
            if(slot > last)
                last = slot;
        return last;
    }
}
