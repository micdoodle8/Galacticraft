package micdoodle8.mods.galacticraft.uranus.client;

import micdoodle8.mods.galacticraft.API.IGalaxy;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;

public class GCUranusMapPlanet implements IMapPlanet
{
	@Override
	public float getPlanetSize()
	{
		return 60F;
	}

	@Override
	public float getDistanceFromCenter()
	{
		return 28670F;
	}

	@Override
	public float getPhaseShift()
	{
		return 2780F;
	}

	@Override
	public float getStretchValue()
	{
		return 84.32F;
	}

	@Override
	public IPlanetSlotRenderer getSlotRenderer()
	{
		return new GCUranusSlotRenderer();
	}

	@Override
	public IGalaxy getParentGalaxy()
	{
		return GalacticraftCore.galaxyMilkyWay;
	}
}
