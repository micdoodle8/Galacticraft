package micdoodle8.mods.galacticraft.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.mars.GCMarsConfigManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
@Mod(name="Galacticraft Core", version="v1", useMetadata = true, modid = "GalacticraftCore")
@NetworkMod(channels = {"GalacticraftCore"}, clientSideRequired = true, serverSideRequired = false)
public class GalacticraftCore 
{
	@SidedProxy(clientSide = "micdoodle8.mods.galacticraft.core.client.CoreClientProxy", serverSide = "micdoodle8.mods.galacticraft.core.CoreCommonProxy")
	public static CoreCommonProxy proxy;
	
	@Instance("GalacticraftCore")
	public static GalacticraftCore instance;
	
	public static GCLocalization lang;
	
	public static List serverPlayerBaseList = new ArrayList();
	public static List serverPlayerAPIs = new ArrayList();
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		new GCCoreConfigManager(new File(event.getModConfigurationDirectory(), "Galacticraft/core.conf"));
		
		lang = new GCLocalization("micdoodle8/mods/galacticraft/core/client");
	}
}
