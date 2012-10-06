package micdoodle8.mods.galacticraft;

import java.util.Random;

import net.minecraft.src.BiomeDecorator;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCBiomeDecorator 
{
	protected World currentWorld;

	protected Random randomGenerator;

	protected int chunk_X;

	protected int chunk_Z;

	protected BiomeGenBase biome;

	protected WorldGenerator dirtGen;

	protected WorldGenerator deshGen;

	protected WorldGenerator electrumGen;

	protected WorldGenerator greenstoneGen;

	protected WorldGenerator quandriumGen;

	protected WorldGenerator rhodiumGen;

	public GCBiomeDecorator(BiomeGenBase par1BiomeGenBase) 
	{
//		this.deshGen = new GCWorldGenMinable(GCBlocks.marsOreDesh.blockID, 10, 0, false); TODO
//		this.electrumGen = new GCWorldGenMinable(GCBlocks.marsOreElectrum.blockID, 4, 0, false);
//		this.quandriumGen = new GCWorldGenMinable(GCBlocks.marsOreQuandrium.blockID, 3, 0, false);
		this.dirtGen = new GCWorldGenMinable(GCBlocks.marsDirt.blockID, 32, 0, false);
		this.biome = par1BiomeGenBase;
	}

	public void decorate(World par1World, Random par2Random, int par3, int par4) 
	{
		if (this.currentWorld != null) 
		{
			throw new RuntimeException("Already decorating!!");
		} 
		else 
		{
			this.currentWorld = par1World;
			this.randomGenerator = par2Random;
			this.chunk_X = par3;
			this.chunk_Z = par4;
			this.generateOres();
			this.currentWorld = null;
			this.randomGenerator = null;
		}
	}

	protected void genStandardOre1(int par1, WorldGenerator par2WorldGenerator, int par3, int par4) 
	{
		for (int var5 = 0; var5 < par1; ++var5) 
		{
			int var6 = this.chunk_X + this.randomGenerator.nextInt(16);
			int var7 = this.randomGenerator.nextInt(par4 - par3) + par3;
			int var8 = this.chunk_Z + this.randomGenerator.nextInt(16);
			par2WorldGenerator.generate(this.currentWorld, this.randomGenerator, var6, var7, var8);
		}
	}

	protected void generateOres() {
		this.genStandardOre1(20, this.dirtGen, 0, 192);
		this.genStandardOre1(10, this.deshGen, 0, 192);
		this.genStandardOre1(7, this.greenstoneGen, 0, 32);
		this.genStandardOre1(4, this.rhodiumGen, 0, 16);
		this.genStandardOre1(2, this.electrumGen, 0, 16);
		this.genStandardOre1(1, this.quandriumGen, 0, 10);
	}
}
