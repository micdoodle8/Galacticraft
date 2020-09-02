package micdoodle8.mods.galacticraft.planets.asteroids.world.gen;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.perlin.NoiseModule;
import micdoodle8.mods.galacticraft.core.perlin.generator.Billowed;
import micdoodle8.mods.galacticraft.core.perlin.generator.Gradient;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.ConfigManagerPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.BlockAsteroidRock;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.AsteroidGenSettings;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.DimensionAsteroids;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

import java.util.*;

public class AsteroidChunkGenerator extends ChunkGenerator<AsteroidGenSettings>
{
    //    final Block ASTEROID_STONE = AsteroidBlocks.blockBasic;
//    final byte ASTEROID_STONE_META_0 = 0;
//    final byte ASTEROID_STONE_META_1 = 1;
//    final byte ASTEROID_STONE_META_2 = 2;
    private final BlockState ASTEROID_STONE_0 = AsteroidBlocks.rock0.getDefaultState();
    private final BlockState ASTEROID_STONE_1 = AsteroidBlocks.rock1.getDefaultState();
    private final BlockState ASTEROID_STONE_2 = AsteroidBlocks.rock2.getDefaultState();

    final Block DIRT = Blocks.DIRT;
    //    final byte DIRT_META = 0;
    final Block GRASS = Blocks.GRASS;
    //    final byte GRASS_META = 0;
    final Block LIGHT = Blocks.GLOWSTONE;
//    final byte LIGHT_META = 0;
//    TallGrassBlock.EnumType GRASS_TYPE = TallGrassBlock.EnumType.GRASS;
//    final FlowerBlock FLOWER = Blocks.RED_FLOWER;

    final Block LAVA = Blocks.LAVA;
    //    final byte LAVA_META = 0;
    final Block WATER = Blocks.WATER;
//    final byte WATER_META = 0;

//    private final Random rand;
//
//    private final World world;

    private final NoiseModule asteroidDensity;

    private final NoiseModule asteroidTurbulance;

    private final NoiseModule asteroidSkewX;
    private final NoiseModule asteroidSkewY;
    private final NoiseModule asteroidSkewZ;

    private final SpecialAsteroidBlockHandler coreHandler;
    private final SpecialAsteroidBlockHandler shellHandler;

    // DO NOT CHANGE
    private static final int CHUNK_SIZE_X = 16;
    private static final int CHUNK_SIZE_Y = 256;
    private static final int CHUNK_SIZE_Z = 16;

    private static final int MAX_ASTEROID_RADIUS = 25;
    private static final int MIN_ASTEROID_RADIUS = 5;

    private static final int MAX_ASTEROID_SKEW = 8;

    private static final int MIN_ASTEROID_Y = 48;
    private static final int MAX_ASTEROID_Y = AsteroidChunkGenerator.CHUNK_SIZE_Y - 48;

    private static final int ASTEROID_CHANCE = 800; //About 1 / n chance per XZ pair

    private static final int ASTEROID_CORE_CHANCE = 2; //1 / n chance per asteroid
    private static final int ASTEROID_SHELL_CHANCE = 2; //1 / n chance per asteroid

    private static final int MIN_BLOCKS_PER_CHUNK = 50;
    private static final int MAX_BLOCKS_PER_CHUNK = 200;

    private static final int ILMENITE_CHANCE = 400;
    private static final int IRON_CHANCE = 300;
    private static final int ALUMINUM_CHANCE = 250;

    private static final int RANDOM_BLOCK_FADE_SIZE = 32;
    private static final int FADE_BLOCK_CHANCE = 5; //1 / n chance of a block being in the fade zone

    private static final int NOISE_OFFSET_SIZE = 256;

    private static final float MIN_HOLLOW_SIZE = .6F;
    private static final float MAX_HOLLOW_SIZE = .8F;
    private static final int HOLLOW_CHANCE = 10; //1 / n chance per asteroid
    private static final int MIN_RADIUS_FOR_HOLLOW = 15;
    private static final float HOLLOW_LAVA_SIZE = .12F;

    //Per chunk per asteroid
    private static final int TREE_CHANCE = 2;
    private static final int TALL_GRASS_CHANCE = 2;
    private static final int FLOWER_CHANCE = 2;
    private static final int WATER_CHANCE = 2;
    private static final int LAVA_CHANCE = 2;
    private static final int GLOWSTONE_CHANCE = 20;

    private final LinkedList<AsteroidData> largeAsteroids = new LinkedList<>();
    private int largeCount = 0;
    private static final HashSet<BlockVec3> chunksDone = new HashSet<>();
    private int largeAsteroidsLastChunkX;
    private int largeAsteroidsLastChunkZ;
//    private final MapGenAbandonedBase dungeonGenerator = new MapGenAbandonedBase(); TODO Asteroid dungeons

    public AsteroidChunkGenerator(IWorld worldIn, BiomeProvider biomeProviderIn, AsteroidGenSettings generationSettingsIn)
    {
        super(worldIn, biomeProviderIn, generationSettingsIn);
//        this.rand = new Random(par2);

        this.asteroidDensity = new Billowed(worldIn.getRandom().nextLong(), 2, .25F);
        this.asteroidDensity.setFrequency(.009F);
        this.asteroidDensity.amplitude = .6F;

        this.asteroidTurbulance = new Gradient(worldIn.getRandom().nextLong(), 1, .2F);
        this.asteroidTurbulance.setFrequency(.08F);
        this.asteroidTurbulance.amplitude = .5F;

        this.asteroidSkewX = new Gradient(worldIn.getRandom().nextLong(), 1, 1);
        this.asteroidSkewX.amplitude = AsteroidChunkGenerator.MAX_ASTEROID_SKEW;
        this.asteroidSkewX.frequencyX = 0.005F;

        this.asteroidSkewY = new Gradient(worldIn.getRandom().nextLong(), 1, 1);
        this.asteroidSkewY.amplitude = AsteroidChunkGenerator.MAX_ASTEROID_SKEW;
        this.asteroidSkewY.frequencyY = 0.005F;

        this.asteroidSkewZ = new Gradient(worldIn.getRandom().nextLong(), 1, 1);
        this.asteroidSkewZ.amplitude = AsteroidChunkGenerator.MAX_ASTEROID_SKEW;
        this.asteroidSkewZ.frequencyZ = 0.005F;

        this.coreHandler = new SpecialAsteroidBlockHandler();
        this.coreHandler.addBlock(new SpecialAsteroidBlock(this.ASTEROID_STONE_2, 5, .3));
        this.coreHandler.addBlock(new SpecialAsteroidBlock(this.ASTEROID_STONE_1, 7, .3));
        this.coreHandler.addBlock(new SpecialAsteroidBlock(this.ASTEROID_STONE_0, 11, .25));

        if (!ConfigManagerPlanets.disableAluminumGenAsteroids)
        {
            this.coreHandler.addBlock(new SpecialAsteroidBlock(AsteroidBlocks.oreAluminum.getDefaultState(), 5, .2));
        }
        if (!ConfigManagerPlanets.disableIlmeniteGenAsteroids)
        {
            this.coreHandler.addBlock(new SpecialAsteroidBlock(AsteroidBlocks.oreIlmenite.getDefaultState(), 4, .15));
        }
        if (!ConfigManagerPlanets.disableIronGenAsteroids)
        {
            this.coreHandler.addBlock(new SpecialAsteroidBlock(AsteroidBlocks.oreIron.getDefaultState(), 3, .2));
        }
        if (ConfigManagerCore.INSTANCE.enableSiliconOreGen.get())
        {
            this.coreHandler.addBlock(new SpecialAsteroidBlock(GCBlocks.oreSilicon.getDefaultState(), 2, .15)); //TODO: Asteroids version of silicon ore
        }
        //Solid Meteoric Iron - has no config to disable
        this.coreHandler.addBlock(new SpecialAsteroidBlock(GCBlocks.oreMeteoricIron.getDefaultState(), 2, .13));
        //Diamond ore - has no config to disable
        this.coreHandler.addBlock(new SpecialAsteroidBlock(Blocks.DIAMOND_ORE.getDefaultState(), 1, .1));  //TODO: Asteroids version of diamond ore

        this.shellHandler = new SpecialAsteroidBlockHandler();
        this.shellHandler.addBlock(new SpecialAsteroidBlock(this.ASTEROID_STONE_0, 1, .15));
        this.shellHandler.addBlock(new SpecialAsteroidBlock(this.ASTEROID_STONE_1, 3, .15));
        this.shellHandler.addBlock(new SpecialAsteroidBlock(this.ASTEROID_STONE_2, 1, .15));
        this.shellHandler.addBlock(new SpecialAsteroidBlock(AsteroidBlocks.blockDenseIce.getDefaultState(), 1, .15));
    }

    public void generateTerrain(int chunkX, int chunkZ, ChunkPrimer primer, boolean flagDataOnly)
    {
        this.largeAsteroids.clear();
        this.largeCount = 0;
        final Random random = new Random();
        final int asteroidChance = AsteroidChunkGenerator.ASTEROID_CHANCE;
        final int rangeY = AsteroidChunkGenerator.MAX_ASTEROID_Y - AsteroidChunkGenerator.MIN_ASTEROID_Y;
        final int rangeSize = AsteroidChunkGenerator.MAX_ASTEROID_RADIUS - AsteroidChunkGenerator.MIN_ASTEROID_RADIUS;

        //If there is an asteroid centre nearby, it might need to generate some asteroid parts in this chunk
        for (int i = chunkX - 3; i < chunkX + 3; i++)
        {
            int minX = i * 16;
            int maxX = minX + AsteroidChunkGenerator.CHUNK_SIZE_X;
            for (int k = chunkZ - 3; k < chunkZ + 3; k++)
            {
                int minZ = k * 16;
                int maxZ = minZ + AsteroidChunkGenerator.CHUNK_SIZE_Z;

                //NOTE: IF UPDATING THIS CODE also update addLargeAsteroids() which is the same algorithm
                //??? ^^ this now seems redundant
                for (int x = minX; x < maxX; x += 2)
                {
                    for (int z = minZ; z < maxZ; z += 2)
                    {
                        //The next line is called 3136 times per chunk generated.  getNoise is a little slow.
                        if (this.randFromPointPos(x, z) < (this.asteroidDensity.getNoise(x, z) + .4) / asteroidChance)
                        {
                            random.setSeed(x + z * 3067);
                            int y = random.nextInt(rangeY) + AsteroidChunkGenerator.MIN_ASTEROID_Y;
                            int size = random.nextInt(rangeSize) + AsteroidChunkGenerator.MIN_ASTEROID_RADIUS;

                            //Generate the parts of the asteroid which are in this chunk
                            this.generateAsteroid(random, x, y, z, chunkX << 4, chunkZ << 4, size, primer, flagDataOnly);
                            this.largeCount++;
                        }
                    }
                }
            }
        }
    }

    private void generateAsteroid(Random rand, int asteroidX, int asteroidY, int asteroidZ, int chunkX, int chunkZ, int size, ChunkPrimer primer, boolean flagDataOnly)
    {
        SpecialAsteroidBlock core = this.coreHandler.getBlock(rand, size);

        SpecialAsteroidBlock shell = null;
        if (rand.nextInt(AsteroidChunkGenerator.ASTEROID_SHELL_CHANCE) == 0)
        {
            shell = this.shellHandler.getBlock(rand, size);
        }

        boolean isHollow = false;
        final float hollowSize = rand.nextFloat() * (AsteroidChunkGenerator.MAX_HOLLOW_SIZE - AsteroidChunkGenerator.MIN_HOLLOW_SIZE) + AsteroidChunkGenerator.MIN_HOLLOW_SIZE;
        if (rand.nextInt(AsteroidChunkGenerator.HOLLOW_CHANCE) == 0 && size >= AsteroidChunkGenerator.MIN_RADIUS_FOR_HOLLOW)
        {
            isHollow = true;
            shell = new SpecialAsteroidBlock(AsteroidBlocks.blockDenseIce.getDefaultState(), 1, .15);
        }

        //Add to the list of asteroids for external use
        ((DimensionAsteroids) this.world.getDimension()).addAsteroid(asteroidX, asteroidY, asteroidZ, size, isHollow ? -1 : core.index);

        final int xMin = this.clamp(Math.max(chunkX, asteroidX - size - AsteroidChunkGenerator.MAX_ASTEROID_SKEW - 2) - chunkX, 0, 16);
        final int zMin = this.clamp(Math.max(chunkZ, asteroidZ - size - AsteroidChunkGenerator.MAX_ASTEROID_SKEW - 2) - chunkZ, 0, 16);
        final int yMin = asteroidY - size - AsteroidChunkGenerator.MAX_ASTEROID_SKEW - 2;
        final int yMax = asteroidY + size + AsteroidChunkGenerator.MAX_ASTEROID_SKEW + 2;
        final int xMax = this.clamp(Math.min(chunkX + 16, asteroidX + size + AsteroidChunkGenerator.MAX_ASTEROID_SKEW + 2) - chunkX, 0, 16);
        final int zMax = this.clamp(Math.min(chunkZ + 16, asteroidZ + size + AsteroidChunkGenerator.MAX_ASTEROID_SKEW + 2) - chunkZ, 0, 16);
        final int xSize = xMax - xMin;
        final int ySize = yMax - yMin;
        final int zSize = zMax - zMin;

        if (xSize <= 0 || ySize <= 0 || zSize <= 0)
        {
            return;
        }

        final float noiseOffsetX = this.randFromPoint(asteroidX, asteroidY, asteroidZ) * AsteroidChunkGenerator.NOISE_OFFSET_SIZE + chunkX;
        final float noiseOffsetY = this.randFromPoint(asteroidX * 7, asteroidY * 11, asteroidZ * 13) * AsteroidChunkGenerator.NOISE_OFFSET_SIZE;
        final float noiseOffsetZ = this.randFromPoint(asteroidX * 17, asteroidY * 23, asteroidZ * 29) * AsteroidChunkGenerator.NOISE_OFFSET_SIZE + chunkZ;
        this.setOtherAxisFrequency(1F / (size * 2F / 2F));

        float[] sizeXArray = new float[ySize * zSize];
        float[] sizeZArray = new float[xSize * ySize];
        float[] sizeYArray = new float[xSize * zSize];

        for (int x = 0; x < xSize; x++)
        {
            int xx = x * zSize;
            float xxx = x + noiseOffsetX;
            for (int z = 0; z < zSize; z++)
            {
                sizeYArray[xx + z] = this.asteroidSkewY.getNoise(xxx, z + noiseOffsetZ);
            }
        }

        AsteroidData asteroidData = new AsteroidData(isHollow, sizeYArray, xMin, zMin, xMax, zMax, zSize, size, asteroidX, asteroidY, asteroidZ);
        this.largeAsteroids.add(asteroidData);
        this.largeAsteroidsLastChunkX = chunkX;
        this.largeAsteroidsLastChunkZ = chunkZ;

        if (flagDataOnly)
        {
            return;
        }

        for (int y = 0; y < ySize; y++)
        {
            int yy = y * zSize;
            float yyy = y + noiseOffsetY;
            for (int z = 0; z < zSize; z++)
            {
                sizeXArray[yy + z] = this.asteroidSkewX.getNoise(yyy, z + noiseOffsetZ);
            }
        }

        for (int x = 0; x < xSize; x++)
        {
            int xx = x * ySize;
            float xxx = x + noiseOffsetX;
            for (int y = 0; y < ySize; y++)
            {
                sizeZArray[xx + y] = this.asteroidSkewZ.getNoise(xxx, y + noiseOffsetY);
            }
        }

        double shellThickness = 0;
        int terrainY = 0;
        int terrainYY = 0;

        BlockState asteroidShell = null;
        if (shell != null)
        {
            asteroidShell = shell.state;
            shellThickness = 1.0 - shell.thickness;
        }

        BlockState asteroidCore = core.state;
        BlockState asteroidRock0 = this.ASTEROID_STONE_0;
        BlockState asteroidRock1 = this.ASTEROID_STONE_1;
        BlockState airBlock = Blocks.AIR.getDefaultState();
        BlockState dirtBlock = this.DIRT.getDefaultState();
        BlockState grassBlock = this.GRASS.getDefaultState();

        for (int x = xMax - 1; x >= xMin; x--)
        {
            int indexXY = (x - xMin) * ySize - yMin;
            int indexXZ = (x - xMin) * zSize - zMin;
            int distanceX = asteroidX - (x + chunkX);
            int indexBaseX = x * AsteroidChunkGenerator.CHUNK_SIZE_Y << 4;
            float xx = x + chunkX;

            for (int z = zMin; z < zMax; z++)
            {
                if (isHollow)
                {
                    float sizeModY = sizeYArray[indexXZ + z];
                    terrainY = this.getTerrainHeightFor(sizeModY, asteroidY, size);
                    terrainYY = this.getTerrainHeightFor(sizeModY, asteroidY - 1, size);
                }

                float sizeY = size + sizeYArray[indexXZ + z];
                sizeY *= sizeY;
                int distanceZ = asteroidZ - (z + chunkZ);
                int indexBase = indexBaseX | z * AsteroidChunkGenerator.CHUNK_SIZE_Y;
                float zz = z + chunkZ;

                for (int y = yMin; y < yMax; y++)
                {
                    float dSizeX = distanceX / (size + sizeXArray[(y - yMin) * zSize + z - zMin]);
                    float dSizeZ = distanceZ / (size + sizeZArray[indexXY + y]);
                    dSizeX *= dSizeX;
                    dSizeZ *= dSizeZ;
                    int distanceY = asteroidY - y;
                    distanceY *= distanceY;
                    float distance = dSizeX + distanceY / sizeY + dSizeZ;
                    float distanceAbove = distance;
                    distance += this.asteroidTurbulance.getNoise(xx, y, zz);

                    if (isHollow && distance <= hollowSize)
                    {
                        distanceAbove += this.asteroidTurbulance.getNoise(xx, y + 1, zz);
                        if (distanceAbove <= 1)
                        {
                            if ((y - 1) == terrainYY)
                            {
                                int index = indexBase | (y + 1);
                                primer.setBlockState(new BlockPos(x, y + 1, z), this.LIGHT.getDefaultState(), false);
//                                blockArray[index] = this.LIGHT;
//                                metaArray[index] = this.LIGHT_META;
                            }
                        }
                    }

                    if (distance <= 1)
                    {
                        int index = indexBase | y;
                        if (isHollow && distance <= hollowSize)
                        {
                            if (y == terrainY)
                            {
                                primer.setBlockState(new BlockPos(x, y, z), grassBlock, false);
//                                blockArray[index] = this.GRASS;
//                                metaArray[index] = this.GRASS_META;
                            }
                            else if (y < terrainY)
                            {
                                primer.setBlockState(new BlockPos(x, y, z), dirtBlock, false);
//                                blockArray[index] = this.DIRT;
//                                metaArray[index] = this.DIRT_META;
                            }
                            else
                            {
                                primer.setBlockState(new BlockPos(x, y, z), airBlock, false);
//                                blockArray[index] = Blocks.air;
//                                metaArray[index] = 0;
                            }
                        }
                        else if (distance <= core.thickness)
                        {
                            if (rand.nextBoolean())
                            {
                                primer.setBlockState(new BlockPos(x, y, z), asteroidCore, false);
//	                        	blockArray[index] = core.block;
//	                            metaArray[index] = core.meta;
                            }
                            else
                            {
                                primer.setBlockState(new BlockPos(x, y, z), asteroidRock0, false);
//	                        	blockArray[index] = this.ASTEROID_STONE;
//	                            metaArray[index] = this.ASTEROID_STONE_META_0;
                            }
                        }
                        else if (shell != null && distance >= shellThickness)
                        {
                            primer.setBlockState(new BlockPos(x, y, z), asteroidShell, false);
//                            blockArray[index] = shell.block;
//                            metaArray[index] = shell.meta;
                        }
                        else
                        {
                            primer.setBlockState(new BlockPos(x, y, z), asteroidRock1, false);
//                            blockArray[index] = this.ASTEROID_STONE;
//                            metaArray[index] = this.ASTEROID_STONE_META_1;
                        }
                    }
                }
            }
        }

        if (isHollow)
        {
            shellThickness = 0;
            if (shell != null)
            {
                shellThickness = 1.0 - shell.thickness;
            }
            for (int x = xMin; x < xMax; x++)
            {
                int indexXY = (x - xMin) * ySize - yMin;
                int indexXZ = (x - xMin) * zSize - zMin;
                int distanceX = asteroidX - (x + chunkX);
                distanceX *= distanceX;

                for (int z = zMin; z < zMax; z++)
                {
                    float sizeModY = sizeYArray[indexXZ + z];
                    float sizeY = size + sizeYArray[indexXZ + z];
                    sizeY *= sizeY;
                    int distanceZ = asteroidZ - (z + chunkZ);
                    distanceZ *= distanceZ;

                    for (int y = yMin; y < yMax; y++)
                    {
                        float sizeX = size + sizeXArray[(y - yMin) * zSize + z - zMin];
                        float sizeZ = size + sizeZArray[indexXY + y];
                        sizeX *= sizeX;
                        sizeZ *= sizeZ;
                        int distanceY = asteroidY - y;
                        distanceY *= distanceY;
                        float distance = distanceX / sizeX + distanceY / sizeY + distanceZ / sizeZ;
                        distance += this.asteroidTurbulance.getNoise(x + chunkX, y, z + chunkZ);

                        if (distance <= 1)
                        {
                            BlockState state = primer.getBlockState(new BlockPos(x, y, z));
                            BlockState stateAbove = primer.getBlockState(new BlockPos(x, y + 1, z));
                            if (Blocks.AIR == stateAbove.getBlock() && (state.getBlock() instanceof BlockAsteroidRock || state.getBlock() == GRASS))
                            {
                                if (this.world.getRandom().nextInt(GLOWSTONE_CHANCE) == 0)
                                {
                                    primer.setBlockState(new BlockPos(x, y, z), this.LIGHT.getDefaultState(), false);
//                                    blockArray[index] = this.LIGHT;
//                                    metaArray[index] = this.LIGHT_META;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void setOtherAxisFrequency(float frequency)
    {
        this.asteroidSkewX.frequencyY = frequency;
        this.asteroidSkewX.frequencyZ = frequency;

        this.asteroidSkewY.frequencyX = frequency;
        this.asteroidSkewY.frequencyZ = frequency;

        this.asteroidSkewZ.frequencyX = frequency;
        this.asteroidSkewZ.frequencyY = frequency;
    }

    private int clamp(int x, int min, int max)
    {
        if (x < min)
        {
            x = min;
        }
        else if (x > max)
        {
            x = max;
        }
        return x;
    }

    private double clamp(double x, double min, double max)
    {
        if (x < min)
        {
            x = min;
        }
        else if (x > max)
        {
            x = max;
        }
        return x;
    }

    private int getTerrainHeightFor(float yMod, int asteroidY, int asteroidSize)
    {
        return (int) (asteroidY - asteroidSize / 4 + yMod * 1.5F);
    }

    private int getTerrainHeightAt(int x, int z, float[] yModArray, int xMin, int zMin, int zSize, int asteroidY, int asteroidSize)
    {
        final int index = (x - xMin) * zSize - zMin;
        if (index < yModArray.length && index >= 0)
        {
            final float yMod = yModArray[index];
            return this.getTerrainHeightFor(yMod, asteroidY, asteroidSize);
        }
        return 1;
    }

    @Override
    public void makeBase(IWorld worldIn, IChunk chunkIn)
    {
        ChunkPrimer chunkprimer = (ChunkPrimer) chunkIn;
        this.generateTerrain(chunkprimer.getPos().x, chunkprimer.getPos().z, chunkprimer, false);
    }

//    @Override
//    public Chunk generateChunk(int par1, int par2)
//    {
//        ChunkPrimer primer = new ChunkPrimer();
////        long time1 = System.nanoTime();
//        this.rand.setSeed(par1 * 341873128712L + par2 * 132897987541L);
////        final Block[] ids = new Block[65536];
////        final byte[] meta = new byte[65536];
//        this.generateTerrain(par1, par2, primer, false);
//        //this.biomesForGeneration = this.world.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, par1 * 16, par2 * 16, 16, 16);
//
////        if (this.world.getDimension() instanceof DimensionAsteroids && ((DimensionAsteroids)this.world.getDimension()).checkHasAsteroids())
////        {
////            this.dungeonGenerator.generate(this.world, par1, par2, primer);
////        }
////
//////        long time2 = System.nanoTime();
////        final Chunk var4 = new Chunk(this.world, primer, par1, par2);
////        final byte[] biomesArray = var4.getBiomeArray();
////
////        final byte b = (byte) Biome.getIdForBiome( BiomeAdaptive.biomeDefault );
////        for (int i = 0; i < biomesArray.length; ++i)
////        {
////            biomesArray[i] = b;
////        }
//
////        long time3 = System.nanoTime();
//        this.generateSkylightMap(var4, par1, par2);
////        long time4 = System.nanoTime();
////        if (ConfigManagerCore.INSTANCE.enableDebug)
////        {
////	        BlockVec3 vec = new BlockVec3(par1, par2, 0);
////	        if (chunksDone.contains(vec)) System.out.println("Done chunk already at "+par1+","+par2);
////	        else chunksDone.add(vec);
////        	System.out.println("Chunk gen: " + timeString(time1, time4) + " at "+par1+","+par2 + " - L"+this.largeCount+ " H"+this.largeAsteroids.size()+ " Terrain:"+timeString(time1, time2)+ " Biomes:"+timeString(time2,time3)+ " Light:"+timeString(time3, time4));
////        }
//        return var4;
//    }

    private int getIndex(int x, int y, int z)
    {
        return x * AsteroidChunkGenerator.CHUNK_SIZE_Y * 16 | z * AsteroidChunkGenerator.CHUNK_SIZE_Y | y;
    }

    private String timeString(long time1, long time2)
    {
        int ms100 = (int) ((time2 - time1) / 10000);
        int msdecimal = ms100 % 100;
        String msd = ((ms100 < 10) ? "0" : "") + ms100;
        return "" + ms100 / 100 + "." + msd + "ms";
    }

    private float randFromPoint(int x, int y, int z)
    {
        int n = x + z * 57 + y * 571;
        n ^= n << 13;
        n = n * (n * n * 15731 + 789221) + 1376312589 & 0x7fffffff;
        return 1.0F - n / 1073741824.0F;
    }

    private float randFromPoint(int x, int z)
    {
        int n = x + z * 57;
        n ^= n << 13;
        n = n * (n * n * 15731 + 789221) + 1376312589 & 0x7fffffff;
        return 1.0F - n / 1073741824.0F;
    }

    private float randFromPointPos(int x, int z)
    {
        int n = x + z * 57;
        n ^= n << 13;
        n = n * (n * n * 15731 + 789221) + 1376312589 & 0x3fffffff;
        return 1.0F - n / 1073741824.0F;
    }


    @Override
    public int getGroundHeight()
    {
        return 0;
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type type)
    {
        return 0;
    }

    @Override
    public void generateSurface(WorldGenRegion region, IChunk chunkIn)
    {
        int chunkX = chunkIn.getPos().x;
        int chunkZ = chunkIn.getPos().z;
        int x = chunkX << 4;
        int z = chunkZ << 4;
        if (!AsteroidChunkGenerator.chunksDone.add(new BlockVec3(x, 0, z)))
        {
            return;
        }

//        BlockFalling.fallInstantly = true;
//        this.world.getBiome(new BlockPos(x + 16, 0, z + 16));
//        BlockFalling.fallInstantly = false;

        this.world.getRandom().setSeed(this.world.getSeed());
        long var7 = this.world.getRandom().nextLong() / 2L * 2L + 1L;
        long var9 = this.world.getRandom().nextLong() / 2L * 2L + 1L;
        this.world.getRandom().setSeed(chunkX * var7 + chunkZ * var9 ^ this.world.getSeed());

        //50:50 chance to include small blocks each chunk
        if (this.world.getRandom().nextBoolean())
        {
            double density = this.asteroidDensity.getNoise(chunkX * 16, chunkZ * 16) * 0.54;
            double numOfBlocks = this.clamp(this.randFromPoint(chunkX, chunkZ), .4, 1) * AsteroidChunkGenerator.MAX_BLOCKS_PER_CHUNK * density + AsteroidChunkGenerator.MIN_BLOCKS_PER_CHUNK;
            int y0 = this.world.getRandom().nextInt(2);
            BlockState state;
//            int meta;
            int yRange = AsteroidChunkGenerator.MAX_ASTEROID_Y - AsteroidChunkGenerator.MIN_ASTEROID_Y;
            x += 4;
            z += 4;

            for (int i = 0; i < numOfBlocks; i++)
            {
                int y = this.world.getRandom().nextInt(yRange) + AsteroidChunkGenerator.MIN_ASTEROID_Y;

                //50:50 chance vertically as well
                if (y0 == (y / 16) % 2)
                {
                    int px = x + this.world.getRandom().nextInt(AsteroidChunkGenerator.CHUNK_SIZE_X);
                    int pz = z + this.world.getRandom().nextInt(AsteroidChunkGenerator.CHUNK_SIZE_Z);

                    state = this.ASTEROID_STONE_1;
//                    meta = this.ASTEROID_STONE_META_1;

                    if (this.world.getRandom().nextInt(ILMENITE_CHANCE) == 0)
                    {
                        state = AsteroidBlocks.oreIlmenite.getDefaultState();

                        if (ConfigManagerPlanets.disableIlmeniteGenAsteroids)
                        {
                            continue;
                        }
                    }
                    else if (this.world.getRandom().nextInt(IRON_CHANCE) == 0)
                    {
                        state = AsteroidBlocks.oreIron.getDefaultState();

                        if (ConfigManagerPlanets.disableIronGenAsteroids)
                        {
                            continue;
                        }
                    }
                    else if (this.world.getRandom().nextInt(ALUMINUM_CHANCE) == 0)
                    {
                        state = AsteroidBlocks.oreAluminum.getDefaultState();

                        if (ConfigManagerPlanets.disableAluminumGenAsteroids)
                        {
                            continue;
                        }
                    }

                    world.setBlockState(new BlockPos(px, y, pz), state, 2);
//                    int count = 9;
//                    if (!(world.getBlockState(new BlockPos(px - 1, y, pz)).getBlock() instanceof AirBlock))
//                    {
//                        count = 1;
//                    }
//                    else if (!(world.getBlockState(new BlockPos(px - 2, y, pz)).getBlock() instanceof AirBlock))
//                    {
//                        count = 3;
//                    }
//                    else if (!(world.getBlockState(new BlockPos(px - 3, y, pz)).getBlock() instanceof AirBlock))
//                    {
//                        count = 5;
//                    }
//                    else if (!(world.getBlockState(new BlockPos(px - 4, y, pz)).getBlock() instanceof AirBlock))
//                    {
//                        count = 7;
//                    }
//LIGHTEMP                    world.setLightFor(EnumSkyBlock.BLOCK, new BlockPos(px - (count > 1 ? 1 : 0), y, pz), count);
                }
            }
        }

        if (this.largeAsteroidsLastChunkX != chunkX || this.largeAsteroidsLastChunkZ != chunkZ)
        {
            this.generateTerrain(chunkX, chunkZ, null, true);
        }

        this.world.getRandom().setSeed(chunkX * var7 + chunkZ * var9 ^ this.world.getSeed());

        //Look for hollow asteroids to populate
        if (!this.largeAsteroids.isEmpty())
        {
            for (AsteroidData asteroidIndex : new ArrayList<>(this.largeAsteroids))
            {
                if (!asteroidIndex.isHollow)
                {
                    continue;
                }

                float[] sizeYArray = asteroidIndex.sizeYArray;
                int xMin = asteroidIndex.xMinArray;
                int zMin = asteroidIndex.zMinArray;
                int zSize = asteroidIndex.zSizeArray;
                int asteroidY = asteroidIndex.asteroidYArray;
                int asteroidSize = asteroidIndex.asteroidSizeArray;
                boolean treesdone = false;

                if (ConfigManagerCore.INSTANCE.challengeAsteroidPopulation || world.getRandom().nextInt(AsteroidChunkGenerator.TREE_CHANCE) == 0)
                {
                    int treeType = world.getRandom().nextInt(3);
                    if (treeType == 1)
                    {
                        treeType = 0;
                    }
//                    BlockState log = Blocks.OAK_LOG.getDefaultState();
//                    BlockState leaves = Blocks.OAK_LEAVES.getDefaultState().with(PERSISTENT, true);
//                    BlockState leaves = Blocks.LEAVES.getDefaultState().with(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).with(LeavesBlock.CHECK_DECAY, Boolean.valueOf(false));
                    ConfiguredFeature<TreeFeatureConfig, ?> wg = (new TreeFeature(TreeFeatureConfig::deserializeJungle)).withConfiguration(DefaultBiomeFeatures.OAK_TREE_CONFIG);
                    for (int tries = 0; tries < 5; tries++)
                    {
                        int i = world.getRandom().nextInt(16) + x + 8;
                        int k = world.getRandom().nextInt(16) + z + 8;
                        if (wg.place(world, this, world.getRandom(), new BlockPos(i, this.getTerrainHeightAt(i - x, k - z, sizeYArray, xMin, zMin, zSize, asteroidY, asteroidSize), k)))
                        {
                            break;
                        }
                    }
                    treesdone = true;
                }
//                if (!treesdone || rand.nextInt(ChunkProviderAsteroids.TALL_GRASS_CHANCE) == 0)
//                {
//                    int i = rand.nextInt(16) + x + 8;
//                    int k = rand.nextInt(16) + z + 8;
//                    new TallGrassFeature(GRASS_TYPE).generate(world, rand, new BlockPos(i, this.getTerrainHeightAt(i - x, k - z, sizeYArray, xMin, zMin, zSize, asteroidY, asteroidSize), k));
//                }
//                if (rand.nextInt(ChunkProviderAsteroids.FLOWER_CHANCE) == 0)
//                {
//                    int i = rand.nextInt(16) + x + 8;
//                    int k = rand.nextInt(16) + z + 8;
//                    int[] types = new int[]{2, 4, 5, 7};
//                    new FlowersFeature(this.FLOWER, EnumFlowerType.getType(FlowerBlock.EnumFlowerColor.RED, types[rand.nextInt(types.length)])).generate(world, rand, new BlockPos(i, this.getTerrainHeightAt(i - x, k - z, sizeYArray, xMin, zMin, zSize, asteroidY, asteroidSize), k));
//                }
//                if (rand.nextInt(ChunkProviderAsteroids.LAVA_CHANCE) == 0)
//                {
//                    int i = rand.nextInt(16) + x + 8;
//                    int k = rand.nextInt(16) + z + 8;
//                    new LakesFeature(this.LAVA).generate(world, rand, new BlockPos(i, this.getTerrainHeightAt(i - x, k - z, sizeYArray, xMin, zMin, zSize, asteroidY, asteroidSize), k));
//                }
//                if (rand.nextInt(ChunkProviderAsteroids.WATER_CHANCE) == 0)
//                {
//                    int i = rand.nextInt(16) + x + 8;
//                    int k = rand.nextInt(16) + z + 8;
//                    new LakesFeature(this.WATER).generate(world, rand, new BlockPos(i, this.getTerrainHeightAt(i - x, k - z, sizeYArray, xMin, zMin, zSize, asteroidY, asteroidSize), k));
//                } TODO Asteroid features
            }
        }

        //Update all block lighting
        for (int xx = 0; xx < 16; xx++)
        {
            int xPos = x + xx;
            for (int zz = 0; zz < 16; zz++)
            {
                int zPos = z + zz;

                //Asteroid at min height 48, size 20, can't have lit blocks below 16
                for (int y = 16; y < 240; y++)
                {
//LIGHTTEMP                    world.checkLightFor(EnumSkyBlock.BLOCK, new BlockPos(xPos, y, zPos));
                }
            }
        }

//        this.dungeonGenerator.generate(this.world, chunkX, chunkZ, chunkIn);
    }

//    @Override
//    public void recreateStructures(Chunk chunk, int x, int z)
//    {
//        this.dungeonGenerator.generate(this.world, x, z, null);
//    }

    public void generateSkylightMap(Chunk chunk, int cx, int cz)
    {
//        World w = chunk.getWorld();
//        boolean flagXChunk = w.getChunkProvider().chunkExists(cx - 1, cz);
//        boolean flagZUChunk = w.getChunkProvider().chunkExists(cx, cz + 1);
//        boolean flagZDChunk = w.getChunkProvider().chunkExists(cx, cz - 1);
//        boolean flagXZUChunk = w.getChunkProvider().chunkExists(cx - 1, cz + 1);
//        boolean flagXZDChunk = w.getChunkProvider().chunkExists(cx - 1, cz - 1);

        boolean flag = world.getDimension().hasSkyLight();
//        for (int j = 0; j < 16; j++)
//        {
//            if (chunk.getBlockStorageArray()[j] == null)
//            {
//                chunk.getBlockStorageArray()[j] = new ExtendedBlockStorage(j << 4, flag);
//            }
//        }

//        int i = chunk.getTopFilledSegment();
//        chunk.heightMapMinimum = Integer.MAX_VALUE;
//
//        for (int j = 0; j < 16; ++j)
//        {
//            int k = 0;
//
//            while (k < 16)
//            {
//                chunk.precipitationHeightMap[j + (k << 4)] = -999;
//                int y = i + 15;
//
//                while (true)
//                {
//                    if (y > 0)
//                    {
//                        if (chunk.getBlockLightOpacity(new BlockPos(j, y - 1, k)) == 0)
//                        {
//                            --y;
//                            continue;
//                        }
//
//                        chunk.heightMap[k << 4 | j] = y;
//
//                        if (y < chunk.heightMapMinimum)
//                        {
//                            chunk.heightMapMinimum = y;
//                        }
//                    }
//
//                    ++k;
//                    break;
//                }
//            }
//        }

        for (AsteroidData a : this.largeAsteroids)
        {
            int yMin = a.asteroidYArray - a.asteroidSizeArray;
            int yMax = a.asteroidYArray + a.asteroidSizeArray;
            int xMin = a.xMinArray;
            if (yMin < 0)
            {
                yMin = 0;
            }
            if (yMax > 255)
            {
                yMax = 255;
            }
            if (xMin == 0)
            {
                xMin = 1;
            }
            for (int x = a.xMax - 1; x >= xMin; x--)
            {
                for (int z = a.zMinArray; z < a.zMax; z++)
                {
                    for (int y = yMin; y < yMax; y++)
                    {
                        if (chunk.getBlockState(new BlockPos(x - 1, y, z)).getBlock() instanceof AirBlock && !(chunk.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof AirBlock))
                        {
                            int count = 2;

                            if (x > 1)
                            {
                                if ((chunk.getBlockState(new BlockPos(x - 2, y, z)).getBlock() instanceof AirBlock))
                                {
                                    count += 2;
                                }
                            }
                            if (x > 2)
                            {
                                if ((chunk.getBlockState(new BlockPos(x - 3, y, z)).getBlock() instanceof AirBlock))
                                {
                                    count += 2;
                                }
                                if ((chunk.getBlockState(new BlockPos(x - 3, y + 1, z)).getBlock() instanceof AirBlock))
                                {
                                    count++;
                                }
                                if ((chunk.getBlockState(new BlockPos(x - 3, y + 1, z)).getBlock() instanceof AirBlock))
                                {
                                    count++;
                                }
                                if ((z > 0 /*|| ((xPos & 15) > 2 ? flagZDChunk : flagXZDChunk)*/) && (chunk.getBlockState(new BlockPos(x - 3, y, z - 1)).getBlock() instanceof AirBlock))
                                {
                                    count++;
                                }
                                if ((z < 15/* || ((xPos & 15) > 2 ? flagZUChunk : flagXZUChunk)*/) && (chunk.getBlockState(new BlockPos(x - 3, y, z + 1)).getBlock() instanceof AirBlock))
                                {
                                    count++;
                                }
                            }
                            if (/*flagXChunk || */x > 3)
                            {
                                if ((chunk.getBlockState(new BlockPos(x - 4, y, z)).getBlock() instanceof AirBlock))
                                {
                                    count += 2;
                                }
                                if ((chunk.getBlockState(new BlockPos(x - 4, y + 1, z)).getBlock() instanceof AirBlock))
                                {
                                    count++;
                                }
                                if ((chunk.getBlockState(new BlockPos(x - 4, y + 1, z)).getBlock() instanceof AirBlock))
                                {
                                    count++;
                                }
                                if ((z > 0/* || ((xPos & 15) > 3 ? flagZDChunk : flagXZDChunk)*/) && !(chunk.getBlockState(new BlockPos(x - 4, y, z - 1)).getBlock() instanceof AirBlock))
                                {
                                    count++;
                                }
                                if ((z < 15/* || ((xPos & 15) > 3 ? flagZUChunk : flagXZUChunk)*/) && !(chunk.getBlockState(new BlockPos(x - 4, y, z + 1)).getBlock() instanceof AirBlock))
                                {
                                    count++;
                                }
                            }
                            if (count > 12)
                            {
                                count = 12;
                            }
                            //                            chunk.setBlockState(new BlockPos(x - 1, y, z), GCBlocks.brightAir.getStateFromMeta(13 - count)); TODO ? Has bright air ever had metadata?
                            chunk.setBlockState(new BlockPos(x - 1, y, z), GCBlocks.brightAir.getDefaultState(), false);
//                            ExtendedBlockStorage extendedblockstorage = chunk.getLastExtendedBlockStorage()[y >> 4];
//                            if (extendedblockstorage != null)
//                            {
//                                extendedblockstorage.setBlockLight(x - 1, y & 15, z, count + 2);
//                            } TODO Block lighting asteroid gen?
                        }
                    }
                }
            }
        }

        chunk.setModified(true);
    }

//    public void resetBase()
//    {
//        this.dungeonGenerator.reset();
//    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EntityClassification creatureType, BlockPos pos)
    {
        Biome biome = this.world.getBiome(pos);
        return biome.getSpawns(creatureType);
    }

    /**
     * Whether a large asteroid is located the provided coordinates
     *
     * @param x0 X-Coordinate to check, in Block Coords
     * @param z0 Z-Coordinate to check, in Block Coords
     * @return True if large asteroid is located here, False if not
     */
    public BlockVec3 isLargeAsteroidAt(int x0, int z0)
    {
        int xToCheck;
        int zToCheck;
        for (int i0 = 0; i0 <= 32; i0++)
        {
            for (int i1 = -i0; i1 <= i0; i1++)
            {
                xToCheck = (x0 >> 4) + i0;
                zToCheck = (z0 >> 4) + i1;

                if (isLargeAsteroidAt0(xToCheck * 16, zToCheck * 16))
                {
                    return new BlockVec3(xToCheck * 16, 0, zToCheck * 16);
                }

                xToCheck = (x0 >> 4) + i0;
                zToCheck = (z0 >> 4) - i1;

                if (isLargeAsteroidAt0(xToCheck * 16, zToCheck * 16))
                {
                    return new BlockVec3(xToCheck * 16, 0, zToCheck * 16);
                }

                xToCheck = (x0 >> 4) - i0;
                zToCheck = (z0 >> 4) + i1;

                if (isLargeAsteroidAt0(xToCheck * 16, zToCheck * 16))
                {
                    return new BlockVec3(xToCheck * 16, 0, zToCheck * 16);
                }

                xToCheck = (x0 >> 4) - i0;
                zToCheck = (z0 >> 4) - i1;

                if (isLargeAsteroidAt0(xToCheck * 16, zToCheck * 16))
                {
                    return new BlockVec3(xToCheck * 16, 0, zToCheck * 16);
                }
            }
        }

        return null;
    }

    private boolean isLargeAsteroidAt0(int x0, int z0)
    {
        for (int x = x0; x < x0 + AsteroidChunkGenerator.CHUNK_SIZE_X; x += 2)
        {
            for (int z = z0; z < z0 + AsteroidChunkGenerator.CHUNK_SIZE_Z; z += 2)
            {
                if ((Math.abs(this.randFromPoint(x, z)) < (this.asteroidDensity.getNoise(x, z) + .4) / AsteroidChunkGenerator.ASTEROID_CHANCE))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public static void reset()
    {
        chunksDone.clear();
    }

    private static class AsteroidData
    {
        public final boolean isHollow;
        public final float[] sizeYArray;
        public final int xMinArray;
        public final int zMinArray;
        public final int xMax;
        public final int zMax;
        public final int zSizeArray;
        public final int asteroidSizeArray;
        public final int asteroidXArray;
        public final int asteroidYArray;
        public final int asteroidZArray;

        public AsteroidData(boolean hollow, float[] sizeYArray2, int xMin, int zMin, int xmax, int zmax, int zSize, int size, int asteroidX, int asteroidY, int asteroidZ)
        {
            this.isHollow = hollow;
            this.sizeYArray = sizeYArray2.clone();
            this.xMinArray = xMin;
            this.zMinArray = zMin;
            this.xMax = xmax;
            this.zMax = zmax;
            this.zSizeArray = zSize;
            this.asteroidSizeArray = size;
            this.asteroidXArray = asteroidX;
            this.asteroidYArray = asteroidY;
            this.asteroidZArray = asteroidZ;
        }
    }
}