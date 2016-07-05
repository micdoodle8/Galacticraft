package micdoodle8.mods.galacticraft.core.util;

import cpw.mods.fml.common.Loader;

import java.lang.reflect.Method;

import micdoodle8.mods.galacticraft.core.blocks.BlockEnclosed;

public class CompatibilityManager
{
    private static boolean modIc2Loaded;
    private static boolean modBCraftLoaded;
    private static boolean modGTLoaded;
    private static boolean modTELoaded;
    private static boolean modAetherIILoaded;
    private static boolean modBasicComponentsLoaded;
    private static boolean modAppEngLoaded;
    private static boolean modPneumaticCraftLoaded;
    public static Class<?> classBCBlockGenericPipe = null;
    public static Class<?> classGTOre = null;
    public static Method methodBCBlockPipe_getPipe = null;

    public static void checkForCompatibleMods()
    {
        if (Loader.isModLoaded("gregtech") || Loader.isModLoaded("GregTech_Addon") || Loader.isModLoaded("GregTech") )
        {
            CompatibilityManager.modGTLoaded = true;
            try {
            	Class<?> clazz = Class.forName("gregtech.common.blocks.GT_Block_Ores");
            	if (clazz != null)
            	{
            		classGTOre = clazz;
            	}
            }
            catch (Exception e) { e.printStackTrace(); }
        }

        if (Loader.isModLoaded("ThermalExpansion"))
        {
            CompatibilityManager.modTELoaded = true;
        }

        if (Loader.isModLoaded("IC2"))
        {
            CompatibilityManager.modIc2Loaded = true;
            
            try {
            	Class<?> clazz = Class.forName("ic2.core.block.wiring.TileEntityCable");
            	if (clazz != null)
            	{
            		BlockEnclosed.onBlockNeighbourChangeIC2 = clazz.getMethod("onNeighborBlockChange");
            	}
            }
            catch (Exception e) { e.printStackTrace(); }

        }

        if (Loader.isModLoaded("BuildCraft|Core"))
        {
            CompatibilityManager.modBCraftLoaded = true;

            try
            {
            	classBCBlockGenericPipe = Class.forName("buildcraft.transport.BlockGenericPipe");

                for (Method m : classBCBlockGenericPipe.getDeclaredMethods())
                {
                    if (m.getName().equals("getPipe"))
                    {
                        CompatibilityManager.methodBCBlockPipe_getPipe = m;
                        break;
                    }
                }
            }
            catch (Exception e) { e.printStackTrace(); }
            
            BlockEnclosed.initialiseBC();

            if (CompatibilityManager.methodBCBlockPipe_getPipe == null)
            {
                CompatibilityManager.modBCraftLoaded = false;
            }
        }

        if (Loader.isModLoaded("AetherII"))
        {
            CompatibilityManager.modAetherIILoaded = true;
        }

        if (Loader.isModLoaded("BasicComponents"))
        {
            CompatibilityManager.modBasicComponentsLoaded = true;
        }

        if (Loader.isModLoaded("appliedenergistics2"))
        {
            CompatibilityManager.modAppEngLoaded = true;
        }

        if (Loader.isModLoaded("PneumaticCraft"))
        {
            CompatibilityManager.modPneumaticCraftLoaded = true;
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

    public static boolean isPneumaticCraftLoaded()
    {
        return CompatibilityManager.modPneumaticCraftLoaded;
    }
}
