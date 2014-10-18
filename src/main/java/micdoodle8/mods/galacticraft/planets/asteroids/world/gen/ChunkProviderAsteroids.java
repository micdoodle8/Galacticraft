package micdoodle8.mods.galacticraft.planets.asteroids.world.gen;

import micdoodle8.mods.galacticraft.api.prefab.core.BlockMetaPair;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedCreeper;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSkeleton;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSpider;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedZombie;
import micdoodle8.mods.galacticraft.core.perlin.NoiseModule;
import micdoodle8.mods.galacticraft.core.perlin.generator.Billowed;
import micdoodle8.mods.galacticraft.core.perlin.generator.Gradient;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.WorldProviderAsteroids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenTrees;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChunkProviderAsteroids extends ChunkProviderGenerate
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
    final Block TALL_GRASS = Blocks.tallgrass;
    final byte TALL_GRASS_META = 1;
    final Block FLOWER = Blocks.red_flower;

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

    //	private final MapGenDungeon dungeonGenerator = new MapGenDungeon(MarsBlocks.marsBlock, 7, 8, 16, 6);

    //	{
    //		this.dungeonGenerator.otherRooms.add(new RoomEmptyMars(null, 0, 0, 0, ForgeDirection.UNKNOWN));
    //		this.dungeonGenerator.otherRooms.add(new RoomSpawnerMars(null, 0, 0, 0, ForgeDirection.UNKNOWN));
    //		this.dungeonGenerator.otherRooms.add(new RoomSpawnerMars(null, 0, 0, 0, ForgeDirection.UNKNOWN));
    //		this.dungeonGenerator.otherRooms.add(new RoomSpawnerMars(null, 0, 0, 0, ForgeDirection.UNKNOWN));
    //		this.dungeonGenerator.otherRooms.add(new RoomSpawnerMars(null, 0, 0, 0, ForgeDirection.UNKNOWN));
    //		this.dungeonGenerator.otherRooms.add(new RoomSpawnerMars(null, 0, 0, 0, ForgeDirection.UNKNOWN));
    //		this.dungeonGenerator.otherRooms.add(new RoomSpawnerMars(null, 0, 0, 0, ForgeDirection.UNKNOWN));
    //		this.dungeonGenerator.otherRooms.add(new RoomSpawnerMars(null, 0, 0, 0, ForgeDirection.UNKNOWN));
    //		this.dungeonGenerator.otherRooms.add(new RoomSpawnerMars(null, 0, 0, 0, ForgeDirection.UNKNOWN));
    //		this.dungeonGenerator.otherRooms.add(new RoomChestsMars(null, 0, 0, 0, ForgeDirection.UNKNOWN));
    //		this.dungeonGenerator.otherRooms.add(new RoomChestsMars(null, 0, 0, 0, ForgeDirection.UNKNOWN));
    //		this.dungeonGenerator.bossRooms.add(new RoomBossMars(null, 0, 0, 0, ForgeDirection.UNKNOWN));
    //		this.dungeonGenerator.treasureRooms.add(new RoomTreasureMars(null, 0, 0, 0, ForgeDirection.UNKNOWN));
    //	} TODO Asteroid dungeons?

    // DO NOT CHANGE
    private static final int CHUNK_SIZE_X = 16;
    private static final int CHUNK_SIZE_Y = 256;
    private static final int CHUNK_SIZE_Z = 16;

    private static final int MAX_ASTEROID_RADIUS = 20;
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

    //Used in populate to get the y level of the terrain
    private ArrayList<float[]> sizeYArray;
    private ArrayList<Integer> xMinArray;
    private ArrayList<Integer> zMinArray;
    private ArrayList<Integer> zSizeArray;
    private ArrayList<Integer> asteroidSizeArray;
    private ArrayList<Integer> asteroidXArray;
    private ArrayList<Integer> asteroidYArray;
    private ArrayList<Integer> asteroidZArray;

    public ChunkProviderAsteroids(World par1World, long par2, boolean par4)
    {
        super(par1World, par2, par4);
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
        this.coreHandler.addBlock(new SpecialAsteroidBlock(this.ASTEROID_STONE, this.ASTEROID_STONE_META_2, 1, .3));
        this.coreHandler.addBlock(new SpecialAsteroidBlock(this.ASTEROID_STONE, this.ASTEROID_STONE_META_0, 1, .15));
        this.shellHandler = new SpecialAsteroidBlockHandler();
        this.shellHandler.addBlock(new SpecialAsteroidBlock(this.ASTEROID_STONE, this.ASTEROID_STONE_META_1, 5, .15));
        this.shellHandler.addBlock(new SpecialAsteroidBlock(AsteroidBlocks.blockDenseIce, (byte) 0, 1, .15));
    }

    public void generateTerrain(int chunkX, int chunkZ, Block[] idArray, byte[] metaArray)
    {
        this.sizeYArray = new ArrayList<float[]>();
        this.xMinArray = new ArrayList<Integer>();
        this.zMinArray = new ArrayList<Integer>();
        this.zSizeArray = new ArrayList<Integer>();
        this.asteroidSizeArray = new ArrayList<Integer>();
        this.asteroidXArray = new ArrayList<Integer>();
        this.asteroidYArray = new ArrayList<Integer>();
        this.asteroidZArray = new ArrayList<Integer>();

        final Random random = new Random();

        //If there is an asteroid centre nearby, it might need to generate some asteroid parts in this chunk
        for (int i = chunkX - 3; i < chunkX + 3; i++)
        {
            int xx = i * 16;
            for (int k = chunkZ - 3; k < chunkZ + 3; k++)
            {
                int zz = k * 16;

                //NOTE: IF UPDATING THIS CODE also update addLargeAsteroids() which is the same algorithm
                for (int x = xx; x < xx + ChunkProviderAsteroids.CHUNK_SIZE_X; x+=2)
                {
                    for (int z = zz; z < zz + ChunkProviderAsteroids.CHUNK_SIZE_Z; z+=2)
                    {
                        if (Math.abs(this.randFromPoint(x, z)) < (this.asteroidDensity.getNoise(x, z) + .4) / ChunkProviderAsteroids.ASTEROID_CHANCE)
                        {
                            random.setSeed(x + z * 3067);
                            int y = random.nextInt(ChunkProviderAsteroids.MAX_ASTEROID_Y - ChunkProviderAsteroids.MIN_ASTEROID_Y) + ChunkProviderAsteroids.MIN_ASTEROID_Y;
                            int size = random.nextInt(ChunkProviderAsteroids.MAX_ASTEROID_RADIUS - ChunkProviderAsteroids.MIN_ASTEROID_RADIUS) + ChunkProviderAsteroids.MIN_ASTEROID_RADIUS;

                            //Add to the list of asteroids for external use
                            ((WorldProviderAsteroids) this.worldObj.provider).addAsteroid(x, y, z);
                            //Generate the parts of the asteroid which are in this chunk
                            this.generateAsteroid(random, x, y, z, chunkX * 16, chunkZ * 16, size, idArray, metaArray);
                        }
                    }
                }
            }
        }

        double density = this.asteroidDensity.getNoise(chunkX * 16, chunkZ * 16) * 0.54;
        double numOfBlocks = this.clamp(this.randFromPoint(chunkX, chunkZ), .4, 1) * ChunkProviderAsteroids.MAX_BLOCKS_PER_CHUNK * density + ChunkProviderAsteroids.MIN_BLOCKS_PER_CHUNK;

        int y0 = this.rand.nextInt(2);

        BlockMetaPair pair;

        if (y0 < 0 && this.rand.nextBoolean())
        {
            for (int i = 0; i < numOfBlocks; i++)
            {
                int y = this.rand.nextInt(ChunkProviderAsteroids.MAX_ASTEROID_Y - ChunkProviderAsteroids.MIN_ASTEROID_Y) + ChunkProviderAsteroids.MIN_ASTEROID_Y;

                if (y0 == (y / 16) % 2)
                {
                    int x = this.rand.nextInt(ChunkProviderAsteroids.CHUNK_SIZE_X);
                    int z = this.rand.nextInt(ChunkProviderAsteroids.CHUNK_SIZE_Z);

                    pair = new BlockMetaPair(this.ASTEROID_STONE, this.ASTEROID_STONE_META_1);

                    if (this.rand.nextInt(ILMENITE_CHANCE) == 0)
                    {
                        pair = new BlockMetaPair(this.ASTEROID_STONE, (byte)4);
                    }
                    else if (this.rand.nextInt(IRON_CHANCE) == 0)
                    {
                        pair = new BlockMetaPair(this.ASTEROID_STONE, (byte)5);
                    }
                    else if (this.rand.nextInt(ALUMINUM_CHANCE) == 0)
                    {
                        pair = new BlockMetaPair(this.ASTEROID_STONE, (byte)3);
                    }

                    idArray[this.getIndex(x, y, z)] = pair.getBlock();
                    metaArray[this.getIndex(x, y, z)] = pair.getMetadata();
                }
            }
        }
    }

    private void generateAsteroid(Random rand, int asteroidX, int asteroidY, int asteroidZ, int chunkX, int chunkZ, int size, Block[] blockArray, byte[] metaArray)
    {
        SpecialAsteroidBlock core = null;
        SpecialAsteroidBlock shell = null;
        if (rand.nextInt(ChunkProviderAsteroids.ASTEROID_CORE_CHANCE) == 0)
        {
            core = this.coreHandler.getBlock(rand);
        }
        if (rand.nextInt(ChunkProviderAsteroids.ASTEROID_SHELL_CHANCE) == 0)
        {
            shell = this.shellHandler.getBlock(rand);
        }

        boolean isHollow = false;
        final float hollowSize = rand.nextFloat() * (ChunkProviderAsteroids.MAX_HOLLOW_SIZE - ChunkProviderAsteroids.MIN_HOLLOW_SIZE) + ChunkProviderAsteroids.MIN_HOLLOW_SIZE;
        if (rand.nextInt(ChunkProviderAsteroids.HOLLOW_CHANCE) == 0 && size >= ChunkProviderAsteroids.MIN_RADIUS_FOR_HOLLOW)
        {
            isHollow = true;
            shell = new SpecialAsteroidBlock(AsteroidBlocks.blockDenseIce, (byte) 0, 1, .15);
        }

        final float noiseOffsetX = this.randFromPoint(asteroidX, asteroidY, asteroidZ) * ChunkProviderAsteroids.NOISE_OFFSET_SIZE;
        final float noiseOffsetY = this.randFromPoint(asteroidX * 7, asteroidY * 11, asteroidZ * 13) * ChunkProviderAsteroids.NOISE_OFFSET_SIZE;
        final float noiseOffsetZ = this.randFromPoint(asteroidX * 17, asteroidY * 23, asteroidZ * 29) * ChunkProviderAsteroids.NOISE_OFFSET_SIZE;
        final int xMin = this.clamp(Math.max(chunkX, asteroidX - size - ChunkProviderAsteroids.MAX_ASTEROID_SKEW - 2) - chunkX, 0, 16);
        final int zMin = this.clamp(Math.max(chunkZ, asteroidZ - size - ChunkProviderAsteroids.MAX_ASTEROID_SKEW - 2) - chunkZ, 0, 16);
        final int yMin = asteroidY - size - ChunkProviderAsteroids.MAX_ASTEROID_SKEW - 2;
        final int yMax = asteroidY + size + ChunkProviderAsteroids.MAX_ASTEROID_SKEW + 2;
        final int xMax = this.clamp(Math.min(chunkX + 16, asteroidX + size + ChunkProviderAsteroids.MAX_ASTEROID_SKEW + 2) - chunkX, 0, 16);
        final int zMax = this.clamp(Math.min(chunkZ + 16, asteroidZ + size + ChunkProviderAsteroids.MAX_ASTEROID_SKEW + 2) - chunkZ, 0, 16);
        final int xSize = xMax - xMin;
        final int ySize = yMax - yMin;
        final int zSize = zMax - zMin;

        if (xSize <= 0 || ySize <= 0 || zSize <=0)
            return;

        this.setOtherAxisFrequency(1F / (size * 2F / 2F));

        float[] sizeXArray = new float[ySize * zSize];
        float[] sizeZArray = new float[xSize * ySize];
        float[] sizeYArray = new float[xSize * zSize];

        for (int y = 0; y < ySize; y++)
        {
            for (int z = 0; z < zSize; z++)
            {
                sizeXArray[y * zSize + z] = this.asteroidSkewX.getNoise(y + noiseOffsetY, z + chunkZ + noiseOffsetZ);
            }
        }

        for (int x = 0; x < xSize; x++)
        {
            for (int z = 0; z < zSize; z++)
            {
                sizeYArray[x * zSize + z] = this.asteroidSkewY.getNoise(x + chunkX + noiseOffsetX, z + chunkZ + noiseOffsetZ);
            }
        }

        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                sizeZArray[x * ySize + y] = this.asteroidSkewZ.getNoise(x + chunkX + noiseOffsetX, y + noiseOffsetY);
            }
        }

        double shellThickness = 0;
        if (shell != null) shellThickness = 1.0 - shell.thickness;
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
                int indexBase = x * ChunkProviderAsteroids.CHUNK_SIZE_Y * 16 | z * ChunkProviderAsteroids.CHUNK_SIZE_Y;

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
                    float distanceAbove = distanceX / sizeX + distanceY / sizeY + distanceZ / sizeZ;
                    distanceAbove += this.asteroidTurbulance.getNoise(x + chunkX, y + 1, z + chunkZ);

                    if (distanceAbove <= 1)
                    {
                        int index = indexBase | (y + 1);
                        if (isHollow && distance <= hollowSize)
                        {
                            final int terrainY = this.getTerrainHeightFor(sizeModY, asteroidY - 1, size);
                            if ((y - 1) == terrainY)
                            {
                                blockArray[index] = this.LIGHT;
                                metaArray[index] = this.LIGHT_META;
                            }
                        }
                    }

                    if (distance <= 1)
                    {
                        int index = indexBase | y;
                        if (isHollow && distance <= hollowSize)
                        {
                            final int terrainY = this.getTerrainHeightFor(sizeModY, asteroidY, size);
                            if (y == terrainY)
                            {
                                blockArray[index] = this.GRASS;
                                metaArray[index] = this.GRASS_META;
                            }
                            else if (y < terrainY)
                            {
                                blockArray[index] = this.DIRT;
                                metaArray[index] = this.DIRT_META;
                            }
                            else
                            {
                                blockArray[index] = Blocks.air;
                                metaArray[index] = 0;
                            }
                        }
                        else if (core != null && distance <= core.thickness)
                        {
                            blockArray[index] = core.block;
                            metaArray[index] = core.meta;
                        }
                        else if (shell != null && distance >= shellThickness)
                        {
                            blockArray[index] = shell.block;
                            metaArray[index] = shell.meta;
                        }
                        else
                        {
                            blockArray[index] = this.ASTEROID_STONE;
                            metaArray[index] = this.ASTEROID_STONE_META_1;
                        }
                    }
                }
            }
        }

        shellThickness = 0;
        if (shell != null) shellThickness = 1.0 - shell.thickness;
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
                int indexBase = x * ChunkProviderAsteroids.CHUNK_SIZE_Y * 16 | z * ChunkProviderAsteroids.CHUNK_SIZE_Y;

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
                        if (isHollow)
                        {
                            if (Blocks.air == blockArray[indexAbove] && (blockArray[index] == ASTEROID_STONE || blockArray[index] == GRASS))
                            {
                                if (this.rand.nextInt(GLOWSTONE_CHANCE) == 0)
                                {
                                    blockArray[index] = this.LIGHT;
                                    metaArray[index] = this.LIGHT_META;
                                }
                            }
                        }
                    }
                }
            }
        }

        if(isHollow)
        {
            this.sizeYArray.add(sizeYArray);
            this.xMinArray.add(xMin);
            this.zMinArray.add(zMin);
            this.zSizeArray.add(zSize);
            this.asteroidSizeArray.add(size);
            this.asteroidXArray.add(asteroidX);
            this.asteroidYArray.add(asteroidY);
            this.asteroidZArray.add(asteroidZ);
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
        return (int)(((asteroidY - (asteroidSize / 4)) - (-yMod * 1.5)));
    }

    private final int getTerrainHeightAt(int x, int z, float[] yModArray, int xMin, int zMin, int zSize, int asteroidY, int asteroidSize)
    {
        final int index = (x - xMin) * zSize - zMin;
        if(index < yModArray.length && index >= 0) {
            final float yMod = yModArray[index];
            return this.getTerrainHeightFor(yMod, asteroidY, asteroidSize);
        }
        return 1;
    }

    @Override
    public Chunk provideChunk(int par1, int par2)
    {
        //long time1 = System.nanoTime();
        this.rand.setSeed(par1 * 341873128712L + par2 * 132897987541L);
        final Block[] ids = new Block[32768 * 2];
        final byte[] meta = new byte[32768 * 2];
        this.generateTerrain(par1, par2, ids, meta);
        //this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, par1 * 16, par2 * 16, 16, 16);

        //long time2 = System.nanoTime();
        final Chunk var4 = new Chunk(this.worldObj, ids, meta, par1, par2);
        final byte[] var5 = var4.getBiomeArray();

        for (int var6 = 0; var6 < var5.length; ++var6)
        {
            var5[var6] = (byte) BiomeGenBaseAsteroids.asteroid.biomeID;
        }

        //long time3 = System.nanoTime();
        var4.generateSkylightMap();
        //long time4 = System.nanoTime();
        //System.out.println("  Chunk gen terrain: " + (time2 - time1) / 1000000.0D + "ms");
        //System.out.println("  Chunk gen biomes: " + (time3 - time2) / 1000000.0D + "ms");
        //System.out.println("  Chunk gen lighting: " + (time4 - time3) / 1000000.0D + "ms");
        //System.out.println("Chunk gen total: " + (time4 - time1) / 1000000.0D + "ms");
        return var4;
    }

    private int getIndex(int x, int y, int z)
    {
        return x * ChunkProviderAsteroids.CHUNK_SIZE_Y * 16 | z * ChunkProviderAsteroids.CHUNK_SIZE_Y | y;
    }

    private float randFromPoint(int x, int y, int z)
    {
        int n = x + z * 57 + y * 571;
        n = n << 13 ^ n;
        n = n * (n * n * 15731 + 789221) + 1376312589 & 0x7fffffff;
        return 1.0F - n / 1073741824.0F;
    }

    private float randFromPoint(int x, int z)
    {
        int n = x + z * 57;
        n = n << 13 ^ n;
        n = n * (n * n * 15731 + 789221) + 1376312589 & 0x7fffffff;
        return 1.0F - n / 1073741824.0F;
    }

    @Override
    public boolean chunkExists(int par1, int par2)
    {
        return true;
    }

    @Override
    public void populate(IChunkProvider par1IChunkProvider, int chunkX, int chunkZ)
    {
        BlockFalling.fallInstantly = true;
        int x = chunkX * 16;
        int z = chunkZ * 16;
        this.worldObj.getBiomeGenForCoords(x + 16, z + 16);
        this.rand.setSeed(this.worldObj.getSeed());
        final long var7 = this.rand.nextLong() / 2L * 2L + 1L;
        final long var9 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed(chunkX * var7 + chunkZ * var9 ^ this.worldObj.getSeed());

        BlockFalling.fallInstantly = false;

        if (this.sizeYArray != null)
        {
            for(int asteroidIndex = 0; asteroidIndex < this.sizeYArray.size(); asteroidIndex++)
            {
                float[] sizeYArray = this.sizeYArray.get(asteroidIndex);
                int xMin = this.xMinArray.get(asteroidIndex);
                int zMin = this.zMinArray.get(asteroidIndex);
                int zSize = this.zSizeArray.get(asteroidIndex);
                int asteroidX = this.asteroidXArray.get(asteroidIndex);
                int asteroidY = this.asteroidYArray.get(asteroidIndex);
                int asteroidZ = this.asteroidZArray.get(asteroidIndex);
                int asteroidSize = this.asteroidSizeArray.get(asteroidIndex);
                if(rand.nextInt(ChunkProviderAsteroids.TREE_CHANCE) == 0)
                {
                    int i = rand.nextInt(16) + x + 8;
                    int k = rand.nextInt(16) + z + 8;
                    new WorldGenTrees(false).generate(worldObj, rand, i, this.getTerrainHeightAt(i - x, k - z, sizeYArray, xMin, zMin, zSize, asteroidY, asteroidSize), k);
                }
                if(rand.nextInt(ChunkProviderAsteroids.TALL_GRASS_CHANCE) == 0)
                {
                    int i = rand.nextInt(16) + x + 8;
                    int k = rand.nextInt(16) + z + 8;
                    new WorldGenTallGrass(this.TALL_GRASS, this.TALL_GRASS_META).generate(worldObj, rand, i, this.getTerrainHeightAt(i - x, k - z, sizeYArray, xMin, zMin, zSize, asteroidY, asteroidSize), k);
                }
                if(rand.nextInt(ChunkProviderAsteroids.FLOWER_CHANCE) == 0)
                {
                    int i = rand.nextInt(16) + x + 8;
                    int k = rand.nextInt(16) + z + 8;
                    new WorldGenFlowers(this.FLOWER).generate(worldObj, rand, i, this.getTerrainHeightAt(i - x, k - z, sizeYArray, xMin, zMin, zSize, asteroidY, asteroidSize), k);
                }
                if(rand.nextInt(ChunkProviderAsteroids.LAVA_CHANCE) == 0)
                {
                    int i = rand.nextInt(16) + x + 8;
                    int k = rand.nextInt(16) + z + 8;
                    new WorldGenLakes(this.LAVA).generate(worldObj, rand, i, this.getTerrainHeightAt(i - x, k - z, sizeYArray, xMin, zMin, zSize, asteroidY, asteroidSize), k);
                }
                if(rand.nextInt(ChunkProviderAsteroids.WATER_CHANCE) == 0)
                {
                    int i = rand.nextInt(16) + x + 8;
                    int k = rand.nextInt(16) + z + 8;
                    new WorldGenLakes(this.WATER).generate(worldObj, rand, i, this.getTerrainHeightAt(i - x, k - z, sizeYArray, xMin, zMin, zSize, asteroidY, asteroidSize), k);
                }
            }
        }
    }

    @Override
    public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate)
    {
        return true;
    }

    @Override
    public boolean canSave()
    {
        return true;
    }

    @Override
    public String makeString()
    {
        return "RandomLevelSource";
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType, int i, int j, int k)
    {
        if (par1EnumCreatureType == EnumCreatureType.monster)
        {
            final List monsters = new ArrayList();
            monsters.add(new SpawnListEntry(EntityEvolvedZombie.class, 2000, 1, 1));
            monsters.add(new SpawnListEntry(EntityEvolvedSpider.class, 2000, 1, 1));
            monsters.add(new SpawnListEntry(EntityEvolvedSkeleton.class, 2000, 1, 1));
            monsters.add(new SpawnListEntry(EntityEvolvedCreeper.class, 2000, 1, 1));
            return monsters;
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

                if (isLargeAsteroidAt0(xToCheck * 16, zToCheck * 16)) {
                    return new BlockVec3(xToCheck * 16, 0, zToCheck * 16);
                }

                xToCheck = (x0 >> 4) + i0;
                zToCheck = (z0 >> 4) - i1;

                if (isLargeAsteroidAt0(xToCheck * 16, zToCheck * 16)) {
                    return new BlockVec3(xToCheck * 16, 0, zToCheck * 16);
                }

                xToCheck = (x0 >> 4) - i0;
                zToCheck = (z0 >> 4) + i1;

                if (isLargeAsteroidAt0(xToCheck * 16, zToCheck * 16)) {
                    return new BlockVec3(xToCheck * 16, 0, zToCheck * 16);
                }

                xToCheck = (x0 >> 4) - i0;
                zToCheck = (z0 >> 4) - i1;

                if (isLargeAsteroidAt0(xToCheck * 16, zToCheck * 16)) {
                    return new BlockVec3(xToCheck * 16, 0, zToCheck * 16);
                }
            }
        }

        return null;
    }

    private boolean isLargeAsteroidAt0(int x0, int z0)
    {
        for (int x = x0; x < x0 + ChunkProviderAsteroids.CHUNK_SIZE_X; x += 2) {
            for (int z = z0; z < z0 + ChunkProviderAsteroids.CHUNK_SIZE_Z; z += 2) {
                if ((Math.abs(this.randFromPoint(x, z)) < (this.asteroidDensity.getNoise(x, z) + .4) / ChunkProviderAsteroids.ASTEROID_CHANCE))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public void addLargeAsteroids(int chunkX, int chunkZ)
    {
        this.sizeYArray = new ArrayList<float[]>();
        this.xMinArray = new ArrayList<Integer>();
        this.zMinArray = new ArrayList<Integer>();
        this.zSizeArray = new ArrayList<Integer>();
        this.asteroidSizeArray = new ArrayList<Integer>();
        this.asteroidXArray = new ArrayList<Integer>();
        this.asteroidYArray = new ArrayList<Integer>();
        this.asteroidZArray = new ArrayList<Integer>();

        final Random random = new Random();

        //If there is an asteroid centre nearby, it might need to generate some asteroid parts in this chunk
        for (int i = chunkX - 3; i < chunkX + 3; i++)
        {
            int xx = i * 16;
            for (int k = chunkZ - 3; k < chunkZ + 3; k++)
            {
                int zz = k * 16;

                for (int x = xx; x < xx + ChunkProviderAsteroids.CHUNK_SIZE_X; x+=2)
                {
                    for (int z = zz; z < zz + ChunkProviderAsteroids.CHUNK_SIZE_Z; z+=2)
                    {
                        if (Math.abs(this.randFromPoint(x, z)) < (this.asteroidDensity.getNoise(x, z) + .4) / ChunkProviderAsteroids.ASTEROID_CHANCE)
                        {
                            random.setSeed(x + z * 3067);
                            int y = random.nextInt(ChunkProviderAsteroids.MAX_ASTEROID_Y - ChunkProviderAsteroids.MIN_ASTEROID_Y) + ChunkProviderAsteroids.MIN_ASTEROID_Y;

                            //Add to the list of asteroids for external use
                            ((WorldProviderAsteroids) this.worldObj.provider).addAsteroid(x, y, z);
                        }
                    }
                }
            }
        }
    }
}