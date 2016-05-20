// ==================================================================
// This file is part of Smart Render.
//
// Smart Render is free software: you can redistribute it and/or
// modify it under the terms of the GNU General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// Smart Render is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Smart Render. If not, see <http://www.gnu.org/licenses/>.
// ==================================================================

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