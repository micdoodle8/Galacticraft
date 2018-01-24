package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import micdoodle8.mods.galacticraft.api.prefab.world.gen.BiomeProviderSpace;
import net.minecraft.world.biome.Biome;

public class BiomeProviderMars extends BiomeProviderSpace
{
    @Override
    public Biome getBiome()
    {
        return BiomeMars.marsFlat;
    }
}
