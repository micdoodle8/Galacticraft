package micdoodle8.mods.galacticraft.api.prefab.world.gen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

/**
 * Do not include this prefab class in your released mod download.
 */
public abstract class GCWorldChunkManager extends WorldChunkManager
{
	private GenLayer genBiomes;
	private GenLayer biomeIndexLayer;
	private final BiomeCache biomeCache;
	private final List<BiomeGenBase> biomesToSpawnIn;

	protected GCWorldChunkManager()
	{
		this.biomeCache = new BiomeCache(this);
		this.biomesToSpawnIn = new ArrayList<BiomeGenBase>();
		this.biomesToSpawnIn.addAll(this.getBiomeList());
	}

	public GCWorldChunkManager(long par1, WorldType par3WorldType)
	{
		this();
		final GenLayer[] var4 = GenLayer.initializeAllBiomeGenerators(par1, par3WorldType);
		this.genBiomes = var4[0];
		this.biomeIndexLayer = var4[1];
	}

	public GCWorldChunkManager(World par1World, float par2)
	{
		this(par1World.getSeed(), par1World.getWorldInfo().getTerrainType());
	}

	public abstract BiomeGenBase getPlanetBiomeGenAt(int par1, int par2);

	public abstract List<BiomeGenBase> getBiomeList();

	@Override
	public List<BiomeGenBase> getBiomesToSpawnIn()
	{
		return this.biomesToSpawnIn;
	}

	@Override
	public BiomeGenBase getBiomeGenAt(int par1, int par2)
	{
		return this.getPlanetBiomeGenAt(par1, par2);
	}

	@Override
	public float[] getRainfall(float[] par1ArrayOfFloat, int par2, int par3, int par4, int par5)
	{
		if (par1ArrayOfFloat == null || par1ArrayOfFloat.length < par4 * par5)
		{
			par1ArrayOfFloat = new float[par4 * par5];
		}

		Arrays.fill(par1ArrayOfFloat, 0, par4 * par5, 0);
		return par1ArrayOfFloat;
	}

	@Override
	public float getTemperatureAtHeight(float par1, int par2)
	{
		return par1;
	}

	@Override
	public float[] getTemperatures(float[] par1ArrayOfFloat, int par2, int par3, int par4, int par5)
	{
		IntCache.resetIntCache();

		if (par1ArrayOfFloat == null || par1ArrayOfFloat.length < par4 * par5)
		{
			par1ArrayOfFloat = new float[par4 * par5];
		}

		final int[] var6 = this.biomeIndexLayer.getInts(par2, par3, par4, par5);

		for (int var7 = 0; var7 < par4 * par5; ++var7)
		{
			float var8 = BiomeGenBase.biomeList[var6[var7]].getIntTemperature() / 65536.0F;

			if (var8 > 1.0F)
			{
				var8 = 1.0F;
			}

			par1ArrayOfFloat[var7] = var8;
		}

		return par1ArrayOfFloat;
	}

	@Override
	public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5)
	{
		IntCache.resetIntCache();

		if (par1ArrayOfBiomeGenBase == null || par1ArrayOfBiomeGenBase.length < par4 * par5)
		{
			par1ArrayOfBiomeGenBase = new BiomeGenBase[par4 * par5];
		}

		final int[] var6 = this.genBiomes.getInts(par2, par3, par4, par5);

		for (int var7 = 0; var7 < par4 * par5; ++var7)
		{
			par1ArrayOfBiomeGenBase[var7] = BiomeGenBase.biomeList[var6[var7]];
		}

		return par1ArrayOfBiomeGenBase;
	}

	@Override
	public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5)
	{
		return this.getBiomeGenAt(par1ArrayOfBiomeGenBase, par2, par3, par4, par5, true);
	}

	@Override
	public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5, boolean par6)
	{
		IntCache.resetIntCache();

		if (par1ArrayOfBiomeGenBase == null || par1ArrayOfBiomeGenBase.length < par4 * par5)
		{
			par1ArrayOfBiomeGenBase = new BiomeGenBase[par4 * par5];
		}

		if (par6 && par4 == 16 && par5 == 16 && (par2 & 15) == 0 && (par3 & 15) == 0)
		{
			final BiomeGenBase[] var9 = this.biomeCache.getCachedBiomes(par2, par3);
			System.arraycopy(var9, 0, par1ArrayOfBiomeGenBase, 0, par4 * par5);
			return par1ArrayOfBiomeGenBase;
		}
		else
		{
			final int[] var7 = this.biomeIndexLayer.getInts(par2, par3, par4, par5);

			for (int var8 = 0; var8 < par4 * par5; ++var8)
			{
				par1ArrayOfBiomeGenBase[var8] = BiomeGenBase.biomeList[var7[var8]];
			}

			return par1ArrayOfBiomeGenBase;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean areBiomesViable(int par1, int par2, int par3, List par4List)
	{
		final int var5 = par1 - par3 >> 2;
		final int var6 = par2 - par3 >> 2;
		final int var7 = par1 + par3 >> 2;
		final int var8 = par2 + par3 >> 2;
		final int var9 = var7 - var5 + 1;
		final int var10 = var8 - var6 + 1;
		final int[] var11 = this.genBiomes.getInts(var5, var6, var9, var10);

		for (int var12 = 0; var12 < var9 * var10; ++var12)
		{
			final BiomeGenBase var13 = BiomeGenBase.biomeList[var11[var12]];

			if (!par4List.contains(var13))
			{
				return false;
			}
		}

		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ChunkPosition findBiomePosition(int par1, int par2, int par3, List par4List, Random par5Random)
	{
		final int var6 = par1 - par3 >> 2;
		final int var7 = par2 - par3 >> 2;
		final int var8 = par1 + par3 >> 2;
		final int var9 = par2 + par3 >> 2;
		final int var10 = var8 - var6 + 1;
		final int var11 = var9 - var7 + 1;
		final int[] var12 = this.genBiomes.getInts(var6, var7, var10, var11);
		ChunkPosition var13 = null;
		int var14 = 0;

		for (int var15 = 0; var15 < var12.length; ++var15)
		{
			final int var16 = var6 + var15 % var10 << 2;
			final int var17 = var7 + var15 / var10 << 2;
			final BiomeGenBase var18 = BiomeGenBase.biomeList[var12[var15]];

			if (par4List.contains(var18) && (var13 == null || par5Random.nextInt(var14 + 1) == 0))
			{
				var13 = new ChunkPosition(var16, 0, var17);
				++var14;
			}
		}

		return var13;
	}

	@Override
	public void cleanupCache()
	{
		this.biomeCache.cleanupCache();
	}
}
