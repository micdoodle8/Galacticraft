package micdoodle8.mods.galacticraft.core.tile;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import micdoodle8.mods.galacticraft.core.BlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.NetworkUtil;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.util.DelayTimer;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FluidHandlerWrapper;
import micdoodle8.mods.galacticraft.core.wrappers.IFluidHandlerWrapper;
import micdoodle8.mods.galacticraft.core.wrappers.ScheduledBlockChange;
import net.minecraft.block.Block;
import net.minecraft.block.AirBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TileEntityFluidTank extends TileEntityAdvanced implements IFluidHandlerWrapper
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.fluidTank)
    public static TileEntityType<TileEntityFluidTank> TYPE;

    public FluidTankGC fluidTank = new FluidTankGC(16000, this);
    public boolean updateClient = false;
    private DelayTimer delayTimer = new DelayTimer(1);
    private AxisAlignedBB renderAABB;

    public TileEntityFluidTank()
    {
        super(TYPE);
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

    @Override
    protected boolean handleInventory()
    {
        return false;
    }

//    public void onBreak()
//    {
//        if (fluidTank.getFluidAmount() > 0)
//        {
//            FluidEvent.fireEvent(new FluidEvent.FluidSpilledEvent(fluidTank.getFluid(), world, pos));
//            if (!this.world.isRemote && fluidTank.getFluidAmount() > 1000)
//            {
//                Block b = fluidTank.getFluid().getFluid().getBlock();
//                if (!(b == null || b instanceof AirBlock))
//                {
//                	TickHandlerServer.scheduleNewBlockChange(GCCoreUtil.getDimensionID(this.world), new ScheduledBlockChange(pos, b.getStateFromMeta(0), 3));
//                }
//            }
//        }
//    }

    @Override
    public void tick()
    {
        super.tick();

        if (fluidTank.getFluid() != FluidStack.EMPTY)
        {
            moveFluidDown();
        }

        if (!this.world.isRemote && updateClient && delayTimer.markTimeIfDelay(this.world))
        {
            PacketDynamic packet = new PacketDynamic(this);
            GalacticraftCore.packetPipeline.sendToAllAround(packet, new PacketDistributor.TargetPoint(getPos().getX(), getPos().getY(), getPos().getZ(), this.getPacketRange(), GCCoreUtil.getDimensionID(this.world)));
            this.updateClient = false;
        }
    }

    @Override
    public int fill(Direction from, FluidStack resource, IFluidHandler.FluidAction action)
    {
        if (resource == null)
        {
            return 0;
        }

        FluidStack copy = resource.copy();
        int totalUsed = 0;
        TileEntityFluidTank toFill = getLastTank();

        FluidStack fluid = toFill.fluidTank.getFluid();
        if (fluid != null && fluid.getAmount() > 0 && !fluid.isFluidEqual(copy))
        {
            return 0;
        }

        while (toFill != null && copy.getAmount() > 0)
        {
            int used = toFill.fluidTank.fill(copy, action);
            if (used > 0)
            {
                toFill.updateClient = true;
            }
            copy.setAmount(copy.getAmount() - used);
            totalUsed += used;
            toFill = getNextTank(toFill.getPos());
        }

        return totalUsed;
    }

    @Override
    public FluidStack drain(Direction from, FluidStack resource, IFluidHandler.FluidAction action)
    {
        if (resource == null)
        {
            return null;
        }
        TileEntityFluidTank last = getLastTank();
        if (!resource.isFluidEqual(last.fluidTank.getFluid()))
        {
            return null;
        }
        return drain(from, resource.getAmount(), action);
    }

    @Override
    public FluidStack drain(Direction from, int maxDrain, IFluidHandler.FluidAction action)
    {
        TileEntityFluidTank last = getLastTank();
        FluidStack stack = last.fluidTank.drain(maxDrain, action);
        if (action.execute() && stack != null && stack.getAmount() > 0)
        {
            last.updateClient = true;
        }
        return stack;
    }

    @Override
    public boolean canFill(Direction from, Fluid fluid)
    {
        return fluidTank.getFluid() == FluidStack.EMPTY || fluidTank.getFluid() == FluidStack.EMPTY || fluid == null || fluidTank.getFluid().getFluid() == fluid;
    }

    @Override
    public boolean canDrain(Direction from, Fluid fluid)
    {
        return fluid == null || fluidTank.getFluid() != FluidStack.EMPTY && fluidTank.getFluid().getFluid() == fluid;
    }

    @Override
    public int getTanks()
    {
        return 1;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank)
    {
        TileEntityFluidTank last = getLastTank();

        if (last != null)
        {
            return last.fluidTank.getFluid();
        }
        else
        {
            return FluidStack.EMPTY;
        }
    }

    @Override
    public int getTankCapacity(int tank)
    {
        if (tank != 0)
        {
            return 0;
        }

        TileEntityFluidTank last = getLastTank();

        int capacity = last.fluidTank.getCapacity();
        last = getNextTank(last.getPos());

        while (last != null)
        {
            capacity += last.fluidTank.getCapacity();
            last = getNextTank(last.getPos());
        }

        return capacity;
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack)
    {
        if (tank != 0)
        {
            return false;
        }

        TileEntityFluidTank last = getLastTank();

        if (last != null)
        {
            if (last.fluidTank.getFluid() != FluidStack.EMPTY)
            {
                return last.fluidTank.getFluid().getFluid() == stack.getFluid();
            }
            else
            {
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    //    @Override
//    public FluidTankInfo[] getTankInfo(Direction from)
//    {
//        FluidTank compositeTank = new FluidTank(fluidTank.getCapacity());
//        TileEntityFluidTank last = getLastTank();
//
//        if (last != null && last.fluidTank.getFluid() != FluidStack.EMPTY)
//        {
//            compositeTank.setFluid(last.fluidTank.getFluid().copy());
//        }
//        else
//        {
//            return new FluidTankInfo[] { compositeTank.getInfo() };
//        }
//
//        int capacity = last.fluidTank.getCapacity();
//        last = getNextTank(last.getPos());
//
//        while (last != null)
//        {
//            FluidStack fluid = last.fluidTank.getFluid();
//            if (fluid == null || fluid.amount == 0)
//            {
//
//            }
//            else if (!compositeTank.getFluid().isFluidEqual(fluid))
//            {
//                break;
//            }
//            else
//            {
//                compositeTank.getFluid().getAmount() += fluid.amount;
//            }
//
//            capacity += last.fluidTank.getCapacity();
//            last = getNextTank(last.getPos());
//        }
//
//        compositeTank.setCapacity(capacity);
//        return new FluidTankInfo[] { compositeTank.getInfo() };
//    }

    public void moveFluidDown()
    {
        TileEntityFluidTank next = getPreviousTank(this.getPos());
        if (next != null)
        {
            int used = next.fluidTank.fill(fluidTank.getFluid(), IFluidHandler.FluidAction.EXECUTE);

            if (used > 0)
            {
                this.updateClient = true;
                next.updateClient = true;
                fluidTank.drain(used, IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

    public TileEntityFluidTank getLastTank()
    {
        TileEntityFluidTank lastTank = this;

        while (true)
        {
            TileEntityFluidTank tank = getPreviousTank(lastTank.getPos());
            if (tank != null)
            {
                lastTank = tank;
            }
            else
            {
                break;
            }
        }

        return lastTank;
    }

    public TileEntityFluidTank getNextTank(BlockPos current)
    {
        TileEntity above = this.world.getTileEntity(current.up());
        if (above instanceof TileEntityFluidTank)
        {
            return (TileEntityFluidTank) above;
        }
        return null;
    }

    public TileEntityFluidTank getPreviousTank(BlockPos current)
    {
        TileEntity below = this.world.getTileEntity(current.down());
        if (below instanceof TileEntityFluidTank)
        {
            return (TileEntityFluidTank) below;
        }
        return null;
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);

        if (nbt.contains("fuelTank"))
        {
            this.fluidTank.readFromNBT(nbt.getCompound("fuelTank"));
        }

        this.updateClient = GCCoreUtil.getEffectiveSide() == LogicalSide.SERVER;
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);

        if (this.fluidTank.getFluid() != FluidStack.EMPTY)
        {
            nbt.put("fuelTank", this.fluidTank.writeToNBT(new CompoundNBT()));
        }
        return nbt;
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.write(new CompoundNBT());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("net-type", "desc-packet");
        PacketDynamic packet = new PacketDynamic(this);
        ByteBuf buf = Unpooled.buffer();
        packet.encodeInto(buf);
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        nbt.putByteArray("net-data", bytes);
        SUpdateTileEntityPacket tileUpdate = new SUpdateTileEntityPacket(getPos(), 0, nbt);
        return tileUpdate;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        if (!this.world.isRemote)
        {
            return;
        }
        if (pkt.getNbtCompound() == null)
        {
            throw new RuntimeException("[GC] Missing NBTTag compound!");
        }
        CompoundNBT nbt = pkt.getNbtCompound();
        try
        {
            if ("desc-packet".equals(nbt.getString("net-type")))
            {
                byte[] bytes = nbt.getByteArray("net-data");
                ByteBuf data = Unpooled.wrappedBuffer(bytes);
                PacketDynamic packet = new PacketDynamic();
                packet.decodeInto(data);
                packet.handleClientSide(Minecraft.getInstance().player);
            }
        }
        catch (Throwable t)
        {
            throw new RuntimeException("[GC] Failed to read a packet! (" + nbt.get("net-type") + ", " + nbt.get("net-data"), t);
        }
    }

    @Override
    public void addExtraNetworkedData(List<Object> networkedList)
    {
        if (!this.world.isRemote)
        {
            if (fluidTank == null)
            {
                networkedList.add(0);
                networkedList.add("");
                networkedList.add(0);
            }
            else
            {
                networkedList.add(fluidTank.getCapacity());
                networkedList.add(fluidTank.getFluid() == FluidStack.EMPTY ? "" : fluidTank.getFluid().getFluid().getRegistryName().toString());
                networkedList.add(fluidTank.getFluidAmount());
            }
        }
    }

    @Override
    public void readExtraNetworkedData(ByteBuf buffer)
    {
        if (this.world.isRemote)
        {
            int capacity = buffer.readInt();
            String fluidName = NetworkUtil.readUTF8String(buffer);
            FluidTankGC fluidTank = new FluidTankGC(capacity, this);
            int amount = buffer.readInt();

            if (fluidName.equals(""))
            {
                fluidTank.setFluid(null);
            }
            else
            {
                Fluid fluid = Registry.FLUID.getOrDefault(new ResourceLocation(fluidName));
//                Fluid fluid = FluidRegistry.getFluid(fluidName);
                fluidTank.setFluid(new FluidStack(fluid, amount));
            }

            this.fluidTank = fluidTank;
        }
    }

    @Override
    public double getPacketRange()
    {
        return 40;
    }

    @Override
    public int getPacketCooldown()
    {
        return 1;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return false;
    }

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
                        return new FluidHandlerWrapper(TileEntityFluidTank.this, facing);
                    }
                });
            }
            return holder.cast();
        }
        return super.getCapability(capability, facing);
    }

//    @Override
//    public boolean hasCapability(Capability<?> capability, @Nullable Direction facing)
//    {
//        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
//    }
//
//    @Nullable
//    @Override
//    public <T> T getCapability(Capability<T> capability, @Nullable Direction facing)
//    {
//        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
//        {
//            return (T) new FluidHandlerWrapper(this, facing);
//        }
//        return super.getCapability(capability, facing);
//    }

    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        if (this.renderAABB == null)
        {
            this.renderAABB = new AxisAlignedBB(pos, pos.add(1, 1, 1));
        }
        return this.renderAABB;
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return Constants.RENDERDISTANCE_MEDIUM;
    }
}