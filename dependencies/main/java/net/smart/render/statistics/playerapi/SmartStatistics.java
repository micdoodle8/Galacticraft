package net.smart.render.statistics.playerapi;

import net.minecraft.client.entity.*;
import net.minecraft.entity.player.*;

import api.player.client.*;

import net.smart.render.*;
import net.smart.render.statistics.*;

public abstract class SmartStatistics
{
	public final static String ID = SmartRenderInfo.ModName;
	
	public static void register()
	{
		ClientPlayerAPI.register(ID, SmartStatisticsPlayerBase.class);
	}

	public static IEntityPlayerSP getPlayerBase(EntityPlayer entityPlayer)
	{
		if(entityPlayer instanceof EntityPlayerSP)
			return (SmartStatisticsPlayerBase)((IClientPlayerAPI)entityPlayer).getClientPlayerBase(ID);
		return null;
	}
}
