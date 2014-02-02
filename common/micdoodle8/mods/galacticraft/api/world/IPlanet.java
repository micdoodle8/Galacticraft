package micdoodle8.mods.galacticraft.api.world;

/**
 * Used for determining basic qualities of a planet.
 * 
 * Separate from world providers since you can have non-reachable planet types
 * that have no world provider
 * 
 */
public interface IPlanet extends ICelestialBody
{
	/**
	 * @return IGalaxy object for the galaxy this planet resides in
	 * 
	 * @see IGalaxy
	 */
	public IGalaxy getParentGalaxy();
}
