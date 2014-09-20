package net.smart.render.statistics;

import net.minecraft.entity.player.*;

public class SmartStatisticsData
{
	public float prevLegYaw;
	public float legYaw;
	public float total;

	public float getCurrentSpeed(float renderPartialTicks)
	{
		return Math.min(1.0F, prevLegYaw + (legYaw - prevLegYaw) * renderPartialTicks);
	}

	public float getTotalDistance(float renderPartialTicks)
	{
		return total - legYaw * (1.0F - renderPartialTicks);
	}

	public void initialize(SmartStatisticsData previous)
	{
		prevLegYaw = previous.legYaw;
		legYaw = previous.legYaw;
		total = previous.total;
	}

	public float calcualte(float distance)
	{
		distance = distance * 4F;

		legYaw += (distance - legYaw) * 0.4F;
		total += legYaw;

		return distance;
	}

	public void apply(EntityPlayer sp)
	{
		sp.prevLimbSwingAmount = prevLegYaw;
		sp.limbSwingAmount = legYaw;
		sp.limbSwing = total;
	}
}