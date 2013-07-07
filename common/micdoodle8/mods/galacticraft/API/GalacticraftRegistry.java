package micdoodle8.mods.galacticraft.API;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import micdoodle8.mods.galacticraft.API.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.API.world.ICelestialBody;
import micdoodle8.mods.galacticraft.API.world.IGalaxy;
import micdoodle8.mods.galacticraft.API.world.ITeleportType;
import micdoodle8.mods.galacticraft.API.world.SpaceStationType;
import net.minecraft.world.WorldProvider;

public class GalacticraftRegistry
{
    private static Map<Class<? extends WorldProvider>, ITeleportType> teleportTypeMap = new HashMap<Class<? extends WorldProvider>, ITeleportType>();
    private static Map<Integer, SpaceStationType> spaceStations = new HashMap<Integer, SpaceStationType>();
    private static List<ICelestialBody> celestialBodies = new ArrayList<ICelestialBody>();
    private static List<IGalaxy> galaxies = new ArrayList<IGalaxy>();
    private static List<INasaWorkbenchRecipe> rocketBenchRecipes = new ArrayList<INasaWorkbenchRecipe>();
    private static List<INasaWorkbenchRecipe> buggyBenchRecipes = new ArrayList<INasaWorkbenchRecipe>();

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
     * @param body
     *            celestial body object. Must implement IPlanet or IMoon to
     *            function as intended
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
     * @param galaxy
     *            the Galaxy object to add to the list of all galaxies
     */
    public static void registerGalaxy(IGalaxy galaxy)
    {
        if (!GalacticraftRegistry.galaxies.contains(galaxy))
        {
            GalacticraftRegistry.galaxies.add(galaxy);
        }
    }

    public static void addT1RocketRecipe(INasaWorkbenchRecipe recipe)
    {
        GalacticraftRegistry.rocketBenchRecipes.add(recipe);
    }

    public static void addMoonBuggyRecipe(INasaWorkbenchRecipe recipe)
    {
        GalacticraftRegistry.buggyBenchRecipes.add(recipe);
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

    public static List<ICelestialBody> getCelestialBodies()
    {
        return GalacticraftRegistry.celestialBodies;
    }

    public static List<IGalaxy> getGalaxyList()
    {
        return GalacticraftRegistry.galaxies;
    }

    public static List<INasaWorkbenchRecipe> getRocketT1Recipes()
    {
        return GalacticraftRegistry.rocketBenchRecipes;
    }

    public static List<INasaWorkbenchRecipe> getBuggyBenchRecipes()
    {
        return GalacticraftRegistry.buggyBenchRecipes;
    }
}
