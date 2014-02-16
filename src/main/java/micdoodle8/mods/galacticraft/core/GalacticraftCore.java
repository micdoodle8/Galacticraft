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
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockFluid;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.gui.GuiHandler;
import micdoodle8.mods.galacticraft.core.command.GCCoreCommandPlanetTeleport;
import micdoodle8.mods.galacticraft.core.command.GCCoreCommandSpaceStationAddOwner;
import micdoodle8.mods.galacticraft.core.command.GCCoreCommandSpaceStationRemoveOwner;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreWorldProviderSpaceStation;
import micdoodle8.mods.galacticraft.core.dimension.GCMoonWorldProvider;
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
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerSP;
import micdoodle8.mods.galacticraft.core.event.GCCoreEvents;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBasic;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBlock;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.network.PacketPipeline;
import micdoodle8.mods.galacticraft.core.proxy.CommonProxy;
import micdoodle8.mods.galacticraft.core.recipe.GCCoreRecipeManager;
import micdoodle8.mods.galacticraft.core.schematic.GCCoreSchematicAdd;
import micdoodle8.mods.galacticraft.core.schematic.GCCoreSchematicMoonBuggy;
import micdoodle8.mods.galacticraft.core.schematic.GCCoreSchematicRocketT1;
import micdoodle8.mods.galacticraft.core.tick.GCCoreTickHandlerServer;
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
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.world.ChunkLoadingCallback;
import micdoodle8.mods.galacticraft.core.world.gen.GCCoreOverworldGenerator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
import cpw.mods.fml.common.FMLLog;
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
    
	public static Map<String, GCCorePlayerSP> playersClient = new HashMap<String, GCCorePlayerSP>();
	public static Map<String, GCCorePlayerMP> playersServer = new HashMap<String, GCCorePlayerMP>();

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
		proxy.preInit(event);
		MinecraftForge.EVENT_BUS.register(new GCCoreEvents());
		
		GCCoreConfigManager.setDefaultValues(new File(event.getModConfigurationDirectory(), GalacticraftCore.CONFIG_FILE));
		NetworkConfigHandler.setDefaultValues(new File(event.getModConfigurationDirectory(), GalacticraftCore.POWER_CONFIG_FILE));
		ChunkLoadingCallback.loadConfig(new File(event.getModConfigurationDirectory(), GalacticraftCore.CHUNKLOADER_CONFIG_FILE));
		
		GalacticraftCore.galaxyBlockyWay = new Galaxy("blockyWay").setMapPosition(new Vector3(0.0F, 0.0F));
		GalacticraftCore.planetOverworld = (Planet) new Planet("overworld").setParentGalaxy(GalacticraftCore.galaxyBlockyWay).setRingColorRGB(0.1F, 0.9F, 0.6F).setPhaseShift(0.75F);
		GalacticraftCore.planetOverworld.setPlanetIcon(new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/planets/overworld.png"));
		GalacticraftCore.moonMoon = (Moon) new Moon("moon").setParentPlanet(GalacticraftCore.planetOverworld).setRelativeSize(0.2667F).setRelativeDistanceFromCenter(40F).setRelativeOrbitTime(0.01F);
		GalacticraftCore.moonMoon.setDimensionInfo(GCCoreConfigManager.dimensionIDMoon, GCMoonWorldProvider.class);
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
			GCCoreBlocks.crudeOilStill = new GCCoreBlockFluid(GalacticraftCore.fluidOil, "oil");
			((GCCoreBlockFluid) GCCoreBlocks.crudeOilStill).setQuantaPerBlock(3);
			GCCoreBlocks.crudeOilStill.setBlockName("crudeOilStill");
			GameRegistry.registerBlock(GCCoreBlocks.crudeOilStill, GCCoreItemBlock.class, GCCoreBlocks.crudeOilStill.getUnlocalizedName(), GalacticraftCore.MOD_ID);
			GalacticraftCore.fluidOil.setBlock(GCCoreBlocks.crudeOilStill);
		}
		else
		{
			GCCoreBlocks.crudeOilStill = GalacticraftCore.fluidOil.getBlock();
		}

		if (GalacticraftCore.fluidFuel.getBlock() == null)
		{
			GCCoreBlocks.fuelStill = new GCCoreBlockFluid(GalacticraftCore.fluidFuel, "fuel");
			((GCCoreBlockFluid) GCCoreBlocks.fuelStill).setQuantaPerBlock(6);
			GCCoreBlocks.fuelStill.setBlockName("fuel");
			GameRegistry.registerBlock(GCCoreBlocks.fuelStill, GCCoreItemBlock.class, GCCoreBlocks.fuelStill.getUnlocalizedName(), GalacticraftCore.MOD_ID);
			GalacticraftCore.fluidFuel.setBlock(GCCoreBlocks.fuelStill);
		}
		else
		{
			GCCoreBlocks.fuelStill = GalacticraftCore.fluidFuel.getBlock();
		}
		
		GCCoreBlocks.initBlocks();
		GCCoreItems.initItems();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init(event);
		GalacticraftCore.galacticraftTab = new GCCoreCreativeTab(CreativeTabs.getNextID(), MOD_NAME, GCCoreBlocks.airLockFrame, 0);
		DimensionManager.registerProviderType(GCCoreConfigManager.idDimensionOverworldOrbit, GCCoreWorldProviderSpaceStation.class, false);
		
		this.packetPipeline = new PacketPipeline();
		this.packetPipeline.initalise();
		this.packetPipeline.postInitialise();

		ForgeChunkManager.setForcedChunkLoadingCallback(GalacticraftCore.instance, new ChunkLoadingCallback());
		
		FMLCommonHandler.instance().bus().register(new GCCoreTickHandlerServer());
		
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		
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
		ChunkPowerHandler.initiate();
		NetworkConfigHandler.initGas();

		this.registerCreatures();
		this.registerOtherEntities();
		GalacticraftRegistry.registerRocketGui(GCCoreWorldProviderSpaceStation.class, new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/overworldRocketGui.png"));
		GalacticraftRegistry.registerRocketGui(WorldProviderSurface.class, new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/overworldRocketGui.png"));
		GalacticraftRegistry.registerRocketGui(GCMoonWorldProvider.class, new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/moonRocketGui.png"));
		GalacticraftRegistry.addDungeonLoot(1, new ItemStack(GCCoreItems.schematic, 1, 0));
		GalacticraftRegistry.addDungeonLoot(1, new ItemStack(GCCoreItems.schematic, 1, 1));
		
		if (GCCoreConfigManager.enableCopperOreGen)
		{
			GameRegistry.registerWorldGenerator(new GCCoreOverworldGenerator(GCCoreBlocks.basicBlock, 5, 24, 0, 75, 7), 5);
		}

		if (GCCoreConfigManager.enableTinOreGen)
		{
			GameRegistry.registerWorldGenerator(new GCCoreOverworldGenerator(GCCoreBlocks.basicBlock, 6, 22, 0, 60, 7), 5);
		}

		if (GCCoreConfigManager.enableAluminumOreGen)
		{
			GameRegistry.registerWorldGenerator(new GCCoreOverworldGenerator(GCCoreBlocks.basicBlock, 7, 18, 0, 45, 7), 5);
		}

		if (GCCoreConfigManager.enableSiliconOreGen)
		{
			GameRegistry.registerWorldGenerator(new GCCoreOverworldGenerator(GCCoreBlocks.basicBlock, 8, 3, 0, 25, 7), 5);
		}
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
		
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

		GCCoreCompatibilityManager.checkForCompatibleMods();
		this.registerTileEntities();
		GCCoreRecipeManager.loadRecipes();

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
		
		// Support for the other spelling of Aluminum
		if (OreDictionary.getOres("ingotAluminium").size() > 0 || OreDictionary.getOres("ingotNaturalAluminum").size() > 0)
		{
			List<ItemStack> aluminumIngotList = new ArrayList<ItemStack>();
			aluminumIngotList.addAll(OreDictionary.getOres("ingotAluminium"));
			aluminumIngotList.addAll(OreDictionary.getOres("ingotNaturalAluminum"));

			for (ItemStack stack : aluminumIngotList)
			{
				CompressorRecipes.addShapelessRecipe(new ItemStack(GCCoreItems.basicItem, 1, 8), stack, stack);
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

		CompressorRecipes.addShapelessRecipe(new ItemStack(GCCoreItems.basicItem, 1, 9), Items.coal, new ItemStack(GCCoreItems.basicItem, 1, 11), Items.coal);
		CompressorRecipes.addShapelessRecipe(new ItemStack(GCCoreItems.basicItem, 1, 10), new ItemStack(GCCoreItems.basicItem, 1, 6), new ItemStack(GCCoreItems.basicItem, 1, 7));

		CompressorRecipes.addShapelessRecipe(new ItemStack(GCCoreItems.basicItem, 1, 11), Items.iron_ingot, Items.iron_ingot);
		CompressorRecipes.addShapelessRecipe(new ItemStack(GCCoreItems.meteoricIronIngot, 1, 1), new ItemStack(GCCoreItems.meteoricIronIngot, 1, 0));

		CompressorRecipes.addRecipe(new ItemStack(GCCoreItems.heavyPlatingTier1, 1, 0), "XYZ", "XYZ", 'X', new ItemStack(GCCoreItems.basicItem, 1, 9), 'Y', new ItemStack(GCCoreItems.basicItem, 1, 8), 'Z', new ItemStack(GCCoreItems.basicItem, 1, 10));

		CircuitFabricatorRecipes.addRecipe(new ItemStack(GCCoreItems.basicItem, 9, 12), new ItemStack[] { new ItemStack(Items.diamond), new ItemStack(GCCoreItems.basicItem, 1, 2), new ItemStack(GCCoreItems.basicItem, 1, 2), new ItemStack(Items.redstone), new ItemStack(Items.dye, 1, 4) });

		CircuitFabricatorRecipes.addRecipe(new ItemStack(GCCoreItems.basicItem, 3, 13), new ItemStack[] { new ItemStack(Items.diamond), new ItemStack(GCCoreItems.basicItem, 1, 2), new ItemStack(GCCoreItems.basicItem, 1, 2), new ItemStack(Items.redstone), new ItemStack(Blocks.redstone_torch) });

		CircuitFabricatorRecipes.addRecipe(new ItemStack(GCCoreItems.basicItem, 1, 14), new ItemStack[] { new ItemStack(Items.diamond), new ItemStack(GCCoreItems.basicItem, 1, 2), new ItemStack(GCCoreItems.basicItem, 1, 2), new ItemStack(Items.redstone), new ItemStack(Items.repeater) });
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new GCCoreCommandSpaceStationAddOwner());
		event.registerServerCommand(new GCCoreCommandSpaceStationRemoveOwner());
		event.registerServerCommand(new GCCoreCommandPlanetTeleport());

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
		GameRegistry.registerTileEntity(TileEntityTreasureChest.class, GCCoreCompatibilityManager.isAIILoaded() ? "Space Treasure Chest" : "Treasure Chest");
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
		GCCoreUtil.registerGalacticraftCreature(GCCoreEntitySpider.class, "EvolvedSpider", GCCoreConfigManager.idEntityEvolvedSpider, 3419431, 11013646);
		GCCoreUtil.registerGalacticraftCreature(GCCoreEntityZombie.class, "EvolvedZombie", GCCoreConfigManager.idEntityEvolvedZombie, 44975, 7969893);
		GCCoreUtil.registerGalacticraftCreature(GCCoreEntityCreeper.class, "EvolvedCreeper", GCCoreConfigManager.idEntityEvolvedCreeper, 894731, 0);
		GCCoreUtil.registerGalacticraftCreature(GCCoreEntitySkeleton.class, "EvolvedSkeleton", GCCoreConfigManager.idEntityEvolvedSkeleton, 12698049, 4802889);
		GCCoreUtil.registerGalacticraftCreature(GCCoreEntitySkeletonBoss.class, "EvolvedSkeletonBoss", GCCoreConfigManager.idEntityEvolvedSkeletonBoss, 12698049, 4802889);
		GCCoreUtil.registerGalacticraftCreature(GCCoreEntityAlienVillager.class, "AlienVillager", GCCoreConfigManager.idEntityAlienVillager, GCCoreUtil.to32BitColor(255, 103, 145, 181), 12422002);
	}

	public void registerOtherEntities()
	{
		GCCoreUtil.registerGalacticraftNonMobEntity(GCCoreEntityRocketT1.class, "Spaceship", GCCoreConfigManager.idEntitySpaceship, 150, 1, false);
		GCCoreUtil.registerGalacticraftNonMobEntity(GCCoreEntityArrow.class, "GravityArrow", GCCoreConfigManager.idEntityAntiGravityArrow, 150, 5, true);
		GCCoreUtil.registerGalacticraftNonMobEntity(GCCoreEntityMeteor.class, "Meteor", GCCoreConfigManager.idEntityMeteor, 150, 5, true);
		GCCoreUtil.registerGalacticraftNonMobEntity(GCCoreEntityBuggy.class, "Buggy", GCCoreConfigManager.idEntityBuggy, 150, 5, true);
		GCCoreUtil.registerGalacticraftNonMobEntity(GCCoreEntityFlag.class, "Flag", GCCoreConfigManager.idEntityFlag, 150, 5, true);
		GCCoreUtil.registerGalacticraftNonMobEntity(GCCoreEntityParaChest.class, "ParaChest", GCCoreConfigManager.idEntityParaChest, 150, 5, true);
		GCCoreUtil.registerGalacticraftNonMobEntity(GCCoreEntityOxygenBubble.class, "OxygenBubble", GCCoreConfigManager.idEntityOxygenBubble, 150, 20, false);
		GCCoreUtil.registerGalacticraftNonMobEntity(GCCoreEntityLander.class, "Lander", GCCoreConfigManager.idEntityLander, 150, 5, false);
		GCCoreUtil.registerGalacticraftNonMobEntity(GCCoreEntityMeteorChunk.class, "MeteorChunk", GCCoreConfigManager.idEntityMeteorChunk, 150, 5, true);
	}
}
