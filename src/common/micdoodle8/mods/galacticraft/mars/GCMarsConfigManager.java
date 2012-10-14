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
	        
			dimensionIDMars = configuration.getOrCreateIntProperty("dimensionIDMars", 										"Dimension", 					29).getInt(29);

			idBlockMarsStone = configuration.getOrCreateIntProperty("idBlockMarsStone", 										configuration.CATEGORY_BLOCK, 	3500).getInt(3500);
			idBlockMarsGrass = configuration.getOrCreateIntProperty("idBlockMarsGrass", 										configuration.CATEGORY_BLOCK, 	3501).getInt(3501);
			idBlockMarsDirt = configuration.getOrCreateIntProperty("idBlockMarsDirt", 											configuration.CATEGORY_BLOCK, 	3502).getInt(3502);
			idBlockMarsCobblestone = configuration.getOrCreateIntProperty("idBlockMarsCobblestone", 							configuration.CATEGORY_BLOCK, 	3503).getInt(3503);
	        idBlockCreeperEgg = configuration.getOrCreateIntProperty("idBlockMarsCreeperEgg", 									configuration.CATEGORY_BLOCK, 	3504).getInt(3504);
	        idBlockCreeperDungeonWall = configuration.getOrCreateIntProperty("idBlockMarsCreeperDungeonWall", 					configuration.CATEGORY_BLOCK, 	3505).getInt(3505);
	        idBlockBacterialSludgeMoving = configuration.getOrCreateIntProperty("idBlockMarsBacterialSludgeMoving", 			configuration.CATEGORY_BLOCK, 	3506).getInt(3506);
	        idBlockBacterialSludgeStill = configuration.getOrCreateIntProperty("idBlockMarsBacterialSludgeStill",				configuration.CATEGORY_BLOCK, 	3507).getInt(3507);
	        idBlockOre = configuration.getOrCreateIntProperty("idBlockMarsOre", 												configuration.CATEGORY_BLOCK, 	3508).getInt(3508);

			idItemReinforcedBucket = configuration.getOrCreateIntProperty("idItemReinforcedBucket", 							configuration.CATEGORY_ITEM, 	9865).getInt(9865);
			idItemReinforcedBucketMilk = configuration.getOrCreateIntProperty("idItemReinforcedBucketMilk", 					configuration.CATEGORY_ITEM, 	9866).getInt(9866);
			idItemReinforcedBucketWater = configuration.getOrCreateIntProperty("idItemReinforcedBucketWater", 					configuration.CATEGORY_ITEM, 	9867).getInt(9867);
			idItemReinforcedBucketLava = configuration.getOrCreateIntProperty("idItemReinforcedBucketLava", 					configuration.CATEGORY_ITEM, 	9868).getInt(9868);
			idItemReinforcedBucketBacteria = configuration.getOrCreateIntProperty("idItemReinforcedBucketBacteria", 			configuration.CATEGORY_ITEM, 	9869).getInt(9869);
			idItemRawDesh = configuration.getOrCreateIntProperty("idItemRawDesh", 												configuration.CATEGORY_ITEM, 	9870).getInt(9870);
			idItemDeshStick = configuration.getOrCreateIntProperty("idItemDeshStick", 											configuration.CATEGORY_ITEM, 	9871).getInt(9871);
			idItemIngotQuandrium = configuration.getOrCreateIntProperty("idItemIngotQuandrium",									configuration.CATEGORY_ITEM, 	9872).getInt(9872);
			idItemIngotDesh = configuration.getOrCreateIntProperty("idItemIngotDesh", 											configuration.CATEGORY_ITEM, 	9873).getInt(9873);

			idToolPlanetStoneSword = configuration.getOrCreateIntProperty("idToolPlanetStoneSword", 							configuration.CATEGORY_ITEM, 	9877).getInt(9877);
			idToolPlanetStonePickaxe = configuration.getOrCreateIntProperty("idToolPlanetStonePickaxe", 						configuration.CATEGORY_ITEM, 	9878).getInt(9878);
			idToolPlanetStoneSpade = configuration.getOrCreateIntProperty("idToolPlanetStoneSpade", 							configuration.CATEGORY_ITEM, 	9879).getInt(9879);
			idToolPlanetStoneHoe = configuration.getOrCreateIntProperty("idToolPlanetStoneHoe", 								configuration.CATEGORY_ITEM, 	9880).getInt(9880);
			idToolPlanetStoneAxe = configuration.getOrCreateIntProperty("idToolPlanetStoneAxe", 								configuration.CATEGORY_ITEM, 	9881).getInt(9881);
			idToolDeshSword = configuration.getOrCreateIntProperty("idToolDeshSword", 											configuration.CATEGORY_ITEM, 	9882).getInt(9882);
			idToolDeshPickaxe = configuration.getOrCreateIntProperty("idToolDeshPickaxe", 										configuration.CATEGORY_ITEM, 	9883).getInt(9883);
			idToolDeshSpade = configuration.getOrCreateIntProperty("idToolDeshSpade", 											configuration.CATEGORY_ITEM, 	9884).getInt(9884);
			idToolDeshHoe = configuration.getOrCreateIntProperty("idToolDeshHoe", 												configuration.CATEGORY_ITEM, 	9885).getInt(9885);
			idToolDeshAxe = configuration.getOrCreateIntProperty("idToolDeshAxe", 												configuration.CATEGORY_ITEM, 	9886).getInt(9886);
			idToolQuandriumSword = configuration.getOrCreateIntProperty("idToolQuandriumSword", 								configuration.CATEGORY_ITEM, 	9887).getInt(9887);
			idToolQuandriumPickaxe = configuration.getOrCreateIntProperty("idToolQuandriumPickaxe", 							configuration.CATEGORY_ITEM, 	9888).getInt(9888);
			idToolQuandriumSpade = configuration.getOrCreateIntProperty("idToolQuandriumSpade", 								configuration.CATEGORY_ITEM, 	9889).getInt(9889);
			idToolQuandriumHoe = configuration.getOrCreateIntProperty("idToolQuandriumHoe", 									configuration.CATEGORY_ITEM, 	9890).getInt(9890);
			idToolQuandriumAxe = configuration.getOrCreateIntProperty("idToolQuandriumAxe", 									configuration.CATEGORY_ITEM, 	9891).getInt(9891);
			
			idArmorQuandriumHelmet = configuration.getOrCreateIntProperty("idArmorQuandriumHelmet", 							configuration.CATEGORY_ITEM, 	9897).getInt(9897);
			idArmorQuandriumChestplate = configuration.getOrCreateIntProperty("idArmorQuandriumChestplate", 					configuration.CATEGORY_ITEM, 	9898).getInt(9898);
			idArmorQuandriumLeggings = configuration.getOrCreateIntProperty("idArmorQuandriumLeggings", 						configuration.CATEGORY_ITEM, 	9899).getInt(9899);
			idArmorQuandriumBoots = configuration.getOrCreateIntProperty("idArmorQuandriumBoots", 								configuration.CATEGORY_ITEM, 	9900).getInt(9900);
			idArmorQuandriumHelmetBreathable = configuration.getOrCreateIntProperty("idArmorQuandriumHelmetBreathable", 		configuration.CATEGORY_ITEM, 	9901).getInt(9901);
			idArmorDeshHelmet = configuration.getOrCreateIntProperty("idArmorDeshHelmet", 										configuration.CATEGORY_ITEM, 	9902).getInt(9902);
			idArmorDeshChestplate = configuration.getOrCreateIntProperty("idArmorDeshChestplate", 								configuration.CATEGORY_ITEM, 	9903).getInt(9903);
			idArmorDeshLeggings = configuration.getOrCreateIntProperty("idArmorDeshLeggings", 									configuration.CATEGORY_ITEM, 	9904).getInt(9904);
			idArmorDeshBoots = configuration.getOrCreateIntProperty("idArmorDeshBoots", 										configuration.CATEGORY_ITEM, 	9905).getInt(9905);
			idArmorDeshHelmetBreathable = configuration.getOrCreateIntProperty("idArmorDeshHelmetBreathable", 					configuration.CATEGORY_ITEM, 	9906).getInt(9906);
			idArmorHeavyBoots = configuration.getOrCreateIntProperty("idArmorHeavyBoots", 										configuration.CATEGORY_ITEM, 	9912).getInt(9912);
			idArmorJetpack = configuration.getOrCreateIntProperty("idArmorJetpack", 											configuration.CATEGORY_ITEM, 	9913).getInt(9913);

			idEntityCreeperBoss = configuration.getOrCreateIntProperty("idEntityCreeperBoss", 									"ENTITIES", 					161).getInt(161); 	
			idEntitySludgeling = configuration.getOrCreateIntProperty("idEntitySludgeling", 									"ENTITIES", 					162).getInt(162); 	
			idEntityProjectileTNT = configuration.getOrCreateIntProperty("idEntityProjectileTNT", 								"ENTITIES", 					163).getInt(163); 	
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
