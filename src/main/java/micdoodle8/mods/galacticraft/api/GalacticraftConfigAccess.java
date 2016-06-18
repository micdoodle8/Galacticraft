package micdoodle8.mods.galacticraft.api;

import java.lang.reflect.Field;

public class GalacticraftConfigAccess
{
	private static Field quickMode;
	private static Field hardMode;
	private static Field adventureMode;
	
	public static boolean getQuickMode()
	{
		if (quickMode == null)
			setup();
		
		try {
			return (boolean) quickMode.getBoolean(null);
		} catch (Exception e) { }
		return false;
	}
	
	public static boolean getHardMode()
	{
		if (quickMode == null)
			setup();
		
		try {
			return (boolean) hardMode.getBoolean(null);
		} catch (Exception e) { }
		return false;
	}
	
	public static boolean getChallengeMode()
	{
		if (quickMode == null)
			setup();
		
		try {
			return (boolean) adventureMode.getBoolean(null);
		} catch (Exception e) { }
		return false;
	}
	
	private static void setup()
	{
		try {
			Class<?> GCConfig = Class.forName("micdoodle8.mods.galacticraft.core.util.ConfigManagerCore");
			quickMode = GCConfig.getField("quickMode");
			hardMode = GCConfig.getField("hardMode");
			adventureMode = GCConfig.getField("challengeMode");
		} catch (Exception e) { e.printStackTrace(); }
	}
}
