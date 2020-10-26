package micdoodle8.mods.galacticraft.planets.mars;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.CloudRenderer;
import micdoodle8.mods.galacticraft.core.client.render.item.ItemModelRocket;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.IPlanetsModuleClient;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.item.ItemModelCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.client.fx.EntityCryoFX;
import micdoodle8.mods.galacticraft.planets.mars.client.fx.ParticleDrip;
import micdoodle8.mods.galacticraft.planets.mars.client.gui.GuiSlimeling;
import micdoodle8.mods.galacticraft.planets.mars.client.gui.GuiSlimelingFeed;
import micdoodle8.mods.galacticraft.planets.mars.client.render.item.ItemModelRocketT2;
import micdoodle8.mods.galacticraft.planets.mars.dimension.DimensionMars;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.items.ItemSchematicTier2;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static micdoodle8.mods.galacticraft.planets.mars.client.fx.MarsParticles.CRYO;
import static micdoodle8.mods.galacticraft.planets.mars.client.fx.MarsParticles.DRIP;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MarsModuleClient implements IPlanetsModuleClient
{
    private static final ModelResourceLocation sludgeLocation = new ModelResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "sludge", "fluid");

//    @Override
//    public void preInit(FMLPreInitializationEvent event)
//    {
//    }

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

//    @Override
//    public void registerVariants()
//    {
//        addPlanetVariants("mars", "ore_copper_mars", "ore_tin_mars", "ore_desh_mars", "ore_iron_mars", "cobblestone", "mars_surface", "mars_middle", "dungeon_brick", "desh_block", "mars_stone");
//        addPlanetVariants("cavern_vines", "vine_0", "vine_1", "vine_2");
//        addPlanetVariants("item_basic_mars", "raw_desh", "desh_stick", "ingot_desh", "reinforced_plate_t2", "slimeling_cargo", "compressed_desh", "fluid_manip");
//        addPlanetVariants("schematic", "schematic_rocket_t3", "schematic_rocket_cargo", "schematic_astro_miner");
//        addPlanetVariants("slimeling_egg", "slimeling_egg_red", "slimeling_egg_blue", "slimeling_egg_yellow");
//        addPlanetVariants("mars_machine", "terraformer", "cryogenic_chamber", "launch_controller");
//        addPlanetVariants("mars_machine_t2", "gas_liquefier", "methane_synthesizer", "electrolyzer");
//
//        Item sludge = Item.getItemFromBlock(MarsBlocks.blockSludge);
//        ModelBakery.registerItemVariants(sludge, new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "sludge"));
//        ModelLoader.setCustomMeshDefinition(sludge, IItemMeshDefinitionCustom.create((ItemStack stack) -> sludgeLocation));
//        ModelLoader.setCustomStateMapper(MarsBlocks.blockSludge, new StateMapperBase()
//        {
//            @Override
//            protected ModelResourceLocation getModelResourceLocation(BlockState state)
//            {
//                return sludgeLocation;
//            }
//        });
//
//        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "rocket_t2", "inventory");
//        for (int i = 0; i < 5; ++i)
//        {
//            ModelLoader.setCustomModelResourceLocation(MarsItems.rocketMars, i, modelResourceLocation);
//        }
//
//        modelResourceLocation = new ModelResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "rocket_cargo", "inventory");
//        for (int i = 11; i < 15; ++i)
//        {
//            ModelLoader.setCustomModelResourceLocation(MarsItems.rocketMars, i, modelResourceLocation);
//        }
//    }


    @Override
    public void init(FMLCommonSetupEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);

//        RenderingRegistry.registerEntityRenderingHandler(EntitySludgeling.class, (EntityRendererManager manager) -> new RenderSludgeling(manager));
//        RenderingRegistry.registerEntityRenderingHandler(EntitySlimeling.class, (EntityRendererManager manager) -> new RenderSlimeling(manager));
//        RenderingRegistry.registerEntityRenderingHandler(EntityCreeperBoss.class, (EntityRendererManager manager) -> new RenderCreeperBoss(manager));
//        RenderingRegistry.registerEntityRenderingHandler(EntityProjectileTNT.class, (EntityRendererManager manager) -> new RenderProjectileTNT(manager));
//        RenderingRegistry.registerEntityRenderingHandler(EntityCargoRocket.class, (EntityRendererManager manager) -> new RenderCargoRocket(manager));
//        RenderingRegistry.registerEntityRenderingHandler(EntityLandingBalloons.class, (EntityRendererManager manager) -> new RenderLandingBalloons(manager));
//        RenderingRegistry.registerEntityRenderingHandler(EntityTier2Rocket.class, (EntityRendererManager manager) -> new RenderTier2Rocket(manager));

        // ==============================

        MinecraftForge.EVENT_BUS.register(new TickHandlerClient());
//        MarsModuleClient.registerBlockRenderers();

        // ==============================

        RenderType cutout = RenderType.getCutout();
        RenderTypeLookup.setRenderLayer(MarsBlocks.vine, cutout);

//            IModelCustom chamberModel = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/chamber.obj"));
//            IModelCustom cargoRocketModel = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/cargoRocket.obj"));
//
//        // Tile Entity Renderers
//        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTreasureChestMars.class, new TileEntityTreasureChestRenderer());
//            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCryogenicChamber.class, new TileEntityCryogenicChamberRenderer(chamberModel));
//        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTerraformer.class, new TileEntityBubbleProviderRenderer<>(0.25F, 1.0F, 0.25F));
//
//        // Entities

        // Add Armor Renderer Prefix
//        RenderingRegistry.addNewArmourRendererPrefix("desh");

        ItemSchematicTier2.registerTextures();

        ClientProxyCore.setCustomModel(MarsItems.rocketTierTwo.getRegistryName(), ItemModelRocketT2::new);
        ClientProxyCore.setCustomModel(MarsItems.rocketTierTwoCargo1.getRegistryName(), ItemModelRocketT2::new);
        ClientProxyCore.setCustomModel(MarsItems.rocketTierTwoCargo2.getRegistryName(), ItemModelRocketT2::new);
        ClientProxyCore.setCustomModel(MarsItems.rocketTierTwoCargo3.getRegistryName(), ItemModelRocketT2::new);
        ClientProxyCore.setCustomModel(MarsItems.rocketTierTwoCreative.getRegistryName(), ItemModelRocketT2::new);
        ClientProxyCore.setCustomModel(MarsItems.rocketCargo1.getRegistryName(), ItemModelCargoRocket::new);
        ClientProxyCore.setCustomModel(MarsItems.rocketCargo2.getRegistryName(), ItemModelCargoRocket::new);
        ClientProxyCore.setCustomModel(MarsItems.rocketCargo3.getRegistryName(), ItemModelCargoRocket::new);
        ClientProxyCore.setCustomModel(MarsItems.rocketCargoCreative.getRegistryName(), ItemModelCargoRocket::new);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void loadTextures(TextureStitchEvent.Pre event)
    {
        registerTexture(event, "rocket_t2");
        registerTexture(event, "cargo_rocket");
        registerTexture(event, "landing_balloon");
    }

    private void registerTexture(TextureStitchEvent.Pre event, String texture)
    {
        event.addSprite(new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "blocks/" + texture));
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onModelBakeEvent(ModelBakeEvent event)
    {
//        replaceModelDefault(event, "rocket_t2", "rocket_t2.obj", ImmutableList.of("Rocket"), ItemModelRocketT2.class, TRSRTransformation.identity());
//        replaceModelDefault(event, "rocket_cargo", "cargo_rocket.obj", ImmutableList.of("Rocket"), ItemModelCargoRocket.class, TRSRTransformation.identity());

//        RenderLandingBalloons.updateModels(event.getModelLoader());
    }

    @SubscribeEvent
    public static void registerParticleFactories(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particles.registerFactory(DRIP, ParticleDrip.Factory::new);
        Minecraft.getInstance().particles.registerFactory(CRYO, EntityCryoFX.Factory::new);
    }

//    private void replaceModelDefault(ModelBakeEvent event, String resLoc, String objLoc, List<String> visibleGroups, Class<? extends ModelTransformWrapper> clazz, IModelState parentState, String... variants)
//    {
//        ClientUtil.replaceModel(GalacticraftPlanets.ASSET_PREFIX, event, resLoc, objLoc, visibleGroups, clazz, parentState, variants);
//    }

//    public static void registerBlockRenderers()
//    {
//        for (BlockBasicMars.EnumBlockBasic blockBasic : BlockBasicMars.EnumBlockBasic.values())
//        {
//            ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.marsBlock, blockBasic.getMeta(), blockBasic.getName());
//        }
//
//        for (BlockCavernousVine.EnumVineType vineType : BlockCavernousVine.EnumVineType.values())
//        {
//            ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.vine, vineType.getMeta(), vineType.getName());
//        }
//
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.rock, 0, "slimeling_egg_red");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.rock, 1, "slimeling_egg_blue");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.rock, 2, "slimeling_egg_yellow");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.creeperEgg);
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.machine, 0, "terraformer");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.machine, 4, "cryogenic_chamber");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.machine, 8, "launch_controller");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.machineT2, 0, "gas_liquefier");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.machineT2, 4, "methane_synthesizer");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.machineT2, 8, "electrolyzer");
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.treasureChestTier2);
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.marsBricksStairs);
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.marsCobblestoneStairs);
//        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.bossSpawner);
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.marsItemBasic, 0, "raw_desh");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.marsItemBasic, 1, "desh_stick");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.marsItemBasic, 2, "ingot_desh");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.marsItemBasic, 3, "reinforced_plate_t2");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.marsItemBasic, 4, "slimeling_cargo");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.marsItemBasic, 5, "compressed_desh");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.marsItemBasic, 6, "fluid_manip");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.key, 0, "key");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.schematic, 0, "schematic_rocket_t3");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.schematic, 1, "schematic_rocket_cargo");
//        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.schematic, 2, "schematic_astro_miner");
//    }

//    @Override
//    public Object getGuiElement(LogicalSide LogicalSide, int ID, PlayerEntity player, World world, int x, int y, int z)
//    {
//        if (LogicalSide == LogicalSide.CLIENT)
//        {
//            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
//
//            if (ID == GuiIdsPlanets.MACHINE_MARS)
//            {
//                if (tile instanceof TileEntityTerraformer)
//                {
//                    return new GuiTerraformer(player.inventory, (TileEntityTerraformer) tile);
//                }
//                else if (tile instanceof TileEntityLaunchController)
//                {
//                    return new GuiLaunchController(player.inventory, (TileEntityLaunchController) tile);
//                }
//                else if (tile instanceof TileEntityElectrolyzer)
//                {
//                    return new GuiWaterElectrolyzer(player.inventory, (TileEntityElectrolyzer) tile);
//                }
//                else if (tile instanceof TileEntityGasLiquefier)
//                {
//                    return new GuiGasLiquefier(player.inventory, (TileEntityGasLiquefier) tile);
//                }
//                else if (tile instanceof TileEntityMethaneSynthesizer)
//                {
//                    return new GuiMethaneSynthesizer(player.inventory, (TileEntityMethaneSynthesizer) tile);
//                }
//            }
//        }
//
//        return null;
//    }
//
//    @Override
//    public void addParticle(String particleID, Vector3 position, Vector3 motion, Object... extraData)
//    {
//        Minecraft mc = Minecraft.getInstance();
//
//        if (mc != null && mc.getRenderViewEntity() != null && mc.particles != null)
//        {
//            final double dPosX = mc.getRenderViewEntity().posX - position.x;
//            final double dPosY = mc.getRenderViewEntity().posY - position.y;
//            final double dPosZ = mc.getRenderViewEntity().posZ - position.z;
//            Particle particle = null;
//            final double maxDistSqrd = 64.0D;
//
//            if (dPosX * dPosX + dPosY * dPosY + dPosZ * dPosZ < maxDistSqrd * maxDistSqrd)
//            {
//                if (particleID.equals("sludgeDrip"))
//                {
////                    particle = new EntityDropParticleFX(mc.world, position.x, position.y, position.z, Material.WATER); TODO
//                }
//                else if (particleID.equals("bacterialDrip"))
//                {
//                    particle = new ParticleDrip(mc.world, position.x, position.y, position.z);
//                }
//            }
//
//            if (particle != null)
//            {
//                mc.particles.addEffect(particle);
//            }
//        }
//    }
//
//    @Override
//    public void getGuiIDs(List<Integer> idList)
//    {
//        idList.add(GuiIdsPlanets.MACHINE_MARS);
//    }

    public static void openSlimelingGui(EntitySlimeling slimeling, int gui)
    {
        switch (gui)
        {
        case 0:
            Minecraft.getInstance().displayGuiScreen(new GuiSlimeling(slimeling));
            break;
        case 1:
            Minecraft.getInstance().displayGuiScreen(new GuiSlimelingFeed(slimeling));
            break;
        }
    }

    public static class TickHandlerClient
    {
        @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
        public void onClientTick(TickEvent.ClientTickEvent event)
        {
            final Minecraft minecraft = Minecraft.getInstance();

            final ClientWorld world = minecraft.world;

            if (world != null)
            {
                if (world.getDimension() instanceof DimensionMars)
                {
//                    if (world.getDimension().getSkyRenderer() == null)
//                    {
//                        world.getDimension().setSkyRenderer(new SkyProviderMars((IGalacticraftDimension) world.getDimension()));
//                    } TODO Sky providers

                    if (world.getDimension().getCloudRenderer() == null)
                    {
                        world.getDimension().setCloudRenderer(new CloudRenderer());
                    }
                }
            }
        }
    }
}
