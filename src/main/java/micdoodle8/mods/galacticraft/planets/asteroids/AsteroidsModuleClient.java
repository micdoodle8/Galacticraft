package micdoodle8.mods.galacticraft.planets.asteroids;

import com.google.common.collect.ImmutableList;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.IPlanetsModuleClient;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.client.FluidTexturesGC;
import micdoodle8.mods.galacticraft.planets.asteroids.client.fx.EntityFXTeleport;
import micdoodle8.mods.galacticraft.planets.asteroids.client.gui.GuiAstroMinerDock;
import micdoodle8.mods.galacticraft.planets.asteroids.client.gui.GuiShortRangeTelepad;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity.*;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.item.*;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile.TileEntityBeamReceiverRenderer;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile.TileEntityBeamReflectorRenderer;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile.TileEntityMinerBaseRenderer;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile.TileEntityShortRangeTelepadRenderer;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.*;
import micdoodle8.mods.galacticraft.planets.asteroids.event.AsteroidsEventHandlerClient;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReceiver;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReflector;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import micdoodle8.mods.galacticraft.planets.mars.client.fx.EntityCryoFX;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class AsteroidsModuleClient implements IPlanetsModuleClient
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        RenderingRegistry.registerEntityRenderingHandler(EntitySmallAsteroid.class, (RenderManager manager) -> new RenderSmallAsteroid(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityGrapple.class, (RenderManager manager) -> new RenderGrapple(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityEntryPod.class, (RenderManager manager) -> new RenderEntryPod(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityTier3Rocket.class, (RenderManager manager) -> new RenderTier3Rocket(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityAstroMiner.class, (RenderManager manager) -> new RenderAstroMiner(manager));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void registerVariants()
    {
        addPlanetVariants("asteroids_block", "asteroids_block", "asteroid_rock_1", "asteroid_rock_2", "ore_aluminum_asteroids", "ore_ilmenite_asteroids", "ore_iron_asteroids", "asteroid_deco", "titanium_block");
        addPlanetVariants("thermal_padding", "thermal_padding", "thermal_chestplate", "thermal_leggings", "thermal_boots");
        addPlanetVariants("item_basic_asteroids", "item_basic_asteroids", "engine_t2", "rocket_fins_t2", "shard_iron", "shard_titanium", "ingot_titanium", "compressed_titanium", "thermal_cloth", "beam_core", "dust_titanium");
        addPlanetVariants("walkway", "walkway", "walkway_wire", "walkway_pipe");
        addPlanetVariants("strange_seed", "strange_seed", "strange_seed1");

        Item receiver = Item.getItemFromBlock(AsteroidBlocks.beamReceiver);
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation("galacticraftplanets:beam_receiver", "inventory");
        ModelLoader.setCustomModelResourceLocation(receiver, 0, modelResourceLocation);

        Item reflector = Item.getItemFromBlock(AsteroidBlocks.beamReflector);
        modelResourceLocation = new ModelResourceLocation("galacticraftplanets:beam_reflector", "inventory");
        ModelLoader.setCustomModelResourceLocation(reflector, 0, modelResourceLocation);

        Item teleporter = Item.getItemFromBlock(AsteroidBlocks.shortRangeTelepad);
        modelResourceLocation = new ModelResourceLocation("galacticraftplanets:telepad_short", "inventory");
        ModelLoader.setCustomModelResourceLocation(teleporter, 0, modelResourceLocation);

        modelResourceLocation = new ModelResourceLocation("galacticraftplanets:grapple", "inventory");
        ModelLoader.setCustomModelResourceLocation(AsteroidsItems.grapple, 0, modelResourceLocation);

        modelResourceLocation = new ModelResourceLocation("galacticraftplanets:rocket_t3", "inventory");
        for (int i = 0; i < 5; ++i)
        {
            ModelLoader.setCustomModelResourceLocation(AsteroidsItems.tier3Rocket, i, modelResourceLocation);
        }

        modelResourceLocation = new ModelResourceLocation("galacticraftplanets:astro_miner", "inventory");
        ModelLoader.setCustomModelResourceLocation(AsteroidsItems.astroMiner, 0, modelResourceLocation);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onModelBakeEvent(ModelBakeEvent event)
    {
        replaceModelDefault(event, "beam_receiver", "block/receiver.obj", ImmutableList.of("Main", "Receiver", "Ring"), ItemModelBeamReceiver.class, TRSRTransformation.identity(), "inventory", "facing=up", "facing=down", "facing=north", "facing=west", "facing=east", "facing=south");
        replaceModelDefault(event, "beam_reflector", "block/reflector.obj", ImmutableList.of("Base", "Axle", "EnergyBlaster", "Ring"), ItemModelBeamReflector.class, TRSRTransformation.identity(), "inventory", "normal");
        replaceModelDefault(event, "telepad_short", "block/telepad_short.obj", ImmutableList.of("Top", "Bottom", "Connector"), ItemModelTelepad.class, TRSRTransformation.identity(), "inventory", "normal");
        replaceModelDefault(event, "grapple", "grapple.obj", ImmutableList.of("Grapple"), ItemModelGrapple.class, TRSRTransformation.identity());
        replaceModelDefault(event, "rocket_t3", "tier3rocket.obj", ImmutableList.of("Boosters", "Cube", "NoseCone", "Rocket"), ItemModelRocketT3.class, TRSRTransformation.identity());
        replaceModelDefault(event, "astro_miner", "astro_miner_inv.obj", ImmutableList.of("Hull_Center"), ItemModelAstroMiner.class, TRSRTransformation.identity());
    }

    private void replaceModelDefault(ModelBakeEvent event, String resLoc, String objLoc, List<String> visibleGroups, Class<? extends ModelTransformWrapper> clazz, IModelState parentState, String... variants)
    {
        ClientUtil.replaceModel(GalacticraftPlanets.ASSET_PREFIX, event, resLoc, objLoc, visibleGroups, clazz, parentState, variants);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void loadTextures(TextureStitchEvent.Pre event)
    {
        registerTexture(event, "minerbase");
        registerTexture(event, "beam_reflector");
        registerTexture(event, "beam_receiver");
        registerTexture(event, "telepad_short");
        registerTexture(event, "telepad_short0");
        registerTexture(event, "grapple");
        registerTexture(event, "tier3rocket");
        registerTexture(event, "astro_miner");
        registerTexture(event, "astro_miner_off");
        registerTexture(event, "astro_miner_fx");
        registerTexture(event, "space_pod");
        registerTexture(event, "fluids/argon");
        registerTexture(event, "fluids/atmosphericgases");
        registerTexture(event, "fluids/carbondioxide");
        registerTexture(event, "fluids/helium");
        registerTexture(event, "fluids/liquidargon");
        registerTexture(event, "fluids/liquidmethane");
        registerTexture(event, "fluids/liquidnitrogen");
        registerTexture(event, "fluids/liquidoxygen");
        registerTexture(event, "fluids/methane");
        registerTexture(event, "fluids/nitrogen");
    }

    private void registerTexture(TextureStitchEvent.Pre event, String texture)
    {
        event.getMap().registerSprite(new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "blocks/" + texture));
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        AsteroidsEventHandlerClient clientEventHandler = new AsteroidsEventHandlerClient();
        MinecraftForge.EVENT_BUS.register(clientEventHandler);
        FluidTexturesGC.init();
        AsteroidsModuleClient.registerBlockRenderers();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
//          RenderingRegistry.registerEntityRenderingHandler(EntityAstroMiner.class, (RenderManager manager) -> new RenderAstroMiner());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBeamReflector.class, new TileEntityBeamReflectorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBeamReceiver.class, new TileEntityBeamReceiverRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMinerBase.class, new TileEntityMinerBaseRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityShortRangeTelepad.class, new TileEntityShortRangeTelepadRenderer());

//        if (Loader.isModLoaded("craftguide"))
//        {
//            CraftGuideIntegration.register();
//        }
    }

    public static void registerBlockRenderers()
    {
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockBasic, 0, "asteroids_block");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockBasic, 1, "asteroid_rock_1");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockBasic, 2, "asteroid_rock_2");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockBasic, 3, "ore_aluminum_asteroids");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockBasic, 4, "ore_ilmenite_asteroids");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockBasic, 5, "ore_iron_asteroids");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockBasic, 6, "asteroid_deco");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockBasic, 7, "titanium_block");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockWalkway, 0, "walkway");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockWalkway, 1, "walkway_wire");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockWalkway, 2, "walkway_pipe");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockDenseIce);
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockMinerBase);
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.minerBaseFull);
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.thermalPadding, 0, "thermal_padding");  //helm
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.thermalPadding, 1, "thermal_chestplate");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.thermalPadding, 2, "thermal_leggings");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.thermalPadding, 3, "thermal_boots");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 5, "item_basic_asteroids");  //reinforced_plate_t3
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 1, "engine_t2");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 2, "rocket_fins_t2");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 3, "shard_iron");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 4, "shard_titanium");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 0, "ingot_titanium");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 6, "compressed_titanium");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 7, "thermal_cloth");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 8, "beam_core");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 9, "dust_titanium");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.heavyNoseCone, 0, "heavy_nose_cone");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.strangeSeed, 0, "strange_seed");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.strangeSeed, 1, "strange_seed1");
    }

    private void addPlanetVariants(String name, String... variants)
    {
        Item itemBlockVariants = Item.REGISTRY.getObject(new ResourceLocation(Constants.MOD_ID_PLANETS, name));
        ResourceLocation[] variants0 = new ResourceLocation[variants.length];
        for (int i = 0; i < variants.length; ++i)
        {
            variants0[i] = new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + variants[i]);
        }
        ModelBakery.registerItemVariants(itemBlockVariants, variants0);
    }

    @Override
    public void getGuiIDs(List<Integer> idList)
    {
        idList.add(GuiIdsPlanets.MACHINE_ASTEROIDS);
    }

    @Override
    public Object getGuiElement(Side side, int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

        switch (ID)
        {
        case GuiIdsPlanets.MACHINE_ASTEROIDS:

            if (tile instanceof TileEntityShortRangeTelepad)
            {
                return new GuiShortRangeTelepad(player.inventory, ((TileEntityShortRangeTelepad) tile));
            }
            if (tile instanceof TileEntityMinerBase)
            {
                return new GuiAstroMinerDock(player.inventory, (TileEntityMinerBase) tile);
            }

            break;
        }

        return null;
    }

    @Override
    public void spawnParticle(String particleID, Vector3 position, Vector3 motion, Object... extraData)
    {
        Minecraft mc = FMLClientHandler.instance().getClient();

        if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null)
        {
            double dX = mc.getRenderViewEntity().posX - position.x;
            double dY = mc.getRenderViewEntity().posY - position.y;
            double dZ = mc.getRenderViewEntity().posZ - position.z;
            Particle particle = null;
            double viewDistance = 64.0D;

            if (dX * dX + dY * dY + dZ * dZ < viewDistance * viewDistance)
            {
                if (particleID.equals("portalBlue"))
                {
                    particle = new EntityFXTeleport(mc.world, position, motion, (TileEntityShortRangeTelepad) extraData[0], (Boolean) extraData[1]);
                }
                else if (particleID.equals("cryoFreeze"))
                {
                    particle = new EntityCryoFX(mc.world, position, motion);
                }
            }

            if (particle != null)
            {
                mc.effectRenderer.addEffect(particle);
            }
        }
    }
}
