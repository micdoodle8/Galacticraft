package micdoodle8.mods.galacticraft.mars.inventory;

import micdoodle8.mods.galacticraft.core.inventory.GCCoreSlotRocketBenchResult;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.mars.util.RecipeUtilMars;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * GCMarsContainerCargoRocketBench.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsContainerCargoRocketBench extends Container
{
	public GCMarsInventoryCargoRocketBench craftMatrix = new GCMarsInventoryCargoRocketBench(this);
	public IInventory craftResult = new InventoryCraftResult();
	private final World worldObj;

	public GCMarsContainerCargoRocketBench(InventoryPlayer par1InventoryPlayer, int x, int y, int z)
	{
		int change = 27;
		this.worldObj = par1InventoryPlayer.player.worldObj;
		this.addSlotToContainer(new GCCoreSlotRocketBenchResult(par1InventoryPlayer.player, this.craftMatrix, this.craftResult, 0, 142, 69 + change));
		int var6;
		int var7;

		// Cone
		this.addSlotToContainer(new GCMarsSlotCargoRocketBench(this.craftMatrix, 1, 48, -9 + change, x, y, z, par1InventoryPlayer.player));

		this.addSlotToContainer(new GCMarsSlotCargoRocketBench(this.craftMatrix, 2, 48, -9 + 18 + change, x, y, z, par1InventoryPlayer.player));

		// Body
		for (var6 = 0; var6 < 3; ++var6)
		{
			this.addSlotToContainer(new GCMarsSlotCargoRocketBench(this.craftMatrix, 3 + var6, 39, -7 + var6 * 18 + 16 + 18 + change, x, y, z, par1InventoryPlayer.player));
		}

		// Body Right
		for (var6 = 0; var6 < 3; ++var6)
		{
			this.addSlotToContainer(new GCMarsSlotCargoRocketBench(this.craftMatrix, 6 + var6, 57, -7 + var6 * 18 + 16 + 18 + change, x, y, z, par1InventoryPlayer.player));
		}

		// Left fins
		this.addSlotToContainer(new GCMarsSlotCargoRocketBench(this.craftMatrix, 9, 21, 63 + change, x, y, z, par1InventoryPlayer.player));
		this.addSlotToContainer(new GCMarsSlotCargoRocketBench(this.craftMatrix, 10, 21, 81 + change, x, y, z, par1InventoryPlayer.player));

		// Engine
		this.addSlotToContainer(new GCMarsSlotCargoRocketBench(this.craftMatrix, 11, 48, 81 + change, x, y, z, par1InventoryPlayer.player));

		// Right fins
		this.addSlotToContainer(new GCMarsSlotCargoRocketBench(this.craftMatrix, 12, 75, 63 + change, x, y, z, par1InventoryPlayer.player));
		this.addSlotToContainer(new GCMarsSlotCargoRocketBench(this.craftMatrix, 13, 75, 81 + change, x, y, z, par1InventoryPlayer.player));

		// Addons
		for (int var8 = 0; var8 < 3; var8++)
		{
			this.addSlotToContainer(new GCMarsSlotCargoRocketBench(this.craftMatrix, 14 + var8, 93 + var8 * 26, -15 + change, x, y, z, par1InventoryPlayer.player));
		}

		change = 9;

		// Player inv:

		for (var6 = 0; var6 < 3; ++var6)
		{
			for (var7 = 0; var7 < 9; ++var7)
			{
				this.addSlotToContainer(new Slot(par1InventoryPlayer, var7 + var6 * 9 + 9, 8 + var7 * 18, 129 + var6 * 18 + change));
			}
		}

		for (var6 = 0; var6 < 9; ++var6)
		{
			this.addSlotToContainer(new Slot(par1InventoryPlayer, var6, 8 + var6 * 18, 18 + 169 + change));
		}

		this.onCraftMatrixChanged(this.craftMatrix);
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);

		if (!this.worldObj.isRemote)
		{
			for (int var2 = 1; var2 < this.craftMatrix.getSizeInventory(); ++var2)
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
	public void onCraftMatrixChanged(IInventory par1IInventory)
	{
		this.craftResult.setInventorySlotContents(0, RecipeUtilMars.findMatchingCargoRocketRecipe(this.craftMatrix));
	}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
	{
		ItemStack var2 = null;
		final Slot var3 = (Slot) this.inventorySlots.get(par1);

		if (var3 != null && var3.getHasStack())
		{
			final ItemStack var4 = var3.getStack();
			var2 = var4.copy();

			if (par1 <= 17)
			{
				if (!this.mergeItemStack(var4, 18, 46, false))
				{
					return null;
				}

				var3.onSlotChange(var4, var2);
			}
			else if (var2.getItem().itemID == GCCoreItems.partNoseCone.itemID && !((Slot) this.inventorySlots.get(1)).getHasStack())
			{
				if (!this.mergeItemStack(var4, 1, 2, false))
				{
					return null;
				}
			}
			else if (var2.getItem().itemID == GCCoreItems.heavyPlatingTier1.itemID && !((Slot) this.inventorySlots.get(2)).getHasStack())
			{
				if (!this.mergeItemStack(var4, 2, 3, false))
				{
					return null;
				}
			}
			else if (var2.getItem().itemID == GCCoreItems.heavyPlatingTier1.itemID && !((Slot) this.inventorySlots.get(3)).getHasStack())
			{
				if (!this.mergeItemStack(var4, 3, 4, false))
				{
					return null;
				}
			}
			else if (var2.getItem().itemID == GCCoreItems.heavyPlatingTier1.itemID && !((Slot) this.inventorySlots.get(4)).getHasStack())
			{
				if (!this.mergeItemStack(var4, 4, 5, false))
				{
					return null;
				}
			}
			else if (var2.getItem().itemID == GCCoreItems.heavyPlatingTier1.itemID && !((Slot) this.inventorySlots.get(5)).getHasStack())
			{
				if (!this.mergeItemStack(var4, 5, 6, false))
				{
					return null;
				}
			}
			else if (var2.getItem().itemID == GCCoreItems.heavyPlatingTier1.itemID && !((Slot) this.inventorySlots.get(6)).getHasStack())
			{
				if (!this.mergeItemStack(var4, 6, 7, false))
				{
					return null;
				}
			}
			else if (var2.getItem().itemID == GCCoreItems.heavyPlatingTier1.itemID && !((Slot) this.inventorySlots.get(7)).getHasStack())
			{
				if (!this.mergeItemStack(var4, 7, 8, false))
				{
					return null;
				}
			}
			else if (var2.getItem().itemID == GCCoreItems.heavyPlatingTier1.itemID && !((Slot) this.inventorySlots.get(8)).getHasStack())
			{
				if (!this.mergeItemStack(var4, 8, 9, false))
				{
					return null;
				}
			}
			else if (var2.getItem().itemID == GCCoreItems.heavyPlatingTier1.itemID && !((Slot) this.inventorySlots.get(9)).getHasStack())
			{
				if (!this.mergeItemStack(var4, 9, 10, false))
				{
					return null;
				}
			}
			else if (var2.getItem().itemID == GCCoreItems.partFins.itemID && !((Slot) this.inventorySlots.get(10)).getHasStack())
			{
				if (!this.mergeItemStack(var4, 10, 11, false))
				{
					return null;
				}
			}
			else if (var2.getItem().itemID == GCCoreItems.partFins.itemID && !((Slot) this.inventorySlots.get(11)).getHasStack())
			{
				if (!this.mergeItemStack(var4, 11, 12, false))
				{
					return null;
				}
			}
			else if (var2.getItem().itemID == GCCoreItems.rocketEngine.itemID && !((Slot) this.inventorySlots.get(12)).getHasStack())
			{
				if (!this.mergeItemStack(var4, 12, 13, false))
				{
					return null;
				}
			}
			else if (var2.getItem().itemID == GCCoreItems.partFins.itemID && !((Slot) this.inventorySlots.get(13)).getHasStack())
			{
				if (!this.mergeItemStack(var4, 13, 14, false))
				{
					return null;
				}
			}
			else if (var2.getItem().itemID == GCCoreItems.partFins.itemID && !((Slot) this.inventorySlots.get(14)).getHasStack())
			{
				if (!this.mergeItemStack(var4, 14, 15, false))
				{
					return null;
				}
			}
			else if (var2.getItem().itemID == Block.chest.blockID && !((Slot) this.inventorySlots.get(15)).getHasStack())
			{
				if (!this.mergeItemStack(var4, 15, 16, false))
				{
					return null;
				}
			}
			else if (var2.getItem().itemID == Block.chest.blockID && !((Slot) this.inventorySlots.get(16)).getHasStack())
			{
				if (!this.mergeItemStack(var4, 16, 17, false))
				{
					return null;
				}
			}
			else if (var2.getItem().itemID == Block.chest.blockID && !((Slot) this.inventorySlots.get(17)).getHasStack())
			{
				if (!this.mergeItemStack(var4, 17, 18, false))
				{
					return null;
				}
			}
			else if (par1 >= 18 && par1 < 37)
			{
				if (!this.mergeItemStack(var4, 37, 46, false))
				{
					return null;
				}
			}
			else if (par1 >= 37 && par1 < 46)
			{
				if (!this.mergeItemStack(var4, 18, 37, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(var4, 18, 46, false))
			{
				return null;
			}

			if (var4.stackSize == 0)
			{
				var3.putStack((ItemStack) null);
			}
			else
			{
				var3.onSlotChanged();
			}

			if (var4.stackSize == var2.stackSize)
			{
				return null;
			}

			var3.onPickupFromSlot(par1EntityPlayer, var4);
		}

		return var2;
	}
}
