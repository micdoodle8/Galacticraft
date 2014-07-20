package micdoodle8.mods.galacticraft.planets.mars.tile;

import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.item.ItemElectric;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.tile.ElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemMethaneCanister;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityGasLiquefier extends ElectricBlockWithInventory implements ISidedInventory, IDisableableMachine, IFluidHandler
{
	private final int tankCapacity = 2000;

	@NetworkedField(targetSide = Side.CLIENT)
	public FluidTank gasTank = new FluidTank(this.tankCapacity);
	@NetworkedField(targetSide = Side.CLIENT)
	public FluidTank fuelTank = new FluidTank(this.tankCapacity);

	public static final int PROCESS_TIME_REQUIRED = 3;
	@NetworkedField(targetSide = Side.CLIENT)
	public int processTicks = 0;
	private ItemStack[] containingItems = new ItemStack[3];
	
	public TileEntityGasLiquefier()
	{
		this.storage.setMaxExtract(60);
		this.setTierGC(2);
	}

	@Override
	public void invalidate()
	{
		super.invalidate();
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			ItemStack inputCanister = this.containingItems[1];
			if (inputCanister != null)
			{
				FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(inputCanister);
				FluidStack gcMethane = FluidRegistry.getFluidStack(AsteroidsModule.fluidLiquidMethane.getName(), 1);

				if (liquid != null && FluidRegistry.getFluidName(liquid).toLowerCase().contains("methane"))
				{
					gcMethane.amount = liquid.amount;

					if (this.gasTank.getFluid() == null || this.gasTank.getFluid().amount + liquid.amount <= this.gasTank.getCapacity())
					{
						this.gasTank.fill(gcMethane, true);

						if (inputCanister.getItem() instanceof ItemMethaneCanister)
						{
							this.containingItems[1] = new ItemStack(AsteroidsItems.methaneCanister, 1, AsteroidsItems.methaneCanister.getMaxDamage());
						}
						else if (FluidContainerRegistry.isBucket(inputCanister) && FluidContainerRegistry.isFilledContainer(inputCanister))
						{
							final int amount = inputCanister.stackSize;
							this.containingItems[1] = new ItemStack(Items.bucket, amount);
						}
						else
						{
							inputCanister.stackSize--;

							if (inputCanister.stackSize == 0)
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

				if (liquid != null && liquid.getFluid().getName().equalsIgnoreCase("fuel"))
				{
					if (FluidContainerRegistry.isEmptyContainer(this.containingItems[2]))
					{
						boolean isCanister = this.containingItems[2].isItemEqual(new ItemStack(GCItems.fuelCanister, 1, GCItems.fuelCanister.getMaxDamage())) || this.containingItems[2].isItemEqual(new ItemStack(AsteroidsItems.methaneCanister, 1, AsteroidsItems.methaneCanister.getMaxDamage()));
						final int amountToFill = Math.min(liquid.amount, isCanister ? GCItems.fuelCanister.getMaxDamage() - 1 : FluidContainerRegistry.BUCKET_VOLUME);

						if (isCanister)
						{
							this.containingItems[2] = new ItemStack(GCItems.fuelCanister, 1, GCItems.fuelCanister.getMaxDamage() - amountToFill);
							this.fuelTank.drain(amountToFill, true);
						}
						else
						{
							ItemStack originalContainer = this.containingItems[2];
							this.containingItems[2] = FluidContainerRegistry.fillFluidContainer(liquid, this.containingItems[2]);
							if (this.containingItems[2] == null) this.containingItems[2] = originalContainer;
							else this.fuelTank.drain(amountToFill, true);
						}
					}
				}
			}

			if (this.canProcess() && this.hasEnoughEnergyToRun)
			{
				if (this.processTicks == 0)
				{
					this.processTicks = TileEntityGasLiquefier.PROCESS_TIME_REQUIRED;
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

	public int getScaledGasLevel(int i)
	{
		return this.gasTank.getFluid() != null ? this.gasTank.getFluid().amount * i / this.gasTank.getCapacity() : 0;
	}

	public int getScaledFuelLevel(int i)
	{
		return this.fuelTank.getFluid() != null ? this.fuelTank.getFluid().amount * i / this.fuelTank.getCapacity() : 0;
	}

	public boolean canProcess()
	{
		if (this.gasTank.getFluid() == null || this.gasTank.getFluid().amount <= 0)
		{
			return false;
		}

        return !this.getDisabled(0);

    }

	public void smeltItem()
	{
		if (this.canProcess())
		{
			final int gasAmount = this.gasTank.getFluid().amount;
			final int fuelSpace = this.fuelTank.getCapacity() - (this.fuelTank.getFluid() == null ? 0 : this.fuelTank.getFluid().amount);

			final int amountToDrain = Math.min(Math.min(gasAmount, fuelSpace), 2);

			this.gasTank.drain(amountToDrain, true);
			this.fuelTank.fill(FluidRegistry.getFluidStack("fuel", amountToDrain), true);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.processTicks = nbt.getInteger("smeltingTicks");
		this.containingItems = this.readStandardItemsFromNBT(nbt);

		if (nbt.hasKey("gasTank"))
		{
			this.gasTank.readFromNBT(nbt.getCompoundTag("gasTank"));
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
		this.writeStandardItemsToNBT(nbt);

		if (this.gasTank.getFluid() != null)
		{
			nbt.setTag("gasTank", this.gasTank.writeToNBT(new NBTTagCompound()));
		}

		if (this.fuelTank.getFluid() != null)
		{
			nbt.setTag("fuelTank", this.fuelTank.writeToNBT(new NBTTagCompound()));
		}
	}

	@Override
	protected ItemStack[] getContainingItems()
	{
		return this.containingItems;
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return true;
	}

	@Override
	public String getInventoryName()
	{
		return GCCoreUtil.translate("tile.marsMachine.4.name");
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
				return itemstack.getItem() instanceof ItemElectric && ((ItemElectric) itemstack.getItem()).getElectricityStored(itemstack) > 0;
			case 1:
				FluidStack stack = FluidContainerRegistry.getFluidForFilledItem(itemstack);
				return stack != null && stack.getFluid() != null && stack.getFluid().getName().toLowerCase().contains("methane");
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
				return itemstack.getItem() instanceof ItemElectric && ((ItemElectric) itemstack.getItem()).getElectricityStored(itemstack) <= 0 || !this.shouldPullEnergy();
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
			return ItemElectric.isElectricItem(itemstack.getItem());
		case 1:
			FluidStack stack = FluidContainerRegistry.getFluidForFilledItem(itemstack);
			return stack != null && stack.getFluid() != null && stack.getFluid().getName().toLowerCase().contains("methane") || FluidContainerRegistry.isContainer(itemstack);
		case 2:
			FluidStack stack2 = FluidContainerRegistry.getFluidForFilledItem(itemstack);
			return stack2 != null && stack2.getFluid() != null && stack2.getFluid().getName().equalsIgnoreCase("fuel") || FluidContainerRegistry.isContainer(itemstack);
		}

		return false;
	}

	@Override
	public boolean shouldUseEnergy()
	{
		return this.canProcess();
	}

	@Override
	public double getPacketRange()
	{
		return 320.0D;
	}

	@Override
	public ForgeDirection getElectricInputDirection()
	{
		return ForgeDirection.getOrientation((this.getBlockMetadata() & 3) + 2);
	}

	@Override
	public ItemStack getBatteryInSlot()
	{
		return this.getStackInSlot(1);
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
			return this.gasTank.getFluid() == null || this.gasTank.getFluidAmount() < this.gasTank.getCapacity();
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

			if (liquidName != null && liquidName.toLowerCase().contains("methane"))
			{
				used = this.gasTank.fill(resource, doFill);
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
			tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.gasTank) };
		}
		else if (from == ForgeDirection.getOrientation(this.getBlockMetadata() + 2))
		{
			tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.fuelTank) };
		}

		return tankInfo;
	}
}
