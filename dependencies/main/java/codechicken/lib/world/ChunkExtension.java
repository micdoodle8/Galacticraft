package codechicken.lib.world;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;

import java.util.HashSet;

public abstract class ChunkExtension
{
    public final Chunk chunk;
    public final ChunkCoordIntPair coord;
    public final WorldExtension world;
    public HashSet<EntityPlayerMP> watchedPlayers;
    
    public ChunkExtension(Chunk chunk, WorldExtension world)
    {
        this.chunk = chunk;
        coord = chunk.getChunkCoordIntPair();
        this.world = world;
        watchedPlayers = new HashSet<EntityPlayerMP>();
    }
    
    public void loadData(NBTTagCompound tag)
    {
    }
    
    public void saveData(NBTTagCompound tag)
    {
    }
    
    public void load()
    {
    }
    
    public void unload()
    {
    }
    
    public final void sendPacketToPlayers(Packet packet)
    {
        for(EntityPlayerMP player : watchedPlayers)
            player.playerNetServerHandler.sendPacket(packet);
    }

    public final void watchPlayer(EntityPlayerMP player)
    {
        watchedPlayers.add(player);
        onWatchPlayer(player);
    }
    
    public void onWatchPlayer(EntityPlayerMP player)
    {
    }

    public final void unwatchPlayer(EntityPlayerMP player)
    {
        watchedPlayers.remove(player);
        onUnWatchPlayer(player);
    }
    
    public void onUnWatchPlayer(EntityPlayerMP player)
    {
    }

    public void sendUpdatePackets()
    {
    }
    
    @Override
    public int hashCode()
    {
        return coord.chunkXPos ^ coord.chunkZPos;
    }
    
    @Override
    public boolean equals(Object o)
    {
        return (o instanceof ChunkExtension && ((ChunkExtension)o).coord.equals(coord)) ||
                (o instanceof ChunkCoordIntPair && coord.equals(o)) ||
                (o instanceof Long && (Long)o == (((long)coord.chunkXPos)<<32 | coord.chunkZPos));
    }
}
