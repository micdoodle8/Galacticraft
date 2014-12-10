package net.smart.render.statistics;

public class SmartStatisticsDatas
{
	public final SmartStatisticsData horizontal = new SmartStatisticsData();
	public final SmartStatisticsData vertical = new SmartStatisticsData();
	public final SmartStatisticsData all = new SmartStatisticsData();

	private float renderPartialTicks;

	public float getTotalHorizontalDistance()
	{
		return horizontal.getTotalDistance(renderPartialTicks);
	}

	public float getTotalVerticalDistance()
	{
		return vertical.getTotalDistance(renderPartialTicks);
	}

	public float getTotalDistance()
	{
		return all.getTotalDistance(renderPartialTicks);
	}

	public float getCurrentHorizontalSpeed()
	{
		return horizontal.getCurrentSpeed(renderPartialTicks);
	}

	public float getCurrentVerticalSpeed()
	{
		return vertical.getCurrentSpeed(renderPartialTicks);
	}

	public float getCurrentSpeed()
	{
		return all.getCurrentSpeed(renderPartialTicks);
	}

	public void setReady(float renderPartialTicks)
	{
		this.renderPartialTicks = renderPartialTicks;
	}

	public boolean isReady()
	{
		return !Float.isNaN(renderPartialTicks);
	}

	public void initialize(SmartStatisticsDatas previous)
	{
		renderPartialTicks = Float.NaN;

		horizontal.initialize(previous.horizontal);
		vertical.initialize(previous.vertical);
		all.initialize(previous.all);
	}
}