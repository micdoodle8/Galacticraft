package micdoodle8.mods.galacticraft.API;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
    
    /**
     * The Map Object for this planet/moon
     * 
     * @return an instance of a class which implements IMapObject
     */
    public IMapObject getMapObject();
    
    /**
     * Whether or not this celestial body should be added to the list of dimensions when player leaves orbit
     * 
     * @return whether or not this celestial body should be added to the list of dimensions when player leaves orbit
     */
    public boolean addToList();
}
