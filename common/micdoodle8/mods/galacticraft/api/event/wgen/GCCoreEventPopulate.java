package micdoodle8.mods.galacticraft.api.event.wgen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraftforge.event.Event;

/**
 * Event is thrown when a chunk is populated on planets.
 * 
 * If you're adding your own dimensions, make sure you post these two events to
 * the forge event bus when decorating your planet/moon
 */
public class GCCoreEventPopulate extends Event
{
	public final World worldObj;
	public final Random rand;
	public final int chunkX;
	public final int chunkZ;

	public GCCoreEventPopulate(World worldObj, Random rand, int chunkX, int chunkZ)
	{
		this.worldObj = worldObj;
		this.rand = rand;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
	}

	public static class Pre extends GCCoreEventPopulate
	{
		public Pre(World world, Random rand, int worldX, int worldZ)
		{
			super(world, rand, worldX, worldZ);
		}
	}

	public static class Post extends GCCoreEventPopulate
	{
		public Post(World world, Random rand, int worldX, int worldZ)
		{
			super(world, rand, worldX, worldZ);
		}
	}
}
