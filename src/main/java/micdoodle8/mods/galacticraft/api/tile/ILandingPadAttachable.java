package micdoodle8.mods.galacticraft.api.tile;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Implement into tile entities that can attack to landing pads. It is
 * recommended that you check if the landing pad at the provided coordinates is
 * valid for connection.
 */
public interface ILandingPadAttachable
{
    /**
     * Determines if this tile can connect to the landing pad at the provided
     * coordinates.
     *
     * @param world World the tiles are located in
     * @param pos   Coordinate the landing pad is located at
     * @return True if the block can attach to the landing pad, false if not
     */
    boolean canAttachToLandingPad(IBlockAccess world, BlockPos pos);
}
