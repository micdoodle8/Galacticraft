package micdoodle8.mods.galacticraft.moon;

import java.io.File;
import java.util.logging.Level;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012, micdoodle8
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
	public static int idBlockMoonCobblestone;
	public static int idBlockOre;
	public static int idBlockCheese;
	
	// ITEMS
	public static int idItemCheeseCurd;
	public static int idItemMeteoricIronRaw;
	public static int idItemBlockCheese;
	public static int idItemMeteoricIronIngot;
	
	public GCMoonConfigManager(File file)
	{
		if (!loaded)
		{
			configuration = new Configuration(file);
			setDefaultValues();
		}
	}
	
	private void setDefaultValues()
    {
		try
		{
	        configuration.load();

	        dimensionIDMoon = 					configuration.get("Dimensions", 												"Moon Dimension ID", 				-28)		.getInt(-28);

	        idItemCheeseCurd = 					configuration.get(configuration.CATEGORY_ITEM, 									"idItemCheeseCurd", 				10000)	.getInt(10000);
	        idItemMeteoricIronRaw =				configuration.get(configuration.CATEGORY_ITEM, 									"idItemMeteoricIronRaw", 			10001)	.getInt(10001);
	        idItemBlockCheese =					configuration.get(configuration.CATEGORY_ITEM, 									"idItemBlockCheese", 				10002)	.getInt(10002);
	        idItemMeteoricIronIngot =			configuration.get(configuration.CATEGORY_ITEM, 									"idItemMeteoricIronIngot", 			10003)	.getInt(10003);
	        
	        idBlockMoonStone = 					configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockMoonStone", 				219)	.getInt(219);
	        idBlockMoonGrass = 					configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockMoonGrass", 				220)	.getInt(220);
	        idBlockMoonDirt = 					configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockMoonDirt", 					221)	.getInt(221);
	        idBlockMoonCobblestone = 			configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockMoonCobblestone", 			222)	.getInt(222);
	        idBlockOre = 						configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockMoonOre", 					223)	.getInt(223);
	        idBlockCheese = 					configuration.get(configuration.CATEGORY_BLOCK, 								"idBlockCheese", 					224)	.getInt(224);
		}
		catch (Exception e)
		{
			FMLLog.log(Level.SEVERE, e, "Galacticraft Moon (core) has a problem loading it's configuration");
		}
		finally 
		{
			configuration.save();
			loaded = true;
		}
    }
}
