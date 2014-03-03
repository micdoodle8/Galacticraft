package micdoodle8.mods.galacticraft.core.tile;

import java.util.EnumSet;

import micdoodle8.mods.galacticraft.api.transmission.core.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockOxygenCompressor;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;

/**
 * GCCoreTileEntityOxygenDecompressor.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreTileEntityOxygenDecompressor extends GCCoreTileEntityOxygen implements IInventory, ISidedInventory
{
	private ItemStack[] containingItems = new ItemStack[2];

	public static final float WATTS_PER_TICK = 0.2F;

	public static final int OUTPUT_PER_TICK = 100;

	public GCCoreTileEntityOxygenDecompressor()
	{
		super(GCCoreTileEntityOxygenDecompressor.WATTS_PER_TICK, 50, 1200, 0);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			if (this.containingItems[0] != null && this.getEnergyStored() > 0.0F && this.getOxygenStored() < this.getMaxOxygenStored())
			{
				ItemStack tank1 = this.containingItems[0];

				if (tank1.getItem() instanceof GCCoreItemOxygenTank && tank1.getItemDamage() < tank1.getMaxDamage())
				{
					tank1.setItemDamage(tank1.getItemDamage() + 1);
					this.receiveOxygen(1, true);
				}
			}

			this.produceOxygen();
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);

		final NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
		this.containingItems = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			final NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
			final byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.containingItems.length)
			{
				this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);

		final NBTTagList list = new NBTTagList();

		for (int var3 = 0; var3 < this.containingItems.length; ++var3)
		{
			if (this.containingItems[var3] != null)
			{
				final NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.containingItems[var3].writeToNBT(var4);
				list.appendTag(var4);
			}
		}

		par1NBTTagCompound.setTag("Items", list);
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
			final ItemStack var2 = this.containingItems[par1];
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
		return StatCollector.translateToLocal("container.oxygendecompressor.name");
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
	public void openChest()
	{
	}

	@Override
	public void closeChest()
	{
	}

	// ISidedInventory Implementation:

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return new int[] { 0, 1 };
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemstack, int side)
	{
		if (this.isItemValidForSlot(slotID, itemstack))
		{
			switch (slotID)
			{
			case 0:
				return itemstack.getItemDamage() < itemstack.getMaxDamage();
			case 1:
				return ((IItemElectric) itemstack.getItem()).getElectricityStored(itemstack) > 0;
			default:
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, int side)
	{
		if (this.isItemValidForSlot(slotID, itemstack))
		{
			switch (slotID)
			{
			case 0:
				return itemstack.getItemDamage() == itemstack.getMaxDamage();
			case 1:
				return ((IItemElectric) itemstack.getItem()).getElectricityStored(itemstack) <= 0;
			default:
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
	{
		switch (slotID)
		{
		case 0:
			return itemstack.getItem() instanceof GCCoreItemOxygenTank;
		case 1:
			return itemstack.getItem() instanceof IItemElectric;
		}

		return false;
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return true;
	}

	@Override
	public boolean shouldPullEnergy()
	{
		return this.getEnergyStored() <= this.getMaxEnergyStored() - this.ueWattsPerTick;
	}

	@Override
	public boolean shouldUseEnergy()
	{
		return GCCoreTileEntityOxygen.timeSinceOxygenRequest > 0 && this.getStackInSlot(0) != null;
	}

	@Override
	public ForgeDirection getElectricInputDirection()
	{
		return ForgeDirection.getOrientation(this.getBlockMetadata() - GCCoreBlockOxygenCompressor.OXYGEN_DECOMPRESSOR_METADATA + 2);
	}

	@Override
	public ItemStack getBatteryInSlot()
	{
		return this.getStackInSlot(1);
	}

	public ForgeDirection getOxygenOutputDirection()
	{
		return this.getElectricInputDirection().getOpposite();
	}

	@Override
	public EnumSet<ForgeDirection> getOxygenInputDirections()
	{
		return EnumSet.noneOf(ForgeDirection.class);
	}

	@Override
	public EnumSet<ForgeDirection> getOxygenOutputDirections()
	{
		return EnumSet.of(this.getElectricInputDirection().getOpposite());
	}

	@Override
	public boolean shouldPullOxygen()
	{
		return false;
	}

	@Override
	public boolean shouldUseOxygen()
	{
		return false;
	}

	@Override
	public float getOxygenProvide(ForgeDirection direction)
	{
		return this.getOxygenOutputDirections().contains(direction) ? Math.min(GCCoreTileEntityOxygenDecompressor.OUTPUT_PER_TICK, this.getOxygenStored()) : 0.0F;
	}
}
