package micdoodle8.mods.galacticraft.planets.asteroids;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
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
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityEntryPod;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityGrapple;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntitySmallAsteroid;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityTier3Rocket;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.player.AsteroidsPlayerHandler;
import micdoodle8.mods.galacticraft.planets.asteroids.event.AsteroidsEventHandler;
import micdoodle8.mods.galacticraft.planets.asteroids.inventory.ContainerShortRangeTelepad;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.network.PacketSimpleAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.recipe.CanisterRecipes;
import micdoodle8.mods.galacticraft.planets.asteroids.recipe.RecipeManagerAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.schematic.SchematicTier3Rocket;
import micdoodle8.mods.galacticraft.planets.asteroids.tick.AsteroidsTickHandlerServer;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.*;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import net.minecraft.block.Block;
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
import net.minecraftforge.oredict.RecipeSorter;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class AsteroidsModule implements IPlanetsModule
{
    public static Planet planetAsteroids;

    public static final String ASSET_PREFIX = "galacticraftasteroids";
    public static final String TEXTURE_PREFIX = AsteroidsModule.ASSET_PREFIX + ":";

    public static AsteroidsPlayerHandler playerHandler;
    public static Fluid fluidMethaneGas;
    public static Fluid fluidOxygenGas;
    public static Fluid fluidNitrogenGas;
    public static Fluid fluidLiquidMethane;
    public static Fluid fluidLiquidOxygen;
    public static Fluid fluidLiquidNitrogen;
    public static Fluid fluidLiquidArgon;
    public static Fluid fluidAtmosphericGases;
    //public static Fluid fluidCO2Gas;

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        new ConfigManagerAsteroids(new File(event.getModConfigurationDirectory(), "Galacticraft/asteroids.conf"));
        playerHandler = new AsteroidsPlayerHandler();
        MinecraftForge.EVENT_BUS.register(playerHandler);
        FMLCommonHandler.instance().bus().register(playerHandler);
        AsteroidsEventHandler eventHandler = new AsteroidsEventHandler();
        MinecraftForge.EVENT_BUS.register(eventHandler);
        FMLCommonHandler.instance().bus().register(eventHandler);
        RecipeSorter.register("galacticraftmars:canisterRecipe", CanisterRecipes.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

        FluidRegistry.registerFluid(new Fluid("methane").setDensity(1).setViscosity(11).setGaseous(true));
        FluidRegistry.registerFluid(new Fluid("atmosphericgases").setDensity(1).setViscosity(13).setGaseous(true));
        FluidRegistry.registerFluid(new Fluid("liquidmethane").setDensity(450).setViscosity(120).setTemperature(109));
        //Data source for liquid methane: http://science.nasa.gov/science-news/science-at-nasa/2005/25feb_titan2/
        FluidRegistry.registerFluid(new Fluid("liquidoxygen").setDensity(1141).setViscosity(140).setTemperature(90));
        FluidRegistry.registerFluid(new Fluid("oxygen").setDensity(1).setViscosity(13).setGaseous(true));
        FluidRegistry.registerFluid(new Fluid("liquidnitrogen").setDensity(808).setViscosity(130).setTemperature(90));
        FluidRegistry.registerFluid(new Fluid("nitrogen").setDensity(1).setViscosity(12).setGaseous(true));
        FluidRegistry.registerFluid(new Fluid("carbondioxide").setDensity(2).setViscosity(20).setGaseous(true));
        FluidRegistry.registerFluid(new Fluid("hydrogen").setDensity(1).setViscosity(1).setGaseous(true));
        FluidRegistry.registerFluid(new Fluid("argon").setDensity(1).setViscosity(4).setGaseous(true));
        FluidRegistry.registerFluid(new Fluid("liquidargon").setDensity(900).setViscosity(100).setTemperature(87));
        FluidRegistry.registerFluid(new Fluid("helium").setDensity(1).setViscosity(1).setGaseous(true));
        AsteroidsModule.fluidMethaneGas = FluidRegistry.getFluid("methane");
        AsteroidsModule.fluidAtmosphericGases = FluidRegistry.getFluid("atmosphericgases");
        AsteroidsModule.fluidLiquidMethane = FluidRegistry.getFluid("liquidmethane");
        AsteroidsModule.fluidLiquidOxygen = FluidRegistry.getFluid("liquidoxygen");
        AsteroidsModule.fluidOxygenGas = FluidRegistry.getFluid("oxygen");
        AsteroidsModule.fluidLiquidNitrogen = FluidRegistry.getFluid("liquidnitrogen");
        AsteroidsModule.fluidLiquidArgon = FluidRegistry.getFluid("liquidargon");
        AsteroidsModule.fluidNitrogenGas = FluidRegistry.getFluid("nitrogen");
        //AsteroidsModule.fluidCO2Gas = FluidRegistry.getFluid("carbondioxide");

        AsteroidBlocks.initBlocks();
        AsteroidBlocks.registerBlocks();
        AsteroidBlocks.setHarvestLevels();
        AsteroidBlocks.oreDictRegistration();
        
        AsteroidsItems.initItems();
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        this.registerMicroBlocks();
    	SchematicRegistry.registerSchematicRecipe(new SchematicTier3Rocket());

        GalacticraftCore.packetPipeline.addDiscriminator(7, PacketSimpleAsteroids.class);

        AsteroidsTickHandlerServer eventHandler = new AsteroidsTickHandlerServer();
        FMLCommonHandler.instance().bus().register(eventHandler);
        MinecraftForge.EVENT_BUS.register(eventHandler);

        this.registerEntities();

        RecipeManagerAsteroids.loadRecipes();

        AsteroidsModule.planetAsteroids = new Planet("asteroids").setParentSolarSystem(GalacticraftCore.solarSystemSol);
        AsteroidsModule.planetAsteroids.setDimensionInfo(ConfigManagerAsteroids.dimensionIDAsteroids, WorldProviderAsteroids.class).setTierRequired(3);
        AsteroidsModule.planetAsteroids.setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(1.375F, 1.375F)).setRelativeOrbitTime(45.0F).setPhaseShift((float) (Math.random() * (2 * Math.PI)));
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
        MarsModule.registerGalacticraftNonMobEntity(EntitySmallAsteroid.class, "SmallAsteroidGC", 150, 3, true);
        MarsModule.registerGalacticraftNonMobEntity(EntityGrapple.class, "GrappleHookGC", 150, 1, true);
        MarsModule.registerGalacticraftNonMobEntity(EntityTier3Rocket.class, "Tier3RocketGC", 150, 1, false);
        MarsModule.registerGalacticraftNonMobEntity(EntityEntryPod.class, "EntryPodAsteroids", 150, 1, true);
        MarsModule.registerGalacticraftNonMobEntity(EntityAstroMiner.class, "AstroMiner", 80, 1, true);
    }

    private void registerMicroBlocks()
    {
		try {
			Class clazz = Class.forName("codechicken.microblock.MicroMaterialRegistry");
			if (clazz != null)
			{
				Method registerMethod = null;
				Method[] methodz = clazz.getMethods();
				for (Method m : methodz)
				{
					if (m.getName().equals("registerMaterial"))
					{
						registerMethod = m;
						break;
					}
				}
				Class clazzbm = Class.forName("codechicken.microblock.BlockMicroMaterial");
				registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(AsteroidBlocks.blockBasic, 0), "tile.asteroidsBlock.asteroid0");
				registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(AsteroidBlocks.blockBasic, 1), "tile.asteroidsBlock.asteroid1");
				registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(AsteroidBlocks.blockBasic, 2), "tile.asteroidsBlock.asteroid2");
				registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(AsteroidBlocks.blockDenseIce, 0), "tile.denseIce");
			}
		} catch (Exception e) {}
	}

    private void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntityBeamReflector.class, "Beam Reflector");
        GameRegistry.registerTileEntity(TileEntityBeamReceiver.class, "Beam Receiver");
        GameRegistry.registerTileEntity(TileEntityShortRangeTelepad.class, "Short Range Telepad");
        GameRegistry.registerTileEntity(TileEntityTelepadFake.class, "Fake Short Range Telepad");
        GameRegistry.registerTileEntity(TileEntityTreasureChestAsteroids.class, "Asteroids Treasure Chest");
        GameRegistry.registerTileEntity(TileEntityMinerBaseSingle.class, "Astro Miner Base Builder");
        GameRegistry.registerTileEntity(TileEntityMinerBase.class, "Astro Miner Base");
    }

    @Override
    public Configuration getConfiguration()
    {
        return ConfigManagerAsteroids.config;
    }

    @Override
    public void syncConfig()
    {
        ConfigManagerAsteroids.syncConfig(false);
    }
}
