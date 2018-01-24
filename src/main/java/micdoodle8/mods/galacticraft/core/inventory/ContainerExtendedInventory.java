package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.items.ItemCanisterOxygenInfinite;
import micdoodle8.mods.galacticraft.core.items.ItemOxygenTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerExtendedInventory extends Container
{
    public InventoryPlayer inventoryPlayer;
    public InventoryExtended extendedInventory;

    public ContainerExtendedInventory(EntityPlayer thePlayer, InventoryExtended extendedInventory)
    {
        this.inventoryPlayer = thePlayer.inventory;
        this.extendedInventory = extendedInventory;

        int i;
        int j;

        for (i = 0; i < 3; ++i)
        {
            for (j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(thePlayer.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(thePlayer.inventory, i, 8 + i * 18, 142));
        }

        for (i = 0; i < 4; ++i)
        {
            this.addSlotToContainer(new SlotArmorGC(thePlayer, thePlayer.inventory, 39 - i, 61, 8 + i * 18, i));
        }

        this.addSlotToContainer(new SlotExtendedInventory(extendedInventory, 0, 125, 26));
        this.addSlotToContainer(new SlotExtendedInventory(extendedInventory, 1, 125, 44));
        this.addSlotToContainer(new SlotExtendedInventory(extendedInventory, 2, 116, 62));
        this.addSlotToContainer(new SlotExtendedInventory(extendedInventory, 3, 134, 62));
        this.addSlotToContainer(new SlotExtendedInventory(extendedInventory, 4, 143, 26));
        this.addSlotToContainer(new SlotExtendedInventory(extendedInventory, 5, 107, 26));

        for (i = 0; i < 4; ++i)
        {
            this.addSlotToContainer(new SlotExtendedInventory(extendedInventory, 6 + i, 79, 8 + i * 18));
        }

        this.addSlotToContainer(new SlotExtendedInventory(extendedInventory, 10, 125, 8));
    }

    @Override
    public boolean canInteractWith(EntityPlayer var1)
    {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(par1);

        if (slot != null && slot.getHasStack())
        {
            final ItemStack stack = slot.getStack();
            var2 = stack.copy();

            if (par1 >= 36)
            {
                if (!this.mergeItemStack(stack, 0, 36, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                boolean flag = false;
                for (int j = 36; j < 40; j++)
                {
                    if (((SlotArmorGC) this.inventorySlots.get(j)).isItemValid(stack))
                    {
                        if (!this.mergeOneItem(stack, j, j + 1, false))
                        {
                            return ItemStack.EMPTY;
                        }
                        flag = true;
                        break;
                    }
                }

                if (!flag)
                {
                    if (stack.getItem() instanceof ItemOxygenTank || stack.getItem() instanceof ItemCanisterOxygenInfinite)
                    {
                        if (!this.mergeOneItem(stack, 42, 44, false))
                        {
                            return ItemStack.EMPTY;
                        }
                        flag = true;
                    }
                    else
                    {
                        for (int j = 40; j < 51; j++)
                        {
                            if (((SlotExtendedInventory) this.inventorySlots.get(j)).isItemValid(stack))
                            {
                                if (!this.mergeOneItem(stack, j, j + 1, false))
                                {
                                    return ItemStack.EMPTY;
                                }
                                flag = true;
                                break;
                            }
                        }
                    }
                }

                if (!flag)
                {
                    if (par1 < 27)
                    {
                        if (!this.mergeItemStack(stack, 27, 36, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!this.mergeItemStack(stack, 0, 27, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (stack.getCount() == 0)
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (stack.getCount() == var2.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(par1EntityPlayer, stack);
        }

        return var2;
    }

    protected boolean mergeOneItem(ItemStack par1ItemStack, int par2, int par3, boolean par4)
    {
        boolean flag1 = false;
        if (par1ItemStack.getCount() > 0)
        {
            Slot slot;
            ItemStack slotStack;

            for (int k = par2; k < par3; k++)
            {
                slot = this.inventorySlots.get(k);
                slotStack = slot.getStack();

                if (slotStack.isEmpty())
                {
                    ItemStack stackOneItem = par1ItemStack.copy();
                    stackOneItem.setCount(1);
                    par1ItemStack.shrink(1);
                    slot.putStack(stackOneItem);
                    slot.onSlotChanged();
                    flag1 = true;
                    break;
                }
            }
        }

        return flag1;
    }
}
