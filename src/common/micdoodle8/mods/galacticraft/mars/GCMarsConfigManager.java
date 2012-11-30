package micdoodle8.mods.galacticraft.mars;

import java.io.File;
import java.util.logging.Level;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMarsConfigManager 
{
	public static boolean loaded;
	
	static Configuration configuration;
	
	public GCMarsConfigManager(File file)
	{
		if (!loaded)
		{
			configuration = new Configuration(file);
			setDefaultValues();
		}
	}
	
	// DIMENSIONS
	public static int dimensionIDMars;

	// BLOCKS
	public static int idBlockMarsStone;
	public static int idBlockMarsGrass;
	public static int idBlockMarsDirt;
	public static int idBlockMarsCobblestone;
	public static int idBlockCreeperEgg;
	public static int idBlockCreeperDungeonWall;
	public static int idBlockBacterialSludgeMoving;
	public static int idBlockBacterialSludgeStill;
	public static int idBlockOre;

	// ITEMS
	public static int idItemReinforcedBucket;
	public static int idItemReinforcedBucketMilk;
	public static int idItemReinforcedBucketLava;
	public static int idItemReinforcedBucketWater;
	public static int idItemReinforcedBucketBacteria;
	public static int idItemRawDesh;
	public static int idItemDeshStick;
	public static int idItemIngotQuandrium;
	public static int idItemIngotDesh;
	
	// ARMOR
	public static int idArmorQuandriumHelmet;
	public static int idArmorQuandriumChestplate;
	public static int idArmorQuandriumLeggings;
	public static int idArmorQuandriumBoots;
	public static int idArmorQuandriumHelmetBreathable;
	public static int idArmorDeshHelmet;
	public static int idArmorDeshChestplate;
	public static int idArmorDeshLeggings;
	public static int idArmorDeshBoots;
	public static int idArmorDeshHelmetBreathable;
	public static int idArmorHeavyBoots;
	public static int idArmorJetpack;

	// TOOLS
	public static int idToolDeshSword;
	public static int idToolDeshPickaxe;
	public static int idToolDeshAxe;
	public static int idToolDeshSpade;
	public static int idToolDeshHoe;
	public static int idToolPlanetStoneSword;
	public static int idToolPlanetStonePickaxe;
	public static int idToolPlanetStoneAxe;
	public static int idToolPlanetStoneSpade;
	public static int idToolPlanetStoneHoe;
	public static int idToolQuandriumSword;
	public static int idToolQuandriumPickaxe;
	public static int idToolQuandriumAxe;
	public static int idToolQuandriumSpade;
	public static int idToolQuandriumHoe;
	
	// MOBS
	public static int idEntityCreeperBoss;
	public static int idEntitySludgeling;
	public static int idEntityProjectileTNT;
	
	private void setDefaultValues()
    {
		try
		{
	        configuration.load();
	        
	        dimensionIDMars = 					configuration.get("Dimensions", 												"Mars Dimension ID",				-29)		.getInt(-29);
	        
	        idBlockMarsStone = 					configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockMarsStone", 				210)	.getInt(210);
	        idBlockMarsGrass = 					configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockMarsGrass", 				211)	.getInt(211);
	        idBlockMarsDirt = 					configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockMarsDirt", 					212)	.getInt(212);
	        idBlockMarsCobblestone = 			configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockMarsCobblestone", 			213)	.getInt(213);
	        idBlockCreeperEgg = 				configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockCreeperEgg", 				214)	.getInt(214);
	        idBlockCreeperDungeonWall = 		configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockCreeperDungeonWall", 		215)	.getInt(215);
	        idBlockBacterialSludgeMoving = 		configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockBacterialSludgeMoving", 	216)	.getInt(216);
	        idBlockBacterialSludgeStill = 		configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockBacterialSludgeStill", 		217)	.getInt(217);
	        idBlockOre = 						configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockOre", 						218)	.getInt(218);

	        idItemReinforcedBucket = 			configuration.get(configuration.CATEGORY_ITEM, 									"idItemReinforcedBucket", 			9900)	.getInt(9900);
	        idItemReinforcedBucketMilk = 		configuration.get(configuration.CATEGORY_ITEM, 									"idItemReinforcedBucketMilk", 		9901)	.getInt(9901);
	        idItemReinforcedBucketWater = 		configuration.get(configuration.CATEGORY_ITEM, 									"idItemReinforcedBucketWater", 		9902)	.getInt(9902);
	        idItemReinforcedBucketLava = 		configuration.get(configuration.CATEGORY_ITEM, 									"idItemReinforcedBucketLava", 		9903)	.getInt(9903);
	        idItemReinforcedBucketBacteria = 	configuration.get(configuration.CATEGORY_ITEM, 									"idItemReinforcedBucketBacteria", 	9904)	.getInt(9904);
	        idItemRawDesh = 					configuration.get(configuration.CATEGORY_ITEM, 									"idItemRawDesh", 					9905)	.getInt(9905);
	        idItemDeshStick = 					configuration.get(configuration.CATEGORY_ITEM, 									"idItemDeshStick", 					9906)	.getInt(9906);
	        idItemIngotQuandrium = 				configuration.get(configuration.CATEGORY_ITEM, 									"idItemIngotQuandrium", 			9907)	.getInt(9907);
	        idItemIngotDesh = 					configuration.get(configuration.CATEGORY_ITEM, 									"idItemIngotDesh", 					9908)	.getInt(9908);

	        idToolPlanetStoneSword = 			configuration.get(configuration.CATEGORY_ITEM, 									"idToolPlanetStoneSword", 			9909)	.getInt(9909);
	        idToolPlanetStonePickaxe = 			configuration.get(configuration.CATEGORY_ITEM, 									"idToolPlanetStonePickaxe", 		9910)	.getInt(9910);
	        idToolPlanetStoneSpade = 			configuration.get(configuration.CATEGORY_ITEM, 									"idToolPlanetStoneSpade", 			9911)	.getInt(9911);
	        idToolPlanetStoneHoe = 				configuration.get(configuration.CATEGORY_ITEM, 									"idToolPlanetStoneHoe", 			9912)	.getInt(9912);
	        idToolPlanetStoneAxe = 				configuration.get(configuration.CATEGORY_ITEM, 									"idToolPlanetStoneAxe", 			9913)	.getInt(9913);
	        idToolDeshSword = 					configuration.get(configuration.CATEGORY_ITEM, 									"idToolDeshSword", 					9914)	.getInt(9914);
	        idToolDeshPickaxe = 				configuration.get(configuration.CATEGORY_ITEM, 									"idToolDeshPickaxe", 				9915)	.getInt(9915);
	        idToolDeshSpade = 					configuration.get(configuration.CATEGORY_ITEM, 									"idToolDeshSpade", 					9916)	.getInt(9916);
	        idToolDeshHoe = 					configuration.get(configuration.CATEGORY_ITEM, 									"idToolDeshHoe", 					9917)	.getInt(9917);
	        idToolDeshAxe = 					configuration.get(configuration.CATEGORY_ITEM, 									"idToolDeshAxe", 					9918)	.getInt(9918);
	        idToolQuandriumSword = 				configuration.get(configuration.CATEGORY_ITEM, 									"idToolQuandriumSword", 			9919)	.getInt(9919);
	        idToolQuandriumPickaxe = 			configuration.get(configuration.CATEGORY_ITEM, 									"idToolQuandriumPickaxe", 			9920)	.getInt(9920);
	        idToolQuandriumSpade = 				configuration.get(configuration.CATEGORY_ITEM, 									"idToolQuandriumSpade", 			9921)	.getInt(9921);
	        idToolQuandriumHoe = 				configuration.get(configuration.CATEGORY_ITEM, 									"idToolQuandriumHoe", 				9922)	.getInt(9922);
	        idToolQuandriumAxe = 				configuration.get(configuration.CATEGORY_ITEM, 									"idToolQuandriumAxe", 				9923)	.getInt(9923);

	        idArmorQuandriumHelmet = 			configuration.get(configuration.CATEGORY_ITEM, 									"idArmorQuandriumHelmet", 			9924)	.getInt(9924);
	        idArmorQuandriumChestplate = 		configuration.get(configuration.CATEGORY_ITEM, 									"idArmorQuandriumChestplate", 		9925)	.getInt(9925);
	        idArmorQuandriumLeggings = 			configuration.get(configuration.CATEGORY_ITEM, 									"idArmorQuandriumLeggings", 		9926)	.getInt(9926);
	        idArmorQuandriumBoots = 			configuration.get(configuration.CATEGORY_ITEM, 									"idArmorQuandriumBoots", 			9927)	.getInt(9927);
	        idArmorQuandriumHelmetBreathable = 	configuration.get(configuration.CATEGORY_ITEM, 									"idArmorQuandriumHelmetBreathable", 9928)	.getInt(9928);
	        idArmorDeshHelmet = 				configuration.get(configuration.CATEGORY_ITEM, 									"idArmorDeshHelmet", 				9929)	.getInt(9929);
	        idArmorDeshChestplate = 			configuration.get(configuration.CATEGORY_ITEM, 									"idArmorDeshChestplate", 			9930)	.getInt(9930);
	        idArmorDeshLeggings = 				configuration.get(configuration.CATEGORY_ITEM, 									"idArmorDeshLeggings", 				9931)	.getInt(9931);
	        idArmorDeshBoots = 					configuration.get(configuration.CATEGORY_ITEM, 									"idArmorDeshBoots", 				9932)	.getInt(9932);
	        idArmorDeshHelmetBreathable = 		configuration.get(configuration.CATEGORY_ITEM, 									"idArmorDeshHelmetBreathable",		9933)	.getInt(9933);

	        idArmorHeavyBoots = 				configuration.get(configuration.CATEGORY_ITEM, 									"idArmorHeavyBoots", 				9934)	.getInt(9934);
	        idArmorJetpack = 					configuration.get(configuration.CATEGORY_ITEM, 									"idArmorJetpack",					9935)	.getInt(9935);
	        
	        idEntityCreeperBoss = 				configuration.get("Entities", 													"idEntityCreeperBoss", 				162)	.getInt(162);
	        idEntitySludgeling = 				configuration.get("Entities", 													"idEntitySludgeling",				163)	.getInt(163);
	        idEntityProjectileTNT = 			configuration.get("Entities", 													"idEntityProjectileTNT",			164)	.getInt(164);
		}
		catch (Exception e)
		{
			FMLLog.log(Level.SEVERE, e, "Galacticraft has a problem loading it's configuration");
		}
		finally 
		{
			configuration.save();
			loaded = true;
		}
    }
}
