package micdoodle8.mods.galacticraft.mars;

import java.io.File;
import java.util.logging.Level;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012-2013, micdoodle8
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
		if (!GCMarsConfigManager.loaded)
		{
			GCMarsConfigManager.configuration = new Configuration(file);
			this.setDefaultValues();
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
	        GCMarsConfigManager.configuration.load();
	        
	        GCMarsConfigManager.dimensionIDMars = 					GCMarsConfigManager.configuration.get("Dimensions", 												"Mars Dimension ID",				-29)		.getInt(-29);
	        
	        GCMarsConfigManager.idBlockMarsStone = 					GCMarsConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, 								"idBlockMarsStone", 				210)	.getInt(210);
	        GCMarsConfigManager.idBlockMarsGrass = 					GCMarsConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, 								"idBlockMarsGrass", 				211)	.getInt(211);
	        GCMarsConfigManager.idBlockMarsDirt = 					GCMarsConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, 								"idBlockMarsDirt", 					212)	.getInt(212);
	        GCMarsConfigManager.idBlockMarsCobblestone = 			GCMarsConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, 								"idBlockMarsCobblestone", 			213)	.getInt(213);
	        GCMarsConfigManager.idBlockCreeperEgg = 				GCMarsConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, 								"idBlockCreeperEgg", 				214)	.getInt(214);
	        GCMarsConfigManager.idBlockCreeperDungeonWall = 		GCMarsConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, 								"idBlockCreeperDungeonWall", 		215)	.getInt(215);
	        GCMarsConfigManager.idBlockBacterialSludgeMoving = 		GCMarsConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, 								"idBlockBacterialSludgeMoving", 	216)	.getInt(216);
	        GCMarsConfigManager.idBlockBacterialSludgeStill = 		GCMarsConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, 								"idBlockBacterialSludgeStill", 		217)	.getInt(217);
	        GCMarsConfigManager.idBlockOre = 						GCMarsConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, 								"idBlockOre", 						218)	.getInt(218);

	        GCMarsConfigManager.idItemReinforcedBucket = 			GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idItemReinforcedBucket", 			9900)	.getInt(9900);
	        GCMarsConfigManager.idItemReinforcedBucketMilk = 		GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idItemReinforcedBucketMilk", 		9901)	.getInt(9901);
	        GCMarsConfigManager.idItemReinforcedBucketWater = 		GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idItemReinforcedBucketWater", 		9902)	.getInt(9902);
	        GCMarsConfigManager.idItemReinforcedBucketLava = 		GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idItemReinforcedBucketLava", 		9903)	.getInt(9903);
	        GCMarsConfigManager.idItemReinforcedBucketBacteria = 	GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idItemReinforcedBucketBacteria", 	9904)	.getInt(9904);
	        GCMarsConfigManager.idItemRawDesh = 					GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idItemRawDesh", 					9905)	.getInt(9905);
	        GCMarsConfigManager.idItemDeshStick = 					GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idItemDeshStick", 					9906)	.getInt(9906);
	        GCMarsConfigManager.idItemIngotQuandrium = 				GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idItemIngotQuandrium", 			9907)	.getInt(9907);
	        GCMarsConfigManager.idItemIngotDesh = 					GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idItemIngotDesh", 					9908)	.getInt(9908);

	        GCMarsConfigManager.idToolPlanetStoneSword = 			GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idToolPlanetStoneSword", 			9909)	.getInt(9909);
	        GCMarsConfigManager.idToolPlanetStonePickaxe = 			GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idToolPlanetStonePickaxe", 		9910)	.getInt(9910);
	        GCMarsConfigManager.idToolPlanetStoneSpade = 			GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idToolPlanetStoneSpade", 			9911)	.getInt(9911);
	        GCMarsConfigManager.idToolPlanetStoneHoe = 				GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idToolPlanetStoneHoe", 			9912)	.getInt(9912);
	        GCMarsConfigManager.idToolPlanetStoneAxe = 				GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idToolPlanetStoneAxe", 			9913)	.getInt(9913);
	        GCMarsConfigManager.idToolDeshSword = 					GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idToolDeshSword", 					9914)	.getInt(9914);
	        GCMarsConfigManager.idToolDeshPickaxe = 				GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idToolDeshPickaxe", 				9915)	.getInt(9915);
	        GCMarsConfigManager.idToolDeshSpade = 					GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idToolDeshSpade", 					9916)	.getInt(9916);
	        GCMarsConfigManager.idToolDeshHoe = 					GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idToolDeshHoe", 					9917)	.getInt(9917);
	        GCMarsConfigManager.idToolDeshAxe = 					GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idToolDeshAxe", 					9918)	.getInt(9918);
	        GCMarsConfigManager.idToolQuandriumSword = 				GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idToolQuandriumSword", 			9919)	.getInt(9919);
	        GCMarsConfigManager.idToolQuandriumPickaxe = 			GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idToolQuandriumPickaxe", 			9920)	.getInt(9920);
	        GCMarsConfigManager.idToolQuandriumSpade = 				GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idToolQuandriumSpade", 			9921)	.getInt(9921);
	        GCMarsConfigManager.idToolQuandriumHoe = 				GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idToolQuandriumHoe", 				9922)	.getInt(9922);
	        GCMarsConfigManager.idToolQuandriumAxe = 				GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idToolQuandriumAxe", 				9923)	.getInt(9923);

	        GCMarsConfigManager.idArmorQuandriumHelmet = 			GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idArmorQuandriumHelmet", 			9924)	.getInt(9924);
	        GCMarsConfigManager.idArmorQuandriumChestplate = 		GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idArmorQuandriumChestplate", 		9925)	.getInt(9925);
	        GCMarsConfigManager.idArmorQuandriumLeggings = 			GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idArmorQuandriumLeggings", 		9926)	.getInt(9926);
	        GCMarsConfigManager.idArmorQuandriumBoots = 			GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idArmorQuandriumBoots", 			9927)	.getInt(9927);
	        GCMarsConfigManager.idArmorQuandriumHelmetBreathable = 	GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idArmorQuandriumHelmetBreathable", 9928)	.getInt(9928);
	        GCMarsConfigManager.idArmorDeshHelmet = 				GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idArmorDeshHelmet", 				9929)	.getInt(9929);
	        GCMarsConfigManager.idArmorDeshChestplate = 			GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idArmorDeshChestplate", 			9930)	.getInt(9930);
	        GCMarsConfigManager.idArmorDeshLeggings = 				GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idArmorDeshLeggings", 				9931)	.getInt(9931);
	        GCMarsConfigManager.idArmorDeshBoots = 					GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idArmorDeshBoots", 				9932)	.getInt(9932);
	        GCMarsConfigManager.idArmorDeshHelmetBreathable = 		GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idArmorDeshHelmetBreathable",		9933)	.getInt(9933);

	        GCMarsConfigManager.idArmorHeavyBoots = 				GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idArmorHeavyBoots", 				9934)	.getInt(9934);
	        GCMarsConfigManager.idArmorJetpack = 					GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idArmorJetpack",					9935)	.getInt(9935);
	        
	        GCMarsConfigManager.idEntityCreeperBoss = 				GCMarsConfigManager.configuration.get("Entities", 													"idEntityCreeperBoss", 				162)	.getInt(162);
	        GCMarsConfigManager.idEntitySludgeling = 				GCMarsConfigManager.configuration.get("Entities", 													"idEntitySludgeling",				163)	.getInt(163);
	        GCMarsConfigManager.idEntityProjectileTNT = 			GCMarsConfigManager.configuration.get("Entities", 													"idEntityProjectileTNT",			164)	.getInt(164);
		}
		catch (final Exception e)
		{
			FMLLog.log(Level.SEVERE, e, "Galacticraft has a problem loading it's configuration");
		}
		finally
		{
			GCMarsConfigManager.configuration.save();
			GCMarsConfigManager.loaded = true;
		}
    }
}
