package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreContainerAirDistributor extends Container
{
    private final GCCoreTileEntityOxygenDistributor distributor;
    private final int lastCookTime = 0;
    private final int lastBurnTime = 0;
    private final int lastItemBurnTime = 0;

    public GCCoreContainerAirDistributor(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityOxygenDistributor par2TileEntityAirDistributor)
    {
        this.distributor = par2TileEntityAirDistributor;
        this.addSlotToContainer(new GCCoreSlotTankRefill(par2TileEntityAirDistributor, 0, 80, 41));
        this.addSlotToContainer(new Slot(par2TileEntityAirDistributor, 1, 148, 32));
        this.addSlotToContainer(new Slot(par2TileEntityAirDistributor, 2, 148, 51));
        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 89 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 147));
        }
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
    
    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return true;
    }
}
