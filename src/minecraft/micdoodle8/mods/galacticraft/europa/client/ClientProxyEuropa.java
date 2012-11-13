package micdoodle8.mods.galacticraft.europa.client;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubModClient;
import micdoodle8.mods.galacticraft.core.GCCoreLocalization;

public class ClientProxyEuropa implements IGalacticraftSubModClient
{
	public static GCCoreLocalization lang = new GCCoreLocalization("micdoodle8/mods/galacticraft/europa/client");

	@Override
	public String getDimensionName() 
	{
		return "Europa";
	}
	
	@Override
	public GCCoreLocalization getLanguageFile() 
	{
		return this.lang;
	}

	@Override
	public String getPlanetSpriteDirectory() 
	{
		return "/micdoodle8/mods/galacticraft/europa/client/planets/";
	}
}
