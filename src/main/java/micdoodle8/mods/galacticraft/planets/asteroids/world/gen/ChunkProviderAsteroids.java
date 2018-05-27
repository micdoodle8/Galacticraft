package micdoodle8.mods.galacticraft.planets.asteroids.world.gen;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.world.ChunkProviderBase;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.perlin.NoiseModule;
import micdoodle8.mods.galacticraft.core.perlin.generator.Billowed;
import micdoodle8.mods.galacticraft.core.perlin.generator.Gradient;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.asteroids.ConfigManagerAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.WorldProviderAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base.MapGenAbandonedBase;
import net.minecraft.block.*;
import net.minecraft.block.BlockFlower.EnumFlowerType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenTrees;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ChunkProviderAsteroids extends ChunkProviderBase
{
    final Block ASTEROID_STONE = AsteroidBlocks.blockBasic;
    final byte ASTEROID_STONE_META_0 = 0;
    final byte ASTEROID_STONE_META_1 = 1;
    final byte ASTEROID_STONE_META_2 = 2;

    final Block DIRT = Blocks.dirt;
    final byte DIRT_META = 0;
    final Block GRASS = Blocks.grass;
    final byte GRASS_META = 0;
    final Block LIGHT = Blocks.glowstone;
    final byte LIGHT_META = 0;
    BlockTallGrass.EnumType GRASS_TYPE = BlockTallGrass.EnumType.GRASS;
    final BlockFlower FLOWER = Blocks.red_flower;

    final Block LAVA = Blocks.lava;
    final byte LAVA_META = 0;
    final Block WATER = Blocks.water;
    final byte WATER_META = 0;

    private final Random rand;

    private final World worldObj;

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
    private static final int MAX_ASTEROID_Y = ChunkProviderAsteroids.CHUNK_SIZE_Y - 48;

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

    private LinkedList<AsteroidData> largeAsteroids = new LinkedList<AsteroidData>();
    private int largeCount = 0;
    private static HashSet<BlockVec3> chunksDone = new HashSet<BlockVec3>();
    private int largeAsteroidsLastChunkX;
    private int largeAsteroidsLastChunkZ;
    private final MapGenAbandonedBase dungeonGenerator = new MapGenAbandonedBase();

    public ChunkProviderAsteroids(World par1World, long par2, boolean par4)
    {
        this.worldObj = par1World;
        this.rand = new Random(par2);

        this.asteroidDensity = new Billowed(this.rand.nextLong(), 2, .25F);
        this.asteroidDensity.setFrequency(.009F);
        this.asteroidDensity.amplitude = .6F;

        this.asteroidTurbulance = new Gradient(this.rand.nextLong(), 1, .2F);
        this.asteroidTurbulance.setFrequency(.08F);
        this.asteroidTurbulance.amplitude = .5F;

        this.asteroidSkewX = new Gradient(this.rand.nextLong(), 1, 1);
        this.asteroidSkewX.amplitude = ChunkProviderAsteroids.MAX_ASTEROID_SKEW;
        this.asteroidSkewX.frequencyX = 0.005F;

        this.asteroidSkewY = new Gradient(this.rand.nextLong(), 1, 1);
        this.asteroidSkewY.amplitude = ChunkProviderAsteroids.MAX_ASTEROID_SKEW;
        this.asteroidSkewY.frequencyY = 0.005F;

        this.asteroidSkewZ = new Gradient(this.rand.nextLong(), 1, 1);
        this.asteroidSkewZ.amplitude = ChunkProviderAsteroids.MAX_ASTEROID_SKEW;
        this.asteroidSkewZ.frequencyZ = 0.005F;

        this.coreHandler = new SpecialAsteroidBlockHandler();
        this.coreHandler.addBlock(new SpecialAsteroidBlock(this.ASTEROID_STONE, this.ASTEROID_STONE_META_2, 5, .3));
        this.coreHandler.addBlock(new SpecialAsteroidBlock(this.ASTEROID_STONE, this.ASTEROID_STONE_META_1, 7, .3));
        this.coreHandler.addBlock(new SpecialAsteroidBlock(this.ASTEROID_STONE, this.ASTEROID_STONE_META_0, 11, .25));
        
        if (!ConfigManagerAsteroids.disableAluminumGen)
        this.coreHandler.addBlock(new SpecialAsteroidBlock(this.ASTEROID_STONE, (byte) 3, 5, .2));
        if (!ConfigManagerAsteroids.disableIlmeniteGen)
        this.coreHandler.addBlock(new SpecialAsteroidBlock(this.ASTEROID_STONE, (byte) 4, 4, .15));
        if (!ConfigManagerAsteroids.disableIronGen)
        this.coreHandler.addBlock(new SpecialAsteroidBlock(this.ASTEROID_STONE, (byte) 5, 3, .2));
        if (ConfigManagerCore.enableSiliconOreGen)
            this.coreHandler.addBlock(new SpecialAsteroidBlock(GCBlocks.basicBlock, (byte) 8, 2, .15)); //TODO: Asteroids version of silicon ore
        //Solid Meteoric Iron - has no config to disable
        this.coreHandler.addBlock(new SpecialAsteroidBlock(GCBlocks.basicBlock, (byte) 12, 2, .13));
        //Diamond ore - has no config to disable
        this.coreHandler.addBlock(new SpecialAsteroidBlock(Blocks.diamond_ore, (byte) 0, 1, .1));  //TODO: Asteroids version of diamond ore
        
        this.shellHandler = new SpecialAsteroidBlockHandler();
        this.shellHandler.addBlock(new SpecialAsteroidBlock(this.ASTEROID_STONE, this.ASTEROID_STONE_META_0, 1, .15));
        this.shellHandler.addBlock(new SpecialAsteroidBlock(this.ASTEROID_STONE, this.ASTEROID_STONE_META_1, 3, .15));
        this.shellHandler.addBlock(new SpecialAsteroidBlock(this.ASTEROID_STONE, this.ASTEROID_STONE_META_2, 1, .15));
        this.shellHandler.addBlock(new SpecialAsteroidBlock(AsteroidBlocks.blockDenseIce, (byte) 0, 1, .15));
    }

    public void generateTerrain(int chunkX, int chunkZ, ChunkPrimer primer, boolean flagDataOnly)
    {
        this.largeAsteroids.clear();
        this.largeCount = 0;
        final Random random = new Random();
        final int asteroidChance = ChunkProviderAsteroids.ASTEROID_CHANCE;
        final int rangeY = ChunkProviderAsteroids.MAX_ASTEROID_Y - ChunkProviderAsteroids.MIN_ASTEROID_Y;
        final int rangeSize = ChunkProviderAsteroids.MAX_ASTEROID_RADIUS - ChunkProviderAsteroids.MIN_ASTEROID_RADIUS;

        //If there is an asteroid centre nearby, it might need to generate some asteroid parts in this chunk
        for (int i = chunkX - 3; i < chunkX + 3; i++)
        {
            int minX = i * 16;
            int maxX = minX + ChunkProviderAsteroids.CHUNK_SIZE_X;
            for (int k = chunkZ - 3; k < chunkZ + 3; k++)
            {
                int minZ = k * 16;
                int maxZ = minZ + ChunkProviderAsteroids.CHUNK_SIZE_Z;

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
                            int y = random.nextInt(rangeY) + ChunkProviderAsteroids.MIN_ASTEROID_Y;
                            int size = random.nextInt(rangeSize) + ChunkProviderAsteroids.MIN_ASTEROID_RADIUS;

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
        if (rand.nextInt(ChunkProviderAsteroids.ASTEROID_SHELL_CHANCE) == 0)
        {
            shell = this.shellHandler.getBlock(rand, size);
        }

        boolean isHollow = false;
        final float hollowSize = rand.nextFloat() * (ChunkProviderAsteroids.MAX_HOLLOW_SIZE - ChunkProviderAsteroids.MIN_HOLLOW_SIZE) + ChunkProviderAsteroids.MIN_HOLLOW_SIZE;
        if (rand.nextInt(ChunkProviderAsteroids.HOLLOW_CHANCE) == 0 && size >= ChunkProviderAsteroids.MIN_RADIUS_FOR_HOLLOW)
        {
            isHollow = true;
            shell = new SpecialAsteroidBlock(AsteroidBlocks.blockDenseIce, (byte) 0, 1, .15);
        }

        //Add to the list of asteroids for external use
        ((WorldProviderAsteroids) this.worldObj.provider).addAsteroid(asteroidX, asteroidY, asteroidZ, size, isHollow ? -1 : core.index);

        final int xMin = this.clamp(Math.max(chunkX, asteroidX - size - ChunkProviderAsteroids.MAX_ASTEROID_SKEW - 2) - chunkX, 0, 16);
        final int zMin = this.clamp(Math.max(chunkZ, asteroidZ - size - ChunkProviderAsteroids.MAX_ASTEROID_SKEW - 2) - chunkZ, 0, 16);
        final int yMin = asteroidY - size - ChunkProviderAsteroids.MAX_ASTEROID_SKEW - 2;
        final int yMax = asteroidY + size + ChunkProviderAsteroids.MAX_ASTEROID_SKEW + 2;
        final int xMax = this.clamp(Math.min(chunkX + 16, asteroidX + size + ChunkProviderAsteroids.MAX_ASTEROID_SKEW + 2) - chunkX, 0, 16);
        final int zMax = this.clamp(Math.min(chunkZ + 16, asteroidZ + size + ChunkProviderAsteroids.MAX_ASTEROID_SKEW + 2) - chunkZ, 0, 16);
        final int xSize = xMax - xMin;
        final int ySize = yMax - yMin;
        final int zSize = zMax - zMin;

        if (xSize <= 0 || ySize <= 0 || zSize <= 0)
        {
            return;
        }

        final float noiseOffsetX = this.randFromPoint(asteroidX, asteroidY, asteroidZ) * ChunkProviderAsteroids.NOISE_OFFSET_SIZE + chunkX;
        final float noiseOffsetY = this.randFromPoint(asteroidX * 7, asteroidY * 11, asteroidZ * 13) * ChunkProviderAsteroids.NOISE_OFFSET_SIZE;
        final float noiseOffsetZ = this.randFromPoint(asteroidX * 17, asteroidY * 23, asteroidZ * 29) * ChunkProviderAsteroids.NOISE_OFFSET_SIZE + chunkZ;
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

        IBlockState asteroidShell = null;
        if (shell != null)
        {
            asteroidShell = shell.block.getStateFromMeta(shell.meta);
            shellThickness = 1.0 - shell.thickness;
        }
        
        IBlockState asteroidCore = core.block.getStateFromMeta(core.meta);
        IBlockState asteroidRock0 = this.ASTEROID_STONE.getStateFromMeta(this.ASTEROID_STONE_META_0);
        IBlockState asteroidRock1 = this.ASTEROID_STONE.getStateFromMeta(this.ASTEROID_STONE_META_1);
        IBlockState airBlock = Blocks.air.getDefaultState();
        IBlockState dirtBlock = this.DIRT.getStateFromMeta(this.DIRT_META);
        IBlockState grassBlock = this.GRASS.getStateFromMeta(this.GRASS_META);
        
        for (int x = xMax - 1; x >= xMin; x--)
        {
            int indexXY = (x - xMin) * ySize - yMin;
            int indexXZ = (x - xMin) * zSize - zMin;
            int distanceX = asteroidX - (x + chunkX);
            int indexBaseX = x * ChunkProviderAsteroids.CHUNK_SIZE_Y << 4;
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
                int indexBase = indexBaseX | z * ChunkProviderAsteroids.CHUNK_SIZE_Y;
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
                                primer.setBlockState(index, this.LIGHT.getStateFromMeta(this.LIGHT_META));
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
                                primer.setBlockState(index, grassBlock);
//                                blockArray[index] = this.GRASS;
//                                metaArray[index] = this.GRASS_META;
                            }
                            else if (y < terrainY)
                            {
                                primer.setBlockState(index, dirtBlock);
//                                blockArray[index] = this.DIRT;
//                                metaArray[index] = this.DIRT_META;
                            }
                            else
                            {
                                primer.setBlockState(index, airBlock);
//                                blockArray[index] = Blocks.air;
//                                metaArray[index] = 0;
                            }
                        }
                        else if (distance <= core.thickness)
                        {
                            if (rand.nextBoolean())
                            {
                                primer.setBlockState(index, asteroidCore);
//	                        	blockArray[index] = core.block;
//	                            metaArray[index] = core.meta;
                            }
                            else
                            {
                                primer.setBlockState(index, asteroidRock0);
//	                        	blockArray[index] = this.ASTEROID_STONE;
//	                            metaArray[index] = this.ASTEROID_STONE_META_0;
                            }
                        }
                        else if (shell != null && distance >= shellThickness)
                        {
                            primer.setBlockState(index, asteroidShell);
//                            blockArray[index] = shell.block;
//                            metaArray[index] = shell.meta;
                        }
                        else
                        {
                            primer.setBlockState(index, asteroidRock1);
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
                int indexBaseX = x * ChunkProviderAsteroids.CHUNK_SIZE_Y << 4;

                for (int z = zMin; z < zMax; z++)
                {
                    float sizeModY = sizeYArray[indexXZ + z];
                    float sizeY = size + sizeYArray[indexXZ + z];
                    sizeY *= sizeY;
                    int distanceZ = asteroidZ - (z + chunkZ);
                    distanceZ *= distanceZ;
                    int indexBase = indexBaseX | z * ChunkProviderAsteroids.CHUNK_SIZE_Y;

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
                            int index = indexBase | y;
                            int indexAbove = indexBase | (y + 1);
                            IBlockState state = primer.getBlockState(index);
                            IBlockState stateAbove = primer.getBlockState(indexAbove);
                            if (Blocks.air == stateAbove.getBlock() && (state.getBlock() == ASTEROID_STONE || state.getBlock() == GRASS))
                            {
                                if (this.rand.nextInt(GLOWSTONE_CHANCE) == 0)
                                {
                                    primer.setBlockState(index, this.LIGHT.getStateFromMeta(this.LIGHT_META));
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

    private final void setOtherAxisFrequency(float frequency)
    {
        this.asteroidSkewX.frequencyY = frequency;
        this.asteroidSkewX.frequencyZ = frequency;

        this.asteroidSkewY.frequencyX = frequency;
        this.asteroidSkewY.frequencyZ = frequency;

        this.asteroidSkewZ.frequencyX = frequency;
        this.asteroidSkewZ.frequencyY = frequency;
    }

    private final int clamp(int x, int min, int max)
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

    private final double clamp(double x, double min, double max)
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

    private final int getTerrainHeightFor(float yMod, int asteroidY, int asteroidSize)
    {
        return (int) (asteroidY - asteroidSize / 4 + yMod * 1.5F);
    }

    private final int getTerrainHeightAt(int x, int z, float[] yModArray, int xMin, int zMin, int zSize, int asteroidY, int asteroidSize)
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
    public Chunk provideChunk(int par1, int par2)
    {
        ChunkPrimer primer = new ChunkPrimer();
//        long time1 = System.nanoTime();
        this.rand.setSeed(par1 * 341873128712L + par2 * 132897987541L);
//        final Block[] ids = new Block[65536];
//        final byte[] meta = new byte[65536];
        this.generateTerrain(par1, par2, primer, false);
        //this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, par1 * 16, par2 * 16, 16, 16);

        if (this.worldObj.provider instanceof WorldProviderAsteroids && ((WorldProviderAsteroids)this.worldObj.provider).checkHasAsteroids())
        {
            this.dungeonGenerator.generate(this, this.worldObj, par1, par2, primer);
        }

//        long time2 = System.nanoTime();
        final Chunk var4 = new Chunk(this.worldObj, primer, par1, par2);
        final byte[] biomesArray = var4.getBiomeArray();
        byte id = (byte) BiomeGenBaseAsteroids.asteroid.biomeID;
        for (int i = 0; i < biomesArray.length; ++i)
        {
            biomesArray[i] = id;
        }

//        long time3 = System.nanoTime();
        this.generateSkylightMap(var4, par1, par2);
//        long time4 = System.nanoTime();
//        if (ConfigManagerCore.enableDebug)
//        {       
//	        BlockVec3 vec = new BlockVec3(par1, par2, 0);
//	        if (chunksDone.contains(vec)) System.out.println("Done chunk already at "+par1+","+par2);
//	        else chunksDone.add(vec);
//        	System.out.println("Chunk gen: " + timeString(time1, time4) + " at "+par1+","+par2 + " - L"+this.largeCount+ " H"+this.largeAsteroids.size()+ " Terrain:"+timeString(time1, time2)+ " Biomes:"+timeString(time2,time3)+ " Light:"+timeString(time3, time4));
//        }
        return var4;
    }

    private int getIndex(int x, int y, int z)
    {
        return x * ChunkProviderAsteroids.CHUNK_SIZE_Y * 16 | z * ChunkProviderAsteroids.CHUNK_SIZE_Y | y;
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
    public void populate(IChunkProvider par1IChunkProvider, int chunkX, int chunkZ)
    {
        int x = chunkX << 4;
        int z = chunkZ << 4;
        if (!ChunkProviderAsteroids.chunksDone.add(new BlockVec3(x, 0, z)))
        {
            return;
        }

        BlockFalling.fallInstantly = true;
        this.worldObj.getBiomeGenForCoords(new BlockPos(x + 16, 0, z + 16));
        BlockFalling.fallInstantly = false;

        this.rand.setSeed(this.worldObj.getSeed());
        long var7 = this.rand.nextLong() / 2L * 2L + 1L;
        long var9 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed(chunkX * var7 + chunkZ * var9 ^ this.worldObj.getSeed());

        //50:50 chance to include small blocks each chunk
        if (this.rand.nextBoolean())
        {
            double density = this.asteroidDensity.getNoise(chunkX * 16, chunkZ * 16) * 0.54;
            double numOfBlocks = this.clamp(this.randFromPoint(chunkX, chunkZ), .4, 1) * ChunkProviderAsteroids.MAX_BLOCKS_PER_CHUNK * density + ChunkProviderAsteroids.MIN_BLOCKS_PER_CHUNK;
            int y0 = this.rand.nextInt(2);
            Block block;
            int meta;
            int yRange = ChunkProviderAsteroids.MAX_ASTEROID_Y - ChunkProviderAsteroids.MIN_ASTEROID_Y;
            x += 4;
            z += 4;

            for (int i = 0; i < numOfBlocks; i++)
            {
                int y = this.rand.nextInt(yRange) + ChunkProviderAsteroids.MIN_ASTEROID_Y;

                //50:50 chance vertically as well
                if (y0 == (y / 16) % 2)
                {
                    int px = x + this.rand.nextInt(ChunkProviderAsteroids.CHUNK_SIZE_X);
                    int pz = z + this.rand.nextInt(ChunkProviderAsteroids.CHUNK_SIZE_Z);

                    block = this.ASTEROID_STONE;
                    meta = this.ASTEROID_STONE_META_1;

                    if (this.rand.nextInt(ILMENITE_CHANCE) == 0)
                    {
                        meta = 4;

                        if (ConfigManagerAsteroids.disableIlmeniteGen)
                        {
                            continue;
                        }
                    }
                    else if (this.rand.nextInt(IRON_CHANCE) == 0)
                    {
                        meta = 5;

                        if (ConfigManagerAsteroids.disableIronGen)
                        {
                            continue;
                        }
                    }
                    else if (this.rand.nextInt(ALUMINUM_CHANCE) == 0)
                    {
                        meta = 3;

                        if (ConfigManagerAsteroids.disableAluminumGen)
                        {
                            continue;
                        }
                    }

                    worldObj.setBlockState(new BlockPos(px, y, pz), block.getStateFromMeta(meta), 2);
                    int count = 9;
                    if (!(worldObj.getBlockState(new BlockPos(px - 1, y, pz)).getBlock() instanceof BlockAir))
                    {
                        count = 1;
                    }
                    else if (!(worldObj.getBlockState(new BlockPos(px - 2, y, pz)).getBlock() instanceof BlockAir))
                    {
                        count = 3;
                    }
                    else if (!(worldObj.getBlockState(new BlockPos(px - 3, y, pz)).getBlock() instanceof BlockAir))
                    {
                        count = 5;
                    }
                    else if (!(worldObj.getBlockState(new BlockPos(px - 4, y, pz)).getBlock() instanceof BlockAir))
                    {
                        count = 7;
                    }
//LIGHTEMP                    worldObj.setLightFor(EnumSkyBlock.BLOCK, new BlockPos(px - (count > 1 ? 1 : 0), y, pz), count);
                }
            }
        }

        if (this.largeAsteroidsLastChunkX != chunkX || this.largeAsteroidsLastChunkZ != chunkZ)
        {
            this.generateTerrain(chunkX, chunkZ, null, true);
        }

        this.rand.setSeed(chunkX * var7 + chunkZ * var9 ^ this.worldObj.getSeed());

        //Look for hollow asteroids to populate
        if (!this.largeAsteroids.isEmpty())
        {
            for (AsteroidData asteroidIndex : new ArrayList<AsteroidData>(this.largeAsteroids))
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
                
                if (ConfigManagerCore.challengeAsteroidPopulation || rand.nextInt(ChunkProviderAsteroids.TREE_CHANCE) == 0)
                {
                    int treeType = rand.nextInt(3);
                    if (treeType == 1)
                    {
                        treeType = 0;
                    }
                    IBlockState log = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
                    IBlockState leaves = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
                    WorldGenTrees wg = new WorldGenTrees(false, 2, log, leaves, false);
                    for (int tries = 0; tries < 5; tries++)
                    {
                        int i = rand.nextInt(16) + x + 8;
                        int k = rand.nextInt(16) + z + 8;
                        if (wg.generate(worldObj, rand, new BlockPos(i, this.getTerrainHeightAt(i - x, k - z, sizeYArray, xMin, zMin, zSize, asteroidY, asteroidSize), k)))
                        {
                            break;
                        }
                    }
                    treesdone = true;
                }
                if (!treesdone || rand.nextInt(ChunkProviderAsteroids.TALL_GRASS_CHANCE) == 0)
                {
                    int i = rand.nextInt(16) + x + 8;
                    int k = rand.nextInt(16) + z + 8;
                    new WorldGenTallGrass(GRASS_TYPE).generate(worldObj, rand, new BlockPos(i, this.getTerrainHeightAt(i - x, k - z, sizeYArray, xMin, zMin, zSize, asteroidY, asteroidSize), k));
                }
                if (rand.nextInt(ChunkProviderAsteroids.FLOWER_CHANCE) == 0)
                {
                    int i = rand.nextInt(16) + x + 8;
                    int k = rand.nextInt(16) + z + 8;
                    int[] types = new int[]{2, 4, 5, 7};
                    new WorldGenFlowers(this.FLOWER, EnumFlowerType.getType(BlockFlower.EnumFlowerColor.RED, types[rand.nextInt(types.length)])).generate(worldObj, rand, new BlockPos(i, this.getTerrainHeightAt(i - x, k - z, sizeYArray, xMin, zMin, zSize, asteroidY, asteroidSize), k));
                }
                if (rand.nextInt(ChunkProviderAsteroids.LAVA_CHANCE) == 0)
                {
                    int i = rand.nextInt(16) + x + 8;
                    int k = rand.nextInt(16) + z + 8;
                    new WorldGenLakes(this.LAVA).generate(worldObj, rand, new BlockPos(i, this.getTerrainHeightAt(i - x, k - z, sizeYArray, xMin, zMin, zSize, asteroidY, asteroidSize), k));
                }
                if (rand.nextInt(ChunkProviderAsteroids.WATER_CHANCE) == 0)
                {
                    int i = rand.nextInt(16) + x + 8;
                    int k = rand.nextInt(16) + z + 8;
                    new WorldGenLakes(this.WATER).generate(worldObj, rand, new BlockPos(i, this.getTerrainHeightAt(i - x, k - z, sizeYArray, xMin, zMin, zSize, asteroidY, asteroidSize), k));
                }
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
//LIGHTTEMP                    worldObj.checkLightFor(EnumSkyBlock.BLOCK, new BlockPos(xPos, y, zPos));
                }
            }
        }

        this.dungeonGenerator.generateStructure(this.worldObj, this.rand, new ChunkCoordIntPair(chunkX, chunkZ));
    }

    @Override
    public void recreateStructures(Chunk chunk, int x, int z)
    {
        this.dungeonGenerator.generate(this, this.worldObj, x, z, null);
    }

    public void generateSkylightMap(Chunk chunk, int cx, int cz)
    {
        World w = chunk.getWorld();
        boolean flagXChunk = w.getChunkProvider().chunkExists(cx - 1, cz);
        boolean flagZUChunk = w.getChunkProvider().chunkExists(cx, cz + 1);
        boolean flagZDChunk = w.getChunkProvider().chunkExists(cx, cz - 1);
        boolean flagXZUChunk = w.getChunkProvider().chunkExists(cx - 1, cz + 1);
        boolean flagXZDChunk = w.getChunkProvider().chunkExists(cx - 1, cz - 1);

        for (int j = 0; j < 16; j++)
        {
            if (chunk.getBlockStorageArray()[j] == null)
            {
                chunk.getBlockStorageArray()[j] = new ExtendedBlockStorage(j << 4, false);
            }
        }

        int i = chunk.getTopFilledSegment();
        chunk.heightMapMinimum = Integer.MAX_VALUE;

        for (int j = 0; j < 16; ++j)
        {
            int k = 0;

            while (k < 16)
            {
                chunk.precipitationHeightMap[j + (k << 4)] = -999;
                int y = i + 15;

                while (true)
                {
                    if (y > 0)
                    {
                        if (chunk.getBlockLightOpacity(new BlockPos(j, y - 1, k)) == 0)
                        {
                            --y;
                            continue;
                        }

                        chunk.heightMap[k << 4 | j] = y;

                        if (y < chunk.heightMapMinimum)
                        {
                            chunk.heightMapMinimum = y;
                        }
                    }

                    ++k;
                    break;
                }
            }
        }

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
                        if (chunk.getBlock(x - 1, y, z) instanceof BlockAir && !(chunk.getBlock(x, y, z) instanceof BlockAir))
                        {
                            int count = 2;

                            if (x > 1)
                            {
                                if ((chunk.getBlock(x - 2, y, z) instanceof BlockAir))
                                {
                                    count += 2;
                                }
                            }
                            if (x > 2)
                            {
                                if ((chunk.getBlock(x - 3, y, z) instanceof BlockAir))
                                {
                                    count += 2;
                                }
                                if ((chunk.getBlock(x - 3, y + 1, z) instanceof BlockAir))
                                {
                                    count++;
                                }
                                if ((chunk.getBlock(x - 3, y + 1, z) instanceof BlockAir))
                                {
                                    count++;
                                }
                                if ((z > 0 /*|| ((xPos & 15) > 2 ? flagZDChunk : flagXZDChunk)*/) && (chunk.getBlock(x - 3, y, z - 1) instanceof BlockAir))
                                {
                                    count++;
                                }
                                if ((z < 15/* || ((xPos & 15) > 2 ? flagZUChunk : flagXZUChunk)*/) && (chunk.getBlock(x - 3, y, z + 1) instanceof BlockAir))
                                {
                                    count++;
                                }
                            }
                            if (/*flagXChunk || */x > 3)
                            {
                                if ((chunk.getBlock(x - 4, y, z) instanceof BlockAir))
                                {
                                    count += 2;
                                }
                                if ((chunk.getBlock(x - 4, y + 1, z) instanceof BlockAir))
                                {
                                    count++;
                                }
                                if ((chunk.getBlock(x - 4, y + 1, z) instanceof BlockAir))
                                {
                                    count++;
                                }
                                if ((z > 0/* || ((xPos & 15) > 3 ? flagZDChunk : flagXZDChunk)*/) && !(chunk.getBlock(x - 4, y, z - 1) instanceof BlockAir))
                                {
                                    count++;
                                }
                                if ((z < 15/* || ((xPos & 15) > 3 ? flagZUChunk : flagXZUChunk)*/) && !(chunk.getBlock(x - 4, y, z + 1) instanceof BlockAir))
                                {
                                    count++;
                                }
                            }
                            if (count > 12)
                            {
                                count = 12;
                            }
                            if (count > 12) count = 12;
                            chunk.setBlockState(new BlockPos(x - 1, y, z), GCBlocks.brightAir.getStateFromMeta(13 - count));
                            ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[y >> 4];
                            if (extendedblockstorage != null)
                            {
                                extendedblockstorage.setExtBlocklightValue(x - 1, y & 15, z, count + 2);
                            }
                        }
                    }
                }
            }
        }

        chunk.isModified = true;
    }

    public void resetBase()
    {
        this.dungeonGenerator.reset();
    }

    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        if (creatureType == EnumCreatureType.MONSTER)
        {
        	return BiomeGenBaseAsteroids.asteroid.getSpawnableList(creatureType);
        }
        else
        {
            return null;
        }
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
        for (int x = x0; x < x0 + ChunkProviderAsteroids.CHUNK_SIZE_X; x += 2)
        {
            for (int z = z0; z < z0 + ChunkProviderAsteroids.CHUNK_SIZE_Z; z += 2)
            {
                if ((Math.abs(this.randFromPoint(x, z)) < (this.asteroidDensity.getNoise(x, z) + .4) / ChunkProviderAsteroids.ASTEROID_CHANCE))
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

    private class AsteroidData
    {
        public boolean isHollow;
        public float[] sizeYArray;
        public int xMinArray;
        public int zMinArray;
        public int xMax;
        public int zMax;
        public int zSizeArray;
        public int asteroidSizeArray;
        public int asteroidXArray;
        public int asteroidYArray;
        public int asteroidZArray;

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