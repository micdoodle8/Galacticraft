package micdoodle8.mods.galacticraft.planets.mars.tile;

import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.item.ItemElectric;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.core.tile.ElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemAtmosphericValve;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemCanisterLiquidOxygen;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemCanisterMethane;
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

	public int processTimeRequired = 3;
	@NetworkedField(targetSide = Side.CLIENT)
	public int processTicks = 0;
	private ItemStack[] containingItems = new ItemStack[3];
	
	@NetworkedField(targetSide = Side.CLIENT)
	public int gasTankType = -1;
	@NetworkedField(targetSide = Side.CLIENT)
	public int fluidTankType = -1;
	
	public enum TankGases
	{
		METHANE(0, "methane", "fuel"),
		OXYGEN(1, "oxygen", "liquidoxygen"),
		AIR(2, "atmosphericgases", "liquidnitrogen");

		int index;
		String gas;
		String liquid;

		TankGases(int id, String fluidname, String outputname)
		{
			this.index = id;
			this.gas = new String(fluidname);
			this.liquid = new String(outputname);
		}
	}

	
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
			FluidStack currentgas = this.gasTank.getFluid();
			if (currentgas == null || currentgas.amount <= 0) this.gasTankType = -1;
			else this.gasTankType = this.getIdFromName(currentgas.getFluid().getName());

			FluidStack currentLiquid = this.fuelTank.getFluid();
			if (currentLiquid == null || currentLiquid.amount == 0) this.fluidTankType = -1;
			else this.fluidTankType = this.getFluidIdFromName(currentLiquid.getFluid().getName());
			
			//First, see if any gas needs to be put into the gas storage
			ItemStack inputCanister = this.containingItems[1];
			if (inputCanister != null)
			{
				if (inputCanister.getItem() instanceof ItemAtmosphericValve)
				{
					//Air -> Air tank
					if (this.gasTankType == -1 || (this.gasTankType == 2 && this.gasTank.getFluid().amount < this.gasTank.getCapacity()))
					{
						FluidStack gcAtmosphere = FluidRegistry.getFluidStack(TankGases.AIR.gas, 4);
						this.gasTank.fill(gcAtmosphere, true);
						this.gasTankType = 2;
					}
				}
				else
				{
					FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(inputCanister);
					if (liquid != null && liquid.amount > 0)
					{
						String inputName = FluidRegistry.getFluidName(liquid);
						System.out.println("methanetest 2");
						//Methane -> Methane tank
						if (this.gasTankType <= 0 && inputName.contains("methane"))
						{
							System.out.println("methanetest 3");
							if (currentgas == null || currentgas.amount + liquid.amount <= this.gasTank.getCapacity())
							{
								System.out.println("methanetest 4");
								FluidStack gcMethane = FluidRegistry.getFluidStack(TankGases.METHANE.gas, liquid.amount);	
								this.gasTank.fill(gcMethane, true);
								this.gasTankType = 0;
		
								if (inputCanister.getItem() instanceof ItemCanisterMethane)
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
						else 						//Oxygen -> Oxygen tank
						if ((this.gasTankType == 1 || this.gasTankType == -1) && inputName.contains("oxygen"))
						{
							if (currentgas == null || currentgas.amount + liquid.amount <= this.gasTank.getCapacity())
							{
								FluidStack gcOxygen = FluidRegistry.getFluidStack(TankGases.OXYGEN.gas, liquid.amount);	
								this.gasTank.fill(gcOxygen, true);
								this.gasTankType = 1;
		
								if (inputCanister.getItem() instanceof ItemCanisterLiquidOxygen)
								{
									this.containingItems[1] = new ItemStack(AsteroidsItems.canisterLOX, 1, AsteroidsItems.canisterLOX.getMaxDamage());
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
				}
			}

			//Now see if any fuel from the fuel tank needs to be put into the output slot
			if (this.containingItems[2] != null && FluidContainerRegistry.isEmptyContainer(this.containingItems[2]))
			{
				final FluidStack liquid = this.fuelTank.getFluid();

				if (liquid != null && liquid.getFluid().getName().equalsIgnoreCase(TankGases.METHANE.liquid))
				{
					ItemStack slotItem = this.containingItems[2];
					boolean isCanister = slotItem.getItem() instanceof ItemCanisterGeneric && slotItem.getItemDamage() == GCItems.fuelCanister.getMaxDamage();
					final int amountToFill = Math.min(liquid.amount, isCanister ? GCItems.fuelCanister.getMaxDamage() - 1 : FluidContainerRegistry.BUCKET_VOLUME);

					if (isCanister)
					{
						this.containingItems[2] = new ItemStack(GCItems.fuelCanister, 1, GCItems.fuelCanister.getMaxDamage() - amountToFill);
						this.fuelTank.drain(amountToFill, true);
					}
					else
					{
						this.containingItems[2] = FluidContainerRegistry.fillFluidContainer(liquid, this.containingItems[2]);
						if (this.containingItems[2] == null) this.containingItems[2] = slotItem;
						else this.fuelTank.drain(amountToFill, true);
					}
				}
				else
				if (liquid != null && liquid.getFluid().getName().equalsIgnoreCase(TankGases.OXYGEN.liquid))
				{
					ItemStack slotItem = this.containingItems[2];
					boolean isCanister = slotItem.getItem() instanceof ItemCanisterGeneric && slotItem.getItemDamage() == GCItems.fuelCanister.getMaxDamage();
					final int amountToFill = Math.min(liquid.amount, isCanister ? GCItems.fuelCanister.getMaxDamage() - 1 : FluidContainerRegistry.BUCKET_VOLUME);

					if (isCanister)
					{
						this.containingItems[2] = new ItemStack(AsteroidsItems.canisterLOX, 1, AsteroidsItems.canisterLOX.getMaxDamage() - amountToFill);
						this.fuelTank.drain(amountToFill, true);
					}
					else
					{
						this.containingItems[2] = FluidContainerRegistry.fillFluidContainer(liquid, this.containingItems[2]);
						if (this.containingItems[2] == null) this.containingItems[2] = slotItem;
						else this.fuelTank.drain(amountToFill, true);
					}
				}
				else
				if (liquid != null && liquid.getFluid().getName().equalsIgnoreCase(TankGases.AIR.liquid))
				{
					ItemStack slotItem = this.containingItems[2];
					boolean isCanister = slotItem.getItem() instanceof ItemCanisterGeneric && slotItem.getItemDamage() == GCItems.fuelCanister.getMaxDamage();
					final int amountToFill = Math.min(liquid.amount, isCanister ? GCItems.fuelCanister.getMaxDamage() - 1 : FluidContainerRegistry.BUCKET_VOLUME);

					if (isCanister)
					{
						this.containingItems[2] = new ItemStack(AsteroidsItems.canisterLN2, 1, AsteroidsItems.canisterLN2.getMaxDamage() - amountToFill);
						this.fuelTank.drain(amountToFill, true);
					}
					else
					{
						this.containingItems[2] = FluidContainerRegistry.fillFluidContainer(liquid, this.containingItems[2]);
						if (this.containingItems[2] == null) this.containingItems[2] = slotItem;
						else this.fuelTank.drain(amountToFill, true);
					}
				}
			}

			if (this.canProcess() && this.hasEnoughEnergyToRun)
			{
				//50% extra speed boost for Tier 2 machine if powered by Tier 2 power 
				if (this.tierGC == 2) this.processTimeRequired = (this.poweredByTierGC == 2) ? 2 : 3;
				
				if (this.processTicks == 0)
				{
					this.processTicks = this.processTimeRequired;
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

	public int getIdFromName(String gasname)
	{
		for (TankGases type : TankGases.values())
		{
			if (type.gas.equals(gasname))
			{
				return type.index;
			}
		}

		return -1;
	}

	public int getFluidIdFromName(String gasname)
	{
		for (TankGases type : TankGases.values())
		{
			if (type.liquid.equals(gasname))
			{
				return type.index;
			}
		}

		return -1;
	}

	//private boolean gasTankMatches(TankGases tankgas)
	//{
	//}

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
		
		if (this.gasTankType == this.fluidTankType || this.fluidTankType == -1)
			return !this.getDisabled(0);

		return false;
    }

	public void smeltItem()
	{
		if (this.canProcess())
		{
			final int gasAmount = this.gasTank.getFluid().amount;
			final int fuelSpace = this.fuelTank.getCapacity() - (this.fuelTank.getFluid() == null ? 0 : this.fuelTank.getFluid().amount);

			final int amountToDrain = Math.min(Math.min(gasAmount, fuelSpace), 3);

			this.gasTank.drain(amountToDrain, true);
			this.fuelTank.fill(FluidRegistry.getFluidStack(TankGases.values()[this.gasTankType].liquid, amountToDrain), true);
			this.fluidTankType = this.gasTankType;
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
