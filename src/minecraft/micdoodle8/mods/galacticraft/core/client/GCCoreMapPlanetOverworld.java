package micdoodle8.mods.galacticraft.core.client;

import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;

public class GCCoreMapPlanetOverworld implements IMapPlanet
{
	@Override
	public boolean isMoon()
	{
		return false;
	}

	@Override
	public float getPlanetSize() 
	{
		return 15;
	}

	@Override
	public float getDistanceFromCenter() 
	{
		return 1500F;
	}

	@Override
	public float getPhaseShift() 
	{
		return 0;
	}

	@Override
	public float getStretchValue() 
	{
		return 1F;
	}

	@Override
	public IPlanetSlotRenderer getSlotRenderer() 
	{
		return new GCCoreSlotRendererOverworld();
	}
}
