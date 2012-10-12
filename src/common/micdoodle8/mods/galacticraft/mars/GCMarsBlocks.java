package micdoodle8.mods.galacticraft.mars;

import micdoodle8.mods.galacticraft.core.GCBlockBreathableAir;
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
public class GCMarsBlocks 
{
	public static GCBlockBreathableAir breatheableAir;
	public static Block marsStone;
	public static Block marsDirt;
	public static Block marsGrass;
	public static Block marsCobblestone;
	public static Block creeperEgg;
	public static Block creeperDungeonWall;
	public static Block treasureChest;
	public static Block landingPad;
	public static Block bacterialSludgeStill;
	public static Block bacterialSludgeMoving;
	public static GCBlockUnlitTorch unlitTorch;
	public static GCBlockUnlitTorch unlitTorchLit;
	public static GCBlockOxygenDistributor airDistributor;
	public static GCBlockOxygenDistributor airDistributorActive;
	public static GCBlockOxygenPipe oxygenPipe;
	public static Block blockOres;
	public static Block blockAirCollector;

    public static final Material bacterialSludge = (new MaterialLiquid(MapColor.waterColor));
	
	public static void initBlocks() 
	{
		blockOres = 										new GCMarsBlockOre					(GCMarsConfigManager.idBlockOre,  						9)						.setHardness(3.0F) 																				 																											.setBlockName("blockores");
		breatheableAir = (GCBlockBreathableAir)				new GCBlockBreathableAir		(GCMarsConfigManager.idBlockBreatheableAir)										.setHardness(0.0F) 		.setResistance(1000F)													.setCreativeTab(CreativeTabs.tabDecorations)																.setBlockName("breatheableAir");
		marsCobblestone = 									new GCMarsBlock						(GCMarsConfigManager.idBlockMarsCobblestone, 			27, Material.rock)		.setHardness(2.2F)																				.setCreativeTab(CreativeTabs.tabDecorations)																.setBlockName("marsCobblestone");
		marsStone = 										new GCMarsBlockStone				(GCMarsConfigManager.idBlockMarsStone, 					2)						.setHardness(1.7F)																				.setCreativeTab(CreativeTabs.tabDecorations)																.setBlockName("marsStone");
		marsDirt = 											new GCMarsBlock						(GCMarsConfigManager.idBlockMarsDirt, 					5, Material.ground)		.setHardness(0.6F)																				.setCreativeTab(CreativeTabs.tabDecorations)																.setBlockName("marsDirt");
		marsGrass = 										new GCMarsBlockGrass				(GCMarsConfigManager.idBlockMarsGrass,					4)						.setHardness(0.7F)																				.setCreativeTab(CreativeTabs.tabDecorations)																.setBlockName("marsGrass");
		creeperEgg = 										new GCMarsBlockCreeperEgg			(GCMarsConfigManager.idBlockCreeperEgg, 				23)						.setHardness(3.0F)		.setResistance(15.0F)	.setLightValue(0.125F)							.setCreativeTab(CreativeTabs.tabDecorations)		.setStepSound(Block.soundStoneFootstep)					.setBlockName("creeperEgg");
		creeperDungeonWall = 								new GCMarsBlockCreeperDungeonWall	(GCMarsConfigManager.idBlockCreeperDungeonWall,     	22, Material.rock)		.setHardness(5.0F)      .setResistance(100F)    .setLightValue(0F)								.setCreativeTab(CreativeTabs.tabDecorations)       	.setStepSound(Block.soundStoneFootstep)					.setBlockName("creeperDungeonWall");
		treasureChest = 									new GCMarsBlockTreasureChest		(GCMarsConfigManager.idBlockTreasureChest)										.setHardness(1.0F)      .setResistance(10.0F)   												.setCreativeTab(CreativeTabs.tabDecorations)       	.setStepSound(Block.soundStoneFootstep)                 .setBlockName("treasureChest");
		landingPad = 										new GCBlockLandingPad			(GCMarsConfigManager.idBlockLandingPad,					24)						.setHardness(1.0F)      .setResistance(10.0F)  	 												.setCreativeTab(CreativeTabs.tabDecorations)       	.setStepSound(Block.soundStoneFootstep)                 .setBlockName("landingPad");
		bacterialSludgeStill = 								new GCMarsBlockStationary 			(GCMarsConfigManager.idBlockBacterialSludgeStill,  		237, bacterialSludge)	.setHardness(0.0F)								.setLightValue(0.2F)	.setLightOpacity(3)		.setCreativeTab((CreativeTabs)null)																			.setBlockName("bacterialSludgeStill");
		bacterialSludgeMoving = 							new GCMarsBlockFlowing 				(GCMarsConfigManager.idBlockBacterialSludgeMoving, 		237, bacterialSludge)	.setHardness(0.0F)								.setLightValue(0.2F)	.setLightOpacity(3)		.setCreativeTab((CreativeTabs)null)																			.setBlockName("bacterialSludgeMoving");
		unlitTorch = (GCBlockUnlitTorch) 					new GCBlockUnlitTorch 			(GCMarsConfigManager.idBlockUnlitTorch,             	37, false)				.setHardness(0.0F)								.setLightValue(0.2F)							.setCreativeTab((CreativeTabs)null)					.setStepSound(Block.soundWoodFootstep)					.setBlockName("unlitTorch");
		unlitTorchLit = (GCBlockUnlitTorch) 				new GCBlockUnlitTorch 			(GCMarsConfigManager.idBlockUnlitTorchLit,             	37, true)				.setHardness(0.0F)								.setLightValue(0.9375F)							.setCreativeTab((CreativeTabs)null)					.setStepSound(Block.soundWoodFootstep)					.setBlockName("unlitTorchLit");
		airDistributor = (GCBlockOxygenDistributor)			new GCBlockOxygenDistributor	(GCMarsConfigManager.idBlockAirDistributor, false)								.setHardness(3.5F)																				.setCreativeTab(CreativeTabs.tabDecorations)		.setStepSound(Block.soundStoneFootstep)					.setBlockName("distributor");
		airDistributorActive = (GCBlockOxygenDistributor)	new GCBlockOxygenDistributor	(GCMarsConfigManager.idBlockAirDistributorActive, true)							.setHardness(3.5F)																				.setCreativeTab((CreativeTabs)null)					.setStepSound(Block.soundStoneFootstep)					.setBlockName("distributorActive");
		oxygenPipe = (GCBlockOxygenPipe)					new GCBlockOxygenPipe			(GCMarsConfigManager.idBlockAirPipe, 40)										.setHardness(0.3F)																				.setCreativeTab(CreativeTabs.tabDecorations)  		.setStepSound(Block.soundGlassFootstep)                 .setBlockName("oxygenPipe");
		Item.itemsList[blockOres.blockID] = 				new GCMarsItemBlockOre				(blockOres.blockID - 256)																																																																.setItemName("blockores");	
		blockAirCollector = 								new GCBlockOxygenCollector		(GCMarsConfigManager.idBlockAirCollector, 34)									.setHardness(3.5F)  	  																		.setCreativeTab(CreativeTabs.tabDecorations) 		.setStepSound(Block.soundStoneFootstep)					.setBlockName("oxygenCollector");
	}

	public static void setHarvestLevels() 
	{
		MinecraftForge.setBlockHarvestLevel(creeperEgg, 				"pickaxe", 	2);
		MinecraftForge.setBlockHarvestLevel(creeperDungeonWall, 		"pickaxe", 	2);
		MinecraftForge.setBlockHarvestLevel(marsCobblestone, 			"pickaxe", 	1);
		MinecraftForge.setBlockHarvestLevel(marsStone, 					"pickaxe", 	1);
//		MinecraftForge.setBlockHarvestLevel(marsOreDesh, 				"pickaxe", 	3);
//		MinecraftForge.setBlockHarvestLevel(marsOreQuandrium, 			"pickaxe", 	5);
//		MinecraftForge.setBlockHarvestLevel(marsOreElectrum, 			"pickaxe", 	6);
	}
	
	public static void registerBlocks() 
	{
		GameRegistry.registerBlock(marsStone);
		GameRegistry.registerBlock(marsDirt);
		GameRegistry.registerBlock(marsGrass);
		GameRegistry.registerBlock(marsCobblestone);
		GameRegistry.registerBlock(creeperEgg);
		GameRegistry.registerBlock(creeperDungeonWall);
		GameRegistry.registerBlock(treasureChest);
		GameRegistry.registerBlock(landingPad);
		GameRegistry.registerBlock(bacterialSludgeStill);
		GameRegistry.registerBlock(bacterialSludgeMoving);
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
		addName(marsStone);
		addName(marsDirt);
		addName(marsGrass);
		addName(marsCobblestone);
		addName(creeperEgg);
		addName(creeperDungeonWall);
		addName(treasureChest);
		addName(landingPad);
		addName(unlitTorch);
		addName(unlitTorchLit);
		addName(airDistributor);
		addName(airDistributorActive);
		addName(oxygenPipe);
		addNameWithMetadata("tile.blockores.desh.name");
		addNameWithMetadata("tile.blockores.electrum.name");
		addNameWithMetadata("tile.blockores.quandrium.name");
		addNameWithMetadata("tile.blockores.aluminumearth.name");
		addNameWithMetadata("tile.blockores.aluminummars.name");
		addNameWithMetadata("tile.blockores.copperearth.name");
		addNameWithMetadata("tile.blockores.coppermars.name");
		addNameWithMetadata("tile.blockores.titaniumearth.name");
		addNameWithMetadata("tile.blockores.titaniummars.name");
		addName(blockAirCollector);
		
//		LanguageRegistry.instance().addNameForObject(marsStone, 			"en_US", 				"Stone");
//		LanguageRegistry.instance().addNameForObject(marsDirt, 				"en_US", 				"Dirt");
//		LanguageRegistry.instance().addNameForObject(marsGrass, 			"en_US", 				"Grass");
//		LanguageRegistry.instance().addNameForObject(marsCobblestone, 		"en_US", 				"Cobblestone");
//		LanguageRegistry.instance().addNameForObject(creeperEgg, 			"en_US", 				"Creeper Egg");
//		LanguageRegistry.instance().addNameForObject(creeperDungeonWall,	"en_US", 				"Creeper Dungeon Wall");
//		LanguageRegistry.instance().addNameForObject(treasureChest,			"en_US", 				"Treasure Chest");
//		LanguageRegistry.instance().addNameForObject(landingPad,			"en_US", 				"Landing Pad");
//		LanguageRegistry.instance().addNameForObject(bacterialSludgeStill,	"en_US", 				"Bacterial Sludge Still");
//		LanguageRegistry.instance().addNameForObject(bacterialSludgeMoving,	"en_US", 				"Bacterial Sludge Moving");
//		LanguageRegistry.instance().addNameForObject(unlitTorch,			"en_US", 				"Torch");
//		LanguageRegistry.instance().addNameForObject(unlitTorchLit,			"en_US", 				"Torch");
//		LanguageRegistry.instance().addNameForObject(airDistributor,		"en_US", 				"Air Distributor");
//		LanguageRegistry.instance().addNameForObject(airDistributorActive,	"en_US", 				"Air Distributor Active");
//		LanguageRegistry.instance().addStringLocalization("tile.blockores.desh.name", 				"Desh Ore");
//		LanguageRegistry.instance().addStringLocalization("tile.blockores.electrum.name", 			"Electrum Ore");
//		LanguageRegistry.instance().addStringLocalization("tile.blockores.quandrium.name", 			"Quandrium Ore");
//		LanguageRegistry.instance().addStringLocalization("tile.blockores.aluminumearth.name", 		"Aluminum Ore");
//		LanguageRegistry.instance().addStringLocalization("tile.blockores.aluminummars.name", 		"Aluminum Ore");
//		LanguageRegistry.instance().addStringLocalization("tile.blockores.copperearth.name", 		"Copper Ore");
//		LanguageRegistry.instance().addStringLocalization("tile.blockores.coppermars.name", 		"Copper Ore");
//		LanguageRegistry.instance().addStringLocalization("tile.blockores.titaniumearth.name", 		"Titanium Ore");
//		LanguageRegistry.instance().addStringLocalization("tile.blockores.titaniummars.name", 		"Titanium Ore");
//		LanguageRegistry.instance().addNameForObject(blockAirCollector,	"en_US", 					"Air Collector");
	}
	
	private static void addName(Block block)
	{
        LanguageRegistry.instance().addStringLocalization(block.getBlockName() + ".name", Galacticraft.lang.get(block.getBlockName() + ".name"));
	}
	
	private static void addNameWithMetadata(String string)
	{
        LanguageRegistry.instance().addStringLocalization(string, Galacticraft.lang.get(string));
	}
}
