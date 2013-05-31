package micdoodle8.mods.galacticraft.core;

import cpw.mods.fml.common.Loader;

public class GCCoreCompatibilityManager
{
    private static boolean modIc2Loaded = false;

    private static boolean modBCraftLoaded = false;

    private static boolean modGTLoaded = false;

    private static boolean modTELoaded = false;

    public static void checkForCompatibleMods()
    {
        if (Loader.isModLoaded("GregTech_Addon"))
        {
            GCCoreCompatibilityManager.modGTLoaded = true;
        }

        if (Loader.isModLoaded("ThermalExpansion"))
        {
            GCCoreCompatibilityManager.modTELoaded = true;
        }

        if (Loader.isModLoaded("IC2"))
        {
            GCCoreCompatibilityManager.modIc2Loaded = true;
        }

        if (Loader.isModLoaded("BuildCraft|Core"))
        {
            GCCoreCompatibilityManager.modBCraftLoaded = true;
        }
    }

    public static boolean isIc2Loaded()
    {
        return GCCoreCompatibilityManager.modIc2Loaded;
    }

    public static boolean isBCraftLoaded()
    {
        return GCCoreCompatibilityManager.modBCraftLoaded;
    }

    public static boolean isTELoaded()
    {
        return GCCoreCompatibilityManager.modTELoaded;
    }

    public static boolean isGTLoaded()
    {
        return GCCoreCompatibilityManager.modGTLoaded;
    }
}
