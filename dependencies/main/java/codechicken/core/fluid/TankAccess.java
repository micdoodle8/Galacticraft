package codechicken.core.fluid;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class TankAccess
{
    public IFluidHandler tank;
    public ForgeDirection side;
    
    public TankAccess(IFluidHandler tank, ForgeDirection side)
    {
        this.tank = tank;
        this.side = side;
    }
    
    public TankAccess(IFluidHandler tank, int side)
    {
        this(tank, ForgeDirection.getOrientation(side));
    }

    public int fill(FluidStack resource, boolean doFill)
    {
        return tank.fill(side, resource, doFill);
    }
    
    public FluidStack drain(int maxDrain, boolean doDrain)
    {
        return tank.drain(side, maxDrain, doDrain);
    }
}
