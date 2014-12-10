package net.smart.render.statistics;

import net.minecraft.entity.player.*;
import net.minecraft.util.*;

public class SmartStatistics extends SmartStatisticsContext
{
	private final EntityPlayer sp;

	private float tickDistance;

	public int ticksRiding;
	public float prevHorizontalAngle = Float.NaN;

	private final static SmartStatisticsDatas dummy = new SmartStatisticsDatas(); 
	private final SmartStatisticsDatas[] datas = new SmartStatisticsDatas[10];
	private int currentDataIndex = -1;

	public SmartStatistics(EntityPlayer sp)
	{
		this.sp = sp;
	}

	public void calculateAllStats()
	{
		double diffX = sp.posX - sp.prevPosX;
		double diffY = sp.posY - sp.prevPosY;
		double diffZ = sp.posZ - sp.prevPosZ;

		SmartStatisticsDatas previous = get();

		currentDataIndex++;
		if(currentDataIndex >= datas.length)
			currentDataIndex = 0;

		SmartStatisticsDatas data = datas[currentDataIndex];
		if(data == null)
			data = datas[currentDataIndex] = new SmartStatisticsDatas();
		data.initialize(previous);

		data.horizontal.calcualte(MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ));
		data.vertical.calcualte((float)Math.abs(diffY));
		tickDistance = data.all.calcualte(MathHelper.sqrt_double(diffX * diffX + diffY * diffY + diffZ * diffZ));

		if(calculateHorizontalStats)
			data.horizontal.apply(sp);
	}

	public void calculateRiddenStats()
	{
		ticksRiding++;
	}

	public float getHorizontalPrevLegYaw() { return sp.prevLimbSwingAmount; }
	public float getHorizontalLegYaw() { return sp.limbSwingAmount; }
	public float getHorizontalTotal() { return sp.limbSwing; }

	public float getVerticalPrevLegYaw() { return datas[currentDataIndex].vertical.prevLegYaw; }
	public float getVerticalLegYaw() { return datas[currentDataIndex].vertical.legYaw; }
	public float getVerticalTotal() { return datas[currentDataIndex].vertical.total; }

	public float getAllPrevLegYaw() { return datas[currentDataIndex].all.prevLegYaw; }
	public float getAllLegYaw() { return datas[currentDataIndex].all.legYaw; }
	public float getAllTotal() { return datas[currentDataIndex].all.total; }

	public float getTickDistance() { return tickDistance; }

	public float getTotalHorizontalDistance(float renderPartialTicks)
	{
		return get(renderPartialTicks).getTotalHorizontalDistance();
	}

	public float getTotalVerticalDistance(float renderPartialTicks)
	{
		return get(renderPartialTicks).getTotalVerticalDistance();
	}

	public float getTotalDistance(float renderPartialTicks)
	{
		return get(renderPartialTicks).getTotalDistance();
	}

	public float getCurrentHorizontalSpeed(float renderPartialTicks)
	{
		return get(renderPartialTicks).getCurrentHorizontalSpeed();
	}

	public float getCurrentVerticalSpeed(float renderPartialTicks)
	{
		return get(renderPartialTicks).getCurrentVerticalSpeed();
	}

	public float getCurrentSpeed(float renderPartialTicks)
	{
		return get(renderPartialTicks).getCurrentSpeed();
	}

	private SmartStatisticsDatas get()
	{
		return currentDataIndex == -1 ? dummy : datas[currentDataIndex];
	}

	private SmartStatisticsDatas get(float renderPartialTicks)
	{
		SmartStatisticsDatas data = get();;
		data.setReady(renderPartialTicks);
		return data;
	}

	public float getCurrentHorizontalSpeedFlattened(float renderPartialTicks, int strength)
	{
		strength = Math.min(strength, datas.length);
		if(strength<0)
			strength = datas.length;

		get(renderPartialTicks);
		float sum = 0;
		int count = 0;
		for(int i=0, dataIndex=currentDataIndex; i<strength; i++, dataIndex--)
		{
			if(dataIndex<0)
				dataIndex = datas.length - 1;
			SmartStatisticsDatas data = datas[dataIndex];
			if(data == null || !data.isReady())
				break;

			sum += data.getCurrentHorizontalSpeed();
			count++;
		}

		return sum / count;
	}

	public float getCurrentVerticalSpeedFlattened(float renderPartialTicks, int strength)
	{
		strength = Math.min(strength, datas.length);
		if(strength<0)
			strength = datas.length;

		get(renderPartialTicks);
		float sum = 0;
		int count = 0;
		for(int i=0, dataIndex=currentDataIndex; i<strength; i++, dataIndex--)
		{
			if(dataIndex<0)
				dataIndex = datas.length - 1;
			SmartStatisticsDatas data = datas[dataIndex];
			if(data == null || !data.isReady())
				break;

			sum += data.getCurrentVerticalSpeed();
			count++;
		}

		return sum / count;
	}

	public float getCurrentSpeedFlattened(float renderPartialTicks, int strength)
	{
		strength = Math.min(strength, datas.length);
		if(strength<0)
			strength = datas.length;

		get(renderPartialTicks);
		float sum = 0;
		int count = 0;
		for(int i=0, dataIndex=currentDataIndex; i<strength; i++, dataIndex--)
		{
			if(dataIndex<0)
				dataIndex = datas.length - 1;
			SmartStatisticsDatas data = datas[dataIndex];
			if(data == null || !data.isReady())
				break;

			sum += data.getCurrentSpeed();
			count++;
		}

		return sum / count;
	}
}