package micdoodle8.mods.galacticraft.api.world;

import micdoodle8.mods.galacticraft.api.vector.Vector3;

/**
 * Implement this one per set of planets (assuming only one galaxy in your mod)
 */
public interface IGalaxy
{
	/**
	 * @return Galaxy name, will be displayed above galaxy on map
	 */
	public String getGalaxyName();

	/**
	 * @return X-Coordinate from center of map.
	 */
	public int getXCoord();

	/**
	 * @return Y-Coordinate from center of map.
	 */
	public int getYCoord();

	/**
	 * The color will appear on the map, as the galaxy's orbital ring color
	 * 
	 * x - red value y - green value z - blue value
	 * 
	 * @return
	 */
	public Vector3 getRGBRingColors();
}
