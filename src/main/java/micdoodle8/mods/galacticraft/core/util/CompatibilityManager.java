package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockEnclosed;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

import java.lang.reflect.Method;

//import cpw.mods.fml.common.Loader;
//import cpw.mods.fml.common.registry.GameRegistry;


public class CompatibilityManager
{
    private static boolean modIc2Loaded;
	private static boolean modBCraftEnergyLoaded;
    private static boolean modBCraftTransportLoaded;
    private static boolean modGTLoaded;
    private static boolean modTELoaded;
    private static boolean modAetherIILoaded;
    private static boolean modBasicComponentsLoaded;
    private static boolean modAppEngLoaded;
    private static boolean modPneumaticCraftLoaded;
	public static Class classBCBlockGenericPipe = null;
    public static Class<?> classGTOre = null;
	public static Method methodBCBlockPipe_createPipe = null;

    public static void checkForCompatibleMods()
    {
        if (Loader.isModLoaded("gregtech") || Loader.isModLoaded("GregTech_Addon") || Loader.isModLoaded("GregTech"))
        {
            CompatibilityManager.modGTLoaded = true;
            try
            {
                Class<?> clazz = Class.forName("gregtech.common.blocks.GT_Block_Ores");
                if (clazz != null)
                {
                    classGTOre = clazz;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        if (Loader.isModLoaded("ThermalExpansion"))
        {
            CompatibilityManager.modTELoaded = true;
        }

        if (Loader.isModLoaded("IC2"))
        {
            CompatibilityManager.modIc2Loaded = true;

            try
            {
                Class<?> clazz = Class.forName("ic2.core.block.wiring.TileEntityCable");
                if (clazz != null)
                {
                    BlockEnclosed.onBlockNeighbourChangeIC2 = clazz.getMethod("onNeighborChange", Block.class);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        if (Loader.isModLoaded("BuildCraft|Energy"))
        {
            CompatibilityManager.modBCraftEnergyLoaded = true;
        }

        if (Loader.isModLoaded("BuildCraft|Transport"))
        {
            CompatibilityManager.modBCraftTransportLoaded = true;

            try
            {
            	BlockEnclosed.blockPipeBC = (BlockContainer) Block.REGISTRY.getObject(new ResourceLocation("BuildCraft|Transport", "pipeBlock"));
            	classBCBlockGenericPipe = BlockEnclosed.blockPipeBC.getClass(); 

            	for (Method m : classBCBlockGenericPipe.getMethods())
                {
                	if (m.getName().equals("createPipe") && m.getParameterTypes().length == 1)
                    {
                		methodBCBlockPipe_createPipe = m;
                        break;
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            BlockEnclosed.initialiseBC();

            if (CompatibilityManager.methodBCBlockPipe_createPipe == null)
            {
                CompatibilityManager.modBCraftTransportLoaded = false;
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

    public static boolean isBCraftTransportLoaded()
    {
        return CompatibilityManager.modBCraftTransportLoaded;
    }

    public static boolean isBCraftEnergyLoaded()
    {
        return CompatibilityManager.modBCraftEnergyLoaded;
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

    public static void registerMicroBlocks()
    {
        try
        {
            Class clazz = Class.forName("codechicken.microblock.MicroMaterialRegistry");
            if (clazz != null)
            {
                Method registerMethod = null;
                Method[] methodz = clazz.getMethods();
                for (Method m : methodz)
                {
                    if (m.getName().equals("registerMaterial"))
                    {
                        registerMethod = m;
                        break;
                    }
                }
                Class<?> clazzbm = Class.forName("codechicken.microblock.BlockMicroMaterial");
                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(GCBlocks.basicBlock, 3), "tile.gcBlockCore.decoblock1");
                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(GCBlocks.basicBlock, 4), "tile.gcBlockCore.decoblock2");
                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(GCBlocks.basicBlock, 9), "tile.gcBlockCore.copperBlock");
                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(GCBlocks.basicBlock, 10), "tile.gcBlockCore.tinBlock");
                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(GCBlocks.basicBlock, 11), "tile.gcBlockCore.aluminumBlock");
                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(GCBlocks.basicBlock, 12), "tile.gcBlockCore.meteorironBlock");
                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(GCBlocks.blockMoon, 3), "tile.moonBlock.moondirt");
                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(GCBlocks.blockMoon, 4), "tile.moonBlock.moonstone");
                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(GCBlocks.blockMoon, 5), "tile.moonBlock.moongrass");
                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(GCBlocks.blockMoon, 14), "tile.moonBlock.bricks");
            }
        }
        catch (Exception e)
        {
        }
    }
}
