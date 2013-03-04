package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBlockBase;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBlockOre;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemSapling;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreBlocks
{
	public static GCBlockBreathableAir breatheableAir;
	public static Block treasureChest;
	public static Block landingPad;
	public static GCCoreBlockUnlitTorch unlitTorch;
	public static GCCoreBlockUnlitTorch unlitTorchLit;
	public static GCCoreBlockOxygenDistributor airDistributor;
	public static GCCoreBlockOxygenPipe oxygenPipe;
	public static Block blockOres;
	public static Block blockAirCollector;
	public static Block sapling;
	public static Block rocketBench;
	public static Block fallenMeteor;
	public static Block decorationBlocks;
	public static Block airLockFrame;
	public static Block airLockSeal;
	public static Block crudeOilMoving;
	public static Block crudeOilStill;
	public static Block refinery;
	public static Block compressor;
	
	public static LiquidStack crudeOilStack;
	
	public static void initBlocks()
	{
		GCCoreBlocks.blockOres = 											new GCCoreBlockOre					(GCCoreConfigManager.idBlockOre,  						0)						.setHardness(3.0F) 																				 																											.setBlockName("blockores");
		GCCoreBlocks.breatheableAir = (GCBlockBreathableAir)				new GCBlockBreathableAir			(GCCoreConfigManager.idBlockBreatheableAir, 			16)						.setHardness(0.0F) 		.setResistance(1000F)													.setCreativeTab((CreativeTabs)null)																			.setBlockName("breatheableAir");
		GCCoreBlocks.treasureChest = 										new GCCoreBlockTreasureChest		(GCCoreConfigManager.idBlockTreasureChest				)						.setHardness(1.0F)      .setResistance(10.0F)   												.setCreativeTab(GalacticraftCore.galacticraftTab)   .setStepSound(Block.soundStoneFootstep)                 .setBlockName("treasureChest");
		GCCoreBlocks.landingPad = 											new GCCoreBlockLandingPad			(GCCoreConfigManager.idBlockLandingPad,					27)						.setHardness(1.0F)      .setResistance(10.0F)  	 												.setCreativeTab(GalacticraftCore.galacticraftTab)   .setStepSound(Block.soundStoneFootstep)                 .setBlockName("landingPad");
		GCCoreBlocks.unlitTorch = (GCCoreBlockUnlitTorch) 					new GCCoreBlockUnlitTorch 			(GCCoreConfigManager.idBlockUnlitTorch,             	11, false)				.setHardness(0.0F)								.setLightValue(0.2F)							.setCreativeTab((CreativeTabs)null)					.setStepSound(Block.soundWoodFootstep)					.setBlockName("unlitTorch");
		GCCoreBlocks.unlitTorchLit = (GCCoreBlockUnlitTorch) 				new GCCoreBlockUnlitTorch 			(GCCoreConfigManager.idBlockUnlitTorchLit,             	11, true)				.setHardness(0.0F)								.setLightValue(0.9375F)							.setCreativeTab((CreativeTabs)null)					.setStepSound(Block.soundWoodFootstep)					.setBlockName("unlitTorchLit");
		GCCoreBlocks.airDistributor = (GCCoreBlockOxygenDistributor)		new GCCoreBlockOxygenDistributor	(GCCoreConfigManager.idBlockAirDistributor, 			false)					.setHardness(3.5F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)	.setStepSound(Block.soundStoneFootstep)					.setBlockName("distributor");
		GCCoreBlocks.oxygenPipe = (GCCoreBlockOxygenPipe)					new GCCoreBlockOxygenPipe			(GCCoreConfigManager.idBlockAirPipe, 					17)						.setHardness(0.3F)																				.setCreativeTab(GalacticraftCore.galacticraftTab)  	.setStepSound(Block.soundGlassFootstep)                 .setBlockName("oxygenPipe");
		Item.itemsList[GCCoreBlocks.blockOres.blockID] = 					new GCCoreItemBlockOre				(GCCoreBlocks.blockOres.blockID - 256)																																																														.setItemName("blockores");
		GCCoreBlocks.blockAirCollector = 									new GCCoreBlockOxygenCollector		(GCCoreConfigManager.idBlockAirCollector, 				8)						.setHardness(3.5F)  	  																		.setCreativeTab(GalacticraftCore.galacticraftTab) 	.setStepSound(Block.soundStoneFootstep)					.setBlockName("oxygenCollector");
		GCCoreBlocks.sapling =												new GCCoreBlockSapling				(GCCoreConfigManager.idBlockSapling2,                  	15)						.setHardness(0.0F)																				.setCreativeTab((CreativeTabs)null)					.setStepSound(Block.soundGrassFootstep)					.setBlockName("sapling2");
		GCCoreBlocks.rocketBench = 											new GCCoreBlockAdvancedCraftingTable(GCCoreConfigManager.idBlockRocketBench)										.setHardness(2.5F)																																	.setStepSound(Block.soundMetalFootstep)					.setBlockName("rocketWorkbench");
		GCCoreBlocks.fallenMeteor = 										new GCCoreBlockFallenMeteor			(GCCoreConfigManager.idBlockFallenMeteor)										.setHardness(50.0F)																																	.setStepSound(Block.soundStoneFootstep)					.setBlockName("fallenMeteor");
		Item.itemsList[GCCoreBlocks.sapling.blockID] = 						new GCCoreItemSapling				(GCCoreBlocks.sapling.blockID - 256)																																																														.setItemName("sapling2");
		GCCoreBlocks.decorationBlocks = 									new GCCoreBlock						(GCCoreConfigManager.idBlockDecorationBlock,  			3)						.setHardness(3.0F) 																				 																											.setBlockName("decorationblock");
		Item.itemsList[GCCoreBlocks.decorationBlocks.blockID] = 			new GCCoreItemBlockBase				(GCCoreBlocks.decorationBlocks.blockID - 256)																																																												.setItemName("decorationblock");
		GCCoreBlocks.airLockFrame = 										new GCCoreBlockAirLockFrame			(GCCoreConfigManager.idBlockAirLockFrame, 				29)						.setHardness(3.0F)                        														.setCreativeTab(GalacticraftCore.galacticraftTab)	.setStepSound(Block.soundMetalFootstep)	   				.setBlockName("airLockFrame");
		GCCoreBlocks.airLockSeal = 											new GCCoreBlockAirLockWall			(GCCoreConfigManager.idBlockAirLockSeal, 				26)						                        																		.setCreativeTab(null)								.setStepSound(Block.soundMetalFootstep)	   				.setBlockName("airLockSeal");
		GCCoreBlocks.crudeOilStill = 										new GCCoreBlockCrudeOilStationary	(GCCoreConfigManager.idBlockCrudeOilStill, 				Material.water)			.setHardness(3.0F)                        														.setCreativeTab(null)																						.setBlockName("crudeOilStill");
		GCCoreBlocks.crudeOilMoving = 										new GCCoreBlockCrudeOilMoving		(GCCoreConfigManager.idBlockCrudeOilMoving, 			Material.water)			.setHardness(3.0F)                        														.setCreativeTab(GalacticraftCore.galacticraftTab)															.setBlockName("crudeOilMoving");
		GCCoreBlocks.refinery = 											new GCCoreBlockRefinery 			(GCCoreConfigManager.idBlockRefinery, 0)                                        .setHardness(3.0F)                                                                              .setCreativeTab(GalacticraftCore.galacticraftTab)	.setStepSound(Block.soundMetalFootstep)					.setBlockName("refinery");
		GCCoreBlocks.compressor = 											new GCCoreBlockOxygenCompressor		(GCCoreConfigManager.idBlockAirCompressor, 				false)					.setHardness(3.5F)  	  																		.setCreativeTab(GalacticraftCore.galacticraftTab) 	.setStepSound(Block.soundStoneFootstep)					.setBlockName("oxygenCompressor");
	}

	public static void setHarvestLevels()
	{
		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.blockOres, 0, "pickaxe", 1);
		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.blockOres, 1, "pickaxe", 2);
		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.blockOres, 2, "pickaxe", 3);
		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.decorationBlocks, 0, "pickaxe", 1);
		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.decorationBlocks, 1, "pickaxe", 2);
		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.decorationBlocks, 2, "pickaxe", 3);
		MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.fallenMeteor, "pickaxe", 4);
	}
	
	public static void registerBlocks()
	{
		GameRegistry.registerBlock(GCCoreBlocks.treasureChest);
		GameRegistry.registerBlock(GCCoreBlocks.landingPad);
		GameRegistry.registerBlock(GCCoreBlocks.unlitTorch);
		GameRegistry.registerBlock(GCCoreBlocks.unlitTorchLit);
		GameRegistry.registerBlock(GCCoreBlocks.breatheableAir);
		GameRegistry.registerBlock(GCCoreBlocks.airDistributor);
		GameRegistry.registerBlock(GCCoreBlocks.oxygenPipe);
		GameRegistry.registerBlock(GCCoreBlocks.blockAirCollector);
		GameRegistry.registerBlock(GCCoreBlocks.rocketBench);
		GameRegistry.registerBlock(GCCoreBlocks.fallenMeteor);
		GameRegistry.registerBlock(GCCoreBlocks.airLockFrame);
		GameRegistry.registerBlock(GCCoreBlocks.airLockSeal);
		GameRegistry.registerBlock(GCCoreBlocks.crudeOilStill);
		GameRegistry.registerBlock(GCCoreBlocks.crudeOilMoving);
		GameRegistry.registerBlock(GCCoreBlocks.refinery);
		GameRegistry.registerBlock(GCCoreBlocks.compressor);
		
		GCCoreBlocks.crudeOilStack = LiquidDictionary.getOrCreateLiquid("Oil", new LiquidStack(GCCoreBlocks.crudeOilStill, 1));
	}

	public static void addNames()
	{
		GCCoreBlocks.addName(GCCoreBlocks.treasureChest);
		GCCoreBlocks.addName(GCCoreBlocks.landingPad);
		GCCoreBlocks.addName(GCCoreBlocks.unlitTorch);
		GCCoreBlocks.addName(GCCoreBlocks.unlitTorchLit);
		GCCoreBlocks.addName(GCCoreBlocks.airDistributor);
		GCCoreBlocks.addName(GCCoreBlocks.oxygenPipe);
		GCCoreBlocks.addNameWithMetadata("tile.blockores.aluminiumearth.name");
		GCCoreBlocks.addNameWithMetadata("tile.blockores.copperearth.name");
		GCCoreBlocks.addNameWithMetadata("tile.blockores.titaniumearth.name");
		GCCoreBlocks.addName(GCCoreBlocks.blockAirCollector);
		GCCoreBlocks.addName(GCCoreBlocks.rocketBench);
		GCCoreBlocks.addName(GCCoreBlocks.fallenMeteor);
		GCCoreBlocks.addNameWithMetadata("tile.decorationblock.aluminiumblock.name");
		GCCoreBlocks.addNameWithMetadata("tile.decorationblock.copperblock.name");
		GCCoreBlocks.addNameWithMetadata("tile.decorationblock.titaniumblock.name");
		GCCoreBlocks.addNameWithMetadata("tile.decorationblock.decoblock1.name");
		GCCoreBlocks.addNameWithMetadata("tile.decorationblock.decoblock2.name");
		GCCoreBlocks.addName(GCCoreBlocks.airLockFrame);
		GCCoreBlocks.addName(GCCoreBlocks.airLockSeal);
		GCCoreBlocks.addName(GCCoreBlocks.crudeOilStill);
		GCCoreBlocks.addName(GCCoreBlocks.crudeOilMoving);
		GCCoreBlocks.addName(GCCoreBlocks.refinery);
		GCCoreBlocks.addName(GCCoreBlocks.compressor);
	}
	
	private static void addName(Block block)
	{
        LanguageRegistry.instance().addStringLocalization(block.getBlockName() + ".name", GalacticraftCore.lang.get(block.getBlockName() + ".name"));
	}
	
	private static void addNameWithMetadata(String string)
	{
        LanguageRegistry.instance().addStringLocalization(string, GalacticraftCore.lang.get(string));
	}
}
