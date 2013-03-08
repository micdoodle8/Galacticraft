package micdoodle8.mods.galacticraft.io;

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
public class GCIoConfigManager
{
	public static boolean loaded;

	static Configuration configuration;

	// DIMENSIONS
	public static int dimensionIDIo;

	// BLOCKS
	public static int idBlock;
	public static int idBlockPyroxene;

	public GCIoConfigManager(File file)
	{
		if (!GCIoConfigManager.loaded)
		{
			GCIoConfigManager.configuration = new Configuration(file);
			this.setDefaultValues();
		}
	}

	private void setDefaultValues()
    {
		try
		{
	        GCIoConfigManager.configuration.load();

	        GCIoConfigManager.dimensionIDIo = 				GCIoConfigManager.configuration.get("Dimensions", 										"Io Dimension ID",				-26)		.getInt(-26);

	        GCIoConfigManager.idBlock = 						GCIoConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, 						"idBlockIo", 					199)	.getInt(199);
	        GCIoConfigManager.idBlockPyroxene =  				GCIoConfigManager.configuration.get(Configuration.CATEGORY_BLOCK, 						"idBlockPyroxene", 				195)	.getInt(195);
		}
		catch (final Exception e)
		{
			FMLLog.log(Level.SEVERE, e, "Galacticraft Io has a problem loading it's configuration");
		}
		finally
		{
			GCIoConfigManager.configuration.save();
			GCIoConfigManager.loaded = true;
		}
    }
}
