package micdoodle8.mods.galacticraft.core.dimension.chunk;

import net.minecraft.util.Util;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.OctavesNoiseGenerator;

public class MoonChunkGenerator extends NoiseChunkGenerator<MoonGenSettings>
{
    private static final float[] BIOME_WEIGHTS = Util.make(new float[25], (weights) ->
    {
        for (int xw = -2; xw <= 2; ++xw)
        {
            for (int zw = -2; zw <= 2; ++zw)
            {
                float weight = 10.0F / (float) Math.sqrt((float) (xw * xw + zw * zw) + 0.2F);
                weights[xw + 2 + (zw + 2) * 5] = weight;
            }
        }
    });
//    public static final BlockState BLOCK_TOP = GCBlocks.blockMoon.getDefaultState().with(BlockBasicMoon.BASIC_TYPE_MOON, BlockBasicMoon.EnumBlockBasicMoon.MOON_TURF);
//    public static final BlockState BLOCK_FILL = GCBlocks.blockMoon.getDefaultState().with(BlockBasicMoon.BASIC_TYPE_MOON, BlockBasicMoon.EnumBlockBasicMoon.MOON_DIRT);
//    public static final BlockState BLOCK_LOWER = GCBlocks.blockMoon.getDefaultState().with(BlockBasicMoon.BASIC_TYPE_MOON, BlockBasicMoon.EnumBlockBasicMoon.MOON_STONE);
//
//    private final Random rand;
//
//    private final NoiseModule noiseGen1;
//    private final NoiseModule noiseGen2;
//    private final NoiseModule noiseGen3;
//    private final NoiseModule noiseGen4;
//
//    private final World world;
//    private final MapGenVillageMoon villageGenerator = new MapGenVillageMoon();
//
//    private final MapGenDungeon dungeonGeneratorMoon = new MapGenDungeon(new DungeonConfiguration(GCBlocks.blockMoon.getDefaultState().with(BlockBasicMoon.BASIC_TYPE_MOON, BlockBasicMoon.EnumBlockBasicMoon.MOON_DUNGEON_BRICK), 25, 8, 16, 5, 6, RoomBoss.class, RoomTreasure.class));
//
//    private Biome[] biomesForGeneration = { BiomeAdaptive.biomeDefault };
//
//    private final MapGenBaseMeta caveGenerator = new MapGenCavesMoon();
//
//    private static final int CRATER_PROB = 300;
//
//    // DO NOT CHANGE
//    private static final int MID_HEIGHT = 63;
//    private static final int CHUNK_SIZE_X = 16;
//    private static final int CHUNK_SIZE_Y = 128;
//    private static final int CHUNK_SIZE_Z = 16;

    private final OctavesNoiseGenerator depthNoise;

    public MoonChunkGenerator(IWorld worldIn, BiomeProvider biomeProvider, MoonGenSettings settingsIn)
    {
        super(worldIn, biomeProvider, 4, 8, 128, settingsIn, true);
        this.depthNoise = new OctavesNoiseGenerator(this.randomSeed, 15, 0);
    }

//    public ChunkProviderMoon(World par1World, long par2, boolean par4)
//    {
//        this.world = par1World;
//        this.rand = new Random(par2);
//        this.noiseGen1 = new Gradient(this.rand.nextLong(), 4, 0.25F);
//        this.noiseGen2 = new Gradient(this.rand.nextLong(), 4, 0.25F);
//        this.noiseGen3 = new Gradient(this.rand.nextLong(), 1, 0.25F);
//        this.noiseGen4 = new Gradient(this.rand.nextLong(), 1, 0.25F);
//        depthNoise = new OctavesNoiseGenerator(randomSeed, 16);
//    }

//    public void setBlocksInChunk(int chunkX, int chunkZ, ChunkPrimer primer)
//    {
//        this.noiseGen1.setFrequency(0.0125F);
//        this.noiseGen2.setFrequency(0.015F);
//        this.noiseGen3.setFrequency(0.01F);
//        this.noiseGen4.setFrequency(0.02F);
//
//        for (int x = 0; x < ChunkProviderMoon.CHUNK_SIZE_X; x++)
//        {
//            for (int z = 0; z < ChunkProviderMoon.CHUNK_SIZE_Z; z++)
//            {
//                final double d = this.noiseGen1.getNoise(x + chunkX * 16, z + chunkZ * 16) * 8;
//                final double d2 = this.noiseGen2.getNoise(x + chunkX * 16, z + chunkZ * 16) * 24;
//                double d3 = this.noiseGen3.getNoise(x + chunkX * 16, z + chunkZ * 16) - 0.1;
//                d3 *= 4;
//
//                double yDev;
//
//                if (d3 < 0.0D)
//                {
//                    yDev = d;
//                }
//                else if (d3 > 1.0D)
//                {
//                    yDev = d2;
//                }
//                else
//                {
//                    yDev = d + (d2 - d) * d3;
//                }
//
//                for (int y = 0; y < ChunkProviderMoon.CHUNK_SIZE_Y; y++)
//                {
//                    if (y < ChunkProviderMoon.MID_HEIGHT + yDev)
//                    {
//                        primer.setBlockState(x, y, z, BLOCK_LOWER);
//                    }
//                }
//            }
//        }
//    }
//
//    public void replaceBlocksForBiome(int par1, int par2, ChunkPrimer primer, Biome[] par4ArrayOfBiome)
//    {
//        final int var5 = 20;
//        for (int var8 = 0; var8 < 16; ++var8)
//        {
//            for (int var9 = 0; var9 < 16; ++var9)
//            {
//                final int var12 = (int) (this.noiseGen4.getNoise(var8 + par1 * 16, var9 * par2 * 16) / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
//                int var13 = -1;
//                BlockState state0 = BLOCK_TOP;
//                BlockState state1 = BLOCK_FILL;
//
//                for (int var16 = 127; var16 >= 0; --var16)
//                {
//                    final int index = this.getIndex(var8, var16, var9);
//
//                    if (var16 <= this.rand.nextInt(5))
//                    {
//                        primer.setBlockState(var8, var16, var9, Blocks.BEDROCK.getDefaultState());
//                    }
//                    else
//                    {
//                        BlockState var18 = primer.getBlockState(var8, var16, var9);
//                        if (Blocks.AIR == var18.getBlock())
//                        {
//                            var13 = -1;
//                        }
//                        else if (var18 == BLOCK_LOWER)
//                        {
//                            if (var13 == -1)
//                            {
//                                if (var12 <= 0)
//                                {
//                                    state0 = Blocks.AIR.getDefaultState();
//                                    state1 = BLOCK_LOWER;
//                                }
//                                else if (var16 >= var5 - -16 && var16 <= var5 + 1)
//                                {
//                                    state0 = BLOCK_FILL;
//                                }
//
//                                var13 = var12;
//
//                                if (var16 >= var5 - 1)
//                                {
//                                    primer.setBlockState(var8, var16, var9, state0);
//                                }
//                                else if (var16 < var5 - 1 && var16 >= var5 - 2)
//                                {
//                                    primer.setBlockState(var8, var16, var9, state1);
//                                }
//                            }
//                            else if (var13 > 0)
//                            {
//                                --var13;
//                                primer.setBlockState(var8, var16, var9, state1);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public Chunk generateChunk(int x, int z)
//    {
//        this.rand.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
//        ChunkPrimer chunkprimer = new ChunkPrimer();
//        this.setBlocksInChunk(x, z, chunkprimer);
//        this.createCraters(x, z, chunkprimer);
//        this.replaceBlocksForBiome(x, z, chunkprimer, null);
//
//        this.caveGenerator.generate(this.world, x, z, chunkprimer);
//
//        this.dungeonGeneratorMoon.generate(this.world, x, z, chunkprimer);
//        this.villageGenerator.generate(this.world, x, z, chunkprimer);
//
//        Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
//        byte[] abyte = chunk.getBiomeArray();
//        final byte b = (byte) Biome.getIdForBiome( BiomeAdaptive.biomeDefault );
//        for (int i = 0; i < abyte.length; ++i)
//        {
//            abyte[i] = b;
//        }
//
//        chunk.generateSkylightMap();
//        return chunk;
//    }

//    private void createCraters(int chunkX, int chunkZ, ChunkPrimer primer)
//    {
//        for (int cx = chunkX - 2; cx <= chunkX + 2; cx++)
//        {
//            for (int cz = chunkZ - 2; cz <= chunkZ + 2; cz++)
//            {
//                for (int x = 0; x < ChunkProviderMoon.CHUNK_SIZE_X; x++)
//                {
//                    for (int z = 0; z < ChunkProviderMoon.CHUNK_SIZE_Z; z++)
//                    {
//                        if (Math.abs(this.randFromPoint(cx * 16 + x, (cz * 16 + z) * 1000)) < this.noiseGen4.getNoise(x * ChunkProviderMoon.CHUNK_SIZE_X + x, cz * ChunkProviderMoon.CHUNK_SIZE_Z + z) / ChunkProviderMoon.CRATER_PROB)
//                        {
//                            final Random random = new Random(cx * 16 + x + (cz * 16 + z) * 5000);
//                            final EnumCraterSize cSize = EnumCraterSize.sizeArray[random.nextInt(EnumCraterSize.sizeArray.length)];
//                            final int size = random.nextInt(cSize.MAX_SIZE - cSize.MIN_SIZE) + cSize.MIN_SIZE;
//                            this.makeCrater(cx * 16 + x, cz * 16 + z, chunkX * 16, chunkZ * 16, size, primer);
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private void makeCrater(int craterX, int craterZ, int chunkX, int chunkZ, int size, ChunkPrimer primer)
//    {
//        for (int x = 0; x < ChunkProviderMoon.CHUNK_SIZE_X; x++)
//        {
//            for (int z = 0; z < ChunkProviderMoon.CHUNK_SIZE_Z; z++)
//            {
//                double xDev = craterX - (chunkX + x);
//                double zDev = craterZ - (chunkZ + z);
//                if (xDev * xDev + zDev * zDev < size * size)
//                {
//                    xDev /= size;
//                    zDev /= size;
//                    final double sqrtY = xDev * xDev + zDev * zDev;
//                    double yDev = sqrtY * sqrtY * 6;
//                    yDev = 5 - yDev;
//                    int helper = 0;
//                    for (int y = 127; y > 0; y--)
//                    {
//                        if (Blocks.AIR != primer.getBlockState(x, y, z).getBlock() && helper <= yDev)
//                        {
//                            primer.setBlockState(x, y, z, Blocks.AIR.getDefaultState());
//                            helper++;
//                        }
//                        if (helper > yDev)
//                        {
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private int getIndex(int x, int y, int z)
//    {
//        return (x * 16 + z) * 256 + y;
//    }
//
//    private double randFromPoint(int x, int z)
//    {
//        int n;
//        n = x + z * 57;
//        n = n << 13 ^ n;
//        return 1.0 - (n * (n * n * 15731 + 789221) + 1376312589 & 0x7fffffff) / 1073741824.0;
//    }
//
//    @Override
//    public void populate(int x, int z)
//    {
//        FallingBlock.fallInstantly = true;
//        int i = x * 16;
//        int j = z * 16;
//        BlockPos blockpos = new BlockPos(i, 0, j);
//        Biome biomegenbase = this.world.getBiome(blockpos.add(16, 0, 16));
//        this.rand.setSeed(this.world.getSeed());
//        long k = this.rand.nextLong() / 2L * 2L + 1L;
//        long l = this.rand.nextLong() / 2L * 2L + 1L;
//        this.rand.setSeed((long) x * k + (long) z * l ^ this.world.getSeed());
//
//        if (!ConfigManagerCore.INSTANCE.disableMoonVillageGen)
//        {
//            this.villageGenerator.generateStructure(this.world, this.rand, new ChunkPos(x, z));
//        }
//
//        this.dungeonGeneratorMoon.generateStructure(this.world, this.rand, new ChunkPos(x, z));
//
//        biomegenbase.decorate(this.world, this.rand, new BlockPos(i, 0, j));
//        FallingBlock.fallInstantly = false;
//    }
//
//    @Override
//    public List<Biome.SpawnListEntry> getPossibleCreatures(EntityClassification creatureType, BlockPos pos)
//    {
//        Biome biomegenbase = this.world.getBiome(pos);
//        return biomegenbase.getSpawnableList(creatureType);
//    }
//
//    @Override
//    public void recreateStructures(Chunk chunk, int x, int z)
//    {
//        if (!ConfigManagerCore.INSTANCE.disableMoonVillageGen)
//        {
//            this.villageGenerator.generate(this.world, x, z, null);
//        }
//
//        this.dungeonGeneratorMoon.generate(this.world, x, z, null);
//    }

    // get depth / scale
    @Override
    protected double[] getBiomeNoiseColumn(int x, int z)
    {
        double[] depthAndScale = new double[2];
        float scaleF1 = 0.0F;
        float depthF1 = 0.0F;
        float divisor = 0.0F;
        int j = this.getSeaLevel();
        float baseDepth = this.biomeProvider.getNoiseBiome(x, j, z).getDepth();

        for (int xMod = -2; xMod <= 2; ++xMod)
        {
            for (int zMod = -2; zMod <= 2; ++zMod)
            {
                Biome biomeAt = this.biomeProvider.getNoiseBiome(x + xMod, j, z + zMod);
                float biomeDepth = biomeAt.getDepth();
                float biomeScale = biomeAt.getScale();

                float weight = BIOME_WEIGHTS[xMod + 2 + (zMod + 2) * 5] / (biomeDepth + 2.0F);
                if (biomeAt.getDepth() > baseDepth)
                {
                    weight /= 2.0F;
                }

                scaleF1 += biomeScale * weight;
                depthF1 += biomeDepth * weight;
                divisor += weight;
            }
        }

        scaleF1 /= divisor;
        depthF1 /= divisor;
        scaleF1 = scaleF1 * 0.9F + 0.1F;
        depthF1 = (depthF1 * 4.0F - 1.0F) / 8.0F;
        depthAndScale[0] = (double) depthF1 + this.getSpecialDepth(x, z);
        depthAndScale[1] = scaleF1;
        return depthAndScale;
    }

    private double getSpecialDepth(int x, int z)
    {
        double sDepth = this.depthNoise.getValue(x * 200, 10.0D, z * 200, 1.0D, 0.0D, true) / 8000.0D;
        if (sDepth < 0.0D)
        {
            sDepth = -sDepth * 0.3D;
        }

        sDepth = sDepth * 3.0D - 2.0D;
        if (sDepth < 0.0D)
        {
            sDepth /= 28.0D;
        }
        else
        {
            if (sDepth > 1.0D)
            {
                sDepth = 1.0D;
            }

            sDepth /= 40.0D;
        }

        return sDepth;
    }

    // yoffset
    @Override
    protected double func_222545_a(double depth, double scale, int yy)
    {
        // The higher this value is, the higher the terrain is!
        final double baseSize = 17D;
        double yOffsets = ((double) yy - (baseSize + depth * baseSize / 8.0D * 4.0D)) * 12.0D * 128.0D / 256.0D / scale;
        if (yOffsets < 0.0D)
        {
            yOffsets *= 4.0D;
        }

        return yOffsets;
    }

    // populate noise
    @Override
    protected void fillNoiseColumn(double[] noiseColumn, int x, int z)
    {
        double xzScale = 684.4119873046875D;
        double yScale = 684.4119873046875D;
        double xzOtherScale = 8.555149841308594D;
        double yOtherScale = 4.277574920654297D;

        final int topSlideMax = 0;
        final int topSlideScale = 3;

        calcNoiseColumn(noiseColumn, x, z, xzScale, yScale, xzOtherScale, yOtherScale, topSlideScale, topSlideMax);
    }

    @Override
    public int getGroundHeight()
    {
        return 69;
    }

    @Override
    public int getSeaLevel()
    {
        return super.getSeaLevel();
    }
}
