package micdoodle8.mods.galacticraft.mars.world.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityCreeper;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeleton;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpider;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityZombie;
import micdoodle8.mods.galacticraft.core.perlin.generator.Gradient;
import micdoodle8.mods.galacticraft.core.world.gen.GCCoreCraterSize;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.GCCoreMapGenDungeon;
import micdoodle8.mods.galacticraft.mars.GCMarsConfigManager;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
import micdoodle8.mods.galacticraft.mars.world.gen.dungeon.GCMarsRoomBoss;
import micdoodle8.mods.galacticraft.mars.world.gen.dungeon.GCMarsRoomChests;
import micdoodle8.mods.galacticraft.mars.world.gen.dungeon.GCMarsRoomEmpty;
import micdoodle8.mods.galacticraft.mars.world.gen.dungeon.GCMarsRoomSpawner;
import micdoodle8.mods.galacticraft.mars.world.gen.dungeon.GCMarsRoomTreasure;
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
import net.minecraftforge.common.ForgeDirection;

/**
 * GCMarsChunkProvider.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsChunkProvider extends ChunkProviderGenerate
{
	final short topBlockID = (short) GCMarsBlocks.marsBlock.blockID;
	final byte topBlockMeta = 5;
	final short fillBlockID = (short) GCMarsBlocks.marsBlock.blockID;
	final byte fillBlockMeta = 6;
	final short lowerBlockID = (short) GCMarsBlocks.marsBlock.blockID;
	final byte lowerBlockMeta = 9;

	private final Random rand;

	private final Gradient noiseGen1;
	private final Gradient noiseGen2;
	private final Gradient noiseGen3;
	private final Gradient noiseGen4;
	private final Gradient noiseGen5;
	private final Gradient noiseGen6;
	private final Gradient noiseGen7;
	public GCMarsBiomeDecorator biomedecoratorplanet = new GCMarsBiomeDecorator();

	private final World worldObj;

	private final GCMarsCavern caveGenerator = new GCMarsCavern();
	private final GCMarsCaveGen caveGenerator2 = new GCMarsCaveGen();

	private final GCCoreMapGenDungeon dungeonGenerator = new GCCoreMapGenDungeon(GCMarsBlocks.marsBlock.blockID, 7, 8, 16, 6);

	{
		this.dungeonGenerator.otherRooms.add(new GCMarsRoomEmpty(null, 0, 0, 0, ForgeDirection.UNKNOWN));
		this.dungeonGenerator.otherRooms.add(new GCMarsRoomSpawner(null, 0, 0, 0, ForgeDirection.UNKNOWN));
		this.dungeonGenerator.otherRooms.add(new GCMarsRoomSpawner(null, 0, 0, 0, ForgeDirection.UNKNOWN));
		this.dungeonGenerator.otherRooms.add(new GCMarsRoomSpawner(null, 0, 0, 0, ForgeDirection.UNKNOWN));
		this.dungeonGenerator.otherRooms.add(new GCMarsRoomSpawner(null, 0, 0, 0, ForgeDirection.UNKNOWN));
		this.dungeonGenerator.otherRooms.add(new GCMarsRoomSpawner(null, 0, 0, 0, ForgeDirection.UNKNOWN));
		this.dungeonGenerator.otherRooms.add(new GCMarsRoomSpawner(null, 0, 0, 0, ForgeDirection.UNKNOWN));
		this.dungeonGenerator.otherRooms.add(new GCMarsRoomSpawner(null, 0, 0, 0, ForgeDirection.UNKNOWN));
		this.dungeonGenerator.otherRooms.add(new GCMarsRoomSpawner(null, 0, 0, 0, ForgeDirection.UNKNOWN));
		this.dungeonGenerator.otherRooms.add(new GCMarsRoomChests(null, 0, 0, 0, ForgeDirection.UNKNOWN));
		this.dungeonGenerator.otherRooms.add(new GCMarsRoomChests(null, 0, 0, 0, ForgeDirection.UNKNOWN));
		this.dungeonGenerator.bossRooms.add(new GCMarsRoomBoss(null, 0, 0, 0, ForgeDirection.UNKNOWN));
		this.dungeonGenerator.treasureRooms.add(new GCMarsRoomTreasure(null, 0, 0, 0, ForgeDirection.UNKNOWN));
	}

	private BiomeGenBase[] biomesForGeneration = { GCMarsBiomeGenBase.marsFlat };

	double[] noise1;
	double[] noise2;
	double[] noise3;
	double[] noise4;
	double[] noise5;
	double[] noise6;
	float[] field_35388_l;
	int[][] field_914_i = new int[32][32];

	private static final double TERRAIN_HEIGHT_MOD = 12;
	private static final double SMALL_FEATURE_HEIGHT_MOD = 26;
	private static final double MOUNTAIN_HEIGHT_MOD = 95;
	private static final double VALLEY_HEIGHT_MOD = 50;
	private static final int CRATER_PROB = 2000;

	// DO NOT CHANGE
	private static final int MID_HEIGHT = 93;
	private static final int CHUNK_SIZE_X = 16;
	private static final int CHUNK_SIZE_Y = 256;
	private static final int CHUNK_SIZE_Z = 16;
	private static final double MAIN_FEATURE_FILTER_MOD = 4;
	private static final double LARGE_FEATURE_FILTER_MOD = 8;
	private static final double SMALL_FEATURE_FILTER_MOD = 8;

	public GCMarsChunkProvider(World par1World, long par2, boolean par4)
	{
		super(par1World, par2, par4);
		this.worldObj = par1World;
		this.rand = new Random(par2);

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

		for (int x = 0; x < GCMarsChunkProvider.CHUNK_SIZE_X; x++)
		{
			for (int z = 0; z < GCMarsChunkProvider.CHUNK_SIZE_Z; z++)
			{
				final double baseHeight = this.noiseGen1.getNoise(chunkX * 16 + x, chunkZ * 16 + z) * GCMarsChunkProvider.TERRAIN_HEIGHT_MOD;
				final double smallHillHeight = this.noiseGen2.getNoise(chunkX * 16 + x, chunkZ * 16 + z) * GCMarsChunkProvider.SMALL_FEATURE_HEIGHT_MOD;
				double mountainHeight = Math.abs(this.noiseGen3.getNoise(chunkX * 16 + x, chunkZ * 16 + z));
				double valleyHeight = Math.abs(this.noiseGen4.getNoise(chunkX * 16 + x, chunkZ * 16 + z));
				final double featureFilter = this.noiseGen5.getNoise(chunkX * 16 + x, chunkZ * 16 + z) * GCMarsChunkProvider.MAIN_FEATURE_FILTER_MOD;
				final double largeFilter = this.noiseGen6.getNoise(chunkX * 16 + x, chunkZ * 16 + z) * GCMarsChunkProvider.LARGE_FEATURE_FILTER_MOD;
				final double smallFilter = this.noiseGen7.getNoise(chunkX * 16 + x, chunkZ * 16 + z) * GCMarsChunkProvider.SMALL_FEATURE_FILTER_MOD - 0.5;
				/*
				 * if(largeFilter < 0.0) { featureDev = valleyHeight; } else
				 * if(largeFilter > 1.0) { featureDev = mountainHeight; } else {
				 * featureDev = valleyHeight + (mountainHeight - valleyHeight) *
				 * (largeFilter); }
				 * 
				 * if(smallFilter < 0.0) { featureDev = smallHillHeight; } else
				 * if(smallFilter < 1.0) { featureDev = smallHillHeight +
				 * (featureDev - smallHillHeight) * (smallFilter); }
				 */

				/*
				 * if(featureFilter < 0.0) { yDev = baseHeight; } else
				 * if(featureFilter > 1.0) { yDev = featureDev; } else { yDev =
				 * baseHeight + (featureDev - baseHeight) * (featureFilter); }
				 */
				mountainHeight = this.lerp(smallHillHeight, mountainHeight * GCMarsChunkProvider.MOUNTAIN_HEIGHT_MOD, this.fade(this.clamp(mountainHeight * 2, 0, 1)));
				valleyHeight = this.lerp(smallHillHeight, valleyHeight * GCMarsChunkProvider.VALLEY_HEIGHT_MOD - GCMarsChunkProvider.VALLEY_HEIGHT_MOD + 9, this.fade(this.clamp((valleyHeight + 2) * 4, 0, 1)));

				double yDev = this.lerp(valleyHeight, mountainHeight, this.fade(largeFilter));
				yDev = this.lerp(smallHillHeight, yDev, smallFilter);
				yDev = this.lerp(baseHeight, yDev, featureFilter);

				for (int y = 0; y < GCMarsChunkProvider.CHUNK_SIZE_Y; y++)
				{
					if (y < GCMarsChunkProvider.MID_HEIGHT + yDev)
					{
						idArray[this.getIndex(x, y, z)] = this.lowerBlockID;
						metaArray[this.getIndex(x, y, z)] = this.lowerBlockMeta;
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
				short var14 = this.topBlockID;
				byte var14m = this.topBlockMeta;
				short var15 = this.fillBlockID;
				byte var15m = this.fillBlockMeta;

				for (int var16 = GCMarsChunkProvider.CHUNK_SIZE_Y - 1; var16 >= 0; --var16)
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
						else if (var18 == this.lowerBlockID)
						{
							arrayOfMeta[index] = this.lowerBlockMeta;

							if (var13 == -1)
							{
								if (var12 <= 0)
								{
									var14 = 0;
									var14m = 0;
									var15 = this.lowerBlockID;
									var15m = this.lowerBlockMeta;
								}
								else if (var16 >= var5 - -16 && var16 <= var5 + 1)
								{
									var14 = this.topBlockID;
									var14m = this.topBlockMeta;
									var14 = this.fillBlockID;
									var14m = this.fillBlockMeta;
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
		this.caveGenerator.generate(this, this.worldObj, par1, par2, ids, meta);
		this.caveGenerator2.generate(this, this.worldObj, par1, par2, ids, meta);
		this.dungeonGenerator.generateUsingArrays(this.worldObj, this.worldObj.getSeed(), par1 * 16, 30, par2 * 16, par1, par2, ids, meta);

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
				for (int x = 0; x < GCMarsChunkProvider.CHUNK_SIZE_X; x++)
				{
					for (int z = 0; z < GCMarsChunkProvider.CHUNK_SIZE_Z; z++)
					{
						if (Math.abs(this.randFromPoint(cx * 16 + x, (cz * 16 + z) * 1000)) < this.noiseGen5.getNoise(cx * 16 + x, cz * 16 + z) / GCMarsChunkProvider.CRATER_PROB)
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
		for (int x = 0; x < GCMarsChunkProvider.CHUNK_SIZE_X; x++)
		{
			for (int z = 0; z < GCMarsChunkProvider.CHUNK_SIZE_Z; z++)
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
		this.biomedecoratorplanet.decorate(par1World, par2Random, par3, par4);
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
		this.dungeonGenerator.handleTileEntities(this.rand);

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
		return GCMarsConfigManager.generateOtherMods ? "RandomLevelSource" : "MarsLevelSource";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType, int i, int j, int k)
	{
		if (par1EnumCreatureType == EnumCreatureType.monster)
		{
			final List monsters = new ArrayList();
			monsters.add(new SpawnListEntry(GCCoreEntityZombie.class, 8, 2, 3));
			monsters.add(new SpawnListEntry(GCCoreEntitySpider.class, 8, 2, 3));
			monsters.add(new SpawnListEntry(GCCoreEntitySkeleton.class, 8, 2, 3));
			monsters.add(new SpawnListEntry(GCCoreEntityCreeper.class, 8, 2, 3));
			return monsters;
		}
		else
		{
			return null;
		}
	}
}
