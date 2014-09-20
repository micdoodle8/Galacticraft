package net.smart.render.statistics;

import net.minecraft.client.*;

public abstract class SmartStatisticsContext
{
	protected static boolean calculateHorizontalStats = false;

	public static void setCalculateHorizontalStats(boolean flag)
	{
		calculateHorizontalStats = flag;
	}

	public static void onTickInGame()
	{
		Minecraft minecraft = Minecraft.getMinecraft();

		if(minecraft.theWorld != null && minecraft.theWorld.isRemote)
			SmartStatisticsFactory.handleMultiPlayerTick(minecraft);
	}
}
