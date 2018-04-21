package micdoodle8.mods.galacticraft.planets.deepspace;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.ConfigManagerMars;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLLog;

import org.apache.logging.log4j.Level;

import java.io.File;

public class ConfigManagerDeepSpace
{
    public static boolean loaded;

    static Configuration config;

    public ConfigManagerDeepSpace(File file)
    {
        if (!ConfigManagerDeepSpace.loaded)
        {
            if (file.exists())
            {
                ConfigManagerDeepSpace.config = new Configuration(file);
                ConfigManagerDeepSpace.syncConfig(true, true);
                file.delete();
                config = ConfigManagerMars.config;
            }
            else
            {
                config = ConfigManagerMars.config;
                ConfigManagerDeepSpace.syncConfig(true, false);
            }
        }
    }

    // DIMENSIONS
    public static int dimensionIDDeepSpace;

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

            prop = config.get(Constants.CONFIG_CATEGORY_DIMENSIONS, "dimensionIDDeepSpace", -32);
            prop.setComment("Dimension ID for Deep Space Station");
            prop.setLanguageKey("gc.configgui.dimension_id_deep_space").setRequiresMcRestart(true);
            if (update)
            {
                propCopy = ConfigManagerMars.config.get(Constants.CONFIG_CATEGORY_DIMENSIONS, prop.getName(), prop.getInt(), prop.getComment());
                propCopy.setLanguageKey(prop.getLanguageKey());
                propCopy.setRequiresMcRestart(prop.requiresMcRestart());
            }
            dimensionIDDeepSpace = prop.getInt();
            GalacticraftPlanets.finishProp(prop, Constants.CONFIG_CATEGORY_DIMENSIONS);
        }
        catch (final Exception e)
        {
            FMLLog.log(Level.ERROR, e, "Galacticraft Deep Space (Planets) has a problem loading its config");
        }
    }
}
