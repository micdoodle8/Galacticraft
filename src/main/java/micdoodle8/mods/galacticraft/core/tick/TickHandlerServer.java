package micdoodle8.mods.galacticraft.core.tick;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3Dim;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockUnlitTorch;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRace;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.dimension.WorldDataSpaceRaces;
import micdoodle8.mods.galacticraft.core.energy.grid.EnergyNetwork;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseConductor;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.oxygen.ThreadFindSeal;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenTransmitter;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.MapUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.wrappers.Footprint;
import micdoodle8.mods.galacticraft.core.wrappers.ScheduledBlockChange;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityHydrogenPipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class TickHandlerServer
{
    private static Map<Integer, CopyOnWriteArrayList<ScheduledBlockChange>> scheduledBlockChanges = new ConcurrentHashMap<Integer, CopyOnWriteArrayList<ScheduledBlockChange>>();
    private static Map<Integer, CopyOnWriteArrayList<BlockVec3>> scheduledTorchUpdates = new ConcurrentHashMap<Integer, CopyOnWriteArrayList<BlockVec3>>();
    private static Map<Integer, List<BlockVec3>> edgeChecks = new HashMap<Integer, List<BlockVec3>>();
    private static LinkedList<EnergyNetwork> networkTicks = new LinkedList<EnergyNetwork>();
    public static Map<Integer, Map<Long, List<Footprint>>> serverFootprintMap = new HashMap<Integer, Map<Long, List<Footprint>>>();
    public static List<BlockVec3Dim> footprintBlockChanges = Lists.newArrayList();
    public static WorldDataSpaceRaces spaceRaceData = null;
    public static ArrayList<EntityPlayerMP> playersRequestingMapData = Lists.newArrayList();
    private static long tickCount;
	public static LinkedList<TileEntityOxygenTransmitter> oxygenTransmitterUpdates  = new LinkedList<TileEntityOxygenTransmitter>();
	public static LinkedList<TileEntityHydrogenPipe> hydrogenTransmitterUpdates  = new LinkedList<TileEntityHydrogenPipe>();
	public static LinkedList<TileBaseConductor> energyTransmitterUpdates  = new LinkedList<TileBaseConductor>();
	private final int MAX_BLOCKS_PER_TICK = 50000; 
	
    public static void restart()
    {
        TickHandlerServer.scheduledBlockChanges.clear();
        TickHandlerServer.scheduledTorchUpdates.clear();
        TickHandlerServer.edgeChecks.clear();
        TickHandlerServer.networkTicks.clear();
        TickHandlerServer.serverFootprintMap.clear();
        TickHandlerServer.oxygenTransmitterUpdates.clear();
        TickHandlerServer.hydrogenTransmitterUpdates.clear();
        TickHandlerServer.energyTransmitterUpdates.clear();
        TickHandlerServer.playersRequestingMapData.clear();
        TickHandlerServer.networkTicks.clear();

        for (SpaceRace race : SpaceRaceManager.getSpaceRaces())
        {
            SpaceRaceManager.removeSpaceRace(race);
        }

        TickHandlerServer.spaceRaceData = null;
        TickHandlerServer.tickCount = 0L;
        MapUtil.reset();
    }

    public static void addFootprint(long chunkKey, Footprint print, int dimID)
    {
        Map<Long, List<Footprint>> footprintMap = TickHandlerServer.serverFootprintMap.get(dimID);
        List<Footprint> footprints;

        if (footprintMap == null)
        {
            footprintMap = new HashMap<Long, List<Footprint>>();
            footprints = new ArrayList<Footprint>();
        }
        else
        {
            footprints = footprintMap.get(chunkKey);

            if (footprints == null)
            {
                footprints = new ArrayList<Footprint>();
            }
        }

        footprints.add(print);
        footprintMap.put(chunkKey, footprints);
        TickHandlerServer.serverFootprintMap.put(dimID, footprintMap);
    }

    public static void scheduleNewBlockChange(int dimID, ScheduledBlockChange change)
    {
        CopyOnWriteArrayList<ScheduledBlockChange> changeList = TickHandlerServer.scheduledBlockChanges.get(dimID);

        if (changeList == null)
        {
            changeList = new CopyOnWriteArrayList<ScheduledBlockChange>();
        }

        changeList.add(change);
        TickHandlerServer.scheduledBlockChanges.put(dimID, changeList);
    }

    /**
     * Only use this for AIR blocks (any type of BlockAir)
     * 
     * @param dimID
     * @param changeAdd  List of <ScheduledBlockChange>
     */
    public static void scheduleNewBlockChange(int dimID, List<ScheduledBlockChange> changeAdd)
    {
        CopyOnWriteArrayList<ScheduledBlockChange> changeList = TickHandlerServer.scheduledBlockChanges.get(dimID);

        if (changeList == null)
        {
            changeList = new CopyOnWriteArrayList<ScheduledBlockChange>();
        }

        changeList.addAll(changeAdd);
        TickHandlerServer.scheduledBlockChanges.put(dimID, changeList);
    }

    public static void scheduleNewTorchUpdate(int dimID, List<BlockVec3> torches)
    {
        CopyOnWriteArrayList<BlockVec3> updateList = TickHandlerServer.scheduledTorchUpdates.get(dimID);

        if (updateList == null)
        {
            updateList = new CopyOnWriteArrayList<BlockVec3>();
        }

        updateList.addAll(torches);
        TickHandlerServer.scheduledTorchUpdates.put(dimID, updateList);
    }

    public static void scheduleNewEdgeCheck(int dimID, BlockVec3 edgeBlock)
    {
        List<BlockVec3> updateList = TickHandlerServer.edgeChecks.get(dimID);

        if (updateList == null)
        {
            updateList = new ArrayList<BlockVec3>();
        }

        updateList.add(edgeBlock);
        TickHandlerServer.edgeChecks.put(dimID, updateList);
    }

    public static boolean scheduledForChange(int dimID, BlockVec3 test)
    {
        CopyOnWriteArrayList<ScheduledBlockChange> changeList = TickHandlerServer.scheduledBlockChanges.get(dimID);

        if (changeList != null)
        {
            for (ScheduledBlockChange change : changeList)
            {
                if (test.equals(change.getChangePosition()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static void scheduleNetworkTick(EnergyNetwork grid)
    {
        TickHandlerServer.networkTicks.add(grid);
    }

    public static void removeNetworkTick(EnergyNetwork grid)
    {
        TickHandlerServer.networkTicks.remove(grid);
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent event)
    {
    	MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
    	//Prevent issues when clients switch to LAN servers
    	if (server == null) return;

    	if (event.phase == Phase.START)
        {       	
        	if (MapUtil.calculatingMap.get()) MapUtil.BiomeMapNextTick();
            else if (!MapUtil.doneOverworldTexture) MapUtil.makeOverworldTexture();
            
        	if (TickHandlerServer.spaceRaceData == null)
            {
                World world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(0);
                TickHandlerServer.spaceRaceData = (WorldDataSpaceRaces) world.mapStorage.loadData(WorldDataSpaceRaces.class, WorldDataSpaceRaces.saveDataID);

                if (TickHandlerServer.spaceRaceData == null)
                {
                    TickHandlerServer.spaceRaceData = new WorldDataSpaceRaces(WorldDataSpaceRaces.saveDataID);
                    world.mapStorage.setData(WorldDataSpaceRaces.saveDataID, TickHandlerServer.spaceRaceData);
                }
            }

            SpaceRaceManager.tick();
            
            TileEntityOxygenSealer.onServerTick();

            if (TickHandlerServer.tickCount % 100 == 0)
            {
                WorldServer[] worlds = server.worldServers;

                for (int i = 0; i < worlds.length; i++)
                {
                    WorldServer world = worlds[i];
                    ChunkProviderServer chunkProviderServer = world.theChunkProviderServer;

                    Map<Long, List<Footprint>> footprintMap = TickHandlerServer.serverFootprintMap.get(world.provider.dimensionId);

                    if (footprintMap != null)
                    {
                        boolean mapChanged = false;

                        if (chunkProviderServer != null)
                        {
                            Iterator iterator = chunkProviderServer.loadedChunks.iterator();

                            while (iterator.hasNext())
                            {
                                Chunk chunk = (Chunk) iterator.next();
                                long chunkKey = ChunkCoordIntPair.chunkXZ2Int(chunk.xPosition, chunk.zPosition);

                                List<Footprint> footprints = footprintMap.get(chunkKey);

                                if (footprints != null)
                                {
                                    List<Footprint> toRemove = new ArrayList<Footprint>();

                                    for (int j = 0; j < footprints.size(); j++)
                                    {
                                        footprints.get(j).age += 100;

                                        if (footprints.get(j).age >= Footprint.MAX_AGE)
                                        {
                                            toRemove.add(footprints.get(j));
                                        }
                                    }

                                    if (!toRemove.isEmpty())
                                    {
                                        footprints.removeAll(toRemove);
                                    }

                                    footprintMap.put(chunkKey, footprints);
                                    mapChanged = true;

                                    GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_FOOTPRINT_LIST, new Object[] { chunkKey, footprints.toArray(new Footprint[footprints.size()]) }), worlds[i].provider.dimensionId);
                                }
                            }
                        }

                        if (mapChanged)
                        {
                            TickHandlerServer.serverFootprintMap.put(world.provider.dimensionId, footprintMap);
                        }
                    }
                }
            }

            if (!footprintBlockChanges.isEmpty())
            {
                for (BlockVec3Dim targetPoint : footprintBlockChanges)
                {
                    WorldServer[] worlds = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers;

                    for (int i = 0; i < worlds.length; i++)
                    {
                        WorldServer world = worlds[i];

                        if (world.provider.dimensionId == targetPoint.dim)
                        {
                            long chunkKey = ChunkCoordIntPair.chunkXZ2Int((int)targetPoint.x >> 4, (int)targetPoint.z >> 4);
                            GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_FOOTPRINTS_REMOVED, new Object[] { chunkKey, new BlockVec3(targetPoint.x, targetPoint.y, targetPoint.z) }), new NetworkRegistry.TargetPoint(targetPoint.dim, targetPoint.x, targetPoint.y, targetPoint.z, 50));


//                            Map<Long, List<Footprint>> footprintMap = TickHandlerServer.serverFootprintMap.get(world.provider.dimensionId);
//
//                            if (footprintMap != null && !footprintMap.isEmpty())
//                            {
//                                List<Footprint> footprints = footprintMap.get(chunkKey);
//                                if (footprints != null)
//                                	GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_UPDATE_FOOTPRINT_LIST, new Object[] { chunkKey, footprints.toArray(new Footprint[footprints.size()]) }), new NetworkRegistry.TargetPoint(targetPoint.dim, targetPoint.x, targetPoint.y, targetPoint.z, 50));
//                            }
                        }
                    }
                }

                footprintBlockChanges.clear();
            }

            if (tickCount % 20 == 0)
            {
                if (!playersRequestingMapData.isEmpty())
                {
                    File baseFolder = new File(MinecraftServer.getServer().worldServerForDimension(0).getChunkSaveLocation(), "galacticraft/overworldMap");
                    if (!baseFolder.exists() && !baseFolder.mkdirs())
                    {
                    	GCLog.severe("Base folder(s) could not be created: " + baseFolder.getAbsolutePath());
                    }
                    else
                    {
	                	ArrayList<EntityPlayerMP> copy = new ArrayList<EntityPlayerMP>(playersRequestingMapData);
	                    BufferedImage reusable = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
	                    for (EntityPlayerMP playerMP : copy)
	                    {
	                        GCPlayerStats stats = GCPlayerStats.get(playerMP);
	                    	MapUtil.makeVanillaMap(playerMP.dimension, (int)Math.floor(stats.coordsTeleportedFromX) >> 4, (int)Math.floor(stats.coordsTeleportedFromZ) >> 4, baseFolder, reusable);
	                    }
	                    playersRequestingMapData.removeAll(copy);
                    }
                }
            }

            TickHandlerServer.tickCount++;

            if (TickHandlerServer.tickCount >= Long.MAX_VALUE)
            {
                TickHandlerServer.tickCount = 0;
            }

            EnergyNetwork.tickCount++;
        }
        else if (event.phase == Phase.END)
        {
            int maxPasses = 10;
            while (!TickHandlerServer.networkTicks.isEmpty())
            {
                LinkedList<EnergyNetwork> pass = new LinkedList();
                pass.addAll(TickHandlerServer.networkTicks);
                TickHandlerServer.networkTicks.clear();
                for (EnergyNetwork grid : pass)
                {
                    grid.tickEnd();
                }

                if (--maxPasses <= 0)
                {
                    break;
                }
            }

            maxPasses = 10;
            while (!TickHandlerServer.oxygenTransmitterUpdates.isEmpty())
            {
                LinkedList<TileEntityOxygenTransmitter> pass = new LinkedList();
                pass.addAll(TickHandlerServer.oxygenTransmitterUpdates);
                TickHandlerServer.oxygenTransmitterUpdates.clear();
                for (TileEntityOxygenTransmitter newTile : pass)
                {
                    if (!newTile.isInvalid()) newTile.refresh();
                }            

                if (--maxPasses <= 0)
                {
                    break;
                }
            }

            maxPasses = 10;
            while (!TickHandlerServer.hydrogenTransmitterUpdates.isEmpty())
            {
                LinkedList<TileEntityHydrogenPipe> pass = new LinkedList();
                pass.addAll(TickHandlerServer.hydrogenTransmitterUpdates);
                TickHandlerServer.hydrogenTransmitterUpdates.clear();
                for (TileEntityHydrogenPipe newTile : pass)
                {
                    if (!newTile.isInvalid()) newTile.refresh();
                }            

                if (--maxPasses <= 0)
                {
                    break;
                }
            }

            maxPasses = 10;
            while (!TickHandlerServer.energyTransmitterUpdates.isEmpty())
            {
                LinkedList<TileBaseConductor> pass = new LinkedList();
                pass.addAll(TickHandlerServer.energyTransmitterUpdates);
                TickHandlerServer.energyTransmitterUpdates.clear();
                for (TileBaseConductor newTile : pass)
                {
                	if (!newTile.isInvalid()) newTile.refresh();
                }            

                if (--maxPasses <= 0)
                {
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldTick(WorldTickEvent event)
    {
        if (event.phase == Phase.START)
        {
            final WorldServer world = (WorldServer) event.world;

            CopyOnWriteArrayList<ScheduledBlockChange> changeList = TickHandlerServer.scheduledBlockChanges.get(world.provider.dimensionId);

            if (changeList != null && !changeList.isEmpty())
            {
                int blockCount = 0;
                int blockCountMax = Math.max(this.MAX_BLOCKS_PER_TICK, changeList.size() / 4);
                List<ScheduledBlockChange> newList = new ArrayList<ScheduledBlockChange>(Math.max(0, changeList.size() - blockCountMax));

                for (ScheduledBlockChange change : changeList)
                {
                    if (++blockCount > blockCountMax)
                    {
                    	newList.add(change);
                    }
                    else
                    {
	                    if (change != null)
	                    {
	                        BlockVec3 changePosition = change.getChangePosition();
                            Block block = world.getBlock(changePosition.x, changePosition.y, changePosition.z);
	                        //Only replace blocks of type BlockAir or fire - this is to prevent accidents where other mods have moved blocks
	                        if (changePosition != null && (block instanceof BlockAir || block == Blocks.fire))
	                        {
	                            world.setBlock(changePosition.x, changePosition.y, changePosition.z, change.getChangeID(), change.getChangeMeta(), change.getChangeUpdateFlag());
	                        }
	                    }
                    }
                }

                changeList.clear();
                TickHandlerServer.scheduledBlockChanges.remove(world.provider.dimensionId);
                if (newList.size() > 0) TickHandlerServer.scheduledBlockChanges.put(world.provider.dimensionId, new CopyOnWriteArrayList<ScheduledBlockChange>(newList));
            }

            CopyOnWriteArrayList<BlockVec3> torchList = TickHandlerServer.scheduledTorchUpdates.get(world.provider.dimensionId);

            if (torchList != null && !torchList.isEmpty())
            {
                for (BlockVec3 torch : torchList)
                {
                    if (torch != null)
                    {
                        Block b = world.getBlock(torch.x, torch.y, torch.z); 
                    	if (b instanceof BlockUnlitTorch)
                        {
                            world.scheduleBlockUpdateWithPriority(torch.x, torch.y, torch.z, b, 2 + world.rand.nextInt(30), 0);
                        }
                    }
                }

                torchList.clear();
                TickHandlerServer.scheduledTorchUpdates.remove(world.provider.dimensionId);
            }

            if (world.provider instanceof IOrbitDimension)
            {
                final Object[] entityList = world.loadedEntityList.toArray();

                for (final Object o : entityList)
                {
                    if (o instanceof Entity)
                    {
                        final Entity e = (Entity) o;

                        if (e.worldObj.provider instanceof IOrbitDimension)
                        {
                            final IOrbitDimension dimension = (IOrbitDimension) e.worldObj.provider;

                            if (e.posY <= dimension.getYCoordToTeleportToPlanet())
                            {
                                int dim = 0;
                            	try {
                                	dim = WorldUtil.getProviderForNameServer(dimension.getPlanetToOrbit()).dimensionId;
                            	} catch (Exception ex) {} 

                                WorldUtil.transferEntityToDimension(e, dim, world, false, null);
                            }
                        }
                    }
                }
            }
        }
        else if (event.phase == Phase.END)
        {
            final WorldServer world = (WorldServer) event.world;

            List<BlockVec3> edgesList = TickHandlerServer.edgeChecks.get(world.provider.dimensionId);
            final HashSet<BlockVec3> checkedThisTick = new HashSet();

            if (edgesList != null && !edgesList.isEmpty())
            {
                List<BlockVec3> edgesListCopy = new ArrayList();
                edgesListCopy.addAll(edgesList);
                for (BlockVec3 edgeBlock : edgesListCopy)
                {
                    if (edgeBlock != null && !checkedThisTick.contains(edgeBlock))
                    {
                        if (TickHandlerServer.scheduledForChange(world.provider.dimensionId, edgeBlock))
                        {
                            continue;
                        }

                        ThreadFindSeal done = new ThreadFindSeal(world, edgeBlock, 2000, new ArrayList<TileEntityOxygenSealer>());
                        checkedThisTick.addAll(done.checkedAll());
                    }
                }

                TickHandlerServer.edgeChecks.remove(world.provider.dimensionId);
            }
        }
    }
}
