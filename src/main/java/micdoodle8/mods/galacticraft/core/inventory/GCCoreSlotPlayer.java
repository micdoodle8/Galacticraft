package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenMask;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenTank;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemParachute;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * GCCoreSlotPlayer.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreSlotPlayer extends Slot
{
	public GCCoreSlotPlayer(IInventory par2IInventory, int par3, int par4, int par5)
	{
		super(par2IInventory, par3, par4, par5);
	}

	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}

	@Override
	public boolean isItemValid(ItemStack itemstack)
	{
		switch (this.getSlotIndex())
		{
		case 0:
			return itemstack.getItem() instanceof GCCoreItemOxygenMask;
		case 1:
			return itemstack.itemID == GCCoreItems.oxygenGear.itemID;
		case 2:
			return itemstack.getItem() instanceof GCCoreItemOxygenTank;
		case 3:
			return itemstack.getItem() instanceof GCCoreItemOxygenTank;
		case 4:
			return itemstack.getItem() instanceof GCCoreItemParachute;
		case 5:
			return itemstack.getItem().itemID == GCCoreItems.basicItem.itemID && itemstack.getItemDamage() == 19;
		}

		return super.isItemValid(itemstack);
	}
}
