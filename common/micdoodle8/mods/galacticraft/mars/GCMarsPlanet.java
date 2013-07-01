package micdoodle8.mods.galacticraft.mars;

import micdoodle8.mods.galacticraft.API.IGalaxy;
import micdoodle8.mods.galacticraft.API.IMapObject;
import micdoodle8.mods.galacticraft.API.IPlanet;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.mars.client.GCMarsMapPlanet;

public class GCMarsPlanet implements IPlanet
{
    private final IMapObject mars = new GCMarsMapPlanet();
    
    @Override
    public String getName()
    {
        return "Mars";
    }

    @Override
    public boolean isReachable()
    {
        return true;
    }

    @Override
    public IGalaxy getParentGalaxy()
    {
        return GalacticraftCore.galaxyMilkyWay;
    }

    @Override
    public IMapObject getMapObject()
    {
        return this.mars;
    }

    @Override
    public boolean addToList()
    {
        return false;
    }
}
