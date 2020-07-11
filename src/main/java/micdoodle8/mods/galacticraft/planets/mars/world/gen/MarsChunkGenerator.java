package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import micdoodle8.mods.galacticraft.planets.mars.dimension.MarsGenSettings;
import micdoodle8.mods.galacticraft.planets.venus.dimension.VenusGenSettings;
import net.minecraft.util.Util;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.OctavesNoiseGenerator;

public class MarsChunkGenerator extends NoiseChunkGenerator<MarsGenSettings>
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

//    private final BiomeDecoratorMars marsBiomeDecorator = new BiomeDecoratorMars();
//    private final MapGenCavernMars caveGenerator = new MapGenCavernMars();
//    private final MapGenCaveMars cavernGenerator = new MapGenCaveMars();
//
//    private final MapGenDungeon dungeonGenerator = new MapGenDungeonMars(new DungeonConfiguration(MarsBlocks.marsBlock.getDefaultState().with(BlockBasicMars.BASIC_TYPE, BlockBasicMars.EnumBlockBasic.DUNGEON_BRICK), 30, 8, 16, 7, 7, RoomBossMars.class, RoomTreasureMars.class));

    private final OctavesNoiseGenerator depthNoise;

    public MarsChunkGenerator(IWorld worldIn, BiomeProvider biomeProvider, MarsGenSettings settingsIn)
    {
        super(worldIn, biomeProvider, 4, 8, 128, settingsIn, true);
        this.depthNoise = new OctavesNoiseGenerator(this.randomSeed, 15, 0);
    }

//    public ChunkProviderMars(World par1World, long seed, boolean mapFeaturesEnabled)
//    {
//        super(par1World, seed, mapFeaturesEnabled);
//    }

//    @Override
//    protected BiomeDecoratorSpace getBiomeGenerator()
//    {
//        return this.marsBiomeDecorator;
//    }
//
//    @Override
//    protected Biome[] getBiomesForGeneration()
//    {
//        return new Biome[] { BiomeAdaptive.biomeDefault };
//    }
//
//    @Override
//    protected int getSeaLevel()
//    {
//        return 93;
//    }
//
//    @Override
//    protected List<MapGenBaseMeta> getWorldGenerators()
//    {
//        List<MapGenBaseMeta> generators = Lists.newArrayList();
//        generators.add(this.caveGenerator);
//        generators.add(this.cavernGenerator);
//        return generators;
//    }
//
//    @Override
//    protected BlockMetaPair getGrassBlock()
//    {
//        return BiomeMars.BLOCK_TOP;
//    }
//
//    @Override
//    protected BlockMetaPair getDirtBlock()
//    {
//        return BiomeMars.BLOCK_FILL;
//    }
//
//    @Override
//    protected BlockMetaPair getStoneBlock()
//    {
//        return BiomeMars.BLOCK_LOWER;
//    }
//
//    @Override
//    public double getHeightModifier()
//    {
//        return 12;
//    }
//
//    @Override
//    public double getSmallFeatureHeightModifier()
//    {
//        return 26;
//    }
//
//    @Override
//    public double getMountainHeightModifier()
//    {
//        return 95;
//    }
//
//    @Override
//    public double getValleyHeightModifier()
//    {
//        return 50;
//    }
//
//    @Override
//    public int getCraterProbability()
//    {
//        return 2000;
//    }
//
//    @Override
//    public void onChunkProvide(int cX, int cZ, ChunkPrimer primer)
//    {
//        this.dungeonGenerator.generate(this.world, cX, cZ, primer);
//    }
//
//    @Override
//    public void onPopulate(int cX, int cZ)
//    {
//        this.dungeonGenerator.generateStructure(this.world, this.rand, new ChunkPos(cX, cZ));
//    }
//
//    @Override
//    public void recreateStructures(Chunk chunk, int x, int z)
//    {
//        this.dungeonGenerator.generate(this.world, x, z, null);
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
                Biome biomeAt = this.biomeProvider.getNoiseBiome(x + xMod, j,z + zMod);
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
