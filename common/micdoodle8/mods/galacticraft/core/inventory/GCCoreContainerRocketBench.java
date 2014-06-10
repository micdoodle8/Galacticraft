package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
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
 * GCCoreContainerRocketBench.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreContainerRocketBench extends Container
{
	public GCCoreInventoryRocketBench craftMatrix = new GCCoreInventoryRocketBench(this);
	public IInventory craftResult = new InventoryCraftResult();
	private final World worldObj;

	public GCCoreContainerRocketBench(InventoryPlayer par1InventoryPlayer, int x, int y, int z)
	{
		final int change = 27;
		this.worldObj = par1InventoryPlayer.player.worldObj;
		this.addSlotToContainer(new GCCoreSlotRocketBenchResult(par1InventoryPlayer.player, this.craftMatrix, this.craftResult, 0, 142, 69 + change));
		int var6;
		int var7;

		// Cone
		this.addSlotToContainer(new GCCoreSlotRocketBench(this.craftMatrix, 1, 48, -8 + change, x, y, z, par1InventoryPlayer.player));

		// Body
		for (var6 = 0; var6 < 4; ++var6)
		{
			this.addSlotToContainer(new GCCoreSlotRocketBench(this.craftMatrix, 2 + var6, 39, -6 + var6 * 18 + 16 + change, x, y, z, par1InventoryPlayer.player));
		}

		// Body Right
		for (var6 = 0; var6 < 4; ++var6)
		{
			this.addSlotToContainer(new GCCoreSlotRocketBench(this.craftMatrix, 6 + var6, 57, -6 + var6 * 18 + 16 + change, x, y, z, par1InventoryPlayer.player));
		}

		// Left fins
		this.addSlotToContainer(new GCCoreSlotRocketBench(this.craftMatrix, 10, 21, 64 + change, x, y, z, par1InventoryPlayer.player));
		this.addSlotToContainer(new GCCoreSlotRocketBench(this.craftMatrix, 11, 21, 82 + change, x, y, z, par1InventoryPlayer.player));

		// Engine
		this.addSlotToContainer(new GCCoreSlotRocketBench(this.craftMatrix, 12, 48, 82 + change, x, y, z, par1InventoryPlayer.player));

		// Right fins
		this.addSlotToContainer(new GCCoreSlotRocketBench(this.craftMatrix, 13, 75, 64 + change, x, y, z, par1InventoryPlayer.player));
		this.addSlotToContainer(new GCCoreSlotRocketBench(this.craftMatrix, 14, 75, 82 + change, x, y, z, par1InventoryPlayer.player));

		// Addons
		for (int var8 = 0; var8 < 3; var8++)
		{
			this.addSlotToContainer(new GCCoreSlotRocketBench(this.craftMatrix, 15 + var8, 93 + var8 * 26, -15 + change, x, y, z, par1InventoryPlayer.player));
		}

		// Player inv:

		for (var6 = 0; var6 < 3; ++var6)
		{
			for (var7 = 0; var7 < 9; ++var7)
			{
				this.addSlotToContainer(new Slot(par1InventoryPlayer, var7 + var6 * 9 + 9, 8 + var7 * 18, 111 + var6 * 18 + change));
			}
		}

		for (var6 = 0; var6 < 9; ++var6)
		{
			this.addSlotToContainer(new Slot(par1InventoryPlayer, var6, 8 + var6 * 18, 169 + change));
		}

		this.onCraftMatrixChanged(this.craftMatrix);
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);

		if (!this.worldObj.isRemote)
		{
			for (int var2 = 1; var2 < 18; ++var2)
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
		this.craftResult.setInventorySlotContents(0, RecipeUtil.findMatchingSpaceshipRecipe(this.craftMatrix));
	}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{
		return true;
	}

	/**
	 * Called to transfer a stack from one inventory to the other eg. when shift
	 * clicking.
	 */
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
				if (!this.mergeItemStack(var4, 18, 54, false))
				{
					return null;
				}

				if (par1 == 0) var3.onSlotChange(var4, var2);
			}
			else if (var2.getItem().itemID == GCCoreItems.partNoseCone.itemID)
			{
				if (!this.mergeOneItem(var4, 1, 2, false))
				{
					return null;
				}
			}
			else if (var2.getItem().itemID == GCCoreItems.heavyPlatingTier1.itemID)
			{
				if (!this.mergeOneItem(var4, 2, 10, false))
				{
					return null;
				}
			}
			else if (var2.getItem().itemID == GCCoreItems.partFins.itemID)
			{
				if (!this.mergeOneItem(var4, 10, 12, false) && !this.mergeOneItem(var4, 13, 15, false) )
				{
					return null;
				}
			}
			else if (var2.getItem().itemID == GCCoreItems.rocketEngine.itemID)
			{
				if (!this.mergeOneItem(var4, 12, 13, false))
				{
					return null;
				}
			}
			else if (var2.getItem().itemID == Block.chest.blockID)
			{
				if (!this.mergeOneItem(var4, 15, 18, false))
				{
					return null;
				}
			}
			else if (par1 >= 18 && par1 < 45)
			{
				if (!this.mergeItemStack(var4, 45, 54, false))
				{
					return null;
				}
			}
			else if (par1 >= 45 && par1 < 54)
			{
				if (!this.mergeItemStack(var4, 18, 45, false))
				{
					return null;
				}
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

    protected boolean mergeOneItem(ItemStack par1ItemStack, int par2, int par3, boolean par4)
    {
        boolean flag1 = false;
        if (par1ItemStack.stackSize > 0)
        {
            Slot slot;
            ItemStack slotStack;

            for (int k = par2; k < par3; k++)
            {
                slot = (Slot)this.inventorySlots.get(k);
                slotStack = slot.getStack();

                if (slotStack == null)
                {
                    ItemStack stackOneItem = par1ItemStack.copy();
                    stackOneItem.stackSize = 1;
                    par1ItemStack.stackSize--;
                	slot.putStack(stackOneItem);
                    slot.onSlotChanged();
                    flag1 = true;
                    break;
                }
            }
        }

        return flag1;
    }
}
