package micdoodle8.mods.galacticraft.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerParaChest extends Container
{
    private IInventory parachestInventory;
    public int numRows;

    public ContainerParaChest(IInventory par1IInventory, IInventory par2IInventory, EntityPlayer player)
    {
        this.parachestInventory = par2IInventory;
        this.numRows = (par2IInventory.getSizeInventory() - 3) / 9;
        par2IInventory.openInventory(player);
        int i = (this.numRows - 4) * 18 + 19;
        int j;
        int k;

        for (j = 0; j < this.numRows; ++j)
        {
            for (k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(par2IInventory, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        this.addSlotToContainer(new Slot(par2IInventory, par2IInventory.getSizeInventory() - 3, 125 + 0 * 18, (this.numRows == 0 ? 24 : 26) + this.numRows * 18));
        this.addSlotToContainer(new Slot(par2IInventory, par2IInventory.getSizeInventory() - 2, 125 + 1 * 18, (this.numRows == 0 ? 24 : 26) + this.numRows * 18));
        this.addSlotToContainer(new Slot(par2IInventory, par2IInventory.getSizeInventory() - 1, 75, (this.numRows == 0 ? 24 : 26) + this.numRows * 18));

        for (j = 0; j < 3; ++j)
        {
            for (k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(par1IInventory, k + j * 9 + 9, 8 + k * 18, (this.numRows == 0 ? 116 : 118) + j * 18 + i));
            }
        }

        for (j = 0; j < 9; ++j)
        {
            this.addSlotToContainer(new Slot(par1IInventory, j, 8 + j * 18, (this.numRows == 0 ? 174 : 176) + i));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.parachestInventory.isUsableByPlayer(par1EntityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(par2);
        final int b = this.inventorySlots.size();

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 < this.parachestInventory.getSizeInventory())
            {
                if (!this.mergeItemStack(itemstack1, b - 36, b, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.parachestInventory.getSizeInventory(), false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0)
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    /**
     * Callback for when the crafting gui is closed.
     */
    @Override
    public void onContainerClosed(EntityPlayer par1EntityPlayer)
    {
        super.onContainerClosed(par1EntityPlayer);
        this.parachestInventory.closeInventory(par1EntityPlayer);
    }

    /**
     * Return this chest container's lower chest inventory.
     */
    public IInventory getparachestInventory()
    {
        return this.parachestInventory;
    }
}
