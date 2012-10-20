package micdoodle8.mods.galacticraft.moon;

import java.io.File;
import java.util.EnumSet;

import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GCCoreEntityArrow;
import micdoodle8.mods.galacticraft.core.GCCoreEntityMeteor;
import micdoodle8.mods.galacticraft.core.GCCoreEntitySpaceship;
import micdoodle8.mods.galacticraft.core.GCCoreLocalization;
import micdoodle8.mods.galacticraft.core.GalacticraftCore.CommonTickHandler;
import micdoodle8.mods.galacticraft.mars.GCMarsBlocks;
import net.minecraft.src.ServerPlayerAPI;
import net.minecraft.src.World;
import net.minecraft.src.WorldProviderSurface;
import net.minecraftforge.common.DimensionManager;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.TickRegistry;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GalacticraftMoon
{
	public static GCCoreLocalization lang;
	
	public void preInit(FMLPreInitializationEvent event)
	{
		new GCMoonConfigManager(new File(event.getModConfigurationDirectory(), "Galacticraft/moon.conf"));
		
		lang = new GCCoreLocalization("micdoodle8/mods/galacticraft/moon/client");
		
		GCMoonBlocks.initBlocks();
		GCMoonBlocks.registerBlocks();
		GCMoonBlocks.setHarvestLevels();
		GCMoonBlocks.addNames();
		
		try
		{
			ServerPlayerAPI.register("GalacticraftMoon", GCMoonPlayerBaseServer.class);
		}
		catch(Exception e)
		{
			FMLLog.severe("PLAYER API NOT INSTALLED.");
			FMLLog.severe("Galacticraft Moon (core) will now fail to load.");
			e.printStackTrace();
		}
	}
	
	public void init(FMLInitializationEvent event)
	{
		DimensionManager.registerProviderType(GCMoonConfigManager.dimensionIDMoon, GCMoonWorldProvider.class, true);
		DimensionManager.registerDimension(GCMoonConfigManager.dimensionIDMoon, GCMoonConfigManager.dimensionIDMoon);
	}
	
	public void serverInit(FMLServerStartedEvent event)
	{
        TickRegistry.registerTickHandler(new CommonTickHandler(), Side.SERVER);
	}
	
	public class CommonTickHandler implements ITickHandler
	{
		@Override
		public void tickStart(EnumSet<TickType> type, Object... tickData) 
		{
			if (type.equals(EnumSet.of(TickType.WORLD)))
            {
				World world = (World) tickData[0];
            }
		}

		@Override
		public void tickEnd(EnumSet<TickType> type, Object... tickData) { }

		@Override
		public EnumSet<TickType> ticks() 
		{
			return EnumSet.of(TickType.WORLD);
		}

		@Override
		public String getLabel() 
		{
			return "Galacticraft Moon Common";
		}
	}
}
