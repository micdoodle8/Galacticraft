package micdoodle8.mods.galacticraft.planets.asteroids;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;

public class ConfigManagerAsteroids
{
    public static boolean loaded;

    static Configuration config;

    public ConfigManagerAsteroids(File file)
    {
        if (!ConfigManagerAsteroids.loaded)
        {
            ConfigManagerAsteroids.config = new Configuration(file);
            ConfigManagerAsteroids.syncConfig(true);
        }
    }

    // DIMENSIONS
    public static int dimensionIDAsteroids;

    // GUI

    // SCHEMATIC
    public static int idSchematicRocketT3;

    // GENERAL

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

            prop = config.get(Constants.CONFIG_CATEGORY_DIMENSIONS, "dimensionIDAsteroids", -30);
            prop.comment = "Dimension ID for Asteroids";
            prop.setLanguageKey("gc.configgui.dimensionIDAsteroids").setRequiresMcRestart(true);
            dimensionIDAsteroids = prop.getInt();
            propOrder.add(prop.getName());

            //

            prop = config.get(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicRocketT3", 4);
            prop.comment = "Schematic ID for Tier 3 Rocket, must be unique.";
            prop.setLanguageKey("gc.configgui.idSchematicRocketT3");
            idSchematicRocketT3 = prop.getInt(4);
            propOrder.add(prop.getName());

            config.setCategoryPropertyOrder(CATEGORY_GENERAL, propOrder);

            if (config.hasChanged())
            {
                config.save();
            }
        }
        catch (final Exception e)
        {
            FMLLog.log(Level.ERROR, e, "Galacticraft Asteroids (Planets) has a problem loading it's config");
        }
    }
}
