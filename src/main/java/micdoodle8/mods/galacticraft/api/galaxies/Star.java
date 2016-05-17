package micdoodle8.mods.galacticraft.api.galaxies;

public class Star extends CelestialBody
{
    protected SolarSystem parentSolarSystem = null;

    public Star(String planetName)
    {
        super(planetName);
    }

    public SolarSystem getParentSolarSystem()
    {
        return this.parentSolarSystem;
    }

    @Override
    public int getID()
    {
        return this.parentSolarSystem.getID();
    }

    @Override
    public String getUnlocalizedNamePrefix()
    {
        return "star";
    }

    public Star setParentSolarSystem(SolarSystem galaxy)
    {
        this.parentSolarSystem = galaxy;
        return this;
    }
}
