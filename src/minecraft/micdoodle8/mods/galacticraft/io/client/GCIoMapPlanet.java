package micdoodle8.mods.galacticraft.io.client;

import micdoodle8.mods.galacticraft.API.IGalaxy;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;

public class GCIoMapPlanet implements IMapPlanet
{
	@Override
	public float getPlanetSize()
	{
		return 10;
	}

	@Override
	public float getDistanceFromCenter()
	{
		return 5500F / 15F;
	}

	@Override
	public float getPhaseShift()
	{
		return 0;
	}

	@Override
	public float getStretchValue()
	{
		return 0.05F;
	}

	@Override
	public IPlanetSlotRenderer getSlotRenderer()
	{
		return new GCIoSlotRenderer();
	}

	@Override
	public IGalaxy getParentGalaxy()
	{
		return GalacticraftCore.galaxyMilkyWay;
	}
}
