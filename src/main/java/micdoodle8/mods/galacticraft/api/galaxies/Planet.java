package micdoodle8.mods.galacticraft.api.galaxies;


public class Planet extends CelestialBody
{
    protected Galaxy parentGalaxy = null;

    public Planet(String planetName)
    {
    	super(planetName);
    }
    
    public Galaxy getParentGalaxy()
    {
    	return this.parentGalaxy;
    }

    public int getID()
    {
        return GalaxyRegistry.getPlanetID(this.bodyName);
    }

	@Override
	protected String getUnlocalizedNamePrefix() 
	{
		return "planet";
	}
	
	public Planet setParentGalaxy(Galaxy galaxy)
	{
		this.parentGalaxy = galaxy;
		return this;
	}
}
