package micdoodle8.mods.galacticraft.planets.venus.tile;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlockNames;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntitySolarArrayModule extends TileEntitySolarTransmitter
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + VenusBlockNames.solarArrayModule)
    public static TileEntityType<TileEntitySolarArrayModule> TYPE;

    public TileEntitySolarArrayModule()
    {
        super(TYPE);
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
    public CompoundNBT write(CompoundNBT compound)
    {
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound)
    {
        super.read(compound);
    }

    @Override
    public boolean canTransmit()
    {
        return true;
    }

    @Override
    protected boolean handleInventory()
    {
        return false;
    }
}
