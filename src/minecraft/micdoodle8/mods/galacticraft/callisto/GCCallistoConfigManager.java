package micdoodle8.mods.galacticraft.callisto;

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
public class GCCallistoConfigManager 
{
	public static boolean loaded;
	
	static Configuration configuration;
	
	// DIMENSIONS
	public static int dimensionIDCallisto;
	
	// BLOCKS
	public static int idBlock;
	
	public GCCallistoConfigManager(File file)
	{
		if (!loaded)
		{
			configuration = new Configuration(file);
			this.setDefaultValues();
		}
	}
	
	private void setDefaultValues()
    {
		try
		{
	        configuration.load();
	        
	        dimensionIDCallisto = 				configuration.get("Dimensions", 										"Callisto Dimension ID",				-22)		.getInt(-22);
	        
	        idBlock = 							configuration.get(Configuration.CATEGORY_BLOCK, 						"idBlockCallisto", 					197)	.getInt(197);
		}
		catch (final Exception e)
		{
			FMLLog.log(Level.SEVERE, e, "Galacticraft Callisto has a problem loading it's configuration");
		}
		finally 
		{
			configuration.save();
			loaded = true;
		}
    }
}
