package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.items.GCCoreItemParachute;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * GCCoreSlotTankRefill.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
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
		if (this.slotNumber == 49)
		{
			return par1ItemStack.getItem() instanceof GCCoreItemParachute;
		}

		return OxygenUtil.isItemValidForPlayerTankInv(this.slotNumber - 45, par1ItemStack);
	}

	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}
}
