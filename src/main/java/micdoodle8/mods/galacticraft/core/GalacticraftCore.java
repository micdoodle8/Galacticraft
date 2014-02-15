package micdoodle8.mods.galacticraft.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.Galaxy;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockFluid;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.gui.GuiHandler;
import micdoodle8.mods.galacticraft.core.command.GCCoreCommandPlanetTeleport;
import micdoodle8.mods.galacticraft.core.command.GCCoreCommandSpaceStationAddOwner;
import micdoodle8.mods.galacticraft.core.command.GCCoreCommandSpaceStationRemoveOwner;
import micdoodle8.mods.galacticraft.core.dimension.GCMoonWorldProvider;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerSP;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBlock;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.network.PacketPipeline;
import micdoodle8.mods.galacticraft.core.proxy.CommonProxy;
import micdoodle8.mods.galacticraft.core.tick.GCCoreTickHandlerClient;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.DimensionManager;
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
import cpw.mods.fml.common.event.FMLServerStartingEvent;
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
		
		GCCoreConfigManager.setDefaultValues(new File(event.getModConfigurationDirectory(), GalacticraftCore.CONFIG_FILE));
		
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
		
		this.packetPipeline = new PacketPipeline();
		this.packetPipeline.initalise();
		
		FMLCommonHandler.instance().bus().register(new GCCoreTickHandlerClient());
		
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		
		for (int i = GCCoreItems.fuelCanister.getMaxDamage() - 1; i > 0; i--)
		{
			FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(GalacticraftCore.fluidFuel, GCCoreItems.fuelCanister.getMaxDamage() - i), new ItemStack(GCCoreItems.fuelCanister, 1, i), new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.fuelCanister.getMaxDamage())));
		}

		for (int i = GCCoreItems.oilCanister.getMaxDamage() - 1; i > 0; i--)
		{
			FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(GalacticraftCore.fluidOil, GCCoreItems.oilCanister.getMaxDamage() - i), new ItemStack(GCCoreItems.oilCanister, 1, i), new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.fuelCanister.getMaxDamage())));
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
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new GCCoreCommandSpaceStationAddOwner());
		event.registerServerCommand(new GCCoreCommandSpaceStationRemoveOwner());
		event.registerServerCommand(new GCCoreCommandPlanetTeleport());

//		WorldUtil.registerSpaceStations(event.getServer().worldServerForDimension(0).getSaveHandler().getMapFileFromName("dummy").getParentFile());
		
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
}
