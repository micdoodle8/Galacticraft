package micdoodle8.mods.galacticraft.planets.mars;

import cpw.mods.fml.common.FMLLog;
import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

import java.io.File;

public class ConfigManagerMars
{
    public static boolean loaded;

    static Configuration configuration;

    public ConfigManagerMars(File file)
    {
        if (!ConfigManagerMars.loaded)
        {
            ConfigManagerMars.configuration = new Configuration(file);
            this.setDefaultValues();
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
    public static boolean generateOtherMods;
    public static boolean launchControllerChunkLoad;

    private void setDefaultValues()
    {
        ConfigManagerMars.configuration.load();
        ConfigManagerMars.syncConfig();
    }

    public static void syncConfig()
    {
        try
        {
            ConfigManagerMars.dimensionIDMars = ConfigManagerMars.configuration.get(Constants.CONFIG_CATEGORY_DIMENSIONS, "Mars Dimension ID", -29).getInt(-29);

            ConfigManagerMars.idEntityCreeperBoss = ConfigManagerMars.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityCreeperBoss", 171).getInt(171);
            ConfigManagerMars.idEntityProjectileTNT = ConfigManagerMars.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityProjectileTNT", 172).getInt(172);
            ConfigManagerMars.idEntitySpaceshipTier2 = ConfigManagerMars.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntitySpaceshipTier2", 173).getInt(173);
            ConfigManagerMars.idEntitySludgeling = ConfigManagerMars.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntitySludgeling", 174).getInt(174);
            ConfigManagerMars.idEntitySlimeling = ConfigManagerMars.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntitySlimeling", 175).getInt(175);
            ConfigManagerMars.idEntityTerraformBubble = ConfigManagerMars.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityTerraformBubble", 176).getInt(176);
            ConfigManagerMars.idEntityLandingBalloons = ConfigManagerMars.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityLandingBalloons", 177).getInt(177);
            ConfigManagerMars.idEntityCargoRocket = ConfigManagerMars.configuration.get(Constants.CONFIG_CATEGORY_ENTITIES, "idEntityCargoRocket", 178).getInt(178);

            ConfigManagerMars.idSchematicRocketT2 = ConfigManagerMars.configuration.get(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicRocketT2", 2).getInt(2);
            ConfigManagerMars.idSchematicCargoRocket = ConfigManagerMars.configuration.get(Constants.CONFIG_CATEGORY_SCHEMATIC, "idSchematicCargoRocket", 3).getInt(3);

            ConfigManagerMars.generateOtherMods = ConfigManagerMars.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Generate other mod's features on Mars", false).getBoolean(false);
            ConfigManagerMars.launchControllerChunkLoad = ConfigManagerMars.configuration.get(Constants.CONFIG_CATEGORY_GENERAL, "Launch Controller acts as chunkloader", true, "Will cause issues if disabled!").getBoolean(true);
        }
        catch (final Exception e)
        {
            FMLLog.log(Level.ERROR, e, "Galacticraft Mars (Planets) has a problem loading it's configuration");
        }
        finally
        {
            if (ConfigManagerMars.configuration.hasChanged())
            {
                ConfigManagerMars.configuration.save();
            }
            ConfigManagerMars.loaded = true;
        }
    }
}
