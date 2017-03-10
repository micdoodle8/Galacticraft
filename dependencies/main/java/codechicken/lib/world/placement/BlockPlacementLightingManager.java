package codechicken.lib.world.placement;

import codechicken.lib.world.placement.lighting.LightingCalcEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by covers1624 on 8/1/2016.
 */
public class BlockPlacementLightingManager {

    private static final LinkedList<LightingCalcEntry> lightingQue = new LinkedList<LightingCalcEntry>();
    public static final int lightEntriesPerTick = Integer.parseInt(System.getProperty("ccl.lightingEntriesPerTic", "100"));

    static {
        MinecraftForge.EVENT_BUS.register(new BlockPlacementLightingManager());
    }

    @SubscribeEvent
    public void tickEvent(ServerTickEvent serverTick) {
        if (serverTick.phase == Phase.START) {
            if (!lightingQue.isEmpty()) {
                synchronized (lightingQue) {
                    FMLLog.info("Que Size: " + lightingQue.size());
                    Iterator<LightingCalcEntry> iterator = lightingQue.iterator();
                    int entriesProcessed = 0;
                    while (iterator.hasNext()) {
                        if (entriesProcessed >= lightEntriesPerTick) {
                            break;
                        }
                        LightingCalcEntry entry = iterator.next();
                        if (entry == null) {
                            continue;
                        }
                        WorldServer world = getServer().worldServerForDimension(entry.dimension);
                        Chunk chunk = getChunk(world, entry.pos);
                        if (entry.chunkHeightModified) {
                            //FMLLog.info("GeneratingSkyLightMap in Chunk: X: %s, Z: %s", entry.pos.getX() >> 4, entry.pos.getX() >> 4);
                            chunk.generateSkylightMap();
                        } else {
                            if (entry.newLightOpacity > 0) {
                                if (entry.pos.getY() >= entry.height) {
                                    //FMLLog.info("RelightBlock");
                                    chunk.relightBlock(entry.pos.getX() & 15, entry.pos.getY() + 1, entry.pos.getZ() & 15);
                                }
                            } else if (entry.pos.getY() == entry.height - 1) {
                                //FMLLog.info("RelightBlock");
                                chunk.relightBlock(entry.pos.getX() & 15, entry.pos.getY(), entry.pos.getZ() & 15);
                            }

                            if (entry.newLightOpacity != entry.oldLightOpacity && (entry.newLightOpacity < entry.oldLightOpacity || entry.skyLight > 0 || entry.blockLight > 0)) {
                                //FMLLog.info("SkyLightOcclusion at: X: %s, Z: %s", entry.pos.getX() & 15, entry.pos.getX() & 15);
                                chunk.propagateSkylightOcclusion(entry.pos.getX() & 15, entry.pos.getZ() & 15);
                            }
                        }
                        entriesProcessed++;
                        iterator.remove();
                    }
                }
            }
        }
    }

    private static MinecraftServer getServer() {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }

    public static void uploadToLightingQue(LinkedList<LightingCalcEntry> lightingEntries) {
        addEntries(lightingEntries);
    }

    public static synchronized void addEntries(LinkedList<LightingCalcEntry> entries) {
        synchronized (lightingQue) {
            lightingQue.addAll(entries);
        }
    }

    protected static Chunk getChunk(WorldServer world, BlockPos pos) {
        return world.getChunkFromChunkCoords(pos.getX() >> 4, pos.getZ() >> 4);
    }

}
