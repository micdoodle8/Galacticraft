package micdoodle8.mods.galacticraft.planets.mars.tile;

import micdoodle8.mods.galacticraft.api.prefab.world.gen.DimensionSpace;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.world.EnumAtmosphericGas;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.fluid.FluidNetwork;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import micdoodle8.mods.galacticraft.core.fluid.NetworkHelper;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenStorageModule;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FluidHandlerWrapper;
import micdoodle8.mods.galacticraft.core.wrappers.IFluidHandlerWrapper;
import micdoodle8.mods.galacticraft.planets.PlanetFluids;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockMethaneSynthesizer;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlockNames;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.world.dimension.Dimension;
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
import java.util.ArrayList;

public class TileEntityMethaneSynthesizer extends TileBaseElectricBlockWithInventory implements ISidedInventory, IDisableableMachine, IFluidHandlerWrapper
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + MarsBlockNames.methaneSynthesizer)
    public static TileEntityType<TileEntityMethaneSynthesizer> TYPE;

    private final int tankCapacity = 4000;

    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public final FluidTank gasTank = new FluidTank(this.tankCapacity);
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public final FluidTank gasTank2 = new FluidTank(this.tankCapacity / 2);
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public final FluidTank liquidTank = new FluidTank(this.tankCapacity / 2);

    public int processTimeRequired = 3;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int processTicks = -8;
    private int hasCO2 = -1;
    private boolean noCoal = true;
    private int coalPartial = 0;

    public TileEntityMethaneSynthesizer()
    {
        super(TYPE);
        this.storage.setMaxExtract(ConfigManagerCore.INSTANCE.hardMode.get() ? 90 : 45);
        this.setTierGC(2);
        this.inventory = NonNullList.withSize(5, ItemStack.EMPTY);
    }

//    @Override
//    public boolean hasCapability(Capability<?> capability, Direction facing)
//    {
//        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
//            return true;
//
//        if (EnergyUtil.checkMekGasHandler(capability))
//        	return true;
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
                        return new FluidHandlerWrapper(TileEntityMethaneSynthesizer.this, facing);
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

        if (this.hasCO2 == -1)
        {
            this.hasCO2 = this.getAirProducts();
        }

        if (!this.world.isRemote)
        {
            //If somehow it has CO2 in a CO2-free dimension, flush it out
            if (this.hasCO2 == 0 && this.gasTank2.getFluidAmount() > 0)
            {
                this.gasTank2.drain(this.gasTank2.getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
            }

            //First, see if any gas needs to be put into the hydogen storage
            //TODO - in 1.7.10 implement support for Mekanism internal hydrogen tanks
            //TODO add support for hydrogen atmospheres

            //Now check the CO2 storage
            ItemStack inputCanister = this.getInventory().get(2);
            if (!inputCanister.isEmpty())
            {
                if (inputCanister.getItem() == AsteroidsItems.atmosphericValve && this.hasCO2 > 0)
                {
                    //CO2 -> CO2 tank
                    if (this.gasTank2.getFluidAmount() < this.gasTank2.getCapacity())
                    {
                        BlockState stateAbove = this.world.getBlockState(this.getPos().up());
                        Block blockAbove = stateAbove.getBlock();
                        if (blockAbove.getMaterial(stateAbove) == Material.AIR && blockAbove != GCBlocks.breatheableAir && blockAbove != GCBlocks.brightBreatheableAir)
                        {
                            if (!OxygenUtil.inOxygenBubble(this.world, this.getPos().getX() + 0.5D, this.getPos().getY() + 1D, this.getPos().getZ() + 0.5D))
                            {
                                FluidStack gcAtmosphere = new FluidStack(PlanetFluids.GAS_CARBON_DIOXIDE.getFluid(), 4);
                                this.gasTank2.fill(gcAtmosphere, IFluidHandler.FluidAction.EXECUTE);
                            }
                        }
                    }
                }
            }


            //Now see if any methane from the methane tank needs to be put into the output slot
            checkFluidTankTransfer(4, this.liquidTank);

            if (this.hasEnoughEnergyToRun && this.canProcess())
            {
                //50% extra speed boost for Tier 2 machine if powered by Tier 2 power
                if (this.tierGC == 2)
                {
                    this.processTimeRequired = Math.max(1, 4 - this.poweredByTierGC);
                }

                if (this.processTicks <= 0)
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
                if (this.processTicks > 0)
                {
                    this.processTicks = 0;
                }
                else if (--this.processTicks <= -8)
                {
                    this.processTicks = -8;
                }
            }

            this.produceOutput(this.getHydrogenInputDirection().getOpposite());
        }
    }

    private void produceOutput()
    {
        // TODO Auto-generated method stub

    }

    private void checkFluidTankTransfer(int slot, FluidTank tank)
    {
        if (FluidUtil.isValidContainer(this.getInventory().get(slot)))
        {
            final FluidStack liquid = tank.getFluid();

            FluidUtil.tryFillContainer(tank, liquid, this.getInventory(), slot, AsteroidsItems.methaneCanister);
        }
        else if (!this.getInventory().get(slot).isEmpty() && this.getInventory().get(slot).getItem() == AsteroidsItems.atmosphericValve)
        {
            tank.drain(4, IFluidHandler.FluidAction.EXECUTE);
        }
    }

    public int getScaledGasLevel(int i)
    {
        return this.gasTank.getFluid() != FluidStack.EMPTY ? this.gasTank.getFluid().getAmount() * i / this.gasTank.getCapacity() : 0;
    }

    public int getScaledGasLevel2(int i)
    {
        return this.gasTank2.getFluid() != FluidStack.EMPTY ? this.gasTank2.getFluid().getAmount() * i / this.gasTank2.getCapacity() : 0;
    }

    public int getScaledFuelLevel(int i)
    {
        return this.liquidTank.getFluid() != FluidStack.EMPTY ? this.liquidTank.getFluid().getAmount() * i / this.liquidTank.getCapacity() : 0;
    }

    public boolean canProcess()
    {
        if (this.gasTank.getFluid() == FluidStack.EMPTY || this.gasTank.getFluid().getAmount() <= 0 || this.getDisabled(0))
        {
            return false;
        }

        this.noCoal = this.getInventory().get(3).isEmpty() || this.getInventory().get(3).getItem() != MarsItems.carbonFragments;

        if (this.noCoal && this.coalPartial == 0 && (this.gasTank2.getFluid() == FluidStack.EMPTY || this.gasTank2.getFluidAmount() <= 0))
        {
            return false;
        }

        return this.liquidTank.getFluidAmount() < this.liquidTank.getCapacity();
    }

    public int getAirProducts()
    {
        Dimension WP = this.world.getDimension();
        if (WP instanceof DimensionSpace)
        {
            ArrayList<EnumAtmosphericGas> atmos = ((DimensionSpace) WP).getCelestialBody().atmosphere.composition;
            if (atmos.size() > 0)
            {
                if (atmos.get(0) == EnumAtmosphericGas.CO2)
                {
                    return 1;
                }
            }
            if (atmos.size() > 1)
            {
                if (atmos.get(1) == EnumAtmosphericGas.CO2)
                {
                    return 1;
                }
            }
            if (atmos.size() > 2)
            {
                if (atmos.get(2) == EnumAtmosphericGas.CO2)
                {
                    return 1;
                }
            }

            return 0;
        }

        return 0;
    }

    public void doLiquefaction()
    {
        if (this.noCoal && this.coalPartial == 0)
        {
            if (this.gasTank2.getFluid() == FluidStack.EMPTY || this.gasTank2.drain(1, IFluidHandler.FluidAction.EXECUTE).getAmount() < 1)
            {
                return;
            }
        }
        else
        {
            if (this.coalPartial == 0)
            {
                this.decrStackSize(3, 1);
            }
            this.coalPartial++;
            if (this.coalPartial == 40)
            {
                this.coalPartial = 0;
            }
        }
        this.gasTank.drain(this.placeIntoFluidTanks(2) * 8, IFluidHandler.FluidAction.EXECUTE);
    }

    private int placeIntoFluidTanks(int amountToDrain)
    {
        final int fuelSpace = this.liquidTank.getCapacity() - this.liquidTank.getFluidAmount();

        if (fuelSpace > 0)
        {
            if (amountToDrain > fuelSpace)
            {
                amountToDrain = fuelSpace;
            }
            this.liquidTank.fill(new FluidStack(PlanetFluids.GAS_METHANE.getFluid(), amountToDrain), IFluidHandler.FluidAction.EXECUTE);
        }
        else
        {
            amountToDrain = 0;
        }

        return amountToDrain;
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);
        this.processTicks = nbt.getInt("smeltingTicks");

        if (nbt.contains("gasTank"))
        {
            this.gasTank.readFromNBT(nbt.getCompound("gasTank"));
        }

        if (nbt.contains("gasTank2"))
        {
            this.gasTank2.readFromNBT(nbt.getCompound("gasTank2"));
        }

        if (nbt.contains("liquidTank"))
        {
            this.liquidTank.readFromNBT(nbt.getCompound("liquidTank"));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        nbt.putInt("smeltingTicks", this.processTicks);

        if (this.gasTank.getFluid() != FluidStack.EMPTY)
        {
            nbt.put("gasTank", this.gasTank.writeToNBT(new CompoundNBT()));
        }

        if (this.gasTank2.getFluid() != FluidStack.EMPTY)
        {
            nbt.put("gasTank2", this.gasTank2.writeToNBT(new CompoundNBT()));
        }

        if (this.liquidTank.getFluid() != FluidStack.EMPTY)
        {
            nbt.put("liquidTank", this.liquidTank.writeToNBT(new CompoundNBT()));
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
        return new int[]{0, 1, 2, 3, 4};
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
                case 3:
                    return itemstack.getItem() == MarsItems.carbonFragments;
                case 4:
                    return FluidUtil.isPartialContainer(itemstack, AsteroidsItems.methaneCanister);
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
                    return ItemElectricBase.isElectricItemEmpty(itemstack) || !this.shouldPullEnergy();
                case 4:
                    return FluidUtil.isFullContainer(itemstack);
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
                return itemstack.getItem() == AsteroidsItems.atmosphericValve;
            case 3:
                return itemstack.getItem() == MarsItems.carbonFragments;
            case 4:
                return FluidUtil.isValidContainer(itemstack);
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
        if (from == this.getHydrogenInputDirection().getOpposite())
        {
            return this.liquidTank.getFluid() != FluidStack.EMPTY && this.liquidTank.getFluidAmount() > 0;
        }

        return false;
    }

    @Override
    public FluidStack drain(Direction from, FluidStack resource, IFluidHandler.FluidAction action)
    {
        if (from == this.getHydrogenInputDirection().getOpposite())
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
        if (from == getHydrogenInputDirection().getOpposite())
        {
            return this.liquidTank.drain(maxDrain, action);
        }

        return null;
    }

    @Override
    public boolean canFill(Direction from, Fluid fluid)
    {
        if (from == this.getHydrogenInputDirection())
        {
            return fluid == null || "hydrogen".equals(fluid.getRegistryName().getPath());
        }

        return false;
    }

    @Override
    public int fill(Direction from, FluidStack resource, IFluidHandler.FluidAction action)
    {
        int used = 0;

        if (resource != null && this.canFill(from, resource.getFluid()))
        {
            if (this.gasTank.getFluidAmount() < this.gasTank.getCapacity())
            {
                used = this.gasTank.fill(resource, action);
            }
        }

        return used;
    }

//    @Override
//    public FluidTankInfo[] getTankInfo(Direction from)
//    {
//        FluidTankInfo[] tankInfo = new FluidTankInfo[] {};
//
//        if (from == this.getHydrogenInputDirection())
//        {
//            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.gasTank) };
//        }
//        else if (from == this.getHydrogenInputDirection().getOpposite())
//        {
//            tankInfo = new FluidTankInfo[] { new FluidTankInfo(this.liquidTank) };
//        }
//
//        return tankInfo;
//    }

    @Override
    public int getTanks()
    {
        return 2;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank)
    {
        switch (tank)
        {
            case 0:
                return this.gasTank.getFluid();
            case 1:
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
                return this.gasTank.getCapacity();
            case 1:
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
                return this.gasTank.isFluidValid(stack);
            case 1:
                return this.liquidTank.isFluidValid(stack);
        }
        return false;
    }

//    @Override
//    public int getBlockMetadata()
//    {
//        return this.getBlockType().getMetaFromState(this.world.getBlockState(getPos()));
//    }

//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public int receiveGas(Direction side, GasStack stack, boolean doTransfer)
//    {
//        if (!stack.getGas().getName().equals("hydrogen"))
//        {
//            return 0;
//        }
//        int used = 0;
//        //System.out.println("Giving gas amount "+stack.amount);
//        if (this.gasTank.getFluidAmount() < this.gasTank.getCapacity())
//        {
//            used = this.gasTank.fill(FluidRegistry.getFluidStack("hydrogen", stack.amount), doTransfer);
//        }
//        return used;
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public int receiveGas(Direction side, GasStack stack)
//    {
//        return this.receiveGas(LogicalSide, stack, IFluidHandler.FluidAction.EXECUTE);
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public GasStack drawGas(Direction side, int amount, boolean doTransfer)
//    {
//        return null;
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public GasStack drawGas(Direction side, int amount)
//    {
//        return null;
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public boolean canReceiveGas(Direction side, Gas type)
//    {
//        //System.out.println("Testing receipt of gas "+type.getName());
//        return type.getName().equals("hydrogen") && LogicalSide.equals(this.getHydrogenInputDirection());
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public boolean canDrawGas(Direction side, Gas type)
//    {
//        return false;
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.ITubeConnection", modID = CompatibilityManager.modidMekanism)
//    public boolean canTubeConnect(Direction side)
//    {
//        return LogicalSide.equals(this.getHydrogenInputDirection());
//    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
    {
        if (direction == null)
        {
            return false;
        }

        if (type == NetworkType.POWER)
        {
            return direction == this.getElectricInputDirection();
        }

        //Hydrogen pipe
        if (type == NetworkType.FLUID)
        {
            return direction == this.getHydrogenInputDirection() || direction == this.getHydrogenInputDirection().getOpposite();
        }

        return false;
    }

    public int getHydrogenRequest(Direction direction)
    {
        return this.receiveHydrogen(direction, 1000000F, IFluidHandler.FluidAction.SIMULATE);
    }

    public boolean shouldPullHydrogen()
    {
        return this.gasTank.getFluidAmount() < this.gasTank.getCapacity();
    }

    public int receiveHydrogen(Direction from, float receive, IFluidHandler.FluidAction action)
    {
        if (from == this.getHydrogenInputDirection() && this.shouldPullHydrogen())
        {
            FluidStack fluidToFill = new FluidStack(GCFluids.HYDROGEN.getFluid(), (int) (receive));
            return this.gasTank.fill(fluidToFill, action);
        }

        return 0;
    }

    @Override
    public Direction getFront()
    {
        BlockState state = this.world.getBlockState(getPos());
//        if (state.getBlock() instanceof BlockMachineMarsT2)
//        {
//            return state.get(BlockMachineMarsT2.FACING);
//        }
        return state.get(BlockMethaneSynthesizer.FACING);
    }

    public Direction getHydrogenInputDirection()
    {
        return this.getFront().rotateY();
    }

    private boolean produceOutput(Direction outputDirection)
    {
        int provide = this.getMethaneProvide();

        if (provide > 0)
        {
            TileEntity outputTile = new BlockVec3(this).getTileEntityOnSide(this.world, outputDirection);
            FluidNetwork outputNetwork = NetworkHelper.getFluidNetworkFromTile(outputTile, outputDirection);

            if (outputNetwork != null)
            {
                int gasRequested = outputNetwork.getRequest();

                if (gasRequested > 0)
                {
                    int usedGas = outputNetwork.emitToBuffer(new FluidStack(PlanetFluids.GAS_METHANE.getFluid(), Math.min(gasRequested, provide)), IFluidHandler.FluidAction.EXECUTE);
                    this.liquidTank.drain(usedGas, IFluidHandler.FluidAction.EXECUTE);
                    return true;
                }
            }
        }

        return false;
    }

    private int getMethaneProvide()
    {
        return Math.min(TileEntityOxygenStorageModule.OUTPUT_PER_TICK, this.liquidTank.getFluidAmount());
    }
}