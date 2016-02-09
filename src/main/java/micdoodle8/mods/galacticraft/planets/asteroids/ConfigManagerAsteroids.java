package micdoodle8.mods.galacticraft.planets.asteroids;

import cpw.mods.fml.common.FMLLog;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.mars.ConfigManagerMars;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
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
            prop.comment = "Dimension ID for Asteroids";
            prop.setLanguageKey("gc.configgui.dimensionIDAsteroids").setRequiresMcRestart(true);
            if (update)
            {
            	propCopy = ConfigManagerMars.config.get(Constants.CONFIG_CATEGORY_DIMENSIONS, prop.getName(), prop.getInt(), prop.comment);
            	propCopy.setLanguageKey(prop.getLanguageKey());
            	propCopy.setRequiresMcRestart(prop.requiresMcRestart());
            }
            dimensionIDAsteroids = prop.getInt();

            //

            prop = config.get(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicRocketT3", 4);
            prop.comment = "Schematic ID for Tier 3 Rocket, must be unique.";
            prop.setLanguageKey("gc.configgui.idSchematicRocketT3");
            if (update)
            {
            	propCopy = ConfigManagerMars.config.get(Constants.CONFIG_CATEGORY_SCHEMATIC, prop.getName(), prop.getInt(), prop.comment);
            	propCopy.setLanguageKey(prop.getLanguageKey());
            }
            idSchematicRocketT3 = prop.getInt(4);

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "disableGalacticraftHelium", false);
            prop.comment = "Option to disable Helium gas in Galacticraft (because it will be registered by another mod eg GregTech).";
            prop.setLanguageKey("gc.configgui.disableGalacticraftHelium");
            if (update)
            {
            	propCopy = ConfigManagerMars.config.get(Constants.CONFIG_CATEGORY_GENERAL, prop.getName(), prop.getBoolean(), prop.comment);
            	propCopy.setLanguageKey(prop.getLanguageKey());
            }
            disableGalacticraftHelium = prop.getBoolean(false);
            ConfigManagerMars.propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "maximumAstroMiners", 6);
            prop.comment = "Maximum number of Astro Miners each player is allowed to have active (default 4).";
            prop.setLanguageKey("gc.configgui.astroMinersMax");
            if (update)
            {
            	propCopy = ConfigManagerMars.config.get(Constants.CONFIG_CATEGORY_GENERAL, prop.getName(), prop.getInt(), prop.comment);
            	propCopy.setLanguageKey(prop.getLanguageKey());
            }
            astroMinerMax = prop.getInt(6);
            ConfigManagerMars.propOrder.add(prop.getName());

            prop = config.get(update ? Constants.CONFIG_CATEGORY_GENERAL : Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Iron Ore Gen on Asteroids", false);
            prop.comment = "Disable Iron Ore Gen on Asteroids.";
            prop.setLanguageKey("gc.configgui.disableIronGenAsteroids");
            if (update)
            {
            	propCopy = ConfigManagerMars.config.get(Constants.CONFIG_CATEGORY_WORLDGEN, prop.getName(), prop.getBoolean(), prop.comment);
            	propCopy.setLanguageKey(prop.getLanguageKey());
            }
            disableIronGen = prop.getBoolean(false);
            ConfigManagerMars.propOrder.add(prop.getName());

            prop = config.get(update ? Constants.CONFIG_CATEGORY_GENERAL : Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Aluminum Ore Gen on Asteroids", false);
            prop.comment = "Disable Aluminum Ore Gen on Asteroids.";
            prop.setLanguageKey("gc.configgui.disableAluminumGenAsteroids");
            if (update)
            {
            	propCopy = ConfigManagerMars.config.get(Constants.CONFIG_CATEGORY_WORLDGEN, prop.getName(), prop.getBoolean(), prop.comment);
            	propCopy.setLanguageKey(prop.getLanguageKey());
            }
            disableAluminumGen = prop.getBoolean(false);
            ConfigManagerMars.propOrder.add(prop.getName());

            prop = config.get(update ? Constants.CONFIG_CATEGORY_GENERAL : Constants.CONFIG_CATEGORY_WORLDGEN, "Disable Ilmenite Ore Gen on Asteroids", false);
            prop.comment = "Disable Ilmenite Ore Gen on Asteroids.";
            prop.setLanguageKey("gc.configgui.disableIlmeniteGenAsteroids");
            if (update)
            {
            	propCopy = ConfigManagerMars.config.get(Constants.CONFIG_CATEGORY_WORLDGEN, prop.getName(), prop.getBoolean(), prop.comment);
            	propCopy.setLanguageKey(prop.getLanguageKey());
            }
            disableIlmeniteGen = prop.getBoolean(false);
            ConfigManagerMars.propOrder.add(prop.getName());

            if (load) ConfigManagerMars.config.setCategoryPropertyOrder(Constants.CONFIG_CATEGORY_WORLDGEN, ConfigManagerMars.propOrder);

            //Always save - this is last to be called both at load time and at mid-game
            if (ConfigManagerMars.config.hasChanged())
            {
            	ConfigManagerMars.config.save();
            }
        }
        catch (final Exception e)
        {
            FMLLog.log(Level.ERROR, e, "Galacticraft Asteroids (Planets) has a problem loading it's config");
        }
    }
}
