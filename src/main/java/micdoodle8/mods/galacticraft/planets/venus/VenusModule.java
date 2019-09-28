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
import micdoodle8.mods.galacticraft.core.entities.*;
import micdoodle8.mods.galacticraft.core.event.EventHandlerGC;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.items.ItemBucketGC;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.planets.GCPlanetDimensions;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.IPlanetsModule;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockSulphuricAcid;
import micdoodle8.mods.galacticraft.planets.venus.dimension.TeleportTypeVenus;
import micdoodle8.mods.galacticraft.planets.venus.dimension.WorldProviderVenus;
import micdoodle8.mods.galacticraft.planets.venus.entities.EntityEntryPodVenus;
import micdoodle8.mods.galacticraft.planets.venus.entities.EntityJuicer;
import micdoodle8.mods.galacticraft.planets.venus.entities.EntitySpiderQueen;
import micdoodle8.mods.galacticraft.planets.venus.entities.EntityWebShot;
import micdoodle8.mods.galacticraft.planets.venus.event.EventHandlerVenus;
import micdoodle8.mods.galacticraft.planets.venus.inventory.ContainerCrashedProbe;
import micdoodle8.mods.galacticraft.planets.venus.inventory.ContainerGeothermal;
import micdoodle8.mods.galacticraft.planets.venus.inventory.ContainerLaserTurret;
import micdoodle8.mods.galacticraft.planets.venus.inventory.ContainerSolarArrayController;
import micdoodle8.mods.galacticraft.planets.venus.network.PacketSimpleVenus;
import micdoodle8.mods.galacticraft.planets.venus.recipe.RecipeManagerVenus;
import micdoodle8.mods.galacticraft.planets.venus.tick.VenusTickHandlerServer;
import micdoodle8.mods.galacticraft.planets.venus.tile.*;
import micdoodle8.mods.galacticraft.planets.venus.world.gen.BiomeVenus;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class VenusModule implements IPlanetsModule
{
    public static Planet planetVenus;
    public static Fluid sulphuricAcid;
    public static Fluid sulphuricAcidGC;
    public static Material acidMaterial = new MaterialLiquid(MapColor.EMERALD);

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        VenusModule.planetVenus = (Planet) new Planet("venus").setParentSolarSystem(GalacticraftCore.solarSystemSol).setRingColorRGB(0.1F, 0.9F, 0.6F).setPhaseShift(2.0F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(0.75F, 0.75F)).setRelativeOrbitTime(0.61527929901423877327491785323111F);
        MinecraftForge.EVENT_BUS.register(new EventHandlerVenus());

        if (!FluidRegistry.isFluidRegistered("sulphuricacid"))
        {
            ResourceLocation stillIcon = new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "blocks/fluids/sulphuric_acid_still");
            ResourceLocation flowingIcon = new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "blocks/fluids/sulphuric_acid_flow");
            sulphuricAcidGC = new Fluid("sulphuricacid", stillIcon, flowingIcon).setDensity(6229).setViscosity(1400);
            FluidRegistry.registerFluid(sulphuricAcidGC);
        }
        else
        {
            GCLog.info("Galacticraft sulphuric acid is not default, issues may occur.");
        }

        sulphuricAcid = FluidRegistry.getFluid("sulphuricacid");

        if (sulphuricAcid.getBlock() == null)
        {
            VenusBlocks.sulphuricAcid = new BlockSulphuricAcid("sulphuric_acid");
            ((BlockSulphuricAcid) VenusBlocks.sulphuricAcid).setQuantaPerBlock(5);
            VenusBlocks.registerBlock(VenusBlocks.sulphuricAcid, ItemBlockDesc.class);
            sulphuricAcid.setBlock(VenusBlocks.sulphuricAcid);
        }
        else
        {
            VenusBlocks.sulphuricAcid = sulphuricAcid.getBlock();
        }

        if (VenusBlocks.sulphuricAcid != null)
        {
        	FluidRegistry.addBucketForFluid(sulphuricAcid);  //Create a Universal Bucket AS WELL AS our type, this is needed to pull fluids out of other mods tanks
            VenusItems.bucketSulphuricAcid = new ItemBucketGC(VenusBlocks.sulphuricAcid, VenusModule.sulphuricAcid).setUnlocalizedName("bucket_sulphuric_acid");
            VenusItems.registerItem(VenusItems.bucketSulphuricAcid);
            EventHandlerGC.bucketList.put(VenusBlocks.sulphuricAcid, VenusItems.bucketSulphuricAcid);
        }

        VenusBlocks.initBlocks();
        VenusItems.initItems();
        
        VenusModule.planetVenus.setBiomeInfo(BiomeVenus.venusFlat, BiomeVenus.venusMountain, BiomeVenus.venusValley);
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        VenusBlocks.oreDictRegistration();
        this.registerMicroBlocks();

        GalacticraftCore.packetPipeline.addDiscriminator(8, PacketSimpleVenus.class);

        this.registerTileEntities();
        this.registerCreatures();
        this.registerOtherEntities();

        VenusModule.planetVenus.setBodyIcon(new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/celestialbodies/venus.png"));
        VenusModule.planetVenus.setDimensionInfo(ConfigManagerVenus.dimensionIDVenus, WorldProviderVenus.class).setTierRequired(3);
        VenusModule.planetVenus.setAtmosphere(new AtmosphereInfo(false, true, true, 5.0F, 0.3F, 54.0F));
        VenusModule.planetVenus.atmosphereComponent(EnumAtmosphericGas.CO2).atmosphereComponent(EnumAtmosphericGas.NITROGEN);
        VenusModule.planetVenus.addMobInfo(new SpawnListEntry(EntityEvolvedZombie.class, 8, 2, 3));
        VenusModule.planetVenus.addMobInfo(new SpawnListEntry(EntityEvolvedSpider.class, 8, 2, 3));
        VenusModule.planetVenus.addMobInfo(new SpawnListEntry(EntityEvolvedSkeleton.class, 8, 2, 3));
        VenusModule.planetVenus.addMobInfo(new SpawnListEntry(EntityEvolvedCreeper.class, 8, 2, 3));
        VenusModule.planetVenus.addMobInfo(new SpawnListEntry(EntityEvolvedEnderman.class, 10, 1, 4));
        VenusModule.planetVenus.addChecklistKeys("equip_oxygen_suit", "equip_shield_controller", "thermal_padding_t2");

        GalaxyRegistry.registerPlanet(VenusModule.planetVenus);
        GalacticraftRegistry.registerTeleportType(WorldProviderVenus.class, new TeleportTypeVenus());
        GalacticraftRegistry.registerRocketGui(WorldProviderVenus.class, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/venus_rocket_gui.png"));
        GalacticraftRegistry.addDungeonLoot(3, new ItemStack(VenusItems.volcanicPickaxe, 1, 0));
        GalacticraftRegistry.addDungeonLoot(3, new ItemStack(VenusItems.basicItem, 1, 0));

        GalacticraftRegistry.registerGear(Constants.GEAR_ID_SHIELD_CONTROLLER, EnumExtendedInventorySlot.SHIELD_CONTROLLER, new ItemStack(VenusItems.basicItem, 1, 0));
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_THERMAL_PADDING_T2_HELMET, EnumExtendedInventorySlot.THERMAL_HELMET, new ItemStack(VenusItems.thermalPaddingTier2, 1, 0));
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_THERMAL_PADDING_T2_CHESTPLATE, EnumExtendedInventorySlot.THERMAL_CHESTPLATE, new ItemStack(VenusItems.thermalPaddingTier2, 1, 1));
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_THERMAL_PADDING_T2_LEGGINGS, EnumExtendedInventorySlot.THERMAL_LEGGINGS, new ItemStack(VenusItems.thermalPaddingTier2, 1, 2));
        GalacticraftRegistry.registerGear(Constants.GEAR_ID_THERMAL_PADDING_T2_BOOTS, EnumExtendedInventorySlot.THERMAL_BOOTS, new ItemStack(VenusItems.thermalPaddingTier2, 1, 3));

        GalacticraftCore.proxy.registerFluidTexture(VenusModule.sulphuricAcid, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/misc/underacid.png"));

        MinecraftForge.EVENT_BUS.register(new VenusTickHandlerServer());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        RecipeManagerVenus.loadCompatibilityRecipes();

        GCPlanetDimensions.VENUS = WorldUtil.getDimensionTypeById(ConfigManagerVenus.dimensionIDVenus);
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

    public void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntitySpout.class, "GC Venus Spout");
        GameRegistry.registerTileEntity(TileEntityDungeonSpawnerVenus.class, "GC Venus Dungeon Spawner");
        GameRegistry.registerTileEntity(TileEntityTreasureChestVenus.class, "GC Tier 3 Treasure Chest");
        GameRegistry.registerTileEntity(TileEntityGeothermalGenerator.class, "GC Geothermal Generator");
        GameRegistry.registerTileEntity(TileEntityCrashedProbe.class, "GC Crashed Probe");
        GameRegistry.registerTileEntity(TileEntitySolarArrayModule.class, "GC Solar Array Module");
        GameRegistry.registerTileEntity(TileEntitySolarArrayController.class, "GC Solar Array Controller");
        GameRegistry.registerTileEntity(TileEntityLaserTurret.class, "GC Laser Turret");
    }

    public void registerCreatures()
    {
        this.registerGalacticraftCreature(EntityJuicer.class, "juicer", ColorUtil.to32BitColor(180, 180, 50, 0), ColorUtil.to32BitColor(255, 0, 2, 0));
        this.registerGalacticraftCreature(EntitySpiderQueen.class, "spider_queen", ColorUtil.to32BitColor(180, 180, 50, 0), ColorUtil.to32BitColor(255, 0, 2, 0));
    }

    public void registerOtherEntities()
    {
        VenusModule.registerGalacticraftNonMobEntity(EntityEntryPodVenus.class, "entry_pod_venus", 150, 1, true);
        VenusModule.registerGalacticraftNonMobEntity(EntityWebShot.class, "web_shot", 150, 1, true);
    }

    @Override
    public void getGuiIDs(List<Integer> idList)
    {
        idList.add(GuiIdsPlanets.MACHINE_VENUS);
    }

    @Override
    public Object getGuiElement(Side side, int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tile = world.getTileEntity(pos);

        if (ID == GuiIdsPlanets.MACHINE_VENUS)
        {
            if (tile instanceof TileEntityGeothermalGenerator)
            {
                return new ContainerGeothermal(player.inventory, (TileEntityGeothermalGenerator) tile);
            }
            else if (tile instanceof TileEntityCrashedProbe)
            {
                return new ContainerCrashedProbe(player.inventory, (TileEntityCrashedProbe) tile);
            }
            else if (tile instanceof TileEntitySolarArrayController)
            {
                return new ContainerSolarArrayController(player.inventory, (TileEntitySolarArrayController) tile);
            }
            else if (tile instanceof TileEntityLaserTurret)
            {
                return new ContainerLaserTurret(player.inventory, (TileEntityLaserTurret) tile);
            }
        }

        return null;
    }

    @Override
    public Configuration getConfiguration()
    {
        return ConfigManagerVenus.config;
    }

    @Override
    public void syncConfig()
    {
        ConfigManagerVenus.syncConfig(false, false);
    }

    public void registerGalacticraftCreature(Class<? extends Entity> clazz, String name, int back, int fore)
    {
        VenusModule.registerGalacticraftNonMobEntity(clazz, name, 80, 3, true);
        int nextEggID = GCCoreUtil.getNextValidID();
        if (nextEggID < 65536)
        {
            ResourceLocation resourcelocation = new ResourceLocation(Constants.MOD_ID_PLANETS, name);
//            name = Constants.MOD_ID_PLANETS + "." + name;
//            net.minecraftforge.fml.common.registry.EntityEntry entry = new net.minecraftforge.fml.common.registry.EntityEntry(clazz, name);
//            net.minecraftforge.fml.common.registry.GameData.getEntityRegistry().register(nextEggID, resourcelocation, entry);
            EntityList.ENTITY_EGGS.put(resourcelocation, new EntityList.EntityEggInfo(resourcelocation, back, fore));
        }
    }

    public static void registerGalacticraftNonMobEntity(Class<? extends Entity> var0, String var1, int trackingDistance, int updateFreq, boolean sendVel)
    {
        ResourceLocation registryName = new ResourceLocation(Constants.MOD_ID_PLANETS, var1);
        EntityRegistry.registerModEntity(registryName, var0, var1, GCCoreUtil.nextInternalID(), GalacticraftPlanets.instance, trackingDistance, updateFreq, sendVel);
    }
}
