package micdoodle8.mods.galacticraft.mars.world.gen;

import java.util.Random;

import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

/**
 * GCMarsWorldGenTerraformTree.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsWorldGenTerraformTree extends WorldGenerator
{
	private final int minTreeHeight;
	private final boolean vinesGrow;
	private final int metaWood;
	private final int metaLeaves;

	public GCMarsWorldGenTerraformTree(boolean par1)
	{
		this(par1, 4, 0, 0, false);
	}

	public GCMarsWorldGenTerraformTree(boolean par1, int par2, int par3, int par4, boolean par5)
	{
		super(par1);
		this.minTreeHeight = par2;
		this.metaWood = par3;
		this.metaLeaves = par4;
		this.vinesGrow = par5;
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
	{
		int l = par2Random.nextInt(3) + this.minTreeHeight;
		boolean flag = true;

		if (par4 >= 1 && par4 + l + 1 <= 256)
		{
			int i1;
			byte b0;
			int j1;
			int k1;
			int k2;

			for (i1 = par4; i1 <= par4 + 1 + l; ++i1)
			{
				b0 = 1;

				if (i1 == par4)
				{
					b0 = 0;
				}

				if (i1 >= par4 + 1 + l - 2)
				{
					b0 = 2;
				}

				b0 += 5;

				for (int l1 = par3 - b0; l1 <= par3 + b0 && flag; ++l1)
				{
					for (j1 = par5 - b0; j1 <= par5 + b0 && flag; ++j1)
					{
						if (i1 >= 0 && i1 < 256)
						{
							k1 = par1World.getBlockId(l1, i1, j1);
							k2 = par1World.getBlockMetadata(l1, i1, j1);

							boolean isAir = par1World.isAirBlock(l1, i1, j1);

							if (!isAir && k1 != Block.grass.blockID && k1 != Block.waterStill.blockID && k1 != Block.waterMoving.blockID && k1 != GCMarsBlocks.marsBlock.blockID && k2 != 5)
							{
								flag = false;
							}
						}
						else
						{
							flag = false;
						}
					}
				}
			}

			if (!flag)
			{
				return false;
			}
			else
			{
				i1 = par1World.getBlockId(par3, par4 - 1, par5);

				if (par4 < 256 - l - 1)
				{
					b0 = 3;
					byte b1 = 0;
					int i2;
					int j2;

					for (j1 = par4 - b0 + l; j1 <= par4 + l; ++j1)
					{
						k1 = j1 - (par4 + l);
						i2 = b1 + 1 - k1 / 2;

						for (j2 = par3 - i2; j2 <= par3 + i2; ++j2)
						{
							k2 = j2 - par3;

							for (int l2 = par5 - i2; l2 <= par5 + i2; ++l2)
							{
								int i3 = l2 - par5;

								if (Math.abs(k2) != i2 || Math.abs(i3) != i2 || par2Random.nextInt(2) != 0 && k1 != 0)
								{
									int j3 = par1World.getBlockId(j2, j1, l2);
									Block block = Block.blocksList[j3];

									if (block == null || block.canBeReplacedByLeaves(par1World, j2, j1, l2))
									{
										this.setBlockAndMetadata(par1World, j2, j1, l2, Block.leaves.blockID, this.metaLeaves);
									}
								}
							}
						}
					}

					for (j1 = 0; j1 < l; ++j1)
					{
						k1 = par1World.getBlockId(par3, par4 + j1, par5);

						Block block = Block.blocksList[k1];

						if (k1 == 0 || block == null || block.isLeaves(par1World, par3, par4 + j1, par5))
						{
							this.setBlockAndMetadata(par1World, par3, par4 + j1, par5, Block.wood.blockID, this.metaWood);

							if (this.vinesGrow && j1 > 0)
							{
								if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3 - 1, par4 + j1, par5))
								{
									this.setBlockAndMetadata(par1World, par3 - 1, par4 + j1, par5, Block.vine.blockID, 8);
								}

								if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3 + 1, par4 + j1, par5))
								{
									this.setBlockAndMetadata(par1World, par3 + 1, par4 + j1, par5, Block.vine.blockID, 2);
								}

								if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3, par4 + j1, par5 - 1))
								{
									this.setBlockAndMetadata(par1World, par3, par4 + j1, par5 - 1, Block.vine.blockID, 1);
								}

								if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3, par4 + j1, par5 + 1))
								{
									this.setBlockAndMetadata(par1World, par3, par4 + j1, par5 + 1, Block.vine.blockID, 4);
								}
							}
						}
					}

					if (this.vinesGrow)
					{
						for (j1 = par4 - 3 + l; j1 <= par4 + l; ++j1)
						{
							k1 = j1 - (par4 + l);
							i2 = 2 - k1 / 2;

							for (j2 = par3 - i2; j2 <= par3 + i2; ++j2)
							{
								for (k2 = par5 - i2; k2 <= par5 + i2; ++k2)
								{
									Block block = Block.blocksList[par1World.getBlockId(j2, j1, k2)];
									if (block != null && block.isLeaves(par1World, j2, j1, k2))
									{
										if (par2Random.nextInt(4) == 0 && par1World.isAirBlock(j2 - 1, j1, k2))
										{
											this.growVines(par1World, j2 - 1, j1, k2, 8);
										}

										if (par2Random.nextInt(4) == 0 && par1World.isAirBlock(j2 + 1, j1, k2))
										{
											this.growVines(par1World, j2 + 1, j1, k2, 2);
										}

										if (par2Random.nextInt(4) == 0 && par1World.isAirBlock(j2, j1, k2 - 1))
										{
											this.growVines(par1World, j2, j1, k2 - 1, 1);
										}

										if (par2Random.nextInt(4) == 0 && par1World.isAirBlock(j2, j1, k2 + 1))
										{
											this.growVines(par1World, j2, j1, k2 + 1, 4);
										}
									}
								}
							}
						}

						if (par2Random.nextInt(5) == 0 && l > 5)
						{
							for (j1 = 0; j1 < 2; ++j1)
							{
								for (k1 = 0; k1 < 4; ++k1)
								{
									if (par2Random.nextInt(4 - j1) == 0)
									{
										i2 = par2Random.nextInt(3);
										this.setBlockAndMetadata(par1World, par3 + Direction.offsetX[Direction.rotateOpposite[k1]], par4 + l - 5 + j1, par5 + Direction.offsetZ[Direction.rotateOpposite[k1]], Block.cocoaPlant.blockID, i2 << 2 | k1);
									}
								}
							}
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

	/**
	 * Grows vines downward from the given block for a given length. Args:
	 * World, x, starty, z, vine-length
	 */
	private void growVines(World par1World, int par2, int par3, int par4, int par5)
	{
		this.setBlockAndMetadata(par1World, par2, par3, par4, Block.vine.blockID, par5);
		int i1 = 4;

		while (true)
		{
			--par3;

			if (!par1World.isAirBlock(par2, par3, par4) || i1 <= 0)
			{
				return;
			}

			this.setBlockAndMetadata(par1World, par2, par3, par4, Block.vine.blockID, par5);
			--i1;
		}
	}
}
