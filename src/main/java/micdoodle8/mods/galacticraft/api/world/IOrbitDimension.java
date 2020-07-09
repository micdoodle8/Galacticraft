package micdoodle8.mods.galacticraft.api.world;

import net.minecraft.world.dimension.DimensionType;

public interface IOrbitDimension extends IGalacticraftDimension
{
    /**
     * @return the y-coordinate that entities will fall back into the world we
     * are orbiting
     */
    int getYCoordToTeleportToPlanet();

    DimensionType getPlanetIdToOrbit();
}
