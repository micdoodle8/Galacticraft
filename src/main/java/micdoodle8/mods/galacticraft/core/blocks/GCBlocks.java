package micdoodle8.mods.galacticraft.core.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import micdoodle8.mods.galacticraft.core.items.*;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
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

    public static final Material machine = new Material(MapColor.ironColor);

    public static ArrayList<Block> hiddenBlocks = new ArrayList<Block>();

    public static void initBlocks()
    {
        GCBlocks.breatheableAir = new BlockBreathableAir("breatheableAir");
        GCBlocks.brightAir = new BlockBrightAir("brightAir");
        GCBlocks.brightBreatheableAir = new BlockBrightBreathableAir("brightBreathableAir");
        GCBlocks.brightLamp = new BlockBrightLamp("arclamp");
        GCBlocks.treasureChestTier1 = new BlockT1TreasureChest("treasureChest");
        GCBlocks.landingPad = new BlockLandingPad("landingPad");
        GCBlocks.landingPadFull = new BlockLandingPadFull("landingPadFull");
        GCBlocks.unlitTorch = new BlockUnlitTorch(false, "unlitTorch");
        GCBlocks.unlitTorchLit = new BlockUnlitTorch(true, "unlitTorchLit");
        GCBlocks.oxygenDistributor = new BlockOxygenDistributor("distributor");
        GCBlocks.oxygenPipe = new BlockOxygenPipe("oxygenPipe");
        GCBlocks.oxygenCollector = new BlockOxygenCollector("oxygenCollector");
        GCBlocks.nasaWorkbench = new BlockNasaWorkbench("rocketWorkbench");
        GCBlocks.fallenMeteor = new BlockFallenMeteor("fallenMeteor");
        GCBlocks.basicBlock = new BlockBasic("gcBlockCore");
        GCBlocks.airLockFrame = new BlockAirLockFrame("airLockFrame");
        GCBlocks.airLockSeal = new BlockAirLockWall("airLockSeal");
        GCBlocks.refinery = new BlockRefinery("refinery");
        GCBlocks.oxygenCompressor = new BlockOxygenCompressor(false, "oxygenCompressor");
        GCBlocks.fuelLoader = new BlockFuelLoader("fuelLoader");
        GCBlocks.spaceStationBase = new BlockSpaceStationBase("spaceStationBase");
        GCBlocks.fakeBlock = new BlockMulti("dummyblock");
        GCBlocks.oxygenSealer = new BlockOxygenSealer("sealer");
        GCBlocks.sealableBlock = new BlockEnclosed("enclosed");
        GCBlocks.oxygenDetector = new BlockOxygenDetector("oxygenDetector");
        GCBlocks.cargoLoader = new BlockCargoLoader("cargo");
        GCBlocks.parachest = new BlockParaChest("parachest");
        GCBlocks.solarPanel = new BlockSolar("solar");
        GCBlocks.machineBase = new BlockMachine("machine");
        GCBlocks.machineBase2 = new BlockMachine2("machine2");
        GCBlocks.machineTiered = new BlockMachineTiered("machineTiered");
        GCBlocks.aluminumWire = new BlockAluminumWire("aluminumWire");
        GCBlocks.glowstoneTorch = new BlockGlowstoneTorch("glowstoneTorch");
        GCBlocks.blockMoon = new BlockBasicMoon();
        GCBlocks.cheeseBlock = new BlockCheese();
        GCBlocks.spinThruster = new BlockSpinThruster("spinThruster");

        GCCoreUtil.registerGalacticraftBlock("rocketLaunchPad", GCBlocks.landingPad, 0);
        GCCoreUtil.registerGalacticraftBlock("buggyFuelingPad", GCBlocks.landingPad, 1);
        GCCoreUtil.registerGalacticraftBlock("oxygenCollector", GCBlocks.oxygenCollector);
        GCCoreUtil.registerGalacticraftBlock("oxygenCompressor", GCBlocks.oxygenCompressor);
        GCCoreUtil.registerGalacticraftBlock("oxygenDistributor", GCBlocks.oxygenDistributor);
        GCCoreUtil.registerGalacticraftBlock("oxygenSealer", GCBlocks.oxygenSealer);
        GCCoreUtil.registerGalacticraftBlock("oxygenDetector", GCBlocks.oxygenDetector);
        GCCoreUtil.registerGalacticraftBlock("oxygenPipe", GCBlocks.oxygenPipe);
        GCCoreUtil.registerGalacticraftBlock("refinery", GCBlocks.refinery);
        GCCoreUtil.registerGalacticraftBlock("fuelLoader", GCBlocks.fuelLoader);
        GCCoreUtil.registerGalacticraftBlock("cargoLoader", GCBlocks.cargoLoader, 0);
        GCCoreUtil.registerGalacticraftBlock("cargoUnloader", GCBlocks.cargoLoader, 4);
        GCCoreUtil.registerGalacticraftBlock("nasaWorkbench", GCBlocks.nasaWorkbench);
        GCCoreUtil.registerGalacticraftBlock("tinDecorationBlock1", GCBlocks.basicBlock, 3);
        GCCoreUtil.registerGalacticraftBlock("tinDecorationBlock2", GCBlocks.basicBlock, 4);
        GCCoreUtil.registerGalacticraftBlock("airLockFrame", GCBlocks.airLockFrame);
        GCCoreUtil.registerGalacticraftBlock("sealableCopperWire", GCBlocks.sealableBlock, 0);
        GCCoreUtil.registerGalacticraftBlock("sealableOxygenPipe", GCBlocks.sealableBlock, 1);
        GCCoreUtil.registerGalacticraftBlock("sealableCopperCable", GCBlocks.sealableBlock, 2);
        GCCoreUtil.registerGalacticraftBlock("sealableGoldCable", GCBlocks.sealableBlock, 3);
        GCCoreUtil.registerGalacticraftBlock("sealableHighVoltageCable", GCBlocks.sealableBlock, 4);
        GCCoreUtil.registerGalacticraftBlock("sealableGlassFibreCable", GCBlocks.sealableBlock, 5);
        GCCoreUtil.registerGalacticraftBlock("sealableLowVoltageCable", GCBlocks.sealableBlock, 6);
        GCCoreUtil.registerGalacticraftBlock("sealableStonePipeItem", GCBlocks.sealableBlock, 7);
        GCCoreUtil.registerGalacticraftBlock("sealableCobblestonePipeItem", GCBlocks.sealableBlock, 8);
        GCCoreUtil.registerGalacticraftBlock("sealableStonePipeFluid", GCBlocks.sealableBlock, 9);
        GCCoreUtil.registerGalacticraftBlock("sealableCobblestonePipeFluid", GCBlocks.sealableBlock, 10);
        GCCoreUtil.registerGalacticraftBlock("sealableStonePipePower", GCBlocks.sealableBlock, 11);
        GCCoreUtil.registerGalacticraftBlock("sealableGoldPipePower", GCBlocks.sealableBlock, 12);
        GCCoreUtil.registerGalacticraftBlock("copperWire", GCBlocks.aluminumWire);
        GCCoreUtil.registerGalacticraftBlock("parachest", GCBlocks.parachest);
        GCCoreUtil.registerGalacticraftBlock("coalGenerator", GCBlocks.machineBase, 0);
        GCCoreUtil.registerGalacticraftBlock("solarPanelBasic", GCBlocks.solarPanel, 0);
        GCCoreUtil.registerGalacticraftBlock("solarPanelAdvanced", GCBlocks.solarPanel, 4);
        GCCoreUtil.registerGalacticraftBlock("energyStorageModule", GCBlocks.machineTiered, 0);
        GCCoreUtil.registerGalacticraftBlock("electricFurnace", GCBlocks.machineTiered, 4);
        GCCoreUtil.registerGalacticraftBlock("ingotCompressor", GCBlocks.machineBase, 12);
        GCCoreUtil.registerGalacticraftBlock("circuitFabricator", GCBlocks.machineBase2, 4);
        GCCoreUtil.registerGalacticraftBlock("ingotCompressorElectric", GCBlocks.machineBase2, 0);
        GCCoreUtil.registerGalacticraftBlock("electricArcFurnace", GCBlocks.machineTiered, 12);
        GCCoreUtil.registerGalacticraftBlock("energyStorageCluster", GCBlocks.machineTiered, 8);
        GCCoreUtil.registerGalacticraftBlock("oreCopper", GCBlocks.basicBlock, 5);
        GCCoreUtil.registerGalacticraftBlock("oreTin", GCBlocks.basicBlock, 6);
        GCCoreUtil.registerGalacticraftBlock("oreAluminum", GCBlocks.basicBlock, 7);
        GCCoreUtil.registerGalacticraftBlock("oreSilicon", GCBlocks.basicBlock, 8);
        GCCoreUtil.registerGalacticraftBlock("fallenMeteor", GCBlocks.fallenMeteor);
        GCCoreUtil.registerGalacticraftBlock("torchGlowstone", GCBlocks.glowstoneTorch);
        GCCoreUtil.registerGalacticraftBlock("wireAluminum", GCBlocks.aluminumWire);
        GCCoreUtil.registerGalacticraftBlock("wireAluminumHeavy", GCBlocks.aluminumWire, 1);
        GCCoreUtil.registerGalacticraftBlock("spinThruster", GCBlocks.spinThruster);
        GCCoreUtil.registerGalacticraftBlock("arclamp", GCBlocks.brightLamp);
        GCCoreUtil.registerGalacticraftBlock("treasureChestTier1", GCBlocks.treasureChestTier1);

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

    public static void setHarvestLevels()
    {
        GCBlocks.basicBlock.setHarvestLevel("pickaxe", 2, 5); //Copper ore
        GCBlocks.basicBlock.setHarvestLevel("pickaxe", 2, 6); //Tin ore
        GCBlocks.basicBlock.setHarvestLevel("pickaxe", 2, 7); //Aluminium ore
        GCBlocks.basicBlock.setHarvestLevel("pickaxe", 1, 8); //Silicon ore
        GCBlocks.fallenMeteor.setHarvestLevel("pickaxe", 3);
        GCBlocks.blockMoon.setHarvestLevel("pickaxe", 2, 0); //Copper ore
        GCBlocks.blockMoon.setHarvestLevel("pickaxe", 2, 1); //Tin ore
        GCBlocks.blockMoon.setHarvestLevel("pickaxe", 1, 2); //Cheese ore
        GCBlocks.blockMoon.setHarvestLevel("shovel", 0, 3); //Moon dirt
        GCBlocks.blockMoon.setHarvestLevel("pickaxe", 1, 4); //Moon rock

        for (int num = 5; num < 14; num++)
        {
            //Various types of Moon top dirt
            GCBlocks.blockMoon.setHarvestLevel("shovel", 0, num);
        }

        GCBlocks.blockMoon.setHarvestLevel("pickaxe", 3, 14); //Moon dungeon brick (actually unharvestable)
    }

    public static void registerBlocks()
    {
        GameRegistry.registerBlock(GCBlocks.landingPad, ItemBlockLandingPad.class, GCBlocks.landingPad.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.landingPadFull, ItemBlockGC.class, GCBlocks.landingPadFull.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.unlitTorch, ItemBlockGC.class, GCBlocks.unlitTorch.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.unlitTorchLit, ItemBlockGC.class, GCBlocks.unlitTorchLit.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.breatheableAir, ItemBlockGC.class, GCBlocks.breatheableAir.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.brightAir, ItemBlockGC.class, GCBlocks.brightAir.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.brightBreatheableAir, ItemBlockGC.class, GCBlocks.brightBreatheableAir.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.oxygenCollector, ItemBlockDesc.class, GCBlocks.oxygenCollector.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.oxygenCompressor, ItemBlockOxygenCompressor.class, GCBlocks.oxygenCompressor.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.oxygenDistributor, ItemBlockDesc.class, GCBlocks.oxygenDistributor.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.oxygenSealer, ItemBlockDesc.class, GCBlocks.oxygenSealer.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.oxygenDetector, ItemBlockDesc.class, GCBlocks.oxygenDetector.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.oxygenPipe, ItemBlockDesc.class, GCBlocks.oxygenPipe.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.refinery, ItemBlockDesc.class, GCBlocks.refinery.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.fuelLoader, ItemBlockDesc.class, GCBlocks.fuelLoader.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.cargoLoader, ItemBlockCargoLoader.class, GCBlocks.cargoLoader.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.nasaWorkbench, ItemBlockDesc.class, GCBlocks.nasaWorkbench.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.basicBlock, ItemBlockBase.class, GCBlocks.basicBlock.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.airLockFrame, ItemBlockAirLock.class, GCBlocks.airLockFrame.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.airLockSeal, ItemBlockGC.class, GCBlocks.airLockSeal.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.sealableBlock, ItemBlockEnclosed.class, GCBlocks.sealableBlock.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.spaceStationBase, ItemBlockGC.class, GCBlocks.spaceStationBase.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.fakeBlock, ItemBlockDummy.class, GCBlocks.fakeBlock.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.parachest, ItemBlockDesc.class, GCBlocks.parachest.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.solarPanel, ItemBlockSolar.class, GCBlocks.solarPanel.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.machineBase, ItemBlockMachine.class, GCBlocks.machineBase.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.machineBase2, ItemBlockMachine.class, GCBlocks.machineBase2.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.machineTiered, ItemBlockMachine.class, GCBlocks.machineTiered.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.aluminumWire, ItemBlockAluminumWire.class, GCBlocks.aluminumWire.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.glowstoneTorch, ItemBlockDesc.class, GCBlocks.glowstoneTorch.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.fallenMeteor, ItemBlockDesc.class, GCBlocks.fallenMeteor.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.blockMoon, ItemBlockMoon.class, GCBlocks.blockMoon.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.cheeseBlock, ItemBlockCheese.class, GCBlocks.cheeseBlock.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.spinThruster, ItemBlockThruster.class, GCBlocks.spinThruster.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.brightLamp, ItemBlockArclamp.class, GCBlocks.brightLamp.getUnlocalizedName());
        GameRegistry.registerBlock(GCBlocks.treasureChestTier1, ItemBlockDesc.class, GCBlocks.treasureChestTier1.getUnlocalizedName());
    }
}
