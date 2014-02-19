package micdoodle8.mods.galacticraft.core.tick;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.dimension.WorldDataSpaceRaces;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.wrappers.ScheduledBlockChange;
import net.minecraft.entity.Entity;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
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
	private static Map<Integer, List<ScheduledBlockChange>> scheduledBlockChanges = new ConcurrentHashMap<Integer, List<ScheduledBlockChange>>();
	public static WorldDataSpaceRaces spaceRaceData = null;

	public static void scheduleNewBlockChange(int dimID, ScheduledBlockChange change)
	{
		List<ScheduledBlockChange> changeList = TickHandlerServer.scheduledBlockChanges.get(dimID);

		if (changeList == null)
		{
			changeList = new ArrayList<ScheduledBlockChange>();
		}

		changeList.add(change);
		TickHandlerServer.scheduledBlockChanges.put(dimID, changeList);
	}
	
	@SubscribeEvent
	public void onServerTick(ServerTickEvent event)
	{
		if (spaceRaceData == null)
		{
			World world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(0);
			world.mapStorage.loadData(WorldDataSpaceRaces.class, WorldDataSpaceRaces.saveDataID);
		}
		
		SpaceRaceManager.tick();
	}

	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event)
	{
		final WorldServer world = (WorldServer) event.world;

		List<ScheduledBlockChange> scheduledChanges = TickHandlerServer.scheduledBlockChanges.get(world.provider.dimensionId);

		if (scheduledChanges != null && !scheduledChanges.isEmpty())
		{
			for (Iterator<ScheduledBlockChange> it = scheduledChanges.iterator(); it.hasNext();)
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
