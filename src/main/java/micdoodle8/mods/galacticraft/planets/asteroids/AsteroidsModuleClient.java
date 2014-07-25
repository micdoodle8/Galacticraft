package micdoodle8.mods.galacticraft.planets.asteroids;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.IPlanetsModuleClient;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.client.fx.EntityFXTeleport;
import micdoodle8.mods.galacticraft.planets.asteroids.client.gui.GuiShortRangeTelepad;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.block.BlockRendererTier3TreasureChest;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.block.BlockRendererWalkway;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity.RenderEntryPod;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity.RenderGrapple;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity.RenderSmallAsteroid;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity.RenderTier3Rocket;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.item.*;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile.TileEntityBeamReceiverRenderer;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile.TileEntityBeamReflectorRenderer;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile.TileEntityShortRangeTelepadRenderer;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityEntryPod;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityGrapple;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntitySmallAsteroid;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityTier3Rocket;
import micdoodle8.mods.galacticraft.planets.asteroids.event.AsteroidsEventHandlerClient;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReceiver;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReflector;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityTreasureChestAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile.TileEntityTreasureChestRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

public class AsteroidsModuleClient implements IPlanetsModuleClient
{
	private static int walkwayRenderID;
	private static int treasureChestID;

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{

	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		AsteroidsModuleClient.walkwayRenderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new BlockRendererWalkway(AsteroidsModuleClient.walkwayRenderID));
		AsteroidsModuleClient.treasureChestID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new BlockRendererTier3TreasureChest(AsteroidsModuleClient.treasureChestID));
		AsteroidsEventHandlerClient clientEventHandler = new AsteroidsEventHandlerClient();
		FMLCommonHandler.instance().bus().register(clientEventHandler);
		MinecraftForge.EVENT_BUS.register(clientEventHandler);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		RenderingRegistry.registerEntityRenderingHandler(EntitySmallAsteroid.class, new RenderSmallAsteroid());
		RenderingRegistry.registerEntityRenderingHandler(EntityGrapple.class, new RenderGrapple());
        IModelCustom podModel = AdvancedModelLoader.loadModel(new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "models/pod.obj"));
        RenderingRegistry.registerEntityRenderingHandler(EntityEntryPod.class, new RenderEntryPod(podModel));
		IModelCustom rocketModel = AdvancedModelLoader.loadModel(new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "models/tier3rocket.obj"));
		RenderingRegistry.registerEntityRenderingHandler(EntityTier3Rocket.class, new RenderTier3Rocket(rocketModel, AsteroidsModule.ASSET_PREFIX, "tier3rocket"));
		IModelCustom grappleModel = AdvancedModelLoader.loadModel(new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "models/grapple.obj"));
		MinecraftForgeClient.registerItemRenderer(AsteroidsItems.grapple, new ItemRendererGrappleHook(grappleModel));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(AsteroidBlocks.beamReceiver), new ItemRendererBeamReceiver());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(AsteroidBlocks.beamReflector), new ItemRendererBeamReflector());
		MinecraftForgeClient.registerItemRenderer(AsteroidsItems.tier3Rocket, new ItemRendererTier3Rocket(rocketModel));
		MinecraftForgeClient.registerItemRenderer(AsteroidsItems.thermalPadding, new ItemRendererThermalArmor());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(AsteroidBlocks.shortRangeTelepad), new ItemRendererShortRangeTelepad());
        MinecraftForgeClient.registerItemRenderer(AsteroidsItems.heavyNoseCone, new ItemRendererHeavyNoseCone());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBeamReflector.class, new TileEntityBeamReflectorRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBeamReceiver.class, new TileEntityBeamReceiverRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityShortRangeTelepad.class, new TileEntityShortRangeTelepadRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTreasureChestAsteroids.class, new TileEntityTreasureChestRenderer());
	}

	@Override
	public void getGuiIDs(List<Integer> idList)
	{
        idList.add(GuiIdsPlanets.MACHINE_ASTEROIDS);
	}

	@Override
	public Object getGuiElement(Side side, int ID, EntityPlayer player, World world, int x, int y, int z)
	{
        TileEntity tile = world.getTileEntity(x, y, z);

        switch (ID)
        {
            case GuiIdsPlanets.MACHINE_ASTEROIDS:

                if (tile instanceof TileEntityShortRangeTelepad)
                {
                    return new GuiShortRangeTelepad(player.inventory, ((TileEntityShortRangeTelepad) tile));
                }

                break;
        }

        return null;
	}

	@Override
	public int getBlockRenderID(Block block)
	{
		if (block == AsteroidBlocks.blockWalkway || block == AsteroidBlocks.blockWalkwayWire || block == AsteroidBlocks.blockWalkwayOxygenPipe)
		{
			return AsteroidsModuleClient.walkwayRenderID;
		}

        if (block == AsteroidBlocks.treasureChestTier3)
        {
            return AsteroidsModuleClient.treasureChestID;
        }

		return 0;
	}

	@Override
	public void spawnParticle(String particleID, Vector3 position, Vector3 motion, Object... extraData)
	{
        Minecraft mc = FMLClientHandler.instance().getClient();

        if (mc != null && mc.renderViewEntity != null && mc.effectRenderer != null)
        {
            double dX = mc.renderViewEntity.posX - position.x;
            double dY = mc.renderViewEntity.posY - position.y;
            double dZ = mc.renderViewEntity.posZ - position.z;
            EntityFX particle = null;
            double viewDistance = 64.0D;

            if (dX * dX + dY * dY + dZ * dZ < viewDistance * viewDistance)
            {
                if (particleID.equals("portalBlue"))
                {
                    particle = new EntityFXTeleport(mc.theWorld, position, motion, (TileEntityShortRangeTelepad)extraData[0], (Boolean)extraData[1]);
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
