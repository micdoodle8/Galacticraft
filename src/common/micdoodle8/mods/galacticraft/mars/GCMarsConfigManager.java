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
	        
	        dimensionIDMars = 					configuration.get("Dimensions", 												"Mars Dimension ID",				29)		.getInt(29);
	        
	        idBlockMarsStone = 					configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockMarsStone", 				210)	.getInt(210);
	        idBlockMarsGrass = 					configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockMarsGrass", 				211)	.getInt(211);
	        idBlockMarsDirt = 					configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockMarsDirt", 					212)	.getInt(212);
	        idBlockMarsCobblestone = 			configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockMarsCobblestone", 			213)	.getInt(213);
	        idBlockCreeperEgg = 				configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockCreeperEgg", 				214)	.getInt(214);
	        idBlockCreeperDungeonWall = 		configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockCreeperDungeonWall", 		215)	.getInt(215);
	        idBlockBacterialSludgeMoving = 		configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockBacterialSludgeMoving", 	216)	.getInt(216);
	        idBlockBacterialSludgeStill = 		configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockBacterialSludgeStill", 		217)	.getInt(217);
	        idBlockOre = 						configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockOre", 						218)	.getInt(218);

	        idItemReinforcedBucket = 			configuration.get(configuration.CATEGORY_ITEM, 									"idItemReinforcedBucket", 			9865)	.getInt(9865);
	        idItemReinforcedBucketMilk = 		configuration.get(configuration.CATEGORY_ITEM, 									"idItemReinforcedBucketMilk", 		9866)	.getInt(9866);
	        idItemReinforcedBucketWater = 		configuration.get(configuration.CATEGORY_ITEM, 									"idItemReinforcedBucketWater", 		9867)	.getInt(9867);
	        idItemReinforcedBucketLava = 		configuration.get(configuration.CATEGORY_ITEM, 									"idItemReinforcedBucketLava", 		9868)	.getInt(9868);
	        idItemReinforcedBucketBacteria = 	configuration.get(configuration.CATEGORY_ITEM, 									"idItemReinforcedBucketBacteria", 	9869)	.getInt(9869);
	        idItemRawDesh = 					configuration.get(configuration.CATEGORY_ITEM, 									"idItemRawDesh", 					9870)	.getInt(9870);
	        idItemDeshStick = 					configuration.get(configuration.CATEGORY_ITEM, 									"idItemDeshStick", 					9871)	.getInt(9871);
	        idItemIngotQuandrium = 				configuration.get(configuration.CATEGORY_ITEM, 									"idItemIngotQuandrium", 			9872)	.getInt(9872);
	        idItemIngotDesh = 					configuration.get(configuration.CATEGORY_ITEM, 									"idItemIngotDesh", 					9873)	.getInt(9873);

	        idToolPlanetStoneSword = 			configuration.get(configuration.CATEGORY_ITEM, 									"idToolPlanetStoneSword", 			9874)	.getInt(9874);
	        idToolPlanetStonePickaxe = 			configuration.get(configuration.CATEGORY_ITEM, 									"idToolPlanetStonePickaxe", 		9875)	.getInt(9875);
	        idToolPlanetStoneSpade = 			configuration.get(configuration.CATEGORY_ITEM, 									"idToolPlanetStoneSpade", 			9876)	.getInt(9876);
	        idToolPlanetStoneHoe = 				configuration.get(configuration.CATEGORY_ITEM, 									"idToolPlanetStoneHoe", 			9877)	.getInt(9877);
	        idToolPlanetStoneAxe = 				configuration.get(configuration.CATEGORY_ITEM, 									"idToolPlanetStoneAxe", 			9878)	.getInt(9878);
	        idToolDeshSword = 					configuration.get(configuration.CATEGORY_ITEM, 									"idToolDeshSword", 					9879)	.getInt(9879);
	        idToolDeshPickaxe = 				configuration.get(configuration.CATEGORY_ITEM, 									"idToolDeshPickaxe", 				9880)	.getInt(9880);
	        idToolDeshSpade = 					configuration.get(configuration.CATEGORY_ITEM, 									"idToolDeshSpade", 					9881)	.getInt(9881);
	        idToolDeshHoe = 					configuration.get(configuration.CATEGORY_ITEM, 									"idToolDeshHoe", 					9882)	.getInt(9882);
	        idToolDeshAxe = 					configuration.get(configuration.CATEGORY_ITEM, 									"idToolDeshAxe", 					9883)	.getInt(9883);
	        idToolQuandriumSword = 				configuration.get(configuration.CATEGORY_ITEM, 									"idToolQuandriumSword", 			9884)	.getInt(9884);
	        idToolQuandriumPickaxe = 			configuration.get(configuration.CATEGORY_ITEM, 									"idToolQuandriumPickaxe", 			9885)	.getInt(9885);
	        idToolQuandriumSpade = 				configuration.get(configuration.CATEGORY_ITEM, 									"idToolQuandriumSpade", 			9886)	.getInt(9886);
	        idToolQuandriumHoe = 				configuration.get(configuration.CATEGORY_ITEM, 									"idToolQuandriumHoe", 				9887)	.getInt(9887);
	        idToolQuandriumAxe = 				configuration.get(configuration.CATEGORY_ITEM, 									"idToolQuandriumAxe", 				9888)	.getInt(9888);

	        idArmorQuandriumHelmet = 			configuration.get(configuration.CATEGORY_ITEM, 									"idArmorQuandriumHelmet", 			9889)	.getInt(9889);
	        idArmorQuandriumChestplate = 		configuration.get(configuration.CATEGORY_ITEM, 									"idArmorQuandriumChestplate", 		9890)	.getInt(9890);
	        idArmorQuandriumLeggings = 			configuration.get(configuration.CATEGORY_ITEM, 									"idArmorQuandriumLeggings", 		9891)	.getInt(9891);
	        idArmorQuandriumBoots = 			configuration.get(configuration.CATEGORY_ITEM, 									"idArmorQuandriumBoots", 			9892)	.getInt(9892);
	        idArmorQuandriumHelmetBreathable = 	configuration.get(configuration.CATEGORY_ITEM, 									"idArmorQuandriumHelmetBreathable", 9893)	.getInt(9893);
	        idArmorDeshHelmet = 				configuration.get(configuration.CATEGORY_ITEM, 									"idArmorDeshHelmet", 				9894)	.getInt(9894);
	        idArmorDeshChestplate = 			configuration.get(configuration.CATEGORY_ITEM, 									"idArmorDeshChestplate", 			9895)	.getInt(9895);
	        idArmorDeshLeggings = 				configuration.get(configuration.CATEGORY_ITEM, 									"idArmorDeshLeggings", 				9896)	.getInt(9896);
	        idArmorDeshBoots = 					configuration.get(configuration.CATEGORY_ITEM, 									"idArmorDeshBoots", 				9897)	.getInt(9897);
	        idArmorDeshHelmetBreathable = 		configuration.get(configuration.CATEGORY_ITEM, 									"idArmorDeshHelmetBreathable",		9898)	.getInt(9898);

	        idArmorHeavyBoots = 				configuration.get(configuration.CATEGORY_ITEM, 									"idArmorHeavyBoots", 				9899)	.getInt(9899);
	        idArmorJetpack = 					configuration.get(configuration.CATEGORY_ITEM, 									"idArmorJetpack",					9900)	.getInt(9900);
	        
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
