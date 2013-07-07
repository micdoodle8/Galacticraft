package micdoodle8.mods.galacticraft.api.tile;

import java.util.HashSet;
import net.minecraft.tileentity.TileEntity;

public interface IFuelDock
{
    public HashSet<TileEntity> getConnectedTiles();
}
