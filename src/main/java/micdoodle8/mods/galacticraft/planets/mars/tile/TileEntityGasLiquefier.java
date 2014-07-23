package micdoodle8.mods.galacticraft.planets.mars.tile;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.item.ItemElectric;
import micdoodle8.mods.galacticraft.api.world.IAtmosphericGas;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.core.tile.ElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemAtmosphericValve;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemCanisterLiquidNitrogen;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemCanisterLiquidOxygen;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemCanisterMethane;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldProvider;
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
	public FluidTank gasTank = new FluidTank(this.tankCapacity * 2);
	@NetworkedField(targetSide = Side.CLIENT)
	public FluidTank fuelTank = new FluidTank(this.tankCapacity);
	@NetworkedField(targetSide = Side.CLIENT)
	public FluidTank fuelTank2 = new FluidTank(this.tankCapacity);

	public int processTimeRequired = 3;
	@NetworkedField(targetSide = Side.CLIENT)
	public int processTicks = 0;
	private ItemStack[] containingItems = new ItemStack[4];
	private int airProducts = -1;
	
	@NetworkedField(targetSide = Side.CLIENT)
	public int gasTankType = -1;
	@NetworkedField(targetSide = Side.CLIENT)
	public int fluidTankType = -1;
	@NetworkedField(targetSide = Side.CLIENT)
	public int fluidTank2Type = -1;
	
	public enum TankGases
	{
		METHANE(0, "methane", "fuel"),
		OXYGEN(1, "oxygen", "liquidoxygen"),
		NITROGEN(2, "nitrogen", "liquidnitrogen"),
		ARGON(3, "argon", "liquidargon"),
		AIR(4, "atmosphericgases", "xxyyzz");

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

			if (this.airProducts == -1)
			{
				this.airProducts = this.getAirProducts();
				//If somehow it has air in an airless dimension, flush it out
				if (this.airProducts == 0 && this.gasTankType == TankGases.AIR.index)
				{
					this.gasTank.drain(this.gasTank.getFluidAmount(), true);
				}
			}
			
			FluidStack currentLiquid = this.fuelTank.getFluid();
			if (currentLiquid == null || currentLiquid.amount == 0) this.fluidTankType = -1;
			else this.fluidTankType = this.getProductIdFromName(currentLiquid.getFluid().getName());

			currentLiquid = this.fuelTank2.getFluid();
			if (currentLiquid == null || currentLiquid.amount == 0) this.fluidTank2Type = -1;
			else this.fluidTank2Type = this.getProductIdFromName(currentLiquid.getFluid().getName());

			//First, see if any gas needs to be put into the gas storage
			ItemStack inputCanister = this.containingItems[1];
			if (inputCanister != null)
			{
				if (inputCanister.getItem() instanceof ItemAtmosphericValve && this.airProducts > 0)
				{
					//Air -> Air tank
					if (this.gasTankType == -1 || (this.gasTankType == TankGases.AIR.index && this.gasTank.getFluid().amount < this.gasTank.getCapacity()))
					{
						FluidStack gcAtmosphere = FluidRegistry.getFluidStack(TankGases.AIR.gas, 4);
						this.gasTank.fill(gcAtmosphere, true);
						this.gasTankType = TankGases.AIR.index;
					}
				}
				else
				{
					FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(inputCanister);
					if (liquid != null && liquid.amount > 0)
					{
						String inputName = FluidRegistry.getFluidName(liquid);
						//Methane -> Methane tank
						if (this.gasTankType <= 0 && inputName.contains("methane"))
						{
							if (currentgas == null || currentgas.amount + liquid.amount * 2  <= this.gasTank.getCapacity())
							{
								FluidStack gcMethane = FluidRegistry.getFluidStack(TankGases.METHANE.gas, liquid.amount * 2);	
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
						if ((this.gasTankType == TankGases.OXYGEN.index || this.gasTankType == -1) && inputName.contains("oxygen"))
						{
							if (currentgas == null || currentgas.amount + liquid.amount * 2 <= this.gasTank.getCapacity())
							{
								FluidStack gcgas = FluidRegistry.getFluidStack(TankGases.OXYGEN.gas, liquid.amount * 2);	
								this.gasTank.fill(gcgas, true);
								this.gasTankType = TankGases.OXYGEN.index;
		
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
						else 						//Nitrogen -> Nitrogen tank
						if ((this.gasTankType == TankGases.NITROGEN.index || this.gasTankType == -1) && inputName.contains("nitrogen"))
						{
							if (currentgas == null || currentgas.amount + liquid.amount * 2  <= this.gasTank.getCapacity())
							{
								FluidStack gcgas = FluidRegistry.getFluidStack(TankGases.NITROGEN.gas, liquid.amount * 2);
								this.gasTank.fill(gcgas, true);
								this.gasTankType = TankGases.NITROGEN.index;
		
								if (inputCanister.getItem() instanceof ItemCanisterLiquidNitrogen)
								{
									this.containingItems[1] = new ItemStack(AsteroidsItems.canisterLN2, 1, AsteroidsItems.canisterLN2.getMaxDamage());
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
			checkFluidTankTransfer(2, this.fuelTank);
			checkFluidTankTransfer(3, this.fuelTank2);		

			if (this.hasEnoughEnergyToRun && this.canProcess())
			{
				//50% extra speed boost for Tier 2 machine if powered by Tier 2 power 
				if (this.tierGC == 2) this.processTimeRequired = (this.poweredByTierGC == 2) ? 2 : 3;
				
				if (this.processTicks == 0)
				{
					this.processTicks = this.processTimeRequired;
				}
				else
				{
					if (--this.processTicks <= 0)
					{
						this.doLiquefaction();
						this.processTicks = this.canProcess() ? this.processTimeRequired : 0;
					}
				}
			}
			else
			{
				this.processTicks = 0;
			}
		}
	}
	
	private void checkFluidTankTransfer(int slot, FluidTank tank)
	{
		if (this.containingItems[slot] != null && FluidContainerRegistry.isEmptyContainer(this.containingItems[slot]))
		{

			final FluidStack liquid = tank.getFluid();

			if (liquid != null && liquid.getFluid().getName().equalsIgnoreCase(TankGases.METHANE.liquid))
			{
				ItemStack slotItem = this.containingItems[slot];
				boolean isCanister = slotItem.getItem() instanceof ItemCanisterGeneric && slotItem.getItemDamage() == GCItems.fuelCanister.getMaxDamage();
				final int amountToFill = Math.min(liquid.amount, isCanister ? GCItems.fuelCanister.getMaxDamage() - 1 : FluidContainerRegistry.BUCKET_VOLUME);

				if (isCanister)
				{
					this.containingItems[slot] = new ItemStack(GCItems.fuelCanister, 1, GCItems.fuelCanister.getMaxDamage() - amountToFill);
					tank.drain(amountToFill, true);
				}
				else
				{
					this.containingItems[slot] = FluidContainerRegistry.fillFluidContainer(liquid, this.containingItems[slot]);
					if (this.containingItems[slot] == null) this.containingItems[slot] = slotItem;
					else tank.drain(amountToFill, true);
				}
			}
			else
			if (liquid != null && liquid.getFluid().getName().equalsIgnoreCase(TankGases.OXYGEN.liquid))
			{
				ItemStack slotItem = this.containingItems[slot];
				boolean isCanister = slotItem.getItem() instanceof ItemCanisterGeneric && slotItem.getItemDamage() == GCItems.fuelCanister.getMaxDamage();
				final int amountToFill = Math.min(liquid.amount, isCanister ? GCItems.fuelCanister.getMaxDamage() - 1 : FluidContainerRegistry.BUCKET_VOLUME);

				if (isCanister)
				{
					this.containingItems[slot] = new ItemStack(AsteroidsItems.canisterLOX, 1, AsteroidsItems.canisterLOX.getMaxDamage() - amountToFill);
					tank.drain(amountToFill, true);
				}
				else
				{
					this.containingItems[slot] = FluidContainerRegistry.fillFluidContainer(liquid, this.containingItems[slot]);
					if (this.containingItems[slot] == null) this.containingItems[slot] = slotItem;
					else tank.drain(amountToFill, true);
				}
			}
			else
			if (liquid != null && liquid.getFluid().getName().equalsIgnoreCase("liquidnitrogen"))
			{
				ItemStack slotItem = this.containingItems[slot];
				boolean isCanister = slotItem.getItem() instanceof ItemCanisterGeneric && slotItem.getItemDamage() == GCItems.fuelCanister.getMaxDamage();
				final int amountToFill = Math.min(liquid.amount, isCanister ? GCItems.fuelCanister.getMaxDamage() - 1 : FluidContainerRegistry.BUCKET_VOLUME);

				if (isCanister)
				{
					this.containingItems[slot] = new ItemStack(AsteroidsItems.canisterLN2, 1, AsteroidsItems.canisterLN2.getMaxDamage() - amountToFill);
					tank.drain(amountToFill, true);
				}
				else
				{
					this.containingItems[slot] = FluidContainerRegistry.fillFluidContainer(liquid, this.containingItems[slot]);
					if (this.containingItems[slot] == null) this.containingItems[slot] = slotItem;
					else tank.drain(amountToFill, true);
				}
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

	public int getProductIdFromName(String gasname)
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

	public int getScaledGasLevel(int i)
	{
		return this.gasTank.getFluid() != null ? this.gasTank.getFluid().amount * i / this.gasTank.getCapacity() : 0;
	}

	public int getScaledFuelLevel(int i)
	{
		return this.fuelTank.getFluid() != null ? this.fuelTank.getFluid().amount * i / this.fuelTank.getCapacity() : 0;
	}

	public int getScaledFuelLevel2(int i)
	{
		return this.fuelTank2.getFluid() != null ? this.fuelTank2.getFluid().amount * i / this.fuelTank2.getCapacity() : 0;
	}

	public boolean canProcess()
	{
		if (this.gasTank.getFluid() == null || this.gasTank.getFluid().amount <= 0 || this.getDisabled(0))
		{
			return false;
		}

		if (this.fluidTankType == -1 || this.fluidTank2Type == -1)
			return true;
		 
		if (this.gasTankType == TankGases.AIR.index)
		{
			int airProducts = this.airProducts;
			do
			{
				int thisProduct = (airProducts & 15) - 1;			
				if (thisProduct == this.fluidTankType || thisProduct == this.fluidTank2Type)
					return true;
				airProducts = airProducts >> 4;
			}
			while (airProducts > 0);
			return false;		
		}
		
		if (this.gasTankType == this.fluidTankType || this.gasTankType == this.fluidTank2Type)
			return true;

		return false;
    }
	
	public int getAirProducts()
	{
		WorldProvider WP = this.worldObj.provider;
		if (WP instanceof WorldProviderSpace)
		{
			int result = 0;
			ArrayList<IAtmosphericGas> atmos = ((WorldProviderSpace)WP).getCelestialBody().atmosphere;
			if (atmos.size() > 0)
			{	
				result = this.getIdFromName(atmos.get(0).name().toLowerCase()) + 1;
			}
			if (atmos.size() > 1)
			{	
				result += 16 * (this.getIdFromName(atmos.get(1).name().toLowerCase()) + 1);
			}
			
			return result;
		}	

		return 35;
	}

	public void doLiquefaction()
	{
		//Can't be called if the gasTank fluid is null
		final int gasAmount = this.gasTank.getFluid().amount;
		if (gasAmount == 0) return;

		if (this.gasTankType == TankGases.AIR.index)
		{
			int airProducts = this.airProducts;
			int amountToDrain = Math.min(gasAmount / 2, (airProducts > 15) ? 2 : 3);
			if (amountToDrain == 0) amountToDrain = 1;

			do
			{
				int thisProduct = (airProducts & 15) - 1;			
				this.gasTank.drain(this.placeIntoFluidTanks(thisProduct, amountToDrain) * 2, true);
				airProducts = airProducts >> 4;
				amountToDrain = amountToDrain >> 1;
				if (amountToDrain == 0) break;
			}
			while (airProducts > 0);
		}
		else
		{
			if (gasAmount == 1)
				this.gasTank.drain(this.placeIntoFluidTanks(this.gasTankType, 1), true);
			else
				this.gasTank.drain(this.placeIntoFluidTanks(this.gasTankType, Math.min(gasAmount / 2, 3)) * 2, true);
		}
	}
	
	private int placeIntoFluidTanks(int thisProduct, int amountToDrain)
	{
		final int fuelSpace = this.fuelTank.getCapacity() - this.fuelTank.getFluidAmount();
		final int fuelSpace2 = this.fuelTank2.getCapacity() - this.fuelTank2.getFluidAmount();

		if ((thisProduct == this.fluidTankType || this.fluidTankType == -1) && fuelSpace > 0)
		{
			if (amountToDrain > fuelSpace) amountToDrain = fuelSpace;
			this.fuelTank.fill(FluidRegistry.getFluidStack(TankGases.values()[thisProduct].liquid, amountToDrain), true);
			this.fluidTankType = thisProduct;
		} else
		if ((thisProduct == this.fluidTank2Type || this.fluidTank2Type == -1) && fuelSpace2 > 0)
		{
			if (amountToDrain > fuelSpace2) amountToDrain = fuelSpace2;
			this.gasTank.drain(amountToDrain, true);
			this.fuelTank2.fill(FluidRegistry.getFluidStack(TankGases.values()[thisProduct].liquid, amountToDrain), true);
			this.fluidTank2Type = thisProduct;
		} else
			amountToDrain = 0;
		
		return amountToDrain;
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
		if (nbt.hasKey("fuelTank2"))
		{
			this.fuelTank2.readFromNBT(nbt.getCompoundTag("fuelTank2"));
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
		if (this.fuelTank2.getFluid() != null)
		{
			nbt.setTag("fuelTank2", this.fuelTank2.writeToNBT(new NBTTagCompound()));
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
		return new int[] { 0, 1, 2, 3 };
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
			case 3:
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
				return FluidContainerRegistry.isFilledContainer(itemstack);
			case 3:
				return FluidContainerRegistry.isFilledContainer(itemstack);
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
			return stack != null && stack.getFluid() != null && this.getIdFromName(stack.getFluid().getName()) > -1;
		case 2:
			return FluidContainerRegistry.isEmptyContainer(itemstack);
		case 3:
			return FluidContainerRegistry.isEmptyContainer(itemstack);
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
