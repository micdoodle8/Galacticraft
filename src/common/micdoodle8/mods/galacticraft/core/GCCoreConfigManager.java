package micdoodle8.mods.galacticraft.core;

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
public class GCCoreConfigManager 
{
	public static boolean loaded;
	
	static Configuration configuration;
	
	public GCCoreConfigManager(File file)
	{
		if (!loaded)
		{
			configuration = new Configuration(file);
			setDefaultValues();
		}
	}

	// BLOCKS
	public static int idBlockBreatheableAir;
	public static int idBlockTreasureChest;
	public static int idBlockLandingPad;
	public static int idBlockUnlitTorch;
	public static int idBlockUnlitTorchLit;
	public static int idBlockAirDistributor;
	public static int idBlockAirDistributorActive;
	public static int idBlockAirPipe;
	public static int idBlockAirCollector;
	public static int idBlockOre;
	public static int idBlockSapling2;
	public static int idBlockRocketBench;

	// ITEMS
	public static int idItemLightOxygenTank;
	public static int idItemLightOxygenTankEmpty;
	public static int idItemMedOxygenTank;
	public static int idItemMedOxygenTankEmpty;
	public static int idItemHeavyOxygenTank;
	public static int idItemHeavyOxygenTankEmpty;
	public static int idItemSpaceship;
	public static int idItemIngotTitanium;
	public static int idItemIngotCopper;
	public static int idItemIngotAluminum;
	public static int idItemAluminumCanister;
	public static int idItemAirVent;
	public static int idItemOxygenConcentrator;
	public static int idItemFan;
	public static int idItemGravityBow;
	public static int idItemRocketEngine;
	public static int idItemHeavyPlate;
	public static int idItemRocketNoseCone;
	public static int idItemRocketFins;
	
	// ARMOR
	public static int idArmorOxygenMask;
	public static int idArmorSensorGlasses;
	public static int idArmorSensorGlassesWithOxygenMask;
	public static int idArmorTitaniumHelmet;
	public static int idArmorTitaniumChestplate;
	public static int idArmorTitaniumLeggings;
	public static int idArmorTitaniumBoots;
	public static int idArmorTitaniumHelmetBreathable;

	// TOOLS
	public static int idToolTitaniumSword;
	public static int idToolTitaniumPickaxe;
	public static int idToolTitaniumAxe;
	public static int idToolTitaniumSpade;
	public static int idToolTitaniumHoe;
	
	// GUI
	public static int idGuiTankRefill;
	public static int idGuiAirDistributor;
	public static int idGuiRocketCraftingBench;
	
	// ACHIEVEMENTS
	public static int idAchievBase;
	
	public static int idEntityEvolvedSpider;
	public static int idEntityEvolvedCreeper;
	public static int idEntityEvolvedZombie;
	public static int idEntityEvolvedSkeleton;
	public static int idEntitySpaceship;
	public static int idEntityAntiGravityArrow;
	public static int idEntityMeteor;
	
	private void setDefaultValues()
    {
		try
		{
	        configuration.load();
	        
			idBlockBreatheableAir = configuration.getOrCreateIntProperty("idBlockCoreBreatheableAir",							configuration.CATEGORY_BLOCK, 	3350).getInt(3350);
	        idBlockTreasureChest = configuration.getOrCreateIntProperty("idBlockCoreTreasureChest",		 						configuration.CATEGORY_BLOCK, 	3351).getInt(3351);
	        idBlockLandingPad = configuration.getOrCreateIntProperty("idBlockCoreLandingPad", 									configuration.CATEGORY_BLOCK, 	3352).getInt(3352);
	        idBlockUnlitTorch = configuration.getOrCreateIntProperty("idBlockCoreUnlitTorch", 									configuration.CATEGORY_BLOCK, 	3353).getInt(3353);
	        idBlockUnlitTorchLit = configuration.getOrCreateIntProperty("idBlockCoreUnlitTorchLit", 							configuration.CATEGORY_BLOCK, 	3354).getInt(3354);
	        idBlockAirDistributor = configuration.getOrCreateIntProperty("idBlockCoreAirDistributor", 							configuration.CATEGORY_BLOCK, 	3355).getInt(3355);
	        idBlockAirDistributorActive = configuration.getOrCreateIntProperty("idBlockCoreAirDistributorActive", 				configuration.CATEGORY_BLOCK, 	3356).getInt(3356);
	        idBlockAirPipe = configuration.getOrCreateIntProperty("idBlockCoreAirPipe", 										configuration.CATEGORY_BLOCK, 	3357).getInt(3357);
	        idBlockAirCollector = configuration.getOrCreateIntProperty("idBlockCoreAirCollector", 								configuration.CATEGORY_BLOCK, 	3358).getInt(3358);
	        idBlockOre = configuration.getOrCreateIntProperty("idBlockCoreOre", 												configuration.CATEGORY_BLOCK, 	3359).getInt(3359);
	        idBlockSapling2 = configuration.getOrCreateIntProperty("idBlockCoreSapling2", 										configuration.CATEGORY_BLOCK, 	3360).getInt(3360);
	        idBlockRocketBench = configuration.getOrCreateIntProperty("idBlockRocketBench", 									configuration.CATEGORY_BLOCK, 	3361).getInt(3361);

	        idItemLightOxygenTankEmpty = configuration.getOrCreateIntProperty("idItemLightOxygenTankEmpty", 					configuration.CATEGORY_ITEM, 	9854).getInt(9854);
	        idItemLightOxygenTank = configuration.getOrCreateIntProperty("idItemLightOxygenTank", 								configuration.CATEGORY_ITEM, 	9855).getInt(9855);
	        idItemMedOxygenTankEmpty = configuration.getOrCreateIntProperty("idItemMedOxygenTankEmpty", 						configuration.CATEGORY_ITEM, 	9856).getInt(9856);
	        idItemMedOxygenTank = configuration.getOrCreateIntProperty("idItemMedOxygenTank", 									configuration.CATEGORY_ITEM, 	9857).getInt(9857);
	        idItemHeavyOxygenTankEmpty = configuration.getOrCreateIntProperty("idItemHeavyOxygenTankEmpty", 					configuration.CATEGORY_ITEM, 	9858).getInt(9858);
	        idItemHeavyOxygenTank = configuration.getOrCreateIntProperty("idItemHeavyOxygenTank", 								configuration.CATEGORY_ITEM, 	9859).getInt(9859);
	        idArmorOxygenMask = configuration.getOrCreateIntProperty("idArmorOxygenMask", 										configuration.CATEGORY_ITEM, 	9861).getInt(9861);
	        idItemSpaceship = configuration.getOrCreateIntProperty("idItemSpaceship", 											configuration.CATEGORY_ITEM, 	9862).getInt(9862);
			idArmorSensorGlasses = configuration.getOrCreateIntProperty("idItemSensorGlasses", 									configuration.CATEGORY_ITEM, 	9863).getInt(9863);
			idArmorSensorGlassesWithOxygenMask = configuration.getOrCreateIntProperty("idItemSensorGlassesWithOxygenMask", 		configuration.CATEGORY_ITEM, 	9864).getInt(9864);
			idItemIngotTitanium = configuration.getOrCreateIntProperty("idItemIngotTitanium", 									configuration.CATEGORY_ITEM, 	9874).getInt(9874);
			idItemIngotCopper = configuration.getOrCreateIntProperty("idItemIngotCopper", 										configuration.CATEGORY_ITEM, 	9875).getInt(9875);
			idItemIngotAluminum = configuration.getOrCreateIntProperty("idItemIngotAluminum", 									configuration.CATEGORY_ITEM, 	9876).getInt(9876);
			idItemAluminumCanister = configuration.getOrCreateIntProperty("idItemAluminumCanister", 							configuration.CATEGORY_ITEM, 	9914).getInt(9914);
			idItemAirVent = configuration.getOrCreateIntProperty("idItemAirVent", 												configuration.CATEGORY_ITEM, 	9915).getInt(9915);
			idItemOxygenConcentrator = configuration.getOrCreateIntProperty("idItemOxygenConcentrator", 						configuration.CATEGORY_ITEM, 	9916).getInt(9916);
			idItemFan = configuration.getOrCreateIntProperty("idItemFan", 														configuration.CATEGORY_ITEM, 	9917).getInt(9917);
			idItemGravityBow = configuration.getOrCreateIntProperty("idGravityBow", 											configuration.CATEGORY_ITEM, 	9918).getInt(9918);
			idItemRocketEngine = configuration.getOrCreateIntProperty("idItemRocketEngine", 									configuration.CATEGORY_ITEM, 	9919).getInt(9919);
			idItemHeavyPlate = configuration.getOrCreateIntProperty("idItemHeavyPlate", 										configuration.CATEGORY_ITEM, 	9920).getInt(9920);
			idItemRocketNoseCone = configuration.getOrCreateIntProperty("idItemRocketNoseCone", 								configuration.CATEGORY_ITEM, 	9921).getInt(9921);
			idItemRocketFins = configuration.getOrCreateIntProperty("idItemRocketFins", 										configuration.CATEGORY_ITEM, 	9922).getInt(9922);

			idToolTitaniumSword = configuration.getOrCreateIntProperty("idToolTitaniumSword", 									configuration.CATEGORY_ITEM, 	9892).getInt(9892);
			idToolTitaniumPickaxe = configuration.getOrCreateIntProperty("idToolTitaniumPickaxe", 								configuration.CATEGORY_ITEM, 	9893).getInt(9893);
			idToolTitaniumSpade = configuration.getOrCreateIntProperty("idToolTitaniumSpade", 									configuration.CATEGORY_ITEM, 	9894).getInt(9894);
			idToolTitaniumHoe = configuration.getOrCreateIntProperty("idToolTitaniumHoe", 										configuration.CATEGORY_ITEM, 	9895).getInt(9895);
			idToolTitaniumAxe = configuration.getOrCreateIntProperty("idToolTitaniumAxe", 										configuration.CATEGORY_ITEM, 	9896).getInt(9896);
			
			idArmorTitaniumHelmet = configuration.getOrCreateIntProperty("idArmorTitaniumHelmet", 								configuration.CATEGORY_ITEM, 	9907).getInt(9907);
			idArmorTitaniumChestplate = configuration.getOrCreateIntProperty("idArmorTitaniumChestplate", 						configuration.CATEGORY_ITEM, 	9908).getInt(9908);
			idArmorTitaniumLeggings = configuration.getOrCreateIntProperty("idArmorTitaniumLeggings", 							configuration.CATEGORY_ITEM, 	9909).getInt(9909);
			idArmorTitaniumBoots = configuration.getOrCreateIntProperty("idArmorTitaniumBoots", 								configuration.CATEGORY_ITEM, 	9910).getInt(9910);
			idArmorTitaniumHelmetBreathable = configuration.getOrCreateIntProperty("idArmorTitaniumHelmetBreathable", 			configuration.CATEGORY_ITEM, 	9911).getInt(9911);
			
	        idGuiTankRefill = configuration.getOrCreateIntProperty("GuiIdTankRefill", 											"GUI", 							128).getInt(128);
	        idGuiAirDistributor = configuration.getOrCreateIntProperty("idGuiAirDistributor", 									"GUI", 							129).getInt(129);
	        idGuiRocketCraftingBench = configuration.getOrCreateIntProperty("idGuiRocketCraftingBench", 						"GUI", 							130).getInt(130);
	        
	        idAchievBase = configuration.getOrCreateIntProperty("idAchievBase", 												"Achievements", 				1784).getInt(1784);

	        idEntityEvolvedSpider = configuration.getOrCreateIntProperty("idEntityEvolvedSpider", 								"ENTITIES", 					155).getInt(155); 	
	        idEntityEvolvedCreeper = configuration.getOrCreateIntProperty("idEntityEvolvedCreeper", 							"ENTITIES", 					156).getInt(156); 	
	        idEntityEvolvedZombie = configuration.getOrCreateIntProperty("idEntityEvolvedZombie", 								"ENTITIES", 					157).getInt(157); 	
	        idEntityEvolvedSkeleton = configuration.getOrCreateIntProperty("idEntityEvolvedSkeleton", 							"ENTITIES", 					158).getInt(158); 	
	        idEntitySpaceship = configuration.getOrCreateIntProperty("idEntitySpaceship", 										"ENTITIES", 					159).getInt(159); 	
	        idEntityAntiGravityArrow = configuration.getOrCreateIntProperty("idEntityAntiGravityArrow", 						"ENTITIES", 					160).getInt(160); 	
	        idEntityMeteor = configuration.getOrCreateIntProperty("idEntityMeteor", 											"ENTITIES", 					161).getInt(161); 	
		}
		catch (Exception e)
		{
			FMLLog.log(Level.SEVERE, e, "Galacticraft Core has a problem loading it's configuration");
		}
		finally 
		{
			configuration.save();
			loaded = true;
		}
    }
}
