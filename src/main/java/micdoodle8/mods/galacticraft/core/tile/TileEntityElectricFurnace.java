package micdoodle8.mods.galacticraft.core.tile;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import micdoodle8.mods.galacticraft.api.transmission.core.item.IItemElectric;
import micdoodle8.mods.galacticraft.api.transmission.tile.IElectrical;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockMachine;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;

/**
 * TileEntityElectricFurnace.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class TileEntityElectricFurnace extends TileEntityUniversalElectrical implements IElectrical, IInventory, ISidedInventory
{
	/**
	 * The amount of watts required every TICK.
	 */
	public static final float WATTS_PER_TICK = 0.2f;

	/**
	 * The amount of processing time required.
	 */
	public static final int PROCESS_TIME_REQUIRED = 130;

	/**
	 * The amount of ticks this machine has been processing.
	 */
	@NetworkedField(targetSide = Side.CLIENT)
	public int processTicks = 0;

	/**
	 * The ItemStacks that hold the items currently being used in the battery
	 * box
	 */
	private ItemStack[] containingItems = new ItemStack[11];

	/**
	 * The amount of players using the electric furnace.
	 */
	public final Set<EntityPlayer> playersUsing = new HashSet<EntityPlayer>();

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		this.discharge(this.containingItems[0]);

		/**
		 * Attempts to smelt an item.
		 */
		if (!this.worldObj.isRemote)
		{
			if (this.canProcess())
			{
				if (this.getEnergyStored() >= TileEntityElectricFurnace.WATTS_PER_TICK)
				{
					if (this.processTicks == 0)
					{
						this.processTicks = TileEntityElectricFurnace.PROCESS_TIME_REQUIRED;
					}
					else if (this.processTicks > 0)
					{
						this.processTicks--;

						/**
						 * Process the item when the process timer is done.
						 */
						if (this.processTicks < 1)
						{
							this.smeltItem();
							this.processTicks = 0;
						}
					}
					else
					{
						this.processTicks = 0;
					}
				}
				else
				{
					this.processTicks = 0;
				}

				this.setEnergyStored(this.getEnergyStored() - TileEntityElectricFurnace.WATTS_PER_TICK);
			}
			else
			{
				this.processTicks = 0;
			}
		}
	}

	@Override
	public float getRequest(ForgeDirection direction)
	{
		if (this.canProcess())
		{
			return TileEntityElectricFurnace.WATTS_PER_TICK;
		}
		else
		{
			return 0;
		}
	}

	@Override
	public void openInventory()
	{
	}

	@Override
	public void closeInventory()
	{
	}

	/**
	 * @return Is this machine able to process its specific task?
	 */
	public boolean canProcess()
	{
		if (FurnaceRecipes.smelting().getSmeltingResult(this.containingItems[1]) == null)
		{
			return false;
		}

		if (this.containingItems[1] == null)
		{
			return false;
		}

		if (this.containingItems[2] != null)
		{
			if (!this.containingItems[2].isItemEqual(FurnaceRecipes.smelting().getSmeltingResult(this.containingItems[1])))
			{
				return false;
			}

			if (this.containingItems[2].stackSize + 1 > 64)
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Turn one item from the furnace source stack into the appropriate smelted
	 * item in the furnace result stack
	 */
	public void smeltItem()
	{
		if (this.canProcess())
		{
			ItemStack resultItemStack = FurnaceRecipes.smelting().getSmeltingResult(this.containingItems[1]);

			if (this.containingItems[2] == null)
			{
				this.containingItems[2] = resultItemStack.copy();
			}
			else if (this.containingItems[2].isItemEqual(resultItemStack))
			{
				this.containingItems[2].stackSize++;
			}

			this.containingItems[1].stackSize--;

			if (this.containingItems[1].stackSize <= 0)
			{
				this.containingItems[1] = null;
			}
		}
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.processTicks = par1NBTTagCompound.getInteger("smeltingTicks");
		NBTTagList var2 = par1NBTTagCompound.getTagList("Items", 10);
		this.containingItems = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			NBTTagCompound var4 = (NBTTagCompound) var2.getCompoundTagAt(var3);
			byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.containingItems.length)
			{
				this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("smeltingTicks", this.processTicks);
		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.containingItems.length; ++var3)
		{
			if (this.containingItems[var3] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.containingItems[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		par1NBTTagCompound.setTag("Items", var2);
	}

	@Override
	public int getSizeInventory()
	{
		return this.containingItems.length;
	}

	@Override
	public ItemStack getStackInSlot(int par1)
	{
		return this.containingItems[par1];
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (this.containingItems[par1] != null)
		{
			ItemStack var3;

			if (this.containingItems[par1].stackSize <= par2)
			{
				var3 = this.containingItems[par1];
				this.containingItems[par1] = null;
				return var3;
			}
			else
			{
				var3 = this.containingItems[par1].splitStack(par2);

				if (this.containingItems[par1].stackSize == 0)
				{
					this.containingItems[par1] = null;
				}

				return var3;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int par1)
	{
		if (this.containingItems[par1] != null)
		{
			ItemStack var2 = this.containingItems[par1];
			this.containingItems[par1] = null;
			return var2;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		this.containingItems[par1] = par2ItemStack;

		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
		{
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName()
	{
		return StatCollector.translateToLocal("tile.machine.2.name");
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return true;
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring
	 * stack size) into the given slot.
	 */
	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemStack)
	{
		return slotID == 1 ? FurnaceRecipes.smelting().getSmeltingResult(itemStack) != null : slotID == 0 ? itemStack.getItem() instanceof IItemElectric : false;
	}

	/**
	 * Get the size of the side inventory.
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return side == 0 ? new int[] { 2 } : side == 1 ? new int[] { 0, 1 } : new int[] { 0 };
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack par2ItemStack, int par3)
	{
		return this.isItemValidForSlot(slotID, par2ItemStack);
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack par2ItemStack, int par3)
	{
		return slotID == 2;
	}

	@Override
	public EnumSet<ForgeDirection> getElectricalInputDirections()
	{
		return EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() - GCCoreBlockMachine.ELECTRIC_FURNACE_METADATA + 2), ForgeDirection.UNKNOWN);
	}

	@Override
	public EnumSet<ForgeDirection> getElectricalOutputDirections()
	{
		return EnumSet.noneOf(ForgeDirection.class);
	}

	@Override
	public float getProvide(ForgeDirection direction)
	{
		return 0;
	}

	@Override
	public float getMaxEnergyStored()
	{
		return TileEntityElectricFurnace.WATTS_PER_TICK;
	}
}
