package micdoodle8.mods.galacticraft.saturn;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubMod;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.enceladus.GalacticraftEnceladus;
import micdoodle8.mods.galacticraft.europa.GCEuropaConfigManager;
import micdoodle8.mods.galacticraft.mimas.GalacticraftMimas;
import micdoodle8.mods.galacticraft.titan.GalacticraftTitan;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarted;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.network.NetworkMod;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
@Mod(name="Galacticraft Saturn", version="v1", useMetadata = false, modid = "GalacticraftSaturn")
@NetworkMod(channels = {"GalacticraftSaturn"}, clientSideRequired = true, serverSideRequired = false)
public class GalacticraftSaturn implements IGalacticraftSubMod
{
	@SidedProxy(clientSide = "micdoodle8.mods.galacticraft.saturn.client.ClientProxySaturn", serverSide = "micdoodle8.mods.galacticraft.saturn.CommonProxySaturn")
	public static CommonProxySaturn proxy;
	
	@Instance("GalacticraftSaturn")
	public static GalacticraftSaturn instance;
	
	public static GalacticraftTitan moonTitan = new GalacticraftTitan();
	public static GalacticraftEnceladus moonEnceladus = new GalacticraftEnceladus();
	public static GalacticraftMimas moonMimas = new GalacticraftMimas();
	
	public static List saturnPlayers = new ArrayList();
	public static List gcSaturnPlayers = new ArrayList();
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		moonTitan.preInit(event);
		moonEnceladus.preInit(event);
		moonMimas.preInit(event);
		
		GalacticraftCore.registerSubMod(this);
		
		new GCEuropaConfigManager(new File(event.getModConfigurationDirectory(), "Galacticraft/saturn.conf"));
		
		proxy.preInit(event);
	}

	@Init
	public void load(FMLInitializationEvent event)
	{
		moonTitan.init(event);
		moonEnceladus.init(event);
		moonMimas.init(event);
		proxy.init(event);
	}

	@PostInit
	public void postLoad(FMLPostInitializationEvent event)
	{
		moonTitan.postInit(event);
		moonEnceladus.postInit(event);
		moonMimas.postInit(event);
		proxy.postInit(event);
		proxy.registerRenderInformation();
	}
	
	@ServerStarted
	public void serverStarted(FMLServerStartedEvent event)
	{
		moonTitan.serverInit(event);
		moonEnceladus.serverInit(event);
		moonMimas.serverInit(event);
	}

	@Override
	public String getDimensionName() 
	{
		return "Saturn";
	}

	@Override
	public boolean reachableDestination() 
	{
		return false;
	}
}
