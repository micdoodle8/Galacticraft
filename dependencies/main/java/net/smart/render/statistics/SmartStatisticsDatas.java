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