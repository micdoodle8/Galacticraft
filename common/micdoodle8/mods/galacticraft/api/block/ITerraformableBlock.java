package micdoodle8.mods.galacticraft.api.block;

import net.minecraft.world.World;

public interface ITerraformableBlock
{
	/**
	 * Determines if the block as this position is terraformable. This will only
	 * be called for blocks inside the terraformer bubble. It is recommended
	 * that you test whether the block can see the surface before returning
	 * true.
	 * 
	 * @param world
	 *            World that the block is a part of
	 * @param x
	 *            Position of the block on the x-axis
	 * @param y
	 *            Position of the block on the y-axis
	 * @param z
	 *            Position of the block on the z-axis
	 * @return True if the block can be terraformed, false if not.
	 */
	public boolean isTerraformable(World world, int x, int y, int z);
}
