package micdoodle8.mods.galacticraft.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mekanism.api.GasTransmission;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.world.ICelestialBody;
import micdoodle8.mods.galacticraft.api.world.IGalaxy;
import micdoodle8.mods.galacticraft.api.world.IMoon;
import micdoodle8.mods.galacticraft.api.world.IPlanet;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.GCCorePlayerSP;
import micdoodle8.mods.galacticraft.core.command.GCCoreCommandSpaceStationAddOwner;
import micdoodle8.mods.galacticraft.core.command.GCCoreCommandSpaceStationRemoveOwner;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreOrbitTeleportType;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreOverworldTeleportType;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreWorldProviderSpaceStation;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityAlienVillager;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityArrow;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityBuggy;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityCreeper;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityFlag;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityLander;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityMeteor;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityOxygenBubble;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityParaChest;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityRocketT1;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeleton;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeletonBoss;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpider;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityZombie;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.network.GCCoreConnectionHandler;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerServer;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketManager;
import micdoodle8.mods.galacticraft.core.recipe.GCCoreRecipeManager;
import micdoodle8.mods.galacticraft.core.schematic.GCCoreSchematicAdd;
import micdoodle8.mods.galacticraft.core.schematic.GCCoreSchematicMoonBuggy;
import micdoodle8.mods.galacticraft.core.schematic.GCCoreSchematicRocketT1;
import micdoodle8.mods.galacticraft.core.tick.GCCoreTickHandlerServer;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAdvancedCraftingTable;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAirLock;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityBuggyFueler;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityBuggyFuelerSingle;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCargoLoader;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCargoPad;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCargoPadSingle;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCargoUnloader;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityFuelLoader;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityLandingPadSingle;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCollector;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCompressor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDetector;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDistributor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenPipe;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityParachest;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityRefinery;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntitySolar;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntitySpaceStationBase;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityTreasureChest;
import micdoodle8.mods.galacticraft.core.tile.TileEntityMulti;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.moon.GalacticraftMoon;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import universalelectricity.prefab.ore.OreGenerator;
import basiccomponents.common.BasicComponents;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
@Mod(name = GalacticraftCore.NAME, version = GalacticraftCore.LOCALMAJVERSION + "." + GalacticraftCore.LOCALMINVERSION + "." + GalacticraftCore.LOCALBUILDVERSION, useMetadata = true, modid = GalacticraftCore.MODID, dependencies = "required-after:Forge@[9.10.0.789,)")
@NetworkMod(channels = { GalacticraftCore.CHANNEL }, clientSideRequired = true, serverSideRequired = false, connectionHandler = GCCoreConnectionHandler.class, packetHandler = GCCorePacketManager.class)
public class GalacticraftCore
{
    public static final String NAME = "Galacticraft Core"; 
    public static final String MODID = "GalacticraftCore";
    public static final String CHANNEL = "GalacticraftCore";
    public static final String CHANNELENTITIES = "GCCoreEntities";

    public static final int LOCALMAJVERSION = 0; 
    public static final int LOCALMINVERSION = 1;
    public static final int LOCALBUILDVERSION = 39;
    public static int remoteMajVer;
    public static int remoteMinVer;
    public static int remoteBuildVer;

    @SidedProxy(clientSide = "micdoodle8.mods.galacticraft.core.client.ClientProxyCore", serverSide = "micdoodle8.mods.galacticraft.core.CommonProxyCore")
    public static CommonProxyCore proxy;

    @Instance(GalacticraftCore.MODID)
    public static GalacticraftCore instance;

    public static GalacticraftMoon moon = new GalacticraftMoon();

    public static Map<String, GCCorePlayerSP> playersClient = new HashMap<String, GCCorePlayerSP>();
    public static Map<String, GCCorePlayerMP> playersServer = new HashMap<String, GCCorePlayerMP>();

    public static List<IPlanet> mapPlanets = new ArrayList<IPlanet>();
    public static HashMap<IPlanet, ArrayList<IMoon>> mapMoons = new HashMap<IPlanet, ArrayList<IMoon>>();

    public static CreativeTabs galacticraftTab;

    public static final IGalaxy galaxyMilkyWay = new GCCoreGalaxyBlockyWay();

    public static final String FILE_PATH = "/micdoodle8/mods/galacticraft/core/";
    public static final String CLIENT_PATH = "client/";
    public static final String LANGUAGE_PATH = "/assets/galacticraftcore/lang/";
    public static final String BLOCK_TEXTURE_FILE = GalacticraftCore.FILE_PATH + GalacticraftCore.CLIENT_PATH + "blocks/core.png";
    public static final String ITEM_TEXTURE_FILE = GalacticraftCore.FILE_PATH + GalacticraftCore.CLIENT_PATH + "items/core.png";
    public static final String CONFIG_FILE = "Galacticraft/core.conf";
    private static final String[] LANGUAGES_SUPPORTED = new String[] { "cz_CZE", "de_DE", "en_GB", "en_US", "es_ES", "fi_FI", "fr_CA", "fr_FR", "nl_NL", "pl_PL", "ru_RU", "zh_CN", "ja_JP" };

    public static String TEXTURE_DOMAIN = "galacticraftcore";
    public static String TEXTURE_PREFIX = GalacticraftCore.TEXTURE_DOMAIN + ":";
    public static String TEXTURE_SUFFIX;

    public static boolean setSpaceStationRecipe = false;

    public static GCCorePlanetOverworld overworld;
    public static GCCorePlanetSun sun;

    public static Fluid CRUDEOIL;
    public static Fluid FUEL;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        GalacticraftCore.moon.preLoad(event);
        GalacticraftCore.proxy.preInit(event);

        new GCCoreConfigManager(new File(event.getModConfigurationDirectory(), GalacticraftCore.CONFIG_FILE));

        GalacticraftCore.TEXTURE_SUFFIX = GCCoreConfigManager.hiresTextures ? "_32" : "";

        GalacticraftCore.CRUDEOIL = new Fluid("oil").setBlockID(GCCoreConfigManager.idBlockCrudeOilStill).setViscosity(3000);
        if (!FluidRegistry.registerFluid(GalacticraftCore.CRUDEOIL))
        {
            GCLog.info("\"oil\" has already been registered as a fluid, ignoring...");
        }
        GalacticraftCore.FUEL = new Fluid("fuel").setViscosity(800);
        if (!FluidRegistry.registerFluid(GalacticraftCore.FUEL))
        {
            GCLog.info("\"fuel\" has already been registered as a fluid, ignoring...");
        }

        GCCoreBlocks.initBlocks();
        GCCoreBlocks.registerBlocks();
        GCCoreBlocks.setHarvestLevels();

        GCCoreItems.initItems();
        GCCoreItems.registerHarvestLevels();

        if (GCCoreConfigManager.loadBC.getBoolean(true))
        {
            BasicComponents.registerTileEntities();

            BasicComponents.requestItem("ingotCopper", 0);
            BasicComponents.requestItem("ingotTin", 0);
            BasicComponents.requestBlock("oreCopper", 0);
            BasicComponents.requestBlock("oreTin", 0);
            BasicComponents.requestItem("ingotSteel", 0);
            BasicComponents.requestItem("dustSteel", 0);
            BasicComponents.requestItem("ingotBronze", 0);
            BasicComponents.requestItem("dustBronze", 0);
            BasicComponents.requestItem("plateBronze", 0);
            BasicComponents.requestItem("plateCopper", 0);
            BasicComponents.requestItem("plateTin", 0);
            BasicComponents.requestItem("plateIron", 0);
            BasicComponents.requestItem("plateGold", 0);
            BasicComponents.requestBlock("copperWire", 0);
            BasicComponents.requestItem("circuitBasic", 0);
            BasicComponents.requestItem("circuitAdvanced", 0);
            BasicComponents.requestItem("circuitElite", 0);
            BasicComponents.requestItem("motor", 0);
            BasicComponents.requestItem("wrench", 0);
            BasicComponents.requestItem("battery", 0);
            BasicComponents.requestItem("infiniteBattery", 0);
            BasicComponents.requireMachines(GalacticraftCore.instance, 0);
            BasicComponents.registerTileEntities();

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
    }

    @EventHandler
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

        for (int i = GCCoreItems.fuelCanister.getMaxDamage() - 1; i > 0; i--)
        {
            FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(GalacticraftCore.FUEL, GCCoreItems.fuelCanister.getMaxDamage() - i), new ItemStack(GCCoreItems.fuelCanister, 1, i), new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.fuelCanister.getMaxDamage())));
        }

        for (int i = GCCoreItems.oilCanister.getMaxDamage() - 1; i > 0; i--)
        {
            FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(GalacticraftCore.CRUDEOIL, GCCoreItems.oilCanister.getMaxDamage() - i), new ItemStack(GCCoreItems.oilCanister, 1, i), new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.fuelCanister.getMaxDamage())));
        }

        SchematicRegistry.registerSchematicRecipe(new GCCoreSchematicRocketT1());
        SchematicRegistry.registerSchematicRecipe(new GCCoreSchematicMoonBuggy());
        SchematicRegistry.registerSchematicRecipe(new GCCoreSchematicAdd());

        GasTransmission.register();

        this.registerCreatures();
        this.registerOtherEntities();
        MinecraftForge.EVENT_BUS.register(new GCCoreEvents());
        NetworkRegistry.instance().registerChannel(new GCCorePacketManager(), GalacticraftCore.CHANNELENTITIES, Side.CLIENT);
    }

    @EventHandler
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

        this.registerTileEntities();

        GCCoreRecipeManager.loadRecipes();

        // Register steel plate after, so the heavy duty boots recipe is added
        // to recipe list first, allowing it to be crafted.
        if (GCCoreConfigManager.loadBC.getBoolean(true))
        {
            BasicComponents.requestItem("plateSteel", 0);
        }

        NetworkRegistry.instance().registerGuiHandler(this, GalacticraftCore.proxy);

        GalacticraftCore.proxy.postInit(event);
        GalacticraftCore.proxy.registerRenderInformation();

        GCCoreThreadRequirementMissing.startCheck(FMLCommonHandler.instance().getEffectiveSide());
    }

    @EventHandler
    public void serverInit(FMLServerStartedEvent event)
    {
        GalacticraftCore.moon.serverInit(event);

        GCCoreUtil.checkVersion(Side.SERVER);
        TickRegistry.registerTickHandler(new GCCoreTickHandlerServer(), Side.SERVER);
        NetworkRegistry.instance().registerChannel(new GCCorePacketHandlerServer(), GalacticraftCore.CHANNEL, Side.SERVER);
    }

    @EventHandler
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

    @EventHandler
    public void unregisterDims(FMLServerStoppedEvent var1)
    {
        WorldUtil.unregisterPlanets();
        WorldUtil.unregisterSpaceStations();
    }

    public void registerTileEntities()
    {
        GameRegistry.registerTileEntity(GCCoreTileEntityTreasureChest.class, GCCoreCompatibilityManager.isAIILoaded() ? "Space Treasure Chest" : "Treasure Chest");
        GameRegistry.registerTileEntity(GCCoreTileEntityOxygenDistributor.class, "Air Distributor");
        GameRegistry.registerTileEntity(GCCoreTileEntityOxygenCollector.class, "Air Collector");
        GameRegistry.registerTileEntity(GCCoreTileEntityOxygenPipe.class, "Oxygen Pipe");
        GameRegistry.registerTileEntity(GCCoreTileEntityAirLock.class, "Air Lock Frame");
        GameRegistry.registerTileEntity(GCCoreTileEntityRefinery.class, "Refinery");
        GameRegistry.registerTileEntity(GCCoreTileEntityAdvancedCraftingTable.class, "NASA Workbench");
        GameRegistry.registerTileEntity(GCCoreTileEntityOxygenCompressor.class, "Air Compressor");
        GameRegistry.registerTileEntity(GCCoreTileEntityFuelLoader.class, "Fuel Loader");
        GameRegistry.registerTileEntity(GCCoreTileEntityLandingPadSingle.class, "Landing Pad");
        GameRegistry.registerTileEntity(GCCoreTileEntityLandingPad.class, "Landing Pad Full");
        GameRegistry.registerTileEntity(GCCoreTileEntitySpaceStationBase.class, "Space Station");
        GameRegistry.registerTileEntity(TileEntityMulti.class, "Dummy Block");
        GameRegistry.registerTileEntity(GCCoreTileEntityOxygenSealer.class, "Air Sealer");
        GameRegistry.registerTileEntity(GCCoreTileEntityDungeonSpawner.class, "Dungeon Boss Spawner");
        GameRegistry.registerTileEntity(GCCoreTileEntityOxygenDetector.class, "Oxygen Detector");
        GameRegistry.registerTileEntity(GCCoreTileEntityBuggyFueler.class, "Buggy Fueler");
        GameRegistry.registerTileEntity(GCCoreTileEntityBuggyFuelerSingle.class, "Buggy Fueler Single");
        GameRegistry.registerTileEntity(GCCoreTileEntityCargoPad.class, "Cargo Pad Full");
        GameRegistry.registerTileEntity(GCCoreTileEntityCargoPadSingle.class, "Cargo Pad");
        GameRegistry.registerTileEntity(GCCoreTileEntityCargoLoader.class, "Cargo Loader");
        GameRegistry.registerTileEntity(GCCoreTileEntityCargoUnloader.class, "Cargo Unloader");
        GameRegistry.registerTileEntity(GCCoreTileEntityParachest.class, "Parachest Tile");
        GameRegistry.registerTileEntity(GCCoreTileEntitySolar.class, "Galacticraft Solar Panel");
    }

    public void registerCreatures()
    {
        GCCoreUtil.registerGalacticraftCreature(GCCoreEntitySpider.class, "EvolvedSpider", GCCoreConfigManager.idEntityEvolvedSpider, 3419431, 11013646);
        GCCoreUtil.registerGalacticraftCreature(GCCoreEntityZombie.class, "EvolvedZombie", GCCoreConfigManager.idEntityEvolvedZombie, 44975, 7969893);
        GCCoreUtil.registerGalacticraftCreature(GCCoreEntityCreeper.class, "EvolvedCreeper", GCCoreConfigManager.idEntityEvolvedCreeper, 894731, 0);
        GCCoreUtil.registerGalacticraftCreature(GCCoreEntitySkeleton.class, "EvolvedSkeleton", GCCoreConfigManager.idEntityEvolvedSkeleton, 12698049, 4802889);
        GCCoreUtil.registerGalacticraftCreature(GCCoreEntitySkeletonBoss.class, "EvolvedSkeletonBoss", GCCoreConfigManager.idEntityEvolvedSkeletonBoss, 12698049, 4802889);
        GCCoreUtil.registerGalacticraftCreature(GCCoreEntityAlienVillager.class, "AlienVillager", GCCoreConfigManager.idEntityAlienVillager, GCCoreUtil.convertTo32BitColor(255, 103, 181, 145), 12422002);
    }

    public void registerOtherEntities()
    {
        GCCoreUtil.registerGalacticraftNonMobEntity(GCCoreEntityRocketT1.class, "Spaceship", GCCoreConfigManager.idEntitySpaceship, 150, 1, true);
        GCCoreUtil.registerGalacticraftNonMobEntity(GCCoreEntityArrow.class, "GravityArrow", GCCoreConfigManager.idEntityAntiGravityArrow, 150, 5, true);
        GCCoreUtil.registerGalacticraftNonMobEntity(GCCoreEntityMeteor.class, "Meteor", GCCoreConfigManager.idEntityMeteor, 150, 5, true);
        GCCoreUtil.registerGalacticraftNonMobEntity(GCCoreEntityBuggy.class, "Buggy", GCCoreConfigManager.idEntityBuggy, 150, 5, true);
        GCCoreUtil.registerGalacticraftNonMobEntity(GCCoreEntityFlag.class, "Flag", GCCoreConfigManager.idEntityFlag, 150, 5, true);
        GCCoreUtil.registerGalacticraftNonMobEntity(GCCoreEntityParaChest.class, "ParaChest", GCCoreConfigManager.idEntityParaChest, 150, 5, true);
        GCCoreUtil.registerGalacticraftNonMobEntity(GCCoreEntityOxygenBubble.class, "OxygenBubble", GCCoreConfigManager.idEntityOxygenBubble, 150, 20, false);
        GCCoreUtil.registerGalacticraftNonMobEntity(GCCoreEntityLander.class, "Lander", GCCoreConfigManager.idEntityLander, 150, 5, true);
    }
}
