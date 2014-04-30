package codechicken.lib.world;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.HashMap;

public abstract class WorldExtension
{
    public final World world;
    public HashMap<Chunk, ChunkExtension> chunkMap = new HashMap<Chunk, ChunkExtension>();
    
    public WorldExtension(World world)
    {
        this.world = world;
    }
    
    public void load()
    {
    }
    
    public void unload()
    {
    }
    
    public void save()
    {
    }
    
    public void preTick()
    {
    }
    
    public void postTick()
    {
    }
        
    protected final void addChunk(ChunkExtension extension)
    {
        chunkMap.put(extension.chunk, extension);
    }
    
    protected final void loadChunk(Chunk chunk)
    {
        chunkMap.get(chunk).load();
    }
    
    protected final void unloadChunk(Chunk chunk)
    {
        chunkMap.get(chunk).unload();
    }
    
    protected final void loadChunkData(Chunk chunk, NBTTagCompound tag)
    {
        chunkMap.get(chunk).loadData(tag);
    }
    
    protected final void saveChunkData(Chunk chunk, NBTTagCompound tag)
    {
        chunkMap.get(chunk).saveData(tag);
    }
    
    protected final void remChunk(Chunk chunk)
    {
        chunkMap.remove(chunk);
    }

    protected final void watchChunk(Chunk chunk, EntityPlayerMP player)
    {
        chunkMap.get(chunk).watchPlayer(player);
    }

    protected final void unwatchChunk(Chunk chunk, EntityPlayerMP player)
    {
        ChunkExtension extension = chunkMap.get(chunk);
        if(extension != null)
            extension.unwatchPlayer(player);
    }

    protected final void sendChunkUpdates(Chunk chunk)
    {
        chunkMap.get(chunk).sendUpdatePackets();
    }

    public boolean containsChunk(Chunk chunk)
    {
        return chunkMap.containsKey(chunk);
    }

    public ChunkExtension getChunkExtension(int chunkXPos, int chunkZPos)
    {
        if(!world.blockExists(chunkXPos<<4, 128, chunkZPos<<4))
            return null;
        
        return chunkMap.get(world.getChunkFromChunkCoords(chunkXPos, chunkZPos));
    }
}
