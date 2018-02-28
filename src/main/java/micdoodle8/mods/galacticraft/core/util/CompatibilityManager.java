package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockEnclosed;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldType;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

//import cpw.mods.fml.common.Loader;
//import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;


public class CompatibilityManager
{
    public static boolean PlayerAPILoaded = Loader.isModLoaded("PlayerAPI");
    public static boolean RenderPlayerAPILoaded = Loader.isModLoaded("RenderPlayerAPI");

    public static boolean modJEILoaded = Loader.isModLoaded("JEI");
    private static boolean modIc2Loaded = Loader.isModLoaded("IC2");
	private static boolean modBCraftEnergyLoaded = Loader.isModLoaded("BuildCraft|Energy");
    private static boolean modBCraftTransportLoaded;
    private static boolean modGTLoaded;
    private static boolean modTELoaded = Loader.isModLoaded("ThermalExpansion");
    private static boolean modMekLoaded = Loader.isModLoaded("Mekanism");
    private static boolean modAetherIILoaded;
    private static boolean modBasicComponentsLoaded;
    private static boolean modAppEngLoaded;
    private static boolean modPneumaticCraftLoaded;
    private static boolean modBOPLoaded = Loader.isModLoaded("BiomesOPlenty");
    private static boolean modEIOLoaded = Loader.isModLoaded("EnderIO");
    public static boolean modAALoaded = Loader.isModLoaded("ActuallyAdditions");
    private static boolean modMatterOverdriveLoaded;
    private static boolean wailaLoaded;
    public static boolean isMFRLoaded = Loader.isModLoaded("MineFactoryReloaded");
    public static boolean isSmartMovingLoaded = Loader.isModLoaded("SmartMoving");
    public static boolean isTConstructLoaded = Loader.isModLoaded("tconstruct");
    public static boolean isWitcheryLoaded = Loader.isModLoaded("witchery");
//    public static int isBG2Loaded = 0;

	public static Class classBCBlockGenericPipe = null;
    public static Class<?> classGTOre = null;
	public static Method methodBCBlockPipe_createPipe = null;
    public static Field fieldBCoilBucket;
	public static Class classBOPWorldType = null;
	public static Class classBOPws = null;
    public static Class classBOPwcm = null;
    public static Class classIC2wrench = null;
    public static Class classIC2wrenchElectric = null;
    public static Class classIC2tileEventLoad;
    public static Class classIC2tileEventUnload;
    public static Class classIC2cableType = null;
    public static Constructor constructorIC2cableTE = null;
    private static Method androidPlayerGet;
    private static Method androidPlayerIsAndroid;
	
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
                GCLog.info("Galacticraft: activating GregTech compatibility feature.");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        if (CompatibilityManager.modMekLoaded)
        {
            GCLog.info("Galacticraft: activating Mekanism compatibility.");
        }

        if (CompatibilityManager.isMFRLoaded)
        {
            GCLog.info("Galacticraft: activating MFR compatibility feature.");
        }

        if (CompatibilityManager.modTELoaded)
        {
            GCLog.info("Galacticraft: activating ThermalExpansion compatibility features.");
        }
        
        if (CompatibilityManager.isTConstructLoaded)
        {
            GCLog.info("Galacticraft: activating Tinker's Construct compatibility features.");
        }

        if (CompatibilityManager.modIc2Loaded)
        {
            try
            {
                try {
                    classIC2wrench = Class.forName("ic2.core.item.tool.ItemToolWrench");
                } catch (ClassNotFoundException e) { }
                
                try {
                    classIC2wrenchElectric = Class.forName("ic2.core.item.tool.ItemToolWrenchElectric");
                } catch (ClassNotFoundException e) { }
                
                try {
                    classIC2tileEventLoad = Class.forName("ic2.api.energy.event.EnergyTileLoadEvent");
                    classIC2tileEventUnload = Class.forName("ic2.api.energy.event.EnergyTileUnloadEvent");
                } catch (ClassNotFoundException e) { }
                
                Class classIC2cable = Class.forName("ic2.core.block.wiring.TileEntityCable");
                classIC2cableType = Class.forName("ic2.core.block.wiring.CableType");
                if (classIC2cable != null)
                {
                    try {
                        BlockEnclosed.onBlockNeighbourChangeIC2a = classIC2cable.getMethod("onNeighborChange", Block.class);
                    }
                    catch (NoSuchMethodException e)
                    {
                        BlockEnclosed.onBlockNeighbourChangeIC2b = classIC2cable.getMethod("onNeighborChange", Block.class, BlockPos.class);
                    }
                    
                    Constructor<?>[] constructors = classIC2cable.getDeclaredConstructors();
                    for (Constructor<?> constructor2 : constructors)
                    {
                        if (constructor2.getGenericParameterTypes().length == 2)
                        {
                            constructorIC2cableTE = constructor2;
                            break;
                        }
                    }
                }
                GCLog.info("Galacticraft: activating IndustrialCraft2 compatibility features.");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        if (CompatibilityManager.modBCraftEnergyLoaded)
        {
            try
            {
                Class<?> buildCraftClass = null;
                if ((buildCraftClass = Class.forName("buildcraft.BuildCraftEnergy")) != null)
                {
                    for (final Field f : buildCraftClass.getFields())
                    {
                        if (f.getName().equals("bucketOil"))
                        {
                            fieldBCoilBucket = f;
                            break;
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            GCLog.info("Galacticraft: activating BuildCraft Oil compatibility features.");
        }

        if (Loader.isModLoaded("BuildCraft|Transport"))
        {
            CompatibilityManager.modBCraftTransportLoaded = true;

            try
            {
            	BlockEnclosed.blockPipeBC = (BlockContainer) GameRegistry.findBlock("BuildCraft|Transport", "pipeBlock");
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
            else
            {
                GCLog.info("Galacticraft: activating BuildCraft Transport (Pipes) compatibility features.");
            }
        }

        if (CompatibilityManager.modBOPLoaded)
        {
            try {
                classBOPWorldType = Class.forName("biomesoplenty.common.world.WorldTypeBOP");
                classBOPws = Class.forName("biomesoplenty.common.world.BOPWorldSettings");
                classBOPwcm = Class.forName("biomesoplenty.common.world.WorldChunkManagerBOP");
                GCLog.info("Galacticraft: activating Biomes O'Plenty compatibility feature.");
            } catch (Exception e) { e.printStackTrace(); }
        }

        if (Loader.isModLoaded("AetherII"))
        {
            CompatibilityManager.modAetherIILoaded = true;
            GCLog.info("Galacticraft: activating AetherII compatibility feature.");
        }

        if (Loader.isModLoaded("BasicComponents"))
        {
            CompatibilityManager.modBasicComponentsLoaded = true;
        }

        if (Loader.isModLoaded("appliedenergistics2"))
        {
            CompatibilityManager.modAppEngLoaded = true;
            GCLog.info("Galacticraft: activating AppliedEnergistics2 compatibility features.");
        }

        if (Loader.isModLoaded("PneumaticCraft"))
        {
            CompatibilityManager.modPneumaticCraftLoaded = true;
            GCLog.info("Galacticraft: activating PneumaticCraft compatibility features.");
        }

        if (Loader.isModLoaded("Waila"))
        {
            CompatibilityManager.wailaLoaded = true;
            GCLog.info("Galacticraft: activating WAILA compatibility features.");
        }

//TODO      
//        //Compatibility with BattleGear2 - was used by RenderEvolvedSkeleton in 1.7.10
//        try
//        {
//            Class<?> clazz = Class.forName("mods.battlegear2.MobHookContainerClass");
//
//            //accessing this: public static final int Skell_Arrow_Datawatcher = 25;
//            CompatibilityManager.isBG2Loaded = clazz.getField("Skell_Arrow_Datawatcher").getInt(null);
//        }
//        catch (Exception e)
//        {
//        }

        if (Loader.isModLoaded("mo"))
        {
            try {
                Class<?> androidPlayer = Class.forName("matteroverdrive.entity.player.AndroidPlayer");
                CompatibilityManager.androidPlayerGet = androidPlayer.getMethod("get", EntityPlayer.class);
                CompatibilityManager.androidPlayerIsAndroid = androidPlayer.getMethod("isAndroid");
                CompatibilityManager.modMatterOverdriveLoaded = true;
            }
            catch (Exception e) { e.printStackTrace(); }
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

    public static boolean isMekanismLoaded()
    {
        return CompatibilityManager.modMekLoaded;
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

    public static boolean isBOPLoaded()
    {
        return CompatibilityManager.modBOPLoaded;
    }

    public static boolean isBOPWorld(WorldType worldType)
    {
        if (modBOPLoaded && classBOPWorldType != null && classBOPws != null && classBOPwcm != null)
        {
            return classBOPWorldType.isInstance(worldType);
        }
        return false;
    }

    public static boolean isPneumaticCraftLoaded()
    {
        return CompatibilityManager.modPneumaticCraftLoaded;
    }

    public static boolean isWailaLoaded()
    {
        return CompatibilityManager.wailaLoaded;
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
                GCLog.info("Galacticraft: activating CodeChicken Microblocks compatibility.");
            }
        }
        catch (Exception e)
        {
        }
    }

    public static boolean isAndroid(EntityPlayer player)
    {
        if (CompatibilityManager.modMatterOverdriveLoaded)
        {
//          Equivalent to:
//            AndroidPlayer androidPlayer = AndroidPlayer.get(player);
//            return (androidPlayer != null && androidPlayer.isAndroid());
            try
            {
                Object androidPlayer = CompatibilityManager.androidPlayerGet.invoke(null, player);
                if (androidPlayer != null)
                {
                    return (Boolean) CompatibilityManager.androidPlayerIsAndroid.invoke(androidPlayer);
                }
            } catch (Exception ignore)
            {
            }
        }
        return false;
    }
    
    public static boolean useAluDust()
    {
        return modIc2Loaded || modAppEngLoaded || modTELoaded || modEIOLoaded || modAALoaded;
    }
}
