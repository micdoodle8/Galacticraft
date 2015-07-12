package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.items.*;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

public class GCBlocks
{
    public static Block breatheableAir;
    public static Block brightAir;
    public static Block brightBreatheableAir;
    public static Block brightLamp;
    public static Block treasureChestTier1;
    public static Block landingPad;
    public static Block unlitTorch;
    public static Block unlitTorchLit;
    public static Block oxygenDistributor;
    public static Block oxygenPipe;
    public static Block oxygenCollector;
    public static Block oxygenCompressor;
    public static Block oxygenSealer;
    public static Block oxygenDetector;
    public static Block nasaWorkbench;
    public static Block fallenMeteor;
    public static Block basicBlock;
    public static Block airLockFrame;
    public static Block airLockSeal;
    public static Block crudeOilStill;
    public static Block fuelStill;
    public static Block refinery;
    public static Block fuelLoader;
    public static Block landingPadFull;
    public static Block spaceStationBase;
    public static Block fakeBlock;
    public static Block sealableBlock;
    public static Block cargoLoader;
    public static Block parachest;
    public static Block solarPanel;
    public static Block machineBase;
    public static Block machineBase2;
    public static Block machineTiered;
    public static Block aluminumWire;
    public static Block glowstoneTorch;
    public static Block blockMoon;
    public static Block cheeseBlock;
    public static Block spinThruster;
    public static Block screen;
    public static Block telemetry;
    /*public static Block slabGCHalf;
    public static Block slabGCDouble;
    public static Block tinStairs1;
    public static Block tinStairs2;
    public static Block moonStoneStairs;
    public static Block moonBricksStairs;
    public static Block wallGC;*/

    public static final Material machine = new Material(MapColor.ironColor);

    public static ArrayList<Block> hiddenBlocks = new ArrayList<Block>();

    public static void initBlocks()
    {
        GCBlocks.breatheableAir = new BlockBreathableAir("breatheable_air");
        GCBlocks.brightAir = new BlockBrightAir("bright_air");
        GCBlocks.brightBreatheableAir = new BlockBrightBreathableAir("bright_breathable_air");
        GCBlocks.brightLamp = new BlockBrightLamp("arclamp");
        GCBlocks.treasureChestTier1 = new BlockT1TreasureChest("treasure_chest");
        GCBlocks.landingPad = new BlockLandingPad("landing_pad");
        GCBlocks.landingPadFull = new BlockLandingPadFull("landing_pad_full");
        GCBlocks.unlitTorch = new BlockUnlitTorch(false, "unlit_torch");
        GCBlocks.unlitTorchLit = new BlockUnlitTorch(true, "unlit_torch_lit");
        GCBlocks.oxygenDistributor = new BlockOxygenDistributor("distributor");
        GCBlocks.oxygenPipe = new BlockOxygenPipe("oxygen_pipe");
        GCBlocks.oxygenCollector = new Block(Material.cake).setUnlocalizedName("collector");
        GCBlocks.nasaWorkbench = new BlockNasaWorkbench("rocket_workbench");
        GCBlocks.fallenMeteor = new BlockFallenMeteor("fallen_meteor");
        GCBlocks.basicBlock = new BlockBasic("basic_block_core");
        GCBlocks.airLockFrame = new BlockAirLockFrame("air_lock_frame");
        GCBlocks.airLockSeal = new BlockAirLockWall("air_lock_seal");
        GCBlocks.refinery = new BlockRefinery("refinery");
        GCBlocks.oxygenCompressor = new BlockOxygenCompressor(false, "oxygen_compressor");
        GCBlocks.fuelLoader = new BlockFuelLoader("fuel_loader");
        GCBlocks.spaceStationBase = new BlockSpaceStationBase("space_station_base");
        GCBlocks.fakeBlock = new BlockMulti("dummyblock");
        GCBlocks.oxygenSealer = new BlockOxygenSealer("sealer");
        GCBlocks.sealableBlock = new BlockEnclosed("enclosed");
        GCBlocks.oxygenDetector = new BlockOxygenDetector("oxygen_detector");
        GCBlocks.cargoLoader = new BlockCargoLoader("cargo");
        GCBlocks.parachest = new BlockParaChest("parachest");
        GCBlocks.solarPanel = new BlockSolar("solar");
        GCBlocks.machineBase = new BlockMachine("machine");
        GCBlocks.machineBase2 = new BlockMachine2("machine2");
        GCBlocks.machineTiered = new BlockMachineTiered("machine_tiered");
        GCBlocks.aluminumWire = new BlockAluminumWire("aluminum_wire");
        GCBlocks.glowstoneTorch = new BlockGlowstoneTorch("glowstone_torch");
        GCBlocks.blockMoon = new BlockBasicMoon("basic_block_moon");
        GCBlocks.cheeseBlock = new BlockCheese("cheese");
        GCBlocks.spinThruster = new BlockSpinThruster("spin_thruster");
        GCBlocks.screen = new BlockScreen("view_screen");
        GCBlocks.telemetry = new BlockTelemetry("telemetry");
        /*GCBlocks.slabGCHalf = new BlockSlabGC("slabGCHalf", false, Material.rock);
        GCBlocks.slabGCDouble = new BlockSlabGC("slabGCDouble", true, Material.rock);
        GCBlocks.tinStairs1 = new BlockStairsGC("tinStairs1", blockMoon, BlockStairsGC.StairsCategoryGC.TIN1).setHardness(2.0F);
        GCBlocks.tinStairs2 = new BlockStairsGC("tinStairs2", blockMoon, BlockStairsGC.StairsCategoryGC.TIN2).setHardness(2.0F);
        GCBlocks.moonStoneStairs = new BlockStairsGC("moonStoneStairs", blockMoon, BlockStairsGC.StairsCategoryGC.MOON_STONE).setHardness(1.5F);
        GCBlocks.moonBricksStairs = new BlockStairsGC("moonBricksStairs", blockMoon, BlockStairsGC.StairsCategoryGC.MOON_BRICKS).setHardness(4.0F);
        GCBlocks.wallGC = new BlockWallGC("wallGC", blockMoon);*/

        GCCoreUtil.registerGalacticraftBlock("rocket_launch_pad", GCBlocks.landingPad, 0);
        GCCoreUtil.registerGalacticraftBlock("buggy_fueling_pad", GCBlocks.landingPad, 1);
        GCCoreUtil.registerGalacticraftBlock("collector", GCBlocks.oxygenCollector);
        GCCoreUtil.registerGalacticraftBlock("oxygen_compressor", GCBlocks.oxygenCompressor);
        GCCoreUtil.registerGalacticraftBlock("oxygen_distributor", GCBlocks.oxygenDistributor);
        GCCoreUtil.registerGalacticraftBlock("oxygen_sealer", GCBlocks.oxygenSealer);
        GCCoreUtil.registerGalacticraftBlock("oxygen_detector", GCBlocks.oxygenDetector);
        GCCoreUtil.registerGalacticraftBlock("oxygen_pipe", GCBlocks.oxygenPipe);
        GCCoreUtil.registerGalacticraftBlock("refinery", GCBlocks.refinery);
        GCCoreUtil.registerGalacticraftBlock("fuel_loader", GCBlocks.fuelLoader);
        GCCoreUtil.registerGalacticraftBlock("cargo_loader", GCBlocks.cargoLoader, 0);
        GCCoreUtil.registerGalacticraftBlock("cargo_unloader", GCBlocks.cargoLoader, 4);
        GCCoreUtil.registerGalacticraftBlock("nasa_workbench", GCBlocks.nasaWorkbench);
        GCCoreUtil.registerGalacticraftBlock("tin_decoration_block_1", GCBlocks.basicBlock, 3);
        GCCoreUtil.registerGalacticraftBlock("tin_decoration_block_2", GCBlocks.basicBlock, 4);
        GCCoreUtil.registerGalacticraftBlock("air_lock_frame", GCBlocks.airLockFrame);
        GCCoreUtil.registerGalacticraftBlock("sealable_copper_wire", GCBlocks.sealableBlock, 0);
        GCCoreUtil.registerGalacticraftBlock("sealable_oxygen_pipe", GCBlocks.sealableBlock, 1);
        GCCoreUtil.registerGalacticraftBlock("sealable_copper_cable", GCBlocks.sealableBlock, 2);
        GCCoreUtil.registerGalacticraftBlock("sealable_gold_cable", GCBlocks.sealableBlock, 3);
        GCCoreUtil.registerGalacticraftBlock("sealable_high_voltage_cable", GCBlocks.sealableBlock, 4);
        GCCoreUtil.registerGalacticraftBlock("sealable_glass_fibre_cable", GCBlocks.sealableBlock, 5);
        GCCoreUtil.registerGalacticraftBlock("sealable_low_voltage_cable", GCBlocks.sealableBlock, 6);
        GCCoreUtil.registerGalacticraftBlock("sealable_stone_pipe_item", GCBlocks.sealableBlock, 7);
        GCCoreUtil.registerGalacticraftBlock("sealable_cobblestone_pipe_item", GCBlocks.sealableBlock, 8);
        GCCoreUtil.registerGalacticraftBlock("sealable_stone_pipe_fluid", GCBlocks.sealableBlock, 9);
        GCCoreUtil.registerGalacticraftBlock("sealable_cobblestone_pipe_fluid", GCBlocks.sealableBlock, 10);
        GCCoreUtil.registerGalacticraftBlock("sealable_stone_pipe_power", GCBlocks.sealableBlock, 11);
        GCCoreUtil.registerGalacticraftBlock("sealable_gold_pipe_power", GCBlocks.sealableBlock, 12);
        GCCoreUtil.registerGalacticraftBlock("copper_wire", GCBlocks.aluminumWire);
        GCCoreUtil.registerGalacticraftBlock("parachest", GCBlocks.parachest);
        GCCoreUtil.registerGalacticraftBlock("coal_generator", GCBlocks.machineBase, 0);
        GCCoreUtil.registerGalacticraftBlock("solar_panel_basic", GCBlocks.solarPanel, 0);
        GCCoreUtil.registerGalacticraftBlock("solar_panel_advanced", GCBlocks.solarPanel, 4);
        GCCoreUtil.registerGalacticraftBlock("energy_storage_module", GCBlocks.machineTiered, 0);
        GCCoreUtil.registerGalacticraftBlock("electric_furnace", GCBlocks.machineTiered, 4);
        GCCoreUtil.registerGalacticraftBlock("ingot_compressor", GCBlocks.machineBase, 12);
        GCCoreUtil.registerGalacticraftBlock("circuit_fabricator", GCBlocks.machineBase2, 4);
        GCCoreUtil.registerGalacticraftBlock("ingot_compressor_electric", GCBlocks.machineBase2, 0);
        GCCoreUtil.registerGalacticraftBlock("electric_arc_furnace", GCBlocks.machineTiered, 12);
        GCCoreUtil.registerGalacticraftBlock("energy_storage_cluster", GCBlocks.machineTiered, 8);
        GCCoreUtil.registerGalacticraftBlock("ore_copper", GCBlocks.basicBlock, 5);
        GCCoreUtil.registerGalacticraftBlock("ore_tin", GCBlocks.basicBlock, 6);
        GCCoreUtil.registerGalacticraftBlock("ore_aluminum", GCBlocks.basicBlock, 7);
        GCCoreUtil.registerGalacticraftBlock("ore_silicon", GCBlocks.basicBlock, 8);
        GCCoreUtil.registerGalacticraftBlock("fallen_meteor", GCBlocks.fallenMeteor);
        GCCoreUtil.registerGalacticraftBlock("torch_glowstone", GCBlocks.glowstoneTorch);
        GCCoreUtil.registerGalacticraftBlock("wire_aluminum", GCBlocks.aluminumWire);
        GCCoreUtil.registerGalacticraftBlock("wire_aluminumHeavy", GCBlocks.aluminumWire, 1);
        GCCoreUtil.registerGalacticraftBlock("spin_thruster", GCBlocks.spinThruster);
        GCCoreUtil.registerGalacticraftBlock("view_screen", GCBlocks.screen);
        GCCoreUtil.registerGalacticraftBlock("telemetry", GCBlocks.telemetry);
        GCCoreUtil.registerGalacticraftBlock("arclamp", GCBlocks.brightLamp);
        GCCoreUtil.registerGalacticraftBlock("treasure_chest_tier_1", GCBlocks.treasureChestTier1);

        // Hide certain items from NEI
        GCBlocks.hiddenBlocks.add(GCBlocks.airLockSeal);
        GCBlocks.hiddenBlocks.add(GCBlocks.breatheableAir);
        GCBlocks.hiddenBlocks.add(GCBlocks.brightBreatheableAir);
        GCBlocks.hiddenBlocks.add(GCBlocks.brightAir);
        GCBlocks.hiddenBlocks.add(GCBlocks.unlitTorch);
        GCBlocks.hiddenBlocks.add(GCBlocks.unlitTorchLit);
        GCBlocks.hiddenBlocks.add(GCBlocks.landingPadFull);
        GCBlocks.hiddenBlocks.add(GCBlocks.fakeBlock);
        GCBlocks.hiddenBlocks.add(GCBlocks.spaceStationBase);
        //GCBlocks.hiddenBlocks.add(GCBlocks.slabGCDouble);

        // Register blocks before register ores, so that the ItemStack picks up the correct item
        GCBlocks.registerBlocks();
        GCBlocks.setHarvestLevels();

        OreDictionary.registerOre("oreCopper", new ItemStack(GCBlocks.basicBlock, 1, 5));
        OreDictionary.registerOre("oreCopper", new ItemStack(GCBlocks.blockMoon, 1, 0));
        OreDictionary.registerOre("oreTin", new ItemStack(GCBlocks.basicBlock, 1, 6));
        OreDictionary.registerOre("oreTin", new ItemStack(GCBlocks.blockMoon, 1, 1));
        OreDictionary.registerOre("oreAluminum", new ItemStack(GCBlocks.basicBlock, 1, 7));
        OreDictionary.registerOre("oreAluminium", new ItemStack(GCBlocks.basicBlock, 1, 7));
        OreDictionary.registerOre("oreNaturalAluminum", new ItemStack(GCBlocks.basicBlock, 1, 7));
        OreDictionary.registerOre("oreSilicon", new ItemStack(GCBlocks.basicBlock, 1, 8));
        OreDictionary.registerOre("oreCheese", new ItemStack(GCBlocks.blockMoon, 1, 2));
    }

    private static void setHarvestLevel(Block block, String toolClass, int level, int meta)
    {
        block.setHarvestLevel(toolClass, level, block.getStateFromMeta(meta));
    }

    private static void setHarvestLevel(Block block, String toolClass, int level)
    {
        block.setHarvestLevel(toolClass, level);
    }

    public static void setHarvestLevels()
    {
        setHarvestLevel(GCBlocks.basicBlock, "pickaxe", 2, 5); //Copper ore
        setHarvestLevel(GCBlocks.basicBlock, "pickaxe", 2, 6); //Tin ore
        setHarvestLevel(GCBlocks.basicBlock, "pickaxe", 2, 7); //Aluminium ore
        setHarvestLevel(GCBlocks.basicBlock, "pickaxe", 1, 8); //Silicon ore
        setHarvestLevel(GCBlocks.fallenMeteor, "pickaxe", 3);
        setHarvestLevel(GCBlocks.blockMoon, "pickaxe", 2, 0); //Copper ore
        setHarvestLevel(GCBlocks.blockMoon, "pickaxe", 2, 1); //Tin ore
        setHarvestLevel(GCBlocks.blockMoon, "pickaxe", 1, 2); //Cheese ore
        setHarvestLevel(GCBlocks.blockMoon, "shovel", 0, 3); //Moon dirt
        setHarvestLevel(GCBlocks.blockMoon, "pickaxe", 1, 4); //Moon rock

        /*setHarvestLevel(GCBlocks.slabGCHalf, "pickaxe", 1, 0);
        setHarvestLevel(GCBlocks.slabGCHalf, "pickaxe", 1, 1);
        setHarvestLevel(GCBlocks.slabGCHalf, "pickaxe", 1, 2);
        setHarvestLevel(GCBlocks.slabGCHalf, "pickaxe", 3, 3);
        setHarvestLevel(GCBlocks.slabGCHalf, "pickaxe", 1, 4);
        setHarvestLevel(GCBlocks.slabGCHalf, "pickaxe", 3, 5);

        setHarvestLevel(GCBlocks.slabGCDouble, "pickaxe", 1, 0);
        setHarvestLevel(GCBlocks.slabGCDouble, "pickaxe", 1, 1);
        setHarvestLevel(GCBlocks.slabGCDouble, "pickaxe", 1, 2);
        setHarvestLevel(GCBlocks.slabGCDouble, "pickaxe", 3, 3);
        setHarvestLevel(GCBlocks.slabGCDouble, "pickaxe", 1, 4);
        setHarvestLevel(GCBlocks.slabGCDouble, "pickaxe", 3, 5);

        setHarvestLevel(GCBlocks.tinStairs1, "pickaxe", 1);
        setHarvestLevel(GCBlocks.tinStairs1, "pickaxe", 1);

        setHarvestLevel(GCBlocks.moonStoneStairs, "pickaxe", 1);
        setHarvestLevel(GCBlocks.moonBricksStairs, "pickaxe", 3);

        setHarvestLevel(GCBlocks.wallGC, "pickaxe", 1, 0);
        setHarvestLevel(GCBlocks.wallGC, "pickaxe", 1, 1);
        setHarvestLevel(GCBlocks.wallGC, "pickaxe", 1, 2);
        setHarvestLevel(GCBlocks.wallGC, "pickaxe", 3, 3);
        setHarvestLevel(GCBlocks.wallGC, "pickaxe", 0, 4);
        setHarvestLevel(GCBlocks.wallGC, "pickaxe", 3, 5);

        for (int num = 5; num < 14; num++)
        {
            //Various types of Moon top dirt
            setHarvestLevel(GCBlocks.wallGC, "shovel", 0, num);
        }*/

        setHarvestLevel(GCBlocks.blockMoon, "pickaxe", 3, 14); //Moon dungeon brick (actually unharvestable)
    }

    private static void registerBlock(Block block, Class<? extends ItemBlock> itemClass)
    {
        GameRegistry.registerBlock(block, itemClass, block.getUnlocalizedName().substring(5));
    }

    public static void registerBlocks()
    {
        registerBlock(GCBlocks.landingPad, ItemBlockLandingPad.class);
        registerBlock(GCBlocks.landingPadFull, ItemBlockGC.class);
        registerBlock(GCBlocks.unlitTorch, ItemBlockGC.class);
        registerBlock(GCBlocks.unlitTorchLit, ItemBlockGC.class);
        registerBlock(GCBlocks.breatheableAir, ItemBlockGC.class);
        registerBlock(GCBlocks.brightAir, ItemBlockGC.class);
        registerBlock(GCBlocks.brightBreatheableAir, ItemBlockGC.class);
        registerBlock(GCBlocks.oxygenDistributor, ItemBlockDesc.class);
        registerBlock(GCBlocks.oxygenCollector, ItemBlock.class);
        registerBlock(GCBlocks.oxygenCompressor, ItemBlockOxygenCompressor.class);
        registerBlock(GCBlocks.oxygenSealer, ItemBlockDesc.class);
        registerBlock(GCBlocks.oxygenDetector, ItemBlockDesc.class);
        registerBlock(GCBlocks.oxygenPipe, ItemBlockDesc.class);
        registerBlock(GCBlocks.refinery, ItemBlockDesc.class);
        registerBlock(GCBlocks.fuelLoader, ItemBlockDesc.class);
        registerBlock(GCBlocks.cargoLoader, ItemBlockCargoLoader.class);
        registerBlock(GCBlocks.nasaWorkbench, ItemBlockDesc.class);
        registerBlock(GCBlocks.basicBlock, ItemBlockBase.class);
        registerBlock(GCBlocks.airLockFrame, ItemBlockAirLock.class);
        registerBlock(GCBlocks.airLockSeal, ItemBlockGC.class);
        registerBlock(GCBlocks.sealableBlock, ItemBlockEnclosed.class);
        registerBlock(GCBlocks.spaceStationBase, ItemBlockGC.class);
        registerBlock(GCBlocks.fakeBlock, ItemBlockDummy.class);
        registerBlock(GCBlocks.parachest, ItemBlockDesc.class);
        registerBlock(GCBlocks.solarPanel, ItemBlockSolar.class);
        registerBlock(GCBlocks.machineBase, ItemBlockMachine.class);
        registerBlock(GCBlocks.machineBase2, ItemBlockMachine.class);
        registerBlock(GCBlocks.machineTiered, ItemBlockMachine.class);
        registerBlock(GCBlocks.aluminumWire, ItemBlockAluminumWire.class);
        registerBlock(GCBlocks.glowstoneTorch, ItemBlockDesc.class);
        registerBlock(GCBlocks.fallenMeteor, ItemBlockDesc.class);
        registerBlock(GCBlocks.blockMoon, ItemBlockMoon.class);
        registerBlock(GCBlocks.cheeseBlock, ItemBlockCheese.class);
        registerBlock(GCBlocks.spinThruster, ItemBlockThruster.class);
        registerBlock(GCBlocks.screen, ItemBlockDesc.class);
        registerBlock(GCBlocks.telemetry, ItemBlockDesc.class);
        registerBlock(GCBlocks.brightLamp, ItemBlockArclamp.class);
        registerBlock(GCBlocks.treasureChestTier1, ItemBlockDesc.class);
        /*registerBlock(GCBlocks.tinStairs1, ItemBlockGC.class, GCBlocks.tinStairs1.getUnlocalizedName());
        registerBlock(GCBlocks.tinStairs2, ItemBlockGC.class, GCBlocks.tinStairs2.getUnlocalizedName());
        registerBlock(GCBlocks.moonStoneStairs, ItemBlockGC.class, GCBlocks.moonStoneStairs.getUnlocalizedName());
        registerBlock(GCBlocks.moonBricksStairs, ItemBlockGC.class, GCBlocks.moonBricksStairs.getUnlocalizedName());
        registerBlock(GCBlocks.wallGC, ItemBlockWallGC.class, GCBlocks.wallGC.getUnlocalizedName());
        registerBlock(GCBlocks.slabGCHalf, ItemBlockSlabGC.class, GCBlocks.slabGCHalf.getUnlocalizedName().replace("tile.", ""), GCBlocks.slabGCHalf, GCBlocks.slabGCDouble);
        registerBlock(GCBlocks.slabGCDouble, ItemBlockSlabGC.class, GCBlocks.slabGCDouble.getUnlocalizedName().replace("tile.", ""), GCBlocks.slabGCHalf, GCBlocks.slabGCDouble);*/
    }
}
