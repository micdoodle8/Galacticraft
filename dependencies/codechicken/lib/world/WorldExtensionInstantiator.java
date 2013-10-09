package codechicken.lib.world;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.World;

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
