package micdoodle8.mods.galacticraft.planets.mars;

import cpw.mods.fml.common.FMLLog;
import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

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

    // ENTITIES
    public static int idEntityCreeperBoss;
    public static int idEntityProjectileTNT;
    public static int idEntitySpaceshipTier2;
    public static int idEntitySludgeling;
    public static int idEntitySlimeling;
    public static int idEntityTerraformBubble;
    public static int idEntityLandingBalloons;
    public static int idEntityCargoRocket;

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

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityCreeperBoss", 171);
            prop.comment = "Entity ID for Evolved Creeper Boss, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntityCreeperBoss").setRequiresMcRestart(true);
            idEntityCreeperBoss = prop.getInt(171);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityProjectileTNT", 172);
            prop.comment = "Entity ID for Projectile TNT, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntityProjectileTNT").setRequiresMcRestart(true);
            idEntityProjectileTNT = prop.getInt(172);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntitySpaceshipTier2", 173);
            prop.comment = "Entity ID for Tier 2 Rocket, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntitySpaceshipTier2").setRequiresMcRestart(true);
            idEntitySpaceshipTier2 = prop.getInt(173);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntitySludgeling", 174);
            prop.comment = "Entity ID for Sludgeling, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntitySludgeling").setRequiresMcRestart(true);
            idEntitySludgeling = prop.getInt(174);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntitySlimeling", 175);
            prop.comment = "Entity ID for Slimeling, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntitySlimeling").setRequiresMcRestart(true);
            idEntitySlimeling = prop.getInt(175);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityTerraformBubble", 176);
            prop.comment = "Entity ID for Terraform Bubble, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntityTerraformBubble").setRequiresMcRestart(true);
            idEntityTerraformBubble = prop.getInt(176);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityLandingBalloons", 177);
            prop.comment = "Entity ID for Landing Balloons, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntityLandingBalloons").setRequiresMcRestart(true);
            idEntityLandingBalloons = prop.getInt(177);
            propOrder.add(prop.getName());

            prop = config.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityCargoRocket", 178);
            prop.comment = "Entity ID for Cargo Rocket, must be unique.";
            prop.setLanguageKey("gc.configgui.idEntityCargoRocket").setRequiresMcRestart(true);
            idEntityCargoRocket = prop.getInt(178);
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
