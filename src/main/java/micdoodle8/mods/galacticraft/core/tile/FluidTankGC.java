package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidTank;

public class FluidTankGC extends FluidTank
{
    public FluidTankGC(int capacity, TileEntity tile)
    {
        super(capacity);
        this.tile = tile;
    }
}
