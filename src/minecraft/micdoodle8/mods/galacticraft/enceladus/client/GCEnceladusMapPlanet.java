package micdoodle8.mods.galacticraft.enceladus.client;

import micdoodle8.mods.galacticraft.API.IGalaxy;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;

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

	@Override
	public IGalaxy getParentGalaxy()
	{
		return GalacticraftCore.galaxyMilkyWay;
	}
}
