package codechicken.core.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class MappedInventoryAccess implements IInventory
{
    public static interface InventoryAccessor
    {
        public boolean canAccessSlot(int slot);
    }

    public static final InventoryAccessor fullAccess = new InventoryAccessor()
    {
        public boolean canAccessSlot(int slot)
        {
            return true;
        }
    };
    
    private ArrayList<Integer> slotMap = new ArrayList<Integer>();
    private IInventory inv;
    private ArrayList<InventoryAccessor> accessors = new ArrayList<MappedInventoryAccess.InventoryAccessor>();
    
    public MappedInventoryAccess(IInventory inv, InventoryAccessor... accessors)
    {
        this.inv = inv;
        
        for(InventoryAccessor a : accessors)
            this.accessors.add(a);
        
        reset();
    }
    
    public void reset()
    {
        slotMap.clear();
        nextslot: for(int i = 0; i < inv.getSizeInventory(); i++)
        {
            for(InventoryAccessor a : accessors)
                if(!a.canAccessSlot(i))
                    continue nextslot;
            
            slotMap.add(i);
        }
    }
    
    @Override
    public int getSizeInventory()
    {
        return slotMap.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return inv.getStackInSlot(slotMap.get(slot));
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        return inv.decrStackSize(slotMap.get(slot), amount);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        return inv.getStackInSlotOnClosing(slotMap.get(slot));
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        inv.setInventorySlotContents(slotMap.get(slot), stack);
    }

    @Override
    public String getInvName()
    {
        return inv.getInvName();
    }

    @Override
    public int getInventoryStackLimit()
    {
        return inv.getInventoryStackLimit();
    }

    @Override
    public void onInventoryChanged()
    {
        inv.onInventoryChanged();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return inv.isUseableByPlayer(player);
    }

    @Override
    public void openChest()
    {
        inv.openChest();
    }

    @Override
    public void closeChest()
    {
        inv.closeChest();
    }

    public void addAccessor(InventoryAccessor accessor)
    {
        accessors.add(accessor);
        reset();
    }
    
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return inv.isItemValidForSlot(slotMap.get(slot), stack);
    }
    
    @Override
    public boolean isInvNameLocalized()
    {
        return true;
    }

    public List<InventoryAccessor> accessors()
    {
        return accessors;
    }
}
