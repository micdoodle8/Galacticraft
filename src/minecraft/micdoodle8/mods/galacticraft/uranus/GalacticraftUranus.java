package micdoodle8.mods.galacticraft.uranus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubMod;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.europa.GCEuropaConfigManager;
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
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
@Mod(name="Galacticraft Uranus", version="v1", useMetadata = false, modid = "GalacticraftUranus")
@NetworkMod(channels = {"GalacticraftUranus"}, clientSideRequired = true, serverSideRequired = false)
public class GalacticraftUranus implements IGalacticraftSubMod
{
	@SidedProxy(clientSide = "micdoodle8.mods.galacticraft.uranus.client.ClientProxyUranus", serverSide = "micdoodle8.mods.galacticraft.uranus.CommonProxyUranus")
	public static CommonProxyUranus proxy;
	
	@Instance("GalacticraftUranus")
	public static GalacticraftUranus instance;
	
	public static List uranusPlayers = new ArrayList();
	public static List gcUranusPlayers = new ArrayList();
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		GalacticraftCore.registerSubMod(this);
		
		new GCEuropaConfigManager(new File(event.getModConfigurationDirectory(), "Galacticraft/uranus.conf"));
		
		proxy.preInit(event);
	}

	@Init
	public void load(FMLInitializationEvent event)
	{
		proxy.init(event);
	}

	@PostInit
	public void postLoad(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
		proxy.registerRenderInformation();
	}
	
	@ServerStarted
	public void serverStarted(FMLServerStartedEvent event)
	{
	}

	@Override
	public String getDimensionName() 
	{
		return "Uranus";
	}

	@Override
	public boolean reachableDestination() 
	{
		return true;
	}
}
