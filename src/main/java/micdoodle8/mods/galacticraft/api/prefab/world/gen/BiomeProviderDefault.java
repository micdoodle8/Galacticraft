package micdoodle8.mods.galacticraft.api.prefab.world.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.world.BiomeGenBaseGC;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.IntCache;


/**
 * A Biome Provider for Galacticraft planets (and other bodies!) which only have 1 biome
 * 
 * Makes use of Galacticraft's BiomeAdaptive so that only one Biome is registered in Forge
 * (this BiomeProvider in combination with BiomeAdaptive makes sure the correct actual biome is seen) 
 */
public class BiomeProviderDefault extends BiomeProviderSpace
{
    private static final Biome[] biomeCache = new Biome[256];
    private final List<Biome> biomesToSpawnIn;

    private BiomeGenBaseGC biomeTrue;
    private CelestialBody body; 
    
    static
    {
        for (int i = 0; i < 16; ++i)
        {
            biomeCache[i] = BiomeAdaptive.biomeDefault;
        }
        System.arraycopy(biomeCache, 0, biomeCache, 16, 16);
        System.arraycopy(biomeCache, 0, biomeCache, 32, 32);
        System.arraycopy(biomeCache, 0, biomeCache, 64, 64);
        System.arraycopy(biomeCache, 0, biomeCache, 128, 128);
    }
    
    public BiomeProviderDefault(CelestialBody theBody)
    {
        body = theBody;
        biomeTrue = BiomeAdaptive.getDefaultBiomeFor(body);
        this.biomesToSpawnIn = new ArrayList<Biome>();
        this.biomesToSpawnIn.add(this.getBiome());
    }
    
    @Override
    public Biome getBiome()
    {
        if (body != null) BiomeAdaptive.setBody(body);
        return BiomeAdaptive.biomeDefault;
    }
    
    public Biome getBiomeTrue()
    {
        return biomeTrue;
    }

    @Override
    public List getBiomesToSpawnIn()
    {
        return this.biomesToSpawnIn;
    }

    @Override
    public Biome getBiome(BlockPos pos, Biome defaultBiome)
    {
        return this.getBiome();
    }

    @Override
    public float getTemperatureAtHeight(float par1, int par2)
    {
        return par1;
    }

    @Override
    public Biome[] getBiomesForGeneration(Biome[] arrayToReuse, int x, int z, int width, int length)
    {
        return this.getBiomes(arrayToReuse, x, z, width, length, true);
    }

    @Override
    public Biome[] getBiomes(Biome[] par1ArrayOfBiome, int par2, int par3, int par4, int par5)
    {
        return this.getBiomes(par1ArrayOfBiome, par2, par3, par4, par5, true);
    }

    @Override
    public Biome[] getBiomes(Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag)
    {
        BiomeAdaptive.setBody(body);
        IntCache.resetIntCache();
        int size = width * length;

        if (listToReuse == null || listToReuse.length < size)
        {
            listToReuse = new Biome[size];
        }

        if (size <= 256)
        {
            System.arraycopy(biomeCache, 0, listToReuse, 0, size);
        }
        else
        {
            System.arraycopy(biomeCache, 0, listToReuse, 0, 256);
            for (int i = 256; i < size; i += i)
            {
                System.arraycopy(listToReuse, 0, listToReuse, i, ((size - i) < i) ? (size - i) : i);
            }
        }

        return listToReuse;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean areBiomesViable(int par1, int par2, int par3, List par4List)
    {
        return par4List.contains(this.getBiomeTrue());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public BlockPos findBiomePosition(int par1, int par2, int par3, List par4List, Random par5Random)
    {
        final int var6 = par1 - par3 >> 2;
        final int var7 = par2 - par3 >> 2;
        final int var8 = par1 + par3 >> 2;
        final int var10 = var8 - var6 + 1;

        final int var16 = var6 + 0 % var10 << 2;
        final int var17 = var7 + 0 / var10 << 2;

        return new BlockPos(var16, 0, var17);
    }

    @Override
    public void cleanupCache()
    {
    }
}
