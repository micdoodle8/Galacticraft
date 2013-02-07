package micdoodle8.mods.galacticraft.mars.client;

import micdoodle8.mods.galacticraft.API.IGalaxy;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;

public class GCMarsMapPlanet implements IMapPlanet
{
	@Override
	public float getPlanetSize() 
	{
		return 15F / 1.88F;
	}

	@Override
	public float getDistanceFromCenter() 
	{
		return 2280F;
	}

	@Override
	public float getPhaseShift() 
	{
		return 2880F / 6F;
	}

	@Override
	public float getStretchValue() 
	{
		return 1.88F;
	}

	@Override
	public IPlanetSlotRenderer getSlotRenderer() 
	{
		return new GCMarsSlotRenderer();
	}

	@Override
	public IGalaxy getParentGalaxy() 
	{
		return GalacticraftCore.galaxyMilkyWay;
	}
}
