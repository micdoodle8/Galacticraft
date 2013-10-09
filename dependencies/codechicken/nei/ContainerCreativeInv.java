package codechicken.nei;

import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotArmor;

public class ContainerCreativeInv extends Container
{
    public ContainerCreativeInv(EntityPlayer player, ExtendedCreativeInv extraInv)
    {
        InventoryPlayer invPlayer = player.inventory;
        for(int row = 0; row < 6; row++)
            for(int col = 0; col < 9; col++)
                addSlotToContainer(new Slot(extraInv, col + row * 9, 8 + col * 18, 5 + row * 18));

        for(int row = 0; row < 3; ++row)
            for(int col = 0; col < 9; ++col)
                addSlotToContainer(new Slot(invPlayer, col + row * 9 + 9, 8 + col * 18, 118 + row * 18));

        for(int col = 0; col < 9; ++col)
            addSlotToContainer(new Slot(invPlayer, col, 8 + col * 18, 176));
        
        addSlotToContainer(new SlotBlockArmor((ContainerPlayer) player.inventoryContainer, invPlayer, invPlayer.getSizeInventory() - 1, -15, 23, 0));
        for(int armourslot = 1; armourslot < 4; ++armourslot)
            addSlotToContainer(new SlotArmor((ContainerPlayer) player.inventoryContainer, invPlayer, invPlayer.getSizeInventory() - 1 - armourslot, -15, 23 + armourslot * 18, armourslot));
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotIndex)
    {
        ItemStack transferredStack = null;
        Slot slot = (Slot)inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack())
        {
            ItemStack stack = slot.getStack();
            transferredStack = stack.copy();
            
            if(stack.getItem() instanceof ItemArmor)
            {
                ItemArmor armor = (ItemArmor)stack.getItem();
                if(!getSlot(90+armor.armorType).getHasStack())
                {
                    getSlot(90+armor.armorType).putStack(transferredStack);
                    slot.putStack(null);
                    return transferredStack;
                }                
            }

            if (slotIndex < 54)
            {
                if (!this.mergeItemStack(stack, 54, 90, true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(stack, 0, 54, false))
            {
                return null;
            }

            if (stack.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return transferredStack;
    }
    
    public boolean canInteractWith(EntityPlayer var1) 
    {
        return true;
    }
}
