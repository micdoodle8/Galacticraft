package micdoodle8.mods.galacticraft.core.wrappers;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class FluidHandlerWrapper implements IFluidHandler
{
    public IFluidHandlerWrapper wrapper;

    public EnumFacing side;

    public FluidHandlerWrapper(IFluidHandlerWrapper w, EnumFacing s)
    {
        wrapper = w;
        side = s;
    }

    @Override
    public IFluidTankProperties[] getTankProperties()
    {
        FluidTankInfo[] infos = wrapper.getTankInfo(side); 
        if (infos != null)
        {
            FluidTankProperties[] properties = new FluidTankProperties[infos.length];
            for (int i = 0; i < infos.length; i++)
            {
                FluidTankInfo info = infos[i];
                properties[i] = new FluidTankProperties(info.fluid, info.capacity, this.wrapper.canFill(side, null), this.wrapper.canDrain(side, null));
            }
            return properties;
        }
        return new IFluidTankProperties[]{};
    }

    @Override
    public int fill(FluidStack resource, boolean doFill)
    {
        if (wrapper.canFill(side, resource != null ? resource.getFluid() : null))
        {
            return wrapper.fill(side, resource, doFill);
        }

        return 0;
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain)
    {
        if (wrapper.canDrain(side, resource != null ? resource.getFluid() : null))
        {
            return wrapper.drain(side, resource, doDrain);
        }

        return null;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain)
    {
        if (wrapper.canDrain(side, null))
        {
            return wrapper.drain(side, maxDrain, doDrain);
        }

        return null;
    }
}