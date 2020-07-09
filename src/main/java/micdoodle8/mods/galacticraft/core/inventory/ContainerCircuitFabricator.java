package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.api.recipe.CircuitFabricatorRecipes;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCircuitFabricator;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.FurnaceResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ContainerCircuitFabricator extends Container
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.CIRCUIT_FABRICATOR)
    public static ContainerType<ContainerCircuitFabricator> TYPE;

    private final TileEntityCircuitFabricator fabricator;

    public ContainerCircuitFabricator(int containerId, PlayerInventory playerInv, TileEntityCircuitFabricator fabricator)
    {
        super(TYPE, containerId);
        this.fabricator = fabricator;

        // Energy slot
        this.addSlot(new SlotSpecific(fabricator, 0, 6, 69, IItemElectric.class));

        // Diamond
        ArrayList<ItemStack> slotContentsList = CircuitFabricatorRecipes.slotValidItems.get(0);
        this.addSlot(new SlotSpecific(fabricator, 1, 15, 17, slotContentsList.toArray(new ItemStack[slotContentsList.size()])));

        // Silicon
        slotContentsList = CircuitFabricatorRecipes.slotValidItems.get(1);
        this.addSlot(new SlotSpecific(fabricator, 2, 74, 46, slotContentsList.toArray(new ItemStack[slotContentsList.size()])));
        slotContentsList = CircuitFabricatorRecipes.slotValidItems.get(2);
        this.addSlot(new SlotSpecific(fabricator, 3, 74, 64, slotContentsList.toArray(new ItemStack[slotContentsList.size()])));

        // Redstone
        slotContentsList = CircuitFabricatorRecipes.slotValidItems.get(3);
        this.addSlot(new SlotSpecific(fabricator, 4, 122, 46, slotContentsList.toArray(new ItemStack[slotContentsList.size()])));

        // Optional
        slotContentsList = CircuitFabricatorRecipes.slotValidItems.get(4);
        this.addSlot(new SlotSpecific(fabricator, 5, 145, 20, slotContentsList.toArray(new ItemStack[slotContentsList.size()])));

        // Smelting result
        this.addSlot(new FurnaceResultSlot(playerInv.player, fabricator, 6, 152, 86));

        int slot;

        for (slot = 0; slot < 3; ++slot)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlot(new Slot(playerInv, var4 + slot * 9 + 9, 8 + var4 * 18, 110 + slot * 18));
            }
        }

        for (slot = 0; slot < 9; ++slot)
        {
            this.addSlot(new Slot(playerInv, slot, 8 + slot * 18, 168));
        }
    }

    public TileEntityCircuitFabricator getFabricator()
    {
        return fabricator;
    }

    @Override
    public boolean canInteractWith(PlayerEntity par1EntityPlayer)
    {
        return this.fabricator.isUsableByPlayer(par1EntityPlayer);
    }

    @Override
    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
        super.onCraftMatrixChanged(par1IInventory);
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift
     * clicking.
     */
    @Override
    public ItemStack transferStackInSlot(PlayerEntity par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(par1);
        final int b = this.inventorySlots.size();

        if (slot != null && slot.getHasStack())
        {
            ItemStack var4 = slot.getStack();
            var2 = var4.copy();

            if (par1 < b - 36)
            {
                if (!this.mergeItemStack(var4, b - 36, b, true))
                {
                    return ItemStack.EMPTY;
                }

                if (par1 == 6)
                {
                    slot.onSlotChange(var4, var2);
                }
            }
            else
            {
                Item i = var4.getItem();
                if (EnergyUtil.isElectricItem(i))
                {
                    if (!this.mergeItemStack(var4, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (i == Items.DIAMOND)
                {
                    if (!this.mergeItemStack(var4, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (this.isSilicon(var4))
                {
                    if (!this.mergeEven(var4, 2, 4))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (i == Items.REDSTONE)
                {
                    if (!this.mergeItemStack(var4, 4, 5, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (i == Items.REPEATER || i == new ItemStack(Blocks.REDSTONE_TORCH).getItem() || i == Items.BLUE_DYE)
                {
                    if (!this.mergeItemStack(var4, 5, 6, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (par1 < b - 9)
                {
                    if (!this.mergeItemStack(var4, b - 9, b, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.mergeItemStack(var4, b - 36, b - 9, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (var4.getCount() == 0)
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (var4.getCount() == var2.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(par1EntityPlayer, var4);
        }

        return var2;
    }

    private boolean mergeEven(ItemStack stack, int a, int b)
    {
        List<Slot> acceptSlots = new LinkedList<>();
        List<Integer> acceptQuantity = new LinkedList<>();
        int minQuantity = 64;
        int acceptTotal = 0;
        for (int i = a; i < b; i++)
        {
            Slot slot = this.inventorySlots.get(i);

            if (slot != null)
            {
                ItemStack target = slot.getStack();
                if (matchingStacks(stack, target))
                {
                    acceptSlots.add(slot);
                    int availSpace = stack.getMaxStackSize() - target.getCount();
                    acceptQuantity.add(availSpace);
                    acceptTotal += availSpace;
                    if (availSpace < minQuantity)
                    {
                        minQuantity = availSpace;
                    }
                }
            }
        }

        for (Slot slot : acceptSlots)
        {
            ItemStack target = slot.getStack();
            if (target.isEmpty())
            {
                target = stack.copy();
                target.setCount(1);
                slot.putStack(target);
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
            {
                return false;
            }

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
                if (transfer > stack.getCount())
                {
                    transfer = stack.getCount();
                }
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
                {
                    break;
                }
            }
        }

        return true;
    }

    private boolean isSilicon(ItemStack test)
    {
        for (ItemStack stack : CircuitFabricatorRecipes.slotValidItems.get(1))
        {
            if (stack.isItemEqual(test))
            {
                return true;
            }
        }
        return false;
    }

    private boolean matchingStacks(ItemStack stack, ItemStack target)
    {
        return target.isEmpty() || target.getItem() == stack.getItem() /*&& (!stack.getHasSubtypes() || stack.getMetadata() == target.getMetadata())*/ && ItemStack.areItemStackTagsEqual(stack, target) && (target.isStackable() && target.getCount() < target.getMaxStackSize());
    }
}
