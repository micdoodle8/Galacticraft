package micdoodle8.mods.galacticraft.planets.asteroids;

import java.util.List;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.planets.IPlanetsModuleClient;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.block.BlockRendererWalkway;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity.RenderGrapple;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity.RenderSmallAsteroid;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity.RenderTier3Rocket;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.item.ItemRendererBeamReceiver;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.item.ItemRendererBeamReflector;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.item.ItemRendererGrappleHook;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.item.ItemRendererTier3Rocket;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile.TileEntityBeamReceiverRenderer;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile.TileEntityBeamReflectorRenderer;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityGrapple;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntitySmallAsteroid;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityTier3Rocket;
import micdoodle8.mods.galacticraft.planets.asteroids.event.AsteroidsEventHandlerClient;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReceiver;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReflector;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;

public class AsteroidsModuleClient implements IPlanetsModuleClient
{
	private static int walkwayRenderID;
	
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		
	}

	@Override
	public void init(FMLInitializationEvent event) 
	{
		AsteroidsModuleClient.walkwayRenderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new BlockRendererWalkway(AsteroidsModuleClient.walkwayRenderID));
		FMLCommonHandler.instance().bus().register(new AsteroidsEventHandlerClient());
		MinecraftForge.EVENT_BUS.register(new AsteroidsEventHandlerClient());
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) 
	{
		RenderingRegistry.registerEntityRenderingHandler(EntitySmallAsteroid.class, new RenderSmallAsteroid());
		RenderingRegistry.registerEntityRenderingHandler(EntityGrapple.class, new RenderGrapple());
		IModelCustom rocketModel = AdvancedModelLoader.loadModel(new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "models/tier3rocket.obj"));
		RenderingRegistry.registerEntityRenderingHandler(EntityTier3Rocket.class, new RenderTier3Rocket(rocketModel, AsteroidsModule.ASSET_PREFIX, "tier3rocket"));
		IModelCustom grappleModel = AdvancedModelLoader.loadModel(new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "models/grapple.obj"));
		MinecraftForgeClient.registerItemRenderer(AsteroidsItems.itemGrapple, new ItemRendererGrappleHook(grappleModel));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(AsteroidBlocks.beamReceiver), new ItemRendererBeamReceiver());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(AsteroidBlocks.beamReflector), new ItemRendererBeamReflector());
		MinecraftForgeClient.registerItemRenderer(AsteroidsItems.itemTier3Rocket, new ItemRendererTier3Rocket(rocketModel));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBeamReflector.class, new TileEntityBeamReflectorRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBeamReceiver.class, new TileEntityBeamReceiverRenderer());
	}

	@Override
	public void serverStarting(FMLServerStartingEvent event)
	{
		
	}

	@Override
	public void getGuiIDs(List<Integer> idList)
	{
		
	}

	@Override
	public Object getGuiElement(Side side, int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}

	@Override
	public int getBlockRenderID(Block block)
	{
		if (block == AsteroidBlocks.blockWalkway || block == AsteroidBlocks.blockWalkwayWire || block == AsteroidBlocks.blockWalkwayOxygenPipe)
		{
			return walkwayRenderID;
		}
		
		return 0;
	}

	@Override
	public void spawnParticle(String particleID, Vector3 position, Vector3 color) 
	{
		
	}
}
