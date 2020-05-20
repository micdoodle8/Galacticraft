package micdoodle8.mods.galacticraft.planets.venus.tile;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;

public class TileEntitySolarArrayModule extends TileEntitySolarTransmitter
{
    public TileEntitySolarArrayModule()
    {
        super("container.solar_array_module.name");
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
    {
        return type == NetworkType.SOLAR_MODULE && direction.getAxis() != Direction.Axis.Y;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return false;
    }

    @Override
    public CompoundNBT writeToNBT(CompoundNBT compound)
    {
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(CompoundNBT compound)
    {
        super.readFromNBT(compound);
    }

    @Override
    public boolean canTransmit()
    {
        return true;
    }

    protected boolean handleInventory()
    {
        return false;
    }
}
