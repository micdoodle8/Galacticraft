package micdoodle8.mods.galacticraft.callisto.client;

import micdoodle8.mods.galacticraft.API.IGalaxy;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;

public class GCCallistoMapPlanet implements IMapPlanet
{
	@Override
	public float getPlanetSize() 
	{
		return 18;
	}

	@Override
	public float getDistanceFromCenter() 
	{
		return 11000F / 15F;
	}

	@Override
	public float getPhaseShift() 
	{
		return 0;
	}

	@Override
	public float getStretchValue() 
	{
		return 0.4F;
	}

	@Override
	public IPlanetSlotRenderer getSlotRenderer() 
	{
		return new GCCallistoSlotRenderer();
	}

	@Override
	public IGalaxy getParentGalaxy() 
	{
		return GalacticraftCore.galaxyMilkyWay;
	}
}
