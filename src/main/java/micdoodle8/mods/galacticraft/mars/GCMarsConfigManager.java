package micdoodle8.mods.galacticraft.mars;

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
public class GCMarsConfigManager
{
	public static boolean loaded;

	static Configuration configuration;

	public GCMarsConfigManager(File file)
	{
		if (!GCMarsConfigManager.loaded)
		{
			GCMarsConfigManager.configuration = new Configuration(file);
			this.setDefaultValues();
		}
	}

	// DIMENSIONS
	public static int dimensionIDMars;

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

	private void setDefaultValues()
	{
		try
		{
			GCMarsConfigManager.configuration.load();

			GCMarsConfigManager.dimensionIDMars = GCMarsConfigManager.configuration.get("Dimensions", "Mars Dimension ID", -29).getInt(-29);

			GCMarsConfigManager.idEntityCreeperBoss = GCMarsConfigManager.configuration.get("Entities", "idEntityCreeperBoss", 171).getInt(171);
			GCMarsConfigManager.idEntityProjectileTNT = GCMarsConfigManager.configuration.get("Entities", "idEntityProjectileTNT", 172).getInt(172);
			GCMarsConfigManager.idEntitySpaceshipTier2 = GCMarsConfigManager.configuration.get("Entities", "idEntitySpaceshipTier2", 173).getInt(173);
			GCMarsConfigManager.idEntitySludgeling = GCMarsConfigManager.configuration.get("Entities", "idEntitySludgeling", 174).getInt(174);
			GCMarsConfigManager.idEntitySlimeling = GCMarsConfigManager.configuration.get("Entities", "idEntitySlimeling", 175).getInt(175);
			GCMarsConfigManager.idEntityTerraformBubble = GCMarsConfigManager.configuration.get("Entities", "idEntityTerraformBubble", 176).getInt(176);
			GCMarsConfigManager.idEntityLandingBalloons = GCMarsConfigManager.configuration.get("Entities", "idEntityLandingBalloons", 177).getInt(177);
			GCMarsConfigManager.idEntityCargoRocket = GCMarsConfigManager.configuration.get("Entities", "idEntityCargoRocket", 178).getInt(178);

			GCMarsConfigManager.idGuiRocketCraftingBenchT2 = GCMarsConfigManager.configuration.get("GUI", "idGuiRocketCraftingBenchT2", 143).getInt(143);
			GCMarsConfigManager.idGuiMachine = GCMarsConfigManager.configuration.get("GUI", "idGuiMachine", 146).getInt(146);
			GCMarsConfigManager.idGuiCargoRocketCraftingBench = GCMarsConfigManager.configuration.get("GUI", "idGuiCargoRocketCraftingBench", 147).getInt(147);

			GCMarsConfigManager.idSchematicRocketT2 = GCMarsConfigManager.configuration.get("Schematic", "idSchematicRocketT2", 2).getInt(2);
			GCMarsConfigManager.idSchematicCargoRocket = GCMarsConfigManager.configuration.get("Schematic", "idSchematicCargoRocket", 3).getInt(3);

			GCMarsConfigManager.generateOtherMods = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Generate other mod's features on Mars", false).getBoolean(false);
			GCMarsConfigManager.launchControllerChunkLoad = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Whether launch controller keeps chunks loaded. This will cause issues if disabled.", true).getBoolean(true);
		}
		catch (final Exception e)
		{
			FMLLog.log(Level.FATAL, e, "Galacticraft Mars has a problem loading it's configuration");
		}
		finally
		{
			GCMarsConfigManager.configuration.save();
			GCMarsConfigManager.loaded = true;
		}
	}
}
