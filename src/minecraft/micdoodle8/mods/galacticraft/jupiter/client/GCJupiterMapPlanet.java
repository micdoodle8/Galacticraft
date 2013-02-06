package micdoodle8.mods.galacticraft.jupiter.client;

import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;

public class GCJupiterMapPlanet implements IMapPlanet
{
	@Override
	public float getPlanetSize() 
	{
		return 15F * 11.1F;
	}

	@Override
	public float getDistanceFromCenter() 
	{
		return 7780F;
	}

	@Override
	public float getPhaseShift() 
	{
		return 2350F;
	}

	@Override
	public float getStretchValue() 
	{
		return 11.86F;
	}

	@Override
	public IPlanetSlotRenderer getSlotRenderer() 
	{
		return new GCJupiterSlotRenderer();
	}
}
