package micdoodle8.mods.galacticraft.planets.venus.world.gen;

import micdoodle8.mods.galacticraft.planets.venus.world.gen.layer.GenLayerVenus;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WorldChunkManagerVenus extends WorldChunkManager
{
    private GenLayer unzoomedBiomes;
    private GenLayer zoomedBiomes;
    private BiomeCache biomeCache;
    private List<Biome> biomesToSpawnIn;

    protected WorldChunkManagerVenus()
    {
        biomeCache = new BiomeCache(this);
        biomesToSpawnIn = new ArrayList<>();
//        biomesToSpawnIn.add()
    }

    public WorldChunkManagerVenus(long seed, WorldType type)
    {
        this();
        GenLayer[] genLayers = GenLayerVenus.createWorld(seed);
        this.unzoomedBiomes = genLayers[0];
        this.zoomedBiomes = genLayers[1];
    }

    public WorldChunkManagerVenus(World world)
    {
        this(world.getSeed(), world.getWorldInfo().getTerrainType());
    }

    @Override
    public List<Biome> getBiomesToSpawnIn()
    {
        return this.biomesToSpawnIn;
    }

    @Override
    public Biome getBiomeGenerator(BlockPos pos, Biome biomeGenBaseIn)
    {
        return this.biomeCache.func_180284_a(pos.getX(), pos.getZ(), BiomeVenus.venusFlat);
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
    @SideOnly(Side.CLIENT)
    public float getTemperatureAtHeight(float par1, int par2)
    {
        return par1;
    }

    @Override
    public Biome[] getBiomesForGeneration(Biome[] biomes, int x, int z, int length, int width)
    {
        IntCache.resetIntCache();

        if (biomes == null || biomes.length < length * width)
        {
            biomes = new Biome[length * width];
        }

        int[] intArray = unzoomedBiomes.getInts(x, z, length, width);

        for (int i = 0; i < length * width; ++i)
        {
            if (intArray[i] >= 0)
            {
                biomes[i] = Biome.getBiome(intArray[i]);
            }
            else
            {
                biomes[i] = BiomeVenus.venusFlat;
            }
        }

        return biomes;
    }

    @Override
    public Biome[] loadBlockGeneratorData(Biome[] oldBiomeList, int x, int z, int length, int width)
    {
        return getBiomeGenAt(oldBiomeList, x, z, length, width, true);
    }

    @Override
    public Biome[] getBiomeGenAt(Biome[] listToReuse, int x, int z, int length, int width, boolean cacheFlag)
    {
        IntCache.resetIntCache();

        if (listToReuse == null || listToReuse.length < length * width)
        {
            listToReuse = new Biome[width * length];
        }

        if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0)
        {
            Biome[] cached = this.biomeCache.getCachedBiomes(x, z);
            System.arraycopy(cached, 0, listToReuse, 0, width * length);
            return listToReuse;
        }

        int[] zoomed = zoomedBiomes.getInts(x, z, width, length);

        for (int i = 0; i < width * length; ++i)
        {
            if (zoomed[i] >= 0)
            {
                listToReuse[i] = Biome.getBiome(zoomed[i]);
            }
            else
            {
                listToReuse[i] = Biome.beach; // TODO Replace with venus default
            }
        }

        return listToReuse;
    }

    @Override
    public boolean areBiomesViable(int x, int z, int range, List<Biome> viables)
    {
        int i = x - range >> 2;
        int j = z - range >> 2;
        int k = x + range >> 2;
        int l = z + range >> 2;
        int diffX = (k - i) + 1;
        int diffZ = (l - j) + 1;
        int[] unzoomed = this.unzoomedBiomes.getInts(i, j, diffX, diffZ);

        for (int a = 0; a < diffX * diffZ; ++a)
        {
            Biome biome = Biome.getBiome(unzoomed[a]);

            if (!viables.contains(biome))
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random)
    {
        int i = x - range >> 2;
        int j = z - range >> 2;
        int k = x + range >> 2;
        int l = z + range >> 2;
        int diffX = (k - i) + 1;
        int diffZ = (l - j) + 1;
        int[] unzoomed = this.unzoomedBiomes.getInts(i, j, diffX, diffZ);
        BlockPos blockPos = null;
        int count = 0;

        for (int a = 0; a < unzoomed.length; ++a)
        {
            int x0 = i + a % diffX << 2;
            int z0 = j + a / diffX << 2;
            Biome biome = Biome.getBiome(unzoomed[a]);

            if (biomes.contains(biome) && (blockPos == null || random.nextInt(count + 1) == 0))
            {
                blockPos = new BlockPos(x0, 0, z0);
                count++;
            }
        }

        return blockPos;
    }

    @Override
    public void cleanupCache()
    {
        this.biomeCache.cleanupCache();
    }
}
