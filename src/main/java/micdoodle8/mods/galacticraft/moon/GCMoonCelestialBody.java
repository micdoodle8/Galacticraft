package micdoodle8.mods.galacticraft.moon;

import micdoodle8.mods.galacticraft.api.world.IMapObject;
import micdoodle8.mods.galacticraft.api.world.IMoon;
import micdoodle8.mods.galacticraft.api.world.IPlanet;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.moon.client.GCMoonMapPlanet;
import micdoodle8.mods.galacticraft.moon.dimension.GCMoonWorldProvider;
import net.minecraft.world.WorldProvider;

/**
 * GCMoonCelestialBody.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMoonCelestialBody implements IMoon
{
	private final IMapObject moon = new GCMoonMapPlanet();

	@Override
	public String getName()
	{
		return "Moon";
	}

	@Override
	public boolean isReachable()
	{
		return true;
	}

	@Override
	public IPlanet getParentPlanet()
	{
		return GalacticraftCore.overworld;
	}

	@Override
	public IMapObject getMapObject()
	{
		return this.moon;
	}

	@Override
	public boolean autoRegister()
	{
		return true;
	}

	@Override
	public boolean addToList()
	{
		return true;
	}

	@Override
	public Class<? extends WorldProvider> getWorldProvider()
	{
		return GCMoonWorldProvider.class;
	}

	@Override
	public int getDimensionID()
	{
		return GCMoonConfigManager.dimensionIDMoon;
	}

	@Override
	public boolean forceStaticLoad()
	{
		return true;
	}
}
