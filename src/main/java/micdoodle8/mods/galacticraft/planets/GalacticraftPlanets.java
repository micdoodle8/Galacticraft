package micdoodle8.mods.galacticraft.planets;

import java.io.File;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.GCCreativeTab;
import micdoodle8.mods.galacticraft.planets.asteroids.ModuleAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.proxy.CommonProxyAsteroids;
import micdoodle8.mods.galacticraft.planets.mars.ModuleMars;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.proxy.CommonProxyMars;
import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

/**
 * GalacticraftMars.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@Mod(name = GalacticraftPlanets.NAME, useMetadata = true, modid = GalacticraftPlanets.MOD_ID, dependencies = "required-after:" + GalacticraftCore.MOD_ID + ";required-after:Forge@[7.0,);required-after:FML@[5.0.5,)")
public class GalacticraftPlanets
{
	public static final String NAME = "Galacticraft Planets";
	public static final String MOD_ID = "GalacticraftPlanets";

	@SidedProxy(clientSide = "micdoodle8.mods.galacticraft.planets.mars.proxy.ClientProxyMars", serverSide = "micdoodle8.mods.galacticraft.planets.mars.proxy.CommonProxyMars")
	public static CommonProxyMars proxyMars;

	@SidedProxy(clientSide = "micdoodle8.mods.galacticraft.planets.asteroids.proxy.ClientProxyAsteroids", serverSide = "micdoodle8.mods.galacticraft.planets.asteroids.proxy.CommonProxyAsteroids")
	public static CommonProxyAsteroids proxyAsteroids;

	@Instance(GalacticraftPlanets.MOD_ID)
	public static GalacticraftPlanets instance;
	
	public static IPlanetsModule moduleMars = new ModuleMars();
	public static IPlanetsModule moduleAsteroids = new ModuleAsteroids();

	public static CreativeTabs creativeTab;

	public static final String CONFIG_FILE = "Galacticraft/planets.conf";
	
	public static final String ASSET_DOMAIN = "galacticraftplanets";
	public static final String TEXTURE_PREFIX = GalacticraftPlanets.ASSET_DOMAIN + ":";

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		ConfigManagerPlanets.setDefaultValues(new File(event.getModConfigurationDirectory(), GalacticraftPlanets.CONFIG_FILE));
		moduleMars.preInit(event);
		moduleAsteroids.preInit(event);
		GalacticraftPlanets.proxyMars.preInit(event);
		GalacticraftPlanets.proxyAsteroids.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		moduleMars.init(event);
		moduleAsteroids.init(event);
		GalacticraftPlanets.proxyMars.init(event);
		GalacticraftPlanets.proxyAsteroids.init(event);
		GalacticraftPlanets.creativeTab = new GCCreativeTab(CreativeTabs.getNextID(), GalacticraftPlanets.MOD_ID, MarsItems.spaceship, 5);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		moduleMars.postInit(event);
		moduleAsteroids.postInit(event);
		GalacticraftPlanets.proxyMars.postInit(event);
		GalacticraftPlanets.proxyAsteroids.postInit(event);
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		moduleMars.serverStarting(event);
		moduleAsteroids.serverStarting(event);
	}
	
	/**
	 * Used internally to load different parts of the planets pack.
	 */
	public static interface IPlanetsModule
	{
		public void preInit(FMLPreInitializationEvent event);
		
		public void init(FMLInitializationEvent event);
		
		public void postInit(FMLPostInitializationEvent event);
		
		public void serverStarting(FMLServerStartingEvent event);
	}
}
