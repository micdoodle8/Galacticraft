package micdoodle8.mods.galacticraft.europa.client;

import micdoodle8.mods.galacticraft.API.IGalaxy;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;

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

	@Override
	public IGalaxy getParentGalaxy()
	{
		return GalacticraftCore.galaxyMilkyWay;
	}
}
