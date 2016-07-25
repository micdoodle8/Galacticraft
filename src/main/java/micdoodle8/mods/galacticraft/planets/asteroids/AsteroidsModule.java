package micdoodle8.mods.galacticraft.planets.asteroids;

import micdoodle8.mods.galacticraft.core.util.CreativeTabGC;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.command.CommandGCAstroMiner;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
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
import micdoodle8.mods.galacticraft.planets.asteroids.inventory.ContainerAstroMinerDock;
import micdoodle8.mods.galacticraft.planets.asteroids.inventory.ContainerShortRangeTelepad;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.network.PacketSimpleAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.recipe.CanisterRecipes;
import micdoodle8.mods.galacticraft.planets.asteroids.recipe.RecipeManagerAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.schematic.SchematicAstroMiner;
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
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.oredict.RecipeSorter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class AsteroidsModule implements IPlanetsModule
{
    public static Planet planetAsteroids;

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

    private Fluid registerFluid(String fluidName, int density, int viscosity, int temperature, boolean gaseous, String fluidTexture)
    {
        Fluid returnFluid = FluidRegistry.getFluid(fluidName);
    	if (returnFluid == null)
        {
            ResourceLocation texture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX + ":fluids/" + fluidTexture);
    		FluidRegistry.registerFluid(new Fluid(fluidName, texture, texture).setDensity(density).setViscosity(viscosity).setTemperature(temperature).setGaseous(gaseous));
    		returnFluid = FluidRegistry.getFluid(fluidName);
        }
    	return returnFluid;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        playerHandler = new AsteroidsPlayerHandler();
        MinecraftForge.EVENT_BUS.register(playerHandler);
        AsteroidsEventHandler eventHandler = new AsteroidsEventHandler();
        MinecraftForge.EVENT_BUS.register(eventHandler);
        RecipeSorter.register("galacticraftplanets:canisterRecipe", CanisterRecipes.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

        registerFluid("methane", 1, 11, 295, true, "MethaneGas");
        registerFluid("atmosphericgases", 1, 13, 295, true, "AtmosphericGases");
        registerFluid("liquidmethane", 450, 120, 109, false, "LiquidMethane");
        //Data source for liquid methane: http://science.nasa.gov/science-news/science-at-nasa/2005/25feb_titan2/
        registerFluid("liquidoxygen", 1141, 140, 90, false, "LiquidOxygen");
        registerFluid("oxygen", 1, 13, 295, true, "OxygenGas");
        registerFluid("liquidnitrogen", 808, 130, 90, false, "LiquidNitrogen");
        registerFluid("nitrogen", 1, 12, 295, true, "NitrogenGas");
        registerFluid("carbondioxide", 2, 20, 295, true, "CarbonDioxideGas");
        registerFluid("hydrogen", 1, 1, 295, true, "HydrogenGas");
        registerFluid("argon", 1, 4, 295, true, "ArgonGas");
        registerFluid("liquidargon", 900, 100, 87, false, "LiquidArgon");
        registerFluid("helium", 1, 1, 295, true, "HeliumGas");
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
        
        FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(AsteroidsModule.fluidMethaneGas, 1000), new ItemStack(AsteroidsItems.methaneCanister, 1, 1), new ItemStack(GCItems.oilCanister, 1, ItemCanisterGeneric.EMPTY)));
        FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(AsteroidsModule.fluidLiquidOxygen, 1000), new ItemStack(AsteroidsItems.canisterLOX, 1, 1), new ItemStack(GCItems.oilCanister, 1, ItemCanisterGeneric.EMPTY)));
        FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(AsteroidsModule.fluidLiquidNitrogen, 1000), new ItemStack(AsteroidsItems.canisterLN2, 1, 1), new ItemStack(GCItems.oilCanister, 1, ItemCanisterGeneric.EMPTY)));
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        ((CreativeTabGC) GalacticraftCore.galacticraftItemsTab).setItemForTab(AsteroidsItems.astroMiner); // Set creative tab item to Astro Miner

        this.registerMicroBlocks();
    	SchematicRegistry.registerSchematicRecipe(new SchematicTier3Rocket());
    	SchematicRegistry.registerSchematicRecipe(new SchematicAstroMiner());

        GalacticraftCore.packetPipeline.addDiscriminator(7, PacketSimpleAsteroids.class);

        AsteroidsTickHandlerServer eventHandler = new AsteroidsTickHandlerServer();
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

        input = new HashMap<Integer, ItemStack>();
        input.put(1, new ItemStack(GCItems.heavyPlatingTier1));
        input.put(3, new ItemStack(GCItems.heavyPlatingTier1));
        input.put(5, new ItemStack(GCItems.heavyPlatingTier1));
        input.put(11, new ItemStack(GCItems.heavyPlatingTier1));
        input.put(2, new ItemStack(AsteroidsItems.orionDrive));
        input.put(4, new ItemStack(AsteroidsItems.orionDrive));
        input.put(9, new ItemStack(AsteroidsItems.orionDrive));
        input.put(10, new ItemStack(AsteroidsItems.orionDrive));
        input.put(12, new ItemStack(AsteroidsItems.orionDrive));
        input.put(6, new ItemStack(GCItems.basicItem, 1, 14));
        input.put(7, new ItemStack(Blocks.chest));
        input.put(8, new ItemStack(Blocks.chest));
        input.put(13, new ItemStack(AsteroidsItems.basicItem, 1, 8));
        input.put(14, new ItemStack(GCItems.flagPole));
        GalacticraftRegistry.addAstroMinerRecipe(new NasaWorkbenchRecipe(new ItemStack(AsteroidsItems.astroMiner, 1, 0), input));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {

    }

    @Override
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandGCAstroMiner());
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
        if (side == Side.SERVER)
        {
	        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
	
	        switch (ID)
	        {
	        case GuiIdsPlanets.MACHINE_ASTEROIDS:
	
	            if (tile instanceof TileEntityShortRangeTelepad)
	            {
	                return new ContainerShortRangeTelepad(player.inventory, ((TileEntityShortRangeTelepad) tile), player);
	            }
	            if (tile instanceof TileEntityMinerBase)
	            {
	            	return new ContainerAstroMinerDock(player.inventory, (TileEntityMinerBase) tile);
	            }
	
	            break;
	        }
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
        ConfigManagerAsteroids.syncConfig(false, false);
    }
}
