package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.core.Constants;
import org.apache.logging.log4j.Logger;

public class GCLog
{
    
    private static Logger log;
    
    public static void info(String message)
    {
        log.info(Constants.MOD_NAME_SIMPLE + ": "+ message);
        
    }

    public static void severe(String message)
    {
        log.error(Constants.MOD_NAME_SIMPLE + ": " + message);
    }

    public static void debug(String message)
    {
        if (ConfigManagerCore.enableDebug)
        {
            log.debug(Constants.MOD_NAME_SIMPLE + ": "+ message);
        }
    }

    public static void exception(Exception e)
    {
        log.catching(e);
    }
}
