package micdoodle8.mods.galacticraft.core.inventory;

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

        this.addSlotToContainer(new SlotExtendedInventory(extendedInventory, 0, 106 + 19, 17));
        this.addSlotToContainer(new SlotExtendedInventory(extendedInventory, 1, 106 + 19, 35));
        this.addSlotToContainer(new SlotExtendedInventory(extendedInventory, 2, 106 + 19 - 9, 53));
        this.addSlotToContainer(new SlotExtendedInventory(extendedInventory, 3, 106 + 19 + 9, 53));
        this.addSlotToContainer(new SlotExtendedInventory(extendedInventory, 4, 124 + 19, 17));
        this.addSlotToContainer(new SlotExtendedInventory(extendedInventory, 5, 106 + 1, 17));

        for (i = 0; i < 4; ++i)
        {
            this.addSlotToContainer(new SlotExtendedInventory(extendedInventory, 6 + i, 79, 8 + i * 18));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer var1)
    {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
    {
        ItemStack var2 = null;
        final Slot slot = (Slot) this.inventorySlots.get(par1);

        if (slot != null && slot.getHasStack())
        {
            final ItemStack stack = slot.getStack();
            var2 = stack.copy();

            if (par1 >= 36)
            {
                if (!this.mergeItemStack(stack, 0, 36, true))
                {
                    return null;
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
                            return null;
                        }
                        flag = true;
                        break;
                    }
                }

                if (!flag)
                {
                    if (stack.getItem() instanceof ItemOxygenTank)
                    {
                        if (!this.mergeOneItem(stack, 42, 44, false))
                        {
                            return null;
                        }
                        flag = true;
                    }
                    else
                    {
                        for (int j = 40; j < 50; j++)
                        {
                            if (((SlotExtendedInventory) this.inventorySlots.get(j)).isItemValid(stack))
                            {
                                if (!this.mergeOneItem(stack, j, j + 1, false))
                                {
                                    return null;
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
                            return null;
                        }
                    }
                    else if (!this.mergeItemStack(stack, 0, 27, false))
                    {
                        return null;
                    }
                }
            }

            if (stack.stackSize == 0)
            {
                slot.putStack((ItemStack) null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (stack.stackSize == var2.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, stack);
        }

        return var2;
    }

    protected boolean mergeOneItem(ItemStack par1ItemStack, int par2, int par3, boolean par4)
    {
        boolean flag1 = false;
        if (par1ItemStack.stackSize > 0)
        {
            Slot slot;
            ItemStack slotStack;

            for (int k = par2; k < par3; k++)
            {
                slot = (Slot) this.inventorySlots.get(k);
                slotStack = slot.getStack();

                if (slotStack == null)
                {
                    ItemStack stackOneItem = par1ItemStack.copy();
                    stackOneItem.stackSize = 1;
                    par1ItemStack.stackSize--;
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
