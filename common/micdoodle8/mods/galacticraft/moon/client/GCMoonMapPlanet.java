package micdoodle8.mods.galacticraft.moon.client;

import micdoodle8.mods.galacticraft.API.IGalaxy;
import micdoodle8.mods.galacticraft.API.IMapObject;
import micdoodle8.mods.galacticraft.API.ICelestialBodyRenderer;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;

public class GCMoonMapPlanet implements IMapObject
{
    @Override
    public float getPlanetSize()
    {
        return 4;
    }

    @Override
    public float getDistanceFromCenter()
    {
        return 1500F / 15F;
    }

    @Override
    public float getPhaseShift()
    {
        return 0;
    }

    @Override
    public float getStretchValue()
    {
        return 0.01F;
    }

    @Override
    public ICelestialBodyRenderer getSlotRenderer()
    {
        return new GCMoonSlotRenderer();
    }

    @Override
    public IGalaxy getParentGalaxy()
    {
        return GalacticraftCore.galaxyMilkyWay;
    }
}
