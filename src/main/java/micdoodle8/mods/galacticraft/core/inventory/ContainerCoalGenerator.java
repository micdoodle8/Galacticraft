package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.tile.TileEntityCoalGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerCoalGenerator extends Container
{
    private TileEntityCoalGenerator tileEntity;

    public ContainerCoalGenerator(InventoryPlayer par1InventoryPlayer, TileEntityCoalGenerator tileEntity)
    {
        this.tileEntity = tileEntity;
        this.addSlotToContainer(new SlotSpecific(tileEntity, 0, 33, 34, new ItemStack(Items.COAL), new ItemStack(Item.getItemFromBlock(Blocks.COAL_BLOCK))));
        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 142));
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.tileEntity.isUsableByPlayer(par1EntityPlayer);
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift
     * clicking.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        Slot var3 = this.inventorySlots.get(par1);

        if (var3 != null && var3.getHasStack())
        {
            ItemStack var4 = var3.getStack();
            var2 = var4.copy();

            if (par1 != 0)
            {
                if (var4.getItem() == Items.COAL)
                {
                    if (!this.mergeItemStack(var4, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (par1 >= 28)
                {
                    if (!this.mergeItemStack(var4, 1, 28, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.mergeItemStack(var4, 28, 37, false))
                {
                    return ItemStack.EMPTY;
                }

            }
            else if (!this.mergeItemStack(var4, 1, 37, false))
            {
                return ItemStack.EMPTY;
            }

            if (var4.getCount() == 0)
            {
                var3.putStack(ItemStack.EMPTY);
            }
            else
            {
                var3.onSlotChanged();
            }

            if (var4.getCount() == var2.getCount())
            {
                return ItemStack.EMPTY;
            }

            var3.onTake(par1EntityPlayer, var4);
        }

        return var2;
    }
}
