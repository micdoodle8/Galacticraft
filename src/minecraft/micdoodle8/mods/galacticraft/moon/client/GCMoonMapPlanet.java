package micdoodle8.mods.galacticraft.moon.client;

import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;

public class GCMoonMapPlanet implements IMapPlanet
{
	@Override
	public float getPlanetSize() 
	{
		return 4;
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
		return 0.01F;
	}

	@Override
	public IPlanetSlotRenderer getSlotRenderer() 
	{
		return new GCMoonSlotRenderer();
	}
}
