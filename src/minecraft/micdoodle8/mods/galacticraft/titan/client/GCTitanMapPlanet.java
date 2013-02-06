package micdoodle8.mods.galacticraft.titan.client;

import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;

public class GCTitanMapPlanet implements IMapPlanet
{
	@Override
	public float getPlanetSize() 
	{
		return 15;
	}

	@Override
	public float getDistanceFromCenter() 
	{
		return 15000F / 15F;
	}

	@Override
	public float getPhaseShift() 
	{
		return 0;
	}

	@Override
	public float getStretchValue() 
	{
		return 6;
	}

	@Override
	public IPlanetSlotRenderer getSlotRenderer() 
	{
		return new GCTitanSlotRenderer();
	}
}
