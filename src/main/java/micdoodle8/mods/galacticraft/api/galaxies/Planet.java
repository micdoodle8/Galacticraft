package micdoodle8.mods.galacticraft.api.galaxies;

import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;

public class Planet extends CelestialBody
{
	protected SolarSystem parentSolarSystem = null;
	public PlanetPreGen threadPreGen = null;

	public Planet(String planetName)
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
		return GalaxyRegistry.getPlanetID(this.bodyName);
	}

	@Override
	public String getUnlocalizedNamePrefix()
	{
		return "planet";
	}

	public Planet setParentSolarSystem(SolarSystem galaxy)
	{
		this.parentSolarSystem = galaxy;
		return this;
	}

	public void initiatePreGen(int chunkCoordX, int chunkCoordZ)
	{
		if (this.threadPreGen != null && this.threadPreGen.looping.get())
		{
			GCLog.info("Already generating: terrain pregen skipped for planet " + this.bodyName + " at " + chunkCoordX + ", " + chunkCoordZ);
		}
		else
		{
			if (ConfigManagerCore.enableDebug)
				GCLog.info("Starting terrain pregen for planet " + this.bodyName + " at " + chunkCoordX + ", " + chunkCoordZ);
		}
		//TODO  maybe check the dimension is empty to avoid conflicts?
		
		this.threadPreGen = new PlanetPreGen(this.dimensionID, chunkCoordX, chunkCoordZ, this);
	}
}
