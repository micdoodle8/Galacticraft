package micdoodle8.mods.galacticraft.planets.mars.tile;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IOxygenReceiver;
import micdoodle8.mods.galacticraft.api.transmission.tile.IOxygenStorage;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GCFluids;
import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.fluid.FluidNetwork;
import micdoodle8.mods.galacticraft.core.fluid.NetworkHelper;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenStorageModule;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FluidHandlerWrapper;
import micdoodle8.mods.galacticraft.core.wrappers.IFluidHandlerWrapper;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemAtmosphericValve;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockMachineMarsT2;
import micdoodle8.mods.miccore.Annotations;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;

public class TileEntityElectrolyzer extends TileBaseElectricBlockWithInventory implements ISidedInventory, IDisableableMachine, IFluidHandlerWrapper, IOxygenStorage, IOxygenReceiver
{
    private final int tankCapacity = 4000;

    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTank waterTank = new FluidTank(this.tankCapacity);
    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTank liquidTank = new FluidTank(this.tankCapacity);
    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTank liquidTank2 = new FluidTank(this.tankCapacity);

    public int processTimeRequired = 3;
    @NetworkedField(targetSide = Side.CLIENT)
    public int processTicks = 0;

    public TileEntityElectrolyzer()
    {
        super("tile.mars_machine.6.name");
        this.storage.setMaxExtract(ConfigManagerCore.hardMode ? 150 : 120);
        this.setTierGC(2);
        this.inventory = NonNullList.withSize(4, ItemStack.EMPTY);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return true;

        if (EnergyUtil.checkMekGasHandler(capability))
        	return true;
       
        return super.hasCapability(capability, facing);  
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return (T) new FluidHandlerWrapper(this, facing);
        }

        if (EnergyUtil.checkMekGasHandler(capability))
        {
            return (T) this;
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public void update()
    {
        super.update();

        if (!this.world.isRemote)
        {
            final FluidStack liquid = FluidUtil.getFluidContained(this.getInventory().get(1));
            if (FluidUtil.isFluidStrict(liquid, FluidRegistry.WATER.getName()))
            {
                FluidUtil.loadFromContainer(waterTank, FluidRegistry.WATER, this.getInventory(), 1, liquid.amount);
            }

            //Only drain with atmospheric valve
            checkFluidTankTransfer(2, this.liquidTank);
            checkFluidTankTransfer(3, this.liquidTank2);

            if (this.hasEnoughEnergyToRun && this.canProcess())
            {
                //50% extra speed boost for Tier 2 machine if powered by Tier 2 power
                if (this.tierGC == 2)
                {
                    this.processTimeRequired = Math.max(1, 4 - this.poweredByTierGC);
                }

                if (this.processTicks == 0)
                {
                    this.processTicks = this.processTimeRequired;
                }
                else
                {
                    if (--this.processTicks <= 0)
                    {
                        this.doElectrolysis();
                        this.processTicks = this.canProcess() ? this.processTimeRequired : 0;
                    }
                }
            }
            else
            {
                this.processTicks = 0;
            }

            this.produceOxygen(this.getOxygenOutputDirection());
            this.produceHydrogen(this.getHydrogenOutputDirection());
        }
    }

    private void doElectrolysis()
    {
        //Can't be called if the gasTank fluid is null
        final int waterAmount = this.waterTank.getFluid().amount;
        if (waterAmount == 0)
        {
            return;
        }

        this.placeIntoFluidTanks(2);
        this.waterTank.drain(1, true);
    }

    private int placeIntoFluidTanks(int amountToDrain)
    {
        final int fuelSpace = this.liquidTank.getCapacity() - this.liquidTank.getFluidAmount();
        final int fuelSpace2 = this.liquidTank2.getCapacity() - this.liquidTank2.getFluidAmount();
        int amountToDrain2 = amountToDrain * 2;

        if (amountToDrain > fuelSpace)
        {
            amountToDrain = fuelSpace;
        }
        this.liquidTank.fill(FluidRegistry.getFluidStack("oxygen", amountToDrain), true);

        if (amountToDrain2 > fuelSpace2)
        {
            amountToDrain2 = fuelSpace2;
        }
        this.liquidTank2.fill(FluidRegistry.getFluidStack("hydrogen", amountToDrain2), true);

        return amountToDrain;
    }

    private void checkFluidTankTransfer(int slot, FluidTank tank)
    {
        if (!this.getInventory().get(slot).isEmpty() && this.getInventory().get(slot).getItem() instanceof ItemAtmosphericValve)
        {
            tank.drain(4, true);
        }
    }

    public int getScaledGasLevel(int i)
    {
        return this.waterTank.getFluid() != null ? this.waterTank.getFluid().amount * i / this.waterTank.getCapacity() : 0;
    }

    public int getScaledFuelLevel(int i)
    {
        return this.liquidTank.getFluid() != null ? this.liquidTank.getFluid().amount * i / this.liquidTank.getCapacity() : 0;
    }

    public int getScaledFuelLevel2(int i)
    {
        return this.liquidTank2.getFluid() != null ? this.liquidTank2.getFluid().amount * i / this.liquidTank2.getCapacity() : 0;
    }

    public boolean canProcess()
    {
        if (this.waterTank.getFluid() == null || this.waterTank.getFluid().amount <= 0 || this.getDisabled(0))
        {
            return false;
        }

        boolean tank1HasSpace = this.liquidTank.getFluidAmount() < this.liquidTank.getCapacity();
        boolean tank2HasSpace = this.liquidTank2.getFluidAmount() < this.liquidTank2.getCapacity();

        return tank1HasSpace || tank2HasSpace;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.processTicks = nbt.getInteger("processTicks");

        if (nbt.hasKey("waterTank"))
        {
            this.waterTank.readFromNBT(nbt.getCompoundTag("waterTank"));
        }

        if (nbt.hasKey("gasTank"))
        {
            this.liquidTank.readFromNBT(nbt.getCompoundTag("gasTank"));
        }
        if (nbt.hasKey("gasTank2"))
        {
            this.liquidTank2.readFromNBT(nbt.getCompoundTag("gasTank2"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("processTicks", this.processTicks);

        if (this.waterTank.getFluid() != null)
        {
            nbt.setTag("waterTank", this.waterTank.writeToNBT(new NBTTagCompound()));
        }

        if (this.liquidTank.getFluid() != null)
        {
            nbt.setTag("gasTank", this.liquidTank.writeToNBT(new NBTTagCompound()));
        }
        if (this.liquidTank2.getFluid() != null)
        {
            nbt.setTag("gasTank2", this.liquidTank2.writeToNBT(new NBTTagCompound()));
        }

        return nbt;
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[] { 0, 1 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        if (this.isItemValidForSlot(slotID, itemstack))
        {
            switch (slotID)
            {
            case 0:
                return ItemElectricBase.isElectricItemCharged(itemstack);
            case 1:
                return itemstack.getItem() == Items.WATER_BUCKET;
            default:
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        if (this.isItemValidForSlot(slotID, itemstack))
        {
            switch (slotID)
            {
            case 0:
                return ItemElectricBase.isElectricItemEmpty(itemstack);
            case 1:
                return itemstack.getItem() == Items.BUCKET;
            default:
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        Item item = itemstack.getItem();
        switch (slotID)
        {
        case 0:
            return ItemElectricBase.isElectricItem(item);
        case 1:
            return item == Items.BUCKET || item == Items.WATER_BUCKET;
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
    public EnumFacing getElectricInputDirection()
    {
        return EnumFacing.DOWN;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid)
    {
        if (from == this.getHydrogenOutputDirection())
        {
            return this.liquidTank2.getFluid() != null && this.liquidTank2.getFluidAmount() > 0;
        }
        if (from == this.getOxygenOutputDirection())
        {
            return this.liquidTank.getFluid() != null && this.liquidTank.getFluidAmount() > 0;
        }

        return false;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
    {
        if (from == this.getHydrogenOutputDirection())
        {
            if (resource != null && resource.isFluidEqual(this.liquidTank2.getFluid()))
            {
                return this.liquidTank2.drain(resource.amount, doDrain);
            }
        }

        if (from == this.getOxygenOutputDirection())
        {
            if (resource != null && resource.isFluidEqual(this.liquidTank.getFluid()))
            {
                return this.liquidTank.drain(resource.amount, doDrain);
            }
        }

        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
    {
        if (from == this.getHydrogenOutputDirection())
        {
            return this.liquidTank2.drain(maxDrain, doDrain);
        }

        if (from == this.getOxygenOutputDirection())
        {
            return this.liquidTank.drain(maxDrain, doDrain);
        }

        return null;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    {
        if (from == this.getFront().rotateY())
        {
            //Can fill with water
            return fluid == null || fluid.getName().equals(FluidRegistry.WATER.getName());
        }

        return false;
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill)
    {
        int used = 0;

        if (resource != null && this.canFill(from, resource.getFluid()))
        {
            used = this.waterTank.fill(resource, doFill);
        }

        return used;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from)
    {
        FluidTankInfo[] tankInfo = new FluidTankInfo[] {};

        if (from == this.getFront().rotateY())
        {
            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.waterTank) };
        }
        else if (from == this.getHydrogenOutputDirection())
        {
            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.liquidTank2) };
        }
        else if (from == this.getOxygenOutputDirection())
        {
            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.liquidTank) };
        }

        return tankInfo;
    }

    @Override
    public int getBlockMetadata()
    {
        return getBlockType().getMetaFromState(this.world.getBlockState(getPos()));
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer)
    {
        return 0;
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public int receiveGas(EnumFacing side, GasStack stack)
    {
        return 0;
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public GasStack drawGas(EnumFacing from, int amount, boolean doTransfer)
    {
        if (from == this.getHydrogenOutputDirection() && this.liquidTank2.getFluid() != null)
        {
            int amountH = Math.min(8, this.liquidTank2.getFluidAmount());
            amountH = this.liquidTank2.drain(amountH, doTransfer).amount;
            return new GasStack((Gas) EnergyConfigHandler.gasHydrogen, amountH);
        }
        else if (from == this.getOxygenOutputDirection() && this.liquidTank.getFluid() != null)
        {
            int amountO = Math.min(8, this.liquidTank.getFluidAmount());
            amountO = this.liquidTank.drain(amountO, doTransfer).amount;
            return new GasStack((Gas) EnergyConfigHandler.gasOxygen, amountO);
        }
        return null;
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public GasStack drawGas(EnumFacing from, int amount)
    {
        return this.drawGas(from, amount, true);
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public boolean canReceiveGas(EnumFacing side, Gas type)
    {
        return false;
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public boolean canDrawGas(EnumFacing from, Gas type)
    {
        if (from == this.getHydrogenOutputDirection())
        {
            return type.getName().equals("hydrogen");
        }
        else if (from == this.getOxygenOutputDirection())
        {
            return type.getName().equals("oxygen");
        }
        return false;
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.ITubeConnection", modID = CompatibilityManager.modidMekanism)
    public boolean canTubeConnect(EnumFacing from)
    {
        if (from == this.getHydrogenOutputDirection())
        {
            return true;
        }
        else if (from == this.getOxygenOutputDirection())
        {
            return true;
        }
        return false;
    }

    @Override
    public void setOxygenStored(int amount)
    {
        this.liquidTank.setFluid(new FluidStack(AsteroidsModule.fluidOxygenGas, (int) amount));
    }

    @Override
    public int getOxygenStored()
    {
        return this.liquidTank.getFluidAmount();
    }

    public int getHydrogenStored()
    {
        return this.liquidTank2.getFluidAmount();
    }

    @Override
    public int getMaxOxygenStored()
    {
        return this.liquidTank.getCapacity();
    }

    private EnumFacing getOxygenOutputDirection()
    {
        return this.getFront().getOpposite();
    }

    private EnumFacing getHydrogenOutputDirection()
    {
        return this.getFront().rotateYCCW();
    }

    private boolean produceOxygen(EnumFacing outputDirection)
    {
        int provide = this.getOxygenProvide(outputDirection);

        if (provide > 0)
        {
            TileEntity outputTile = new BlockVec3(this).modifyPositionFromSide(outputDirection).getTileEntity(this.world);
            FluidNetwork outputNetwork = NetworkHelper.getFluidNetworkFromTile(outputTile, outputDirection);

            if (outputNetwork != null)
            {
                int gasRequested = outputNetwork.getRequest();

                if (gasRequested > 0)
                {
                    int usedGas = outputNetwork.emitToBuffer(new FluidStack(GCFluids.fluidOxygenGas, Math.min(gasRequested, provide)), true);

                    this.drawOxygen(usedGas, true);
                    return true;
                }
            }
            else if (outputTile instanceof IOxygenReceiver)
            {
                float requestedOxygen = ((IOxygenReceiver) outputTile).getOxygenRequest(outputDirection.getOpposite());

                if (requestedOxygen > 0)
                {
                    int toSend = Math.min(this.getOxygenStored(), provide);
                    int acceptedOxygen = ((IOxygenReceiver) outputTile).receiveOxygen(outputDirection.getOpposite(), toSend, true);
                    this.drawOxygen(acceptedOxygen, true);
                    return true;
                }
            }
//            else if (EnergyConfigHandler.isMekanismLoaded())
//            {
//                //TODO Oxygen item handling - internal tank (IGasItem)
//                //int acceptedOxygen = GasTransmission.addGas(itemStack, type, amount);
//                //this.drawOxygen(acceptedOxygen, true);
//
//                if (outputTile instanceof IGasHandler && ((IGasHandler) outputTile).canReceiveGas(outputDirection.getOpposite(), (Gas) EnergyConfigHandler.gasOxygen))
//                {
//                    GasStack toSend = new GasStack((Gas) EnergyConfigHandler.gasOxygen, (int) Math.floor(Math.min(this.getOxygenStored(), provide)));
//                    int acceptedOxygen = 0;
//                    try {
//                    	acceptedOxygen = ((IGasHandler) outputTile).receiveGas(outputDirection.getOpposite(), toSend);
//                    } catch (Exception e) { }
//                    this.drawOxygen(acceptedOxygen, true);
//                    return true;
//                }
//            }
        }

        return false;
    }

    private boolean produceHydrogen(EnumFacing outputDirection)
    {
        int provide = this.getHydrogenProvide(outputDirection);

        if (provide > 0)
        {
            TileEntity outputTile = new BlockVec3(this).modifyPositionFromSide(outputDirection).getTileEntity(this.world);
            FluidNetwork outputNetwork = NetworkHelper.getFluidNetworkFromTile(outputTile, outputDirection);

            if (outputNetwork != null)
            {
                int gasRequested = outputNetwork.getRequest();

                if (gasRequested > 0)
                {
                    int usedGas = outputNetwork.emitToBuffer(new FluidStack(FluidRegistry.getFluid("hydrogen"), Math.min(gasRequested, provide)), true);

                    this.drawHydrogen(usedGas, true);
                    return true;
                }
            }
            else if (outputTile instanceof TileEntityMethaneSynthesizer)
            {
                float requestedHydrogen = ((TileEntityMethaneSynthesizer) outputTile).getHydrogenRequest(outputDirection.getOpposite());

                if (requestedHydrogen > 0)
                {
                    int toSend = Math.min(this.getHydrogenStored(), provide);
                    int acceptedHydrogen = ((TileEntityMethaneSynthesizer) outputTile).receiveHydrogen(outputDirection.getOpposite(), toSend, true);
                    this.drawHydrogen(acceptedHydrogen, true);
                    return true;
                }
            }
//            else if (EnergyConfigHandler.isMekanismLoaded())
//            {
//                //TODO Gas item handling - internal tank (IGasItem)
//                //int acceptedHydrogen = GasTransmission.addGas(itemStack, type, amount);
//                //this.drawHydrogen(acceptedHydrogen, true);
//
//                if (outputTile instanceof IGasHandler && ((IGasHandler) outputTile).canReceiveGas(outputDirection.getOpposite(), (Gas) EnergyConfigHandler.gasHydrogen))
//                {
//                    GasStack toSend = new GasStack((Gas) EnergyConfigHandler.gasHydrogen, (int) Math.floor(Math.min(this.getHydrogenStored(), provide)));
//                    int acceptedHydrogen = 0;
//                    try {
//                    	acceptedHydrogen = ((IGasHandler) outputTile).receiveGas(outputDirection.getOpposite(), toSend);
//                    } catch (Exception e) { }
//                    this.drawHydrogen(acceptedHydrogen, true);
//                    return true;
//                }
//            }
        }

        return false;
    }

    @Override
    public int provideOxygen(EnumFacing from, int request, boolean doProvide)
    {
        if (this.getOxygenOutputDirection() == from)
        {
            return this.drawOxygen(request, doProvide);
        }

        return 0;
    }

    public int drawOxygen(int request, boolean doProvide)
    {
        if (request > 0)
        {
            int requestedOxygen = Math.min(request, this.liquidTank.getFluidAmount());

            if (doProvide)
            {
                this.setOxygenStored(this.liquidTank.getFluidAmount() - requestedOxygen);
            }

            return requestedOxygen;
        }

        return 0;
    }

    public int drawHydrogen(int request, boolean doProvide)
    {
        if (request > 0)
        {
            int currentHydrogen = this.liquidTank2.getFluidAmount();
            int requestedHydrogen = Math.min(request, currentHydrogen);

            if (doProvide)
            {
                this.liquidTank2.setFluid(new FluidStack(FluidRegistry.getFluid("hydrogen"), currentHydrogen - requestedHydrogen));
            }

            return requestedHydrogen;
        }

        return 0;
    }

    @Override
    public int getOxygenProvide(EnumFacing direction)
    {
        return this.getOxygenOutputDirection() == direction ? Math.min(TileEntityOxygenStorageModule.OUTPUT_PER_TICK, this.getOxygenStored()) : 0;
    }

    public int getHydrogenProvide(EnumFacing direction)
    {
        return this.getHydrogenOutputDirection() == direction ? Math.min(TileEntityOxygenStorageModule.OUTPUT_PER_TICK, this.getHydrogenStored()) : 0;
    }

    @Override
    public boolean shouldPullOxygen()
    {
        return false;
    }

    @Override
    public int receiveOxygen(EnumFacing from, int receive, boolean doReceive)
    {
        return 0;
    }

    @Override
    public int getOxygenRequest(EnumFacing direction)
    {
        return 0;
    }

    @Override
    public boolean canConnect(EnumFacing direction, NetworkType type)
    {
        if (direction == null)
        {
            return false;
        }

        if (type == NetworkType.FLUID)
        {
            return this.getOxygenOutputDirection() == direction || this.getHydrogenOutputDirection() == direction || direction == this.getFront().rotateY();
        }

        if (type == NetworkType.POWER)
        {
            return direction == this.getElectricInputDirection();
        }

        return false;
    }

    @Override
    public EnumFacing getFront()
    {
    	IBlockState state = this.world.getBlockState(getPos()); 
    	if (state.getBlock() instanceof BlockMachineMarsT2)
    	{
    		return state.getValue(BlockMachineMarsT2.FACING);
    	}
    	return EnumFacing.NORTH;
    }
}