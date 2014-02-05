package micdoodle8.mods.galacticraft.core.tile;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import micdoodle8.mods.galacticraft.api.transmission.core.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockMachine;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;

/**
 * GCCoreTileEntityEnergyStorageModule.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreTileEntityEnergyStorageModule extends GCCoreTileEntityUniversalElectrical implements IPacketReceiver, ISidedInventory
{
	private ItemStack[] containingItems = new ItemStack[2];

	public final Set<EntityPlayer> playersUsing = new HashSet<EntityPlayer>();
	public int scaledEnergyLevel;
	public int lastScaledEnergyLevel;

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		this.scaledEnergyLevel = (int) Math.floor(this.getEnergyStored() * 16 / this.getMaxEnergyStored());

		if (this.scaledEnergyLevel != this.lastScaledEnergyLevel)
		{
			this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
		}

		if (!this.worldObj.isRemote)
		{
			this.recharge(this.containingItems[0]);
			this.discharge(this.containingItems[1]);
		}

		/**
		 * Gradually lose energy.
		 */
		this.setEnergyStored(this.getEnergyStored() - 0.00005f);

		if (!this.worldObj.isRemote)
		{
			this.produce();
		}

		this.lastScaledEnergyLevel = this.scaledEnergyLevel;
	}

	@Override
	public void openChest()
	{
	}

	@Override
	public void closeChest()
	{
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);

		NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
		this.containingItems = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
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
	public String getInvName()
	{
		return StatCollector.translateToLocal("tile.machine.1.name");
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
	{
		return itemstack.getItem() instanceof IItemElectric;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int slotID)
	{
		return new int[] { 0, 1 };
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemstack, int side)
	{
		if (this.isItemValidForSlot(slotID, itemstack))
		{
			if (slotID == 0)
			{
				return ((IItemElectric) itemstack.getItem()).getTransfer(itemstack) > 0;
			}
			else if (slotID == 1)
			{
				return ((IItemElectric) itemstack.getItem()).getElectricityStored(itemstack) > 0;
			}
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, int side)
	{
		if (this.isItemValidForSlot(slotID, itemstack))
		{
			if (slotID == 0)
			{
				return ((IItemElectric) itemstack.getItem()).getTransfer(itemstack) <= 0;
			}
			else if (slotID == 1)
			{
				return ((IItemElectric) itemstack.getItem()).getElectricityStored(itemstack) <= 0 || this.getEnergyStored() >= this.getMaxEnergyStored();
			}
		}

		return false;

	}

	@Override
	public float getRequest(ForgeDirection direction)
	{
		return this.getElectricalInputDirections().contains(direction) ? this.getMaxEnergyStored() - this.getEnergyStored() : 0;
	}

	@Override
	public float getProvide(ForgeDirection direction)
	{
		return this.getElectricalOutputDirections().contains(direction) ? Math.min(1.3F, this.getEnergyStored()) : 0;
	}

	@Override
	public EnumSet<ForgeDirection> getElectricalInputDirections()
	{
		return EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() - GCCoreBlockMachine.STORAGE_MODULE_METADATA + 2).getOpposite(), ForgeDirection.UNKNOWN);
	}

	@Override
	public EnumSet<ForgeDirection> getElectricalOutputDirections()
	{
		return EnumSet.of(ForgeDirection.getOrientation(this.getBlockMetadata() - GCCoreBlockMachine.STORAGE_MODULE_METADATA + 2), ForgeDirection.UNKNOWN);
	}

	@Override
	public float getMaxEnergyStored()
	{
		return 2500;
	}
}
