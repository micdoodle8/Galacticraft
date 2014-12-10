package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFuelLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ContainerFuelLoader extends Container
{
    private TileBaseElectricBlock tileEntity;

    public ContainerFuelLoader(InventoryPlayer par1InventoryPlayer, TileEntityFuelLoader fuelLoader)
    {
        this.tileEntity = fuelLoader;
        this.addSlotToContainer(new SlotSpecific(fuelLoader, 0, 51, 55, ItemElectricBase.class));
        this.addSlotToContainer(new Slot(fuelLoader, 1, 7, 12));

        int var6;
        int var7;

        // Player inv:

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var7 + var6 * 9 + 9, 8 + var7 * 18, 31 + 58 + var6 * 18));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var6, 8 + var6 * 18, 31 + 116));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer var1)
    {
        return this.tileEntity.isUseableByPlayer(var1);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack var3 = null;
        final Slot slot = (Slot) this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            final ItemStack var5 = slot.getStack();
            var3 = var5.copy();

            if (par2 < 2)
            {
                if (!this.mergeItemStack(var5, 2, 38, true))
                {
                    return null;
                }
            }
            else
            {
                if (var5.getItem() instanceof IItemElectric)
                {
                    if (!this.mergeItemStack(var5, 0, 1, false))
                    {
                        return null;
                    }
                }
                else
                {
                    FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(var5);
                    if (liquid != null && FluidRegistry.getFluidName(liquid).equalsIgnoreCase("Fuel"))
                    {
                        if (!this.mergeItemStack(var5, 1, 2, false))
                        {
                            return null;
                        }
                    }
                    else if (par2 < 29)
                    {
                        if (!this.mergeItemStack(var5, 29, 38, false))
                        {
                            return null;
                        }
                    }
                    else if (!this.mergeItemStack(var5, 2, 29, false))
                    {
                        return null;
                    }
                }
            }

            if (var5.stackSize == 0)
            {
                slot.putStack((ItemStack) null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (var5.stackSize == var3.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, var5);
        }

        return var3;
    }
}
