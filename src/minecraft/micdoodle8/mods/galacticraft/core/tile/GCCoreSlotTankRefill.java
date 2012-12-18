package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.GCCoreUtil;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreSlotTankRefill extends Slot 
{
	public GCCoreSlotTankRefill(IInventory par3IInventory, int par4, int par5, int par6) 
	{
		super(par3IInventory, par4, par5, par6);
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		return GCCoreUtil.getDrainSpacing(par1ItemStack) > 0;
	}

	@Override
	public int getSlotStackLimit() 
	{
		return 1;
	}
}
