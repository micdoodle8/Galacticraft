package micdoodle8.mods.galacticraft.moon;

import micdoodle8.mods.galacticraft.API.IMapObject;
import micdoodle8.mods.galacticraft.API.IMoon;
import micdoodle8.mods.galacticraft.API.IPlanet;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.moon.client.GCMoonMapPlanet;
import micdoodle8.mods.galacticraft.moon.dimension.GCMoonWorldProvider;
import net.minecraft.world.WorldProvider;

public class GCMoonCelestialBody implements IMoon
{
    private final IMapObject moon = new GCMoonMapPlanet();
    
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
        return GalacticraftCore.overworld;
    }

    @Override
    public IMapObject getMapObject()
    {
        return this.moon;
    }

    @Override
    public boolean autoRegister()
    {
        return true;
    }

    @Override
    public boolean addToList()
    {
        return true;
    }

    @Override
    public Class<? extends WorldProvider> getWorldProvider()
    {
        return GCMoonWorldProvider.class;
    }

    @Override
    public int getDimensionID()
    {
        return GCMoonConfigManager.dimensionIDMoon;
    }
}