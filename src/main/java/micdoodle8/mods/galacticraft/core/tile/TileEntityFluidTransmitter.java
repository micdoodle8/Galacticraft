package micdoodle8.mods.galacticraft.core.tile;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.grid.IGridNetwork;
import micdoodle8.mods.galacticraft.api.transmission.grid.IOxygenNetwork;
import micdoodle8.mods.galacticraft.api.transmission.tile.IBufferTransmitter;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkProvider;
import micdoodle8.mods.galacticraft.api.transmission.tile.ITransmitter;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.blocks.BlockFluidPipe;
import micdoodle8.mods.galacticraft.core.fluid.FluidNetwork;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FluidHandlerWrapper;
import micdoodle8.mods.galacticraft.core.wrappers.IFluidHandlerWrapper;
import micdoodle8.mods.miccore.Annotations;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public abstract class TileEntityFluidTransmitter extends TileEntityAdvanced implements IBufferTransmitter<FluidStack>, IFluidHandlerWrapper
{
    private IGridNetwork network;
    public TileEntity[] adjacentConnections = null;
    private int pullAmount;
    private boolean validated = true;

    public TileEntityFluidTransmitter(String tileName, int pullAmount)
    {
        super(tileName);
        this.pullAmount = pullAmount;
    }

    @Override
    public void validate()
    {
        this.validated = false;
        super.validate();
    }

    @Override
    public void invalidate()
    {
        if (!BlockFluidPipe.ignoreDrop)
        {
            this.getNetwork().split(this);
        }
//        else
//        {
//            this.setNetwork(null);
//        }

        super.invalidate();
    }

    @Override
    public void onChunkUnload()
    {
        super.invalidate();
        super.onChunkUnload();
    }

//    @Override
//    public boolean canUpdate()
//    {
//        return false;
//    }

    @Override
    public IGridNetwork getNetwork()
    {
        if (this.network == null)
        {
            this.resetNetwork();
        }

        return this.network;
    }

    @Override
    public boolean hasNetwork()
    {
        return this.network != null;
    }

    @Override
    public void onNetworkChanged()
    {
        this.world.markBlockRangeForRenderUpdate(this.getPos(), this.getPos());
    }

    @Override
    public void update()
    {
        if (!this.world.isRemote)
        {
            if (!this.validated)
            {
                TickHandlerServer.oxygenTransmitterUpdates.add(this);
                this.validated = true;
            }

            Block blockType = this.getBlockType();
            if (blockType instanceof BlockFluidPipe && ((BlockFluidPipe) blockType).getMode() == BlockFluidPipe.EnumPipeMode.PULL)
            {
                TileEntity[] tiles = OxygenUtil.getAdjacentFluidConnections(this);

                for (EnumFacing side : EnumFacing.VALUES)
                {
                    TileEntity sideTile = tiles[side.ordinal()];

                    if (sideTile != null && !(sideTile instanceof IBufferTransmitter))
                    {
                        IFluidHandler handler = sideTile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());

                        if (handler != null)
                        {
                            FluidStack received = handler.drain(this.pullAmount, false);

                            if (received != null && received.amount != 0)
                            {
                                handler.drain(this.fill(EnumFacing.DOWN, received, true), true);
                            }
                        }
                    }
                }
            }
        }

        super.update();
    }

    protected void resetNetwork()
    {
        FluidNetwork network = new FluidNetwork();
        network.addTransmitter(this);
        network.register();
        this.setNetwork(network);
    }

    @Override
    public void setNetwork(IGridNetwork network)
    {
        if (this.network == network)
        {
            return;
        }

        if (this.world.isRemote && this.network != null)
        {
            FluidNetwork fluidNetwork = (FluidNetwork) this.network;
            fluidNetwork.removeTransmitter(this);

            if (fluidNetwork.getTransmitters().isEmpty())
            {
                fluidNetwork.unregister();
            }
        }

        this.network = network;

        if (this.world.isRemote && this.network != null)
        {
            ((FluidNetwork) this.network).pipes.add(this);
        }
    }

    @Override
    public void refresh()
    {
        if (!this.world.isRemote)
        {
            this.adjacentConnections = null;

            BlockVec3 thisVec = new BlockVec3(this);
            for (EnumFacing side : EnumFacing.VALUES)
            {
                TileEntity tileEntity = thisVec.getTileEntityOnSide(this.world, side);

                if (tileEntity != null)
                {
                    if (tileEntity.getClass() == this.getClass() && tileEntity instanceof INetworkProvider && ((INetworkProvider) tileEntity).hasNetwork())
                    {
                        if (!(tileEntity instanceof ITransmitter) || (((ITransmitter) tileEntity).canConnect(side.getOpposite(), ((ITransmitter) tileEntity).getNetworkType())))
                        {
                            if (!this.hasNetwork())
                            {
                                this.setNetwork(((INetworkProvider) tileEntity).getNetwork());
                                ((FluidNetwork) this.getNetwork()).addTransmitter(this);
                                ((FluidNetwork) this.getNetwork()).onTransmitterAdded(this);
                            }
                            else if (this.hasNetwork() && !this.getNetwork().equals(((INetworkProvider) tileEntity).getNetwork()))
                            {
                                this.setNetwork((IGridNetwork) this.getNetwork().merge(((INetworkProvider) tileEntity).getNetwork()));
                            }
                        }
                    }
                }
            }

            this.getNetwork().refresh();
        }
    }

    @Override
    public TileEntity[] getAdjacentConnections()
    {
        /**
         * Cache the adjacentConnections.
         */
        if (this.adjacentConnections == null)
        {
            this.adjacentConnections = OxygenUtil.getAdjacentFluidConnections(this, this.world.isRemote);
        }

        return this.adjacentConnections;
    }

    @Override
    public boolean canConnect(EnumFacing direction, NetworkType type)
    {
        return type == NetworkType.FLUID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), this.getPos().getX() + 1, this.getPos().getY() + 1, this.getPos().getZ() + 1);
    }

    @Override
    public NetworkType getNetworkType()
    {
        return NetworkType.FLUID;
    }

    @Override
    public double getPacketRange()
    {
        return 0;
    }

    @Override
    public int getPacketCooldown()
    {
        return 0;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return false;
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer)
    {
        if (!stack.getGas().getName().equals("oxygen"))
        {
            return 0;
        }
        return stack.amount - (int) Math.floor(((IOxygenNetwork) this.getNetwork()).produce(stack.amount, this));
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public int receiveGas(EnumFacing side, GasStack stack)
    {
        return this.receiveGas(side, stack, true);
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer)
    {
        return null;
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public GasStack drawGas(EnumFacing side, int amount)
    {
        return null;
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public boolean canDrawGas(EnumFacing side, Gas type)
    {
        return false;
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public boolean canReceiveGas(EnumFacing side, Gas type)
    {
        return type.getName().equals("oxygen");
    }

    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.ITubeConnection", modID = CompatibilityManager.modidMekanism)
    public boolean canTubeConnect(EnumFacing side)
    {
        return this.canConnect(side, NetworkType.FLUID);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return (T) new FluidHandlerWrapper(this, facing);
        }
        return super.getCapability(capability, facing);
    }
}
