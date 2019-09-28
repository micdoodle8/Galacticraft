package micdoodle8.mods.galacticraft.planets.asteroids.tick;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.ShortRangeTelepadHandler;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class AsteroidsTickHandlerServer
{
    public static ShortRangeTelepadHandler spaceRaceData = null;
    public static List<EntityAstroMiner> activeMiners = new ArrayList<>();
    public static AtomicBoolean loadingSavedChunks = new AtomicBoolean();
    private static Field droppedChunks = null;
    
    public static void restart()
    {
        spaceRaceData = null;
        activeMiners.clear();
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        //Prevent issues when clients switch to LAN servers
        if (server == null)
        {
            return;
        }

        if (event.phase == TickEvent.Phase.START)
        {
            TileEntityMinerBase.checkNewMinerBases();
            
            if (AsteroidsTickHandlerServer.spaceRaceData == null)
            {
                World world = server.getWorld(0);
                AsteroidsTickHandlerServer.spaceRaceData = (ShortRangeTelepadHandler) world.getMapStorage().getOrLoadData(ShortRangeTelepadHandler.class, ShortRangeTelepadHandler.saveDataID);

                if (AsteroidsTickHandlerServer.spaceRaceData == null)
                {
                    AsteroidsTickHandlerServer.spaceRaceData = new ShortRangeTelepadHandler(ShortRangeTelepadHandler.saveDataID);
                    world.getMapStorage().setData(ShortRangeTelepadHandler.saveDataID, AsteroidsTickHandlerServer.spaceRaceData);
                }
            }
            
            int index = -1;
            for(EntityAstroMiner miner : activeMiners)
            {
                index ++;
                if (miner.isDead)
                {
//                    minerIt.remove();  Don't remove it, we want the index number to be static for the others
                    continue;
                }
                
                if (miner.playerMP != null)
                {
                    GCPlayerStats stats = GCPlayerStats.get(miner.playerMP);
                    if (stats != null)
                    {
                        List<BlockVec3> list = stats.getActiveAstroMinerChunks();
                        boolean inListAlready = false;
                        Iterator<BlockVec3> it = list.iterator();
                        while (it.hasNext())
                        {
                            BlockVec3 data = it.next();
                            if (data.sideDoneBits == index)  //SideDoneBits won't be saved to NBT, but during an active server session we can use it as a cross-reference to the index here - it's a 4th data int hidden inside a BlockVec3
                            {
                                if (miner.isDead)
                                {
                                    it.remove();  //Player stats should not save position of dead AstroMiner entity (probably broken by player deliberately breaking it)
                                }
                                else
                                {
                                    data.x = miner.chunkCoordX;
                                    data.z = miner.chunkCoordZ;
                                }
                                inListAlready = true;
                                break;
                            }
                        }
                        if (!inListAlready)
                        {
                            BlockVec3 data = new BlockVec3(miner.chunkCoordX, miner.dimension, miner.chunkCoordZ);
                            data.sideDoneBits = index;
                            list.add(data);
                        }
                    }
                }

            }
        }
    }
    
    @SubscribeEvent
    public void onWorldTick(WorldTickEvent event)
    {
        if (event.phase == Phase.START)
        {
            for(EntityAstroMiner miner : activeMiners)
            {
                if (miner.playerMP != null && miner.world == event.world && !miner.isDead)
                {
                    miner.serverTick = true;
                    miner.serverTickSave = miner.ticksExisted;
                }
            }
        }
        else if (event.phase == Phase.END)
        {
            for(EntityAstroMiner miner : activeMiners)
            {
                if (miner.isDead)
                {
//                    minerIt.remove();  Don't remove it, we want the index number to be static for the others
                    continue;
                }
                if (miner.playerMP != null && miner.world == event.world)
                {
                    if (miner.serverTick)
                    {
                        //Force an entity update tick, if it didn't happen already (mainly needed on Sponge servers - entities not super close to players seem to be not updated at all on Sponge even if the chunk is active, see issue #3307)
                        miner.ticksExisted = miner.serverTickSave + 1;
                        miner.onUpdate();
                    }

                    try
                    {
                        if (droppedChunks == null)
                        {
                            Class clazz = ((WorldServer)miner.world).getChunkProvider().getClass();
                            droppedChunks = clazz.getDeclaredField(GCCoreUtil.isDeobfuscated() ? "droppedChunksSet" : "field_73248_b");
                            droppedChunks.setAccessible(true);
                        }
                        Set<Long> undrop = (Set<Long>) droppedChunks.get(((WorldServer)miner.world).getChunkProvider());
                        undrop.remove(ChunkPos.asLong(miner.chunkCoordX, miner.chunkCoordZ));
                    } catch (Exception ignore)
                    {
                    }
                }
            }            
        }
    }

    public static void removeChunkData(GCPlayerStats stats, EntityAstroMiner entityToRemove)
    {
        int index = 0;
        for(EntityAstroMiner miner : activeMiners)
        {
            if (miner == entityToRemove)  //Found it in the list here
            {
                List<BlockVec3> list = stats.getActiveAstroMinerChunks();
                Iterator<BlockVec3> it = list.iterator();
                while (it.hasNext())
                {
                    BlockVec3 data = it.next();
                    if (data.sideDoneBits == index)  //Found it in the player's stats
                    {
                        it.remove();
                        return;
                    }
                }
                return;
            }
            index++;
        }
    }

    /**
     * How this works: every spawned or saved (in player stats) miner is added to the
     * activeMiners list here.
     * Once per server tick its position will be saved to player stats.
     * When the player quits, the saved miner positions will be saved with the player's stats NBT
     * When the player next loads, loadAstroChunkList will force load those chunks, therefore
     * reactivating AstroMiners if those chunks weren't already loaded.
     */
    public static void loadAstroChunkList(List<BlockVec3> activeChunks)
    {
        List<BlockVec3> copyList = new LinkedList<>(activeChunks);
        activeChunks.clear();
        if (!(AsteroidsTickHandlerServer.loadingSavedChunks.getAndSet(true)))
        {
            for (BlockVec3 data : copyList)
            {
                WorldProvider p = WorldUtil.getProviderForDimensionServer(data.y);
                if (p != null && p.world != null)
                {
                    GCLog.debug("Loading chunk " + data.y + ": " + data.x + "," + data.z + " - should contain a miner!");
                    WorldServer w = (WorldServer)p.world;
                    boolean previous = CompatibilityManager.forceLoadChunks(w);
                    w.getChunkProvider().loadChunk(data.x, data.z);
                    CompatibilityManager.forceLoadChunksEnd(w, previous);
                }
            }
            AsteroidsTickHandlerServer.loadingSavedChunks.set(false);
        }
    }

    public static int monitorMiner(EntityAstroMiner entityAstroMiner)
    {
        int result = activeMiners.size();
        activeMiners.add(entityAstroMiner);
        GCLog.debug("Monitoring miner " + result);
        return result;
    }

}
