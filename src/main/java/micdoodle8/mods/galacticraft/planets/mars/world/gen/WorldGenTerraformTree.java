//package micdoodle8.mods.galacticraft.planets.mars.world.gen;
//
//import net.minecraft.block.*;
//import net.minecraft.block.VineBlock;
//import net.minecraft.block.CocoaBlock;
//import net.minecraft.block.material.Material;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.Blocks;
//import net.minecraft.item.ItemStack;
//import net.minecraft.state.BooleanProperty;
//import net.minecraft.tags.BlockTags;
//import net.minecraft.util.Direction;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IWorld;
//import net.minecraft.world.World;
//import net.minecraft.world.gen.ChunkGenerator;
//import net.minecraft.world.gen.GenerationSettings;
//import net.minecraft.world.gen.IWorldGenerationBaseReader;
//import net.minecraft.world.gen.feature.Feature;
//import net.minecraft.world.gen.feature.NoFeatureConfig;
//
//import java.util.Random;
//
//public class WorldGenTerraformTree extends Feature<NoFeatureConfig>
//{
//    private final int minTreeHeight;
//    private final boolean vinesGrow;
////    private final int metaWood;
////    private final int metaLeaves;
//
//    public WorldGenTerraformTree(boolean par1, ItemStack sapling)
//    {
//        this(par1, 4, 0, 0, false);
//    }
//
//    public WorldGenTerraformTree(boolean par1, int par2, int par3, int par4, boolean vines)
//    {
//        super(par1);
//        this.minTreeHeight = par2;
//        this.metaWood = par3;
//        this.metaLeaves = par4;
//        this.vinesGrow = vines;
//    }
//
//    @Override
//    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
//    {
//        int i = rand.nextInt(3) + this.minTreeHeight;
//        boolean flag = true;
//
//        if (pos.getY() >= 1 && pos.getY() + i + 1 <= 256)
//        {
//            byte b0;
//            int l;
//
//            for (int j = pos.getY(); j <= pos.getY() + 1 + i; ++j)
//            {
//                b0 = 1;
//
//                if (j == pos.getY())
//                {
//                    b0 = 0;
//                }
//
//                if (j >= pos.getY() + 1 + i - 2)
//                {
//                    b0 = 2;
//                }
//
//                for (int k = pos.getX() - b0; k <= pos.getX() + b0 && flag; ++k)
//                {
//                    for (l = pos.getZ() - b0; l <= pos.getZ() + b0 && flag; ++l)
//                    {
//                        if (j >= 0 && j < 256)
//                        {
//                            if (!this.isReplaceable(worldIn, new BlockPos(k, j, l)))
//                            {
//                                flag = false;
//                            }
//                        }
//                        else
//                        {
//                            flag = false;
//                        }
//                    }
//                }
//            }
//
//            if (!flag)
//            {
//                return false;
//            }
//            else
//            {
//                BlockPos down = pos.down();
//                Block block1 = worldIn.getBlockState(down).getBlock();
//                boolean isSoil = block1.canSustainPlant(worldIn.getBlockState(down), worldIn, down, Direction.UP, (SaplingBlock) Blocks.SAPLING);
//
//                if (isSoil && pos.getY() < 256 - i - 1)
//                {
//                    block1.onPlantGrow(worldIn.getBlockState(down), worldIn, down, pos);
//                    b0 = 3;
//                    byte b1 = 0;
//                    int i1;
//                    int j1;
//                    int k1;
//                    int l1;
//                    BlockPos blockpos1;
//
//                    for (l = pos.getY() - b0 + i; l <= pos.getY() + i; ++l)
//                    {
//                        i1 = l - (pos.getY() + i);
//                        j1 = b1 + 1 - i1 / 2;
//
//                        for (k1 = pos.getX() - j1; k1 <= pos.getX() + j1; ++k1)
//                        {
//                            l1 = k1 - pos.getX();
//
//                            for (int i2 = pos.getZ() - j1; i2 <= pos.getZ() + j1; ++i2)
//                            {
//                                int j2 = i2 - pos.getZ();
//
//                                if (Math.abs(l1) != j1 || Math.abs(j2) != j1 || rand.nextInt(2) != 0 && i1 != 0)
//                                {
//                                    blockpos1 = new BlockPos(k1, l, i2);
//                                    BlockState state = worldIn.getBlockState(blockpos1);
//                                    Block block = state.getBlock();
//
//                                    if (block.isAir(state, worldIn, blockpos1) || block.isLeaves(state, worldIn, blockpos1) || block.getMaterial(state) == Material.VINE)
//                                    {
//                                        this.setBlockAndNotifyAdequately(worldIn, blockpos1, Blocks.LEAVES.getStateFromMeta(this.metaLeaves));
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    for (l = 0; l < i; ++l)
//                    {
//                        BlockPos upN = pos.up(l);
//                        BlockState state2 = worldIn.getBlockState(upN);
//                        Block block2 = state2.getBlock();
//
//                        if (block2.isAir(state2, worldIn, upN) || block2.isLeaves(state2, worldIn, upN) || block2.getMaterial(state2) == Material.VINE)
//                        {
//                            this.setBlockState(worldIn, pos.up(l), Blocks.LOG.getStateFromMeta(this.metaWood));
//
//                            if (this.vinesGrow && l > 0)
//                            {
//                                if (rand.nextInt(3) > 0 && worldIn.isAirBlock(pos.add(-1, l, 0)))
//                                {
//                                    this.func_181647_a(worldIn, pos.add(-1, l, 0), VineBlock.EAST);
//                                }
//
//                                if (rand.nextInt(3) > 0 && worldIn.isAirBlock(pos.add(1, l, 0)))
//                                {
//                                    this.func_181647_a(worldIn, pos.add(1, l, 0), VineBlock.WEST);
//                                }
//
//                                if (rand.nextInt(3) > 0 && worldIn.isAirBlock(pos.add(0, l, -1)))
//                                {
//                                    this.func_181647_a(worldIn, pos.add(0, l, -1), VineBlock.SOUTH);
//                                }
//
//                                if (rand.nextInt(3) > 0 && worldIn.isAirBlock(pos.add(0, l, 1)))
//                                {
//                                    this.func_181647_a(worldIn, pos.add(0, l, 1), VineBlock.NORTH);
//                                }
//                            }
//                        }
//                    }
//
//                    if (this.vinesGrow)
//                    {
//                        for (l = pos.getY() - 3 + i; l <= pos.getY() + i; ++l)
//                        {
//                            i1 = l - (pos.getY() + i);
//                            j1 = 2 - i1 / 2;
//
//                            for (k1 = pos.getX() - j1; k1 <= pos.getX() + j1; ++k1)
//                            {
//                                for (l1 = pos.getZ() - j1; l1 <= pos.getZ() + j1; ++l1)
//                                {
//                                    BlockPos blockpos3 = new BlockPos(k1, l, l1);
//
//                                    BlockState state3 = worldIn.getBlockState(blockpos3);
//                                    if (state3.getBlock().isLeaves(state3, worldIn, blockpos3))
//                                    {
//                                        BlockPos blockpos4 = blockpos3.west();
//                                        blockpos1 = blockpos3.east();
//                                        BlockPos blockpos5 = blockpos3.north();
//                                        BlockPos blockpos2 = blockpos3.south();
//
//                                        BlockState state4 = worldIn.getBlockState(blockpos4);
//                                        if (rand.nextInt(4) == 0 && state4.getBlock().isAir(state4, worldIn, blockpos4))
//                                        {
//                                            this.func_181647_a(worldIn, blockpos4, VineBlock.EAST);
//                                        }
//
//                                        BlockState state1 = worldIn.getBlockState(blockpos1);
//                                        if (rand.nextInt(4) == 0 && state1.getBlock().isAir(state1, worldIn, blockpos1))
//                                        {
//                                            this.func_181647_a(worldIn, blockpos1, VineBlock.WEST);
//                                        }
//
//                                        BlockState state5 = worldIn.getBlockState(blockpos5);
//                                        if (rand.nextInt(4) == 0 && state5.getBlock().isAir(state5, worldIn, blockpos5))
//                                        {
//                                            this.func_181647_a(worldIn, blockpos5, VineBlock.SOUTH);
//                                        }
//
//                                        BlockState state2 = worldIn.getBlockState(blockpos2);
//                                        if (rand.nextInt(4) == 0 && state2.getBlock().isAir(state2, worldIn, blockpos2))
//                                        {
//                                            this.func_181647_a(worldIn, blockpos2, VineBlock.NORTH);
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                        if (rand.nextInt(5) == 0 && i > 5)
//                        {
//                            for (l = 0; l < 2; ++l)
//                            {
//                                for (Direction enumfacing : Direction.Plane.HORIZONTAL)
//                                {
//                                    if (rand.nextInt(4 - l) == 0)
//                                    {
//                                        j1 = rand.nextInt(3);
//                                        this.func_181652_a(worldIn, rand.nextInt(3), pos.add(enumfacing.getXOffset(), i - 5 + l, enumfacing.getZOffset()), enumfacing);
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    return true;
//                }
//                else
//                {
//                    return false;
//                }
//            }
//        }
//        else
//        {
//            return false;
//        }
//    }
//
//    private void func_181647_a(IWorld world, BlockPos pos, BooleanProperty facing)
//    {
//        BlockState iblockstate = Blocks.VINE.getDefaultState().with(facing, Boolean.valueOf(true));
//        this.setBlockState(world, pos, iblockstate);
//        int i = 4;
//
//        for (pos = pos.down(); world.getBlockState(pos).getBlock().isAir(world.getBlockState(pos), world, pos) && i > 0; --i)
//        {
//            this.setBlockState(world, pos, iblockstate);
//            pos = pos.down();
//        }
//    }
//
//    private void func_181652_a(World p_181652_1_, int p_181652_2_, BlockPos p_181652_3_, Direction p_181652_4_)
//    {
//        this.setBlockState(p_181652_1_, p_181652_3_, Blocks.COCOA.getDefaultState().with(CocoaBlock.AGE, Integer.valueOf(p_181652_2_)).with(CocoaBlock.FACING, p_181652_4_));
//    }
//
//    public boolean isReplaceable(World world, BlockPos pos)
//    {
//        return isAirOrLeaves(world, pos);
//    }
//
//    protected static boolean isAirOrLeaves(IWorldGenerationBaseReader worldIn, BlockPos pos) {
//        if (!(worldIn instanceof net.minecraft.world.IWorldReader)) // FORGE: Redirect to state method when possible
//            return worldIn.hasBlockState(pos, (state) -> state.isAir() || state.isIn(BlockTags.LEAVES));
//        else return worldIn.hasBlockState(pos, state -> state.canBeReplacedByLeaves((net.minecraft.world.IWorldReader)worldIn, pos));
//    }
//}
