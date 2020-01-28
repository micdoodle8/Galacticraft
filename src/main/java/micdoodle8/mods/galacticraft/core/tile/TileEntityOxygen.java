package micdoodle8.mods.galacticraft.core.tile;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IOxygenReceiver;
import micdoodle8.mods.galacticraft.api.transmission.tile.IOxygenStorage;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GCFluids;
import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlock;
import micdoodle8.mods.galacticraft.core.fluid.FluidNetwork;
import micdoodle8.mods.galacticraft.core.fluid.NetworkHelper;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.wrappers.FluidHandlerWrapper;
import micdoodle8.mods.galacticraft.core.wrappers.IFluidHandlerWrapper;
import micdoodle8.mods.miccore.Annotations;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public abstract class TileEntityOxygen extends TileBaseElectricBlock implements IOxygenReceiver, IOxygenStorage, IFluidHandlerWrapper
{
    public int oxygenPerTick;
    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTankGC tank;
    public float lastStoredOxygen;
    public static int timeSinceOxygenRequest;

    public TileEntityOxygen(String tileName, int maxOxygen, int oxygenPerTick)
    {
        super(tileName);
        this.tank = new FluidTankGC(maxOxygen, this);
        this.oxygenPerTick = oxygenPerTick;
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
    public void update()
    {
        super.update();

        if (!this.world.isRemote)
        {
            if (TileEntityOxygen.timeSinceOxygenRequest > 0)
            {
                TileEntityOxygen.timeSinceOxygenRequest--;
            }

            if (this.shouldUseOxygen())
            {
                if (this.tank.getFluid() != null)
                {
                    FluidStack fluid = this.tank.getFluid().copy();
                    fluid.amount = Math.max(fluid.amount - this.oxygenPerTick, 0);
                    this.tank.setFluid(fluid);
                }
            }
        }

        this.lastStoredOxygen = this.tank.getFluidAmount();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        if (nbt.hasKey("storedOxygen"))
        {
            this.tank.setFluid(new FluidStack(GCFluids.fluidOxygenGas, nbt.getInteger("storedOxygen")));
        }
        else if (nbt.hasKey("storedOxygenF"))
        {
            int oxygen = (int) nbt.getFloat("storedOxygenF");
            oxygen = Math.min(this.tank.getCapacity(), oxygen);
            this.tank.setFluid(new FluidStack(GCFluids.fluidOxygenGas, oxygen));
        }
        else
        {
            this.tank.readFromNBT(nbt);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        this.tank.writeToNBT(nbt);
        return nbt;
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }
    
    @Override
    public void setOxygenStored(int oxygen)
    {
        this.tank.setFluid(new FluidStack(GCFluids.fluidOxygenGas, (int) Math.max(Math.min(oxygen, this.getMaxOxygenStored()), 0)));
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

    public EnumSet<EnumFacing> getOxygenInputDirections()
    {
        return EnumSet.allOf(EnumFacing.class);
    }

    public EnumSet<EnumFacing> getOxygenOutputDirections()
    {
        return EnumSet.noneOf(EnumFacing.class);
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
    public int receiveOxygen(EnumFacing from, int receive, boolean doReceive)
    {
        if (this.getOxygenInputDirections().contains(from))
        {
            if (!doReceive)
            {
                return this.getOxygenRequest(from);
            }

            return this.receiveOxygen(receive, doReceive);
        }

        return 0;
    }

    public int receiveOxygen(int receive, boolean doReceive)
    {
        if (receive > 0)
        {
            int prevOxygenStored = this.getOxygenStored();
            int newStoredOxygen = Math.min(prevOxygenStored + receive, this.getMaxOxygenStored());

            if (doReceive)
            {
                TileEntityOxygen.timeSinceOxygenRequest = 20;
                this.setOxygenStored(newStoredOxygen);
            }

            return Math.max(newStoredOxygen - prevOxygenStored, 0);
        }

        return 0;
    }

    @Override
    public int provideOxygen(EnumFacing from, int request, boolean doProvide)
    {
        if (this.getOxygenOutputDirections().contains(from))
        {
            return this.drawOxygen(request, doProvide);
        }

        return 0;
    }

    public int drawOxygen(int request, boolean doProvide)
    {
        if (request > 0)
        {
            int requestedOxygen = Math.min(request, this.getOxygenStored());

            if (doProvide)
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
            for (EnumFacing direction : this.getOxygenOutputDirections())
            {
                if (direction != null)
                {
                    this.produceOxygen(direction);
                }
            }
        }
    }

    public boolean produceOxygen(EnumFacing outputDirection)
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
                    int acceptedOxygen = ((IOxygenReceiver) outputTile).receiveOxygen(outputDirection.getOpposite(), provide, true);
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
    public int getOxygenRequest(EnumFacing direction)
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
    public int getOxygenProvide(EnumFacing direction)
    {
        return 0;
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer)
    {
        if (!stack.getGas().getName().equals("oxygen"))
        {
            return 0;
        }
        return (int) Math.floor(this.receiveOxygen(stack.amount, doTransfer));
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public int receiveGas(EnumFacing side, GasStack stack)
    {
        return this.receiveGas(side, stack, true);
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer)
    {
        return new GasStack((Gas) EnergyConfigHandler.gasOxygen, (int) Math.floor(this.drawOxygen(amount, doTransfer)));
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public GasStack drawGas(EnumFacing side, int amount)
    {
        return this.drawGas(side, amount, true);
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public boolean canReceiveGas(EnumFacing side, Gas type)
    {
        return type.getName().equals("oxygen") && this.getOxygenInputDirections().contains(side);
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public boolean canDrawGas(EnumFacing side, Gas type)
    {
        return type.getName().equals("oxygen") && this.getOxygenOutputDirections().contains(side);
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.ITubeConnection", modID = CompatibilityManager.modidMekanism)
    public boolean canTubeConnect(EnumFacing side)
    {
        return this.canConnect(side, NetworkType.FLUID);
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill)
    {
        if (this.getOxygenInputDirections().contains(from) && resource != null)
        {
            if (!doFill)
            {
                return this.getOxygenRequest(from);
            }

            return this.receiveOxygen(resource.amount, doFill);
        }

        return 0;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
    {
        return resource == null ? null : drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
    {
        if (this.getOxygenOutputDirections().contains(from))
        {
            return new FluidStack(GCFluids.fluidOxygenGas, this.drawOxygen(maxDrain, doDrain));
        }

        return null;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    {
        return this.getOxygenInputDirections().contains(from) && (fluid == null || fluid.getName().equals("oxygen"));
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid)
    {
        return this.getOxygenOutputDirections().contains(from) && (fluid == null || fluid.getName().equals("oxygen"));
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from)
    {
        if (canConnect(from, NetworkType.FLUID))
        {
            return new FluidTankInfo[] { this.tank.getInfo() };
        }

        return new FluidTankInfo[] {};
    }
}
