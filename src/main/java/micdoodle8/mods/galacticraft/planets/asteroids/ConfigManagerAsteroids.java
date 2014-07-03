package micdoodle8.mods.galacticraft.planets.asteroids;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;

public class ConfigManagerAsteroids
{
	public static boolean loaded;

	static Configuration configuration;

	public ConfigManagerAsteroids(File file)
	{
		if (!ConfigManagerAsteroids.loaded)
		{
			ConfigManagerAsteroids.configuration = new Configuration(file);
			this.setDefaultValues();
		}
	}

	// DIMENSIONS
	public static int dimensionIDAsteroids;

	// ENTITIES
	public static int idEntitySmallAsteroid;
	public static int idEntityGrappleHook;
	public static int idEntityTier3Rocket;

	// GUI

	// SCHEMATIC
    public static int idSchematicRocketT3;

	// GENERAL

	private void setDefaultValues()
	{
		try
		{
			ConfigManagerAsteroids.configuration.load();

			ConfigManagerAsteroids.dimensionIDAsteroids = ConfigManagerAsteroids.configuration.get("Dimensions", "Asteroids Dimension ID", -30).getInt(-30);

			ConfigManagerAsteroids.idEntitySmallAsteroid = ConfigManagerAsteroids.configuration.get("Entities", "idEntitySmallAsteroid", 180).getInt(180);
			ConfigManagerAsteroids.idEntityGrappleHook = ConfigManagerAsteroids.configuration.get("Entities", "idEntityGrappleHook", 181).getInt(181);
			ConfigManagerAsteroids.idEntityTier3Rocket = ConfigManagerAsteroids.configuration.get("Entities", "idEntityTier3Rocket", 182).getInt(182);

            ConfigManagerAsteroids.idSchematicRocketT3 = ConfigManagerAsteroids.configuration.get("Schematic", "idSchematicRocketT3", 4).getInt(4);
		}
		catch (final Exception e)
		{
			FMLLog.log(Level.ERROR, e, "Galacticraft Asteroids (Planets) has a problem loading it's configuration");
		}
		finally
		{
			ConfigManagerAsteroids.configuration.save();
			ConfigManagerAsteroids.loaded = true;
		}
	}
}
