package micdoodle8.mods.galacticraft.core.tick;

import com.google.common.collect.Lists;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3Dim;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockUnlitTorch;
import micdoodle8.mods.galacticraft.core.command.CommandGCHouston;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRace;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.dimension.WorldDataSpaceRaces;
import micdoodle8.mods.galacticraft.core.energy.grid.EnergyNetwork;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseConductor;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.fluid.FluidNetwork;
import micdoodle8.mods.galacticraft.core.fluid.ThreadFindSeal;
import micdoodle8.mods.galacticraft.core.network.GalacticraftPacketHandler;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFluidTank;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFluidTransmitter;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityPainter;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.MapUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.wrappers.Footprint;
import micdoodle8.mods.galacticraft.core.wrappers.ScheduledBlockChange;
import micdoodle8.mods.galacticraft.core.wrappers.ScheduledDimensionChange;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class TickHandlerServer
{
    private static Map<Integer, CopyOnWriteArrayList<ScheduledBlockChange>> scheduledBlockChanges = new ConcurrentHashMap<Integer, CopyOnWriteArrayList<ScheduledBlockChange>>();
    private static Map<Integer, CopyOnWriteArrayList<BlockVec3>> scheduledTorchUpdates = new ConcurrentHashMap<Integer, CopyOnWriteArrayList<BlockVec3>>();
    private static Map<Integer, Set<BlockPos>> edgeChecks = new TreeMap<Integer, Set<BlockPos>>();
    private static LinkedList<EnergyNetwork> networkTicks = new LinkedList<EnergyNetwork>();
    public static Map<Integer, Map<Long, List<Footprint>>> serverFootprintMap = new TreeMap<Integer, Map<Long, List<Footprint>>>();
    public static List<BlockVec3Dim> footprintBlockChanges = Lists.newArrayList();
    public static WorldDataSpaceRaces spaceRaceData = null;
    public static ArrayList<EntityPlayerMP> playersRequestingMapData = Lists.newArrayList();
    private static long tickCount;
    public static LinkedList<TileEntityFluidTransmitter> oxygenTransmitterUpdates = new LinkedList<TileEntityFluidTransmitter>();
    public static LinkedList<TileBaseConductor> energyTransmitterUpdates = new LinkedList<TileBaseConductor>();
    private static CopyOnWriteArrayList<ScheduledDimensionChange> scheduledDimensionChanges = new CopyOnWriteArrayList<ScheduledDimensionChange>();
    private final int MAX_BLOCKS_PER_TICK = 50000;
    private static List<GalacticraftPacketHandler> packetHandlers = Lists.newCopyOnWriteArrayList();
    private static List<FluidNetwork> fluidNetworks = Lists.newArrayList();
    public static int timerHoustonCommand;

    public static void addFluidNetwork(FluidNetwork network)
    {
        fluidNetworks.add(network);
    }

    public static void removeFluidNetwork(FluidNetwork network)
    {
        fluidNetworks.remove(network);
    }

    public static void addPacketHandler(GalacticraftPacketHandler handler)
    {
        TickHandlerServer.packetHandlers.add(handler);
    }

    @SubscribeEvent
    public void worldUnloadEvent(WorldEvent.Unload event)
    {
        for (GalacticraftPacketHandler packetHandler : packetHandlers)
        {
            packetHandler.unload(event.getWorld());
        }
    }

    public static void restart()
    {
        TickHandlerServer.scheduledBlockChanges.clear();
        TickHandlerServer.scheduledTorchUpdates.clear();
        TickHandlerServer.edgeChecks.clear();
        TickHandlerServer.networkTicks.clear();
        TickHandlerServer.serverFootprintMap.clear();
        TickHandlerServer.oxygenTransmitterUpdates.clear();
//        TickHandlerServer.hydrogenTransmitterUpdates.clear();
        TickHandlerServer.energyTransmitterUpdates.clear();
        TickHandlerServer.playersRequestingMapData.clear();
        TickHandlerServer.networkTicks.clear();

        for (SpaceRace race : SpaceRaceManager.getSpaceRaces())
        {
            SpaceRaceManager.removeSpaceRace(race);
        }

        TickHandlerServer.spaceRaceData = null;
        TickHandlerServer.tickCount = 0L;
        TickHandlerServer.fluidNetworks.clear();
        MapUtil.reset();
        TileEntityPainter.loadedTilesForDim.clear();
    }

    public static void addFootprint(long chunkKey, Footprint print, int dimID)
    {
        Map<Long, List<Footprint>> footprintMap = TickHandlerServer.serverFootprintMap.get(dimID);
        List<Footprint> footprints;

        if (footprintMap == null)
        {
            footprintMap = new HashMap<Long, List<Footprint>>();
            TickHandlerServer.serverFootprintMap.put(dimID, footprintMap);
            footprints = new ArrayList<Footprint>();
            footprintMap.put(chunkKey, footprints);
        }
        else
        {
            footprints = footprintMap.get(chunkKey);

            if (footprints == null)
            {
                footprints = new ArrayList<Footprint>();
                footprintMap.put(chunkKey, footprints);
            }
        }

        footprints.add(print);
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
     * @param changeAdd List of <ScheduledBlockChange>
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

    public static void scheduleNewDimensionChange(ScheduledDimensionChange change)
    {
        scheduledDimensionChanges.add(change);
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

    public static void scheduleNewEdgeCheck(int dimID, BlockPos edgeBlock)
    {
        Set<BlockPos> updateList = TickHandlerServer.edgeChecks.get(dimID);

        if (updateList == null)
        {
            updateList = new HashSet<BlockPos>();
        }

        updateList.add(edgeBlock);
        TickHandlerServer.edgeChecks.put(dimID, updateList);
    }

    public static boolean scheduledForChange(int dimID, BlockPos test)
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
        if (server == null)
        {
            return;
        }

        if (event.phase == Phase.START)
        {
            if (timerHoustonCommand > 0)
            {
                if (--timerHoustonCommand == 0)
                {
                    CommandGCHouston.reset();
                }
            }
            
            for (ScheduledDimensionChange change : TickHandlerServer.scheduledDimensionChanges)
            {
                try
                {
                    GCPlayerStats stats = GCPlayerStats.get(change.getPlayer());
                    final WorldProvider provider = WorldUtil.getProviderForDimensionServer(change.getDimensionId());
                    if (provider != null)
                    {
                        final int dim = GCCoreUtil.getDimensionID(provider);
                        GCLog.info("Found matching world (" + dim + ") for name: " + change.getDimensionId());

                        if (change.getPlayer().world instanceof WorldServer)
                        {
                            final WorldServer world = (WorldServer) change.getPlayer().world;

                            WorldUtil.transferEntityToDimension(change.getPlayer(), dim, world);
                        }
                    }
                    else
                    {
                        GCLog.severe("World not found when attempting to transfer entity to dimension: " + change.getDimensionId());
                    }

                    stats.setTeleportCooldown(10);
                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_CLOSE_GUI, GCCoreUtil.getDimensionID(change.getPlayer().world), new Object[] {}), change.getPlayer());
                }
                catch (Exception e)
                {
                    GCLog.severe("Error occurred when attempting to transfer entity to dimension: " + change.getDimensionId());
                    e.printStackTrace();
                }
            }

            TickHandlerServer.scheduledDimensionChanges.clear();

            if (MapUtil.calculatingMap.get())
            {
                MapUtil.BiomeMapNextTick_MultiThreaded();
            }
            else if (!MapUtil.doneOverworldTexture)
            {
                MapUtil.makeOverworldTexture();
            }

            if (TickHandlerServer.spaceRaceData == null)
            {
                World world = server.getWorld(0);
                TickHandlerServer.spaceRaceData = (WorldDataSpaceRaces) world.getMapStorage().getOrLoadData(WorldDataSpaceRaces.class, WorldDataSpaceRaces.saveDataID);

                if (TickHandlerServer.spaceRaceData == null)
                {
                    TickHandlerServer.spaceRaceData = new WorldDataSpaceRaces(WorldDataSpaceRaces.saveDataID);
                    world.getMapStorage().setData(WorldDataSpaceRaces.saveDataID, TickHandlerServer.spaceRaceData);
                }
            }

            SpaceRaceManager.tick();
            
            TileEntityOxygenSealer.onServerTick();

            if (TickHandlerServer.tickCount % 33 == 0)
            {
                WorldServer[] worlds = server.worlds;

                for (int i = worlds.length - 1; i >= 0; i--)
                {
                    WorldServer world = worlds[i];
                    TileEntityPainter.onServerTick(world);
                }                    
            }
            if (TickHandlerServer.tickCount % 100 == 0)
            {
                WorldServer[] worlds = server.worlds;

                for (int i = 0; i < worlds.length; i++)
                {
                    WorldServer world = worlds[i];
                    ChunkProviderServer chunkProviderServer = world.getChunkProvider();

                    Map<Long, List<Footprint>> footprintMap = TickHandlerServer.serverFootprintMap.get(GCCoreUtil.getDimensionID(world));

                    if (footprintMap != null)
                    {
                        boolean mapChanged = false;

                        if (chunkProviderServer != null)
                        {
                            Iterator iterator = chunkProviderServer.getLoadedChunks().iterator();

                            while (iterator.hasNext())
                            {
                                Chunk chunk = (Chunk) iterator.next();
                                long chunkKey = ChunkPos.asLong(chunk.x, chunk.z);

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

                                    GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_FOOTPRINT_LIST, worlds[i].provider.getDimension(), new Object[] { chunkKey, footprints.toArray(new Footprint[footprints.size()]) }), worlds[i].provider.getDimension());
                                }
                            }
                        }

                        if (mapChanged)
                        {
                            TickHandlerServer.serverFootprintMap.put(GCCoreUtil.getDimensionID(world), footprintMap);
                        }
                    }
                }
            }

            if (!footprintBlockChanges.isEmpty())
            {
                for (BlockVec3Dim targetPoint : footprintBlockChanges)
                {
                    WorldServer[] worlds = server.worlds;

                    for (int i = 0; i < worlds.length; i++)
                    {
                        WorldServer world = worlds[i];

                        if (GCCoreUtil.getDimensionID(world) == targetPoint.dim)
                        {
                            long chunkKey = ChunkPos.asLong((int) targetPoint.x >> 4, (int) targetPoint.z >> 4);
                            GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_FOOTPRINTS_REMOVED, GCCoreUtil.getDimensionID(world), new Object[] { chunkKey, new BlockVec3(targetPoint.x, targetPoint.y, targetPoint.z) }), new NetworkRegistry.TargetPoint(targetPoint.dim, targetPoint.x, targetPoint.y, targetPoint.z, 50));


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
                    File baseFolder = new File(DimensionManager.getCurrentSaveRootDirectory(), "galacticraft/overworldMap");
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
                            MapUtil.makeVanillaMap(playerMP.dimension, (int) Math.floor(stats.getCoordsTeleportedFromZ()) >> 4, (int) Math.floor(stats.getCoordsTeleportedFromZ()) >> 4, baseFolder, reusable);
                        }
                        playersRequestingMapData.removeAll(copy);
                    }
                }
            }

            TickHandlerServer.tickCount++;

            EnergyNetwork.tickCount++;
        }
        else if (event.phase == Phase.END)
        {
            for (FluidNetwork network : new ArrayList<>(fluidNetworks))
            {
                if (!network.pipes.isEmpty())
                {
                    network.tickEnd();
                }
                else
                {
                    fluidNetworks.remove(network);
                }
            }

            int maxPasses = 10;
            while (!TickHandlerServer.networkTicks.isEmpty())
            {
                LinkedList<EnergyNetwork> pass = new LinkedList<>();
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
                LinkedList<TileEntityFluidTransmitter> pass = new LinkedList<>();
                pass.addAll(TickHandlerServer.oxygenTransmitterUpdates);
                TickHandlerServer.oxygenTransmitterUpdates.clear();
                for (TileEntityFluidTransmitter newTile : pass)
                {
                    if (!newTile.isInvalid())
                    {
                        newTile.refresh();
                    }
                }

                if (--maxPasses <= 0)
                {
                    break;
                }
            }

            maxPasses = 10;
            while (!TickHandlerServer.energyTransmitterUpdates.isEmpty())
            {
                LinkedList<TileBaseConductor> pass = new LinkedList<>();
                pass.addAll(TickHandlerServer.energyTransmitterUpdates);
                TickHandlerServer.energyTransmitterUpdates.clear();
                for (TileBaseConductor newTile : pass)
                {
                    // I'm not sure why this would be null, but apparently it can be
                    //      See https://github.com/micdoodle8/Galacticraft/issues/3700
                    if (newTile != null && !newTile.isInvalid())
                    {
                        newTile.refresh();
                    }
                }

                if (--maxPasses <= 0)
                {
                    break;
                }
            }
        }
    }

    private static Set<Integer> worldsNeedingUpdate = new HashSet<Integer>();

    public static void markWorldNeedsUpdate(int dimension)
    {
        worldsNeedingUpdate.add(dimension);
    }

    @SubscribeEvent
    public void onWorldTick(WorldTickEvent event)
    {
        if (event.phase == Phase.START)
        {
            final WorldServer world = (WorldServer) event.world;

            CopyOnWriteArrayList<ScheduledBlockChange> changeList = TickHandlerServer.scheduledBlockChanges.get(GCCoreUtil.getDimensionID(world));

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
                            BlockPos changePosition = change.getChangePosition();
                            Block block = world.getBlockState(changePosition).getBlock();
                            //Only replace blocks of type BlockAir or fire - this is to prevent accidents where other mods have moved blocks
                            if (changePosition != null && (block instanceof BlockAir || block == Blocks.FIRE))
                            {
                                world.setBlockState(changePosition, change.getChangeState(), change.getChangeUpdateFlag());
                            }
                        }
                    }
                }

                changeList.clear();
                TickHandlerServer.scheduledBlockChanges.remove(GCCoreUtil.getDimensionID(world));
                if (newList.size() > 0)
                {
                    TickHandlerServer.scheduledBlockChanges.put(GCCoreUtil.getDimensionID(world), new CopyOnWriteArrayList<ScheduledBlockChange>(newList));
                }
            }

            CopyOnWriteArrayList<BlockVec3> torchList = TickHandlerServer.scheduledTorchUpdates.get(GCCoreUtil.getDimensionID(world));

            if (torchList != null && !torchList.isEmpty())
            {
                for (BlockVec3 torch : torchList)
                {
                    if (torch != null)
                    {
                        BlockPos pos = new BlockPos(torch.x, torch.y, torch.z);
                        Block b = world.getBlockState(pos).getBlock();
                        if (b instanceof BlockUnlitTorch)
                        {
                            world.scheduleUpdate(pos, b, 2 + world.rand.nextInt(30));
                        }
                    }
                }

                torchList.clear();
                TickHandlerServer.scheduledTorchUpdates.remove(GCCoreUtil.getDimensionID(world));
            }

            if (world.provider instanceof IOrbitDimension)
            {
                try
                {
                    int dim = ((IOrbitDimension) world.provider).getPlanetIdToOrbit();
                    if (dim == Integer.MIN_VALUE) dim = GCCoreUtil.getDimensionID(WorldUtil.getProviderForNameServer(((IOrbitDimension)world.provider).getPlanetToOrbit()));
                    int minY = ((IOrbitDimension)world.provider).getYCoordToTeleportToPlanet();

                    final Entity[] entityList = world.loadedEntityList.toArray(new Entity[world.loadedEntityList.size()]);
                    for (final Entity e : entityList)
                    {
                        if (e.posY <= minY && e.world == world)
                        {
                            WorldUtil.transferEntityToDimension(e, dim, world, false, null);
                        }
                    }
                }
                catch (Exception ex)
                {
                }
            }

            int dimensionID = GCCoreUtil.getDimensionID(world);
            if (worldsNeedingUpdate.contains(dimensionID))
            {
                worldsNeedingUpdate.remove(dimensionID);
                for (Object obj : event.world.loadedTileEntityList)
                {
                    TileEntity tile = (TileEntity) obj;
                    if (tile instanceof TileEntityFluidTank)
                    {
                        ((TileEntityFluidTank) tile).updateClient = true;
                    }
                }
            }
        }
        else if (event.phase == Phase.END)
        {
            final WorldServer world = (WorldServer) event.world;

            for (GalacticraftPacketHandler handler : packetHandlers)
            {
                handler.tick(world);
            }

            int dimID = GCCoreUtil.getDimensionID(world);
            Set<BlockPos> edgesList = TickHandlerServer.edgeChecks.get(dimID);
            final HashSet<BlockPos> checkedThisTick = new HashSet<>();

            if (edgesList != null && !edgesList.isEmpty())
            {
                List<BlockPos> edgesListCopy = new ArrayList<>();
                edgesListCopy.addAll(edgesList);
                for (BlockPos edgeBlock : edgesListCopy)
                {
                    if (edgeBlock != null && !checkedThisTick.contains(edgeBlock))
                    {
                        if (TickHandlerServer.scheduledForChange(dimID, edgeBlock))
                        {
                            continue;
                        }

                        ThreadFindSeal done = new ThreadFindSeal(world, edgeBlock, 0, new ArrayList<TileEntityOxygenSealer>());
                        checkedThisTick.addAll(done.checkedAll());
                    }
                }

                TickHandlerServer.edgeChecks.remove(GCCoreUtil.getDimensionID(world));
            }
        }
    }
}
