package micdoodle8.mods.galacticraft.core.world.gen;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.block.IPlantableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenTaigaGC extends WorldGenerator
{
    public WorldGenTaigaGC(boolean par1)
    {
        super(par1);
    }

    @Override
    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
    {
        final int var6 = par2Random.nextInt(4) + 6;
        final int var7 = 1 + par2Random.nextInt(2);
        final int var8 = var6 - var7;
        final int var9 = 2 + par2Random.nextInt(2);
        boolean var10 = true;

        if (par4 >= 1 && par4 + var6 + 1 <= 256)
        {
            int var11;
            int var13;
            int var15;
            int var10a;
            int var21 = 1;

            for (var11 = par4; var11 <= par4 + 1 + var6 && var10; ++var11)
            {
                if (var11 - par4 < var7)
                {
                    var21 = 0;
                }

                if (var11 >= par4 + 1 + var6 - 2)
                {
                    var21 = var9;
                }

                for (var13 = par3 - var21; var13 <= par3 + var21 && var10; ++var13)
                {
                    for (int var14 = par5 - var21; var14 <= par5 + var21 && var10; ++var14)
                    {
                        if (var11 >= 0 && var11 < 256)
                        {
                            Block var15b = par1World.getBlock(var13, var11, var14);

                            if (Blocks.air != var15b && var15b != null && !var15b.isLeaves(par1World, var13, var11, var14))
                            {
                                var10 = false;
                            }
                        }
                        else
                        {
                            var10 = false;
                        }
                    }
                }
            }

            if (!var10)
            {
                return false;
            }
            else
            {
                Block var11b = par1World.getBlock(par3, par4 - 1, par5);
                var10a = par1World.getBlockMetadata(par3, par4 - 1, par5);

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

                if (var11b != null)
                {
                    final boolean flag = var11b instanceof IPlantableBlock || var11b instanceof IPlantableBlock && ((IPlantableBlock) var11b).isPlantable(var10a);
                    final boolean flag2 = var11b instanceof IPlantableBlock && waterBlocksNearby >= ((IPlantableBlock) var11b).requiredLiquidBlocksNearby() || var11b instanceof IPlantableBlock && waterBlocksNearby >= ((IPlantableBlock) var11b).requiredLiquidBlocksNearby();
                    final boolean flag3 = (var11b instanceof BlockGrass || var11b instanceof BlockDirt) && waterBlocksNearby >= 4;

                    if (flag && flag2 || flag3)
                    {
                        var21 = par2Random.nextInt(2);
                        var13 = 1;
                        byte var22 = 0;
                        int var17;
                        int var16;

                        for (var15 = 0; var15 <= var8; ++var15)
                        {
                            var16 = par4 + var6 - var15;

                            for (var17 = par3 - var21; var17 <= par3 + var21; ++var17)
                            {
                                final int var18 = var17 - par3;

                                for (int var19 = par5 - var21; var19 <= par5 + var21; ++var19)
                                {
                                    final int var20 = var19 - par5;

                                    final Block block = par1World.getBlock(var17, var16, var19);

                                    if ((Math.abs(var18) != var21 || Math.abs(var20) != var21 || var21 <= 0) && (block == null || block.canBeReplacedByLeaves(par1World, var17, var16, var19)))
                                    {
                                        this.setBlockAndNotifyAdequately(par1World, var17, var16, var19, Blocks.leaves, 1);
                                    }
                                }
                            }

                            if (var21 >= var13)
                            {
                                var21 = var22;
                                var22 = 1;
                                ++var13;

                                if (var13 > var9)
                                {
                                    var13 = var9;
                                }
                            }
                            else
                            {
                                ++var21;
                            }
                        }

                        var15 = par2Random.nextInt(3);

                        for (var16 = 0; var16 < var6 - var15; ++var16)
                        {
                            final Block block = par1World.getBlock(par3, par4 + var16, par5);

                            if (block.isAir(par1World, par3, par4 + var16, par5) || block.isLeaves(par1World, par3, par4 + var16, par5))
                            {
                                this.setBlockAndNotifyAdequately(par1World, par3, par4 + var16, par5, Blocks.log, 1);
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
