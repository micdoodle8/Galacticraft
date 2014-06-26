package micdoodle8.mods.galacticraft.core.util;

import cpw.mods.fml.common.Loader;

public class CompatibilityManager
{
	private static boolean modIc2Loaded;
	private static boolean modBCraftLoaded;
	private static boolean modGTLoaded;
	private static boolean modTELoaded;
	private static boolean modAetherIILoaded;
	private static boolean modBasicComponentsLoaded;
	private static boolean modAppEngLoaded;

	public static void checkForCompatibleMods()
	{
		if (Loader.isModLoaded("GregTech_Addon"))
		{
			CompatibilityManager.modGTLoaded = true;
		}

		if (Loader.isModLoaded("ThermalExpansion"))
		{
			CompatibilityManager.modTELoaded = true;
		}

		if (Loader.isModLoaded("IC2"))
		{
			CompatibilityManager.modIc2Loaded = true;
		}

		if (Loader.isModLoaded("BuildCraft|Core"))
		{
			CompatibilityManager.modBCraftLoaded = true;
		}

		if (Loader.isModLoaded("Aether II"))
		{
			CompatibilityManager.modAetherIILoaded = true;
		}

		if (Loader.isModLoaded("BasicComponents"))
		{
			CompatibilityManager.modBasicComponentsLoaded = true;
		}

		if (Loader.isModLoaded("AppliedEnergistics"))
		{
			CompatibilityManager.modAppEngLoaded = true;
		}
	}

	public static boolean isIc2Loaded()
	{
		return CompatibilityManager.modIc2Loaded;
	}

	public static boolean isBCraftLoaded()
	{
		return CompatibilityManager.modBCraftLoaded;
	}

	public static boolean isTELoaded()
	{
		return CompatibilityManager.modTELoaded;
	}

	public static boolean isGTLoaded()
	{
		return CompatibilityManager.modGTLoaded;
	}

	public static boolean isAIILoaded()
	{
		return CompatibilityManager.modAetherIILoaded;
	}

	public static boolean isBCLoaded()
	{
		return CompatibilityManager.modBasicComponentsLoaded;
	}

	public static boolean isAppEngLoaded()
	{
		return CompatibilityManager.modAppEngLoaded;
	}
}
