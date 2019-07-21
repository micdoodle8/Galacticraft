package micdoodle8.mods.galacticraft.core.tile;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.util.DelayTimer;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FluidHandlerWrapper;
import micdoodle8.mods.galacticraft.core.wrappers.IFluidHandlerWrapper;
import micdoodle8.mods.galacticraft.core.wrappers.ScheduledBlockChange;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityFluidTank extends TileEntityAdvanced implements IFluidHandlerWrapper
{
    public FluidTankGC fluidTank = new FluidTankGC(16000, this);
    public boolean updateClient = false;
    private DelayTimer delayTimer = new DelayTimer(1);
    private AxisAlignedBB renderAABB;

    public TileEntityFluidTank()
    {
        super("tile.fluid_tank.name");
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

    public void onBreak()
    {
        if (fluidTank.getFluidAmount() > 0)
        {
            FluidEvent.fireEvent(new FluidEvent.FluidSpilledEvent(fluidTank.getFluid(), world, pos));
            if (!this.world.isRemote && fluidTank.getFluidAmount() > 1000)
            {
                Block b = fluidTank.getFluid().getFluid().getBlock();
                if (!(b == null || b instanceof BlockAir))
                {
                	TickHandlerServer.scheduleNewBlockChange(GCCoreUtil.getDimensionID(this.world), new ScheduledBlockChange(pos, b.getStateFromMeta(0), 3));
                }
            }
        }
    }

    @Override
    public void update()
    {
        super.update();

        if (fluidTank.getFluid() != null)
        {
            moveFluidDown();
        }

        if (!this.world.isRemote && updateClient && delayTimer.markTimeIfDelay(this.world))
        {
            PacketDynamic packet = new PacketDynamic(this);
            GalacticraftCore.packetPipeline.sendToAllAround(packet, new NetworkRegistry.TargetPoint(GCCoreUtil.getDimensionID(this.world), getPos().getX(), getPos().getY(), getPos().getZ(), this.getPacketRange()));
            this.updateClient = false;
        }
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill)
    {
        if (resource == null)
        {
            return 0;
        }

        FluidStack copy = resource.copy();
        int totalUsed = 0;
        TileEntityFluidTank toFill = getLastTank();

        FluidStack fluid = toFill.fluidTank.getFluid();
        if (fluid != null && fluid.amount > 0 && !fluid.isFluidEqual(copy))
        {
            return 0;
        }

        while (toFill != null && copy.amount > 0)
        {
            int used = toFill.fluidTank.fill(copy, doFill);
            if (used > 0)
            {
                toFill.updateClient = true;
            }
            copy.amount -= used;
            totalUsed += used;
            toFill = getNextTank(toFill.getPos());
        }

        return totalUsed;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
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
        return drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
    {
        TileEntityFluidTank last = getLastTank();
        FluidStack stack = last.fluidTank.drain(maxDrain, doDrain);
        if (doDrain && stack != null && stack.amount > 0)
        {
            last.updateClient = true;
        }
        return stack;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    {
        return fluidTank.getFluid() == null || fluidTank.getFluid().getFluid() == null || fluid == null || fluidTank.getFluid().getFluid() == fluid;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid)
    {
        return fluid == null || fluidTank.getFluid() != null && fluidTank.getFluid().getFluid() == fluid;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from)
    {
        FluidTank compositeTank = new FluidTank(fluidTank.getCapacity());
        TileEntityFluidTank last = getLastTank();

        if (last != null && last.fluidTank.getFluid() != null)
        {
            compositeTank.setFluid(last.fluidTank.getFluid().copy());
        }
        else
        {
            return new FluidTankInfo[] { compositeTank.getInfo() };
        }

        int capacity = last.fluidTank.getCapacity();
        last = getNextTank(last.getPos());

        while (last != null)
        {
            FluidStack fluid = last.fluidTank.getFluid();
            if (fluid == null || fluid.amount == 0)
            {

            }
            else if (!compositeTank.getFluid().isFluidEqual(fluid))
            {
                break;
            }
            else
            {
                compositeTank.getFluid().amount += fluid.amount;
            }

            capacity += last.fluidTank.getCapacity();
            last = getNextTank(last.getPos());
        }

        compositeTank.setCapacity(capacity);
        return new FluidTankInfo[] { compositeTank.getInfo() };
    }

    public void moveFluidDown()
    {
        TileEntityFluidTank next = getPreviousTank(this.getPos());
        if (next != null)
        {
            int used = next.fluidTank.fill(fluidTank.getFluid(), true);

            if (used > 0)
            {
                this.updateClient = true;
                next.updateClient = true;
                fluidTank.drain(used, true);
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
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        if (nbt.hasKey("fuelTank"))
        {
            this.fluidTank.readFromNBT(nbt.getCompoundTag("fuelTank"));
        }

        this.updateClient = GCCoreUtil.getEffectiveSide() == Side.SERVER;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        if (this.fluidTank.getFluid() != null)
        {
            nbt.setTag("fuelTank", this.fluidTank.writeToNBT(new NBTTagCompound()));
        }
        return nbt;
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("net-type", "desc-packet");
        PacketDynamic packet = new PacketDynamic(this);
        ByteBuf buf = Unpooled.buffer();
        packet.encodeInto(buf);
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        nbt.setByteArray("net-data", bytes);
        SPacketUpdateTileEntity tileUpdate = new SPacketUpdateTileEntity(getPos(), 0, nbt);
        return tileUpdate;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        if (!this.world.isRemote)
        {
            return;
        }
        if (pkt.getNbtCompound() == null)
        {
            throw new RuntimeException("[GC] Missing NBTTag compound!");
        }
        NBTTagCompound nbt = pkt.getNbtCompound();
        try
        {
            if ("desc-packet".equals(nbt.getString("net-type")))
            {
                byte[] bytes = nbt.getByteArray("net-data");
                ByteBuf data = Unpooled.wrappedBuffer(bytes);
                PacketDynamic packet = new PacketDynamic();
                packet.decodeInto(data);
                packet.handleClientSide(FMLClientHandler.instance().getClientPlayerEntity());
            }
        }
        catch (Throwable t)
        {
            throw new RuntimeException("[GC] Failed to read a packet! (" + nbt.getTag("net-type") + ", " + nbt.getTag("net-data"), t);
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
                networkedList.add(fluidTank.getFluid() == null ? "" : fluidTank.getFluid().getFluid().getName());
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
            String fluidName = ByteBufUtils.readUTF8String(buffer);
            FluidTankGC fluidTank = new FluidTankGC(capacity, this);
            int amount = buffer.readInt();

            if (fluidName.equals(""))
            {
                fluidTank.setFluid(null);
            }
            else
            {
                Fluid fluid = FluidRegistry.getFluid(fluidName);
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
        return Constants.RENDERDISTANCE_MEDIUM;
    }
}