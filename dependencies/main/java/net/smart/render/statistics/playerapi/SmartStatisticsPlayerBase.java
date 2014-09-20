package net.smart.render.statistics.playerapi;

import api.player.client.*;

import net.smart.render.statistics.*;

public class SmartStatisticsPlayerBase extends ClientPlayerBase implements IEntityPlayerSP
{
	public SmartStatisticsPlayerBase(ClientPlayerAPI playerApi)
	{
		super(playerApi);
		statistics = new net.smart.render.statistics.SmartStatistics(player);
	}

	@Override
	public void afterMoveEntityWithHeading(float f, float f1)
	{
		statistics.calculateAllStats();
	}

	@Override
	public void afterUpdateRidden()
	{
		statistics.calculateRiddenStats();
	}

	public net.smart.render.statistics.SmartStatistics getStatistics()
	{
		return statistics;
	}

	public net.smart.render.statistics.SmartStatistics statistics;
}
