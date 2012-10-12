package micdoodle8.mods.galacticraft.API;

import java.util.Random;

import micdoodle8.mods.galacticraft.mars.GCMarsBlocks;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenMinable;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class WorldGenMinableMeta extends WorldGenMinable 
{
	private int minableBlockId;

	private int numberOfBlocks;

	private int metadata;

	private boolean usingMetadata = false;

	public WorldGenMinableMeta(int par1, int par2, int par3, boolean par4)
	{
		super(par1, par2);
		this.minableBlockId = par1;
		this.numberOfBlocks = par2;
		this.metadata = par3;
		this.usingMetadata = par4;
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) 
	{
		float var6 = par2Random.nextFloat() * (float) Math.PI;
		double var7 = par3 + 8 + MathHelper.sin(var6) * this.numberOfBlocks / 8.0F;
		double var9 = par3 + 8 - MathHelper.sin(var6) * this.numberOfBlocks	/ 8.0F;
		double var11 = par5 + 8 + MathHelper.cos(var6) * this.numberOfBlocks / 8.0F;
		double var13 = par5 + 8 - MathHelper.cos(var6) * this.numberOfBlocks / 8.0F;
		double var15 = par4 + par2Random.nextInt(3) - 2;
		double var17 = par4 + par2Random.nextInt(3) - 2;

		for (int var19 = 0; var19 <= this.numberOfBlocks; ++var19) 
		{
			double var20 = var7 + (var9 - var7) * var19 / this.numberOfBlocks;
			double var22 = var15 + (var17 - var15) * var19 / this.numberOfBlocks;
			double var24 = var11 + (var13 - var11) * var19 / this.numberOfBlocks;
			double var26 = par2Random.nextDouble() * this.numberOfBlocks / 16.0D;
			double var28 = (MathHelper.sin(var19 * (float) Math.PI / this.numberOfBlocks) + 1.0F) * var26 + 1.0D;
			double var30 = (MathHelper.sin(var19 * (float) Math.PI / this.numberOfBlocks) + 1.0F) * var26 + 1.0D;
			int var32 = MathHelper.floor_double(var20 - var28 / 2.0D);
			int var33 = MathHelper.floor_double(var22 - var30 / 2.0D);
			int var34 = MathHelper.floor_double(var24 - var28 / 2.0D);
			int var35 = MathHelper.floor_double(var20 + var28 / 2.0D);
			int var36 = MathHelper.floor_double(var22 + var30 / 2.0D);
			int var37 = MathHelper.floor_double(var24 + var28 / 2.0D);

			for (int var38 = var32; var38 <= var35; ++var38)
			{
				double var39 = (var38 + 0.5D - var20) / (var28 / 2.0D);

				if (var39 * var39 < 1.0D) 
				{
					for (int var41 = var33; var41 <= var36; ++var41)
					{
						double var42 = (var41 + 0.5D - var22) / (var30 / 2.0D);

						if (var39 * var39 + var42 * var42 < 1.0D) 
						{
							for (int var44 = var34; var44 <= var37; ++var44) 
							{
								double var45 = (var44 + 0.5D - var24) / (var28 / 2.0D);

								if (var39 * var39 + var42 * var42 + var45 * var45 < 1.0D && par1World.getBlockId(var38, var41, var44) == GCMarsBlocks.marsStone.blockID && usingMetadata == false) 
								{
									par1World.setBlock(var38, var41, var44, this.minableBlockId);
								} 
								else if (var39 * var39 + var42 * var42 + var45 * var45 < 1.0D && par1World.getBlockId(var38, var41, var44) == GCMarsBlocks.marsStone.blockID && usingMetadata == true) 
								{
									par1World.setBlockAndMetadata(var38, var41,	var44, this.minableBlockId, this.metadata);
								}
							}
						}
					}
				}
			}
		}

		return true;
	}
}
