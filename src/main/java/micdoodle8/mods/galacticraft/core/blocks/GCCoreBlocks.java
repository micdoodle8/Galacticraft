package micdoodle8.mods.galacticraft.core.blocks;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBlock;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBlockAirLock;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBlockAluminumWire;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBlockBase;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBlockCargoLoader;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBlockDummy;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBlockEnclosedBlock;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBlockLandingPad;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBlockMachine;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBlockOxygenCompressor;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBlockSolar;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
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
public class GCCoreBlocks
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
//	public static Block blockMoon;
//	public static Block cheeseBlock;

	public static final Material machine = new Material(MapColor.ironColor);

	public static ArrayList<Block> hiddenBlocks = new ArrayList<Block>();

	public static void initBlocks()
	{
		GCCoreBlocks.breatheableAir = new GCCoreBlockBreathableAir("breatheableAir");
		GCCoreBlocks.treasureChestTier1 = new GCCoreBlockT1TreasureChest("treasureChest");
		GCCoreBlocks.landingPad = new GCCoreBlockLandingPad("landingPad");
		GCCoreBlocks.landingPadFull = new GCCoreBlockLandingPadFull("landingPadFull");
		GCCoreBlocks.unlitTorch = new GCCoreBlockUnlitTorch(false, "unlitTorch");
		GCCoreBlocks.unlitTorchLit = new GCCoreBlockUnlitTorch(true, "unlitTorchLit");
		GCCoreBlocks.oxygenDistributor = new GCCoreBlockOxygenDistributor("distributor");
		GCCoreBlocks.oxygenPipe = new GCCoreBlockOxygenPipe("oxygenPipe");
		GCCoreBlocks.oxygenCollector = new GCCoreBlockOxygenCollector("oxygenCollector");
		GCCoreBlocks.nasaWorkbench = new GCCoreBlockAdvancedCraftingTable("rocketWorkbench");
		GCCoreBlocks.fallenMeteor = new GCCoreBlockFallenMeteor("fallenMeteor");
		GCCoreBlocks.basicBlock = new GCCoreBlock("gcBlockCore");
		GCCoreBlocks.airLockFrame = new GCCoreBlockAirLockFrame("airLockFrame");
		GCCoreBlocks.airLockSeal = new GCCoreBlockAirLockWall("airLockSeal");
		GCCoreBlocks.refinery = new GCCoreBlockRefinery("refinery");
		GCCoreBlocks.oxygenCompressor = new GCCoreBlockOxygenCompressor(false, "oxygenCompressor");
		GCCoreBlocks.fuelLoader = new GCCoreBlockFuelLoader("fuelLoader");
		GCCoreBlocks.spaceStationBase = new GCCoreBlockSpaceStationBase("spaceStationBase");
		GCCoreBlocks.fakeBlock = new GCCoreBlockMulti("dummyblock");
		GCCoreBlocks.oxygenSealer = new GCCoreBlockOxygenSealer("sealer");
		GCCoreBlocks.sealableBlock = new GCCoreBlockEnclosed("enclosed");
		GCCoreBlocks.oxygenDetector = new GCCoreBlockOxygenDetector("oxygenDetector");
		GCCoreBlocks.cargoLoader = new GCCoreBlockCargoLoader("cargo");
		GCCoreBlocks.parachest = new GCCoreBlockParachest("parachest");
		GCCoreBlocks.solarPanel = new GCCoreBlockSolar("solar");
		GCCoreBlocks.machineBase = new GCCoreBlockMachine("machine");
		GCCoreBlocks.aluminumWire = new GCCoreBlockAluminumWire("aluminumWire");
		GCCoreBlocks.machineBase2 = new GCCoreBlockMachine2("machine2");
		GCCoreBlocks.glowstoneTorch = new GCCoreBlockGlowstoneTorch("glowstoneTorch");
//		GCCoreBlocks.blockMoon = new GCMoonBlock();
//		GCCoreBlocks.cheeseBlock = new GCMoonBlockCheese().setHardness(0.5F).setStepSound(Block.soundTypeStone).setBlockName("cheeseBlock");

//		GCCoreUtil.registerGalacticraftBlock("rocketLaunchPad", GCCoreBlocks.landingPad, 0);
//		GCCoreUtil.registerGalacticraftBlock("buggyFuelingPad", GCCoreBlocks.landingPad, 1);
//		GCCoreUtil.registerGalacticraftBlock("oxygenDistributor", GCCoreBlocks.oxygenDistributor);
//		GCCoreUtil.registerGalacticraftBlock("oxygenCompressor", GCCoreBlocks.oxygenCompressor);
//		GCCoreUtil.registerGalacticraftBlock("oxygenCollector", GCCoreBlocks.oxygenCollector);
//		GCCoreUtil.registerGalacticraftBlock("refinery", GCCoreBlocks.refinery);
//		GCCoreUtil.registerGalacticraftBlock("fuelLoader", GCCoreBlocks.fuelLoader);
//		GCCoreUtil.registerGalacticraftBlock("oxygenSealer", GCCoreBlocks.oxygenSealer);
//		GCCoreUtil.registerGalacticraftBlock("oxygenDetector", GCCoreBlocks.oxygenDetector);
//		GCCoreUtil.registerGalacticraftBlock("cargoLoader", GCCoreBlocks.cargoLoader, 0);
//		GCCoreUtil.registerGalacticraftBlock("cargoUnloader", GCCoreBlocks.cargoLoader, 4);
//		GCCoreUtil.registerGalacticraftBlock("oxygenPipe", GCCoreBlocks.oxygenPipe);
//		GCCoreUtil.registerGalacticraftBlock("nasaWorkbench", GCCoreBlocks.nasaWorkbench);
//		GCCoreUtil.registerGalacticraftBlock("fallenMeteor", GCCoreBlocks.fallenMeteor);
//		GCCoreUtil.registerGalacticraftBlock("tinDecorationBlock1", GCCoreBlocks.basicBlock, 3);
//		GCCoreUtil.registerGalacticraftBlock("tinDecorationBlock2", GCCoreBlocks.basicBlock, 4);
//		GCCoreUtil.registerGalacticraftBlock("airLockFrame", GCCoreBlocks.airLockFrame);
//		GCCoreUtil.registerGalacticraftBlock("sealableCopperWire", GCCoreBlocks.sealableBlock, 0);
//		GCCoreUtil.registerGalacticraftBlock("sealableOxygenPipe", GCCoreBlocks.sealableBlock, 1);
//		GCCoreUtil.registerGalacticraftBlock("sealableCopperCable", GCCoreBlocks.sealableBlock, 2);
//		GCCoreUtil.registerGalacticraftBlock("sealableGoldCable", GCCoreBlocks.sealableBlock, 3);
//		GCCoreUtil.registerGalacticraftBlock("sealableHighVoltageCable", GCCoreBlocks.sealableBlock, 4);
//		GCCoreUtil.registerGalacticraftBlock("sealableGlassFibreCable", GCCoreBlocks.sealableBlock, 5);
//		GCCoreUtil.registerGalacticraftBlock("sealableLowVoltageCable", GCCoreBlocks.sealableBlock, 6);
//		GCCoreUtil.registerGalacticraftBlock("sealableStonePipeItem", GCCoreBlocks.sealableBlock, 7);
//		GCCoreUtil.registerGalacticraftBlock("sealableCobblestonePipeItem", GCCoreBlocks.sealableBlock, 8);
//		GCCoreUtil.registerGalacticraftBlock("sealableStonePipeFluid", GCCoreBlocks.sealableBlock, 9);
//		GCCoreUtil.registerGalacticraftBlock("sealableCobblestonePipeFluid", GCCoreBlocks.sealableBlock, 10);
//		GCCoreUtil.registerGalacticraftBlock("sealableStonePipePower", GCCoreBlocks.sealableBlock, 11);
//		GCCoreUtil.registerGalacticraftBlock("sealableGoldPipePower", GCCoreBlocks.sealableBlock, 12);
//		GCCoreUtil.registerGalacticraftBlock("treasureChestTier1", GCCoreBlocks.treasureChestTier1);
//		GCCoreUtil.registerGalacticraftBlock("parachest", GCCoreBlocks.parachest);
//		GCCoreUtil.registerGalacticraftBlock("solarPanelBasic", GCCoreBlocks.solarPanel, 0);
//		GCCoreUtil.registerGalacticraftBlock("solarPanelAdvanced", GCCoreBlocks.solarPanel, 4);
//		GCCoreUtil.registerGalacticraftBlock("copperWire", GCCoreBlocks.aluminumWire);
//		GCCoreUtil.registerGalacticraftBlock("coalGenerator", GCCoreBlocks.machineBase, 0);
//		GCCoreUtil.registerGalacticraftBlock("energyStorageModule", GCCoreBlocks.machineBase, 4);
//		GCCoreUtil.registerGalacticraftBlock("electricFurnace", GCCoreBlocks.machineBase, 8);
//		GCCoreUtil.registerGalacticraftBlock("ingotCompressor", GCCoreBlocks.machineBase, 12);
//		GCCoreUtil.registerGalacticraftBlock("ingotCompressorElectric", GCCoreBlocks.machineBase2, 0);
//		GCCoreUtil.registerGalacticraftBlock("circuitFabricator", GCCoreBlocks.machineBase2, 4);
//		GCCoreUtil.registerGalacticraftBlock("oreCopper", GCCoreBlocks.basicBlock, 5);
//		GCCoreUtil.registerGalacticraftBlock("oreTin", GCCoreBlocks.basicBlock, 6);
//		GCCoreUtil.registerGalacticraftBlock("oreAluminum", GCCoreBlocks.basicBlock, 7);
//		GCCoreUtil.registerGalacticraftBlock("oreSilicon", GCCoreBlocks.basicBlock, 8);
//		GCCoreUtil.registerGalacticraftBlock("torchGlowstone", GCCoreBlocks.glowstoneTorch);
//		GCCoreUtil.registerGalacticraftBlock("wireAluminum", GCCoreBlocks.aluminumWire);
//		GCCoreUtil.registerGalacticraftBlock("wireAluminumHeavy", GCCoreBlocks.aluminumWire, 1);

		// Hide certain items from NEI
		GCCoreBlocks.hiddenBlocks.add(GCCoreBlocks.airLockSeal);
		GCCoreBlocks.hiddenBlocks.add(GCCoreBlocks.breatheableAir);
		GCCoreBlocks.hiddenBlocks.add(GCCoreBlocks.unlitTorch);
		GCCoreBlocks.hiddenBlocks.add(GCCoreBlocks.unlitTorchLit);
		GCCoreBlocks.hiddenBlocks.add(GCCoreBlocks.landingPadFull);
		GCCoreBlocks.hiddenBlocks.add(GCCoreBlocks.fakeBlock);
		GCCoreBlocks.hiddenBlocks.add(GCCoreBlocks.spaceStationBase);

		OreDictionary.registerOre("oreCopper", new ItemStack(GCCoreBlocks.basicBlock, 1, 5));
//		OreDictionary.registerOre("oreCopper", new ItemStack(GCCoreBlocks.blockMoon, 1, 0));
		OreDictionary.registerOre("oreTin", new ItemStack(GCCoreBlocks.basicBlock, 1, 6));
//		OreDictionary.registerOre("oreTin", new ItemStack(GCCoreBlocks.blockMoon, 1, 1));
		OreDictionary.registerOre("oreAluminum", new ItemStack(GCCoreBlocks.basicBlock, 1, 7));
		OreDictionary.registerOre("oreAluminium", new ItemStack(GCCoreBlocks.basicBlock, 1, 7));
		OreDictionary.registerOre("oreNaturalAluminum", new ItemStack(GCCoreBlocks.basicBlock, 1, 7));
		OreDictionary.registerOre("oreSilicon", new ItemStack(GCCoreBlocks.basicBlock, 1, 8));
//		OreDictionary.registerOre("oreCheese", new ItemStack(GCCoreBlocks.blockMoon, 1, 2));

		GCCoreBlocks.setHarvestLevels();
		GCCoreBlocks.registerBlocks();
	}

	public static void setHarvestLevels()
	{
//		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.basicBlock, 0, "pickaxe", 1);
//		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.basicBlock, 1, "pickaxe", 2);
//		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.basicBlock, 2, "pickaxe", 3);
//		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.fallenMeteor, "pickaxe", 3);
//		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.basicBlock, 5, "pickaxe", 1);
//		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.basicBlock, 6, "pickaxe", 1);
//		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.basicBlock, 7, "pickaxe", 2);
//		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.basicBlock, 8, "pickaxe", 2);
//		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.blockMoon, 0, "pickaxe", 3);
//		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.blockMoon, 1, "pickaxe", 3);
//		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.blockMoon, 2, "pickaxe", 3);
//		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.blockMoon, 3, "shovel", 0);
//		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.blockMoon, 4, "pickaxe", 2);
//
//		for (int num = 5; num < 14; num++)
//		{
//			MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.blockMoon, num, "shovel", 0);
//		}
//
//		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.blockMoon, 14, "pickaxe", 2);
	}

	public static void registerBlocks()
	{
		GameRegistry.registerBlock(GCCoreBlocks.treasureChestTier1, GCCoreItemBlock.class, GCCoreBlocks.treasureChestTier1.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.landingPad, GCCoreItemBlockLandingPad.class, GCCoreBlocks.landingPad.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.landingPadFull, GCCoreItemBlock.class, GCCoreBlocks.landingPadFull.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.unlitTorch, GCCoreItemBlock.class, GCCoreBlocks.unlitTorch.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.unlitTorchLit, GCCoreItemBlock.class, GCCoreBlocks.unlitTorchLit.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.breatheableAir, GCCoreItemBlock.class, GCCoreBlocks.breatheableAir.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.oxygenDistributor, GCCoreItemBlock.class, GCCoreBlocks.oxygenDistributor.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.oxygenPipe, GCCoreItemBlock.class, GCCoreBlocks.oxygenPipe.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.oxygenCollector, GCCoreItemBlock.class, GCCoreBlocks.oxygenCollector.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.nasaWorkbench, GCCoreItemBlock.class, GCCoreBlocks.nasaWorkbench.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.fallenMeteor, GCCoreItemBlock.class, GCCoreBlocks.fallenMeteor.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.airLockFrame, GCCoreItemBlockAirLock.class, GCCoreBlocks.airLockFrame.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.airLockSeal, GCCoreItemBlock.class, GCCoreBlocks.airLockSeal.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.refinery, GCCoreItemBlock.class, GCCoreBlocks.refinery.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.oxygenCompressor, GCCoreItemBlockOxygenCompressor.class, GCCoreBlocks.oxygenCompressor.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.basicBlock, GCCoreItemBlockBase.class, GCCoreBlocks.basicBlock.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.fuelLoader, GCCoreItemBlock.class, GCCoreBlocks.fuelLoader.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.spaceStationBase, GCCoreItemBlock.class, GCCoreBlocks.spaceStationBase.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.fakeBlock, GCCoreItemBlockDummy.class, GCCoreBlocks.fakeBlock.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.oxygenSealer, GCCoreItemBlock.class, GCCoreBlocks.oxygenSealer.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.sealableBlock, GCCoreItemBlockEnclosedBlock.class, GCCoreBlocks.sealableBlock.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.oxygenDetector, GCCoreItemBlock.class, GCCoreBlocks.oxygenDetector.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.cargoLoader, GCCoreItemBlockCargoLoader.class, GCCoreBlocks.cargoLoader.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.parachest, GCCoreItemBlock.class, GCCoreBlocks.parachest.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.solarPanel, GCCoreItemBlockSolar.class, GCCoreBlocks.solarPanel.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.machineBase, GCCoreItemBlockMachine.class, GCCoreBlocks.machineBase.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.machineBase2, GCCoreItemBlockMachine.class, GCCoreBlocks.machineBase2.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.aluminumWire, GCCoreItemBlockAluminumWire.class, GCCoreBlocks.aluminumWire.getUnlocalizedName(), GalacticraftCore.MOD_ID);
		GameRegistry.registerBlock(GCCoreBlocks.glowstoneTorch, GCCoreItemBlock.class, GCCoreBlocks.glowstoneTorch.getUnlocalizedName(), GalacticraftCore.MOD_ID);
//		GameRegistry.registerBlock(GCCoreBlocks.blockMoon, GCMoonItemBlock.class, GCCoreBlocks.blockMoon.getUnlocalizedName(), GalacticraftCore.MOD_ID);
//		GameRegistry.registerBlock(GCCoreBlocks.cheeseBlock, ItemBlock.class, GCCoreBlocks.cheeseBlock.getUnlocalizedName(), GalacticraftCore.MOD_ID);
	}
}
