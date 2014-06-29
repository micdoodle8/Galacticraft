package micdoodle8.mods.galacticraft.planets.asteroids.world.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedCreeper;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSkeleton;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSpider;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedZombie;
import micdoodle8.mods.galacticraft.core.perlin.NoiseModule;
import micdoodle8.mods.galacticraft.core.perlin.generator.Billowed;
import micdoodle8.mods.galacticraft.core.perlin.generator.Gradient;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
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

public class ChunkProviderAsteroids extends ChunkProviderGenerate
{
	final Block ASTEROID_STONE = AsteroidBlocks.blockBasic;
	final byte ASTEROID_STONE_META_0 = 0;
	final byte ASTEROID_STONE_META_1 = 1;
	final byte ASTEROID_STONE_META_2 = 2;

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

	private static final int RANDOM_BLOCK_FADE_SIZE = 32;
	private static final int FADE_BLOCK_CHANCE = 5; //1 / n chance of a block being in the fade zone

	private static final int NOISE_OFFSET_SIZE = 256;

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
		this.shellHandler.addBlock(new SpecialAsteroidBlock(Blocks.ice, (byte) 0, 1, .15));
	}

	public void generateTerrain(int chunkX, int chunkZ, Block[] idArray, byte[] metaArray)
	{
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
							int size = random.nextInt(ChunkProviderAsteroids.MAX_ASTEROID_RADIUS - ChunkProviderAsteroids.MIN_ASTEROID_RADIUS) + ChunkProviderAsteroids.MIN_ASTEROID_RADIUS;

							//Generate the parts of the asteroid which are in this chunk
							this.generateAsteroid(random, x, y, z, chunkX * 16, chunkZ * 16, size, idArray, metaArray);
						}
					}
				}
			}
		}
		
//		if (distanceFromCenter >= 1)
		{
//			double density = this.asteroidDensity.getNoise(chunkX * 16, chunkZ * 16) * +.4;
//			density *= distanceFromCenter;
			double numOfBlocks = this.clamp(this.randFromPoint(chunkX, chunkZ), .4, 1) * (ChunkProviderAsteroids.MAX_BLOCKS_PER_CHUNK - ChunkProviderAsteroids.MAX_BLOCKS_PER_CHUNK) + ChunkProviderAsteroids.MIN_BLOCKS_PER_CHUNK;
//			numOfBlocks *= density;
			for (int i = 0; i < numOfBlocks; i++)
			{
				int x = this.rand.nextInt(ChunkProviderAsteroids.CHUNK_SIZE_X);
				int y = this.rand.nextInt(ChunkProviderAsteroids.CHUNK_SIZE_Y - ChunkProviderAsteroids.RANDOM_BLOCK_FADE_SIZE * 2) + ChunkProviderAsteroids.RANDOM_BLOCK_FADE_SIZE;
				if (this.rand.nextInt(ChunkProviderAsteroids.FADE_BLOCK_CHANCE) == 0)
				{
					y = this.rand.nextInt(ChunkProviderAsteroids.CHUNK_SIZE_Y);
				}
				int z = this.rand.nextInt(ChunkProviderAsteroids.CHUNK_SIZE_Z);
				idArray[this.getIndex(x, y, z)] = this.ASTEROID_STONE;
				metaArray[this.getIndex(x, y, z)] = this.ASTEROID_STONE_META_1;
			}
		}
		//System.out.println("   Time taken big asteroids: " + (time2 - time1) / 1000000.0D + "ms");
		//System.out.println("   Time taken small asteroids: " + (time3 - time2) / 1000000.0D + "ms");
		//System.out.println("   Total Time taken: " + (time2 - time1) / 1000000.0D + "ms");
	}

	private void generateAsteroid(Random rand, int asteroidX, int asteroidY, int asteroidZ, int chunkX, int chunkZ, int size, Block[] idArray, byte[] metaArray)
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
        float[] sizeYArray = new float[xSize * zSize];
        float[] sizeZArray = new float[xSize * ySize];

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
				double sizeY = size + sizeYArray[indexXZ + z];
				sizeY *= sizeY;
				int distanceZ = asteroidZ - (z + chunkZ);
				distanceZ *= distanceZ;
				int indexBase = x * ChunkProviderAsteroids.CHUNK_SIZE_Y * 16 | z * ChunkProviderAsteroids.CHUNK_SIZE_Y;

				for (int y = yMin; y < yMax; y++)
				{
					double sizeX = size + sizeXArray[(y - yMin) * zSize + z - zMin];
					double sizeZ = size + sizeZArray[indexXY + y];
					sizeX *= sizeX;
					sizeZ *= sizeZ;
					int distanceY = asteroidY - y;
					distanceY *= distanceY;
					double distance = distanceX / sizeX + distanceY / sizeY + distanceZ / sizeZ;

					distance += this.asteroidTurbulance.getNoise(x + chunkX, y, z + chunkZ);

					if (distance <= 1)
					{
						int index = indexBase | y;
						if (core != null && distance <= core.thickness)
						{
							idArray[index] = core.block;
							metaArray[index] = core.meta;
						}
						else if (shell != null && distance >= shellThickness)
						{
							idArray[index] = shell.block;
							metaArray[index] = shell.meta;
						}
						else
						{
							idArray[index] = this.ASTEROID_STONE;
							metaArray[index] = this.ASTEROID_STONE_META_1;
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
	public void populate(IChunkProvider par1IChunkProvider, int par2, int par3)
	{
		BlockFalling.fallInstantly = true;
		int var4 = par2 * 16;
		int var5 = par3 * 16;
		this.worldObj.getBiomeGenForCoords(var4 + 16, var5 + 16);
		this.rand.setSeed(this.worldObj.getSeed());
		final long var7 = this.rand.nextLong() / 2L * 2L + 1L;
		final long var9 = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed(par2 * var7 + par3 * var9 ^ this.worldObj.getSeed());

		BlockFalling.fallInstantly = false;
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
			monsters.add(new SpawnListEntry(EntityEvolvedZombie.class, 8, 2, 3));
			monsters.add(new SpawnListEntry(EntityEvolvedSpider.class, 8, 2, 3));
			monsters.add(new SpawnListEntry(EntityEvolvedSkeleton.class, 8, 2, 3));
			monsters.add(new SpawnListEntry(EntityEvolvedCreeper.class, 8, 2, 3));
			return monsters;
		}
		else
		{
			return null;
		}
	}
}
