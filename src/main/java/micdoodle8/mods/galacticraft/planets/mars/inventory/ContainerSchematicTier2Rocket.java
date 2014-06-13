package micdoodle8.mods.galacticraft.planets.mars.inventory;

import micdoodle8.mods.galacticraft.core.inventory.SlotRocketBenchResult;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.planets.mars.util.RecipeUtilMars;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;



public class ContainerSchematicTier2Rocket extends Container
{
	public InventorySchematicTier2Rocket craftMatrix = new InventorySchematicTier2Rocket(this);
	public IInventory craftResult = new InventoryCraftResult();
	private final World worldObj;

	public ContainerSchematicTier2Rocket(InventoryPlayer par1InventoryPlayer, int x, int y, int z)
	{
		final int change = 27;
		this.worldObj = par1InventoryPlayer.player.worldObj;
		this.addSlotToContainer(new SlotRocketBenchResult(par1InventoryPlayer.player, this.craftMatrix, this.craftResult, 0, 142, 18 + 69 + change));
		int var6;
		int var7;

		// Cone
		this.addSlotToContainer(new SlotSchematicTier2Rocket(this.craftMatrix, 1, 48, -8 + change, x, y, z, par1InventoryPlayer.player));

		// Body
		for (var6 = 0; var6 < 5; ++var6)
		{
			this.addSlotToContainer(new SlotSchematicTier2Rocket(this.craftMatrix, 2 + var6, 39, -6 + var6 * 18 + 16 + change, x, y, z, par1InventoryPlayer.player));
		}

		// Body Right
		for (var6 = 0; var6 < 5; ++var6)
		{
			this.addSlotToContainer(new SlotSchematicTier2Rocket(this.craftMatrix, 7 + var6, 57, -6 + var6 * 18 + 16 + change, x, y, z, par1InventoryPlayer.player));
		}

		// Left fins
		this.addSlotToContainer(new SlotSchematicTier2Rocket(this.craftMatrix, 12, 21, 64 + change, x, y, z, par1InventoryPlayer.player));
		this.addSlotToContainer(new SlotSchematicTier2Rocket(this.craftMatrix, 13, 21, 82 + change, x, y, z, par1InventoryPlayer.player));
		this.addSlotToContainer(new SlotSchematicTier2Rocket(this.craftMatrix, 14, 21, 100 + change, x, y, z, par1InventoryPlayer.player));

		// Engine
		this.addSlotToContainer(new SlotSchematicTier2Rocket(this.craftMatrix, 15, 48, 100 + change, x, y, z, par1InventoryPlayer.player));

		// Right fins
		this.addSlotToContainer(new SlotSchematicTier2Rocket(this.craftMatrix, 16, 75, 64 + change, x, y, z, par1InventoryPlayer.player));
		this.addSlotToContainer(new SlotSchematicTier2Rocket(this.craftMatrix, 17, 75, 82 + change, x, y, z, par1InventoryPlayer.player));
		this.addSlotToContainer(new SlotSchematicTier2Rocket(this.craftMatrix, 18, 75, 100 + change, x, y, z, par1InventoryPlayer.player));

		// Addons
		for (int var8 = 0; var8 < 3; var8++)
		{
			this.addSlotToContainer(new SlotSchematicTier2Rocket(this.craftMatrix, 19 + var8, 93 + var8 * 26, -15 + change, x, y, z, par1InventoryPlayer.player));
		}

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
					par1EntityPlayer.entityDropItem(var3, 0.0F);
				}
			}
		}
	}

	@Override
	public void onCraftMatrixChanged(IInventory par1IInventory)
	{
		this.craftResult.setInventorySlotContents(0, RecipeUtilMars.findMatchingSpaceshipT2Recipe(this.craftMatrix));
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

			boolean done = false;
			if (par1 <= 21)
			{
				if (!this.mergeItemStack(var4, 22, 58, false))
				{
					return null;
				}

				var3.onSlotChange(var4, var2);
			}
			else
			{
				for (int i = 1; i < 19; i++)
				{
					Slot testSlot = (Slot) this.inventorySlots.get(i); 
					if (!testSlot.getHasStack() && testSlot.isItemValid(var2))
					{
						if (!this.mergeItemStack(var4, i, i+1, false))
						{
							return null;
						}
						done = true;
						break;
					}
				}
				
				if (!done)
				{
					if (var2.getItem() == Item.getItemFromBlock(Blocks.chest) && !((Slot) this.inventorySlots.get(19)).getHasStack())
					{
						if (!this.mergeItemStack(var4, 19, 20, false))
						{
							return null;
						}
					}
					else if (var2.getItem() == Item.getItemFromBlock(Blocks.chest) && !((Slot) this.inventorySlots.get(20)).getHasStack())
					{
						if (!this.mergeItemStack(var4, 20, 21, false))
						{
							return null;
						}
					}
					else if (var2.getItem() == Item.getItemFromBlock(Blocks.chest) && !((Slot) this.inventorySlots.get(21)).getHasStack())
					{
						if (!this.mergeItemStack(var4, 21, 22, false))
						{
							return null;
						}
					}
					else if (par1 >= 22 && par1 < 49)
					{
						if (!this.mergeItemStack(var4, 49, 58, false))
						{
							return null;
						}
					}
					else if (par1 >= 49 && par1 < 58)
					{
						if (!this.mergeItemStack(var4, 22, 49, false))
						{
							return null;
						}
					}
					else if (!this.mergeItemStack(var4, 22, 58, false))
					{
						return null;
					}
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
}
