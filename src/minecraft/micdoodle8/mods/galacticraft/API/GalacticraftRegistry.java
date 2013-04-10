package micdoodle8.mods.galacticraft.API;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.world.WorldProvider;

public class GalacticraftRegistry 
{
	private static Map<Class<? extends WorldProvider>, ITeleportType> teleportTypeMap = new HashMap<Class<? extends WorldProvider>, ITeleportType>();
	/** Galacticraft internal use only. You should be adding it using the registerSpaceStationRecipe method **/
	private static Map<Class<? extends IOrbitDimension>, SpaceStationRecipe> spaceStationRecipes = new HashMap<Class<? extends IOrbitDimension>, SpaceStationRecipe>();
	/** Galacticraft internal use only. You should be adding it using the registerSpaceStationRecipe method **/
	private static Map<Integer, SpaceStationType> spaceStations = new HashMap<Integer, SpaceStationType>();
	
	/**
	 * Register a new Teleport type for the world provider passed
	 * 
	 * @param clazz the world provider class that you wish to customize teleportation for
	 * @param type an ITeleportType-implemented class that will be used for the provided world type
	 */
	public static void registerTeleportType(Class<? extends WorldProvider> clazz, ITeleportType type)
	{
		if (!teleportTypeMap.containsKey(clazz))
		{
			teleportTypeMap.put(clazz, type);
		}
	}
	
	public static ITeleportType getTeleportTypeForDimension(Class<? extends WorldProvider> clazz)
	{
		return teleportTypeMap.get(clazz);
	}
	
	public static void registerSpaceStation(SpaceStationType type)
	{
		spaceStations.put(type.getWorldToOrbitID(), type);
	}
	
	public SpaceStationType getTypeFromPlanetID(int planetID)
	{
		return this.spaceStations.get(planetID);
	}
	
	public static Map<Integer, SpaceStationType> getSpaceStationData()
	{
		return spaceStations;
	}

//	/**
//	 * Register recipes for space stations here (items required to create it)
//	 * 
//	 * @param clazz the world provider class that implements IOrbitDimension (space station)
//	 * @param recipe the GCCoreSpaceStationRecipe for this space station
//	 */
//	public static void registerSpaceStationRecipe(Class<? extends IOrbitDimension> clazz, SpaceStationRecipe recipe)
//	{
//		if (!spaceStationRecipes.containsKey(clazz))
//		{
//			spaceStationRecipes.put(clazz, recipe);
//		}
//	}
//	
//	public static SpaceStationRecipe getRecipeFromSpaceStation(Class<? extends IOrbitDimension> clazz)
//	{
//		return spaceStationRecipes.get(clazz);
//	}
}
