package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenGear;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenMask;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenTank;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemParachute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreContainerTankRefill extends Container
{
	public GCCoreContainerTankRefill(EntityPlayer par1EntityPlayer, GCCoreInventoryTankRefill inventorytankrefill)
	{
		this.addSlotToContainer(new SlotCrafting(par1EntityPlayer, ((ContainerPlayer)par1EntityPlayer.inventoryContainer).craftMatrix, ((ContainerPlayer)par1EntityPlayer.inventoryContainer).craftResult, 0, 116, 62));
        int var4;
        int var5;
        
        this.addSlotToContainer(new GCCoreSlotTankRefill(inventorytankrefill, 4, 154, 6));

        for (var4 = 0; var4 < 2; ++var4)
        {
            for (var5 = 0; var5 < 2; ++var5)
            {
                this.addSlotToContainer(new Slot(((ContainerPlayer)par1EntityPlayer.inventoryContainer).craftMatrix, var5 + var4 * 2, 108 + var5 * 18, 6 + var4 * 18));
            }
        }

        for (var4 = 0; var4 < 4; ++var4)
        {
            this.addSlotToContainer(new GCCoreSlotArmor((ContainerPlayer)par1EntityPlayer.inventoryContainer, par1EntityPlayer.inventory, par1EntityPlayer.inventory.getSizeInventory() - 1 - var4, 8, 8 + var4 * 18, var4));
        }

        for (var4 = 0; var4 < 3; ++var4)
        {
            for (var5 = 0; var5 < 9; ++var5)
            {
                this.addSlotToContainer(new Slot(par1EntityPlayer.inventory, var5 + (var4 + 1) * 9, 8 + var5 * 18, 84 + var4 * 18));
            }
        }

        for (var4 = 0; var4 < 9; ++var4)
        {
            this.addSlotToContainer(new Slot(par1EntityPlayer.inventory, var4, 8 + var4 * 18, 142));
        }


        for (var4 = 0; var4 < 4; ++var4)
        {
            this.addSlotToContainer(new GCCoreSlotTankRefill(inventorytankrefill, var4, 80, 8 + var4 * 18));
        }
	}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{
		return true;
	}

	@Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotIndex)
    {
        ItemStack var3 = null;
        final Slot var4 = (Slot)this.inventorySlots.get(slotIndex);

        if (var4 != null && var4.getHasStack())
        {
            final ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (slotIndex == 0)
            {
                if (!this.mergeItemStack(var5, 10, 46, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);
            }
            else if (slotIndex >= 2 && slotIndex < 6)
            {
                if (!this.mergeItemStack(var5, 10, 46, false))
                {
                    return null;
                }
            }
            // GC
            else if (slotIndex >= 46 && slotIndex < 49 || slotIndex == 1)
            {
                if (!this.mergeItemStack(var5, 10, 46, false))
                {
                    return null;
                }
            }
            else if (var3.getItem() instanceof GCCoreItemOxygenMask && !((Slot) this.inventorySlots.get(46)).getHasStack())
            {
                if (!this.mergeItemStack(var5, 46, 46 + 1, false))
                {
                    return null;
                }
            }
            else if (var3.getItem() instanceof GCCoreItemOxygenGear && !((Slot) this.inventorySlots.get(47)).getHasStack())
            {
                if (!this.mergeItemStack(var5, 47, 47 + 1, false))
                {
                    return null;
                }
            }
            else if (var3.getItem() instanceof GCCoreItemOxygenTank && !((Slot) this.inventorySlots.get(48)).getHasStack())
            {
                if (!this.mergeItemStack(var5, 48, 48 + 1, false))
                {
                    return null;
                }
            }
            else if (var3.getItem() instanceof GCCoreItemOxygenTank && !((Slot) this.inventorySlots.get(49)).getHasStack())
            {
                if (!this.mergeItemStack(var5, 49, 49 + 1, false))
                {
                    return null;
                }
            }
            else if (var3.getItem() instanceof GCCoreItemParachute && !((Slot) this.inventorySlots.get(1)).getHasStack())
            {
                if (!this.mergeItemStack(var5, 1, 1 + 1, false))
                {
                    return null;
                }
            }
            // GC
            else if (slotIndex >= 6 && slotIndex < 10)
            {
                if (!this.mergeItemStack(var5, 10, 46, false))
                {
                    return null;
                }
            }
            else if (var3.getItem() instanceof ItemArmor && !((Slot)this.inventorySlots.get(6 + ((ItemArmor)var3.getItem()).armorType)).getHasStack())
            {
                final int var6 = 6 + ((ItemArmor)var3.getItem()).armorType;

                if (!this.mergeItemStack(var5, var6, var6 + 1, false))
                {
                    return null;
                }
            }
            else if (slotIndex >= 10 && slotIndex < 37)
            {
                if (!this.mergeItemStack(var5, 37, 46, false))
                {
                    return null;
                }
            }
            else if (slotIndex >= 37 && slotIndex < 46)
            {
                if (!this.mergeItemStack(var5, 10, 37, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 10, 46, false))
            {
                return null;
            }

            if (var5.stackSize == 0)
            {
                var4.putStack((ItemStack)null);
            }
            else
            {
                var4.onSlotChanged();
            }

            if (var5.stackSize == var3.stackSize)
            {
                return null;
            }

            var4.onPickupFromSlot(par1EntityPlayer, var5);
        }

        return var3;
    }
}