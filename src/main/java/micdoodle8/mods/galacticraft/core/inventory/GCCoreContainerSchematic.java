package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * GCCoreContainerSchematic.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreContainerSchematic extends Container
{
	public GCCoreInventorySchematic craftMatrix = new GCCoreInventorySchematic(this);
	public IInventory craftResult = new InventoryCraftResult();
	private final World worldObj;

	public GCCoreContainerSchematic(InventoryPlayer par1InventoryPlayer, int x, int y, int z)
	{
		this.worldObj = par1InventoryPlayer.player.worldObj;
		this.addSlotToContainer(new SlotSpecific(this.craftMatrix, 0, 80, 1, ISchematicItem.class));
		int var6;
		int var7;

		// Player inv:

		for (var6 = 0; var6 < 3; ++var6)
		{
			for (var7 = 0; var7 < 9; ++var7)
			{
				this.addSlotToContainer(new Slot(par1InventoryPlayer, var7 + var6 * 9 + 9, 8 + var7 * 18, 111 + var6 * 18 - 59 + 16));
			}
		}

		for (var6 = 0; var6 < 9; ++var6)
		{
			this.addSlotToContainer(new Slot(par1InventoryPlayer, var6, 8 + var6 * 18, 169 - 59 + 16));
		}

		this.onCraftMatrixChanged(this.craftMatrix);
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);

		if (!this.worldObj.isRemote)
		{
			for (int var2 = 0; var2 < 1; ++var2)
			{
				final ItemStack var3 = this.craftMatrix.getStackInSlotOnClosing(var2);

				if (var3 != null)
				{
					par1EntityPlayer.dropPlayerItem(var3);
				}
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack var3 = null;
		final Slot var4 = (Slot) this.inventorySlots.get(par2);

		if (var4 != null && var4.getHasStack())
		{
			final ItemStack var5 = var4.getStack();
			var3 = var5.copy();

			if (par2 < 27)
			{
				if (!this.mergeItemStack(var5, 27, this.inventorySlots.size(), true))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(var5, 0, 27, false))
			{
				return null;
			}

			if (var5.stackSize == 0)
			{
				var4.putStack((ItemStack) null);
			}
			else
			{
				var4.onSlotChanged();
			}
		}

		return var3;
	}
}
