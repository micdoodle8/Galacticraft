package micdoodle8.mods.galacticraft.planets.mars.tile;

import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.item.ItemElectric;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityRefinery;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemMethaneCanister;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityGasLiquefier extends TileEntityElectricBlock implements IInventory, ISidedInventory, IDisableableMachine, IFluidHandler
{
	private final int tankCapacity = 2000;

	@NetworkedField(targetSide = Side.CLIENT)
	public FluidTank gasTank = new FluidTank(this.tankCapacity);
	@NetworkedField(targetSide = Side.CLIENT)
	public FluidTank fuelTank = new FluidTank(this.tankCapacity);

	public static final int PROCESS_TIME_REQUIRED = 2;
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
				System.out.println("Gas liquefier - found canister");
				FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(inputCanister);
				FluidStack gcMethane = FluidRegistry.getFluidStack(AsteroidsModule.fluidLiquidMethane.getName(), liquid.amount);

				if (liquid != null && FluidRegistry.getFluidName(liquid).toLowerCase().contains("methane"))
				{
					System.out.println("Gas liquefier - found methane canister");

					if (this.gasTank.getFluid() == null || this.gasTank.getFluid().amount + liquid.amount <= this.gasTank.getCapacity())
					{
						System.out.println("Gas liquefier - transferring liquid: "+gcMethane.amount);
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

				if (liquid != null && this.fuelTank.getFluid() != null && this.fuelTank.getFluid().getFluid().getName().equalsIgnoreCase("Fuel"))
				{
					if (FluidContainerRegistry.isEmptyContainer(this.containingItems[2]))
					{
						boolean isCanister = this.containingItems[2].isItemEqual(new ItemStack(AsteroidsItems.methaneCanister, 1, AsteroidsItems.methaneCanister.getMaxDamage()));
						final int amountToFill = Math.min(liquid.amount, isCanister ? GCItems.fuelCanister.getMaxDamage() - 1 : FluidContainerRegistry.BUCKET_VOLUME);

						if (isCanister)
						{
							this.containingItems[2] = new ItemStack(GCItems.fuelCanister, 1, GCItems.fuelCanister.getMaxDamage() - amountToFill);
						}
						else
						{
							this.containingItems[2] = FluidContainerRegistry.fillFluidContainer(liquid, this.containingItems[2]);
						}

						this.fuelTank.drain(amountToFill, true);
					}
				}
			}

			if (this.canProcess() && this.hasEnoughEnergyToRun)
			{
				if (this.processTicks == 0)
				{
					this.processTicks = TileEntityRefinery.PROCESS_TIME_REQUIRED;
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

			final int amountToDrain = Math.min(Math.min(gasAmount, fuelSpace), TileEntityRefinery.OUTPUT_PER_SECOND);

			this.gasTank.drain(amountToDrain, true);
			this.fuelTank.fill(FluidRegistry.getFluidStack("fuel", amountToDrain), true);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.processTicks = nbt.getInteger("smeltingTicks");
		final NBTTagList var2 = nbt.getTagList("Items", 10);
		this.containingItems = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
			final byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < this.containingItems.length)
			{
				this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}

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
	public String getInventoryName()
	{
		return GCCoreUtil.translate("tile.marsMachine.4.name");
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
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
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && entityplayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return true;
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
	public void openInventory()
	{
	}

	@Override
	public void closeInventory()
	{
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
