package micdoodle8.mods.galacticraft.API.world;

public interface IMoon extends ICelestialBody
{
    /**
     * @return IPlanet object for the planet this moon orbits
     * 
     * @see IPlanet
     */
    public IPlanet getParentPlanet();
}
