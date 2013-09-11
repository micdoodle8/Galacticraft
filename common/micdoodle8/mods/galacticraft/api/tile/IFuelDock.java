package micdoodle8.mods.galacticraft.api.tile;

import java.util.HashSet;
import net.minecraft.world.IBlockAccess;

public interface IFuelDock
{
    public HashSet<ILandingPadAttachable> getConnectedTiles();
    
    public boolean isBlockAttachable(IBlockAccess world, int x, int y, int z);
}
