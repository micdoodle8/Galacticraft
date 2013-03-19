package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBlockBase;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemSapling;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.Icon;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.prefab.multiblock.BlockMulti;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreBlocks
{
	public static GCCoreBlockBreathableAir breatheableAir;
	public static Block treasureChest;
	public static Block landingPad;
	public static GCCoreBlockUnlitTorch unlitTorch;
	public static GCCoreBlockUnlitTorch unlitTorchLit;
	public static GCCoreBlockOxygenDistributor airDistributor;
	public static GCCoreBlockOxygenPipe oxygenPipe;
	public static Block blockAirCollector;
	public static GCCoreBlockSapling sapling;
	public static Block rocketBench;
	public static Block fallenMeteor;
	public static Block decorationBlocks;
	public static Block airLockFrame;
	public static Block airLockSeal;
	public static GCCoreBlockCrudeOilMoving crudeOilMoving;
	public static GCCoreBlockCrudeOilStationary crudeOilStill;
	public static Block refinery;
	public static Block compressor;
	public static Block fuelLoader;
	public static Block landingPadFull;
	public static Block spaceStationBase;
	public static GCCoreBlockMulti dummyBlock;
	
	public static Icon[] blockIcons;

	public static void initBlocks()
	{
		GCCoreBlocks.breatheableAir = (GCCoreBlockBreathableAir)			new GCCoreBlockBreathableAir		(GCCoreConfigManager.idBlockBreatheableAir)							.setHardness(0.0F) 		.setResistance(1000F)																.setUnlocalizedName("breatheableAir");
		GCCoreBlocks.treasureChest = 										new GCCoreBlockTreasureChest		(GCCoreConfigManager.idBlockTreasureChest)							.setHardness(1.0F)      .setResistance(10.0F)      	.setStepSound(Block.soundStoneFootstep)                 .setUnlocalizedName("treasureChest");
		GCCoreBlocks.landingPad = 											new GCCoreBlockLandingPad			(GCCoreConfigManager.idBlockLandingPad)								.setHardness(1.0F)      .setResistance(10.0F)  	   	.setStepSound(Block.soundStoneFootstep)                 .setUnlocalizedName("landingPad");
		GCCoreBlocks.landingPadFull = 										new GCCoreBlockLandingPadFull		(GCCoreConfigManager.idBlockLandingPadFull)							.setHardness(1.0F)      .setResistance(10.0F)  	   	.setStepSound(Block.soundStoneFootstep)                 .setUnlocalizedName("landingPadFull");
		GCCoreBlocks.unlitTorch = (GCCoreBlockUnlitTorch) 					new GCCoreBlockUnlitTorch 			(GCCoreConfigManager.idBlockUnlitTorch, false)						.setHardness(0.0F)									.setStepSound(Block.soundWoodFootstep)					.setUnlocalizedName("unlitTorch");
		GCCoreBlocks.unlitTorchLit = (GCCoreBlockUnlitTorch) 				new GCCoreBlockUnlitTorch 			(GCCoreConfigManager.idBlockUnlitTorchLit, true)					.setHardness(0.0F)									.setStepSound(Block.soundWoodFootstep)					.setUnlocalizedName("unlitTorchLit");
		GCCoreBlocks.airDistributor = (GCCoreBlockOxygenDistributor)		new GCCoreBlockOxygenDistributor	(GCCoreConfigManager.idBlockAirDistributor, false)					.setHardness(3.5F)									.setStepSound(Block.soundStoneFootstep)					.setUnlocalizedName("distributor");
		GCCoreBlocks.oxygenPipe = (GCCoreBlockOxygenPipe)					new GCCoreBlockOxygenPipe			(GCCoreConfigManager.idBlockAirPipe)								.setHardness(0.3F)								  	.setStepSound(Block.soundGlassFootstep)                 .setUnlocalizedName("oxygenPipe");
		GCCoreBlocks.blockAirCollector = 									new GCCoreBlockOxygenCollector		(GCCoreConfigManager.idBlockAirCollector)							.setHardness(3.5F)  	  						 	.setStepSound(Block.soundStoneFootstep)					.setUnlocalizedName("oxygenCollector");
		GCCoreBlocks.sapling = (GCCoreBlockSapling)							new GCCoreBlockSapling				(GCCoreConfigManager.idBlockSapling2)								.setHardness(0.0F)									.setStepSound(Block.soundGrassFootstep)					.setUnlocalizedName("sapling2");
		GCCoreBlocks.rocketBench = 											new GCCoreBlockAdvancedCraftingTable(GCCoreConfigManager.idBlockRocketBench)							.setHardness(2.5F)									.setStepSound(Block.soundMetalFootstep)					.setUnlocalizedName("rocketWorkbench");
		GCCoreBlocks.fallenMeteor = 										new GCCoreBlockFallenMeteor			(GCCoreConfigManager.idBlockFallenMeteor)							.setHardness(50.0F)									.setStepSound(Block.soundStoneFootstep)					.setUnlocalizedName("fallenMeteor");
		GCCoreBlocks.decorationBlocks = 									new GCCoreBlock						(GCCoreConfigManager.idBlockDecorationBlock)						.setHardness(3.0F) 																							.setUnlocalizedName("decorationblock");
		GCCoreBlocks.airLockFrame = 										new GCCoreBlockAirLockFrame			(GCCoreConfigManager.idBlockAirLockFrame)							.setHardness(3.0F)                        			.setStepSound(Block.soundMetalFootstep)	   				.setUnlocalizedName("airLockFrame");
		GCCoreBlocks.airLockSeal = 											new GCCoreBlockAirLockWall			(GCCoreConfigManager.idBlockAirLockSeal)						    .setHardness(100.0F)                    			.setStepSound(Block.soundMetalFootstep)	   				.setUnlocalizedName("airLockSeal");
		GCCoreBlocks.crudeOilStill = (GCCoreBlockCrudeOilStationary)		new GCCoreBlockCrudeOilStationary	(GCCoreConfigManager.idBlockCrudeOilStill, Material.water)			.setHardness(3.0F)                        																	.setUnlocalizedName("crudeOilStill");
		GCCoreBlocks.crudeOilMoving = (GCCoreBlockCrudeOilMoving)			new GCCoreBlockCrudeOilMoving		(GCCoreConfigManager.idBlockCrudeOilMoving, Material.water)			.setHardness(3.0F)                        																	.setUnlocalizedName("crudeOilMoving");
		GCCoreBlocks.refinery = 											new GCCoreBlockRefinery 			(GCCoreConfigManager.idBlockRefinery, 0)                          	.setHardness(3.0F)                              	.setStepSound(Block.soundMetalFootstep)					.setUnlocalizedName("refinery");
		GCCoreBlocks.compressor = 											new GCCoreBlockOxygenCompressor		(GCCoreConfigManager.idBlockAirCompressor, false)					.setHardness(3.5F)  	  						 	.setStepSound(Block.soundStoneFootstep)					.setUnlocalizedName("oxygenCompressor");
		GCCoreBlocks.fuelLoader = 											new GCCoreBlockFuelLoader			(GCCoreConfigManager.idBlockFuelLoader)								.setHardness(3.0F)       							.setStepSound(Block.soundMetalFootstep)					.setUnlocalizedName("fuelLoader");
		GCCoreBlocks.spaceStationBase = 									new GCCoreBlockSpaceStationBase		(GCCoreConfigManager.idBlockSpaceStationBase)						.setHardness(3.0F)       							.setStepSound(Block.soundMetalFootstep)					.setUnlocalizedName("spaceStationBase");
		GCCoreBlocks.dummyBlock = (GCCoreBlockMulti) 						new GCCoreBlockMulti				(GCCoreConfigManager.idBlockDummy)																						.setStepSound(Block.soundMetalFootstep)					.setUnlocalizedName("dummyblock");
		
		// Hide certain items from NEI
		GalacticraftCore.hiddenItems.add(airLockSeal.blockID);
		GalacticraftCore.hiddenItems.add(sapling.blockID);
		GalacticraftCore.hiddenItems.add(breatheableAir.blockID);
		GalacticraftCore.hiddenItems.add(unlitTorch.blockID);
		GalacticraftCore.hiddenItems.add(unlitTorchLit.blockID);
		GalacticraftCore.hiddenItems.add(landingPadFull.blockID);
		GalacticraftCore.hiddenItems.add(dummyBlock.blockID);
		GalacticraftCore.hiddenItems.add(spaceStationBase.blockID);
	}

	public static void setHarvestLevels()
	{
		//									Block							Meta	Tool		Harvest Level
		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.decorationBlocks, 	0, 		"pickaxe", 	1);
		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.decorationBlocks, 	1, 		"pickaxe", 	2);
		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.decorationBlocks, 	2, 		"pickaxe", 	3);
		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.fallenMeteor, 				"pickaxe", 	3);
	}

	public static void registerBlocks()
	{
		//							Block							ItemBlock class				Block Name												Mod ID
		GameRegistry.registerBlock(GCCoreBlocks.treasureChest, 		ItemBlock.class, 			GCCoreBlocks.treasureChest.getUnlocalizedName(), 		GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCCoreBlocks.landingPad, 		ItemBlock.class, 			GCCoreBlocks.landingPad.getUnlocalizedName(), 			GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCCoreBlocks.landingPadFull, 	ItemBlock.class, 			GCCoreBlocks.landingPadFull.getUnlocalizedName(), 		GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCCoreBlocks.unlitTorch, 		ItemBlock.class, 			GCCoreBlocks.unlitTorch.getUnlocalizedName(), 			GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCCoreBlocks.unlitTorchLit, 		ItemBlock.class, 			GCCoreBlocks.unlitTorchLit.getUnlocalizedName(), 		GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCCoreBlocks.breatheableAir, 	ItemBlock.class, 			GCCoreBlocks.breatheableAir.getUnlocalizedName(), 		GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCCoreBlocks.airDistributor, 	ItemBlock.class, 			GCCoreBlocks.airDistributor.getUnlocalizedName(), 		GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCCoreBlocks.oxygenPipe, 		ItemBlock.class, 			GCCoreBlocks.oxygenPipe.getUnlocalizedName(), 			GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCCoreBlocks.blockAirCollector, 	ItemBlock.class, 			GCCoreBlocks.blockAirCollector.getUnlocalizedName(), 	GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCCoreBlocks.rocketBench, 		ItemBlock.class, 			GCCoreBlocks.rocketBench.getUnlocalizedName(), 			GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCCoreBlocks.fallenMeteor, 		ItemBlock.class, 			GCCoreBlocks.fallenMeteor.getUnlocalizedName(), 		GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCCoreBlocks.airLockFrame, 		ItemBlock.class, 			GCCoreBlocks.airLockFrame.getUnlocalizedName(), 		GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCCoreBlocks.airLockSeal, 		ItemBlock.class, 			GCCoreBlocks.airLockSeal.getUnlocalizedName(), 			GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCCoreBlocks.crudeOilStill, 		ItemBlock.class, 			GCCoreBlocks.crudeOilStill.getUnlocalizedName(), 		GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCCoreBlocks.crudeOilMoving, 	ItemBlock.class, 			GCCoreBlocks.crudeOilMoving.getUnlocalizedName(), 		GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCCoreBlocks.refinery, 			ItemBlock.class, 			GCCoreBlocks.refinery.getUnlocalizedName(), 			GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCCoreBlocks.compressor, 		ItemBlock.class, 			GCCoreBlocks.compressor.getUnlocalizedName(), 			GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCCoreBlocks.decorationBlocks, 	GCCoreItemBlockBase.class, 	GCCoreBlocks.decorationBlocks.getUnlocalizedName(), 	GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCCoreBlocks.sapling, 			GCCoreItemSapling.class, 	GCCoreBlocks.sapling.getUnlocalizedName(), 				GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCCoreBlocks.fuelLoader, 		ItemBlock.class, 			GCCoreBlocks.fuelLoader.getUnlocalizedName(), 			GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCCoreBlocks.spaceStationBase, 	ItemBlock.class, 			GCCoreBlocks.spaceStationBase.getUnlocalizedName(), 	GalacticraftCore.MODID);
		GameRegistry.registerBlock(GCCoreBlocks.dummyBlock, 		ItemBlock.class, 			GCCoreBlocks.dummyBlock.getUnlocalizedName(), 	GalacticraftCore.MODID);
	}
}
