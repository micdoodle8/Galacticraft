package micdoodle8.mods.galacticraft.venus;

import java.io.File;

import net.minecraft.src.ServerPlayerAPI;
import net.minecraftforge.common.DimensionManager;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
@Mod(name="Galacticraft - Venus", version="v1", useMetadata = false, modid = "GalacticraftVenus")
@NetworkMod(channels = {"GalacticraftVenus"}, clientSideRequired = true, serverSideRequired = false)
public class GalacticraftVenus 
{
	@SidedProxy(clientSide = "micdoodle8.mods.galacticraft.venus.client.ClientProxy", serverSide = "micdoodle8.mods.galacticraft.venus.CommonProxy")
	public static CommonProxy proxy;
	
	@Instance("Galacticraft|Venus")
	public static GalacticraftVenus instance;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		new GCVenusConfigManager(new File(event.getModConfigurationDirectory(), "Galacticraft/venus.conf"));
		
		try
		{
			ServerPlayerAPI.register("GalacticraftVenus", GCVenusPlayerBaseServer.class);
		}
		catch(Exception e)
		{
			FMLLog.severe("PLAYER API NOT INSTALLED.");
			FMLLog.severe("Galacticraft Venus will now fail to load.");
			e.printStackTrace();
		}
	}
	
	@Init
	public void init(FMLInitializationEvent event)
	{
		DimensionManager.registerProviderType(GCVenusConfigManager.dimensionIDVenus, GCVenusWorldProvider.class, true);
		DimensionManager.registerDimension(GCVenusConfigManager.dimensionIDVenus, GCVenusConfigManager.dimensionIDVenus);
	}
}
