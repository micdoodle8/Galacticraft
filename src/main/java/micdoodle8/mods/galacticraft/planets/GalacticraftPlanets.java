package micdoodle8.mods.galacticraft.planets;

import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import net.minecraft.block.Block;
import net.minecraftforge.common.config.ConfigElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod(name = GalacticraftPlanets.NAME, version = Constants.LOCALMAJVERSION + "." + Constants.LOCALMINVERSION + "." + Constants.LOCALBUILDVERSION, useMetadata = true, modid = Constants.MOD_ID_PLANETS, dependencies = "required-after:" + Constants.MOD_ID_CORE + ";", guiFactory = "micdoodle8.mods.galacticraft.planets.ConfigGuiFactoryPlanets")
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
        GalacticraftPlanets.commonModules.put(GalacticraftPlanets.MODULE_KEY_MARS, new MarsModule());
        GalacticraftPlanets.commonModules.put(GalacticraftPlanets.MODULE_KEY_ASTEROIDS, new AsteroidsModule());
        GalacticraftPlanets.proxy.preInit(event);
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

        for (IPlanetsModule module : GalacticraftPlanets.commonModules.values())
        {
            list.addAll(new ConfigElement(module.getConfiguration().getCategory(Constants.CONFIG_CATEGORY_DIMENSIONS)).getChildElements());
        }

        for (IPlanetsModule module : GalacticraftPlanets.commonModules.values())
        {
            list.addAll(new ConfigElement(module.getConfiguration().getCategory(Constants.CONFIG_CATEGORY_ENTITIES)).getChildElements());
        }

        for (IPlanetsModule module : GalacticraftPlanets.commonModules.values())
        {
            list.addAll(new ConfigElement(module.getConfiguration().getCategory(Constants.CONFIG_CATEGORY_ACHIEVEMENTS)).getChildElements());
        }

        for (IPlanetsModule module : GalacticraftPlanets.commonModules.values())
        {
            list.addAll(new ConfigElement(module.getConfiguration().getCategory(Constants.CONFIG_CATEGORY_ENTITIES)).getChildElements());
        }

        for (IPlanetsModule module : GalacticraftPlanets.commonModules.values())
        {
            list.addAll(new ConfigElement(module.getConfiguration().getCategory(Constants.CONFIG_CATEGORY_GENERAL)).getChildElements());
        }

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
