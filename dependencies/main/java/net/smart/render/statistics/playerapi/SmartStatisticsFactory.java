package net.smart.render.statistics.playerapi;

import net.minecraft.entity.player.*;

import net.smart.render.statistics.*;

public class SmartStatisticsFactory extends net.smart.render.statistics.SmartStatisticsFactory
{
	public static void initialize()
	{
		if(!isInitialized())
			new SmartStatisticsFactory();
	}

	protected net.smart.render.statistics.SmartStatistics doGetInstance(EntityPlayer entityPlayer)
	{
		net.smart.render.statistics.SmartStatistics statistics = super.doGetInstance(entityPlayer);
		if(statistics != null)
			return statistics;

   		IEntityPlayerSP playerBase = SmartStatistics.getPlayerBase(entityPlayer);
   		if(playerBase != null)
   			return playerBase.getStatistics();

   		return null;
	}
}
