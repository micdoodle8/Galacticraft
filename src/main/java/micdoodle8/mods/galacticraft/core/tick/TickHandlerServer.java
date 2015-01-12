package micdoodle8.mods.galacticraft.core.tick;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
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
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.wrappers.Footprint;
import micdoodle8.mods.galacticraft.core.wrappers.ScheduledBlockChange;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityHydrogenPipe;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    public static List<NetworkRegistry.TargetPoint> footprintRefreshList = Lists.newArrayList();
    public static WorldDataSpaceRaces spaceRaceData = null;
    public static ArrayList<EntityPlayerMP> playersRequestingMapData = Lists.newArrayList();
    private static long tickCount;
	public static LinkedList<TileEntityOxygenTransmitter> oxygenTransmitterUpdates  = new LinkedList<TileEntityOxygenTransmitter>();
	public static LinkedList<TileEntityHydrogenPipe> hydrogenTransmitterUpdates  = new LinkedList<TileEntityHydrogenPipe>();
	public static LinkedList<TileBaseConductor> energyTransmitterUpdates  = new LinkedList<TileBaseConductor>();

    public static void restart()
    {
        TickHandlerServer.scheduledBlockChanges.clear();
        TickHandlerServer.scheduledTorchUpdates.clear();
        TickHandlerServer.edgeChecks.clear();
        TickHandlerServer.networkTicks.clear();
        TickHandlerServer.serverFootprintMap.clear();

        for (SpaceRace race : SpaceRaceManager.getSpaceRaces())
        {
            SpaceRaceManager.removeSpaceRace(race);
        }

        TickHandlerServer.spaceRaceData = null;
        TickHandlerServer.tickCount = 0L;
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
        if (event.phase == Phase.START)
        {
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

            if (TickHandlerServer.tickCount % 100 == 0)
            {
                WorldServer[] worlds = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers;

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
            else if (!footprintRefreshList.isEmpty())
            {
                for (NetworkRegistry.TargetPoint targetPoint : footprintRefreshList)
                {
                    WorldServer[] worlds = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers;

                    for (int i = 0; i < worlds.length; i++)
                    {
                        WorldServer world = worlds[i];

                        if (world.provider.dimensionId == targetPoint.dimension)
                        {
                            long chunkKey = ChunkCoordIntPair.chunkXZ2Int((int)targetPoint.x >> 4, (int)targetPoint.z >> 4);
                            Map<Long, List<Footprint>> footprintMap = TickHandlerServer.serverFootprintMap.get(world.provider.dimensionId);

                            if (footprintMap != null && !footprintMap.isEmpty())
                            {
                                List<Footprint> footprints = footprintMap.get(chunkKey);
                                if (footprints != null)
                                	GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_UPDATE_FOOTPRINT_LIST, new Object[] { chunkKey, footprints.toArray(new Footprint[footprints.size()]) }), targetPoint);
                            }
                        }
                    }
                }
            }

            if (tickCount % 20 == 0)
            {
                if (!playersRequestingMapData.isEmpty())
                {
                    ArrayList<EntityPlayerMP> copy = new ArrayList<EntityPlayerMP>(playersRequestingMapData);
                    for (EntityPlayerMP playerMP : copy)
                    {
                        GCPlayerStats stats = GCPlayerStats.get(playerMP);
                        ChunkCoordIntPair chunkCoordIntPair = new ChunkCoordIntPair((int)Math.floor(stats.coordsTeleportedFromX) >> 4, (int)Math.floor(stats.coordsTeleportedFromZ) >> 4);
                        BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);

                        for (int x0 = -12; x0 <= 12; x0++)
                        {
                            for (int z0 = -12; z0 <= 12; z0++)
                            {
                                Chunk chunk = MinecraftServer.getServer().worldServerForDimension(0).getChunkFromChunkCoords(chunkCoordIntPair.chunkXPos + x0, chunkCoordIntPair.chunkZPos + z0);

                                if (chunk != null)
                                {
                                    for (int z = 0; z < 16; z++)
                                    {
                                        for (int x = 0; x < 16; x++)
                                        {
                                            int l4 = chunk.getHeightValue(x, z) + 1;
                                            Block block = Blocks.air;
                                            int i5 = 0;

                                            if (l4 > 1)
                                            {
                                                do
                                                {
                                                    --l4;
                                                    block = chunk.getBlock(x, l4, z);
                                                    i5 = chunk.getBlockMetadata(x, l4, z);
                                                }
                                                while (block.getMapColor(i5) == MapColor.airColor && l4 > 0);
                                            }

                                            int col = block.getMapColor(i5).colorValue;
                                            image.setRGB(x + (x0 + 12) * 16, z + (z0 + 12) * 16, col);
                                        }
                                    }
                                }
                            }
                        }

                        try
                        {
                            File baseFolder = new File(MinecraftServer.getServer().worldServerForDimension(0).getChunkSaveLocation(), "galacticraft/overworldMap");

                            if (!baseFolder.exists())
                            {
                                baseFolder.mkdirs();
                            }

                            File outputFile = new File(baseFolder, "" + chunkCoordIntPair.chunkXPos + "_" + chunkCoordIntPair.chunkZPos + ".jpg");

                            if (!outputFile.exists() || (outputFile.canWrite() && outputFile.canRead()))
                            {
                                ImageIO.write(image, "jpg", outputFile);
                            }
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    playersRequestingMapData.removeAll(copy);
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
                for (ScheduledBlockChange change : changeList)
                {
                    if (change != null)
                    {
                        BlockVec3 changePosition = change.getChangePosition();
                        if (changePosition != null)
                        {
                            world.setBlock(changePosition.x, changePosition.y, changePosition.z, change.getChangeID(), change.getChangeMeta(), 2);
                        }
                    }
                }

                changeList.clear();
                TickHandlerServer.scheduledBlockChanges.remove(world.provider.dimensionId);
            }

            CopyOnWriteArrayList<BlockVec3> torchList = TickHandlerServer.scheduledTorchUpdates.get(world.provider.dimensionId);

            if (torchList != null && !torchList.isEmpty())
            {
                for (BlockVec3 torch : torchList)
                {
                    if (torch != null)
                    {
                        if (world.getBlock(torch.x, torch.y, torch.z) == GCBlocks.unlitTorch)
                        {
                            world.scheduleBlockUpdateWithPriority(torch.x, torch.y, torch.z, GCBlocks.unlitTorch, 2 + world.rand.nextInt(30), 0);
                        }
                        else if (world.getBlock(torch.x, torch.y, torch.z) == GCBlocks.unlitTorchLit)
                        {
                            world.scheduleBlockUpdateWithPriority(torch.x, torch.y, torch.z, GCBlocks.unlitTorchLit, 2 + world.rand.nextInt(30), 0);
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
                                final Integer dim = WorldUtil.getProviderForName(dimension.getPlanetToOrbit()).dimensionId;

                                WorldUtil.transferEntityToDimension(e, dim, world, false, null);
                            }
                        }
                    }
                }

                if (ConfigManagerCore.enableDebug && tickCount % 400 == 0)
                {
                	System.out.println("Space Station " + world.provider.dimensionId +": loaded tile entity count = " + world.loadedTileEntityList.size());
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
                        checkedThisTick.addAll(done.checked);
                    }
                }

                TickHandlerServer.edgeChecks.remove(world.provider.dimensionId);
            }
        }
    }
}
