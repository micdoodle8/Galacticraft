package micdoodle8.mods.galacticraft.core.util;

import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.minecraftforge.common.ForgeConfigSpec.*;

public class ConfigManagerCore
{
//    static Configuration config;

    public static final ConfigManagerCore INSTANCE;
    public static final ForgeConfigSpec COMMON_SPEC;
    static
    {
        final Pair<ConfigManagerCore, ForgeConfigSpec> specPair = new Builder().configure(ConfigManagerCore::new);
        COMMON_SPEC = specPair.getRight();
        INSTANCE = specPair.getLeft();
    }
    private final Builder COMMON_BUILDER;

    // GAME CONTROL
    public static BooleanValue forceOverworldRespawn;
    public static BooleanValue hardMode;
    public static BooleanValue quickMode;
    public static BooleanValue challengeMode;
    private static IntValue challengeFlags;
    public static boolean challengeRecipes;
    public static boolean challengeMobDropsAndSpawning;
    public static boolean challengeSpawnHandling;
    public static boolean challengeAsteroidPopulation;
    public static BooleanValue disableRocketsToOverworld;
    public static BooleanValue disableSpaceStationCreation;
    public static BooleanValue spaceStationsRequirePermission;
    public static BooleanValue disableUpdateCheck;
    public static BooleanValue enableSpaceRaceManagerPopup;
    public static BooleanValue enableDebug;
    public static BooleanValue enableSealerEdgeChecks;
    public static BooleanValue disableLander;
    public static BooleanValue recipesRequireGCAdvancedMetals;
    public static BooleanValue allowLiquidGratings;
//    public static int mapfactor;
//    public static int mapsize;

    // DIMENSIONS
//    public static int idDimensionOverworld;
//    public static int idDimensionOverworldOrbit;
//    public static int idDimensionOverworldOrbitStatic;
//    public static int idDimensionMoon;
    public static IntValue biomeIDbase;
    public static BooleanValue disableBiomeTypeRegistrations;
    public static ConfigValue<List<String>> staticLoadDimensions;
    public static ConfigValue<List<String>> disableRocketLaunchDimensions;
    public static BooleanValue disableRocketLaunchAllNonGC;
    public static IntValue otherPlanetWorldBorders;
    public static BooleanValue keepLoadedNewSpaceStations;

    // SCHEMATICS
    public static IntValue idSchematicRocketT1;
    public static IntValue idSchematicMoonBuggy;
    public static IntValue idSchematicAddSchematic;

    // ACHIEVEMENTS
    public static IntValue idAchievBase;

    // CLIENT / VISUAL FX
    public static BooleanValue moreStars;
    public static BooleanValue disableSpaceshipParticles;
    public static BooleanValue disableVehicleCameraChanges;
    public static BooleanValue oxygenIndicatorLeft;
    public static BooleanValue oxygenIndicatorBottom;
    public static BooleanValue overrideCapes;

    //DIFFICULTY
    public static DoubleValue dungeonBossHealthMod;
    public static IntValue suffocationCooldown;
    public static IntValue suffocationDamage;
    public static IntValue rocketFuelFactor;
    public static DoubleValue meteorSpawnMod;
    public static BooleanValue meteorBlockDamageEnabled;
    public static BooleanValue disableSpaceshipGrief;
    public static DoubleValue spaceStationEnergyScalar;

    // WORLDGEN
    public static BooleanValue enableCopperOreGen;
    public static BooleanValue enableTinOreGen;
    public static BooleanValue enableAluminumOreGen;
    public static BooleanValue enableSiliconOreGen;
    public static BooleanValue disableCheeseMoon;
    public static BooleanValue disableTinMoon;
    public static BooleanValue disableCopperMoon;
    public static BooleanValue disableMoonVillageGen;
    public static BooleanValue disableSapphireMoon;
//    public static IntValue[] externalOilGen;
    public static DoubleValue oilGenFactor;
    public static BooleanValue retrogenOil;
    public static ConfigValue<List<String>> oregenIDs;
    public static BooleanValue enableOtherModsFeatures;
    public static BooleanValue whitelistCoFHCoreGen;
    public static BooleanValue enableThaumCraftNodes;

    //COMPATIBILITY
    public static ConfigValue<List<String>> sealableIDs;
    public static ConfigValue<List<String>> detectableIDs;
    public static BooleanValue alternateCanisterRecipe;
    public static ConfigValue<String> otherModsSilicon;
    public static BooleanValue useOldOilFluidID;
    public static BooleanValue useOldFuelFluidID;

    //KEYBOARD AND MOUSE
    public static ConfigValue<String> keyOverrideMap;
    public static ConfigValue<String> keyOverrideFuelLevel;
    public static ConfigValue<String> keyOverrideToggleAdvGoggles;
//    public static IntValue keyOverrideMapI;
//    public static IntValue keyOverrideFuelLevelI;
//    public static IntValue keyOverrideToggleAdvGogglesI;
    public static DoubleValue mapMouseScrollSensitivity;
    public static BooleanValue invertMapMouseScroll;

    public static ArrayList<Object> clientSave = null;
    //    private static Map<String, List<String>> propOrder = new TreeMap<>();
    private static String currentCat;

//    public static void initialize(File file)
//    {
//        ConfigManagerCore.config.get() = new Configuration(file);
//        ConfigManagerCore.syncConfig.get()(true);
//    }

//    public static void forceSave()
//    {
//        ConfigManagerCore.config.get().save();
//    }

    //    public static void syncConfig(boolean load)
    public ConfigManagerCore(Builder builder)
    {
        COMMON_BUILDER = builder;
        try
        {
//            propOrder.clear();
//            Property prop;
//
//            if (!config.isChild)
//            {
//                if (load)
//                {
//                    config.load();
//                }
//            }

//            enableDebug = COMMON_BUILDER.comment("If this is enabled, debug messages will appear in the console. This is useful for finding bugs in the mod.")
//                    .translation("gc.configgui.enable_debug")
//                    .define("enableDebug", false).get();
//
////            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Enable Debug Messages", false);
////            prop.setComment("If this is enabled, debug messages will appear in the console. This is useful for finding bugs in the mod.");
////            prop.setLanguageKey("gc.configgui.enable_debug");
////            enableDebug = prop.getBoolean(false);
////            finishProp(prop);
//
//            COMMON_BUILDER.push("Dimensions");
//
//            idDimensionOverworld = COMMON_BUILDER.comment("Dimension ID for the Overworld (as seen in the Celestial Map)")
//                    .translation("gc.configgui.id_dimension_overworld")
//                    .defineInRange("idDimensionOverworld", 0, Integer.MIN_VALUE, Integer.MAX_VALUE).get();
//
//            idDimensionOverworld = COMMON_BUILDER.comment("Dimension ID for the Overworld (as seen in the Celestial Map)")
//                    .translation("gc.configgui.id_dimension_overworld")
//                    .defineInRange("idDimensionOverworld", 0, -Double.MAX_VALUE, Double.MAX_VALUE).get();
//
//            idDimensionMoon = COMMON_BUILDER.comment("Dimension ID for the Moon")
//                    .translation("gc.configgui.id_dimension_moon")
//                    .defineInRange("idDimensionMoon", -28, Integer.MIN_VALUE, Integer.MAX_VALUE).get();
//
//            idDimensionOverworldOrbit = COMMON_BUILDER.comment("Dimension ID for the Moon")
//                    .translation("gc.configgui.id_dimension_overworld_orbit")
//                    .defineInRange("idDimensionOverworldOrbit", -27, Integer.MIN_VALUE, Integer.MAX_VALUE).get();
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIMENSIONS, "idDimensionOverworldOrbitStatic", -26);
//            prop.setComment("WorldProvider ID for Static Space Stations (advanced: do not change unless you have conflicts)");
//            prop.setLanguageKey("gc.configgui.id_dimension_overworld_orbit_static").setRequiresMcRestart(true);
//            idDimensionOverworldOrbitStatic = prop.getInt();
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIMENSIONS, "biomeIDBase", 102);
//            prop.setComment("Biome ID base. GC will use biome IDs from this to this + 3, or more with addons. Allowed 40-250. Default 102.");
//            prop.setLanguageKey("gc.configgui.biome_id_base").setRequiresMcRestart(true);
//            biomeIDbase = prop.getInt();
//            if (biomeIDbase < 40 || biomeIDbase > 250)
//            {
//                biomeIDbase = 102;
//            }
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIMENSIONS, "Static Loaded Dimensions", ConfigManagerCore.staticLoadDimensions.get());
//            prop.setComment("IDs to load at startup, and keep loaded until server stops. Can be added via /gckeeploaded");
//            prop.setLanguageKey("gc.configgui.static_loaded_dimensions");
//            staticLoadDimensions = prop.getIntList();
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIMENSIONS, "Set new Space Stations to be static loaded", true);
//            prop.setComment("Set this to true to have an automatic /gckeeploaded for any new Space Station created.");
//            prop.setLanguageKey("gc.configgui.static_loaded_new_ss");
//            keepLoadedNewSpaceStations = prop.getBoolean();
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIMENSIONS, "Dimensions where rockets cannot launch", new String[] { "1", "-1" });
//            prop.setComment("IDs of dimensions where rockets should not launch - this should always include the Nether.");
//            prop.setLanguageKey("gc.configgui.rocket_disabled_dimensions");
//            disableRocketLaunchDimensions = prop.getIntList();
//            disableRocketLaunchAllNonGC = searchAsterisk(prop.getStringList());
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIMENSIONS, "Disable rockets from returning to Overworld", false);
//            prop.setComment("If true, rockets will be unable to reach the Overworld (only use this in special modpacks!)");
//            prop.setLanguageKey("gc.configgui.rocket_disable_overworld_return");
//            disableRocketsToOverworld = prop.getBoolean(false);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_SERVER, "World border for landing location on other planets (Moon, Mars, etc)", 0);
//            prop.setComment("Set this to 0 for no borders (default).  If set to e.g. 2000, players will land on the Moon inside the x,z range -2000 to 2000.)");
//            prop.setLanguageKey("gc.configgui.planet_worldborders");
//            otherPlanetWorldBorders = prop.getInt(0);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Force Overworld Spawn", false);
//            prop.setComment("By default, you will respawn on Galacticraft dimensions if you die. If you are dying over and over on a planet, set this to true, and you will respawn back on the Overworld.");
//            prop.setLanguageKey("gc.configgui.force_overworld_respawn");
//            forceOverworldRespawn = prop.getBoolean(false);
//            finishProp(prop);
//
//            //
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicRocketT1", 0);
//            prop.setComment("Schematic ID for Tier 1 Rocket, must be unique.");
//            prop.setLanguageKey("gc.configgui.id_schematic_rocket_t1");
//            idSchematicRocketT1 = prop.getInt(0);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicMoonBuggy", 1);
//            prop.setComment("Schematic ID for Moon Buggy, must be unique.");
//            prop.setLanguageKey("gc.configgui.id_schematic_moon_buggy");
//            idSchematicMoonBuggy = prop.getInt(1);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicAddSchematic", Integer.MAX_VALUE);
//            prop.setComment("Schematic ID for \"Add Schematic\" Page, must be unique");
//            prop.setLanguageKey("gc.configgui.id_schematic_add_schematic");
//            idSchematicAddSchematic = prop.getInt(Integer.MAX_VALUE);
//            finishProp(prop);
//
//            //
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_ACHIEVEMENTS, "idAchievBase", 1784);
//            prop.setComment("Base Achievement ID. All achievement IDs will start at this number.");
//            prop.setLanguageKey("gc.configgui.id_achiev_base");
//            idAchievBase = prop.getInt(1784);
//            finishProp(prop);
//
////Client LogicalSide
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_CLIENT, "More Stars", true);
//            prop.setComment("Setting this to false will revert night skies back to default minecraft star count");
//            prop.setLanguageKey("gc.configgui.more_stars");
//            moreStars = prop.getBoolean(true);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_CLIENT, "Disable Spaceship Particles", false);
//            prop.setComment("If you have FPS problems, setting this to true will help if rocket particles are in your sights");
//            prop.setLanguageKey("gc.configgui.disable_spaceship_particles");
//            disableSpaceshipParticles = prop.getBoolean(false);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_CLIENT, "Disable Vehicle Third-Person and Zoom", false);
//            prop.setComment("If you're using this mod in virtual reality, or if you don't want the camera changes when entering a Galacticraft vehicle, set this to true.");
//            prop.setLanguageKey("gc.configgui.disable_vehicle_camera_changes");
//            disableVehicleCameraChanges = prop.getBoolean(false);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_CLIENT, "Minimap Left", false);
//            prop.setComment("If true, this will move the Oxygen Indicator to the left LogicalSide. You can combine this with \"Minimap Bottom\"");
//            prop.setLanguageKey("gc.configgui.oxygen_indicator_left");
//            oxygenIndicatorLeft = prop.getBoolean(false);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_CLIENT, "Minimap Bottom", false);
//            prop.setComment("If true, this will move the Oxygen Indicator to the bottom. You can combine this with \"Minimap Left\"");
//            prop.setLanguageKey("gc.configgui.oxygen_indicator_bottom");
//            oxygenIndicatorBottom = prop.getBoolean(false);
//            finishProp(prop);
//
////World gen
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Oil Generation Factor", 1.8);
//            prop.setComment("Increasing this will increase amount of oil that will generate in each chunk.");
//            prop.setLanguageKey("gc.configgui.oil_gen_factor");
//            oilGenFactor = prop.getDouble(1.8);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Oil gen in external dimensions", new int[] { 0 });
//            prop.setComment("List of non-galacticraft dimension IDs to generate oil in.");
//            prop.setLanguageKey("gc.configgui.external_oil_gen");
//            externalOilGen = prop.getIntList();
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Retro Gen of GC Oil in existing map chunks", false);
//            prop.setComment("If this is enabled, GC oil will be added to existing Overworld maps where possible.");
//            prop.setLanguageKey("gc.configgui.enable_retrogen_oil");
//            retrogenOil = prop.getBoolean(false);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Enable Copper Ore Gen", true);
//            prop.setComment("If this is enabled, copper ore will generate on the overworld.");
//            prop.setLanguageKey("gc.configgui.enable_copper_ore_gen").setRequiresMcRestart(true);
//            enableCopperOreGen = prop.getBoolean(true);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Enable Tin Ore Gen", true);
//            prop.setComment("If this is enabled, tin ore will generate on the overworld.");
//            prop.setLanguageKey("gc.configgui.enable_tin_ore_gen").setRequiresMcRestart(true);
//            enableTinOreGen = prop.getBoolean(true);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Enable Aluminum Ore Gen", true);
//            prop.setComment("If this is enabled, aluminum ore will generate on the overworld.");
//            prop.setLanguageKey("gc.configgui.enable_aluminum_ore_gen").setRequiresMcRestart(true);
//            enableAluminumOreGen = prop.getBoolean(true);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Enable Silicon Ore Gen", true);
//            prop.setComment("If this is enabled, silicon ore will generate on the overworld.");
//            prop.setLanguageKey("gc.configgui.enable_silicon_ore_gen").setRequiresMcRestart(true);
//            enableSiliconOreGen = prop.getBoolean(true);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Cheese Ore Gen on Moon", false);
//            prop.setComment("Disable Cheese Ore Gen on Moon.");
//            prop.setLanguageKey("gc.configgui.disable_cheese_moon");
//            disableCheeseMoon = prop.getBoolean(false);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Tin Ore Gen on Moon", false);
//            prop.setComment("Disable Tin Ore Gen on Moon.");
//            prop.setLanguageKey("gc.configgui.disable_tin_moon");
//            disableTinMoon = prop.getBoolean(false);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Copper Ore Gen on Moon", false);
//            prop.setComment("Disable Copper Ore Gen on Moon.");
//            prop.setLanguageKey("gc.configgui.disable_copper_moon");
//            disableCopperMoon = prop.getBoolean(false);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Sapphire Ore Gen on Moon", false);
//            prop.setComment("Disable Sapphire Ore Gen on Moon.");
//            prop.setLanguageKey("gc.configgui.disable_sapphire_moon");
//            disableSapphireMoon = prop.getBoolean(false);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Moon Village Gen", false);
//            prop.setComment("If true, moon villages will not generate.");
//            prop.setLanguageKey("gc.configgui.disable_moon_village_gen");
//            disableMoonVillageGen = prop.getBoolean(false);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Generate all other mods features on planets", false);
//            prop.setComment("If this is enabled, other mods' standard ores and all other features (eg. plants) can generate on the Moon and planets. Apart from looking wrong, this make cause 'Already Decorating!' type crashes.  NOT RECOMMENDED!  See Wiki.");
//            prop.setLanguageKey("gc.configgui.enable_other_mods_features");
//            enableOtherModsFeatures = prop.getBoolean(false);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Whitelist CoFHCore worldgen to generate its ores and lakes on planets", false);
//            prop.setComment("If generate other mods features is disabled as recommended, this setting can whitelist CoFHCore custom worldgen on planets.");
//            prop.setLanguageKey("gc.configgui.whitelist_co_f_h_core_gen");
//            whitelistCoFHCoreGen = prop.getBoolean(false);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Generate ThaumCraft wild nodes on planetary surfaces", true);
//            prop.setComment("If ThaumCraft is installed, ThaumCraft wild nodes can generate on the Moon and planets.");
//            prop.setLanguageKey("gc.configgui.enable_thaum_craft_nodes");
//            enableThaumCraftNodes = prop.getBoolean(true);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Other mods ores for GC to generate on the Moon and planets", new String[] {});
//            prop.setComment("Enter IDs of other mods' ores here for Galacticraft to generate them on the Moon and other planets. Format is BlockName or BlockName:metadata. Use optional parameters at end of each line: /RARE /UNCOMMON or /COMMON for rarity in a chunk; /DEEP /SHALLOW or /BOTH for height; /SINGLE /STANDARD or /LARGE for clump size; /XTRARANDOM for ores sometimes there sometimes not at all.  /ONLYMOON or /ONLYMARS if wanted on one planet only.  If nothing specified, defaults are /COMMON, /BOTH and /STANDARD.  Repeat lines to generate a huge quantity of ores.");
//            prop.setLanguageKey("gc.configgui.other_mod_ore_gen_i_ds");
//            oregenIDs = prop.getStringList();
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_COMPATIBILITY, "Use legacy oilgc fluid registration", false);
//            prop.setComment("Set to true to make Galacticraft oil register as oilgc, for backwards compatibility with previously generated worlds.");
//            prop.setLanguageKey("gc.configgui.use_old_oil_fluid_i_d");
//            useOldOilFluidID = prop.getBoolean(false);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_COMPATIBILITY, "Use legacy fuelgc fluid registration", false);
//            prop.setComment("Set to true to make Galacticraft fuel register as fuelgc, for backwards compatibility with previously generated worlds.");
//            prop.setLanguageKey("gc.configgui.use_old_fuel_fluid_i_d");
//            useOldFuelFluidID = prop.getBoolean(false);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Disable lander on Moon and other planets", false);
//            prop.setComment("If this is true, the player will parachute onto the Moon instead - use only in debug situations.");
//            prop.setLanguageKey("gc.configgui.disable_lander");
//            disableLander = prop.getBoolean(false);
//            finishProp(prop);
//
////Server LogicalSide
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Disable Spaceship Explosion", false);
//            prop.setComment("Spaceships will not explode on contact if set to true.");
//            prop.setLanguageKey("gc.configgui.disable_spaceship_grief");
//            disableSpaceshipGrief = prop.getBoolean(false);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_SERVER, "Space Stations Require Permission", true);
//            prop.setComment("While true, space stations require you to invite other players using /ssinvite <playername>");
//            prop.setLanguageKey("gc.configgui.space_stations_require_permission");
//            spaceStationsRequirePermission = prop.getBoolean(true);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_SERVER, "Disable Space Station creation", false);
//            prop.setComment("If set to true on a server, players will be completely unable to create space stations.");
//            prop.setLanguageKey("gc.configgui.disable_space_station_creation");
//            disableSpaceStationCreation = prop.getBoolean(false);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_CLIENT, "Override Capes", true);
//            prop.setComment("By default, Galacticraft will override capes with the mod's donor cape. Set to false to disable.");
//            prop.setLanguageKey("gc.configgui.override_capes");
//            overrideCapes = prop.getBoolean(true);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Space Station Solar Energy Multiplier", 2.0);
//            prop.setComment("Solar panels will work (default 2x) more effective on space stations.");
//            prop.setLanguageKey("gc.configgui.space_station_energy_scalar");
//            spaceStationEnergyScalar = prop.getDouble(2.0);
//            finishProp(prop);
//
//            try
//            {
//                prop = getConfig(Constants.CONFIG_CATEGORY_COMPATIBILITY, "External Sealable IDs", new String[] { Block.REGISTRY.getNameForObject(Blocks.GLASS_PANE) + ":0" });
//                prop.setComment("List non-opaque blocks from other mods (for example, special types of glass) that the Oxygen Sealer should recognize as solid seals. Format is BlockName or BlockName:metadata");
//                prop.setLanguageKey("gc.configgui.sealable_i_ds").setRequiresMcRestart(true);
//                sealableIDs = prop.getStringList();
//                finishProp(prop);
//            }
//            catch (Exception e)
//            {
//                FMLLog.severe("[Galacticraft] It appears you have installed the 'Dev' version of Galacticraft instead of the regular version (or vice versa).  Please re-install.");
//            }
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_COMPATIBILITY, "External Detectable IDs", new String[] {
//                    Block.REGISTRY.getNameForObject(Blocks.COAL_ORE).getResourcePath(),
//                    Block.REGISTRY.getNameForObject(Blocks.DIAMOND_ORE).getResourcePath(),
//                    Block.REGISTRY.getNameForObject(Blocks.GOLD_ORE).getResourcePath(),
//                    Block.REGISTRY.getNameForObject(Blocks.IRON_ORE).getResourcePath(),
//                    Block.REGISTRY.getNameForObject(Blocks.LAPIS_ORE).getResourcePath(),
//                    Block.REGISTRY.getNameForObject(Blocks.REDSTONE_ORE).getResourcePath(),
//                    Block.REGISTRY.getNameForObject(Blocks.LIT_REDSTONE_ORE).getResourcePath() });
//            prop.setComment("List blocks from other mods that the Sensor Glasses should recognize as solid blocks. Format is BlockName or BlockName:metadata.");
//            prop.setLanguageKey("gc.configgui.detectable_i_ds").setRequiresMcRestart(true);
//            detectableIDs = prop.getStringList();
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Quick Game Mode", false);
//            prop.setComment("Set this to true for less metal use in Galacticraft recipes (makes the game easier).");
//            prop.setLanguageKey("gc.configgui.quick_mode");
//            quickMode = prop.getBoolean(false);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Harder Difficulty", false);
//            prop.setComment("Set this to true for increased difficulty in modpacks (see forum for more info).");
//            prop.setLanguageKey("gc.configgui.hard_mode");
//            hardMode = prop.getBoolean(false);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Adventure Game Mode", false);
//            prop.setComment("Set this to true for a challenging adventure where the player starts the game stranded in the Asteroids dimension with low resources (only effective if Galacticraft Planets installed).");
//            prop.setLanguageKey("gc.configgui.asteroids_start");
//            challengeMode = prop.getBoolean(false);
//            if (!GalacticraftCore.isPlanetsLoaded)
//            {
//                challengeMode = false;
//            }
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Adventure Game Mode Flags", 15);
//            prop.setComment("Add together flags 8, 4, 2, 1 to enable the four elements of adventure game mode. Default 15.  1 = extended compressor recipes.  2 = mob drops and spawning.  4 = more trees in hollow asteroids.  8 = start stranded in Asteroids.");
//            prop.setLanguageKey("gc.configgui.asteroids_flags");
//            challengeFlags = prop.getInt(15);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Suffocation Cooldown", 100);
//            prop.setComment("Lower/Raise this value to change time between suffocation damage ticks (allowed range 50-250)");
//            prop.setLanguageKey("gc.configgui.suffocation_cooldown");
//            suffocationCooldown = Math.min(Math.max(50, prop.getInt(100)), 250);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Suffocation Damage", 2);
//            prop.setComment("Change this value to modify the damage taken per suffocation tick");
//            prop.setLanguageKey("gc.configgui.suffocation_damage");
//            suffocationDamage = prop.getInt(2);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Dungeon Boss Health Modifier", 1.0);
//            prop.setComment("Change this if you wish to balance the mod (if you have more powerful weapon mods).");
//            prop.setLanguageKey("gc.configgui.dungeon_boss_health_mod");
//            dungeonBossHealthMod = prop.getDouble(1.0);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_SERVER, "Enable Sealed edge checks", true);
//            prop.setComment("If this is enabled, areas sealed by Oxygen Sealers will run a seal check when the player breaks or places a block (or on block updates).  This should be enabled for a 100% accurate sealed status, but can be disabled on servers for performance reasons.");
//            prop.setLanguageKey("gc.configgui.enable_sealer_edge_checks");
//            enableSealerEdgeChecks = prop.getBoolean(true);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_COMPATIBILITY, "Alternate recipe for canisters", false);
//            prop.setComment("Enable this if the standard canister recipe causes a conflict.");
//            prop.setLanguageKey("gc.configgui.alternate_canister_recipe").setRequiresMcRestart(true);
//            alternateCanisterRecipe = prop.getBoolean(false);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_COMPATIBILITY, "OreDict name of other mod's silicon", "itemSilicon");
//            prop.setComment("This needs to match the OreDictionary name used in the other mod. Set a nonsense name to disable.");
//            prop.setLanguageKey("gc.configgui.ore_dict_silicon");
//            otherModsSilicon = prop.getString();
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_COMPATIBILITY, "Must use GC's own space metals in recipes", true);
//            prop.setComment("Should normally be true. If you set this to false, in a modpack with other mods with the same metals, players may be able to craft advanced GC items without travelling to Moon, Mars, Asteroids etc.");
//            prop.setLanguageKey("gc.configgui.disable_ore_dict_space_metals");
//            recipesRequireGCAdvancedMetals = prop.getBoolean(true);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_KEYS, "Open Galaxy Map", "KEY_M");
//            prop.setComment("Default Map key on first Galacticraft run only. After first run, change keys by Minecraft in-game Controls menu.  Valid settings: KEY_ followed by 0-9 or A-Z.");
//            prop.setLanguageKey("gc.configgui.override_map").setRequiresMcRestart(true);
//            keyOverrideMap = prop.getString();
//            keyOverrideMapI = parseKeyValue(keyOverrideMap);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_KEYS, "Open Rocket GUI", "KEY_G");
//            prop.setComment("Default Rocket/Fuel key on first Galacticraft run only. After first run, change keys by Minecraft in-game Controls menu.  Valid settings: KEY_ followed by 0-9 or A-Z.");
//            prop.setLanguageKey("gc.configgui.key_override_fuel_level").setRequiresMcRestart(true);
//            keyOverrideFuelLevel = prop.getString();
//            keyOverrideFuelLevelI = parseKeyValue(keyOverrideFuelLevel);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_KEYS, "Toggle Advanced Goggles", "KEY_K");
//            prop.setComment("Default Goggles key on first Galacticraft run only. After first run, change keys by Minecraft in-game Controls menu.  Valid settings: KEY_ followed by 0-9 or A-Z.");
//            prop.setLanguageKey("gc.configgui.key_override_toggle_adv_goggles").setRequiresMcRestart(true);
//            keyOverrideToggleAdvGoggles = prop.getString();
//            keyOverrideToggleAdvGogglesI = parseKeyValue(keyOverrideToggleAdvGoggles);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_COMPATIBILITY, "Rocket fuel factor", 1);
//            prop.setComment("The normal factor is 1.  Increase this to 2 - 5 if other mods with a lot of oil (e.g. BuildCraft) are installed to increase GC rocket fuel requirement.");
//            prop.setLanguageKey("gc.configgui.rocket_fuel_factor");
//            rocketFuelFactor = prop.getInt(1);
//            finishProp(prop);
//
////            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Map factor", 1);
////            prop.setComment("Allowed values 1-4 etc";
////            prop.setLanguageKey("gc.configgui.mapFactor");
////            mapfactor = prop.getInt(1);
////            finishProp(prop);
////
////            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Map size", 400);
////            prop.setComment("Suggested value 400";
////            prop.setLanguageKey("gc.configgui.mapSize");
////            mapsize = prop.getInt(400);
////            finishProp(prop);
////
//            prop = getConfig(Constants.CONFIG_CATEGORY_CONTROLS, "Map Scroll Mouse Sensitivity", 1.0);
//            prop.setComment("Increase to make the mouse drag scroll more sensitive, decrease to lower sensitivity.");
//            prop.setLanguageKey("gc.configgui.map_scroll_sensitivity");
//            mapMouseScrollSensitivity = (float) prop.getDouble(1.0);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_CONTROLS, "Map Scroll Mouse Invert", false);
//            prop.setComment("Set to true to invert the mouse scroll feature on the galaxy map.");
//            prop.setLanguageKey("gc.configgui.map_scroll_invert");
//            invertMapMouseScroll = prop.getBoolean(false);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Meteor Spawn Modifier", 1.0);
//            prop.setComment("Set to a value between 0.0 and 1.0 to decrease meteor spawn chance (all dimensions).");
//            prop.setLanguageKey("gc.configgui.meteor_spawn_mod");
//            meteorSpawnMod = prop.getDouble(1.0);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Meteor Block Damage Enabled", true);
//            prop.setComment("Set to false to stop meteors from breaking blocks on contact.");
//            prop.setLanguageKey("gc.configgui.meteor_block_damage");
//            meteorBlockDamageEnabled = prop.getBoolean(true);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Disable Update Check", false);
//            prop.setComment("Update check will not run if this is set to true.");
//            prop.setLanguageKey("gc.configgui.disable_update_check");
//            disableUpdateCheck = prop.getBoolean(false);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Allow liquids into Gratings", true);
//            prop.setComment("Liquids will not flow into Grating block if this is set to false.");
//            prop.setLanguageKey("gc.configgui.allow_liquids_grating").setRequiresMcRestart(true);
//            allowLiquidGratings = prop.getBoolean(true);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Biome Type Registrations", false);
//            prop.setComment("Biome Types will not be registered in the BiomeDictionary if this is set to true.");
//            prop.setLanguageKey("gc.configgui.disable_biome_type_registrations");
//            disableBiomeTypeRegistrations = prop.getBoolean(false);
//            finishProp(prop);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Enable Space Race Manager Popup", false);
//            prop.setComment("Space Race Manager will show on-screen after login, if enabled.");
//            prop.setLanguageKey("gc.configgui.enable_space_race_manager_popup");
//            enableSpaceRaceManagerPopup = prop.getBoolean(false);
//            finishProp(prop);

            builder.push("WORLDGEN");

            oilGenFactor = builder.comment("Increasing this will increase amount of oil that will generate in each chunk.")
                    .translation("gc.configgui.oil_gen_factor")
                    .defineInRange("oil_generation_factor", 1.8, -Double.MAX_VALUE, Double.MAX_VALUE);

//            externalOilGen = builder.comment("List of non-galacticraft dimension IDs to generate oil in.")
//                    .translation("gc.configgui.external_oil_gen")
//                    .define("oil_gen_in_external_dimension", new IntValue[1]);

            retrogenOil = builder.comment("If this is enabled, GC oil will be added to existing Overworld maps where possible.")
                    .translation("gc.configgui.enable_retrogen_oil")
                    .define("retro_gen_of_gc_oil_in_existing_map_chunks", false);

            enableCopperOreGen = builder.comment("If this is enabled, copper ore will generate on the overworld.")
                    .translation("gc.configgui.enable_copper_ore_gen")
                    .define("enable_copper_ore_gen", true);

            enableTinOreGen = builder.comment("If this is enabled, tin ore will generate on the overworld.")
                    .translation("gc.configgui.enable_tin_ore_gen")
                    .define("enable_tin_ore_gen", true);

            enableAluminumOreGen = builder.comment("If this is enabled, aluminum ore will generate on the overworld.")
                    .translation("gc.configgui.enable_aluminum_ore_gen")
                    .define("enable_aluminum_ore_gen", true);

            enableSiliconOreGen = builder.comment("If this is enabled, silicon ore will generate on the overworld.")
                    .translation("gc.configgui.enable_silicon_ore_gen")
                    .define("enable_silicon_ore_gen", true);

            disableCheeseMoon = builder.comment("Disable Cheese Ore Gen on Moon.")
                    .translation("gc.configgui.disable_cheese_moon")
                    .define("disable_cheese_ore_gen_on_moon", false);

            disableTinMoon = builder.comment("Disable Tin Ore Gen on Moon.")
                    .translation("gc.configgui.disable_tin_moon")
                    .define("disable_tin_ore_gen_on_moon", false);

            disableCopperMoon = builder.comment("Disable Copper Ore Gen on Moon.")
                    .translation("gc.configgui.disable_copper_moon")
                    .define("disable_copper_ore_gen_on_moon", false);

            disableSapphireMoon = builder.comment("Disable Sapphire Ore Gen on Moon.")
                    .translation("gc.configgui.disable_sapphire_moon")
                    .define("disable_sapphire_ore_gen_on_moon", false);

            disableMoonVillageGen = builder.comment("If true, moon villages will not generate.")
                    .translation("gc.configgui.disable_moon_village_gen")
                    .define("disable_moon_village_gen", false);

            enableOtherModsFeatures = builder.comment("If this is enabled, other mods' standard ores and all other features (eg. plants) can generate on the Moon and planets. Apart from looking wrong, this make cause 'Already Decorating!' type crashes.  NOT RECOMMENDED!  See Wiki.")
                    .translation("gc.configgui.enable_other_mods_features")
                    .define("generate_all_other_mods_features_on_planets", false);

            whitelistCoFHCoreGen = builder.comment("If generate other mods features is disabled as recommended, this setting can whitelist CoFHCore custom worldgen on planets.")
                    .translation("gc.configgui.whitelist_co_f_h_core_gen")
                    .define("whitelist_cofhcore_worldgen_to_generate_its_ores_and_lakes_on_planets", false);

            enableThaumCraftNodes = builder.comment("If ThaumCraft is installed, ThaumCraft wild nodes can generate on the Moon and planets.")
                    .translation("gc.configgui.enable_thaum_craft_nodes")
                    .define("generate_thaumcraft_wild_nodes_on_planetary_surfaces", true);

            oregenIDs = builder.comment("Enter IDs of other mods' ores here for Galacticraft to generate them on the Moon and other planets. Format is BlockName or BlockName:metadata. Use optional parameters at end of each line: /RARE /UNCOMMON or /COMMON for rarity in a chunk; /DEEP /SHALLOW or /BOTH for height; /SINGLE /STANDARD or /LARGE for clump size; /XTRARANDOM for ores sometimes there sometimes not at all.  /ONLYMOON or /ONLYMARS if wanted on one planet only.  If nothing specified, defaults are /COMMON, /BOTH and /STANDARD.  Repeat lines to generate a huge quantity of ores.")
                    .translation("gc.configgui.other_mod_ore_gen_i_ds")
                    .define("Other_mods_ores_for_gc_to_generate_on_the_moon_and_planets", Lists.newArrayList());

            disableBiomeTypeRegistrations = builder.comment("Biome Types will not be registered in the BiomeDictionary if this is set to true.")
                    .translation("gc.configgui.disable_biome_type_registrations")
                    .define("disable_biome_type_registrations", false);

            builder.pop();

            builder.push("DIMENSIONS");

//            idDimensionOverworld = builder.comment("Dimension ID for the Overworld (as seen in the Celestial Map)")
//                    .translation("gc.configgui.id_dimension_overworld")
//                    .defineInRange("iddimensionoverworld", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
//
//            idDimensionMoon = builder.comment("Dimension ID for the Moon")
//                    .translation("gc.configgui.id_dimension_moon")
//                    .defineInRange("iddimensionmoon", -28, Integer.MIN_VALUE, Integer.MAX_VALUE);
//
//            idDimensionOverworldOrbit = builder.comment("WorldProvider ID for Overworld Space Stations (advanced: do not change unless you have conflicts)")
//                    .translation("gc.configgui.id_dimension_overworld_orbit")
//                    .defineInRange("iddimensionoverworldorbit", -27, Integer.MIN_VALUE, Integer.MAX_VALUE);
//
//            idDimensionOverworldOrbitStatic = builder.comment("WorldProvider ID for Static Space Stations (advanced: do not change unless you have conflicts)")
//                    .translation("gc.configgui.id_dimension_overworld_orbit_static")
//                    .defineInRange("iddimensionoverworldorbitstatic", -26, Integer.MIN_VALUE, Integer.MAX_VALUE);

            biomeIDbase = builder.comment("Biome ID base. GC will use biome IDs from this to this + 3, or more with addons. Allowed 40-250. Default 102.")
                    .translation("gc.configgui.biome_id_base")
                    .defineInRange("biomeidbase", 102, Integer.MIN_VALUE, Integer.MAX_VALUE);

            staticLoadDimensions = builder.comment("IDs to load at startup, and keep loaded until server stops. Can be added via /gckeeploaded")
                    .translation("gc.configgui.static_loaded_dimensions")
                    .define("static_loaded_dimensions", Lists.newArrayList(""));

            keepLoadedNewSpaceStations = builder.comment("Set this to true to have an automatic /gckeeploaded for any new Space Station created.")
                    .translation("gc.configgui.static_loaded_new_ss")
                    .define("set_new_space_stations_to_be_static_loaded", true);

            disableRocketLaunchDimensions = builder.comment("IDs of dimensions where rockets should not launch - this should always include the Nether.")
                    .translation("gc.configgui.rocket_disabled_dimensions")
                    .define("dimensions_where_rockets_cannot_launch", Lists.newArrayList(DimensionType.THE_NETHER.getRegistryName().toString(), DimensionType.THE_END.getRegistryName().toString()));

            disableRocketsToOverworld = builder.comment("If true, rockets will be unable to reach the Overworld (only use this in special modpacks!)")
                    .translation("gc.configgui.rocket_disable_overworld_return")
                    .define("disable_rockets_from_returning_to_overworld", false);

            builder.pop();

            builder.push("ACHIEVEMENTS");

            idAchievBase = builder.comment("Base Achievement ID. All achievement IDs will start at this number.")
                    .translation("gc.configgui.id_achiev_base")
                    .defineInRange("idachievbase", 1784, Integer.MIN_VALUE, Integer.MAX_VALUE);

            builder.pop();

            builder.push("SCHEMATIC");

            idSchematicRocketT1 = builder.comment("Schematic ID for Tier 1 Rocket, must be unique.")
                    .translation("gc.configgui.id_schematic_rocket_t1")
                    .defineInRange("idschematicrockett1", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            idSchematicMoonBuggy = builder.comment("Schematic ID for Moon Buggy, must be unique.")
                    .translation("gc.configgui.id_schematic_moon_buggy")
                    .defineInRange("idschematicmoonbuggy", 1, Integer.MIN_VALUE, Integer.MAX_VALUE);

            idSchematicAddSchematic = builder.comment("Schematic ID for \"Add Schematic\" Page, must be unique")
                    .translation("gc.configgui.id_schematic_add_schematic")
                    .defineInRange("idschematicmoonbuggy", Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);

            builder.pop();

            builder.push("SERVER");

            otherPlanetWorldBorders = builder.comment("Set this to 0 for no borders (default).  If set to e.g. 2000, players will land on the Moon inside the x,z range -2000 to 2000.)")
                    .translation("gc.configgui.planet_worldborders")
                    .defineInRange("world_border_for_landing_location_on_other_planets_(moon,_mars,_etc)", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            spaceStationsRequirePermission = builder.comment("While true, space stations require you to invite other players using /ssinvite <playername>")
                    .translation("gc.configgui.space_stations_require_permission")
                    .define("space_stations_require_permission", true);

            disableSpaceStationCreation = builder.comment("If set to true on a server, players will be completely unable to create space stations.")
                    .translation("gc.configgui.disable_space_station_creation")
                    .define("disable_space_station_creation", false);

            enableSealerEdgeChecks = builder.comment("If this is enabled, areas sealed by Oxygen Sealers will run a seal check when the player breaks or places a block (or on block updates).  This should be enabled for a 100% accurate sealed status, but can be disabled on servers for performance reasons.")
                    .translation("gc.configgui.enable_sealer_edge_checks")
                    .define("enable_sealed_edge_checks", true);

            builder.pop();

            builder.push("KEYS");

            keyOverrideMap = builder.comment("Default Map key on first Galacticraft run only. After first run, change keys by Minecraft in-game Controls menu.  Valid settings: KEY_ followed by 0-9 or A-Z.")
                    .translation("gc.configgui.override_map")
                    .define("open_galaxy_map", "KEY_M");

            keyOverrideFuelLevel = builder.comment("Default Rocket/Fuel key on first Galacticraft run only. After first run, change keys by Minecraft in-game Controls menu.  Valid settings: KEY_ followed by 0-9 or A-Z.")
                    .translation("gc.configgui.key_override_fuel_level")
                    .define("open_rocket_gui", "KEY_G");

            keyOverrideToggleAdvGoggles = builder.comment("Default Goggles key on first Galacticraft run only. After first run, change keys by Minecraft in-game Controls menu.  Valid settings: KEY_ followed by 0-9 or A-Z.")
                    .translation("gc.configgui.key_override_toggle_adv_goggles")
                    .define("toggle_advanced_goggles", "KEY_K");

            builder.pop();

            builder.push("COMPATIBILITY");

            useOldOilFluidID = builder.comment("Set to true to make Galacticraft oil register as oilgc, for backwards compatibility with previously generated worlds.")
                    .translation("gc.configgui.use_old_oil_fluid_i_d")
                    .define("use_legacy_oilgc_fluid_registration", false);

            useOldFuelFluidID = builder.comment("Set to true to make Galacticraft fuel register as fuelgc, for backwards compatibility with previously generated worlds.")
                    .translation("gc.configgui.use_old_fuel_fluid_i_d")
                    .define("use_legacy_fuelgc_fluid_registration", false);

            sealableIDs = builder.comment("List non-opaque blocks from other mods (for example, special types of glass) that the Oxygen Sealer should recognize as solid seals. Format is BlockName or BlockName:metadata")
                    .translation("gc.configgui.sealable_i_ds")
                    .define("external_sealable_ids", Lists.newArrayList(Blocks.GLASS_PANE.getRegistryName().toString()));

            detectableIDs = builder.comment("List blocks from other mods that the Sensor Glasses should recognize as solid blocks. Format is BlockName or BlockName:metadata.")
                    .translation("gc.configgui.detectable_i_ds")
                    .define("external_detectable_ids", Lists.newArrayList(
                            Blocks.COAL_ORE.getRegistryName().getPath(),
                            Blocks.DIAMOND_ORE.getRegistryName().getPath(),
                            Blocks.GOLD_ORE.getRegistryName().getPath(),
                            Blocks.IRON_ORE.getRegistryName().getPath(),
                            Blocks.LAPIS_ORE.getRegistryName().getPath(),
                            Blocks.REDSTONE_ORE.getRegistryName().getPath()));

            alternateCanisterRecipe = builder.comment("Enable this if the standard canister recipe causes a conflict.")
                    .translation("gc.configgui.alternate_canister_recipe")
                    .define("alternate_recipe_for_canisters", false);

            otherModsSilicon = builder.comment("This needs to match the OreDictionary name used in the other mod. Set a nonsense name to disable.")
                    .translation("gc.configgui.ore_dict_silicon")
                    .define("oredict_name_of_other_mod's_silicon", "itemSilicon");

            recipesRequireGCAdvancedMetals = builder.comment("Should normally be true. If you set this to false, in a modpack with other mods with the same metals, players may be able to craft advanced GC items without travelling to Moon, Mars, Asteroids etc.")
                    .translation("gc.configgui.disable_ore_dict_space_metals")
                    .define("must_use_gc's_own_space_metals_in_recipes", true);

            rocketFuelFactor = builder.comment("The normal factor is 1.  Increase this to 2 - 5 if other mods with a lot of oil (e.g. BuildCraft) are installed to increase GC rocket fuel requirement.")
                    .translation("gc.configgui.rocket_fuel_factor")
                    .defineInRange("rocket_fuel_factor", 1, Integer.MIN_VALUE, Integer.MAX_VALUE);

            builder.pop();

            builder.push("ENERGY_COMPATIBILITY");

            EnergyConfigHandler.BC_RATIO = builder.comment("Buildcraft Conversion Ratio").defineInRange("bc_conv_ratio", 16F, 0.01F, 1000.0F);
            EnergyConfigHandler.IC2_RATIO = builder.comment("IndustrialCraft2 Conversion Ratio").defineInRange("ic2_conv_ratio", 6.557377049F, 0.01F, 1000F);
            EnergyConfigHandler.RF_RATIO = builder.comment("Redstone Flux Conversion Ratio").defineInRange("rf_conv_ratio", 1.6F, 0.001F, 100.0F);
            EnergyConfigHandler.MEKANISM_RATIO = builder.comment("Mekanism Conversion Ratio").defineInRange("mek_conv_ratio", 0.6557377049F, 0.001F, 100.0F);
            EnergyConfigHandler.conversionLossFactor = builder.comment("Loss factor when converting energy as a percentage (100 = no loss, 90 = 10% loss ...)").defineInRange("conv_loss_factor", 100, 5, 100);

            EnergyConfigHandler.displayEnergyUnitsBC = builder.comment("If BuildCraft is loaded, show Galacticraft machines energy as MJ instead of gJ?").define("display_energy_bc", false);
            EnergyConfigHandler.displayEnergyUnitsIC2 = builder.comment("If IndustrialCraft2 is loaded, show Galacticraft machines energy as EU instead of gJ?").define("display_energy_ic2", false);
            EnergyConfigHandler.displayEnergyUnitsMek = builder.comment("If Mekanism is loaded, show Galacticraft machines energy as Joules (J) instead of gJ?").define("display_energy_mek", false);
            EnergyConfigHandler.displayEnergyUnitsRF = builder.comment("Show Galacticraft machines energy in RF instead of gJ?").define("display_energy_rf", false);

            EnergyConfigHandler.disableMJinterface = builder.comment("Disable old Buildcraft API (MJ) interfacing completely?").define("disable_mj_interface", false);

            EnergyConfigHandler.disableBuildCraftInput = builder.comment("Disable INPUT of BuildCraft energy").define("disable_input_bc", false);
            EnergyConfigHandler.disableBuildCraftOutput = builder.comment("Disable OUTPUT of BuildCraft energy").define("disable_output_bc", false);
            EnergyConfigHandler.disableRFInput = builder.comment("Disable INPUT of RF energy").define("disable_input_rf", false);
            EnergyConfigHandler.disableRFOutput = builder.comment("Disable OUTPUT of RF energy").define("disable_output_rf", false);
            EnergyConfigHandler.disableFEInput = builder.comment("Disable INPUT of Forge Energy to GC machines").define("disable_input_forge", false);
            EnergyConfigHandler.disableFEOutput = builder.comment("Disable OUTPUT of Forge Energy from GC machines").define("disable_output_forge", false);
            EnergyConfigHandler.disableIC2Input = builder.comment("Disable INPUT of IC2 energy").define("disable_input_ic2", false);
            EnergyConfigHandler.disableIC2Output = builder.comment("Disable OUTPUT of IC2 energy").define("disable_output_ic2", false);
            EnergyConfigHandler.disableMekanismInput = builder.comment("Disable INPUT of Mekanism energy").define("disable_input_mek", false);
            EnergyConfigHandler.disableMekanismOutput = builder.comment("Disable OUTPUT of Mekanism energy").define("disable_output_mek", false);

            builder.pop();

            builder.push("GENERAL");

            enableDebug = builder.comment("If this is enabled, debug messages will appear in the console. This is useful for finding bugs in the mod.")
                    .translation("gc.configgui.enable_debug")
                    .define("enable_debug_messages", false);

            forceOverworldRespawn = builder.comment("By default, you will respawn on Galacticraft dimensions if you die. If you are dying over and over on a planet, set this to true, and you will respawn back on the Overworld.")
                    .translation("gc.configgui.force_overworld_respawn")
                    .define("force_overworld_spawn", false);

            disableLander = builder.comment("If this is true, the player will parachute onto the Moon instead - use only in debug situations.")
                    .translation("gc.configgui.disable_lander")
                    .define("disable_lander_on_moon_and_other_planets", false);

//            mapfactor = builder.comment("null") .translation("gc.configgui.mapFactor").defineInRange("map_factor", 1, Integer.MIN_VALUE, Integer.MAX_VALUE);

//            mapsize = builder.comment("null").translation("gc.configgui.mapSize").defineInRange("map_size", 400, Integer.MIN_VALUE, Integer.MAX_VALUE);

            disableUpdateCheck = builder.comment("Update check will not run if this is set to true.")
                    .translation("gc.configgui.disable_update_check")
                    .define("disable_update_check", false);

            allowLiquidGratings = builder.comment("Liquids will not flow into Grating block if this is set to false.")
                    .translation("gc.configgui.allow_liquids_grating")
                    .define("allow_liquids_into_gratings", true);

            enableSpaceRaceManagerPopup = builder.comment("Space Race Manager will show on-screen after login, if enabled.")
                    .translation("gc.configgui.enable_space_race_manager_popup")
                    .define("enable_space_race_manager_popup", false);

            builder.pop();

            builder.push("CONTROLS");

            mapMouseScrollSensitivity = builder.comment("Increase to make the mouse drag scroll more sensitive, decrease to lower sensitivity.")
                    .translation("gc.configgui.map_scroll_sensitivity")
                    .defineInRange("map_scroll_mouse_sensitivity", 1.0, 0.0, Float.MAX_VALUE);

            invertMapMouseScroll = builder.comment("Set to true to invert the mouse scroll feature on the galaxy map.")
                    .translation("gc.configgui.map_scroll_invert")
                    .define("map_scroll_mouse_invert", false);

            builder.pop();

            builder.push("CLIENT");

            moreStars = builder.comment("Setting this to false will revert night skies back to default minecraft star count")
                    .translation("gc.configgui.more_stars")
                    .define("more_stars", true);

            disableSpaceshipParticles = builder.comment("If you have FPS problems, setting this to true will help if rocket particles are in your sights")
                    .translation("gc.configgui.disable_spaceship_particles")
                    .define("disable_spaceship_particles", false);

            disableVehicleCameraChanges = builder.comment("If you're using this mod in virtual reality, or if you don't want the camera changes when entering a Galacticraft vehicle, set this to true.")
                    .translation("gc.configgui.disable_vehicle_camera_changes")
                    .define("disable_vehicle_third-person_and_zoom", false);

            oxygenIndicatorLeft = builder.comment("If true, this will move the Oxygen Indicator to the left LogicalSide. You can combine this with \"Minimap Bottom\"")
                    .translation("gc.configgui.oxygen_indicator_left")
                    .define("minimap_left", false);

            oxygenIndicatorBottom = builder.comment("If true, this will move the Oxygen Indicator to the bottom. You can combine this with \"Minimap Left\"")
                    .translation("gc.configgui.oxygen_indicator_bottom")
                    .define("minimap_bottom", false);

            overrideCapes = builder.comment("By default, Galacticraft will override capes with the mod's donor cape. Set to false to disable.")
                    .translation("gc.configgui.override_capes")
                    .define("override_capes", true);

            builder.pop();

            builder.push("DIFFICULTY");

            disableSpaceshipGrief = builder.comment("Spaceships will not explode on contact if set to true.")
                    .translation("gc.configgui.disable_spaceship_grief")
                    .define("disable_spaceship_explosion", false);

            spaceStationEnergyScalar = builder.comment("Solar panels will work (default 2x) more effective on space stations.")
                    .translation("gc.configgui.space_station_energy_scalar")
                    .defineInRange("space_station_solar_energy_multiplier", 2.0, -Double.MAX_VALUE, Double.MAX_VALUE);

            quickMode = builder.comment("Set this to true for less metal use in Galacticraft recipes (makes the game easier).")
                    .translation("gc.configgui.quick_mode")
                    .define("quick_game_mode", false);

            hardMode = builder.comment("Set this to true for increased difficulty in modpacks (see forum for more info).")
                    .translation("gc.configgui.hard_mode")
                    .define("harder_difficulty", false);

            challengeMode = builder.comment("Set this to true for a challenging adventure where the player starts the game stranded in the Asteroids dimension with low resources (only effective if Galacticraft Planets installed).")
                    .translation("gc.configgui.asteroids_start")
                    .define("adventure_game_mode", false);

            challengeFlags = builder.comment("Add together flags 8, 4, 2, 1 to enable the four elements of adventure game mode. Default 15.  1 = extended compressor recipes.  2 = mob drops and spawning.  4 = more trees in hollow asteroids.  8 = start stranded in Asteroids.")
                    .translation("gc.configgui.asteroids_flags")
                    .defineInRange("adventure_game_mode_flags", 15, Integer.MIN_VALUE, Integer.MAX_VALUE);

            suffocationCooldown = builder.comment("Lower/Raise this value to change time between suffocation damage ticks (allowed range 50-250)")
                    .translation("gc.configgui.suffocation_cooldown")
                    .defineInRange("suffocation_cooldown", 100, 50, 250);

            suffocationDamage = builder.comment("Change this value to modify the damage taken per suffocation tick")
                    .translation("gc.configgui.suffocation_damage")
                    .defineInRange("suffocation_damage", 2, Integer.MIN_VALUE, Integer.MAX_VALUE);

            dungeonBossHealthMod = builder.comment("Change this if you wish to balance the mod (if you have more powerful weapon mods).")
                    .translation("gc.configgui.dungeon_boss_health_mod")
                    .defineInRange("dungeon_boss_health_modifier", 1.0, -Double.MAX_VALUE, Double.MAX_VALUE);

            meteorSpawnMod = builder.comment("Set to a value between 0.0 and 1.0 to decrease meteor spawn chance (all dimensions).")
                    .translation("gc.configgui.meteor_spawn_mod")
                    .defineInRange("meteor_spawn_modifier", 1.0, -Double.MAX_VALUE, Double.MAX_VALUE);

            meteorBlockDamageEnabled = builder.comment("Set to false to stop meteors from breaking blocks on contact.")
                    .translation("gc.configgui.meteor_block_damage")
                    .define("meteor_block_damage_enabled", true);

            builder.pop();

            challengeModeUpdate();
        }
        catch (final Exception e)
        {
            GCLog.severe("Problem loading core config (\"core.conf\")");
            e.printStackTrace();
        }
    }

    public static void onConfigEvent()
    {
        EnergyConfigHandler.BC8_INTERNAL_RATIO = (float) (EnergyConfigHandler.BC_RATIO.get() / EnergyConfigHandler.BC8_MICROJOULE_RATIO);
        EnergyConfigHandler.TO_BC_RATIO = (float) (1 / EnergyConfigHandler.BC_RATIO.get() * EnergyConfigHandler.BC8_MICROJOULE_RATIO);
        EnergyConfigHandler.TO_RF_RATIO = (float) (1 / EnergyConfigHandler.RF_RATIO.get());
        EnergyConfigHandler.TO_IC2_RATIO = (float) (1 / EnergyConfigHandler.IC2_RATIO.get());
        EnergyConfigHandler.TO_MEKANISM_RATIO = (float) (1 / EnergyConfigHandler.MEKANISM_RATIO.get());
        EnergyConfigHandler.TO_BC_RATIOdisp = (float) (1 / EnergyConfigHandler.BC_RATIO.get());
        EnergyConfigHandler.TO_RF_RATIOdisp = (float) (1 / EnergyConfigHandler.RF_RATIO.get());
        EnergyConfigHandler.TO_IC2_RATIOdisp = (float) (1 / EnergyConfigHandler.IC2_RATIO.get());
        EnergyConfigHandler.TO_MEKANISM_RATIOdisp = (float) (1 / EnergyConfigHandler.MEKANISM_RATIO.get());
        EnergyConfigHandler.updateRatios();

        if (!EnergyConfigHandler.isIndustrialCraft2Loaded())
        {
            EnergyConfigHandler.displayEnergyUnitsIC2.set(false);
        }
        if (!EnergyConfigHandler.isMekanismLoaded())
        {
            EnergyConfigHandler.displayEnergyUnitsMek.set(false);
        }
        if (EnergyConfigHandler.displayEnergyUnitsIC2.get())
        {
            EnergyConfigHandler.displayEnergyUnitsBC.set(false);
        }
        if (EnergyConfigHandler.displayEnergyUnitsMek.get())
        {
            EnergyConfigHandler.displayEnergyUnitsBC.set(false);
            EnergyConfigHandler.displayEnergyUnitsIC2.set(false);
        }
        if (EnergyConfigHandler.displayEnergyUnitsRF.get())
        {
            EnergyConfigHandler.displayEnergyUnitsBC.set(false);
            EnergyConfigHandler.displayEnergyUnitsIC2.set(false);
            EnergyConfigHandler.displayEnergyUnitsMek.set(false);
        }
    }

    public boolean setLoaded(DimensionType newID)
    {
        boolean found = false;

        for (String staticLoadDimension : ConfigManagerCore.staticLoadDimensions.get())
        {
            if (staticLoadDimension.equals(newID.getRegistryName().toString()))
            {
                found = true;
                break;
            }
        }

        if (!found)
        {
            ConfigManagerCore.staticLoadDimensions.get().add(newID.getRegistryName().toString());
            Collections.sort(ConfigManagerCore.staticLoadDimensions.get());

            COMMON_BUILDER.comment("IDs to load at startup, and keep loaded until server stops. Can be added via /gckeeploaded")
                    .translation("gc.configgui.static_loaded_dimensions")
                    .define("static_loaded_dimensions", ConfigManagerCore.staticLoadDimensions.get()).set(ConfigManagerCore.staticLoadDimensions.get());
        }

        return !found;
    }

    public boolean setUnloaded(DimensionType idToRemove)
    {
        int foundCount = 0;

        for (String staticLoadDimension : ConfigManagerCore.staticLoadDimensions.get())
        {
            if (staticLoadDimension.equals(idToRemove.getRegistryName().toString()))
            {
                foundCount++;
            }
        }

        if (foundCount > 0)
        {
            ConfigManagerCore.staticLoadDimensions.get().remove(idToRemove.getRegistryName().toString());
            Collections.sort(ConfigManagerCore.staticLoadDimensions.get());

            COMMON_BUILDER.comment("IDs to load at startup, and keep loaded until server stops. Can be added via /gckeeploaded")
                    .translation("gc.configgui.static_loaded_dimensions")
                    .define("static_loaded_dimensions", ConfigManagerCore.staticLoadDimensions.get()).set(ConfigManagerCore.staticLoadDimensions.get());
        }

        return foundCount > 0;
    }

    public static void challengeModeUpdate()
    {
        if (challengeMode.get())
        {
            challengeRecipes = (challengeFlags.get() & 1) > 0;
            challengeMobDropsAndSpawning = (challengeFlags.get() & 2) > 0;
            challengeAsteroidPopulation = (challengeFlags.get() & 4) > 0;
            challengeSpawnHandling = (challengeFlags.get() & 8) > 0;
        }
        else
        {
            challengeRecipes = false;
            challengeMobDropsAndSpawning = false;
            challengeAsteroidPopulation = false;
            challengeSpawnHandling = false;
        }
    }

    /**
     * Note for this to be effective, the prop = config.get() call has to provide a String[] as the default values
     * If you use an Integer[] then the config parser deletes all non-numerical lines from the config before GC even sees them
     */
    private static boolean searchAsterisk(String[] strings)
    {
        for (String s : strings)
        {
            if (s != null && "*".equals(s.trim()))
            {
                return true;
            }
        }
        return false;
    }

//    public static List<IConfigElement> getConfigElements()
//    {
//        List<IConfigElement> list = new ArrayList<IConfigElement>();
//        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_DIFFICULTY)).getChildElements());
//        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_GENERAL)).getChildElements());
//        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_CLIENT)).getChildElements());
//        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_CONTROLS)).getChildElements());
//        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_COMPATIBILITY)).getChildElements());
//        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_WORLDGEN)).getChildElements());
//        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_SERVER)).getChildElements());
//        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_DIMENSIONS)).getChildElements());
//        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_SCHEMATIC)).getChildElements());
//        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_ACHIEVEMENTS)).getChildElements());
//        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_ENTITIES)).getChildElements());
//
//        return list;
//    }

    public static Block stringToBlock(ResourceLocation name, String caller, boolean logging)
    {
        Block block = ForgeRegistries.BLOCKS.getValue(name);
        if (block == null)
        {
            Item item = ForgeRegistries.ITEMS.getValue(name);
            if (item instanceof BlockItem)
            {
                block = ((BlockItem) item).getBlock();
            }
            if (block == null)
            {
                if (logging)
                {
                    GCLog.severe("[config] " + caller + ": unrecognised block name '" + name + "'.");
                }
                return null;
            }
        }
        if (Blocks.AIR == block)
        {
            if (logging)
            {
                GCLog.info("[config] " + caller + ": not a good idea to specify air, skipping that!");
            }
            return null;
        }

        return block;
    }

//    public static List<Object> getServerConfigOverride()
//    {
//    	ArrayList<Object> returnList = new ArrayList<>();
//    	int modeFlags = ConfigManagerCore.hardMode.get() ? 1 : 0;
//    	modeFlags += ConfigManagerCore.quickMode.get() ? 2 : 0;
//    	modeFlags += ConfigManagerCore.challengeMode.get() ? 4 : 0;
//    	modeFlags += ConfigManagerCore.disableSpaceStationCreation.get() ? 8 : 0;
//    	modeFlags += ConfigManagerCore.recipesRequireGCAdvancedMetals.get() ? 16 : 0;
//    	modeFlags += ConfigManagerCore.challengeRecipes.get() ? 32 : 0;
//        modeFlags += ConfigManagerCore.allowLiquidGratings.get() ? 64 : 0;
//    	returnList.add(modeFlags);
//    	returnList.add(ConfigManagerCore.dungeonBossHealthMod.get());
//    	returnList.add(ConfigManagerCore.suffocationDamage.get());
//    	returnList.add(ConfigManagerCore.suffocationCooldown.get());
//    	returnList.add(ConfigManagerCore.rocketFuelFactor.get());
//    	returnList.add(ConfigManagerCore.otherModsSilicon.get());
//    	//If changing this, update definition of EnumSimplePacket.C_UPDATE_CONFIGS - see comment in setConfigOverride() below
//    	EnergyConfigHandler.serverConfigOverride(returnList);
//
//    	returnList.add(ConfigManagerCore.detectableIDs.get().clone());
//    	//TODO Should this include any other client-LogicalSide configurables too?
//    	return returnList;
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    public static void setConfigOverride(List<Object> configs)
//    {
//        int dataCount = 0;
//    	int modeFlag = (Integer) configs.get(dataCount++);
//    	ConfigManagerCore.hardMode.get() = (modeFlag & 1) != 0;
//    	ConfigManagerCore.quickMode.get() = (modeFlag & 2) != 0;
//    	ConfigManagerCore.challengeMode.get() = (modeFlag & 4) != 0;
//    	ConfigManagerCore.disableSpaceStationCreation.get() = (modeFlag & 8) != 0;
//    	ConfigManagerCore.recipesRequireGCAdvancedMetals.get() = (modeFlag & 16) != 0;
//    	ConfigManagerCore.challengeRecipes.get() = (modeFlag & 32) != 0;
//        ConfigManagerCore.allowLiquidGratings.get() = (modeFlag & 64) != 0;
//    	ConfigManagerCore.dungeonBossHealthMod.get() = (Double) configs.get(dataCount++);
//    	ConfigManagerCore.suffocationDamage.get() = (Integer) configs.get(dataCount++);
//    	ConfigManagerCore.suffocationCooldown.get() = (Integer) configs.get(dataCount++);
//    	ConfigManagerCore.rocketFuelFactor.get() = (Integer) configs.get(dataCount++);
//    	ConfigManagerCore.otherModsSilicon.get() = (String) configs.get(dataCount++);
//    	//If adding any additional data objects here, also remember to update the packet definition of EnumSimplePacket.C_UPDATE_CONFIGS in PacketSimple
//    	//Current working packet definition: Integer.class, Double.class, Integer.class, Integer.class, Integer.class, String.class, Float.class, Float.class, Float.class, Float.class, Integer.class, String[].class
//
//    	EnergyConfigHandler.setConfigOverride((Float) configs.get(dataCount++), (Float) configs.get(dataCount++), (Float) configs.get(dataCount++), (Float) configs.get(dataCount++), (Integer) configs.get(dataCount++));
//
//    	int sizeIDs = configs.size() - dataCount;
//    	if (sizeIDs > 0)
//    	{
//    	    Object dataLast = configs.get(dataCount);
//    		if (dataLast instanceof String)
//    		{
//    			ConfigManagerCore.detectableIDs.get() = new String[sizeIDs];
//		    	for (int j = 0; j < sizeIDs; j++)
//		    	ConfigManagerCore.detectableIDs.get()[j] = new String((String) configs.get(dataCount++));
//    		}
//    		else if (dataLast instanceof String[])
//    		{
//    			ConfigManagerCore.detectableIDs.get() = ((String[])dataLast);
//    		}
//        	TickHandlerClient.registerDetectableBlocks(false);
//    	}
//
//    	challengeModeUpdate();
//    	RecipeManagerGC.setConfigurableRecipes();
//    }

//    public static void saveClientConfigOverrideable()
//    {
//        if (ConfigManagerCore.clientSave.get() == null)
//        {
//            ConfigManagerCore.clientSave.get() = (ArrayList<Object>) ConfigManagerCore.getServerConfigOverride.get()();
//        }
//    }
//
//    public static void restoreClientConfigOverrideable()
//    {
//        if (ConfigManagerCore.clientSave.get() != null)
//        {
//            ConfigManagerCore.setConfigOverride.get()(clientSave);
//        }
//    }

//    private static int parseKeyValue(String key)
//    {
//        if (key.equals("KEY_A"))
//        {
//            return Keyboard.KEY_A;
//        }
//        else if (key.equals("KEY_B"))
//        {
//            return Keyboard.KEY_B;
//        }
//        else if (key.equals("KEY_C"))
//        {
//            return Keyboard.KEY_C;
//        }
//        else if (key.equals("KEY_D"))
//        {
//            return Keyboard.KEY_D;
//        }
//        else if (key.equals("KEY_E"))
//        {
//            return Keyboard.KEY_E;
//        }
//        else if (key.equals("KEY_F"))
//        {
//            return Keyboard.KEY_F;
//        }
//        else if (key.equals("KEY_G"))
//        {
//            return Keyboard.KEY_G;
//        }
//        else if (key.equals("KEY_H"))
//        {
//            return Keyboard.KEY_H;
//        }
//        else if (key.equals("KEY_I"))
//        {
//            return Keyboard.KEY_I;
//        }
//        else if (key.equals("KEY_J"))
//        {
//            return Keyboard.KEY_J;
//        }
//        else if (key.equals("KEY_K"))
//        {
//            return Keyboard.KEY_K;
//        }
//        else if (key.equals("KEY_L"))
//        {
//            return Keyboard.KEY_L;
//        }
//        else if (key.equals("KEY_M"))
//        {
//            return Keyboard.KEY_M;
//        }
//        else if (key.equals("KEY_N"))
//        {
//            return Keyboard.KEY_N;
//        }
//        else if (key.equals("KEY_O"))
//        {
//            return Keyboard.KEY_O;
//        }
//        else if (key.equals("KEY_P"))
//        {
//            return Keyboard.KEY_P;
//        }
//        else if (key.equals("KEY_Q"))
//        {
//            return Keyboard.KEY_Q;
//        }
//        else if (key.equals("KEY_R"))
//        {
//            return Keyboard.KEY_R;
//        }
//        else if (key.equals("KEY_S"))
//        {
//            return Keyboard.KEY_S;
//        }
//        else if (key.equals("KEY_T"))
//        {
//            return Keyboard.KEY_T;
//        }
//        else if (key.equals("KEY_U"))
//        {
//            return Keyboard.KEY_U;
//        }
//        else if (key.equals("KEY_V"))
//        {
//            return Keyboard.KEY_V;
//        }
//        else if (key.equals("KEY_W"))
//        {
//            return Keyboard.KEY_W;
//        }
//        else if (key.equals("KEY_X"))
//        {
//            return Keyboard.KEY_X;
//        }
//        else if (key.equals("KEY_Y"))
//        {
//            return Keyboard.KEY_Y;
//        }
//        else if (key.equals("KEY_Z"))
//        {
//            return Keyboard.KEY_Z;
//        }
//        else if (key.equals("KEY_1"))
//        {
//            return Keyboard.KEY_1;
//        }
//        else if (key.equals("KEY_2"))
//        {
//            return Keyboard.KEY_2;
//        }
//        else if (key.equals("KEY_3"))
//        {
//            return Keyboard.KEY_3;
//        }
//        else if (key.equals("KEY_4"))
//        {
//            return Keyboard.KEY_4;
//        }
//        else if (key.equals("KEY_5"))
//        {
//            return Keyboard.KEY_5;
//        }
//        else if (key.equals("KEY_6"))
//        {
//            return Keyboard.KEY_6;
//        }
//        else if (key.equals("KEY_7"))
//        {
//            return Keyboard.KEY_7;
//        }
//        else if (key.equals("KEY_8"))
//        {
//            return Keyboard.KEY_8;
//        }
//        else if (key.equals("KEY_9"))
//        {
//            return Keyboard.KEY_9;
//        }
//        else if (key.equals("KEY_0"))
//        {
//            return Keyboard.KEY_0;
//        }
//
//        GCLog.severe("Failed to parse keyboard key: " + key + "... Use values A-Z or 0-9");
//
//        return 0;
//    }
}
