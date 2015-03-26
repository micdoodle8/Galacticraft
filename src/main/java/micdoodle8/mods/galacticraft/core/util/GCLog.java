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
}
