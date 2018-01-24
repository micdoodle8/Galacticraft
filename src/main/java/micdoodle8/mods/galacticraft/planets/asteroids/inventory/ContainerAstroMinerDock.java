package micdoodle8.mods.galacticraft.planets.asteroids.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.inventory.SlotSpecific;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAstroMinerDock extends Container
{
    private TileEntityMinerBase tileEntity;

    public ContainerAstroMinerDock(InventoryPlayer par1InventoryPlayer, IInventory tile)
    {
        this.tileEntity = (TileEntityMinerBase) tile;
        this.addSlotToContainer(new SlotSpecific(tile, 0, 230, 108, IItemElectric.class));

        int i;
        int j;

        for (i = 0; i < 6; ++i)
        {
            for (j = 0; j < 12; ++j)
            {
                this.addSlotToContainer(new Slot(tile, j + i * 12 + 1, 8 + j * 18, 18 + i * 18));
            }
        }

        // Player inv:

        for (i = 0; i < 3; ++i)
        {
            for (j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, 8 + j * 18, 139 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, i, 8 + i * 18, 58 + 139));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer var1)
    {
        return this.tileEntity.isUsableByPlayer(var1);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack var3 = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(par2);
        int b = TileEntityMinerBase.HOLDSIZE + 1;

        if (slot != null && slot.getHasStack())
        {
            final ItemStack var5 = slot.getStack();
            var3 = var5.copy();

            if (par2 < b)
            {
                if (!this.mergeItemStack(var5, b, b + 36, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if (EnergyUtil.isElectricItem(var5.getItem()))
                {
                    if (!this.mergeItemStack(var5, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (par2 < b + 27)
                {
                    if (!this.mergeItemStack(var5, 1, b, false) && !this.mergeItemStack(var5, b + 27, b + 36, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.mergeItemStack(var5, 1, b, false) && !this.mergeItemStack(var5, b, b + 27, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (var5.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (var5.getCount() == var3.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(par1EntityPlayer, var5);
        }

        return var3;
    }
}
