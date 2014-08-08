package micdoodle8.mods.galacticraft.planets.mars.tile;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.world.IAtmosphericGas;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemAtmosphericValve;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import micdoodle8.mods.miccore.Annotations.RuntimeInterface;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
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

public class TileEntityMethaneSynthesizer extends TileBaseElectricBlockWithInventory implements ISidedInventory, IDisableableMachine, IFluidHandler
{
	private final int tankCapacity = 4000;

	@NetworkedField(targetSide = Side.CLIENT)
	public FluidTank gasTank = new FluidTank(this.tankCapacity);
	@NetworkedField(targetSide = Side.CLIENT)
	public FluidTank gasTank2 = new FluidTank(this.tankCapacity/2);
	@NetworkedField(targetSide = Side.CLIENT)
	public FluidTank liquidTank = new FluidTank(this.tankCapacity/2);

	public int processTimeRequired = 3;
	@NetworkedField(targetSide = Side.CLIENT)
	public int processTicks = 0;
	private ItemStack[] containingItems = new ItemStack[5];
	private int hasCO2 = -1;
	private boolean noCoal = true;
	private int coalPartial = 0;
	
	public TileEntityMethaneSynthesizer()
	{
		this.storage.setMaxExtract(30);
		this.setTierGC(2);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (this.hasCO2 == -1)
		{
			this.hasCO2 = this.getAirProducts();
		}

		if (!this.worldObj.isRemote)
		{
			//If somehow it has CO2 in a CO2-free dimension, flush it out
			if (this.hasCO2 == 0 && this.gasTank2.getFluidAmount() > 0)
			{
				this.gasTank2.drain(this.gasTank2.getFluidAmount(), true);
			}
			
			//First, see if any gas needs to be put into the hydogen storage
			//TODO - in 1.7.10 implement support for Mekanism internal hydrogen tanks

			//TODO TEMPORARY
			this.gasTank.fill(FluidRegistry.getFluidStack("hydrogen", 4), true);
			//TODO add support for hydrogen atmospheres
			
			//Now check the CO2 storage
			ItemStack inputCanister = this.containingItems[2];
			if (inputCanister != null)
			{
				if (inputCanister.getItem() instanceof ItemAtmosphericValve && this.hasCO2 > 0)
				{
					//CO2 -> CO2 tank
					if (this.gasTank2.getFluidAmount() < this.gasTank2.getCapacity())
					{
						FluidStack gcAtmosphere = FluidRegistry.getFluidStack("carbondioxide", 4);
						this.gasTank2.fill(gcAtmosphere, true);
					}
				}
			}
			

			//Now see if any methane from the methane tank needs to be put into the output slot
			checkFluidTankTransfer(4, this.liquidTank);

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
		if (this.containingItems[slot] != null && FluidContainerRegistry.isContainer(this.containingItems[slot]))
		{
			final FluidStack liquid = tank.getFluid();

			if (liquid != null)
			{
				tryFillContainer(tank, liquid, slot, AsteroidsItems.methaneCanister);
			}
		}
		else if (this.containingItems[slot] != null && this.containingItems[slot].getItem() instanceof ItemAtmosphericValve)
		{
			tank.drain(4, true);
		}
	}

	private void tryFillContainer(FluidTank tank, FluidStack liquid, int slot, Item canister)
	{
		ItemStack slotItem = this.containingItems[slot];
		boolean isCanister = slotItem.getItem() instanceof ItemCanisterGeneric && slotItem.getItemDamage() > 1;
		final int amountToFill = Math.min(liquid.amount, isCanister ? slotItem.getItemDamage() - 1 : FluidContainerRegistry.BUCKET_VOLUME);

		if (isCanister)
		{
			this.containingItems[slot] = new ItemStack(canister, 1, slotItem.getItemDamage() - amountToFill);
			tank.drain(amountToFill, true);
		}
		else if (amountToFill == FluidContainerRegistry.BUCKET_VOLUME)
		{
			this.containingItems[slot] = FluidContainerRegistry.fillFluidContainer(liquid, this.containingItems[slot]);
			if (this.containingItems[slot] == null) this.containingItems[slot] = slotItem;
			else tank.drain(amountToFill, true);
		}
	}

	public int getScaledGasLevel(int i)
	{
		return this.gasTank.getFluid() != null ? this.gasTank.getFluid().amount * i / this.gasTank.getCapacity() : 0;
	}

	public int getScaledGasLevel2(int i)
	{
		return this.gasTank2.getFluid() != null ? this.gasTank2.getFluid().amount * i / this.gasTank2.getCapacity() : 0;
	}

	public int getScaledFuelLevel(int i)
	{
		return this.liquidTank.getFluid() != null ? this.liquidTank.getFluid().amount * i / this.liquidTank.getCapacity() : 0;
	}

	public boolean canProcess()
	{
		if (this.gasTank.getFluid() == null || this.gasTank.getFluid().amount <= 0 || this.getDisabled(0))
		{
			return false;
		}

		this.noCoal = this.containingItems[3] == null || this.containingItems[3].stackSize == 0 || this.containingItems[3].getItem() != MarsItems.carbonFragments;

		if (this.noCoal && this.coalPartial == 0 && (this.gasTank2.getFluid() == null || this.gasTank2.getFluidAmount() <=0))
			return false;
		
		return this.liquidTank.getFluidAmount() < this.liquidTank.getCapacity();
    }
	
	public int getAirProducts()
	{
		WorldProvider WP = this.worldObj.provider;
		if (WP instanceof WorldProviderSpace)
		{
			ArrayList<IAtmosphericGas> atmos = ((WorldProviderSpace)WP).getCelestialBody().atmosphere;
			if (atmos.size() > 0)
			{	
				if (atmos.get(0) == IAtmosphericGas.CO2) return 1; 
			}
			if (atmos.size() > 1)
			{	
				if (atmos.get(1) == IAtmosphericGas.CO2) return 1; 
			}
			if (atmos.size() > 2)
			{	
				if (atmos.get(2) == IAtmosphericGas.CO2) return 1; 
			}
			
			return 0;
		}	

		return 0;
	}

	public void doLiquefaction()
	{
		if (this.noCoal && this.coalPartial == 0)
		{
			if (this.gasTank2.drain(1, true).amount < 1) return;
		}
		else
		{
			if (this.coalPartial == 0)
				this.decrStackSize(3, 1);
			this.coalPartial++;
			if (this.coalPartial == 40) this.coalPartial = 0;
		}
		this.gasTank.drain(this.placeIntoFluidTanks(1) * 2, true);
	}
	
	private int placeIntoFluidTanks(int amountToDrain)
	{
		final int fuelSpace = this.liquidTank.getCapacity() - this.liquidTank.getFluidAmount();

		if (fuelSpace > 0)
		{
			if (amountToDrain > fuelSpace) amountToDrain = fuelSpace;
			this.liquidTank.fill(FluidRegistry.getFluidStack("methane", amountToDrain), true);
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

		if (nbt.hasKey("gasTank2"))
		{
			this.gasTank2.readFromNBT(nbt.getCompoundTag("gasTank2"));
		}

		if (nbt.hasKey("liquidTank"))
		{
			this.liquidTank.readFromNBT(nbt.getCompoundTag("liquidTank"));
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

		if (this.gasTank2.getFluid() != null)
		{
			nbt.setTag("gasTank2", this.gasTank2.writeToNBT(new NBTTagCompound()));
		}

		if (this.liquidTank.getFluid() != null)
		{
			nbt.setTag("liquidTank", this.liquidTank.writeToNBT(new NBTTagCompound()));
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
		return GCCoreUtil.translate("tile.marsMachine.5.name");
	}

	// ISidedInventory Implementation:

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return new int[] { 0, 1, 2, 3, 4 };
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemstack, int side)
	{
		if (this.isItemValidForSlot(slotID, itemstack))
		{
			switch (slotID)
			{
			case 0:
				return ItemElectricBase.isElectricItem(itemstack.getItem());
			case 3:
				return itemstack.getItem() == MarsItems.carbonFragments;
			case 4:
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
				return itemstack.getItem() instanceof ItemElectricBase && ((ItemElectricBase) itemstack.getItem()).getElectricityStored(itemstack) <= 0 || !this.shouldPullEnergy();
			case 4:
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
			return ItemElectricBase.isElectricItem(itemstack.getItem());
		case 1:
			return false;
		case 2:
			return itemstack.getItem() instanceof ItemAtmosphericValve;
		case 3:
			return itemstack.getItem() == MarsItems.carbonFragments;
		case 4:
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
		return ForgeDirection.DOWN;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		int metaside = this.getBlockMetadata() + 2;
		int side = from.ordinal();
		if (side == (metaside ^ 1))
			return this.liquidTank.getFluid() != null && this.liquidTank.getFluidAmount() > 0;

		return false;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		int metaside = this.getBlockMetadata() + 2;
		int side = from.ordinal();
		if (side == (metaside ^ 1))
		{
			if (resource != null && resource.isFluidEqual(this.liquidTank.getFluid()))
				return this.liquidTank.drain(resource.amount, doDrain);
		}

		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		int metaside = this.getBlockMetadata() + 2;
		int side = from.ordinal();
		if (side == (metaside ^ 1))
		{
			return this.liquidTank.drain(maxDrain, doDrain);
		}
			
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		if (from.equals(ForgeDirection.getOrientation(this.getBlockMetadata() + 2)))
		{
			return fluid != null && "hydrogen".equals(fluid.getName());
		}

		return false;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		int used = 0;

		if (resource != null && this.canFill(from, resource.getFluid()))
		{
			if (this.gasTank.getFluidAmount() < this.gasTank.getCapacity())
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

		if (from == ForgeDirection.getOrientation(this.getBlockMetadata() + 2))
		{
			tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.gasTank) };
		}
		else if (from == ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite())
		{
			tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.liquidTank) };
		}

		return tankInfo;
	}
	
	@Override
    public int getBlockMetadata()
    {
        if (this.blockMetadata == -1)
        {
            this.blockMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
        }

        return this.blockMetadata & 3;
    }
	
	@RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
	public int receiveGas(ForgeDirection side, GasStack stack)
	{
		int used = 0;
		//System.out.println("Giving gas amount "+stack.amount);
		if (this.gasTank.getFluidAmount() < this.gasTank.getCapacity())
		{
			used = this.gasTank.fill(FluidRegistry.getFluidStack("hydrogen", stack.amount), true);
		}
		return used;
	}

	@RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
	public GasStack drawGas(ForgeDirection side, int amount)
	{
		return null;
	}

	@RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
	public boolean canReceiveGas(ForgeDirection side, Gas type)
	{
		//System.out.println("Testing receipt of gas "+type.getName());
		return type.getName().equals("hydrogen") && side.equals(ForgeDirection.getOrientation(this.getBlockMetadata() + 2));
	}

	@RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = "Mekanism")
	public boolean canDrawGas(ForgeDirection side, Gas type)
	{
		return false;
	}

	@RuntimeInterface(clazz = "mekanism.api.gas.ITubeConnection", modID = "Mekanism")
	public boolean canTubeConnect(ForgeDirection side)
	{
		return side.equals(ForgeDirection.getOrientation(this.getBlockMetadata() + 2));
	}
}
