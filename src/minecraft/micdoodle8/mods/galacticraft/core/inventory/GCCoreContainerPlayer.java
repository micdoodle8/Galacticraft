package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenGear;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenMask;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenTank;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemParachute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class GCCoreContainerPlayer extends ContainerPlayer
{
	public GCCoreContainerPlayer(InventoryPlayer par1InventoryPlayer, boolean par2, EntityPlayer par3EntityPlayer)
	{
		super(par1InventoryPlayer, par2, par3EntityPlayer);

		this.inventoryItemStacks.clear();
		this.inventorySlots.clear();

        this.isLocalWorld = par2;
        this.addSlotToContainer(new SlotCrafting(par1InventoryPlayer.player, this.craftMatrix, this.craftResult, 0, 116, 62));
        int i;
        int j;

        for (i = 0; i < 2; ++i)
        {
            for (j = 0; j < 2; ++j)
            {
                this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 2, 108 + j * 18, 6 + i * 18));
            }
        }

        for (i = 0; i < 4; ++i)
        {
            this.addSlotToContainer(new GCCoreSlotArmor(this, par1InventoryPlayer, par1InventoryPlayer.getSizeInventory() - 1 - i - 5, 8, 8 + i * 18, i));
        }

        for (i = 0; i < 3; ++i)
        {
            for (j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, i, 8 + i * 18, 142));
        }

        for (int var4 = 1; var4 < 5; ++var4)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, 40 + var4 - 1, 80, 8 + (var4 - 1) * 18));
        }

        this.addSlotToContainer(new Slot(par1InventoryPlayer, 44, 154, 6 + 18));
	}

	@Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotIndex)
    {
        ItemStack itemstack = null;
        final Slot slot = (Slot)this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack())
        {
            final ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotIndex == 0)
            {
                if (!this.mergeItemStack(itemstack1, 9, 45, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (slotIndex >= 1 && slotIndex < 5)
            {
                if (!this.mergeItemStack(itemstack1, 9, 45, false))
                {
                    return null;
                }
            }
            // GC
            else if (slotIndex >= 45 && slotIndex < 50)
            {
                if (!this.mergeItemStack(itemstack1, 9, 45, false))
                {
                    return null;
                }
            }
            else if (itemstack.getItem() instanceof GCCoreItemOxygenMask && !((Slot) this.inventorySlots.get(45)).getHasStack())
            {
                if (!this.mergeItemStack(itemstack1, 45, 46, false))
                {
                    return null;
                }
            }
            else if (itemstack.getItem() instanceof GCCoreItemOxygenGear && !((Slot) this.inventorySlots.get(46)).getHasStack())
            {
                if (!this.mergeItemStack(itemstack1, 46, 47, false))
                {
                    return null;
                }
            }
            else if (itemstack.getItem() instanceof GCCoreItemOxygenTank && !((Slot) this.inventorySlots.get(47)).getHasStack())
            {
                if (!this.mergeItemStack(itemstack1, 47, 48, false))
                {
                    return null;
                }
            }
            else if (itemstack.getItem() instanceof GCCoreItemOxygenTank && !((Slot) this.inventorySlots.get(48)).getHasStack())
            {
                if (!this.mergeItemStack(itemstack1, 48, 49, false))
                {
                    return null;
                }
            }
            else if (itemstack.getItem() instanceof GCCoreItemParachute && !((Slot) this.inventorySlots.get(0)).getHasStack())
            {
                if (!this.mergeItemStack(itemstack1, 49, 50, false))
                {
                    return null;
                }
            }
            // GC
            else if (slotIndex >= 5 && slotIndex < 9)
            {
                if (!this.mergeItemStack(itemstack1, 9, 45, false))
                {
                    return null;
                }
            }
            else if (itemstack.getItem() instanceof ItemArmor && !((Slot)this.inventorySlots.get(5 + ((ItemArmor)itemstack.getItem()).armorType)).getHasStack())
            {
                final int var6 = 5 + ((ItemArmor)itemstack.getItem()).armorType;

                if (!this.mergeItemStack(itemstack1, var6, var6 + 1, false))
                {
                    return null;
                }
            }
            else if (slotIndex >= 9 && slotIndex < 36)
            {
                if (!this.mergeItemStack(itemstack1, 36, 45, false))
                {
                    return null;
                }
            }
            else if (slotIndex >= 36 && slotIndex < 45)
            {
                if (!this.mergeItemStack(itemstack1, 9, 36, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 9, 45, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }

        return itemstack;
    }
}
