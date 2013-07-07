package micdoodle8.mods.galacticraft.core.client;

import micdoodle8.mods.galacticraft.api.world.ICelestialBodyRenderer;
import micdoodle8.mods.galacticraft.api.world.IGalaxy;
import micdoodle8.mods.galacticraft.api.world.IMapObject;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;

public class GCCoreMapSun implements IMapObject
{
    @Override
    public float getPlanetSize()
    {
        return 108F;
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
    public ICelestialBodyRenderer getSlotRenderer()
    {
        return new GCCoreSlotRendererSun();
    }

    @Override
    public IGalaxy getParentGalaxy()
    {
        return GalacticraftCore.galaxyMilkyWay;
    }
}
