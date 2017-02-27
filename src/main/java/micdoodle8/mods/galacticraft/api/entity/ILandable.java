package micdoodle8.mods.galacticraft.api.entity;

import net.minecraft.util.math.BlockPos;

/**
 * An entity which requires a hook into landing pad events should implement this interface
 */
public interface ILandable extends IDockable
{
	/**
	 * Called when the entity lands on a dock
	 * 
	 * @param pos coordinates of the dock
	 */
	void landEntity(BlockPos pos);
}
