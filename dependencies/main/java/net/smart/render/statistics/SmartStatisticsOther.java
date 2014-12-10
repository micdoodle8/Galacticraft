package net.smart.render.statistics;

import net.minecraft.client.entity.*;

public class SmartStatisticsOther extends SmartStatistics
{
	public boolean foundAlive;

	public SmartStatisticsOther(EntityOtherPlayerMP sp)
	{
		super(sp);
	}

	protected void calculateHorizontalStats(double diffX, double diffY)
	{
	}
}
