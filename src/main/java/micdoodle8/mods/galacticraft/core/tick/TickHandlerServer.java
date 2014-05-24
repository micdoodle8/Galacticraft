package micdoodle8.mods.galacticraft.core.tick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import micdoodle8.mods.galacticraft.api.transmission.compatibility.UniversalNetwork;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.dimension.WorldDataSpaceRaces;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.oxygen.ThreadFindSeal;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.wrappers.Footprint;
import micdoodle8.mods.galacticraft.core.wrappers.ScheduledBlockChange;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;

/**
 * GCCoreTickHandlerServer.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class TickHandlerServer
{
	private static Map<Integer, CopyOnWriteArrayList<ScheduledBlockChange>> scheduledBlockChanges = new ConcurrentHashMap<Integer, CopyOnWriteArrayList<ScheduledBlockChange>>();
	private static Map<Integer, CopyOnWriteArrayList<BlockVec3>> scheduledTorchUpdates = new ConcurrentHashMap<Integer, CopyOnWriteArrayList<BlockVec3>>();
	private static Map<Integer, List<BlockVec3>> edgeChecks = new HashMap<Integer, List<BlockVec3>>();
	private static LinkedList<UniversalNetwork> networkTicks = new LinkedList<UniversalNetwork>();
	private static Map<Integer, List<Footprint>> footprintList = new HashMap<Integer, List<Footprint>>();
	public static WorldDataSpaceRaces spaceRaceData = null;
	private long tickCount;
	
	public static void addFootprint(Footprint print, int dimID)
	{
		List<Footprint> footprints = TickHandlerServer.footprintList.get(dimID);

		if (footprints == null)
		{
			footprints = new ArrayList<Footprint>();
		}

		footprints.add(print);
		footprintList.put(dimID, footprints);
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
				if (test.equals(change.getChangePosition())) return true;
			}
		}
		return false;
	}
	
	public static void removeNetworkTick(UniversalNetwork grid)
	{
		TickHandlerServer.networkTicks.remove(grid);
	}

	@SubscribeEvent
	public void onServerTick(ServerTickEvent event)
	{
		if (event.phase == Phase.START)
		{
			if (spaceRaceData == null)
			{
				World world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(0);
				spaceRaceData = (WorldDataSpaceRaces) world.mapStorage.loadData(WorldDataSpaceRaces.class, WorldDataSpaceRaces.saveDataID);

		        if (spaceRaceData == null)
		        {
		        	spaceRaceData = new WorldDataSpaceRaces(WorldDataSpaceRaces.saveDataID);
		            world.mapStorage.setData(WorldDataSpaceRaces.saveDataID, this.spaceRaceData);
		        }
			}

			SpaceRaceManager.tick();

			if (tickCount % 100 == 0)
			{
				WorldServer[] worlds = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers;

				for (int i = 0; i < worlds.length; i++)
				{
					List<Footprint> footprints = footprintList.get(worlds[i].provider.dimensionId);

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

						footprints.removeAll(toRemove);
						footprintList.put(worlds[i].provider.dimensionId, footprints);	

						GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_FOOTPRINT_LIST, new Object[] { footprints.toArray(new Footprint[footprints.size()]) }), worlds[i].provider.dimensionId);
					}
				}
			}

			tickCount++;

			if (tickCount >= Long.MAX_VALUE)
			{
				tickCount = 0;
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
					if (change != null && change.getChangePosition() != null)
					{
						world.setBlock(change.getChangePosition().x, change.getChangePosition().y, change.getChangePosition().z, change.getChangeID(), change.getChangeMeta(), change.getChangeFlag());
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
							world.scheduleBlockUpdateWithPriority(torch.x, torch.y, torch.z, GCBlocks.unlitTorch, 2 + world.rand.nextInt(30), 0);
						else if (world.getBlock(torch.x, torch.y, torch.z) == GCBlocks.unlitTorchLit)
							world.scheduleBlockUpdateWithPriority(torch.x, torch.y, torch.z, GCBlocks.unlitTorchLit, 2 + world.rand.nextInt(30), 0);
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
			}
		}
	}
	
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		for (UniversalNetwork grid : TickHandlerServer.networkTicks)
		{
			grid.tickEnd();
		}
		TickHandlerServer.networkTicks.clear();
		
		if (type.equals(EnumSet.of(TickType.WORLD)))
		{
			final WorldServer world = (WorldServer) tickData[0];

			List<BlockVec3> edgesList = TickHandlerServer.edgeChecks.get(world.provider.dimensionId);
			final HashSet<BlockVec3> checkedThisTick = new HashSet();
			
			if (edgesList != null && !edgesList.isEmpty())
			{
				for (BlockVec3 edgeBlock : edgesList)
				{
					if (edgeBlock != null && !checkedThisTick.contains(edgeBlock)) 
					{
						if (TickHandlerServer.scheduledForChange(world.provider.dimensionId, edgeBlock)) continue;

						ThreadFindSeal done = new ThreadFindSeal(world, edgeBlock, 2000, new ArrayList<TileEntityOxygenSealer>());
						checkedThisTick.addAll(done.checked);
					}
				}
	
				edgesList.clear();
				TickHandlerServer.edgeChecks.remove(world.provider.dimensionId);
			}
		}
	}
}
