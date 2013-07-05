package micdoodle8.mods.galacticraft.asm;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import mekanism.api.GasTransmission;
import micdoodle8.mods.galacticraft.API.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.API.ICelestialBody;
import micdoodle8.mods.galacticraft.API.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.GCCoreCompatibilityManager;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GCCoreCreativeTab;
import micdoodle8.mods.galacticraft.core.GCCoreEvents;
import micdoodle8.mods.galacticraft.core.GCCorePlanetOverworld;
import micdoodle8.mods.galacticraft.core.GCCorePlanetSun;
import micdoodle8.mods.galacticraft.core.GCCoreThreadRequirementMissing;
import micdoodle8.mods.galacticraft.core.GCLog;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.command.GCCoreCommandSpaceStationAddOwner;
import micdoodle8.mods.galacticraft.core.command.GCCoreCommandSpaceStationRemoveOwner;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreOrbitTeleportType;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreOverworldTeleportType;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreWorldProviderSpaceStation;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.network.GCCoreConnectionHandler;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerServer;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketManager;
import micdoodle8.mods.galacticraft.core.recipe.GCCoreRecipeManager;
import micdoodle8.mods.galacticraft.core.schematic.GCCoreSchematicAdd;
import micdoodle8.mods.galacticraft.core.schematic.GCCoreSchematicMoonBuggy;
import micdoodle8.mods.galacticraft.core.schematic.GCCoreSchematicRocketT1;
import micdoodle8.mods.galacticraft.core.tick.GCCoreTickHandlerCommon;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.liquids.LiquidContainerData;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import universalelectricity.prefab.ore.OreGenerator;
import basiccomponents.common.BasicComponents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.common.versioning.VersionRange;
import cpw.mods.fml.relauncher.Side;

@NetworkMod(
        channels = { GalacticraftCore.CHANNEL }, 
        clientSideRequired = true, 
        serverSideRequired = false, 
        connectionHandler = GCCoreConnectionHandler.class, 
        packetHandler = GCCorePacketManager.class
    )
public class GalacticraftModContainer extends DummyModContainer
{
    public GalacticraftModContainer()
    {
        super(new ModMetadata());
        ModMetadata meta    = getMetadata();
        meta.modId          = GalacticraftCore.MODID;
        meta.name           = GalacticraftCore.NAME;
        meta.logoFile       = "/micdoodle8/mods/galacticraft/logo.png";
        meta.updateUrl      = "http://www.micdoodle8.com/";
        meta.credits        = "fishtaco - World Gen Code. Jackson Cordes - Music. crummy194 - Models.";
        meta.description    = "An advanced space travel mod for Minecraft 1.6";
        meta.version        = GalacticraftCore.LOCALMAJVERSION + "." + GalacticraftCore.LOCALMINVERSION + "." + GalacticraftCore.LOCALBUILDVERSION;
        meta.authorList     = Arrays.asList("micdoodle8");
        meta.url            = "http://www.micdoodle8.com/";
    }
    
    @Override
    public List<ArtifactVersion> getDependencies()
    {
        LinkedList<ArtifactVersion> deps = new LinkedList<ArtifactVersion>();
        deps.add(VersionParser.parseVersionReference("required-after:Forge@[8.9.0.762,)"));
        return deps;
    }
    
    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(this);
        return true;
    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent event)
    {
        GalacticraftCore.moon.preLoad(event);

        new GCCoreConfigManager(new File(event.getModConfigurationDirectory(), GalacticraftCore.CONFIG_FILE));

        GalacticraftCore.TEXTURE_SUFFIX = GCCoreConfigManager.hiresTextures ? "_32" : "";

        GCCoreBlocks.initBlocks();
        GCCoreBlocks.registerBlocks();
        GCCoreBlocks.setHarvestLevels();

        GCCoreItems.initItems();
        GCCoreItems.registerHarvestLevels();

        if (GCCoreConfigManager.loadBC.getBoolean(false))
        {
            BasicComponents.registerTileEntities();
            BasicComponents.requestAll(GalacticraftCore.instance);
            BasicComponents.register(GalacticraftCore.CHANNELENTITIES);
            
            if (GCCoreConfigManager.disableOreGenTin && BasicComponents.generationOreTin != null)
            {
                OreGenerator.removeOre(BasicComponents.generationOreTin);
            }
            
            if (GCCoreConfigManager.disableOreGenCopper && BasicComponents.generationOreCopper != null)
            {
                OreGenerator.removeOre(BasicComponents.generationOreCopper);
            }
        }

        GalacticraftCore.proxy.preInit(event);
    }

    @Subscribe
    public void init(FMLInitializationEvent event)
    {
        GalacticraftCore.galacticraftTab = new GCCoreCreativeTab(CreativeTabs.getNextID(), GalacticraftCore.CHANNEL, GCCoreItems.spaceship.itemID, 0);
        
        GalacticraftCore.overworld = new GCCorePlanetOverworld();
        GalacticraftRegistry.registerCelestialBody(GalacticraftCore.overworld);
        GalacticraftCore.sun = new GCCorePlanetSun();
        GalacticraftRegistry.registerCelestialBody(GalacticraftCore.sun);
        GalacticraftRegistry.registerGalaxy(GalacticraftCore.galaxyMilkyWay);

        DimensionManager.registerProviderType(GCCoreConfigManager.idDimensionOverworldOrbit, GCCoreWorldProviderSpaceStation.class, false);

        GalacticraftCore.proxy.init(event);

        GalacticraftRegistry.registerTeleportType(WorldProviderSurface.class, new GCCoreOverworldTeleportType());
        GalacticraftRegistry.registerTeleportType(GCCoreWorldProviderSpaceStation.class, new GCCoreOrbitTeleportType());
        
        int languages = 0;

        for (String language : GalacticraftCore.LANGUAGES_SUPPORTED)
        {
            LanguageRegistry.instance().loadLocalization(GalacticraftCore.LANGUAGE_PATH + language + ".lang", language, false);

            if (LanguageRegistry.instance().getStringLocalization("children", language) != "")
            {
                try
                {
                    String[] children = LanguageRegistry.instance().getStringLocalization("children", language).split(",");

                    for (String child : children)
                    {
                        if (child != "" || child != null)
                        {
                            LanguageRegistry.instance().loadLocalization(GalacticraftCore.LANGUAGE_PATH + language + ".lang", child, false);
                            languages++;
                        }
                    }
                }
                catch (Exception e)
                {
                    FMLLog.severe("Failed to load a child language file.");
                    e.printStackTrace();
                }
            }

            languages++;
        }

        GCLog.info("Galacticraft Loaded: " + languages + " Languages.");

        GalacticraftCore.moon.load(event);

        LiquidDictionary.getOrCreateLiquid("Oil", new LiquidStack(GCCoreBlocks.crudeOilStill, 1));
        LiquidDictionary.getOrCreateLiquid("Fuel", new LiquidStack(GCCoreItems.fuel, 1));

        float f = LiquidContainerRegistry.BUCKET_VOLUME * 2.0F / GCCoreItems.fuelCanister.getMaxDamage();

        for (int i = GCCoreItems.fuelCanister.getMaxDamage() - 1; i > 0; i--)
        {
            final float f1 = GCCoreItems.fuelCanister.getMaxDamage() - i;

            LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Fuel", i == 1 ? 2000 : MathHelper.floor_float(f * f1 * 1.017F)), new ItemStack(GCCoreItems.fuelCanister, 1, i), new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.fuelCanister.getMaxDamage())));
        }

        f = LiquidContainerRegistry.BUCKET_VOLUME * 2.0F / GCCoreItems.oilCanister.getMaxDamage();

        for (int i = GCCoreItems.oilCanister.getMaxDamage() - 1; i > 0; i--)
        {
            final float f1 = GCCoreItems.oilCanister.getMaxDamage() - i;

            LiquidContainerRegistry.registerLiquid(new LiquidContainerData(LiquidDictionary.getLiquid("Oil", MathHelper.floor_float(f * f1 * 1.017F)), new ItemStack(GCCoreItems.oilCanister, 1, i), new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.oilCanister.getMaxDamage())));
        }

        SchematicRegistry.registerSchematicRecipe(new GCCoreSchematicRocketT1());
        SchematicRegistry.registerSchematicRecipe(new GCCoreSchematicMoonBuggy());
        SchematicRegistry.registerSchematicRecipe(new GCCoreSchematicAdd());

        GasTransmission.register();

        GalacticraftCore.registerCreatures();
        GalacticraftCore.registerOtherEntities();
        MinecraftForge.EVENT_BUS.register(new GCCoreEvents());
        NetworkRegistry.instance().registerChannel(new GCCorePacketManager(), GalacticraftCore.CHANNELENTITIES, Side.CLIENT);
    }

    @Subscribe
    public void postInit(FMLPostInitializationEvent event)
    {
        GalacticraftCore.moon.postLoad(event);
        
        for (ICelestialBody celestialBody : GalacticraftRegistry.getCelestialBodies())
        {
            if (celestialBody.autoRegister())
            {
                DimensionManager.registerProviderType(celestialBody.getDimensionID(), celestialBody.getWorldProvider(), false);
            }
        }
        
        GCCoreCompatibilityManager.checkForCompatibleMods();

        GalacticraftCore.registerTileEntities();
        
        GCCoreRecipeManager.loadRecipes();

        NetworkRegistry.instance().registerGuiHandler(this, GalacticraftCore.proxy);

        GalacticraftCore.proxy.postInit(event);
        GalacticraftCore.proxy.registerRenderInformation();

        GCCoreThreadRequirementMissing.startCheck();
    }

    @Subscribe
    public void serverInit(FMLServerStartedEvent event)
    {
        GalacticraftCore.moon.serverInit(event);

        GCCoreUtil.checkVersion(Side.SERVER);
        TickRegistry.registerTickHandler(new GCCoreTickHandlerCommon(), Side.SERVER);
        NetworkRegistry.instance().registerChannel(new GCCorePacketHandlerServer(), GalacticraftCore.CHANNEL, Side.SERVER);
    }

    @Subscribe
    public void serverStarting(FMLServerStartingEvent event)
    {
        GalacticraftCore.moon.serverStarting(event);
        event.registerServerCommand(new GCCoreCommandSpaceStationAddOwner());
        event.registerServerCommand(new GCCoreCommandSpaceStationRemoveOwner());
        WorldUtil.registerSpaceStations(event.getServer().worldServerForDimension(0).getSaveHandler().getMapFileFromName("dummy").getParentFile());

        for (ICelestialBody celestialBody : GalacticraftRegistry.getCelestialBodies())
        {
            if (celestialBody.autoRegister())
            {
                WorldUtil.registerPlanet(celestialBody.getDimensionID(), true);
            }
        }
    }

    @Subscribe
    public void unregisterDims(FMLServerStoppedEvent var1)
    {
        WorldUtil.unregisterPlanets();
        WorldUtil.unregisterSpaceStations();
    }
    
    @Override
    public VersionRange acceptableMinecraftVersionRange()
    {
        return VersionParser.parseRange(GalacticraftPlugin.mcVersion);
    }
}
