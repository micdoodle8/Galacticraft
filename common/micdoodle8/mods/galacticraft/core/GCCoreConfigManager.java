package micdoodle8.mods.galacticraft.core;

import java.io.File;
import net.minecraft.block.Block;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
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

    public static int idDimensionOverworldOrbit;
    // public static int idDimensionSpace;

    // BLOCKS
    public static int idBlockBreatheableAir;
    public static int idBlockTreasureChest;
    public static int idBlockLandingPad;
    public static int idBlockLandingPadFull;
    public static int idBlockUnlitTorch;
    public static int idBlockUnlitTorchLit;
    public static int idBlockAirDistributor;
    public static int idBlockAirPipe;
    public static int idBlockAirCollector;
    public static int idBlockSapling2;
    public static int idBlockRocketBench;
    public static int idBlockFallenMeteor;
    public static int idBlockDecorationBlock;
    public static int idBlockAirLockFrame;
    public static int idBlockAirLockSeal;
    public static int idBlockCrudeOilStill;
    public static int idBlockCrudeOilMoving;
    public static int idBlockRefinery;
    public static int idBlockAirCompressor;
    public static int idBlockFuelLoader;
    public static int idBlockSpaceStationBase;
    public static int idBlockDummy;
    public static int idBlockOxygenSealer;
    public static int idBlockEnclosedWire;
    public static int idBlockOxygenDetector;

    // ITEMS
    public static int idItemKnowledgeBook;
    public static int idItemLightOxygenTank;
    public static int idItemMedOxygenTank;
    public static int idItemHeavyOxygenTank;
    public static int idItemSpaceship;
    public static int idItemIngotTitanium;
    public static int idItemIngotCopper;
    public static int idItemIngotAluminum;
    public static int idItemTinCanister;
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
    public static int idItemOilCanister;
    public static int idItemOilExtractor;
    public static int idItemFuel;
    public static int idItemSchematic;
    public static int idItemKey;
    public static int idItemBuggyMaterial;

    // RECIPES
    public static boolean useRecipesIC2;
    public static boolean useRecipesGT;
    public static boolean useRecipesTE;
    public static boolean useRecipesUE;

    // SCHEMATICS
    public static int idSchematicRocketT1;
    public static int idSchematicMoonBuggy;
    public static int idSchematicAddSchematic;

    // ARMOR
    public static int idArmorOxygenMask;
    public static int idArmorSensorGlasses;
    public static int idArmorSteelHelmet;
    public static int idArmorSteelChestplate;
    public static int idArmorSteelLeggings;
    public static int idArmorSteelBoots;

    // TOOLS
    public static int idToolSteelSword;
    public static int idToolSteelPickaxe;
    public static int idToolSteelAxe;
    public static int idToolSteelSpade;
    public static int idToolSteelHoe;

    // GUI
    public static int idGuiTankRefill;
    public static int idGuiRocketCraftingBench;
    public static int idGuiBuggyCraftingBench;
    public static int idGuiGalaxyMap;
    public static int idGuiSpaceshipInventory;
    public static int idGuiRefinery;
    public static int idGuiAirCollector;
    public static int idGuiAirDistributor;
    public static int idGuiAirCompressor;
    public static int idGuiFuelLoader;
    public static int idGuiAddSchematic;
    public static int idGuiAirSealer;
    public static int idGuiKnowledgeBook;

    // ACHIEVEMENTS
    public static int idAchievBase;

    public static int idEntityEvolvedSpider;
    public static int idEntityEvolvedCreeper;
    public static int idEntityEvolvedZombie;
    public static int idEntityEvolvedSkeleton;
    public static int idEntityEvolvedSkeletonBoss;
    public static int idEntitySpaceship;
    public static int idEntityAntiGravityArrow;
    public static int idEntityMeteor;
    public static int idEntityBuggy;
    public static int idEntityFlag;
    public static int idEntityAstroOrb;
    public static int idEntityGiantWorm;
    public static int idEntityParaChest;
    public static int idEntityAlienVillager;
    public static int idEntityOxygenBubble;
    public static int idEntityLander;
    public static int idEntityLanderChest;
    public static int idEntitySun;
    public static int idEntityOverworld;

    // GENERAL
    public static boolean transparentBreathableAir;
    public static boolean moreStars;
    public static boolean wasdMapMovement;
    public static boolean disableAluminiumEarth;
    public static boolean disableCopperEarth;
    public static boolean disableTitaniumEarth;
    public static boolean disableOilGen;
    public static int oreGenFactor;
    public static String[] sealableIDs;
    public static boolean disableSpaceshipParticles;
    public static boolean disableSpaceshipGrief;
    public static boolean disableTutorialItemText;
    public static boolean oxygenIndicatorLeftSide;
    public static double oilGenFactor;
    public static boolean disableLeafDecay;
    public static boolean spaceStationsRequirePermission;
    public static boolean canRespawnOnSpaceStations;
    public static boolean overrideCapes;
    public static boolean hiresTextures;
    public static double spaceStationEnergyScalar;
    public static boolean disableLander;
    public static Property forceLoadGC;
    public static Property loadBC;
    public static boolean enableKnowledgeBook;

    private void setDefaultValues()
    {
        try
        {
            GCCoreConfigManager.configuration.load();

            // GCCoreConfigManager.idDimensionSpace =
            // GCCoreConfigManager.configuration.get("DIMENSIONS",
            // "idDimensionSpace", -26) .getInt(-26);
            GCCoreConfigManager.idDimensionOverworldOrbit = GCCoreConfigManager.configuration.get("DIMENSIONS", "idDimensionOverworldOrbit", -27).getInt(-27);

            GCCoreConfigManager.idBlockBreatheableAir = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockCoreBreatheableAir", 3350).getInt(3350);
            GCCoreConfigManager.idBlockLandingPad = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockLandingPad", 3351).getInt(3351);
            GCCoreConfigManager.idBlockLandingPadFull = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockLandingPadFull", 3352).getInt(3352);
            GCCoreConfigManager.idBlockUnlitTorch = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockUnlitTorch", 3353).getInt(3353);
            GCCoreConfigManager.idBlockUnlitTorchLit = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockUnlitTorchLit", 3354).getInt(3354);
            GCCoreConfigManager.idBlockAirDistributor = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockAirDistributor", 3355).getInt(3355);
            GCCoreConfigManager.idBlockAirCompressor = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockAirCompressor", 3356).getInt(3356);
            GCCoreConfigManager.idBlockAirCollector = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockAirCollector", 3357).getInt(3357);
            GCCoreConfigManager.idBlockRefinery = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockRefinery", 3358).getInt(3358);
            GCCoreConfigManager.idBlockFuelLoader = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockFuelLoader", 3359).getInt(3359);
            GCCoreConfigManager.idBlockOxygenSealer = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockOxygenSealer", 3360).getInt(3360);
            GCCoreConfigManager.idBlockOxygenDetector = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockOxygenDetector", 3361).getInt(3361);
            GCCoreConfigManager.idBlockAirPipe = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockAirPipe", 3368).getInt(3368);
            GCCoreConfigManager.idBlockSapling2 = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockSapling2", 3369).getInt(3369);
            GCCoreConfigManager.idBlockRocketBench = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockRocketBench", 3370).getInt(3370);
            GCCoreConfigManager.idBlockFallenMeteor = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockFallenMeteor", 3371).getInt(3371);
            GCCoreConfigManager.idBlockDecorationBlock = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockDecorationBlock", 3372).getInt(3372);
            GCCoreConfigManager.idBlockAirLockFrame = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockAirLockFrame", 3373).getInt(3373);
            GCCoreConfigManager.idBlockAirLockSeal = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockAirLockSeal", 3374).getInt(3374);
            GCCoreConfigManager.idBlockCrudeOilMoving = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockCrudeOilMoving", 3376).getInt(3376);
            GCCoreConfigManager.idBlockCrudeOilStill = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockCrudeOilStill", 3377).getInt(3377);
            GCCoreConfigManager.idBlockDummy = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockDummy", 3378).getInt(3378);
            GCCoreConfigManager.idBlockEnclosedWire = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockEnclosedWire", 3379).getInt(3379);
            GCCoreConfigManager.idBlockSpaceStationBase = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockSpaceStationBase", 3380, "Even though this will be generated, it can use block IDs greater than 256").getInt(3380);
            GCCoreConfigManager.idBlockTreasureChest = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockTieredTreasureChest", 3381, "Even though this will be generated, it can use block IDs greater than 256").getInt(3381);

            GCCoreConfigManager.idItemSpaceship = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemSpaceship", 9855).getInt(9855);
            GCCoreConfigManager.idItemLightOxygenTank = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemLightOxygenTank", 9856).getInt(9856);
            GCCoreConfigManager.idItemMedOxygenTank = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemMedOxygenTank", 9857).getInt(9857);
            GCCoreConfigManager.idItemHeavyOxygenTank = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemHeavyOxygenTank", 9858).getInt(9858);
            GCCoreConfigManager.idArmorOxygenMask = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idArmorOxygenMask", 9859).getInt(9859);
            GCCoreConfigManager.idArmorSensorGlasses = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idArmorSensorGlasses", 9860).getInt(9860);
            GCCoreConfigManager.idItemTinCanister = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemCanister", 9861).getInt(9861);
            GCCoreConfigManager.idItemAirVent = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemAirVent", 9862).getInt(9862);
            GCCoreConfigManager.idItemOxygenConcentrator = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemOxygenConcentrator", 9863).getInt(9863);
            GCCoreConfigManager.idItemFan = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemFan", 9864).getInt(9864);
            GCCoreConfigManager.idItemGravityBow = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemGravityBow", 9865).getInt(9865);
            GCCoreConfigManager.idItemRocketEngine = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemRocketEngine", 9866).getInt(9866);
            GCCoreConfigManager.idItemHeavyPlate = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemHeavyPlate", 9867).getInt(9867);
            GCCoreConfigManager.idItemRocketNoseCone = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemRocketNoseCone", 9868).getInt(9868);
            GCCoreConfigManager.idItemRocketFins = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemRocketFins", 9869).getInt(9869);
            GCCoreConfigManager.idItemSensorLens = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemSensorLens", 9870).getInt(9870);
            GCCoreConfigManager.idItemBuggy = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemBuggyPlacer", 9871).getInt(9871);
            GCCoreConfigManager.idItemFlagPole = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemFlagPole", 9872).getInt(9872);
            GCCoreConfigManager.idItemFlag = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemFlagPlacer", 9873).getInt(9873);
            GCCoreConfigManager.idItemOxygenGear = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemOxygenGear", 9874).getInt(9874);
            GCCoreConfigManager.idItemCanvas = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemCanvas", 9875).getInt(9875);
            GCCoreConfigManager.idItemParachute = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemParachute", 9876).getInt(9876);
            GCCoreConfigManager.idItemOilExtractor = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemOilExtractor", 9877).getInt(9877);
            GCCoreConfigManager.idItemOilCanister = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemOilCanister", 9878).getInt(9878);
            GCCoreConfigManager.idItemRocketFuelBucket = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemRocketFuelCanister", 9879).getInt(9879);
            GCCoreConfigManager.idItemFuel = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "iditemFuel", 9880).getInt(9880);
            GCCoreConfigManager.idItemKnowledgeBook = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemKnowledgeBook", 9881).getInt(9881);
            GCCoreConfigManager.idItemSchematic = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemSchematic", 9989).getInt(9989);
            GCCoreConfigManager.idItemKey = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemKey", 9990).getInt(9990);
            GCCoreConfigManager.idItemBuggyMaterial = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemBuggyMaterial", 9991).getInt(9991);

            GCCoreConfigManager.idToolSteelSword = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idToolSteelSword", 9980).getInt(9880);
            GCCoreConfigManager.idToolSteelPickaxe = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idToolSteelPickaxe", 9981).getInt(9881);
            GCCoreConfigManager.idToolSteelSpade = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idToolSteelSpade", 9982).getInt(9882);
            GCCoreConfigManager.idToolSteelHoe = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idToolSteelHoe", 9983).getInt(9883);
            GCCoreConfigManager.idToolSteelAxe = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idToolSteelAxe", 9984).getInt(9884);

            GCCoreConfigManager.idArmorSteelHelmet = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idArmorSteelHelmet", 9985).getInt(9885);
            GCCoreConfigManager.idArmorSteelChestplate = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idArmorSteelChestplate", 9986).getInt(9886);
            GCCoreConfigManager.idArmorSteelLeggings = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idArmorSteelLeggings", 9987).getInt(9887);
            GCCoreConfigManager.idArmorSteelBoots = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idArmorSteelBoots", 9988).getInt(9888);

            GCCoreConfigManager.useRecipesIC2 = GCCoreConfigManager.configuration.get("RECIPES", "Enable IC2 Recipes", true).getBoolean(true);
            GCCoreConfigManager.useRecipesGT = GCCoreConfigManager.configuration.get("RECIPES", "Enable Gregtech Recipes", true).getBoolean(true);
            GCCoreConfigManager.useRecipesUE = GCCoreConfigManager.configuration.get("RECIPES", "Enable Basic Components Recipes", true).getBoolean(true);
            GCCoreConfigManager.useRecipesTE = GCCoreConfigManager.configuration.get("RECIPES", "Enable Thermal Expansion Recipes", true).getBoolean(true);

            GCCoreConfigManager.idGuiTankRefill = GCCoreConfigManager.configuration.get("GUI", "idGuiTankRefill", 128).getInt(128);
            GCCoreConfigManager.idGuiAirCompressor = GCCoreConfigManager.configuration.get("GUI", "idGuiAirCompressor", 129).getInt(129);
            GCCoreConfigManager.idGuiRocketCraftingBench = GCCoreConfigManager.configuration.get("GUI", "idGuiRocketCraftingBench", 130).getInt(130);
            GCCoreConfigManager.idGuiBuggyCraftingBench = GCCoreConfigManager.configuration.get("GUI", "idGuiBuggyCraftingBench", 131).getInt(131);
            GCCoreConfigManager.idGuiGalaxyMap = GCCoreConfigManager.configuration.get("GUI", "idGuiGalaxyMap", 132).getInt(132);
            GCCoreConfigManager.idGuiSpaceshipInventory = GCCoreConfigManager.configuration.get("GUI", "idGuiSpaceshipInventory", 133).getInt(133);
            GCCoreConfigManager.idGuiRefinery = GCCoreConfigManager.configuration.get("GUI", "idGuiRefinery", 134).getInt(134);
            GCCoreConfigManager.idGuiAirCollector = GCCoreConfigManager.configuration.get("GUI", "idGuiAirCollector", 135).getInt(135);
            GCCoreConfigManager.idGuiAirDistributor = GCCoreConfigManager.configuration.get("GUI", "idGuiAirDistributor", 136).getInt(136);
            GCCoreConfigManager.idGuiFuelLoader = GCCoreConfigManager.configuration.get("GUI", "idGuiFuelLoader", 137).getInt(137);
            GCCoreConfigManager.idGuiAddSchematic = GCCoreConfigManager.configuration.get("GUI", "idGuiAddSchematic", 138).getInt(138);
            GCCoreConfigManager.idGuiAirSealer = GCCoreConfigManager.configuration.get("GUI", "idGuiAirSealer", 139).getInt(139);
            GCCoreConfigManager.idGuiKnowledgeBook = GCCoreConfigManager.configuration.get("GUI", "idGuiKnowledgeBook", 140).getInt(140);

            GCCoreConfigManager.idSchematicRocketT1 = GCCoreConfigManager.configuration.get("Schematic", "idSchematicRocketT1", 0).getInt(0);
            GCCoreConfigManager.idSchematicMoonBuggy = GCCoreConfigManager.configuration.get("Schematic", "idSchematicMoonBuggy", 1).getInt(1);
            GCCoreConfigManager.idSchematicAddSchematic = GCCoreConfigManager.configuration.get("Schematic", "idSchematicAddSchematic", Integer.MAX_VALUE).getInt(Integer.MAX_VALUE);

            GCCoreConfigManager.idAchievBase = GCCoreConfigManager.configuration.get("Achievements", "idAchievBase", 1784).getInt(1784);

            GCCoreConfigManager.idEntityEvolvedSpider = GCCoreConfigManager.configuration.get("Entities", "idEntityEvolvedSpider", 155).getInt(155);
            GCCoreConfigManager.idEntityEvolvedCreeper = GCCoreConfigManager.configuration.get("Entities", "idEntityEvolvedCreeper", 156).getInt(156);
            GCCoreConfigManager.idEntityEvolvedZombie = GCCoreConfigManager.configuration.get("Entities", "idEntityEvolvedZombie", 157).getInt(157);
            GCCoreConfigManager.idEntityEvolvedSkeleton = GCCoreConfigManager.configuration.get("Entities", "idEntityEvolvedSkeleton", 158).getInt(158);
            GCCoreConfigManager.idEntitySpaceship = GCCoreConfigManager.configuration.get("Entities", "idEntitySpaceship", 159).getInt(159);
            GCCoreConfigManager.idEntityAntiGravityArrow = GCCoreConfigManager.configuration.get("Entities", "idEntityAntiGravityArrow", 160).getInt(160);
            GCCoreConfigManager.idEntityMeteor = GCCoreConfigManager.configuration.get("Entities", "idEntityMeteor", 161).getInt(161);
            GCCoreConfigManager.idEntityBuggy = GCCoreConfigManager.configuration.get("Entities", "idEntityBuggy", 162).getInt(162);
            GCCoreConfigManager.idEntityFlag = GCCoreConfigManager.configuration.get("Entities", "idEntityFlag", 163).getInt(163);
            GCCoreConfigManager.idEntityAstroOrb = GCCoreConfigManager.configuration.get("Entities", "idEntityAstroOrb", 164).getInt(164);
            GCCoreConfigManager.idEntityGiantWorm = GCCoreConfigManager.configuration.get("Entities", "idEntityGiantWorm", 165).getInt(165);
            GCCoreConfigManager.idEntityParaChest = GCCoreConfigManager.configuration.get("Entities", "idEntityParaChest", 166).getInt(166);
            GCCoreConfigManager.idEntityAlienVillager = GCCoreConfigManager.configuration.get("Entities", "idEntityAlienVillager", 167).getInt(167);
            GCCoreConfigManager.idEntityOxygenBubble = GCCoreConfigManager.configuration.get("Entities", "idEntityOxygenBubble", 168).getInt(168);
            GCCoreConfigManager.idEntityLander = GCCoreConfigManager.configuration.get("Entities", "idEntityLander", 169).getInt(169);
            GCCoreConfigManager.idEntityLanderChest = GCCoreConfigManager.configuration.get("Entities", "idEntityLanderChest", 170).getInt(170);
            GCCoreConfigManager.idEntityEvolvedSkeletonBoss = GCCoreConfigManager.configuration.get("Entities", "idEntityEvolvedSkeletonBoss", 171).getInt(171);
            GCCoreConfigManager.idEntitySun = GCCoreConfigManager.configuration.get("Entities", "idEntitySun", 172).getInt(172);
            GCCoreConfigManager.idEntityOverworld = GCCoreConfigManager.configuration.get("Entities", "idEntityOverworld", 173).getInt(173);

            GCCoreConfigManager.transparentBreathableAir = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Transparent Breathable Air", true, "If you have FPS problems and wish to have solid blue breathable air blocks, set this to false").getBoolean(true);
            GCCoreConfigManager.moreStars = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "More Stars", true, "Setting this to false will revert night skies back to default minecraft star count").getBoolean(true);
            GCCoreConfigManager.wasdMapMovement = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "WASD Map Movement", true, "If you prefer to move the Galaxy map with your mouse, set to false").getBoolean(true);
            GCCoreConfigManager.disableOilGen = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Disable oil Gen on Overworld", false, "Set to true if you do not wish for Galacticraft oil to generate.").getBoolean(false);
            GCCoreConfigManager.oilGenFactor = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Oil Generation Factor", 1.8, "Increasing this will increase amount of oil that will generate in each chunk.").getDouble(1.8);
            GCCoreConfigManager.disableSpaceshipParticles = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Disable Spaceship Particles", false, "If you have FPS problems, setting this to true will help if spaceship particles are in your sights").getBoolean(false);
            GCCoreConfigManager.disableSpaceshipGrief = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Disable Spaceship Explosion", false, "Spaceships will not explode on contact if set to true").getBoolean(false);
            GCCoreConfigManager.disableTutorialItemText = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Disable Tutorial Item Info Text", false, "When set to true, the \"Press R to open Galacticraft Inventory\" message will not show").getBoolean(false);
            GCCoreConfigManager.oxygenIndicatorLeftSide = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Using Minimap Mod", false, "If true, this will move the Oxygen Indicator to the left side.").getBoolean(false);
            GCCoreConfigManager.disableLeafDecay = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Disable Oxygen Collector Leaf Decay", false, "If set to true, Oxygen Collectors will not consume leaf blocks.").getBoolean(false);
            GCCoreConfigManager.spaceStationsRequirePermission = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Space Stations Require Permission", true, "While true, space stations require you to invite other players using /ssinvite <playername>").getBoolean(true);
            GCCoreConfigManager.canRespawnOnSpaceStations = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Can players respawn on Space Stations", false, "If you want to respawn on spacestations if you die there, set to true. When false beds will explode on use.").getBoolean(false);
            GCCoreConfigManager.overrideCapes = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Override Capes", true, "By default, Galacticraft will override capes with the mod's donor cape. Set to false to disable.").getBoolean(true);
            GCCoreConfigManager.hiresTextures = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Hi-Res Textures (32x32)", true, "If you prefer low-resolution textures, set this to false").getBoolean(true);
            GCCoreConfigManager.spaceStationEnergyScalar = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Space Station Solar Energy Multiplier", 3.0, "If Mekanism is installed, solar panels will work (default 3x) more effective on space stations.").getDouble(3.0);
            GCCoreConfigManager.sealableIDs = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "External Sealable IDs", new String[] { String.valueOf(Block.glass.blockID + ":0"), String.valueOf(Block.thinGlass.blockID + ":0") }, "List IDs from other mods that the Oxygen Sealer should recognize as solid blocks. Format is ID:METADATA").getStringList();
            GCCoreConfigManager.disableLander = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Disable Lander", false, "Set to true if you do not want to use the new Moon Lander feature, and land on the moon with a parachute instead.").getBoolean(false);
            GCCoreConfigManager.forceLoadGC = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Force Load", false, "Bypass mod requirements and load anyway (not recommended).");
            GCCoreConfigManager.loadBC = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Load Basic Components", false, "Set to true to load the required Basic Components items from Galacticraft.");
            GCCoreConfigManager.enableKnowledgeBook = GCCoreConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Enable Knowledge Book", true, "Set to false if you do not wish to have the book of knowledge item").getBoolean(true);
        }
        catch (final Exception e)
        {
            GCLog.severe("Problem loading core config (\"core.conf\")");
        }
        finally
        {
            if (GCCoreConfigManager.configuration.hasChanged())
            {
                GCCoreConfigManager.configuration.save();
            }

            GCCoreConfigManager.loaded = true;
        }
    }
}
