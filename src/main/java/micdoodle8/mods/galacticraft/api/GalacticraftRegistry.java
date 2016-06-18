package micdoodle8.mods.galacticraft.api;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.client.IGameScreen;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.api.world.SpaceStationType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalacticraftRegistry
{
    private static Map<Class<? extends WorldProvider>, ITeleportType> teleportTypeMap = new HashMap<Class<? extends WorldProvider>, ITeleportType>();
    private static List<SpaceStationType> spaceStations = new ArrayList<SpaceStationType>();
    private static List<INasaWorkbenchRecipe> rocketBenchT1Recipes = new ArrayList<INasaWorkbenchRecipe>();
    private static List<INasaWorkbenchRecipe> buggyBenchRecipes = new ArrayList<INasaWorkbenchRecipe>();
    private static List<INasaWorkbenchRecipe> rocketBenchT2Recipes = new ArrayList<INasaWorkbenchRecipe>();
    private static List<INasaWorkbenchRecipe> cargoRocketRecipes = new ArrayList<INasaWorkbenchRecipe>();
    private static List<INasaWorkbenchRecipe> rocketBenchT3Recipes = new ArrayList<INasaWorkbenchRecipe>();
    private static List<INasaWorkbenchRecipe> astroMinerRecipes = new ArrayList<INasaWorkbenchRecipe>();
    private static Map<Class<? extends WorldProvider>, ResourceLocation> rocketGuiMap = new HashMap<Class<? extends WorldProvider>, ResourceLocation>();
    private static Map<Integer, List<ItemStack>> dungeonLootMap = new HashMap<Integer, List<ItemStack>>();
    private static List<Integer> worldProviderIDs = new ArrayList<Integer>();
    private static List<IGameScreen> gameScreens = new ArrayList<IGameScreen>();
    private static int maxScreenTypes;

    /**
     * Register a new Teleport type for the world provider passed
     *
     * @param clazz the world provider class that you wish to customize
     *              teleportation for
     * @param type  an ITeleportType-implemented class that will be used for the
     *              provided world type
     */
    public static void registerTeleportType(Class<? extends WorldProvider> clazz, ITeleportType type)
    {
        if (!GalacticraftRegistry.teleportTypeMap.containsKey(clazz))
        {
            GalacticraftRegistry.teleportTypeMap.put(clazz, type);
        }
    }

    /**
     * Link a world provider to a gui texture. This texture will be shown on the
     * left-side of the screen while the player is in the rocket.
     *
     * @param clazz     The World Provider class
     * @param rocketGui Resource Location for the gui texture
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
     * @param tier Tier of dungeon chest to add loot to. For example Moon is 1
     *             and Mars is 2
     * @param loot The itemstack to add to the possible list of items
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

    public static void addT3RocketRecipe(INasaWorkbenchRecipe recipe)
    {
        GalacticraftRegistry.rocketBenchT3Recipes.add(recipe);
    }

    public static void addCargoRocketRecipe(INasaWorkbenchRecipe recipe)
    {
        GalacticraftRegistry.cargoRocketRecipes.add(recipe);
    }

    public static void addMoonBuggyRecipe(INasaWorkbenchRecipe recipe)
    {
        GalacticraftRegistry.buggyBenchRecipes.add(recipe);
    }

    public static void addAstroMinerRecipe(INasaWorkbenchRecipe recipe)
    {
        GalacticraftRegistry.astroMinerRecipes.add(recipe);
    }

    public static ITeleportType getTeleportTypeForDimension(Class<? extends WorldProvider> clazz)
    {
        if (!IGalacticraftWorldProvider.class.isAssignableFrom(clazz))
        {
            clazz = WorldProviderSurface.class;
        }
        return GalacticraftRegistry.teleportTypeMap.get(clazz);
    }

    public static void registerSpaceStation(SpaceStationType type)
    {
        for (SpaceStationType type1 : GalacticraftRegistry.spaceStations)
        {
            if (type1.getWorldToOrbitID() == type.getWorldToOrbitID())
            {
                throw new RuntimeException("Two space station types registered with the same home planet ID: " + type.getWorldToOrbitID());
            }
        }

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

    public static List<INasaWorkbenchRecipe> getRocketT1Recipes()
    {
        return GalacticraftRegistry.rocketBenchT1Recipes;
    }

    public static List<INasaWorkbenchRecipe> getRocketT2Recipes()
    {
        return GalacticraftRegistry.rocketBenchT2Recipes;
    }

    public static List<INasaWorkbenchRecipe> getRocketT3Recipes()
    {
        return GalacticraftRegistry.rocketBenchT3Recipes;
    }

    public static List<INasaWorkbenchRecipe> getCargoRocketRecipes()
    {
        return GalacticraftRegistry.cargoRocketRecipes;
    }

    public static List<INasaWorkbenchRecipe> getBuggyBenchRecipes()
    {
        return GalacticraftRegistry.buggyBenchRecipes;
    }

	public static List<INasaWorkbenchRecipe> getAstroMinerRecipes()
	{
        return GalacticraftRegistry.astroMinerRecipes;
	}   

	@SideOnly(Side.CLIENT)
    public static ResourceLocation getResouceLocationForDimension(Class<? extends WorldProvider> clazz)
    {
        if (!IGalacticraftWorldProvider.class.isAssignableFrom(clazz))
        {
            clazz = WorldProviderSurface.class;
        }
        return GalacticraftRegistry.rocketGuiMap.get(clazz);
    }

    public static List<ItemStack> getDungeonLoot(int tier)
    {
        return GalacticraftRegistry.dungeonLootMap.get(tier);
    }
    
    /***
     * Now returns a boolean to indicate whether registration of the WorldProvider type was successful.
     * (If it failed, you should probably set the CelestialBody as unreachable.)
     * 
     * @param id
     * @param provider
     * @param keepLoaded
     * @return <boolean> success
     */
    public static boolean registerProvider(int id, Class<? extends WorldProvider> provider, boolean keepLoaded, int defaultID)
    {
    	boolean flag = DimensionManager.registerProviderType(id, provider, keepLoaded);
    	if (flag)
    	{
    		GalacticraftRegistry.worldProviderIDs.add(id);
    		return true;
    	}
    	else
    	{
    		GalacticraftRegistry.worldProviderIDs.add(defaultID);  //Adding the 0 here preserves the order, important for network compatibility between GC versions
    		FMLLog.severe("Could not register dimension " + id + " - does it clash with another mod?  Change the ID in config.");
    		return false;
    	}
    }

    /**
     * You should now use GalacticraftRegistry.registerProvider(int id, Class<? extends WorldProvider> provider, boolean keepLoaded, int defaultID)
     * which returns a boolean indicating if the Provider was registered OK.
     * 
     * @param id
     * @param provider
     * @param keepLoaded
     */
    @Deprecated
    public static void registerProvider(int id, Class<? extends WorldProvider> provider, boolean keepLoaded)
    {
    	GalacticraftRegistry.registerProvider(id, provider, keepLoaded, 0);
    }
    
    public static int getProviderID(int index)
    {
    	return GalacticraftRegistry.worldProviderIDs.get(index);
    }
    
    /**
     * Register an IGameScreen so the Display Screen can access it
     * 
     * @param screen  The IGameScreen to be registered
     * @return   The type ID assigned to this screen type
     */
    public static int registerScreen(IGameScreen screen)
    {
    	GalacticraftRegistry.gameScreens.add(screen);
        maxScreenTypes++;
    	screen.setFrameSize(0.098F);
    	return maxScreenTypes - 1;
    }

    public static int getMaxScreenTypes() {
        return maxScreenTypes;
    }

    public static IGameScreen getGameScreen(int type)
    {
    	return GalacticraftRegistry.gameScreens.get(type);
    }
}
