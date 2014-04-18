package micdoodle8.mods.galacticraft.core;

import micdoodle8.mods.galacticraft.api.world.IGalaxy;
import micdoodle8.mods.galacticraft.api.world.IMapObject;
import micdoodle8.mods.galacticraft.api.world.IPlanet;
import micdoodle8.mods.galacticraft.core.client.GCCoreMapPlanetOverworld;
import net.minecraft.world.WorldProvider;

/**
 * GCCorePlanetOverworld.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCorePlanetOverworld implements IPlanet
{
	private final IMapObject overworld = new GCCoreMapPlanetOverworld();

	@Override
	public String getName()
	{
		return "Overworld";
	}

	@Override
	public boolean isReachable()
	{
		return true;
	}

	@Override
	public IMapObject getMapObject()
	{
		return this.overworld;
	}

	@Override
	public IGalaxy getParentGalaxy()
	{
		return GalacticraftCore.galaxyMilkyWay;
	}

	@Override
	public boolean addToList()
	{
		return true;
	}

	@Override
	public boolean autoRegister()
	{
		return false;
	}

	@Override
	public Class<? extends WorldProvider> getWorldProvider()
	{
		return null;
	}

	@Override
	public int getDimensionID()
	{
		return 0;
	}

	@Override
	public boolean forceStaticLoad()
	{
		return false;
	}
}
