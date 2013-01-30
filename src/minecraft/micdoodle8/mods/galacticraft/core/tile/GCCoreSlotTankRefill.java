package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBreathableHelmet;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenGear;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Copyright 2012-2013, micdoodle8
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
		switch (this.slotNumber - 45)
		{
		case 0:
			return par1ItemStack.getItem() instanceof GCCoreItemBreathableHelmet;
		case 1:
			return par1ItemStack.getItem() instanceof GCCoreItemOxygenGear;
		case 2:
			return GCCoreUtil.getDrainSpacing(par1ItemStack) > 0;
		case 3:
			return GCCoreUtil.getDrainSpacing(par1ItemStack) > 0;
		}
		return false;
	}

	@Override
	public int getSlotStackLimit() 
	{
		return 1;
	}
}
