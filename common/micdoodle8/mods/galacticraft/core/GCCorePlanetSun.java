package micdoodle8.mods.galacticraft.core;

import micdoodle8.mods.galacticraft.API.world.IGalaxy;
import micdoodle8.mods.galacticraft.API.world.IMapObject;
import micdoodle8.mods.galacticraft.API.world.IPlanet;
import micdoodle8.mods.galacticraft.core.client.GCCoreMapSun;
import net.minecraft.world.WorldProvider;

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
    public boolean autoRegister()
    {
        return false;
    }

    @Override
    public boolean addToList()
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
        return -1;
    }
}
