package micdoodle8.mods.galacticraft.api.galaxies;

import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;

public class Satellite extends CelestialBody implements IChildBody
{
    protected Planet parentCelestialBody = null;
    protected int dimensionIdStatic = 0;

    public Satellite(String satelliteName)
    {
        super(satelliteName);
    }

    @Override
    public Planet getParentPlanet()
    {
        return this.parentCelestialBody;
    }

    public Satellite setParentBody(Planet parentCelestialBody)
    {
        this.parentCelestialBody = parentCelestialBody;
        return this;
    }

    @Override
    @Deprecated
    public CelestialBody setDimensionInfo(DimensionType type, Class<? extends Dimension> providerClass, boolean autoRegister)
    {
        // Since satellites need a static ID, the subclass method will not work correctly.
        throw new UnsupportedOperationException("Satellite registered using an outdated method (setDimensionInfo)! Tell Galacticraft addon authors to update to the latest API.");
    }

    public CelestialBody setDimensionInfo(DimensionType providerIdDynamic, int providerIdStatic, Class<? extends Dimension> providerClass)
    {
        this.dimensionType = providerIdDynamic;
        this.dimensionIdStatic = providerIdStatic;
        this.providerClass = providerClass;
        this.autoRegisterDimension = false; // Addons need to register satellites manually
        this.isReachable = true;
        return this;
    }

    @Override
    public int getID()
    {
        return GalaxyRegistry.getSatelliteID(this.bodyName);
    }

    @Override
    public String getUnlocalizedNamePrefix()
    {
        return "satellite";
    }

    public int getDimensionIdStatic()
    {
        return dimensionIdStatic;
    }
}
