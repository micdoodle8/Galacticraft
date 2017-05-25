package micdoodle8.mods.galacticraft.api.world;

public interface IOrbitDimension extends IGalacticraftWorldProvider
{
    /**
     * @return the name of the world that this dimension is orbiting. For the
     * overworld it returns "Overworld"
     */
    String getPlanetToOrbit();

    /**
     * @return the y-coordinate that entities will fall back into the world we
     * are orbiting
     */
    int getYCoordToTeleportToPlanet();
}
