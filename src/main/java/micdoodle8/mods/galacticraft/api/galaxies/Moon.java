package micdoodle8.mods.galacticraft.api.galaxies;

public class Moon extends CelestialBody
{
	protected Planet parentPlanet = null;

	public Moon(String moonName)
	{
		super(moonName);
	}

	public Planet getParentPlanet()
	{
		return this.parentPlanet;
	}

	public Moon setParentPlanet(Planet planet)
	{
		this.parentPlanet = planet;
		return this;
	}

	@Override
	public int getID()
	{
		return GalaxyRegistry.getMoonID(this.bodyName);
	}

	@Override
	public String getUnlocalizedNamePrefix()
	{
		return "moon";
	}
}
