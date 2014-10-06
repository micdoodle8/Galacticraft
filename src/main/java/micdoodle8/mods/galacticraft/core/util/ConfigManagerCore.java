package micdoodle8.mods.galacticraft.core.util;

import com.google.common.primitives.Ints;

import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameData;
import micdoodle8.mods.galacticraft.api.vector.BlockTuple;
import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

public class ConfigManagerCore
{
    static Configuration config;

    public static int idDimensionOverworldOrbit;
    public static int idDimensionOverworldOrbitStatic;
    public static int idDimensionMoon;

    // SCHEMATICS
    public static int idSchematicRocketT1;
    public static int idSchematicMoonBuggy;
    public static int idSchematicAddSchematic;

    // ACHIEVEMENTS
    public static int idAchievBase;

    public static int idEntityEvolvedSpider;
    public static int idEntityEvolvedCreeper;
    public static int idEntityEvolvedZombie;
    public static int idEntityEvolvedSkeleton;
    public static int idEntityEvolvedSkeletonBoss;
    public static int idEntitySpaceship;
    public static int idEntityMeteor;
    public static int idEntityBuggy;
    public static int idEntityFlag;
    public static int idEntityParaChest;
    public static int idEntityAlienVillager;
    public static int idEntityOxygenBubble;
    public static int idEntityLander;
    public static int idEntityMeteorChunk;
	public static int idEntityCelestial;

    // GENERAL
    public static boolean moreStars;
    public static String[] sealableIDs = { };
    public static String[] detectableIDs = { };
    public static boolean disableSpaceshipParticles;
    public static boolean disableSpaceshipGrief;
    public static boolean oxygenIndicatorLeft;
    public static boolean oxygenIndicatorBottom;
    public static double oilGenFactor;
    public static boolean spaceStationsRequirePermission;
    public static boolean disableSpaceStationCreation;
    public static boolean overrideCapes;
    public static double spaceStationEnergyScalar;
    public static boolean disableLander;
    public static double dungeonBossHealthMod;
    public static boolean hardMode;
    public static int suffocationCooldown;
    public static int suffocationDamage;
    public static int[] externalOilGen;
    public static boolean forceOverworldRespawn;
    public static boolean enableDebug;
    public static boolean enableCopperOreGen;
    public static boolean enableTinOreGen;
    public static boolean enableAluminumOreGen;
    public static boolean enableSiliconOreGen;
    public static int[] staticLoadDimensions = { };
    public static int[] disableRocketLaunchDimensions = { -1, 1 };
    public static boolean disableCheeseMoon;
    public static boolean disableTinMoon;
    public static boolean disableCopperMoon;
    public static boolean disableMoonVillageGen;
    public static boolean enableOtherModsFeatures;
    public static boolean enableSealerEdgeChecks;
    public static boolean alternateCanisterRecipe;
    public static boolean disableRocketsToOverworld;
    public static int rocketFuelFactor;

    public static void initialize(File file)
    {
        ConfigManagerCore.config = new Configuration(file);
        ConfigManagerCore.syncConfig(true);
    }

    public static void syncConfig(boolean load)
    {
        List<String> propOrder = new ArrayList<String>();

        try
        {
            Property prop;

            if (!config.isChild)
            {
                if (load)
                {
                    config.load();
                }
            }

            prop = config.get(Constants.CONFIG_CATEGORY_DIMENSIONS, "idDimensionMoon", -28);
            prop.comment = "Dimension ID for the Moon";
            prop.setLanguageKey("gc.configgui.idDimensionMoon").setRequiresMcRestart(true);
            idDimensionMoon = prop.getInt();
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_DIMENSIONS, "idDimensionOverworldOrbit", -27);
            prop.comment = "Dimension ID for Overworld Space Stations";
            prop.setLanguageKey("gc.configgui.idDimensionOverworldOrbit").setRequiresMcRestart(true);
            idDimensionOverworldOrbit = prop.getInt();
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_DIMENSIONS, "idDimensionOverworldOrbitStatic", -26);
            prop.comment = "Dimension ID for Static Overworld Space Stations";
            prop.setLanguageKey("gc.configgui.idDimensionOverworldOrbitStatic").setRequiresMcRestart(true);
            idDimensionOverworldOrbitStatic = prop.getInt();
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_DIMENSIONS, "Static Loaded Dimensions", ConfigManagerCore.staticLoadDimensions);
            prop.comment = "IDs to load at startup, and keep loaded until server stops. Can be added via /gckeeploaded";
            prop.setLanguageKey("gc.configgui.staticLoadedDimensions");
            staticLoadDimensions = prop.getIntList();
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_DIMENSIONS, "Dimensions where rockets cannot launch", ConfigManagerCore.disableRocketLaunchDimensions);
            prop.comment = "IDs of dimensions where rockets should not launch - this should always include the Nether.";
            prop.setLanguageKey("gc.configgui.rocketDisabledDimensions");
            disableRocketLaunchDimensions = prop.getIntList();
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_DIMENSIONS, "Disable rockets from returning to Overworld", false);
            prop.comment = "If true, rockets will be unable to reach the Overworld (only use this in special modpacks!)";
            prop.setLanguageKey("gc.configgui.rocketDisableOverworldReturn");
            disableRocketsToOverworld = prop.getBoolean(false);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Force Overworld Spawn", false);
            prop.comment = "By default, you will respawn on galacticraft dimensions if you die. If you set this to true, you will respawn back on earth.";
            prop.setLanguageKey("gc.configgui.forceOverworldRespawn");
            forceOverworldRespawn = prop.getBoolean(false);
            propOrder.add(prop.getName());

            //

            prop = config.get(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicRocketT1", 0);
            prop.comment = "Schematic ID for Tier 1 Rocket, must be unique.";
            prop.setLanguageKey("gc.configgui.idSchematicRocketT1");
            idSchematicRocketT1 = prop.getInt(0);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicMoonBuggy", 1);
            prop.comment = "Schematic ID for Moon Buggy, must be unique.";
            prop.setLanguageKey("gc.configgui.idSchematicMoonBuggy");
            idSchematicMoonBuggy = prop.getInt(1);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicAddSchematic", Integer.MAX_VALUE);
            prop.comment = "Schematic ID for \"Add Schematic\" Page, must be unique";
            prop.setLanguageKey("gc.configgui.idSchematicAddSchematic");
            idSchematicAddSchematic = prop.getInt(Integer.MAX_VALUE);
            propOrder.add(prop.getName());

            //

            prop = config.get(Constants.CONFIG_CATEGORY_ACHIEVEMENTS, "idAchievBase", 1784);
            prop.comment = "Base Achievement ID. All achievement IDs will start at this number.";
            prop.setLanguageKey("gc.configgui.idAchievBase");
            idAchievBase = prop.getInt(1784);
            propOrder.add(prop.getName());

            //

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityEvolvedSpider", 155);
            prop.comment = "Entity ID for Evolved Spider, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntityEvolvedSpider").setRequiresMcRestart(true);
            idEntityEvolvedSpider = prop.getInt(155);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityEvolvedCreeper", 156);
            prop.comment = "Entity ID for Evolved Creeper, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntityEvolvedCreeper").setRequiresMcRestart(true);
            idEntityEvolvedCreeper = prop.getInt(156);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityEvolvedZombie", 157);
            prop.comment = "Entity ID for Evolved Zombie, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntityEvolvedZombie").setRequiresMcRestart(true);
            idEntityEvolvedZombie = prop.getInt(157);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityEvolvedSkeleton", 158);
            prop.comment = "Entity ID for Evolved Skeleton, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntityEvolvedSkeleton").setRequiresMcRestart(true);
            idEntityEvolvedSkeleton = prop.getInt(158);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntitySpaceship", 159);
            prop.comment = "Entity ID for Tier 1 Rocket, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntitySpaceship").setRequiresMcRestart(true);
            idEntitySpaceship = prop.getInt(159);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityMeteor", 161);
            prop.comment = "Entity ID for Meteor, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntityMeteor").setRequiresMcRestart(true);
            idEntityMeteor = prop.getInt(161);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityBuggy", 162);
            prop.comment = "Entity ID for Buggy, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntityBuggy").setRequiresMcRestart(true);
            idEntityBuggy = prop.getInt(162);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityFlag", 163);
            prop.comment = "Entity ID for Flag Entity, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntityFlag").setRequiresMcRestart(true);
            idEntityFlag = prop.getInt(163);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityParaChest", 165);
            prop.comment = "Entity ID for Parachest, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntityParaChest").setRequiresMcRestart(true);
            idEntityParaChest = prop.getInt(165);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityAlienVillager", 166);
            prop.comment = "Entity ID for Alien Villager, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntityAlienVillager").setRequiresMcRestart(true);
            idEntityAlienVillager = prop.getInt(166);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityOxygenBubble", 167);
            prop.comment = "Entity ID for Oxygen Bubble, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntityOxygenBubble").setRequiresMcRestart(true);
            idEntityOxygenBubble = prop.getInt(167);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityLander", 168);
            prop.comment = "Entity ID for Moon Lander, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntityLander").setRequiresMcRestart(true);
            idEntityLander = prop.getInt(168);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityEvolvedSkeletonBoss", 170);
            prop.comment = "Entity ID for Skeleton Boss, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntityEvolvedSkeletonBoss").setRequiresMcRestart(true);
            idEntityEvolvedSkeletonBoss = prop.getInt(170);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityCelestialScreen", 184);
            prop.comment = "Entity ID for (hidden) Celestial Selection Entity, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntityCelestialScreen").setRequiresMcRestart(true);
            idEntityCelestial = prop.getInt(184);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityMeteorChunk", 179);
            prop.comment = "Entity ID for Throwable Meteor Chunk, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntityMeteorChunk").setRequiresMcRestart(true);
            idEntityMeteorChunk = prop.getInt(179);
            propOrder.add(prop.getName());

//Client side

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "More Stars", true);
            prop.comment = "Setting this to false will revert night skies back to default minecraft star count";
            prop.setLanguageKey("gc.configgui.moreStars");
            moreStars = prop.getBoolean(true);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Disable Spaceship Particles", false);
            prop.comment = "If you have FPS problems, setting this to true will help if rocket particles are in your sights";
            prop.setLanguageKey("gc.configgui.disableSpaceshipParticles");
            disableSpaceshipParticles = prop.getBoolean(false);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Minimap Left", false);
            prop.comment = "If true, this will move the Oxygen Indicator to the left side. You can combine this with \"Minimap Bottom\"";
            prop.setLanguageKey("gc.configgui.oxygenIndicatorLeft");
            oxygenIndicatorLeft = prop.getBoolean(false);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Minimap Bottom", false);
            prop.comment = "If true, this will move the Oxygen Indicator to the bottom. You can combine this with \"Minimap Left\"";
            prop.setLanguageKey("gc.configgui.oxygenIndicatorBottom");
            oxygenIndicatorBottom = prop.getBoolean(false);
            propOrder.add(prop.getName());

//World gen

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Oil Generation Factor", 1.8);
            prop.comment = "Increasing this will increase amount of oil that will generate in each chunk.";
            prop.setLanguageKey("gc.configgui.oilGenFactor");
            oilGenFactor = prop.getDouble(1.8);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Oil gen in external dimensions", new int[] { 0 });
            prop.comment = "List of non-galacticraft dimension IDs to generate oil in.";
            prop.setLanguageKey("gc.configgui.externalOilGen");
            externalOilGen = prop.getIntList();
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Enable Copper Ore Gen", true);
            prop.comment = "If this is enabled, copper ore will generate on the overworld.";
            prop.setLanguageKey("gc.configgui.enableCopperOreGen").setRequiresMcRestart(true);
            enableCopperOreGen = prop.getBoolean(true);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Enable Tin Ore Gen", true);
            prop.comment = "If this is enabled, tin ore will generate on the overworld.";
            prop.setLanguageKey("gc.configgui.enableTinOreGen").setRequiresMcRestart(true);
            enableTinOreGen = prop.getBoolean(true);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Enable Aluminum Ore Gen", true);
            prop.comment = "If this is enabled, aluminum ore will generate on the overworld.";
            prop.setLanguageKey("gc.configgui.enableAluminumOreGen").setRequiresMcRestart(true);
            enableAluminumOreGen = prop.getBoolean(true);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Enable Silicon Ore Gen", true);
            prop.comment = "If this is enabled, silicon ore will generate on the overworld.";
            prop.setLanguageKey("gc.configgui.enableSiliconOreGen").setRequiresMcRestart(true);
            enableSiliconOreGen = prop.getBoolean(true);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Disable Cheese Ore Gen on Moon", false);
            prop.comment = "Disable Cheese Ore Gen on Moon.";
            prop.setLanguageKey("gc.configgui.disableCheeseMoon");
            disableCheeseMoon = prop.getBoolean(false);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Disable Tin Ore Gen on Moon", false);
            prop.comment = "Disable Tin Ore Gen on Moon.";
            prop.setLanguageKey("gc.configgui.disableTinMoon");
            disableTinMoon = prop.getBoolean(false);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Disable Copper Ore Gen on Moon", false);
            prop.comment = "Disable Tin Ore Gen on Moon.";
            prop.setLanguageKey("gc.configgui.disableCopperMoon");
            disableCopperMoon = prop.getBoolean(false);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Disable Moon Village Gen", false);
            prop.comment = "If true, moon villages will not generate.";
            prop.setLanguageKey("gc.configgui.disableMoonVillageGen");
            disableMoonVillageGen = prop.getBoolean(false);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Generate other mods features on planets", false);
            prop.comment = "If this is enabled, other mods' ores and all other features (eg. plants) can generate on the Moon and planets.";
            prop.setLanguageKey("gc.configgui.enableOtherModsFeatures");
            enableOtherModsFeatures = prop.getBoolean(false);
            propOrder.add(prop.getName());

//Debug

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Enable Debug Messages", false);
            prop.comment = "If this is enabled, debug messages will appear in the console. This is useful for finding bugs in the mod.";
            prop.setLanguageKey("gc.configgui.enableDebug");
            enableDebug = prop.getBoolean(false);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Disable lander on Moon and other planets", false);
            prop.comment = "If this is true, the player will parachute onto the Moon instead - use only in debug situations.";
            prop.setLanguageKey("gc.configgui.disableLander");
            disableLander = prop.getBoolean(false);
            propOrder.add(prop.getName());

//Server side

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Disable Spaceship Explosion", false);
            prop.comment = "Spaceships will not explode on contact if set to true.";
            prop.setLanguageKey("gc.configgui.disableSpaceshipGrief");
            disableSpaceshipGrief = prop.getBoolean(false);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Space Stations Require Permission", true);
            prop.comment = "While true, space stations require you to invite other players using /ssinvite <playername>";
            prop.setLanguageKey("gc.configgui.spaceStationsRequirePermission");
            spaceStationsRequirePermission = prop.getBoolean(true);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Disable Space Station creation", false);
            prop.comment = "If set to true on a server, players will be completely unable to create space stations.";
            prop.setLanguageKey("gc.configgui.disableSpaceStationCreation");
            disableSpaceStationCreation = prop.getBoolean(false);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Override Capes", true);
            prop.comment = "By default, Galacticraft will override capes with the mod's donor cape. Set to false to disable.";
            prop.setLanguageKey("gc.configgui.overrideCapes");
            overrideCapes = prop.getBoolean(true);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Space Station Solar Energy Multiplier", 2.0);
            prop.comment = "Solar panels will work (default 2x) more effective on space stations.";
            prop.setLanguageKey("gc.configgui.spaceStationEnergyScalar");
            spaceStationEnergyScalar = prop.getDouble(2.0);
            propOrder.add(prop.getName());

            try
            {
                prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "External Sealable IDs", new String[] { GameData.getBlockRegistry().getNameForObject(Blocks.glass_pane) + ":0" });
                prop.comment = "List non-opaque blocks from other mods (for example, special types of glass) that the Oxygen Sealer should recognize as solid seals. Format is BlockName or BlockName:metadata";
                prop.setLanguageKey("gc.configgui.sealableIDs").setRequiresMcRestart(true);
                sealableIDs = prop.getStringList();
                propOrder.add(prop.getName());
            }
            catch (Exception e)
            {
                FMLLog.severe("[Galacticraft] It appears you have installed the 'Dev' version of Galacticraft instead of the regular version (or vice versa).  Please re-install.");
            }

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "External Detectable IDs", new String[] { GameData.getBlockRegistry().getNameForObject(Blocks.coal_ore), GameData.getBlockRegistry().getNameForObject(Blocks.diamond_ore), GameData.getBlockRegistry().getNameForObject(Blocks.gold_ore), GameData.getBlockRegistry().getNameForObject(Blocks.iron_ore), GameData.getBlockRegistry().getNameForObject(Blocks.lapis_ore), GameData.getBlockRegistry().getNameForObject(Blocks.redstone_ore), GameData.getBlockRegistry().getNameForObject(Blocks.lit_redstone_ore) });
            prop.comment = "List blocks from other mods that the Sensor Glasses should recognize as solid blocks. Format is BlockName or BlockName:metadata.";
            prop.setLanguageKey("gc.configgui.detectableIDs").setRequiresMcRestart(true);
            detectableIDs = prop.getStringList();
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Suffocation Cooldown", 100);
            prop.comment = "Lower/Raise this value to change time between suffocation damage ticks";
            prop.setLanguageKey("gc.configgui.suffocationCooldown");
            suffocationCooldown = prop.getInt(100);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Suffocation Damage", 2);
            prop.comment = "Change this value to modify the damage taken per suffocation tick";
            prop.setLanguageKey("gc.configgui.suffocationDamage");
            suffocationDamage = prop.getInt(2);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Dungeon Boss Health Modifier", 1.0);
            prop.comment = "Change this if you wish to balance the mod (if you have more powerful weapon mods).";
            prop.setLanguageKey("gc.configgui.dungeonBossHealthMod");
            dungeonBossHealthMod = prop.getDouble(1.0);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Harder Difficulty", false);
            prop.comment = "Set this to true for increased difficulty in modpacks (see forum for more info).";
            prop.setLanguageKey("gc.configgui.hardMode");
            hardMode = prop.getBoolean(false);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Enable Sealed edge checks", true);
            prop.comment = "If this is enabled, areas sealed by Oxygen Sealers will run a seal check when the player breaks or places a block (or on block updates).  This should be enabled for a 100% accurate sealed status, but can be disabled on servers for performance reasons.";
            prop.setLanguageKey("gc.configgui.enableSealerEdgeChecks");
            enableSealerEdgeChecks = prop.getBoolean(true);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Alternate recipe for canisters", false);
            prop.comment = "Enable this if the standard canister recipe causes a conflict.";
            prop.setLanguageKey("gc.configgui.alternateCanisterRecipe").setRequiresMcRestart(true);
            alternateCanisterRecipe = prop.getBoolean(false);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "Rocket fuel factor", 1);
            prop.comment = "The normal factor is 1.  Increase this to 2 - 5 if other mods with a lot of oil (e.g. BuildCraft) are installed to increase GC rocket fuel requirement.";
            prop.setLanguageKey("gc.configgui.rocketFuelFactor");
            rocketFuelFactor = prop.getInt(1);
            propOrder.add(prop.getName());

            config.setCategoryPropertyOrder(CATEGORY_GENERAL, propOrder);

            if (config.hasChanged())
            {
                config.save();
            }
        }
        catch (final Exception e)
        {
            GCLog.severe("Problem loading core config (\"core.conf\")");
        }
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
            prop.comment = "IDs to load at startup, and keep loaded until server stops. Can be added via /gckeeploaded";
            prop.setLanguageKey("gc.configgui.staticLoadedDimensions");
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
            prop.comment = "IDs to load at startup, and keep loaded until server stops. Can be added via /gckeeploaded";
            prop.setLanguageKey("gc.configgui.staticLoadedDimensions");
            prop.set(values);

            ConfigManagerCore.config.save();
        }

        return foundCount > 0;
    }

    public static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_DIMENSIONS)).getChildElements());
        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_SCHEMATIC)).getChildElements());
        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_ACHIEVEMENTS)).getChildElements());
        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_ENTITIES)).getChildElements());
        list.addAll(new ConfigElement(config.getCategory(Constants.CONFIG_CATEGORY_GENERAL)).getChildElements());
        return list;
    }
    
    public static BlockTuple stringToBlock(String s, String caller)
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
            catch (NumberFormatException ex) { }
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
            GCLog.severe("[config] " + caller + ": unrecognised block name '" + s + "'.");
            return null;
        }
        try
        {
            Integer.parseInt(name);
            String bName = GameData.getBlockRegistry().getNameForObject(block);
            GCLog.info("[config] " + caller + ": the use of numeric IDs is discouraged, please use " + bName + " instead of " + name);
        }
        catch (NumberFormatException ex) { }
        if (Blocks.air == block)
        {
            GCLog.info("[config] " + caller + ": not a good idea to specify air, skipping that!");
            return null;
        }
   	
    	return new BlockTuple(block, meta);
    }
}
