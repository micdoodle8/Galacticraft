package micdoodle8.mods.galacticraft.planets.venus.world.gen;

import micdoodle8.mods.galacticraft.api.prefab.world.gen.BiomeAdaptive;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.MapGenBaseMeta;
import micdoodle8.mods.galacticraft.api.world.ChunkProviderBase;
import micdoodle8.mods.galacticraft.core.perlin.generator.Gradient;
import micdoodle8.mods.galacticraft.planets.venus.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockBasicVenus;
import micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon.DungeonConfigurationVenus;
import micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon.MapGenDungeonVenus;
import micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon.RoomBossVenus;
import micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon.RoomTreasureVenus;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;

import java.util.List;
import java.util.Random;

public class ChunkProviderVenus extends ChunkProviderBase
{
    public static final IBlockState BLOCK_FILL = VenusBlocks.venusBlock.getDefaultState().withProperty(BlockBasicVenus.BASIC_TYPE_VENUS, BlockBasicVenus.EnumBlockBasicVenus.ROCK_HARD);

    private final BiomeDecoratorVenus biomeDecoratorVenus = new BiomeDecoratorVenus();
    private Random rand;
    private NoiseGeneratorOctaves noiseGen1;
    private NoiseGeneratorOctaves noiseGen2;
    private NoiseGeneratorOctaves noiseGen3;
    private NoiseGeneratorPerlin noiseGen4;
    private NoiseGeneratorOctaves noiseGen5;
    private NoiseGeneratorOctaves noiseGen6;
    private NoiseGeneratorOctaves mobSpawnerNoise;
    private final Gradient noiseGenSmooth1;
    private World world;
    private WorldType worldType;
    private final double[] terrainCalcs;
    private final float[] parabolicField;
    private double[] stoneNoise = new double[256];
    private MapGenBaseMeta caveGenerator = new MapGenCaveVenus();
    private MapGenBaseMeta lavaCaveGenerator = new MapGenLavaVenus();
    private Biome[] biomesForGeneration;
    private double[] octaves1;
    private double[] octaves2;
    private double[] octaves3;
    private double[] octaves4;
    private final MapGenDungeonVenus dungeonGenerator = new MapGenDungeonVenus(new DungeonConfigurationVenus(VenusBlocks.venusBlock.getDefaultState().withProperty(BlockBasicVenus.BASIC_TYPE_VENUS, BlockBasicVenus.EnumBlockBasicVenus.DUNGEON_BRICK_1),
            VenusBlocks.venusBlock.getDefaultState().withProperty(BlockBasicVenus.BASIC_TYPE_VENUS, BlockBasicVenus.EnumBlockBasicVenus.DUNGEON_BRICK_2),
            30, 8, 16, 7, 7, RoomBossVenus.class, RoomTreasureVenus.class));

    public ChunkProviderVenus(World worldIn, long seed, boolean mapFeaturesEnabled)
    {
        this.world = worldIn;
        this.worldType = worldIn.getWorldInfo().getTerrainType();
        this.rand = new Random(seed);
        this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 16);
        this.noiseGen2 = new NoiseGeneratorOctaves(this.rand, 16);
        this.noiseGen3 = new NoiseGeneratorOctaves(this.rand, 8);
        this.noiseGen4 = new NoiseGeneratorPerlin(this.rand, 4);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
        this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
        this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
        this.noiseGenSmooth1 = new Gradient(this.rand.nextLong(), 4, 0.25F);
        this.terrainCalcs = new double[825];
        this.parabolicField = new float[25];

        for (int i = -2; i <= 2; ++i)
        {
            for (int j = -2; j <= 2; ++j)
            {
                float f = 10.0F / MathHelper.sqrt((float) (i * i + j * j) + 0.2F);
                this.parabolicField[i + 2 + (j + 2) * 5] = f;
            }
        }

        NoiseGenerator[] noiseGens = {noiseGen1, noiseGen2, noiseGen3, noiseGen4, noiseGen5, noiseGen6, mobSpawnerNoise};
        this.noiseGen1 = (NoiseGeneratorOctaves) noiseGens[0];
        this.noiseGen2 = (NoiseGeneratorOctaves) noiseGens[1];
        this.noiseGen3 = (NoiseGeneratorOctaves) noiseGens[2];
        this.noiseGen4 = (NoiseGeneratorPerlin) noiseGens[3];
        this.noiseGen5 = (NoiseGeneratorOctaves) noiseGens[4];
        this.noiseGen6 = (NoiseGeneratorOctaves) noiseGens[5];
        this.mobSpawnerNoise = (NoiseGeneratorOctaves) noiseGens[6];
    }

    private void setBlocksInChunk(int chunkX, int chunkZ, ChunkPrimer primer)
    {
        this.noiseGenSmooth1.setFrequency(0.015F);
        this.biomesForGeneration = this.world.getBiomeProvider().getBiomesForGeneration(this.biomesForGeneration, chunkX * 4 - 2, chunkZ * 4 - 2, 10, 10);
        this.createLandPerBiome(chunkX * 4, chunkZ * 4);

        for (int i = 0; i < 4; ++i)
        {
            int j = i * 5;
            int k = (i + 1) * 5;

            for (int l = 0; l < 4; ++l)
            {
                int i1 = (j + l) * 33;
                int j1 = (j + l + 1) * 33;
                int k1 = (k + l) * 33;
                int l1 = (k + l + 1) * 33;

                for (int i2 = 0; i2 < 32; ++i2)
                {
                    double d0 = 0.125D;
                    double d1 = this.terrainCalcs[i1 + i2];
                    double d2 = this.terrainCalcs[j1 + i2];
                    double d3 = this.terrainCalcs[k1 + i2];
                    double d4 = this.terrainCalcs[l1 + i2];
                    double d5 = (this.terrainCalcs[i1 + i2 + 1] - d1) * d0;
                    double d6 = (this.terrainCalcs[j1 + i2 + 1] - d2) * d0;
                    double d7 = (this.terrainCalcs[k1 + i2 + 1] - d3) * d0;
                    double d8 = (this.terrainCalcs[l1 + i2 + 1] - d4) * d0;

                    for (int j2 = 0; j2 < 8; ++j2)
                    {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;

                        for (int k2 = 0; k2 < 4; ++k2)
                        {
                            double d14 = 0.25D;
                            double d16 = (d11 - d10) * d14;
                            double lvt_45_1_ = d10 - d16;

                            for (int l2 = 0; l2 < 4; ++l2)
                            {
                                if ((lvt_45_1_ += d16) > this.noiseGenSmooth1.getNoise(chunkX * 16 + (i * 4 + k2), chunkZ * 16 + (l * 4 + l2)) * 20.0)
                                {
                                    primer.setBlockState(i * 4 + k2, i2 * 8 + j2, l * 4 + l2, BLOCK_FILL);
                                }
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }

    private void replaceBlocksForBiome(int p_180517_1_, int p_180517_2_, ChunkPrimer p_180517_3_, Biome[] p_180517_4_)
    {
        double d0 = 0.03125D;
        this.stoneNoise = this.noiseGen4.getRegion(this.stoneNoise, (double) (p_180517_1_ * 16), (double) (p_180517_2_ * 16), 16, 16, d0 * 2.0D, d0 * 2.0D, 1.0D);

        for (int i = 0; i < 16; ++i)
        {
            for (int j = 0; j < 16; ++j)
            {
                Biome biomegenbase = p_180517_4_[j + i * 16];
                biomegenbase.genTerrainBlocks(this.world, this.rand, p_180517_3_, p_180517_1_ * 16 + i, p_180517_2_ * 16 + j, this.stoneNoise[j + i * 16]);
            }
        }
    }

    @Override
    public Chunk generateChunk(int x, int z)
    {
        this.rand.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.setBlocksInChunk(x, z, chunkprimer);
        this.biomesForGeneration = this.world.getBiomeProvider().getBiomes(this.biomesForGeneration, x * 16, z * 16, 16, 16);

        this.caveGenerator.generate(this.world, x, z, chunkprimer);
        this.lavaCaveGenerator.generate(this.world, x, z, chunkprimer);

        this.replaceBlocksForBiome(x, z, chunkprimer, this.biomesForGeneration);

        this.dungeonGenerator.generate(this.world, x, z, chunkprimer);

        Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
        byte[] abyte = chunk.getBiomeArray();

        for (int i = 0; i < abyte.length; ++i)
        {
            abyte[i] = (byte) Biome.getIdForBiome(this.biomesForGeneration[i]);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    private void createLandPerBiome(int x, int z)
    {
        this.octaves4 = this.noiseGen6.generateNoiseOctaves(this.octaves4, x, z, 5, 5, 2000.0, 2000.0, 0.5);
        this.octaves1 = this.noiseGen3.generateNoiseOctaves(this.octaves1, x, 0, z, 5, 33, 5, 8.555150000000001D, 4.277575000000001D, 8.555150000000001D);
        this.octaves2 = this.noiseGen1.generateNoiseOctaves(this.octaves2, x, 0, z, 5, 33, 5, 684.412D, 684.412D, 684.412D);
        this.octaves3 = this.noiseGen2.generateNoiseOctaves(this.octaves3, x, 0, z, 5, 33, 5, 684.412D, 684.412D, 684.412D);
        int i = 0;
        int j = 0;

        for (int k = 0; k < 5; ++k)
        {
            for (int l = 0; l < 5; ++l)
            {
                float f2 = 0.0F;
                float f3 = 0.0F;
                float f4 = 0.0F;
                int i1 = 2;
                Biome biomegenbase = this.biomesForGeneration[k + 2 + (l + 2) * 10];

                for (int j1 = -i1; j1 <= i1; ++j1)
                {
                    for (int k1 = -i1; k1 <= i1; ++k1)
                    {
                        Biome biomegenbase1 = this.biomesForGeneration[k + j1 + 2 + (l + k1 + 2) * 10];
                        float f5 = biomegenbase1.getBaseHeight();
                        float f6 = biomegenbase1.getHeightVariation();

                        if (this.worldType == WorldType.AMPLIFIED && f5 > 0.0F)
                        {
                            f5 = 1.0F + f5 * 2.0F;
                            f6 = 1.0F + f6 * 4.0F;
                        }

                        float f7 = this.parabolicField[j1 + 2 + (k1 + 2) * 5] / (f5 + 2.0F);

                        if (biomegenbase1.getBaseHeight() > biomegenbase.getBaseHeight())
                        {
                            f7 /= 2.0F;
                        }

                        f2 += f6 * f7;
                        f3 += f5 * f7;
                        f4 += f7;
                    }
                }

                f2 = f2 / f4;
                f3 = f3 / f4;
                f2 = f2 * 0.9F + 0.1F;
                f3 = (f3 * 4.0F - 1.0F) / 8.0F;
                double d7 = this.octaves4[j] / 4000.0D;

                if (d7 < 0.0D)
                {
                    d7 = -d7 * 0.3D;
                }

                d7 = d7 * 3.0D - 2.0D;

                if (d7 < 0.0D)
                {
                    d7 = d7 / 2.0D;

                    if (d7 < -1.0D)
                    {
                        d7 = -1.0D;
                    }

                    d7 = d7 / 1.4D;
                    d7 = d7 / 2.0D;
                }
                else
                {
                    if (d7 > 1.0D)
                    {
                        d7 = 1.0D;
                    }

                    d7 = d7 / 8.0D;
                }

                ++j;
                double d8 = (double) f3;
                double d9 = (double) f2;
                d8 = d8 + d7 * 0.2D;
                d8 = d8 * 8.5 / 8.0D;
                double d0 = 8.5 + d8 * 4.0D;

                for (int l1 = 0; l1 < 33; ++l1)
                {
                    double d1 = ((double) l1 - d0) * 12.0 * 128.0D / 256.0D / d9;

                    if (d1 < 0.0D)
                    {
                        d1 *= 4.0D;
                    }

                    double d2 = this.octaves2[i] / 512.0;
                    double d3 = this.octaves3[i] / 1024.0;
                    double d4 = (this.octaves1[i] / 10.0D + 1.0D) / 2.0D;
//                    double d5 = MathHelper.clampedLerp(d2, d3, d4) - d1;
                    double d5 = d3 - d1;

                    if (l1 > 29)
                    {
                        double d6 = (double) ((float) (l1 - 29) / 3.0F);
                        d5 = d5 * (1.0D - d6) + -10.0D * d6;
                    }

                    this.terrainCalcs[i] = d5;
                    ++i;
                }
            }
        }
    }

    @Override
    public void populate(int x, int z)
    {
        BlockFalling.fallInstantly = true;
        int i = x * 16;
        int j = z * 16;
        BlockPos blockpos = new BlockPos(i, 0, j);
        Biome biomegenbase = this.world.getBiome(blockpos.add(16, 0, 16));
        this.rand.setSeed(this.world.getSeed());
        long k = this.rand.nextLong() / 2L * 2L + 1L;
        long l = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long) x * k + (long) z * l ^ this.world.getSeed());
        boolean isValley = biomegenbase instanceof BiomeAdaptive && ((BiomeAdaptive)biomegenbase).isInstance(BiomeGenVenusValley.class);

        if (this.rand.nextInt(isValley ? 3 : 10) == 0)
        {
            int i2 = this.rand.nextInt(16) + 8;
            int l2 = this.rand.nextInt(this.rand.nextInt(248) + 8);
            int k3 = this.rand.nextInt(16) + 8;

            (new WorldGenLakesVenus()).generate(this.world, this.rand, blockpos.add(i2, l2, k3));
        }


        if (isValley)
        {
            if (this.rand.nextInt(5) == 0)
            {
                int i2 = this.rand.nextInt(16) + 8;
                int k3 = this.rand.nextInt(16) + 8;
                int l2 = this.world.getTopSolidOrLiquidBlock(blockpos.add(i2, 0, k3)).getY() - 10 - this.rand.nextInt(5);

                (new WorldGenVaporPool()).generate(this.world, this.rand, blockpos.add(i2, l2, k3));
            }
            else if (this.rand.nextInt(190) == 0)
            {
                int i2 = this.rand.nextInt(16) + 8;
                int k3 = this.rand.nextInt(16) + 8;
                int l2 = this.world.getTopSolidOrLiquidBlock(blockpos.add(i2, 0, k3)).getY();

                (new WorldGenCrashedProbe()).generate(this.world, this.rand, blockpos.add(i2, l2, k3));
            }
        }

        this.dungeonGenerator.generateStructure(this.world, this.rand, new ChunkPos(x, z));

        biomegenbase.decorate(this.world, this.rand, new BlockPos(i, 0, j));
        WorldEntitySpawner.performWorldGenSpawning(this.world, biomegenbase, i + 8, j + 8, 16, 16, this.rand);

        BlockFalling.fallInstantly = false;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        Biome biomegenbase = this.world.getBiome(pos);

        return biomegenbase.getSpawnableList(creatureType);
    }

    @Override
    public void recreateStructures(Chunk chunk, int x, int z)
    {
        this.dungeonGenerator.generate(this.world, x, z, null);
    }
}