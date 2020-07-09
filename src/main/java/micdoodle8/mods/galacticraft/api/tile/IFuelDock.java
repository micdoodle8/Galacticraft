package micdoodle8.mods.galacticraft.api.tile;

import micdoodle8.mods.galacticraft.api.entity.IDockable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

import java.util.HashSet;

public interface IFuelDock
{
    HashSet<ILandingPadAttachable> getConnectedTiles();

    boolean isBlockAttachable(IWorldReader world, BlockPos pos);

    IDockable getDockedEntity();

    void dockEntity(IDockable entity);
}
