package micdoodle8.mods.galacticraft.api.galaxies;

import net.minecraft.world.biome.Biome.SpawnListEntry;

public class Planet extends CelestialBody
{
    protected SolarSystem parentSolarSystem = null;

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
    
    public static void addMobToSpawn(String planetName, SpawnListEntry mobData)
    {
        GalaxyRegistry.getCelestialBodyFromUnlocalizedName("planet." + planetName).addMobInfo(mobData);
    }
}
