package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenGear;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenMask;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemParachute;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
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
		switch (this.slotNumber)
		{
		case 45:
			return par1ItemStack.getItem() instanceof GCCoreItemOxygenMask;
		case 46:
			return par1ItemStack.getItem() instanceof GCCoreItemOxygenGear;
		case 47:
			return OxygenUtil.getDrainSpacing(par1ItemStack) > 0;
		case 48:
			return OxygenUtil.getDrainSpacing(par1ItemStack) > 0;
		case 49:
			return par1ItemStack.getItem() instanceof GCCoreItemParachute;
		}

		return false;
	}

	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}
}
