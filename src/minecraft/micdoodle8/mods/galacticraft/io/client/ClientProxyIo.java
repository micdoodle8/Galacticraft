package micdoodle8.mods.galacticraft.io.client;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubModClient;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.GCCoreLocalization;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.europa.client.GCEuropaSlotRenderer;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxyIo implements IGalacticraftSubModClient
{
	public static GCCoreLocalization lang = new GCCoreLocalization("micdoodle8/mods/galacticraft/io/client");

	@Override
	public String getDimensionName() 
	{
		return "Io";
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
		return this.lang;
	}

	@Override
	public String getPlanetSpriteDirectory() 
	{
		return "/micdoodle8/mods/galacticraft/io/client/planets/";
	}

	@Override
	public IPlanetSlotRenderer getSlotRenderer() 
	{
		return new GCIoSlotRenderer();
	}
}
