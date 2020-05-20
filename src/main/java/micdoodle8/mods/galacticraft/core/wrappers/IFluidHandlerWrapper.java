package micdoodle8.mods.galacticraft.core.wrappers;

import net.minecraft.util.Direction;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

public interface IFluidHandlerWrapper 
{
    int fill(Direction from, FluidStack resource, boolean doFill);

    FluidStack drain(Direction from, FluidStack resource, boolean doDrain);

    FluidStack drain(Direction from, int maxDrain, boolean doDrain);

    boolean canFill(Direction from, Fluid fluid);

    boolean canDrain(Direction from, Fluid fluid);
    
    FluidTankInfo[] getTankInfo(Direction from);
}