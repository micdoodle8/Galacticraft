package micdoodle8.mods.galacticraft.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import micdoodle8.mods.galacticraft.API.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.API.IGalacticraftSubMod;
import micdoodle8.mods.galacticraft.API.IGalacticraftSubModClient;
import micdoodle8.mods.galacticraft.API.IGalaxy;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.API.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.GCCorePlayerSP;
import micdoodle8.mods.galacticraft.core.command.GCCoreCommandSpaceStationAddOwner;
import micdoodle8.mods.galacticraft.core.command.GCCoreCommandSpaceStationRemoveOwner;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreOrbitTeleportType;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreOverworldTeleportType;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreWorldProviderSpaceStation;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityAlienVillager;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityArrow;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityAstroOrb;
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
import micdoodle8.mods.galacticraft.core.schematic.GCCoreSchematicAdd;
import micdoodle8.mods.galacticraft.core.schematic.GCCoreSchematicMoonBuggy;
import micdoodle8.mods.galacticraft.core.schematic.GCCoreSchematicRocketT1;
import micdoodle8.mods.galacticraft.core.tick.GCCoreTickHandlerCommon;
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
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityRefinery;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntitySpaceStationBase;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityTreasureChest;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityUnlitTorch;
import micdoodle8.mods.galacticraft.core.util.DupKeyHashMap;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.moon.GalacticraftMoon;
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
import universalelectricity.prefab.TranslationHelper;
import universalelectricity.prefab.multiblock.TileEntityMulti;
import basiccomponents.common.BasicComponents;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarted;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.Mod.ServerStopped;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
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
@Mod(name = GalacticraftCore.NAME, version = GalacticraftCore.LOCALMAJVERSION + "." + GalacticraftCore.LOCALMINVERSION + "." + GalacticraftCore.LOCALBUILDVERSION, useMetadata = true, modid = GalacticraftCore.MODID, dependencies = "required-after:Forge@[7.8.0.685,)")
@NetworkMod(channels = { GalacticraftCore.CHANNEL }, clientSideRequired = true, serverSideRequired = false, connectionHandler = GCCoreConnectionHandler.class, packetHandler = GCCorePacketManager.class)
public class GalacticraftCore
{
    public static final String NAME = "Galacticraft Core"; 
    public static final String MODID = "GalacticraftCore";
    public static final String CHANNEL = "GalacticraftCore";
    public static final String CHANNELENTITIES = "GCCoreEntities";

    public static final int LOCALMAJVERSION = 0;
    public static final int LOCALMINVERSION = 1;
    public static final int LOCALBUILDVERSION = 35;
    public static int remoteMajVer;
    public static int remoteMinVer;
    public static int remoteBuildVer;

    // public static float spaceScale = 4.0F;
    // public static float spaceSpeedScale = 5.0F;

    @SidedProxy(clientSide = "micdoodle8.mods.galacticraft.core.client.ClientProxyCore", serverSide = "micdoodle8.mods.galacticraft.core.CommonProxyCore")
    public static CommonProxyCore proxy;

    @Instance(GalacticraftCore.MODID)
    public static GalacticraftCore instance;

    public static GalacticraftMoon moon = new GalacticraftMoon();

    public static Map<String, GCCorePlayerSP> playersClient = new HashMap<String, GCCorePlayerSP>();
    public static Map<String, GCCorePlayerMP> playersServer = new HashMap<String, GCCorePlayerMP>();

    public static List<IGalacticraftSubMod> subMods = new ArrayList<IGalacticraftSubMod>();
    public static List<IGalacticraftSubModClient> clientSubMods = new ArrayList<IGalacticraftSubModClient>();

    public static List<IGalaxy> galaxies = new ArrayList<IGalaxy>();

    public static List<IMapPlanet> mapPlanets = new ArrayList<IMapPlanet>();
    public static DupKeyHashMap mapMoons = new DupKeyHashMap();

    public static CreativeTabs galacticraftTab;

    public static final IGalaxy galaxyMilkyWay = new GCCoreGalaxyBlockyWay();

    public static final String FILE_PATH = "/micdoodle8/mods/galacticraft/core/";
    public static final String CLIENT_PATH = "client/";
    public static final String LANGUAGE_PATH = GalacticraftCore.FILE_PATH + GalacticraftCore.CLIENT_PATH + "lang/";
    public static final String BLOCK_TEXTURE_FILE = GalacticraftCore.FILE_PATH + GalacticraftCore.CLIENT_PATH + "blocks/core.png";
    public static final String ITEM_TEXTURE_FILE = GalacticraftCore.FILE_PATH + GalacticraftCore.CLIENT_PATH + "items/core.png";
    public static final String CONFIG_FILE = "Galacticraft/core.conf";
    private static final String[] LANGUAGES_SUPPORTED = new String[] { "cz_CZE", "de_DE", "en_GB", "en_US", "es_ES", "fi_FI", "fr_CA", "fr_FR", "nl_NL", "pl_PL", "ru_RU", "zh_CN" };

    public static double toBuildcraftEnergyScalar = 0.04D;
    public static double fromBuildcraftEnergyScalar = 25.0D;

    public static boolean usingDevVersion = false;

    public static ArrayList<Integer> hiddenItems = new ArrayList<Integer>();

    public static boolean inMCP = true;
    public static boolean playerAPILoaded = true;

    public static String TEXTURE_SUFFIX;

    public static LiquidStack oilStack;
    public static LiquidStack fuelStack;

    public static boolean setSpaceStationRecipe = false;

    @PreInit
    public void preInit(FMLPreInitializationEvent event)
    {
        if (!this.checkForCoremod())
        {
            final String err = "Galacticraft Core is not placed in the coremods folder!";
            System.err.println(err);

            final JEditorPane ep = new JEditorPane("text/html", "<html>" + err + "</html>");

            ep.setEditable(false);
            ep.setOpaque(false);

            JOptionPane.showMessageDialog(null, ep, "Fatal error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        GalacticraftCore.moon.preLoad(event);

        GalacticraftCore.registerSubMod(GalacticraftCore.moon);

        new GCCoreConfigManager(new File(event.getModConfigurationDirectory(), GalacticraftCore.CONFIG_FILE));

        GalacticraftCore.TEXTURE_SUFFIX = GCCoreConfigManager.hiresTextures ? "_32" : "";

        GCCoreBlocks.initBlocks();
        GCCoreBlocks.registerBlocks();
        GCCoreBlocks.setHarvestLevels();

        GCCoreItems.initItems();
        GCCoreItems.registerHarvestLevels();

        GalacticraftCore.proxy.preInit(event);
    }

    @Init
    public void init(FMLInitializationEvent event)
    {
        GalacticraftCore.galacticraftTab = new GCCoreCreativeTab(CreativeTabs.getNextID(), GalacticraftCore.CHANNEL, GCCoreItems.spaceship.itemID, 0);

        DimensionManager.registerProviderType(GCCoreConfigManager.idDimensionOverworldOrbit, GCCoreWorldProviderSpaceStation.class, false);

        GalacticraftCore.proxy.init(event);

        GalacticraftRegistry.registerTeleportType(WorldProviderSurface.class, new GCCoreOverworldTeleportType());
        GalacticraftRegistry.registerTeleportType(GCCoreWorldProviderSpaceStation.class, new GCCoreOrbitTeleportType());

        for (final IGalacticraftSubMod mod : GalacticraftCore.subMods)
        {
            if (mod.getParentGalaxy() != null && !GalacticraftCore.galaxies.contains(mod.getParentGalaxy()))
            {
                GalacticraftCore.galaxies.add(mod.getParentGalaxy());
            }
        }

        GCLog.info("Galacticraft Loaded: " + TranslationHelper.loadLanguages(GalacticraftCore.LANGUAGE_PATH, GalacticraftCore.LANGUAGES_SUPPORTED) + " Languages.");

        GalacticraftCore.moon.load(event);

        GalacticraftCore.oilStack = LiquidDictionary.getOrCreateLiquid("Oil", new LiquidStack(GCCoreBlocks.crudeOilStill, 1));
        GalacticraftCore.fuelStack = LiquidDictionary.getOrCreateLiquid("Fuel", new LiquidStack(GCCoreItems.fuel, 1));

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

        RecipeUtil.addSmeltingRecipes();
        this.registerCreatures();
        this.registerOtherEntities();
        MinecraftForge.EVENT_BUS.register(new GCCoreEvents());
        NetworkRegistry.instance().registerChannel(new GCCorePacketManager(), GalacticraftCore.CHANNELENTITIES, Side.CLIENT);
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event)
    {
        GalacticraftCore.moon.postLoad(event);
        GCCoreCompatibilityManager.checkForCompatibleMods();

        this.registerTileEntities();

        if (GCCoreCompatibilityManager.isTELoaded() && GCCoreConfigManager.useRecipesTE)
        {
            RecipeUtil.addThermalExpansionCraftingRecipes();
        }

        if (GCCoreCompatibilityManager.isGTLoaded() && GCCoreConfigManager.useRecipesGT)
        {
            RecipeUtil.addGregTechCraftingRecipes();
        }

        if (GCCoreCompatibilityManager.isIc2Loaded() && GCCoreConfigManager.useRecipesIC2)
        {
            RecipeUtil.addIndustrialcraftCraftingRecipes();
        }

        if (GCCoreConfigManager.loadBC.getBoolean(false))
        {
            BasicComponents.registerTileEntities();
            BasicComponents.requestAll(GalacticraftCore.instance);
            BasicComponents.register(GalacticraftCore.CHANNELENTITIES);

            RecipeUtil.addBasicComponentsCraftingRecipes();
        }
        
        NetworkRegistry.instance().registerGuiHandler(this, GalacticraftCore.proxy);

        GalacticraftCore.proxy.postInit(event);
        GalacticraftCore.proxy.registerRenderInformation();

        GCCoreThreadRequirementMissing.startCheck();
    }

    @ServerStarted
    public void serverInit(FMLServerStartedEvent event)
    {
        GalacticraftCore.moon.serverInit(event);

        GCCoreUtil.checkVersion(Side.SERVER);
        TickRegistry.registerTickHandler(new GCCoreTickHandlerCommon(), Side.SERVER);
        NetworkRegistry.instance().registerChannel(new GCCorePacketHandlerServer(), GalacticraftCore.CHANNEL, Side.SERVER);
    }

    @ServerStarting
    public void serverStarting(FMLServerStartingEvent event)
    {
        // WorldUtil.registerPlanet(GCCoreConfigManager.idDimensionSpace, true);
        GalacticraftCore.moon.serverStarting(event);
        event.registerServerCommand(new GCCoreCommandSpaceStationAddOwner());
        event.registerServerCommand(new GCCoreCommandSpaceStationRemoveOwner());
        WorldUtil.registerSpaceStations(event.getServer().worldServerForDimension(0).getSaveHandler().getMapFileFromName("dummy").getParentFile());
    }

    @ServerStopped
    public void unregisterDims(FMLServerStoppedEvent var1)
    {
        WorldUtil.unregisterPlanets();
        WorldUtil.unregisterSpaceStations();
    }

    public static void registerSlotRenderer(IPlanetSlotRenderer renderer)
    {
        GalacticraftCore.proxy.addSlotRenderer(renderer);
    }

    public static void registerSubMod(IGalacticraftSubMod mod)
    {
        GalacticraftCore.subMods.add(mod);
    }

    public static void registerClientSubMod(IGalacticraftSubModClient mod)
    {
        GalacticraftCore.clientSubMods.add(mod);
    }

    public static void addAdditionalMapPlanet(IMapPlanet planet)
    {
        GalacticraftCore.mapPlanets.add(planet);
    }

    public static void addAdditionalMapMoon(IMapPlanet planet, IMapPlanet moon)
    {
        GalacticraftCore.mapMoons.put(planet, moon);
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
        GameRegistry.registerTileEntity(GCCoreTileEntityUnlitTorch.class, "Unlit Torch");
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
    }

    public void registerCreatures()
    {
        this.registerGalacticraftCreature(GCCoreEntitySpider.class, "Evolved Spider", GCCoreConfigManager.idEntityEvolvedSpider, 3419431, 11013646);
        this.registerGalacticraftCreature(GCCoreEntityZombie.class, "Evolved Zombie", GCCoreConfigManager.idEntityEvolvedZombie, 44975, 7969893);
        this.registerGalacticraftCreature(GCCoreEntityCreeper.class, "Evolved Creeper", GCCoreConfigManager.idEntityEvolvedCreeper, 894731, 0);
        this.registerGalacticraftCreature(GCCoreEntitySkeleton.class, "Evolved Skeleton", GCCoreConfigManager.idEntityEvolvedSkeleton, 12698049, 4802889);
        this.registerGalacticraftCreature(GCCoreEntitySkeletonBoss.class, "Evolved Skeleton Boss", GCCoreConfigManager.idEntityEvolvedSkeletonBoss, 12698049, 4802889);
        this.registerGalacticraftCreature(GCCoreEntityAlienVillager.class, "Alien Villager", GCCoreConfigManager.idEntityAlienVillager, GCCoreUtil.convertTo32BitColor(255, 103, 181, 145), 12422002);
    }

    public void registerOtherEntities()
    {
        this.registerGalacticraftNonMobEntity(GCCoreEntityRocketT1.class, "Spaceship", GCCoreConfigManager.idEntitySpaceship, 150, 1, true);
        this.registerGalacticraftNonMobEntity(GCCoreEntityArrow.class, "Gravity Arrow", GCCoreConfigManager.idEntityAntiGravityArrow, 150, 5, true);
        this.registerGalacticraftNonMobEntity(GCCoreEntityMeteor.class, "Meteor", GCCoreConfigManager.idEntityMeteor, 150, 5, true);
        this.registerGalacticraftNonMobEntity(GCCoreEntityBuggy.class, "Buggy", GCCoreConfigManager.idEntityBuggy, 150, 5, true);
        this.registerGalacticraftNonMobEntity(GCCoreEntityFlag.class, "Flag", GCCoreConfigManager.idEntityFlag, 150, 5, true);
        this.registerGalacticraftNonMobEntity(GCCoreEntityAstroOrb.class, "AstroOrb", GCCoreConfigManager.idEntityAstroOrb, 150, 5, true);
        this.registerGalacticraftNonMobEntity(GCCoreEntityParaChest.class, "ParaChest", GCCoreConfigManager.idEntityParaChest, 150, 5, true);
        this.registerGalacticraftNonMobEntity(GCCoreEntityOxygenBubble.class, "OxygenBubble", GCCoreConfigManager.idEntityOxygenBubble, 150, 20, false);
        this.registerGalacticraftNonMobEntity(GCCoreEntityLander.class, "Lander", GCCoreConfigManager.idEntityLander, 150, 5, true);
    }

    public void registerGalacticraftCreature(Class var0, String var1, int id, int back, int fore)
    {
        EntityRegistry.registerGlobalEntityID(var0, var1, id, back, fore);
        EntityRegistry.registerModEntity(var0, var1, id, GalacticraftCore.instance, 80, 3, true);
        LanguageRegistry.instance().addStringLocalization("entity." + var1 + ".name", "en_US", var1);
    }

    public void registerGalacticraftNonMobEntity(Class var0, String var1, int id, int trackingDistance, int updateFreq, boolean sendVel)
    {
        EntityRegistry.registerModEntity(var0, var1, id, this, trackingDistance, updateFreq, sendVel);
    }

    public static File minecraftDir;

    private boolean checkForCoremod()
    {
        if (GalacticraftCore.minecraftDir != null)
        {
            final File modsDir = new File(GalacticraftCore.minecraftDir, "coremods");

            if (!modsDir.exists())
            {
                return false;
            }

            for (final File file : modsDir.listFiles())
            {
                if (file.getName().endsWith(".jar") && file.getName().toLowerCase().contains("galacticraft"))
                {
                    return true;
                }
            }
        }

        return false;
    }
}
