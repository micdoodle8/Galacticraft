package micdoodle8.mods.galacticraft.core;

import api.player.server.ServerPlayerAPI;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.client.IGameScreen;
import micdoodle8.mods.galacticraft.api.galaxies.*;
import micdoodle8.mods.galacticraft.api.item.EnumExtendedInventorySlot;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.AtmosphereInfo;
import micdoodle8.mods.galacticraft.api.world.EnumAtmosphericGas;
import micdoodle8.mods.galacticraft.core.client.gui.GuiHandler;
import micdoodle8.mods.galacticraft.core.client.screen.GameScreenBasic;
import micdoodle8.mods.galacticraft.core.client.screen.GameScreenCelestial;
import micdoodle8.mods.galacticraft.core.client.screen.GameScreenText;
import micdoodle8.mods.galacticraft.core.command.*;
import micdoodle8.mods.galacticraft.core.dimension.*;
import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;
import micdoodle8.mods.galacticraft.core.energy.grid.ChunkPowerHandler;
import micdoodle8.mods.galacticraft.core.entities.*;
import micdoodle8.mods.galacticraft.core.entities.player.GCCapabilities;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerBaseMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler;
import micdoodle8.mods.galacticraft.core.event.EventHandlerGC;
import micdoodle8.mods.galacticraft.core.items.ItemSchematic;
import micdoodle8.mods.galacticraft.core.network.ConnectionEvents;
import micdoodle8.mods.galacticraft.core.network.ConnectionPacket;
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
import micdoodle8.mods.galacticraft.core.world.gen.BiomeGenBaseMoon;
import micdoodle8.mods.galacticraft.core.world.gen.BiomeGenBaseOrbit;
import micdoodle8.mods.galacticraft.core.world.gen.OreGenOtherMods;
import micdoodle8.mods.galacticraft.core.world.gen.OverworldGenerator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@Mod(modid = Constants.MOD_ID_CORE, name = GalacticraftCore.NAME, version = Constants.COMBINEDVERSION, useMetadata = true, acceptedMinecraftVersions = Constants.MCVERSION, dependencies = Constants.DEPENDENCIES_FORGE + Constants.DEPENDENCIES_MICCORE + Constants.DEPENDENCIES_MODS, guiFactory = "micdoodle8.mods.galacticraft.core.client.gui.screen.ConfigGuiFactoryCore")
public class GalacticraftCore
{
    public static final String NAME = "Galacticraft Core";

    @SidedProxy(clientSide = "micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore", serverSide = "micdoodle8.mods.galacticraft.core.proxy.CommonProxyCore")
    public static CommonProxyCore proxy;

    @Instance(Constants.MOD_ID_CORE)
    public static GalacticraftCore instance;

    public static boolean isPlanetsLoaded;

    public static boolean isHeightConflictingModInstalled;
    
    public static GalacticraftChannelHandler packetPipeline;
    public static GCPlayerHandler handler;

    public static CreativeTabGC galacticraftBlocksTab;
    public static CreativeTabGC galacticraftItemsTab;

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


    public static HashMap<String, ItemStack> itemList = new HashMap<>();
    public static HashMap<String, ItemStack> blocksList = new HashMap<>();

    public static ImageWriter jpgWriter;
    public static ImageWriteParam writeParam;
    public static boolean enableJPEG = false;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        GCCapabilities.register();

    	this.initModInfo(event.getModMetadata());
    	isPlanetsLoaded = Loader.isModLoaded(Constants.MOD_ID_PLANETS);
    	GCCoreUtil.nextID = 0;
    	
        if (CompatibilityManager.isSmartMovingLoaded || CompatibilityManager.isWitcheryLoaded)
        {
            isHeightConflictingModInstalled = true;
        }
    	
    	MinecraftForge.EVENT_BUS.register(new EventHandlerGC());
        handler = new GCPlayerHandler();
        MinecraftForge.EVENT_BUS.register(handler);
        GalacticraftCore.proxy.preInit(event);

        ConnectionPacket.bus = NetworkRegistry.INSTANCE.newEventDrivenChannel(ConnectionPacket.CHANNEL);
        ConnectionPacket.bus.register(new ConnectionPacket());

        ConfigManagerCore.initialize(new File(event.getModConfigurationDirectory(), Constants.CONFIG_FILE));
        EnergyConfigHandler.setDefaultValues(new File(event.getModConfigurationDirectory(), Constants.POWER_CONFIG_FILE));
        ChunkLoadingCallback.loadConfig(new File(event.getModConfigurationDirectory(), Constants.CHUNKLOADER_CONFIG_FILE));

        GalacticraftCore.galacticraftBlocksTab = new CreativeTabGC(CreativeTabs.getNextID(), "galacticraft_blocks", null, 0, null);
        GalacticraftCore.galacticraftItemsTab = new CreativeTabGC(CreativeTabs.getNextID(), "galacticraft_items", null, 0, null);

        GCFluids.registerOilandFuel();

        if (CompatibilityManager.PlayerAPILoaded)
        {
            ServerPlayerAPI.register(Constants.MOD_ID_CORE, GCPlayerBaseMP.class);
        }

        GCBlocks.initBlocks();
        GCItems.initItems();

        proxy.registerVariants();

        GCFluids.registerFluids();

        //Force initialisation of GC biome types in preinit (after config load) - this helps BiomeTweaker by initialising mod biomes in a fixed order during mod loading
        BiomeGenBase biomeOrbitPreInit = BiomeGenBaseOrbit.space;
        BiomeGenBase biomeMoonPreInit = BiomeGenBaseMoon.moonFlat;
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        GCBlocks.doOtherModsTorches();
        GalacticraftCore.handler.registerItemChanges();
        GalacticraftCore.galacticraftBlocksTab.setItemForTab(Item.getItemFromBlock(GCBlocks.machineBase2));
        GalacticraftCore.galacticraftItemsTab.setItemForTab(GCItems.rocketTier1);

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            GCBlocks.finalizeSort();
            GCItems.finalizeSort();
        }

        GalacticraftCore.proxy.init(event);

        GalacticraftCore.packetPipeline = GalacticraftChannelHandler.init();

        GalacticraftCore.solarSystemSol = new SolarSystem("sol", "milky_way").setMapPosition(new Vector3(0.0F, 0.0F, 0.0F));
        Star starSol = (Star) new Star("sol").setParentSolarSystem(GalacticraftCore.solarSystemSol).setTierRequired(-1);
        starSol.setBodyIcon(new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/celestialbodies/sun.png"));
        GalacticraftCore.solarSystemSol.setMainStar(starSol);

        GalacticraftCore.planetOverworld = (Planet) new Planet("overworld").setParentSolarSystem(GalacticraftCore.solarSystemSol).setRingColorRGB(0.1F, 0.9F, 0.6F).setPhaseShift(0.0F);
        GalacticraftCore.planetOverworld.setBodyIcon(new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/celestialbodies/earth.png"));
        GalacticraftCore.planetOverworld.setDimensionInfo(ConfigManagerCore.idDimensionOverworld, WorldProvider.class, false).setTierRequired(1);
        GalacticraftCore.planetOverworld.atmosphereComponent(EnumAtmosphericGas.NITROGEN).atmosphereComponent(EnumAtmosphericGas.OXYGEN).atmosphereComponent(EnumAtmosphericGas.ARGON).atmosphereComponent(EnumAtmosphericGas.WATER);
        GalacticraftCore.planetOverworld.addChecklistKeys("equip_parachute");

        GalacticraftCore.moonMoon = (Moon) new Moon("moon").setParentPlanet(GalacticraftCore.planetOverworld).setRelativeSize(0.2667F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(13F, 13F)).setRelativeOrbitTime(1 / 0.01F);
        GalacticraftCore.moonMoon.setDimensionInfo(ConfigManagerCore.idDimensionMoon, WorldProviderMoon.class).setTierRequired(1);
        GalacticraftCore.moonMoon.setBodyIcon(new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/celestialbodies/moon.png"));
        GalacticraftCore.moonMoon.setAtmosphere(new AtmosphereInfo(false, false, false, 0.0F, 0.0F, 0.0F));
        GalacticraftCore.moonMoon.setBiomeInfo(BiomeGenBaseMoon.moonFlat);
        GalacticraftCore.moonMoon.addMobInfo(new SpawnListEntry(EntityEvolvedZombie.class, 8, 2, 3));
        GalacticraftCore.moonMoon.addMobInfo(new SpawnListEntry(EntityEvolvedSpider.class, 8, 2, 3));
        GalacticraftCore.moonMoon.addMobInfo(new SpawnListEntry(EntityEvolvedSkeleton.class, 8, 2, 3));
        GalacticraftCore.moonMoon.addMobInfo(new SpawnListEntry(EntityEvolvedCreeper.class, 8, 2, 3));
        GalacticraftCore.moonMoon.addMobInfo(new SpawnListEntry(EntityEvolvedEnderman.class, 10, 1, 4));
        GalacticraftCore.moonMoon.addChecklistKeys("equip_oxygen_suit");

        //Satellites must always have a WorldProvider implementing IOrbitDimension
        GalacticraftCore.satelliteSpaceStation = (Satellite) new Satellite("spacestation.overworld").setParentBody(GalacticraftCore.planetOverworld).setRelativeSize(0.2667F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(9F, 9F)).setRelativeOrbitTime(1 / 0.05F);
        GalacticraftCore.satelliteSpaceStation.setDimensionInfo(ConfigManagerCore.idDimensionOverworldOrbit, ConfigManagerCore.idDimensionOverworldOrbitStatic, WorldProviderOverworldOrbit.class).setTierRequired(1);
        GalacticraftCore.satelliteSpaceStation.setBodyIcon(new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/celestialbodies/space_station.png"));
        GalacticraftCore.satelliteSpaceStation.setAtmosphere(new AtmosphereInfo(false, false, false, 0.0F, 0.1F, 0.02F));
        GalacticraftCore.satelliteSpaceStation.addChecklistKeys("equip_oxygen_suit", "create_grapple");

        ForgeChunkManager.setForcedChunkLoadingCallback(GalacticraftCore.instance, new ChunkLoadingCallback());
        MinecraftForge.EVENT_BUS.register(new ConnectionEvents());

        SchematicRegistry.registerSchematicRecipe(new SchematicRocketT1());
        SchematicRegistry.registerSchematicRecipe(new SchematicMoonBuggy());
        SchematicRegistry.registerSchematicRecipe(new SchematicAdd());
        ChunkPowerHandler.initiate();
        EnergyConfigHandler.initGas();

        CompatibilityManager.registerMicroBlocks();
        this.registerCreatures();
        this.registerOtherEntities();
        this.registerTileEntities();

        GalaxyRegistry.registerSolarSystem(GalacticraftCore.solarSystemSol);
        GalaxyRegistry.registerPlanet(GalacticraftCore.planetOverworld);
        GalaxyRegistry.registerMoon(GalacticraftCore.moonMoon);
        GalaxyRegistry.registerSatellite(GalacticraftCore.satelliteSpaceStation);
        GalacticraftRegistry.registerProvider(ConfigManagerCore.idDimensionOverworldOrbit, WorldProviderOverworldOrbit.class, false, 0);
        GalacticraftRegistry.registerProvider(ConfigManagerCore.idDimensionOverworldOrbitStatic, WorldProviderOverworldOrbit.class, true, 0);
        GalacticraftRegistry.registerTeleportType(WorldProviderSurface.class, new TeleportTypeOverworld());
        GalacticraftRegistry.registerTeleportType(WorldProviderOverworldOrbit.class, new TeleportTypeOrbit());
        GalacticraftRegistry.registerTeleportType(WorldProviderMoon.class, new TeleportTypeMoon());
        GalacticraftRegistry.registerRocketGui(WorldProviderOverworldOrbit.class, new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/overworld_rocket_gui.png"));
        GalacticraftRegistry.registerRocketGui(WorldProviderSurface.class, new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/overworld_rocket_gui.png"));
        GalacticraftRegistry.registerRocketGui(WorldProviderMoon.class, new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/moon_rocket_gui.png"));
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
        registerCoreGameScreens();

        GCFluids.registerLegacyFluids();
        GCFluids.registerDispenserBehaviours();

        GalacticraftRegistry.registerGear(Constants.GEAR_ID_OXYGEN_MASK, EnumExtendedInventorySlot.MASK, GCItems.oxMask);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_OXYGEN_GEAR, EnumExtendedInventorySlot.GEAR, GCItems.oxygenGear);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_OXYGEN_TANK_LIGHT, EnumExtendedInventorySlot.LEFT_TANK, GCItems.oxTankLight);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_OXYGEN_TANK_LIGHT, EnumExtendedInventorySlot.RIGHT_TANK, GCItems.oxTankLight);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_OXYGEN_TANK_MEDIUM, EnumExtendedInventorySlot.LEFT_TANK, GCItems.oxTankMedium);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_OXYGEN_TANK_MEDIUM, EnumExtendedInventorySlot.RIGHT_TANK, GCItems.oxTankMedium);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_OXYGEN_TANK_HEAVY, EnumExtendedInventorySlot.LEFT_TANK, GCItems.oxTankHeavy);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_OXYGEN_TANK_HEAVY, EnumExtendedInventorySlot.RIGHT_TANK, GCItems.oxTankHeavy);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_OXYGEN_TANK_INFINITE, EnumExtendedInventorySlot.LEFT_TANK, GCItems.oxygenCanisterInfinite);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_OXYGEN_TANK_INFINITE, EnumExtendedInventorySlot.RIGHT_TANK, GCItems.oxygenCanisterInfinite);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_PARACHUTE, EnumExtendedInventorySlot.PARACHUTE, GCItems.parachute);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_FREQUENCY_MODULE, EnumExtendedInventorySlot.FREQUENCY_MODULE, new ItemStack(GCItems.basicItem, 1, 19));

        GalacticraftCore.proxy.registerFluidTexture(GCFluids.fluidOil, new ResourceLocation(Constants.ASSET_PREFIX, "textures/misc/underoil.png"));
		GalacticraftCore.proxy.registerFluidTexture(GCFluids.fluidFuel, new ResourceLocation(Constants.ASSET_PREFIX, "textures/misc/underfuel.png"));

//        switch (this.getSlotIndex())
//        {
//        case 0:
//            return itemstack.getItem() instanceof ItemOxygenMask;
//        case 1:
//            return itemstack.getItem() == GCItems.oxygenGear;
//        case 2:
//        case 3:
//            return itemstack.getItem() instanceof ItemOxygenTank || itemstack.getItem() instanceof ItemCanisterOxygenInfinite;
//        case 4:
//            return itemstack.getItem() instanceof ItemParaChute;
//        case 5:
//            return itemstack.getItem() == GCItems.basicItem && itemstack.getItemDamage() == 19;
//        case 6:
//            return this.thermalArmorSlotValid(itemstack, 0);
//        case 7:
//            return this.thermalArmorSlotValid(itemstack, 1);
//        case 8:
//            return this.thermalArmorSlotValid(itemstack, 2);
//        case 9:
//            return this.thermalArmorSlotValid(itemstack, 3);
//        case 10:
//            return this.shieldControllerSlotValid(itemstack);
//        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        GalacticraftCore.planetMercury = makeDummyPlanet("mercury", GalacticraftCore.solarSystemSol);
        if (GalacticraftCore.planetMercury != null)
        {
            GalacticraftCore.planetMercury.setRingColorRGB(0.1F, 0.9F, 0.6F).setPhaseShift(1.45F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(0.5F, 0.5F)).setRelativeOrbitTime(0.24096385542168674698795180722892F);
        }
        GalacticraftCore.planetVenus = makeDummyPlanet("venus", GalacticraftCore.solarSystemSol);
        if (GalacticraftCore.planetVenus != null)
        {
            GalacticraftCore.planetVenus.setRingColorRGB(0.1F, 0.9F, 0.6F).setPhaseShift(2.0F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(0.75F, 0.75F)).setRelativeOrbitTime(0.61527929901423877327491785323111F);
        }
        GalacticraftCore.planetMars = makeDummyPlanet("mars", GalacticraftCore.solarSystemSol);
        if (GalacticraftCore.planetMars != null)
        {
            GalacticraftCore.planetMars.setRingColorRGB(0.67F, 0.1F, 0.1F).setPhaseShift(0.1667F).setRelativeSize(0.5319F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(1.25F, 1.25F)).setRelativeOrbitTime(1.8811610076670317634173055859803F);
        }
        GalacticraftCore.planetJupiter = makeDummyPlanet("jupiter", GalacticraftCore.solarSystemSol);
        if (GalacticraftCore.planetJupiter != null)
        {
            GalacticraftCore.planetJupiter.setRingColorRGB(0.1F, 0.9F, 0.6F).setPhaseShift((float) Math.PI).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(1.5F, 1.5F)).setRelativeOrbitTime(11.861993428258488499452354874042F);
        }
        GalacticraftCore.planetSaturn = makeDummyPlanet("saturn", GalacticraftCore.solarSystemSol);
        if (GalacticraftCore.planetSaturn != null)
        {
            GalacticraftCore.planetSaturn.setRingColorRGB(0.1F, 0.9F, 0.6F).setPhaseShift(5.45F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(1.75F, 1.75F)).setRelativeOrbitTime(29.463307776560788608981380065717F);
        }
        GalacticraftCore.planetUranus = makeDummyPlanet("uranus", GalacticraftCore.solarSystemSol);
        if (GalacticraftCore.planetUranus != null)
        {
            GalacticraftCore.planetUranus.setRingColorRGB(0.1F, 0.9F, 0.6F).setPhaseShift(1.38F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(2.0F, 2.0F)).setRelativeOrbitTime(84.063526834611171960569550930997F);
        }
        GalacticraftCore.planetNeptune = makeDummyPlanet("neptune", GalacticraftCore.solarSystemSol);
        if (GalacticraftCore.planetNeptune != null)
        {
            GalacticraftCore.planetNeptune.setRingColorRGB(0.1F, 0.9F, 0.6F).setPhaseShift(1.0F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(2.25F, 2.25F)).setRelativeOrbitTime(164.84118291347207009857612267251F);
        }

        MinecraftForge.EVENT_BUS.register(new OreGenOtherMods());

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
                if (GalacticraftRegistry.registerProvider(body.getDimensionID(), body.getWorldProvider(), body.getForceStaticLoad() || id < 0, 0))
                {
                    body.initialiseMobSpawns();
                }
                else
                {
                    body.setUnreachable();
                }
            }
            
            if (body.getSurfaceBlocks() != null)
            {
                TransformerHooks.spawnListAE2_GC.addAll(body.getSurfaceBlocks());
            }
        }

        CompatibilityManager.checkForCompatibleMods();
        RecipeManagerGC.loadRecipes();
        TileEntityDeconstructor.initialiseRecipeList();
        ItemSchematic.registerSchematicItems();
        NetworkRegistry.INSTANCE.registerGuiHandler(GalacticraftCore.instance, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(new TickHandlerServer());
        GalaxyRegistry.refreshGalaxies();

        GalacticraftRegistry.registerScreen(new GameScreenText());  //Screen API demo
        //Note: add-ons can register their own screens in postInit by calling GalacticraftRegistry.registerScreen(IGameScreen) like this.
        //[Called on both client and server: do not include any client-specific code in the new game screen's constructor method.]

        try
        {
            jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
            writeParam = jpgWriter.getDefaultWriteParam();
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            writeParam.setCompressionQuality(1.0f);
            enableJPEG = true;
        }
        catch (UnsatisfiedLinkError e)
        {
        	GCLog.severe("Error initialising JPEG compressor - this is likely caused by OpenJDK - see https://wiki.micdoodle8.com/wiki/Compatibility#For_clients_running_OpenJDK");
        	e.printStackTrace();
        }
    }

    @EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event)
    {
        TickHandlerServer.restart();
    }

    @EventHandler
    public void serverInit(FMLServerStartedEvent event)
    {
        if (ThreadRequirementMissing.INSTANCE == null)
        {
            ThreadRequirementMissing.beginCheck(GCCoreUtil.getEffectiveSide());
        }

        ThreadVersionCheck.startCheck();
        BlockVec3.chunkCacheDim = Integer.MAX_VALUE;
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event)
    {
        File worldFolder = DimensionManager.getCurrentSaveRootDirectory();
        moveLegacyGCFileLocations(worldFolder);
        
        event.registerServerCommand(new CommandSpaceStationAddOwner());
        event.registerServerCommand(new CommandSpaceStationChangeOwner());
        event.registerServerCommand(new CommandSpaceStationRemoveOwner());
        event.registerServerCommand(new CommandPlanetTeleport());
        event.registerServerCommand(new CommandKeepDim());
        event.registerServerCommand(new CommandGCInv());
        event.registerServerCommand(new CommandGCHelp());
        event.registerServerCommand(new CommandGCKit());
        event.registerServerCommand(new CommandGCHouston());
        event.registerServerCommand(new CommandGCEnergyUnits());
        event.registerServerCommand(new CommandJoinSpaceRace());

        WorldUtil.initialiseDimensionNames();
        WorldUtil.registerSpaceStations(event.getServer(), new File(worldFolder, "galacticraft"));

        ArrayList<CelestialBody> cBodyList = new ArrayList<CelestialBody>();
        cBodyList.addAll(GalaxyRegistry.getRegisteredPlanets().values());
        cBodyList.addAll(GalaxyRegistry.getRegisteredMoons().values());

        for (CelestialBody body : cBodyList)
        {
            if (body.shouldAutoRegister())
            {
                if (!WorldUtil.registerPlanet(body.getDimensionID(), body.getReachable(), 0))
                {
                    body.setUnreachable();
                }
            }
        }

        RecipeManagerGC.setConfigurableRecipes();
    }

    private void moveLegacyGCFileLocations(File worldFolder)
    {
        File destFolder = new File(worldFolder, "galacticraft");
        if (!destFolder.exists())
        {
            if (!destFolder.mkdirs()) return;
        }
        File dataFolder = new File(worldFolder, "data");
        if (!dataFolder.exists()) return;
        
        moveGCFile(new File(dataFolder, "GCAsteroidData.dat"), destFolder);
        moveGCFile(new File(dataFolder, "GCSpaceRaceData.dat"), destFolder);
        moveGCFile(new File(dataFolder, "GCSpinData.dat"), destFolder);
        moveGCFile(new File(dataFolder, "GCInv_savefile.dat"), destFolder);
        String[] names = dataFolder.list();
        for (String name : names)
        {
            if (name.startsWith("spacestation_") && name.endsWith(".dat"))
            {
                moveGCFile(new File(dataFolder, name), destFolder);
            }
        }
    }
    
    private void moveGCFile(File file, File destFolder)
    {
        if (file.exists())
        {
            File destPath = new File(destFolder, file.getName());
            if (destPath.exists())
            {
                GCLog.info("Deleting duplicate Galacticraft data file: " + file.getName());
                file.delete();
                return;
            }
            try
            {
                java.nio.file.Files.move(file.toPath(), destPath.toPath());
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }        
    }

    @EventHandler
    public void onServerStopping(FMLServerStoppingEvent var1)
    {
        MapUtil.saveMapProgress();
    }

    @EventHandler
    public void onServerStop(FMLServerStoppedEvent var1)
    {
        // Unregister dimensions
        WorldUtil.unregisterPlanets();
        WorldUtil.unregisterSpaceStations();
    }

    private static void registerCoreGameScreens()
    {
        if (GCCoreUtil.getEffectiveSide() == Side.CLIENT)
        {
            IGameScreen rendererBasic = new GameScreenBasic();
            IGameScreen rendererCelest = new GameScreenCelestial();
            GalacticraftRegistry.registerScreen(rendererBasic);  //Type 0 - blank
            GalacticraftRegistry.registerScreen(rendererBasic);  //Type 1 - local satellite view
            GalacticraftRegistry.registerScreen(rendererCelest);  //Type 2 - solar system
            GalacticraftRegistry.registerScreen(rendererCelest);  //Type 3 - local planet
            GalacticraftRegistry.registerScreen(rendererCelest);  //Type 4 - render test
        }
        else
        {
            GalacticraftRegistry.registerScreensServer(5);
        }
    }

    private void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntityTreasureChest.class, "GC Treasure Chest");
        GameRegistry.registerTileEntity(TileEntityOxygenDistributor.class, "GC Air Distributor");
        GameRegistry.registerTileEntity(TileEntityOxygenCollector.class, "GC Air Collector");
        GameRegistry.registerTileEntity(TileEntityFluidPipe.class, "GC Oxygen Pipe");
        GameRegistry.registerTileEntity(TileEntityAirLock.class, "GC Air Lock Frame");
        GameRegistry.registerTileEntity(TileEntityRefinery.class, "GC Refinery");
        GameRegistry.registerTileEntity(TileEntityNasaWorkbench.class, "GC NASA Workbench");
        GameRegistry.registerTileEntity(TileEntityDeconstructor.class, "GC Deconstructor");
        GameRegistry.registerTileEntity(TileEntityOxygenCompressor.class, "GC Air Compressor");
        GameRegistry.registerTileEntity(TileEntityFuelLoader.class, "GC Fuel Loader");
        GameRegistry.registerTileEntity(TileEntityLandingPadSingle.class, "GC Landing Pad");
        GameRegistry.registerTileEntity(TileEntityLandingPad.class, "GC Landing Pad Full");
        GameRegistry.registerTileEntity(TileEntitySpaceStationBase.class, "GC Space Station");
        GameRegistry.registerTileEntity(TileEntityMulti.class, "GC Dummy Block");
        GameRegistry.registerTileEntity(TileEntityOxygenSealer.class, "GC Air Sealer");
        GameRegistry.registerTileEntity(TileEntityDungeonSpawner.class, "GC Dungeon Boss Spawner");
        GameRegistry.registerTileEntity(TileEntityOxygenDetector.class, "GC Oxygen Detector");
        GameRegistry.registerTileEntity(TileEntityBuggyFueler.class, "GC Buggy Fueler");
        GameRegistry.registerTileEntity(TileEntityBuggyFuelerSingle.class, "GC Buggy Fueler Single");
        GameRegistry.registerTileEntity(TileEntityCargoLoader.class, "GC Cargo Loader");
        GameRegistry.registerTileEntity(TileEntityCargoUnloader.class, "GC Cargo Unloader");
        GameRegistry.registerTileEntity(TileEntityParaChest.class, "GC Parachest Tile");
        GameRegistry.registerTileEntity(TileEntitySolar.class, "GC Solar Panel");
        GameRegistry.registerTileEntity(TileEntityDish.class, "GC Radio Telescope");
        GameRegistry.registerTileEntity(TileEntityCrafting.class, "GC Magnetic Crafting Table");
        GameRegistry.registerTileEntity(TileEntityEnergyStorageModule.class, "GC Energy Storage Module");
        GameRegistry.registerTileEntity(TileEntityCoalGenerator.class, "GC Coal Generator");
        GameRegistry.registerTileEntity(TileEntityElectricFurnace.class, "GC Electric Furnace");
        GameRegistry.registerTileEntity(TileEntityAluminumWire.class, "GC Aluminum Wire");
        GameRegistry.registerTileEntity(TileEntityAluminumWireSwitch.class, "GC Switchable Aluminum Wire");
        GameRegistry.registerTileEntity(TileEntityFallenMeteor.class, "GC Fallen Meteor");
        GameRegistry.registerTileEntity(TileEntityIngotCompressor.class, "GC Ingot Compressor");
        GameRegistry.registerTileEntity(TileEntityElectricIngotCompressor.class, "GC Electric Ingot Compressor");
        GameRegistry.registerTileEntity(TileEntityCircuitFabricator.class, "GC Circuit Fabricator");
        GameRegistry.registerTileEntity(TileEntityAirLockController.class, "GC Air Lock Controller");
        GameRegistry.registerTileEntity(TileEntityOxygenStorageModule.class, "GC Oxygen Storage Module");
        GameRegistry.registerTileEntity(TileEntityOxygenDecompressor.class, "GC Oxygen Decompressor");
        GameRegistry.registerTileEntity(TileEntityThruster.class, "GC Space Station Thruster");
        GameRegistry.registerTileEntity(TileEntityArclamp.class, "GC Arc Lamp");
        GameRegistry.registerTileEntity(TileEntityScreen.class, "GC View Screen");
        GameRegistry.registerTileEntity(TileEntityPanelLight.class, "GC Panel Lighting");
        GameRegistry.registerTileEntity(TileEntityTelemetry.class, "GC Telemetry Unit");
        GameRegistry.registerTileEntity(TileEntityPainter.class, "GC Painter");
        GameRegistry.registerTileEntity(TileEntityFluidTank.class, "GC Fluid Tank");
        GameRegistry.registerTileEntity(TileEntityPlayerDetector.class, "GC Player Detector");
        GameRegistry.registerTileEntity(TileEntityPlatform.class, "GC Platform");
        GameRegistry.registerTileEntity(TileEntityEmergencyBox.class, "GC Emergency Post");
        GameRegistry.registerTileEntity(TileEntityNull.class, "GC Null Tile");
    }

    private void registerCreatures()
    {
        GCCoreUtil.registerGalacticraftCreature(EntityEvolvedSpider.class, "evolved_spider", 3419431, 11013646);
        GCCoreUtil.registerGalacticraftCreature(EntityEvolvedZombie.class, "evolved_zombie", 44975, 7969893);
        GCCoreUtil.registerGalacticraftCreature(EntityEvolvedCreeper.class, "evolved_creeper", 894731, 0);
        GCCoreUtil.registerGalacticraftCreature(EntityEvolvedSkeleton.class, "evolved_skeleton", 12698049, 4802889);
        GCCoreUtil.registerGalacticraftCreature(EntitySkeletonBoss.class, "evolved_skeleton_boss", 12698049, 4802889);
        GCCoreUtil.registerGalacticraftCreature(EntityAlienVillager.class, "alien_villager", ColorUtil.to32BitColor(255, 103, 145, 181), 12422002);
        GCCoreUtil.registerGalacticraftCreature(EntityEvolvedEnderman.class, "evolved_enderman", 1447446, 0);
        GCCoreUtil.registerGalacticraftCreature(EntityEvolvedWitch.class, "evolved_witch", 3407872, 5349438);
    }

    private void registerOtherEntities()
    {
        GCCoreUtil.registerGalacticraftNonMobEntity(EntityTier1Rocket.class, "rocket_t1", 150, 1, false);
        GCCoreUtil.registerGalacticraftNonMobEntity(EntityMeteor.class, "meteor", 150, 5, true);
        GCCoreUtil.registerGalacticraftNonMobEntity(EntityBuggy.class, "buggy", 150, 5, true);
        GCCoreUtil.registerGalacticraftNonMobEntity(EntityFlag.class, "gcflag", 150, 5, true);
        GCCoreUtil.registerGalacticraftNonMobEntity(EntityParachest.class, "para_chest", 150, 5, true);
        GCCoreUtil.registerGalacticraftNonMobEntity(EntityLander.class, "lander", 150, 5, false);
        GCCoreUtil.registerGalacticraftNonMobEntity(EntityMeteorChunk.class, "meteor_chunk", 150, 5, true);
        GCCoreUtil.registerGalacticraftNonMobEntity(EntityCelestialFake.class, "celestial_screen", 150, 5, false);
        GCCoreUtil.registerGalacticraftNonMobEntity(EntityHangingSchematic.class, "hanging_schematic", 150, 5, false);
    }

    private Planet makeDummyPlanet(String name, SolarSystem system)
    {
        // Loop through all planets to make sure it's not registered as a reachable dimension first
        for (CelestialBody body : new ArrayList<>(GalaxyRegistry.getRegisteredPlanets().values()))
        {
            if (body instanceof Planet && name.equals(body.getName()))
            {
                if (((Planet) body).getParentSolarSystem() == system)
                {
                    return null;
                }
            }
        }

        Planet planet = new Planet(name).setParentSolarSystem(system);
        planet.setBodyIcon(new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/celestialbodies/" + name + ".png"));
        GalaxyRegistry.registerPlanet(planet);
        return planet;
    }

    private void initModInfo(ModMetadata info)
    {
        info.autogenerated = false;
        info.modId = Constants.MOD_ID_CORE;
        info.name = GalacticraftCore.NAME;
        info.version = Constants.COMBINEDVERSION;
        info.description = "An advanced space travel mod for Minecraft!";
        info.url = "https://micdoodle8.com/";
        info.authorList = Arrays.asList("micdoodle8", "radfast", "EzerArch", "fishtaco", "SpaceViking", "SteveKunG");
        info.logoFile = "assets/galacticraftcore/galacticraft_logo.png";
    }
}
