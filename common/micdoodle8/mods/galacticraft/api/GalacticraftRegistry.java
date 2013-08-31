package micdoodle8.mods.galacticraft.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.api.world.ICelestialBody;
import micdoodle8.mods.galacticraft.api.world.IGalaxy;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.api.world.SpaceStationType;
import net.minecraft.world.WorldProvider;
import net.minecraft.item.ItemStack;

public class GalacticraftRegistry
{
    private static Map<Class<? extends WorldProvider>, ITeleportType> teleportTypeMap = new HashMap<Class<? extends WorldProvider>, ITeleportType>();
    private static List<SpaceStationType> spaceStations = new ArrayList<SpaceStationType>();
    private static List<ICelestialBody> celestialBodies = new ArrayList<ICelestialBody>();
    private static List<IGalaxy> galaxies = new ArrayList<IGalaxy>();
    private static List<INasaWorkbenchRecipe> rocketBenchT1Recipes = new ArrayList<INasaWorkbenchRecipe>();
    private static List<INasaWorkbenchRecipe> buggyBenchRecipes = new ArrayList<INasaWorkbenchRecipe>();
    private static List<INasaWorkbenchRecipe> rocketBenchT2Recipes = new ArrayList<INasaWorkbenchRecipe>();
	private static List<ItemStack> t1TreasureChestItems = new ArrayList<ItemStack>();

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

	/**
	* Add an itemstack to spawn in the T1 Treasure Chest
	*
	* @param item
	*            the itemstack to add to the spawnable list
	*/
	public static void addT1TreasureChestItem(Itemstack item)
	{
		GalacticraftRegistry.t1TreasureChestItems.add(item);
	}

    public static void addT1RocketRecipe(INasaWorkbenchRecipe recipe)
    {
        GalacticraftRegistry.rocketBenchT1Recipes.add(recipe);
    }

    public static void addT2RocketRecipe(INasaWorkbenchRecipe recipe)
    {
        GalacticraftRegistry.rocketBenchT2Recipes.add(recipe);
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
        GalacticraftRegistry.spaceStations.add(type);
    }

    public SpaceStationType getTypeFromPlanetID(int planetID)
    {
        return GalacticraftRegistry.spaceStations.get(planetID);
    }

    public static List<SpaceStationType> getSpaceStationData()
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
        return GalacticraftRegistry.rocketBenchT1Recipes;
    }

    public static List<INasaWorkbenchRecipe> getRocketT2Recipes()
    {
        return GalacticraftRegistry.rocketBenchT2Recipes;
    }

    public static List<INasaWorkbenchRecipe> getBuggyBenchRecipes()
    {
        return GalacticraftRegistry.buggyBenchRecipes;
    }

	public static List<ItemStack> getT1TreasureItems()
	{
		return GalacticraftRegistry.t1TreasureChestItems;
	}
}
