package micdoodle8.mods.galacticraft.core.blocks;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemBlockAirLock;
import micdoodle8.mods.galacticraft.core.items.ItemBlockAluminumWire;
import micdoodle8.mods.galacticraft.core.items.ItemBlockBasic;
import micdoodle8.mods.galacticraft.core.items.ItemBlockCargoLoader;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDummy;
import micdoodle8.mods.galacticraft.core.items.ItemBlockEnclosedBlock;
import micdoodle8.mods.galacticraft.core.items.ItemBlockGC;
import micdoodle8.mods.galacticraft.core.items.ItemBlockLandingPad;
import micdoodle8.mods.galacticraft.core.items.ItemBlockMachine;
import micdoodle8.mods.galacticraft.core.items.ItemBlockMoon;
import micdoodle8.mods.galacticraft.core.items.ItemBlockOxygenCompressor;
import micdoodle8.mods.galacticraft.core.items.ItemBlockSolar;
import micdoodle8.mods.galacticraft.core.util.CoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * GCCoreBlocks.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCBlocks
{
	public static Block breatheableAir;
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
	public static Block aluminumWire;
	public static Block glowstoneTorch;
	public static Block blockMoon;
	public static Block cheeseBlock;

	public static final Material machine = new Material(MapColor.ironColor);

	public static ArrayList<Block> hiddenBlocks = new ArrayList<Block>();

	public static void initBlocks()
	{
		GCBlocks.breatheableAir = new BlockBreathableAir("breatheableAir");
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
		GCBlocks.basicBlock = new BlockGC("gcBlockCore");
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
		GCBlocks.parachest = new BlockParachest("parachest");
		GCBlocks.solarPanel = new BlockSolar("solar");
		GCBlocks.machineBase = new BlockMachine("machine");
		GCBlocks.aluminumWire = new BlockAluminumWire("aluminumWire");
		GCBlocks.machineBase2 = new BlockMachine2("machine2");
		GCBlocks.glowstoneTorch = new BlockGlowstoneTorch("glowstoneTorch");
		GCBlocks.blockMoon = new BlockMoon();
		GCBlocks.cheeseBlock = new BlockCheese().setHardness(0.5F).setStepSound(Block.soundTypeStone).setBlockName("cheeseBlock");

		CoreUtil.registerGalacticraftBlock("rocketLaunchPad", GCBlocks.landingPad, 0);
		CoreUtil.registerGalacticraftBlock("buggyFuelingPad", GCBlocks.landingPad, 1);
		CoreUtil.registerGalacticraftBlock("oxygenDistributor", GCBlocks.oxygenDistributor);
		CoreUtil.registerGalacticraftBlock("oxygenCompressor", GCBlocks.oxygenCompressor);
		CoreUtil.registerGalacticraftBlock("oxygenCollector", GCBlocks.oxygenCollector);
		CoreUtil.registerGalacticraftBlock("refinery", GCBlocks.refinery);
		CoreUtil.registerGalacticraftBlock("fuelLoader", GCBlocks.fuelLoader);
		CoreUtil.registerGalacticraftBlock("oxygenSealer", GCBlocks.oxygenSealer);
		CoreUtil.registerGalacticraftBlock("oxygenDetector", GCBlocks.oxygenDetector);
		CoreUtil.registerGalacticraftBlock("cargoLoader", GCBlocks.cargoLoader, 0);
		CoreUtil.registerGalacticraftBlock("cargoUnloader", GCBlocks.cargoLoader, 4);
		CoreUtil.registerGalacticraftBlock("oxygenPipe", GCBlocks.oxygenPipe);
		CoreUtil.registerGalacticraftBlock("nasaWorkbench", GCBlocks.nasaWorkbench);
		CoreUtil.registerGalacticraftBlock("fallenMeteor", GCBlocks.fallenMeteor);
		CoreUtil.registerGalacticraftBlock("tinDecorationBlock1", GCBlocks.basicBlock, 3);
		CoreUtil.registerGalacticraftBlock("tinDecorationBlock2", GCBlocks.basicBlock, 4);
		CoreUtil.registerGalacticraftBlock("airLockFrame", GCBlocks.airLockFrame);
		CoreUtil.registerGalacticraftBlock("sealableCopperWire", GCBlocks.sealableBlock, 0);
		CoreUtil.registerGalacticraftBlock("sealableOxygenPipe", GCBlocks.sealableBlock, 1);
		CoreUtil.registerGalacticraftBlock("sealableCopperCable", GCBlocks.sealableBlock, 2);
		CoreUtil.registerGalacticraftBlock("sealableGoldCable", GCBlocks.sealableBlock, 3);
		CoreUtil.registerGalacticraftBlock("sealableHighVoltageCable", GCBlocks.sealableBlock, 4);
		CoreUtil.registerGalacticraftBlock("sealableGlassFibreCable", GCBlocks.sealableBlock, 5);
		CoreUtil.registerGalacticraftBlock("sealableLowVoltageCable", GCBlocks.sealableBlock, 6);
		CoreUtil.registerGalacticraftBlock("sealableStonePipeItem", GCBlocks.sealableBlock, 7);
		CoreUtil.registerGalacticraftBlock("sealableCobblestonePipeItem", GCBlocks.sealableBlock, 8);
		CoreUtil.registerGalacticraftBlock("sealableStonePipeFluid", GCBlocks.sealableBlock, 9);
		CoreUtil.registerGalacticraftBlock("sealableCobblestonePipeFluid", GCBlocks.sealableBlock, 10);
		CoreUtil.registerGalacticraftBlock("sealableStonePipePower", GCBlocks.sealableBlock, 11);
		CoreUtil.registerGalacticraftBlock("sealableGoldPipePower", GCBlocks.sealableBlock, 12);
		CoreUtil.registerGalacticraftBlock("treasureChestTier1", GCBlocks.treasureChestTier1);
		CoreUtil.registerGalacticraftBlock("parachest", GCBlocks.parachest);
		CoreUtil.registerGalacticraftBlock("solarPanelBasic", GCBlocks.solarPanel, 0);
		CoreUtil.registerGalacticraftBlock("solarPanelAdvanced", GCBlocks.solarPanel, 4);
		CoreUtil.registerGalacticraftBlock("copperWire", GCBlocks.aluminumWire);
		CoreUtil.registerGalacticraftBlock("coalGenerator", GCBlocks.machineBase, 0);
		CoreUtil.registerGalacticraftBlock("energyStorageModule", GCBlocks.machineBase, 4);
		CoreUtil.registerGalacticraftBlock("electricFurnace", GCBlocks.machineBase, 8);
		CoreUtil.registerGalacticraftBlock("ingotCompressor", GCBlocks.machineBase, 12);
		CoreUtil.registerGalacticraftBlock("ingotCompressorElectric", GCBlocks.machineBase2, 0);
		CoreUtil.registerGalacticraftBlock("circuitFabricator", GCBlocks.machineBase2, 4);
		CoreUtil.registerGalacticraftBlock("oreCopper", GCBlocks.basicBlock, 5);
		CoreUtil.registerGalacticraftBlock("oreTin", GCBlocks.basicBlock, 6);
		CoreUtil.registerGalacticraftBlock("oreAluminum", GCBlocks.basicBlock, 7);
		CoreUtil.registerGalacticraftBlock("oreSilicon", GCBlocks.basicBlock, 8);
		CoreUtil.registerGalacticraftBlock("torchGlowstone", GCBlocks.glowstoneTorch);
		CoreUtil.registerGalacticraftBlock("wireAluminum", GCBlocks.aluminumWire);
		CoreUtil.registerGalacticraftBlock("wireAluminumHeavy", GCBlocks.aluminumWire, 1);

		// Hide certain items from NEI
		GCBlocks.hiddenBlocks.add(GCBlocks.airLockSeal);
		GCBlocks.hiddenBlocks.add(GCBlocks.breatheableAir);
		GCBlocks.hiddenBlocks.add(GCBlocks.unlitTorch);
		GCBlocks.hiddenBlocks.add(GCBlocks.unlitTorchLit);
		GCBlocks.hiddenBlocks.add(GCBlocks.landingPadFull);
		GCBlocks.hiddenBlocks.add(GCBlocks.fakeBlock);
		GCBlocks.hiddenBlocks.add(GCBlocks.spaceStationBase);

		OreDictionary.registerOre("oreCopper", new ItemStack(GCBlocks.basicBlock, 1, 5));
		OreDictionary.registerOre("oreCopper", new ItemStack(GCBlocks.blockMoon, 1, 0));
		OreDictionary.registerOre("oreTin", new ItemStack(GCBlocks.basicBlock, 1, 6));
		OreDictionary.registerOre("oreTin", new ItemStack(GCBlocks.blockMoon, 1, 1));
		OreDictionary.registerOre("oreAluminum", new ItemStack(GCBlocks.basicBlock, 1, 7));
		OreDictionary.registerOre("oreAluminium", new ItemStack(GCBlocks.basicBlock, 1, 7));
		OreDictionary.registerOre("oreNaturalAluminum", new ItemStack(GCBlocks.basicBlock, 1, 7));
		OreDictionary.registerOre("oreSilicon", new ItemStack(GCBlocks.basicBlock, 1, 8));
		OreDictionary.registerOre("oreCheese", new ItemStack(GCBlocks.blockMoon, 1, 2));

		GCBlocks.registerBlocks();
	}

	public static void registerBlocks()
	{
		GameRegistry.registerBlock(GCBlocks.unlitTorch, ItemBlockGC.class, GCBlocks.unlitTorch.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.unlitTorchLit, ItemBlockGC.class, GCBlocks.unlitTorchLit.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.breatheableAir, ItemBlockGC.class, GCBlocks.breatheableAir.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.fallenMeteor, ItemBlockGC.class, GCBlocks.fallenMeteor.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.basicBlock, ItemBlockBasic.class, GCBlocks.basicBlock.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.spaceStationBase, ItemBlockGC.class, GCBlocks.spaceStationBase.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.fakeBlock, ItemBlockDummy.class, GCBlocks.fakeBlock.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.nasaWorkbench, ItemBlockGC.class, GCBlocks.nasaWorkbench.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.treasureChestTier1, ItemBlockGC.class, GCBlocks.treasureChestTier1.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.parachest, ItemBlockGC.class, GCBlocks.parachest.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.airLockFrame, ItemBlockAirLock.class, GCBlocks.airLockFrame.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.airLockSeal, ItemBlockGC.class, GCBlocks.airLockSeal.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.fuelLoader, ItemBlockGC.class, GCBlocks.fuelLoader.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.cargoLoader, ItemBlockCargoLoader.class, GCBlocks.cargoLoader.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.landingPad, ItemBlockLandingPad.class, GCBlocks.landingPad.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.landingPadFull, ItemBlockGC.class, GCBlocks.landingPadFull.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.solarPanel, ItemBlockSolar.class, GCBlocks.solarPanel.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.refinery, ItemBlockGC.class, GCBlocks.refinery.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.machineBase, ItemBlockMachine.class, GCBlocks.machineBase.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.machineBase2, ItemBlockMachine.class, GCBlocks.machineBase2.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.oxygenSealer, ItemBlockGC.class, GCBlocks.oxygenSealer.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.oxygenDetector, ItemBlockGC.class, GCBlocks.oxygenDetector.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.oxygenDistributor, ItemBlockGC.class, GCBlocks.oxygenDistributor.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.oxygenPipe, ItemBlockGC.class, GCBlocks.oxygenPipe.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.oxygenCollector, ItemBlockGC.class, GCBlocks.oxygenCollector.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.oxygenCompressor, ItemBlockOxygenCompressor.class, GCBlocks.oxygenCompressor.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.aluminumWire, ItemBlockAluminumWire.class, GCBlocks.aluminumWire.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.sealableBlock, ItemBlockEnclosedBlock.class, GCBlocks.sealableBlock.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.glowstoneTorch, ItemBlockGC.class, GCBlocks.glowstoneTorch.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.blockMoon, ItemBlockMoon.class, GCBlocks.blockMoon.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCBlocks.cheeseBlock, ItemBlock.class, GCBlocks.cheeseBlock.getUnlocalizedName(), GalacticraftCore.MOD_ID);
	}
}
