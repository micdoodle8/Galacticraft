package micdoodle8.mods.galacticraft.planets.asteroids.world.gen;

import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldChunkManagerSpace;
import net.minecraft.world.biome.BiomeGenBase;

public class WorldChunkManagerAsteroids extends WorldChunkManagerSpace
{
    @Override
    public BiomeGenBase getBiome()
    {
        return BiomeGenBaseAsteroids.asteroid;
    }
}
