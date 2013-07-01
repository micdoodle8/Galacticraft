package micdoodle8.mods.galacticraft.core;

import micdoodle8.mods.galacticraft.API.IGalaxy;
import micdoodle8.mods.galacticraft.API.IMapObject;
import micdoodle8.mods.galacticraft.API.IPlanet;
import micdoodle8.mods.galacticraft.core.client.GCCoreMapSun;

/**
 * Yes, I know it's not a planet...
 */
public class GCCorePlanetSun implements IPlanet
{
    @Override
    public String getName()
    {
        return "Sun";
    }

    @Override
    public boolean isReachable()
    {
        return false;
    }

    @Override
    public IMapObject getMapObject()
    {
        return new GCCoreMapSun();
    }

    @Override
    public IGalaxy getParentGalaxy()
    {
        return GalacticraftCore.galaxyMilkyWay;
    }

    @Override
    public boolean addToList()
    {
        return false;
    }
}
