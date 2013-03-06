package micdoodle8.mods.galacticraft.jupiter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubMod;
import micdoodle8.mods.galacticraft.API.IGalaxy;
import micdoodle8.mods.galacticraft.callisto.GalacticraftCallisto;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.europa.GCEuropaConfigManager;
import micdoodle8.mods.galacticraft.europa.GalacticraftEuropa;
import micdoodle8.mods.galacticraft.io.GalacticraftIo;
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
@Mod(name="Galacticraft Jupiter", version="v1", useMetadata = false, modid = "GalacticraftJupiter")
@NetworkMod(channels = {"GalacticraftJupiter"}, clientSideRequired = true, serverSideRequired = false)
public class GalacticraftJupiter implements IGalacticraftSubMod
{
	@SidedProxy(clientSide = "micdoodle8.mods.galacticraft.jupiter.client.ClientProxyJupiter", serverSide = "micdoodle8.mods.galacticraft.jupiter.CommonProxyJupiter")
	public static CommonProxyJupiter proxy;
	
	@Instance("GalacticraftJupiter")
	public static GalacticraftJupiter instance;
	
	public static GalacticraftEuropa moonEuropa = new GalacticraftEuropa();
	public static GalacticraftIo moonIo = new GalacticraftIo();
	public static GalacticraftCallisto moonCallisto = new GalacticraftCallisto();
	
	public static List jupiterPlayers = new ArrayList();
	public static List gcJupiterPlayers = new ArrayList();
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		GalacticraftJupiter.moonEuropa.preInit(event);
		GalacticraftJupiter.moonIo.preInit(event);
		GalacticraftJupiter.moonCallisto.preInit(event);
		
		GalacticraftCore.registerSubMod(this);
		
		new GCEuropaConfigManager(new File(event.getModConfigurationDirectory(), "Galacticraft/jupiter.conf"));
		
		GalacticraftJupiter.proxy.preInit(event);
	}

	@Init
	public void load(FMLInitializationEvent event)
	{
		GalacticraftJupiter.moonEuropa.init(event);
		GalacticraftJupiter.moonIo.init(event);
		GalacticraftJupiter.moonCallisto.init(event);
		GalacticraftJupiter.proxy.init(event);
	}

	@PostInit
	public void postLoad(FMLPostInitializationEvent event)
	{
		GalacticraftJupiter.moonEuropa.postInit(event);
		GalacticraftJupiter.moonIo.postInit(event);
		GalacticraftJupiter.moonCallisto.postInit(event);
		GalacticraftJupiter.proxy.postInit(event);
		GalacticraftJupiter.proxy.registerRenderInformation();
	}
	
	@ServerStarted
	public void serverStarted(FMLServerStartedEvent event)
	{
		GalacticraftJupiter.moonEuropa.serverInit(event);
		GalacticraftJupiter.moonIo.serverInit(event);
		GalacticraftJupiter.moonCallisto.serverInit(event);
	}

	@Override
	public String getDimensionName()
	{
		return "Jupiter";
	}

	@Override
	public boolean reachableDestination()
	{
		return false;
	}

	@Override
	public IGalaxy getParentGalaxy()
	{
		return GalacticraftCore.galaxyMilkyWay;
	}
}
