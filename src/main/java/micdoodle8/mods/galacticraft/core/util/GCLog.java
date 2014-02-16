package micdoodle8.mods.galacticraft.core.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;


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
	public static void info(String message, Object... data)
	{
        LogManager.getLogger("Galacticraft").log(Level.INFO, String.format(message, data));
	}

	public static void severe(String message, Object... data)
	{
        LogManager.getLogger("Galacticraft").log(Level.ERROR, String.format(message, data));
	}
}
