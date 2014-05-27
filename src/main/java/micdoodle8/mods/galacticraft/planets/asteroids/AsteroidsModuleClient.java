package micdoodle8.mods.galacticraft.planets.asteroids;

import java.util.List;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.planets.IPlanetsModuleClient;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.block.BlockRendererWalkway;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity.RenderSmallAsteroid;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntitySmallAsteroid;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.client.registry.RenderingRegistry;
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
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) 
	{
		RenderingRegistry.registerEntityRenderingHandler(EntitySmallAsteroid.class, new RenderSmallAsteroid());
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
