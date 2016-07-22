package micdoodle8.mods.galacticraft.planets.asteroids;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.loader.AdvancedModelLoader;
import micdoodle8.mods.galacticraft.core.client.model.loader.IModelCustom;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity.RenderTier3Rocket;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.item.ItemModelGrapple;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.item.ItemModelRocketT3;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile.*;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.*;
import micdoodle8.mods.galacticraft.planets.mars.client.fx.EntityCryoFX;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.IPlanetsModuleClient;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.client.FluidTexturesGC;
import micdoodle8.mods.galacticraft.planets.asteroids.client.fx.EntityFXTeleport;
import micdoodle8.mods.galacticraft.planets.asteroids.client.gui.GuiAstroMinerDock;
import micdoodle8.mods.galacticraft.planets.asteroids.client.gui.GuiShortRangeTelepad;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity.RenderGrapple;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity.RenderSmallAsteroid;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityGrapple;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntitySmallAsteroid;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityTier3Rocket;
import micdoodle8.mods.galacticraft.planets.asteroids.event.AsteroidsEventHandlerClient;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.recipe.craftguide.CraftGuideIntegration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class AsteroidsModuleClient implements IPlanetsModuleClient
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        addPlanetVariants("asteroidsBlock", "asteroid_rock_0", "asteroid_rock_1", "asteroid_rock_2", "ore_aluminum_asteroids", "ore_ilmenite_asteroids", "ore_iron_asteroids");
        addPlanetVariants("thermalPadding", "thermalHelm", "thermalChestplate", "thermalLeggings", "thermalBoots");
        addPlanetVariants("itemBasicAsteroids", "reinforcedPlateT3", "engineT2", "rocketFinsT2", "shardIron", "shardTitanium", "ingotTitanium", "compressedTitanium", "thermalCloth", "beamCore");
        addPlanetVariants("walkway", "walkway", "walkway_wire", "walkway_pipe");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void registerVariants()
    {
        Item receiver = Item.getItemFromBlock(AsteroidBlocks.beamReceiver);
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation("galacticraftplanets:beamReceiver", "inventory");
        ModelLoader.setCustomModelResourceLocation(receiver, 0, modelResourceLocation);

        Item reflector = Item.getItemFromBlock(AsteroidBlocks.beamReflector);
        modelResourceLocation = new ModelResourceLocation("galacticraftplanets:beamReflector", "inventory");
        ModelLoader.setCustomModelResourceLocation(reflector, 0, modelResourceLocation);

        Item teleporter = Item.getItemFromBlock(AsteroidBlocks.shortRangeTelepad);
        modelResourceLocation = new ModelResourceLocation("galacticraftplanets:telepadShort", "inventory");
        ModelLoader.setCustomModelResourceLocation(teleporter, 0, modelResourceLocation);

        modelResourceLocation = new ModelResourceLocation("galacticraftplanets:grapple", "inventory");
        ModelLoader.setCustomModelResourceLocation(AsteroidsItems.grapple, 0, modelResourceLocation);

        modelResourceLocation = new ModelResourceLocation("galacticraftplanets:itemTier3Rocket", "inventory");
        for (int i = 0; i < 5; ++i)
        {
            ModelLoader.setCustomModelResourceLocation(AsteroidsItems.tier3Rocket, i, modelResourceLocation);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onModelBakeEvent(ModelBakeEvent event)
    {
        replaceModelDefault(event, "beamReceiver", "receiver.obj", ImmutableList.of("Main", "Receiver", "Ring"), null, new ItemTransformVec3f(new Vector3f(), new Vector3f(), new Vector3f(1.0F, 1.0F, 1.0F)));
        replaceModelDefault(event, "beamReflector", "reflector.obj", ImmutableList.of("Base", "Axle", "EnergyBlaster", "Ring"), null, new ItemTransformVec3f(new Vector3f(), new Vector3f(), new Vector3f(1.0F, 1.0F, 1.0F)));
        replaceModelDefault(event, "telepadShort", "telepadShort.obj", ImmutableList.of("Top", "Bottom", "Connector"), null, new ItemTransformVec3f(new Vector3f(), new Vector3f(), new Vector3f(0.2F, 0.2F, 0.2F)));
        replaceModelDefault(event, "grapple", "grapple.obj", ImmutableList.of("Grapple"), ItemModelGrapple.class, TRSRTransformation.identity());
        replaceModelDefault(event, "itemTier3Rocket", "tier3rocket.obj", ImmutableList.of("Boosters", "Cube", "NoseCone", "Rocket"), ItemModelRocketT3.class, TRSRTransformation.identity());
    }

    private void replaceModelDefault(ModelBakeEvent event, String resLoc, String objLoc, List<String> visibleGroups, Class<? extends ModelTransformWrapper> clazz, IModelState parentState)
    {
        ClientUtil.replaceModel(GalacticraftPlanets.ASSET_PREFIX, event, resLoc, objLoc, visibleGroups, clazz, parentState);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void loadTextures(TextureStitchEvent.Pre event)
    {
        registerTexture(event, "minerbase");
        registerTexture(event, "beamReflector");
        registerTexture(event, "beamReceiver");
        registerTexture(event, "telepadShort");
        registerTexture(event, "telepadShort0");
        registerTexture(event, "grapple");
        registerTexture(event, "tier3rocket");
    }

    private void registerTexture(TextureStitchEvent.Pre event, String texture)
    {
        event.map.registerSprite(new ResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "blocks/" + texture));
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        AsteroidsModuleClient.registerBlockRenderers();
        AsteroidsEventHandlerClient clientEventHandler = new AsteroidsEventHandlerClient();
        MinecraftForge.EVENT_BUS.register(clientEventHandler);
        FluidTexturesGC.init();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        RenderingRegistry.registerEntityRenderingHandler(EntitySmallAsteroid.class, new RenderSmallAsteroid());
        RenderingRegistry.registerEntityRenderingHandler(EntityGrapple.class, new RenderGrapple());
//          IModelCustom podModel = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/pod.obj"));
//          RenderingRegistry.registerEntityRenderingHandler(EntityEntryPod.class, new RenderEntryPod(podModel));
          RenderingRegistry.registerEntityRenderingHandler(EntityTier3Rocket.class, new RenderTier3Rocket());
//          RenderingRegistry.registerEntityRenderingHandler(EntityAstroMiner.class, new RenderAstroMiner());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBeamReflector.class, new TileEntityBeamReflectorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBeamReceiver.class, new TileEntityBeamReceiverRenderer());
//        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMinerBase.class, new TileEntityMinerBaseRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityShortRangeTelepad.class, new TileEntityShortRangeTelepadRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTreasureChestAsteroids.class, new TileEntityTreasureChestRenderer());

        if (Loader.isModLoaded("craftguide"))
        	CraftGuideIntegration.register();
    }

    public static void registerBlockRenderers()
    {
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockBasic, 0, "asteroid_rock_0");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockBasic, 1, "asteroid_rock_1");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockBasic, 2, "asteroid_rock_2");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockBasic, 3, "ore_aluminum_asteroids");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockBasic, 4, "ore_ilmenite_asteroids");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockBasic, 5, "ore_iron_asteroids");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockWalkway, 0, "walkway");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockWalkway, 1, "walkway_wire");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockWalkway, 2, "walkway_pipe");
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockDenseIce);
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.blockMinerBase);
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.minerBaseFull);
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.treasureChestTier2);
        ClientUtil.registerBlockJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidBlocks.treasureChestTier3);
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.thermalPadding, 0, "thermalHelm");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.thermalPadding, 1, "thermalChestplate");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.thermalPadding, 2, "thermalLeggings");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.thermalPadding, 3, "thermalBoots");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 0, "reinforcedPlateT3");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 1, "engineT2");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 2, "rocketFinsT2");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 3, "shardIron");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 4, "shardTitanium");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 5, "shardTitanium");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 6, "compressedTitanium");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 7, "thermalCloth");
        ClientUtil.registerItemJson(GalacticraftPlanets.TEXTURE_PREFIX, AsteroidsItems.basicItem, 8, "beamCore");
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
            EntityFX particle = null;
            double viewDistance = 64.0D;

            if (dX * dX + dY * dY + dZ * dZ < viewDistance * viewDistance)
            {
                if (particleID.equals("portalBlue"))
                {
                    particle = new EntityFXTeleport(mc.theWorld, position, motion, (TileEntityShortRangeTelepad) extraData[0], (Boolean) extraData[1]);
                }
                else if (particleID.equals("cryoFreeze"))
                {
                    particle = new EntityCryoFX(mc.theWorld, position, motion);
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
}
