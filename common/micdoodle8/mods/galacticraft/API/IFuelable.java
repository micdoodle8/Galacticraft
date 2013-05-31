package micdoodle8.mods.galacticraft.API;

import net.minecraftforge.liquids.LiquidStack;

public interface IFuelable
{
    public int addFuel(LiquidStack liquid, int amount, boolean doDrain);

    public LiquidStack removeFuel(LiquidStack liquid, int amount);
}
