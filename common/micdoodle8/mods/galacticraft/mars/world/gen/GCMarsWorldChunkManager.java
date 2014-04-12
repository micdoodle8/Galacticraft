package micdoodle8.mods.galacticraft.mars.world.gen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.world.ChunkPosition;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.IntCache;

/**
 * GCMarsWorldChunkManager.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsWorldChunkManager extends WorldChunkManager
{
	private final BiomeCache biomeCache;
	private final List<BiomeGenBase> biomesToSpawnIn;

	public GCMarsWorldChunkManager()
	{
		this.biomeCache = new BiomeCache(this);
		this.biomesToSpawnIn = new ArrayList<BiomeGenBase>();
		this.biomesToSpawnIn.add(GCMarsBiomeGenBase.marsFlat);
	}

	@Override
	public List<BiomeGenBase> getBiomesToSpawnIn()
	{
		return this.biomesToSpawnIn;
	}

	@Override
	public BiomeGenBase getBiomeGenAt(int par1, int par2)
	{
		return GCMarsBiomeGenBase.marsFlat;
	}

	@Override
	public float[] getRainfall(float[] par1ArrayOfFloat, int par2, int par3, int par4, int par5)
	{
		if (par1ArrayOfFloat == null || par1ArrayOfFloat.length < par4 * par5)
		{
			par1ArrayOfFloat = new float[par4 * par5];
		}

		Arrays.fill(par1ArrayOfFloat, 0, par4 * par5, 0.0F);
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

		for (int var7 = 0; var7 < par4 * par5; ++var7)
		{
			float var8 = GCMarsBiomeGenBase.marsFlat.getIntTemperature() / 65536.0F;

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

		for (int var7 = 0; var7 < par4 * par5; ++var7)
		{
			par1ArrayOfBiomeGenBase[var7] = GCMarsBiomeGenBase.marsFlat;
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
			for (int var8 = 0; var8 < par4 * par5; ++var8)
			{
				par1ArrayOfBiomeGenBase[var8] = GCMarsBiomeGenBase.marsFlat;
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

		for (int var12 = 0; var12 < var9 * var10; ++var12)
		{
			final BiomeGenBase var13 = GCMarsBiomeGenBase.marsFlat;

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
		final int var10 = var8 - var6 + 1;

		final int var16 = var6 + 0 % var10 << 2;
		final int var17 = var7 + 0 / var10 << 2;

		return new ChunkPosition(var16, 0, var17);
	}

	@Override
	public void cleanupCache()
	{
		this.biomeCache.cleanupCache();
	}
}
