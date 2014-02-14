package micdoodle8.mods.galacticraft.core.tick;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.wrappers.ScheduledBlockChange;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;

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
	private static Map<Integer, List<ScheduledBlockChange>> scheduledBlockChanges = new ConcurrentHashMap<Integer, List<ScheduledBlockChange>>();
	
	public static void scheduleNewBlockChange(int dimID, ScheduledBlockChange change)
	{
		List<ScheduledBlockChange> changeList = scheduledBlockChanges.get(dimID);
		
		if (changeList == null)
		{
			changeList = new ArrayList<ScheduledBlockChange>();
		}
		
		changeList.add(change);
		scheduledBlockChanges.put(dimID, changeList);
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		if (type.equals(EnumSet.of(TickType.WORLD)))
		{
			final WorldServer world = (WorldServer) tickData[0];

			List<ScheduledBlockChange> scheduledChanges = scheduledBlockChanges.get(world.provider.dimensionId);
			
			if (scheduledChanges != null && !scheduledChanges.isEmpty())
			{
				for (Iterator<ScheduledBlockChange> it = scheduledChanges.iterator(); it.hasNext(); )
				{
					ScheduledBlockChange change = it.next();
					world.setBlock(change.getChangePosition().intX(), change.getChangePosition().intY(), change.getChangePosition().intZ(), change.getChangeBlock(), change.getChangeMeta(), change.getChangeFlag());
					it.remove();
				}
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
