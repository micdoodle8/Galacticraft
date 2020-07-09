package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IOxygenReceiver;
import micdoodle8.mods.galacticraft.api.transmission.tile.IOxygenStorage;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlock;
import micdoodle8.mods.galacticraft.core.fluid.FluidNetwork;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import micdoodle8.mods.galacticraft.core.fluid.NetworkHelper;
import micdoodle8.mods.galacticraft.core.wrappers.IFluidHandlerWrapper;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.LogicalSide;

import javax.annotation.Nonnull;
import java.util.EnumSet;

public abstract class TileEntityOxygen extends TileBaseElectricBlock implements IOxygenReceiver, IOxygenStorage, IFluidHandlerWrapper
{
    public int oxygenPerTick;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public FluidTankGC tank;
    public float lastStoredOxygen;
    public static int timeSinceOxygenRequest;
    private final LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> tank);

    public TileEntityOxygen(TileEntityType<?> type, int maxOxygen, int oxygenPerTick)
    {
        super(type);
        this.tank = new FluidTankGC(maxOxygen, this);
        this.oxygenPerTick = oxygenPerTick;
    }

//    @Override
//    public boolean hasCapability(Capability<?> capability, Direction facing)
//    {
//    	if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
//    		return true;
//
//    	if (EnergyUtil.checkMekGasHandler(capability))
//    		return true;
//
//    	return super.hasCapability(capability, facing);
//    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return holder.cast();
        }

//    	if (EnergyUtil.checkMekGasHandler(capability))
//    	{
//    		return (T) this;
//    	}

        return super.getCapability(capability, facing);
    }

    public int getScaledOxygenLevel(int scale)
    {
        return (int) Math.floor(this.getOxygenStored() * scale / (this.getMaxOxygenStored() - this.oxygenPerTick));
    }

    public abstract boolean shouldUseOxygen();

    public int getCappedScaledOxygenLevel(int scale)
    {
        return (int) Math.max(Math.min(Math.floor((double) this.tank.getFluidAmount() / (double) this.tank.getCapacity() * scale), scale), 0);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (!this.world.isRemote)
        {
            if (TileEntityOxygen.timeSinceOxygenRequest > 0)
            {
                TileEntityOxygen.timeSinceOxygenRequest--;
            }

            if (this.shouldUseOxygen())
            {
                if (this.tank.getFluid() != FluidStack.EMPTY)
                {
                    FluidStack fluid = this.tank.getFluid().copy();
                    fluid.setAmount(Math.max(fluid.getAmount() - this.oxygenPerTick, 0));
                    this.tank.setFluid(fluid);
                }
            }
        }

        this.lastStoredOxygen = this.tank.getFluidAmount();
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);

        if (nbt.contains("storedOxygen"))
        {
            this.tank.setFluid(new FluidStack(GCFluids.OXYGEN.getFluid(), nbt.getInt("storedOxygen")));
        }
        else if (nbt.contains("storedOxygenF"))
        {
            int oxygen = (int) nbt.getFloat("storedOxygenF");
            oxygen = Math.min(this.tank.getCapacity(), oxygen);
            this.tank.setFluid(new FluidStack(GCFluids.OXYGEN.getFluid(), oxygen));
        }
        else
        {
            this.tank.readFromNBT(nbt);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        this.tank.writeToNBT(nbt);
        return nbt;
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.write(new CompoundNBT());
    }

    @Override
    public void setOxygenStored(int oxygen)
    {
        this.tank.setFluid(new FluidStack(GCFluids.OXYGEN.getFluid(), Math.max(Math.min(oxygen, this.getMaxOxygenStored()), 0)));
    }

    @Override
    public int getOxygenStored()
    {
        return this.tank.getFluidAmount();
    }

    @Override
    public int getMaxOxygenStored()
    {
        return this.tank.getCapacity();
    }

    public EnumSet<Direction> getOxygenInputDirections()
    {
        return EnumSet.allOf(Direction.class);
    }

    public EnumSet<Direction> getOxygenOutputDirections()
    {
        return EnumSet.noneOf(Direction.class);
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
            return this.getOxygenInputDirections().contains(direction) || this.getOxygenOutputDirections().contains(direction);
        }
        if (type == NetworkType.POWER)
        //			return this.nodeAvailable(new EnergySourceAdjacent(direction));
        {
            return super.canConnect(direction, type);
        }

        return false;
    }

    @Override
    public int receiveOxygen(Direction from, int receive, IFluidHandler.FluidAction action)
    {
        if (this.getOxygenInputDirections().contains(from))
        {
            if (action.simulate())
            {
                return this.getOxygenRequest(from);
            }

            return this.receiveOxygen(receive, action);
        }

        return 0;
    }

    public int receiveOxygen(int receive, IFluidHandler.FluidAction action)
    {
        if (receive > 0)
        {
            int prevOxygenStored = this.getOxygenStored();
            int newStoredOxygen = Math.min(prevOxygenStored + receive, this.getMaxOxygenStored());

            if (action.execute())
            {
                TileEntityOxygen.timeSinceOxygenRequest = 20;
                this.setOxygenStored(newStoredOxygen);
            }

            return Math.max(newStoredOxygen - prevOxygenStored, 0);
        }

        return 0;
    }

    @Override
    public int provideOxygen(Direction from, int request, IFluidHandler.FluidAction action)
    {
        if (this.getOxygenOutputDirections().contains(from))
        {
            return this.drawOxygen(request, action);
        }

        return 0;
    }

    public int drawOxygen(int request, IFluidHandler.FluidAction action)
    {
        if (request > 0)
        {
            int requestedOxygen = Math.min(request, this.getOxygenStored());

            if (action.execute())
            {
                this.setOxygenStored(this.getOxygenStored() - requestedOxygen);
            }

            return requestedOxygen;
        }

        return 0;
    }

    public void produceOxygen()
    {
        if (!this.world.isRemote)
        {
            for (Direction direction : this.getOxygenOutputDirections())
            {
                if (direction != null)
                {
                    this.produceOxygen(direction);
                }
            }
        }
    }

    public boolean produceOxygen(Direction outputDirection)
    {
        int provide = this.getOxygenProvide(outputDirection);

        if (provide > 0)
        {
            TileEntity outputTile = new BlockVec3(this).getTileEntityOnSide(this.world, outputDirection);
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
                    int acceptedOxygen = ((IOxygenReceiver) outputTile).receiveOxygen(outputDirection.getOpposite(), provide, IFluidHandler.FluidAction.EXECUTE);
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
//                    GasStack toSend = new GasStack((Gas) EnergyConfigHandler.gasOxygen, (int) Math.floor(provide));
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

    @Override
    public int getOxygenRequest(Direction direction)
    {
        if (this.shouldPullOxygen())
        {
            return Math.min(this.oxygenPerTick * 2, this.getMaxOxygenStored() - this.getOxygenStored());
        }
        else
        {
            return 0;
        }
    }

    @Override
    public boolean shouldPullOxygen()
    {
        return this.getOxygenStored() < this.getMaxOxygenStored();
    }

    /**
     * Make sure this does not exceed the oxygen stored.
     * This should return 0 if no oxygen is stored.
     * Implementing tiles must respect this or you will generate infinite oxygen.
     */
    @Override
    public int getOxygenProvide(Direction direction)
    {
        return 0;
    }

//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public int receiveGas(Direction side, GasStack stack, boolean doTransfer)
//    {
//        if (!stack.getGas().getName().equals("oxygen"))
//        {
//            return 0;
//        }
//        return (int) Math.floor(this.receiveOxygen(stack.amount, doTransfer));
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public int receiveGas(Direction side, GasStack stack)
//    {
//        return this.receiveGas(LogicalSide, stack, true);
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public GasStack drawGas(Direction side, int amount, boolean doTransfer)
//    {
//        return new GasStack((Gas) EnergyConfigHandler.gasOxygen, (int) Math.floor(this.drawOxygen(amount, doTransfer)));
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public GasStack drawGas(Direction side, int amount)
//    {
//        return this.drawGas(LogicalSide, amount, true);
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public boolean canReceiveGas(Direction side, Gas type)
//    {
//        return type.getName().equals("oxygen") && this.getOxygenInputDirections().contains(LogicalSide);
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
//    public boolean canDrawGas(Direction side, Gas type)
//    {
//        return type.getName().equals("oxygen") && this.getOxygenOutputDirections().contains(LogicalSide);
//    }
//
//    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.ITubeConnection", modID = CompatibilityManager.modidMekanism)
//    public boolean canTubeConnect(Direction side)
//    {
//        return this.canConnect(side, NetworkType.FLUID);
//    } TODO Mekanism Support

    @Override
    public int fill(Direction from, FluidStack resource, IFluidHandler.FluidAction action)
    {
        if (this.getOxygenInputDirections().contains(from) && resource != null)
        {
            if (action.simulate())
            {
                return this.getOxygenRequest(from);
            }

            return this.receiveOxygen(resource.getAmount(), action);
        }

        return 0;
    }

    @Override
    public FluidStack drain(Direction from, FluidStack resource, IFluidHandler.FluidAction action)
    {
        return resource == null ? null : drain(from, resource.getAmount(), action);
    }

    @Override
    public FluidStack drain(Direction from, int maxDrain, IFluidHandler.FluidAction action)
    {
        if (this.getOxygenOutputDirections().contains(from))
        {
            return new FluidStack(GCFluids.OXYGEN.getFluid(), this.drawOxygen(maxDrain, action));
        }

        return null;
    }

    @Override
    public boolean canFill(Direction from, Fluid fluid)
    {
        return this.getOxygenInputDirections().contains(from) && (fluid == null || fluid.getRegistryName().getPath().equals("oxygen"));
    }

    @Override
    public boolean canDrain(Direction from, Fluid fluid)
    {
        return this.getOxygenOutputDirections().contains(from) && (fluid == null || fluid.getRegistryName().getPath().equals("oxygen"));
    }

//    @Override
//    public FluidTankInfo[] getTankInfo(Direction from)
//    {
//        if (canConnect(from, NetworkType.FLUID))
//        {
//            return new FluidTankInfo[] { this.tank.getInfo() };
//        }
//
//        return new FluidTankInfo[] {};
//    }


    @Override
    public int getTanks()
    {
        return this.tank.getTanks();
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank)
    {
        return this.tank.getFluidInTank(tank);
    }

    @Override
    public int getTankCapacity(int tank)
    {
        return this.tank.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack)
    {
        return this.tank.isFluidValid(tank, stack);
    }
}
