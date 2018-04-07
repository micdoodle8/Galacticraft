package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.item.IPaintable;
import micdoodle8.mods.galacticraft.core.tile.TileEntityPainter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ContainerPainter extends Container
{
    private TileEntityPainter tileEntity;

    public ContainerPainter(InventoryPlayer par1InventoryPlayer, TileEntityPainter tileEntity2)
    {
        this.tileEntity = tileEntity2;
 
        // To be painted
        this.addSlotToContainer(new Slot(tileEntity2, 0, 40, 25));
        //TODO: slots which can only accept one item

        // For dyes and other colour giving items
        this.addSlotToContainer(new Slot(tileEntity2, 1, 122, 25));

        int i;
        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, 8 + j * 18, 104 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, i, 8 + i * 18, 162));
        }

        tileEntity2.playersUsing.add(par1InventoryPlayer.player);
    }

    @Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
        this.tileEntity.playersUsing.remove(entityplayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.tileEntity.isUseableByPlayer(par1EntityPlayer);
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift
     * clicking.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int index)
    {
        ItemStack stackOrig = null;
        Slot slot = (Slot) this.inventorySlots.get(index);
        final int b = this.inventorySlots.size();

        if (slot != null && slot.getHasStack())
        {
            ItemStack stack = slot.getStack();
            stackOrig = stack.copy();

            if (index < 2)
            {
                if (!this.mergeItemStack(stack, b - 36, b, true))
                {
                    return null;
                }
                slot.onSlotChange(stack, stackOrig);
            }
            else if (index != 1 && index != 0)
            {
                Item item = stack.getItem();
                if (item instanceof IPaintable || (item instanceof ItemBlock && ((ItemBlock)item).block instanceof IPaintable))
                {
                    if (!this.mergeOneItem(stack, 1, 2, false))
                    {
                        return null;
                    }
                }
                else if (index < b - 9)
                {
                    if (!this.mergeItemStack(stack, b - 9, b, false))
                    {
                        return null;
                    }
                }
                else if (!this.mergeItemStack(stack, b - 36, b - 9, false))
                {
                    return null;
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

            if (stack.stackSize == stackOrig.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, stack);
        }

        return stackOrig;
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
