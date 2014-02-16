package micdoodle8.mods.galacticraft.core.util;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;

/**
 * GCCoreConfigManager.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCConfigManager
{
	public static boolean loaded;

	static Configuration configuration;

	public static int idDimensionOverworldOrbit;
	public static int dimensionIDMoon;

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
	public static int idBlockRefinery;
	public static int idBlockAirCompressor;
	public static int idBlockFuelLoader;
	public static int idBlockSpaceStationBase;
	public static int idBlockDummy;
	public static int idBlockOxygenSealer;
	public static int idBlockEnclosedWire;
	public static int idBlockOxygenDetector;
	public static int idBlockCargoLoader;
	public static int idBlockParachest;
	public static int idBlockSolarPanel;
	public static int idBlockBasicMachine;
	public static int idBlockCopperWire;
	public static int idBlockBasicMachine2;
	public static int idBlockGlowstoneTorch;
	public static int idBlockFuel;

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
	public static int idItemSchematic;
	public static int idItemKey;
	public static int idItemBuggyMaterial;
	public static int idItemBasic;
	public static int idItemBattery;
	public static int idItemInfiniteBattery;
	public static int idItemMeteorChunk;
	public static int idItemStandardWrench;

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
	public static int idGuiRocketCraftingBench;
	public static int idGuiBuggyCraftingBench;
	public static int idGuiGalaxyMap;
	public static int idGuiSpaceshipInventory;
	public static int idGuiAddSchematic;
	public static int idGuiKnowledgeBook;
	public static int idGuiExtendedInventory;

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
	public static int idEntityParaChest;
	public static int idEntityAlienVillager;
	public static int idEntityOxygenBubble;
	public static int idEntityLander;
	public static int idEntityLanderChest;
	public static int idEntityMeteorChunk;

	// GENERAL
	public static boolean moreStars;
	public static boolean wasdMapMovement;
	public static String[] sealableIDs;
	public static String[] detectableIDs;
	public static boolean disableSpaceshipParticles;
	public static boolean disableSpaceshipGrief;
	public static boolean oxygenIndicatorLeft;
	public static boolean oxygenIndicatorBottom;
	public static double oilGenFactor;
	public static boolean disableLeafDecay;
	public static boolean spaceStationsRequirePermission;
	public static boolean overrideCapes;
	public static double spaceStationEnergyScalar;
	public static boolean disableLander;
	public static double dungeonBossHealthMod;
	public static int suffocationCooldown;
	public static int suffocationDamage;
	public static int[] externalOilGen;
	public static boolean forceOverworldRespawn;
	public static boolean enableDebug;
	public static boolean enableCopperOreGen;
	public static boolean enableTinOreGen;
	public static boolean enableAluminumOreGen;
	public static boolean enableSiliconOreGen;
	public static boolean disableCheeseMoon;
	public static boolean disableTinMoon;
	public static boolean disableCopperMoon;
	public static boolean disableMoonVillageGen;

	public static void setDefaultValues(File file)
	{
		if (!GCConfigManager.loaded)
		{
			GCConfigManager.configuration = new Configuration(file);
		}

		try
		{
			GCConfigManager.configuration.load();

			GCConfigManager.idDimensionOverworldOrbit = GCConfigManager.configuration.get("DIMENSIONS", "Overworld Orbit Dimension ID", -27).getInt(-27);
			GCConfigManager.dimensionIDMoon = GCConfigManager.configuration.get("DIMENSIONS", "Moon Dimension ID", -28).getInt(-28);

			GCConfigManager.idGuiRocketCraftingBench = GCConfigManager.configuration.get("GUI", "idGuiRocketCraftingBench", 130).getInt(130);
			GCConfigManager.idGuiBuggyCraftingBench = GCConfigManager.configuration.get("GUI", "idGuiBuggyCraftingBench", 131).getInt(131);
			GCConfigManager.idGuiGalaxyMap = GCConfigManager.configuration.get("GUI", "idGuiGalaxyMap", 132).getInt(132);
			GCConfigManager.idGuiSpaceshipInventory = GCConfigManager.configuration.get("GUI", "idGuiSpaceshipInventory", 133).getInt(133);
			GCConfigManager.idGuiAddSchematic = GCConfigManager.configuration.get("GUI", "idGuiAddSchematic", 138).getInt(138);
			GCConfigManager.idGuiKnowledgeBook = GCConfigManager.configuration.get("GUI", "idGuiKnowledgeBook", 140).getInt(140);
			GCConfigManager.idGuiExtendedInventory = GCConfigManager.configuration.get("GUI", "idGuiExtendedInventory", 145).getInt(145);

			GCConfigManager.idSchematicRocketT1 = GCConfigManager.configuration.get("Schematic", "idSchematicRocketT1", 0).getInt(0);
			GCConfigManager.idSchematicMoonBuggy = GCConfigManager.configuration.get("Schematic", "idSchematicMoonBuggy", 1).getInt(1);
			GCConfigManager.idSchematicAddSchematic = GCConfigManager.configuration.get("Schematic", "idSchematicAddSchematic", Integer.MAX_VALUE).getInt(Integer.MAX_VALUE);

			GCConfigManager.idAchievBase = GCConfigManager.configuration.get("Achievements", "idAchievBase", 1784).getInt(1784);

			GCConfigManager.idEntityEvolvedSpider = GCConfigManager.configuration.get("Entities", "idEntityEvolvedSpider", 155).getInt(155);
			GCConfigManager.idEntityEvolvedCreeper = GCConfigManager.configuration.get("Entities", "idEntityEvolvedCreeper", 156).getInt(156);
			GCConfigManager.idEntityEvolvedZombie = GCConfigManager.configuration.get("Entities", "idEntityEvolvedZombie", 157).getInt(157);
			GCConfigManager.idEntityEvolvedSkeleton = GCConfigManager.configuration.get("Entities", "idEntityEvolvedSkeleton", 158).getInt(158);
			GCConfigManager.idEntitySpaceship = GCConfigManager.configuration.get("Entities", "idEntitySpaceship", 159).getInt(159);
			GCConfigManager.idEntityAntiGravityArrow = GCConfigManager.configuration.get("Entities", "idEntityAntiGravityArrow", 160).getInt(160);
			GCConfigManager.idEntityMeteor = GCConfigManager.configuration.get("Entities", "idEntityMeteor", 161).getInt(161);
			GCConfigManager.idEntityBuggy = GCConfigManager.configuration.get("Entities", "idEntityBuggy", 162).getInt(162);
			GCConfigManager.idEntityFlag = GCConfigManager.configuration.get("Entities", "idEntityFlag", 163).getInt(163);
			GCConfigManager.idEntityAstroOrb = GCConfigManager.configuration.get("Entities", "idEntityAstroOrb", 164).getInt(164);
			GCConfigManager.idEntityParaChest = GCConfigManager.configuration.get("Entities", "idEntityParaChest", 165).getInt(165);
			GCConfigManager.idEntityAlienVillager = GCConfigManager.configuration.get("Entities", "idEntityAlienVillager", 166).getInt(166);
			GCConfigManager.idEntityOxygenBubble = GCConfigManager.configuration.get("Entities", "idEntityOxygenBubble", 167).getInt(167);
			GCConfigManager.idEntityLander = GCConfigManager.configuration.get("Entities", "idEntityLander", 168).getInt(168);
			GCConfigManager.idEntityLanderChest = GCConfigManager.configuration.get("Entities", "idEntityLanderChest", 169).getInt(169);
			GCConfigManager.idEntityEvolvedSkeletonBoss = GCConfigManager.configuration.get("Entities", "idEntityEvolvedSkeletonBoss", 170).getInt(170);
			GCConfigManager.idEntityMeteorChunk = GCConfigManager.configuration.get("Entities", "idEntityMeteorChunk", 179).getInt(179);

			GCConfigManager.moreStars = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "More Stars", true, "Setting this to false will revert night skies back to default minecraft star count").getBoolean(true);
			GCConfigManager.wasdMapMovement = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "WASD Map Movement", true, "If you prefer to move the Galaxy map with your mouse, set to false").getBoolean(true);
			GCConfigManager.oilGenFactor = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Oil Generation Factor", 1.8, "Increasing this will increase amount of oil that will generate in each chunk.").getDouble(1.8);
			GCConfigManager.disableSpaceshipParticles = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Disable Spaceship Particles", false, "If you have FPS problems, setting this to true will help if spaceship particles are in your sights").getBoolean(false);
			GCConfigManager.disableSpaceshipGrief = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Disable Spaceship Explosion", false, "Spaceships will not explode on contact if set to true").getBoolean(false);
			GCConfigManager.oxygenIndicatorLeft = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Minimap Left", false, "If true, this will move the Oxygen Indicator to the left side. You can combine this with \"Minimap Bottom\"").getBoolean(false);
			GCConfigManager.oxygenIndicatorBottom = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Minimap Bottom", false, "If true, this will move the Oxygen Indicator to the bottom. You can combine this with \"Minimap Left\"").getBoolean(false);
			GCConfigManager.disableLeafDecay = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Disable Oxygen Collector Leaf Decay", false, "If set to true, Oxygen Collectors will not consume leaf blocks.").getBoolean(false);
			GCConfigManager.spaceStationsRequirePermission = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Space Stations Require Permission", true, "While true, space stations require you to invite other players using /ssinvite <playername>").getBoolean(true);
			GCConfigManager.overrideCapes = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Override Capes", true, "By default, Galacticraft will override capes with the mod's donor cape. Set to false to disable.").getBoolean(true);
			GCConfigManager.spaceStationEnergyScalar = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Space Station Solar Energy Multiplier", 2.0, "If Mekanism is installed, solar panels will work (default 2x) more effective on space stations.").getDouble(2.0);
			GCConfigManager.sealableIDs = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "External Sealable IDs", new String[] { String.valueOf(Block.getIdFromBlock(Blocks.glass) + ":0"), String.valueOf(Block.getIdFromBlock(Blocks.glass_pane) + ":0") }, "List IDs from other mods that the Oxygen Sealer should recognize as solid blocks. Format is ID:METADATA").getStringList();
			GCConfigManager.detectableIDs = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "External Detectable IDs", new String[] { String.valueOf(Block.getIdFromBlock(Blocks.coal_ore) + ":0"), String.valueOf(Block.getIdFromBlock(Blocks.diamond_ore) + ":0"), String.valueOf(Block.getIdFromBlock(Blocks.gold_ore) + ":0"), String.valueOf(Block.getIdFromBlock(Blocks.iron_ore) + ":0"), String.valueOf(Block.getIdFromBlock(Blocks.lapis_ore) + ":0"), String.valueOf(Block.getIdFromBlock(Blocks.redstone_ore) + ":0") }, "List IDs from other mods that the Sensor Glasses should recognize as solid blocks. Format is ID:METADATA").getStringList();
			GCConfigManager.dungeonBossHealthMod = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Dungeon Boss Health Modifier", 1.0D, "Change this is you wish to balance the mod (if you have more powerful weapon mods)").getDouble(1.0D);
			GCConfigManager.suffocationCooldown = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Suffocation Cooldown", 100, "Lower/Raise this value to change time between suffocation damage ticks").getInt(100);
			GCConfigManager.suffocationDamage = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Suffocation Damage", 2, "Change this value to modify the damage taken per suffocation tick").getInt(2);
			GCConfigManager.externalOilGen = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Oil gen in external dimensions", new int[] { 0 }, "List of non-galacticraft dimension IDs to generate oil in").getIntList();
			GCConfigManager.forceOverworldRespawn = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Force Overworld Spawn", false, "By default, you will respawn on galacticraft dimensions if you die. If you set this to true, you will respawn back on earth.").getBoolean(false);
			GCConfigManager.enableDebug = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Enable Debug Messages", false, "If this is enabled, debug messages will appear in the console. This is useful for finding bugs in the mod.").getBoolean(false);
			GCConfigManager.enableCopperOreGen = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Enable Copper Ore Gen", true, "If this is enabled, copper ore will generate on the overworld.").getBoolean(true);
			GCConfigManager.enableTinOreGen = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Enable Tin Ore Gen", true, "If this is enabled, tin ore will generate on the overworld.").getBoolean(true);
			GCConfigManager.enableAluminumOreGen = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Enable Aluminum Ore Gen", true, "If this is enabled, aluminum ore will generate on the overworld.").getBoolean(true);
			GCConfigManager.enableSiliconOreGen = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Enable Silicon Ore Gen", true, "If this is enabled, silicon ore will generate on the overworld.").getBoolean(true);
			GCConfigManager.disableCheeseMoon = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Disable Cheese Ore Gen on Moon", false).getBoolean(false);
			GCConfigManager.disableTinMoon = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Disable Cheese Ore Gen on Moon", false).getBoolean(false);
			GCConfigManager.disableCopperMoon = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Disable Cheese Ore Gen on Moon", false).getBoolean(false);
			GCConfigManager.disableMoonVillageGen = GCConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Disable Moon Village Gen", false).getBoolean(false);
		}
		catch (final Exception e)
		{
			GCLog.severe("Problem loading core config (\"core.conf\")");
		}
		finally
		{
			if (GCConfigManager.configuration.hasChanged())
			{
				GCConfigManager.configuration.save();
			}

			GCConfigManager.loaded = true;
		}
	}
}
