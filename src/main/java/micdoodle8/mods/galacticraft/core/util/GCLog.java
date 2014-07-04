package micdoodle8.mods.galacticraft.core.util;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import org.apache.logging.log4j.Level;

public class GCLog
{
	public static void info(String message)
	{
		FMLRelaunchLog.log("Galacticraft", Level.INFO, message);
	}

	public static void severe(String message)
	{
		FMLRelaunchLog.log("Galacticraft", Level.ERROR, message);
	}
}
