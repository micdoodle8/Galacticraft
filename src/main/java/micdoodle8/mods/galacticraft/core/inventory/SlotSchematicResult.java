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
public class SlotSchematicResult extends Slot
{
	private final IInventory craftMatrix;
	private final EntityPlayer thePlayer;
	private int field_48436_g;

	public SlotSchematicResult(EntityPlayer par1EntityPlayer, IInventory par2IInventory, IInventory par3IInventory, int par4, int par5, int par6)
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
	public ItemStack decrStackSize(int par1)
	{
		if (this.getHasStack())
		{
			this.field_48436_g += Math.min(par1, this.getStack().stackSize);
		}

		return super.decrStackSize(par1);
	}

	@Override
	protected void onCrafting(ItemStack par1ItemStack, int par2)
	{
		this.field_48436_g += par2;
		this.onCrafting(par1ItemStack);
	}

	@Override
	protected void onCrafting(ItemStack par1ItemStack)
	{
		par1ItemStack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_48436_g);
		this.field_48436_g = 0;
	}

	@Override
	public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par1ItemStack)
	{
		this.onCrafting(par1ItemStack);

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
							this.thePlayer.entityDropItem(var4, 0.0F);
						}
					}
				}
			}
		}
	}
}
