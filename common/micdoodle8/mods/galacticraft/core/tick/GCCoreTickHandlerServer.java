package micdoodle8.mods.galacticraft.core.tick;

import java.util.ArrayList;
import java.util.EnumSet;
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
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.oxygen.OxygenPressureProtocol;
import micdoodle8.mods.galacticraft.core.oxygen.ThreadFindSeal;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.wrappers.ScheduledBlockChange;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

/**
 * GCCoreTickHandlerServer.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreTickHandlerServer implements ITickHandler
{
	private static Map<Integer, CopyOnWriteArrayList<ScheduledBlockChange>> scheduledBlockChanges = new ConcurrentHashMap<Integer, CopyOnWriteArrayList<ScheduledBlockChange>>();
	private static Map<Integer, CopyOnWriteArrayList<BlockVec3>> scheduledTorchUpdates = new ConcurrentHashMap<Integer, CopyOnWriteArrayList<BlockVec3>>();
	private static Map<Integer, List<BlockVec3>> edgeChecks = new HashMap<Integer, List<BlockVec3>>();
	private static LinkedList<UniversalNetwork> networkTicks = new LinkedList<UniversalNetwork>();
	
	public static void scheduleNewBlockChange(int dimID, ScheduledBlockChange change)
	{
		CopyOnWriteArrayList<ScheduledBlockChange> changeList = GCCoreTickHandlerServer.scheduledBlockChanges.get(dimID);
		
		if (changeList == null)
		{
			changeList = new CopyOnWriteArrayList<ScheduledBlockChange>();
		}

		changeList.add(change);
		GCCoreTickHandlerServer.scheduledBlockChanges.put(dimID, changeList);
	}

	public static void scheduleNewBlockChange(int dimID, List<ScheduledBlockChange> changeAdd)
	{
		CopyOnWriteArrayList<ScheduledBlockChange> changeList = GCCoreTickHandlerServer.scheduledBlockChanges.get(dimID);

		if (changeList == null)
		{
			changeList = new CopyOnWriteArrayList<ScheduledBlockChange>();
		}

		changeList.addAll(changeAdd);
		GCCoreTickHandlerServer.scheduledBlockChanges.put(dimID, changeList);
	}

	public static void scheduleNewTorchUpdate(int dimID, List<BlockVec3> torches)
	{
		CopyOnWriteArrayList<BlockVec3> updateList = GCCoreTickHandlerServer.scheduledTorchUpdates.get(dimID);

		if (updateList == null)
		{
			updateList = new CopyOnWriteArrayList<BlockVec3>();
		}

		updateList.addAll(torches);
		GCCoreTickHandlerServer.scheduledTorchUpdates.put(dimID, updateList);
	}

	public static void scheduleNewEdgeCheck(int dimID, BlockVec3 edgeBlock)
	{
		List<BlockVec3> updateList = GCCoreTickHandlerServer.edgeChecks.get(dimID);

		if (updateList == null)
		{
			updateList = new ArrayList<BlockVec3>();
		}

		updateList.add(edgeBlock);
		GCCoreTickHandlerServer.edgeChecks.put(dimID, updateList);
	}

	public static boolean scheduledForChange(int dimID, BlockVec3 test)
	{
		CopyOnWriteArrayList<ScheduledBlockChange> changeList = GCCoreTickHandlerServer.scheduledBlockChanges.get(dimID);

		if (changeList != null)
		{
			for (ScheduledBlockChange change : changeList)
			{
				if (test.equals(change.getChangePosition())) return true;
			}
		}
		return false;
	}
	
	public static void scheduleNetworkTick(UniversalNetwork grid)
	{
		GCCoreTickHandlerServer.networkTicks.add(grid);
	}

	public static void removeNetworkTick(UniversalNetwork grid)
	{
		GCCoreTickHandlerServer.networkTicks.remove(grid);
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		UniversalNetwork.tickCount++;
		
		if (type.equals(EnumSet.of(TickType.WORLD)))
		{
			final WorldServer world = (WorldServer) tickData[0];

			CopyOnWriteArrayList<ScheduledBlockChange> changeList = GCCoreTickHandlerServer.scheduledBlockChanges.get(world.provider.dimensionId);

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
				GCCoreTickHandlerServer.scheduledBlockChanges.remove(world.provider.dimensionId);
			}

			CopyOnWriteArrayList<BlockVec3> torchList = GCCoreTickHandlerServer.scheduledTorchUpdates.get(world.provider.dimensionId);
			
			if (torchList != null && !torchList.isEmpty())
			{
				for (BlockVec3 torch : torchList)
				{
					if (torch != null)
					{
						if (world.getBlockId(torch.x, torch.y, torch.z) == GCCoreBlocks.unlitTorch.blockID)
							world.scheduleBlockUpdateWithPriority(torch.x, torch.y, torch.z, GCCoreBlocks.unlitTorch.blockID, 2 + world.rand.nextInt(30), 0);
						else if (world.getBlockId(torch.x, torch.y, torch.z) == GCCoreBlocks.unlitTorchLit.blockID)
							world.scheduleBlockUpdateWithPriority(torch.x, torch.y, torch.z, GCCoreBlocks.unlitTorchLit.blockID, 2 + world.rand.nextInt(30), 0);
					}
				}

				torchList.clear();
				GCCoreTickHandlerServer.scheduledTorchUpdates.remove(world.provider.dimensionId);
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
		int maxPasses = 10;
		while (!GCCoreTickHandlerServer.networkTicks.isEmpty())
		{
			LinkedList<UniversalNetwork> pass = new LinkedList();
			pass.addAll(GCCoreTickHandlerServer.networkTicks);
			GCCoreTickHandlerServer.networkTicks.clear();		
			for (UniversalNetwork grid : pass)
			{
				grid.tickEnd();
			}
			if (--maxPasses<=0) break;
		}
		
		if (type.equals(EnumSet.of(TickType.WORLD)))
		{
			final WorldServer world = (WorldServer) tickData[0];

			List<BlockVec3> edgesList = GCCoreTickHandlerServer.edgeChecks.get(world.provider.dimensionId);
			final HashSet<BlockVec3> checkedThisTick = new HashSet();
			
			if (edgesList != null && !edgesList.isEmpty())
			{
				for (BlockVec3 edgeBlock : edgesList)
				{
					if (edgeBlock != null && !checkedThisTick.contains(edgeBlock)) 
					{
						if (GCCoreTickHandlerServer.scheduledForChange(world.provider.dimensionId, edgeBlock)) continue;

						ThreadFindSeal done = new ThreadFindSeal(world, edgeBlock, 2000, new ArrayList<GCCoreTileEntityOxygenSealer>());
						checkedThisTick.addAll(done.checked);
					}
				}
	
				edgesList.clear();
				GCCoreTickHandlerServer.edgeChecks.remove(world.provider.dimensionId);
			}
		}
	}

	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.WORLD);
	}

	@Override
	public String getLabel()
	{
		return "Galacticraft Core Common";
	}
}
