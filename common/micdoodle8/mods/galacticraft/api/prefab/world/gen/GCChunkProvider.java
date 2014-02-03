package micdoodle8.mods.galacticraft.api.prefab.world.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.api.prefab.core.BlockMetaPair;
import micdoodle8.mods.galacticraft.core.perlin.generator.Gradient;
import micdoodle8.mods.galacticraft.core.world.gen.GCCoreCraterSize;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;

/**
 * Do not include this prefab class in your released mod download.
 */
public abstract class GCChunkProvider extends ChunkProviderGenerate
{
	private final Random rand;

	private final Gradient noiseGen1;
	private final Gradient noiseGen2;
	private final Gradient noiseGen3;
	private final Gradient noiseGen4;
	private final Gradient noiseGen5;
	private final Gradient noiseGen6;
	private final Gradient noiseGen7;
	public GCBiomeDecoratorBase biomeDecoratorPlanet = this.getBiomeGenerator();

	private final World worldObj;

	private BiomeGenBase[] biomesForGeneration = this.getBiomesForGeneration();

	double[] noise1;
	double[] noise2;
	double[] noise3;
	double[] noise4;
	double[] noise5;
	double[] noise6;
	float[] field_35388_l;
	int[][] field_914_i = new int[32][32];

	private final double TERRAIN_HEIGHT_MOD = this.getHeightModifier();
	private final double SMALL_FEATURE_HEIGHT_MOD = this.getSmallFeatureHeightModifier();
	private final double MOUNTAIN_HEIGHT_MOD = this.getMountainHeightModifier();
	private final double VALLEY_HEIGHT_MOD = this.getValleyHeightModifier();
	private final int CRATER_PROB = this.getCraterProbability();

	// DO NOT CHANGE
	private final int MID_HEIGHT = this.getSeaLevel();
	private static final int CHUNK_SIZE_X = 16;
	private static final int CHUNK_SIZE_Y = 256;
	private static final int CHUNK_SIZE_Z = 16;
	private static final double MAIN_FEATURE_FILTER_MOD = 4;
	private static final double LARGE_FEATURE_FILTER_MOD = 8;
	private static final double SMALL_FEATURE_FILTER_MOD = 8;

	private List<GCCoreMapGenBaseMeta> worldGenerators;

	public GCChunkProvider(World par1World, long seed, boolean mapFeaturesEnabled)
	{
		super(par1World, seed, mapFeaturesEnabled);
		this.worldObj = par1World;
		this.rand = new Random(seed);

		this.noiseGen1 = new Gradient(this.rand.nextLong(), 4, 0.25);
		this.noiseGen2 = new Gradient(this.rand.nextLong(), 4, 0.25);
		this.noiseGen3 = new Gradient(this.rand.nextLong(), 4, 0.25);
		this.noiseGen4 = new Gradient(this.rand.nextLong(), 2, 0.25);
		this.noiseGen5 = new Gradient(this.rand.nextLong(), 1, 0.25);
		this.noiseGen6 = new Gradient(this.rand.nextLong(), 1, 0.25);
		this.noiseGen7 = new Gradient(this.rand.nextLong(), 1, 0.25);
	}

	public void generateTerrain(int chunkX, int chunkZ, short[] idArray, byte[] metaArray)
	{
		this.noiseGen1.frequency = 0.015;
		this.noiseGen2.frequency = 0.01;
		this.noiseGen3.frequency = 0.01;
		this.noiseGen4.frequency = 0.01;
		this.noiseGen5.frequency = 0.01;
		this.noiseGen6.frequency = 0.001;
		this.noiseGen7.frequency = 0.005;

		for (int x = 0; x < GCChunkProvider.CHUNK_SIZE_X; x++)
		{
			for (int z = 0; z < GCChunkProvider.CHUNK_SIZE_Z; z++)
			{
				final double baseHeight = this.noiseGen1.getNoise(chunkX * 16 + x, chunkZ * 16 + z) * this.TERRAIN_HEIGHT_MOD;
				final double smallHillHeight = this.noiseGen2.getNoise(chunkX * 16 + x, chunkZ * 16 + z) * this.SMALL_FEATURE_HEIGHT_MOD;
				double mountainHeight = Math.abs(this.noiseGen3.getNoise(chunkX * 16 + x, chunkZ * 16 + z));
				double valleyHeight = Math.abs(this.noiseGen4.getNoise(chunkX * 16 + x, chunkZ * 16 + z));
				final double featureFilter = this.noiseGen5.getNoise(chunkX * 16 + x, chunkZ * 16 + z) * GCChunkProvider.MAIN_FEATURE_FILTER_MOD;
				final double largeFilter = this.noiseGen6.getNoise(chunkX * 16 + x, chunkZ * 16 + z) * GCChunkProvider.LARGE_FEATURE_FILTER_MOD;
				final double smallFilter = this.noiseGen7.getNoise(chunkX * 16 + x, chunkZ * 16 + z) * GCChunkProvider.SMALL_FEATURE_FILTER_MOD - 0.5;
				mountainHeight = this.lerp(smallHillHeight, mountainHeight * this.MOUNTAIN_HEIGHT_MOD, this.fade(this.clamp(mountainHeight * 2, 0, 1)));
				valleyHeight = this.lerp(smallHillHeight, valleyHeight * this.VALLEY_HEIGHT_MOD - this.VALLEY_HEIGHT_MOD + 9, this.fade(this.clamp((valleyHeight + 2) * 4, 0, 1)));

				double yDev = this.lerp(valleyHeight, mountainHeight, this.fade(largeFilter));
				yDev = this.lerp(smallHillHeight, yDev, smallFilter);
				yDev = this.lerp(baseHeight, yDev, featureFilter);

				for (int y = 0; y < GCChunkProvider.CHUNK_SIZE_Y; y++)
				{
					if (y < this.MID_HEIGHT + yDev)
					{
						idArray[this.getIndex(x, y, z)] = this.getStoneBlock().getBlockID();
						metaArray[this.getIndex(x, y, z)] = this.getStoneBlock().getMetadata();
					}
				}
			}
		}
	}

	private double lerp(double d1, double d2, double t)
	{
		if (t < 0.0)
		{
			return d1;
		}
		else if (t > 1.0)
		{
			return d2;
		}
		else
		{
			return d1 + (d2 - d1) * t;
		}
	}

	private double fade(double n)
	{
		return n * n * n * (n * (n * 6 - 15) + 10);
	}

	private double clamp(double x, double min, double max)
	{
		if (x < min)
		{
			return min;
		}
		if (x > max)
		{
			return max;
		}
		return x;
	}

	public void replaceBlocksForBiome(int par1, int par2, short[] arrayOfIDs, byte[] arrayOfMeta, BiomeGenBase[] par4ArrayOfBiomeGenBase)
	{
		final int var5 = 20;
		final double var6 = 0.03125D;
		this.noiseGen4.frequency = var6 * 2;
		for (int var8 = 0; var8 < 16; ++var8)
		{
			for (int var9 = 0; var9 < 16; ++var9)
			{
				final int var12 = (int) (this.noiseGen4.getNoise(par1 * 16 + var8, par2 * 16 + var9) / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
				int var13 = -1;
				short var14 = this.getGrassBlock().getBlockID();
				byte var14m = this.getGrassBlock().getMetadata();
				short var15 = this.getDirtBlock().getBlockID();
				byte var15m = this.getDirtBlock().getMetadata();

				for (int var16 = GCChunkProvider.CHUNK_SIZE_Y - 1; var16 >= 0; --var16)
				{
					final int index = this.getIndex(var8, var16, var9);

					if (var16 <= 0 + this.rand.nextInt(5))
					{
						arrayOfIDs[index] = (short) Block.bedrock.blockID;
					}
					else
					{
						final int var18 = arrayOfIDs[index];

						if (var18 == 0)
						{
							var13 = -1;
						}
						else if (var18 == this.getStoneBlock().getBlockID())
						{
							arrayOfMeta[index] = this.getStoneBlock().getMetadata();

							if (var13 == -1)
							{
								if (var12 <= 0)
								{
									var14 = 0;
									var14m = 0;
									var15 = this.getStoneBlock().getBlockID();
									var15m = this.getStoneBlock().getMetadata();
								}
								else if (var16 >= var5 - -16 && var16 <= var5 + 1)
								{
									var14 = this.getGrassBlock().getBlockID();
									var14m = this.getGrassBlock().getMetadata();
									var14 = this.getDirtBlock().getBlockID();
									var14m = this.getDirtBlock().getMetadata();
								}

								if (var16 < var5 && var14 == 0)
								{
									var14 = 0;
								}

								var13 = var12;

								if (var16 >= var5 - 1)
								{
									arrayOfIDs[index] = var14;
									arrayOfMeta[index] = var14m;
								}
								else
								{
									arrayOfIDs[index] = var15;
									arrayOfMeta[index] = var15m;
								}
							}
							else if (var13 > 0)
							{
								--var13;
								arrayOfIDs[index] = var15;
								arrayOfMeta[index] = var15m;
							}
						}
					}
				}
			}
		}
	}

	@Override
	public Chunk provideChunk(int par1, int par2)
	{
		this.rand.setSeed(par1 * 341873128712L + par2 * 132897987541L);
		final short[] ids = new short[32768 * 2];
		final byte[] meta = new byte[32768 * 2];
		this.generateTerrain(par1, par2, ids, meta);
		this.createCraters(par1, par2, ids, meta);
		this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, par1 * 16, par2 * 16, 16, 16);
		this.replaceBlocksForBiome(par1, par2, ids, meta, this.biomesForGeneration);

		if (this.worldGenerators == null)
		{
			this.worldGenerators = this.getWorldGenerators();
		}

		for (GCCoreMapGenBaseMeta generator : this.worldGenerators)
		{
			generator.generate(this, this.worldObj, par1, par2, ids, meta);
		}

		final Chunk var4 = new Chunk(this.worldObj, ids, meta, par1, par2);
		final byte[] var5 = var4.getBiomeArray();

		for (int var6 = 0; var6 < var5.length; ++var6)
		{
			var5[var6] = (byte) this.biomesForGeneration[var6].biomeID;
		}

		var4.generateSkylightMap();
		return var4;
	}

	public void createCraters(int chunkX, int chunkZ, short[] chunkArray, byte[] metaArray)
	{
		this.noiseGen5.frequency = 0.015;
		for (int cx = chunkX - 2; cx <= chunkX + 2; cx++)
		{
			for (int cz = chunkZ - 2; cz <= chunkZ + 2; cz++)
			{
				for (int x = 0; x < GCChunkProvider.CHUNK_SIZE_X; x++)
				{
					for (int z = 0; z < GCChunkProvider.CHUNK_SIZE_Z; z++)
					{
						if (Math.abs(this.randFromPoint(cx * 16 + x, (cz * 16 + z) * 1000)) < this.noiseGen5.getNoise(cx * 16 + x, cz * 16 + z) / this.CRATER_PROB)
						{
							final Random random = new Random(cx * 16 + x + (cz * 16 + z) * 5000);
							final GCCoreCraterSize cSize = GCCoreCraterSize.sizeArray[random.nextInt(GCCoreCraterSize.sizeArray.length)];
							final int size = random.nextInt(cSize.MAX_SIZE - cSize.MIN_SIZE) + cSize.MIN_SIZE + 15;
							this.makeCrater(cx * 16 + x, cz * 16 + z, chunkX * 16, chunkZ * 16, size, chunkArray, metaArray);
						}
					}
				}
			}
		}
	}

	public void makeCrater(int craterX, int craterZ, int chunkX, int chunkZ, int size, short[] chunkArray, byte[] metaArray)
	{
		for (int x = 0; x < GCChunkProvider.CHUNK_SIZE_X; x++)
		{
			for (int z = 0; z < GCChunkProvider.CHUNK_SIZE_Z; z++)
			{
				double xDev = craterX - (chunkX + x);
				double zDev = craterZ - (chunkZ + z);
				if (xDev * xDev + zDev * zDev < size * size)
				{
					xDev /= size;
					zDev /= size;
					final double sqrtY = xDev * xDev + zDev * zDev;
					double yDev = sqrtY * sqrtY * 6;
					yDev = 5 - yDev;
					int helper = 0;
					for (int y = 127; y > 0; y--)
					{
						if (chunkArray[this.getIndex(x, y, z)] != 0 && helper <= yDev)
						{
							chunkArray[this.getIndex(x, y, z)] = 0;
							metaArray[this.getIndex(x, y, z)] = 0;
							helper++;
						}
						if (helper > yDev)
						{
							break;
						}
					}
				}
			}
		}
	}

	private int getIndex(int x, int y, int z)
	{
		return y << 8 | z << 4 | x;
	}

	private double randFromPoint(int x, int z)
	{
		int n;
		n = x + z * 57;
		n = n << 13 ^ n;
		return 1.0 - (n * (n * n * 15731 + 789221) + 1376312589 & 0x7fffffff) / 1073741824.0;
	}

	@Override
	public boolean chunkExists(int par1, int par2)
	{
		return true;
	}

	public void decoratePlanet(World par1World, Random par2Random, int par3, int par4)
	{
		this.biomeDecoratorPlanet.decorate(par1World, par2Random, par3, par4);
	}

	@Override
	public void populate(IChunkProvider par1IChunkProvider, int par2, int par3)
	{
		BlockSand.fallInstantly = true;
		int var4 = par2 * 16;
		int var5 = par3 * 16;
		this.worldObj.getBiomeGenForCoords(var4 + 16, var5 + 16);
		this.rand.setSeed(this.worldObj.getSeed());
		final long var7 = this.rand.nextLong() / 2L * 2L + 1L;
		final long var9 = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed(par2 * var7 + par3 * var9 ^ this.worldObj.getSeed());
		this.decoratePlanet(this.worldObj, this.rand, var4, var5);
		var4 += 8;
		var5 += 8;

		BlockSand.fallInstantly = false;
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

			for (SpawnListEntry monster : this.getMonsters())
			{
				monsters.add(monster);
			}

			return monsters;
		}
		else if (par1EnumCreatureType == EnumCreatureType.creature)
		{
			final List creatures = new ArrayList();

			for (SpawnListEntry creature : this.getCreatures())
			{
				creatures.add(creature);
			}

			return creatures;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Do not return null
	 * 
	 * @return The biome generator for this world, handles ore, flower, etc
	 *         generation. See GCBiomeDecoratorBase.
	 */
	protected abstract GCBiomeDecoratorBase getBiomeGenerator();

	/**
	 * Do not return null, have at least one biome for generation
	 * 
	 * @return Biome instance for generation
	 */
	protected abstract BiomeGenBase[] getBiomesForGeneration();

	/**
	 * @return The average terrain level. Default is 64.
	 */
	protected abstract int getSeaLevel();

	/**
	 * List of all world generators to use. Caves, ravines, structures, etc.
	 * 
	 * Return an empty list for no world generators. Do not return null.
	 * 
	 * @return
	 */
	protected abstract List<GCCoreMapGenBaseMeta> getWorldGenerators();

	/**
	 * @return List of spawn list entries for monsters
	 */
	protected abstract SpawnListEntry[] getMonsters();

	/**
	 * @return List of spawn list entries for creatures
	 */
	protected abstract SpawnListEntry[] getCreatures();

	/**
	 * The grass block to be generated. Doesn't have to be grass of course.
	 * 
	 * @return BlockMetaPair instance containing ID and metadata for grass
	 *         block.
	 */
	protected abstract BlockMetaPair getGrassBlock();

	/**
	 * The dirt block to be generated. Doesn't have to be dirt of course.
	 * 
	 * @return BlockMetaPair instance containing ID and metadata for dirt block.
	 */
	protected abstract BlockMetaPair getDirtBlock();

	/**
	 * The stone block to be generated. Doesn't have to be stone of course.
	 * 
	 * @return BlockMetaPair instance containing ID and metadata for stone
	 *         block.
	 */
	protected abstract BlockMetaPair getStoneBlock();

	/**
	 * @return Base height modifier
	 */
	public abstract double getHeightModifier();

	/**
	 * @return Height modifier for small hills
	 */
	public abstract double getSmallFeatureHeightModifier();

	/**
	 * @return Height modifier for mountains
	 */
	public abstract double getMountainHeightModifier();

	/**
	 * @return Height modifier for valleys
	 */
	public abstract double getValleyHeightModifier();

	/**
	 * @return Probability that craters will be generated
	 */
	public abstract int getCraterProbability();
}
