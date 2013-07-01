package micdoodle8.mods.galacticraft.core.client;

import micdoodle8.mods.galacticraft.API.IGalaxy;
import micdoodle8.mods.galacticraft.API.IMapObject;
import micdoodle8.mods.galacticraft.API.ICelestialBodyRenderer;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;

public class GCCoreMapPlanetOverworld implements IMapObject
{
    @Override
    public float getPlanetSize()
    {
        return 15;
    }

    @Override
    public float getDistanceFromCenter()
    {
        return 1500F;
    }

    @Override
    public float getPhaseShift()
    {
        return 2160F;
    }

    @Override
    public float getStretchValue()
    {
        return 1F;
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
