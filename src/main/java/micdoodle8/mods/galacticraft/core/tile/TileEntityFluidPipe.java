package micdoodle8.mods.galacticraft.core.tile;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasStack;
import micdoodle8.mods.galacticraft.api.tile.IColorable;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.grid.IGridNetwork;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCFluids;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockFluidPipe;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.fluid.FluidNetwork;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FluidHandlerWrapper;
import micdoodle8.mods.miccore.Annotations;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityFluidPipe extends TileEntityFluidTransmitter implements IColorable
{
    public FluidTankGC buffer = new FluidTankGC(1000, this);
    private boolean dataRequest = false;
    private AxisAlignedBB renderAABB;

    public TileEntityFluidPipe()
    {
        super("tile.fluid_pipe.name", 100);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[0];
    }

    @Override
    protected boolean handleInventory()
    {
        return false;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        // Do not re-create tile entity if only the pipe's color changed!
        if (oldState != newState)
        {
            return oldState.getBlock() != newState.getBlock();
        }

        return super.shouldRefresh(world, pos, oldState, newState);
    }

    @Override
    public boolean canConnect(EnumFacing direction, NetworkType type)
    {
        TileEntity adjacentTile = new BlockVec3(this).getTileEntityOnSide(this.world, direction);

        if (type == NetworkType.FLUID)
        {
            if (adjacentTile instanceof IColorable)
            {
                IBlockState state = this.world.getBlockState(this.getPos());
                IBlockState adjacentTileState = adjacentTile.getWorld().getBlockState(adjacentTile.getPos());
                byte thisCol = this.getColor(state);
                byte otherCol = ((IColorable) adjacentTile).getColor(adjacentTileState);
                return thisCol == otherCol || thisCol == EnumDyeColor.WHITE.getDyeDamage() || otherCol == EnumDyeColor.WHITE.getDyeDamage();
            }

            return true;
        }

        return false;
    }

//    @Override
//    public boolean canUpdate()
//    {
//        return this.world == null || !this.world.isRemote;
//
//    }

    @Override
    public double getPacketRange()
    {
        return 12.0D;
    }

    @Override
    public int getPacketCooldown()
    {
        return 5;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return false;
    }

    @Override
    public void onLoad()
    {
        if (this.world.isRemote)
        {
            this.world.notifyLightSet(getPos());
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_DATA, GCCoreUtil.getDimensionID(this.world), new Object[] { GCCoreUtil.getDimensionID(this.world), this.getPos() }));
        }
    }

    @Override
    public void onColorUpdate()
    {
        if (this.world != null)
        {
            if (this.world.isRemote)
            {
                this.world.notifyLightSet(getPos());
            }
            else
            {
                this.getNetwork().split(this);
                this.resetNetwork();
            }
        }
    }

    @Override
    public byte getColor(IBlockState state)
    {
        if (state.getBlock() instanceof BlockFluidPipe)
        {
            return (byte) state.getValue(BlockFluidPipe.COLOR).getDyeDamage();
        }
        return 15;
    }

    @Override
    public void onAdjacentColorChanged(EnumFacing direction)
    {
        IBlockState state = this.world.getBlockState(this.getPos());
        this.world.notifyBlockUpdate(this.getPos(), state, state, 3);

        if (!this.world.isRemote)
        {
            this.refresh();
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        if (this.buffer.getFluid() != null)
        {
            tagCompound.setTag("buff", this.buffer.writeToNBT(new NBTTagCompound()));
        }
        return tagCompound;
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }
       
    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        if (tagCompound.hasKey("pipeColor"))
        {
            // Backwards compatibility
            this.world.setBlockState(getPos(), this.world.getBlockState(getPos()).withProperty(BlockFluidPipe.COLOR, EnumDyeColor.byDyeDamage(tagCompound.getByte("pipeColor"))));
        }
        
        if (tagCompound.hasKey("buff"))
        {
            this.buffer.readFromNBT(tagCompound.getCompoundTag("buff"));
        }
    }

    @Override
    public FluidStack getBuffer()
    {
        return buffer.getFluid() == null ? null : buffer.getFluid();
    }

    @Override
    public int getCapacity()
    {
        return 200;
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill)
    {
        IGridNetwork network = this.getNetwork();
        if (network instanceof FluidNetwork)
        {
            FluidNetwork fluidNetwork = (FluidNetwork) network;
            return fluidNetwork.emitToBuffer(resource, doFill);
        }
        else
        {
            return this.buffer.fill(resource, doFill);
        }
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
    {
        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
    {
        return null;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    {
        return true;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid)
    {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from)
    {
        return new FluidTankInfo[0];
    }

    public boolean switchType()
    {
        if (this.ticks < 10)
        {
            return false;
        }

        Block block;
        Block currentType = this.getBlockType();

        if (!(currentType instanceof BlockFluidPipe))
        {
            return false;
        }

        switch (((BlockFluidPipe) currentType).getMode())
        {
        case NORMAL:
            block = GCBlocks.oxygenPipePull;
            break;
        default:
            block = GCBlocks.oxygenPipe;
            break;
        }

        BlockFluidPipe.ignoreDrop = true;
        this.world.setBlockState(pos, block.getStateFromMeta(currentType.getMetaFromState(this.world.getBlockState(pos))));
        BlockFluidPipe.ignoreDrop = false;
        if (this.hasNetwork())
        {
            this.refresh();
            this.getNetwork().refresh();
        }

        return true;
    }

    @Override
    public boolean canTransmit()
    {
        Block currentType = this.getBlockType();

        if (!(currentType instanceof BlockFluidPipe))
        {
            // Walkway blocks, etc
            return true;
        }

        return ((BlockFluidPipe) currentType).getMode() != BlockFluidPipe.EnumPipeMode.PULL;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        if (this.renderAABB == null)
        {
            this.renderAABB = new AxisAlignedBB(pos, pos.add(1, 1, 1));
        }
        return this.renderAABB;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return 16384;  //128 squared
    }

    @Override
    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public int receiveGas(EnumFacing side, GasStack stack, boolean doTransfer)
    {
        String mekGas = stack.getGas().getName(); 
        if (mekGas == null)
        {
            return 0;
        }
        FluidStack mekEquivalent = null;
        if (mekGas.equals("oxygen"))
        {
            mekEquivalent = new FluidStack(GCFluids.fluidOxygenGas, stack.amount);
        }
        else if (mekGas.equals("hydrogen"))
        {
            mekEquivalent = new FluidStack(GCFluids.fluidHydrogenGas, stack.amount);
        }
        else
        {
            return 0;
        }
        return this.fill(side, mekEquivalent, doTransfer);
    }

    @Override
    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public int receiveGas(EnumFacing side, GasStack stack)
    {
        return this.receiveGas(side, stack, true);
    }

    @Override
    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public GasStack drawGas(EnumFacing side, int amount, boolean doTransfer)
    {
        return null;
    }

    @Override
    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public GasStack drawGas(EnumFacing side, int amount)
    {
        return null;
    }

    @Override
    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public boolean canReceiveGas(EnumFacing side, Gas type)
    {
        return type.getName().equals("oxygen") || type.getName().equals("hydrogen");
    }

    @Override
    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.IGasHandler", modID = CompatibilityManager.modidMekanism)
    public boolean canDrawGas(EnumFacing side, Gas type)
    {
        return false;
    }

    @Override
    @Annotations.RuntimeInterface(clazz = "mekanism.api.gas.ITubeConnection", modID = CompatibilityManager.modidMekanism)
    public boolean canTubeConnect(EnumFacing side)
    {
        return false;
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
}
