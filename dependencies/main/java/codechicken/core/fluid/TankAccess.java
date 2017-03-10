package codechicken.core.fluid;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

@Deprecated
public class TankAccess {
    public IFluidHandler tank;
    public EnumFacing face;

    public TankAccess(IFluidHandler tank, EnumFacing face) {
        this.tank = tank;
        this.face = face;
    }

    public TankAccess(IFluidHandler tank, int side) {
        this(tank, EnumFacing.values()[side]);
    }

    public int fill(FluidStack resource, boolean doFill) {
        return tank.fill(face, resource, doFill);
    }

    public FluidStack drain(int maxDrain, boolean doDrain) {
        return tank.drain(face, maxDrain, doDrain);
    }
}
