package micdoodle8.mods.galacticraft.core.client;

import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;

public class GCCoreMapSun implements IMapPlanet
{
	@Override
	public boolean isMoon()
	{
		return false;
	}

	@Override
	public float getPlanetSize() 
	{
		return 15F * 108F;
	}

	@Override
	public float getDistanceFromCenter() 
	{
		return 0F;
	}

	@Override
	public float getPhaseShift() 
	{
		return 0;
	}

	@Override
	public float getStretchValue() 
	{
		return 0F;
	}

	@Override
	public IPlanetSlotRenderer getSlotRenderer() 
	{
		return new GCCoreSlotRendererSun();
	}
}
