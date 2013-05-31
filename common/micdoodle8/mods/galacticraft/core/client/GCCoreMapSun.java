package micdoodle8.mods.galacticraft.core.client;

import micdoodle8.mods.galacticraft.API.IGalaxy;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;

public class GCCoreMapSun implements IMapPlanet
{
    @Override
    public float getPlanetSize()
    {
        return 15F * 108F;
    }

    @Override
    public float getDistanceFromCenter()
    {
        return 0F;
    }

    @Override
    public float getPhaseShift()
    {
        return 0;
    }

    @Override
    public float getStretchValue()
    {
        return 0F;
    }

    @Override
    public IPlanetSlotRenderer getSlotRenderer()
    {
        return new GCCoreSlotRendererSun();
    }

    @Override
    public IGalaxy getParentGalaxy()
    {
        return GalacticraftCore.galaxyMilkyWay;
    }
}
