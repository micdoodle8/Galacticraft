package micdoodle8.mods.galacticraft.planets.asteroids;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.loader.AdvancedModelLoader;
import micdoodle8.mods.galacticraft.core.client.model.loader.IModelCustom;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity.RenderTier3Rocket;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile.TileEntityTreasureChestRenderer;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityEntryPod;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityGrapple;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntitySmallAsteroid;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityTier3Rocket;
import micdoodle8.mods.galacticraft.planets.asteroids.event.AsteroidsEventHandlerClient;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.recipe.craftguide.CraftGuideIntegration;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReceiver;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReflector;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityTreasureChestAsteroids;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

public class AsteroidsModuleClient implements IPlanetsModuleClient
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        addVariants("asteroidsBlock",
                "galacticraftplanets:asteroid_rock_0",
                "galacticraftplanets:asteroid_rock_1",
                "galacticraftplanets:asteroid_rock_2",
                "galacticraftplanets:ore_aluminum_asteroids",
                "galacticraftplanets:ore_ilmenite_asteroids",
                "galacticraftplanets:ore_iron_asteroids");
        addVariants("thermalPadding",
                "galacticraftplanets:thermalHelm",
                "galacticraftplanets:thermalChestplate",
                "galacticraftplanets:thermalLeggings",
                "galacticraftplanets:thermalBoots");
        addVariants("itemBasicAsteroids",
                "galacticraftplanets:reinforcedPlateT3",
                "galacticraftplanets:engineT2",
                "galacticraftplanets:rocketFinsT2",
                "galacticraftplanets:shardIron",
                "galacticraftplanets:shardTitanium",
                "galacticraftplanets:ingotTitanium",
                "galacticraftplanets:compressedTitanium",
                "galacticraftplanets:thermalCloth",
                "galacticraftplanets:beamCore");
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
          IModelCustom rocketModel = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/tier3rocket.obj"));
          RenderingRegistry.registerEntityRenderingHandler(EntityTier3Rocket.class, new RenderTier3Rocket(rocketModel, GalacticraftPlanets.ASSET_PREFIX, "tier3rocket"));
//          RenderingRegistry.registerEntityRenderingHandler(EntityAstroMiner.class, new RenderAstroMiner());
//          IModelCustom grappleModel = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/grapple.obj"));
//        MinecraftForgeClient.registerItemRenderer(AsteroidsItems.grapple, new ItemRendererGrappleHook(grappleModel));
//        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(AsteroidBlocks.beamReceiver), new ItemRendererBeamReceiver());
//        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(AsteroidBlocks.beamReflector), new ItemRendererBeamReflector());
//        MinecraftForgeClient.registerItemRenderer(AsteroidsItems.tier3Rocket, new ItemRendererTier3Rocket(rocketModel));
//        MinecraftForgeClient.registerItemRenderer(AsteroidsItems.astroMiner, new ItemRendererAstroMiner());
//        MinecraftForgeClient.registerItemRenderer(AsteroidsItems.thermalPadding, new ItemRendererThermalArmor());
//        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(AsteroidBlocks.shortRangeTelepad), new ItemRendererShortRangeTelepad());
//        MinecraftForgeClient.registerItemRenderer(AsteroidsItems.heavyNoseCone, new ItemRendererHeavyNoseCone());
//        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(AsteroidBlocks.blockWalkway), new ItemRendererWalkway());
//        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(AsteroidBlocks.blockWalkwayOxygenPipe), new ItemRendererWalkway());
//        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(AsteroidBlocks.blockWalkwayWire), new ItemRendererWalkway());
//            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBeamReflector.class, new TileEntityBeamReflectorRenderer());
//            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBeamReceiver.class, new TileEntityBeamReceiverRenderer());
//            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMinerBase.class, new TileEntityMinerBaseRenderer());
//            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityShortRangeTelepad.class, new TileEntityShortRangeTelepadRenderer());
//            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTreasureChestAsteroids.class, new TileEntityTreasureChestRenderer());

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

    private void addVariants(String name, String... variants)
    {
        Item itemBlockVariants = GameRegistry.findItem(Constants.MOD_ID_PLANETS, name);
        ModelBakery.addVariantName(itemBlockVariants, variants);
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
