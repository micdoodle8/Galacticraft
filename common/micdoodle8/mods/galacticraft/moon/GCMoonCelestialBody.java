package micdoodle8.mods.galacticraft.moon;

import micdoodle8.mods.galacticraft.API.IMoon;
import micdoodle8.mods.galacticraft.API.IPlanet;

public class GCMoonCelestialBody implements IMoon
{
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
        return null;
    }
}
