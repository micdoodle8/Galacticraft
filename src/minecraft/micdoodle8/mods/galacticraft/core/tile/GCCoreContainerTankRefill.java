package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreContainerTankRefill extends Container
{
	public GCCoreContainerTankRefill(EntityPlayer par1EntityPlayer, GCCoreInventoryTankRefill inventorytankrefill) 
	{
		this.addSlotToContainer(new SlotCrafting(par1EntityPlayer, ((ContainerPlayer)par1EntityPlayer.inventoryContainer).craftMatrix, ((ContainerPlayer)par1EntityPlayer.inventoryContainer).craftResult, 0, 128, 62));
        int var4;
        int var5;

        for (var4 = 0; var4 < 2; ++var4)
        {
            for (var5 = 0; var5 < 2; ++var5)
            {
                this.addSlotToContainer(new Slot(((ContainerPlayer)par1EntityPlayer.inventoryContainer).craftMatrix, var5 + var4 * 2, 120 + var5 * 18, 6 + var4 * 18));
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
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
    {
        ItemStack stack = null;
        final Slot slotObj = (Slot)this.inventorySlots.get(par1);

        if (slotObj != null && slotObj.getHasStack())
        {
        	final ItemStack stackInSlot = slotObj.getStack();
        	stack = stackInSlot.copy();
            
            if (par1 == 0)
            {
            	if (!this.mergeItemStack(stackInSlot, 1, this.inventorySlots.size(), true)) 
            	{
            		return null;
            	}
            }
            else if (!this.mergeItemStack(stackInSlot, 0, 1, false))
            {
            	return null;
            }
            
            if (stackInSlot.stackSize == 0)
            {
            	slotObj.putStack(null);
            }
            else
            {
            	slotObj.onSlotChanged();
            }
        }
        
    	return null;
    }
}