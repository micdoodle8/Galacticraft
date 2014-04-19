package micdoodle8.mods.galacticraft.core.world.gen;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.block.IPlantableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.init.Blocks;
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
public class WorldGenForestGC extends WorldGenerator
{
	public WorldGenForestGC(boolean par1)
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
							Block var12b = par1World.getBlock(var10, var8, var11);

							if (var12b != Blocks.air && !var12b.isLeaves(par1World, var10, var8, var11))
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
				Block var8b = par1World.getBlock(par3, par4 - 1, par5);
				final int var10a = par1World.getBlockMetadata(par3, par4 - 1, par5);

				int waterBlocksNearby = 0;

				for (int i = -4; i < 5; i++)
				{
					for (int j = -4; j < 5; j++)
					{
						if (par1World.getBlock(par3 + i, par4 - 1, par5 + j) == Blocks.flowing_water || par1World.getBlock(par3 + i, par4 - 1, par5 + j) == Blocks.water)
						{
							waterBlocksNearby++;
						}
					}
				}

				final boolean flag = var8b instanceof IPlantableBlock || var8b instanceof IPlantableBlock && ((IPlantableBlock) var8b).isPlantable(var10a);
				final boolean flag2 = var8b instanceof IPlantableBlock && waterBlocksNearby >= ((IPlantableBlock) var8b).requiredLiquidBlocksNearby() || var8b instanceof IPlantableBlock && waterBlocksNearby >= ((IPlantableBlock) var8b).requiredLiquidBlocksNearby();
				final boolean flag3 = par4 < 256 - var6 - 1;
				final boolean flag4 = (var8b instanceof BlockGrass || var8b instanceof BlockDirt) && waterBlocksNearby >= 4;

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

								final Block block = par1World.getBlock(var12, var16, var14);

								if ((Math.abs(var13) != var11 || Math.abs(var15) != var11 || par2Random.nextInt(2) != 0 && var10 != 0) && (block == null || block.canBeReplacedByLeaves(par1World, var12, var16, var14)))
								{
									this.setBlockAndNotifyAdequately(par1World, var12, var16, var14, Blocks.leaves, 2);
								}
							}
						}
					}

					for (var16 = 0; var16 < var6; ++var16)
					{
						Block block = par1World.getBlock(par3, par4 + var16, par5);

						if (block == Blocks.air || block.isLeaves(par1World, par3, par4 + var16, par5))
						{
							this.setBlockAndNotifyAdequately(par1World, par3, par4 + var16, par5, Blocks.planks, 2);
						}
					}

					return true;
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
