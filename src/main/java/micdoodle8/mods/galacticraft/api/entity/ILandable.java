package micdoodle8.mods.galacticraft.api.entity;

/**
 * An entity which requires a hook into landing pad events should implement this interface
 */
public interface ILandable extends IDockable
{
	/**
	 * Called when the entity lands on a dock
	 * 
	 * @param x coordinate of the dock on the x-axis
	 * @param y coordinate of the dock on the y-axis
	 * @param z coordinate of the dock on the z-axis
	 */
	void landEntity(int x, int y, int z);
}
