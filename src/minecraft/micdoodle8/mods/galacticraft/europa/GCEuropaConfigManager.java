package micdoodle8.mods.galacticraft.europa;

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
public class GCEuropaConfigManager 
{
	public static boolean loaded;
	
	static Configuration configuration;
	
	// DIMENSIONS
	public static int dimensionIDEuropa;
	
	// BLOCKS
	public static int idBlockBrittleIce;
	
	public GCEuropaConfigManager(File file)
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
	        
	        dimensionIDEuropa = 				configuration.get("Dimensions", 										"Europa Dimension ID",				-27)		.getInt(-27);
	    
	        idBlockBrittleIce = 				configuration.get(Configuration.CATEGORY_BLOCK, 						"idBlockBrittleIce", 				225)	.getInt(220);
		}
		catch (final Exception e)
		{
			FMLLog.log(Level.SEVERE, e, "Galacticraft Europa has a problem loading it's configuration");
		}
		finally 
		{
			configuration.save();
			loaded = true;
		}
    }
}
