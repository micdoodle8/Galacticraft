package micdoodle8.mods.galacticraft.core;

import java.util.logging.Level;

public class GCLog
{
    private static cpw.mods.fml.relauncher.FMLRelaunchLog coreLog = cpw.mods.fml.relauncher.FMLRelaunchLog.log;

    public static void info(String message)
    {
        coreLog.log("Galacticraft", Level.INFO, message);
    }
    
    public static void severe(String message)
    {
        coreLog.log("Galacticraft", Level.SEVERE, message);
    }
}
