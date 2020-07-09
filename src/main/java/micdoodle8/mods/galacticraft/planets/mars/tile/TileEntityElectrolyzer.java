package micdoodle8.mods.galacticraft.planets.mars.tile;

import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IOxygenReceiver;
import micdoodle8.mods.galacticraft.api.transmission.tile.IOxygenStorage;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.fluid.FluidNetwork;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import micdoodle8.mods.galacticraft.core.fluid.NetworkHelper;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenStorageModule;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FluidHandlerWrapper;
import micdoodle8.mods.galacticraft.core.wrappers.IFluidHandlerWrapper;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockElectrolyzer;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlockNames;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityElectrolyzer extends TileBaseElectricBlockWithInventory implements ISidedInventory, IDisableableMachine, IFluidHandlerWrapper, IOxygenStorage, IOxygenReceiver
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + MarsBlockNames.electrolyzer)
    public static TileEntityType<TileEntityElectrolyzer> TYPE;

    private final int tankCapacity = 4000;

    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public FluidTank waterTank = new FluidTank(this.tankCapacity);
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public FluidTank liquidTank = new FluidTank(this.tankCapacity);
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public FluidTank liquidTank2 = new FluidTank(this.tankCapacity);

    public int processTimeRequired = 3;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int processTicks = 0;

    public TileEntityElectrolyzer()
    {
        super(TYPE);
        this.storage.setMaxExtract(ConfigManagerCore.hardMode ? 150 : 120);
        this.setTierGC(2);
        this.inventory = NonNullList.withSize(4, ItemStack.EMPTY);
    }

//    @Override
//    public boolean hasCapability(Capability<?> capability, Direction facing)
//    {
//        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
//            return true;
//
////        if (EnergyUtil.checkMekGasHandler(capability))
////        	return true;
//
//        return super.hasCapability(capability, facing);
//    }

    private LazyOptional<IFluidHandler> holder = null;

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            if (holder == null)
            {
                holder = LazyOptional.of(new NonNullSupplier<IFluidHandler>()
                {
                    @Nonnull
                    @Override
                    public IFluidHandler get()
                    {
                        return new FluidHandlerWrapper(TileEntityElectrolyzer.this, facing);
                    }
                });
            }
            return holder.cast();
        }
//        if (EnergyUtil.checkMekGasHandler(capability))
//        {
//            return (T) this;
//        }  TODO Mek support
        return super.getCapability(capability, facing);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (!this.world.isRemote)
        {
            final FluidStack liquid = FluidUtil.getFluidContained(this.getInventory().get(1));
            if (FluidUtil.isFluidStrict(liquid, Fluids.WATER.getRegistryName().getPath()))
            {
                FluidUtil.loadFromContainer(waterTank, Fluids.WATER, this.getInventory(), 1, liquid.getAmount());
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
        final int waterAmount = this.waterTank.getFluid().getAmount();
        if (waterAmount == 0)
        {
            return;
        }

        this.placeIntoFluidTanks(2);
        this.waterTank.drain(1, IFluidHandler.FluidAction.EXECUTE);
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
        this.liquidTank.fill(new FluidStack(GCFluids.OXYGEN.getFluid(), amountToDrain), IFluidHandler.FluidAction.EXECUTE);

        if (amountToDrain2 > fuelSpace2)
        {
            amountToDrain2 = fuelSpace2;
        }
        this.liquidTank2.fill(new FluidStack(GCFluids.HYDROGEN.getFluid(), amountToDrain2), IFluidHandler.FluidAction.EXECUTE);

        return amountToDrain;
    }

    private void checkFluidTankTransfer(int slot, FluidTank tank)
    {
        if (!this.getInventory().get(slot).isEmpty() && this.getInventory().get(slot).getItem() == AsteroidsItems.atmosphericValve)
        {
            tank.drain(4, IFluidHandler.FluidAction.EXECUTE);
        }
    }

    public int getScaledGasLevel(int i)
    {
        return this.waterTank.getFluid() != FluidStack.EMPTY ? this.waterTank.getFluid().getAmount() * i / this.waterTank.getCapacity() : 0;
    }

    public int getScaledFuelLevel(int i)
    {
        return this.liquidTank.getFluid() != FluidStack.EMPTY ? this.liquidTank.getFluid().getAmount() * i / this.liquidTank.getCapacity() : 0;
    }

    public int getScaledFuelLevel2(int i)
    {
        return this.liquidTank2.getFluid() != FluidStack.EMPTY ? this.liquidTank2.getFluid().getAmount() * i / this.liquidTank2.getCapacity() : 0;
    }

    public boolean canProcess()
    {
        if (this.waterTank.getFluid() == FluidStack.EMPTY || this.waterTank.getFluid().getAmount() <= 0 || this.getDisabled(0))
        {
            return false;
        }

        boolean tank1HasSpace = this.liquidTank.getFluidAmount() < this.liquidTank.getCapacity();
        boolean tank2HasSpace = this.liquidTank2.getFluidAmount() < this.liquidTank2.getCapacity();

        return tank1HasSpace || tank2HasSpace;
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);
        this.processTicks = nbt.getInt("processTicks");

        if (nbt.contains("waterTank"))
        {
            this.waterTank.readFromNBT(nbt.getCompound("waterTank"));
        }

        if (nbt.contains("gasTank"))
        {
            this.liquidTank.readFromNBT(nbt.getCompound("gasTank"));
        }
        if (nbt.contains("gasTank2"))
        {
            this.liquidTank2.readFromNBT(nbt.getCompound("gasTank2"));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        nbt.putInt("processTicks", this.processTicks);

        if (this.waterTank.getFluid() != FluidStack.EMPTY)
        {
            nbt.put("waterTank", this.waterTank.writeToNBT(new CompoundNBT()));
        }

        if (this.liquidTank.getFluid() != FluidStack.EMPTY)
        {
            nbt.put("gasTank", this.liquidTank.writeToNBT(new CompoundNBT()));
        }
        if (this.liquidTank2.getFluid() != FluidStack.EMPTY)
        {
            nbt.put("gasTank2", this.liquidTank2.writeToNBT(new CompoundNBT()));
        }

        return nbt;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[]{0, 1};
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, Direction side)
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
    public boolean canExtractItem(int slotID, ItemStack itemstack, Direction side)
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
    public Direction getElectricInputDirection()
    {
        return Direction.DOWN;
    }

    @Override
    public boolean canDrain(Direction from, Fluid fluid)
    {
        if (from == this.getHydrogenOutputDirection())
        {
            return this.liquidTank2.getFluid() != FluidStack.EMPTY && this.liquidTank2.getFluidAmount() > 0;
        }
        if (from == this.getOxygenOutputDirection())
        {
            return this.liquidTank.getFluid() != FluidStack.EMPTY && this.liquidTank.getFluidAmount() > 0;
        }

        return false;
    }

    @Override
    public FluidStack drain(Direction from, FluidStack resource, IFluidHandler.FluidAction action)
    {
        if (from == this.getHydrogenOutputDirection())
        {
            if (resource != null && resource.isFluidEqual(this.liquidTank2.getFluid()))
            {
                return this.liquidTank2.drain(resource.getAmount(), action);
            }
        }

        if (from == this.getOxygenOutputDirection())
        {
            if (resource != null && resource.isFluidEqual(this.liquidTank.getFluid()))
            {
                return this.liquidTank.drain(resource.getAmount(), action);
            }
        }

        return null;
    }

    @Override
    public FluidStack drain(Direction from, int maxDrain, IFluidHandler.FluidAction action)
    {
        if (from == this.getHydrogenOutputDirection())
        {
            return this.liquidTank2.drain(maxDrain, action);
        }

        if (from == this.getOxygenOutputDirection())
        {
            return this.liquidTank.drain(maxDrain, action);
        }

        return null;
    }

    @Override
    public boolean canFill(Direction from, Fluid fluid)
    {
        if (from == this.getFront().rotateY())
        {
            //Can fill with water
            return fluid == null || fluid.getRegistryName().getPath().equals(Fluids.WATER.getRegistryName().getPath());
        }

        return false;
    }

    @Override
    public int fill(Direction from, FluidStack resource, IFluidHandler.FluidAction action)
    {
        int used = 0;

        if (resource != null && this.canFill(from, resource.getFluid()))
        {
            used = this.waterTank.fill(resource, action);
        }

        return used;
    }

//    @Override
//    public FluidTankInfo[] getTankInfo(Direction from)
//    {
//        FluidTankInfo[] tankInfo = new FluidTankInfo[] {};
//
//        if (from == this.getFront().rotateY())
//        {
//            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.waterTank) };
//        }
//        else if (from == this.getHydrogenOutputDirection())
//        {
//            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.liquidTank2) };
//        }
//        else if (from == this.getOxygenOutputDirection())
//        {
//            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.liquidTank) };
//        }
//
//        return tankInfo;
//    }

    @Override
    public int getTanks()
    {
        return 3;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank)
    {
        switch (tank)
        {
        case 0:
            return this.waterTank.getFluid();
        case 1:
            return this.liquidTank2.getFluid();
        case 2:
            return this.liquidTank.getFluid();
        }
        return FluidStack.EMPTY;
    }

    @Override
    public int getTankCapacity(int tank)
    {
        switch (tank)
        {
        case 0:
            return this.waterTank.getCapacity();
        case 1:
            return this.liquidTank2.getCapacity();
        case 2:
            return this.liquidTank.getCapacity();
        }
        return 0;
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack)
    {
        switch (tank)
        {
        case 0:
            return this.waterTank.isFluidValid(stack);
        case 1:
            return this.liquidTank2.isFluidValid(stack);
        case 2:
            return this.liquidTank.isFluidValid(stack);
        }
        return false;
    }

//    @Override
//    public int getBlockMetadata()
//    {
//        return getBlockType().getMetaFromState(this.world.getBlockState(getPos()));
//    }

//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public int receiveGas(Direction side, GasStack stack, boolean doTransfer)
//    {
//        return 0;
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public int receiveGas(Direction side, GasStack stack)
//    {
//        return 0;
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public GasStack drawGas(Direction from, int amount, boolean doTransfer)
//    {
//        if (from == this.getHydrogenOutputDirection() && this.liquidTank2.getFluid() != FluidStack.EMPTY)
//        {
//            int amountH = Math.min(8, this.liquidTank2.getFluidAmount());
//            amountH = this.liquidTank2.drain(amountH, doTransfer).amount;
//            return new GasStack((Gas) EnergyConfigHandler.gasHydrogen, amountH);
//        }
//        else if (from == this.getOxygenOutputDirection() && this.liquidTank.getFluid() != FluidStack.EMPTY)
//        {
//            int amountO = Math.min(8, this.liquidTank.getFluidAmount());
//            amountO = this.liquidTank.drain(amountO, doTransfer).amount;
//            return new GasStack((Gas) EnergyConfigHandler.gasOxygen, amountO);
//        }
//        return null;
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public GasStack drawGas(Direction from, int amount)
//    {
//        return this.drawGas(from, amount, true);
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public boolean canReceiveGas(Direction side, Gas type)
//    {
//        return false;
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public boolean canDrawGas(Direction from, Gas type)
//    {
//        if (from == this.getHydrogenOutputDirection())
//        {
//            return type.getName().equals("hydrogen");
//        }
//        else if (from == this.getOxygenOutputDirection())
//        {
//            return type.getName().equals("oxygen");
//        }
//        return false;
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.ITubeConnection", modID = CompatibilityManager.modidMekanism)
//    public boolean canTubeConnect(Direction from)
//    {
//        if (from == this.getHydrogenOutputDirection())
//        {
//            return true;
//        }
//        else if (from == this.getOxygenOutputDirection())
//        {
//            return true;
//        }
//        return false;
//    }

    @Override
    public void setOxygenStored(int amount)
    {
        this.liquidTank.setFluid(new FluidStack(GCFluids.OXYGEN.getFluid(), amount));
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

    private Direction getOxygenOutputDirection()
    {
        return this.getFront().getOpposite();
    }

    private Direction getHydrogenOutputDirection()
    {
        return this.getFront().rotateYCCW();
    }

    private boolean produceOxygen(Direction outputDirection)
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
                    int usedGas = outputNetwork.emitToBuffer(new FluidStack(GCFluids.OXYGEN.getFluid(), Math.min(gasRequested, provide)), IFluidHandler.FluidAction.EXECUTE);

                    this.drawOxygen(usedGas, IFluidHandler.FluidAction.EXECUTE);
                    return true;
                }
            }
            else if (outputTile instanceof IOxygenReceiver)
            {
                float requestedOxygen = ((IOxygenReceiver) outputTile).getOxygenRequest(outputDirection.getOpposite());

                if (requestedOxygen > 0)
                {
                    int toSend = Math.min(this.getOxygenStored(), provide);
                    int acceptedOxygen = ((IOxygenReceiver) outputTile).receiveOxygen(outputDirection.getOpposite(), toSend, IFluidHandler.FluidAction.EXECUTE);
                    this.drawOxygen(acceptedOxygen, IFluidHandler.FluidAction.EXECUTE);
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

    private boolean produceHydrogen(Direction outputDirection)
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
                    int usedGas = outputNetwork.emitToBuffer(new FluidStack(GCFluids.HYDROGEN.getFluid(), Math.min(gasRequested, provide)), IFluidHandler.FluidAction.EXECUTE);

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
                    int acceptedHydrogen = ((TileEntityMethaneSynthesizer) outputTile).receiveHydrogen(outputDirection.getOpposite(), toSend, IFluidHandler.FluidAction.EXECUTE);
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
    public int provideOxygen(Direction from, int request, IFluidHandler.FluidAction action)
    {
        if (this.getOxygenOutputDirection() == from)
        {
            return this.drawOxygen(request, action);
        }

        return 0;
    }

    public int drawOxygen(int request, IFluidHandler.FluidAction action)
    {
        if (request > 0)
        {
            int requestedOxygen = Math.min(request, this.liquidTank.getFluidAmount());

            if (action.execute())
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
                this.liquidTank2.setFluid(new FluidStack(GCFluids.HYDROGEN.getFluid(), currentHydrogen - requestedHydrogen));
            }

            return requestedHydrogen;
        }

        return 0;
    }

    @Override
    public int getOxygenProvide(Direction direction)
    {
        return this.getOxygenOutputDirection() == direction ? Math.min(TileEntityOxygenStorageModule.OUTPUT_PER_TICK, this.getOxygenStored()) : 0;
    }

    public int getHydrogenProvide(Direction direction)
    {
        return this.getHydrogenOutputDirection() == direction ? Math.min(TileEntityOxygenStorageModule.OUTPUT_PER_TICK, this.getHydrogenStored()) : 0;
    }

    @Override
    public boolean shouldPullOxygen()
    {
        return false;
    }

    @Override
    public int receiveOxygen(Direction from, int receive, IFluidHandler.FluidAction action)
    {
        return 0;
    }

    @Override
    public int getOxygenRequest(Direction direction)
    {
        return 0;
    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
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
    public Direction getFront()
    {
        BlockState state = this.world.getBlockState(getPos());
//    	if (state.getBlock() instanceof BlockMachineMarsT2)
//    	{
//    		return state.get(BlockMachineMarsT2.FACING);
//    	}
        return state.get(BlockElectrolyzer.FACING);
    }
}