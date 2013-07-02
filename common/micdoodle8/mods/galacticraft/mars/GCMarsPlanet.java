package micdoodle8.mods.galacticraft.mars;

import net.minecraft.world.WorldProvider;
import micdoodle8.mods.galacticraft.API.IGalaxy;
import micdoodle8.mods.galacticraft.API.IMapObject;
import micdoodle8.mods.galacticraft.API.IPlanet;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.mars.client.GCMarsMapPlanet;
import micdoodle8.mods.galacticraft.mars.dimension.GCMarsWorldProvider;

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
    public boolean autoRegister()
    {
        return true;
    }

    @Override
    public boolean addToList()
    {
        return false;
    }

    @Override
    public Class<? extends WorldProvider> getWorldProvider()
    {
        return GCMarsWorldProvider.class;
    }

    @Override
    public int getDimensionID()
    {
        return GCMarsConfigManager.dimensionIDMars;
    }
}
