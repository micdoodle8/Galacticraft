package micdoodle8.mods.galacticraft.planets;

import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.ConfigManagerAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.BiomeGenBaseAsteroids;
import micdoodle8.mods.galacticraft.planets.mars.ConfigManagerMars;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.entities.MFRSpawnHandlerSlimeling;
import micdoodle8.mods.galacticraft.planets.mars.world.gen.BiomeGenBaseMars;
import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.config.ConfigElement;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import powercrystals.minefactoryreloaded.api.FactoryRegistry;

@Mod(modid = Constants.MOD_ID_PLANETS, name = GalacticraftPlanets.NAME, version = Constants.LOCALMAJVERSION + "." + Constants.LOCALMINVERSION + "." + Constants.LOCALBUILDVERSION, acceptedMinecraftVersions = "[1.7.2],[1.7.10]", useMetadata = true, dependencies = "required-after:" + Constants.MOD_ID_CORE + ";", guiFactory = "micdoodle8.mods.galacticraft.planets.ConfigGuiFactoryPlanets")
public class GalacticraftPlanets
{
    public static final String NAME = "Galacticraft Planets";

    @Instance(Constants.MOD_ID_PLANETS)
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
        FMLCommonHandler.instance().bus().register(this);
        
        //Initialise configs, converting mars.conf + asteroids.conf to planets.conf if necessary
        File oldMarsConf = new File(event.getModConfigurationDirectory(), "Galacticraft/mars.conf");
        File newPlanetsConf = new File(event.getModConfigurationDirectory(), "Galacticraft/planets.conf");
        boolean update = false;
        if (oldMarsConf.exists())
        {
        	oldMarsConf.renameTo(newPlanetsConf);
        	update = true;
        }
        new ConfigManagerMars(newPlanetsConf, update);
        new ConfigManagerAsteroids(new File(event.getModConfigurationDirectory(), "Galacticraft/asteroids.conf"));

        GalacticraftPlanets.commonModules.put(GalacticraftPlanets.MODULE_KEY_MARS, new MarsModule());
        GalacticraftPlanets.commonModules.put(GalacticraftPlanets.MODULE_KEY_ASTEROIDS, new AsteroidsModule());
        GalacticraftPlanets.proxy.preInit(event);
        
        //Force initialisation of GC biome types in preinit (after config load) - this helps BiomeTweaker
        BiomeGenBase biomeMarsPreInit = BiomeGenBaseMars.marsFlat;
        BiomeGenBase biomeAsteroidsPreInit = BiomeGenBaseAsteroids.asteroid;
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        GalacticraftPlanets.proxy.init(event);
        NetworkRegistry.INSTANCE.registerGuiHandler(GalacticraftPlanets.instance, GalacticraftPlanets.proxy);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        GalacticraftPlanets.proxy.postInit(event);
        try {
        	if (Loader.isModLoaded("MineFactoryReloaded"))
        		FactoryRegistry.sendMessage("registerSpawnHandler", new MFRSpawnHandlerSlimeling());
        } catch (Exception e)
        {
        	GCLog.severe("Error when attempting to register Slimeling auto-spawnhandler in MFR");
        	GCLog.exception(e);
        }
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        GalacticraftPlanets.proxy.serverStarting(event);
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

    public static void spawnParticle(String particleID, Vector3 position, Vector3 motion, Object... extraData)
    {
        for (IPlanetsModuleClient module : GalacticraftPlanets.clientModules.values())
        {
            module.spawnParticle(particleID, position, motion, extraData);
        }
    }

    public static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        
        //Get the last planet to be configured only, as all will reference and re-use the same planets.conf config file
        IPlanetsModule module = GalacticraftPlanets.commonModules.get(GalacticraftPlanets.commonModules.size() - 1);
        list.addAll(new ConfigElement(module.getConfiguration().getCategory(Constants.CONFIG_CATEGORY_DIMENSIONS)).getChildElements());
        list.addAll(new ConfigElement(module.getConfiguration().getCategory(Constants.CONFIG_CATEGORY_ENTITIES)).getChildElements());
        list.addAll(new ConfigElement(module.getConfiguration().getCategory(Constants.CONFIG_CATEGORY_ACHIEVEMENTS)).getChildElements());
        list.addAll(new ConfigElement(module.getConfiguration().getCategory(Constants.CONFIG_CATEGORY_ENTITIES)).getChildElements());
        list.addAll(new ConfigElement(module.getConfiguration().getCategory(Constants.CONFIG_CATEGORY_GENERAL)).getChildElements());

        return list;
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent event)
    {
        if (event.modID.equals(Constants.MOD_ID_PLANETS))
        {
            for (IPlanetsModule module : GalacticraftPlanets.commonModules.values())
            {
                module.syncConfig();
            }
        }
    }
}
