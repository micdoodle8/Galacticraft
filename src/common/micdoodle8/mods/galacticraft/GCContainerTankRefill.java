package micdoodle8.mods.galacticraft;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Slot;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCContainerTankRefill extends Container
{
	public GCContainerTankRefill(InventoryPlayer par1InventoryPlayer, GCInventoryTankRefill inventorytankrefill) 
	{
        this.addSlotToContainer(new GCSlotTankRefill(inventorytankrefill, 0, 44, 47));
		int var6;
        int var7;

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var7 + var6 * 9 + 9, 8 + var7 * 18, 84 + var6 * 18));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var6, 8 + var6 * 18, 142));
        }
	}

	@Override
	public boolean canInteractWith(EntityPlayer var1)
	{
		return true;
	}
}