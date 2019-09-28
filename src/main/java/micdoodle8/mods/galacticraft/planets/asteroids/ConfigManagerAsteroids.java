package micdoodle8.mods.galacticraft.planets.asteroids;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.ConfigManagerMars;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLLog;

import org.apache.logging.log4j.Level;

import java.io.File;

public class ConfigManagerAsteroids
{
    public static boolean loaded;

    static Configuration config;

    public ConfigManagerAsteroids(File file)
    {
        if (!ConfigManagerAsteroids.loaded)
        {
            if (file.exists())
            {
                ConfigManagerAsteroids.config = new Configuration(file);
                ConfigManagerAsteroids.syncConfig(true, true);
                file.delete();
                config = ConfigManagerMars.config;
            }
            else
            {
                config = ConfigManagerMars.config;
                ConfigManagerAsteroids.syncConfig(true, false);
            }
        }
    }

    // DIMENSIONS
    public static int dimensionIDAsteroids;

    // GUI

    // SCHEMATIC
    public static int idSchematicRocketT3;

    // GENERAL
    public static boolean disableGalacticraftHelium;
    public static boolean disableSmallAsteroids;
    public static int astroMinerMax;

    public static boolean disableIlmeniteGen;
    public static boolean disableIronGen;
    public static boolean disableAluminumGen;

    public static void syncConfig(boolean load, boolean update)
    {
        try
        {
            Property prop;
            Property propCopy;

            if (!config.isChild)
            {
                if (update)
                {
                    config.load();
                }
            }

            prop = config.get(Constants.CONFIG_CATEGORY_DIMENSIONS, "dimensionIDAsteroids", -30);
            prop.setComment("Dimension ID for Asteroids");
            prop.setLanguageKey("gc.configgui.dimension_id_asteroids").setRequiresMcRestart(true);
            if (update)
            {
                propCopy = ConfigManagerMars.config.get(Constants.CONFIG_CATEGORY_DIMENSIONS, prop.getName(), prop.getInt(), prop.getComment());
                propCopy.setLanguageKey(prop.getLanguageKey());
                propCopy.setRequiresMcRestart(prop.requiresMcRestart());
            }
            dimensionIDAsteroids = prop.getInt();
            GalacticraftPlanets.finishProp(prop, Constants.CONFIG_CATEGORY_DIMENSIONS);

            //

            prop = config.get(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicRocketT3", 4);
            prop.setComment("Schematic ID for Tier 3 Rocket, must be unique.");
            prop.setLanguageKey("gc.configgui.id_schematic_rocket_t3");
            if (update)
            {
                propCopy = ConfigManagerMars.config.get(Constants.CONFIG_CATEGORY_SCHEMATIC, prop.getName(), prop.getInt(), prop.getComment());
                propCopy.setLanguageKey(prop.getLanguageKey());
            }
            idSchematicRocketT3 = prop.getInt(4);
            GalacticraftPlanets.finishProp(prop, Constants.CONFIG_CATEGORY_SCHEMATIC);

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "disableGalacticraftHelium", false);
            prop.setComment("Option to disable Helium gas in Galacticraft (because it will be registered by another mod eg GregTech).");
            prop.setLanguageKey("gc.configgui.disable_galacticraft_helium");
            if (update)
            {
                propCopy = ConfigManagerMars.config.get(Constants.CONFIG_CATEGORY_GENERAL, prop.getName(), prop.getBoolean(), prop.getComment());
                propCopy.setLanguageKey(prop.getLanguageKey());
            }
            disableGalacticraftHelium = prop.getBoolean(false);
            GalacticraftPlanets.finishProp(prop, Constants.CONFIG_CATEGORY_GENERAL);

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "maximumAstroMiners", 6);
            prop.setComment("Maximum number of Astro Miners each player is allowed to have active (default 6).");
            prop.setLanguageKey("gc.configgui.astro_miners_max");
            if (update)
            {
                propCopy = ConfigManagerMars.config.get(Constants.CONFIG_CATEGORY_GENERAL, prop.getName(), prop.getInt(), prop.getComment());
                propCopy.setLanguageKey(prop.getLanguageKey());
            }
            astroMinerMax = prop.getInt(6);
            GalacticraftPlanets.finishProp(prop, Constants.CONFIG_CATEGORY_GENERAL);

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "disableSmallAsteroids", false);
            prop.setComment("Option to disable small asteroids from spawning in the Asteroids Dimension.");
            prop.setLanguageKey("gc.configgui.disable_small_asteroids");
            if (update)
            {
                propCopy = ConfigManagerMars.config.get(Constants.CONFIG_CATEGORY_GENERAL, prop.getName(), prop.getBoolean(), prop.getComment());
                propCopy.setLanguageKey(prop.getLanguageKey());
            }
            disableSmallAsteroids = prop.getBoolean(false);
            GalacticraftPlanets.finishProp(prop, Constants.CONFIG_CATEGORY_GENERAL);

            prop = config.get(update ? Constants.CONFIG_CATEGORY_GENERAL : Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Iron Ore Gen on Asteroids", false);
            prop.setComment("Disable Iron Ore Gen on Asteroids.");
            prop.setLanguageKey("gc.configgui.disable_iron_gen_asteroids");
            if (update)
            {
                propCopy = ConfigManagerMars.config.get(Constants.CONFIG_CATEGORY_WORLDGEN, prop.getName(), prop.getBoolean(), prop.getComment());
                propCopy.setLanguageKey(prop.getLanguageKey());
            }
            disableIronGen = prop.getBoolean(false);
            GalacticraftPlanets.finishProp(prop, Constants.CONFIG_CATEGORY_WORLDGEN);

            prop = config.get(update ? Constants.CONFIG_CATEGORY_GENERAL : Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Aluminum Ore Gen on Asteroids", false);
            prop.setComment("Disable Aluminum Ore Gen on Asteroids.");
            prop.setLanguageKey("gc.configgui.disable_aluminum_gen_asteroids");
            if (update)
            {
                propCopy = ConfigManagerMars.config.get(Constants.CONFIG_CATEGORY_WORLDGEN, prop.getName(), prop.getBoolean(), prop.getComment());
                propCopy.setLanguageKey(prop.getLanguageKey());
            }
            disableAluminumGen = prop.getBoolean(false);
            GalacticraftPlanets.finishProp(prop, Constants.CONFIG_CATEGORY_WORLDGEN);

            prop = config.get(update ? Constants.CONFIG_CATEGORY_GENERAL : Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Ilmenite Ore Gen on Asteroids", false);
            prop.setComment("Disable Ilmenite Ore Gen on Asteroids.");
            prop.setLanguageKey("gc.configgui.disable_ilmenite_gen_asteroids");
            if (update)
            {
                propCopy = ConfigManagerMars.config.get(Constants.CONFIG_CATEGORY_WORLDGEN, prop.getName(), prop.getBoolean(), prop.getComment());
                propCopy.setLanguageKey(prop.getLanguageKey());
            }
            disableIlmeniteGen = prop.getBoolean(false);
            GalacticraftPlanets.finishProp(prop, Constants.CONFIG_CATEGORY_WORLDGEN);
        }
        catch (final Exception e)
        {
            FMLLog.log(Level.ERROR, e, "Galacticraft Asteroids (Planets) has a problem loading its config");
        }
    }
}
