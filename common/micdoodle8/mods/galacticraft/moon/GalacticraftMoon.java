package micdoodle8.mods.galacticraft.moon;

import java.io.File;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.core.GCCoreCreativeTab;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.moon.dimension.GCMoonTeleportType;
import micdoodle8.mods.galacticraft.moon.dimension.GCMoonWorldProvider;
import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * GalacticraftMoon.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
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

	public static GCCoreCreativeTab galacticraftMoonTab;

	public static void preLoad(FMLPreInitializationEvent event)
	{
		new GCMoonConfigManager(new File(event.getModConfigurationDirectory(), GalacticraftMoon.CONFIG_FILE));
	}

	public static void load(FMLInitializationEvent event)
	{
		GalacticraftMoon.galacticraftMoonTab = new GCCoreCreativeTab(CreativeTabs.getNextID(), GalacticraftMoon.MODID, GCCoreBlocks.blockMoon.blockID, 5);

		GalacticraftRegistry.registerCelestialBody(new GCMoonCelestialBody());

		GalacticraftRegistry.registerTeleportType(GCMoonWorldProvider.class, new GCMoonTeleportType());
	}
}
