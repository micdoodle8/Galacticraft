package micdoodle8.mods.galacticraft.titan;

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
public class GCTitanConfigManager
{
	public static boolean loaded;

	static Configuration configuration;

	// DIMENSIONS
	public static int dimensionIDTitan;

	public GCTitanConfigManager(File file)
	{
		if (!GCTitanConfigManager.loaded)
		{
			GCTitanConfigManager.configuration = new Configuration(file);
			this.setDefaultValues();
		}
	}

	private void setDefaultValues()
    {
		try
		{
	        GCTitanConfigManager.configuration.load();

	        GCTitanConfigManager.dimensionIDTitan = 				GCTitanConfigManager.configuration.get("Dimensions", 										"Titan Dimension ID",				-25)		.getInt(-25);
	    }
		catch (final Exception e)
		{
			FMLLog.log(Level.SEVERE, e, "Galacticraft Titan has a problem loading it's configuration");
		}
		finally
		{
			GCTitanConfigManager.configuration.save();
			GCTitanConfigManager.loaded = true;
		}
    }
}
