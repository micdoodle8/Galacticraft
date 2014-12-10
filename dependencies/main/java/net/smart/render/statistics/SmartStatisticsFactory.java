package net.smart.render.statistics;

import java.util.*;

import net.minecraft.client.*;
import net.minecraft.client.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;

public class SmartStatisticsFactory
{
	private static SmartStatisticsFactory factory;

	private Hashtable<Integer, SmartStatisticsOther> otherStatistics;

	public SmartStatisticsFactory()
	{
		if(factory != null)
			throw new RuntimeException("FATAL: Can only create one instance of type 'StatisticsFactory'");
		factory = this;
	}

	protected static boolean isInitialized()
	{
		return factory != null;
	}

	public static void initialize()
	{
		if(!isInitialized())
			new SmartStatisticsFactory();
	}

	public static void handleMultiPlayerTick(Minecraft minecraft)
	{
		factory.doHandleMultiPlayerTick(minecraft);
	}

	public static SmartStatistics getInstance(EntityPlayer entityPlayer)
	{
		return factory.doGetInstance(entityPlayer);
	}

	public static SmartStatisticsOther getOtherStatistics(int entityId)
	{
		return factory.doGetOtherStatistics(entityId);
	}

	public static SmartStatisticsOther getOtherStatistics(EntityOtherPlayerMP entity)
	{
		return factory.doGetOtherStatistics(entity);
	}

	protected void doHandleMultiPlayerTick(Minecraft minecraft)
	{
		Iterator<?> others = minecraft.theWorld.playerEntities.iterator();
		while(others.hasNext())
		{
			Entity player = (Entity)others.next();
			if(player instanceof EntityOtherPlayerMP)
			{
				EntityOtherPlayerMP otherPlayer = (EntityOtherPlayerMP)player;
				SmartStatisticsOther statistics = doGetOtherStatistics(otherPlayer);
				statistics.calculateAllStats();
				statistics.foundAlive = true;
			}
		}

		if(otherStatistics == null || otherStatistics.isEmpty())
			return;

		Iterator<Integer> entityIds = otherStatistics.keySet().iterator();
		while(entityIds.hasNext())
		{
			Integer entityId = entityIds.next();
			SmartStatisticsOther statistics = otherStatistics.get(entityId);
			if(statistics.foundAlive)
				statistics.foundAlive = false;
			else
				entityIds.remove();
		}
	}

	protected SmartStatistics doGetInstance(EntityPlayer entityPlayer)
	{
		if(entityPlayer instanceof EntityOtherPlayerMP)
			return doGetOtherStatistics(entityPlayer.getEntityId());
		else if(entityPlayer instanceof IEntityPlayerSP)
			return ((IEntityPlayerSP)entityPlayer).getStatistics();
		return null;
	}

	protected SmartStatisticsOther doGetOtherStatistics(int entityId)
	{
		SmartStatisticsOther statistics = tryGetOtherStatistics(entityId);
		if(statistics == null)
		{
			Entity entity = Minecraft.getMinecraft().theWorld.getEntityByID(entityId);
			if(entity != null && entity instanceof EntityOtherPlayerMP)
				statistics = addOtherStatistics((EntityOtherPlayerMP)entity);
		}
		return statistics;
	}

	protected SmartStatisticsOther doGetOtherStatistics(EntityOtherPlayerMP entity)
	{
		SmartStatisticsOther statistics = tryGetOtherStatistics(entity.getEntityId());
		if(statistics == null)
			statistics = addOtherStatistics(entity);
		return statistics;
	}

	protected final SmartStatisticsOther tryGetOtherStatistics(int entityId)
	{
		if(otherStatistics == null)
			otherStatistics = new Hashtable<Integer, SmartStatisticsOther>();
		return otherStatistics.get(entityId);
	}

	protected final SmartStatisticsOther addOtherStatistics(EntityOtherPlayerMP entity)
	{
		SmartStatisticsOther statistics = new SmartStatisticsOther(entity);
		otherStatistics.put(entity.getEntityId(), statistics);
		return statistics;
	}
}
