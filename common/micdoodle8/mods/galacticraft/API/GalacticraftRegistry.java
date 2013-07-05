package micdoodle8.mods.galacticraft.API;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.WorldProvider;

public class GalacticraftRegistry
{
    private static Map<Class<? extends WorldProvider>, ITeleportType> teleportTypeMap = new HashMap<Class<? extends WorldProvider>, ITeleportType>();
    private static Map<Class<? extends IOrbitDimension>, SpaceStationRecipe> spaceStationRecipes = new HashMap<Class<? extends IOrbitDimension>, SpaceStationRecipe>();
    private static Map<Integer, SpaceStationType> spaceStations = new HashMap<Integer, SpaceStationType>();
    private static ArrayList<ICelestialBody> celestialBodies = new ArrayList<ICelestialBody>();
    private static ArrayList<IGalaxy> galaxies = new ArrayList<IGalaxy>();

    /**
     * Register a new Teleport type for the world provider passed
     * 
     * @param clazz
     *            the world provider class that you wish to customize
     *            teleportation for
     * @param type
     *            an ITeleportType-implemented class that will be used for the
     *            provided world type
     */
    public static void registerTeleportType(Class<? extends WorldProvider> clazz, ITeleportType type)
    {
        if (!GalacticraftRegistry.teleportTypeMap.containsKey(clazz))
        {
            GalacticraftRegistry.teleportTypeMap.put(clazz, type);
        }
    }

    /**
     * Register a Galacticraft Body. See @IPlanet and @IMoon
     * 
     * @param celestial
     *            body object. Must implement IPlanet or IMoon to function as
     *            intended
     */
    public static void registerCelestialBody(ICelestialBody body)
    {
        if (!GalacticraftRegistry.celestialBodies.contains(body))
        {
            GalacticraftRegistry.celestialBodies.add(body);
        }
    }

    /**
     * Register a new Galaxy. This is not fully implemented yet.
     * 
     * @param celestial
     *            body object. Must implement IPlanet or IMoon to function as
     *            intended
     */
    public static void registerGalaxy(IGalaxy galaxy)
    {
        if (!GalacticraftRegistry.galaxies.contains(galaxy))
        {
            GalacticraftRegistry.galaxies.add(galaxy);
        }
    }

    public static ITeleportType getTeleportTypeForDimension(Class<? extends WorldProvider> clazz)
    {
        return GalacticraftRegistry.teleportTypeMap.get(clazz);
    }

    public static void registerSpaceStation(SpaceStationType type)
    {
        GalacticraftRegistry.spaceStations.put(type.getWorldToOrbitID(), type);
    }

    public SpaceStationType getTypeFromPlanetID(int planetID)
    {
        return GalacticraftRegistry.spaceStations.get(planetID);
    }

    public static Map<Integer, SpaceStationType> getSpaceStationData()
    {
        return GalacticraftRegistry.spaceStations;
    }

    public static ArrayList<ICelestialBody> getCelestialBodies()
    {
        return GalacticraftRegistry.celestialBodies;
    }

    public static ArrayList<IGalaxy> getGalaxyList()
    {
        return GalacticraftRegistry.galaxies;
    }
}
