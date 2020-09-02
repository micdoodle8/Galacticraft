package micdoodle8.mods.galacticraft.core.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static net.minecraftforge.fml.loading.LogMarkers.FORGEMOD;

public class ConfigManagerCore
{
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final ConfigManagerCore INSTANCE;

    static {
        final Pair<ConfigManagerCore, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ConfigManagerCore::new);
        COMMON_SPEC = specPair.getRight();
        INSTANCE = specPair.getLeft();
    }
//    static Configuration config;

    // GAME CONTROL
    public final BooleanValue forceOverworldRespawn;
    public final BooleanValue hardMode;
    public final BooleanValue quickMode;
    public final BooleanValue challengeMode;
    public final IntValue challengeFlags;
    public boolean challengeRecipes;
    public boolean challengeMobDropsAndSpawning;
    public boolean challengeSpawnHandling;
    public boolean challengeAsteroidPopulation;
    public final BooleanValue disableRocketsToOverworld;
    public final BooleanValue disableSpaceStationCreation;
    public final BooleanValue spaceStationsRequirePermission;
    public final BooleanValue disableUpdateCheck;
    public final BooleanValue enableSpaceRaceManagerPopup;
    public final BooleanValue enableDebug;
    public final BooleanValue enableSealerEdgeChecks;
    public final BooleanValue disableLander;
    public final BooleanValue recipesRequireGCAdvancedMetals;
    public final BooleanValue allowLiquidGratings;
//    public final IntValue mapfactor;
//    public final IntValue mapsize;

    // DIMENSIONS
    public final IntValue idDimensionOverworld;
    public final IntValue idDimensionOverworldOrbit;
    public final IntValue idDimensionOverworldOrbitStatic;
    public final IntValue idDimensionMoon;
    public final IntValue biomeIDbase;
    public final BooleanValue disableBiomeTypeRegistrations;
    public final ConfigValue<List<? extends String>> staticLoadDimensions;
    public final ConfigValue<List<? extends String>> disableRocketLaunchDimensions;
    public boolean disableRocketLaunchAllNonGC = false;
    public final IntValue otherPlanetWorldBorders;
    public final BooleanValue keepLoadedNewSpaceStations;

    // SCHEMATICS
    public final IntValue idSchematicRocketT1;
    public final IntValue idSchematicMoonBuggy;
    public final IntValue idSchematicAddSchematic;

    // ACHIEVEMENTS
    public final IntValue idAchievBase;

    // CLIENT / VISUAL FX
    public final BooleanValue moreStars;
    public final BooleanValue disableSpaceshipParticles;
    public final BooleanValue disableVehicleCameraChanges;
    public final BooleanValue oxygenIndicatorLeft;
    public final BooleanValue oxygenIndicatorBottom;
    public final BooleanValue overrideCapes;

    //DIFFICULTY
    public final DoubleValue dungeonBossHealthMod;
    public final IntValue suffocationCooldown;
    public final IntValue suffocationDamage;
    public final IntValue rocketFuelFactor;
    public final DoubleValue meteorSpawnMod;
    public final BooleanValue meteorBlockDamageEnabled;
    public final BooleanValue disableSpaceshipGrief;
    public final DoubleValue spaceStationEnergyScalar;

    // WORLDGEN
    public final BooleanValue enableCopperOreGen;
    public final BooleanValue enableTinOreGen;
    public final BooleanValue enableAluminumOreGen;
    public final BooleanValue enableSiliconOreGen;
    public final BooleanValue disableCheeseMoon;
    public final BooleanValue disableTinMoon;
    public final BooleanValue disableCopperMoon;
    public final BooleanValue disableMoonVillageGen;
    public final BooleanValue disableSapphireMoon;
    public final ConfigValue<List<? extends Integer>> externalOilGen;
    public final DoubleValue oilGenFactor;
    public final BooleanValue retrogenOil;
    public final ConfigValue<List<? extends String>> oregenIDs;
    public final BooleanValue enableOtherModsFeatures;
    public final BooleanValue whitelistCoFHCoreGen;
    public final BooleanValue enableThaumCraftNodes;

    //COMPATIBILITY
    public final ConfigValue<List<? extends String>> sealableIDs;
    public final ConfigValue<List<? extends String>> detectableIDs;
    public final BooleanValue alternateCanisterRecipe;
    public final ConfigValue<String> otherModsSilicon;
    public final BooleanValue useOldOilFluidID;
    public final BooleanValue useOldFuelFluidID;

    //ENERGY
    public final DoubleValue ic2ConversionRate;
    public final DoubleValue bcConversionRate;
    public final DoubleValue rfConversionRate;
    public final DoubleValue mekConversionRate;
    public final DoubleValue lossFactor;
    public final BooleanValue disableBuildCraftInput;
    public final BooleanValue disableBuildCraftOutput;
    public final BooleanValue disableRFInput;
    public final BooleanValue disableRFOutput;
    public final BooleanValue disableFEOutput;
    public final BooleanValue disableFEInput;
    public final BooleanValue disableIC2Output;
    public final BooleanValue disableIC2Input;
    public final BooleanValue disableMekanismInput;
    public final BooleanValue disableMekanismOutput;
    public final BooleanValue displayEnergyUnitsBC;
    public final BooleanValue displayEnergyUnitsIC2;
    public final BooleanValue displayEnergyUnitsMek;
    public final BooleanValue displayEnergyUnitsRF;
    public final BooleanValue disableMJinterface;

    //KEYBOARD AND MOUSE
    public final ConfigValue<String> keyOverrideMap;
    public final ConfigValue<String> keyOverrideFuelLevel;
    public final ConfigValue<String> keyOverrideToggleAdvGoggles;
    public static int keyOverrideMapI;
    public static int keyOverrideFuelLevelI;
    public static int keyOverrideToggleAdvGogglesI;
    public final DoubleValue mapMouseScrollSensitivity;
    public final BooleanValue invertMapMouseScroll;

    public static ArrayList<Object> clientSave = null;
    //    public static Map<String, List<String>> propOrder = new TreeMap<>();
    public static String currentCat;

//    public static void initialize(File file)
//    {
//        ConfigManagerCore.INSTANCE.config = new Configuration(file);
//        ConfigManagerCore.INSTANCE.syncConfig(true);
//    }

//    public static void forceSave()
//    {
//        ConfigManagerCore.INSTANCE.config.save();
//    }

    //    public static void syncConfig(boolean load)
    public ConfigManagerCore(final ForgeConfigSpec.Builder builder)
    {
//        try
//        {
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

//            enableDebug = builder.comment("If this is enabled, debug messages will appear in the console. This is useful for finding bugs in the mod.")
//                    .translation("gc.configgui.enable_debug")
//                    .define("enableDebug", false).get();
//
////            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Enable Debug Messages", false);
////            builder.comment("If this is enabled, debug messages will appear in the console. This is useful for finding bugs in the mod.");
////            prop.setLanguageKey("gc.configgui.enable_debug");
////            enableDebug = prop.getBoolean(false);
////            finishProp(prop);
//
//            builder.push("Dimensions");
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_SERVER, "World border for landing location on other planets (Moon, Mars, etc)", 0);
//            builder.comment("Set this to 0 for no borders (default).  If set to e.g. 2000, players will land on the Moon inside the x,z range -2000 to 2000.)");
//                    .translation("gc.configgui.planet_worldborders");
//            otherPlanetWorldBorders = prop.getInt(0);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Force Overworld Spawn", false);
//            builder.comment("By default, you will respawn on Galacticraft dimensions if you die. If you are dying over and over on a planet, set this to true, and you will respawn back on the Overworld.");
//                    .translation("gc.configgui.force_overworld_respawn");
//                    .define("", false);
//
//            //
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicRocketT1", 0);
//            builder.comment("Schematic ID for Tier 1 Rocket, must be unique.");
//                    .translation("gc.configgui.id_schematic_rocket_t1");
//            idSchematicRocketT1 = prop.getInt(0);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicMoonBuggy", 1);
//            builder.comment("Schematic ID for Moon Buggy, must be unique.");
//                    .translation("gc.configgui.id_schematic_moon_buggy");
//            idSchematicMoonBuggy = prop.getInt(1);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicAddSchematic", Integer.MAX_VALUE);
//            builder.comment("Schematic ID for \"Add Schematic\" Page, must be unique");
//                    .translation("gc.configgui.id_schematic_add_schematic");
//            idSchematicAddSchematic = prop.getInt(Integer.MAX_VALUE);
//
//            //
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_ACHIEVEMENTS, "idAchievBase", 1784);
//            builder.comment("Base Achievement ID. All achievement IDs will start at this number.");
//                    .translation("gc.configgui.id_achiev_base");
//            idAchievBase = prop.getInt(1784);
//
////Client LogicalSide
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_CLIENT, "More Stars", true);
//            builder.comment("Setting this to false will revert night skies back to default minecraft star count");
//                    .translation("gc.configgui.more_stars");
//                    .define("", true);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_CLIENT, "Disable Spaceship Particles", false);
//            builder.comment("If you have FPS problems, setting this to true will help if rocket particles are in your sights");
//                    .translation("gc.configgui.disable_spaceship_particles");
//                    .define("", false);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_CLIENT, "Disable Vehicle Third-Person and Zoom", false);
//            builder.comment("If you're using this mod in virtual reality, or if you don't want the camera changes when entering a Galacticraft vehicle, set this to true.");
//                    .translation("gc.configgui.disable_vehicle_camera_changes");
//                    .define("", false);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_CLIENT, "Minimap Left", false);
//            builder.comment("If true, this will move the Oxygen Indicator to the left LogicalSide. You can combine this with \"Minimap Bottom\"");
//                    .translation("gc.configgui.oxygen_indicator_left");
//                    .define("", false);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_CLIENT, "Minimap Bottom", false);
//            builder.comment("If true, this will move the Oxygen Indicator to the bottom. You can combine this with \"Minimap Left\"");
//                    .translation("gc.configgui.oxygen_indicator_bottom");
//                    .define("", false);
//
////World gen
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Oil Generation Factor", 1.8);
//            builder.comment("Increasing this will increase amount of oil that will generate in each chunk.");
//                    .translation("gc.configgui.oil_gen_factor");
//            oilGenFactor = prop.getDouble(1.8);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Oil gen in external dimensions", new int[] { 0 });
//            builder.comment("List of non-galacticraft dimension IDs to generate oil in.");
//                    .translation("gc.configgui.external_oil_gen");
//            externalOilGen = prop.getIntList();
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Retro Gen of GC Oil in existing map chunks", false);
//            builder.comment("If this is enabled, GC oil will be added to existing Overworld maps where possible.");
//                    .translation("gc.configgui.enable_retrogen_oil");
//                    .define("", false);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Enable Copper Ore Gen", true);
//            builder.comment("If this is enabled, copper ore will generate on the overworld.");
//                    .translation("gc.configgui.enable_copper_ore_gen").worldRestart();
//                    .define("", true);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Enable Tin Ore Gen", true);
//            builder.comment("If this is enabled, tin ore will generate on the overworld.");
//                    .translation("gc.configgui.enable_tin_ore_gen").worldRestart();
//                    .define("", true);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Enable Aluminum Ore Gen", true);
//            builder.comment("If this is enabled, aluminum ore will generate on the overworld.");
//                    .translation("gc.configgui.enable_aluminum_ore_gen").worldRestart();
//                    .define("", true);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Enable Silicon Ore Gen", true);
//            builder.comment("If this is enabled, silicon ore will generate on the overworld.");
//                    .translation("gc.configgui.enable_silicon_ore_gen").worldRestart();
//                    .define("", true);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Cheese Ore Gen on Moon", false);
//            builder.comment("Disable Cheese Ore Gen on Moon.");
//                    .translation("gc.configgui.disable_cheese_moon");
//                    .define("", false);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Tin Ore Gen on Moon", false);
//            builder.comment("Disable Tin Ore Gen on Moon.");
//                    .translation("gc.configgui.disable_tin_moon");
//                    .define("", false);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Copper Ore Gen on Moon", false);
//            builder.comment("Disable Copper Ore Gen on Moon.");
//                    .translation("gc.configgui.disable_copper_moon");
//                    .define("", false);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Sapphire Ore Gen on Moon", false);
//            builder.comment("Disable Sapphire Ore Gen on Moon.");
//                    .translation("gc.configgui.disable_sapphire_moon");
//                    .define("", false);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Moon Village Gen", false);
//            builder.comment("If true, moon villages will not generate.");
//                    .translation("gc.configgui.disable_moon_village_gen");
//                    .define("", false);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Generate all other mods features on planets", false);
//            builder.comment("If this is enabled, other mods' standard ores and all other features (eg. plants) can generate on the Moon and planets. Apart from looking wrong, this make cause 'Already Decorating!' type crashes.  NOT RECOMMENDED!  See Wiki.");
//                    .translation("gc.configgui.enable_other_mods_features");
//                    .define("", false);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Whitelist CoFHCore worldgen to generate its ores and lakes on planets", false);
//            builder.comment("If generate other mods features is disabled as recommended, this setting can whitelist CoFHCore custom worldgen on planets.");
//                    .translation("gc.configgui.whitelist_co_f_h_core_gen");
//                    .define("", false);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Generate ThaumCraft wild nodes on planetary surfaces", true);
//            builder.comment("If ThaumCraft is installed, ThaumCraft wild nodes can generate on the Moon and planets.");
//                    .translation("gc.configgui.enable_thaum_craft_nodes");
//                    .define("", true);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Other mods ores for GC to generate on the Moon and planets", new String[] {});
//            builder.comment("Enter IDs of other mods' ores here for Galacticraft to generate them on the Moon and other planets. Format is BlockName or BlockName:metadata. Use optional parameters at end of each line: /RARE /UNCOMMON or /COMMON for rarity in a chunk; /DEEP /SHALLOW or /BOTH for height; /SINGLE /STANDARD or /LARGE for clump size; /XTRARANDOM for ores sometimes there sometimes not at all.  /ONLYMOON or /ONLYMARS if wanted on one planet only.  If nothing specified, defaults are /COMMON, /BOTH and /STANDARD.  Repeat lines to generate a huge quantity of ores.");
//                    .translation("gc.configgui.other_mod_ore_gen_i_ds");
//            oregenIDs = prop.getStringList();
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_COMPATIBILITY, "Use legacy oilgc fluid registration", false);
//            builder.comment("Set to true to make Galacticraft oil register as oilgc, for backwards compatibility with previously generated worlds.");
//                    .translation("gc.configgui.use_old_oil_fluid_i_d");
//                    .define("", false);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_COMPATIBILITY, "Use legacy fuelgc fluid registration", false);
//            builder.comment("Set to true to make Galacticraft fuel register as fuelgc, for backwards compatibility with previously generated worlds.");
//                    .translation("gc.configgui.use_old_fuel_fluid_i_d");
//                    .define("", false);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Disable lander on Moon and other planets", false);
//            builder.comment("If this is true, the player will parachute onto the Moon instead - use only in debug situations.");
//                    .translation("gc.configgui.disable_lander");
//                    .define("", false);
//
////Server LogicalSide
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Disable Spaceship Explosion", false);
//            builder.comment("Spaceships will not explode on contact if set to true.");
//                    .translation("gc.configgui.disable_spaceship_grief");
//                    .define("", false);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_SERVER, "Space Stations Require Permission", true);
//            builder.comment("While true, space stations require you to invite other players using /ssinvite <playername>");
//                    .translation("gc.configgui.space_stations_require_permission");
//                    .define("", true);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_SERVER, "Disable Space Station creation", false);
//            builder.comment("If set to true on a server, players will be completely unable to create space stations.");
//                    .translation("gc.configgui.disable_space_station_creation");
//                    .define("", false);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_CLIENT, "Override Capes", true);
//            builder.comment("By default, Galacticraft will override capes with the mod's donor cape. Set to false to disable.");
//                    .translation("gc.configgui.override_capes");
//                    .define("", true);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Space Station Solar Energy Multiplier", 2.0);
//            builder.comment("Solar panels will work (default 2x) more effective on space stations.");
//                    .translation("gc.configgui.space_station_energy_scalar");
//            spaceStationEnergyScalar = prop.getDouble(2.0);
//
//            try
//            {
//                prop = getConfig(Constants.CONFIG_CATEGORY_COMPATIBILITY, "External Sealable IDs", new String[] { Block.REGISTRY.getNameForObject(Blocks.GLASS_PANE) + ":0" });
//                prop.setComment("List non-opaque blocks from other mods (for example, special types of glass) that the Oxygen Sealer should recognize as solid seals. Format is BlockName or BlockName:metadata");
//                prop.setLanguageKey("gc.configgui.sealable_i_ds").worldRestart();
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
//            builder.comment("List blocks from other mods that the Sensor Glasses should recognize as solid blocks. Format is BlockName or BlockName:metadata.");
//                    .translation("gc.configgui.detectable_i_ds").worldRestart();
//            detectableIDs = prop.getStringList();
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Quick Game Mode", false);
//            builder.comment("Set this to true for less metal use in Galacticraft recipes (makes the game easier).");
//                    .translation("gc.configgui.quick_mode");
//                    .define("", false);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Harder Difficulty", false);
//            builder.comment("Set this to true for increased difficulty in modpacks (see forum for more info).");
//                    .translation("gc.configgui.hard_mode");
//                    .define("", false);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Adventure Game Mode", false);
//            builder.comment("Set this to true for a challenging adventure where the player starts the game stranded in the Asteroids dimension with low resources (only effective if Galacticraft Planets installed).");
//                    .translation("gc.configgui.asteroids_start");
//                    .define("", false);
//            if (!GalacticraftCore.isPlanetsLoaded)
//            {
//                challengeMode = false;
//            }
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Adventure Game Mode Flags", 15);
//            builder.comment("Add together flags 8, 4, 2, 1 to enable the four elements of adventure game mode. Default 15.  1 = extended compressor recipes.  2 = mob drops and spawning.  4 = more trees in hollow asteroids.  8 = start stranded in Asteroids.");
//                    .translation("gc.configgui.asteroids_flags");
//            challengeFlags = prop.getInt(15);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Suffocation Cooldown", 100);
//            builder.comment("Lower/Raise this value to change time between suffocation damage ticks (allowed range 50-250)");
//                    .translation("gc.configgui.suffocation_cooldown");
//            suffocationCooldown = Math.min(Math.max(50, prop.getInt(100)), 250);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Suffocation Damage", 2);
//            builder.comment("Change this value to modify the damage taken per suffocation tick");
//                    .translation("gc.configgui.suffocation_damage");
//            suffocationDamage = prop.getInt(2);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Dungeon Boss Health Modifier", 1.0);
//            builder.comment("Change this if you wish to balance the mod (if you have more powerful weapon mods).");
//                    .translation("gc.configgui.dungeon_boss_health_mod");
//            dungeonBossHealthMod = prop.getDouble(1.0);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_SERVER, "Enable Sealed edge checks", true);
//            builder.comment("If this is enabled, areas sealed by Oxygen Sealers will run a seal check when the player breaks or places a block (or on block updates).  This should be enabled for a 100% accurate sealed status, but can be disabled on servers for performance reasons.");
//                    .translation("gc.configgui.enable_sealer_edge_checks");
//                    .define("", true);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_COMPATIBILITY, "Alternate recipe for canisters", false);
//            builder.comment("Enable this if the standard canister recipe causes a conflict.");
//                    .translation("gc.configgui.alternate_canister_recipe").worldRestart();
//                    .define("", false);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_COMPATIBILITY, "OreDict name of other mod's silicon", "itemSilicon");
//            builder.comment("This needs to match the OreDictionary name used in the other mod. Set a nonsense name to disable.");
//                    .translation("gc.configgui.ore_dict_silicon");
//            otherModsSilicon = prop.getString();
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_COMPATIBILITY, "Must use GC's own space metals in recipes", true);
//            builder.comment("Should normally be true. If you set this to false, in a modpack with other mods with the same metals, players may be able to craft advanced GC items without travelling to Moon, Mars, Asteroids etc.");
//                    .translation("gc.configgui.disable_ore_dict_space_metals");
//                    .define("", true);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_KEYS, "Open Galaxy Map", "KEY_M");
//            builder.comment("Default Map key on first Galacticraft run only. After first run, change keys by Minecraft in-game Controls menu.  Valid settings: KEY_ followed by 0-9 or A-Z.");
//                    .translation("gc.configgui.override_map").worldRestart();
//            keyOverrideMap = prop.getString();
//            keyOverrideMapI = parseKeyValue(keyOverrideMap);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_KEYS, "Open Rocket GUI", "KEY_G");
//            builder.comment("Default Rocket/Fuel key on first Galacticraft run only. After first run, change keys by Minecraft in-game Controls menu.  Valid settings: KEY_ followed by 0-9 or A-Z.");
//                    .translation("gc.configgui.key_override_fuel_level").worldRestart();
//            keyOverrideFuelLevel = prop.getString();
//            keyOverrideFuelLevelI = parseKeyValue(keyOverrideFuelLevel);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_KEYS, "Toggle Advanced Goggles", "KEY_K");
//            builder.comment("Default Goggles key on first Galacticraft run only. After first run, change keys by Minecraft in-game Controls menu.  Valid settings: KEY_ followed by 0-9 or A-Z.");
//                    .translation("gc.configgui.key_override_toggle_adv_goggles").worldRestart();
//            keyOverrideToggleAdvGoggles = prop.getString();
//            keyOverrideToggleAdvGogglesI = parseKeyValue(keyOverrideToggleAdvGoggles);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_COMPATIBILITY, "Rocket fuel factor", 1);
//            builder.comment("The normal factor is 1.  Increase this to 2 - 5 if other mods with a lot of oil (e.g. BuildCraft) are installed to increase GC rocket fuel requirement.");
//                    .translation("gc.configgui.rocket_fuel_factor");
//            rocketFuelFactor = prop.getInt(1);
//
////            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Map factor", 1);
////            builder.comment("Allowed values 1-4 etc";
////            prop.setLanguageKey("gc.configgui.mapFactor");
////            mapfactor = prop.getInt(1);
////            finishProp(prop);
////
////            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Map size", 400);
////            builder.comment("Suggested value 400";
////            prop.setLanguageKey("gc.configgui.mapSize");
////            mapsize = prop.getInt(400);
////            finishProp(prop);
////
//            prop = getConfig(Constants.CONFIG_CATEGORY_CONTROLS, "Map Scroll Mouse Sensitivity", 1.0);
//            builder.comment("Increase to make the mouse drag scroll more sensitive, decrease to lower sensitivity.");
//                    .translation("gc.configgui.map_scroll_sensitivity");
//            mapMouseScrollSensitivity = (float) prop.getDouble(1.0);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_CONTROLS, "Map Scroll Mouse Invert", false);
//            builder.comment("Set to true to invert the mouse scroll feature on the galaxy map.");
//                    .translation("gc.configgui.map_scroll_invert");
//                    .define("", false);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Meteor Spawn Modifier", 1.0);
//            builder.comment("Set to a value between 0.0 and 1.0 to decrease meteor spawn chance (all dimensions).");
//                    .translation("gc.configgui.meteor_spawn_mod");
//            meteorSpawnMod = prop.getDouble(1.0);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Meteor Block Damage Enabled", true);
//            builder.comment("Set to false to stop meteors from breaking blocks on contact.");
//                    .translation("gc.configgui.meteor_block_damage");
//                    .define("", true);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Disable Update Check", false);
//            builder.comment("Update check will not run if this is set to true.");
//                    .translation("gc.configgui.disable_update_check");
//                    .define("", false);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Allow liquids into Gratings", true);
//            builder.comment("Liquids will not flow into Grating block if this is set to false.");
//                    .translation("gc.configgui.allow_liquids_grating").worldRestart();
//                    .define("", true);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Biome Type Registrations", false);
//            builder.comment("Biome Types will not be registered in the BiomeDictionary if this is set to true.");
//                    .translation("gc.configgui.disable_biome_type_registrations");
//                    .define("", false);
//
//            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Enable Space Race Manager Popup", false);
//            builder.comment("Space Race Manager will show on-screen after login, if enabled.");
//                    .translation("gc.configgui.enable_space_race_manager_popup");
//                    .define("", false);

            builder.push("WORLDGEN");

            oilGenFactor = builder.comment("Increasing this will increase amount of oil that will generate in each chunk.")
                    .translation("gc.configgui.oil_gen_factor")
                    .defineInRange("oil_generation_factor", 1.8, -Double.MAX_VALUE, Double.MAX_VALUE);

            externalOilGen = builder.comment("List of non-galacticraft dimension IDs to generate oil in.")
                    .translation("gc.configgui.external_oil_gen")
                    .defineList("oil_gen_in_external_dimension", new ArrayList<>(), o -> o instanceof Integer);

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
                    .defineList("Other_mods_ores_for_gc_to_generate_on_the_moon_and_planets", new ArrayList<>(), Objects::nonNull);

            disableBiomeTypeRegistrations = builder.comment("Biome Types will not be registered in the BiomeDictionary if this is set to true.")
                    .translation("gc.configgui.disable_biome_type_registrations")
                    .define("disable_biome_type_registrations", false);

            builder.pop();

            builder.push("DIMENSIONS");

            idDimensionOverworld = builder.comment("Dimension ID for the Overworld (as seen in the Celestial Map)")
                    .translation("gc.configgui.id_dimension_overworld")
                    .defineInRange("iddimensionoverworld", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            idDimensionMoon = builder.comment("Dimension ID for the Moon")
                    .translation("gc.configgui.id_dimension_moon")
                    .defineInRange("iddimensionmoon", -28, Integer.MIN_VALUE, Integer.MAX_VALUE);

            idDimensionOverworldOrbit = builder.comment("WorldProvider ID for Overworld Space Stations (advanced: do not change unless you have conflicts)")
                    .translation("gc.configgui.id_dimension_overworld_orbit")
                    .defineInRange("iddimensionoverworldorbit", -27, Integer.MIN_VALUE, Integer.MAX_VALUE);

            idDimensionOverworldOrbitStatic = builder.comment("WorldProvider ID for Static Space Stations (advanced: do not change unless you have conflicts)")
                    .translation("gc.configgui.id_dimension_overworld_orbit_static")
                    .defineInRange("iddimensionoverworldorbitstatic", -26, Integer.MIN_VALUE, Integer.MAX_VALUE);

            biomeIDbase = builder.comment("Biome ID base. GC will use biome IDs from this to this + 3, or more with addons. Allowed 40-250. Default 102.")
                    .translation("gc.configgui.biome_id_base")
                    .defineInRange("biomeidbase", 102, Integer.MIN_VALUE, Integer.MAX_VALUE);

            staticLoadDimensions = builder.comment("IDs to load at startup, and keep loaded until server stops. Can be added via /gckeeploaded")
                    .translation("gc.configgui.static_loaded_dimensions")
                    .defineList("static_loaded_dimensions", new ArrayList<>(), Objects::nonNull);

            keepLoadedNewSpaceStations = builder.comment("Set this to true to have an automatic /gckeeploaded for any new Space Station created.")
                    .translation("gc.configgui.static_loaded_new_ss")
                    .define("set_new_space_stations_to_be_static_loaded", true);

            disableRocketLaunchDimensions = builder.comment("IDs of dimensions where rockets should not launch - this should always include the Nether.")
                    .translation("gc.configgui.rocket_disabled_dimensions")
                    .defineList("dimensions_where_rockets_cannot_launch", Arrays.asList(DimensionType.THE_NETHER.getRegistryName().toString(), DimensionType.THE_END.getRegistryName().toString()), Objects::nonNull);

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
                    .defineList("external_sealable_ids", Arrays.asList(Blocks.GLASS_PANE.getRegistryName().toString()), Objects::nonNull);

            detectableIDs = builder.comment("List blocks from other mods that the Sensor Glasses should recognize as solid blocks. Format is BlockName or BlockName:metadata.")
                    .translation("gc.configgui.detectable_i_ds")
                    .defineList("external_detectable_ids", Arrays.asList(
                            Blocks.COAL_ORE.getRegistryName().toString(),
                            Blocks.DIAMOND_ORE.getRegistryName().toString(),
                            Blocks.GOLD_ORE.getRegistryName().toString(),
                            Blocks.IRON_ORE.getRegistryName().toString(),
                            Blocks.LAPIS_ORE.getRegistryName().toString(),
                            Blocks.REDSTONE_ORE.getRegistryName().toString()), Objects::nonNull);

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

            ic2ConversionRate = builder.comment("IndustrialCraft2 Conversion Ratio").defineInRange("ic2_conv_ratio", 16F / 2.44F, 0.01F, 1000F);
            rfConversionRate = builder.comment("Redstone Flux Conversion Ratio").defineInRange("rf_conv_ratio", 16F / 10F, 0.001F, 100.0F);
            bcConversionRate = builder.comment("Buildcraft Conversion Ratio").defineInRange("bc_conv_ratio", 16F, 0.01F, 1000.0F);
            mekConversionRate = builder.comment("Mekanism Conversion Ratio").defineInRange("mek_conv_ratio", (16F / 2.44F) / 10F, 0.001F, 100.0F);
            lossFactor = builder.comment("Loss factor when converting energy as a percentage (100 = no loss, 90 = 10% loss ...)").defineInRange("conv_loss_factor", 100.0F, 1.0F, 100.0F);

//            EnergyConfigHandler.updateRatios();

            displayEnergyUnitsBC = builder.comment("If BuildCraft is loaded, show Galacticraft machines energy as MJ instead of gJ?").define("display_energy_bc", false);
            displayEnergyUnitsIC2 = builder.comment("If IndustrialCraft2 is loaded, show Galacticraft machines energy as EU instead of gJ?").define("display_energy_ic2", false);
            displayEnergyUnitsMek = builder.comment("If Mekanism is loaded, show Galacticraft machines energy as Joules (J) instead of gJ?").define("display_energy_mek", false);
            displayEnergyUnitsRF = builder.comment("Show Galacticraft machines energy in RF instead of gJ?").define("display_energy_rf", false);

            disableMJinterface = builder.comment("Disable old Buildcraft API (MJ) interfacing completely?").define("disable_mj_interface", false);

            disableBuildCraftInput = builder.comment("Disable INPUT of BuildCraft energy").define("disable_input_bc", false);
            disableBuildCraftOutput = builder.comment("Disable OUTPUT of BuildCraft energy").define("disable_output_bc", false);
            disableRFInput = builder.comment("Disable INPUT of RF energy").define("disable_input_rf", false);
            disableRFOutput = builder.comment("Disable OUTPUT of RF energy").define("disable_output_rf", false);
            disableFEInput = builder.comment("Disable INPUT of Forge Energy to GC machines").define("disable_input_forge", false);
            disableFEOutput = builder.comment("Disable OUTPUT of Forge Energy from GC machines").define("disable_output_forge", false);
            disableIC2Input = builder.comment("Disable INPUT of IC2 energy").define("disable_input_ic2", false);
            disableIC2Output = builder.comment("Disable OUTPUT of IC2 energy").define("disable_output_ic2", false);
            disableMekanismInput = builder.comment("Disable INPUT of Mekanism energy").define("disable_input_mek", false);
            disableMekanismOutput = builder.comment("Disable OUTPUT of Mekanism energy").define("disable_output_mek", false);

//            if (!EnergyConfigHandler.isIndustrialCraft2Loaded())
//            {
//                EnergyConfigHandler.displayEnergyUnitsIC2 = false;
//            }
//            if (!EnergyConfigHandler.isMekanismLoaded())
//            {
//                EnergyConfigHandler.displayEnergyUnitsMek = false;
//            }
//            if (EnergyConfigHandler.displayEnergyUnitsIC2)
//            {
//                EnergyConfigHandler.displayEnergyUnitsBC = false;
//            }
//            if (EnergyConfigHandler.displayEnergyUnitsMek)
//            {
//                EnergyConfigHandler.displayEnergyUnitsBC = false;
//                EnergyConfigHandler.displayEnergyUnitsIC2 = false;
//            }
//            if (EnergyConfigHandler.displayEnergyUnitsRF)
//            {
//                EnergyConfigHandler.displayEnergyUnitsBC = false;
//                EnergyConfigHandler.displayEnergyUnitsIC2 = false;
//                EnergyConfigHandler.displayEnergyUnitsMek = false;
//            }

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

//            challengeModeUpdate();
//        }
//        catch (final Exception e)
//        {
//            GCLog.severe("Problem loading core config (\"core.conf\")");
//            e.printStackTrace();
//        }
    }

    public boolean setLoaded(DimensionType newID)
    {
        if (staticLoadDimensions.get().contains(newID.getRegistryName().toString())) return false;
        List<String> list = new ArrayList<>(staticLoadDimensions.get());
        list.add(newID.getRegistryName().toString());
        staticLoadDimensions.set(list);
        return true;
    }

    public boolean setUnloaded(DimensionType idToRemove)
    {
        List<? extends String> list = new ArrayList<>(staticLoadDimensions.get());
        int rem = 0;
        while (list.remove(idToRemove.toString()))
        {
            rem++;
        }

        staticLoadDimensions.set(list);

        return rem > 0;
    }

    public void challengeModeUpdate()
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

    public float getIc2ConversionRate() {
        return (float) (ic2ConversionRate.get() * getFactor());
    }

    public float getBcConversionRate() {
        return (float) (bcConversionRate.get() * getFactor());
    }

    public float getRfConversionRate() {
        return (float) (rfConversionRate.get() * getFactor());
    }

    public float getMekConversionRate() {
        return (float) (mekConversionRate.get() * getFactor());
    }

    public float getToIc2ConversionRate() {
        return (float) (getFactor() / ic2ConversionRate.get());
    }

    public float getToBcConversionRate() {
        return (float) (getFactor() / bcConversionRate.get());
    }

    public float getToRfConversionRate() {
        return (float) (getFactor() / rfConversionRate.get());
    }

    public float getToMekConversionRate() {
        return (float) (getFactor() / mekConversionRate.get());
    }

    public float getToIc2ConversionRateDisplay() {
        return (float) (1.0D / ic2ConversionRate.get());
    }

    public float getToBcConversionRateDisplay() {
        return (float) (1.0D / bcConversionRate.get());
    }

    public float getToRfConversionRateDisplay() {
        return (float) (1.0D / rfConversionRate.get());
    }

    public float getToMekConversionRateDisplay() {
        return (float) (1.0D / mekConversionRate.get());
    }

    private double getFactor() {
        return lossFactor.get() / 100.0D;
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
//    	int modeFlags = ConfigManagerCore.INSTANCE.hardMode ? 1 : 0;
//    	modeFlags += ConfigManagerCore.INSTANCE.quickMode ? 2 : 0;
//    	modeFlags += ConfigManagerCore.INSTANCE.challengeMode ? 4 : 0;
//    	modeFlags += ConfigManagerCore.INSTANCE.disableSpaceStationCreation ? 8 : 0;
//    	modeFlags += ConfigManagerCore.INSTANCE.recipesRequireGCAdvancedMetals ? 16 : 0;
//    	modeFlags += ConfigManagerCore.INSTANCE.challengeRecipes ? 32 : 0;
//        modeFlags += ConfigManagerCore.INSTANCE.allowLiquidGratings ? 64 : 0;
//    	returnList.add(modeFlags);
//    	returnList.add(ConfigManagerCore.INSTANCE.dungeonBossHealthMod);
//    	returnList.add(ConfigManagerCore.INSTANCE.suffocationDamage);
//    	returnList.add(ConfigManagerCore.INSTANCE.suffocationCooldown);
//    	returnList.add(ConfigManagerCore.INSTANCE.rocketFuelFactor);
//    	returnList.add(ConfigManagerCore.INSTANCE.otherModsSilicon);
//    	//If changing this, update definition of EnumSimplePacket.C_UPDATE_CONFIGS - see comment in setConfigOverride() below
//    	EnergyConfigHandler.serverConfigOverride(returnList);
//
//    	returnList.add(ConfigManagerCore.INSTANCE.detectableIDs.clone());
//    	//TODO Should this include any other client-LogicalSide configurables too?
//    	return returnList;
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    public static void setConfigOverride(List<Object> configs)
//    {
//        int dataCount = 0;
//    	int modeFlag = (Integer) configs.get(dataCount++);
//    	ConfigManagerCore.INSTANCE.hardMode = (modeFlag & 1) != 0;
//    	ConfigManagerCore.INSTANCE.quickMode = (modeFlag & 2) != 0;
//    	ConfigManagerCore.INSTANCE.challengeMode = (modeFlag & 4) != 0;
//    	ConfigManagerCore.INSTANCE.disableSpaceStationCreation = (modeFlag & 8) != 0;
//    	ConfigManagerCore.INSTANCE.recipesRequireGCAdvancedMetals = (modeFlag & 16) != 0;
//    	ConfigManagerCore.INSTANCE.challengeRecipes = (modeFlag & 32) != 0;
//        ConfigManagerCore.INSTANCE.allowLiquidGratings = (modeFlag & 64) != 0;
//    	ConfigManagerCore.INSTANCE.dungeonBossHealthMod = (Double) configs.get(dataCount++);
//    	ConfigManagerCore.INSTANCE.suffocationDamage = (Integer) configs.get(dataCount++);
//    	ConfigManagerCore.INSTANCE.suffocationCooldown = (Integer) configs.get(dataCount++);
//    	ConfigManagerCore.INSTANCE.rocketFuelFactor = (Integer) configs.get(dataCount++);
//    	ConfigManagerCore.INSTANCE.otherModsSilicon = (String) configs.get(dataCount++);
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
//    			ConfigManagerCore.INSTANCE.detectableIDs = new String[sizeIDs];
//		    	for (int j = 0; j < sizeIDs; j++)
//		    	ConfigManagerCore.INSTANCE.detectableIDs[j] = new String((String) configs.get(dataCount++));
//    		}
//    		else if (dataLast instanceof String[])
//    		{
//    			ConfigManagerCore.INSTANCE.detectableIDs = ((String[])dataLast);
//    		}
//        	TickHandlerClient.registerDetectableBlocks(false);
//    	}
//
//    	challengeModeUpdate();
//    	RecipeManagerGC.setConfigurableRecipes();
//    }

//    public static void saveClientConfigOverrideable()
//    {
//        if (ConfigManagerCore.INSTANCE.clientSave == null)
//        {
//            ConfigManagerCore.INSTANCE.clientSave = (ArrayList<Object>) ConfigManagerCore.INSTANCE.getServerConfigOverride();
//        }
//    }
//
//    public static void restoreClientConfigOverrideable()
//    {
//        if (ConfigManagerCore.INSTANCE.clientSave != null)
//        {
//            ConfigManagerCore.INSTANCE.setConfigOverride(clientSave);
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

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent)
    {
        LogManager.getLogger().debug(FORGEMOD, "Loaded GalacticraftCore config file {}", configEvent.getConfig().getFileName());
        ConfigManagerCore.INSTANCE.challengeModeUpdate();
        ConfigManagerCore.INSTANCE.disableRocketLaunchAllNonGC = searchAsterisk(ConfigManagerCore.INSTANCE.disableRocketLaunchDimensions.get().toArray(new String[0]));
    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.Reloading configEvent)
    {
        LogManager.getLogger().debug(FORGEMOD, "GalacticraftCore config just got changed on the file system!");
    }
}
