package micdoodle8.mods.galacticraft.europa.client;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubModClient;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.GCCoreLocalization;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxyEuropa implements IGalacticraftSubModClient
{
	public static GCCoreLocalization lang = new GCCoreLocalization("micdoodle8/mods/galacticraft/europa/client");

	@Override
	public String getDimensionName() 
	{
		return "Europa";
	}
	
	public void preInit(FMLPreInitializationEvent event) 
	{
	}

	public void init(FMLInitializationEvent event) 
	{
		GalacticraftCore.registerClientSubMod(this);
	}

	public void postInit(FMLPostInitializationEvent event) 
	{
	}
	
	@Override
	public GCCoreLocalization getLanguageFile() 
	{
		return ClientProxyEuropa.lang;
	}

	@Override
	public String getPlanetSpriteDirectory() 
	{
		return "/micdoodle8/mods/galacticraft/europa/client/planets/";
	}

	@Override
	public IPlanetSlotRenderer getSlotRenderer() 
	{
		return new GCEuropaSlotRenderer();
	}

	@Override
	public IMapPlanet getPlanetForMap() 
	{
		return null;
	}

	@Override
	public IMapPlanet[] getChildMapPlanets() 
	{
		return null;
	}

	@Override
	public String getPathToMusicFile() 
	{
		return null;
	}
}
