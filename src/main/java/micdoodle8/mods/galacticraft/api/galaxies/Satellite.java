package micdoodle8.mods.galacticraft.api.galaxies;

public class Satellite extends CelestialBody implements IChildBody
{
    protected Planet parentCelestialBody = null;

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
