package micdoodle8.mods.galacticraft.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.Galaxy;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.recipe.CircuitFabricatorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.CompressorRecipes;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.transmission.compatibility.NetworkConfigHandler;
import micdoodle8.mods.galacticraft.api.transmission.core.grid.ChunkPowerHandler;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.blocks.BlockFluid;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.client.gui.GuiHandler;
import micdoodle8.mods.galacticraft.core.command.CommandPlanetTeleport;
import micdoodle8.mods.galacticraft.core.command.CommandSpaceStationAddOwner;
import micdoodle8.mods.galacticraft.core.command.CommandSpaceStationRemoveOwner;
import micdoodle8.mods.galacticraft.core.dimension.TeleportTypeOverworld;
import micdoodle8.mods.galacticraft.core.dimension.TeleportTypeSpaceStation;
import micdoodle8.mods.galacticraft.core.dimension.WorldDataSpaceRaces;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderSpaceStation;
import micdoodle8.mods.galacticraft.core.entities.EntityAlienVillager;
import micdoodle8.mods.galacticraft.core.entities.EntityArrowGC;
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
import micdoodle8.mods.galacticraft.core.event.GCEvents;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.items.ItemBasic;
import micdoodle8.mods.galacticraft.core.items.ItemBlockGC;
import micdoodle8.mods.galacticraft.core.network.PacketPipeline;
import micdoodle8.mods.galacticraft.core.proxy.CommonProxy;
import micdoodle8.mods.galacticraft.core.recipe.RecipeManager;
import micdoodle8.mods.galacticraft.core.schematic.SchematicPageAddNew;
import micdoodle8.mods.galacticraft.core.schematic.SchematicPageMoonBuggy;
import micdoodle8.mods.galacticraft.core.schematic.SchematicPageTier1Rocket;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAdvancedCraftingTable;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAirLock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAirLockController;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAluminumWire;
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
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenCollector;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenCompressor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenDecompressor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenDetector;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenDistributor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenPipe;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenStorageModule;
import micdoodle8.mods.galacticraft.core.tile.TileEntityParachest;
import micdoodle8.mods.galacticraft.core.tile.TileEntityRefinery;
import micdoodle8.mods.galacticraft.core.tile.TileEntitySolar;
import micdoodle8.mods.galacticraft.core.tile.TileEntitySpaceStationBase;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.CoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCConfigManager;
import micdoodle8.mods.galacticraft.core.util.GCCreativeTab;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.world.ChunkLoadingCallback;
import micdoodle8.mods.galacticraft.core.world.gen.WorldGenOverworld;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = GalacticraftCore.MOD_ID, name = GalacticraftCore.MOD_NAME, dependencies = "required-after:Forge@[7.0,);required-after:FML@[5.0.5,)")
public class GalacticraftCore
{
	public static final String MOD_ID = "GalacticraftCore";
	public static final String MOD_NAME = "Galacticraft Core";
	public static final String ASSET_DOMAIN = "galacticraftcore";
	public static final String ASSET_PREFIX = GalacticraftCore.ASSET_DOMAIN + ":";
	public static final String CONFIG_FILE = "Galacticraft/core.conf";
	public static final String POWER_CONFIG_FILE = "Galacticraft/power.conf";
	public static final String CHUNKLOADER_CONFIG_FILE = "Galacticraft/chunkloading.conf";

	@SidedProxy(clientSide = "micdoodle8.mods.galacticraft.core.proxy.ClientProxy", serverSide = "micdoodle8.mods.galacticraft.core.proxy.CommonProxy")
	public static CommonProxy proxy;
	@Instance(GalacticraftCore.MOD_ID)
	public static GalacticraftCore instance;
	public static PacketPipeline packetPipeline;

	public static Map<String, GCEntityClientPlayerMP> playersClient = new HashMap<String, GCEntityClientPlayerMP>();
	public static Map<String, GCEntityPlayerMP> playersServer = new HashMap<String, GCEntityPlayerMP>();

	public static HashMap<String, ItemStack> itemList = new HashMap<String, ItemStack>();
	public static HashMap<String, ItemStack> blocksList = new HashMap<String, ItemStack>();

	public static CreativeTabs galacticraftTab;

	public static Fluid gcFluidOil;
	public static Fluid gcFluidFuel;
	public static Fluid fluidOil;
	public static Fluid fluidFuel;

	public static Galaxy galaxyBlockyWay;
	public static Planet planetOverworld;
	public static Planet planetMars;
	public static Moon moonMoon;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		GalacticraftCore.proxy.preInit(event);
		MinecraftForge.EVENT_BUS.register(new GCEvents());

		GCConfigManager.setDefaultValues(new File(event.getModConfigurationDirectory(), GalacticraftCore.CONFIG_FILE));
		NetworkConfigHandler.setDefaultValues(new File(event.getModConfigurationDirectory(), GalacticraftCore.POWER_CONFIG_FILE));
		ChunkLoadingCallback.loadConfig(new File(event.getModConfigurationDirectory(), GalacticraftCore.CHUNKLOADER_CONFIG_FILE));

		GalacticraftCore.galaxyBlockyWay = new Galaxy("blockyWay").setMapPosition(new Vector3(0.0F, 0.0F));
		GalacticraftCore.planetOverworld = (Planet) new Planet("overworld").setParentGalaxy(GalacticraftCore.galaxyBlockyWay).setRingColorRGB(0.1F, 0.9F, 0.6F).setPhaseShift(0.75F);
		GalacticraftCore.planetOverworld.setPlanetIcon(new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/planets/overworld.png"));
		GalacticraftCore.planetOverworld.setDimensionInfo(0, WorldProvider.class, false, true);
		GalacticraftCore.moonMoon = (Moon) new Moon("moon").setParentPlanet(GalacticraftCore.planetOverworld).setRelativeSize(0.2667F).setRelativeDistanceFromCenter(40F).setRelativeOrbitTime(0.01F);
		GalacticraftCore.moonMoon.setDimensionInfo(GCConfigManager.dimensionIDMoon, WorldProviderMoon.class);
		GalacticraftCore.moonMoon.setPlanetIcon(new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/planets/moon.png"));
		GalaxyRegistry.registerGalaxy(GalacticraftCore.galaxyBlockyWay);
		GalaxyRegistry.registerPlanet(GalacticraftCore.planetOverworld);
		GalaxyRegistry.registerMoon(GalacticraftCore.moonMoon);

		GalacticraftCore.gcFluidOil = new Fluid("oil").setDensity(800).setViscosity(1500);
		GalacticraftCore.gcFluidFuel = new Fluid("fuel").setDensity(200).setViscosity(900);
		FluidRegistry.registerFluid(GalacticraftCore.gcFluidOil);
		FluidRegistry.registerFluid(GalacticraftCore.gcFluidFuel);
		GalacticraftCore.fluidOil = FluidRegistry.getFluid("oil");
		GalacticraftCore.fluidFuel = FluidRegistry.getFluid("fuel");

		if (GalacticraftCore.fluidOil.getBlock() == null)
		{
			GCBlocks.crudeOilStill = new BlockFluid(GalacticraftCore.fluidOil, "oil");
			((BlockFluid) GCBlocks.crudeOilStill).setQuantaPerBlock(3);
			GCBlocks.crudeOilStill.setBlockName("crudeOilStill");
			GameRegistry.registerBlock(GCBlocks.crudeOilStill, ItemBlockGC.class, GCBlocks.crudeOilStill.getUnlocalizedName(), GalacticraftCore.MOD_ID);
			GalacticraftCore.fluidOil.setBlock(GCBlocks.crudeOilStill);
		}
		else
		{
			GCBlocks.crudeOilStill = GalacticraftCore.fluidOil.getBlock();
		}

		if (GalacticraftCore.fluidFuel.getBlock() == null)
		{
			GCBlocks.fuelStill = new BlockFluid(GalacticraftCore.fluidFuel, "fuel");
			((BlockFluid) GCBlocks.fuelStill).setQuantaPerBlock(6);
			GCBlocks.fuelStill.setBlockName("fuel");
			GameRegistry.registerBlock(GCBlocks.fuelStill, ItemBlockGC.class, GCBlocks.fuelStill.getUnlocalizedName(), GalacticraftCore.MOD_ID);
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
		GalacticraftCore.proxy.init(event);
		GalacticraftCore.galacticraftTab = new GCCreativeTab(CreativeTabs.getNextID(), GalacticraftCore.MOD_NAME, GCBlocks.airLockFrame, 0);
		DimensionManager.registerProviderType(GCConfigManager.idDimensionOverworldOrbit, WorldProviderSpaceStation.class, false);
		GalacticraftRegistry.registerTeleportType(WorldProviderSurface.class, new TeleportTypeOverworld());
		GalacticraftRegistry.registerTeleportType(WorldProviderSpaceStation.class, new TeleportTypeSpaceStation());

		GalacticraftCore.packetPipeline = new PacketPipeline();
		GalacticraftCore.packetPipeline.initalise();
		GalacticraftCore.packetPipeline.postInitialise();

		ForgeChunkManager.setForcedChunkLoadingCallback(GalacticraftCore.instance, new ChunkLoadingCallback());

		FMLCommonHandler.instance().bus().register(new TickHandlerServer());

		NetworkRegistry.INSTANCE.registerGuiHandler(GalacticraftCore.instance, new GuiHandler());

		for (int i = GCItems.fuelCanister.getMaxDamage() - 1; i > 0; i--)
		{
			FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(GalacticraftCore.fluidFuel, GCItems.fuelCanister.getMaxDamage() - i), new ItemStack(GCItems.fuelCanister, 1, i), new ItemStack(GCItems.oilCanister, 1, GCItems.fuelCanister.getMaxDamage())));
		}

		for (int i = GCItems.oilCanister.getMaxDamage() - 1; i > 0; i--)
		{
			FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(GalacticraftCore.fluidOil, GCItems.oilCanister.getMaxDamage() - i), new ItemStack(GCItems.oilCanister, 1, i), new ItemStack(GCItems.oilCanister, 1, GCItems.fuelCanister.getMaxDamage())));
		}

		SchematicRegistry.registerSchematicRecipe(new SchematicPageTier1Rocket());
		SchematicRegistry.registerSchematicRecipe(new SchematicPageMoonBuggy());
		SchematicRegistry.registerSchematicRecipe(new SchematicPageAddNew());
		ChunkPowerHandler.initiate();
		NetworkConfigHandler.initGas();

		this.registerCreatures();
		this.registerOtherEntities();
		GalacticraftRegistry.registerRocketGui(WorldProviderSpaceStation.class, new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/overworldRocketGui.png"));
		GalacticraftRegistry.registerRocketGui(WorldProviderSurface.class, new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/overworldRocketGui.png"));
		GalacticraftRegistry.registerRocketGui(WorldProviderMoon.class, new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/moonRocketGui.png"));
		GalacticraftRegistry.addDungeonLoot(1, new ItemStack(GCItems.schematic, 1, 0));
		GalacticraftRegistry.addDungeonLoot(1, new ItemStack(GCItems.schematic, 1, 1));

		if (GCConfigManager.enableCopperOreGen)
		{
			GameRegistry.registerWorldGenerator(new WorldGenOverworld(GCBlocks.basicBlock, 5, 24, 0, 75, 7), 5);
		}

		if (GCConfigManager.enableTinOreGen)
		{
			GameRegistry.registerWorldGenerator(new WorldGenOverworld(GCBlocks.basicBlock, 6, 22, 0, 60, 7), 5);
		}

		if (GCConfigManager.enableAluminumOreGen)
		{
			GameRegistry.registerWorldGenerator(new WorldGenOverworld(GCBlocks.basicBlock, 7, 18, 0, 45, 7), 5);
		}

		if (GCConfigManager.enableSiliconOreGen)
		{
			GameRegistry.registerWorldGenerator(new WorldGenOverworld(GCBlocks.basicBlock, 8, 3, 0, 25, 7), 5);
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		GalacticraftCore.proxy.postInit(event);

		ArrayList<CelestialBody> cBodyList = new ArrayList<CelestialBody>();
		cBodyList.addAll(GalaxyRegistry.getRegisteredPlanets().values());
		cBodyList.addAll(GalaxyRegistry.getRegisteredMoons().values());

		for (CelestialBody body : cBodyList)
		{
			if (body.shouldAutoRegister())
			{
				DimensionManager.registerProviderType(body.getDimensionID(), body.getWorldProvider(), false);
			}
		}

		CompatibilityManager.checkForCompatibleMods();
		this.registerTileEntities();
		RecipeManager.loadRecipes();

		for (int i = 3; i < 8; i++)
		{
			if (ItemBasic.names[i].contains("ingot"))
			{
				for (ItemStack stack : OreDictionary.getOres(ItemBasic.names[i]))
				{
					CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, i + 3), stack, stack);
				}
			}
		}

		// Support for the other spelling of Aluminum
		if (OreDictionary.getOres("ingotAluminium").size() > 0 || OreDictionary.getOres("ingotNaturalAluminum").size() > 0)
		{
			List<ItemStack> aluminumIngotList = new ArrayList<ItemStack>();
			aluminumIngotList.addAll(OreDictionary.getOres("ingotAluminium"));
			aluminumIngotList.addAll(OreDictionary.getOres("ingotNaturalAluminum"));

			for (ItemStack stack : aluminumIngotList)
			{
				CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 8), stack, stack);
			}
		}

		if (OreDictionary.getOres("ingotBronze").size() > 0)
		{
			for (ItemStack stack : OreDictionary.getOres("ingotBronze"))
			{
				CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 10), stack, stack);
			}
		}

		if (OreDictionary.getOres("ingotSteel").size() > 0)
		{
			for (ItemStack stack : OreDictionary.getOres("ingotSteel"))
			{
				CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 9), stack, stack);
			}
		}

		CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 9), Items.coal, new ItemStack(GCItems.basicItem, 1, 11), Items.coal);
		CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 10), new ItemStack(GCItems.basicItem, 1, 6), new ItemStack(GCItems.basicItem, 1, 7));

		CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.basicItem, 1, 11), Items.iron_ingot, Items.iron_ingot);
		CompressorRecipes.addShapelessRecipe(new ItemStack(GCItems.meteoricIronIngot, 1, 1), new ItemStack(GCItems.meteoricIronIngot, 1, 0));

		CompressorRecipes.addRecipe(new ItemStack(GCItems.heavyPlatingTier1, 1, 0), "XYZ", "XYZ", 'X', new ItemStack(GCItems.basicItem, 1, 9), 'Y', new ItemStack(GCItems.basicItem, 1, 8), 'Z', new ItemStack(GCItems.basicItem, 1, 10));

		CircuitFabricatorRecipes.addRecipe(new ItemStack(GCItems.basicItem, 9, 12), new ItemStack[] { new ItemStack(Items.diamond), new ItemStack(GCItems.basicItem, 1, 2), new ItemStack(GCItems.basicItem, 1, 2), new ItemStack(Items.redstone), new ItemStack(Items.dye, 1, 4) });

		CircuitFabricatorRecipes.addRecipe(new ItemStack(GCItems.basicItem, 3, 13), new ItemStack[] { new ItemStack(Items.diamond), new ItemStack(GCItems.basicItem, 1, 2), new ItemStack(GCItems.basicItem, 1, 2), new ItemStack(Items.redstone), new ItemStack(Blocks.redstone_torch) });

		CircuitFabricatorRecipes.addRecipe(new ItemStack(GCItems.basicItem, 1, 14), new ItemStack[] { new ItemStack(Items.diamond), new ItemStack(GCItems.basicItem, 1, 2), new ItemStack(GCItems.basicItem, 1, 2), new ItemStack(Items.redstone), new ItemStack(Items.repeater) });
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandSpaceStationAddOwner());
		event.registerServerCommand(new CommandSpaceStationRemoveOwner());
		event.registerServerCommand(new CommandPlanetTeleport());

		WorldUtil.registerSpaceStations(event.getServer().worldServerForDimension(0).getSaveHandler().getMapFileFromName("dummy").getParentFile());

		ArrayList<CelestialBody> cBodyList = new ArrayList<CelestialBody>();
		cBodyList.addAll(GalaxyRegistry.getRegisteredPlanets().values());
		cBodyList.addAll(GalaxyRegistry.getRegisteredMoons().values());

		for (CelestialBody body : cBodyList)
		{
			if (body.shouldAutoRegister())
			{
				WorldUtil.registerPlanet(body.getDimensionID(), true);
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
		GameRegistry.registerTileEntity(TileEntityAdvancedCraftingTable.class, "NASA Workbench");
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
		GameRegistry.registerTileEntity(TileEntityParachest.class, "Parachest Tile");
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
	}

	public void registerCreatures()
	{
		CoreUtil.registerGalacticraftCreature(EntityEvolvedSpider.class, "EvolvedSpider", GCConfigManager.idEntityEvolvedSpider, 3419431, 11013646);
		CoreUtil.registerGalacticraftCreature(EntityEvolvedZombie.class, "EvolvedZombie", GCConfigManager.idEntityEvolvedZombie, 44975, 7969893);
		CoreUtil.registerGalacticraftCreature(EntityEvolvedCreeper.class, "EvolvedCreeper", GCConfigManager.idEntityEvolvedCreeper, 894731, 0);
		CoreUtil.registerGalacticraftCreature(EntityEvolvedSkeleton.class, "EvolvedSkeleton", GCConfigManager.idEntityEvolvedSkeleton, 12698049, 4802889);
		CoreUtil.registerGalacticraftCreature(EntitySkeletonBoss.class, "EvolvedSkeletonBoss", GCConfigManager.idEntityEvolvedSkeletonBoss, 12698049, 4802889);
		CoreUtil.registerGalacticraftCreature(EntityAlienVillager.class, "AlienVillager", GCConfigManager.idEntityAlienVillager, CoreUtil.to32BitColor(255, 103, 145, 181), 12422002);
	}

	public void registerOtherEntities()
	{
		CoreUtil.registerGalacticraftNonMobEntity(EntityTier1Rocket.class, "Spaceship", GCConfigManager.idEntitySpaceship, 150, 1, false);
		CoreUtil.registerGalacticraftNonMobEntity(EntityArrowGC.class, "GravityArrow", GCConfigManager.idEntityAntiGravityArrow, 150, 5, true);
		CoreUtil.registerGalacticraftNonMobEntity(EntityMeteor.class, "Meteor", GCConfigManager.idEntityMeteor, 150, 5, true);
		CoreUtil.registerGalacticraftNonMobEntity(EntityBuggy.class, "Buggy", GCConfigManager.idEntityBuggy, 150, 5, true);
		CoreUtil.registerGalacticraftNonMobEntity(EntityFlag.class, "Flag", GCConfigManager.idEntityFlag, 150, 5, true);
		CoreUtil.registerGalacticraftNonMobEntity(EntityParachest.class, "ParaChest", GCConfigManager.idEntityParaChest, 150, 5, true);
		CoreUtil.registerGalacticraftNonMobEntity(EntityBubble.class, "OxygenBubble", GCConfigManager.idEntityOxygenBubble, 150, 20, false);
		CoreUtil.registerGalacticraftNonMobEntity(EntityLander.class, "Lander", GCConfigManager.idEntityLander, 150, 5, false);
		CoreUtil.registerGalacticraftNonMobEntity(EntityMeteorChunk.class, "MeteorChunk", GCConfigManager.idEntityMeteorChunk, 150, 5, true);
	}
}
