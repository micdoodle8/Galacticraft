package micdoodle8.mods.galacticraft.moon;

import java.io.File;

import micdoodle8.mods.galacticraft.core.GCLog;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

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
