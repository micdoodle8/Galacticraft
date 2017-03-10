package micdoodle8.mods.galacticraft.planets.mars.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
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
    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        int i = rand.nextInt(3) + this.minTreeHeight;
        boolean flag = true;

        if (position.getY() >= 1 && position.getY() + i + 1 <= 256)
        {
            byte b0;
            int l;

            for (int j = position.getY(); j <= position.getY() + 1 + i; ++j)
            {
                b0 = 1;

                if (j == position.getY())
                {
                    b0 = 0;
                }

                if (j >= position.getY() + 1 + i - 2)
                {
                    b0 = 2;
                }

                for (int k = position.getX() - b0; k <= position.getX() + b0 && flag; ++k)
                {
                    for (l = position.getZ() - b0; l <= position.getZ() + b0 && flag; ++l)
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
                BlockPos down = position.down();
                Block block1 = worldIn.getBlockState(down).getBlock();
                boolean isSoil = block1.canSustainPlant(worldIn.getBlockState(down), worldIn, down, net.minecraft.util.EnumFacing.UP, (net.minecraft.block.BlockSapling) Blocks.SAPLING);

                if (isSoil && position.getY() < 256 - i - 1)
                {
                    block1.onPlantGrow(worldIn.getBlockState(down), worldIn, down, position);
                    b0 = 3;
                    byte b1 = 0;
                    int i1;
                    int j1;
                    int k1;
                    int l1;
                    BlockPos blockpos1;

                    for (l = position.getY() - b0 + i; l <= position.getY() + i; ++l)
                    {
                        i1 = l - (position.getY() + i);
                        j1 = b1 + 1 - i1 / 2;

                        for (k1 = position.getX() - j1; k1 <= position.getX() + j1; ++k1)
                        {
                            l1 = k1 - position.getX();

                            for (int i2 = position.getZ() - j1; i2 <= position.getZ() + j1; ++i2)
                            {
                                int j2 = i2 - position.getZ();

                                if (Math.abs(l1) != j1 || Math.abs(j2) != j1 || rand.nextInt(2) != 0 && i1 != 0)
                                {
                                    blockpos1 = new BlockPos(k1, l, i2);
                                    IBlockState state = worldIn.getBlockState(blockpos1);
                                    Block block = state.getBlock();

                                    if (block.isAir(state, worldIn, blockpos1) || block.isLeaves(state, worldIn, blockpos1) || block.getMaterial(state) == Material.VINE)
                                    {
                                        this.setBlockAndNotifyAdequately(worldIn, blockpos1, Blocks.LEAVES.getStateFromMeta(this.metaLeaves));
                                    }
                                }
                            }
                        }
                    }

                    for (l = 0; l < i; ++l)
                    {
                        BlockPos upN = position.up(l);
                        IBlockState state2 = worldIn.getBlockState(upN);
                        Block block2 = state2.getBlock();

                        if (block2.isAir(state2, worldIn, upN) || block2.isLeaves(state2, worldIn, upN) || block2.getMaterial(state2) == Material.VINE)
                        {
                            this.setBlockAndNotifyAdequately(worldIn, position.up(l), Blocks.LOG.getStateFromMeta(this.metaWood));

                            if (this.vinesGrow && l > 0)
                            {
                                if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(-1, l, 0)))
                                {
                                    this.func_181647_a(worldIn, position.add(-1, l, 0), BlockVine.EAST);
                                }

                                if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(1, l, 0)))
                                {
                                    this.func_181647_a(worldIn, position.add(1, l, 0), BlockVine.WEST);
                                }

                                if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(0, l, -1)))
                                {
                                    this.func_181647_a(worldIn, position.add(0, l, -1), BlockVine.SOUTH);
                                }

                                if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(0, l, 1)))
                                {
                                    this.func_181647_a(worldIn, position.add(0, l, 1), BlockVine.NORTH);
                                }
                            }
                        }
                    }

                    if (this.vinesGrow)
                    {
                        for (l = position.getY() - 3 + i; l <= position.getY() + i; ++l)
                        {
                            i1 = l - (position.getY() + i);
                            j1 = 2 - i1 / 2;

                            for (k1 = position.getX() - j1; k1 <= position.getX() + j1; ++k1)
                            {
                                for (l1 = position.getZ() - j1; l1 <= position.getZ() + j1; ++l1)
                                {
                                    BlockPos blockpos3 = new BlockPos(k1, l, l1);

                                    IBlockState state3 = worldIn.getBlockState(blockpos3);
                                    if (state3.getBlock().isLeaves(state3, worldIn, blockpos3))
                                    {
                                        BlockPos blockpos4 = blockpos3.west();
                                        blockpos1 = blockpos3.east();
                                        BlockPos blockpos5 = blockpos3.north();
                                        BlockPos blockpos2 = blockpos3.south();

                                        IBlockState state4 = worldIn.getBlockState(blockpos4);
                                        if (rand.nextInt(4) == 0 && state4.getBlock().isAir(state4, worldIn, blockpos4))
                                        {
                                            this.func_181647_a(worldIn, blockpos4, BlockVine.EAST);
                                        }

                                        IBlockState state1 = worldIn.getBlockState(blockpos1);
                                        if (rand.nextInt(4) == 0 && state1.getBlock().isAir(state1, worldIn, blockpos1))
                                        {
                                            this.func_181647_a(worldIn, blockpos1, BlockVine.WEST);
                                        }

                                        IBlockState state5 = worldIn.getBlockState(blockpos5);
                                        if (rand.nextInt(4) == 0 && state5.getBlock().isAir(state5, worldIn, blockpos5))
                                        {
                                            this.func_181647_a(worldIn, blockpos5, BlockVine.SOUTH);
                                        }

                                        IBlockState state2 = worldIn.getBlockState(blockpos2);
                                        if (rand.nextInt(4) == 0 && state2.getBlock().isAir(state2, worldIn, blockpos2))
                                        {
                                            this.func_181647_a(worldIn, blockpos2, BlockVine.NORTH);
                                        }
                                    }
                                }
                            }
                        }

                        if (rand.nextInt(5) == 0 && i > 5)
                        {
                            for (l = 0; l < 2; ++l)
                            {
                                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
                                {
                                    if (rand.nextInt(4 - l) == 0)
                                    {
                                        j1 = rand.nextInt(3);
                                        this.func_181652_a(worldIn, rand.nextInt(3), position.add(enumfacing.getFrontOffsetX(), i - 5 + l, enumfacing.getFrontOffsetZ()), enumfacing);
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

    private void func_181647_a(World world, BlockPos pos, PropertyBool facing)
    {
        IBlockState iblockstate = Blocks.VINE.getDefaultState().withProperty(facing, Boolean.valueOf(true));
        this.setBlockAndNotifyAdequately(world, pos, iblockstate);
        int i = 4;

        for (pos = pos.down(); world.getBlockState(pos).getBlock().isAir(world.getBlockState(pos), world, pos) && i > 0; --i)
        {
            this.setBlockAndNotifyAdequately(world, pos, iblockstate);
            pos = pos.down();
        }
    }

    private void func_181652_a(World p_181652_1_, int p_181652_2_, BlockPos p_181652_3_, EnumFacing p_181652_4_)
    {
        this.setBlockAndNotifyAdequately(p_181652_1_, p_181652_3_, Blocks.COCOA.getDefaultState().withProperty(BlockCocoa.AGE, Integer.valueOf(p_181652_2_)).withProperty(BlockCocoa.FACING, p_181652_4_));
    }

    protected boolean func_150523_a(IBlockState state)
    {
        Block block = state.getBlock();
        return block.getMaterial(state) == Material.AIR || block.getMaterial(state) == Material.LEAVES || block == Blocks.GRASS || block == Blocks.DIRT || block == Blocks.LOG || block == Blocks.LOG2 || block == Blocks.SAPLING || block == Blocks.VINE;
    }

    public boolean isReplaceable(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock().isAir(state, world, pos) || state.getBlock().isLeaves(state, world, pos) || state.getBlock().isWood(world, pos) || func_150523_a(state);
    }
}
