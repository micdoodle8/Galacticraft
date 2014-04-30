package codechicken.lib.world;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public abstract class WorldExtensionInstantiator
{
    public int instantiatorID;
    
    public abstract WorldExtension createWorldExtension(World world);
    public abstract ChunkExtension createChunkExtension(Chunk chunk, WorldExtension world);
    
    public WorldExtension getExtension(World world)
    {
        return WorldExtensionManager.getWorldExtension(world, instantiatorID);
    }
}
