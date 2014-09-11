package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.api.item.IItemOxygenSupply;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlock;
import micdoodle8.mods.galacticraft.core.items.ItemOxygenTank;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenCompressor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerOxygenCompressor extends Container
{
    private TileBaseElectricBlock tileEntity;

    public ContainerOxygenCompressor(InventoryPlayer par1InventoryPlayer, TileEntityOxygenCompressor compressor)
    {
        this.tileEntity = compressor;
        this.addSlotToContainer(new Slot(compressor, 0, 133, 71));
        this.addSlotToContainer(new SlotSpecific(compressor, 1, 47, 27, ItemElectricBase.class));
        this.addSlotToContainer(new SlotSpecific(compressor, 2, 17, 27, IItemOxygenSupply.class));

        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 20 + 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 20 + 142));
        }

        compressor.openInventory();
    }

    @Override
    public boolean canInteractWith(EntityPlayer var1)
    {
        return this.tileEntity.isUseableByPlayer(var1);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
    {
        ItemStack var2 = null;
        final Slot slot = (Slot) this.inventorySlots.get(par1);
        final int b = this.inventorySlots.size();

        if (slot != null && slot.getHasStack())
        {
            final ItemStack stack = slot.getStack();
            var2 = stack.copy();

            if (par1 < 3)
            {
                if (!this.mergeItemStack(stack, b - 36, b, true))
                {
                    return null;
                }
            }
            else
            {
                if (stack.getItem() instanceof IItemElectric)
                {
                    if (!this.mergeItemStack(stack, 1, 2, false))
                    {
                        return null;
                    }
                }
                else if (stack.getItem() instanceof IItemOxygenSupply)
                {
                    if (!this.mergeItemStack(stack, 2, 3, false))
                    {
                        return null;
                    }
                }
                else if (stack.getItem() instanceof ItemOxygenTank && stack.getItemDamage() > 0)
                {
                    if (!this.mergeItemStack(stack, 0, 1, false))
                    {
                        return null;
                    }
                }
                else
                {
                    if (par1 < b - 9)
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
}
