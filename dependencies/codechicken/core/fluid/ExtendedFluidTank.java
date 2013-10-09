package codechicken.core.fluid;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

public class ExtendedFluidTank implements IFluidTank
{
    private FluidStack fluid;
    private boolean changeType;
    private int capacity;
    
    public ExtendedFluidTank(FluidStack type, int capacity)
    {
        if(type == null)
        {
            fluid = new FluidStack(0, 0);
            changeType = true;
        }
        else
            fluid = FluidUtils.copy(type, 0);
        this.capacity = capacity;
    }
    
    public ExtendedFluidTank(int capacity)
    {
        this(null, capacity);
    }
    
    @Override
    public FluidStack getFluid()
    {
        return fluid.copy();
    }

    @Override
    public int getCapacity()
    {
        return capacity;
    }

    public boolean canAccept(FluidStack type)
    {
        return type == null || type.fluidID <= 0 || (fluid.amount == 0 && changeType) || fluid.isFluidEqual(type);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill)
    {
        if(resource == null || resource.fluidID <= 0)
            return 0;

        if(!canAccept(resource))
            return 0;
        
        int tofill = Math.min(getCapacity()-fluid.amount, resource.amount);
        if(doFill && tofill > 0)
        {
            if(!fluid.isFluidEqual(resource))
                fluid = FluidUtils.copy(resource, fluid.amount+tofill);
            else
                fluid.amount+=tofill;
            onLiquidChanged();
        }
        
        return tofill;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain)
    {
        if(fluid.amount == 0 || maxDrain <= 0)
            return null;
        
        int todrain = Math.min(maxDrain, fluid.amount);
        if(doDrain && todrain > 0)
        {
            fluid.amount-=todrain;
            onLiquidChanged();
        }
        return FluidUtils.copy(fluid, todrain);
    }
    
    public FluidStack drain(FluidStack resource, boolean doDrain)
    {
        if (resource == null || !resource.isFluidEqual(fluid))
            return null;
        
        return drain(resource.amount, doDrain);
    }

    public void onLiquidChanged()
    {
    }

    public void fromTag(NBTTagCompound tag)
    {
        fluid = FluidUtils.read(tag);
    }
    
    public NBTTagCompound toTag()
    {
        return FluidUtils.write(fluid, new NBTTagCompound());
    }

    @Override
    public int getFluidAmount()
    {
        return fluid.amount;
    }

    @Override
    public FluidTankInfo getInfo()
    {
        return new FluidTankInfo(this);
    }
}
