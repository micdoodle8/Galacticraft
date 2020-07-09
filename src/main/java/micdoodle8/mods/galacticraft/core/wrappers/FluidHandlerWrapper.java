package micdoodle8.mods.galacticraft.core.wrappers;

import net.minecraft.fluid.Fluids;
import net.minecraft.util.Direction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

public class FluidHandlerWrapper implements IFluidHandler
{
    public IFluidHandlerWrapper wrapper;

    public Direction side;

    public FluidHandlerWrapper(IFluidHandlerWrapper w, Direction s)
    {
        wrapper = w;
        side = s;
    }

    @Override
    public int getTanks()
    {
        return wrapper.getTanks();
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank)
    {
        return wrapper.getFluidInTank(tank);
    }

    @Override
    public int getTankCapacity(int tank)
    {
        return wrapper.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack)
    {
        return wrapper.isFluidValid(tank, stack);
    }

    @Override
    public int fill(FluidStack resource, IFluidHandler.FluidAction action)
    {
        if (wrapper.canFill(side, resource != null ? resource.getFluid() : null))
        {
            return wrapper.fill(side, resource, action);
        }

        return 0;
    }

    @Override
    public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action)
    {
        if (wrapper.canDrain(side, resource != FluidStack.EMPTY ? resource.getFluid() : Fluids.EMPTY))
        {
            return wrapper.drain(side, resource, action);
        }

        return null;
    }

    @Override
    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action)
    {
        if (wrapper.canDrain(side, Fluids.EMPTY))
        {
            return wrapper.drain(side, maxDrain, action);
        }

        return null;
    }
}