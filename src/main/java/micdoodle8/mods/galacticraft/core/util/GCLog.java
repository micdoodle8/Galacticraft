package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.core.Constants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GCLog
{
    
    private static Logger log = LogManager.getFormatterLogger(Constants.MOD_NAME_SIMPLE);
    
    public static void info(String message)
    {
        log.info(message);
        
    }

    public static void severe(String message)
    {
        log.error(message);
    }

    public static void debug(String message)
    {
        if (ConfigManagerCore.enableDebug)
        {
            log.debug(message);
        }
    }

    public static void exception(Exception e)
    {
        log.catching(e);
    }
}
