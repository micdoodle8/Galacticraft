package micdoodle8.mods.galacticraft.io.client;

import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;

public class GCIoMapPlanet implements IMapPlanet
{
	@Override
	public boolean isMoon()
	{
		return true;
	}

	@Override
	public float getPlanetSize() 
	{
		return 15;
	}

	@Override
	public float getDistanceFromCenter() 
	{
		return 1500F / 15F;
	}

	@Override
	public float getPhaseShift() 
	{
		return 0;
	}

	@Override
	public float getStretchValue() 
	{
		return 0;
	}

	@Override
	public IPlanetSlotRenderer getSlotRenderer() 
	{
		return new GCIoSlotRenderer();
	}
}
