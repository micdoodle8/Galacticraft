package micdoodle8.mods.galacticraft.API;

/**
 *
 * @author micdoodle8
 *
 *	Implement this method for any entity that will travel from planet to planet
 *
 */
public interface IInterplanetaryObject
{
	/**
	 * Gets the Y-Coordinate to teleport this entity from (on the original planet)
	 */
	public int getYCoordToTeleportFrom();

	/**
	 * Gets the Y-Coordinate to teleport this entity to (on the new planet)
	 */
	public int getYCoordToTeleportTo();

	/**
	 * Gets the dimension to teleport this entity to
	 */
	public int getDimensionForTeleport();

	/**
	 * This method is called after entity has been teleported
	 */
	public void onPlanetChanged();
}
