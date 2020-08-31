package micdoodle8.mods.galacticraft.core;

//import api.player.server.ServerPlayerAPI;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.client.IGameScreen;
import micdoodle8.mods.galacticraft.api.galaxies.*;
import micdoodle8.mods.galacticraft.api.item.EnumExtendedInventorySlot;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.AtmosphereInfo;
import micdoodle8.mods.galacticraft.api.world.BiomeGC;
import micdoodle8.mods.galacticraft.api.world.EnumAtmosphericGas;
import micdoodle8.mods.galacticraft.core.advancement.GCTriggers;
import micdoodle8.mods.galacticraft.core.client.screen.GameScreenBasic;
import micdoodle8.mods.galacticraft.core.client.screen.GameScreenCelestial;
import micdoodle8.mods.galacticraft.core.client.screen.GameScreenText;
import micdoodle8.mods.galacticraft.core.dimension.DimensionMoon;
import micdoodle8.mods.galacticraft.core.dimension.TeleportTypeMoon;
import micdoodle8.mods.galacticraft.core.dimension.TeleportTypeOverworld;
import micdoodle8.mods.galacticraft.core.energy.grid.ChunkPowerHandler;
import micdoodle8.mods.galacticraft.core.entities.GCEntities;
import micdoodle8.mods.galacticraft.core.entities.player.GCCapabilities;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler;
import micdoodle8.mods.galacticraft.core.event.EventHandlerGC;
import micdoodle8.mods.galacticraft.core.event.LootHandlerGC;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import micdoodle8.mods.galacticraft.core.items.ItemSchematic;
import micdoodle8.mods.galacticraft.core.network.GalacticraftChannelHandler;
import micdoodle8.mods.galacticraft.core.proxy.CommonProxyCore;
import micdoodle8.mods.galacticraft.core.schematic.SchematicAdd;
import micdoodle8.mods.galacticraft.core.schematic.SchematicMoonBuggy;
import micdoodle8.mods.galacticraft.core.schematic.SchematicRocketT1;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDeconstructor;
import micdoodle8.mods.galacticraft.core.util.*;
import micdoodle8.mods.galacticraft.core.world.gen.BiomeMoon;
import micdoodle8.mods.galacticraft.core.world.gen.BiomeOrbit;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import org.apache.maven.artifact.versioning.ArtifactVersion;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

//import net.minecraft.creativetab.CreativeTabs;

//@Mod(modid = Constants.MOD_ID_CORE, name = GalacticraftCore.NAME, version = Constants.COMBINEDVERSION, useMetadata = true, acceptedMinecraftVersions = Constants.MCVERSION, dependencies = Constants.DEPENDENCIES_FORGE + Constants.DEPENDENCIES_MICCORE + Constants.DEPENDENCIES_MODS, guiFactory = "micdoodle8.mods.galacticraft.core.client.gui.screen.ConfigGuiFactoryCore")
@Mod(Constants.MOD_ID_CORE)
public class GalacticraftCore
{
    public static final String NAME = "Galacticraft Core";
//    private File GCCoreSource;

//    @SidedProxy(clientSide = "micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore", serverSide = "micdoodle8.mods.galacticraft.core.proxy.CommonProxyCore")
//    public static CommonProxyCore proxy;

    public static CommonProxyCore proxy = DistExecutor.safeRunForDist(GalacticraftCore::getClientProxy, () -> CommonProxyCore::new);

    @OnlyIn(Dist.CLIENT)
    private static DistExecutor.SafeSupplier<CommonProxyCore> getClientProxy()
    {
        //NOTE: This extra method is needed to avoid classloading issues on servers
        return CommonProxyCore::new;
    }

    public static GalacticraftCore instance;

    public static boolean isPlanetsLoaded;

    public static boolean isHeightConflictingModInstalled;

    public static GalacticraftChannelHandler packetPipeline = new GalacticraftChannelHandler();
    public static GCPlayerHandler handler;

    public static ItemGroupGC galacticraftBlocksTab;
    public static ItemGroupGC galacticraftItemsTab;

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

    public static LinkedList<ItemStack> itemList = new LinkedList<>();
    public static LinkedList<Item> itemListTrue = new LinkedList<>();
    public static LinkedList<Block> blocksList = new LinkedList<>();
    public static LinkedList<BiomeGC> biomesList = new LinkedList<>();

    public static ImageWriter jpgWriter;
    public static ImageWriteParam writeParam;
    public static boolean enableJPEG = false;

    public final ArtifactVersion versionNumber;

//    static
//    {
//        FluidRegistry.enableUniversalBucket();
//    }

    public GalacticraftCore()
    {
        versionNumber = ModLoadingContext.get().getActiveContainer().getModInfo().getVersion();
        MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
        WorldUtil.DIMENSIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
        handler = new GCPlayerHandler();
        GCEntities.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.register(handler);
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event)
    {
//        GCCoreSource = event.getSourceFile();
        GCCapabilities.register();

        isPlanetsLoaded = ModList.get().isLoaded(Constants.MOD_ID_PLANETS);
        GCCoreUtil.nextID = 0;

        if (CompatibilityManager.isSmartMovingLoaded || CompatibilityManager.isWitcheryLoaded)
        {
            isHeightConflictingModInstalled = true;
        }

        MinecraftForge.EVENT_BUS.register(new EventHandlerGC());


//        ConnectionPacket.bus = PacketBase.createChannel(GalacticraftCore.rl(Constants.MOD_ID_CORE));
//        ConnectionPacket.bus.register(new ConnectionPacket());

//        ConfigManagerCore.initialize(new File(event.getModConfigurationDirectory(), Constants.CONFIG_FILE));
//        EnergyConfigHandler.setDefaultValues(new File(event.getModConfigurationDirectory(), Constants.POWER_CONFIG_FILE));
//        ChunkLoadingCallback.loadConfig(new File(event.getModConfigurationDirectory(), Constants.CHUNKLOADER_CONFIG_FILE));

//        GalacticraftCore.galacticraftBlocksTab = new CreativeTabGC(CreativeTabs.getNextID(), "galacticraft_blocks", null, null);
//        GalacticraftCore.galacticraftItemsTab = new CreativeTabGC(CreativeTabs.getNextID(), "galacticraft_items", null, null);

//        GCFluidRegistry.registerOilandFuel();

        if (CompatibilityManager.PlayerAPILoaded)
        {
//            ServerPlayerAPI.register(Constants.MOD_ID_CORE, GCPlayerBaseMP.class);
        }

        GalacticraftCore.solarSystemSol = new SolarSystem("sol", "milky_way").setMapPosition(new Vector3(0.0F, 0.0F, 0.0F));
        GalacticraftCore.planetOverworld = (Planet) new Planet("overworld").setParentSolarSystem(GalacticraftCore.solarSystemSol).setRingColorRGB(0.1F, 0.9F, 0.6F).setPhaseShift(0.0F);
        GalacticraftCore.moonMoon = (Moon) new Moon("moon").setParentPlanet(GalacticraftCore.planetOverworld).setRelativeSize(0.2667F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(13F, 13F)).setRelativeOrbitTime(1 / 0.01F);
        GalacticraftCore.satelliteSpaceStation = (Satellite) new Satellite("spacestation.overworld").setParentBody(GalacticraftCore.planetOverworld).setRelativeSize(0.2667F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(9F, 9F)).setRelativeOrbitTime(1 / 0.05F);

        GalacticraftCore.satelliteSpaceStation.setBiomeInfo(BiomeOrbit.space);
        GalacticraftCore.moonMoon.setBiomeInfo(BiomeMoon.moonBiome);

//        GCBlocks.initBlocks();
//        GCItems.initItems();

//        GCFluidRegistry.registerFluids();

        //Force initialisation of GC biome types in preinit (after config load) - this helps BiomeTweaker by initialising mod biomes in a fixed order during mod loading

        GalacticraftCore.galacticraftBlocksTab = new ItemGroupGC(-1, "gc_blocks", new ItemStack(GCBlocks.oxygenCompressor), (stack1, stack2) -> 0); //todo sorter
        GalacticraftCore.galacticraftItemsTab = new ItemGroupGC(-1, "gc_items", new ItemStack(GCItems.rocketTierOne), (stack1, stack2) -> 0); //todo sorter

//        if (FMLCommonHandler.instance().getSide() == LogicalSide.CLIENT)
//        {
//            GCBlocks.finalizeSort();
//            GCItems.finalizeSort();
//        }

        GalacticraftCore.proxy.init();

//        GalacticraftCore.packetPipeline = GalacticraftChannelHandler.init();

        Star starSol = (Star) new Star("sol").setParentSolarSystem(GalacticraftCore.solarSystemSol).setTierRequired(-1);
        starSol.setBodyIcon(new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/celestialbodies/sun.png"));
        GalacticraftCore.solarSystemSol.setMainStar(starSol);

        GalacticraftCore.planetOverworld.setBodyIcon(new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/celestialbodies/earth.png"));
        GalacticraftCore.planetOverworld.setDimensionInfo(DimensionType.OVERWORLD, OverworldDimension.class, false).setTierRequired(1);
        GalacticraftCore.planetOverworld.atmosphereComponent(EnumAtmosphericGas.NITROGEN).atmosphereComponent(EnumAtmosphericGas.OXYGEN).atmosphereComponent(EnumAtmosphericGas.ARGON).atmosphereComponent(EnumAtmosphericGas.WATER);
        GalacticraftCore.planetOverworld.addChecklistKeys("equip_parachute");

        GalacticraftCore.moonMoon.setDimensionInfo(WorldUtil.MOON_DIMENSION, DimensionMoon.class).setTierRequired(1);
        GalacticraftCore.moonMoon.setBodyIcon(new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/celestialbodies/moon.png"));
        GalacticraftCore.moonMoon.setAtmosphere(new AtmosphereInfo(false, false, false, 0.0F, 0.0F, 0.0F));
        GalacticraftCore.moonMoon.addMobInfo(new SpawnListEntry(GCEntities.EVOLVED_ZOMBIE.get(), 8, 2, 3), EntityClassification.MONSTER);
        GalacticraftCore.moonMoon.addMobInfo(new SpawnListEntry(GCEntities.EVOLVED_SPIDER.get(), 8, 2, 3), EntityClassification.MONSTER);
        GalacticraftCore.moonMoon.addMobInfo(new SpawnListEntry(GCEntities.EVOLVED_SKELETON.get(), 8, 2, 3), EntityClassification.MONSTER);
        GalacticraftCore.moonMoon.addMobInfo(new SpawnListEntry(GCEntities.EVOLVED_CREEPER.get(), 8, 2, 3), EntityClassification.MONSTER);
        GalacticraftCore.moonMoon.addMobInfo(new SpawnListEntry(GCEntities.EVOLVED_ENDERMAN.get(), 10, 1, 4), EntityClassification.MONSTER);
        GalacticraftCore.moonMoon.addChecklistKeys("equip_oxygen_suit");

        //Satellites must always have a WorldProvider implementing IOrbitDimension
//        GalacticraftCore.satelliteSpaceStation.setDimensionInfo(ConfigManagerCore.idDimensionOverworldOrbit, ConfigManagerCore.idDimensionOverworldOrbitStatic, DimensionOverworldOrbit.class).setTierRequired(1);
//        GalacticraftCore.satelliteSpaceStation.setBodyIcon(new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/celestialbodies/space_station.png"));
//        GalacticraftCore.satelliteSpaceStation.setAtmosphere(new AtmosphereInfo(false, false, false, 0.0F, 0.1F, 0.02F));
//        GalacticraftCore.satelliteSpaceStation.addChecklistKeys("equip_oxygen_suit", "create_grapple");

//        ForgeChunkManager.setForcedChunkLoadingCallback(GalacticraftCore.instance, new ChunkLoadingCallback()); TODO Chunkloader
//        MinecraftForge.EVENT_BUS.register(new ConnectionEvents());

        SchematicRegistry.registerSchematicRecipe(new SchematicRocketT1());
        SchematicRegistry.registerSchematicRecipe(new SchematicMoonBuggy());
        SchematicRegistry.registerSchematicRecipe(new SchematicAdd());
        ChunkPowerHandler.initiate();
//        EnergyConfigHandler.initGas();
        LootHandlerGC.registerAll();

//        this.registerCreatures();
//        this.registerOtherEntities();
//        this.registerTileEntities();

        GalaxyRegistry.registerSolarSystem(GalacticraftCore.solarSystemSol);
        GalaxyRegistry.registerPlanet(GalacticraftCore.planetOverworld);
        GalaxyRegistry.registerMoon(GalacticraftCore.moonMoon);
        GalaxyRegistry.registerSatellite(GalacticraftCore.satelliteSpaceStation);
//        GCDimensions.ORBIT = GalacticraftRegistry.registerDimension("Space Station", "_orbit", ConfigManagerCore.idDimensionOverworldOrbit, DimensionOverworldOrbit.class, false);
//        if (GCDimensions.ORBIT == null)
//        {
//            GCLog.severe("Failed to register space station dimension type with ID " + ConfigManagerCore.idDimensionOverworldOrbit);
//        }
//        GCDimensions.ORBIT_KEEPLOADED = GalacticraftRegistry.registerDimension("Space Station", "_orbit", ConfigManagerCore.idDimensionOverworldOrbitStatic, DimensionOverworldOrbit.class, true);
//        if (GCDimensions.ORBIT_KEEPLOADED == null)
//        {
//            GCLog.severe("Failed to register space station dimension type with ID " + ConfigManagerCore.idDimensionOverworldOrbitStatic);
//        }
        GalacticraftRegistry.registerTeleportType(OverworldDimension.class, new TeleportTypeOverworld());
//        GalacticraftRegistry.registerTeleportType(DimensionOverworldOrbit.class, new TeleportTypeOrbit());
        GalacticraftRegistry.registerTeleportType(DimensionMoon.class, new TeleportTypeMoon());
//        GalacticraftRegistry.registerRocketGui(DimensionOverworldOrbit.class, new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/overworld_rocket_gui.png"));
//        GalacticraftRegistry.registerRocketGui(WorldProviderSurface.class, new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/overworld_rocket_gui.png"));
//        GalacticraftRegistry.registerRocketGui(DimensionMoon.class, new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/moon_rocket_gui.png"));
        GalacticraftRegistry.addDungeonLoot(1, new ItemStack(GCItems.schematicBuggy, 1));
        GalacticraftRegistry.addDungeonLoot(1, new ItemStack(GCItems.schematicRocketT2, 1));

//        if (ConfigManagerCore.enableCopperOreGen)
//        {
//            GameRegistry.registerWorldGenerator(new OverworldGenerator(GCBlocks.basicBlock, 5, 24, 0, 75, 7), 4);
//        }
//
//        if (ConfigManagerCore.enableTinOreGen)
//        {
//            GameRegistry.registerWorldGenerator(new OverworldGenerator(GCBlocks.basicBlock, 6, 22, 0, 60, 7), 4);
//        }
//
//        if (ConfigManagerCore.enableAluminumOreGen)
//        {
//            GameRegistry.registerWorldGenerator(new OverworldGenerator(GCBlocks.basicBlock, 7, 18, 0, 45, 7), 4);
//        }
//
//        if (ConfigManagerCore.enableSiliconOreGen)
//        {
//            GameRegistry.registerWorldGenerator(new OverworldGenerator(GCBlocks.basicBlock, 8, 3, 0, 25, 7), 4);
//        } TODO Ore generation

//        FMLInterModComms.sendMessage("OpenBlocks", "donateUrl", "http://www.patreon.com/micdoodle8"); TODO IMC
        registerCoreGameScreens();

//        GCFluidRegistry.registerLegacyFluids();
//        GCFluidRegistry.registerDispenserBehaviours();
//        if (CompatibilityManager.isBCraftEnergyLoaded()) GCFluidRegistry.registerBCFuel(); TODO BC Compat

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
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_PARACHUTE, EnumExtendedInventorySlot.PARACHUTE, GCItems.parachutePlain);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_PARACHUTE, EnumExtendedInventorySlot.PARACHUTE, GCItems.parachuteBlack);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_PARACHUTE, EnumExtendedInventorySlot.PARACHUTE, GCItems.parachuteBlue);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_PARACHUTE, EnumExtendedInventorySlot.PARACHUTE, GCItems.parachuteLime);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_PARACHUTE, EnumExtendedInventorySlot.PARACHUTE, GCItems.parachuteBrown);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_PARACHUTE, EnumExtendedInventorySlot.PARACHUTE, GCItems.parachuteDarkBlue);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_PARACHUTE, EnumExtendedInventorySlot.PARACHUTE, GCItems.parachuteDarkGray);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_PARACHUTE, EnumExtendedInventorySlot.PARACHUTE, GCItems.parachuteDarkGreen);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_PARACHUTE, EnumExtendedInventorySlot.PARACHUTE, GCItems.parachuteGray);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_PARACHUTE, EnumExtendedInventorySlot.PARACHUTE, GCItems.parachuteMagenta);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_PARACHUTE, EnumExtendedInventorySlot.PARACHUTE, GCItems.parachuteOrange);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_PARACHUTE, EnumExtendedInventorySlot.PARACHUTE, GCItems.parachutePink);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_PARACHUTE, EnumExtendedInventorySlot.PARACHUTE, GCItems.parachutePurple);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_PARACHUTE, EnumExtendedInventorySlot.PARACHUTE, GCItems.parachuteRed);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_PARACHUTE, EnumExtendedInventorySlot.PARACHUTE, GCItems.parachuteTeal);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_PARACHUTE, EnumExtendedInventorySlot.PARACHUTE, GCItems.parachuteYellow);
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_FREQUENCY_MODULE, EnumExtendedInventorySlot.FREQUENCY_MODULE, GCItems.frequencyModule);

        GalacticraftCore.proxy.registerFluidTexture(GCFluids.OIL.getFluid(), new ResourceLocation(Constants.MOD_ID_CORE, "textures/misc/underoil.png"));
        GalacticraftCore.proxy.registerFluidTexture(GCFluids.FUEL.getFluid(), new ResourceLocation(Constants.MOD_ID_CORE, "textures/misc/underfuel.png"));

        PermissionAPI.registerNode(Constants.PERMISSION_CREATE_STATION, DefaultPermissionLevel.ALL, "Allows players to create space stations");

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
//            return itemstack.getItem() == GCItems.basicItem && itemstack.getDamage() == 19;
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
        GCTriggers.registerTriggers();

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

//        MinecraftForge.EVENT_BUS.register(new OreGenOtherMods());

        ArrayList<CelestialBody> cBodyList = new ArrayList<CelestialBody>();
        cBodyList.addAll(GalaxyRegistry.getRegisteredPlanets().values());
        cBodyList.addAll(GalaxyRegistry.getRegisteredMoons().values());

        for (CelestialBody body : cBodyList)
        {
            if (body.shouldAutoRegister())
            {
//                int id = Arrays.binarySearch(ConfigManagerCore.staticLoadDimensions, body.getDimensionID().getId());
                //It's important this is done in the same order as planets will be registered by WorldUtil.registerPlanet();
//                DimensionType type = GalacticraftRegistry.registerDimension(body.getUnlocalizedName(), body.getDimensionSuffix(), body.getDimensionID(), body.getWorldProvider(), body.getForceStaticLoad() || id < 0);
//                if (type != null)
//                {
//                    body.initialiseMobSpawns();
//                }
//                else
//                {
//                    body.setUnreachable();
//                    GCLog.severe("Tried to register dimension for body: " + body.getUnlocalizedName() + " hit conflict with ID " + body.getDimensionID());
//                } TODO Fix registration
            }

            if (body.getSurfaceBlocks() != null)
            {
                TransformerHooks.spawnListAE2_GC.addAll(body.getSurfaceBlocks());
            }
        }

//        GCDimensions.MOON = WorldUtil.getDimensionTypeById(ConfigManagerCore.idDimensionMoon);

        CompatibilityManager.checkForCompatibleMods();
//        RecipeManagerGC.loadCompatibilityRecipes();
        TileEntityDeconstructor.initialiseRecipeList();
        ItemSchematic.registerSchematicItems();
//        NetworkRegistry.INSTANCE.registerGuiHandler(GalacticraftCore.instance, new GuiHandler());
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

//        if (event.getSide() == LogicalSide.SERVER)
//        {
//            this.loadLanguageCore("en_US");
//        } TODO ?
    }

//    public void loadLanguageCore(String lang)
//    {
//        GCCoreUtil.loadLanguage(lang, Constants.MOD_ID_CORE, this.GCCoreSource);
//    }

    @SubscribeEvent
    public void serverAboutToStart(FMLServerAboutToStartEvent event)
    {
        TickHandlerServer.restart();
    }

    @SubscribeEvent
    public void serverInit(FMLServerStartedEvent event)
    {
        if (ThreadRequirementMissing.INSTANCE == null)
        {
            ThreadRequirementMissing.beginCheck(GCCoreUtil.getEffectiveSide());
        }

        ThreadVersionCheck.startCheck();
        BlockVec3.chunkCacheDim = null;
    }

    private void serverStarting(FMLServerStartingEvent event)
    {
        GCCoreUtil.notifyStarted(event.getServer());
//        File worldFolder = DimensionManager.getCurrentSaveRootDirectory();
//        moveLegacyGCFileLocations(worldFolder);

//        event.getCommandDispatcher().register(new CommandSpaceStationAddOwner());
//        event.getCommandDispatcher().register(new CommandSpaceStationChangeOwner());
//        event.getCommandDispatcher().register(new CommandSpaceStationRemoveOwner());
//        event.getCommandDispatcher().register(new CommandPlanetTeleport());
//        event.getCommandDispatcher().register(new CommandKeepDim());
//        event.getCommandDispatcher().register(new CommandGCInv());
//        event.getCommandDispatcher().register(new CommandGCHelp());
//        event.getCommandDispatcher().register(new CommandGCKit());
//        event.getCommandDispatcher().register(new CommandGCHouston());
//        event.getCommandDispatcher().register(new CommandGCEnergyUnits());
//        event.getCommandDispatcher().register(new CommandJoinSpaceRace()); TODO Commands

//        WorldUtil.initialiseDimensionNames();
        WorldUtil.registerSpaceStations(event.getServer(), new File(event.getServer().getWorld(DimensionType.OVERWORLD).getSaveHandler().getPlayerFolder(), "galacticraft"));

        ArrayList<CelestialBody> cBodyList = new ArrayList<CelestialBody>();
        cBodyList.addAll(GalaxyRegistry.getRegisteredPlanets().values());
        cBodyList.addAll(GalaxyRegistry.getRegisteredMoons().values());

//        for (CelestialBody body : cBodyList)
//        {
//            if (body.shouldAutoRegister())
//            {
//                if (!WorldUtil.registerPlanet(body.getDimensionID(), body.getReachable(), 0))
//                {
//                    body.setUnreachable();
//                }
//            }
//        }
    }

    private void moveLegacyGCFileLocations(File worldFolder)
    {
        File destFolder = new File(worldFolder, "galacticraft");
        if (!destFolder.exists())
        {
            if (!destFolder.mkdirs())
            {
                return;
            }
        }
        File dataFolder = new File(worldFolder, "data");
        if (!dataFolder.exists())
        {
            return;
        }

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
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

//    @EventHandler
//    public void onServerStopping(FMLServerStoppingEvent var1)
//    {
//        MapUtil.saveMapProgress();
//    }
//
//    @EventHandler
//    public void onServerStop(FMLServerStoppedEvent var1)
//    {
//        // Unregister dimensions
//        WorldUtil.unregisterPlanets();
//        WorldUtil.unregisterSpaceStations();
//        GCCoreUtil.notifyStarted(null);
//    }

    private static void registerCoreGameScreens()
    {
        if (GCCoreUtil.getEffectiveSide() == LogicalSide.CLIENT)
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

//    private void registerTileEntities()
//    {
//        GameRegistry.registerTileEntity(TileEntityTreasureChest.class, "GC Treasure Chest");
//        GameRegistry.registerTileEntity(TileEntityOxygenDistributor.class, "GC Air Distributor");
//        GameRegistry.registerTileEntity(TileEntityOxygenCollector.class, "GC Air Collector");
//        GameRegistry.registerTileEntity(TileEntityFluidPipe.class, "GC Oxygen Pipe");
//        GameRegistry.registerTileEntity(TileEntityAirLock.class, "GC Air Lock Frame");
//        GameRegistry.registerTileEntity(TileEntityRefinery.class, "GC Refinery");
//        GameRegistry.registerTileEntity(TileEntityNasaWorkbench.class, "GC NASA Workbench");
//        GameRegistry.registerTileEntity(TileEntityDeconstructor.class, "GC Deconstructor");
//        GameRegistry.registerTileEntity(TileEntityOxygenCompressor.class, "GC Air Compressor");
//        GameRegistry.registerTileEntity(TileEntityFuelLoader.class, "GC Fuel Loader");
//        GameRegistry.registerTileEntity(TileEntityLandingPadSingle.class, "GC Landing Pad");
//        GameRegistry.registerTileEntity(TileEntityLandingPad.class, "GC Landing Pad Full");
//        GameRegistry.registerTileEntity(TileEntitySpaceStationBase.class, "GC Space Station");
//        GameRegistry.registerTileEntity(TileEntityMulti.class, "GC Dummy Block");
//        GameRegistry.registerTileEntity(TileEntityOxygenSealer.class, "GC Air Sealer");
//        GameRegistry.registerTileEntity(TileEntityDungeonSpawner.class, "GC Dungeon Boss Spawner");
//        GameRegistry.registerTileEntity(TileEntityOxygenDetector.class, "GC Oxygen Detector");
//        GameRegistry.registerTileEntity(TileEntityBuggyFueler.class, "GC Buggy Fueler");
//        GameRegistry.registerTileEntity(TileEntityBuggyFuelerSingle.class, "GC Buggy Fueler Single");
//        GameRegistry.registerTileEntity(TileEntityCargoLoader.class, "GC Cargo Loader");
//        GameRegistry.registerTileEntity(TileEntityCargoUnloader.class, "GC Cargo Unloader");
//        GameRegistry.registerTileEntity(TileEntityParaChest.class, "GC Parachest Tile");
//        GameRegistry.registerTileEntity(TileEntitySolar.class, "GC Solar Panel");
//        GameRegistry.registerTileEntity(TileEntityDish.class, "GC Radio Telescope");
//        GameRegistry.registerTileEntity(TileEntityCrafting.class, "GC Magnetic Crafting Table");
//        GameRegistry.registerTileEntity(TileEntityEnergyStorageModule.class, "GC Energy Storage Module");
//        GameRegistry.registerTileEntity(TileEntityCoalGenerator.class, "GC Coal Generator");
//        GameRegistry.registerTileEntity(TileEntityElectricFurnace.class, "GC Electric Furnace");
//        GameRegistry.registerTileEntity(TileEntityAluminumWire.class, "GC Aluminum Wire");
//        GameRegistry.registerTileEntity(TileEntityAluminumWireSwitch.class, "GC Switchable Aluminum Wire");
//        GameRegistry.registerTileEntity(TileEntityFallenMeteor.class, "GC Fallen Meteor");
//        GameRegistry.registerTileEntity(TileEntityIngotCompressor.class, "GC Ingot Compressor");
//        GameRegistry.registerTileEntity(TileEntityElectricIngotCompressor.class, "GC Electric Ingot Compressor");
//        GameRegistry.registerTileEntity(TileEntityCircuitFabricator.class, "GC Circuit Fabricator");
//        GameRegistry.registerTileEntity(TileEntityAirLockController.class, "GC Air Lock Controller");
//        GameRegistry.registerTileEntity(TileEntityOxygenStorageModule.class, "GC Oxygen Storage Module");
//        GameRegistry.registerTileEntity(TileEntityOxygenDecompressor.class, "GC Oxygen Decompressor");
//        GameRegistry.registerTileEntity(TileEntityThruster.class, "GC Space Station Thruster");
//        GameRegistry.registerTileEntity(TileEntityArclamp.class, "GC Arc Lamp");
//        GameRegistry.registerTileEntity(TileEntityScreen.class, "GC View Screen");
//        GameRegistry.registerTileEntity(TileEntityPanelLight.class, "GC Panel Lighting");
//        GameRegistry.registerTileEntity(TileEntityTelemetry.class, "GC Telemetry Unit");
//        GameRegistry.registerTileEntity(TileEntityPainter.class, "GC Painter");
//        GameRegistry.registerTileEntity(TileEntityFluidTank.class, "GC Fluid Tank");
//        GameRegistry.registerTileEntity(TileEntityPlayerDetector.class, "GC Player Detector");
//        GameRegistry.registerTileEntity(TileEntityPlatform.class, "GC Platform");
//        GameRegistry.registerTileEntity(TileEntityEmergencyBox.class, "GC Emergency Post");
//        GameRegistry.registerTileEntity(TileEntityNull.class, "GC Null Tile");
//        if (CompatibilityManager.isIc2Loaded())
//        {
//            GameRegistry.registerTileEntity(TileCableIC2Sealed.class, "GC Sealed IC2 Cable");
//        }
//    }

//    private void registerCreatures()
//    {
//        GCCoreUtil.registerGalacticraftCreature(EntityEvolvedSpider.class, "evolved_spider", 3419431, 11013646);
//        GCCoreUtil.registerGalacticraftCreature(EntityEvolvedZombie.class, "evolved_zombie", 44975, 7969893);
//        GCCoreUtil.registerGalacticraftCreature(EntityEvolvedCreeper.class, "evolved_creeper", 894731, 0);
//        GCCoreUtil.registerGalacticraftCreature(EntityEvolvedSkeleton.class, "evolved_skeleton", 12698049, 4802889);
//        GCCoreUtil.registerGalacticraftCreature(EntitySkeletonBoss.class, "evolved_skeleton_boss", 12698049, 4802889);
//        GCCoreUtil.registerGalacticraftCreature(EntityAlienVillager.class, "alien_villager", ColorUtil.to32BitColor(255, 103, 145, 181), 12422002);
//        GCCoreUtil.registerGalacticraftCreature(EntityEvolvedEnderman.class, "evolved_enderman", 1447446, 0);
//        GCCoreUtil.registerGalacticraftCreature(EntityEvolvedWitch.class, "evolved_witch", 3407872, 5349438);
//    }
//
//    private void registerOtherEntities()
//    {
//        GCCoreUtil.registerGalacticraftNonMobEntity(EntityTier1Rocket.class, "rocket_t1", 150, 1, false);
//        GCCoreUtil.registerGalacticraftNonMobEntity(EntityMeteor.class, "meteor", 150, 5, true);
//        GCCoreUtil.registerGalacticraftNonMobEntity(EntityBuggy.class, "buggy", 150, 5, true);
//        GCCoreUtil.registerGalacticraftNonMobEntity(EntityFlag.class, "gcflag", 150, 5, true);
//        GCCoreUtil.registerGalacticraftNonMobEntity(EntityParachest.class, "para_chest", 150, 5, true);
//        GCCoreUtil.registerGalacticraftNonMobEntity(EntityLander.class, "lander", 150, 5, false);
//        GCCoreUtil.registerGalacticraftNonMobEntity(EntityMeteorChunk.class, "meteor_chunk", 150, 5, true);
//        GCCoreUtil.registerGalacticraftNonMobEntity(EntityCelestialFake.class, "celestial_screen", 150, 5, false);
//        GCCoreUtil.registerGalacticraftNonMobEntity(EntityHangingSchematic.class, "hanging_schematic", 150, 5, false);
//    } TODO Entities

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
        planet.setBodyIcon(new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/celestialbodies/" + name + ".png"));
        GalaxyRegistry.registerPlanet(planet);
        return planet;
    }

    public static ResourceLocation rl(String path)
    {
        return new ResourceLocation(Constants.MOD_ID_CORE, path);
    }

//    private void initModInfo(ModMetadata info)
//    {
//        info.autogenerated = false;
//        info.modId = Constants.MOD_ID_CORE;
//        info.name = GalacticraftCore.NAME;
//        info.version = Constants.COMBINEDVERSION;
//        info.description = "An advanced space travel mod for Minecraft!";
//        info.url = "https://micdoodle8.com/";
//        info.authorList = Arrays.asList("micdoodle8", "radfast", "EzerArch", "fishtaco", "SpaceViking", "SteveKunG");
//        info.logoFile = "assets/galacticraftcore/galacticraft_logo.png";
//    } TODO

    //    @Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE)
//    public static class RegistrationHandler
//    {
//        @SubscribeEvent
//        public static void registerBlocks(RegistryEvent.Register<Block> event)
//        {
//            GCBlocks.registerBlocks(event.getRegistry());
//            CompatibilityManager.registerMicroBlocks();
//        }
//
//        @SubscribeEvent
//        public static void registerItems(RegistryEvent.Register<Item> event)
//        {
//            // First, the final steps of block registration
//            IForgeRegistry<Block> blockRegistry = RegistryManager.ACTIVE.getRegistry(GameData.BLOCKS);
//            GCBlocks.doOtherModsTorches(blockRegistry);
//            BlockGrating.createForgeFluidVersions(blockRegistry);
//
//            GCItems.registerItems(event.getRegistry());
//
//            //RegisterSorted for blocks cannot be run until all the items have been registered
//            if (GCCoreUtil.getEffectiveSide() == LogicalSide.CLIENT)
//            {
//                for (Item item : GalacticraftCore.itemListTrue)
//                {
//                    GCItems.registerSorted(item);
//                }
//                for (Block block : GalacticraftCore.blocksList)
//                {
//                    GCBlocks.registerSorted(block);
//                }
//            }
//
//            GCBlocks.oreDictRegistrations();
//            GCItems.oreDictRegistrations();
//        }
//
//        @SubscribeEvent(priority = EventPriority.LOWEST)
//        public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
//        {
//            RecipeManagerGC.addUniversalRecipes();
//            RecipeManagerGC.setConfigurableRecipes();
//            if (isPlanetsLoaded)
//            {
//            	RecipeManagerAsteroids.addUniversalRecipes();
//            	RecipeManagerMars.addUniversalRecipes();
//            	RecipeManagerVenus.addUniversalRecipes();
//            }
//        }
//
//        @SubscribeEvent
//        public static void registerModels(ModelRegistryEvent event)
//        {
//            proxy.registerVariants();
//            if (isPlanetsLoaded)
//            {
//                GalacticraftPlanets.proxy.registerVariants();
//            }
//        }
//
    @SubscribeEvent
    public void registerBiomes(RegistryEvent.Register<Biome> event)
    {
        // First, final steps of item registration
        GalacticraftCore.handler.registerTorchTypes();
        GalacticraftCore.handler.registerItemChanges();

        for (BiomeGC biome : GalacticraftCore.biomesList)
        {
            event.getRegistry().register(biome);
            if (!ConfigManagerCore.disableBiomeTypeRegistrations)
            {
                biome.registerTypes(biome);
            }
        }


    }
//
//        @SubscribeEvent
//        public static void registerSounds(RegistryEvent.Register<SoundEvent> event)
//        {
//            if (GCCoreUtil.getEffectiveSide() == LogicalSide.CLIENT)
//            {
//                GCSounds.registerSounds(event.getRegistry());
//            }
//        }
//    }
}
