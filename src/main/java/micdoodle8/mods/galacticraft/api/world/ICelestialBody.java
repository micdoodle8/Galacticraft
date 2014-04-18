package micdoodle8.mods.galacticraft.api.world;

import net.minecraft.world.WorldProvider;

public interface ICelestialBody
{
	/**
	 * @return name for planet, must be the same as dimension name in world
	 *         provider (if using one)
	 */
	public String getName();

	/**
	 * Determines whether or not this planet/moon is a reachable planet, or just
	 * a parent for it's moons (e.g. Saturn, Jupiter)
	 * 
	 * @return boolean value whether or not this planet/moon is a reachable
	 *         planet
	 */
	public boolean isReachable();

	/**
	 * The Map Object for this planet/moon
	 * 
	 * @return an instance of a class which implements IMapObject
	 */
	public IMapObject getMapObject();

	/**
	 * Whether or not this celestial body should be added to the list of
	 * dimensions when player leaves orbit
	 * 
	 * @return whether or not this celestial body should be added to the list of
	 *         dimensions when player leaves orbit
	 */
	public boolean addToList();

	/**
	 * Whether or not this planet/moon should be automatically registered in the
	 * forge dimension manager at the appropriate time.
	 * 
	 * @return false if you're registering dimensions on your own, true for
	 *         automatic registering
	 */
	public boolean autoRegister();

	/**
	 * Get the world provider class for this celestial body
	 * 
	 * @return the WorldProvider subclass. Must also implement
	 * @IGalacticraftWorldProvider. Return null if planet is not reachable.
	 */
	public Class<? extends WorldProvider> getWorldProvider();

	/**
	 * Get the dimension ID for this celestial body
	 * 
	 * PROTIP: Make this configurable for users since duplicates between mods
	 * aren't allowed.
	 * 
	 * @return the dimension ID to use for this planet. Return -1 if not
	 *         reachable.
	 */
	public int getDimensionID();

	/**
	 * Whether or not to override /GCKeepDim command.
	 * 
	 * @return Load this dimension at server startup and keep loaded if true.
	 */
	public boolean forceStaticLoad();
}
