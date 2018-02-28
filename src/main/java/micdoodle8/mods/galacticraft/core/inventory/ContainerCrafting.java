package micdoodle8.mods.galacticraft.core.inventory;

import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Lists;

import micdoodle8.mods.galacticraft.core.tile.TileEntityCrafting;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerCrafting extends Container
{
    public TileEntityCrafting tileEntity;
    public PersistantInventoryCrafting craftMatrix;
    public IInventory craftResult = new InventoryCraftResult();
    private ItemStack[] memory = new ItemStack[9];

    public ContainerCrafting(InventoryPlayer playerInventory, IInventory tile)
    {
        this.tileEntity = (TileEntityCrafting) tile;
        this.craftMatrix = tileEntity.craftMatrix;
        this.craftMatrix.eventHandler = this;
        this.addSlotToContainer(new SlotCraftingMemory(playerInventory.player, this.craftMatrix, this.craftResult, 0, 124, 35, this.tileEntity));

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
    public List<ItemStack> getInventory()
    {
        List<ItemStack> list = Lists.<ItemStack>newArrayList();

        for (int i = 0; i < this.inventorySlots.size(); ++i)
        {
            list.add(((Slot)this.inventorySlots.get(i)).getStack());
        }
        
        //Override this method to trick vanilla networking into carrying our memory at end of its packets 
        for (int i = 0; i < 9; i++)
            list.add(this.tileEntity.memory[i]);

        return list;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void putStacksInSlots(ItemStack[] stacks)
    {
        for (int i = 0; i < stacks.length; ++i)
        {
            if (i < 46)
                this.getSlot(i).putStack(stacks[i]);
            else if (i < 55)
                //Read memory clientside from the end of the vanilla packet, see getInventory() 
                this.tileEntity.memory[i - 46] = stacks[i];
        }
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn)
    {
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.tileEntity.getWorld()));
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        craftMatrix.eventHandler = null;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileEntity.isUseableByPlayer(playerIn);
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack() && slot.getStack().stackSize > 0)
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 0)
            {
                if (!this.mergeItemStack(itemstack1, 10, 46, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index < 10)
            {
                if (!this.mergeItemStack(itemstack1, 10, 46, false))
                {
                    return null;
                }
            }
            else if (this.matchesCrafting(itemstack1))
            {
                if (!this.mergeToCrafting(itemstack1, false))
                {
                    return null;
                }
            }
            else if (index >= 10 && index < 37)
            {
                if (!this.mergeItemStack(itemstack1, 37, 46, false))
                {
                    return null;
                }
            }
            else if (index >= 37 && index < 46)
            {
                if (!this.mergeItemStack(itemstack1, 10, 37, false))
                {
                    return null;
                }
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(playerIn, itemstack1);
        }

        return itemstack;
    }

    private boolean mergeToCrafting(ItemStack stack, boolean b)
    {
        List<Slot> acceptSlots = new LinkedList<>();
        List<Integer> acceptQuantity = new LinkedList<>();
        int minQuantity = 64;
        int acceptTotal = 0;
        for (int i = 1; i < 10; i++)
        {
            Slot slot = (Slot)this.inventorySlots.get(i);

            if (slot != null)
            {
                ItemStack target = slot.getStack();
                ItemStack target2 = this.memory[i - 1];
                if (target2 == null) continue;
                if (target == null)
                {
                    target = target2;
                }
                if (matchingStacks(stack, target))
                {
                    acceptSlots.add(slot);
                    int availSpace = target.getMaxStackSize() - target.stackSize;
                    acceptQuantity.add(availSpace);
                    acceptTotal += availSpace;
                    if (availSpace < minQuantity) minQuantity = availSpace;
                }
            }
        }
        
        //The stack more than exceeds what the crafting inventory requires
        if (stack.stackSize >= acceptTotal)
        {
            if (acceptTotal == 0)
                return false;
            
            for (Slot slot : acceptSlots)
            {
                ItemStack target = slot.getStack();
                if (target == null)
                {
                    ItemStack target2 = this.memory[slot.slotNumber - 1];
                    if (target2 == null) continue;
                    target = target2.copy();
                    this.craftMatrix.setInventorySlotContents(slot.slotNumber - 1, target);
                }
                stack.stackSize -= target.getMaxStackSize() - target.stackSize;
                target.stackSize = target.getMaxStackSize();
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
        if (stack.stackSize < uneven)
        {
            do
            {
                Slot neediest = null;
                int smallestStack = 64; 
                for (Slot slot : acceptSlots)
                {
                    ItemStack target = slot.getStack();
                    if (target == null)
                    {
                        ItemStack target2 = this.memory[slot.slotNumber - 1];
                        if (target2 == null) continue;
                        target = target2.copy();
                        this.craftMatrix.setInventorySlotContents(slot.slotNumber - 1, target);
                    }
                    if (target.stackSize < smallestStack)
                    {
                        smallestStack = target.stackSize;
                        neediest = slot;
                    }
                }
                neediest.getStack().stackSize++;
            }
            while (--stack.stackSize > 0);
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
                if (target == null)
                {
                    ItemStack target2 = this.memory[slot.slotNumber - 1];
                    if (target2 == null) continue;
                    target = target2.copy();
                    this.craftMatrix.setInventorySlotContents(slot.slotNumber - 1, target);
                }
                stack.stackSize -= targetSize - target.stackSize;
                acceptTotal -= targetSize - target.stackSize;
                target.stackSize = targetSize;
                slot.onSlotChanged();
            }
        }
        
        //Spread the remaining stack over all slots evenly
        int average = stack.stackSize / acceptSlots.size();
        int modulus = stack.stackSize - average * acceptSlots.size();
        for (Slot slot : acceptSlots)
        {
            if (slot != null)
            {
                ItemStack target = slot.getStack();
                ItemStack target2 = this.memory[slot.slotNumber - 1];
                if (target2 == null) continue;
                if (target == null)
                {
                    target = target2.copy();
                    this.craftMatrix.setInventorySlotContents(slot.slotNumber - 1, target);
                }
                int transfer = average;
                if (modulus > 0)
                {
                    transfer++;
                    modulus--;
                }
                stack.stackSize -= transfer;
                target.stackSize += transfer;
                if (target.stackSize > target.getMaxStackSize())
                {
                    GCLog.info("Shift clicking - slot " + slot.slotNumber + " wanted more than it could accept:" + target.stackSize);
                    stack.stackSize += target.stackSize - target.getMaxStackSize();
                    target.stackSize = target.getMaxStackSize();
                }
                if (stack.stackSize < 0)
                {
                    target.stackSize += stack.stackSize;
                    stack.stackSize = 0;
                }
                slot.onSlotChanged();
                if (stack.stackSize == 0)
                    break;
            }
        }
    
        return true;
    }

    private boolean matchesCrafting(ItemStack itemstack1)
    {
        if (this.tileEntity.overrideMemory(itemstack1, this.memory))
            return true;

        for (int i = 0; i < 9; i++)
        {
           if (matchingStacks(itemstack1, this.tileEntity.getMemory(i)) && (this.craftMatrix.getStackInSlot(i) == null || this.craftMatrix.getStackInSlot(i).stackSize < itemstack1.getMaxStackSize()))
           {
               for (int j = 0; j < 9; j++)
               {
                   this.memory[j] = this.tileEntity.getMemory(j);
               }
               return true;
           }
        }
        return false;
    }
    
    private boolean matchingStacks(ItemStack stack, ItemStack target)
    {
        return target != null && target.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == target.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, target) && (target.isStackable() && target.stackSize < target.getMaxStackSize() || target.stackSize == 0);
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