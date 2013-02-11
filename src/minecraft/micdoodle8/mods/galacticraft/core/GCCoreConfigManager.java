package micdoodle8.mods.galacticraft.core;

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
public class GCCoreConfigManager 
{
	public static boolean loaded;
	
	static Configuration configuration;
	
	public GCCoreConfigManager(File file)
	{
		if (!loaded)
		{
			configuration = new Configuration(file);
			this.setDefaultValues();
		}
	}

	// BLOCKS
	public static int idBlockID;
	
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
	public static int idBlockFallenMeteor;
	public static int idBlockDecorationBlock;

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
	public static int idItemSensorLens;
	public static int idItemBuggy;
	public static int idItemFlag;
	public static int idItemOxygenGear;
	public static int idItemCanvas;
	public static int idItemParachute;
	public static int idItemRocketFuelBucket;
	
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
	public static int idGuiBuggyCraftingBench;
	public static int idGuiGalaxyMap;
	public static int idGuiSpaceshipInventory;
	
	// ACHIEVEMENTS
	public static int idAchievBase;
	
	public static int idEntityEvolvedSpider;
	public static int idEntityEvolvedCreeper;
	public static int idEntityEvolvedZombie;
	public static int idEntityEvolvedSkeleton;
	public static int idEntitySpaceship;
	public static int idEntityAntiGravityArrow;
	public static int idEntityMeteor;
	public static int idEntityBuggy;
	public static int idEntityFlag;
	public static int idEntityAstroOrb;
	public static int idEntityGiantWorm;
	public static int idEntityParaChest;
	
	// GENERAL
	public static boolean transparentBreathableAir;
	public static boolean moreStars;
	public static boolean wasdMapMovement;
	
	private void setDefaultValues()
    {
		try
		{
	        configuration.load();
	        
	        idBlockID = 							configuration.get(Configuration.CATEGORY_BLOCK, "idBlock", 								3349)		.getInt(3349);
	        
	        idBlockBreatheableAir = 				configuration.get(Configuration.CATEGORY_BLOCK, "idBlockCoreBreatheableAir", 			3350)		.getInt(3350);
	        idBlockTreasureChest = 					configuration.get(Configuration.CATEGORY_BLOCK, "idBlockTreasureChest", 				3351)		.getInt(3351);
	        idBlockLandingPad = 					configuration.get(Configuration.CATEGORY_BLOCK, "idBlockLandingPad", 					3352)		.getInt(3352);
	        idBlockUnlitTorch = 					configuration.get(Configuration.CATEGORY_BLOCK, "idBlockUnlitTorch", 					3353)		.getInt(3353);
	        idBlockUnlitTorchLit = 					configuration.get(Configuration.CATEGORY_BLOCK, "idBlockUnlitTorchLit", 				3354)		.getInt(3354);
	        idBlockAirDistributor = 				configuration.get(Configuration.CATEGORY_BLOCK, "idBlockAirDistributor", 				3355)		.getInt(3355);
	        idBlockAirDistributorActive = 			configuration.get(Configuration.CATEGORY_BLOCK, "idBlockAirDistributorActive", 			3356)		.getInt(3356);
	        idBlockAirPipe = 						configuration.get(Configuration.CATEGORY_BLOCK, "idBlockAirPipe", 						3357)		.getInt(3357);
	        idBlockAirCollector = 					configuration.get(Configuration.CATEGORY_BLOCK, "idBlockAirCollector", 					3358)		.getInt(3358);
	        idBlockOre = 							configuration.get(Configuration.CATEGORY_BLOCK, "idBlockOre", 							3359)		.getInt(3359);
	        idBlockSapling2 = 						configuration.get(Configuration.CATEGORY_BLOCK, "idBlockSapling2", 						3360)		.getInt(3360);
	        idBlockRocketBench = 					configuration.get(Configuration.CATEGORY_BLOCK, "idBlockRocketBench", 					3361)		.getInt(3361);
	        idBlockFallenMeteor = 					configuration.get(Configuration.CATEGORY_BLOCK, "idBlockFallenMeteor", 					3362)		.getInt(3362);

	        idBlockDecorationBlock = 				configuration.get(Configuration.CATEGORY_BLOCK, "idBlockDecorationBlock",				2362)		.getInt(2362);

	        idItemLightOxygenTankEmpty = 			configuration.get(Configuration.CATEGORY_ITEM, "idItemLightOxygenTankEmpty", 			9854)		.getInt(9854);
	        idItemLightOxygenTank = 				configuration.get(Configuration.CATEGORY_ITEM, "idItemLightOxygenTank", 				9855)		.getInt(9855);
	        idItemMedOxygenTankEmpty = 				configuration.get(Configuration.CATEGORY_ITEM, "idItemMedOxygenTankEmpty", 			9856)		.getInt(9856);
	        idItemMedOxygenTank = 					configuration.get(Configuration.CATEGORY_ITEM, "idItemMedOxygenTank", 					9857)		.getInt(9857);
	        idItemHeavyOxygenTank = 				configuration.get(Configuration.CATEGORY_ITEM, "idItemHeavyOxygenTank", 				9858)		.getInt(9858);
	        idItemHeavyOxygenTankEmpty = 			configuration.get(Configuration.CATEGORY_ITEM, "idItemHeavyOxygenTankEmpty",			9859)		.getInt(9859);
	        idArmorOxygenMask = 					configuration.get(Configuration.CATEGORY_ITEM, "idArmorOxygenMask", 					9860)		.getInt(9860);
	        idItemSpaceship = 						configuration.get(Configuration.CATEGORY_ITEM, "idItemSpaceship", 						9861)		.getInt(9861);
	        idArmorSensorGlasses = 					configuration.get(Configuration.CATEGORY_ITEM, "idArmorSensorGlasses", 				9862)		.getInt(9862);
	        idArmorSensorGlassesWithOxygenMask = 	configuration.get(Configuration.CATEGORY_ITEM, "idArmorSensorGlassesWithOxygenMask", 	9863)		.getInt(9863);
	        idItemIngotTitanium = 					configuration.get(Configuration.CATEGORY_ITEM, "idItemIngotTitanium", 					9864)		.getInt(9864);
	        idItemIngotCopper = 					configuration.get(Configuration.CATEGORY_ITEM, "idItemIngotCopper", 					9865)		.getInt(9865);
	        idItemIngotAluminum = 					configuration.get(Configuration.CATEGORY_ITEM, "idItemIngotAluminum", 					9866)		.getInt(9866);
	        idItemAluminumCanister = 				configuration.get(Configuration.CATEGORY_ITEM, "idItemAluminumCanister", 				9867)		.getInt(9867);
	        idItemAirVent = 						configuration.get(Configuration.CATEGORY_ITEM, "idItemAirVent", 						9868)		.getInt(9868);
	        idItemOxygenConcentrator = 				configuration.get(Configuration.CATEGORY_ITEM, "idItemOxygenConcentrator", 			9869)		.getInt(9869);
	        idItemFan = 							configuration.get(Configuration.CATEGORY_ITEM, "idItemFan", 							9870)		.getInt(9870);
	        idItemGravityBow = 						configuration.get(Configuration.CATEGORY_ITEM, "idItemGravityBow", 					9871)		.getInt(9871);
	        idItemRocketEngine = 					configuration.get(Configuration.CATEGORY_ITEM, "idItemRocketEngine", 					9872)		.getInt(9872);
	        idItemHeavyPlate = 						configuration.get(Configuration.CATEGORY_ITEM, "idItemHeavyPlate", 					9873)		.getInt(9873);
	        idItemRocketNoseCone = 					configuration.get(Configuration.CATEGORY_ITEM, "idItemRocketNoseCone", 				9874)		.getInt(9874);
	        idItemRocketFins = 						configuration.get(Configuration.CATEGORY_ITEM, "idItemRocketFins", 					9875)		.getInt(9875);
	        idItemSensorLens = 						configuration.get(Configuration.CATEGORY_ITEM, "idItemSensorLens", 					9886)		.getInt(9886);
	        idItemBuggy = 							configuration.get(Configuration.CATEGORY_ITEM, "idItemBuggyPlacer", 					9887)		.getInt(9887);
	        idItemFlag = 							configuration.get(Configuration.CATEGORY_ITEM, "idItemFlagPlacer", 					9888)		.getInt(9888);
	        idItemOxygenGear = 						configuration.get(Configuration.CATEGORY_ITEM, "idItemOxygenGear", 					9889)		.getInt(9889);
	        idItemCanvas = 							configuration.get(Configuration.CATEGORY_ITEM, "idItemCanvas", 						9890)		.getInt(9890);
	        idItemParachute = 						configuration.get(Configuration.CATEGORY_ITEM, "idItemParachute", 					9891)		.getInt(9891);
	        idItemRocketFuelBucket = 				configuration.get(Configuration.CATEGORY_ITEM, "idItemRocketFuelBucket", 			9892)		.getInt(9892);
	        
	        idToolTitaniumSword = 					configuration.get(Configuration.CATEGORY_BLOCK, "idToolTitaniumSword", 					9876)		.getInt(9876);
	        idToolTitaniumPickaxe = 				configuration.get(Configuration.CATEGORY_BLOCK, "idToolTitaniumPickaxe", 				9877)		.getInt(9877);
	        idToolTitaniumSpade = 					configuration.get(Configuration.CATEGORY_BLOCK, "idToolTitaniumSpade", 					9878)		.getInt(9878);
	        idToolTitaniumHoe = 					configuration.get(Configuration.CATEGORY_BLOCK, "idToolTitaniumHoe", 					9879)		.getInt(9879);
	        idToolTitaniumAxe = 					configuration.get(Configuration.CATEGORY_BLOCK, "idToolTitaniumAxe", 					9880)		.getInt(9880);

	        idArmorTitaniumHelmet = 				configuration.get(Configuration.CATEGORY_BLOCK, "idArmorTitaniumHelmet", 				9881)		.getInt(9881);
	        idArmorTitaniumChestplate = 			configuration.get(Configuration.CATEGORY_BLOCK, "idArmorTitaniumChestplate", 			9882)		.getInt(9882);
	        idArmorTitaniumLeggings = 				configuration.get(Configuration.CATEGORY_BLOCK, "idArmorTitaniumLeggings", 				9883)		.getInt(9883);
	        idArmorTitaniumBoots = 					configuration.get(Configuration.CATEGORY_BLOCK, "idArmorTitaniumBoots", 				9884)		.getInt(9884);
	        idArmorTitaniumHelmetBreathable = 		configuration.get(Configuration.CATEGORY_BLOCK, "idArmorTitaniumHelmetBreathable", 		9885)		.getInt(9885);

	        idGuiTankRefill = 						configuration.get("GUI", "idGuiTankRefill", 											128)		.getInt(128);
	        idGuiAirDistributor = 					configuration.get("GUI", "idGuiAirDistributor", 										129)		.getInt(129);
	        idGuiRocketCraftingBench = 				configuration.get("GUI", "idGuiRocketCraftingBench", 									130)		.getInt(130);
	        idGuiBuggyCraftingBench = 				configuration.get("GUI", "idGuiBuggyCraftingBench", 									131)		.getInt(131);
	        idGuiGalaxyMap = 						configuration.get("GUI", "idGuiGalaxyMap",			 									132)		.getInt(132);
	        idGuiSpaceshipInventory = 				configuration.get("GUI", "idGuiSpaceshipInventory",			 							133)		.getInt(133);
	        
	        idAchievBase = 							configuration.get("Achievements", "idAchievBase", 										1784)		.getInt(1784);

	        idEntityEvolvedSpider = 				configuration.get("Entities", "idEntityEvolvedSpider", 									155)		.getInt(155);
	        idEntityEvolvedCreeper = 				configuration.get("Entities", "idEntityEvolvedCreeper", 								156)		.getInt(156);
	        idEntityEvolvedZombie = 				configuration.get("Entities", "idEntityEvolvedZombie", 									157)		.getInt(157);
	        idEntityEvolvedSkeleton = 				configuration.get("Entities", "idEntityEvolvedSkeleton", 								158)		.getInt(158);
	        idEntitySpaceship = 					configuration.get("Entities", "idEntitySpaceship", 										159)		.getInt(159);
	        idEntityAntiGravityArrow = 				configuration.get("Entities", "idEntityAntiGravityArrow", 								160)		.getInt(160);
	        idEntityMeteor = 						configuration.get("Entities", "idEntityMeteor", 										161)		.getInt(161);
	        idEntityBuggy = 						configuration.get("Entities", "idEntityBuggy", 											162)		.getInt(162);
	        idEntityFlag = 							configuration.get("Entities", "idEntityFlag", 											163)		.getInt(163);
	        idEntityAstroOrb = 						configuration.get("Entities", "idEntityAstroOrb", 										164)		.getInt(164);
	        idEntityGiantWorm = 					configuration.get("Entities", "idEntityGiantWorm", 										165)		.getInt(165);
	        idEntityParaChest = 					configuration.get("Entities", "idEntityParaChest", 										166)		.getInt(166);
	        
	        transparentBreathableAir = 				configuration.get(Configuration.CATEGORY_GENERAL, "transparentBreathableAir", 			true)		.getBoolean(true);
	        moreStars = 							configuration.get(Configuration.CATEGORY_GENERAL, "moreStars", 							true)		.getBoolean(true);
	        wasdMapMovement = 						configuration.get(Configuration.CATEGORY_GENERAL, "WASD Map Movement", 					true)		.getBoolean(true);
		}
		catch (final Exception e)
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
