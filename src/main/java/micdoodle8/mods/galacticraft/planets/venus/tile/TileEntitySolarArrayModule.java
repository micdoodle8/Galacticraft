package micdoodle8.mods.galacticraft.planets.venus.tile;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileEntitySolarArrayModule extends TileEntitySolarTransmitter
{
    public TileEntitySolarArrayModule()
    {
        super("container.solar_array_module.name");
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[0];
    }

    @Override
    public boolean canConnect(EnumFacing direction, NetworkType type)
    {
        return type == NetworkType.SOLAR_MODULE && direction.getAxis() != EnumFacing.Axis.Y;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return false;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
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
