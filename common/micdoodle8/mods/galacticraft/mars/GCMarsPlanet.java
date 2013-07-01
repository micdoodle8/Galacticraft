package micdoodle8.mods.galacticraft.mars;

import micdoodle8.mods.galacticraft.API.IGalaxy;
import micdoodle8.mods.galacticraft.API.IPlanet;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;

public class GCMarsPlanet implements IPlanet
{
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
}
