package micdoodle8.mods.galacticraft.core.world.gen;

import micdoodle8.mods.galacticraft.api.prefab.world.gen.BiomeProviderSpace;
import net.minecraft.world.biome.Biome;

public class BiomeProviderMoon extends BiomeProviderSpace
{
    @Override
    public Biome getBiome()
    {
        return BiomeMoon.moonFlat;
    }
}
