package micdoodle8.mods.galacticraft.jupiter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubMod;
import micdoodle8.mods.galacticraft.core.GCCoreLocalization;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.europa.GCEuropaConfigManager;
import micdoodle8.mods.galacticraft.europa.GalacticraftEuropa;
import micdoodle8.mods.galacticraft.jupiter.client.ClientProxyJupiter;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

/**
 * Copyright 2012, micdoodle8
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
	
	public static ClientProxyJupiter client = new ClientProxyJupiter();
	
	public static List jupiterPlayers = new ArrayList();
	public static List gcJupiterPlayers = new ArrayList();
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		moonEuropa.preInit(event);
		
		GalacticraftCore.registerClientSubMod(client);
		
		GalacticraftCore.registerSubMod(this);
		
		new GCEuropaConfigManager(new File(event.getModConfigurationDirectory(), "Galacticraft/jupiter.conf"));
		
		proxy.preInit(event);
	}

	@Override
	public void load(FMLInitializationEvent event)
	{
		proxy.init(event);
	}

	@Override
	public void postLoad(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
		proxy.registerRenderInformation();
	}

	@Override
	public String getDimensionName() 
	{
		return "Jupiter";
	}
}
