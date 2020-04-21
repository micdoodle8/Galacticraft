package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.tile.TileEntityCrafting;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.NonNullList;

import java.util.LinkedList;
import java.util.List;

public class ContainerCrafting extends Container
{
    public TileEntityCrafting tileEntity;
    public PersistantInventoryCrafting craftMatrix;
    public IInventory craftResult = new InventoryCraftResult();
    private NonNullList<ItemStack> memory = NonNullList.withSize(9, ItemStack.EMPTY);

    public ContainerCrafting(InventoryPlayer playerInventory, IInventory tile)
    {
        this.tileEntity = (TileEntityCrafting) tile;
        this.craftMatrix = tileEntity.craftMatrix;
        this.craftMatrix.eventHandler = this;
        this.addSlotToContainer(new SlotCraftingMemory(playerInventory.player, this.craftMatrix, this.craftResult, 0, 124, 33, this.tileEntity));

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 3; ++j)
            {
                this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }

        for (int k = 0; k < 3; ++k)
        {
            for (int i1 = 0; i1 < 9; ++i1)
            {
                this.addSlotToContainer(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }

        for (int l = 0; l < 9; ++l)
        {
            this.addSlotToContainer(new Slot(playerInventory, l, 8 + l * 18, 142));
        }

        this.onCraftMatrixChanged(this.craftMatrix);
    }
    
    @Override
    public NonNullList<ItemStack> getInventory()
    {
        NonNullList<ItemStack> list = NonNullList.create();

        for (int i = 0; i < this.inventorySlots.size(); ++i)
        {
            list.add(this.inventorySlots.get(i).getStack());
        }
        
        //Override this method to trick vanilla networking into carrying our memory at end of its packets 
        for (int i = 0; i < 9; i++)
            list.add(this.tileEntity.memory.get(i));

        return list;
    }

    @Override
    public void setAll(List<ItemStack> list)
    {
        for (int i = 0; i < list.size(); ++i)
        {
            if (i < 46)
                this.getSlot(i).putStack(list.get(i));
            else if (i < 55)
            	//Read memory clientside from the end of the vanilla packet, see getInventory() 
                this.tileEntity.memory.set(i - 46, list.get(i));
        }
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn)
    {
        this.craftResult.setInventorySlotContents(0, CraftingManager.findMatchingResult(this.craftMatrix, this.tileEntity.getWorld()));
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        if (!playerIn.world.isRemote)
        {
            craftMatrix.eventHandler = null;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileEntity.isUsableByPlayer(playerIn);
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack() && !slot.getStack().isEmpty())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 0)
            {
                if (!this.mergeItemStack(itemstack1, 10, 46, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index < 10)
            {
                if (!this.mergeItemStack(itemstack1, 10, 46, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (this.matchesCrafting(itemstack1))
            {
                if (!this.mergeToCrafting(itemstack1, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 10 && index < 37)
            {
                if (!this.mergeItemStack(itemstack1, 37, 46, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 37 && index < 46)
            {
                if (!this.mergeItemStack(itemstack1, 10, 37, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    private boolean mergeToCrafting(ItemStack stack, boolean b)
    {
        if (stack.isEmpty())
        {
            return false;
        }
        List<Slot> acceptSlots = new LinkedList<>();
        List<Integer> acceptQuantity = new LinkedList<>();
        int minQuantity = 64;
        int acceptTotal = 0;
        for (int i = 1; i < 10; i++)
        {
            Slot slot = this.inventorySlots.get(i);

            if (slot != null)
            {
                ItemStack target = slot.getStack();
                ItemStack target2 = this.memory.get(i - 1);
                if (target2.isEmpty()) continue;
                if (target.isEmpty())
                {
                    target = target2.copy();
                }
                if (matchingStacks(stack, target))
                {
                    acceptSlots.add(slot);
                    int availSpace = target.getMaxStackSize() - target.getCount();
                    acceptQuantity.add(availSpace);
                    acceptTotal += availSpace;
                    if (availSpace < minQuantity) minQuantity = availSpace;
                }
            }
        }
        
        //First fill any empty slots
        for (Slot slot : acceptSlots)
        {
            ItemStack target = slot.getStack();
            if (target.isEmpty())
            {
                ItemStack target2 = this.memory.get(slot.slotNumber - 1);
                this.craftMatrix.setInventorySlotContents(slot.slotNumber - 1, target2.copy());
                stack.shrink(1);
                if (stack.isEmpty())
                {
                    return false;
                }
            }
        }        
        
        //The stack more than exceeds what the crafting inventory requires
        if (stack.getCount() >= acceptTotal)
        {
            if (acceptTotal == 0)
                return false;
            
            for (Slot slot : acceptSlots)
            {
                ItemStack target = slot.getStack();
                stack.shrink(target.getMaxStackSize() - target.getCount());
                target.setCount(target.getMaxStackSize());
                slot.onSlotChanged();
            }
            return true;
        }
        
        int uneven = 0;
        for (int q : acceptQuantity)
        {
            uneven += q - minQuantity;
        }
        
        //Use the whole stack to try to even up the neediest slots
        if (stack.getCount() <= uneven)
        {
            do
            {
                Slot neediest = null;
                int smallestStack = 64; 
                for (Slot slot : acceptSlots)
                {
                    ItemStack target = slot.getStack();
                    if (target.getCount() < smallestStack)
                    {
                        smallestStack = target.getCount();
                        neediest = slot;
                    }
                }
                neediest.getStack().grow(1);
                stack.shrink(1);
            }
            while (!stack.isEmpty());
            for (Slot slot : acceptSlots)
            {
                slot.onSlotChanged();
            }
            return true;
        }

        //Use some of the stack to even things up
        if (uneven > 0)
        {
            int targetSize = stack.getMaxStackSize() - minQuantity;
            for (Slot slot : acceptSlots)
            {
                ItemStack target = slot.getStack();
                stack.shrink(targetSize - target.getCount());
                acceptTotal -= targetSize - target.getCount();
                target.setCount(targetSize);
                slot.onSlotChanged();
            }
        }
        
        //Spread the remaining stack over all slots evenly
        int average = stack.getCount() / acceptSlots.size();
        int modulus = stack.getCount() - average * acceptSlots.size();
        for (Slot slot : acceptSlots)
        {
            if (slot != null)
            {
                ItemStack target = slot.getStack();
                int transfer = average;
                if (modulus > 0)
                {
                    transfer++;
                    modulus--;
                }
                if (transfer > stack.getCount()) transfer = stack.getCount();
                stack.shrink(transfer);
                target.grow(transfer);
                if (target.getCount() > target.getMaxStackSize())
                {
                    GCLog.info("Shift clicking - slot " + slot.slotNumber + " wanted more than it could accept:" + target.getCount());
                    stack.grow(target.getCount() - target.getMaxStackSize());
                    target.setCount(target.getMaxStackSize());
                }
                slot.onSlotChanged();
                if (stack.isEmpty())
                    break;
            }
        }
    
        return true;
    }

    private boolean matchesCrafting(ItemStack itemstack1)
    {
        if (this.tileEntity.overrideMemory(itemstack1, this.memory))
        {
            return true;
        }

        for (int i = 0; i < 9; i++)
        {
           if (matchingStacks(itemstack1, this.tileEntity.getMemory(i)) && (this.craftMatrix.getStackInSlot(i).isEmpty() || this.craftMatrix.getStackInSlot(i).getCount() < itemstack1.getMaxStackSize()))
           {
               for (int j = 0; j < 9; j++)
               {
                   this.memory.set(j, this.tileEntity.getMemory(j));
               }
               return true;
           }
        }
        return false;
    }
    
    private boolean matchingStacks(ItemStack stack, ItemStack target)
    {
        return !target.isEmpty() && target.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == target.getMetadata()) && RecipeUtil.areItemStackTagsEqual(stack, target) && (!target.isStackable() || target.getCount() < target.getMaxStackSize());
    }

    /**
     * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in
     * is null for the initial slot that was double-clicked.
     */
    @Override
    public boolean canMergeSlot(ItemStack stack, Slot p_94530_2_)
    {
        return p_94530_2_.inventory != this.craftResult && super.canMergeSlot(stack, p_94530_2_);
    }
}