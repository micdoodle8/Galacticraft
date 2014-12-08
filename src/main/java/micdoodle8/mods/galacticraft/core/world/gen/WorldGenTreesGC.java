package micdoodle8.mods.galacticraft.core.world.gen;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.block.IPlantableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenTreesGC extends WorldGenerator
{
    /**
     * The minimum height of a generated tree.
     */
    private final int minTreeHeight;

    /**
     * True if this tree should grow Vines.
     */
    private final boolean vinesGrow;

    /**
     * The metadata value of the wood to use in tree generation.
     */
    private final int metaWood;

    /**
     * The metadata value of the leaves to use in tree generation.
     */
    private final int metaLeaves;

    public WorldGenTreesGC(boolean par1)
    {
        this(par1, 4, 0, 0, false);
    }

    public WorldGenTreesGC(boolean par1, int par2, int par3, int par4, boolean par5)
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
        final int var6 = par2Random.nextInt(3) + this.minTreeHeight;
        boolean var7 = true;

        if (par4 >= 1 && par4 + var6 + 1 <= 256)
        {
            int var8;
            byte var9;
            int var10;
            int var11;
            int var12;

            for (var8 = par4; var8 <= par4 + 1 + var6; ++var8)
            {
                var9 = 1;

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
                            final Block block = par1World.getBlock(var10, var8, var11);

                            if (!block.isAir(par1World, var10, var8, var11) && !block.isLeaves(par1World, var10, var8, var11) && block != Blocks.grass && block != Blocks.dirt && !block.isWood(par1World, var10, var8, var11))
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
                var10 = par1World.getBlockMetadata(par3, par4 - 1, par5);

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

                if (var8b != null)
                {
                    final boolean flag = var8b instanceof IPlantableBlock || var8b instanceof IPlantableBlock && ((IPlantableBlock) var8b).isPlantable(var10);
                    final boolean flag2 = var8b instanceof IPlantableBlock && waterBlocksNearby >= ((IPlantableBlock) var8b).requiredLiquidBlocksNearby() || var8b instanceof IPlantableBlock && waterBlocksNearby >= ((IPlantableBlock) var8b).requiredLiquidBlocksNearby();
                    final boolean flag3 = par4 < 256 - var6 - 1;
                    final boolean flag4 = (var8b instanceof BlockGrass || var8b instanceof BlockDirt) && waterBlocksNearby >= 4;

                    if (flag && flag2 && flag3 || flag4)
                    {
                        var9 = 3;
                        final byte var18 = 0;
                        int var13;
                        int var14;
                        int var15;

                        for (var11 = par4 - var9 + var6; var11 <= par4 + var6; ++var11)
                        {
                            var12 = var11 - (par4 + var6);
                            var13 = var18 + 1 - var12 / 2;

                            for (var14 = par3 - var13; var14 <= par3 + var13; ++var14)
                            {
                                var15 = var14 - par3;

                                for (int var16 = par5 - var13; var16 <= par5 + var13; ++var16)
                                {
                                    final int var17 = var16 - par5;

                                    final Block block = par1World.getBlock(var14, var11, var16);

                                    if ((Math.abs(var15) != var13 || Math.abs(var17) != var13 || par2Random.nextInt(2) != 0 && var12 != 0) && (block == null || block.canBeReplacedByLeaves(par1World, var14, var11, var16)))
                                    {
                                        this.setBlockAndNotifyAdequately(par1World, var14, var11, var16, Blocks.leaves, this.metaLeaves);
                                    }
                                }
                            }
                        }

                        for (var11 = 0; var11 < var6; ++var11)
                        {
                            Block block = par1World.getBlock(par3, par4 + var11, par5);

                            if (block.isAir(par1World, par3, par4 + var11, par5) || block.isLeaves(par1World, par3, par4 + var11, par5))
                            {
                                this.setBlockAndNotifyAdequately(par1World, par3, par4 + var11, par5, Blocks.log, this.metaWood);

                                if (this.vinesGrow && var11 > 0)
                                {
                                    if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3 - 1, par4 + var11, par5))
                                    {
                                        this.setBlockAndNotifyAdequately(par1World, par3 - 1, par4 + var11, par5, Blocks.vine, 8);
                                    }

                                    if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3 + 1, par4 + var11, par5))
                                    {
                                        this.setBlockAndNotifyAdequately(par1World, par3 + 1, par4 + var11, par5, Blocks.vine, 2);
                                    }

                                    if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3, par4 + var11, par5 - 1))
                                    {
                                        this.setBlockAndNotifyAdequately(par1World, par3, par4 + var11, par5 - 1, Blocks.vine, 1);
                                    }

                                    if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3, par4 + var11, par5 + 1))
                                    {
                                        this.setBlockAndNotifyAdequately(par1World, par3, par4 + var11, par5 + 1, Blocks.vine, 4);
                                    }
                                }
                            }
                        }

                        if (this.vinesGrow)
                        {
                            for (var11 = par4 - 3 + var6; var11 <= par4 + var6; ++var11)
                            {
                                var12 = var11 - (par4 + var6);
                                var13 = 2 - var12 / 2;

                                for (var14 = par3 - var13; var14 <= par3 + var13; ++var14)
                                {
                                    for (var15 = par5 - var13; var15 <= par5 + var13; ++var15)
                                    {
                                        final Block block = par1World.getBlock(var14, var11, var15);
                                        if (block != null && block.isLeaves(par1World, var14, var11, var15))
                                        {
                                            if (par2Random.nextInt(4) == 0 && par1World.getBlock(var14 - 1, var11, var15).isAir(par1World, var14 - 1, var11, var15))
                                            {
                                                this.growVines(par1World, var14 - 1, var11, var15, 8);
                                            }

                                            if (par2Random.nextInt(4) == 0 && par1World.getBlock(var14 + 1, var11, var15).isAir(par1World, var14 + 1, var11, var15))
                                            {
                                                this.growVines(par1World, var14 + 1, var11, var15, 2);
                                            }

                                            if (par2Random.nextInt(4) == 0 && par1World.getBlock(var14, var11, var15 - 1).isAir(par1World, var14, var11, var15 - 1))
                                            {
                                                this.growVines(par1World, var14, var11, var15 - 1, 1);
                                            }

                                            if (par2Random.nextInt(4) == 0 && par1World.getBlock(var14, var11, var15 + 1).isAir(par1World, var14, var11, var15 + 1))
                                            {
                                                this.growVines(par1World, var14, var11, var15 + 1, 4);
                                            }
                                        }
                                    }
                                }
                            }

                            if (par2Random.nextInt(5) == 0 && var6 > 5)
                            {
                                for (var11 = 0; var11 < 2; ++var11)
                                {
                                    for (var12 = 0; var12 < 4; ++var12)
                                    {
                                        if (par2Random.nextInt(4 - var11) == 0)
                                        {
                                            var13 = par2Random.nextInt(3);
                                            this.setBlockAndNotifyAdequately(par1World, par3 + Direction.offsetX[Direction.rotateOpposite[var12]], par4 + var6 - 5 + var11, par5 + Direction.offsetZ[Direction.rotateOpposite[var12]], Blocks.cocoa, var13 << 2 | var12);
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
        this.setBlockAndNotifyAdequately(par1World, par2, par3, par4, Blocks.vine, par5);
        int var6 = 4;

        while (true)
        {
            --par3;

            if (!par1World.getBlock(par2, par3, par4).isAir(par1World, par2, par3, par4) || var6 <= 0)
            {
                return;
            }

            this.setBlockAndNotifyAdequately(par1World, par2, par3, par4, Blocks.vine, par5);
            --var6;
        }
    }
}
