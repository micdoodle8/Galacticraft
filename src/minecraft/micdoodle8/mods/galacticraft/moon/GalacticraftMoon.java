package micdoodle8.mods.galacticraft.moon;

import java.io.File;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubMod;
import micdoodle8.mods.galacticraft.API.IGalaxy;
import micdoodle8.mods.galacticraft.core.GCCoreCreativeTab;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.moon.blocks.GCMoonBlocks;
import micdoodle8.mods.galacticraft.moon.dimension.GCMoonWorldProvider;
import micdoodle8.mods.galacticraft.moon.entities.GCMoonPlayerBase;
import micdoodle8.mods.galacticraft.moon.items.GCMoonItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.src.ServerPlayerAPI;
import net.minecraftforge.common.DimensionManager;
import universalelectricity.prefab.TranslationHelper;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GalacticraftMoon implements IGalacticraftSubMod
{
	public static Map<String, GCMoonPlayerBase> playersServer = new HashMap<String, GCMoonPlayerBase>();

	public static final String NAME = "Galacticraft Moon";
	public static final String MODID = "GalacticraftMoon";

	public static final String FILE_PATH = "/micdoodle8/mods/galacticraft/moon/";
	public static final String CLIENT_PATH = "client/";
	public static final String LANGUAGE_PATH = FILE_PATH + CLIENT_PATH + "lang/";
	public static final String BLOCK_TEXTURE_FILE = FILE_PATH + CLIENT_PATH + "blocks/moon.png";
	public static final String ITEM_TEXTURE_FILE = FILE_PATH + CLIENT_PATH + "items/moon.png";
	public static final String CONFIG_FILE = "Galacticraft/moon.conf";
	private static final String[] LANGUAGES_SUPPORTED = new String[] { "en_US", "zh_CN" };

	public static GCCoreCreativeTab galacticraftMoonTab;

	public void preLoad(FMLPreInitializationEvent event)
	{
		new GCMoonConfigManager(new File(event.getModConfigurationDirectory(), CONFIG_FILE));

		try
		{
			ServerPlayerAPI.register(GalacticraftMoon.MODID, GCMoonPlayerBase.class);

		}
		catch (Exception e)
		{
			FMLLog.severe("PLAYER API NOT INSTALLED");
		}
		GCMoonBlocks.initBlocks();
		GCMoonBlocks.registerBlocks();
		GCMoonBlocks.setHarvestLevels();

		GCMoonItems.initItems();
	}

	public void load(FMLInitializationEvent event)
	{
		this.galacticraftMoonTab = new GCCoreCreativeTab(CreativeTabs.getNextID(), GalacticraftMoon.MODID, GCMoonBlocks.blockMoon.blockID, 5);
		
		DimensionManager.registerProviderType(GCMoonConfigManager.dimensionIDMoon, GCMoonWorldProvider.class, true);
		DimensionManager.registerDimension(GCMoonConfigManager.dimensionIDMoon, GCMoonConfigManager.dimensionIDMoon);

		System.out.println("Galacticraft Moon Loaded: " + TranslationHelper.loadLanguages(LANGUAGE_PATH, LANGUAGES_SUPPORTED) + " Languages.");

        GCMoonUtil.addCraftingRecipes();
        GCMoonUtil.addSmeltingRecipes();
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

	@Override
	public IGalaxy getParentGalaxy()
	{
		return GalacticraftCore.galaxyMilkyWay;
	}
}
