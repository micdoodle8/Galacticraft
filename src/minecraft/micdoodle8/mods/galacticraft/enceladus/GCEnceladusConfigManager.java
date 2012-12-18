package micdoodle8.mods.galacticraft.enceladus;

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
public class GCEnceladusConfigManager 
{
	public static boolean loaded;
	
	static Configuration configuration;
	
	// DIMENSIONS
	public static int dimensionIDEnceladus;
	
	public GCEnceladusConfigManager(File file)
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
	        
	        dimensionIDEnceladus = 				configuration.get("Dimensions", 										"Enceladus Dimension ID",				-24)		.getInt(-24);
	    }
		catch (final Exception e)
		{
			FMLLog.log(Level.SEVERE, e, "Galacticraft Enceladus has a problem loading it's configuration");
		}
		finally 
		{
			configuration.save();
			loaded = true;
		}
    }
}
