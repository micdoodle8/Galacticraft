package micdoodle8.mods.galacticraft.core.client;

import micdoodle8.mods.galacticraft.api.world.ICelestialBodyRenderer;
import micdoodle8.mods.galacticraft.api.world.IGalaxy;
import micdoodle8.mods.galacticraft.api.world.IMapObject;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;

/**
 * GCCoreMapPlanetOverworld.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreMapPlanetOverworld implements IMapObject
{
	@Override
	public float getPlanetSize()
	{
		return 1.0F;
	}

	@Override
	public float getDistanceFromCenter()
	{
		return 1.0F;
	}

	@Override
	public float getPhaseShift()
	{
		return 2160F;
	}

	@Override
	public float getStretchValue()
	{
		return 1.0F;
	}

	@Override
	public ICelestialBodyRenderer getSlotRenderer()
	{
		return new GCCoreSlotRendererOverworld();
	}

	@Override
	public IGalaxy getParentGalaxy()
	{
		return GalacticraftCore.galaxyMilkyWay;
	}
}
