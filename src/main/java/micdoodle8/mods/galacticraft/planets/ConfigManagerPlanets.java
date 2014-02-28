package micdoodle8.mods.galacticraft.planets;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;

/**
 * GCMarsConfigManager.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class ConfigManagerPlanets
{
	private static boolean loaded;
	private static Configuration configuration;

	// DIMENSIONS
	public static int dimensionIDMars;
	public static int dimensionIDAsteroids;

	// BLOCKS
	public static int idBlockMars;
	public static int idBlockBacterialSludge;
	public static int idBlockVine;
	public static int idBlockRock;
	public static int idBlockTreasureChestT2;
	public static int idBlockMachine;
	public static int idBlockCreeperEgg;
	public static int idBlockTintedGlassPane;

	// ITEMS
	public static int idItemMarsBasic;
	public static int idItemSpaceshipTier2;
	public static int idItemKeyT2;
	public static int idItemSchematicMars;

	// ARMOR
	public static int idArmorDeshHelmet;
	public static int idArmorDeshChestplate;
	public static int idArmorDeshLeggings;
	public static int idArmorDeshBoots;

	// TOOLS
	public static int idToolDeshSword;
	public static int idToolDeshPickaxe;
	public static int idToolDeshAxe;
	public static int idToolDeshSpade;
	public static int idToolDeshHoe;

	// ENTITIES
	public static int idEntityCreeperBoss;
	public static int idEntityProjectileTNT;
	public static int idEntitySpaceshipTier2;
	public static int idEntitySludgeling;
	public static int idEntitySlimeling;
	public static int idEntityTerraformBubble;
	public static int idEntityLandingBalloons;
	public static int idEntityCargoRocket;

	// GUI
	public static int idGuiRocketCraftingBenchT2;
	public static int idGuiMachine;
	public static int idGuiCargoRocketCraftingBench;

	// SCHEMATIC
	public static int idSchematicRocketT2;
	public static int idSchematicCargoRocket;

	// GENERAL
	public static boolean generateOtherMods;
	public static boolean launchControllerChunkLoad;

	public static void setDefaultValues(File file)
	{
		if (!ConfigManagerPlanets.loaded)
		{
			ConfigManagerPlanets.configuration = new Configuration(file);
		}
		
		try
		{
			ConfigManagerPlanets.configuration.load();

			ConfigManagerPlanets.dimensionIDMars = ConfigManagerPlanets.configuration.get("Dimensions", "Mars Dimension ID", -29).getInt(-29);
			ConfigManagerPlanets.dimensionIDAsteroids = ConfigManagerPlanets.configuration.get("Dimensions", "Asteroids Dimension ID", -30).getInt(-30);

			ConfigManagerPlanets.idEntityCreeperBoss = ConfigManagerPlanets.configuration.get("Entities", "idEntityCreeperBoss", 171).getInt(171);
			ConfigManagerPlanets.idEntityProjectileTNT = ConfigManagerPlanets.configuration.get("Entities", "idEntityProjectileTNT", 172).getInt(172);
			ConfigManagerPlanets.idEntitySpaceshipTier2 = ConfigManagerPlanets.configuration.get("Entities", "idEntitySpaceshipTier2", 173).getInt(173);
			ConfigManagerPlanets.idEntitySludgeling = ConfigManagerPlanets.configuration.get("Entities", "idEntitySludgeling", 174).getInt(174);
			ConfigManagerPlanets.idEntitySlimeling = ConfigManagerPlanets.configuration.get("Entities", "idEntitySlimeling", 175).getInt(175);
			ConfigManagerPlanets.idEntityTerraformBubble = ConfigManagerPlanets.configuration.get("Entities", "idEntityTerraformBubble", 176).getInt(176);
			ConfigManagerPlanets.idEntityLandingBalloons = ConfigManagerPlanets.configuration.get("Entities", "idEntityLandingBalloons", 177).getInt(177);
			ConfigManagerPlanets.idEntityCargoRocket = ConfigManagerPlanets.configuration.get("Entities", "idEntityCargoRocket", 178).getInt(178);

			ConfigManagerPlanets.idGuiRocketCraftingBenchT2 = ConfigManagerPlanets.configuration.get("GUI", "idGuiRocketCraftingBenchT2", 143).getInt(143);
			ConfigManagerPlanets.idGuiMachine = ConfigManagerPlanets.configuration.get("GUI", "idGuiMachine", 146).getInt(146);
			ConfigManagerPlanets.idGuiCargoRocketCraftingBench = ConfigManagerPlanets.configuration.get("GUI", "idGuiCargoRocketCraftingBench", 147).getInt(147);

			ConfigManagerPlanets.idSchematicRocketT2 = ConfigManagerPlanets.configuration.get("Schematic", "idSchematicRocketT2", 2).getInt(2);
			ConfigManagerPlanets.idSchematicCargoRocket = ConfigManagerPlanets.configuration.get("Schematic", "idSchematicCargoRocket", 3).getInt(3);

			ConfigManagerPlanets.generateOtherMods = ConfigManagerPlanets.configuration.get(Configuration.CATEGORY_GENERAL, "Generate other mod's features on Mars", false).getBoolean(false);
			ConfigManagerPlanets.launchControllerChunkLoad = ConfigManagerPlanets.configuration.get(Configuration.CATEGORY_GENERAL, "Whether launch controller keeps chunks loaded. This will cause issues if disabled.", true).getBoolean(true);
		}
		catch (final Exception e)
		{
			FMLLog.log(Level.FATAL, e, "Galacticraft Planets has a problem loading it's configuration");
		}
		finally
		{
			ConfigManagerPlanets.configuration.save();
			ConfigManagerPlanets.loaded = true;
		}
	}
}
