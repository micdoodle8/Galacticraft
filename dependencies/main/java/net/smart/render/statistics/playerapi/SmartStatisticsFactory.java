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

import net.minecraft.entity.player.*;

import net.smart.render.statistics.*;

public class SmartStatisticsFactory extends net.smart.render.statistics.SmartStatisticsFactory
{
	public static void initialize()
	{
		if(!isInitialized())
			new SmartStatisticsFactory();
	}

	@Override
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