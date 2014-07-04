package micdoodle8.mods.galacticraft.planets;

import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModuleClient;
import micdoodle8.mods.galacticraft.planets.mars.MarsModuleClient;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class PlanetsProxyClient extends PlanetsProxy
{
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		GalacticraftPlanets.clientModules.put(GalacticraftPlanets.MODULE_KEY_MARS, new MarsModuleClient());
		GalacticraftPlanets.clientModules.put(GalacticraftPlanets.MODULE_KEY_ASTEROIDS, new AsteroidsModuleClient());

		super.preInit(event);

		for (IPlanetsModuleClient module : GalacticraftPlanets.clientModules.values())
		{
			module.preInit(event);
		}
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);

		for (IPlanetsModuleClient module : GalacticraftPlanets.clientModules.values())
		{
			module.init(event);
		}
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		super.postInit(event);

		for (IPlanetsModuleClient module : GalacticraftPlanets.clientModules.values())
		{
			module.postInit(event);
		}
	}

	@Override
	public void serverStarting(FMLServerStartingEvent event)
	{
		super.serverStarting(event);
	}
}
