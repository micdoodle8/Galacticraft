package micdoodle8.mods.galacticraft.api.galaxies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.common.MinecraftForge;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * Credits to KingLemming and CovertJaguar, since this is based on the
 * Liquid/Fluid API
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
	static HashMap<Planet, List<Moon>> moonList = Maps.newHashMap();
	
	public static CelestialBody getCelestialBodyFromDimensionID(int dimensionID)
	{
		for (Planet planet : planets.values())
		{
			if (planet.getDimensionID() == dimensionID)
			{
				return planet;
			}
		}
		
		for (Moon moon : moons.values())
		{
			if (moon.getDimensionID() == dimensionID)
			{
				return moon;
			}
		}
		
		return null;
	}
	
	public static void refreshGalaxies()
	{
		moonList.clear();
		
		for (Moon moon : getRegisteredMoons().values())
		{
			Planet planet = moon.getParentPlanet();
			List<Moon> listOfMoons = moonList.get(planet);
			if (listOfMoons == null)
			{
				listOfMoons = new ArrayList<Moon>();
			}
			listOfMoons.add(moon);
			moonList.put(planet, listOfMoons);
		}
	}
	
	public static List<Moon> getMoonsForPlanet(Planet planet)
	{
		List<Moon> moonListLocal = moonList.get(planet);
		
		if (moonListLocal == null)
		{
			return new ArrayList();
		}
		
		return ImmutableList.copyOf(moonListLocal);
	}
	
	public static CelestialBody getCelestialBodyFromUnlocalizedName(String unlocalizedName)
	{
		for (Planet planet : planets.values())
		{
			if (planet.getUnlocalizedName().equals(unlocalizedName))
			{
				return planet;
			}
		}
		
		for (Moon moon : moons.values())
		{
			if (moon.getUnlocalizedName().equals(unlocalizedName))
			{
				return moon;
			}
		}
		
		return null;
	}

	public static boolean registerGalaxy(Galaxy galaxy)
	{
		if (GalaxyRegistry.galaxyIDs.containsKey(galaxy.getName()))
		{
			return false;
		}

		GalaxyRegistry.galaxies.put(galaxy.getName(), galaxy);
		GalaxyRegistry.galaxyIDs.put(galaxy.getName(), ++GalaxyRegistry.maxGalaxyID);

		MinecraftForge.EVENT_BUS.post(new GalaxyRegisterEvent(galaxy.getName(), GalaxyRegistry.maxGalaxyID));
		return true;
	}

	public static boolean registerPlanet(Planet planet)
	{
		if (GalaxyRegistry.planetIDs.containsKey(planet.getName()))
		{
			return false;
		}

		GalaxyRegistry.planets.put(planet.getName(), planet);
		GalaxyRegistry.planetIDs.put(planet.getName(), ++GalaxyRegistry.maxPlanetID);

		MinecraftForge.EVENT_BUS.post(new PlanetRegisterEvent(planet.getName(), GalaxyRegistry.maxPlanetID));
		return true;
	}

	public static boolean registerMoon(Moon moon)
	{
		if (GalaxyRegistry.moonIDs.containsKey(moon.getName()))
		{
			return false;
		}

		GalaxyRegistry.moons.put(moon.getName(), moon);
		GalaxyRegistry.moonIDs.put(moon.getName(), ++GalaxyRegistry.maxMoonID);

		MinecraftForge.EVENT_BUS.post(new MoonRegisterEvent(moon.getName(), GalaxyRegistry.maxMoonID));
		return true;
	}

	/**
	 * Returns a read-only map containing Galaxy Names and their associated
	 * Galaxies.
	 */
	public static Map<String, Galaxy> getRegisteredGalaxies()
	{
		return ImmutableMap.copyOf(GalaxyRegistry.galaxies);
	}

	/**
	 * Returns a read-only map containing Galaxy Names and their associated IDs.
	 */
	public static Map<String, Integer> getRegisteredGalaxyIDs()
	{
		return ImmutableMap.copyOf(GalaxyRegistry.galaxyIDs);
	}

	/**
	 * Returns a read-only map containing Planet Names and their associated
	 * Planets.
	 */
	public static Map<String, Planet> getRegisteredPlanets()
	{
		return ImmutableMap.copyOf(GalaxyRegistry.planets);
	}

	/**
	 * Returns a read-only map containing Planet Names and their associated IDs.
	 */
	public static Map<String, Integer> getRegisteredPlanetIDs()
	{
		return ImmutableMap.copyOf(GalaxyRegistry.planetIDs);
	}

	/**
	 * Returns a read-only map containing Moon Names and their associated Moons.
	 */
	public static Map<String, Moon> getRegisteredMoons()
	{
		return ImmutableMap.copyOf(GalaxyRegistry.moons);
	}

	/**
	 * Returns a read-only map containing Moon Names and their associated IDs.
	 */
	public static Map<String, Integer> getRegisteredMoonIDs()
	{
		return ImmutableMap.copyOf(GalaxyRegistry.moonIDs);
	}

	public static int getGalaxyID(String galaxyName)
	{
		return GalaxyRegistry.galaxyIDs.get(galaxyName);
	}

	public static int getPlanetID(String planetName)
	{
		return GalaxyRegistry.planetIDs.get(planetName);
	}

	public static int getMoonID(String moonName)
	{
		return GalaxyRegistry.moonIDs.get(moonName);
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
