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
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GalacticraftRegistry
{
	private static Map<Class<? extends WorldProvider>, ITeleportType> teleportTypeMap = new HashMap<Class<? extends WorldProvider>, ITeleportType>();
	private static List<SpaceStationType> spaceStations = new ArrayList<SpaceStationType>();
	private static List<ICelestialBody> celestialBodies = new ArrayList<ICelestialBody>();
	private static List<IGalaxy> galaxies = new ArrayList<IGalaxy>();
	private static List<INasaWorkbenchRecipe> rocketBenchT1Recipes = new ArrayList<INasaWorkbenchRecipe>();
	private static List<INasaWorkbenchRecipe> buggyBenchRecipes = new ArrayList<INasaWorkbenchRecipe>();
	private static List<INasaWorkbenchRecipe> rocketBenchT2Recipes = new ArrayList<INasaWorkbenchRecipe>();
	private static List<INasaWorkbenchRecipe> cargoRocketRecipes = new ArrayList<INasaWorkbenchRecipe>();
	private static Map<Class<? extends WorldProvider>, ResourceLocation> rocketGuiMap = new HashMap<Class<? extends WorldProvider>, ResourceLocation>();
	private static Map<Integer, List<ItemStack>> dungeonLootMap = new HashMap<Integer, List<ItemStack>>();

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
	 * Link a world provider to a gui texture. This texture will be shown on the
	 * left-side of the screen while the player is in the rocket.
	 * 
	 * @param clazz
	 *            The World Provider class
	 * @param rocketGui
	 *            Resource Location for the gui texture
	 */
	public static void registerRocketGui(Class<? extends WorldProvider> clazz, ResourceLocation rocketGui)
	{
		if (!GalacticraftRegistry.rocketGuiMap.containsKey(clazz))
		{
			GalacticraftRegistry.rocketGuiMap.put(clazz, rocketGui);
		}
	}

	/**
	 * Add loot to the list of items that can possibly spawn in dungeon chests,
	 * but it is guaranteed that one will always spawn
	 * 
	 * @param tier
	 *            Tier of dungeon chest to add loot to. For example Moon is 1
	 *            and Mars is 2
	 * @param loot
	 *            The itemstack to add to the possible list of items
	 */
	public static void addDungeonLoot(int tier, ItemStack loot)
	{
		List<ItemStack> dungeonStacks = null;

		if (GalacticraftRegistry.dungeonLootMap.containsKey(tier))
		{
			dungeonStacks = GalacticraftRegistry.dungeonLootMap.get(tier);
			dungeonStacks.add(loot);
		}
		else
		{
			dungeonStacks = new ArrayList<ItemStack>();
			dungeonStacks.add(loot);
		}

		GalacticraftRegistry.dungeonLootMap.put(tier, dungeonStacks);
	}

	public static void addT1RocketRecipe(INasaWorkbenchRecipe recipe)
	{
		GalacticraftRegistry.rocketBenchT1Recipes.add(recipe);
	}

	public static void addT2RocketRecipe(INasaWorkbenchRecipe recipe)
	{
		GalacticraftRegistry.rocketBenchT2Recipes.add(recipe);
	}

	public static void addCargoRocketRecipe(INasaWorkbenchRecipe recipe)
	{
		GalacticraftRegistry.cargoRocketRecipes.add(recipe);
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

	public static List<INasaWorkbenchRecipe> getCargoRocketRecipes()
	{
		return GalacticraftRegistry.cargoRocketRecipes;
	}

	public static List<INasaWorkbenchRecipe> getBuggyBenchRecipes()
	{
		return GalacticraftRegistry.buggyBenchRecipes;
	}

	@SideOnly(Side.CLIENT)
	public static ResourceLocation getResouceLocationForDimension(Class<? extends WorldProvider> clazz)
	{
		return GalacticraftRegistry.rocketGuiMap.get(clazz);
	}

	public static List<ItemStack> getDungeonLoot(int tier)
	{
		return GalacticraftRegistry.dungeonLootMap.get(tier);
	}
}
