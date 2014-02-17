package micdoodle8.mods.galacticraft.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreContainerExtendedInventory.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class ContainerExtendedInventory extends Container
{
	public InventoryPlayer inventoryPlayer;
	public InventoryExtended extendedInventory;
	private final EntityPlayer thePlayer;

	public ContainerExtendedInventory(EntityPlayer player, InventoryExtended extendedInventory)
	{
		this.thePlayer = player;
		this.inventoryPlayer = this.thePlayer.inventory;
		this.extendedInventory = extendedInventory;

		int i;
		int j;

		for (i = 0; i < 4; ++i)
		{
			final int k = i;
			this.addSlotToContainer(new Slot(this.thePlayer.inventory, this.thePlayer.inventory.getSizeInventory() - 1 - i, 61, 8 + i * 18)
			{
				@Override
				public int getSlotStackLimit()
				{
					return 1;
				}

				@Override
				public boolean isItemValid(ItemStack par1ItemStack)
				{
					if (par1ItemStack == null)
					{
						return false;
					}
					return par1ItemStack.getItem().isValidArmor(par1ItemStack, k, ContainerExtendedInventory.this.thePlayer);
				}

				@Override
				@SideOnly(Side.CLIENT)
				public IIcon getBackgroundIconIndex()
				{
					return ItemArmor.func_94602_b(k);
				}
			});
		}

		for (i = 0; i < 3; ++i)
		{
			for (j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(this.thePlayer.inventory, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(this.thePlayer.inventory, i, 8 + i * 18, 142));
		}

		this.addSlotToContainer(new SlotPlayerInventory(extendedInventory, 0, 106 + 19, 17));
		this.addSlotToContainer(new SlotPlayerInventory(extendedInventory, 1, 106 + 19, 35));
		this.addSlotToContainer(new SlotPlayerInventory(extendedInventory, 2, 106 + 19 - 9, 53));
		this.addSlotToContainer(new SlotPlayerInventory(extendedInventory, 3, 106 + 19 + 9, 53));
		this.addSlotToContainer(new SlotPlayerInventory(extendedInventory, 4, 124 + 19, 17));
		this.addSlotToContainer(new SlotPlayerInventory(extendedInventory, 5, 106 + 1, 17));
	}

	@Override
	public boolean canInteractWith(EntityPlayer var1)
	{
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID)
	{
		return null;
	}
}
