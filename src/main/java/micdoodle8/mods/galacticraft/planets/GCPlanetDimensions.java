package micdoodle8.mods.galacticraft.planets;

import micdoodle8.mods.galacticraft.planets.asteroids.ConfigManagerAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.WorldProviderAsteroids;
import micdoodle8.mods.galacticraft.planets.mars.ConfigManagerMars;
import micdoodle8.mods.galacticraft.planets.mars.dimension.WorldProviderMars;
import micdoodle8.mods.galacticraft.planets.venus.ConfigManagerVenus;
import micdoodle8.mods.galacticraft.planets.venus.dimension.WorldProviderVenus;
import net.minecraft.world.DimensionType;

public class GCPlanetDimensions
{
    public static DimensionType ASTEROIDS = DimensionType.register("Asteroids", "asteroids", ConfigManagerAsteroids.dimensionIDAsteroids, WorldProviderAsteroids.class, false);
    public static DimensionType MARS = DimensionType.register("Mars", "mars", ConfigManagerMars.dimensionIDMars, WorldProviderMars.class, false);
    public static DimensionType VENUS = DimensionType.register("Venus", "venus", ConfigManagerVenus.dimensionIDVenus, WorldProviderVenus.class, false);
}
