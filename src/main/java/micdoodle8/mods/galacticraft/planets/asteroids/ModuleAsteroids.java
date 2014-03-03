package micdoodle8.mods.galacticraft.planets.asteroids;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.planets.ConfigManagerPlanets;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets.IPlanetsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.TeleportTypeAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.WorldProviderAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class ModuleAsteroids implements IPlanetsModule
{
	public static Planet planetAsteroids;
	
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		AsteroidsItems.initItems();
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		this.registerTileEntities();
		this.registerCreatures();
		this.registerOtherEntities();

		ModuleAsteroids.planetAsteroids = (Planet) new Planet("asteroids").setParentGalaxy(GalacticraftCore.galaxyBlockyWay);
		ModuleAsteroids.planetAsteroids.setDimensionInfo(ConfigManagerPlanets.dimensionIDAsteroids, WorldProviderAsteroids.class);
		
		GalaxyRegistry.registerPlanet(ModuleAsteroids.planetAsteroids);
		GalacticraftRegistry.registerTeleportType(WorldProviderAsteroids.class, new TeleportTypeAsteroids());
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
	}

	@Override
	public void serverStarting(FMLServerStartingEvent event)
	{
		;
	}

	public void registerTileEntities()
	{
	}

	public void registerCreatures()
	{
	}

	public void registerOtherEntities()
	{
	}
}
