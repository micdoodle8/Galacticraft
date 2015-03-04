package micdoodle8.mods.galacticraft.core;

import api.player.server.ServerPlayerAPI;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.*;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IAtmosphericGas;
import micdoodle8.mods.galacticraft.core.blocks.BlockFluidGC;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.client.gui.GuiHandler;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GameScreenText;
import micdoodle8.mods.galacticraft.core.command.*;
import micdoodle8.mods.galacticraft.core.dimension.*;
import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;
import micdoodle8.mods.galacticraft.core.energy.grid.ChunkPowerHandler;
import micdoodle8.mods.galacticraft.core.entities.*;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerBaseMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler;
import micdoodle8.mods.galacticraft.core.event.EventHandlerGC;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.items.ItemBlockGC;
import micdoodle8.mods.galacticraft.core.network.ConnectionEvents;
import micdoodle8.mods.galacticraft.core.network.GalacticraftChannelHandler;
import micdoodle8.mods.galacticraft.core.proxy.CommonProxyCore;
import micdoodle8.mods.galacticraft.core.recipe.RecipeManagerGC;
import micdoodle8.mods.galacticraft.core.schematic.SchematicAdd;
import micdoodle8.mods.galacticraft.core.schematic.SchematicMoonBuggy;
import micdoodle8.mods.galacticraft.core.schematic.SchematicRocketT1;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.tile.*;
import micdoodle8.mods.galacticraft.core.util.*;
import micdoodle8.mods.galacticraft.core.world.ChunkLoadingCallback;
import micdoodle8.mods.galacticraft.core.world.gen.OreGenOtherMods;
import micdoodle8.mods.galacticraft.core.world.gen.OverworldGenerator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@Mod(name = GalacticraftCore.NAME, version = Constants.LOCALMAJVERSION + "." + Constants.LOCALMINVERSION + "." + Constants.LOCALBUILDVERSION, useMetadata = true, modid = Constants.MOD_ID_CORE, dependencies = "required-after:Forge@[10.12.2.1147,); required-after:FML@[7.2.217.1147,); required-after:Micdoodlecore; after:IC2; after:BuildCraft|Core; after:BuildCraft|Energy; after:IC2", guiFactory = "micdoodle8.mods.galacticraft.core.client.gui.screen.ConfigGuiFactoryCore")
public class GalacticraftCore
{
    public static final String NAME = "Galacticraft Core";

    @SidedProxy(clientSide = "micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore", serverSide = "micdoodle8.mods.galacticraft.core.proxy.CommonProxyCore")
    public static CommonProxyCore proxy;

    @Instance(Constants.MOD_ID_CORE)
    public static GalacticraftCore instance;

    public static boolean isPlanetsLoaded;
    
    public static GalacticraftChannelHandler packetPipeline;

    public static CreativeTabs galacticraftBlocksTab;
    public static CreativeTabs galacticraftItemsTab;

    public static SolarSystem solarSystemSol;
    public static Planet planetMercury;
    public static Planet planetVenus;
    public static Planet planetMars;  //Used only if GCPlanets not loaded
    public static Planet planetOverworld;
    public static Planet planetJupiter;
    public static Planet planetSaturn;
    public static Planet planetUranus;
    public static Planet planetNeptune;
    public static Moon moonMoon;
    public static Satellite satelliteSpaceStation;

    public static final String CONFIG_FILE = "Galacticraft/core.conf";
    public static final String POWER_CONFIG_FILE = "Galacticraft/power-GC3.conf";
    public static final String CHUNKLOADER_CONFIG_FILE = "Galacticraft/chunkloading.conf";

    public static String ASSET_PREFIX = "galacticraftcore";
    public static String TEXTURE_PREFIX = GalacticraftCore.ASSET_PREFIX + ":";
    public static String PREFIX = "micdoodle8.";  

    public static Fluid gcFluidOil;
    public static Fluid gcFluidFuel;
    public static Fluid fluidOil;
    public static Fluid fluidFuel;

    public static HashMap<String, ItemStack> itemList = new HashMap<String, ItemStack>();
    public static HashMap<String, ItemStack> blocksList = new HashMap<String, ItemStack>();

    public static ImageWriter jpgWriter;
    public static ImageWriteParam writeParam;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	isPlanetsLoaded = Loader.isModLoaded(Constants.MOD_ID_PLANETS);
    	GCCoreUtil.nextID = 0;
    	
    	MinecraftForge.EVENT_BUS.register(new EventHandlerGC());
        GCPlayerHandler handler = new GCPlayerHandler();
        MinecraftForge.EVENT_BUS.register(handler);
        FMLCommonHandler.instance().bus().register(handler);
        GalacticraftCore.proxy.preInit(event);

        ConfigManagerCore.initialize(new File(event.getModConfigurationDirectory(), GalacticraftCore.CONFIG_FILE));
        FMLCommonHandler.instance().bus().register(new ConfigManagerCore());
        EnergyConfigHandler.setDefaultValues(new File(event.getModConfigurationDirectory(), GalacticraftCore.POWER_CONFIG_FILE));
        ChunkLoadingCallback.loadConfig(new File(event.getModConfigurationDirectory(), GalacticraftCore.CHUNKLOADER_CONFIG_FILE));

        String nameOil = "oil";
        String nameFuel = "fuel"; 
        if (FluidRegistry.getFluid(nameOil) != null) nameOil = "oilgc";
        if (FluidRegistry.getFluid(nameFuel) != null) nameFuel = "fuelgc";        
        GalacticraftCore.gcFluidOil = new Fluid(nameOil).setDensity(800).setViscosity(1500);
        GalacticraftCore.gcFluidFuel = new Fluid(nameFuel).setDensity(400).setViscosity(900);
        FluidRegistry.registerFluid(GalacticraftCore.gcFluidOil);
        FluidRegistry.registerFluid(GalacticraftCore.gcFluidFuel);
        GalacticraftCore.gcFluidOil = FluidRegistry.getFluid(nameOil); 
        GalacticraftCore.gcFluidFuel = FluidRegistry.getFluid(nameFuel);
        GalacticraftCore.fluidOil = FluidRegistry.getFluid("oil"); 
        GalacticraftCore.fluidFuel = FluidRegistry.getFluid("fuel");

        GCBlocks.crudeOilStill = new BlockFluidGC(GalacticraftCore.gcFluidOil, "oil");
        ((BlockFluidGC) GCBlocks.crudeOilStill).setQuantaPerBlock(3);
        GCBlocks.crudeOilStill.setBlockName("crudeOilStill");
        GameRegistry.registerBlock(GCBlocks.crudeOilStill, ItemBlockGC.class, GCBlocks.crudeOilStill.getUnlocalizedName());
        if (GalacticraftCore.gcFluidOil.getBlock() == null)
        {
            GalacticraftCore.gcFluidOil.setBlock(GCBlocks.crudeOilStill);
        }

        GCBlocks.fuelStill = new BlockFluidGC(GalacticraftCore.gcFluidFuel, "fuel");
        ((BlockFluidGC) GCBlocks.fuelStill).setQuantaPerBlock(6);
        GCBlocks.fuelStill.setBlockName("fuel");
        GameRegistry.registerBlock(GCBlocks.fuelStill, ItemBlockGC.class, GCBlocks.fuelStill.getUnlocalizedName());
        if (GalacticraftCore.gcFluidFuel.getBlock() == null)
        {
            GalacticraftCore.gcFluidFuel.setBlock(GCBlocks.fuelStill);
        }

        if (Loader.isModLoaded("PlayerAPI"))
        {
            ServerPlayerAPI.register(Constants.MOD_ID_CORE, GCPlayerBaseMP.class);
        }

        GCBlocks.initBlocks();
        GCItems.initItems();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        GalacticraftCore.galacticraftBlocksTab = new CreativeTabGC(CreativeTabs.getNextID(), "GalacticraftBlocks", Item.getItemFromBlock(GCBlocks.machineBase2), 0);
        GalacticraftCore.galacticraftItemsTab = new CreativeTabGC(CreativeTabs.getNextID(), "GalacticraftItems", GCItems.rocketTier1, 0);
        GalacticraftCore.proxy.init(event);

        GalacticraftCore.packetPipeline = GalacticraftChannelHandler.init();

        GalacticraftCore.solarSystemSol = new SolarSystem("sol", "milkyWay").setMapPosition(new Vector3(0.0F, 0.0F));
        Star starSol = (Star) new Star("sol").setParentSolarSystem(GalacticraftCore.solarSystemSol).setTierRequired(-1);
        starSol.setBodyIcon(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/sun.png"));
        GalacticraftCore.solarSystemSol.setMainStar(starSol);

        GalacticraftCore.planetOverworld = (Planet) new Planet("overworld").setParentSolarSystem(GalacticraftCore.solarSystemSol).setRingColorRGB(0.1F, 0.9F, 0.6F).setPhaseShift(0.0F);
        GalacticraftCore.planetOverworld.setBodyIcon(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/earth.png"));
        GalacticraftCore.planetOverworld.setDimensionInfo(0, WorldProvider.class, false).setTierRequired(1);
        GalacticraftCore.planetOverworld.atmosphereComponent(IAtmosphericGas.NITROGEN).atmosphereComponent(IAtmosphericGas.OXYGEN).atmosphereComponent(IAtmosphericGas.ARGON).atmosphereComponent(IAtmosphericGas.WATER);

        GalacticraftCore.moonMoon = (Moon) new Moon("moon").setParentPlanet(GalacticraftCore.planetOverworld).setRelativeSize(0.2667F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(13F, 13F)).setRelativeOrbitTime(1 / 0.01F);
        GalacticraftCore.moonMoon.setDimensionInfo(ConfigManagerCore.idDimensionMoon, WorldProviderMoon.class).setTierRequired(1);
        GalacticraftCore.moonMoon.setBodyIcon(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/moon.png"));

        GalacticraftCore.satelliteSpaceStation = (Satellite) new Satellite("spaceStation.overworld").setParentBody(GalacticraftCore.planetOverworld).setRelativeSize(0.2667F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(9F, 9F)).setRelativeOrbitTime(1 / 0.05F);
        GalacticraftCore.satelliteSpaceStation.setDimensionInfo(ConfigManagerCore.idDimensionOverworldOrbit, WorldProviderOrbit.class, false).setTierRequired(1);
        GalacticraftCore.satelliteSpaceStation.setBodyIcon(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/spaceStation.png"));

        ForgeChunkManager.setForcedChunkLoadingCallback(GalacticraftCore.instance, new ChunkLoadingCallback());
        FMLCommonHandler.instance().bus().register(new ConnectionEvents());

        for (int i = GCItems.fuelCanister.getMaxDamage() - 1; i > 0; i--)
        {
            FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(GalacticraftCore.fluidFuel, GCItems.fuelCanister.getMaxDamage() - i), new ItemStack(GCItems.fuelCanister, 1, i), new ItemStack(GCItems.oilCanister, 1, GCItems.fuelCanister.getMaxDamage())));
        }

        for (int i = GCItems.oilCanister.getMaxDamage() - 1; i > 0; i--)
        {
            FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(GalacticraftCore.fluidOil, GCItems.oilCanister.getMaxDamage() - i), new ItemStack(GCItems.oilCanister, 1, i), new ItemStack(GCItems.oilCanister, 1, GCItems.fuelCanister.getMaxDamage())));
        }

        SchematicRegistry.registerSchematicRecipe(new SchematicRocketT1());
        SchematicRegistry.registerSchematicRecipe(new SchematicMoonBuggy());
        SchematicRegistry.registerSchematicRecipe(new SchematicAdd());
        ChunkPowerHandler.initiate();
        EnergyConfigHandler.initGas();

        this.registerCreatures();
        this.registerOtherEntities();
        this.registerTileEntities();

        GalaxyRegistry.registerSolarSystem(GalacticraftCore.solarSystemSol);
        GalaxyRegistry.registerPlanet(GalacticraftCore.planetOverworld);
        GalaxyRegistry.registerMoon(GalacticraftCore.moonMoon);
        GalaxyRegistry.registerSatellite(GalacticraftCore.satelliteSpaceStation);
        GalacticraftRegistry.registerProvider(ConfigManagerCore.idDimensionOverworldOrbit, WorldProviderOrbit.class, false);
        GalacticraftRegistry.registerProvider(ConfigManagerCore.idDimensionOverworldOrbitStatic, WorldProviderOrbit.class, true);
        GalacticraftRegistry.registerTeleportType(WorldProviderSurface.class, new TeleportTypeOverworld());
        GalacticraftRegistry.registerTeleportType(WorldProviderOrbit.class, new TeleportTypeOrbit());
        GalacticraftRegistry.registerTeleportType(WorldProviderMoon.class, new TeleportTypeMoon());
        GalacticraftRegistry.registerRocketGui(WorldProviderOrbit.class, new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/overworldRocketGui.png"));
        GalacticraftRegistry.registerRocketGui(WorldProviderSurface.class, new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/overworldRocketGui.png"));
        GalacticraftRegistry.registerRocketGui(WorldProviderMoon.class, new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/moonRocketGui.png"));
        GalacticraftRegistry.addDungeonLoot(1, new ItemStack(GCItems.schematic, 1, 0));
        GalacticraftRegistry.addDungeonLoot(1, new ItemStack(GCItems.schematic, 1, 1));

        if (ConfigManagerCore.enableCopperOreGen)
        {
            GameRegistry.registerWorldGenerator(new OverworldGenerator(GCBlocks.basicBlock, 5, 24, 0, 75, 7), 4);
        }

        if (ConfigManagerCore.enableTinOreGen)
        {
            GameRegistry.registerWorldGenerator(new OverworldGenerator(GCBlocks.basicBlock, 6, 22, 0, 60, 7), 4);
        }

        if (ConfigManagerCore.enableAluminumOreGen)
        {
            GameRegistry.registerWorldGenerator(new OverworldGenerator(GCBlocks.basicBlock, 7, 18, 0, 45, 7), 4);
        }

        if (ConfigManagerCore.enableSiliconOreGen)
        {
            GameRegistry.registerWorldGenerator(new OverworldGenerator(GCBlocks.basicBlock, 8, 3, 0, 25, 7), 4);
        }

        FMLInterModComms.sendMessage("OpenBlocks", "donateUrl", "http://www.patreon.com/micdoodle8");
    	GalacticraftRegistry.registerCoreGameScreens();
    	
    	MinecraftForge.EVENT_BUS.register(new OreGenOtherMods());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        GalacticraftCore.planetMercury = makeUnreachablePlanet("mercury", GalacticraftCore.solarSystemSol);
        if (GalacticraftCore.planetMercury != null) GalacticraftCore.planetMercury.setRingColorRGB(0.1F, 0.9F, 0.6F).setPhaseShift(1.45F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(0.5F, 0.5F)).setRelativeOrbitTime(0.24096385542168674698795180722892F);
        GalacticraftCore.planetVenus = makeUnreachablePlanet("venus", GalacticraftCore.solarSystemSol);
        if (GalacticraftCore.planetVenus != null) GalacticraftCore.planetVenus.setRingColorRGB(0.1F, 0.9F, 0.6F).setPhaseShift(2.0F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(0.75F, 0.75F)).setRelativeOrbitTime(0.61527929901423877327491785323111F);
        GalacticraftCore.planetMars = makeUnreachablePlanet("mars", GalacticraftCore.solarSystemSol);
        if (GalacticraftCore.planetMars != null) GalacticraftCore.planetMars.setRingColorRGB(0.67F, 0.1F, 0.1F).setPhaseShift(0.1667F).setRelativeSize(0.5319F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(1.25F, 1.25F)).setRelativeOrbitTime(1.8811610076670317634173055859803F);
        GalacticraftCore.planetJupiter = makeUnreachablePlanet("jupiter", GalacticraftCore.solarSystemSol);
        if (GalacticraftCore.planetJupiter != null) GalacticraftCore.planetJupiter.setRingColorRGB(0.1F, 0.9F, 0.6F).setPhaseShift((float) Math.PI).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(1.5F, 1.5F)).setRelativeOrbitTime(11.861993428258488499452354874042F);
        GalacticraftCore.planetSaturn = makeUnreachablePlanet("saturn", GalacticraftCore.solarSystemSol);
        if (GalacticraftCore.planetSaturn != null) GalacticraftCore.planetSaturn.setRingColorRGB(0.1F, 0.9F, 0.6F).setPhaseShift(5.45F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(1.75F, 1.75F)).setRelativeOrbitTime(29.463307776560788608981380065717F);
        GalacticraftCore.planetUranus = makeUnreachablePlanet("uranus", GalacticraftCore.solarSystemSol);
        if (GalacticraftCore.planetUranus != null) GalacticraftCore.planetUranus.setRingColorRGB(0.1F, 0.9F, 0.6F).setPhaseShift(1.38F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(2.0F, 2.0F)).setRelativeOrbitTime(84.063526834611171960569550930997F);
        GalacticraftCore.planetNeptune = makeUnreachablePlanet("neptune", GalacticraftCore.solarSystemSol);
        if (GalacticraftCore.planetNeptune != null) GalacticraftCore.planetNeptune.setRingColorRGB(0.1F, 0.9F, 0.6F).setPhaseShift(1.0F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(2.25F, 2.25F)).setRelativeOrbitTime(164.84118291347207009857612267251F);

        GalacticraftCore.proxy.postInit(event);

        ArrayList<CelestialBody> cBodyList = new ArrayList<CelestialBody>();
        cBodyList.addAll(GalaxyRegistry.getRegisteredPlanets().values());
        cBodyList.addAll(GalaxyRegistry.getRegisteredMoons().values());

        for (CelestialBody body : cBodyList)
        {
            if (body.shouldAutoRegister())
            {
                int id = Arrays.binarySearch(ConfigManagerCore.staticLoadDimensions, body.getDimensionID());
                //It's important this is done in the same order as planets will be registered by WorldUtil.registerPlanet();
                GalacticraftRegistry.registerProvider(body.getDimensionID(), body.getWorldProvider(), body.getForceStaticLoad() || id < 0);
            }
        }

        CompatibilityManager.checkForCompatibleMods();
        RecipeManagerGC.loadRecipes();
        NetworkRegistry.INSTANCE.registerGuiHandler(GalacticraftCore.instance, new GuiHandler());
        FMLCommonHandler.instance().bus().register(new TickHandlerServer());
        GalaxyRegistry.refreshGalaxies();
        
    	GalacticraftRegistry.registerScreen(new GameScreenText());  //Screen API demo
    	//Note: add-ons can register their own screens in postInit by calling GalacticraftRegistry.registerScreen(IGameScreen) like this.
    	//[Called on both client and server: do not include any client-specific code in the new game screen's constructor method.]
    }

    @EventHandler
    public void serverInit(FMLServerStartedEvent event)
    {
        if (ThreadRequirementMissing.INSTANCE == null)
        {
            ThreadRequirementMissing.beginCheck(FMLCommonHandler.instance().getEffectiveSide());
        }

        ThreadVersionCheck.startCheck();
        TickHandlerServer.restart();
        BlockVec3.chunkCacheDim = Integer.MAX_VALUE;
        
        jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        writeParam = jpgWriter.getDefaultWriteParam();
        writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writeParam.setCompressionQuality(1.0f);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandSpaceStationAddOwner());
        event.registerServerCommand(new CommandSpaceStationRemoveOwner());
        event.registerServerCommand(new CommandPlanetTeleport());
        event.registerServerCommand(new CommandKeepDim());
        event.registerServerCommand(new CommandGCInv());
        event.registerServerCommand(new CommandGCHelp());
        event.registerServerCommand(new CommandGCEnergyUnits());
        event.registerServerCommand(new CommandJoinSpaceRace());

        WorldUtil.registerSpaceStations(event.getServer().worldServerForDimension(0).getSaveHandler().getMapFileFromName("dummy").getParentFile());

        ArrayList<CelestialBody> cBodyList = new ArrayList<CelestialBody>();
        cBodyList.addAll(GalaxyRegistry.getRegisteredPlanets().values());
        cBodyList.addAll(GalaxyRegistry.getRegisteredMoons().values());

        for (CelestialBody body : cBodyList)
        {
            if (body.shouldAutoRegister())
            {
                int id = Arrays.binarySearch(ConfigManagerCore.staticLoadDimensions, body.getDimensionID());
                WorldUtil.registerPlanet(body.getDimensionID(), true);
                if (id >= 0)
                {
                    FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(body.getDimensionID());
                }
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
        GameRegistry.registerTileEntity(TileEntityTreasureChest.class, CompatibilityManager.isAIILoaded() ? "Space Treasure Chest" : "Treasure Chest");
        GameRegistry.registerTileEntity(TileEntityOxygenDistributor.class, "Air Distributor");
        GameRegistry.registerTileEntity(TileEntityOxygenCollector.class, "Air Collector");
        GameRegistry.registerTileEntity(TileEntityOxygenPipe.class, "Oxygen Pipe");
        GameRegistry.registerTileEntity(TileEntityAirLock.class, "Air Lock Frame");
        GameRegistry.registerTileEntity(TileEntityRefinery.class, "Refinery");
        GameRegistry.registerTileEntity(TileEntityNasaWorkbench.class, "NASA Workbench");
        GameRegistry.registerTileEntity(TileEntityOxygenCompressor.class, "Air Compressor");
        GameRegistry.registerTileEntity(TileEntityFuelLoader.class, "Fuel Loader");
        GameRegistry.registerTileEntity(TileEntityLandingPadSingle.class, "Landing Pad");
        GameRegistry.registerTileEntity(TileEntityLandingPad.class, "Landing Pad Full");
        GameRegistry.registerTileEntity(TileEntitySpaceStationBase.class, "Space Station");
        GameRegistry.registerTileEntity(TileEntityMulti.class, "Dummy Block");
        GameRegistry.registerTileEntity(TileEntityOxygenSealer.class, "Air Sealer");
        GameRegistry.registerTileEntity(TileEntityDungeonSpawner.class, "Dungeon Boss Spawner");
        GameRegistry.registerTileEntity(TileEntityOxygenDetector.class, "Oxygen Detector");
        GameRegistry.registerTileEntity(TileEntityBuggyFueler.class, "Buggy Fueler");
        GameRegistry.registerTileEntity(TileEntityBuggyFuelerSingle.class, "Buggy Fueler Single");
        GameRegistry.registerTileEntity(TileEntityCargoLoader.class, "Cargo Loader");
        GameRegistry.registerTileEntity(TileEntityCargoUnloader.class, "Cargo Unloader");
        GameRegistry.registerTileEntity(TileEntityParaChest.class, "Parachest Tile");
        GameRegistry.registerTileEntity(TileEntitySolar.class, "Galacticraft Solar Panel");
        GameRegistry.registerTileEntity(TileEntityEnergyStorageModule.class, "Energy Storage Module");
        GameRegistry.registerTileEntity(TileEntityCoalGenerator.class, "Galacticraft Coal Generator");
        GameRegistry.registerTileEntity(TileEntityElectricFurnace.class, "Galacticraft Electric Furnace");
        GameRegistry.registerTileEntity(TileEntityAluminumWire.class, "Galacticraft Aluminum Wire");
        GameRegistry.registerTileEntity(TileEntityFallenMeteor.class, "Fallen Meteor");
        GameRegistry.registerTileEntity(TileEntityIngotCompressor.class, "Ingot Compressor");
        GameRegistry.registerTileEntity(TileEntityElectricIngotCompressor.class, "Electric Ingot Compressor");
        GameRegistry.registerTileEntity(TileEntityCircuitFabricator.class, "Circuit Fabricator");
        GameRegistry.registerTileEntity(TileEntityAirLockController.class, "Air Lock Controller");
        GameRegistry.registerTileEntity(TileEntityOxygenStorageModule.class, "Oxygen Storage Module");
        GameRegistry.registerTileEntity(TileEntityOxygenDecompressor.class, "Oxygen Decompressor");
        GameRegistry.registerTileEntity(TileEntityThruster.class, "Space Station Thruster");
        GameRegistry.registerTileEntity(TileEntityArclamp.class, "Arc Lamp");
        GameRegistry.registerTileEntity(TileEntityScreen.class, "View Screen");
        GameRegistry.registerTileEntity(TileEntityTelemetry.class, "Telemetry Unit");
    }

    public void registerCreatures()
    {
        GCCoreUtil.registerGalacticraftCreature(EntityEvolvedSpider.class, "EvolvedSpider", 3419431, 11013646);
        GCCoreUtil.registerGalacticraftCreature(EntityEvolvedZombie.class, "EvolvedZombie", 44975, 7969893);
        GCCoreUtil.registerGalacticraftCreature(EntityEvolvedCreeper.class, "EvolvedCreeper", 894731, 0);
        GCCoreUtil.registerGalacticraftCreature(EntityEvolvedSkeleton.class, "EvolvedSkeleton", 12698049, 4802889);
        GCCoreUtil.registerGalacticraftCreature(EntitySkeletonBoss.class, "EvolvedSkeletonBoss", 12698049, 4802889);
        GCCoreUtil.registerGalacticraftCreature(EntityAlienVillager.class, "AlienVillager", ColorUtil.to32BitColor(255, 103, 145, 181), 12422002);
    }

    public void registerOtherEntities()
    {
        GCCoreUtil.registerGalacticraftNonMobEntity(EntityTier1Rocket.class, "Spaceship", 150, 1, false);
        GCCoreUtil.registerGalacticraftNonMobEntity(EntityMeteor.class, "Meteor", 150, 5, true);
        GCCoreUtil.registerGalacticraftNonMobEntity(EntityBuggy.class, "Buggy", 150, 5, true);
        GCCoreUtil.registerGalacticraftNonMobEntity(EntityFlag.class, "GCFlag", 150, 5, true);
        GCCoreUtil.registerGalacticraftNonMobEntity(EntityParachest.class, "ParaChest", 150, 5, true);
        GCCoreUtil.registerGalacticraftNonMobEntity(EntityBubble.class, "OxygenBubble", 150, 20, false);
        GCCoreUtil.registerGalacticraftNonMobEntity(EntityLander.class, "Lander", 150, 5, false);
        GCCoreUtil.registerGalacticraftNonMobEntity(EntityMeteorChunk.class, "MeteorChunk", 150, 5, true);
        GCCoreUtil.registerGalacticraftNonMobEntity(EntityCelestialFake.class, "CelestialScreen", 150, 5, false);
    }
    
    public Planet makeUnreachablePlanet(String name, SolarSystem system)
    {
        ArrayList<CelestialBody> cBodyList = new ArrayList<CelestialBody>();
        cBodyList.addAll(GalaxyRegistry.getRegisteredPlanets().values());
        for (CelestialBody body : cBodyList)
        {
        	if (body instanceof Planet && name.equals(body.getName()))
        		if (((Planet)body).getParentSolarSystem() == system) return null;
        }
        
    	Planet planet = new Planet(name).setParentSolarSystem(system);
        planet.setBodyIcon(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/" + name + ".png"));
        GalaxyRegistry.registerPlanet(planet);
        return planet;
    }
}
