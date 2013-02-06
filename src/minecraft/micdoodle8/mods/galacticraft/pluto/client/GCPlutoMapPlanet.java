package micdoodle8.mods.galacticraft.pluto.client;

import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;

public class GCPlutoMapPlanet implements IMapPlanet
{
	@Override
	public float getPlanetSize() 
	{
		return 15F / 5.55F;
	}

	@Override
	public float getDistanceFromCenter() 
	{
		return 59100F;
	}

	@Override
	public float getPhaseShift() 
	{
		return 2780F;
	}

	@Override
	public float getStretchValue() 
	{
		return 248F;
	}

	@Override
	public IPlanetSlotRenderer getSlotRenderer() 
	{
		return new GCPlutoSlotRenderer();
	}
}
