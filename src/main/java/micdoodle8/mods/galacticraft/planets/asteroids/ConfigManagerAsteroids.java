package micdoodle8.mods.galacticraft.planets.asteroids;

import cpw.mods.fml.common.FMLLog;
import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

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

    // ENTITIES
    public static int idEntitySmallAsteroid;
    public static int idEntityGrappleHook;
    public static int idEntityTier3Rocket;
    public static int idEntityEntryPod;

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

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntitySmallAsteroid", 180);
            prop.comment = "Entity ID for Small Asteroid, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntitySmallAsteroid").setRequiresMcRestart(true);
            idEntitySmallAsteroid = prop.getInt(180);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityGrappleHook", 181);
            prop.comment = "Entity ID for Grapple Hook, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntityGrappleHook").setRequiresMcRestart(true);
            idEntityGrappleHook = prop.getInt(181);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityTier3Rocket", 182);
            prop.comment = "Entity ID for Tier 3 Rocket, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntityTier3Rocket").setRequiresMcRestart(true);
            idEntityTier3Rocket = prop.getInt(182);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityEntryPod", 183);
            prop.comment = "Entity ID for Cargo Rocket, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntityEntryPod").setRequiresMcRestart(true);
            idEntityEntryPod = prop.getInt(183);
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
