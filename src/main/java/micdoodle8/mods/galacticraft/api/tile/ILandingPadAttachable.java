package micdoodle8.mods.galacticraft.api.tile;

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
	 * @param world
	 *            World the tiles are located in
	 * @param x
	 *            Coordinate the landing pad is located at, on the x-axis
	 * @param y
	 *            Coordinate the landing pad is located at, on the y-axis
	 * @param z
	 *            Coordinate the landing pad is located at, on the z-axis
	 * @return True if the block can attach to the landing pad, false if not
	 */
	public boolean canAttachToLandingPad(IBlockAccess world, int x, int y, int z);
}
