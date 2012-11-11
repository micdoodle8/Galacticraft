package micdoodle8.mods.galacticraft.europa;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GCCoreWorldGenMinableMeta;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCEuropaBiomeDecorator 
{
	protected World currentWorld;

	protected Random randomGenerator;

	protected int chunk_X;

	protected int chunk_Z;

	protected BiomeGenBase biome;

	protected WorldGenerator diamondGen;

//	protected WorldGenerator deshGen;
//
//	protected WorldGenerator quandriumGen;
//	
//	protected WorldGenerator aluminumGen;
//	
//	protected WorldGenerator copperGen;
//	
//	protected WorldGenerator titaniumGen;
//	
//	protected WorldGenerator bacteriaGen;

	public GCEuropaBiomeDecorator(BiomeGenBase par1BiomeGenBase) 
	{
//		this.deshGen = new GCCoreWorldGenMinableMeta(GCMarsBlocks.blockOres.blockID, 12, 0, true, GCMarsBlocks.marsStone.blockID);
//		this.copperGen = new GCCoreWorldGenMinableMeta(GCMarsBlocks.blockOres.blockID, 8, 6, true, GCMarsBlocks.marsStone.blockID);
//		this.aluminumGen = new GCCoreWorldGenMinableMeta(GCMarsBlocks.blockOres.blockID, 7, 4, true, GCMarsBlocks.marsStone.blockID);
//		this.titaniumGen = new GCCoreWorldGenMinableMeta(GCMarsBlocks.blockOres.blockID, 7, 8, true, GCMarsBlocks.marsStone.blockID);
//		this.quandriumGen = new GCCoreWorldGenMinableMeta(GCMarsBlocks.blockOres.blockID, 7, 2, true, GCMarsBlocks.marsStone.blockID);
//		this.dirtGen = new GCCoreWorldGenMinableMeta(GCMarsBlocks.marsDirt.blockID, 32, 0, false, GCMarsBlocks.marsStone.blockID);
		this.diamondGen = new GCCoreWorldGenMinableMeta(Block.oreDiamond.blockID, 50, 0, false, Block.stone.blockID);
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

	protected void generateOres() 
	{
		this.genStandardOre1(50, this.diamondGen, 0, 200);
//		this.genStandardOre1(8, this.bacteriaGen, 0, 200);
//        this.genStandardOre1(15, this.deshGen, 0, 128);
//        this.genStandardOre1(10, this.copperGen, 0, 64);
//        this.genStandardOre1(8, this.aluminumGen, 0, 32);
//        this.genStandardOre1(1, this.titaniumGen, 0, 16);
//        this.genStandardOre1(1, this.quandriumGen, 0, 16);
	}
}
