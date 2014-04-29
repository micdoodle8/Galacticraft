package micdoodle8.mods.galacticraft.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.Galaxy;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.transmission.compatibility.NetworkConfigHandler;
import micdoodle8.mods.galacticraft.api.transmission.core.grid.ChunkPowerHandler;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.blocks.BlockFluidGC;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.client.gui.GuiHandler;
import micdoodle8.mods.galacticraft.core.command.CommandGCInv;
import micdoodle8.mods.galacticraft.core.command.CommandKeepDim;
import micdoodle8.mods.galacticraft.core.command.CommandPlanetTeleport;
import micdoodle8.mods.galacticraft.core.command.CommandSpaceStationAddOwner;
import micdoodle8.mods.galacticraft.core.command.CommandSpaceStationRemoveOwner;
import micdoodle8.mods.galacticraft.core.dimension.TeleportTypeMoon;
import micdoodle8.mods.galacticraft.core.dimension.TeleportTypeOrbit;
import micdoodle8.mods.galacticraft.core.dimension.TeleportTypeOverworld;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderOrbit;
import micdoodle8.mods.galacticraft.core.entities.EntityAlienVillager;
import micdoodle8.mods.galacticraft.core.entities.EntityBubble;
import micdoodle8.mods.galacticraft.core.entities.EntityBuggy;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedCreeper;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSkeleton;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSpider;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedZombie;
import micdoodle8.mods.galacticraft.core.entities.EntityFlag;
import micdoodle8.mods.galacticraft.core.entities.EntityLander;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteor;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteorChunk;
import micdoodle8.mods.galacticraft.core.entities.EntityParachest;
import micdoodle8.mods.galacticraft.core.entities.EntitySkeletonBoss;
import micdoodle8.mods.galacticraft.core.entities.EntityTier1Rocket;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityClientPlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.event.EventHandlerGC;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.items.ItemBlockGC;
import micdoodle8.mods.galacticraft.core.network.ConnectionEvents;
import micdoodle8.mods.galacticraft.core.network.PacketPipeline;
import micdoodle8.mods.galacticraft.core.proxy.CommonProxyCore;
import micdoodle8.mods.galacticraft.core.recipe.RecipeManagerGC;
import micdoodle8.mods.galacticraft.core.schematic.SchematicAdd;
import micdoodle8.mods.galacticraft.core.schematic.SchematicMoonBuggy;
import micdoodle8.mods.galacticraft.core.schematic.SchematicRocketT1;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAirLock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAirLockController;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAluminumWire;
import micdoodle8.mods.galacticraft.core.tile.TileEntityBeamReceiver;
import micdoodle8.mods.galacticraft.core.tile.TileEntityBeamReflector;
import micdoodle8.mods.galacticraft.core.tile.TileEntityBuggyFueler;
import micdoodle8.mods.galacticraft.core.tile.TileEntityBuggyFuelerSingle;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCargoLoader;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCargoUnloader;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCircuitFabricator;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCoalGenerator;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricFurnace;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricIngotCompressor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityEnergyStorageModule;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFallenMeteor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFuelLoader;
import micdoodle8.mods.galacticraft.core.tile.TileEntityIngotCompressor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPadSingle;
import micdoodle8.mods.galacticraft.core.tile.TileEntityMulti;
import micdoodle8.mods.galacticraft.core.tile.TileEntityNasaWorkbench;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenCollector;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenCompressor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenDecompressor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenDetector;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenDistributor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenPipe;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenStorageModule;
import micdoodle8.mods.galacticraft.core.tile.TileEntityParaChest;
import micdoodle8.mods.galacticraft.core.tile.TileEntityRefinery;
import micdoodle8.mods.galacticraft.core.tile.TileEntitySolar;
import micdoodle8.mods.galacticraft.core.tile.TileEntitySpaceStationBase;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.CreativeTabGC;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.ThreadRequirementMissing;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.world.ChunkLoadingCallback;
import micdoodle8.mods.galacticraft.core.world.gen.OverworldGenerator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
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
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * GalacticraftCore.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@Mod(name = GalacticraftCore.NAME, version = GalacticraftCore.LOCALMAJVERSION + "." + GalacticraftCore.LOCALMINVERSION + "." + GalacticraftCore.LOCALBUILDVERSION, useMetadata = true, modid = GalacticraftCore.MODID, dependencies = "required-after:Forge@[7.0,); required-after:FML@[5.0.5,); after:ICBM|Explosion; after:IC2; after:BuildCraft|Core; after:BuildCraft|Energy; after:IC2")
public class GalacticraftCore
{
	public static final String NAME = "Galacticraft Core";
	public static final String MODID = "GalacticraftCore";
	public static final String CHANNEL = "GalacticraftCore";
	public static final String CHANNELENTITIES = "GCCoreEntities";

	public static final int LOCALMAJVERSION = 2;
	public static final int LOCALMINVERSION = 0;
	public static final int LOCALBUILDVERSION = 12;
	public static int remoteMajVer;
	public static int remoteMinVer;
	public static int remoteBuildVer;

	@SidedProxy(clientSide = "micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore", serverSide = "micdoodle8.mods.galacticraft.core.proxy.CommonProxyCore")
	public static CommonProxyCore proxy;

	@Instance(GalacticraftCore.MODID)
	public static GalacticraftCore instance;

	public static PacketPipeline packetPipeline;
	
	private static ThreadRequirementMissing missingRequirementThread;

	public static Map<String, GCEntityClientPlayerMP> playersClient = new HashMap<String, GCEntityClientPlayerMP>();
	public static Map<String, GCEntityPlayerMP> playersServer = new HashMap<String, GCEntityPlayerMP>();

	public static CreativeTabs galacticraftTab;

	public static Galaxy galaxyBlockyWay;
	public static Planet planetOverworld;
	public static Moon moonMoon;

	public static final String FILE_PATH = "/micdoodle8/mods/galacticraft/core/";
	public static final String CLIENT_PATH = "client/";
	public static final String LANGUAGE_PATH = "/assets/galacticraftcore/lang/";
	public static final String BLOCK_TEXTURE_FILE = GalacticraftCore.FILE_PATH + GalacticraftCore.CLIENT_PATH + "blocks/core.png";
	public static final String ITEM_TEXTURE_FILE = GalacticraftCore.FILE_PATH + GalacticraftCore.CLIENT_PATH + "items/core.png";
	public static final String CONFIG_FILE = "Galacticraft/core.conf";
	public static final String POWER_CONFIG_FILE = "Galacticraft/power.conf";
	public static final String CHUNKLOADER_CONFIG_FILE = "Galacticraft/chunkloading.conf";

	public static String ASSET_DOMAIN = "galacticraftcore";
	public static String ASSET_PREFIX = GalacticraftCore.ASSET_DOMAIN + ":";

	public static boolean setSpaceStationRecipe = false;

	public static Fluid gcFluidOil;
	public static Fluid gcFluidFuel;
	public static Fluid fluidOil;
	public static Fluid fluidFuel;

	public static HashMap<String, ItemStack> itemList = new HashMap<String, ItemStack>();
	public static HashMap<String, ItemStack> blocksList = new HashMap<String, ItemStack>();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new EventHandlerGC());
		GalacticraftCore.proxy.preInit(event);

		ConfigManagerCore.setDefaultValues(new File(event.getModConfigurationDirectory(), GalacticraftCore.CONFIG_FILE));
		NetworkConfigHandler.setDefaultValues(new File(event.getModConfigurationDirectory(), GalacticraftCore.POWER_CONFIG_FILE));
		ChunkLoadingCallback.loadConfig(new File(event.getModConfigurationDirectory(), GalacticraftCore.CHUNKLOADER_CONFIG_FILE));

		GalacticraftCore.gcFluidOil = new Fluid("oil").setDensity(800).setViscosity(1500);
		GalacticraftCore.gcFluidFuel = new Fluid("fuel").setDensity(200).setViscosity(900);
		FluidRegistry.registerFluid(GalacticraftCore.gcFluidOil);
		FluidRegistry.registerFluid(GalacticraftCore.gcFluidFuel);
		GalacticraftCore.fluidOil = FluidRegistry.getFluid("oil");
		GalacticraftCore.fluidFuel = FluidRegistry.getFluid("fuel");

		if (GalacticraftCore.fluidOil.getBlock() == null)
		{
			GCBlocks.crudeOilStill = new BlockFluidGC(GalacticraftCore.fluidOil, "oil");
			((BlockFluidGC) GCBlocks.crudeOilStill).setQuantaPerBlock(3);
			GCBlocks.crudeOilStill.setBlockName("crudeOilStill");
			GameRegistry.registerBlock(GCBlocks.crudeOilStill, ItemBlockGC.class, GCBlocks.crudeOilStill.getUnlocalizedName(), GalacticraftCore.MODID);
			GalacticraftCore.fluidOil.setBlock(GCBlocks.crudeOilStill);
		}
		else
		{
			GCBlocks.crudeOilStill = GalacticraftCore.fluidOil.getBlock();
		}

		if (GalacticraftCore.fluidFuel.getBlock() == null)
		{
			GCBlocks.fuelStill = new BlockFluidGC(GalacticraftCore.fluidFuel, "fuel");
			((BlockFluidGC) GCBlocks.fuelStill).setQuantaPerBlock(6);
			GCBlocks.fuelStill.setBlockName("fuel");
			GameRegistry.registerBlock(GCBlocks.fuelStill, ItemBlockGC.class, GCBlocks.fuelStill.getUnlocalizedName(), GalacticraftCore.MODID);
			GalacticraftCore.fluidFuel.setBlock(GCBlocks.fuelStill);
		}
		else
		{
			GCBlocks.fuelStill = GalacticraftCore.fluidFuel.getBlock();
		}

		GCBlocks.initBlocks();
		GCItems.initItems();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		GalacticraftCore.galacticraftTab = new CreativeTabGC(CreativeTabs.getNextID(), GalacticraftCore.CHANNEL, GCItems.rocketTier1, 0);
		GalacticraftCore.proxy.init(event);

		GalacticraftCore.packetPipeline = new PacketPipeline();
		GalacticraftCore.packetPipeline.initalise();
		
		GalacticraftCore.galaxyBlockyWay = new Galaxy("blockyWay").setMapPosition(new Vector3(0.0F, 0.0F));
		GalacticraftCore.planetOverworld = (Planet) new Planet("overworld").setParentGalaxy(GalacticraftCore.galaxyBlockyWay).setRingColorRGB(0.1F, 0.9F, 0.6F).setPhaseShift(0.75F);
		GalacticraftCore.planetOverworld.setPlanetIcon(new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/planets/overworld.png"));
		GalacticraftCore.planetOverworld.setDimensionInfo(0, WorldProvider.class, false, true);
		GalacticraftCore.moonMoon = (Moon) new Moon("moon").setParentPlanet(GalacticraftCore.planetOverworld).setRelativeSize(0.2667F).setRelativeDistanceFromCenter(40F).setRelativeOrbitTime(0.01F);
		GalacticraftCore.moonMoon.setDimensionInfo(ConfigManagerCore.idDimensionMoon, WorldProviderMoon.class);
		GalacticraftCore.moonMoon.setPlanetIcon(new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/planets/moon.png"));

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
		NetworkConfigHandler.initGas();

		this.registerCreatures();
		this.registerOtherEntities();
		this.registerTileEntities();

		GalaxyRegistry.registerGalaxy(GalacticraftCore.galaxyBlockyWay);
		GalaxyRegistry.registerPlanet(GalacticraftCore.planetOverworld);
		GalaxyRegistry.registerMoon(GalacticraftCore.moonMoon);
		DimensionManager.registerProviderType(ConfigManagerCore.idDimensionOverworldOrbit, WorldProviderOrbit.class, false);
		DimensionManager.registerProviderType(ConfigManagerCore.idDimensionOverworldOrbitStatic, WorldProviderOrbit.class, true);
		GalacticraftRegistry.registerTeleportType(WorldProviderSurface.class, new TeleportTypeOverworld());
		GalacticraftRegistry.registerTeleportType(WorldProviderOrbit.class, new TeleportTypeOrbit());
		GalacticraftRegistry.registerTeleportType(WorldProviderMoon.class, new TeleportTypeMoon());
		GalacticraftRegistry.registerRocketGui(WorldProviderOrbit.class, new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/overworldRocketGui.png"));
		GalacticraftRegistry.registerRocketGui(WorldProviderSurface.class, new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/overworldRocketGui.png"));
		GalacticraftRegistry.registerRocketGui(WorldProviderMoon.class, new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/moonRocketGui.png"));
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
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		GalacticraftCore.proxy.postInit(event);
		GalacticraftCore.packetPipeline.postInitialise();
		
		ArrayList<CelestialBody> cBodyList = new ArrayList<CelestialBody>();
		cBodyList.addAll(GalaxyRegistry.getRegisteredPlanets().values());
		cBodyList.addAll(GalaxyRegistry.getRegisteredMoons().values());
		
		for (CelestialBody body : cBodyList)
		{
			if (body.shouldAutoRegister())
			{
				int id = Arrays.binarySearch(ConfigManagerCore.staticLoadDimensions, body.getDimensionID());
				DimensionManager.registerProviderType(body.getDimensionID(), body.getWorldProvider(), body.getForceStaticLoad() || id < 0);
			}
		}

		CompatibilityManager.checkForCompatibleMods();
		RecipeManagerGC.loadRecipes();
		NetworkRegistry.INSTANCE.registerGuiHandler(GalacticraftCore.instance, new GuiHandler());
	}

	@EventHandler
	public void serverInit(FMLServerStartedEvent event)
	{
		if (GalacticraftCore.missingRequirementThread == null)
		{
			GalacticraftCore.missingRequirementThread = new ThreadRequirementMissing(FMLCommonHandler.instance().getEffectiveSide());
			GalacticraftCore.missingRequirementThread.start();
		}

		GCCoreUtil.checkVersion(Side.SERVER);
		FMLCommonHandler.instance().bus().register(new TickHandlerServer());
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandSpaceStationAddOwner());
		event.registerServerCommand(new CommandSpaceStationRemoveOwner());
		event.registerServerCommand(new CommandPlanetTeleport());
		event.registerServerCommand(new CommandKeepDim());
		event.registerServerCommand(new CommandGCInv());

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
		GameRegistry.registerTileEntity(TileEntityBeamReflector.class, "Beam Reflector");
		GameRegistry.registerTileEntity(TileEntityBeamReceiver.class, "Beam Receiver");
	}

	public void registerCreatures()
	{
		GCCoreUtil.registerGalacticraftCreature(EntityEvolvedSpider.class, "EvolvedSpider", ConfigManagerCore.idEntityEvolvedSpider, 3419431, 11013646);
		GCCoreUtil.registerGalacticraftCreature(EntityEvolvedZombie.class, "EvolvedZombie", ConfigManagerCore.idEntityEvolvedZombie, 44975, 7969893);
		GCCoreUtil.registerGalacticraftCreature(EntityEvolvedCreeper.class, "EvolvedCreeper", ConfigManagerCore.idEntityEvolvedCreeper, 894731, 0);
		GCCoreUtil.registerGalacticraftCreature(EntityEvolvedSkeleton.class, "EvolvedSkeleton", ConfigManagerCore.idEntityEvolvedSkeleton, 12698049, 4802889);
		GCCoreUtil.registerGalacticraftCreature(EntitySkeletonBoss.class, "EvolvedSkeletonBoss", ConfigManagerCore.idEntityEvolvedSkeletonBoss, 12698049, 4802889);
		GCCoreUtil.registerGalacticraftCreature(EntityAlienVillager.class, "AlienVillager", ConfigManagerCore.idEntityAlienVillager, GCCoreUtil.convertTo32BitColor(255, 103, 181, 145), 12422002);
	}

	public void registerOtherEntities()
	{
		GCCoreUtil.registerGalacticraftNonMobEntity(EntityTier1Rocket.class, "Spaceship", ConfigManagerCore.idEntitySpaceship, 150, 1, false);
		GCCoreUtil.registerGalacticraftNonMobEntity(EntityArrow.class, "GravityArrow", ConfigManagerCore.idEntityAntiGravityArrow, 150, 5, true);
		GCCoreUtil.registerGalacticraftNonMobEntity(EntityMeteor.class, "Meteor", ConfigManagerCore.idEntityMeteor, 150, 5, true);
		GCCoreUtil.registerGalacticraftNonMobEntity(EntityBuggy.class, "Buggy", ConfigManagerCore.idEntityBuggy, 150, 5, true);
		GCCoreUtil.registerGalacticraftNonMobEntity(EntityFlag.class, "Flag", ConfigManagerCore.idEntityFlag, 150, 5, true);
		GCCoreUtil.registerGalacticraftNonMobEntity(EntityParachest.class, "ParaChest", ConfigManagerCore.idEntityParaChest, 150, 5, true);
		GCCoreUtil.registerGalacticraftNonMobEntity(EntityBubble.class, "OxygenBubble", ConfigManagerCore.idEntityOxygenBubble, 150, 20, false);
		GCCoreUtil.registerGalacticraftNonMobEntity(EntityLander.class, "Lander", ConfigManagerCore.idEntityLander, 150, 5, false);
		GCCoreUtil.registerGalacticraftNonMobEntity(EntityMeteorChunk.class, "MeteorChunk", ConfigManagerCore.idEntityMeteorChunk, 150, 5, true);
	}
}
