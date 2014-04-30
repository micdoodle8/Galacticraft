package codechicken.core.inventory;

import codechicken.lib.inventory.InventoryUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotDummy extends SlotHandleClicks
{
    public final int stackLimit;
    public SlotDummy(IInventory inv, int slot, int x, int y)
    {
        this(inv, slot, x, y, 64);
    }
    
    public SlotDummy(IInventory inv, int slot, int x, int y, int limit)
    {
        super(inv, slot, x, y);
        stackLimit = limit;
    }
    
    @Override
    public ItemStack slotClick(ContainerExtended container, EntityPlayer player, int button, int modifier)
    {
        ItemStack held = player.inventory.getItemStack();
        boolean shift = modifier == 1;
        slotClick(held, button, shift);
        return null;
    }
    
    public void slotClick(ItemStack held, int button, boolean shift)
    {
        ItemStack tstack = getStack();
        if(held != null && (tstack == null || !InventoryUtils.canStack(held, tstack)))
        {
            int quantity = Math.min(held.stackSize, stackLimit);
            if(shift)
                quantity = Math.min(stackLimit, held.getMaxStackSize()*16);
            if(button == 1)
                quantity = 1;
            putStack(InventoryUtils.copyStack(held, quantity));
        }
        else if(tstack != null)
        {
            int inc;
            if(held != null)
            {
                inc = button == 1 ? -held.stackSize : held.stackSize;
                if(shift)
                    inc *= 16;
            }
            else
            {
                inc = button == 1 ? -1 : 1;
                if(shift)
                    inc *= 16;
            }
            int quantity = tstack.stackSize+inc;
            if(quantity <= 0)
                putStack(null);
            else
                putStack(InventoryUtils.copyStack(tstack, quantity));
        }
    }
    
    @Override
    public void putStack(ItemStack stack)
    {
        if(stack != null && stack.stackSize > stackLimit)
            stack = InventoryUtils.copyStack(stack, stackLimit);
        super.putStack(stack);
    }
}
