package micdoodle8.mods.galacticraft.moon.client;

import micdoodle8.mods.galacticraft.API.world.ICelestialBodyRenderer;
import micdoodle8.mods.galacticraft.API.world.IGalaxy;
import micdoodle8.mods.galacticraft.API.world.IMapObject;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;

public class GCMoonMapPlanet implements IMapObject
{
    @Override
    public float getPlanetSize()
    {
        return 0.26666666666666666666666666666667F;
    }

    @Override
    public float getDistanceFromCenter()
    {
        return 40.0F;
    }

    @Override
    public float getPhaseShift()
    {
        return 0.0F;
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
