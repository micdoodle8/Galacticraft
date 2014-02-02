package micdoodle8.mods.galacticraft.api.world;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Used for objects rendered on the Galaxy Map
 */
public interface IMapObject
{
	/**
	 * Used for rendering planet's location on the map.
	 * 
	 * Value of 2.0F would result in the planet being rendered twice as large as
	 * earth.
	 * 
	 * @return Size of the planet/moon relative to earth.
	 */
	public float getPlanetSize();

	/**
	 * Used for rendering planet's location on the map.
	 * 
	 * Value of 2.0F would result in an ellipse with twice the radius of the
	 * overworld.
	 * 
	 * @return Distance from the center of the map relative to earth.
	 */
	public float getDistanceFromCenter();

	/**
	 * Used for rendering planet's location on the map.
	 * 
	 * Value of 1440.0F would result in the planet being rendered directly
	 * accross from the original position
	 * 
	 * Value of 2880.0F is a full rotation and therefore would be rendered at
	 * the same spot as the original position
	 * 
	 * @return Phase shift of planet for planet's revolution around the sun.
	 */
	public float getPhaseShift();

	/**
	 * Multiplier for length of time relative to earth that this planet takes to
	 * orbit fully.
	 * 
	 * Value of 2.0F would result in the planet rotating twice as slow (and
	 * therefore take twice as long) as the earth takes to revolve around the
	 * sun.
	 * 
	 * @return Multiple value for planet's revolution around the sun.
	 */
	public float getStretchValue();

	/**
	 * The slot renderer for this map object
	 * 
	 * @return an instance of the slot renderer for this planet/moon
	 */
	@SideOnly(Side.CLIENT)
	public ICelestialBodyRenderer getSlotRenderer();

	/**
	 * @return IGalaxy object for the parent Galaxy
	 * 
	 * @see IGalaxy
	 */
	public IGalaxy getParentGalaxy();
}
