package micdoodle8.mods.galacticraft.core.blocks;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemBlockAirLock;
import micdoodle8.mods.galacticraft.core.items.ItemBlockAluminumWire;
import micdoodle8.mods.galacticraft.core.items.ItemBlockBase;
import micdoodle8.mods.galacticraft.core.items.ItemBlockCargoLoader;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDummy;
import micdoodle8.mods.galacticraft.core.items.ItemBlockEnclosed;
import micdoodle8.mods.galacticraft.core.items.ItemBlockGC;
import micdoodle8.mods.galacticraft.core.items.ItemBlockLandingPad;
import micdoodle8.mods.galacticraft.core.items.ItemBlockMachine;
import micdoodle8.mods.galacticraft.core.items.ItemBlockMoon;
import micdoodle8.mods.galacticraft.core.items.ItemBlockOxygenCompressor;
import micdoodle8.mods.galacticraft.core.items.ItemBlockSolar;
import micdoodle8.mods.galacticraft.core.items.ItemBlockThruster;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
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
	public static Block spinThruster;

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
		GCBlocks.aluminumWire = new BlockAluminumWire("aluminumWire");
		GCBlocks.machineBase2 = new BlockMachine2("machine2");
		GCBlocks.glowstoneTorch = new BlockGlowstoneTorch("glowstoneTorch");
		GCBlocks.blockMoon = new BlockBasicMoon();
		GCBlocks.cheeseBlock = new BlockCheese();
		GCBlocks.spinThruster = new BlockSpinThruster("spinThruster");

		GCCoreUtil.registerGalacticraftBlock("rocketLaunchPad", GCBlocks.landingPad, 0);
		GCCoreUtil.registerGalacticraftBlock("buggyFuelingPad", GCBlocks.landingPad, 1);
		GCCoreUtil.registerGalacticraftBlock("oxygenDistributor", GCBlocks.oxygenDistributor);
		GCCoreUtil.registerGalacticraftBlock("oxygenCompressor", GCBlocks.oxygenCompressor);
		GCCoreUtil.registerGalacticraftBlock("oxygenCollector", GCBlocks.oxygenCollector);
		GCCoreUtil.registerGalacticraftBlock("refinery", GCBlocks.refinery);
		GCCoreUtil.registerGalacticraftBlock("fuelLoader", GCBlocks.fuelLoader);
		GCCoreUtil.registerGalacticraftBlock("oxygenSealer", GCBlocks.oxygenSealer);
		GCCoreUtil.registerGalacticraftBlock("oxygenDetector", GCBlocks.oxygenDetector);
		GCCoreUtil.registerGalacticraftBlock("cargoLoader", GCBlocks.cargoLoader, 0);
		GCCoreUtil.registerGalacticraftBlock("cargoUnloader", GCBlocks.cargoLoader, 4);
		GCCoreUtil.registerGalacticraftBlock("oxygenPipe", GCBlocks.oxygenPipe);
		GCCoreUtil.registerGalacticraftBlock("nasaWorkbench", GCBlocks.nasaWorkbench);
		GCCoreUtil.registerGalacticraftBlock("fallenMeteor", GCBlocks.fallenMeteor);
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
		GCCoreUtil.registerGalacticraftBlock("treasureChestTier1", GCBlocks.treasureChestTier1);
		GCCoreUtil.registerGalacticraftBlock("parachest", GCBlocks.parachest);
		GCCoreUtil.registerGalacticraftBlock("solarPanelBasic", GCBlocks.solarPanel, 0);
		GCCoreUtil.registerGalacticraftBlock("solarPanelAdvanced", GCBlocks.solarPanel, 4);
		GCCoreUtil.registerGalacticraftBlock("copperWire", GCBlocks.aluminumWire);
		GCCoreUtil.registerGalacticraftBlock("coalGenerator", GCBlocks.machineBase, 0);
		GCCoreUtil.registerGalacticraftBlock("energyStorageModule", GCBlocks.machineBase, 4);
		GCCoreUtil.registerGalacticraftBlock("electricFurnace", GCBlocks.machineBase, 8);
		GCCoreUtil.registerGalacticraftBlock("ingotCompressor", GCBlocks.machineBase, 12);
		GCCoreUtil.registerGalacticraftBlock("ingotCompressorElectric", GCBlocks.machineBase2, 0);
		GCCoreUtil.registerGalacticraftBlock("circuitFabricator", GCBlocks.machineBase2, 4);
		GCCoreUtil.registerGalacticraftBlock("oreCopper", GCBlocks.basicBlock, 5);
		GCCoreUtil.registerGalacticraftBlock("oreTin", GCBlocks.basicBlock, 6);
		GCCoreUtil.registerGalacticraftBlock("oreAluminum", GCBlocks.basicBlock, 7);
		GCCoreUtil.registerGalacticraftBlock("oreSilicon", GCBlocks.basicBlock, 8);
		GCCoreUtil.registerGalacticraftBlock("torchGlowstone", GCBlocks.glowstoneTorch);
		GCCoreUtil.registerGalacticraftBlock("wireAluminum", GCBlocks.aluminumWire);
		GCCoreUtil.registerGalacticraftBlock("wireAluminumHeavy", GCBlocks.aluminumWire, 1);
		GCCoreUtil.registerGalacticraftBlock("spinThruster", GCBlocks.spinThruster);
		
		// Hide certain items from NEI
		GCBlocks.hiddenBlocks.add(GCBlocks.airLockSeal);
		GCBlocks.hiddenBlocks.add(GCBlocks.breatheableAir);
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
//		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.blockMoon, 14, "pickaxe", 2); TODO Harvest levels
	}

	public static void registerBlocks()
	{
		GameRegistry.registerBlock(GCBlocks.treasureChestTier1, ItemBlockGC.class, GCBlocks.treasureChestTier1.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.landingPad, ItemBlockLandingPad.class, GCBlocks.landingPad.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.landingPadFull, ItemBlockGC.class, GCBlocks.landingPadFull.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.unlitTorch, ItemBlockGC.class, GCBlocks.unlitTorch.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.unlitTorchLit, ItemBlockGC.class, GCBlocks.unlitTorchLit.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.breatheableAir, ItemBlockGC.class, GCBlocks.breatheableAir.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.oxygenDistributor, ItemBlockGC.class, GCBlocks.oxygenDistributor.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.oxygenPipe, ItemBlockGC.class, GCBlocks.oxygenPipe.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.oxygenCollector, ItemBlockGC.class, GCBlocks.oxygenCollector.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.nasaWorkbench, ItemBlockGC.class, GCBlocks.nasaWorkbench.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.fallenMeteor, ItemBlockGC.class, GCBlocks.fallenMeteor.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.airLockFrame, ItemBlockAirLock.class, GCBlocks.airLockFrame.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.airLockSeal, ItemBlockGC.class, GCBlocks.airLockSeal.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.refinery, ItemBlockGC.class, GCBlocks.refinery.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.oxygenCompressor, ItemBlockOxygenCompressor.class, GCBlocks.oxygenCompressor.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.basicBlock, ItemBlockBase.class, GCBlocks.basicBlock.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.fuelLoader, ItemBlockGC.class, GCBlocks.fuelLoader.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.spaceStationBase, ItemBlockGC.class, GCBlocks.spaceStationBase.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.fakeBlock, ItemBlockDummy.class, GCBlocks.fakeBlock.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.oxygenSealer, ItemBlockGC.class, GCBlocks.oxygenSealer.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.sealableBlock, ItemBlockEnclosed.class, GCBlocks.sealableBlock.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.oxygenDetector, ItemBlockGC.class, GCBlocks.oxygenDetector.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.cargoLoader, ItemBlockCargoLoader.class, GCBlocks.cargoLoader.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.parachest, ItemBlockGC.class, GCBlocks.parachest.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.solarPanel, ItemBlockSolar.class, GCBlocks.solarPanel.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.machineBase, ItemBlockMachine.class, GCBlocks.machineBase.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.machineBase2, ItemBlockMachine.class, GCBlocks.machineBase2.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.aluminumWire, ItemBlockAluminumWire.class, GCBlocks.aluminumWire.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.glowstoneTorch, ItemBlockGC.class, GCBlocks.glowstoneTorch.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.blockMoon, ItemBlockMoon.class, GCBlocks.blockMoon.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.cheeseBlock, ItemBlock.class, GCBlocks.cheeseBlock.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCBlocks.spinThruster, ItemBlockThruster.class, GCBlocks.spinThruster.getUnlocalizedName(), GalacticraftCore.MODID);	}
}
