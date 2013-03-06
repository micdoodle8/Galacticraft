package micdoodle8.mods.galacticraft.API;

public interface IGalacticraftSubMod
{
	/**
	 * @return Dimension name, must be the same as all other getDimensionName methods in your mod
	 */
	public String getDimensionName();
	
	/**
	 * Determines whether or not this planet/moon is a reachable planet, or just a parent for it's moons (e.g. Saturn, Jupiter)
	 * 
	 * @return boolean value whether or not this planet/moon is a reachable planet
	 */
	public boolean reachableDestination();
	
	/**
	 * @return IGalaxy object for the parent Galaxy
	 * 
	 * @see IGalaxy
	 */
	public IGalaxy getParentGalaxy();
}
