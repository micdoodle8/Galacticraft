package micdoodle8.mods.galacticraft.api;

import java.lang.reflect.Field;

public class GalacticraftConfigAccess
{
	private static Field quickMode;
	private static Field hardMode;
	private static Field adventureMode;
	private static Field adventureRecipes;
	private static Field adventureMobDropsAndSpawning;
	private static Field adventureSpawnHandling;
	private static Field adventureAsteroidPopulation;
	
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
	
	public static boolean getChallengeRecipes()
	{
		if (quickMode == null)
			setup();
		
		try {
			return (boolean) adventureRecipes.getBoolean(null);
		} catch (Exception e) { }
		return false;
	}
	
	public static boolean getChallengeMobDropsAndSpawning()
	{
		if (quickMode == null)
			setup();
		
		try {
			return (boolean) adventureMobDropsAndSpawning.getBoolean(null);
		} catch (Exception e) { }
		return false;
	}
	
	public static boolean getChallengeSpawnHandling()
	{
		if (quickMode == null)
			setup();
		
		try {
			return (boolean) adventureSpawnHandling.getBoolean(null);
		} catch (Exception e) { }
		return false;
	}
	
	public static boolean getChallengeAsteroidPopulation()
	{
		if (quickMode == null)
			setup();
		
		try {
			return (boolean) adventureAsteroidPopulation.getBoolean(null);
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
			adventureRecipes = GCConfig.getField("challengeRecipes");
			adventureMobDropsAndSpawning = GCConfig.getField("challengeMobDropsAndSpawning");
			adventureSpawnHandling = GCConfig.getField("challengeSpawnHandling");
			adventureAsteroidPopulation = GCConfig.getField("challengeAsteroidPopulation");
		} catch (Exception e) { e.printStackTrace(); }
	}
}
