package micdoodle8.mods.galacticraft.moon;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubMod;
import micdoodle8.mods.galacticraft.moon.blocks.GCMoonBlocks;
import micdoodle8.mods.galacticraft.moon.dimension.GCMoonWorldProvider;
import micdoodle8.mods.galacticraft.moon.entities.GCMoonPlayerHandler;
import micdoodle8.mods.galacticraft.moon.items.GCMoonItems;
import net.minecraft.src.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GalacticraftMoon implements IGalacticraftSubMod
{
	public static List moonPlayers = new ArrayList();
	public static List gcMoonPlayers = new ArrayList();
	
	public void preLoad(FMLPreInitializationEvent event)
	{
		new GCMoonConfigManager(new File(event.getModConfigurationDirectory(), "Galacticraft/moon.conf"));
		
		GCMoonBlocks.initBlocks();
		GCMoonBlocks.registerBlocks();
		GCMoonBlocks.setHarvestLevels();
		
		GCMoonItems.initItems();
	}
	
	public void load(FMLInitializationEvent event)
	{
		DimensionManager.registerProviderType(GCMoonConfigManager.dimensionIDMoon, GCMoonWorldProvider.class, true);
		DimensionManager.registerDimension(GCMoonConfigManager.dimensionIDMoon, GCMoonConfigManager.dimensionIDMoon);
		GameRegistry.registerPlayerTracker(new GCMoonPlayerHandler());
	}

	public void postLoad(FMLPostInitializationEvent event) 
	{
		
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
				final World world = (World) tickData[0];
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

	@Override
	public String getDimensionName() 
	{
		return "Moon";
	}

	@Override
	public boolean reachableDestination() 
	{
		return true;
	}
}
