package micdoodle8.mods.galacticraft.mars;

import micdoodle8.mods.galacticraft.API.world.IGalaxy;
import micdoodle8.mods.galacticraft.API.world.IMapObject;
import micdoodle8.mods.galacticraft.API.world.IPlanet;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.mars.client.GCMarsMapPlanet;
import micdoodle8.mods.galacticraft.mars.dimension.GCMarsWorldProvider;
import net.minecraft.world.WorldProvider;

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
