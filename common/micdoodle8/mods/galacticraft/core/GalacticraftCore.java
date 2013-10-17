package micdoodle8.mods.galacticraft.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mekanism.api.transmitters.TransmitterNetworkRegistry;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.CircuitFabricatorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.world.ICelestialBody;
import micdoodle8.mods.galacticraft.api.world.IGalaxy;
import micdoodle8.mods.galacticraft.api.world.IMoon;
import micdoodle8.mods.galacticraft.api.world.IPlanet;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockCrudeOil;
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
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityMeteorChunk;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityOxygenBubble;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityParaChest;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityRocketT1;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeleton;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeletonBoss;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpider;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityZombie;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.event.GCCoreEvents;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBasic;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBlock;
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
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAluminumWire;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityBuggyFueler;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityBuggyFuelerSingle;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCargoLoader;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCargoPad;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCargoPadSingle;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCargoUnloader;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCircuitFabricator;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCoalGenerator;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityElectricFurnace;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityElectricIngotCompressor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityEnergyStorageModule;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityFallenMeteor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityFuelLoader;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityIngotCompressor;
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
import micdoodle8.mods.galacticraft.core.world.ChunkLoadingCallback;
import micdoodle8.mods.galacticraft.core.world.gen.GCCoreOverworldGenerator;
import micdoodle8.mods.galacticraft.moon.GalacticraftMoon;
import micdoodle8.mods.galacticraft.moon.dimension.GCMoonWorldProvider;
import micdoodle8.mods.galacticraft.moon.items.GCMoonItems;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import universalelectricity.compatibility.Compatibility;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.ConductorChunkInitiate;
import cpw.mods.fml.common.FMLCommonHandler;
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
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
@Mod(name = GalacticraftCore.NAME, version = GalacticraftCore.LOCALMAJVERSION + "." + GalacticraftCore.LOCALMINVERSION + "." + GalacticraftCore.LOCALBUILDVERSION, useMetadata = true, modid = GalacticraftCore.MODID, dependencies = "after:ICBM|Explosion; after:IC2; after:BuildCraft|Core")
@NetworkMod(channels = { GalacticraftCore.CHANNEL }, clientSideRequired = true, serverSideRequired = false, connectionHandler = GCCoreConnectionHandler.class, packetHandler = GCCorePacketManager.class)
public class GalacticraftCore
{
    public static final String NAME = "Galacticraft Core";
    public static final String MODID = "GalacticraftCore";
    public static final String CHANNEL = "GalacticraftCore";
    public static final String CHANNELENTITIES = "GCCoreEntities";

    public static final int LOCALMAJVERSION = 2;
    public static final int LOCALMINVERSION = 0;
    public static final int LOCALBUILDVERSION = 2;
    public static int remoteMajVer;
    public static int remoteMinVer;
    public static int remoteBuildVer;

    @SidedProxy(clientSide = "micdoodle8.mods.galacticraft.core.client.ClientProxyCore", serverSide = "micdoodle8.mods.galacticraft.core.CommonProxyCore")
    public static CommonProxyCore proxy;

    @Instance(GalacticraftCore.MODID)
    public static GalacticraftCore instance;

    private static GCCoreThreadRequirementMissing missingRequirementThread;

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

    public static String TEXTURE_DOMAIN = "galacticraftcore";
    public static String TEXTURE_PREFIX = GalacticraftCore.TEXTURE_DOMAIN + ":";

    public static boolean setSpaceStationRecipe = false;

    public static GCCorePlanetOverworld overworld;
    public static GCCorePlanetSun sun;

    public static Fluid gcFluidOil;
    public static Fluid gcFluidFuel;
    public static Fluid fluidOil;
    public static Fluid fluidFuel;

    public static HashMap<String, ItemStack> itemList = new HashMap<String, ItemStack>();
    public static HashMap<String, ItemStack> blocksList = new HashMap<String, ItemStack>();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        GalacticraftMoon.preLoad(event);
        MinecraftForge.EVENT_BUS.register(new GCCoreEvents());
        GalacticraftCore.proxy.preInit(event);

        GCCoreConfigManager.setDefaultValues(new File(event.getModConfigurationDirectory(), GalacticraftCore.CONFIG_FILE));

        GalacticraftCore.gcFluidOil = new Fluid("oil").setDensity(800).setViscosity(1500);
        GalacticraftCore.gcFluidFuel = new Fluid("fuel").setViscosity(800);
        FluidRegistry.registerFluid(GalacticraftCore.gcFluidOil);
        FluidRegistry.registerFluid(GalacticraftCore.gcFluidFuel);
        GalacticraftCore.fluidOil = FluidRegistry.getFluid("oil");
        GalacticraftCore.fluidFuel = FluidRegistry.getFluid("fuel");

        if (GalacticraftCore.fluidOil.getBlockID() == -1)
        {
            GCCoreBlocks.crudeOilStill = new GCCoreBlockCrudeOil(GCCoreConfigManager.idBlockCrudeOilStill, GalacticraftCore.fluidOil, "crudeOilStill");
            GameRegistry.registerBlock(GCCoreBlocks.crudeOilStill, GCCoreItemBlock.class, GCCoreBlocks.crudeOilStill.getUnlocalizedName(), GalacticraftCore.MODID);
            GalacticraftCore.fluidOil.setBlockID(GCCoreBlocks.crudeOilStill);
        }
        else
        {
            GCCoreBlocks.crudeOilStill = Block.blocksList[GalacticraftCore.fluidOil.getBlockID()];
        }

        GCCoreBlocks.initBlocks();
        GCCoreItems.initItems();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        GalacticraftCore.galacticraftTab = new GCCoreCreativeTab(CreativeTabs.getNextID(), GalacticraftCore.CHANNEL, GCCoreItems.rocketTier1.itemID, 0);
        GalacticraftCore.overworld = new GCCorePlanetOverworld();
        GalacticraftRegistry.registerCelestialBody(GalacticraftCore.overworld);
        GalacticraftCore.sun = new GCCorePlanetSun();
        GalacticraftRegistry.registerCelestialBody(GalacticraftCore.sun);
        GalacticraftRegistry.registerGalaxy(GalacticraftCore.galaxyMilkyWay);
        DimensionManager.registerProviderType(GCCoreConfigManager.idDimensionOverworldOrbit, GCCoreWorldProviderSpaceStation.class, false);
        GalacticraftCore.proxy.init(event);
        GalacticraftRegistry.registerTeleportType(WorldProviderSurface.class, new GCCoreOverworldTeleportType());
        GalacticraftRegistry.registerTeleportType(GCCoreWorldProviderSpaceStation.class, new GCCoreOrbitTeleportType());

        ForgeChunkManager.setForcedChunkLoadingCallback(GalacticraftCore.instance, new ChunkLoadingCallback());

        GalacticraftMoon.load(event);

        for (int i = GCCoreItems.fuelCanister.getMaxDamage() - 1; i > 0; i--)
        {
            FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(GalacticraftCore.fluidFuel, GCCoreItems.fuelCanister.getMaxDamage() - i), new ItemStack(GCCoreItems.fuelCanister, 1, i), new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.fuelCanister.getMaxDamage())));
        }

        for (int i = GCCoreItems.oilCanister.getMaxDamage() - 1; i > 0; i--)
        {
            FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(GalacticraftCore.fluidOil, GCCoreItems.oilCanister.getMaxDamage() - i), new ItemStack(GCCoreItems.oilCanister, 1, i), new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.fuelCanister.getMaxDamage())));
        }

        SchematicRegistry.registerSchematicRecipe(new GCCoreSchematicRocketT1());
        SchematicRegistry.registerSchematicRecipe(new GCCoreSchematicMoonBuggy());
        SchematicRegistry.registerSchematicRecipe(new GCCoreSchematicAdd());
        ConductorChunkInitiate.register();
        Compatibility.initiate();
        TransmitterNetworkRegistry.initiate();
        this.registerCreatures();
        this.registerOtherEntities();
        this.initiateUENetwork();
        NetworkRegistry.instance().registerChannel(new GCCorePacketManager(), GalacticraftCore.CHANNELENTITIES, Side.CLIENT);
        GalacticraftRegistry.registerRocketGui(GCCoreWorldProviderSpaceStation.class, new ResourceLocation(GalacticraftCore.TEXTURE_DOMAIN, "textures/gui/overworldRocketGui.png"));
        GalacticraftRegistry.registerRocketGui(WorldProviderSurface.class, new ResourceLocation(GalacticraftCore.TEXTURE_DOMAIN, "textures/gui/overworldRocketGui.png"));
        GalacticraftRegistry.registerRocketGui(GCMoonWorldProvider.class, new ResourceLocation(GalacticraftCore.TEXTURE_DOMAIN, "textures/gui/moonRocketGui.png"));
        GalacticraftRegistry.addDungeonLoot(1, new ItemStack(GCCoreItems.schematic, 1, 0));
        GalacticraftRegistry.addDungeonLoot(1, new ItemStack(GCCoreItems.schematic, 1, 1));

        if (GCCoreConfigManager.enableCopperOreGen)
        {
            GameRegistry.registerWorldGenerator(new GCCoreOverworldGenerator(GCCoreBlocks.decorationBlocks, 5, 24, 0, 75, 7));
        }
        
        if (GCCoreConfigManager.enableTinOreGen)
        {
            GameRegistry.registerWorldGenerator(new GCCoreOverworldGenerator(GCCoreBlocks.decorationBlocks, 6, 22, 0, 60, 7));
        }
        
        if (GCCoreConfigManager.enableAluminumOreGen)
        {
            GameRegistry.registerWorldGenerator(new GCCoreOverworldGenerator(GCCoreBlocks.decorationBlocks, 7, 18, 0, 45, 7));
        }
        
        if (GCCoreConfigManager.enableSiliconOreGen)
        {
            GameRegistry.registerWorldGenerator(new GCCoreOverworldGenerator(GCCoreBlocks.decorationBlocks, 8, 3, 0, 25, 7));
        }
    }

    @SuppressWarnings("deprecation")
    private void initiateUENetwork()
    {
        UniversalElectricity.isNetworkActive = true;
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        GalacticraftMoon.postLoad(event);

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
        NetworkRegistry.instance().registerGuiHandler(this, GalacticraftCore.proxy);
        GalacticraftCore.proxy.postInit(event);
        GalacticraftCore.proxy.registerRenderInformation();

        for (int i = 3; i < 8; i++)
        {
            if (GCCoreItemBasic.names[i].contains("ingot"))
            {
                for (ItemStack stack : OreDictionary.getOres(GCCoreItemBasic.names[i]))
                {
                    CompressorRecipes.addShapelessRecipe(new ItemStack(GCCoreItems.basicItem, 1, i + 3), stack, stack);
                }
            }
        }

        if (OreDictionary.getOres("ingotBronze").size() > 0)
        {
            for (ItemStack stack : OreDictionary.getOres("ingotBronze"))
            {
                CompressorRecipes.addShapelessRecipe(new ItemStack(GCCoreItems.basicItem, 1, 10), stack, stack);
            }
        }

        if (OreDictionary.getOres("ingotSteel").size() > 0)
        {
            for (ItemStack stack : OreDictionary.getOres("ingotSteel"))
            {
                CompressorRecipes.addShapelessRecipe(new ItemStack(GCCoreItems.basicItem, 1, 9), stack, stack);
            }
        }

        CompressorRecipes.addShapelessRecipe(new ItemStack(GCCoreItems.basicItem, 1, 9), Item.coal, new ItemStack(GCCoreItems.basicItem, 1, 11), Item.coal);
        CompressorRecipes.addShapelessRecipe(new ItemStack(GCCoreItems.basicItem, 1, 10), new ItemStack(GCCoreItems.basicItem, 1, 6), new ItemStack(GCCoreItems.basicItem, 1, 7));

        CompressorRecipes.addShapelessRecipe(new ItemStack(GCCoreItems.basicItem, 1, 11), Item.ingotIron, Item.ingotIron);
        CompressorRecipes.addShapelessRecipe(new ItemStack(GCMoonItems.meteoricIronIngot, 1, 1), new ItemStack(GCMoonItems.meteoricIronIngot, 1, 0));

        CompressorRecipes.addRecipe(new ItemStack(GCCoreItems.heavyPlatingTier1, 1, 0), "XYZ", "XYZ", 'X', new ItemStack(GCCoreItems.basicItem, 1, 9), 'Y', new ItemStack(GCCoreItems.basicItem, 1, 8), 'Z', new ItemStack(GCCoreItems.basicItem, 1, 10));

        CircuitFabricatorRecipes.addRecipe(new ItemStack(GCCoreItems.basicItem, 9, 12), new ItemStack[] { new ItemStack(Item.diamond), new ItemStack(GCCoreItems.basicItem, 1, 2), new ItemStack(GCCoreItems.basicItem, 1, 2), new ItemStack(Item.redstone), new ItemStack(Item.dyePowder, 1, 4) });

        CircuitFabricatorRecipes.addRecipe(new ItemStack(GCCoreItems.basicItem, 3, 13), new ItemStack[] { new ItemStack(Item.diamond), new ItemStack(GCCoreItems.basicItem, 1, 2), new ItemStack(GCCoreItems.basicItem, 1, 2), new ItemStack(Item.redstone), new ItemStack(Block.torchRedstoneActive) });

        CircuitFabricatorRecipes.addRecipe(new ItemStack(GCCoreItems.basicItem, 1, 14), new ItemStack[] { new ItemStack(Item.diamond), new ItemStack(GCCoreItems.basicItem, 1, 2), new ItemStack(GCCoreItems.basicItem, 1, 2), new ItemStack(Item.redstone), new ItemStack(Item.redstoneRepeater) });
    }

    @EventHandler
    public void serverInit(FMLServerStartedEvent event)
    {
        GalacticraftMoon.serverInit(event);

        if (GalacticraftCore.missingRequirementThread == null)
        {
            GalacticraftCore.missingRequirementThread = new GCCoreThreadRequirementMissing(FMLCommonHandler.instance().getEffectiveSide());
            GalacticraftCore.missingRequirementThread.start();
        }

        GCCoreUtil.checkVersion(Side.SERVER);
        TickRegistry.registerTickHandler(new GCCoreTickHandlerServer(), Side.SERVER);
        NetworkRegistry.instance().registerChannel(new GCCorePacketHandlerServer(), GalacticraftCore.CHANNEL, Side.SERVER);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        GalacticraftMoon.serverStarting(event);
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
        GameRegistry.registerTileEntity(GCCoreTileEntityEnergyStorageModule.class, "Energy Storage Module");
        GameRegistry.registerTileEntity(GCCoreTileEntityCoalGenerator.class, "Galacticraft Coal Generator");
        GameRegistry.registerTileEntity(GCCoreTileEntityElectricFurnace.class, "Galacticraft Electric Furnace");
        GameRegistry.registerTileEntity(GCCoreTileEntityAluminumWire.class, "Galacticraft Aluminum Wire");
        GameRegistry.registerTileEntity(GCCoreTileEntityFallenMeteor.class, "Fallen Meteor");
        GameRegistry.registerTileEntity(GCCoreTileEntityIngotCompressor.class, "Ingot Compressor");
        GameRegistry.registerTileEntity(GCCoreTileEntityElectricIngotCompressor.class, "Electric Ingot Compressor");
        GameRegistry.registerTileEntity(GCCoreTileEntityCircuitFabricator.class, "Circuit Fabricator");
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
        GCCoreUtil.registerGalacticraftNonMobEntity(GCCoreEntityMeteorChunk.class, "MeteorChunk", GCCoreConfigManager.idEntityMeteorChunk, 150, 5, true);
    }

    public static class MinecraftLoadedEvent extends Event
    {
    }

    public static class SleepCancelledEvent extends Event
    {
    }

    public static class OrientCameraEvent extends Event
    {
    }
}
