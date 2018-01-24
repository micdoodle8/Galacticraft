package micdoodle8.mods.galacticraft.planets.asteroids.world.gen;

import micdoodle8.mods.galacticraft.api.prefab.world.gen.BiomeProviderSpace;
import net.minecraft.world.biome.Biome;

public class BiomeProviderAsteroids extends BiomeProviderSpace
{
    @Override
    public Biome getBiome()
    {
        return BiomeAsteroids.asteroid;
    }
}
