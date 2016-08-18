package micdoodle8.mods.galacticraft.core.tile;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.List;

public class TileEntityFluidTank extends TileEntityAdvanced implements IFluidHandler
{
    public FluidTankGC fluidTank = new FluidTankGC(16000, this);
    private boolean updateClient = false;

    public void onBreak()
    {
        if (fluidTank.getFluidAmount() > 0)
        {
            FluidEvent.fireEvent(new FluidEvent.FluidSpilledEvent(fluidTank.getFluid(), worldObj, pos));
        }
    }

    @Override
    public void update()
    {
        super.update();
        if (this.ticks >= 20)
        {
            this.updateClient = false;
        }

        if (fluidTank.getFluid() != null)
        {
            moveFluidDown();
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
                updateClient = true;
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
        last.updateClient = true;
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
        return last.fluidTank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    {
        return fluidTank.getFluid() == null || fluidTank.getFluid().getFluid() == null || fluidTank.getFluid().getFluid() == fluid;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid)
    {
        return fluidTank.getFluid() == null || fluidTank.getFluid().getFluid() == null || fluidTank.getFluid().getFluid() == fluid;
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
        TileEntity above = worldObj.getTileEntity(current.up());
        if (above instanceof TileEntityFluidTank)
        {
            return (TileEntityFluidTank) above;
        }
        return null;
    }

    public TileEntityFluidTank getPreviousTank(BlockPos current)
    {
        TileEntity below = this.worldObj.getTileEntity(current.down());
        if (below instanceof TileEntityFluidTank)
        {
            return (TileEntityFluidTank) below;
        }
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        if (compound.hasKey("fuelTank"))
        {
            this.fluidTank.readFromNBT(compound.getCompoundTag("fuelTank"));
        }

        this.updateClient = true;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        if (this.fluidTank.getFluid() != null)
        {
            compound.setTag("fuelTank", this.fluidTank.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void addExtraNetworkedData(List<Object> networkedList)
    {
        if (!this.worldObj.isRemote)
        {
            System.err.println("SEND");
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
        if (this.worldObj.isRemote)
        {
            System.err.println("REC");
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
        return this.updateClient && this.ticks >= 20;
    }
}