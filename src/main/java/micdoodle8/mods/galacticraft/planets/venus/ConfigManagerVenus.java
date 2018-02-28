package micdoodle8.mods.galacticraft.planets.venus;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.mars.ConfigManagerMars;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import java.io.File;

public class ConfigManagerVenus
{
    public static boolean loaded;

    static Configuration config;

    public ConfigManagerVenus(File file)
    {
        if (!ConfigManagerVenus.loaded)
        {
            if (file.exists())
            {
                ConfigManagerVenus.config = new Configuration(file);
                ConfigManagerVenus.syncConfig(true, true);
                file.delete();
                config = ConfigManagerMars.config;
            }
            else
            {
                config = ConfigManagerMars.config;
                ConfigManagerVenus.syncConfig(true, false);
            }
        }
    }

    // DIMENSIONS
    public static int dimensionIDVenus;

    public static boolean disableAmbientLightning;

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

            prop = config.get(Constants.CONFIG_CATEGORY_DIMENSIONS, "dimensionIDVenus", -31);
            prop.comment = "Dimension ID for Venus";
            prop.setLanguageKey("gc.configgui.dimension_id_venus").setRequiresMcRestart(true);
            if (update)
            {
                propCopy = ConfigManagerMars.config.get(Constants.CONFIG_CATEGORY_DIMENSIONS, prop.getName(), prop.getInt(), prop.comment);
                propCopy.setLanguageKey(prop.getLanguageKey());
                propCopy.setRequiresMcRestart(prop.requiresMcRestart());
            }
            dimensionIDVenus = prop.getInt();

            prop = config.get(Constants.CONFIG_CATEGORY_SCHEMATIC, "disableAmbientLightning", false);
            prop.comment = "Disables background thunder and lightning.";
            prop.setLanguageKey("gc.configgui.disable_ambient_lightning");
            disableAmbientLightning = prop.getBoolean(false);

            if (load)
            {
                ConfigManagerMars.config.setCategoryPropertyOrder(Constants.CONFIG_CATEGORY_WORLDGEN, ConfigManagerMars.propOrder);
            }

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
