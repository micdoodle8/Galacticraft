package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.api.tile.ILockable;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCargoLoader extends Container
{
    private TileBaseElectricBlock tileEntity;
    private boolean locked;

    public ContainerCargoLoader(InventoryPlayer par1InventoryPlayer, IInventory cargoLoader)
    {
        this.tileEntity = (TileBaseElectricBlock) cargoLoader;
        if (tileEntity instanceof ILockable)
        {
            this.locked = ((ILockable)tileEntity).getLocked();
        }

        this.addSlotToContainer(new SlotSpecific(cargoLoader, 0, 10, 27, IItemElectric.class));

        int var6;
        int var7;

        for (var6 = 0; var6 < 2; ++var6)
        {
            for (var7 = 0; var7 < 7; ++var7)
            {
                this.addSlotToContainer(new Slot(cargoLoader, var7 + var6 * 7 + 1, 38 + var7 * 18, 27 + var6 * 18));
            }
        }

        // Player inv:

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var7 + var6 * 9 + 9, 8 + var7 * 18, 124 + var6 * 18));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var6, 8 + var6 * 18, 66 + 116));
        }
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
    {
        if (this.locked && slotId > 0 && slotId < 15)
        {
            return ItemStack.EMPTY;
        }
        return super.slotClick(slotId, dragType, clickTypeIn, player);
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

        if (slot != null && slot.getHasStack())
        {
            final ItemStack var5 = slot.getStack();
            var3 = var5.copy();

            if (par2 < 15)
            {
                if ((this.locked && par2 > 0) || !this.mergeItemStack(var5, 15, 51, true))
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
                else if (par2 < 42)
                {
                    if ((this.locked || !this.mergeItemStack(var5, 1, 15, false)) && !this.mergeItemStack(var5, 42, 51, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if ((this.locked || !this.mergeItemStack(var5, 1, 15, false)) && !this.mergeItemStack(var5, 15, 42, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (var5.getCount() == 0)
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
