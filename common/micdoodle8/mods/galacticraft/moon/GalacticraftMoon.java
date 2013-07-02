package micdoodle8.mods.galacticraft.moon;

import java.io.File;
import java.util.EnumSet;
import micdoodle8.mods.galacticraft.API.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.API.IMoon;
import micdoodle8.mods.galacticraft.core.GCCoreCreativeTab;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.moon.blocks.GCMoonBlocks;
import micdoodle8.mods.galacticraft.moon.dimension.GCMoonTeleportType;
import micdoodle8.mods.galacticraft.moon.dimension.GCMoonWorldProvider;
import micdoodle8.mods.galacticraft.moon.items.GCMoonItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.DimensionManager;
import universalelectricity.prefab.TranslationHelper;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GalacticraftMoon
{
    public static final String NAME = "Galacticraft Moon";
    public static final String MODID = "GalacticraftMoon";

    public static final String FILE_PATH = "/micdoodle8/mods/galacticraft/moon/";
    public static final String CLIENT_PATH = "client/";
    public static final String LANGUAGE_PATH = GalacticraftMoon.FILE_PATH + GalacticraftMoon.CLIENT_PATH + "lang/";
    public static final String BLOCK_TEXTURE_FILE = GalacticraftMoon.FILE_PATH + GalacticraftMoon.CLIENT_PATH + "blocks/moon.png";
    public static final String ITEM_TEXTURE_FILE = GalacticraftMoon.FILE_PATH + GalacticraftMoon.CLIENT_PATH + "items/moon.png";
    public static final String CONFIG_FILE = "Galacticraft/moon.conf";
    private static final String[] LANGUAGES_SUPPORTED = new String[] { "cz_CZE", "de_DE", "en_GB", "en_US", "es_ES", "fi_FI", "fr_FR", "ja_JP", "nl_NL", "pl_PL", "ru_RU", "zh_CN" };

    public static GCCoreCreativeTab galacticraftMoonTab;
    
    public static IMoon celestialBodyMoon;

    public void preLoad(FMLPreInitializationEvent event)
    {
        new GCMoonConfigManager(new File(event.getModConfigurationDirectory(), GalacticraftMoon.CONFIG_FILE));

        GCMoonBlocks.initBlocks();
        GCMoonBlocks.registerBlocks();
        GCMoonBlocks.setHarvestLevels();

        GCMoonItems.initItems();
    }

    public void load(FMLInitializationEvent event)
    {
        GalacticraftMoon.galacticraftMoonTab = new GCCoreCreativeTab(CreativeTabs.getNextID(), GalacticraftMoon.MODID, GCMoonBlocks.blockMoon.blockID, 5);
        
        GalacticraftMoon.celestialBodyMoon = new GCMoonCelestialBody();
        GalacticraftRegistry.registerCelestialBody(GalacticraftMoon.celestialBodyMoon);

        System.out.println("Galacticraft Moon Loaded: " + TranslationHelper.loadLanguages(GalacticraftMoon.LANGUAGE_PATH, GalacticraftMoon.LANGUAGES_SUPPORTED) + " Languages.");

        GalacticraftRegistry.registerTeleportType(GCMoonWorldProvider.class, new GCMoonTeleportType());

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

    public void serverStarting(FMLServerStartingEvent event)
    {
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
        public void tickEnd(EnumSet<TickType> type, Object... tickData)
        {
        }

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
