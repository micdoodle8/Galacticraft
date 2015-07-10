package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenTerraformTree extends WorldGenerator
{
    private final int minTreeHeight;
    private final boolean vinesGrow;
    private final int metaWood;
    private final int metaLeaves;

    public WorldGenTerraformTree(boolean par1, ItemStack sapling)
    {
        this(par1, 4, 0, 0, false);
    }

    public WorldGenTerraformTree(boolean par1, int par2, int par3, int par4, boolean vines)
    {
        super(par1);
        this.minTreeHeight = par2;
        this.metaWood = par3;
        this.metaLeaves = par4;
        this.vinesGrow = vines;
    }

    @Override
    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        int i = p_180709_2_.nextInt(3) + this.minTreeHeight;
        boolean flag = true;

        if (p_180709_3_.getY() >= 1 && p_180709_3_.getY() + i + 1 <= 256)
        {
            byte b0;
            int l;

            for (int j = p_180709_3_.getY(); j <= p_180709_3_.getY() + 1 + i; ++j)
            {
                b0 = 1;

                if (j == p_180709_3_.getY())
                {
                    b0 = 0;
                }

                if (j >= p_180709_3_.getY() + 1 + i - 2)
                {
                    b0 = 2;
                }

                for (int k = p_180709_3_.getX() - b0; k <= p_180709_3_.getX() + b0 && flag; ++k)
                {
                    for (l = p_180709_3_.getZ() - b0; l <= p_180709_3_.getZ() + b0 && flag; ++l)
                    {
                        if (j >= 0 && j < 256)
                        {
                            if (!this.isReplaceable(worldIn, new BlockPos(k, j, l)))
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
                BlockPos down = p_180709_3_.down();
                Block block1 = worldIn.getBlockState(down).getBlock();
                boolean isSoil = block1.canSustainPlant(worldIn, down, net.minecraft.util.EnumFacing.UP, (net.minecraft.block.BlockSapling)Blocks.sapling);

                if (isSoil && p_180709_3_.getY() < 256 - i - 1)
                {
                    block1.onPlantGrow(worldIn, down, p_180709_3_);
                    b0 = 3;
                    byte b1 = 0;
                    int i1;
                    int j1;
                    int k1;
                    int l1;
                    BlockPos blockpos1;

                    for (l = p_180709_3_.getY() - b0 + i; l <= p_180709_3_.getY() + i; ++l)
                    {
                        i1 = l - (p_180709_3_.getY() + i);
                        j1 = b1 + 1 - i1 / 2;

                        for (k1 = p_180709_3_.getX() - j1; k1 <= p_180709_3_.getX() + j1; ++k1)
                        {
                            l1 = k1 - p_180709_3_.getX();

                            for (int i2 = p_180709_3_.getZ() - j1; i2 <= p_180709_3_.getZ() + j1; ++i2)
                            {
                                int j2 = i2 - p_180709_3_.getZ();

                                if (Math.abs(l1) != j1 || Math.abs(j2) != j1 || p_180709_2_.nextInt(2) != 0 && i1 != 0)
                                {
                                    blockpos1 = new BlockPos(k1, l, i2);
                                    Block block = worldIn.getBlockState(blockpos1).getBlock();

                                    if (block.isAir(worldIn, blockpos1) || block.isLeaves(worldIn, blockpos1) || block.getMaterial() == Material.vine)
                                    {
                                        this.func_175905_a(worldIn, blockpos1, Blocks.leaves, this.metaLeaves);
                                    }
                                }
                            }
                        }
                    }

                    for (l = 0; l < i; ++l)
                    {
                        BlockPos upN = p_180709_3_.up(l);
                        Block block2 = worldIn.getBlockState(upN).getBlock();

                        if (block2.isAir(worldIn, upN) || block2.isLeaves(worldIn, upN) || block2.getMaterial() == Material.vine)
                        {
                            this.func_175905_a(worldIn, p_180709_3_.up(l), Blocks.log, this.metaWood);

                            if (this.vinesGrow && l > 0)
                            {
                                if (p_180709_2_.nextInt(3) > 0 && worldIn.isAirBlock(p_180709_3_.add(-1, l, 0)))
                                {
                                    this.func_175905_a(worldIn, p_180709_3_.add(-1, l, 0), Blocks.vine, BlockVine.EAST_FLAG);
                                }

                                if (p_180709_2_.nextInt(3) > 0 && worldIn.isAirBlock(p_180709_3_.add(1, l, 0)))
                                {
                                    this.func_175905_a(worldIn, p_180709_3_.add(1, l, 0), Blocks.vine, BlockVine.WEST_FLAG);
                                }

                                if (p_180709_2_.nextInt(3) > 0 && worldIn.isAirBlock(p_180709_3_.add(0, l, -1)))
                                {
                                    this.func_175905_a(worldIn, p_180709_3_.add(0, l, -1), Blocks.vine, BlockVine.SOUTH_FLAG);
                                }

                                if (p_180709_2_.nextInt(3) > 0 && worldIn.isAirBlock(p_180709_3_.add(0, l, 1)))
                                {
                                    this.func_175905_a(worldIn, p_180709_3_.add(0, l, 1), Blocks.vine, BlockVine.NORTH_FLAG);
                                }
                            }
                        }
                    }

                    if (this.vinesGrow)
                    {
                        for (l = p_180709_3_.getY() - 3 + i; l <= p_180709_3_.getY() + i; ++l)
                        {
                            i1 = l - (p_180709_3_.getY() + i);
                            j1 = 2 - i1 / 2;

                            for (k1 = p_180709_3_.getX() - j1; k1 <= p_180709_3_.getX() + j1; ++k1)
                            {
                                for (l1 = p_180709_3_.getZ() - j1; l1 <= p_180709_3_.getZ() + j1; ++l1)
                                {
                                    BlockPos blockpos3 = new BlockPos(k1, l, l1);

                                    if (worldIn.getBlockState(blockpos3).getBlock().isLeaves(worldIn, blockpos3))
                                    {
                                        BlockPos blockpos4 = blockpos3.west();
                                        blockpos1 = blockpos3.east();
                                        BlockPos blockpos5 = blockpos3.north();
                                        BlockPos blockpos2 = blockpos3.south();

                                        if (p_180709_2_.nextInt(4) == 0 && worldIn.getBlockState(blockpos4).getBlock().isAir(worldIn, blockpos4))
                                        {
                                            this.func_175923_a(worldIn, blockpos4, BlockVine.EAST_FLAG);
                                        }

                                        if (p_180709_2_.nextInt(4) == 0 && worldIn.getBlockState(blockpos1).getBlock().isAir(worldIn, blockpos1))
                                        {
                                            this.func_175923_a(worldIn, blockpos1, BlockVine.WEST_FLAG);
                                        }

                                        if (p_180709_2_.nextInt(4) == 0 && worldIn.getBlockState(blockpos5).getBlock().isAir(worldIn, blockpos5))
                                        {
                                            this.func_175923_a(worldIn, blockpos5, BlockVine.SOUTH_FLAG);
                                        }

                                        if (p_180709_2_.nextInt(4) == 0 && worldIn.getBlockState(blockpos2).getBlock().isAir(worldIn, blockpos2))
                                        {
                                            this.func_175923_a(worldIn, blockpos2, BlockVine.NORTH_FLAG);
                                        }
                                    }
                                }
                            }
                        }

                        if (p_180709_2_.nextInt(5) == 0 && i > 5)
                        {
                            for (l = 0; l < 2; ++l)
                            {
                                for (i1 = 0; i1 < 4; ++i1)
                                {
                                    if (p_180709_2_.nextInt(4 - l) == 0)
                                    {
                                        j1 = p_180709_2_.nextInt(3);
                                        EnumFacing enumfacing = EnumFacing.getHorizontal(i1).getOpposite();
                                        this.func_175905_a(worldIn, p_180709_3_.add(enumfacing.getFrontOffsetX(), i - 5 + l, enumfacing.getFrontOffsetZ()), Blocks.cocoa, j1 << 2 | EnumFacing.getHorizontal(i1).getHorizontalIndex());
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
    private void growVines(World par1World, BlockPos pos, IBlockState state)
    {
        this.func_175903_a(par1World, pos, state);
        int i1 = 4;
        int y = pos.getY();

        while (true)
        {
            --y;

            if (!par1World.isAirBlock(new BlockPos(pos.getX(), y, pos.getZ())) || i1 <= 0)
            {
                return;
            }

            this.func_175903_a(par1World, new BlockPos(pos.getX(), y, pos.getZ()), state);
            --i1;
        }
    }

    private void func_175923_a(World worldIn, BlockPos p_175923_2_, int p_175923_3_)
    {
        this.func_175905_a(worldIn, p_175923_2_, Blocks.vine, p_175923_3_);
        int j = 4;

        for (p_175923_2_ = p_175923_2_.down(); worldIn.getBlockState(p_175923_2_).getBlock().isAir(worldIn, p_175923_2_) && j > 0; --j)
        {
            this.func_175905_a(worldIn, p_175923_2_, Blocks.vine, p_175923_3_);
            p_175923_2_ = p_175923_2_.down();
        }
    }

    protected boolean func_150523_a(Block p_150523_1_)
    {
        return p_150523_1_.getMaterial() == Material.air || p_150523_1_.getMaterial() == Material.leaves || p_150523_1_ == Blocks.grass || p_150523_1_ == Blocks.dirt || p_150523_1_ == Blocks.log || p_150523_1_ == Blocks.log2 || p_150523_1_ == Blocks.sapling || p_150523_1_ == Blocks.vine;
    }

    public boolean isReplaceable(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock().isAir(world, pos) || state.getBlock().isLeaves(world, pos) || state.getBlock().isWood(world, pos) || func_150523_a(state.getBlock());
    }
}
