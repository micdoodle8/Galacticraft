package micdoodle8.mods.galacticraft;

import net.minecraft.src.Block;
import net.minecraft.src.BlockFurnace;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.MapColor;
import net.minecraft.src.Material;
import net.minecraft.src.MaterialLiquid;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCBlocks 
{
	public static GCBlockBreatheableAir breatheableAir;
	public static Block marsStone;
	public static Block marsDirt;
	public static Block marsGrass;
//	public static Block marsOreDesh;
//	public static Block marsOreQuandrium;
//	public static Block marsOreElectrum;
	public static Block marsCobblestone;
	public static Block creeperEgg;
	public static Block creeperDungeonWall;
	public static Block treasureChest;
	public static Block landingPad;
	public static Block bacterialSludgeStill;
	public static Block bacterialSludgeMoving;
	public static GCBlockUnlitTorch unlitTorch;
	public static GCBlockUnlitTorch unlitTorchLit;
	public static GCBlockAirDistributor airDistributor;
	public static GCBlockAirDistributor airDistributorActive;
	public static GCBlockOxygenPipe oxygenPipe;
//	public static Block blockOreAluminum;
//	public static Block blockOreCopper;
//	public static Block blockOreTitanium;
	public static Block blockOres;

    public static final Material bacterialSludge = (new MaterialLiquid(MapColor.waterColor));
	
	public static void initBlocks() 
	{
		blockOres = 									new GCBlockOre					(GCConfigManager.idBlockOre + 256,  						9)						.setHardness(3.0F) 																				.setCreativeTab(CreativeTabs.tabBlock) 																		.setBlockName("blockores");
		breatheableAir = (GCBlockBreatheableAir)		new GCBlockBreatheableAir		(GCConfigManager.idBlockBreatheableAir + 256)										.setHardness(0.0F) 																				.setCreativeTab(CreativeTabs.tabDecorations)																.setBlockName("breatheableAir");
		marsCobblestone = 								new GCBlock						(GCConfigManager.idBlockMarsCobblestone + 256, 			11, Material.rock)		.setHardness(2.2F)																				.setCreativeTab(CreativeTabs.tabDecorations)																.setBlockName("marsCobblestone");
		marsStone = 									new GCBlockStone				(GCConfigManager.idBlockMarsStone + 256, 					2)						.setHardness(1.7F)																				.setCreativeTab(CreativeTabs.tabDecorations)																.setBlockName("marsStone");
		marsDirt = 										new GCBlock						(GCConfigManager.idBlockMarsDirt + 256, 					5, Material.ground)		.setHardness(0.6F)																				.setCreativeTab(CreativeTabs.tabDecorations)																.setBlockName("marsDirt");
		marsGrass = 									new GCBlockGrass				(GCConfigManager.idBlockMarsGrass + 256,					4)						.setHardness(0.7F)																				.setCreativeTab(CreativeTabs.tabBlock)																		.setBlockName("marsGrass");
//		marsOreDesh = 									new GCBlockOre					(GCConfigManager.idBlockOreDesh, 					6)						.setHardness(3.0F)																				.setCreativeTab(CreativeTabs.tabBlock)																		.setBlockName("marsOreDesh");
//		marsOreElectrum = 								new GCBlockOre					(GCConfigManager.idBlockOreElectrum, 				7)						.setHardness(3.0F)																				.setCreativeTab(CreativeTabs.tabBlock)																		.setBlockName("marsOreElectrum");
//		marsOreQuandrium = 								new GCBlockOre					(GCConfigManager.idBlockOreQuandrium, 				8)						.setHardness(3.0F)																				.setCreativeTab(CreativeTabs.tabBlock)																		.setBlockName("marsOreQuandrium");
		creeperEgg = 									new GCBlockCreeperEgg			(GCConfigManager.idBlockCreeperEgg + 256, 				23)						.setHardness(3.0F)		.setResistance(15.0F)	.setLightValue(0.125F)							.setCreativeTab(CreativeTabs.tabDecorations)		.setStepSound(Block.soundStoneFootstep)					.setBlockName("creeperEgg");
		creeperDungeonWall = 							new GCBlockCreeperDungeonWall	(GCConfigManager.idBlockCreeperDungeonWall + 256,     	22, Material.rock)		.setHardness(5.0F)      .setResistance(100F)    .setLightValue(0F)								.setCreativeTab(CreativeTabs.tabDecorations)       	.setStepSound(Block.soundStoneFootstep)					.setBlockName("creeperDungeonWall");
		treasureChest = 								new GCBlockTreasureChest		(GCConfigManager.idBlockTreasureChest + 256)										.setHardness(1.0F)      .setResistance(10.0F)   												.setCreativeTab(CreativeTabs.tabDecorations)       	.setStepSound(Block.soundStoneFootstep)                 .setBlockName("treasureChest");
		landingPad = 									new GCBlockLandingPad			(GCConfigManager.idBlockLandingPad + 256,					24)						.setHardness(1.0F)      .setResistance(10.0F)  	 												.setCreativeTab(CreativeTabs.tabDecorations)       	.setStepSound(Block.soundStoneFootstep)                 .setBlockName("landingPad");
		bacterialSludgeStill = 							new GCBlockStationary 			(GCConfigManager.idBlockBacterialSludgeStill + 256,  		237, bacterialSludge)	.setHardness(0.0F)								.setLightValue(0.2F)	.setLightOpacity(3)		.setCreativeTab(CreativeTabs.tabDecorations)																.setBlockName("bacterialSludgeStill");
		bacterialSludgeMoving = 						new GCBlockFlowing 				(GCConfigManager.idBlockBacterialSludgeMoving + 256, 		237, bacterialSludge)	.setHardness(0.0F)								.setLightValue(0.2F)	.setLightOpacity(3)		.setCreativeTab(CreativeTabs.tabDecorations)																.setBlockName("bacterialSludgeMoving");
		unlitTorch = (GCBlockUnlitTorch) 				new GCBlockUnlitTorch 			(GCConfigManager.idBlockUnlitTorch + 256,             	37, false)				.setHardness(0.0F)								.setLightValue(0.2F)							.setCreativeTab(CreativeTabs.tabDecorations)		.setStepSound(Block.soundWoodFootstep)					.setBlockName("unlitTorch");
		unlitTorchLit = (GCBlockUnlitTorch) 			new GCBlockUnlitTorch 			(GCConfigManager.idBlockUnlitTorchLit + 256,             	37, true)				.setHardness(0.0F)								.setLightValue(0.9375F)							.setCreativeTab(CreativeTabs.tabDecorations)		.setStepSound(Block.soundWoodFootstep)					.setBlockName("unlitTorchLit");
		airDistributor = (GCBlockAirDistributor)		new GCBlockAirDistributor		(GCConfigManager.idBlockAirDistributor + 256, false)								.setHardness(3.5F)																				.setCreativeTab(CreativeTabs.tabDecorations)		.setStepSound(Block.soundStoneFootstep)					.setBlockName("distributor");
		airDistributorActive = (GCBlockAirDistributor)	new GCBlockAirDistributor		(GCConfigManager.idBlockAirDistributorActive + 256, true)							.setHardness(3.5F)																				.setCreativeTab((CreativeTabs)null)					.setStepSound(Block.soundStoneFootstep)					.setBlockName("distributorActive");
		oxygenPipe = (GCBlockOxygenPipe)				new GCBlockOxygenPipe			(GCConfigManager.idBlockAirPipe + 256, 40)										.setHardness(0.3F)																				.setCreativeTab(CreativeTabs.tabDecorations)  		.setStepSound(Block.soundGlassFootstep)                 .setBlockName("oxygenPipe");
//		blockOreAluminum = 								new GCBlockOre					(GCConfigManager.idBlockOreAluminum, 				9)						.setHardness(3.0F) 																				.setCreativeTab(CreativeTabs.tabBlock) 																		.setBlockName("marsOreAluminum");	
//		blockOreCopper = 								new GCBlockOre					(GCConfigManager.idBlockOreCopper, 					9)						.setHardness(3.0F) 																				.setCreativeTab(CreativeTabs.tabBlock) 																		.setBlockName("marsOreCopper");
//		blockOreTitanium = 								new GCBlockOre					(GCConfigManager.idBlockOreTitanium, 				9)						.setHardness(3.0F) 																				.setCreativeTab(CreativeTabs.tabBlock) 																		.setBlockName("marsOreTitanium");		
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
		Item.itemsList[GCConfigManager.idBlockOre] = new GCItemBlockOre(GCConfigManager.idBlockOre, blockOres).setItemName("oreblock");
		
		GameRegistry.registerBlock(marsStone);
		GameRegistry.registerBlock(marsDirt);
		GameRegistry.registerBlock(marsGrass);
//		GameRegistry.registerBlock(marsOreDesh);
//		GameRegistry.registerBlock(marsOreQuandrium);
//		GameRegistry.registerBlock(marsOreElectrum);
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
//		GameRegistry.registerBlock(blockOreAluminum);
//		GameRegistry.registerBlock(blockOreCopper);
//		GameRegistry.registerBlock(blockOreTitanium);
		
//		OreDictionary.registerOre("oreAluminum", blockOreAluminum);
//		OreDictionary.registerOre("oreCopper", blockOreCopper);
//		OreDictionary.registerOre("oreTitanium", blockOreTitanium);
	}

	public static void addNames() 
	{
		LanguageRegistry.instance().addNameForObject(marsStone, 			"en_US", 	"Stone");
		LanguageRegistry.instance().addNameForObject(marsDirt, 				"en_US", 	"Dirt");
		LanguageRegistry.instance().addNameForObject(marsGrass, 			"en_US", 	"Grass");
//		LanguageRegistry.instance().addNameForObject(marsOreDesh, 			"en_US", 	"Desh");
//		LanguageRegistry.instance().addNameForObject(marsOreQuandrium, 		"en_US", 	"Quandrium Ore");
//		LanguageRegistry.instance().addNameForObject(marsOreElectrum, 		"en_US", 	"Electrum Ore");
		LanguageRegistry.instance().addNameForObject(marsCobblestone, 		"en_US", 	"Cobblestone");
		LanguageRegistry.instance().addNameForObject(creeperEgg, 			"en_US", 	"Creeper Egg");
		LanguageRegistry.instance().addNameForObject(creeperDungeonWall,	"en_US", 	"Creeper Dungeon Wall");
		LanguageRegistry.instance().addNameForObject(treasureChest,			"en_US", 	"Treasure Chest");
		LanguageRegistry.instance().addNameForObject(landingPad,			"en_US", 	"Landing Pad");
		LanguageRegistry.instance().addNameForObject(bacterialSludgeStill,	"en_US", 	"Bacterial Sludge Still");
		LanguageRegistry.instance().addNameForObject(bacterialSludgeMoving,	"en_US", 	"Bacterial Sludge Moving");
		LanguageRegistry.instance().addNameForObject(unlitTorch,			"en_US", 	"Torch");
		LanguageRegistry.instance().addNameForObject(unlitTorchLit,			"en_US", 	"Torch");
		LanguageRegistry.instance().addNameForObject(airDistributor,		"en_US", 	"Air Distributor");
		LanguageRegistry.instance().addNameForObject(airDistributorActive,	"en_US", 	"Air Distributor Active");
//		LanguageRegistry.instance().addNameForObject(blockOreAluminum,		"en_US", 	"Aluminum Ore");
//		LanguageRegistry.instance().addNameForObject(blockOreCopper,		"en_US", 	"Copper Ore");
//		LanguageRegistry.instance().addNameForObject(blockOreTitanium,		"en_US", 	"Titanium Ore");
		LanguageRegistry.instance().addStringLocalization("tile.blockores.desh.name", "Desh Ore");
		LanguageRegistry.instance().addStringLocalization("tile.blockores.electrum.name", "Electrum Ore");
		LanguageRegistry.instance().addStringLocalization("tile.blockores.quandrium.name", "Quandrium Ore");
		LanguageRegistry.instance().addStringLocalization("tile.blockores.aluminumearth.name", "Aluminum Ore");
		LanguageRegistry.instance().addStringLocalization("tile.blockores.aluminummars.name", "Aluminum Ore");
		LanguageRegistry.instance().addStringLocalization("tile.blockores.copperearth.name", "Copper Ore");
		LanguageRegistry.instance().addStringLocalization("tile.blockores.coppermars.name", "Copper Ore");
		LanguageRegistry.instance().addStringLocalization("tile.blockores.titaniumearth.name", "Titanium Ore");
		LanguageRegistry.instance().addStringLocalization("tile.blockores.titaniummars.name", "Titanium Ore");
	}
}
