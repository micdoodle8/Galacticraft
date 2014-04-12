package micdoodle8.mods.galacticraft.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * GCCoreSlotRocketBenchResult.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreSlotRocketBenchResult extends Slot
{
	private final IInventory craftMatrix;
	private final EntityPlayer thePlayer;

	public GCCoreSlotRocketBenchResult(EntityPlayer par1EntityPlayer, IInventory par2IInventory, IInventory par3IInventory, int par4, int par5, int par6)
	{
		super(par3IInventory, par4, par5, par6);
		this.thePlayer = par1EntityPlayer;
		this.craftMatrix = par2IInventory;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		return false;
	}

	@Override
	public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par1ItemStack)
	{
		for (int var2 = 0; var2 < this.craftMatrix.getSizeInventory(); ++var2)
		{
			final ItemStack var3 = this.craftMatrix.getStackInSlot(var2);

			if (var3 != null)
			{
				this.craftMatrix.decrStackSize(var2, 1);

				if (var3.getItem().hasContainerItem())
				{
					final ItemStack var4 = new ItemStack(var3.getItem().getContainerItem());

					if (!var3.getItem().doesContainerItemLeaveCraftingGrid(var3) || !this.thePlayer.inventory.addItemStackToInventory(var4))
					{
						if (this.craftMatrix.getStackInSlot(var2) == null)
						{
							this.craftMatrix.setInventorySlotContents(var2, var4);
						}
						else
						{
							this.thePlayer.dropPlayerItem(var4);
						}
					}
				}
			}
		}
	}
}
