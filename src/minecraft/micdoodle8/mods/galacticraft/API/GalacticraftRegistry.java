package micdoodle8.mods.galacticraft.API;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.WorldProvider;

public class GalacticraftRegistry 
{
	private static Map<Class<? extends WorldProvider>, ITeleportType> teleportTypeMap = new HashMap<Class<? extends WorldProvider>, ITeleportType>();
	
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
}
