package micdoodle8.mods.galacticraft.core.wrappers;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.Direction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

public interface IFluidHandlerWrapper
{
    int fill(Direction from, FluidStack resource, IFluidHandler.FluidAction fillAction);

    FluidStack drain(Direction from, FluidStack resource, IFluidHandler.FluidAction fillAction);

    FluidStack drain(Direction from, int maxDrain, IFluidHandler.FluidAction fillAction);

    boolean canFill(Direction from, Fluid fluid);

    boolean canDrain(Direction from, Fluid fluid);

    int getTanks();

    @Nonnull
    FluidStack getFluidInTank(int tank);

    int getTankCapacity(int tank);

    boolean isFluidValid(int tank, @Nonnull FluidStack stack);
}