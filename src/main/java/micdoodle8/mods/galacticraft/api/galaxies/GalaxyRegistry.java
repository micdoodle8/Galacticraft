package micdoodle8.mods.galacticraft.api.galaxies;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.MinecraftForge;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * Credits to KingLemming and CovertJaguar, since this is based on the Liquid/Fluid API
 */
public class GalaxyRegistry 
{
    static int maxGalaxyID = 0;
    static int maxPlanetID = 0;
    static int maxMoonID = 0;
    static HashMap<String, Galaxy> galaxies = Maps.newHashMap();
    static BiMap<String, Integer> galaxyIDs = HashBiMap.create();
    static HashMap<String, Planet> planets = Maps.newHashMap();
    static BiMap<String, Integer> planetIDs = HashBiMap.create();
    static HashMap<String, Moon> moons = Maps.newHashMap();
    static BiMap<String, Integer> moonIDs = HashBiMap.create();

    public static boolean registerGalaxy(Galaxy galaxy)
    {
        if (galaxyIDs.containsKey(galaxy.getName()))
        {
            return false;
        }
        
        galaxies.put(galaxy.getName(), galaxy);
        galaxyIDs.put(galaxy.getName(), ++maxGalaxyID);

        MinecraftForge.EVENT_BUS.post(new GalaxyRegisterEvent(galaxy.getName(), maxGalaxyID));
        return true;
    }

    public static boolean registerPlanet(Planet planet)
    {
        if (planetIDs.containsKey(planet.getName()))
        {
            return false;
        }
        
        planets.put(planet.getName(), planet);
        planetIDs.put(planet.getName(), ++maxPlanetID);

        MinecraftForge.EVENT_BUS.post(new PlanetRegisterEvent(planet.getName(), maxPlanetID));
        return true;
    }

    public static boolean registerMoon(Moon moon)
    {
        if (moonIDs.containsKey(moon.getName()))
        {
            return false;
        }
        
        moons.put(moon.getName(), moon);
        moonIDs.put(moon.getName(), ++maxMoonID);

        MinecraftForge.EVENT_BUS.post(new MoonRegisterEvent(moon.getName(), maxMoonID));
        return true;
    }

    /**
     * Returns a read-only map containing Galaxy Names and their associated Galaxies.
     */
    public static Map<String, Galaxy> getRegisteredGalaxies()
    {
        return ImmutableMap.copyOf(galaxies);
    }

    /**
     * Returns a read-only map containing Galaxy Names and their associated IDs.
     */
    public static Map<String, Integer> getRegisteredGalaxyIDs()
    {
        return ImmutableMap.copyOf(galaxyIDs);
    }

    /**
     * Returns a read-only map containing Planet Names and their associated Planets.
     */
    public static Map<String, Planet> getRegisteredPlanets()
    {
        return ImmutableMap.copyOf(planets);
    }

    /**
     * Returns a read-only map containing Planet Names and their associated IDs.
     */
    public static Map<String, Integer> getRegisteredPlanetIDs()
    {
        return ImmutableMap.copyOf(planetIDs);
    }

    /**
     * Returns a read-only map containing Moon Names and their associated Moons.
     */
    public static Map<String, Moon> getRegisteredMoons()
    {
        return ImmutableMap.copyOf(moons);
    }

    /**
     * Returns a read-only map containing Moon Names and their associated IDs.
     */
    public static Map<String, Integer> getRegisteredMoonIDs()
    {
        return ImmutableMap.copyOf(moonIDs);
    }
    
    public static int getGalaxyID(String galaxyName)
    {
    	return galaxyIDs.get(galaxyName);
    }
    
    public static int getPlanetID(String planetName)
    {
    	return planetIDs.get(planetName);
    }
    
    public static int getMoonID(String moonName)
    {
    	return moonIDs.get(moonName);
    }
    
    public static class GalaxyRegisterEvent extends Event
    {
        public final String galaxyName;
        public final int galaxyID;

        public GalaxyRegisterEvent(String galaxyName, int galaxyID)
        {
            this.galaxyName = galaxyName;
            this.galaxyID = galaxyID;
        }
    }
    
    public static class PlanetRegisterEvent extends Event
    {
        public final String planetName;
        public final int planetID;

        public PlanetRegisterEvent(String planetName, int planetID)
        {
            this.planetName = planetName;
            this.planetID = planetID;
        }
    }
    
    public static class MoonRegisterEvent extends Event
    {
        public final String moonName;
        public final int moonID;

        public MoonRegisterEvent(String moonName, int moonID)
        {
            this.moonName = moonName;
            this.moonID = moonID;
        }
    }
}
