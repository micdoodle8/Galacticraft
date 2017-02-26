package micdoodle8.mods.galacticraft.core.util;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import micdoodle8.mods.galacticraft.core.Constants;

import org.apache.logging.log4j.Level;

public class GCLog
{
    public static void info(String message)
    {
        FMLRelaunchLog.log(Constants.MOD_NAME_SIMPLE, Level.INFO, message);
    }

    public static void severe(String message)
    {
        FMLRelaunchLog.log(Constants.MOD_NAME_SIMPLE, Level.ERROR, message);
    }
    
    public static void debug(String message)
    {
        if (ConfigManagerCore.enableDebug)
        {
        	FMLRelaunchLog.log(Constants.MOD_NAME_SIMPLE, Level.INFO, "Debug: " + message);
        }
    }

	public static void exception(Exception e)
	{
		FMLRelaunchLog.log(Constants.MOD_NAME_SIMPLE, Level.ERROR, e.getMessage());
	}
}
