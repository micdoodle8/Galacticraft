package micdoodle8.mods.galacticraft.core.util;

import com.google.common.primitives.Ints;
import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConfigManagerCore
{
//    static Configuration config;

    ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    // GAME CONTROL
    public static boolean forceOverworldRespawn;
    public static boolean hardMode;
    public static boolean quickMode;
	public static boolean challengeMode;
	private static int challengeFlags;
	public static boolean challengeRecipes;
	public static boolean challengeMobDropsAndSpawning;
	public static boolean challengeSpawnHandling;
	public static boolean challengeAsteroidPopulation;
    public static boolean disableRocketsToOverworld;
    public static boolean disableSpaceStationCreation;
    public static boolean spaceStationsRequirePermission;
    public static boolean disableUpdateCheck;
    public static boolean enableSpaceRaceManagerPopup;
    public static boolean enableDebug;
    public static boolean enableSealerEdgeChecks;
    public static boolean disableLander;
    public static boolean recipesRequireGCAdvancedMetals = true;
    public static boolean allowLiquidGratings;
//    public static int mapfactor;
//    public static int mapsize;

    // DIMENSIONS
//    public static int idDimensionOverworld;
//    public static int idDimensionOverworldOrbit;
//    public static int idDimensionOverworldOrbitStatic;
//    public static int idDimensionMoon;
    public static int biomeIDbase = 102;
    public static boolean disableBiomeTypeRegistrations;
    public static String[] staticLoadDimensions = {};
    public static String[] disableRocketLaunchDimensions = { DimensionType.THE_NETHER.getRegistryName().toString(), DimensionType.THE_END.getRegistryName().toString() };
    public static boolean disableRocketLaunchAllNonGC;
    public static int otherPlanetWorldBorders = 0;
    public static boolean keepLoadedNewSpaceStations;

    // SCHEMATICS
    public static int idSchematicRocketT1;
    public static int idSchematicMoonBuggy;
    public static int idSchematicAddSchematic;

    // ACHIEVEMENTS
    public static int idAchievBase;

    // CLIENT / VISUAL FX
    public static boolean moreStars;
    public static boolean disableSpaceshipParticles;
    public static boolean disableVehicleCameraChanges;
    public static boolean oxygenIndicatorLeft;
    public static boolean oxygenIndicatorBottom;
    public static boolean overrideCapes;

    //DIFFICULTY
    public static double dungeonBossHealthMod;
    public static int suffocationCooldown;
    public static int suffocationDamage;
    public static int rocketFuelFactor;
    public static double meteorSpawnMod;
    public static boolean meteorBlockDamageEnabled;
    public static boolean disableSpaceshipGrief;
    public static double spaceStationEnergyScalar;

    // WORLDGEN
    public static boolean enableCopperOreGen;
    public static boolean enableTinOreGen;
    public static boolean enableAluminumOreGen;
    public static boolean enableSiliconOreGen;
    public static boolean disableCheeseMoon;
    public static boolean disableTinMoon;
    public static boolean disableCopperMoon;
    public static boolean disableMoonVillageGen;
    public static boolean disableSapphireMoon;
    public static int[] externalOilGen;
    public static double oilGenFactor;
    public static boolean retrogenOil;
    public static String[] oregenIDs = {};
    public static boolean enableOtherModsFeatures;
    public static boolean whitelistCoFHCoreGen;
    public static boolean enableThaumCraftNodes;

    //COMPATIBILITY
    public static String[] sealableIDs = {};
    public static String[] detectableIDs = {};
    public static boolean alternateCanisterRecipe;
    public static String otherModsSilicon;
    public static boolean useOldOilFluidID;
    public static boolean useOldFuelFluidID;

    //KEYBOARD AND MOUSE
    public static String keyOverrideMap;
    public static String keyOverrideFuelLevel;
    public static String keyOverrideToggleAdvGoggles;
    public static int keyOverrideMapI;
    public static int keyOverrideFuelLevelI;
    public static int keyOverrideToggleAdvGogglesI;
    public static float mapMouseScrollSensitivity;
    public static boolean invertMapMouseScroll;

    public static ArrayList<Object> clientSave = null;
//    private static Map<String, List<String>> propOrder = new TreeMap<>();
    private static String currentCat;

//    public static void initialize(File file)
//    {
//        ConfigManagerCore.config = new Configuration(file);
//        ConfigManagerCore.syncConfig(true);
//    }

//    public static void forceSave()
//    {
//        ConfigManagerCore.config.save();
//    }

//    public static void syncConfig(boolean load)
    public ConfigManagerCore()
    {
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
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIMENSIONS, "Static Loaded Dimensions", ConfigManagerCore.staticLoadDimensions);
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

            COMMON_BUILDER.push("WORLDGEN");

            oilGenFactor = COMMON_BUILDER.comment("Increasing this will increase amount of oil that will generate in each chunk.")
                    .translation("gc.configgui.oil_gen_factor")
                    .defineInRange("oil_generation_factor", 1.8, -Double.MAX_VALUE, Double.MAX_VALUE).get();

            externalOilGen = COMMON_BUILDER.comment("List of non-galacticraft dimension IDs to generate oil in.")
                    .translation("gc.configgui.external_oil_gen")
                    .define("oil_gen_in_external_dimension", new int[] { 0 }).get();

            retrogenOil = COMMON_BUILDER.comment("If this is enabled, GC oil will be added to existing Overworld maps where possible.")
                    .translation("gc.configgui.enable_retrogen_oil")
                    .define("retro_gen_of_gc_oil_in_existing_map_chunks", false).get();

            enableCopperOreGen = COMMON_BUILDER.comment("If this is enabled, copper ore will generate on the overworld.")
                    .translation("gc.configgui.enable_copper_ore_gen")
                    .define("enable_copper_ore_gen", true).get();

            enableTinOreGen = COMMON_BUILDER.comment("If this is enabled, tin ore will generate on the overworld.")
                    .translation("gc.configgui.enable_tin_ore_gen")
                    .define("enable_tin_ore_gen", true).get();

            enableAluminumOreGen = COMMON_BUILDER.comment("If this is enabled, aluminum ore will generate on the overworld.")
                    .translation("gc.configgui.enable_aluminum_ore_gen")
                    .define("enable_aluminum_ore_gen", true).get();

            enableSiliconOreGen = COMMON_BUILDER.comment("If this is enabled, silicon ore will generate on the overworld.")
                    .translation("gc.configgui.enable_silicon_ore_gen")
                    .define("enable_silicon_ore_gen", true).get();

            disableCheeseMoon = COMMON_BUILDER.comment("Disable Cheese Ore Gen on Moon.")
                    .translation("gc.configgui.disable_cheese_moon")
                    .define("disable_cheese_ore_gen_on_moon", false).get();

            disableTinMoon = COMMON_BUILDER.comment("Disable Tin Ore Gen on Moon.")
                    .translation("gc.configgui.disable_tin_moon")
                    .define("disable_tin_ore_gen_on_moon", false).get();

            disableCopperMoon = COMMON_BUILDER.comment("Disable Copper Ore Gen on Moon.")
                    .translation("gc.configgui.disable_copper_moon")
                    .define("disable_copper_ore_gen_on_moon", false).get();

            disableSapphireMoon = COMMON_BUILDER.comment("Disable Sapphire Ore Gen on Moon.")
                    .translation("gc.configgui.disable_sapphire_moon")
                    .define("disable_sapphire_ore_gen_on_moon", false).get();

            disableMoonVillageGen = COMMON_BUILDER.comment("If true, moon villages will not generate.")
                    .translation("gc.configgui.disable_moon_village_gen")
                    .define("disable_moon_village_gen", false).get();

            enableOtherModsFeatures = COMMON_BUILDER.comment("If this is enabled, other mods' standard ores and all other features (eg. plants) can generate on the Moon and planets. Apart from looking wrong, this make cause 'Already Decorating!' type crashes.  NOT RECOMMENDED!  See Wiki.")
                    .translation("gc.configgui.enable_other_mods_features")
                    .define("generate_all_other_mods_features_on_planets", false).get();

            whitelistCoFHCoreGen = COMMON_BUILDER.comment("If generate other mods features is disabled as recommended, this setting can whitelist CoFHCore custom worldgen on planets.")
                    .translation("gc.configgui.whitelist_co_f_h_core_gen")
                    .define("whitelist_cofhcore_worldgen_to_generate_its_ores_and_lakes_on_planets", false).get();

            enableThaumCraftNodes = COMMON_BUILDER.comment("If ThaumCraft is installed, ThaumCraft wild nodes can generate on the Moon and planets.")
                    .translation("gc.configgui.enable_thaum_craft_nodes")
                    .define("generate_thaumcraft_wild_nodes_on_planetary_surfaces", true).get();

            oregenIDs = COMMON_BUILDER.comment("Enter IDs of other mods' ores here for Galacticraft to generate them on the Moon and other planets. Format is BlockName or BlockName:metadata. Use optional parameters at end of each line: /RARE /UNCOMMON or /COMMON for rarity in a chunk; /DEEP /SHALLOW or /BOTH for height; /SINGLE /STANDARD or /LARGE for clump size; /XTRARANDOM for ores sometimes there sometimes not at all.  /ONLYMOON or /ONLYMARS if wanted on one planet only.  If nothing specified, defaults are /COMMON, /BOTH and /STANDARD.  Repeat lines to generate a huge quantity of ores.")
                    .translation("gc.configgui.other_mod_ore_gen_i_ds")
                    .define("Other_mods_ores_for_gc_to_generate_on_the_moon_and_planets", new String[] {}).get();

            disableBiomeTypeRegistrations = COMMON_BUILDER.comment("Biome Types will not be registered in the BiomeDictionary if this is set to true.")
                    .translation("gc.configgui.disable_biome_type_registrations")
                    .define("disable_biome_type_registrations", false).get();

            COMMON_BUILDER.pop();

            COMMON_BUILDER.push("DIMENSIONS");

//            idDimensionOverworld = COMMON_BUILDER.comment("Dimension ID for the Overworld (as seen in the Celestial Map)")
//                    .translation("gc.configgui.id_dimension_overworld")
//                    .defineInRange("iddimensionoverworld", 0, Integer.MIN_VALUE, Integer.MAX_VALUE).get();
//
//            idDimensionMoon = COMMON_BUILDER.comment("Dimension ID for the Moon")
//                    .translation("gc.configgui.id_dimension_moon")
//                    .defineInRange("iddimensionmoon", -28, Integer.MIN_VALUE, Integer.MAX_VALUE).get();
//
//            idDimensionOverworldOrbit = COMMON_BUILDER.comment("WorldProvider ID for Overworld Space Stations (advanced: do not change unless you have conflicts)")
//                    .translation("gc.configgui.id_dimension_overworld_orbit")
//                    .defineInRange("iddimensionoverworldorbit", -27, Integer.MIN_VALUE, Integer.MAX_VALUE).get();
//
//            idDimensionOverworldOrbitStatic = COMMON_BUILDER.comment("WorldProvider ID for Static Space Stations (advanced: do not change unless you have conflicts)")
//                    .translation("gc.configgui.id_dimension_overworld_orbit_static")
//                    .defineInRange("iddimensionoverworldorbitstatic", -26, Integer.MIN_VALUE, Integer.MAX_VALUE).get();

            biomeIDbase = COMMON_BUILDER.comment("Biome ID base. GC will use biome IDs from this to this + 3, or more with addons. Allowed 40-250. Default 102.")
                    .translation("gc.configgui.biome_id_base")
                    .defineInRange("biomeidbase", 102, Integer.MIN_VALUE, Integer.MAX_VALUE).get();

            staticLoadDimensions = COMMON_BUILDER.comment("IDs to load at startup, and keep loaded until server stops. Can be added via /gckeeploaded")
                    .translation("gc.configgui.static_loaded_dimensions")
                    .define("static_loaded_dimensions", ConfigManagerCore.staticLoadDimensions).get();

            keepLoadedNewSpaceStations = COMMON_BUILDER.comment("Set this to true to have an automatic /gckeeploaded for any new Space Station created.")
                    .translation("gc.configgui.static_loaded_new_ss")
                    .define("set_new_space_stations_to_be_static_loaded", true).get();

            disableRocketLaunchDimensions = COMMON_BUILDER.comment("IDs of dimensions where rockets should not launch - this should always include the Nether.")
                    .translation("gc.configgui.rocket_disabled_dimensions")
                    .define("dimensions_where_rockets_cannot_launch", disableRocketLaunchDimensions).get();

            disableRocketsToOverworld = COMMON_BUILDER.comment("If true, rockets will be unable to reach the Overworld (only use this in special modpacks!)")
                    .translation("gc.configgui.rocket_disable_overworld_return")
                    .define("disable_rockets_from_returning_to_overworld", false).get();

            COMMON_BUILDER.pop();

            COMMON_BUILDER.push("ACHIEVEMENTS");

            idAchievBase = COMMON_BUILDER.comment("Base Achievement ID. All achievement IDs will start at this number.")
                    .translation("gc.configgui.id_achiev_base")
                    .defineInRange("idachievbase", 1784, Integer.MIN_VALUE, Integer.MAX_VALUE).get();

            COMMON_BUILDER.pop();

            COMMON_BUILDER.push("SCHEMATIC");

            idSchematicRocketT1 = COMMON_BUILDER.comment("Schematic ID for Tier 1 Rocket, must be unique.")
                    .translation("gc.configgui.id_schematic_rocket_t1")
                    .defineInRange("idschematicrockett1", 0, Integer.MIN_VALUE, Integer.MAX_VALUE).get();

            idSchematicMoonBuggy = COMMON_BUILDER.comment("Schematic ID for Moon Buggy, must be unique.")
                    .translation("gc.configgui.id_schematic_moon_buggy")
                    .defineInRange("idschematicmoonbuggy", 1, Integer.MIN_VALUE, Integer.MAX_VALUE).get();

            idSchematicAddSchematic = COMMON_BUILDER.comment("Schematic ID for \"Add Schematic\" Page, must be unique")
                    .translation("gc.configgui.id_schematic_add_schematic")
                    .define("idschematicmoonbuggy", Integer.MAX_VALUE).get();

            COMMON_BUILDER.pop();

            COMMON_BUILDER.push("SERVER");

            otherPlanetWorldBorders = COMMON_BUILDER.comment("Set this to 0 for no borders (default).  If set to e.g. 2000, players will land on the Moon inside the x,z range -2000 to 2000.)")
                    .translation("gc.configgui.planet_worldborders")
                    .defineInRange("world_border_for_landing_location_on_other_planets_(moon,_mars,_etc)", 0, Integer.MIN_VALUE, Integer.MAX_VALUE).get();

            spaceStationsRequirePermission = COMMON_BUILDER.comment("While true, space stations require you to invite other players using /ssinvite <playername>")
                    .translation("gc.configgui.space_stations_require_permission")
                    .define("space_stations_require_permission", true).get();

            disableSpaceStationCreation = COMMON_BUILDER.comment("If set to true on a server, players will be completely unable to create space stations.")
                    .translation("gc.configgui.disable_space_station_creation")
                    .define("disable_space_station_creation", false).get();

            enableSealerEdgeChecks = COMMON_BUILDER.comment("If this is enabled, areas sealed by Oxygen Sealers will run a seal check when the player breaks or places a block (or on block updates).  This should be enabled for a 100% accurate sealed status, but can be disabled on servers for performance reasons.")
                    .translation("gc.configgui.enable_sealer_edge_checks")
                    .define("enable_sealed_edge_checks", true).get();

            COMMON_BUILDER.pop();

            COMMON_BUILDER.push("KEYS");

            keyOverrideMap = COMMON_BUILDER.comment("Default Map key on first Galacticraft run only. After first run, change keys by Minecraft in-game Controls menu.  Valid settings: KEY_ followed by 0-9 or A-Z.")
                    .translation("gc.configgui.override_map")
                    .define("open_galaxy_map", "KEY_M").get();

            keyOverrideFuelLevel = COMMON_BUILDER.comment("Default Rocket/Fuel key on first Galacticraft run only. After first run, change keys by Minecraft in-game Controls menu.  Valid settings: KEY_ followed by 0-9 or A-Z.")
                    .translation("gc.configgui.key_override_fuel_level")
                    .define("open_rocket_gui", "KEY_G").get();

            keyOverrideToggleAdvGoggles = COMMON_BUILDER.comment("Default Goggles key on first Galacticraft run only. After first run, change keys by Minecraft in-game Controls menu.  Valid settings: KEY_ followed by 0-9 or A-Z.")
                    .translation("gc.configgui.key_override_toggle_adv_goggles")
                    .define("toggle_advanced_goggles", "KEY_K").get();

            COMMON_BUILDER.pop();

            COMMON_BUILDER.push("COMPATIBILITY");

            useOldOilFluidID = COMMON_BUILDER.comment("Set to true to make Galacticraft oil register as oilgc, for backwards compatibility with previously generated worlds.")
                    .translation("gc.configgui.use_old_oil_fluid_i_d")
                    .define("use_legacy_oilgc_fluid_registration", false).get();

            useOldFuelFluidID = COMMON_BUILDER.comment("Set to true to make Galacticraft fuel register as fuelgc, for backwards compatibility with previously generated worlds.")
                    .translation("gc.configgui.use_old_fuel_fluid_i_d")
                    .define("use_legacy_fuelgc_fluid_registration", false).get();

            sealableIDs = COMMON_BUILDER.comment("List non-opaque blocks from other mods (for example, special types of glass) that the Oxygen Sealer should recognize as solid seals. Format is BlockName or BlockName:metadata")
                    .translation("gc.configgui.sealable_i_ds")
                    .define("external_sealable_ids", new String[] { Blocks.GLASS_PANE.getRegistryName().toString() }).get();

            detectableIDs = COMMON_BUILDER.comment("List blocks from other mods that the Sensor Glasses should recognize as solid blocks. Format is BlockName or BlockName:metadata.")
                    .translation("gc.configgui.detectable_i_ds")
                    .define("external_detectable_ids", new String[] {
                            Blocks.COAL_ORE.getRegistryName().getPath(),
                            Blocks.DIAMOND_ORE.getRegistryName().getPath(),
                            Blocks.GOLD_ORE.getRegistryName().getPath(),
                            Blocks.IRON_ORE.getRegistryName().getPath(),
                            Blocks.LAPIS_ORE.getRegistryName().getPath(),
                            Blocks.REDSTONE_ORE.getRegistryName().getPath() }).get();

            alternateCanisterRecipe = COMMON_BUILDER.comment("Enable this if the standard canister recipe causes a conflict.")
                    .translation("gc.configgui.alternate_canister_recipe")
                    .define("alternate_recipe_for_canisters", false).get();

            otherModsSilicon = COMMON_BUILDER.comment("This needs to match the OreDictionary name used in the other mod. Set a nonsense name to disable.")
                    .translation("gc.configgui.ore_dict_silicon")
                    .define("oredict_name_of_other_mod's_silicon", "itemSilicon").get();

            recipesRequireGCAdvancedMetals = COMMON_BUILDER.comment("Should normally be true. If you set this to false, in a modpack with other mods with the same metals, players may be able to craft advanced GC items without travelling to Moon, Mars, Asteroids etc.")
                    .translation("gc.configgui.disable_ore_dict_space_metals")
                    .define("must_use_gc's_own_space_metals_in_recipes", true).get();

            rocketFuelFactor = COMMON_BUILDER.comment("The normal factor is 1.  Increase this to 2 - 5 if other mods with a lot of oil (e.g. BuildCraft) are installed to increase GC rocket fuel requirement.")
                    .translation("gc.configgui.rocket_fuel_factor")
                    .defineInRange("rocket_fuel_factor", 1, Integer.MIN_VALUE, Integer.MAX_VALUE).get();

            COMMON_BUILDER.pop();

            COMMON_BUILDER.push("ENERGY_COMPATIBILITY");

            EnergyConfigHandler.IC2_RATIO = COMMON_BUILDER.comment("IndustrialCraft2 Conversion Ratio").defineInRange("ic2_conv_ratio", EnergyConfigHandler.IC2_RATIO, 0.01F, 1000F).get().floatValue();
            EnergyConfigHandler.RF_RATIO = COMMON_BUILDER.comment("Redstone Flux Conversion Ratio").defineInRange("rf_conv_ratio", EnergyConfigHandler.RF_RATIO, 0.001F, 100.0F).get().floatValue();
            EnergyConfigHandler.BC_RATIO = COMMON_BUILDER.comment("Buildcraft Conversion Ratio").defineInRange("bc_conv_ratio", EnergyConfigHandler.BC_RATIO, 0.01F, 1000.0F).get().floatValue();
            EnergyConfigHandler.MEKANISM_RATIO = COMMON_BUILDER.comment("Mekanism Conversion Ratio").defineInRange("mek_conv_ratio", EnergyConfigHandler.MEKANISM_RATIO, 0.001F, 100.0F).get().floatValue();
            EnergyConfigHandler.conversionLossFactor = COMMON_BUILDER.comment("Loss factor when converting energy as a percentage (100 = no loss, 90 = 10% loss ...)").defineInRange("conv_loss_factor", 100, 5, 100).get();

            EnergyConfigHandler.updateRatios();

            EnergyConfigHandler.displayEnergyUnitsBC = COMMON_BUILDER.comment("If BuildCraft is loaded, show Galacticraft machines energy as MJ instead of gJ?").define("display_energy_bc", false).get();
            EnergyConfigHandler.displayEnergyUnitsIC2 = COMMON_BUILDER.comment("If IndustrialCraft2 is loaded, show Galacticraft machines energy as EU instead of gJ?").define("display_energy_ic2", false).get();
            EnergyConfigHandler.displayEnergyUnitsMek = COMMON_BUILDER.comment("If Mekanism is loaded, show Galacticraft machines energy as Joules (J) instead of gJ?").define("display_energy_mek", false).get();
            EnergyConfigHandler.displayEnergyUnitsRF = COMMON_BUILDER.comment("Show Galacticraft machines energy in RF instead of gJ?").define("display_energy_rf", false).get();

            EnergyConfigHandler.disableMJinterface = COMMON_BUILDER.comment("Disable old Buildcraft API (MJ) interfacing completely?").define("disable_mj_interface", false).get();

            EnergyConfigHandler.disableBuildCraftInput = COMMON_BUILDER.comment("Disable INPUT of BuildCraft energy").define("disable_input_bc", false).get();
            EnergyConfigHandler.disableBuildCraftOutput = COMMON_BUILDER.comment("Disable OUTPUT of BuildCraft energy").define("disable_output_bc", false).get();
            EnergyConfigHandler.disableRFInput = COMMON_BUILDER.comment("Disable INPUT of RF energy").define("disable_input_rf", false).get();
            EnergyConfigHandler.disableRFOutput = COMMON_BUILDER.comment("Disable OUTPUT of RF energy").define("disable_output_rf", false).get();
            EnergyConfigHandler.disableFEInput = COMMON_BUILDER.comment("Disable INPUT of Forge Energy to GC machines").define("disable_input_forge", false).get();
            EnergyConfigHandler.disableFEOutput = COMMON_BUILDER.comment("Disable OUTPUT of Forge Energy from GC machines").define("disable_output_forge", false).get();
            EnergyConfigHandler.disableIC2Input = COMMON_BUILDER.comment("Disable INPUT of IC2 energy").define("disable_input_ic2", false).get();
            EnergyConfigHandler.disableIC2Output = COMMON_BUILDER.comment("Disable OUTPUT of IC2 energy").define("disable_output_ic2", false).get();
            EnergyConfigHandler.disableMekanismInput = COMMON_BUILDER.comment("Disable INPUT of Mekanism energy").define("disable_input_mek", false).get();
            EnergyConfigHandler.disableMekanismOutput = COMMON_BUILDER.comment("Disable OUTPUT of Mekanism energy").define("disable_output_mek", false).get();

            if (!EnergyConfigHandler.isIndustrialCraft2Loaded())
            {
                EnergyConfigHandler.displayEnergyUnitsIC2 = false;
            }
            if (!EnergyConfigHandler.isMekanismLoaded())
            {
                EnergyConfigHandler.displayEnergyUnitsMek = false;
            }
            if (EnergyConfigHandler.displayEnergyUnitsIC2)
            {
                EnergyConfigHandler.displayEnergyUnitsBC = false;
            }
            if (EnergyConfigHandler.displayEnergyUnitsMek)
            {
                EnergyConfigHandler.displayEnergyUnitsBC = false;
                EnergyConfigHandler.displayEnergyUnitsIC2 = false;
            }
            if (EnergyConfigHandler.displayEnergyUnitsRF)
            {
                EnergyConfigHandler.displayEnergyUnitsBC = false;
                EnergyConfigHandler.displayEnergyUnitsIC2 = false;
                EnergyConfigHandler.displayEnergyUnitsMek = false;
            }

            COMMON_BUILDER.pop();

            COMMON_BUILDER.push("GENERAL");

            enableDebug = COMMON_BUILDER.comment("If this is enabled, debug messages will appear in the console. This is useful for finding bugs in the mod.")
                    .translation("gc.configgui.enable_debug")
                    .define("enable_debug_messages", false).get();

            forceOverworldRespawn = COMMON_BUILDER.comment("By default, you will respawn on Galacticraft dimensions if you die. If you are dying over and over on a planet, set this to true, and you will respawn back on the Overworld.")
                    .translation("gc.configgui.force_overworld_respawn")
                    .define("force_overworld_spawn", false).get();

            disableLander = COMMON_BUILDER.comment("If this is true, the player will parachute onto the Moon instead - use only in debug situations.")
                    .translation("gc.configgui.disable_lander")
                    .define("disable_lander_on_moon_and_other_planets", false).get();

//            mapfactor = COMMON_BUILDER.comment("null") .translation("gc.configgui.mapFactor").defineInRange("map_factor", 1, Integer.MIN_VALUE, Integer.MAX_VALUE).get();

//            mapsize = COMMON_BUILDER.comment("null").translation("gc.configgui.mapSize").defineInRange("map_size", 400, Integer.MIN_VALUE, Integer.MAX_VALUE).get();

            disableUpdateCheck = COMMON_BUILDER.comment("Update check will not run if this is set to true.")
                    .translation("gc.configgui.disable_update_check")
                    .define("disable_update_check", false).get();

            allowLiquidGratings = COMMON_BUILDER.comment("Liquids will not flow into Grating block if this is set to false.")
                    .translation("gc.configgui.allow_liquids_grating")
                    .define("allow_liquids_into_gratings", true).get();

            enableSpaceRaceManagerPopup = COMMON_BUILDER.comment("Space Race Manager will show on-screen after login, if enabled.")
                    .translation("gc.configgui.enable_space_race_manager_popup")
                    .define("enable_space_race_manager_popup", false).get();

            COMMON_BUILDER.pop();

            COMMON_BUILDER.push("CONTROLS");

            mapMouseScrollSensitivity = COMMON_BUILDER.comment("Increase to make the mouse drag scroll more sensitive, decrease to lower sensitivity.")
                    .translation("gc.configgui.map_scroll_sensitivity")
                    .defineInRange("map_scroll_mouse_sensitivity", 1.0, 0.0, Float.MAX_VALUE).get().floatValue();

            invertMapMouseScroll = COMMON_BUILDER.comment("Set to true to invert the mouse scroll feature on the galaxy map.")
                    .translation("gc.configgui.map_scroll_invert")
                    .define("map_scroll_mouse_invert", false).get();

            COMMON_BUILDER.pop();

            COMMON_BUILDER.push("CLIENT");

            moreStars = COMMON_BUILDER.comment("Setting this to false will revert night skies back to default minecraft star count")
                    .translation("gc.configgui.more_stars")
                    .define("more_stars", true).get();

            disableSpaceshipParticles = COMMON_BUILDER.comment("If you have FPS problems, setting this to true will help if rocket particles are in your sights")
                    .translation("gc.configgui.disable_spaceship_particles")
                    .define("disable_spaceship_particles", false).get();

            disableVehicleCameraChanges = COMMON_BUILDER.comment("If you're using this mod in virtual reality, or if you don't want the camera changes when entering a Galacticraft vehicle, set this to true.")
                    .translation("gc.configgui.disable_vehicle_camera_changes")
                    .define("disable_vehicle_third-person_and_zoom", false).get();

            oxygenIndicatorLeft = COMMON_BUILDER.comment("If true, this will move the Oxygen Indicator to the left LogicalSide. You can combine this with \"Minimap Bottom\"")
                    .translation("gc.configgui.oxygen_indicator_left")
                    .define("minimap_left", false).get();

            oxygenIndicatorBottom = COMMON_BUILDER.comment("If true, this will move the Oxygen Indicator to the bottom. You can combine this with \"Minimap Left\"")
                    .translation("gc.configgui.oxygen_indicator_bottom")
                    .define("minimap_bottom", false).get();

            overrideCapes = COMMON_BUILDER.comment("By default, Galacticraft will override capes with the mod's donor cape. Set to false to disable.")
                    .translation("gc.configgui.override_capes")
                    .define("override_capes", true).get();

            COMMON_BUILDER.pop();

            COMMON_BUILDER.push("DIFFICULTY");

            disableSpaceshipGrief = COMMON_BUILDER.comment("Spaceships will not explode on contact if set to true.")
                    .translation("gc.configgui.disable_spaceship_grief")
                    .define("disable_spaceship_explosion", false).get();

            spaceStationEnergyScalar = COMMON_BUILDER.comment("Solar panels will work (default 2x) more effective on space stations.")
                    .translation("gc.configgui.space_station_energy_scalar")
                    .defineInRange("space_station_solar_energy_multiplier", 2.0, -Double.MAX_VALUE, Double.MAX_VALUE).get();

            quickMode = COMMON_BUILDER.comment("Set this to true for less metal use in Galacticraft recipes (makes the game easier).")
                    .translation("gc.configgui.quick_mode")
                    .define("quick_game_mode", false).get();

            hardMode = COMMON_BUILDER.comment("Set this to true for increased difficulty in modpacks (see forum for more info).")
                    .translation("gc.configgui.hard_mode")
                    .define("harder_difficulty", false).get();

            challengeMode = COMMON_BUILDER.comment("Set this to true for a challenging adventure where the player starts the game stranded in the Asteroids dimension with low resources (only effective if Galacticraft Planets installed).")
                    .translation("gc.configgui.asteroids_start")
                    .define("adventure_game_mode", false).get();

            challengeFlags = COMMON_BUILDER.comment("Add together flags 8, 4, 2, 1 to enable the four elements of adventure game mode. Default 15.  1 = extended compressor recipes.  2 = mob drops and spawning.  4 = more trees in hollow asteroids.  8 = start stranded in Asteroids.")
                    .translation("gc.configgui.asteroids_flags")
                    .defineInRange("adventure_game_mode_flags", 15, Integer.MIN_VALUE, Integer.MAX_VALUE).get();

            suffocationCooldown = COMMON_BUILDER.comment("Lower/Raise this value to change time between suffocation damage ticks (allowed range 50-250)")
                    .translation("gc.configgui.suffocation_cooldown")
                    .defineInRange("suffocation_cooldown", 100, 50, 250).get();

            suffocationDamage = COMMON_BUILDER.comment("Change this value to modify the damage taken per suffocation tick")
                    .translation("gc.configgui.suffocation_damage")
                    .defineInRange("suffocation_damage", 2, Integer.MIN_VALUE, Integer.MAX_VALUE).get();

            dungeonBossHealthMod = COMMON_BUILDER.comment("Change this if you wish to balance the mod (if you have more powerful weapon mods).")
                    .translation("gc.configgui.dungeon_boss_health_mod")
                    .defineInRange("dungeon_boss_health_modifier", 1.0, -Double.MAX_VALUE, Double.MAX_VALUE).get();

            meteorSpawnMod = COMMON_BUILDER.comment("Set to a value between 0.0 and 1.0 to decrease meteor spawn chance (all dimensions).")
                    .translation("gc.configgui.meteor_spawn_mod")
                    .defineInRange("meteor_spawn_modifier", 1.0, -Double.MAX_VALUE, Double.MAX_VALUE).get();

            meteorBlockDamageEnabled = COMMON_BUILDER.comment("Set to false to stop meteors from breaking blocks on contact.")
                    .translation("gc.configgui.meteor_block_damage")
                    .define("meteor_block_damage_enabled", true).get();

            COMMON_BUILDER.pop();
            
            challengeModeUpdate();
        }
        catch (final Exception e)
        {
            GCLog.severe("Problem loading core config (\"core.conf\")");
            e.printStackTrace();
        }
    }
    
    public boolean setLoaded(DimensionType newID)
    {
        boolean found = false;

        for (String staticLoadDimension : ConfigManagerCore.staticLoadDimensions)
        {
            if (staticLoadDimension.equals(newID.getRegistryName().toString()))
            {
                found = true;
                break;
            }
        }

        if (!found)
        {
            String[] oldIDs = ConfigManagerCore.staticLoadDimensions;
            ConfigManagerCore.staticLoadDimensions = new String[ConfigManagerCore.staticLoadDimensions.length + 1];
            System.arraycopy(oldIDs, 0, staticLoadDimensions, 0, oldIDs.length);

            ConfigManagerCore.staticLoadDimensions[ConfigManagerCore.staticLoadDimensions.length - 1] = newID.getRegistryName().toString();
            Arrays.sort(ConfigManagerCore.staticLoadDimensions);

            COMMON_BUILDER.comment("IDs to load at startup, and keep loaded until server stops. Can be added via /gckeeploaded")
                    .translation("gc.configgui.static_loaded_dimensions")
                    .define("static_loaded_dimensions", ConfigManagerCore.staticLoadDimensions).set(ConfigManagerCore.staticLoadDimensions);
        }

        return !found;
    }

    public boolean setUnloaded(DimensionType idToRemove)
    {
        int foundCount = 0;

        for (String staticLoadDimension : ConfigManagerCore.staticLoadDimensions)
        {
            if (staticLoadDimension.equals(idToRemove.getRegistryName().toString()))
            {
                foundCount++;
            }
        }

        if (foundCount > 0)
        {
            List<String> idArray = Arrays.asList(ConfigManagerCore.staticLoadDimensions);
            idArray.remove(idToRemove);

            ConfigManagerCore.staticLoadDimensions = new String[idArray.size()];

            for (int i = 0; i < idArray.size(); i++)
            {
                ConfigManagerCore.staticLoadDimensions[i] = idArray.get(i);
            }

            Arrays.sort(ConfigManagerCore.staticLoadDimensions);

            COMMON_BUILDER.comment("IDs to load at startup, and keep loaded until server stops. Can be added via /gckeeploaded")
                    .translation("gc.configgui.static_loaded_dimensions")
                    .define("static_loaded_dimensions", ConfigManagerCore.staticLoadDimensions).set(ConfigManagerCore.staticLoadDimensions);
        }

        return foundCount > 0;
    }

    public static void challengeModeUpdate()
    {
    	if (challengeMode)
    	{
    		challengeRecipes = (challengeFlags & 1) > 0;
    		challengeMobDropsAndSpawning = (challengeFlags & 2) > 0;
            challengeAsteroidPopulation = (challengeFlags & 4) > 0;
    		challengeSpawnHandling = (challengeFlags & 8) > 0;
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
//    	int modeFlags = ConfigManagerCore.hardMode ? 1 : 0;
//    	modeFlags += ConfigManagerCore.quickMode ? 2 : 0;
//    	modeFlags += ConfigManagerCore.challengeMode ? 4 : 0;
//    	modeFlags += ConfigManagerCore.disableSpaceStationCreation ? 8 : 0;
//    	modeFlags += ConfigManagerCore.recipesRequireGCAdvancedMetals ? 16 : 0;
//    	modeFlags += ConfigManagerCore.challengeRecipes ? 32 : 0;
//        modeFlags += ConfigManagerCore.allowLiquidGratings ? 64 : 0;
//    	returnList.add(modeFlags);
//    	returnList.add(ConfigManagerCore.dungeonBossHealthMod);
//    	returnList.add(ConfigManagerCore.suffocationDamage);
//    	returnList.add(ConfigManagerCore.suffocationCooldown);
//    	returnList.add(ConfigManagerCore.rocketFuelFactor);
//    	returnList.add(ConfigManagerCore.otherModsSilicon);
//    	//If changing this, update definition of EnumSimplePacket.C_UPDATE_CONFIGS - see comment in setConfigOverride() below
//    	EnergyConfigHandler.serverConfigOverride(returnList);
//
//    	returnList.add(ConfigManagerCore.detectableIDs.clone());
//    	//TODO Should this include any other client-LogicalSide configurables too?
//    	return returnList;
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    public static void setConfigOverride(List<Object> configs)
//    {
//        int dataCount = 0;
//    	int modeFlag = (Integer) configs.get(dataCount++);
//    	ConfigManagerCore.hardMode = (modeFlag & 1) != 0;
//    	ConfigManagerCore.quickMode = (modeFlag & 2) != 0;
//    	ConfigManagerCore.challengeMode = (modeFlag & 4) != 0;
//    	ConfigManagerCore.disableSpaceStationCreation = (modeFlag & 8) != 0;
//    	ConfigManagerCore.recipesRequireGCAdvancedMetals = (modeFlag & 16) != 0;
//    	ConfigManagerCore.challengeRecipes = (modeFlag & 32) != 0;
//        ConfigManagerCore.allowLiquidGratings = (modeFlag & 64) != 0;
//    	ConfigManagerCore.dungeonBossHealthMod = (Double) configs.get(dataCount++);
//    	ConfigManagerCore.suffocationDamage = (Integer) configs.get(dataCount++);
//    	ConfigManagerCore.suffocationCooldown = (Integer) configs.get(dataCount++);
//    	ConfigManagerCore.rocketFuelFactor = (Integer) configs.get(dataCount++);
//    	ConfigManagerCore.otherModsSilicon = (String) configs.get(dataCount++);
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
//    			ConfigManagerCore.detectableIDs = new String[sizeIDs];
//		    	for (int j = 0; j < sizeIDs; j++)
//		    	ConfigManagerCore.detectableIDs[j] = new String((String) configs.get(dataCount++));
//    		}
//    		else if (dataLast instanceof String[])
//    		{
//    			ConfigManagerCore.detectableIDs = ((String[])dataLast);
//    		}
//        	TickHandlerClient.registerDetectableBlocks(false);
//    	}
//
//    	challengeModeUpdate();
//    	RecipeManagerGC.setConfigurableRecipes();
//    }

//    public static void saveClientConfigOverrideable()
//    {
//        if (ConfigManagerCore.clientSave == null)
//        {
//            ConfigManagerCore.clientSave = (ArrayList<Object>) ConfigManagerCore.getServerConfigOverride();
//        }
//    }
//
//    public static void restoreClientConfigOverrideable()
//    {
//        if (ConfigManagerCore.clientSave != null)
//        {
//            ConfigManagerCore.setConfigOverride(clientSave);
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
