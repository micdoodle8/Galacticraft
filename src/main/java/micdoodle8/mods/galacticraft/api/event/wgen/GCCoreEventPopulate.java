package micdoodle8.mods.galacticraft.api.event.wgen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.Random;

/**
 * Event is thrown when a chunk is populated on planets.
 * <p/>
 * If you're adding your own dimensions, make sure you post these two events to
 * the forge event bus when decorating your planet/moon
 */
public class GCCoreEventPopulate extends Event
{
    public final World world;
    public final Random rand;
    public final BlockPos pos;

    public GCCoreEventPopulate(World world, Random rand, BlockPos pos)
    {
        this.world = world;
        this.rand = rand;
        this.pos = pos;
    }

    public static class Pre extends GCCoreEventPopulate
    {
        public Pre(World world, Random rand, BlockPos pos)
        {
            super(world, rand, pos);
        }
    }

    public static class Post extends GCCoreEventPopulate
    {
        public Post(World world, Random rand, BlockPos pos)
        {
            super(world, rand, pos);
        }
    }
}
