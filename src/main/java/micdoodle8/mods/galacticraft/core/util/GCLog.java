package micdoodle8.mods.galacticraft.core.util;

import org.apache.logging.log4j.LogManager;

import micdoodle8.mods.galacticraft.core.Constants;


public class GCLog
{
	private static org.apache.logging.log4j.Logger GCLog;

	public static org.apache.logging.log4j.Logger getLogger() {
		if(GCLog == null) {
			GCLog = LogManager.getFormatterLogger(Constants.MOD_NAME_SIMPLE);
		}
		return GCLog;
	}

	


}
