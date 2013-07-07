package micdoodle8.mods.galacticraft.api.entity;

import net.minecraftforge.fluids.FluidStack;

public interface IFuelable
{
    public int addFuel(FluidStack liquid, int amount, boolean doDrain);

    public FluidStack removeFuel(FluidStack liquid, int amount);
}
