package micdoodle8.mods.galacticraft.api.prefab.world.gen;

import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.IntCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Do not include this prefab class in your released mod download.
 * <p/>
 * This chunk manager is used for single-biome dimensions, which is common on basic planets.
 */
public abstract class WorldChunkManagerSpace extends WorldChunkManager
{
    private final BiomeCache biomeCache;
    private final List<BiomeGenBase> biomesToSpawnIn;

    public WorldChunkManagerSpace()
    {
        this.biomeCache = new BiomeCache(this);
        this.biomesToSpawnIn = new ArrayList<BiomeGenBase>();
        this.biomesToSpawnIn.add(this.getBiome());
    }

    @Override
    public List<BiomeGenBase> getBiomesToSpawnIn()
    {
        return this.biomesToSpawnIn;
    }

    public BiomeGenBase func_180300_a(BlockPos p_180300_1_, BiomeGenBase p_180300_2_)
    {
        return this.getBiome();
    }

    @Override
    public float[] getRainfall(float[] par1ArrayOfFloat, int par2, int par3, int par4, int par5)
    {
        if (par1ArrayOfFloat == null || par1ArrayOfFloat.length < par4 * par5)
        {
            par1ArrayOfFloat = new float[par4 * par5];
        }

        Arrays.fill(par1ArrayOfFloat, 0, par4 * par5, 0.0F);
        return par1ArrayOfFloat;
    }

    @Override
    public float getTemperatureAtHeight(float par1, int par2)
    {
        return par1;
    }

    @Override
    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5)
    {
        IntCache.resetIntCache();

        if (par1ArrayOfBiomeGenBase == null || par1ArrayOfBiomeGenBase.length < par4 * par5)
        {
            par1ArrayOfBiomeGenBase = new BiomeGenBase[par4 * par5];
        }

        for (int var7 = 0; var7 < par4 * par5; ++var7)
        {
            par1ArrayOfBiomeGenBase[var7] = this.getBiome();
        }

        return par1ArrayOfBiomeGenBase;
    }

    @Override
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5)
    {
        return this.getBiomeGenAt(par1ArrayOfBiomeGenBase, par2, par3, par4, par5, true);
    }

    @Override
    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] listToReuse, int x, int z, int width, int length, boolean cacheFlag)
    {
        IntCache.resetIntCache();

        if (listToReuse == null || listToReuse.length < width * length)
        {
            listToReuse = new BiomeGenBase[width * length];
        }

        if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0)
        {
            final BiomeGenBase[] var9 = this.biomeCache.getCachedBiomes(x, z);
            System.arraycopy(var9, 0, listToReuse, 0, width * length);
            return listToReuse;
        }
        else
        {
            for (int var8 = 0; var8 < width * length; ++var8)
            {
                listToReuse[var8] = this.getBiome();
            }

            return listToReuse;
        }
    }

    @Override
    public boolean areBiomesViable(int par1, int par2, int par3, List par4List)
    {
        return par4List.contains(this.getBiome());
    }

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
        this.biomeCache.cleanupCache();
    }

    public abstract BiomeGenBase getBiome();
}
