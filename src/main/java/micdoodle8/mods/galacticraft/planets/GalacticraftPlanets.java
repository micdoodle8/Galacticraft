package micdoodle8.mods.galacticraft.planets;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDeconstructor;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.ConfigManagerAsteroids;
import micdoodle8.mods.galacticraft.planets.mars.ConfigManagerMars;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.venus.ConfigManagerVenus;
import micdoodle8.mods.galacticraft.planets.venus.VenusModule;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Mod(modid = Constants.MOD_ID_PLANETS, name = GalacticraftPlanets.NAME, version = Constants.COMBINEDVERSION, useMetadata = true, acceptedMinecraftVersions = Constants.MCVERSION, dependencies = "required-after:" + Constants.MOD_ID_CORE + ";", guiFactory = "micdoodle8.mods.galacticraft.planets.ConfigGuiFactoryPlanets")
public class GalacticraftPlanets
{
    public static final String NAME = "Galacticraft Planets";
    private File GCPlanetsSource;

    @Instance(Constants.MOD_ID_PLANETS)
    public static GalacticraftPlanets instance;

    public static List<IPlanetsModule> commonModules = new ArrayList<IPlanetsModule>();
    public static List<IPlanetsModuleClient> clientModules = new ArrayList<IPlanetsModuleClient>();

    public static final String ASSET_PREFIX = "galacticraftplanets";
    public static final String TEXTURE_PREFIX = ASSET_PREFIX + ":";

    @SidedProxy(clientSide = "micdoodle8.mods.galacticraft.planets.PlanetsProxyClient", serverSide = "micdoodle8.mods.galacticraft.planets.PlanetsProxy")
    public static PlanetsProxy proxy;

    public static Map<String, List<String>> propOrder = new TreeMap<>();
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        GCPlanetsSource = event.getSourceFile();
        this.initModInfo(event.getModMetadata());
        MinecraftForge.EVENT_BUS.register(this);

        //Initialise configs, converting mars.conf + asteroids.conf to planets.conf if necessary
        File oldMarsConf = new File(event.getModConfigurationDirectory(), "Galacticraft/mars.conf");
        File newPlanetsConf = new File(event.getModConfigurationDirectory(), "Galacticraft/planets.conf");
        boolean update = false;
        if (oldMarsConf.exists())
        {
            oldMarsConf.renameTo(newPlanetsConf);
            update = true;
        }
        this.configSyncStart();
        new ConfigManagerMars(newPlanetsConf, update);
        new ConfigManagerAsteroids(new File(event.getModConfigurationDirectory(), "Galacticraft/asteroids.conf"));
        new ConfigManagerVenus(new File(event.getModConfigurationDirectory(), "Galacticraft/venus.conf"));
        this.configSyncEnd(true);

        GalacticraftPlanets.commonModules.add(new MarsModule());
        GalacticraftPlanets.commonModules.add(new AsteroidsModule());
        GalacticraftPlanets.commonModules.add(new VenusModule());
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
        TileEntityDeconstructor.initialiseRecipeListPlanets();
        if (event.getSide() == Side.SERVER) this.loadLanguagePlanets("en_US");
    }

    public void loadLanguagePlanets(String lang)
    {
        GCCoreUtil.loadLanguage(lang, GalacticraftPlanets.ASSET_PREFIX, GCPlanetsSource);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        GalacticraftPlanets.proxy.serverStarting(event);
    }

    @EventHandler
    public void serverInit(FMLServerStartedEvent event)
    {
        GalacticraftPlanets.proxy.serverInit(event);
    }

    public static void spawnParticle(String particleID, Vector3 position, Vector3 motion, Object... extraData)
    {
        for (IPlanetsModuleClient module : GalacticraftPlanets.clientModules)
        {
            module.spawnParticle(particleID, position, motion, extraData);
        }
    }

    public static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        
        //Get the last planet to be configured only, as all will reference and re-use the same planets.conf config file
        IPlanetsModule module = GalacticraftPlanets.commonModules.get(GalacticraftPlanets.commonModules.size() - 1);
        list.addAll(new ConfigElement(module.getConfiguration().getCategory(Constants.CONFIG_CATEGORY_ENTITIES)).getChildElements());
        list.addAll(new ConfigElement(module.getConfiguration().getCategory(Constants.CONFIG_CATEGORY_ACHIEVEMENTS)).getChildElements());
        list.addAll(new ConfigElement(module.getConfiguration().getCategory(Constants.CONFIG_CATEGORY_GENERAL)).getChildElements());
        list.addAll(new ConfigElement(module.getConfiguration().getCategory(Constants.CONFIG_CATEGORY_WORLDGEN)).getChildElements());
        list.addAll(new ConfigElement(module.getConfiguration().getCategory(Constants.CONFIG_CATEGORY_DIMENSIONS)).getChildElements());
        list.addAll(new ConfigElement(module.getConfiguration().getCategory(Constants.CONFIG_CATEGORY_SCHEMATIC)).getChildElements());

        return list;
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent event)
    {
        if (event.getModID().equals(Constants.MOD_ID_PLANETS))
        {
            this.configSyncStart();
            for (IPlanetsModule module : GalacticraftPlanets.commonModules)
            {
                module.syncConfig();
            }
            this.configSyncEnd(false);
        }
    }

    private void configSyncEnd(boolean load)
    {
        //Cleanup older GC config files
        ConfigManagerCore.cleanConfig(ConfigManagerMars.config, propOrder);

        //Always save - this is last to be called both at load time and at mid-game
        if (ConfigManagerMars.config.hasChanged())
        {
            ConfigManagerMars.config.save();
        }
    }

    private void configSyncStart()
    {
        propOrder.clear();
    }

    public static void finishProp(Property prop, String currentCat)
    {
        if (propOrder.get(currentCat) == null)
        {
            propOrder.put(currentCat, new ArrayList<String>());
        }
        propOrder.get(currentCat).add(prop.getName());
    }

    private void initModInfo(ModMetadata info)
    {
        info.autogenerated = false;
        info.modId = Constants.MOD_ID_PLANETS;
        info.name = GalacticraftPlanets.NAME;
        info.version = Constants.COMBINEDVERSION;
        info.description = "Planets addon for Galacticraft.";
        info.url = "https://micdoodle8.com/";
        info.authorList = Arrays.asList("micdoodle8", "radfast", "EzerArch", "fishtaco", "SpaceViking", "SteveKunG");
        info.logoFile = "assets/galacticraftplanets/galacticraft_logo.png";
    }

}
