package micdoodle8.mods.galacticraft.mars.inventory;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.transmission.core.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.inventory.SlotSpecific;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityTerraformer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * GCMarsContainerTerraformer.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsContainerTerraformer extends Container
{
	private final GCMarsTileEntityTerraformer tileEntity;

	public GCMarsContainerTerraformer(InventoryPlayer par1InventoryPlayer, GCMarsTileEntityTerraformer tileEntity)
	{
		this.tileEntity = tileEntity;

		this.addSlotToContainer(new SlotSpecific(tileEntity, 0, 25, 19, new ItemStack(Item.bucketWater)));

		this.addSlotToContainer(new SlotSpecific(tileEntity, 1, 25, 39, IItemElectric.class));

		int var6;
		int var7;

		for (var6 = 0; var6 < 3; ++var6)
		{
			for (var7 = 0; var7 < 4; ++var7)
			{
				List<ItemStack> stacks = new ArrayList<ItemStack>();

				if (var6 == 0)
				{
					stacks.add(new ItemStack(Item.dyePowder, 1, 15));
				}
				else if (var6 == 1)
				{
					stacks.add(new ItemStack(Block.sapling, 1, 0));
					stacks.add(new ItemStack(Block.sapling, 1, 1));
					stacks.add(new ItemStack(Block.sapling, 1, 2));
					stacks.add(new ItemStack(Block.sapling, 1, 3));
				}
				else if (var6 == 2)
				{
					stacks.add(new ItemStack(Item.seeds));
				}

				this.addSlotToContainer(new SlotSpecific(tileEntity, var7 + var6 * 4 + 2, 25 + var7 * 18, 63 + var6 * 24, stacks.toArray(new ItemStack[stacks.size()])));
			}
		}

		for (var6 = 0; var6 < 3; ++var6)
		{
			for (var7 = 0; var7 < 9; ++var7)
			{
				this.addSlotToContainer(new Slot(par1InventoryPlayer, var7 + var6 * 9 + 9, 8 + var7 * 18, 146 + var6 * 18));
			}
		}

		for (var6 = 0; var6 < 9; ++var6)
		{
			this.addSlotToContainer(new Slot(par1InventoryPlayer, var6, 8 + var6 * 18, 204));
		}

		tileEntity.openChest();
	}

	@Override
	public void onContainerClosed(EntityPlayer entityplayer)
	{
		super.onContainerClosed(entityplayer);
		this.tileEntity.closeChest();
	}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{
		return this.tileEntity.isUseableByPlayer(par1EntityPlayer);
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

			if (par1 == 2)
			{
				if (!this.mergeItemStack(var4, 3, 39, true))
				{
					return null;
				}

				var3.onSlotChange(var4, var2);
			}
			else if (par1 != 1 && par1 != 0)
			{
				if (var4.getItem() instanceof IItemElectric)
				{
					if (!this.mergeItemStack(var4, 0, 1, false))
					{
						return null;
					}
				}
				else if (FluidContainerRegistry.isContainer(var4) || FluidContainerRegistry.containsFluid(var4, FluidRegistry.getFluidStack("fuel", 1)))
				{
					if (!this.mergeItemStack(var4, 1, 2, false))
					{
						return null;
					}
				}
				else if (par1 >= 3 && par1 < 30)
				{
					if (!this.mergeItemStack(var4, 30, 39, false))
					{
						return null;
					}
				}
				else if (par1 >= 30 && par1 < 39 && !this.mergeItemStack(var4, 3, 30, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(var4, 3, 39, false))
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
