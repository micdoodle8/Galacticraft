package micdoodle8.mods.galacticraft.core.util;

import com.google.common.primitives.Ints;
import micdoodle8.mods.galacticraft.api.vector.BlockTuple;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;
import micdoodle8.mods.galacticraft.core.recipe.RecipeManagerGC;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerClient;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ConfigManagerCore
{
    static Configuration config;

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
    public static int idDimensionOverworld;
    public static int idDimensionOverworldOrbit;
    public static int idDimensionOverworldOrbitStatic;
    public static int idDimensionMoon;
    public static int biomeIDbase = 102;
    public static boolean disableBiomeTypeRegistrations;
    public static int[] staticLoadDimensions = {};
    public static int[] disableRocketLaunchDimensions = { -1, 1 };
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
    private static Map<String, List<String>> propOrder = new TreeMap<>();
    private static String currentCat;

    public static void initialize(File file)
    {
        ConfigManagerCore.config = new Configuration(file);
        ConfigManagerCore.syncConfig(true);
    }

    public static void forceSave()
    {
        ConfigManagerCore.config.save();
    }

    public static void syncConfig(boolean load)
    {
        try
        {
            propOrder.clear();
            Property prop;

            if (!config.isChild)
            {
                if (load)
                {
                    config.load();
                }
            }

            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Enable Debug Messages", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.enable_debug.description"));
            prop.setLanguageKey("gc.configgui.enable_debug");
            enableDebug = prop.getBoolean(false);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_DIMENSIONS, "idDimensionOverworld", 0);
            prop.setComment(GCCoreUtil.translate("gc.configgui.id_dimension_overworld.description"));
            prop.setLanguageKey("gc.configgui.id_dimension_overworld").setRequiresMcRestart(true);
            idDimensionOverworld = prop.getInt();
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_DIMENSIONS, "idDimensionMoon", -28);
            prop.setComment(GCCoreUtil.translate("gc.configgui.id_dimension_moon.description"));
            prop.setLanguageKey("gc.configgui.id_dimension_moon").setRequiresMcRestart(true);
            idDimensionMoon = prop.getInt();
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_DIMENSIONS, "idDimensionOverworldOrbit", -27);
            prop.setComment(GCCoreUtil.translate("gc.configgui.id_dimension_overworld_orbit.description"));
            prop.setLanguageKey("gc.configgui.id_dimension_overworld_orbit").setRequiresMcRestart(true);
            idDimensionOverworldOrbit = prop.getInt();
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_DIMENSIONS, "idDimensionOverworldOrbitStatic", -26);
            prop.setComment(GCCoreUtil.translate("gc.configgui.id_dimension_overworld_orbit_static.description"));
            prop.setLanguageKey("gc.configgui.id_dimension_overworld_orbit_static").setRequiresMcRestart(true);
            idDimensionOverworldOrbitStatic = prop.getInt();
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_DIMENSIONS, "biomeIDBase", 102);
            prop.setComment(GCCoreUtil.translate("gc.configgui.biome_id_base.description"));
            prop.setLanguageKey("gc.configgui.biome_id_base").setRequiresMcRestart(true);
            biomeIDbase = prop.getInt();
            if (biomeIDbase < 40 || biomeIDbase > 250)
            {
                biomeIDbase = 102;
            }
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_DIMENSIONS, "Static Loaded Dimensions", ConfigManagerCore.staticLoadDimensions);
            prop.setComment(GCCoreUtil.translate("gc.configgui.static_loaded_dimensions.description"));
            prop.setLanguageKey("gc.configgui.static_loaded_dimensions");
            staticLoadDimensions = prop.getIntList();
            finishProp(prop);
            
            prop = getConfig(Constants.CONFIG_CATEGORY_DIMENSIONS, "Set new Space Stations to be static loaded", true);
            prop.setComment(GCCoreUtil.translate("gc.configgui.static_loaded_new_ss.description"));
            prop.setLanguageKey("gc.configgui.static_loaded_new_ss");
            keepLoadedNewSpaceStations = prop.getBoolean();
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_DIMENSIONS, "Dimensions where rockets cannot launch", new String[] { "1", "-1" });
            prop.setComment(GCCoreUtil.translate("gc.configgui.rocket_disabled_dimensions.description"));
            prop.setLanguageKey("gc.configgui.rocket_disabled_dimensions");
            disableRocketLaunchDimensions = prop.getIntList();
            disableRocketLaunchAllNonGC = searchAsterisk(prop.getStringList());
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_DIMENSIONS, "Disable rockets from returning to Overworld", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.rocket_disable_overworld_return.description"));
            prop.setLanguageKey("gc.configgui.rocket_disable_overworld_return");
            disableRocketsToOverworld = prop.getBoolean(false);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_SERVER, "World border for landing location on other planets (Moon, Mars, etc)", 0);
            prop.setComment(GCCoreUtil.translate("gc.configgui.planet_worldborders.description"));
            prop.setLanguageKey("gc.configgui.planet_worldborders");
            otherPlanetWorldBorders = prop.getInt(0);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Force Overworld Spawn", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.force_overworld_respawn.description"));
            prop.setLanguageKey("gc.configgui.force_overworld_respawn");
            forceOverworldRespawn = prop.getBoolean(false);
            finishProp(prop);

            //

            prop = getConfig(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicRocketT1", 0);
            prop.setComment(GCCoreUtil.translate("gc.configgui.id_schematic_rocket_t1.description"));
            prop.setLanguageKey("gc.configgui.id_schematic_rocket_t1");
            idSchematicRocketT1 = prop.getInt(0);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicMoonBuggy", 1);
            prop.setComment(GCCoreUtil.translate("gc.configgui.id_schematic_moon_buggy.description"));
            prop.setLanguageKey("gc.configgui.id_schematic_moon_buggy");
            idSchematicMoonBuggy = prop.getInt(1);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicAddSchematic", Integer.MAX_VALUE);
            prop.setComment(GCCoreUtil.translate("gc.configgui.id_schematic_add_schematic.description"));
            prop.setLanguageKey("gc.configgui.id_schematic_add_schematic");
            idSchematicAddSchematic = prop.getInt(Integer.MAX_VALUE);
            finishProp(prop);

            //

            prop = getConfig(Constants.CONFIG_CATEGORY_ACHIEVEMENTS, "idAchievBase", 1784);
            prop.setComment(GCCoreUtil.translate("gc.configgui.id_achiev_base.description"));
            prop.setLanguageKey("gc.configgui.id_achiev_base");
            idAchievBase = prop.getInt(1784);
            finishProp(prop);

//Client side

            prop = getConfig(Constants.CONFIG_CATEGORY_CLIENT, "More Stars", true);
            prop.setComment(GCCoreUtil.translate("gc.configgui.more_stars.description"));
            prop.setLanguageKey("gc.configgui.more_stars");
            moreStars = prop.getBoolean(true);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_CLIENT, "Disable Spaceship Particles", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.disable_spaceship_particles.description"));
            prop.setLanguageKey("gc.configgui.disable_spaceship_particles");
            disableSpaceshipParticles = prop.getBoolean(false);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_CLIENT, "Disable Vehicle Third-Person and Zoom", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.disable_vehicle_camera_changes.description"));
            prop.setLanguageKey("gc.configgui.disable_vehicle_camera_changes");
            disableVehicleCameraChanges = prop.getBoolean(false);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_CLIENT, "Minimap Left", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.oxygen_indicator_left.description"));
            prop.setLanguageKey("gc.configgui.oxygen_indicator_left");
            oxygenIndicatorLeft = prop.getBoolean(false);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_CLIENT, "Minimap Bottom", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.oxygen_indicator_bottom.description"));
            prop.setLanguageKey("gc.configgui.oxygen_indicator_bottom");
            oxygenIndicatorBottom = prop.getBoolean(false);
            finishProp(prop);

//World gen

            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Oil Generation Factor", 1.8);
            prop.setComment(GCCoreUtil.translate("gc.configgui.oil_gen_factor.description"));
            prop.setLanguageKey("gc.configgui.oil_gen_factor");
            oilGenFactor = prop.getDouble(1.8);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Oil gen in external dimensions", new int[] { 0 });
            prop.setComment(GCCoreUtil.translate("gc.configgui.external_oil_gen.description"));
            prop.setLanguageKey("gc.configgui.external_oil_gen");
            externalOilGen = prop.getIntList();
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Retro Gen of GC Oil in existing map chunks", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.enable_retrogen_oil.description"));
            prop.setLanguageKey("gc.configgui.enable_retrogen_oil");
            retrogenOil = prop.getBoolean(false);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Enable Copper Ore Gen", true);
            prop.setComment(GCCoreUtil.translate("gc.configgui.enable_copper_ore_gen.description"));
            prop.setLanguageKey("gc.configgui.enable_copper_ore_gen").setRequiresMcRestart(true);
            enableCopperOreGen = prop.getBoolean(true);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Enable Tin Ore Gen", true);
            prop.setComment(GCCoreUtil.translate("gc.configgui.enable_tin_ore_gen.description"));
            prop.setLanguageKey("gc.configgui.enable_tin_ore_gen").setRequiresMcRestart(true);
            enableTinOreGen = prop.getBoolean(true);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Enable Aluminum Ore Gen", true);
            prop.setComment(GCCoreUtil.translate("gc.configgui.enable_aluminum_ore_gen.description"));
            prop.setLanguageKey("gc.configgui.enable_aluminum_ore_gen").setRequiresMcRestart(true);
            enableAluminumOreGen = prop.getBoolean(true);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Enable Silicon Ore Gen", true);
            prop.setComment(GCCoreUtil.translate("gc.configgui.enable_silicon_ore_gen.description"));
            prop.setLanguageKey("gc.configgui.enable_silicon_ore_gen").setRequiresMcRestart(true);
            enableSiliconOreGen = prop.getBoolean(true);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Cheese Ore Gen on Moon", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.disable_cheese_moon.description"));
            prop.setLanguageKey("gc.configgui.disable_cheese_moon");
            disableCheeseMoon = prop.getBoolean(false);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Tin Ore Gen on Moon", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.disable_tin_moon.description"));
            prop.setLanguageKey("gc.configgui.disable_tin_moon");
            disableTinMoon = prop.getBoolean(false);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Copper Ore Gen on Moon", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.disable_copper_moon.description"));
            prop.setLanguageKey("gc.configgui.disable_copper_moon");
            disableCopperMoon = prop.getBoolean(false);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Sapphire Ore Gen on Moon", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.disable_sapphire_moon.description"));
            prop.setLanguageKey("gc.configgui.disable_sapphire_moon");
            disableSapphireMoon = prop.getBoolean(false);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Moon Village Gen", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.disable_moon_village_gen.description"));
            prop.setLanguageKey("gc.configgui.disable_moon_village_gen");
            disableMoonVillageGen = prop.getBoolean(false);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Generate all other mods features on planets", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.enable_other_mods_features.description"));
            prop.setLanguageKey("gc.configgui.enable_other_mods_features");
            enableOtherModsFeatures = prop.getBoolean(false);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Whitelist CoFHCore worldgen to generate its ores and lakes on planets", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.whitelist_co_f_h_core_gen.description"));
            prop.setLanguageKey("gc.configgui.whitelist_co_f_h_core_gen");
            whitelistCoFHCoreGen = prop.getBoolean(false);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Generate ThaumCraft wild nodes on planetary surfaces", true);
            prop.setComment(GCCoreUtil.translate("gc.configgui.enable_thaum_craft_nodes.description"));
            prop.setLanguageKey("gc.configgui.enable_thaum_craft_nodes");
            enableThaumCraftNodes = prop.getBoolean(true);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Other mods ores for GC to generate on the Moon and planets", new String[] {});
            prop.setComment(GCCoreUtil.translate("gc.configgui.other_mod_ore_gen_i_ds.description"));
            prop.setLanguageKey("gc.configgui.other_mod_ore_gen_i_ds");
            oregenIDs = prop.getStringList();
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_COMPATIBILITY, "Use legacy oilgc fluid registration", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.use_old_oil_fluid_i_d.description"));
            prop.setLanguageKey("gc.configgui.use_old_oil_fluid_i_d");
            useOldOilFluidID = prop.getBoolean(false);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_COMPATIBILITY, "Use legacy fuelgc fluid registration", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.use_old_fuel_fluid_i_d.description"));
            prop.setLanguageKey("gc.configgui.use_old_fuel_fluid_i_d");
            useOldFuelFluidID = prop.getBoolean(false);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Disable lander on Moon and other planets", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.disable_lander.description"));
            prop.setLanguageKey("gc.configgui.disable_lander");
            disableLander = prop.getBoolean(false);
            finishProp(prop);

//Server side

            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Disable Spaceship Explosion", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.disable_spaceship_grief.description"));
            prop.setLanguageKey("gc.configgui.disable_spaceship_grief");
            disableSpaceshipGrief = prop.getBoolean(false);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_SERVER, "Space Stations Require Permission", true);
            prop.setComment(GCCoreUtil.translate("gc.configgui.space_stations_require_permission.description"));
            prop.setLanguageKey("gc.configgui.space_stations_require_permission");
            spaceStationsRequirePermission = prop.getBoolean(true);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_SERVER, "Disable Space Station creation", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.disable_space_station_creation.description"));
            prop.setLanguageKey("gc.configgui.disable_space_station_creation");
            disableSpaceStationCreation = prop.getBoolean(false);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_CLIENT, "Override Capes", true);
            prop.setComment(GCCoreUtil.translate("gc.configgui.override_capes.description"));
            prop.setLanguageKey("gc.configgui.override_capes");
            overrideCapes = prop.getBoolean(true);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Space Station Solar Energy Multiplier", 2.0);
            prop.setComment(GCCoreUtil.translate("gc.configgui.space_station_energy_scalar.description"));
            prop.setLanguageKey("gc.configgui.space_station_energy_scalar");
            spaceStationEnergyScalar = prop.getDouble(2.0);
            finishProp(prop);

            try
            {
                prop = getConfig(Constants.CONFIG_CATEGORY_COMPATIBILITY, "External Sealable IDs", new String[] { Block.REGISTRY.getNameForObject(Blocks.GLASS_PANE) + ":0" });
                prop.setComment(GCCoreUtil.translate("gc.configgui.sealable_i_ds.description"));
                prop.setLanguageKey("gc.configgui.sealable_i_ds").setRequiresMcRestart(true);
                sealableIDs = prop.getStringList();
                finishProp(prop);
            }
            catch (Exception e)
            {
                FMLLog.severe("[Galacticraft] It appears you have installed the 'Dev' version of Galacticraft instead of the regular version (or vice versa).  Please re-install.");
            }

            prop = getConfig(Constants.CONFIG_CATEGORY_COMPATIBILITY, "External Detectable IDs", new String[] {
                    Block.REGISTRY.getNameForObject(Blocks.COAL_ORE).getResourcePath(),
                    Block.REGISTRY.getNameForObject(Blocks.DIAMOND_ORE).getResourcePath(),
                    Block.REGISTRY.getNameForObject(Blocks.GOLD_ORE).getResourcePath(),
                    Block.REGISTRY.getNameForObject(Blocks.IRON_ORE).getResourcePath(),
                    Block.REGISTRY.getNameForObject(Blocks.LAPIS_ORE).getResourcePath(),
                    Block.REGISTRY.getNameForObject(Blocks.REDSTONE_ORE).getResourcePath(),
                    Block.REGISTRY.getNameForObject(Blocks.LIT_REDSTONE_ORE).getResourcePath() });
            prop.setComment(GCCoreUtil.translate("gc.configgui.detectable_i_ds.description"));
            prop.setLanguageKey("gc.configgui.detectable_i_ds").setRequiresMcRestart(true);
            detectableIDs = prop.getStringList();
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Quick Game Mode", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.quick_mode.description"));
            prop.setLanguageKey("gc.configgui.quick_mode");
            quickMode = prop.getBoolean(false);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Harder Difficulty", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.hard_mode.description"));
            prop.setLanguageKey("gc.configgui.hard_mode");
            hardMode = prop.getBoolean(false);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Adventure Game Mode", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.asteroids_start.description"));
            prop.setLanguageKey("gc.configgui.asteroids_start");
            challengeMode = prop.getBoolean(false);
            if (!GalacticraftCore.isPlanetsLoaded)
            {
                challengeMode = false;
            }
            finishProp(prop);
            
            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Adventure Game Mode Flags", 15);
            prop.setComment(GCCoreUtil.translate("gc.configgui.asteroids_flags.description"));
            prop.setLanguageKey("gc.configgui.asteroids_flags");
            challengeFlags = prop.getInt(15);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Suffocation Cooldown", 100);
            prop.setComment(GCCoreUtil.translate("gc.configgui.suffocation_cooldown.description"));
            prop.setLanguageKey("gc.configgui.suffocation_cooldown");
            suffocationCooldown = Math.min(Math.max(50, prop.getInt(100)), 250);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Suffocation Damage", 2);
            prop.setComment(GCCoreUtil.translate("gc.configgui.suffocation_damage.description"));
            prop.setLanguageKey("gc.configgui.suffocation_damage");
            suffocationDamage = prop.getInt(2);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Dungeon Boss Health Modifier", 1.0);
            prop.setComment(GCCoreUtil.translate("gc.configgui.dungeon_boss_health_mod.description"));
            prop.setLanguageKey("gc.configgui.dungeon_boss_health_mod");
            dungeonBossHealthMod = prop.getDouble(1.0);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_SERVER, "Enable Sealed edge checks", true);
            prop.setComment(GCCoreUtil.translate("gc.configgui.enable_sealer_edge_checks.description"));
            prop.setLanguageKey("gc.configgui.enable_sealer_edge_checks");
            enableSealerEdgeChecks = prop.getBoolean(true);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_COMPATIBILITY, "Alternate recipe for canisters", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.alternate_canister_recipe.description"));
            prop.setLanguageKey("gc.configgui.alternate_canister_recipe").setRequiresMcRestart(true);
            alternateCanisterRecipe = prop.getBoolean(false);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_COMPATIBILITY, "OreDict name of other mod's silicon", "itemSilicon");
            prop.setComment(GCCoreUtil.translate("gc.configgui.ore_dict_silicon.description"));
            prop.setLanguageKey("gc.configgui.ore_dict_silicon");
            otherModsSilicon = prop.getString();
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_COMPATIBILITY, "Must use GC's own space metals in recipes", true);
            prop.setComment(GCCoreUtil.translate("gc.configgui.disable_ore_dict_space_metals.description"));
            prop.setLanguageKey("gc.configgui.disable_ore_dict_space_metals");
            recipesRequireGCAdvancedMetals = prop.getBoolean(true);
            finishProp(prop);
            
            prop = getConfig(Constants.CONFIG_CATEGORY_KEYS, "Open Galaxy Map", "KEY_M");
            prop.setComment(GCCoreUtil.translate("gc.configgui.override_map.description"));
            prop.setLanguageKey("gc.configgui.override_map").setRequiresMcRestart(true);
            keyOverrideMap = prop.getString();
            keyOverrideMapI = parseKeyValue(keyOverrideMap);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_KEYS, "Open Rocket GUI", "KEY_G");
            prop.setComment(GCCoreUtil.translate("gc.configgui.key_override_fuel_level.description"));
            prop.setLanguageKey("gc.configgui.key_override_fuel_level").setRequiresMcRestart(true);
            keyOverrideFuelLevel = prop.getString();
            keyOverrideFuelLevelI = parseKeyValue(keyOverrideFuelLevel);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_KEYS, "Toggle Advanced Goggles", "KEY_K");
            prop.setComment(GCCoreUtil.translate("gc.configgui.key_override_toggle_adv_goggles.description"));
            prop.setLanguageKey("gc.configgui.key_override_toggle_adv_goggles").setRequiresMcRestart(true);
            keyOverrideToggleAdvGoggles = prop.getString();
            keyOverrideToggleAdvGogglesI = parseKeyValue(keyOverrideToggleAdvGoggles);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_COMPATIBILITY, "Rocket fuel factor", 1);
            prop.setComment(GCCoreUtil.translate("gc.configgui.rocket_fuel_factor.description"));
            prop.setLanguageKey("gc.configgui.rocket_fuel_factor");
            rocketFuelFactor = prop.getInt(1);
            finishProp(prop);

//            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Map factor", 1);
//            prop.setComment("Allowed values 1-4 etc";
//            prop.setLanguageKey("gc.configgui.mapFactor");
//            mapfactor = prop.getInt(1);
//            finishProp(prop);
//            
//            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Map size", 400);
//            prop.setComment("Suggested value 400";
//            prop.setLanguageKey("gc.configgui.mapSize");
//            mapsize = prop.getInt(400);
//            finishProp(prop);
//            
            prop = getConfig(Constants.CONFIG_CATEGORY_CONTROLS, "Map Scroll Mouse Sensitivity", 1.0);
            prop.setComment(GCCoreUtil.translate("gc.configgui.map_scroll_sensitivity.description"));
            prop.setLanguageKey("gc.configgui.map_scroll_sensitivity");
            mapMouseScrollSensitivity = (float) prop.getDouble(1.0);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_CONTROLS, "Map Scroll Mouse Invert", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.map_scroll_invert.description"));
            prop.setLanguageKey("gc.configgui.map_scroll_invert");
            invertMapMouseScroll = prop.getBoolean(false);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Meteor Spawn Modifier", 1.0);
            prop.setComment(GCCoreUtil.translate("gc.configgui.meteor_spawn_mod.description"));
            prop.setLanguageKey("gc.configgui.meteor_spawn_mod");
            meteorSpawnMod = prop.getDouble(1.0);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_DIFFICULTY, "Meteor Block Damage Enabled", true);
            prop.setComment(GCCoreUtil.translate("gc.configgui.meteor_block_damage.description"));
            prop.setLanguageKey("gc.configgui.meteor_block_damage");
            meteorBlockDamageEnabled = prop.getBoolean(true);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Disable Update Check", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.disable_update_check.description"));
            prop.setLanguageKey("gc.configgui.disable_update_check");
            disableUpdateCheck = prop.getBoolean(false);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Allow liquids into Gratings", true);
            prop.setComment(GCCoreUtil.translate("gc.configgui.allow_liquids_grating.description"));
            prop.setLanguageKey("gc.configgui.allow_liquids_grating").setRequiresMcRestart(true);
            allowLiquidGratings = prop.getBoolean(true);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Biome Type Registrations", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.disable_biome_type_registrations.description"));
            prop.setLanguageKey("gc.configgui.disable_biome_type_registrations");
            disableBiomeTypeRegistrations = prop.getBoolean(false);
            finishProp(prop);

            prop = getConfig(Constants.CONFIG_CATEGORY_GENERAL, "Enable Space Race Manager Popup", false);
            prop.setComment(GCCoreUtil.translate("gc.configgui.enable_space_race_manager_popup.description"));
            prop.setLanguageKey("gc.configgui.enable_space_race_manager_popup");
            enableSpaceRaceManagerPopup = prop.getBoolean(false);
            finishProp(prop);

            //Cleanup older GC config files
            cleanConfig(config, propOrder);

            if (config.hasChanged())
            {
                config.save();
            }
            
            challengeModeUpdate();
        }
        catch (final Exception e)
        {
            GCLog.severe("Problem loading core config (\"core.conf\")");
            e.printStackTrace();
        }
    }
    
    public static void cleanConfig(Configuration config, Map<String, List<String>> propOrder)
    {
        List<String> categoriesToRemove = new LinkedList<>();
        for (String catName : config.getCategoryNames())
        {
            List<String> newProps = propOrder.get(catName); 
            if (newProps == null)
            {
                categoriesToRemove.add(catName);
            }
            else
            {
                ConfigCategory cat = config.getCategory(catName);
                List<String> toRemove = new LinkedList<>();
                for (String oldprop : cat.keySet())
                {
                    if (!newProps.contains(oldprop))
                    {
                        toRemove.add(oldprop);
                    }
                }
                for (String removeMe : toRemove)
                {
                    cat.remove(removeMe);
                }
                config.setCategoryPropertyOrder(catName, propOrder.get(catName));
            }
        }
        for (String catName : categoriesToRemove)
        {
            config.removeCategory(config.getCategory(catName));
        }
    }
    
    private static Property getConfig(String cat, String key, int defaultValue)
    {
        config.moveProperty(Constants.CONFIG_CATEGORY_GENERAL, key, cat);
        currentCat = cat;
        return config.get(cat, key, defaultValue);
    }
    
    private static Property getConfig(String cat, String key, double defaultValue)
    {
        config.moveProperty(Constants.CONFIG_CATEGORY_GENERAL, key, cat);
        currentCat = cat;
        return config.get(cat, key, defaultValue);
    }
    
    private static Property getConfig(String cat, String key, boolean defaultValue)
    {
        config.moveProperty(Constants.CONFIG_CATEGORY_GENERAL, key, cat);
        currentCat = cat;
        return config.get(cat, key, defaultValue);
    }
    
    private static Property getConfig(String cat, String key, String defaultValue)
    {
        config.moveProperty(Constants.CONFIG_CATEGORY_GENERAL, key, cat);
        config.moveProperty(Constants.CONFIG_CATEGORY_CONTROLS, key, cat);
        currentCat = cat;
        return config.get(cat, key, defaultValue);
    }
    
    private static Property getConfig(String cat, String key, String[] defaultValue)
    {
        config.moveProperty(Constants.CONFIG_CATEGORY_GENERAL, key, cat);
        currentCat = cat;
        return config.get(cat, key, defaultValue);
    }
    
    private static Property getConfig(String cat, String key, int[] defaultValue)
    {
        config.moveProperty(Constants.CONFIG_CATEGORY_GENERAL, key, cat);
        currentCat = cat;
        return config.get(cat, key, defaultValue);
    }
    
    private static void finishProp(Property prop)
    {
        if (propOrder.get(currentCat) == null)
        {
            propOrder.put(currentCat, new ArrayList<String>());
        }
        propOrder.get(currentCat).add(prop.getName());
    }

    public static boolean setLoaded(int newID)
    {
        boolean found = false;

        for (int staticLoadDimension : ConfigManagerCore.staticLoadDimensions)
        {
            if (staticLoadDimension == newID)
            {
                found = true;
                break;
            }
        }

        if (!found)
        {
            int[] oldIDs = ConfigManagerCore.staticLoadDimensions;
            ConfigManagerCore.staticLoadDimensions = new int[ConfigManagerCore.staticLoadDimensions.length + 1];
            System.arraycopy(oldIDs, 0, staticLoadDimensions, 0, oldIDs.length);

            ConfigManagerCore.staticLoadDimensions[ConfigManagerCore.staticLoadDimensions.length - 1] = newID;
            String[] values = new String[ConfigManagerCore.staticLoadDimensions.length];
            Arrays.sort(ConfigManagerCore.staticLoadDimensions);

            for (int i = 0; i < values.length; i++)
            {
                values[i] = String.valueOf(ConfigManagerCore.staticLoadDimensions[i]);
            }

            Property prop = config.get(Constants.CONFIG_CATEGORY_DIMENSIONS, "Static Loaded Dimensions", ConfigManagerCore.staticLoadDimensions);
            prop.setComment(GCCoreUtil.translate("gc.configgui.static_loaded_dimensions.description"));
            prop.setLanguageKey("gc.configgui.static_loaded_dimensions");
            prop.set(values);

            ConfigManagerCore.config.save();
        }

        return !found;
    }

    public static boolean setUnloaded(int idToRemove)
    {
        int foundCount = 0;

        for (int staticLoadDimension : ConfigManagerCore.staticLoadDimensions)
        {
            if (staticLoadDimension == idToRemove)
            {
                foundCount++;
            }
        }

        if (foundCount > 0)
        {
            List<Integer> idArray = new ArrayList<Integer>(Ints.asList(ConfigManagerCore.staticLoadDimensions));
            idArray.removeAll(Collections.singleton(idToRemove));

            ConfigManagerCore.staticLoadDimensions = new int[idArray.size()];

            for (int i = 0; i < idArray.size(); i++)
            {
                ConfigManagerCore.staticLoadDimensions[i] = idArray.get(i);
            }

            String[] values = new String[ConfigManagerCore.staticLoadDimensions.length];
            Arrays.sort(ConfigManagerCore.staticLoadDimensions);

            for (int i = 0; i < values.length; i++)
            {
                values[i] = String.valueOf(ConfigManagerCore.staticLoadDimensions[i]);
            }

            Property prop = config.get(Constants.CONFIG_CATEGORY_DIMENSIONS, "Static Loaded Dimensions", ConfigManagerCore.staticLoadDimensions);
            prop.setComment(GCCoreUtil.translate("gc.configgui.static_loaded_dimensions.description"));
            prop.setLanguageKey("gc.configgui.static_loaded_dimensions");
            prop.set(values);

            ConfigManagerCore.config.save();
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

    public static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_DIFFICULTY)).getChildElements());
        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_GENERAL)).getChildElements());
        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_CLIENT)).getChildElements());
        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_CONTROLS)).getChildElements());
        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_COMPATIBILITY)).getChildElements());
        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_WORLDGEN)).getChildElements());
        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_SERVER)).getChildElements());
        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_DIMENSIONS)).getChildElements());
        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_SCHEMATIC)).getChildElements());
        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_ACHIEVEMENTS)).getChildElements());
        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_ENTITIES)).getChildElements());

        return list;
    }

    public static BlockTuple stringToBlock(String s, String caller, boolean logging)
    {
        int lastColon = s.lastIndexOf(':');
        int meta = -1;
        String name;

        if (lastColon > 0)
        {
            try
            {
                meta = Integer.parseInt(s.substring(lastColon + 1, s.length()));
            }
            catch (NumberFormatException ex)
            {
            }
        }

        if (meta == -1)
        {
            name = s;
        }
        else
        {
            name = s.substring(0, lastColon);
        }

        Block block = Block.getBlockFromName(name);
        if (block == null)
        {
            Item item = (Item) Item.REGISTRY.getObject(new ResourceLocation(name));
            if (item instanceof ItemBlock)
            {
                block = ((ItemBlock) item).getBlock();
            }
            if (block == null)
            {
                if (logging)
                {
                    GCLog.severe("[config] " + caller + ": unrecognised block name '" + s + "'.");
                }
                return null;
            }
        }
        try
        {
            Integer.parseInt(name);
            String bName = (String) Block.REGISTRY.getNameForObject(block).toString();
            if (logging)
            {
                GCLog.info("[config] " + caller + ": the use of numeric IDs is discouraged, please use " + bName + " instead of " + name);
            }
        }
        catch (NumberFormatException ex)
        {
        }
        if (Blocks.AIR == block)
        {
            if (logging)
            {
                GCLog.info("[config] " + caller + ": not a good idea to specify air, skipping that!");
            }
            return null;
        }

        return new BlockTuple(block, meta);
    }

    public static List<Object> getServerConfigOverride()
    {
    	ArrayList<Object> returnList = new ArrayList<>();
    	int modeFlags = ConfigManagerCore.hardMode ? 1 : 0;
    	modeFlags += ConfigManagerCore.quickMode ? 2 : 0;
    	modeFlags += ConfigManagerCore.challengeMode ? 4 : 0;
    	modeFlags += ConfigManagerCore.disableSpaceStationCreation ? 8 : 0;
    	modeFlags += ConfigManagerCore.recipesRequireGCAdvancedMetals ? 16 : 0;
    	modeFlags += ConfigManagerCore.challengeRecipes ? 32 : 0;
        modeFlags += ConfigManagerCore.allowLiquidGratings ? 64 : 0;
    	returnList.add(modeFlags);
    	returnList.add(ConfigManagerCore.dungeonBossHealthMod);
    	returnList.add(ConfigManagerCore.suffocationDamage);
    	returnList.add(ConfigManagerCore.suffocationCooldown);
    	returnList.add(ConfigManagerCore.rocketFuelFactor);
    	returnList.add(ConfigManagerCore.otherModsSilicon);
    	//If changing this, update definition of EnumSimplePacket.C_UPDATE_CONFIGS - see comment in setConfigOverride() below
    	EnergyConfigHandler.serverConfigOverride(returnList);
    	
    	returnList.add(ConfigManagerCore.detectableIDs.clone());  	
    	//TODO Should this include any other client-side configurables too?
    	return returnList;
    }

    @SideOnly(Side.CLIENT)
    public static void setConfigOverride(List<Object> configs)
    {
        int dataCount = 0;
    	int modeFlag = (Integer) configs.get(dataCount++);
    	ConfigManagerCore.hardMode = (modeFlag & 1) != 0;
    	ConfigManagerCore.quickMode = (modeFlag & 2) != 0;
    	ConfigManagerCore.challengeMode = (modeFlag & 4) != 0;
    	ConfigManagerCore.disableSpaceStationCreation = (modeFlag & 8) != 0;
    	ConfigManagerCore.recipesRequireGCAdvancedMetals = (modeFlag & 16) != 0;
    	ConfigManagerCore.challengeRecipes = (modeFlag & 32) != 0;
        ConfigManagerCore.allowLiquidGratings = (modeFlag & 64) != 0;
    	ConfigManagerCore.dungeonBossHealthMod = (Double) configs.get(dataCount++);
    	ConfigManagerCore.suffocationDamage = (Integer) configs.get(dataCount++);
    	ConfigManagerCore.suffocationCooldown = (Integer) configs.get(dataCount++);
    	ConfigManagerCore.rocketFuelFactor = (Integer) configs.get(dataCount++);
    	ConfigManagerCore.otherModsSilicon = (String) configs.get(dataCount++);
    	//If adding any additional data objects here, also remember to update the packet definition of EnumSimplePacket.C_UPDATE_CONFIGS in PacketSimple
    	//Current working packet definition: Integer.class, Double.class, Integer.class, Integer.class, Integer.class, String.class, Float.class, Float.class, Float.class, Float.class, Integer.class, String[].class
    	
    	EnergyConfigHandler.setConfigOverride((Float) configs.get(dataCount++), (Float) configs.get(dataCount++), (Float) configs.get(dataCount++), (Float) configs.get(dataCount++), (Integer) configs.get(dataCount++));
    	
    	int sizeIDs = configs.size() - dataCount;
    	if (sizeIDs > 0)
    	{
    	    Object dataLast = configs.get(dataCount); 
    		if (dataLast instanceof String)
    		{
    			ConfigManagerCore.detectableIDs = new String[sizeIDs];
		    	for (int j = 0; j < sizeIDs; j++)
		    	ConfigManagerCore.detectableIDs[j] = new String((String) configs.get(dataCount++));
    		}
    		else if (dataLast instanceof String[])
    		{
    			ConfigManagerCore.detectableIDs = ((String[])dataLast);
    		}
        	TickHandlerClient.registerDetectableBlocks(false);
    	}
    	
    	challengeModeUpdate();
    	RecipeManagerGC.setConfigurableRecipes();
    }

    public static void saveClientConfigOverrideable()
    {
        if (ConfigManagerCore.clientSave == null)
        {
            ConfigManagerCore.clientSave = (ArrayList<Object>) ConfigManagerCore.getServerConfigOverride();
        }
    }

    public static void restoreClientConfigOverrideable()
    {
        if (ConfigManagerCore.clientSave != null)
        {
            ConfigManagerCore.setConfigOverride(clientSave);
        }
    }

    private static int parseKeyValue(String key)
    {
        if (key.equals("KEY_A"))
        {
            return Keyboard.KEY_A;
        }
        else if (key.equals("KEY_B"))
        {
            return Keyboard.KEY_B;
        }
        else if (key.equals("KEY_C"))
        {
            return Keyboard.KEY_C;
        }
        else if (key.equals("KEY_D"))
        {
            return Keyboard.KEY_D;
        }
        else if (key.equals("KEY_E"))
        {
            return Keyboard.KEY_E;
        }
        else if (key.equals("KEY_F"))
        {
            return Keyboard.KEY_F;
        }
        else if (key.equals("KEY_G"))
        {
            return Keyboard.KEY_G;
        }
        else if (key.equals("KEY_H"))
        {
            return Keyboard.KEY_H;
        }
        else if (key.equals("KEY_I"))
        {
            return Keyboard.KEY_I;
        }
        else if (key.equals("KEY_J"))
        {
            return Keyboard.KEY_J;
        }
        else if (key.equals("KEY_K"))
        {
            return Keyboard.KEY_K;
        }
        else if (key.equals("KEY_L"))
        {
            return Keyboard.KEY_L;
        }
        else if (key.equals("KEY_M"))
        {
            return Keyboard.KEY_M;
        }
        else if (key.equals("KEY_N"))
        {
            return Keyboard.KEY_N;
        }
        else if (key.equals("KEY_O"))
        {
            return Keyboard.KEY_O;
        }
        else if (key.equals("KEY_P"))
        {
            return Keyboard.KEY_P;
        }
        else if (key.equals("KEY_Q"))
        {
            return Keyboard.KEY_Q;
        }
        else if (key.equals("KEY_R"))
        {
            return Keyboard.KEY_R;
        }
        else if (key.equals("KEY_S"))
        {
            return Keyboard.KEY_S;
        }
        else if (key.equals("KEY_T"))
        {
            return Keyboard.KEY_T;
        }
        else if (key.equals("KEY_U"))
        {
            return Keyboard.KEY_U;
        }
        else if (key.equals("KEY_V"))
        {
            return Keyboard.KEY_V;
        }
        else if (key.equals("KEY_W"))
        {
            return Keyboard.KEY_W;
        }
        else if (key.equals("KEY_X"))
        {
            return Keyboard.KEY_X;
        }
        else if (key.equals("KEY_Y"))
        {
            return Keyboard.KEY_Y;
        }
        else if (key.equals("KEY_Z"))
        {
            return Keyboard.KEY_Z;
        }
        else if (key.equals("KEY_1"))
        {
            return Keyboard.KEY_1;
        }
        else if (key.equals("KEY_2"))
        {
            return Keyboard.KEY_2;
        }
        else if (key.equals("KEY_3"))
        {
            return Keyboard.KEY_3;
        }
        else if (key.equals("KEY_4"))
        {
            return Keyboard.KEY_4;
        }
        else if (key.equals("KEY_5"))
        {
            return Keyboard.KEY_5;
        }
        else if (key.equals("KEY_6"))
        {
            return Keyboard.KEY_6;
        }
        else if (key.equals("KEY_7"))
        {
            return Keyboard.KEY_7;
        }
        else if (key.equals("KEY_8"))
        {
            return Keyboard.KEY_8;
        }
        else if (key.equals("KEY_9"))
        {
            return Keyboard.KEY_9;
        }
        else if (key.equals("KEY_0"))
        {
            return Keyboard.KEY_0;
        }

        GCLog.severe("Failed to parse keyboard key: " + key + "... Use values A-Z or 0-9");

        return 0;
    }
}
