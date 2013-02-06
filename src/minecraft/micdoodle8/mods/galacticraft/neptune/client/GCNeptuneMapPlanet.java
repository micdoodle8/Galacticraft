package micdoodle8.mods.galacticraft.neptune.client;

import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;

public class GCNeptuneMapPlanet implements IMapPlanet
{
	@Override
	public float getPlanetSize() 
	{
		return 15F * 3.88F;
	}

	@Override
	public float getDistanceFromCenter() 
	{
		return 44880F;
	}

	@Override
	public float getPhaseShift() 
	{
		return 720F;
	}

	@Override
	public float getStretchValue() 
	{
		return 164.79F;
	}

	@Override
	public IPlanetSlotRenderer getSlotRenderer() 
	{
		return new GCNeptuneSlotRenderer();
	}
}
