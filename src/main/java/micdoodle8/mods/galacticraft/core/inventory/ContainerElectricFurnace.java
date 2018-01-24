package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class ContainerElectricFurnace extends Container
{
    private TileEntityElectricFurnace tileEntity;

    public ContainerElectricFurnace(InventoryPlayer par1InventoryPlayer, TileEntityElectricFurnace tileEntity)
    {
        this.tileEntity = tileEntity;

        // Electric Input Slot
        this.addSlotToContainer(new SlotSpecific(tileEntity, 0, 8, 49, IItemElectric.class));

        // To be smelted
        this.addSlotToContainer(new Slot(tileEntity, 1, 56, 25));

        // Smelting result
        this.addSlotToContainer(new SlotFurnaceOutput(par1InventoryPlayer.player, tileEntity, 2, 109, 25));
        if (tileEntity.tierGC == 2)
        {
            this.addSlotToContainer(new SlotFurnaceOutput(par1InventoryPlayer.player, tileEntity, 3, 127, 25));
        }
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

        tileEntity.playersUsing.add(par1InventoryPlayer.player);
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
        int off = this.tileEntity.tierGC == 2 ? 1 : 0;

        if (var3 != null && var3.getHasStack())
        {
            ItemStack var4 = var3.getStack();
            var2 = var4.copy();

            if (par1 >= 2 && par1 <= 2 + off)
            {
                if (!this.mergeItemStack(var4, 3 + off, 39 + off, true))
                {
                    return ItemStack.EMPTY;
                }

                var3.onSlotChange(var4, var2);
            }
            else if (par1 != 1 && par1 != 0)
            {
                if (EnergyUtil.isElectricItem(var4.getItem()))
                {
                    if (!this.mergeItemStack(var4, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!FurnaceRecipes.instance().getSmeltingResult(var4).isEmpty())
                {
                    if (!this.mergeItemStack(var4, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (par1 >= 3 + off && par1 < 30 + off)
                {
                    if (!this.mergeItemStack(var4, 30 + off, 39 + off, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (par1 >= 30 + off && par1 < 39 + off && !this.mergeItemStack(var4, 3 + off, 30 + off, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(var4, 3 + off, 39 + off, false))
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
