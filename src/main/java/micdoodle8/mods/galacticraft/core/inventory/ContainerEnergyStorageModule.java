package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.tile.TileEntityEnergyStorageModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerEnergyStorageModule extends Container
{
    private TileEntityEnergyStorageModule tileEntity;

    public ContainerEnergyStorageModule(InventoryPlayer par1InventoryPlayer, TileEntityEnergyStorageModule batteryBox)
    {
        this.tileEntity = batteryBox;
        // Top slot for battery output
        this.addSlotToContainer(new SlotSpecific(batteryBox, 0, 33, 24, IItemElectric.class));
        // Bottom slot for batter input
        this.addSlotToContainer(new SlotSpecific(batteryBox, 1, 33, 48, IItemElectric.class));
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

        this.tileEntity.playersUsing.add(par1InventoryPlayer.player);
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
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotID)
    {
        ItemStack returnStack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotID);
        final int b = this.inventorySlots.size();

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemStack = slot.getStack();
            returnStack = itemStack.copy();

            if (slotID != 0 && slotID != 1)
            {
                if (itemStack.getItem() instanceof IItemElectric)
                {
                    if (((IItemElectric) itemStack.getItem()).getElectricityStored(itemStack) > 0)
                    {
                        if (!this.mergeItemStack(itemStack, 1, 2, false))
                        {
                            if (((IItemElectric) itemStack.getItem()).getElectricityStored(itemStack) < ((IItemElectric) itemStack.getItem()).getMaxElectricityStored(itemStack) && !this.mergeItemStack(itemStack, 0, 1, false))
                            {
                                return null;
                            }
                        }
                    }
                    else
                    {
                        if (!this.mergeItemStack(itemStack, 0, 1, false))
                        {
                            return null;
                        }
                    }
                }
                else
                {
                    if (slotID < b - 9)
                    {
                        if (!this.mergeItemStack(itemStack, b - 9, b, false))
                        {
                            return null;
                        }
                    }
                    else if (!this.mergeItemStack(itemStack, b - 36, b - 9, false))
                    {
                        return null;
                    }
                }
            }
            else if (!this.mergeItemStack(itemStack, 2, 38, false))
            {
                return null;
            }

            if (itemStack.stackSize == 0)
            {
                slot.putStack((ItemStack) null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemStack.stackSize == returnStack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemStack);
        }

        return returnStack;
    }
}
