package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class FluidTankGC extends FluidTank
{
    protected TileEntity tile;

    public FluidTankGC(int capacity, TileEntity tile)
    {
        super(capacity);
        this.tile = tile;
    }

    public FluidTankGC(FluidStack stack, int capacity, TileEntity tile)
    {
        this(capacity, tile);
        setFluid(stack);
    }

    public BlockPos getTilePosition()
    {
        return this.tile.getPos();
    }

    public TileEntity getTile()
    {
        return this.tile;
    }
}
