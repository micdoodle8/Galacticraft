package micdoodle8.mods.galacticraft.moon;

import java.io.File;
import java.util.logging.Level;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMoonConfigManager
{
	public static boolean loaded;
	
	static Configuration configuration;
	
	// DIMENSIONS
	public static int dimensionIDMoon;
	
	public static int idBlockMoonStone;
	public static int idBlockMoonGrass;
	public static int idBlockMoonDirt;
	public static int idBlockOre;
	public static int idBlockCheese;
	
	// ITEMS
	public static int idItemCheeseCurd;
	public static int idItemMeteoricIronRaw;
	public static int idItemBlockCheese;
	public static int idItemMeteoricIronIngot;
	
	// GENERAL
	public static boolean disableCheeseMoon;
	public static boolean disableAluminiumMoon;
	public static boolean disableIronMoon;
	
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

	        GCMoonConfigManager.dimensionIDMoon = 					GCMoonConfigManager.configuration.get("Dimensions", 												"Moon Dimension ID", 				-28)		.getInt(-28);

	        GCMoonConfigManager.idItemCheeseCurd = 					GCMoonConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idItemCheeseCurd", 				10000)	.getInt(10000);
	        GCMoonConfigManager.idItemMeteoricIronRaw =				GCMoonConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idItemMeteoricIronRaw", 			10001)	.getInt(10001);
	        GCMoonConfigManager.idItemBlockCheese =					GCMoonConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idItemBlockCheese", 				10002)	.getInt(10002);
	        GCMoonConfigManager.idItemMeteoricIronIngot =			GCMoonConfigManager.configuration.get(Configuration.CATEGORY_ITEM, 									"idItemMeteoricIronIngot", 			10003)	.getInt(10003);
	        
	        GCMoonConfigManager.idBlockMoonStone = 					GCMoonConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, 								"idBlockMoonStone", 				219)	.getInt(219);
	        GCMoonConfigManager.idBlockMoonGrass = 					GCMoonConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, 								"idBlockMoonGrass", 				220)	.getInt(220);
	        GCMoonConfigManager.idBlockMoonDirt = 					GCMoonConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, 								"idBlockMoonDirt", 					221)	.getInt(221);
	        GCMoonConfigManager.idBlockOre = 						GCMoonConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, 								"idBlockMoonOre", 					223)	.getInt(223);
	        GCMoonConfigManager.idBlockCheese = 					GCMoonConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, 								"idBlockCheese", 					224)	.getInt(224);
	        
	        GCMoonConfigManager.disableCheeseMoon = 				GCMoonConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Disable Cheese Ore Gen on Moon",		false)		.getBoolean(false);
	        GCMoonConfigManager.disableAluminiumMoon = 				GCMoonConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Disable Aluminium Ore Gen on Moon",	false)		.getBoolean(false);
	        GCMoonConfigManager.disableIronMoon = 					GCMoonConfigManager.configuration.get(Configuration.CATEGORY_GENERAL, "Disable Iron Ore Gen on Moon",		false)		.getBoolean(false);
		}
		catch (final Exception e)
		{
			FMLLog.log(Level.SEVERE, e, "Galacticraft Moon (core) has a problem loading it's configuration");
		}
		finally
		{
			GCMoonConfigManager.configuration.save();
			GCMoonConfigManager.loaded = true;
		}
    }
}
