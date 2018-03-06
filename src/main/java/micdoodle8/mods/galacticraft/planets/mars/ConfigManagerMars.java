package micdoodle8.mods.galacticraft.planets.mars;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLLog;

import org.apache.logging.log4j.Level;

import java.io.File;

public class ConfigManagerMars
{
    public static boolean loaded;

    public static Configuration config;

    public ConfigManagerMars(File file, boolean update)
    {
        if (!ConfigManagerMars.loaded)
        {
            ConfigManagerMars.config = new Configuration(file);
            ConfigManagerMars.syncConfig(true, update);
        }
    }

    // DIMENSIONS
    public static int dimensionIDMars;

    // SCHEMATIC
    public static int idSchematicRocketT2;
    public static int idSchematicCargoRocket;

    // GENERAL
    public static boolean launchControllerChunkLoad;
    public static boolean launchControllerAllDims;

    // WGEN
    public static boolean disableDeshGen;
    public static boolean disableTinGen;
    public static boolean disableCopperGen;
    public static boolean disableIronGen;

    public static void syncConfig(boolean load, boolean update)
    {
        try
        {
            Property prop;

            if (!config.isChild && load)
            {
                config.load();
            }

            prop = config.get(Constants.CONFIG_CATEGORY_DIMENSIONS, "dimensionIDMars", -29);
            prop.comment = "Dimension ID for Mars";
            prop.setLanguageKey("gc.configgui.dimension_id_mars").setRequiresMcRestart(true);
            dimensionIDMars = prop.getInt();
            GalacticraftPlanets.finishProp(prop, Constants.CONFIG_CATEGORY_DIMENSIONS);

            //

            prop = config.get(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicRocketT2", 2);
            prop.comment = "Schematic ID for Tier 2 Rocket, must be unique.";
            prop.setLanguageKey("gc.configgui.id_schematic_rocket_t2");
            idSchematicRocketT2 = prop.getInt(2);
            GalacticraftPlanets.finishProp(prop, Constants.CONFIG_CATEGORY_SCHEMATIC);

            prop = config.get(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicCargoRocket", 3);
            prop.comment = "Schematic ID for Cargo Rocket, must be unique.";
            prop.setLanguageKey("gc.configgui.id_schematic_cargo_rocket");
            idSchematicCargoRocket = prop.getInt(3);
            GalacticraftPlanets.finishProp(prop, Constants.CONFIG_CATEGORY_SCHEMATIC);

            //

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "launchControllerChunkLoad", true);
            prop.comment = "Whether or not the launch controller acts as a chunk loader. Will cause issues if disabled!";
            prop.setLanguageKey("gc.configgui.launch_controller_chunk_load");
            launchControllerChunkLoad = prop.getBoolean(true);
            GalacticraftPlanets.finishProp(prop, Constants.CONFIG_CATEGORY_GENERAL);

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "launchControllerAllDims", false);
            prop.comment = "May rarely cause issues if enabled, depends on how the other mod's dimensions are.";
            prop.setLanguageKey("gc.configgui.launch_controller_all_dims");
            launchControllerAllDims = prop.getBoolean(false);
            GalacticraftPlanets.finishProp(prop, Constants.CONFIG_CATEGORY_GENERAL);

            prop = config.get(update ? Constants.CONFIG_CATEGORY_GENERAL : Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Iron Ore Gen on Mars", false);
            prop.comment = "Disable Iron Ore Gen on Mars.";
            prop.setLanguageKey("gc.configgui.disable_iron_gen_mars");
            if (update)
            {
                prop = config.get(Constants.CONFIG_CATEGORY_WORLDGEN, prop.getName(), prop.getBoolean(), prop.comment);
                prop.setLanguageKey(prop.getLanguageKey());
                config.getCategory(Constants.CONFIG_CATEGORY_GENERAL).remove(prop.getName());
            }
            disableIronGen = prop.getBoolean(false);
            GalacticraftPlanets.finishProp(prop, Constants.CONFIG_CATEGORY_WORLDGEN);

            prop = config.get(update ? Constants.CONFIG_CATEGORY_GENERAL : Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Copper Ore Gen on Mars", false);
            prop.comment = "Disable Copper Ore Gen on Mars.";
            prop.setLanguageKey("gc.configgui.disable_copper_gen_mars");
            if (update)
            {
                prop = config.get(Constants.CONFIG_CATEGORY_WORLDGEN, prop.getName(), prop.getBoolean(), prop.comment);
                prop.setLanguageKey(prop.getLanguageKey());
                config.getCategory(Constants.CONFIG_CATEGORY_GENERAL).remove(prop.getName());
            }
            disableCopperGen = prop.getBoolean(false);
            GalacticraftPlanets.finishProp(prop, Constants.CONFIG_CATEGORY_WORLDGEN);

            prop = config.get(update ? Constants.CONFIG_CATEGORY_GENERAL : Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Tin Ore Gen on Mars", false);
            prop.comment = "Disable Tin Ore Gen on Mars.";
            prop.setLanguageKey("gc.configgui.disable_tin_gen_mars");
            if (update)
            {
                prop = config.get(Constants.CONFIG_CATEGORY_WORLDGEN, prop.getName(), prop.getBoolean(), prop.comment);
                prop.setLanguageKey(prop.getLanguageKey());
                config.getCategory(Constants.CONFIG_CATEGORY_GENERAL).remove(prop.getName());
            }
            disableTinGen = prop.getBoolean(false);
            GalacticraftPlanets.finishProp(prop, Constants.CONFIG_CATEGORY_WORLDGEN);

            prop = config.get(update ? Constants.CONFIG_CATEGORY_GENERAL : Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Desh Ore Gen on Mars", false);
            prop.comment = "Disable Desh Ore Gen on Mars.";
            prop.setLanguageKey("gc.configgui.disable_desh_gen_mars");
            if (update)
            {
                prop = config.get(Constants.CONFIG_CATEGORY_WORLDGEN, prop.getName(), prop.getBoolean(), prop.comment);
                prop.setLanguageKey(prop.getLanguageKey());
                config.getCategory(Constants.CONFIG_CATEGORY_GENERAL).remove(prop.getName());
            }
            disableDeshGen = prop.getBoolean(false);
            GalacticraftPlanets.finishProp(prop, Constants.CONFIG_CATEGORY_WORLDGEN);
        }
        catch (final Exception e)
        {
            FMLLog.log(Level.ERROR, e, "Galacticraft Mars (Planets) has a problem loading its config");
        }
    }
}
