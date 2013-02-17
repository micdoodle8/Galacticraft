package micdoodle8.mods.galacticraft.moon.wgen;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.wgen.GCCoreWorldGenMinableMeta;
import micdoodle8.mods.galacticraft.moon.blocks.GCMoonBlocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMoonBiomeDecorator
{
	protected World currentWorld;

	protected Random randomGenerator;

	protected int chunk_X;

	protected int chunk_Z;

	protected BiomeGenBase biome;

	protected WorldGenerator dirtGen;

	protected WorldGenerator cheeseGen;
	
	protected WorldGenerator aluminumGen;

	protected WorldGenerator ironGen;

	public GCMoonBiomeDecorator(BiomeGenBase par1BiomeGenBase)
	{
		this.aluminumGen = new GCCoreWorldGenMinableMeta(GCMoonBlocks.blockMoonOres.blockID, 7, 0, true, GCMoonBlocks.moonStone.blockID);
		this.ironGen = new GCCoreWorldGenMinableMeta(GCMoonBlocks.blockMoonOres.blockID, 8, 1, true, GCMoonBlocks.moonStone.blockID);
		this.cheeseGen = new GCCoreWorldGenMinableMeta(GCMoonBlocks.blockMoonOres.blockID, 10, 2, true, GCMoonBlocks.moonStone.blockID);
		this.dirtGen = new GCCoreWorldGenMinableMeta(GCMoonBlocks.moonDirt.blockID, 32, 0, false, GCMoonBlocks.moonStone.blockID);
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
		this.genStandardOre1(20, this.dirtGen, 0, 200);
        this.genStandardOre1(3, this.aluminumGen, 0, 14);
        this.genStandardOre1(8, this.ironGen, 0, 34);
        this.genStandardOre1(12, this.cheeseGen, 0, 128);
	}
}
