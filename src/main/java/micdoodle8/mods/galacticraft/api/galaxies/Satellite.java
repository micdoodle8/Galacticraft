package micdoodle8.mods.galacticraft.api.galaxies;

public class Satellite extends CelestialBody
{
    protected CelestialBody parentCelestialBody = null;

    public Satellite(String satelliteName)
    {
        super(satelliteName);
    }

    public CelestialBody getParentBody()
    {
        return this.parentCelestialBody;
    }

    public Satellite setParentBody(CelestialBody parentCelestialBody)
    {
        this.parentCelestialBody = parentCelestialBody;
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
}
