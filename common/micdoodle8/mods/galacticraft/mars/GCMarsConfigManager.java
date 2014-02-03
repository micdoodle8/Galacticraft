package micdoodle8.mods.galacticraft.mars;

import java.io.File;
import java.util.logging.Level;

import net.minecraftforge.common.Configuration;
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

			GCMarsConfigManager.idBlockMars = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockMars", 3390).getInt(3390);
			GCMarsConfigManager.idBlockBacterialSludge = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockBacterialSludge", 3391).getInt(3391);
			GCMarsConfigManager.idBlockVine = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockVine", 3392).getInt(3392);
			GCMarsConfigManager.idBlockRock = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockRock", 3393).getInt(3393);
			GCMarsConfigManager.idBlockTreasureChestT2 = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockTreasureChestT2", 3394).getInt(3394);
			GCMarsConfigManager.idBlockMachine = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockMachine", 3395).getInt(3395);
			GCMarsConfigManager.idBlockCreeperEgg = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockCreeperEgg", 3396).getInt(3396);
			GCMarsConfigManager.idBlockTintedGlassPane = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockTintedGlassPane", 3397).getInt(3397);

			GCMarsConfigManager.idItemMarsBasic = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemMarsBasic", 9905).getInt(9905);
			GCMarsConfigManager.idItemSpaceshipTier2 = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemSpaceshipTier2", 9906).getInt(9906);
			GCMarsConfigManager.idItemKeyT2 = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemKeyT2", 9916).getInt(9916);
			GCMarsConfigManager.idItemSchematicMars = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemSchematicMars", 9917).getInt(9917);

			GCMarsConfigManager.idToolDeshSword = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idToolDeshSword", 9907).getInt(9907);
			GCMarsConfigManager.idToolDeshPickaxe = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idToolDeshPickaxe", 9908).getInt(9908);
			GCMarsConfigManager.idToolDeshSpade = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idToolDeshSpade", 9909).getInt(9909);
			GCMarsConfigManager.idToolDeshHoe = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idToolDeshHoe", 9910).getInt(9910);
			GCMarsConfigManager.idToolDeshAxe = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idToolDeshAxe", 9911).getInt(9911);

			GCMarsConfigManager.idArmorDeshHelmet = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idArmorDeshHelmet", 9912).getInt(9912);
			GCMarsConfigManager.idArmorDeshChestplate = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idArmorDeshChestplate", 9913).getInt(9913);
			GCMarsConfigManager.idArmorDeshLeggings = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idArmorDeshLeggings", 9914).getInt(9914);
			GCMarsConfigManager.idArmorDeshBoots = GCMarsConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idArmorDeshBoots", 9915).getInt(9915);

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
			FMLLog.log(Level.SEVERE, e, "Galacticraft Mars has a problem loading it's configuration");
		}
		finally
		{
			GCMarsConfigManager.configuration.save();
			GCMarsConfigManager.loaded = true;
		}
	}
}
