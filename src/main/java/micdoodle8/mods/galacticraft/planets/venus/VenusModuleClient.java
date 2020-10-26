package micdoodle8.mods.galacticraft.planets.venus;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.IPlanetsModuleClient;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.client.TickHandlerClientVenus;
import micdoodle8.mods.galacticraft.planets.venus.client.fx.ParticleAcidExhaust;
import micdoodle8.mods.galacticraft.planets.venus.client.fx.ParticleAcidVapor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static micdoodle8.mods.galacticraft.planets.venus.client.fx.VenusParticles.ACID_EXHAUST;
import static micdoodle8.mods.galacticraft.planets.venus.client.fx.VenusParticles.ACID_VAPOR;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
public class VenusModuleClient implements IPlanetsModuleClient
{
    private static final ModelResourceLocation sulphuricAcidLocation = new ModelResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "sulphuric_acid", "fluid");

    @Override
    public void init(FMLCommonSetupEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
//        RenderingRegistry.registerEntityRenderingHandler(EntityJuicer.class, (EntityRendererManager manager) -> new RenderJuicer(manager));
//        RenderingRegistry.registerEntityRenderingHandler(EntityEntryPodVenus.class, (EntityRendererManager manager) -> new RenderEntryPodVenus(manager));
//        RenderingRegistry.registerEntityRenderingHandler(EntitySpiderQueen.class, (EntityRendererManager manager) -> new RenderSpiderQueen(manager));
//        RenderingRegistry.registerEntityRenderingHandler(EntityWebShot.class, (EntityRendererManager manager) -> new RenderWebShot(manager));

        MinecraftForge.EVENT_BUS.register(new TickHandlerClientVenus());
//        VenusModuleClient.registerBlockRenderers();

        RenderType cutout = RenderType.getCutout();
        RenderTypeLookup.setRenderLayer(VenusBlocks.torchWebLight, cutout);
        RenderTypeLookup.setRenderLayer(VenusBlocks.torchWebSupport, cutout);

//        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTreasureChestVenus.class, new TileEntityTreasureChestRenderer());
//        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLaserTurret.class, new TileEntityLaserTurretRenderer());
    }

//    private void addPlanetVariants(String name, String... variants)
//    {
//        Item itemBlockVariants = Item.REGISTRY.getObject(new ResourceLocation(Constants.MOD_ID_PLANETS, name));
//        ResourceLocation[] variants0 = new ResourceLocation[variants.length];
//        for (int i = 0; i < variants.length; ++i)
//        {
//            variants0[i] = new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + variants[i]);
//        }
//        ModelBakery.registerItemVariants(itemBlockVariants, variants0);
//    }
//
//    @Override
//    public void registerVariants()
//    {
//        addPlanetVariants("venus", "venus_rock_0", "venus_rock_1", "venus_rock_2", "venus_rock_3", "dungeon_brick_venus_1", "dungeon_brick_venus_2", "venus_ore_aluminum", "venus_ore_copper", "venus_ore_galena", "venus_ore_quartz", "venus_ore_silicon", "venus_ore_tin", "lead_block", "venus_ore_solar");
//        addPlanetVariants("thermal_padding_t2", "thermal_helm_t2", "thermal_chestplate_t2", "thermal_leggings_t2", "thermal_boots_t2");
//        addPlanetVariants("basic_item_venus", "shield_controller", "ingot_lead", "radioisotope_core", "thermal_fabric", "solar_dust", "solar_module_2", "thin_solar_wafer");
//        addPlanetVariants("web_torch", "web_torch_0", "web_torch_1");
//
//        Item sludge = Item.getItemFromBlock(VenusBlocks.sulphuricAcid);
//        ModelBakery.registerItemVariants(sludge, new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "sulphuric_acid"));
//        ModelLoader.setCustomMeshDefinition(sludge, IItemMeshDefinitionCustom.create((ItemStack stack) -> sulphuricAcidLocation));
//        ModelLoader.setCustomStateMapper(VenusBlocks.sulphuricAcid, new StateMapperBase()
//        {
//            @Override
//            protected ModelResourceLocation getModelResourceLocation(BlockState state)
//            {
//                return sulphuricAcidLocation;
//            }
//        });
//    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void loadTextures(TextureStitchEvent.Pre event)
    {
        registerTexture(event, "pod_flame");
        registerTexture(event, "web");
        registerTexture(event, "laser");
        registerTexture(event, "laser_off");
        registerTexture(event, "orb");
    }

    private void registerTexture(TextureStitchEvent.Pre event, String texture)
    {
        event.addSprite(new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "blocks/" + texture));
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onModelBakeEvent(ModelBakeEvent event)
    {
//        RenderEntryPodVenus.updateModels(event.getModelLoader());
//        TileEntityLaserTurretRenderer.updateModels(event.getModelLoader());
    }

//    private void replaceModelDefault(ModelBakeEvent event, String resLoc, String objLoc, List<String> visibleGroups, Class<? extends ModelTransformWrapper> clazz, IModelState parentState, String... variants)
//    {
//        ClientUtil.replaceModel(GalacticraftPlanets.ASSET_PREFIX, event, resLoc, objLoc, visibleGroups, clazz, parentState, variants);
//    }

//    public static void registerBlockRenderers()
//    {
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 0, "venus_rock_0");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 1, "venus_rock_1");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 2, "venus_rock_2");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 3, "venus_rock_3");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 4, "dungeon_brick_venus_1");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 5, "dungeon_brick_venus_2");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 6, "venus_ore_aluminum");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 7, "venus_ore_copper");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 8, "venus_ore_galena");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 9, "venus_ore_quartz");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 10, "venus_ore_silicon");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 11, "venus_ore_tin");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 12, "lead_block");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.venusBlock, 13, "venus_ore_solar");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.spout, 0, "spout");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.treasureChestTier3, 0, "treasure_t3");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.torchWeb, 0, "web_torch_0");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.torchWeb, 1, "web_torch_1");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.geothermalGenerator, 0, "geothermal_generator");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.crashedProbe);
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.scorchedRock);
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.bossSpawner);
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.solarArrayModule);
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.solarArrayController, 0, "solar_array_controller");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusBlocks.laserTurret);
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.thermalPaddingTier2, 0, "thermal_helm_t2");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.thermalPaddingTier2, 1, "thermal_chestplate_t2");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.thermalPaddingTier2, 2, "thermal_leggings_t2");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.thermalPaddingTier2, 3, "thermal_boots_t2");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.basicItem, 0, "shield_controller");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.basicItem, 1, "ingot_lead");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.basicItem, 2, "radioisotope_core");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.basicItem, 3, "thermal_fabric");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.basicItem, 4, "solar_dust");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.basicItem, 5, "solar_module_2");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.basicItem, 6, "thin_solar_wafer");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, VenusItems.key, 0, "key_t3");
//    }

//    @Override
//    public Object getGuiElement(LogicalSide LogicalSide, int ID, PlayerEntity player, World world, int x, int y, int z)
//    {
//        if (LogicalSide == LogicalSide.CLIENT)
//        {
//            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
//
//            if (ID == GuiIdsPlanets.MACHINE_VENUS)
//            {
//                if (tile instanceof TileEntityGeothermalGenerator)
//                {
//                    return new GuiGeothermal(player.inventory, (TileEntityGeothermalGenerator) tile);
//                }
//                else if (tile instanceof TileEntityCrashedProbe)
//                {
//                    return new GuiCrashedProbe(player.inventory, (TileEntityCrashedProbe) tile);
//                }
//                else if (tile instanceof TileEntitySolarArrayController)
//                {
//                    return new GuiSolarArrayController(player.inventory, (TileEntitySolarArrayController) tile);
//                }
//                else if (tile instanceof TileEntityLaserTurret)
//                {
//                    return new GuiLaserTurret(player.inventory, (TileEntityLaserTurret) tile);
//                }
//            }
//        }
//
//        return null;
//    }

//    @Override
//    public void addParticle(String particleID, Vector3 position, Vector3 motion, Object... extraData)
//    {
//        Minecraft mc = Minecraft.getInstance();
//
//        if (mc != null && mc.getRenderViewEntity() != null && mc.particles != null)
//        {
//            double dX = mc.getRenderViewEntity().posX - position.x;
//            double dY = mc.getRenderViewEntity().posY - position.y;
//            double dZ = mc.getRenderViewEntity().posZ - position.z;
//            Particle particle = null;
//            double viewDistance = 64.0D;
//
//            if (particleID.equals("acidVapor"))
//            {
//                particle = new ParticleAcidVapor(mc.world, position.x, position.y, position.z, motion.x, motion.y, motion.z, 2.5F);
//            }
//
//            if (dX * dX + dY * dY + dZ * dZ < viewDistance * viewDistance)
//            {
//                if (particleID.equals("acidExhaust"))
//                {
//                    particle = new ParticleAcidExhaust(mc.world, position.x, position.y, position.z, motion.x, motion.y, motion.z, 0.5F);
//                }
//            }
//
//            if (particle != null)
//            {
//                mc.particles.addEffect(particle);
//            }
//        }
//    }

//    @Override
//    public void getGuiIDs(List<Integer> idList)
//    {
//        idList.add(GuiIdsPlanets.MACHINE_VENUS);
//    }

    @SubscribeEvent
    public static void registerParticleFactories(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particles.registerFactory(ACID_EXHAUST, ParticleAcidExhaust.Factory::new);
        Minecraft.getInstance().particles.registerFactory(ACID_VAPOR, ParticleAcidVapor.Factory::new);
    }
}
