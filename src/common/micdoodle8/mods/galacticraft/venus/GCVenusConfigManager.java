package micdoodle8.mods.galacticraft.venus;

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
public class GCVenusConfigManager 
{
	public static boolean loaded;
	
	static Configuration configuration;
	
	public GCVenusConfigManager(File file)
	{
		if (!loaded)
		{
			configuration = new Configuration(file);
			setDefaultValues();
		}
	}
	
	// DIMENSIONS
	public static int dimensionIDVenus;
	
	private void setDefaultValues()
    {
		try
		{
	        configuration.load();
	        
//			dimensionIDVenus = configuration.getOrCreateIntProperty("Venus Dimension ID", 										"Dimension", 					30).getInt(30);
		}
		catch (Exception e)
		{
			FMLLog.log(Level.SEVERE, e, "Galacticraft Venus has a problem loading it's configuration");
		}
		finally 
		{
			configuration.save();
			loaded = true;
		}
    }
}
