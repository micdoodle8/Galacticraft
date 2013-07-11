package micdoodle8.mods.galacticraft.api.event.wgen;

import java.util.Random;
import net.minecraft.world.World;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;

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
    
    public static class Pre extends DecorateBiomeEvent
    {
        public Pre(World world, Random rand, int worldX, int worldZ)
        {
            super(world, rand, worldX, worldZ);
        }
    }
    
    public static class Post extends DecorateBiomeEvent
    {
        public Post(World world, Random rand, int worldX, int worldZ)
        {
            super(world, rand, worldX, worldZ);
        }
    }
}
