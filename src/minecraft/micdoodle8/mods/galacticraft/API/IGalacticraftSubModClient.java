package micdoodle8.mods.galacticraft.API;

import micdoodle8.mods.galacticraft.core.GCCoreLocalization;
import micdoodle8.mods.galacticraft.moon.client.ClientProxyMoon;

public interface IGalacticraftSubModClient 
{
	public String getDimensionName();
	
	public GCCoreLocalization getLanguageFile();
	
	public String getPlanetSpriteDirectory();
	
	public IPlanetSlotRenderer getSlotRenderer();
	
	public IMapPlanet getPlanetForMap();
	
	public IMapPlanet[] getChildMapPlanets();
	
	/**
	 * @return theme for this planet, can be null if you don't have one. MUST BE AN OGG FILE
	 * 
	 * @see ClientProxyMoon.getPathToMusicFile() 
	 */
	public String getPathToMusicFile();
}
