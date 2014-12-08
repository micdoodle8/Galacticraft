package micdoodle8.mods.galacticraft.planets.mars;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;

public class ConfigManagerMars
{
    public static boolean loaded;

    static Configuration config;

    public ConfigManagerMars(File file)
    {
        if (!ConfigManagerMars.loaded)
        {
            ConfigManagerMars.config = new Configuration(file);
            ConfigManagerMars.syncConfig(true);
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

            prop = config.get(Constants.CONFIG_CATEGORY_DIMENSIONS, "dimensionIDMars", -29);
            prop.comment = "Dimension ID for Mars";
            prop.setLanguageKey("gc.configgui.dimensionIDMars").setRequiresMcRestart(true);
            dimensionIDMars = prop.getInt();
            propOrder.add(prop.getName());

            //

            prop = config.get(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicRocketT2", 2);
            prop.comment = "Schematic ID for Tier 2 Rocket, must be unique.";
            prop.setLanguageKey("gc.configgui.idSchematicRocketT2");
            idSchematicRocketT2 = prop.getInt(2);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicCargoRocket", 3);
            prop.comment = "Schematic ID for Cargo Rocket, must be unique.";
            prop.setLanguageKey("gc.configgui.idSchematicCargoRocket");
            idSchematicCargoRocket = prop.getInt(3);
            propOrder.add(prop.getName());

            //

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "launchControllerChunkLoad", true);
            prop.comment = "Whether or not the launch controller acts as a chunk loader. Will cause issues if disabled!";
            prop.setLanguageKey("gc.configgui.launchControllerChunkLoad");
            launchControllerChunkLoad = prop.getBoolean(true);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_GENERAL, "launchControllerAllDims", false);
            prop.comment = "May rarely cause issues if enabled, depends on how the other mod's dimensions are.";
            prop.setLanguageKey("gc.configgui.launchControllerAllDims");
            launchControllerAllDims = prop.getBoolean(false);
            propOrder.add(prop.getName());

            config.setCategoryPropertyOrder(CATEGORY_GENERAL, propOrder);

            if (config.hasChanged())
            {
                config.save();
            }
        }
        catch (final Exception e)
        {
            FMLLog.log(Level.ERROR, e, "Galacticraft Mars (Planets) has a problem loading it's config");
        }
    }
}
