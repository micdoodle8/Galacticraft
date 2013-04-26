package micdoodle8.mods.galacticraft.core;

import universalelectricity.components.common.BasicComponents;
import cpw.mods.fml.common.Loader;

public class GCCoreCompatibilityManager 
{
	private static boolean modIc2Loaded = false;

	private static boolean modBCraftLoaded = false;

	private static boolean modBCompLoaded = false;

	private static boolean modGTLoaded = false;

	private static boolean modTELoaded = false;
	
	public static void checkForCompatibleMods()
	{
		if (Loader.isModLoaded("GregTech_Addon"))
		{
			modGTLoaded = true;
		}

		if (Loader.isModLoaded("ThermalExpansion"))
		{
			modTELoaded = true;
		}
		
		if (Loader.isModLoaded("IC2"))
		{ 
			modIc2Loaded = true;
		}

		if (Loader.isModLoaded("BuildCraft|Core"))
		{ 
			modBCraftLoaded = true;
		}
		
		if (BasicComponents.INITIALIZED)
		{ 
			modBCompLoaded = true;
		}
	}
	
	public static boolean isIc2Loaded()
	{
		return modIc2Loaded;
	}
	
	public static boolean isBCraftLoaded()
	{
		return modBCraftLoaded;
	}
	
	public static boolean isBCompLoaded()
	{
		return modBCompLoaded;
	}
	
	public static boolean isTELoaded()
	{
		return modTELoaded;
	}
	
	public static boolean isGTLoaded()
	{
		return modGTLoaded;
	}
}
