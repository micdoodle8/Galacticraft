package micdoodle8.mods.galacticraft.moon;

import java.io.File;

import micdoodle8.mods.galacticraft.core.GCLog;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

/**
 * GCMoonConfigManager.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMoonConfigManager
{
	public static boolean loaded;

	static Configuration configuration;

	// DIMENSIONS
	public static int dimensionIDMoon;

	public static int idBlock;
	public static int idBlockCheese;

	public static Property blockIDProp;

	// ITEMS
	public static int idItemCheeseCurd;
	public static int idItemMeteoricIronRaw;
	public static int idItemBlockCheese;
	public static int idItemMeteoricIronIngot;

	// GENERAL
	public static boolean disableCheeseMoon;
	public static boolean disableAluminiumMoon;
	public static boolean disableIronMoon;
	public static boolean generateOtherMods;
	public static boolean disableMoonVillageGen;

	public GCMoonConfigManager(File file)
	{
		if (!GCMoonConfigManager.loaded)
		{
			GCMoonConfigManager.configuration = new Configuration(file);
			this.setDefaultValues();
		}
	}

	private void setDefaultValues()
	{
		try
		{
			GCMoonConfigManager.configuration.load();

			GCMoonConfigManager.dimensionIDMoon = GCMoonConfigManager.configuration.get("Dimensions", "Moon Dimension ID", -28).getInt(-28);

			GCMoonConfigManager.idItemCheeseCurd = GCMoonConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemCheeseCurd", 9851).getInt(9851);
			GCMoonConfigManager.idItemMeteoricIronRaw = GCMoonConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemMeteoricIronRaw", 9852).getInt(9852);
			GCMoonConfigManager.idItemBlockCheese = GCMoonConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemBlockCheese", 9853).getInt(9853);
			GCMoonConfigManager.idItemMeteoricIronIngot = GCMoonConfigManager.configuration.get(Configuration.CATEGORY_ITEM, "idItemMeteoricIronIngot", 9854).getInt(9854);

			GCMoonConfigManager.idBlock = GCMoonConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockMoon", 3347, "This can be above 256, even though it is generated in the world").getInt(3347);
			GCMoonConfigManager.idBlockCheese = GCMoonConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, "idBlockCheese", 3348).getInt(3348);

			GCMoonConfigManager.disableCheeseMoon = GCMoonConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Disable Cheese Ore Gen on Moon", false).getBoolean(false);
			GCMoonConfigManager.generateOtherMods = GCMoonConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Generate other mod's features on Moon", false).getBoolean(false);
			GCMoonConfigManager.disableMoonVillageGen = GCMoonConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Disable Moon Village Gen", false).getBoolean(false);
		}
		catch (final Exception e)
		{
			GCLog.severe("Problem loading moon config (\"moon.conf\")");
		}
		finally
		{
			if (GCMoonConfigManager.configuration.hasChanged())
			{
				GCMoonConfigManager.configuration.save();
			}

			GCMoonConfigManager.loaded = true;
		}
	}
}
