package micdoodle8.mods.galacticraft.planets.mars;

import com.google.common.collect.ImmutableList;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.CloudRenderer;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderTier1Rocket;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.IPlanetsModuleClient;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.item.ItemModelCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockBasicMars;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockCavernousVine;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.client.SkyProviderMars;
import micdoodle8.mods.galacticraft.planets.mars.client.fx.EntityBacterialDripFX;
import micdoodle8.mods.galacticraft.planets.mars.client.gui.*;
import micdoodle8.mods.galacticraft.planets.mars.client.model.ModelTier2Rocket;
import micdoodle8.mods.galacticraft.planets.mars.client.render.entity.*;
import micdoodle8.mods.galacticraft.planets.mars.client.render.item.ItemModelRocketT2;
import micdoodle8.mods.galacticraft.planets.mars.client.render.tile.TileEntityTreasureChestRenderer;
import micdoodle8.mods.galacticraft.planets.mars.dimension.WorldProviderMars;
import micdoodle8.mods.galacticraft.planets.mars.entities.*;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.tile.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class MarsModuleClient implements IPlanetsModuleClient
{
    private static ModelResourceLocation sludgeLocation = new ModelResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "sludge", "fluid");

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        addPlanetVariants("mars", "ore_copper_mars", "ore_tin_mars", "ore_desh_mars", "ore_iron_mars", "cobblestone", "mars_surface", "mars_middle", "dungeon_brick", "desh_block", "mars_stone", "dungeon_spawner");
        addPlanetVariants("cavernVines", "vine_0", "vine_1", "vine_2");
        addPlanetVariants("itemBasicMars", "rawDesh", "deshStick", "ingotDesh", "reinforcedPlateT2", "slimelingCargo", "compressedDesh", "fluidManip");
        addPlanetVariants("schematic", "schematic_rocketT3", "schematic_rocket_cargo", "schematic_astroMiner");
        addPlanetVariants("slimelingEgg", "slimelingEggRed", "slimelingEggBlue", "slimelingEggYellow");
        addPlanetVariants("marsMachine", "terraformer", "cryogenic_chamber", "launch_controller");
        addPlanetVariants("marsMachineT2", "gas_liquefier", "methane_synthesizer", "electrolyzer");
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void addPlanetVariants(String name, String... variants)
    {
        Item itemBlockVariants = GameRegistry.findItem(Constants.MOD_ID_PLANETS, name);
        String[] variants0 = new String[variants.length];
        for (int i = 0; i < variants.length; ++i)
        {
            variants0[i] = GalacticraftPlanets.TEXTURE_PREFIX + variants[i];
        }
        ModelBakery.addVariantName(itemBlockVariants, variants0);
    }

    @Override
    public void registerVariants()
    {
        Item sludge = Item.getItemFromBlock(MarsBlocks.blockSludge);
        ModelBakery.registerItemVariants(sludge);
        ModelBakery.addVariantName(sludge, GalacticraftPlanets.TEXTURE_PREFIX + "sludge");
        ModelLoader.setCustomMeshDefinition(sludge, new ItemMeshDefinition()
        {
            public ModelResourceLocation getModelLocation(ItemStack stack)
            {
                return sludgeLocation;
            }
        });
        ModelLoader.setCustomStateMapper(MarsBlocks.blockSludge, new StateMapperBase()
        {
            protected ModelResourceLocation getModelResourceLocation(IBlockState state)
            {
                return sludgeLocation;
            }
        });

        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "spaceshipTier2", "inventory");
        for (int i = 0; i < 5; ++i)
        {
            ModelLoader.setCustomModelResourceLocation(MarsItems.spaceship, i, modelResourceLocation);
        }

        modelResourceLocation = new ModelResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "cargoRocket", "inventory");
        for (int i = 11; i < 15; ++i)
        {
            ModelLoader.setCustomModelResourceLocation(MarsItems.spaceship, i, modelResourceLocation);
        }
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        MarsModuleClient.registerBlockRenderers();

        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.marsItemBasic, 0, "rawDesh");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.marsItemBasic, 1, "deshStick");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.marsItemBasic, 2, "ingotDesh");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.marsItemBasic, 3, "reinforcedPlateT2");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.marsItemBasic, 4, "slimelingCargo");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.marsItemBasic, 5, "compressedDesh");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.marsItemBasic, 6, "fluidManip");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.key, 0, "key");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.schematic, 0, "schematic_rocketT3");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.schematic, 1, "schematic_rocket_cargo");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsItems.schematic, 2, "schematic_astroMiner");

        MinecraftForge.EVENT_BUS.register(new TickHandlerClient());
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void loadTextures(TextureStitchEvent.Pre event)
    {
        registerTexture(event, "rocketT2");
        registerTexture(event, "cargoRocket");
    }

    private void registerTexture(TextureStitchEvent.Pre event, String texture)
    {
        event.map.registerSprite(new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "blocks/" + texture));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onModelBakeEvent(ModelBakeEvent event)
    {
        replaceModelDefault(event, "spaceshipTier2", "rocketT2.obj", ImmutableList.of("Rocket"), ItemModelRocketT2.class, TRSRTransformation.identity());
        replaceModelDefault(event, "cargoRocket", "cargoRocket.obj", ImmutableList.of("Rocket"), ItemModelCargoRocket.class, TRSRTransformation.identity());
    }

    private void replaceModelDefault(ModelBakeEvent event, String resLoc, String objLoc, List<String> visibleGroups, Class<? extends ModelTransformWrapper> clazz, IModelState parentState)
    {
        ClientUtil.replaceModel(GalacticraftPlanets.ASSET_PREFIX, event, resLoc, objLoc, visibleGroups, clazz, parentState);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
//            IModelCustom chamberModel = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/chamber.obj"));
//            IModelCustom cargoRocketModel = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/cargoRocket.obj"));
//
//        // Tile Entity Renderers
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTreasureChestMars.class, new TileEntityTreasureChestRenderer());
//            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCryogenicChamber.class, new TileEntityCryogenicChamberRenderer(chamberModel));
//        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTerraformer.class, new TileEntityBubbleProviderRenderer(0.25F, 1.0F, 0.25F));
//
//        // Entities
//        RenderingRegistry.registerEntityRenderingHandler(EntitySludgeling.class, new RenderSludgeling());
//        RenderingRegistry.registerEntityRenderingHandler(EntitySlimeling.class, new RenderSlimeling());
//        RenderingRegistry.registerEntityRenderingHandler(EntityCreeperBoss.class, new RenderCreeperBoss());
            RenderingRegistry.registerEntityRenderingHandler(EntityTier2Rocket.class, new RenderTier1Rocket(new ModelTier2Rocket(), GalacticraftPlanets.ASSET_PREFIX, "rocketT2"));
////        RenderingRegistry.registerEntityRenderingHandler(EntityTerraformBubble.class, new RenderBubble(0.25F, 1.0F, 0.25F));
//        RenderingRegistry.registerEntityRenderingHandler(EntityProjectileTNT.class, new RenderProjectileTNT());
//            RenderingRegistry.registerEntityRenderingHandler(EntityLandingBalloons.class, new RenderLandingBalloons());
//            RenderingRegistry.registerEntityRenderingHandler(EntityLandingBalloons.class, new RenderLandingBalloons());
            RenderingRegistry.registerEntityRenderingHandler(EntityCargoRocket.class, new RenderCargoRocket());

        // Add Armor Renderer Prefix
//        RenderingRegistry.addNewArmourRendererPrefix("desh");
    }

    public static void registerBlockRenderers()
    {
        for (BlockBasicMars.EnumBlockBasic blockBasic : BlockBasicMars.EnumBlockBasic.values())
        {
            if (blockBasic != BlockBasicMars.EnumBlockBasic.DUNGEON_SPAWNER)
            {
                ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.marsBlock, blockBasic.getMeta(), blockBasic.getName());
            }
        }

        for (BlockCavernousVine.EnumVineType vineType : BlockCavernousVine.EnumVineType.values())
        {
            ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.vine, vineType.getMeta(), vineType.getName());
        }

        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.rock, 0, "slimelingEggRed");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.rock, 1, "slimelingEggBlue");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.rock, 2, "slimelingEggYellow");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.creeperEgg);
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.machine, 0, "terraformer");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.machine, 4, "cryogenic_chamber");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.machine, 8, "launch_controller");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.machineT2, 0, "gas_liquefier");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.machineT2, 4, "methane_synthesizer");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.machineT2, 8, "electrolyzer");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, MarsBlocks.hydrogenPipe);
    }

    private void addVariants(String name, String... variants)
    {
        Item itemBlockVariants = GameRegistry.findItem(Constants.MOD_ID_PLANETS, name);
        ModelBakery.addVariantName(itemBlockVariants, variants);
    }

    @Override
    public Object getGuiElement(Side side, int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (side == Side.CLIENT)
        {
            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

            if (ID == GuiIdsPlanets.MACHINE_MARS)
            {
                if (tile instanceof TileEntityTerraformer)
                {
                    return new GuiTerraformer(player.inventory, (TileEntityTerraformer) tile);
                }
                else if (tile instanceof TileEntityLaunchController)
                {
                    return new GuiLaunchController(player.inventory, (TileEntityLaunchController) tile);
                }
                else if (tile instanceof TileEntityElectrolyzer)
                {
                    return new GuiWaterElectrolyzer(player.inventory, (TileEntityElectrolyzer) tile);
                }
                else if (tile instanceof TileEntityGasLiquefier)
                {
                    return new GuiGasLiquefier(player.inventory, (TileEntityGasLiquefier) tile);
                }
                else if (tile instanceof TileEntityMethaneSynthesizer)
                {
                    return new GuiMethaneSynthesizer(player.inventory, (TileEntityMethaneSynthesizer) tile);
                }
            }
        }

        return null;
    }

    @Override
    public void spawnParticle(String particleID, Vector3 position, Vector3 motion, Object... extraData)
    {
        Minecraft mc = FMLClientHandler.instance().getClient();

        if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null)
        {
            final double dPosX = mc.getRenderViewEntity().posX - position.x;
            final double dPosY = mc.getRenderViewEntity().posY - position.y;
            final double dPosZ = mc.getRenderViewEntity().posZ - position.z;
            EntityFX particle = null;
            final double maxDistSqrd = 64.0D;

            if (dPosX * dPosX + dPosY * dPosY + dPosZ * dPosZ < maxDistSqrd * maxDistSqrd)
            {
                if (particleID.equals("sludgeDrip"))
                {
//                    particle = new EntityDropParticleFX(mc.theWorld, position.x, position.y, position.z, Material.water); TODO
                }
    			else if (particleID.equals("bacterialDrip"))
    			{
    				particle = new EntityBacterialDripFX(mc.theWorld, position.x, position.y, position.z);
    			}
            }

            if (particle != null)
            {
                particle.prevPosX = particle.posX;
                particle.prevPosY = particle.posY;
                particle.prevPosZ = particle.posZ;
                mc.effectRenderer.addEffect(particle);
            }
        }
    }

    @Override
    public void getGuiIDs(List<Integer> idList)
    {
        idList.add(GuiIdsPlanets.MACHINE_MARS);
    }

    public static void openSlimelingGui(EntitySlimeling slimeling, int gui)
    {
        switch (gui)
        {
        case 0:
            FMLClientHandler.instance().getClient().displayGuiScreen(new GuiSlimeling(slimeling));
            break;
        case 1:
            FMLClientHandler.instance().getClient().displayGuiScreen(new GuiSlimelingFeed(slimeling));
            break;
        }
    }

    public static class TickHandlerClient
    {
        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        public void onClientTick(ClientTickEvent event)
        {
            final Minecraft minecraft = FMLClientHandler.instance().getClient();

            final WorldClient world = minecraft.theWorld;

            if (world != null)
            {
                if (world.provider instanceof WorldProviderMars)
                {
                    if (world.provider.getSkyRenderer() == null)
                    {
                        world.provider.setSkyRenderer(new SkyProviderMars((IGalacticraftWorldProvider) world.provider));
                    }

                    if (world.provider.getCloudRenderer() == null)
                    {
                        world.provider.setCloudRenderer(new CloudRenderer());
                    }
                }
            }
        }
    }
}
