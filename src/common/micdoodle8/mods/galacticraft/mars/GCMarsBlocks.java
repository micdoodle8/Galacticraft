package micdoodle8.mods.galacticraft.mars;

import micdoodle8.mods.galacticraft.API.GCBlockBreathableAir;
import micdoodle8.mods.galacticraft.core.GCCoreBlockLandingPad;
import micdoodle8.mods.galacticraft.core.GCCoreBlockOxygenCollector;
import micdoodle8.mods.galacticraft.core.GCCoreBlockOxygenDistributor;
import micdoodle8.mods.galacticraft.core.GCCoreBlockOxygenPipe;
import micdoodle8.mods.galacticraft.core.GCCoreBlockUnlitTorch;
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
	public static Block marsStone;
	public static Block marsDirt;
	public static Block marsGrass;
	public static Block marsCobblestone;
	public static Block creeperEgg;
	public static Block creeperDungeonWall;
	public static Block bacterialSludgeStill;
	public static Block bacterialSludgeMoving;
	public static Block blockOres;

    public static final Material bacterialSludge = (new MaterialLiquid(MapColor.waterColor));
	
	public static void initBlocks() 
	{	
		blockOres = 										new GCMarsBlockOre					(GCMarsConfigManager.idBlockOre)												.setHardness(3.0F) 																				 																											.setBlockName("blockores");
		marsCobblestone = 									new GCMarsBlock						(GCMarsConfigManager.idBlockMarsCobblestone, 			27, Material.rock)		.setHardness(2.2F)																				.setCreativeTab(CreativeTabs.tabDecorations)																.setBlockName("marsCobblestone");
		marsStone = 										new GCMarsBlockStone				(GCMarsConfigManager.idBlockMarsStone, 					2)						.setHardness(1.7F)																				.setCreativeTab(CreativeTabs.tabDecorations)																.setBlockName("marsStone");
		marsDirt = 											new GCMarsBlock						(GCMarsConfigManager.idBlockMarsDirt, 					5, Material.ground)		.setHardness(0.6F)																				.setCreativeTab(CreativeTabs.tabDecorations)																.setBlockName("marsDirt");
		marsGrass = 										new GCMarsBlockGrass				(GCMarsConfigManager.idBlockMarsGrass,					4)						.setHardness(0.7F)																				.setCreativeTab(CreativeTabs.tabDecorations)																.setBlockName("marsGrass");
		creeperEgg = 										new GCMarsBlockCreeperEgg			(GCMarsConfigManager.idBlockCreeperEgg, 				23)						.setHardness(3.0F)		.setResistance(15.0F)	.setLightValue(0.125F)							.setCreativeTab(CreativeTabs.tabDecorations)		.setStepSound(Block.soundStoneFootstep)					.setBlockName("creeperEgg");
		creeperDungeonWall = 								new GCMarsBlockCreeperDungeonWall	(GCMarsConfigManager.idBlockCreeperDungeonWall,     	22, Material.rock)		.setHardness(5.0F)      .setResistance(100F)    .setLightValue(0F)								.setCreativeTab(CreativeTabs.tabDecorations)       	.setStepSound(Block.soundStoneFootstep)					.setBlockName("creeperDungeonWall");
		bacterialSludgeStill = 								new GCMarsBlockStationary 			(GCMarsConfigManager.idBlockBacterialSludgeStill,  		237, bacterialSludge)	.setHardness(0.0F)								.setLightValue(0.2F)	.setLightOpacity(3)		.setCreativeTab((CreativeTabs)null)																			.setBlockName("bacterialSludgeStill");
		bacterialSludgeMoving = 							new GCMarsBlockFlowing 				(GCMarsConfigManager.idBlockBacterialSludgeMoving, 		237, bacterialSludge)	.setHardness(0.0F)								.setLightValue(0.2F)	.setLightOpacity(3)		.setCreativeTab((CreativeTabs)null)																			.setBlockName("bacterialSludgeMoving");
		Item.itemsList[blockOres.blockID] = 				new GCMarsItemBlockOre				(blockOres.blockID - 256)																																																																.setItemName("blockores");
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
		GameRegistry.registerBlock(bacterialSludgeStill);
		GameRegistry.registerBlock(bacterialSludgeMoving);
	}

	public static void addNames() 
	{
		addName(marsStone);
		addName(marsDirt);
		addName(marsGrass);
		addName(marsCobblestone);
		addName(creeperEgg);
		addName(creeperDungeonWall);
		addNameWithMetadata("tile.blockores.desh.name");
		addNameWithMetadata("tile.blockores.electrum.name");
		addNameWithMetadata("tile.blockores.quandrium.name");
		addNameWithMetadata("tile.blockores.aluminumearth.name");
		addNameWithMetadata("tile.blockores.aluminummars.name");
		addNameWithMetadata("tile.blockores.copperearth.name");
		addNameWithMetadata("tile.blockores.coppermars.name");
		addNameWithMetadata("tile.blockores.titaniumearth.name");
		addNameWithMetadata("tile.blockores.titaniummars.name");
		
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
//       TODO LanguageRegistry.instance().addStringLocalization(block.getBlockName() + ".name", GalacticraftMars.lang.get(block.getBlockName() + ".name"));
	}
	
	private static void addNameWithMetadata(String string)
	{
//     TODO   LanguageRegistry.instance().addStringLocalization(string, GalacticraftMars.lang.get(string));
	}
}
