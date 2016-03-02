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