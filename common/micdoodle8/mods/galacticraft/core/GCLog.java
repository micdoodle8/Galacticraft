package micdoodle8.mods.galacticraft.core;

import java.util.logging.Level;
import cpw.mods.fml.relauncher.FMLRelaunchLog;

public class GCLog
{
    public static void info(String message)
    {
        FMLRelaunchLog.log("Galacticraft", Level.INFO, message);
    }

    public static void severe(String message)
    {
        FMLRelaunchLog.log("Galacticraft", Level.SEVERE, message);
    }
}
