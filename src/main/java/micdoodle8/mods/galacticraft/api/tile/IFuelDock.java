package micdoodle8.mods.galacticraft.api.tile;

import micdoodle8.mods.galacticraft.api.entity.IDockable;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.HashSet;

public interface IFuelDock
{
    public HashSet<ILandingPadAttachable> getConnectedTiles();

    public boolean isBlockAttachable(IBlockAccess world, BlockPos pos);

    public IDockable getDockedEntity();

	void dockEntity(IDockable entity);
}
