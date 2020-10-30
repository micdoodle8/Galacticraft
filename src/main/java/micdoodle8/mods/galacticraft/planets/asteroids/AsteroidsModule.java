package micdoodle8.mods.galacticraft.planets.asteroids;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.item.EnumExtendedInventorySlot;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.world.AtmosphereInfo;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.IPlanetsModule;
import micdoodle8.mods.galacticraft.planets.PlanetDimensions;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.DimensionAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.TeleportTypeAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.player.AsteroidsPlayerHandler;
import micdoodle8.mods.galacticraft.planets.asteroids.event.AsteroidsEventHandler;
import micdoodle8.mods.galacticraft.planets.asteroids.inventory.AsteroidsContainers;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.schematic.SchematicAstroMiner;
import micdoodle8.mods.galacticraft.planets.asteroids.schematic.SchematicTier3Rocket;
import micdoodle8.mods.galacticraft.planets.asteroids.tick.AsteroidsTickHandlerServer;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.AsteroidChunkGenerator;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.BiomeAsteroids;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.dimension.biome.BiomeMars;
import micdoodle8.mods.galacticraft.planets.venus.inventory.VenusContainers;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;

public class AsteroidsModule implements IPlanetsModule
{
    public static Planet planetAsteroids;

    public static AsteroidsPlayerHandler playerHandler;
//    public static Fluid fluidMethaneGas;
//    public static Fluid fluidOxygenGas;
//    public static Fluid fluidNitrogenGas;
//    public static Fluid fluidLiquidMethane;
//    public static Fluid fluidLiquidOxygen;
//    public static Fluid fluidLiquidNitrogen;
//    public static Fluid fluidLiquidArgon;
//    public static Fluid fluidAtmosphericGases;
    //public static Fluid fluidCO2Gas;


    public AsteroidsModule()
    {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addGenericListener(ContainerType.class, AsteroidsContainers::initContainers);
    }

    @Override
    public void init(FMLCommonSetupEvent event)
    {
        AsteroidsModule.planetAsteroids = new Planet("asteroids").setParentSolarSystem(GalacticraftCore.solarSystemSol);

        playerHandler = new AsteroidsPlayerHandler();
        MinecraftForge.EVENT_BUS.register(playerHandler);
        AsteroidsEventHandler eventHandler = new AsteroidsEventHandler();
        MinecraftForge.EVENT_BUS.register(eventHandler);
//        RecipeSorter.register("galacticraftplanets:canisterRecipe", CanisterRecipes.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

//        registerFluid("methane", 1, 11, 295, true);
//        registerFluid("atmosphericgases", 1, 13, 295, true);
//        registerFluid("liquidmethane", 450, 120, 109, false);
//        //Data source for liquid methane: http://science.nasa.gov/science-news/science-at-nasa/2005/25feb_titan2/
//        registerFluid("liquidoxygen", 1141, 140, 90, false);
//        registerFluid("liquidnitrogen", 808, 130, 90, false);
//        registerFluid("nitrogen", 1, 12, 295, true);
//        registerFluid("carbondioxide", 2, 20, 295, true);
//        registerFluid("argon", 1, 4, 295, true);
//        registerFluid("liquidargon", 900, 100, 87, false);
//        registerFluid("helium", 1, 1, 295, true);
//        AsteroidsModule.fluidMethaneGas = FluidRegistry.getFluid("methane");
//        AsteroidsModule.fluidAtmosphericGases = FluidRegistry.getFluid("atmosphericgases");
//        AsteroidsModule.fluidLiquidMethane = FluidRegistry.getFluid("liquidmethane");
//        AsteroidsModule.fluidLiquidOxygen = FluidRegistry.getFluid("liquidoxygen");
//        AsteroidsModule.fluidOxygenGas = FluidRegistry.getFluid("oxygen");
//        AsteroidsModule.fluidLiquidNitrogen = FluidRegistry.getFluid("liquidnitrogen");
//        AsteroidsModule.fluidLiquidArgon = FluidRegistry.getFluid("liquidargon");
//        AsteroidsModule.fluidNitrogenGas = FluidRegistry.getFluid("nitrogen");

        //AsteroidsModule.fluidCO2Gas = FluidRegistry.getFluid("carbondioxide");

//        AsteroidBlocks.initBlocks();
//        AsteroidBlocks.registerBlocks();
//        AsteroidBlocks.setHarvestLevels();

//        AsteroidsItems.initItems();

        //This enables Endermen on Asteroids in Asteroids Challenge mode
        //TODO: could also increase mob spawn frequency in Hard Mode on various dimensions e.g. Mars and Venus?

//        FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(AsteroidsModule.fluidMethaneGas, 1000), new ItemStack(AsteroidsItems.methaneCanister, 1, 1), ItemOilCanister.createEmptyCanister(1)));
//        FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(AsteroidsModule.fluidLiquidOxygen, 1000), new ItemStack(AsteroidsItems.canisterLOX, 1, 1), ItemOilCanister.createEmptyCanister(1)));
//        FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(AsteroidsModule.fluidLiquidNitrogen, 1000), new ItemStack(AsteroidsItems.canisterLN2, 1, 1), ItemOilCanister.createEmptyCanister(1)));
//        AsteroidBlocks.oreDictRegistration();
//        AsteroidsItems.oreDictRegistrations();

//        this.registerMicroBlocks();
        SchematicRegistry.registerSchematicRecipe(new SchematicTier3Rocket());
        SchematicRegistry.registerSchematicRecipe(new SchematicAstroMiner());

//        GalacticraftCore.packetPipeline.addDiscriminator(7, PacketSimpleAsteroids.class);

        AsteroidsTickHandlerServer tickHandler = new AsteroidsTickHandlerServer();
        MinecraftForge.EVENT_BUS.register(tickHandler);

//        this.registerEntities();

//        RecipeManagerAsteroids.loadCompatibilityRecipes();

        AsteroidsModule.planetAsteroids.setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(1.375F, 1.375F)).setRelativeOrbitTime(45.0F).setPhaseShift((float) (Math.random() * (2 * Math.PI)));
        AsteroidsModule.planetAsteroids.setBodyIcon(new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/celestialbodies/asteroid.png"));
        AsteroidsModule.planetAsteroids.setAtmosphere(new AtmosphereInfo(false, false, false, -1.5F, 0.05F, 0.0F));
        AsteroidsModule.planetAsteroids.addChecklistKeys("equip_oxygen_suit", "craft_grapple_hook", "thermal_padding");

        GalaxyRegistry.registerPlanet(AsteroidsModule.planetAsteroids);
        GalacticraftRegistry.registerTeleportType(DimensionAsteroids.class, new TeleportTypeAsteroids());

        GalacticraftRegistry.registerGear(Constants.GEAR_ID_THERMAL_PADDING_T1_HELMET, EnumExtendedInventorySlot.THERMAL_HELMET, new ItemStack(AsteroidsItems.thermalHelm, 1));
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_THERMAL_PADDING_T1_CHESTPLATE, EnumExtendedInventorySlot.THERMAL_CHESTPLATE, new ItemStack(AsteroidsItems.thermalChestplate, 1));
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_THERMAL_PADDING_T1_LEGGINGS, EnumExtendedInventorySlot.THERMAL_LEGGINGS, new ItemStack(AsteroidsItems.thermalLeggings, 1));
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_THERMAL_PADDING_T1_BOOTS, EnumExtendedInventorySlot.THERMAL_BOOTS, new ItemStack(AsteroidsItems.thermalBoots, 1));
    }

    @SubscribeEvent
    public void biomeRegisterEvent(RegistryEvent.Register<Biome> evt)
    {
        IForgeRegistry<Biome> r = evt.getRegistry();
        AsteroidsModule.planetAsteroids.setBiomeInfo(r, BiomeAsteroids.asteroid);
//        BiomeAsteroids.asteroid.resetMonsterListByMode(ConfigManagerCore.challengeMobDropsAndSpawning); TODO Needed?
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event)
    {
//        event.registerServerCommand(new CommandGCAstroMiner()); TODO Commands
        AsteroidChunkGenerator.reset();
    }

    @Override
    public void serverInit(FMLServerStartedEvent event)
    {
        AsteroidsTickHandlerServer.restart();
    }

//    @Override
//    public void getGuiIDs(List<Integer> idList)
//    {
//        idList.add(GuiIdsPlanets.MACHINE_ASTEROIDS);
//    }
//
//    @Override
//    public Object getGuiElement(LogicalSide LogicalSide, int ID, PlayerEntity player, World world, int x, int y, int z)
//    {
//        if (LogicalSide == LogicalSide.SERVER)
//        {
//            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
//
//            switch (ID)
//            {
//            case GuiIdsPlanets.MACHINE_ASTEROIDS:
//
//                if (tile instanceof TileEntityShortRangeTelepad)
//                {
//                    return new ContainerShortRangeTelepad(player.inventory, ((TileEntityShortRangeTelepad) tile), player);
//                }
//                if (tile instanceof TileEntityMinerBase)
//                {
//                    return new ContainerAstroMinerDock(player.inventory, (TileEntityMinerBase) tile);
//                }
//
//                break;
//            }
//        }
//
//        return null;
//    }
//
//    private void registerEntities()
//    {
//        this.registerCreatures();
//        this.registerNonMobEntities();
//        this.registerTileEntities();
//    }
//
//    private void registerCreatures()
//    {
//
//    }
//
//    private void registerNonMobEntities()
//    {
//        MarsModule.registerGalacticraftNonMobEntity(EntitySmallAsteroid.class, "small_asteroid", 150, 3, true);
//        MarsModule.registerGalacticraftNonMobEntity(EntityGrapple.class, "grapple_hook", 150, 1, true);
//        MarsModule.registerGalacticraftNonMobEntity(EntityTier3Rocket.class, "rocket_t3", 150, 1, false);
//        MarsModule.registerGalacticraftNonMobEntity(EntityEntryPod.class, "entry_pod", 150, 1, true);
//        MarsModule.registerGalacticraftNonMobEntity(EntityAstroMiner.class, "astro_miner", 80, 1, true);
//    }
//
//    private void registerMicroBlocks()
//    {
//        try
//        {
//            Class<?> clazz = Class.forName("codechicken.microblock.MicroMaterialRegistry");
//            if (clazz != null)
//            {
//                Method registerMethod = null;
//                Method[] methodz = clazz.getMethods();
//                for (Method m : methodz)
//                {
//                    if (m.getName().equals("registerMaterial"))
//                    {
//                        registerMethod = m;
//                        break;
//                    }
//                }
//                Class<?> clazzbm = Class.forName("codechicken.microblock.BlockMicroMaterial");
//                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(AsteroidBlocks.blockBasic, 0), "tile.asteroids_block.asteroid_rock_0");
//                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(AsteroidBlocks.blockBasic, 1), "tile.asteroids_block.asteroid_rock_1");
//                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(AsteroidBlocks.blockBasic, 2), "tile.asteroids_block.asteroid_rock_2");
//                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(AsteroidBlocks.blockDenseIce, 0), "tile.dense_ice");
//            }
//        }
//        catch (Exception e)
//        {
//        }
//    }
//
//    private void registerTileEntities()
//    {
//        GameRegistry.registerTileEntity(TileEntityBeamReflector.class, "GC Beam Reflector");
//        GameRegistry.registerTileEntity(TileEntityBeamReceiver.class, "GC Beam Receiver");
//        GameRegistry.registerTileEntity(TileEntityShortRangeTelepad.class, "GC Short Range Telepad");
//        GameRegistry.registerTileEntity(TileEntityTelepadFake.class, "GC Fake Short Range Telepad");
//        GameRegistry.registerTileEntity(TileEntityMinerBaseSingle.class, "GC Astro Miner Base Builder");
//        GameRegistry.registerTileEntity(TileEntityMinerBase.class, "GC Astro Miner Base");
//    }
//
//    @Override
//    public Configuration getConfiguration()
//    {
//        return ConfigManagerPlanets.config;
//    }
//
//    @Override
//    public void syncConfig()
//    {
//        ConfigManagerPlanets.syncConfig(false, false);
//    }
}
