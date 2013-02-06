package micdoodle8.mods.galacticraft.enceladus.client;

import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;

public class GCEnceladusMapPlanet implements IMapPlanet
{
	@Override
	public float getPlanetSize() 
	{
		return 15;
	}

	@Override
	public float getDistanceFromCenter() 
	{
		return 7500F / 15F;
	}

	@Override
	public float getPhaseShift() 
	{
		return 0;
	}

	@Override
	public float getStretchValue() 
	{
		return 2;
	}

	@Override
	public IPlanetSlotRenderer getSlotRenderer() 
	{
		return new GCEnceladusSlotRenderer();
	}
}
