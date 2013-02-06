package micdoodle8.mods.galacticraft.europa.client;

import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;

public class GCEuropaMapPlanet implements IMapPlanet
{
	@Override
	public float getPlanetSize() 
	{
		return 2;
	}

	@Override
	public float getDistanceFromCenter() 
	{
		return 7000F / 15F;
	}

	@Override
	public float getPhaseShift() 
	{
		return 0;
	}

	@Override
	public float getStretchValue() 
	{
		return 0.1F;
	}

	@Override
	public IPlanetSlotRenderer getSlotRenderer() 
	{
		return new GCEuropaSlotRenderer();
	}
}
