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
		if (!GCCoreConfigManager.loaded)
		{
			GCCoreConfigManager.configuration = new Configuration(file);
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
	public static int idBlockAirLockFrame;
	public static int idBlockAirLockSeal;

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
	public static int idItemFlagPole;
	
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
	public static boolean disableAluminiumEarth;
	public static boolean disableCopperEarth;
	public static boolean disableTitaniumEarth;
	public static int oreGenFactor;
	public static int[] oreGenDimensions;
	public static boolean disableSpaceshipParticles;
	public static boolean disableSpaceshipGrief;
	
	private void setDefaultValues()
    {
		try
		{
	        GCCoreConfigManager.configuration.load();
	        
	        GCCoreConfigManager.idBlockID = 							GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlock", 								3349)		.getInt(3349);
	        
	        GCCoreConfigManager.idBlockBreatheableAir = 				GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockCoreBreatheableAir", 			3350)		.getInt(3350);
	        GCCoreConfigManager.idBlockTreasureChest = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockTreasureChest", 				3351)		.getInt(3351);
	        GCCoreConfigManager.idBlockLandingPad = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockLandingPad", 					3352)		.getInt(3352);
	        GCCoreConfigManager.idBlockUnlitTorch = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockUnlitTorch", 					3353)		.getInt(3353);
	        GCCoreConfigManager.idBlockUnlitTorchLit = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockUnlitTorchLit", 				3354)		.getInt(3354);
	        GCCoreConfigManager.idBlockAirDistributor = 				GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockAirDistributor", 				3355)		.getInt(3355);
	        GCCoreConfigManager.idBlockAirDistributorActive = 			GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockAirDistributorActive", 			3356)		.getInt(3356);
	        GCCoreConfigManager.idBlockAirPipe = 						GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockAirPipe", 						3357)		.getInt(3357);
	        GCCoreConfigManager.idBlockAirCollector = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockAirCollector", 					3358)		.getInt(3358);
	        GCCoreConfigManager.idBlockOre = 							GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockOre", 							3359)		.getInt(3359);
	        GCCoreConfigManager.idBlockSapling2 = 						GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockSapling2", 						3360)		.getInt(3360);
	        GCCoreConfigManager.idBlockRocketBench = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockRocketBench", 					3361)		.getInt(3361);
	        GCCoreConfigManager.idBlockFallenMeteor = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockFallenMeteor", 					3362)		.getInt(3362);
	        GCCoreConfigManager.idBlockDecorationBlock = 				GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockDecorationBlock",				3363)		.getInt(3363);
	        GCCoreConfigManager.idBlockAirLockFrame = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockAirLockFrame",					3364)		.getInt(3364);
	        GCCoreConfigManager.idBlockAirLockSeal = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockAirLockSeal",					3365)		.getInt(3365);
	        
	        GCCoreConfigManager.idItemLightOxygenTankEmpty = 			GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemLightOxygenTankEmpty", 			9854)		.getInt(9854);
	        GCCoreConfigManager.idItemLightOxygenTank = 				GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemLightOxygenTank", 				9855)		.getInt(9855);
	        GCCoreConfigManager.idItemMedOxygenTankEmpty = 				GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemMedOxygenTankEmpty", 			9856)		.getInt(9856);
	        GCCoreConfigManager.idItemMedOxygenTank = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemMedOxygenTank", 					9857)		.getInt(9857);
	        GCCoreConfigManager.idItemHeavyOxygenTank = 				GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemHeavyOxygenTank", 				9858)		.getInt(9858);
	        GCCoreConfigManager.idItemHeavyOxygenTankEmpty = 			GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemHeavyOxygenTankEmpty",			9859)		.getInt(9859);
	        GCCoreConfigManager.idArmorOxygenMask = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idArmorOxygenMask", 					9860)		.getInt(9860);
	        GCCoreConfigManager.idItemSpaceship = 						GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemSpaceship", 						9861)		.getInt(9861);
	        GCCoreConfigManager.idArmorSensorGlasses = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idArmorSensorGlasses", 				9862)		.getInt(9862);
	        GCCoreConfigManager.idArmorSensorGlassesWithOxygenMask = 	GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idArmorSensorGlassesWithOxygenMask", 	9863)		.getInt(9863);
	        GCCoreConfigManager.idItemIngotTitanium = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemIngotTitanium", 					9864)		.getInt(9864);
	        GCCoreConfigManager.idItemIngotCopper = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemIngotCopper", 					9865)		.getInt(9865);
	        GCCoreConfigManager.idItemIngotAluminum = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemIngotAluminum", 					9866)		.getInt(9866);
	        GCCoreConfigManager.idItemAluminumCanister = 				GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemAluminumCanister", 				9867)		.getInt(9867);
	        GCCoreConfigManager.idItemAirVent = 						GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemAirVent", 						9868)		.getInt(9868);
	        GCCoreConfigManager.idItemOxygenConcentrator = 				GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemOxygenConcentrator", 			9869)		.getInt(9869);
	        GCCoreConfigManager.idItemFan = 							GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemFan", 							9870)		.getInt(9870);
	        GCCoreConfigManager.idItemGravityBow = 						GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemGravityBow", 					9871)		.getInt(9871);
	        GCCoreConfigManager.idItemRocketEngine = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemRocketEngine", 					9872)		.getInt(9872);
	        GCCoreConfigManager.idItemHeavyPlate = 						GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemHeavyPlate", 					9873)		.getInt(9873);
	        GCCoreConfigManager.idItemRocketNoseCone = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemRocketNoseCone", 				9874)		.getInt(9874);
	        GCCoreConfigManager.idItemRocketFins = 						GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemRocketFins", 					9875)		.getInt(9875);
	        GCCoreConfigManager.idItemSensorLens = 						GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemSensorLens", 					9886)		.getInt(9886);
	        GCCoreConfigManager.idItemBuggy = 							GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemBuggyPlacer", 					9887)		.getInt(9887);
	        GCCoreConfigManager.idItemFlag = 							GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemFlagPlacer", 					9888)		.getInt(9888);
	        GCCoreConfigManager.idItemOxygenGear = 						GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemOxygenGear", 					9889)		.getInt(9889);
	        GCCoreConfigManager.idItemCanvas = 							GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemCanvas", 						9890)		.getInt(9890);
	        GCCoreConfigManager.idItemParachute = 						GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemParachute", 					9891)		.getInt(9891);
	        GCCoreConfigManager.idItemRocketFuelBucket = 				GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemRocketFuelBucket", 			9892)		.getInt(9892);
	        GCCoreConfigManager.idItemFlagPole = 						GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemFlagPole", 					9893)		.getInt(9893);
	        
	        GCCoreConfigManager.idToolTitaniumSword = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idToolTitaniumSword", 					9876)		.getInt(9876);
	        GCCoreConfigManager.idToolTitaniumPickaxe = 				GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idToolTitaniumPickaxe", 				9877)		.getInt(9877);
	        GCCoreConfigManager.idToolTitaniumSpade = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idToolTitaniumSpade", 					9878)		.getInt(9878);
	        GCCoreConfigManager.idToolTitaniumHoe = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idToolTitaniumHoe", 					9879)		.getInt(9879);
	        GCCoreConfigManager.idToolTitaniumAxe = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idToolTitaniumAxe", 					9880)		.getInt(9880);

	        GCCoreConfigManager.idArmorTitaniumHelmet = 				GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idArmorTitaniumHelmet", 				9881)		.getInt(9881);
	        GCCoreConfigManager.idArmorTitaniumChestplate = 			GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idArmorTitaniumChestplate", 			9882)		.getInt(9882);
	        GCCoreConfigManager.idArmorTitaniumLeggings = 				GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idArmorTitaniumLeggings", 				9883)		.getInt(9883);
	        GCCoreConfigManager.idArmorTitaniumBoots = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idArmorTitaniumBoots", 				9884)		.getInt(9884);
	        GCCoreConfigManager.idArmorTitaniumHelmetBreathable = 		GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idArmorTitaniumHelmetBreathable", 		9885)		.getInt(9885);

	        GCCoreConfigManager.idGuiTankRefill = 						GCCoreConfigManager.configuration.get("GUI", "idGuiTankRefill", 											128)		.getInt(128);
	        GCCoreConfigManager.idGuiAirDistributor = 					GCCoreConfigManager.configuration.get("GUI", "idGuiAirDistributor", 										129)		.getInt(129);
	        GCCoreConfigManager.idGuiRocketCraftingBench = 				GCCoreConfigManager.configuration.get("GUI", "idGuiRocketCraftingBench", 									130)		.getInt(130);
	        GCCoreConfigManager.idGuiBuggyCraftingBench = 				GCCoreConfigManager.configuration.get("GUI", "idGuiBuggyCraftingBench", 									131)		.getInt(131);
	        GCCoreConfigManager.idGuiGalaxyMap = 						GCCoreConfigManager.configuration.get("GUI", "idGuiGalaxyMap",			 									132)		.getInt(132);
	        GCCoreConfigManager.idGuiSpaceshipInventory = 				GCCoreConfigManager.configuration.get("GUI", "idGuiSpaceshipInventory",			 							133)		.getInt(133);
	        
	        GCCoreConfigManager.idAchievBase = 							GCCoreConfigManager.configuration.get("Achievements", "idAchievBase", 										1784)		.getInt(1784);

	        GCCoreConfigManager.idEntityEvolvedSpider = 				GCCoreConfigManager.configuration.get("Entities", "idEntityEvolvedSpider", 									155)		.getInt(155);
	        GCCoreConfigManager.idEntityEvolvedCreeper = 				GCCoreConfigManager.configuration.get("Entities", "idEntityEvolvedCreeper", 								156)		.getInt(156);
	        GCCoreConfigManager.idEntityEvolvedZombie = 				GCCoreConfigManager.configuration.get("Entities", "idEntityEvolvedZombie", 									157)		.getInt(157);
	        GCCoreConfigManager.idEntityEvolvedSkeleton = 				GCCoreConfigManager.configuration.get("Entities", "idEntityEvolvedSkeleton", 								158)		.getInt(158);
	        GCCoreConfigManager.idEntitySpaceship = 					GCCoreConfigManager.configuration.get("Entities", "idEntitySpaceship", 										159)		.getInt(159);
	        GCCoreConfigManager.idEntityAntiGravityArrow = 				GCCoreConfigManager.configuration.get("Entities", "idEntityAntiGravityArrow", 								160)		.getInt(160);
	        GCCoreConfigManager.idEntityMeteor = 						GCCoreConfigManager.configuration.get("Entities", "idEntityMeteor", 										161)		.getInt(161);
	        GCCoreConfigManager.idEntityBuggy = 						GCCoreConfigManager.configuration.get("Entities", "idEntityBuggy", 											162)		.getInt(162);
	        GCCoreConfigManager.idEntityFlag = 							GCCoreConfigManager.configuration.get("Entities", "idEntityFlag", 											163)		.getInt(163);
	        GCCoreConfigManager.idEntityAstroOrb = 						GCCoreConfigManager.configuration.get("Entities", "idEntityAstroOrb", 										164)		.getInt(164);
	        GCCoreConfigManager.idEntityGiantWorm = 					GCCoreConfigManager.configuration.get("Entities", "idEntityGiantWorm", 										165)		.getInt(165);
	        GCCoreConfigManager.idEntityParaChest = 					GCCoreConfigManager.configuration.get("Entities", "idEntityParaChest", 										166)		.getInt(166);
	        
	        GCCoreConfigManager.transparentBreathableAir = 				GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "transparentBreathableAir", 			true)		.getBoolean(true);
	        GCCoreConfigManager.moreStars = 							GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "moreStars", 							true)		.getBoolean(true);
	        GCCoreConfigManager.wasdMapMovement = 						GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "WASD Map Movement", 					true)		.getBoolean(true);
	        GCCoreConfigManager.disableAluminiumEarth = 				GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Disable aluminium ore Gen on Overworld",	false)		.getBoolean(false);
	        GCCoreConfigManager.disableCopperEarth = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Disable copper ore Gen on Overworld",	false)		.getBoolean(false);
	        GCCoreConfigManager.disableTitaniumEarth = 					GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Disable titanium ore Gen on Overworld",	false)		.getBoolean(false);
	        GCCoreConfigManager.oreGenFactor = 							GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Ore Generation Factor", 				1)			.getInt(1);
	        final int[] dimensions = {0};
	        GCCoreConfigManager.oreGenDimensions =						GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "List of dimensions to generate GC ores in", 	dimensions)	.getIntList();
	        GCCoreConfigManager.disableSpaceshipParticles = 			GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Disable Spaceship Particles",		false)		.getBoolean(false);
	        GCCoreConfigManager.disableSpaceshipGrief = 				GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Disable Spaceship Explosion",		false)		.getBoolean(false);
		}
		catch (final Exception e)
		{
			FMLLog.log(Level.SEVERE, e, "Galacticraft Core has a problem loading it's configuration");
		}
		finally
		{
			GCCoreConfigManager.configuration.save();
			GCCoreConfigManager.loaded = true;
		}
    }
}
