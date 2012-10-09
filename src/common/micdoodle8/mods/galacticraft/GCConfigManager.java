package micdoodle8.mods.galacticraft;

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
public class GCConfigManager 
{
	public static boolean loaded;
	
	static Configuration configuration;
	
	public GCConfigManager(File file)
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
	public static int idBlockBreatheableAir;
	public static int idBlockMarsStone;
	public static int idBlockMarsGrass;
	public static int idBlockMarsDirt;
	public static int idBlockMarsCobblestone;
	public static int idBlockCreeperEgg;
	public static int idBlockCreeperDungeonWall;
	public static int idBlockTreasureChest;
	public static int idBlockLandingPad;
	public static int idBlockBacterialSludgeMoving;
	public static int idBlockBacterialSludgeStill;
	public static int idBlockUnlitTorch;
	public static int idBlockUnlitTorchLit;
	public static int idBlockAirDistributor;
	public static int idBlockAirDistributorActive;
	public static int idBlockAirPipe;
	public static int idBlockOre;
	public static int idBlockAirCollector;

	// ITEMS
	public static int idItemLightOxygenTank;
	public static int idItemLightOxygenTankEmpty;
	public static int idItemMedOxygenTank;
	public static int idItemMedOxygenTankEmpty;
	public static int idItemHeavyOxygenTank;
	public static int idItemHeavyOxygenTankEmpty;
	public static int idItemGalacticOrb;
	public static int idItemSpaceship;
	public static int idItemReinforcedBucket;
	public static int idItemReinforcedBucketMilk;
	public static int idItemReinforcedBucketLava;
	public static int idItemReinforcedBucketWater;
	public static int idItemReinforcedBucketBacteria;
	public static int idItemRawDesh;
	public static int idItemDeshStick;
	public static int idItemIngotQuandrium;
	public static int idItemIngotDesh;
	public static int idItemIngotAluminum;
	public static int idItemIngotCopper;
	
	// ARMOR
	public static int idArmorOxygenMask;
	public static int idArmorSensorGlasses;
	public static int idArmorSensorGlassesWithOxygenMask;
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
	public static int idArmorTitaniumHelmet;
	public static int idArmorTitaniumChestplate;
	public static int idArmorTitaniumLeggings;
	public static int idArmorTitaniumBoots;
	public static int idArmorTitaniumHelmetBreathable;
	public static int idArmorHeavyBoots;

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
	public static int idToolTitaniumSword;
	public static int idToolTitaniumPickaxe;
	public static int idToolTitaniumAxe;
	public static int idToolTitaniumSpade;
	public static int idToolTitaniumHoe;
	
	// GUI
	public static int idGuiTankRefill;
	public static int idGuiAirDistributor;
	
	private void setDefaultValues()
    {
		try
		{
	        configuration.load();
	        
			dimensionIDMars = configuration.getOrCreateIntProperty("Mars Dimension ID", 										"Dimension", 					29).getInt(29);

			idBlockBreatheableAir = configuration.getOrCreateIntProperty("Breathable Air Block",								configuration.CATEGORY_BLOCK, 	202).getInt(202);
			idBlockMarsStone = configuration.getOrCreateIntProperty("idBlockMarsStone", 										configuration.CATEGORY_BLOCK, 	203).getInt(203);
			idBlockMarsGrass = configuration.getOrCreateIntProperty("idBlockMarsGrass", 										configuration.CATEGORY_BLOCK, 	204).getInt(204);
			idBlockMarsDirt = configuration.getOrCreateIntProperty("idBlockMarsDirt", 											configuration.CATEGORY_BLOCK, 	205).getInt(205);
			idBlockMarsCobblestone = configuration.getOrCreateIntProperty("idBlockMarsCobblestone", 							configuration.CATEGORY_BLOCK, 	206).getInt(206);
	        idBlockCreeperEgg = configuration.getOrCreateIntProperty("idBlockCreeperEgg", 										configuration.CATEGORY_BLOCK, 	207).getInt(207);
	        idBlockCreeperDungeonWall = configuration.getOrCreateIntProperty("idBlockCreeperDungeonWall", 						configuration.CATEGORY_BLOCK, 	208).getInt(208);
	        idBlockTreasureChest = configuration.getOrCreateIntProperty("idBlockTreasureChest",		 							configuration.CATEGORY_BLOCK, 	209).getInt(209);
	        idBlockLandingPad = configuration.getOrCreateIntProperty("idBlockLandingPad", 										configuration.CATEGORY_BLOCK, 	210).getInt(210);
	        idBlockBacterialSludgeMoving = configuration.getOrCreateIntProperty("idBlockBacterialSludgeMoving", 				configuration.CATEGORY_BLOCK, 	211).getInt(211);
	        idBlockBacterialSludgeStill = configuration.getOrCreateIntProperty("idBlockBacterialSludgeStill",					configuration.CATEGORY_BLOCK, 	212).getInt(212);
	        idBlockUnlitTorch = configuration.getOrCreateIntProperty("idBlockUnlitTorch", 										configuration.CATEGORY_BLOCK, 	213).getInt(213);
	        idBlockUnlitTorchLit = configuration.getOrCreateIntProperty("idBlockUnlitTorchLit", 								configuration.CATEGORY_BLOCK, 	214).getInt(214);
	        idBlockAirDistributor = configuration.getOrCreateIntProperty("idBlockAirDistributor", 								configuration.CATEGORY_BLOCK, 	215).getInt(215);
	        idBlockAirDistributorActive = configuration.getOrCreateIntProperty("idBlockAirDistributorActive", 					configuration.CATEGORY_BLOCK, 	216).getInt(216);
	        idBlockAirPipe = configuration.getOrCreateIntProperty("idBlockAirPipe", 											configuration.CATEGORY_BLOCK, 	217).getInt(217);
	        idBlockOre = configuration.getOrCreateIntProperty("idBlockOre", 													configuration.CATEGORY_BLOCK, 	218).getInt(218);
	        idBlockAirCollector = configuration.getOrCreateIntProperty("idBlockAirCollector", 									configuration.CATEGORY_BLOCK, 	219).getInt(219);

	        idItemLightOxygenTankEmpty = configuration.getOrCreateIntProperty("idItemLightOxygenTankEmpty", 					configuration.CATEGORY_ITEM, 	9854).getInt(9854);
	        idItemLightOxygenTank = configuration.getOrCreateIntProperty("idItemLightOxygenTank", 								configuration.CATEGORY_ITEM, 	9855).getInt(9855);
	        idItemMedOxygenTankEmpty = configuration.getOrCreateIntProperty("idItemMedOxygenTankEmpty", 						configuration.CATEGORY_ITEM, 	9856).getInt(9856);
	        idItemMedOxygenTank = configuration.getOrCreateIntProperty("idItemMedOxygenTank", 									configuration.CATEGORY_ITEM, 	9857).getInt(9857);
	        idItemHeavyOxygenTankEmpty = configuration.getOrCreateIntProperty("idItemHeavyOxygenTankEmpty", 					configuration.CATEGORY_ITEM, 	9858).getInt(9858);
	        idItemHeavyOxygenTank = configuration.getOrCreateIntProperty("idItemHeavyOxygenTank", 								configuration.CATEGORY_ITEM, 	9859).getInt(9859);
	        idItemGalacticOrb = configuration.getOrCreateIntProperty("idItemGalacticOrb", 										configuration.CATEGORY_ITEM, 	9860).getInt(9860);
	        idArmorOxygenMask = configuration.getOrCreateIntProperty("idArmorOxygenMask", 										configuration.CATEGORY_ITEM, 	9861).getInt(9861);
	        idItemSpaceship = configuration.getOrCreateIntProperty("idItemSpaceship", 											configuration.CATEGORY_ITEM, 	9862).getInt(9862);
			idArmorSensorGlasses = configuration.getOrCreateIntProperty("idItemSensorGlasses", 									configuration.CATEGORY_ITEM, 	9863).getInt(9863);
			idArmorSensorGlassesWithOxygenMask = configuration.getOrCreateIntProperty("idItemSensorGlassesWithOxygenMask", 		configuration.CATEGORY_ITEM, 	9864).getInt(9864);
			idItemReinforcedBucket = configuration.getOrCreateIntProperty("idItemReinforcedBucket", 							configuration.CATEGORY_ITEM, 	9865).getInt(9865);
			idItemReinforcedBucketMilk = configuration.getOrCreateIntProperty("idItemReinforcedBucketMilk", 					configuration.CATEGORY_ITEM, 	9866).getInt(9866);
			idItemReinforcedBucketWater = configuration.getOrCreateIntProperty("idItemReinforcedBucketWater", 					configuration.CATEGORY_ITEM, 	9867).getInt(9867);
			idItemReinforcedBucketLava = configuration.getOrCreateIntProperty("idItemReinforcedBucketLava", 					configuration.CATEGORY_ITEM, 	9868).getInt(9868);
			idItemReinforcedBucketBacteria = configuration.getOrCreateIntProperty("idItemReinforcedBucketBacteria", 			configuration.CATEGORY_ITEM, 	9869).getInt(9869);
			idItemRawDesh = configuration.getOrCreateIntProperty("idItemRawDesh", 												configuration.CATEGORY_ITEM, 	9870).getInt(9870);
			idItemDeshStick = configuration.getOrCreateIntProperty("idItemDeshStick", 											configuration.CATEGORY_ITEM, 	9871).getInt(9871);
			idItemIngotQuandrium = configuration.getOrCreateIntProperty("idItemDeshStick", 										configuration.CATEGORY_ITEM, 	9872).getInt(9872);
			idItemIngotDesh = configuration.getOrCreateIntProperty("idItemIngotDesh", 											configuration.CATEGORY_ITEM, 	9873).getInt(9873);
			idItemIngotAluminum = configuration.getOrCreateIntProperty("idItemIngotAluminum", 									configuration.CATEGORY_ITEM, 	9874).getInt(9874);
			idItemIngotCopper = configuration.getOrCreateIntProperty("idItemIngotCopper", 										configuration.CATEGORY_ITEM, 	9875).getInt(9875);

			idToolPlanetStoneSword = configuration.getOrCreateIntProperty("idToolPlanetStoneSword", 							configuration.CATEGORY_ITEM, 	9876).getInt(9876);
			idToolPlanetStonePickaxe = configuration.getOrCreateIntProperty("idToolPlanetStonePickaxe", 						configuration.CATEGORY_ITEM, 	9877).getInt(9877);
			idToolPlanetStoneSpade = configuration.getOrCreateIntProperty("idToolPlanetStoneSpade", 							configuration.CATEGORY_ITEM, 	9878).getInt(9878);
			idToolPlanetStoneHoe = configuration.getOrCreateIntProperty("idToolPlanetStoneHoe", 								configuration.CATEGORY_ITEM, 	9879).getInt(9879);
			idToolPlanetStoneAxe = configuration.getOrCreateIntProperty("idToolPlanetStoneAxe", 								configuration.CATEGORY_ITEM, 	9880).getInt(9880);
			idToolDeshSword = configuration.getOrCreateIntProperty("idToolDeshSword", 											configuration.CATEGORY_ITEM, 	9876 + 5).getInt(9876 + 5);
			idToolDeshPickaxe = configuration.getOrCreateIntProperty("idToolDeshPickaxe", 										configuration.CATEGORY_ITEM, 	9877 + 5).getInt(9877 + 5);
			idToolDeshSpade = configuration.getOrCreateIntProperty("idToolDeshSpade", 											configuration.CATEGORY_ITEM, 	9878 + 5).getInt(9878 + 5);
			idToolDeshHoe = configuration.getOrCreateIntProperty("idToolDeshHoe", 												configuration.CATEGORY_ITEM, 	9879 + 5).getInt(9879 + 5);
			idToolDeshAxe = configuration.getOrCreateIntProperty("idToolDeshAxe", 												configuration.CATEGORY_ITEM, 	9880 + 5).getInt(9880 + 5);
			idToolQuandriumSword = configuration.getOrCreateIntProperty("idToolQuandriumSword", 								configuration.CATEGORY_ITEM, 	9881 + 5).getInt(9881 + 5);
			idToolQuandriumPickaxe = configuration.getOrCreateIntProperty("idToolQuandriumPickaxe", 							configuration.CATEGORY_ITEM, 	9882 + 5).getInt(9882 + 5);
			idToolQuandriumSpade = configuration.getOrCreateIntProperty("idToolQuandriumSpade", 								configuration.CATEGORY_ITEM, 	9883 + 5).getInt(9883 + 5);
			idToolQuandriumHoe = configuration.getOrCreateIntProperty("idToolQuandriumHoe", 									configuration.CATEGORY_ITEM, 	9884 + 5).getInt(9884 + 5);
			idToolQuandriumAxe = configuration.getOrCreateIntProperty("idToolQuandriumAxe", 									configuration.CATEGORY_ITEM, 	9885 + 5).getInt(9885 + 5);
			idToolTitaniumSword = configuration.getOrCreateIntProperty("idToolTitaniumSword", 									configuration.CATEGORY_ITEM, 	9886 + 5).getInt(9886 + 5);
			idToolTitaniumPickaxe = configuration.getOrCreateIntProperty("idToolTitaniumPickaxe", 								configuration.CATEGORY_ITEM, 	9887 + 5).getInt(9887 + 5);
			idToolTitaniumSpade = configuration.getOrCreateIntProperty("idToolTitaniumSpade", 									configuration.CATEGORY_ITEM, 	9888 + 5).getInt(9888 + 5);
			idToolTitaniumHoe = configuration.getOrCreateIntProperty("idToolTitaniumHoe", 										configuration.CATEGORY_ITEM, 	9889 + 5).getInt(9889 + 5);
			idToolTitaniumAxe = configuration.getOrCreateIntProperty("idToolTitaniumAxe", 										configuration.CATEGORY_ITEM, 	9890 + 5).getInt(9890 + 5);
			
			idArmorQuandriumHelmet = configuration.getOrCreateIntProperty("idArmorQuandriumHelmet", 							configuration.CATEGORY_ITEM, 	9891 + 5).getInt(9891 + 5);
			idArmorQuandriumChestplate = configuration.getOrCreateIntProperty("idArmorQuandriumChestplate", 					configuration.CATEGORY_ITEM, 	9892 + 5).getInt(9892 + 5);
			idArmorQuandriumLeggings = configuration.getOrCreateIntProperty("idArmorQuandriumLeggings", 						configuration.CATEGORY_ITEM, 	9893 + 5).getInt(9893 + 5);
			idArmorQuandriumBoots = configuration.getOrCreateIntProperty("idArmorQuandriumBoots", 								configuration.CATEGORY_ITEM, 	9894 + 5).getInt(9894 + 5);
			idArmorQuandriumHelmetBreathable = configuration.getOrCreateIntProperty("idArmorQuandriumHelmetBreathable", 		configuration.CATEGORY_ITEM, 	9895 + 5).getInt(9895 + 5);
			idArmorDeshHelmet = configuration.getOrCreateIntProperty("idArmorDeshHelmet", 										configuration.CATEGORY_ITEM, 	9896 + 5).getInt(9896 + 5);
			idArmorDeshChestplate = configuration.getOrCreateIntProperty("idArmorDeshChestplate", 								configuration.CATEGORY_ITEM, 	9897 + 5).getInt(9897 + 5);
			idArmorDeshLeggings = configuration.getOrCreateIntProperty("idArmorDeshLeggings", 									configuration.CATEGORY_ITEM, 	9898 + 5).getInt(9898 + 5);
			idArmorDeshBoots = configuration.getOrCreateIntProperty("idArmorDeshBoots", 										configuration.CATEGORY_ITEM, 	9899 + 5).getInt(9899 + 5);
			idArmorDeshHelmetBreathable = configuration.getOrCreateIntProperty("idArmorDeshHelmetBreathable", 					configuration.CATEGORY_ITEM, 	9900 + 5).getInt(9900 + 5);
			idArmorTitaniumHelmet = configuration.getOrCreateIntProperty("idArmorTitaniumHelmet", 								configuration.CATEGORY_ITEM, 	9901 + 5).getInt(9901 + 5);
			idArmorTitaniumChestplate = configuration.getOrCreateIntProperty("idArmorTitaniumChestplate", 						configuration.CATEGORY_ITEM, 	9902 + 5).getInt(9902 + 5);
			idArmorTitaniumLeggings = configuration.getOrCreateIntProperty("idArmorTitaniumLeggings", 							configuration.CATEGORY_ITEM, 	9903 + 5).getInt(9903 + 5);
			idArmorTitaniumBoots = configuration.getOrCreateIntProperty("idArmorTitaniumBoots", 								configuration.CATEGORY_ITEM, 	9904 + 5).getInt(9904 + 5);
			idArmorTitaniumHelmetBreathable = configuration.getOrCreateIntProperty("idArmorTitaniumHelmetBreathable", 			configuration.CATEGORY_ITEM, 	9905 + 5).getInt(9905 + 5);
			idArmorHeavyBoots = configuration.getOrCreateIntProperty("idArmorHeavyBoots", 										configuration.CATEGORY_ITEM, 	9906 + 5).getInt(9906 + 5);
	        
	        idGuiTankRefill = configuration.getOrCreateIntProperty("GuiIdTankRefill", 											"GUI", 							128).getInt(128);
	        idGuiAirDistributor = configuration.getOrCreateIntProperty("idGuiAirDistributor", 									"GUI", 							129).getInt(129);
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
