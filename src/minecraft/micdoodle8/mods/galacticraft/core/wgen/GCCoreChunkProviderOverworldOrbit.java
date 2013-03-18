package micdoodle8.mods.galacticraft.core.wgen;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreChunkProviderOverworldOrbit extends ChunkProviderGenerate
{
	private final Random rand;
	
	private final World worldObj;

	public GCCoreChunkProviderOverworldOrbit(World par1World, long par2, boolean par4)
	{
		super(par1World, par2, par4);
		this.rand = new Random(par2);
		this.worldObj = par1World;
	}

	@Override
	public Chunk provideChunk(int par1, int par2)
	{
		this.rand.setSeed(par1 * 341873128712L + par2 * 132897987541L);
		final int[] ids = new int[32768];
		final int[] meta = new int[32768];

		final Chunk var4 = new GCCoreChunk(this.worldObj, ids, meta, par1, par2);

		var4.generateSkylightMap();
		return var4;
	}
	
	@Override
	public boolean chunkExists(int par1, int par2)
	{
		return true;
	}

	@Override
	public void populate(IChunkProvider par1IChunkProvider, int par2, int par3)
	{
//		BlockSand.fallInstantly = true;
//		int var4 = par2 * 16;
//		int var5 = par3 * 16;
//		this.worldObj.getBiomeGenForCoords(var4 + 16, var5 + 16);
//		this.rand.setSeed(this.worldObj.getSeed());
//		final long var7 = this.rand.nextLong() / 2L * 2L + 1L;
//		final long var9 = this.rand.nextLong() / 2L * 2L + 1L;
//		this.rand.setSeed(par2 * var7 + par3 * var9 ^ this.worldObj.getSeed());
//		this.villageGenerator.generateStructuresInChunk(this.worldObj, this.rand, par2, par3);
//
////        this.mapGenPuzzle.generateStructuresInChunk(this.worldObj, this.rand, par2, par3);
//
//		this.decoratePlanet(this.worldObj, this.rand, var4, var5);
//		var4 += 8;
//		var5 += 8;
//
//		BlockSand.fallInstantly = false;
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
		return "OrbitLevelSource";
	}

	@Override
	public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType,	int i, int j, int k)
	{
		return null;
	}
}
