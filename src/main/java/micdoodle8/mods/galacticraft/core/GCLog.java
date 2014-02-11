package micdoodle8.mods.galacticraft.core;

import java.util.logging.Level;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

/**
 * GCLog.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
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
