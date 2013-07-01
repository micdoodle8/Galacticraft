package micdoodle8.mods.galacticraft.API;

public interface ICelestialBody
{
    /**
     * @return name for planet, must be the same as dimension name in world provider (if using one)
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
}
