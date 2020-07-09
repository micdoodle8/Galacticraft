package micdoodle8.mods.galacticraft.planets.venus;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.item.EnumExtendedInventorySlot;
import micdoodle8.mods.galacticraft.api.world.AtmosphereInfo;
import micdoodle8.mods.galacticraft.api.world.EnumAtmosphericGas;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.GCEntities;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.IPlanetsModule;
import micdoodle8.mods.galacticraft.planets.PlanetDimensions;
import micdoodle8.mods.galacticraft.planets.PlanetFluids;
import micdoodle8.mods.galacticraft.planets.venus.dimension.TeleportTypeVenus;
import micdoodle8.mods.galacticraft.planets.venus.dimension.DimensionVenus;
import micdoodle8.mods.galacticraft.planets.venus.event.EventHandlerVenus;
import micdoodle8.mods.galacticraft.planets.venus.items.VenusItems;
import micdoodle8.mods.galacticraft.planets.venus.tick.VenusTickHandlerServer;
import micdoodle8.mods.galacticraft.planets.venus.dimension.biome.BiomeVenus;
import net.minecraft.entity.EntityClassification;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class VenusModule implements IPlanetsModule
{
    public static Planet planetVenus;
//    public static Fluid sulphuricAcid;
//    public static Fluid sulphuricAcidGC;

    @Override
    public void init(FMLCommonSetupEvent event)
    {
        VenusModule.planetVenus = (Planet) new Planet("venus").setParentSolarSystem(GalacticraftCore.solarSystemSol).setRingColorRGB(0.1F, 0.9F, 0.6F).setPhaseShift(2.0F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(0.75F, 0.75F)).setRelativeOrbitTime(0.61527929901423877327491785323111F);
        MinecraftForge.EVENT_BUS.register(new EventHandlerVenus());

//        if (!FluidRegistry.isFluidRegistered("sulphuricacid"))
//        {
//            ResourceLocation stillIcon = new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "blocks/fluids/sulphuric_acid_still");
//            ResourceLocation flowingIcon = new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "blocks/fluids/sulphuric_acid_flow");
//            sulphuricAcidGC = new Fluid("sulphuricacid", stillIcon, flowingIcon).setDensity(6229).setViscosity(1400);
//            FluidRegistry.registerFluid(sulphuricAcidGC);
//        }
//        else
//        {
//            GCLog.info("Galacticraft sulphuric acid is not default, issues may occur.");
//        }
//
//        sulphuricAcid = FluidRegistry.getFluid("sulphuricacid");
//
//        if (sulphuricAcid.getBlock() == null)
//        {
//            VenusBlocks.sulphuricAcid = new BlockSulphuricAcid("sulphuric_acid");
//            ((BlockSulphuricAcid) VenusBlocks.sulphuricAcid).setQuantaPerBlock(5);
//            VenusBlocks.registerBlock(VenusBlocks.sulphuricAcid, ItemBlockDesc.class);
//            sulphuricAcid.setBlock(VenusBlocks.sulphuricAcid);
//        }
//        else
//        {
//            VenusBlocks.sulphuricAcid = sulphuricAcid.getBlock();
//        }
//
//        if (VenusBlocks.sulphuricAcid != null)
//        {
//        	FluidRegistry.addBucketForFluid(sulphuricAcid);  //Create a Universal Bucket AS WELL AS our type, this is needed to pull fluids out of other mods tanks
//            VenusItems.bucketSulphuricAcid = new ItemBucketGC(VenusBlocks.sulphuricAcid, VenusModule.sulphuricAcid).setUnlocalizedName("bucket_sulphuric_acid");
//            VenusItems.registerItem(VenusItems.bucketSulphuricAcid);
//            EventHandlerGC.bucketList.put(VenusBlocks.sulphuricAcid, VenusItems.bucketSulphuricAcid);
//        }

//        VenusBlocks.initBlocks();
//        VenusItems.initItems();

        VenusModule.planetVenus.setBiomeInfo(BiomeVenus.venusFlat, BiomeVenus.venusMountain, BiomeVenus.venusValley);

        // ========================================

//        VenusBlocks.oreDictRegistration();
        this.registerMicroBlocks();

//        GalacticraftCore.packetPipeline.addDiscriminator(8, PacketSimpleVenus.class);

//        this.registerTileEntities();
//        this.registerCreatures();
//        this.registerOtherEntities();

        VenusModule.planetVenus.setBodyIcon(new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/celestialbodies/venus.png"));
        VenusModule.planetVenus.setDimensionInfo(PlanetDimensions.VENUS_DIMENSION, DimensionVenus.class).setTierRequired(3);
        VenusModule.planetVenus.setAtmosphere(new AtmosphereInfo(false, true, true, 5.0F, 0.3F, 54.0F));
        VenusModule.planetVenus.atmosphereComponent(EnumAtmosphericGas.CO2).atmosphereComponent(EnumAtmosphericGas.NITROGEN);
        VenusModule.planetVenus.addMobInfo(new SpawnListEntry(GCEntities.EVOLVED_ZOMBIE.get(), 8, 2, 3), EntityClassification.MONSTER);
        VenusModule.planetVenus.addMobInfo(new SpawnListEntry(GCEntities.EVOLVED_SPIDER.get(), 8, 2, 3), EntityClassification.MONSTER);
        VenusModule.planetVenus.addMobInfo(new SpawnListEntry(GCEntities.EVOLVED_SKELETON.get(), 8, 2, 3), EntityClassification.MONSTER);
        VenusModule.planetVenus.addMobInfo(new SpawnListEntry(GCEntities.EVOLVED_CREEPER.get(), 8, 2, 3), EntityClassification.MONSTER);
        VenusModule.planetVenus.addMobInfo(new SpawnListEntry(GCEntities.EVOLVED_ENDERMAN.get(), 10, 1, 4), EntityClassification.MONSTER);
        VenusModule.planetVenus.addChecklistKeys("equip_oxygen_suit", "equip_shield_controller", "thermal_padding_t2");

        GalaxyRegistry.registerPlanet(VenusModule.planetVenus);
        GalacticraftRegistry.registerTeleportType(DimensionVenus.class, new TeleportTypeVenus());
        GalacticraftRegistry.registerRocketGui(DimensionVenus.class, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/venus_rocket_gui.png"));
        GalacticraftRegistry.addDungeonLoot(3, new ItemStack(VenusItems.volcanicPickaxe, 1));
        GalacticraftRegistry.addDungeonLoot(3, new ItemStack(VenusItems.shieldController, 1));

        GalacticraftRegistry.registerGear(Constants.GEAR_ID_SHIELD_CONTROLLER, EnumExtendedInventorySlot.SHIELD_CONTROLLER, new ItemStack(VenusItems.shieldController));
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_THERMAL_PADDING_T2_HELMET, EnumExtendedInventorySlot.THERMAL_HELMET, new ItemStack(VenusItems.thermal_helmet_t2));
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_THERMAL_PADDING_T2_CHESTPLATE, EnumExtendedInventorySlot.THERMAL_CHESTPLATE, new ItemStack(VenusItems.thermal_chestplate_t2));
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_THERMAL_PADDING_T2_LEGGINGS, EnumExtendedInventorySlot.THERMAL_LEGGINGS, new ItemStack(VenusItems.thermal_leggings_t2));
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_THERMAL_PADDING_T2_BOOTS, EnumExtendedInventorySlot.THERMAL_BOOTS, new ItemStack(VenusItems.thermal_boots_t2));

        GalacticraftCore.proxy.registerFluidTexture(PlanetFluids.LIQUID_SULPHURIC_ACID.getFluid(), new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/misc/underacid.png"));

        MinecraftForge.EVENT_BUS.register(new VenusTickHandlerServer());

        // ========================================

//        RecipeManagerVenus.loadCompatibilityRecipes();

//        GCPlanetDimensions.VENUS = WorldUtil.getDimensionTypeById(ConfigManagerPlanets.dimensionIDVenus);
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event)
    {
    }

    @Override
    public void serverInit(FMLServerStartedEvent event)
    {

    }

    private void registerMicroBlocks()
    {
    }

//    public void registerTileEntities()
//    {
//        GameRegistry.registerTileEntity(TileEntitySpout.class, "GC Venus Spout");
//        GameRegistry.registerTileEntity(TileEntityDungeonSpawnerVenus.class, "GC Venus Dungeon Spawner");
//        GameRegistry.registerTileEntity(TileEntityTreasureChestVenus.class, "GC Tier 3 Treasure Chest");
//        GameRegistry.registerTileEntity(TileEntityGeothermalGenerator.class, "GC Geothermal Generator");
//        GameRegistry.registerTileEntity(TileEntityCrashedProbe.class, "GC Crashed Probe");
//        GameRegistry.registerTileEntity(TileEntitySolarArrayModule.class, "GC Solar Array Module");
//        GameRegistry.registerTileEntity(TileEntitySolarArrayController.class, "GC Solar Array Controller");
//        GameRegistry.registerTileEntity(TileEntityLaserTurret.class, "GC Laser Turret");
//    }

//    public void registerCreatures()
//    {
//        this.registerGalacticraftCreature(EntityJuicer.class, "juicer", ColorUtil.to32BitColor(180, 180, 50, 0), ColorUtil.to32BitColor(255, 0, 2, 0));
//        this.registerGalacticraftCreature(EntitySpiderQueen.class, "spider_queen", ColorUtil.to32BitColor(180, 180, 50, 0), ColorUtil.to32BitColor(255, 0, 2, 0));
//    }
//
//    public void registerOtherEntities()
//    {
//        VenusModule.registerGalacticraftNonMobEntity(EntityEntryPodVenus.class, "entry_pod_venus", 150, 1, true);
//        VenusModule.registerGalacticraftNonMobEntity(EntityWebShot.class, "web_shot", 150, 1, true);
//    }

//    @Override
//    public void getGuiIDs(List<Integer> idList)
//    {
//        idList.add(GuiIdsPlanets.MACHINE_VENUS);
//    }
//
//    @Override
//    public Object getGuiElement(LogicalSide LogicalSide, int ID, PlayerEntity player, World world, int x, int y, int z)
//    {
//        BlockPos pos = new BlockPos(x, y, z);
//        TileEntity tile = world.getTileEntity(pos);
//
//        if (ID == GuiIdsPlanets.MACHINE_VENUS)
//        {
//            if (tile instanceof TileEntityGeothermalGenerator)
//            {
//                return new ContainerGeothermal(player.inventory, (TileEntityGeothermalGenerator) tile);
//            }
//            else if (tile instanceof TileEntityCrashedProbe)
//            {
//                return new ContainerCrashedProbe(player.inventory, (TileEntityCrashedProbe) tile);
//            }
//            else if (tile instanceof TileEntitySolarArrayController)
//            {
//                return new ContainerSolarArrayController(player.inventory, (TileEntitySolarArrayController) tile);
//            }
//            else if (tile instanceof TileEntityLaserTurret)
//            {
//                return new ContainerLaserTurret(player.inventory, (TileEntityLaserTurret) tile);
//            }
//        }
//
//        return null;
//    }
}
