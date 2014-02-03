package micdoodle8.mods.galacticraft.core.world.gen;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.block.IPlantableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

/**
 * GCCoreWorldGenForest.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreWorldGenForest extends WorldGenerator
{
	public GCCoreWorldGenForest(boolean par1)
	{
		super(par1);
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
	{
		final int var6 = par2Random.nextInt(3) + 5;
		boolean var7 = true;

		if (par4 >= 1 && par4 + var6 + 1 <= 256)
		{
			int var8;
			int var10;
			int var11;
			int var12;

			for (var8 = par4; var8 <= par4 + 1 + var6; ++var8)
			{
				byte var9 = 1;

				if (var8 == par4)
				{
					var9 = 0;
				}

				if (var8 >= par4 + 1 + var6 - 2)
				{
					var9 = 2;
				}

				for (var10 = par3 - var9; var10 <= par3 + var9 && var7; ++var10)
				{
					for (var11 = par5 - var9; var11 <= par5 + var9 && var7; ++var11)
					{
						if (var8 >= 0 && var8 < 256)
						{
							var12 = par1World.getBlockId(var10, var8, var11);

							final Block block = Block.blocksList[var12];

							if (var12 != 0 && block != null && !block.isLeaves(par1World, var10, var8, var11))
							{
								var7 = false;
							}
						}
						else
						{
							var7 = false;
						}
					}
				}
			}

			if (!var7)
			{
				return false;
			}
			else
			{
				var8 = par1World.getBlockId(par3, par4 - 1, par5);
				final int var10a = par1World.getBlockMetadata(par3, par4 - 1, par5);

				int waterBlocksNearby = 0;

				for (int i = -4; i < 5; i++)
				{
					for (int j = -4; j < 5; j++)
					{
						if (par1World.getBlockId(par3 + i, par4 - 1, par5 + j) == Block.waterMoving.blockID || par1World.getBlockId(par3 + i, par4 - 1, par5 + j) == Block.waterStill.blockID)
						{
							waterBlocksNearby++;
						}
					}
				}

				if (Block.blocksList[var8] != null)
				{
					final boolean flag = Block.blocksList[var8] instanceof IPlantableBlock || Block.blocksList[var8] instanceof IPlantableBlock && ((IPlantableBlock) Block.blocksList[var8]).isPlantable(var10a);
					final boolean flag2 = Block.blocksList[var8] instanceof IPlantableBlock && waterBlocksNearby >= ((IPlantableBlock) Block.blocksList[var8]).requiredLiquidBlocksNearby() || Block.blocksList[var8] instanceof IPlantableBlock && waterBlocksNearby >= ((IPlantableBlock) Block.blocksList[var8]).requiredLiquidBlocksNearby();
					final boolean flag3 = par4 < 256 - var6 - 1;
					final boolean flag4 = (Block.blocksList[var8] instanceof BlockGrass || Block.blocksList[var8] instanceof BlockDirt) && waterBlocksNearby >= 4;

					if (flag && flag2 && flag3 || flag4)
					{
						int var16;

						for (var16 = par4 - 3 + var6; var16 <= par4 + var6; ++var16)
						{
							var10 = var16 - (par4 + var6);
							var11 = 1 - var10 / 2;

							for (var12 = par3 - var11; var12 <= par3 + var11; ++var12)
							{
								final int var13 = var12 - par3;

								for (int var14 = par5 - var11; var14 <= par5 + var11; ++var14)
								{
									final int var15 = var14 - par5;

									final Block block = Block.blocksList[par1World.getBlockId(var12, var16, var14)];

									if ((Math.abs(var13) != var11 || Math.abs(var15) != var11 || par2Random.nextInt(2) != 0 && var10 != 0) && (block == null || block.canBeReplacedByLeaves(par1World, var12, var16, var14)))
									{
										this.setBlockAndMetadata(par1World, var12, var16, var14, Block.leaves.blockID, 2);
									}
								}
							}
						}

						for (var16 = 0; var16 < var6; ++var16)
						{
							var10 = par1World.getBlockId(par3, par4 + var16, par5);

							final Block block = Block.blocksList[var10];

							if (var10 == 0 || block == null || block.isLeaves(par1World, par3, par4 + var16, par5))
							{
								this.setBlockAndMetadata(par1World, par3, par4 + var16, par5, Block.wood.blockID, 2);
							}
						}

						return true;
					}
					else
					{
						return false;
					}
				}
				else
				{
					return false;
				}
			}
		}
		else
		{
			return false;
		}
	}
}
