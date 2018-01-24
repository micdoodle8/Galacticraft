package micdoodle8.mods.galacticraft.planets.mars.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.inventory.SlotSpecific;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemAtmosphericValve;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityGasLiquefier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerGasLiquefier extends Container
{
    private final TileEntityGasLiquefier tileEntity;

    public ContainerGasLiquefier(InventoryPlayer par1InventoryPlayer, TileEntityGasLiquefier tileEntity, EntityPlayer player)
    {
        this.tileEntity = tileEntity;

        // Electric Input Slot
        this.addSlotToContainer(new SlotSpecific(tileEntity, 0, 34, 50, IItemElectric.class));

        // Input slot
        this.addSlotToContainer(new Slot(tileEntity, 1, 7, 7));

        // 2 output slots
        this.addSlotToContainer(new Slot(tileEntity, 2, 132, 7));
        this.addSlotToContainer(new Slot(tileEntity, 3, 153, 7));
        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 104 + var3 * 18 - 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 144));
        }

        tileEntity.openInventory(player);
    }

    @Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
        this.tileEntity.closeInventory(entityplayer);
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
        final Slot slot = this.inventorySlots.get(par1);

        if (slot != null && slot.getHasStack())
        {
            final ItemStack var4 = slot.getStack();
            var2 = var4.copy();

            if (par1 < 4)
            {
                if (!this.mergeItemStack(var4, 4, 40, true))
                {
                    return ItemStack.EMPTY;
                }

                if (par1 == 2)
                {
                    slot.onSlotChange(var4, var2);
                }
            }
            else
            {
                if (EnergyUtil.isElectricItem(var4.getItem()))
                {
                    if (!this.mergeItemStack(var4, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else
                {
                    boolean outputTankSlotsSuccess = false;
                    if (FluidUtil.isEmptyContainerFor(var4, this.tileEntity.liquidTank2.getFluid()))
                    {
                        if (this.mergeItemStack(var4, 3, 4, false))
                        {
                            outputTankSlotsSuccess = true;
                        }
                    }
                    if (!outputTankSlotsSuccess && FluidUtil.isEmptyContainerFor(var4, this.tileEntity.liquidTank.getFluid()))
                    {
                        if (this.mergeItemStack(var4, 2, 3, false))
                        {
                            outputTankSlotsSuccess = true;
                        }
                    }

                    if (!outputTankSlotsSuccess)
                    {
                        if (FluidUtil.isFilledContainer(var4) || var4.getItem() instanceof ItemAtmosphericValve)
                        {
                            if (!this.mergeItemStack(var4, 1, 2, false))
                            {
                                return ItemStack.EMPTY;
                            }
                        }
                        else if (par1 < 31)
                        {
                            if (!this.mergeItemStack(var4, 31, 40, false))
                            {
                                return ItemStack.EMPTY;
                            }
                        }
                        else if (!this.mergeItemStack(var4, 4, 31, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }

            if (var4.isEmpty())
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
}
