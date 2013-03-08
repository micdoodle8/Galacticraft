package micdoodle8.mods.galacticraft.io.wgen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCIoBiomeDecorator
{
	protected World currentWorld;

	protected Random randomGenerator;

	protected int chunk_X;

	protected int chunk_Z;

	protected BiomeGenBase biome;

//    protected WorldGenerator gravelGen;
//    protected WorldGenerator coalGen;
//    protected WorldGenerator ironGen;
//    protected WorldGenerator goldGen;
//    protected WorldGenerator redstoneGen;
//    protected WorldGenerator diamondGen;
//    protected WorldGenerator lapisGen;

	public GCIoBiomeDecorator(BiomeGenBase par1BiomeGenBase)
	{
//        this.coalGen = new WorldGenMinable(Block.oreCoal.blockID, 16);
//        this.ironGen = new WorldGenMinable(Block.oreIron.blockID, 8);
//        this.goldGen = new WorldGenMinable(Block.oreGold.blockID, 8);
//        this.redstoneGen = new WorldGenMinable(Block.oreRedstone.blockID, 7);
//        this.diamondGen = new WorldGenMinable(Block.oreDiamond.blockID, 7);
//        this.lapisGen = new WorldGenMinable(Block.oreLapis.blockID, 6);
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
			final int var6 = this.chunk_X + this.randomGenerator.nextInt(16);
			final int var7 = this.randomGenerator.nextInt(par4 - par3) + par3;
			final int var8 = this.chunk_Z + this.randomGenerator.nextInt(16);
			par2WorldGenerator.generate(this.currentWorld, this.randomGenerator, var6, var7, var8);
		}
	}

	protected void generateOres()
	{
//		this.genStandardOre1(50, this.diamondGen, 0, 200);
//		this.genStandardOre1(8, this.bacteriaGen, 0, 200);
//        this.genStandardOre1(15, this.deshGen, 0, 128);
//        this.genStandardOre1(10, this.copperGen, 0, 64);
//        this.genStandardOre1(8, this.aluminumGen, 0, 32);
//        this.genStandardOre1(1, this.titaniumGen, 0, 16);
//        this.genStandardOre1(1, this.quandriumGen, 0, 16);
	}
}
