package micdoodle8.mods.galacticraft.core;

import micdoodle8.mods.galacticraft.API.GCBlockBreathableAir;
import micdoodle8.mods.galacticraft.core.GCBlockLandingPad;
import micdoodle8.mods.galacticraft.core.GCBlockOxygenCollector;
import micdoodle8.mods.galacticraft.core.GCBlockOxygenDistributor;
import micdoodle8.mods.galacticraft.core.GCBlockOxygenPipe;
import micdoodle8.mods.galacticraft.core.GCBlockUnlitTorch;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.MapColor;
import net.minecraft.src.Material;
import net.minecraft.src.MaterialLiquid;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreBlocks 
{
	public static GCBlockBreathableAir breatheableAir;
	public static Block treasureChest;
	public static Block landingPad;
	public static GCBlockUnlitTorch unlitTorch;
	public static GCBlockUnlitTorch unlitTorchLit;
	public static GCBlockOxygenDistributor airDistributor;
	public static GCBlockOxygenDistributor airDistributorActive;
	public static GCBlockOxygenPipe oxygenPipe;
	public static Block blockOres;
	public static Block blockAirCollector;
	
	public static void initBlocks() 
	{
		blockOres = 										new GCCoreBlockOre				(GCCoreConfigManager.idBlockOre,  						9)						.setHardness(3.0F) 																				 																											.setBlockName("blockores");
		breatheableAir = (GCBlockBreathableAir)				new GCBlockBreathableAir		(GCCoreConfigManager.idBlockBreatheableAir, 16)										.setHardness(0.0F) 		.setResistance(1000F)													.setCreativeTab(CreativeTabs.tabDecorations)																.setBlockName("breatheableAir");
		treasureChest = 									new GCCoreBlockTreasureChest	(GCCoreConfigManager.idBlockTreasureChest)										.setHardness(1.0F)      .setResistance(10.0F)   												.setCreativeTab(CreativeTabs.tabDecorations)       	.setStepSound(Block.soundStoneFootstep)                 .setBlockName("treasureChest");
		landingPad = 										new GCBlockLandingPad			(GCCoreConfigManager.idBlockLandingPad,					24)						.setHardness(1.0F)      .setResistance(10.0F)  	 												.setCreativeTab(CreativeTabs.tabDecorations)       	.setStepSound(Block.soundStoneFootstep)                 .setBlockName("landingPad");
		unlitTorch = (GCBlockUnlitTorch) 					new GCBlockUnlitTorch 			(GCCoreConfigManager.idBlockUnlitTorch,             	37, false)				.setHardness(0.0F)								.setLightValue(0.2F)							.setCreativeTab((CreativeTabs)null)					.setStepSound(Block.soundWoodFootstep)					.setBlockName("unlitTorch");
		unlitTorchLit = (GCBlockUnlitTorch) 				new GCBlockUnlitTorch 			(GCCoreConfigManager.idBlockUnlitTorchLit,             	37, true)				.setHardness(0.0F)								.setLightValue(0.9375F)							.setCreativeTab((CreativeTabs)null)					.setStepSound(Block.soundWoodFootstep)					.setBlockName("unlitTorchLit");
		airDistributor = (GCBlockOxygenDistributor)			new GCBlockOxygenDistributor	(GCCoreConfigManager.idBlockAirDistributor, false)								.setHardness(3.5F)																				.setCreativeTab(CreativeTabs.tabDecorations)		.setStepSound(Block.soundStoneFootstep)					.setBlockName("distributor");
		airDistributorActive = (GCBlockOxygenDistributor)	new GCBlockOxygenDistributor	(GCCoreConfigManager.idBlockAirDistributorActive, true)							.setHardness(3.5F)																				.setCreativeTab((CreativeTabs)null)					.setStepSound(Block.soundStoneFootstep)					.setBlockName("distributorActive");
		oxygenPipe = (GCBlockOxygenPipe)					new GCBlockOxygenPipe			(GCCoreConfigManager.idBlockAirPipe, 40)										.setHardness(0.3F)																				.setCreativeTab(CreativeTabs.tabDecorations)  		.setStepSound(Block.soundGlassFootstep)                 .setBlockName("oxygenPipe");
		Item.itemsList[blockOres.blockID] = 				new GCCoreItemBlockOre			(blockOres.blockID - 256)																																																																.setItemName("blockores");	
		blockAirCollector = 								new GCBlockOxygenCollector		(GCCoreConfigManager.idBlockAirCollector, 34)									.setHardness(3.5F)  	  																		.setCreativeTab(CreativeTabs.tabDecorations) 		.setStepSound(Block.soundStoneFootstep)					.setBlockName("oxygenCollector");
	}

	public static void setHarvestLevels() 
	{
//		MinecraftForge.setBlockHarvestLevel(marsOreDesh, 				"pickaxe", 	3);
//		MinecraftForge.setBlockHarvestLevel(marsOreQuandrium, 			"pickaxe", 	5);
//		MinecraftForge.setBlockHarvestLevel(marsOreElectrum, 			"pickaxe", 	6);
	}
	
	public static void registerBlocks() 
	{
		GameRegistry.registerBlock(treasureChest);
		GameRegistry.registerBlock(landingPad);
		GameRegistry.registerBlock(unlitTorch);
		GameRegistry.registerBlock(unlitTorchLit);
		GameRegistry.registerBlock(breatheableAir);
		GameRegistry.registerBlock(airDistributor);
		GameRegistry.registerBlock(airDistributorActive);
		GameRegistry.registerBlock(oxygenPipe);
		GameRegistry.registerBlock(blockAirCollector);
	}

	public static void addNames() 
	{
		addName(treasureChest);
		addName(landingPad);
		addName(unlitTorch);
		addName(unlitTorchLit);
		addName(airDistributor);
		addName(airDistributorActive);
		addName(oxygenPipe);
		addNameWithMetadata("tile.blockores.aluminumearth.name");
		addNameWithMetadata("tile.blockores.copperearth.name");
		addNameWithMetadata("tile.blockores.titaniumearth.name");
		addName(blockAirCollector);
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
