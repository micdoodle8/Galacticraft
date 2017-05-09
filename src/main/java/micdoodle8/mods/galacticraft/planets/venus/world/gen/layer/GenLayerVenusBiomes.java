package micdoodle8.mods.galacticraft.planets.venus.world.gen.layer;

import micdoodle8.mods.galacticraft.planets.venus.world.gen.BiomeGenBaseVenus;
import micdoodle8.mods.miccore.IntCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;

public class GenLayerVenusBiomes extends GenLayer
{
    private static final BiomeGenBase[] biomes = new BiomeGenBase[] { BiomeGenBaseVenus.venusFlat, BiomeGenBaseVenus.venusMountain, BiomeGenBaseVenus.venusValley};

    public GenLayerVenusBiomes(long l, GenLayer parent)
    {
        super(l);
        this.parent = parent;
    }

    public GenLayerVenusBiomes(long l)
    {
        super(l);
    }

    @Override
    public int[] getInts(int x, int z, int width, int depth)
    {
        int[] dest = IntCache.getIntCache(width * depth);

        for (int k = 0; k < depth; ++k)
        {
            for (int i = 0; i < width; ++i)
            {
                initChunkSeed(x + i, z + k);
                dest[i + k * width] = biomes[nextInt(biomes.length)].biomeID;
            }
        }

        return dest;
    }
}
