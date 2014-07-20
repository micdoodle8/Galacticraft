package micdoodle8.mods.galacticraft.planets.asteroids;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.recipe.NasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.IPlanetsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.TeleportTypeAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.WorldProviderAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityEntryPod;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityGrapple;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntitySmallAsteroid;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityTier3Rocket;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.player.AsteroidsPlayerHandler;
import micdoodle8.mods.galacticraft.planets.asteroids.inventory.ContainerShortRangeTelepad;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.network.PacketSimpleAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.schematic.SchematicTier3Rocket;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReceiver;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReflector;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import micdoodle8.mods.galacticraft.planets.asteroids.tick.AsteroidsTickHandlerServer;
import micdoodle8.mods.galacticraft.planets.asteroids.util.AsteroidsUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class AsteroidsModule implements IPlanetsModule
{
	public static Planet planetAsteroids;

	public static final String ASSET_PREFIX = "galacticraftasteroids";
	public static final String TEXTURE_PREFIX = AsteroidsModule.ASSET_PREFIX + ":";

	public static Fluid gcFluidLiquidMethane;
	public static Fluid gcFluidLiquidOxygen;
	public static Fluid gcFluidLiquidNitrogen;
	public static Fluid fluidLiquidMethane;
	public static Fluid fluidLiquidOxygen;
	public static Fluid fluidLiquidNitrogen;

	
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		new ConfigManagerAsteroids(new File(event.getModConfigurationDirectory(), "Galacticraft/asteroids.conf"));
		AsteroidsPlayerHandler playerHandler = new AsteroidsPlayerHandler();
		MinecraftForge.EVENT_BUS.register(playerHandler);
		FMLCommonHandler.instance().bus().register(playerHandler);
		
		AsteroidsModule.gcFluidLiquidMethane = new Fluid("methane").setDensity(422).setViscosity(11);
		AsteroidsModule.gcFluidLiquidOxygen = new Fluid("liquidOxygen").setDensity(1141).setViscosity(20);
		AsteroidsModule.gcFluidLiquidNitrogen = new Fluid("liquidNitrogen").setDensity(808).setViscosity(18);
		FluidRegistry.registerFluid(AsteroidsModule.gcFluidLiquidMethane);
		FluidRegistry.registerFluid(AsteroidsModule.gcFluidLiquidOxygen);
		FluidRegistry.registerFluid(AsteroidsModule.gcFluidLiquidNitrogen);
		AsteroidsModule.fluidLiquidMethane = FluidRegistry.getFluid("methane");
		AsteroidsModule.fluidLiquidOxygen = FluidRegistry.getFluid("liquidOxygen");
		AsteroidsModule.fluidLiquidNitrogen = FluidRegistry.getFluid("liquidNitrogen");
		
		AsteroidBlocks.initBlocks();
		AsteroidBlocks.registerBlocks();
		AsteroidsItems.initItems();
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
        SchematicRegistry.registerSchematicRecipe(new SchematicTier3Rocket());

        GalacticraftCore.packetPipeline.addDiscriminator(7, PacketSimpleAsteroids.class);

        AsteroidsTickHandlerServer eventHandler = new AsteroidsTickHandlerServer();
        FMLCommonHandler.instance().bus().register(eventHandler);
        MinecraftForge.EVENT_BUS.register(eventHandler);

		this.registerEntities();

		AsteroidsModule.planetAsteroids = new Planet("asteroids").setParentSolarSystem(GalacticraftCore.solarSystemSol);
		AsteroidsModule.planetAsteroids.setDimensionInfo(ConfigManagerAsteroids.dimensionIDAsteroids, WorldProviderAsteroids.class);
        AsteroidsModule.planetAsteroids.setRelativeDistanceFromCenter(2.5F).setRelativeOrbitTime(0.001F).setPhaseShift(0.6667F);
        AsteroidsModule.planetAsteroids.setBodyIcon(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/asteroid.png"));

		GalaxyRegistry.registerPlanet(AsteroidsModule.planetAsteroids);
		GalacticraftRegistry.registerTeleportType(WorldProviderAsteroids.class, new TeleportTypeAsteroids());

        HashMap<Integer, ItemStack> input = new HashMap<Integer, ItemStack>();
        input.put(1, new ItemStack(AsteroidsItems.heavyNoseCone));
        input.put(2, new ItemStack(AsteroidsItems.basicItem, 1, 0));
        input.put(3, new ItemStack(AsteroidsItems.basicItem, 1, 0));
        input.put(4, new ItemStack(AsteroidsItems.basicItem, 1, 0));
        input.put(5, new ItemStack(AsteroidsItems.basicItem, 1, 0));
        input.put(6, new ItemStack(AsteroidsItems.basicItem, 1, 0));
        input.put(7, new ItemStack(AsteroidsItems.basicItem, 1, 0));
        input.put(8, new ItemStack(AsteroidsItems.basicItem, 1, 0));
        input.put(9, new ItemStack(AsteroidsItems.basicItem, 1, 0));
        input.put(10, new ItemStack(AsteroidsItems.basicItem, 1, 0));
        input.put(11, new ItemStack(AsteroidsItems.basicItem, 1, 0));
        input.put(12, new ItemStack(GCItems.rocketEngine, 1, 1));
        input.put(13, new ItemStack(AsteroidsItems.basicItem, 1, 2));
        input.put(14, new ItemStack(AsteroidsItems.basicItem, 1, 2));
        input.put(15, new ItemStack(AsteroidsItems.basicItem, 1, 1));
        input.put(16, new ItemStack(GCItems.rocketEngine, 1, 1));
        input.put(17, new ItemStack(AsteroidsItems.basicItem, 1, 2));
        input.put(18, new ItemStack(AsteroidsItems.basicItem, 1, 2));
        input.put(19, null);
        input.put(20, null);
        input.put(21, null);
        GalacticraftRegistry.addT3RocketRecipe(new NasaWorkbenchRecipe(new ItemStack(AsteroidsItems.tier3Rocket, 1, 0), input));

        HashMap<Integer, ItemStack> input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, new ItemStack(Blocks.chest));
        input2.put(20, null);
        input2.put(21, null);
        GalacticraftRegistry.addT3RocketRecipe(new NasaWorkbenchRecipe(new ItemStack(AsteroidsItems.tier3Rocket, 1, 1), input2));

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, null);
        input2.put(20, new ItemStack(Blocks.chest));
        input2.put(21, null);
        GalacticraftRegistry.addT3RocketRecipe(new NasaWorkbenchRecipe(new ItemStack(AsteroidsItems.tier3Rocket, 1, 1), input2));

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, null);
        input2.put(20, null);
        input2.put(21, new ItemStack(Blocks.chest));
        GalacticraftRegistry.addT3RocketRecipe(new NasaWorkbenchRecipe(new ItemStack(AsteroidsItems.tier3Rocket, 1, 1), input2));

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, new ItemStack(Blocks.chest));
        input2.put(20, new ItemStack(Blocks.chest));
        input2.put(21, null);
        GalacticraftRegistry.addT3RocketRecipe(new NasaWorkbenchRecipe(new ItemStack(AsteroidsItems.tier3Rocket, 1, 2), input2));

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, new ItemStack(Blocks.chest));
        input2.put(20, null);
        input2.put(21, new ItemStack(Blocks.chest));
        GalacticraftRegistry.addT3RocketRecipe(new NasaWorkbenchRecipe(new ItemStack(AsteroidsItems.tier3Rocket, 1, 2), input2));

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, null);
        input2.put(20, new ItemStack(Blocks.chest));
        input2.put(21, new ItemStack(Blocks.chest));
        GalacticraftRegistry.addT3RocketRecipe(new NasaWorkbenchRecipe(new ItemStack(AsteroidsItems.tier3Rocket, 1, 2), input2));

        input2 = new HashMap<Integer, ItemStack>(input);
        input2.put(19, new ItemStack(Blocks.chest));
        input2.put(20, new ItemStack(Blocks.chest));
        input2.put(21, new ItemStack(Blocks.chest));
        GalacticraftRegistry.addT3RocketRecipe(new NasaWorkbenchRecipe(new ItemStack(AsteroidsItems.tier3Rocket, 1, 3), input2));
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{

	}

	@Override
	public void serverStarting(FMLServerStartingEvent event)
	{

	}

    @Override
    public void serverInit(FMLServerStartedEvent event)
    {
        AsteroidsTickHandlerServer.restart();
    }

    @Override
	public void getGuiIDs(List<Integer> idList)
	{
        idList.add(GuiIdsPlanets.MACHINE_ASTEROIDS);
	}

	@Override
	public Object getGuiElement(Side side, int ID, EntityPlayer player, World world, int x, int y, int z)
	{
        TileEntity tile = world.getTileEntity(x, y, z);

        switch (ID)
        {
            case GuiIdsPlanets.MACHINE_ASTEROIDS:

                if (tile instanceof TileEntityShortRangeTelepad)
                {
                    return new ContainerShortRangeTelepad(player.inventory, ((TileEntityShortRangeTelepad) tile));
                }

                break;
        }

		return null;
	}

	private void registerEntities()
	{
		this.registerCreatures();
		this.registerNonMobEntities();
		this.registerTileEntities();
	}

	private void registerCreatures()
	{

	}

	private void registerNonMobEntities()
	{
		AsteroidsUtil.registerAsteroidsNonMobEntity(EntitySmallAsteroid.class, "SmallAsteroidGC", ConfigManagerAsteroids.idEntitySmallAsteroid, 150, 1, true);
		AsteroidsUtil.registerAsteroidsNonMobEntity(EntityGrapple.class, "GrappleHookGC", ConfigManagerAsteroids.idEntityGrappleHook, 150, 1, true);
		AsteroidsUtil.registerAsteroidsNonMobEntity(EntityTier3Rocket.class, "Tier3RocketGC", ConfigManagerAsteroids.idEntityTier3Rocket, 150, 1, false);
        AsteroidsUtil.registerAsteroidsNonMobEntity(EntityEntryPod.class, "EntryPodAsteroids", ConfigManagerAsteroids.idEntityEntryPod, 150, 1, true);
	}

	private void registerTileEntities()
	{
		GameRegistry.registerTileEntity(TileEntityBeamReflector.class, "Beam Reflector");
		GameRegistry.registerTileEntity(TileEntityBeamReceiver.class, "Beam Receiver");
		GameRegistry.registerTileEntity(TileEntityShortRangeTelepad.class, "Short Range Telepad");
	}

    @Override
    public Configuration getConfiguration()
    {
        return ConfigManagerAsteroids.configuration;
    }

    @Override
    public void syncConfig()
    {
        ConfigManagerAsteroids.syncConfig();
    }
}
