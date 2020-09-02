package micdoodle8.mods.galacticraft.planets.mars;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.world.AtmosphereInfo;
import micdoodle8.mods.galacticraft.api.world.EnumAtmosphericGas;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.GCEntities;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.IPlanetsModule;
import micdoodle8.mods.galacticraft.planets.PlanetDimensions;
import micdoodle8.mods.galacticraft.planets.PlanetFluids;
import micdoodle8.mods.galacticraft.planets.mars.dimension.TeleportTypeMars;
import micdoodle8.mods.galacticraft.planets.mars.dimension.DimensionMars;
import micdoodle8.mods.galacticraft.planets.mars.items.ItemSchematicTier2;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.schematic.SchematicCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.schematic.SchematicTier2Rocket;
import micdoodle8.mods.galacticraft.planets.mars.dimension.biome.BiomeMars;
import net.minecraft.entity.EntityClassification;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class MarsModule implements IPlanetsModule
{
//    public static Fluid sludge;
//    public static Fluid sludgeGC;
//    public static Material sludgeMaterial = new MaterialLiquid(MapColor.FOLIAGE);

    public static Planet planetMars;

    @Override
    public void init(FMLCommonSetupEvent event)
    {
        MarsModule.planetMars = (Planet) new Planet("mars").setParentSolarSystem(GalacticraftCore.solarSystemSol).setRingColorRGB(0.67F, 0.1F, 0.1F).setPhaseShift(0.1667F).setRelativeSize(0.5319F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(1.25F, 1.25F)).setRelativeOrbitTime(1.8811610076670317634173055859803F);

        MinecraftForge.EVENT_BUS.register(new EventHandlerMars());

//        if (!FluidRegistry.isFluidRegistered("bacterialsludge"))
//        {
//            ResourceLocation stillIcon = new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "blocks/fluids/sludge_still");
//            ResourceLocation flowingIcon = new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "blocks/fluids/sludge_flow");
//            sludgeGC = new Fluid("bacterialsludge", stillIcon, flowingIcon).setDensity(800).setViscosity(1500);
//            FluidRegistry.registerFluid(sludgeGC);
//        }
//        else
//        {
//            GCLog.info("Galacticraft sludge is not default, issues may occur.");
//        }
//
//        sludge = FluidRegistry.getFluid("bacterialsludge");
//
//        if (sludge.getBlock() == null)
//        {
//            MarsBlocks.blockSludge = new BlockSludge("sludge");
//            ((BlockSludge) MarsBlocks.blockSludge).setQuantaPerBlock(3);
//            MarsBlocks.registerBlock(MarsBlocks.blockSludge, ItemBlockDesc.class);
//            sludge.setBlock(MarsBlocks.blockSludge);
//        }
//        else
//        {
//            MarsBlocks.blockSludge = sludge.getBlock();
//        }
//
//        if (MarsBlocks.blockSludge != null)
//        {
//        	FluidRegistry.addBucketForFluid(sludge);  //Create a Universal Bucket AS WELL AS our type, this is needed to pull fluids out of other mods tanks
//            MarsItems.bucketSludge = new ItemBucketGC(MarsBlocks.blockSludge, sludge).setUnlocalizedName("bucket_sludge");
//            MarsItems.registerItem(MarsItems.bucketSludge);
//            EventHandlerGC.bucketList.put(MarsBlocks.blockSludge, MarsItems.bucketSludge);
//        }

        MarsModule.planetMars.setBiomeInfo(BiomeMars.marsFlat);

        // ============================

        SchematicRegistry.registerSchematicRecipe(new SchematicTier2Rocket());
        SchematicRegistry.registerSchematicRecipe(new SchematicCargoRocket());

//        GalacticraftCore.packetPipeline.addDiscriminator(6, PacketSimpleMars.class);

        MarsModule.planetMars.setBodyIcon(new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/celestialbodies/mars.png"));
        MarsModule.planetMars.setDimensionInfo(PlanetDimensions.MARS_DIMENSION, DimensionMars.class).setTierRequired(2);
        MarsModule.planetMars.setAtmosphere(new AtmosphereInfo(false, false, false, -1.0F, 0.3F, 0.1F));
        MarsModule.planetMars.atmosphereComponent(EnumAtmosphericGas.CO2).atmosphereComponent(EnumAtmosphericGas.ARGON).atmosphereComponent(EnumAtmosphericGas.NITROGEN);
        MarsModule.planetMars.addMobInfo(new Biome.SpawnListEntry(GCEntities.EVOLVED_ZOMBIE.get(), 8, 2, 3), EntityClassification.MONSTER);
        MarsModule.planetMars.addMobInfo(new Biome.SpawnListEntry(GCEntities.EVOLVED_SPIDER.get(), 8, 2, 3), EntityClassification.MONSTER);
        MarsModule.planetMars.addMobInfo(new Biome.SpawnListEntry(GCEntities.EVOLVED_SKELETON.get(), 8, 2, 3), EntityClassification.MONSTER);
        MarsModule.planetMars.addMobInfo(new Biome.SpawnListEntry(GCEntities.EVOLVED_CREEPER.get(), 8, 2, 3), EntityClassification.MONSTER);
        MarsModule.planetMars.addMobInfo(new Biome.SpawnListEntry(GCEntities.EVOLVED_ENDERMAN.get(), 10, 1, 4), EntityClassification.MONSTER);
        MarsModule.planetMars.addChecklistKeys("equip_oxygen_suit", "thermal_padding");

        GalaxyRegistry.registerPlanet(MarsModule.planetMars);
        GalacticraftRegistry.registerTeleportType(DimensionMars.class, new TeleportTypeMars());
        GalacticraftRegistry.registerRocketGui(DimensionMars.class, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/mars_rocket_gui.png"));
        GalacticraftRegistry.addDungeonLoot(2, new ItemStack(MarsItems.schematicRocketT3, 1));
        GalacticraftRegistry.addDungeonLoot(2, new ItemStack(MarsItems.schematicAstroMiner, 1));
        GalacticraftRegistry.addDungeonLoot(2, new ItemStack(MarsItems.schematicCargoRocket, 1));

        GalacticraftCore.proxy.registerFluidTexture(PlanetFluids.LIQUID_BACTERIAL_SLUDGE.getFluid(), new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/misc/underbecterial.png"));

        // ============================

//        RecipeManagerMars.loadCompatibilityRecipes();
//        GCPlanetDimensions.MARS = WorldUtil.getDimensionTypeById(ConfigManagerPlanets.dimensionIDMars);
        ItemSchematicTier2.registerSchematicItems();
    }

    @Override
    public void serverInit(FMLServerStartedEvent event)
    {

    }

    @Override
    public void serverStarting(FMLServerStartingEvent event)
    {

    }

    @Override
    public void enqueueIMC(InterModEnqueueEvent event) {

    }

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
//                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(MarsBlocks.marsBlock, 4), "tile.mars.marscobblestone");
//                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(MarsBlocks.marsBlock, 5), "tile.mars.marsgrass");
//                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(MarsBlocks.marsBlock, 6), "tile.mars.marsdirt");
//                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(MarsBlocks.marsBlock, 7), "tile.mars.marsdungeon");
//                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(MarsBlocks.marsBlock, 8), "tile.mars.marsdeco");
//                registerMethod.invoke(null, clazzbm.getConstructor(Block.class, int.class).newInstance(MarsBlocks.marsBlock, 9), "tile.mars.marsstone");
//            }
//        }
//        catch (Exception e)
//        {
//        }
//    } TODO microblocks

//    public void registerCreatures()
//    {
//        this.registerGalacticraftCreature(EntitySludgeling.class, "sludgeling", ColorUtil.to32BitColor(255, 0, 50, 0), ColorUtil.to32BitColor(255, 0, 150, 0));
//        this.registerGalacticraftCreature(EntitySlimeling.class, "slimeling", ColorUtil.to32BitColor(255, 0, 50, 0), ColorUtil.to32BitColor(255, 0, 150, 0));
//        this.registerGalacticraftCreature(EntityCreeperBoss.class, "creeper_boss", ColorUtil.to32BitColor(255, 0, 50, 0), ColorUtil.to32BitColor(255, 0, 150, 0));
//    }
//
//    public void registerOtherEntities()
//    {
//        MarsModule.registerGalacticraftNonMobEntity(EntityTier2Rocket.class, "rocket_t2", 150, 1, false);
//        MarsModule.registerGalacticraftNonMobEntity(EntityProjectileTNT.class, "projectile_tnt", 150, 1, true);
//        MarsModule.registerGalacticraftNonMobEntity(EntityLandingBalloons.class, "landing_balloons", 150, 5, true);
//        MarsModule.registerGalacticraftNonMobEntity(EntityCargoRocket.class, "rocket_cargo", 150, 1, false);
//    }

//    @Override
//    public void getGuiIDs(List<Integer> idList)
//    {
//        idList.add(GuiIdsPlanets.MACHINE_MARS);
//    }
//
//    @Override
//    public Object getGuiElement(LogicalSide LogicalSide, int ID, PlayerEntity player, World world, int x, int y, int z)
//    {
//        if (LogicalSide == LogicalSide.SERVER)
//        {
//            BlockPos pos = new BlockPos(x, y, z);
//            TileEntity tile = world.getTileEntity(pos);
//
//            if (ID == GuiIdsPlanets.MACHINE_MARS)
//            {
//                if (tile instanceof TileEntityTerraformer)
//                {
//                    return new ContainerTerraformer(player.inventory, (TileEntityTerraformer) tile, player);
//                }
//                else if (tile instanceof TileEntityLaunchController)
//                {
//                    return new ContainerLaunchController(player.inventory, (TileEntityLaunchController) tile, player);
//                }
//                else if (tile instanceof TileEntityElectrolyzer)
//                {
//                    return new ContainerElectrolyzer(player.inventory, (TileEntityElectrolyzer) tile, player);
//                }
//                else if (tile instanceof TileEntityGasLiquefier)
//                {
//                    return new ContainerGasLiquefier(player.inventory, (TileEntityGasLiquefier) tile, player);
//                }
//                else if (tile instanceof TileEntityMethaneSynthesizer)
//                {
//                    return new ContainerMethaneSynthesizer(player.inventory, (TileEntityMethaneSynthesizer) tile, player);
//                }
//            }
//        }
//
//        return null;
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
