package micdoodle8.mods.galacticraft.planets;

import java.util.HashMap;
import java.util.Map;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import net.minecraft.block.Block;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(name = GalacticraftPlanets.NAME, version = GalacticraftCore.LOCALMAJVERSION + "." + GalacticraftCore.LOCALMINVERSION + "." + GalacticraftCore.LOCALBUILDVERSION, useMetadata = true, modid = GalacticraftPlanets.MODID, dependencies = "required-after:" + GalacticraftCore.MODID + ";")
public class GalacticraftPlanets
{
	public static final String NAME = "Galacticraft Planets";
	// Sadly, this has to stay as GalacticraftMars for backwards compatibility
	public static final String MODID = "GalacticraftMars";

	@Instance(GalacticraftPlanets.MODID)
	public static GalacticraftPlanets instance;

	public static Map<String, IPlanetsModule> commonModules = new HashMap<String, IPlanetsModule>();
	public static Map<String, IPlanetsModuleClient> clientModules = new HashMap<String, IPlanetsModuleClient>();
	
	public static final String MODULE_KEY_MARS = "MarsModule";
	public static final String MODULE_KEY_ASTEROIDS = "AsteroidsModule";

	@SidedProxy(clientSide = "micdoodle8.mods.galacticraft.planets.PlanetsProxyClient", serverSide = "micdoodle8.mods.galacticraft.planets.PlanetsProxy")
	public static PlanetsProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		commonModules.put(MODULE_KEY_MARS, new MarsModule());
		commonModules.put(MODULE_KEY_ASTEROIDS, new AsteroidsModule());
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init(event);
		NetworkRegistry.INSTANCE.registerGuiHandler(GalacticraftPlanets.instance, GalacticraftPlanets.proxy);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		proxy.serverStarting(event);
	}
	
	public static int getBlockRenderID(Block block)
	{
		for (IPlanetsModuleClient module : GalacticraftPlanets.clientModules.values())
		{
			int id = module.getBlockRenderID(block);
			
			if (id > 1)
			{
				return id;
			}
		}
		
		return 1;
	}
	
	public static int spawnParticle(String particleID, Vector3 position, Vector3 color)
	{
		for (IPlanetsModuleClient module : GalacticraftPlanets.clientModules.values())
		{
			module.spawnParticle(particleID, position, color);
		}
		
		return 1;
	}
}
