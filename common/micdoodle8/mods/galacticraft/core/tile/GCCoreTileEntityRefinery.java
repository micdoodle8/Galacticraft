package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.transmission.core.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.GCCoreAnnotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOilCanister;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import cpw.mods.fml.relauncher.Side;

/**
 * GCCoreTileEntityRefinery.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreTileEntityRefinery extends GCCoreTileEntityElectricBlock implements IInventory, ISidedInventory, IFluidHandler
{
	private final int tankCapacity = 24000;
	@NetworkedField(targetSide = Side.CLIENT)
	public FluidTank oilTank = new FluidTank(this.tankCapacity);
	@NetworkedField(targetSide = Side.CLIENT)
	public FluidTank fuelTank = new FluidTank(this.tankCapacity);

	public static final float WATTS_PER_TICK = 0.4F;
	public static final int PROCESS_TIME_REQUIRED = 2;
	public static final int OUTPUT_PER_SECOND = 1;
	@NetworkedField(targetSide = Side.CLIENT)
	public int processTicks = 0;
	private ItemStack[] containingItems = new ItemStack[3];

	public GCCoreTileEntityRefinery()
	{
		super(GCCoreTileEntityRefinery.WATTS_PER_TICK, 50);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			if (this.containingItems[1] != null)
			{
				FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(this.containingItems[1]);

				if (liquid != null && FluidRegistry.getFluidName(liquid).equalsIgnoreCase("Oil"))
				{
					if (this.oilTank.getFluid() == null || this.oilTank.getFluid().amount + liquid.amount <= this.oilTank.getCapacity())
					{
						this.oilTank.fill(liquid, true);

						if (this.containingItems[1].getItem() instanceof GCCoreItemOilCanister)
						{
							this.containingItems[1] = new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.oilCanister.getMaxDamage());
						}
						else if (FluidContainerRegistry.isBucket(this.containingItems[1]) && FluidContainerRegistry.isFilledContainer(this.containingItems[1]))
						{
							final int amount = this.containingItems[1].stackSize;
							this.containingItems[1] = new ItemStack(Item.bucketEmpty, amount);
						}
						else
						{
							this.containingItems[1].stackSize--;

							if (this.containingItems[1].stackSize == 0)
							{
								this.containingItems[1] = null;
							}
						}
					}
				}
			}

			if (this.containingItems[2] != null && FluidContainerRegistry.isContainer(this.containingItems[2]))
			{
				final FluidStack liquid = this.fuelTank.getFluid();

				if (liquid != null && this.fuelTank.getFluid() != null && this.fuelTank.getFluid().getFluid().getName().equalsIgnoreCase("Fuel"))
				{
					if (FluidContainerRegistry.isEmptyContainer(this.containingItems[2]))
					{
						boolean isCanister = this.containingItems[2].isItemEqual(new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.oilCanister.getMaxDamage()));
						final int amountToFill = Math.min(liquid.amount, isCanister ? GCCoreItems.fuelCanister.getMaxDamage() - 1 : FluidContainerRegistry.BUCKET_VOLUME);

						if (isCanister)
						{
							this.containingItems[2] = new ItemStack(GCCoreItems.fuelCanister, 1, GCCoreItems.fuelCanister.getMaxDamage() - amountToFill);
						}
						else
						{
							this.containingItems[2] = FluidContainerRegistry.fillFluidContainer(liquid, this.containingItems[2]);
						}

						this.fuelTank.drain(amountToFill, true);
					}
				}
			}

			if (this.canProcess())
			{
				if (this.processTicks == 0)
				{
					this.processTicks = GCCoreTileEntityRefinery.PROCESS_TIME_REQUIRED;
				}
				else if (this.processTicks > 0)
				{
					this.processTicks--;

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
		}
	}

	public int getScaledOilLevel(int i)
	{
		return this.oilTank.getFluid() != null ? this.oilTank.getFluid().amount * i / this.oilTank.getCapacity() : 0;
	}

	public int getScaledFuelLevel(int i)
	{
		return this.fuelTank.getFluid() != null ? this.fuelTank.getFluid().amount * i / this.fuelTank.getCapacity() : 0;
	}

	@Override
	public void openChest()
	{
	}

	@Override
	public void closeChest()
	{
	}

	public boolean canProcess()
	{
		if (this.oilTank.getFluid() == null || this.oilTank.getFluid().amount <= 0)
		{
			return false;
		}

		if (this.getDisabled(0))
		{
			return false;
		}

		if (this.getEnergyStored() == 0)
		{
			return false;
		}

		return true;
	}

	public void smeltItem()
	{
		if (this.canProcess())
		{
			final int oilAmount = this.oilTank.getFluid().amount;
			final int fuelSpace = this.fuelTank.getCapacity() - (this.fuelTank.getFluid() == null ? 0 : this.fuelTank.getFluid().amount);

			final int amountToDrain = Math.min(Math.min(oilAmount, fuelSpace), GCCoreTileEntityRefinery.OUTPUT_PER_SECOND);

			this.oilTank.drain(amountToDrain, true);
			this.fuelTank.fill(FluidRegistry.getFluidStack("fuel", amountToDrain), true);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.processTicks = nbt.getInteger("smeltingTicks");
		final NBTTagList var2 = nbt.getTagList("Items");
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

		if (nbt.hasKey("oilTank"))
		{
			this.oilTank.readFromNBT(nbt.getCompoundTag("oilTank"));
		}

		if (nbt.hasKey("fuelTank"))
		{
			this.fuelTank.readFromNBT(nbt.getCompoundTag("fuelTank"));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("smeltingTicks", this.processTicks);
		final NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.containingItems.length; ++var3)
		{
			if (this.containingItems[var3] != null)
			{
				final NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.containingItems[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		nbt.setTag("Items", var2);

		if (this.oilTank.getFluid() != null)
		{
			nbt.setTag("oilTank", this.oilTank.writeToNBT(new NBTTagCompound()));
		}

		if (this.fuelTank.getFluid() != null)
		{
			nbt.setTag("fuelTank", this.fuelTank.writeToNBT(new NBTTagCompound()));
		}
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
		return StatCollector.translateToLocal("container.refinery.name");
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return true;
	}

	// ISidedInventory Implementation:

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return new int[] { 0, 1, 2 };
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemstack, int side)
	{
		if (this.isItemValidForSlot(slotID, itemstack))
		{
			switch (slotID)
			{
			case 0:
				return ((IItemElectric) itemstack.getItem()).getElectricityStored(itemstack) > 0;
			case 1:
				FluidStack stack = FluidContainerRegistry.getFluidForFilledItem(itemstack);
				return stack != null && stack.getFluid() != null && stack.getFluid().getName().equalsIgnoreCase("oil");
			case 2:
				return FluidContainerRegistry.isEmptyContainer(itemstack);
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
				return ((IItemElectric) itemstack.getItem()).getElectricityStored(itemstack) <= 0 || !this.shouldPullEnergy();
			case 1:
				return FluidContainerRegistry.isEmptyContainer(itemstack);
			case 2:
				FluidStack stack = FluidContainerRegistry.getFluidForFilledItem(itemstack);
				return stack != null && stack.getFluid() != null && stack.getFluid().getName().equalsIgnoreCase("fuel");
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
			return itemstack.getItem() instanceof IItemElectric;
		case 1:
			FluidStack stack = FluidContainerRegistry.getFluidForFilledItem(itemstack);
			return stack != null && stack.getFluid() != null && stack.getFluid().getName().equalsIgnoreCase("oil") || FluidContainerRegistry.isContainer(itemstack);
		case 2:
			FluidStack stack2 = FluidContainerRegistry.getFluidForFilledItem(itemstack);
			return stack2 != null && stack2.getFluid() != null && stack2.getFluid().getName().equalsIgnoreCase("fuel") || FluidContainerRegistry.isContainer(itemstack);
		}

		return false;
	}

	@Override
	public boolean shouldPullEnergy()
	{
		return this.getEnergyStored() <= this.getMaxEnergyStored() - this.ueWattsPerTick;
	}

	@Override
	public boolean shouldUseEnergy()
	{
		return this.canProcess();
	}

	@Override
	public ForgeDirection getElectricInputDirection()
	{
		return ForgeDirection.UP;
	}

	@Override
	public ItemStack getBatteryInSlot()
	{
		return this.getStackInSlot(0);
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		if (from.equals(ForgeDirection.getOrientation(this.getBlockMetadata() + 2)))
		{
			return this.fuelTank.getFluid() != null && this.fuelTank.getFluidAmount() > 0;
		}

		return false;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		if (from.equals(ForgeDirection.getOrientation(this.getBlockMetadata() + 2)))
		{
			return this.fuelTank.drain(resource.amount, doDrain);
		}

		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		if (from.equals(ForgeDirection.getOrientation(this.getBlockMetadata() + 2)))
		{
			return this.drain(from, new FluidStack(GalacticraftCore.fluidFuel, maxDrain), doDrain);
		}

		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		if (from.equals(ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite()))
		{
			return this.oilTank.getFluid() == null || this.oilTank.getFluidAmount() < this.oilTank.getCapacity();
		}

		return false;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		int used = 0;

		if (from.equals(ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite()))
		{
			final String liquidName = FluidRegistry.getFluidName(resource);

			if (liquidName != null && liquidName.equalsIgnoreCase("Oil"))
			{
				used = this.oilTank.fill(resource, doFill);
			}
		}

		return used;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		FluidTankInfo[] tankInfo = new FluidTankInfo[] {};

		if (from == ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite())
		{
			tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.oilTank) };
		}
		else if (from == ForgeDirection.getOrientation(this.getBlockMetadata() + 2))
		{
			tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.fuelTank) };
		}

		return tankInfo;
	}
}
