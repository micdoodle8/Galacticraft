package micdoodle8.mods.galacticraft.core.blocks;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.minecraft.block.*;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RedstoneSide;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class BlockConcealedRedstone extends Block implements ISortable
{
    public static final IntegerProperty POWER = BlockStateProperties.POWER_0_15;
    private final Set<BlockPos> blocksNeedingUpdate = Sets.newHashSet();
    private boolean canProvidePower = true;

    public BlockConcealedRedstone(Block.Properties properties)
    {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(POWER, Integer.valueOf(0)));
    }

    protected static boolean canConnectTo(BlockState blockState, IBlockReader world, BlockPos pos, @Nullable Direction side)
    {
        Block block = blockState.getBlock();
        if (block == Blocks.REDSTONE_WIRE)
        {
            return true;
        }
        else if (blockState.getBlock() == Blocks.REPEATER)
        {
            Direction direction = blockState.get(RepeaterBlock.HORIZONTAL_FACING);
            return direction == side || direction.getOpposite() == side;
        }
        else if (Blocks.OBSERVER == blockState.getBlock())
        {
            return side == blockState.get(ObserverBlock.FACING);
        }
        else
        {
            return blockState.canConnectRedstone(world, pos, side) && side != null;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static int colorMultiplier(int p_176337_0_)
    {
        float f = (float) p_176337_0_ / 15.0F;
        float f1 = f * 0.6F + 0.4F;
        if (p_176337_0_ == 0)
        {
            f1 = 0.3F;
        }

        float f2 = f * f * 0.7F - 0.5F;
        float f3 = f * f * 0.6F - 0.7F;
        if (f2 < 0.0F)
        {
            f2 = 0.0F;
        }

        if (f3 < 0.0F)
        {
            f3 = 0.0F;
        }

        int i = MathHelper.clamp((int) (f1 * 255.0F), 0, 255);
        int j = MathHelper.clamp((int) (f2 * 255.0F), 0, 255);
        int k = MathHelper.clamp((int) (f3 * 255.0F), 0, 255);
        return -16777216 | i << 16 | j << 8 | k;
    }

    private RedstoneSide getSide(IBlockReader worldIn, BlockPos pos, Direction face)
    {
        BlockPos blockpos = pos.offset(face);
        BlockState blockstate = worldIn.getBlockState(blockpos);
        BlockPos blockpos1 = pos.up();
        BlockState blockstate1 = worldIn.getBlockState(blockpos1);
        if (!blockstate1.isNormalCube(worldIn, blockpos1))
        {
            boolean flag = blockstate.isSolidSide(worldIn, blockpos, Direction.UP) || blockstate.getBlock() == Blocks.HOPPER;
            if (flag && canConnectTo(worldIn.getBlockState(blockpos.up()), worldIn, blockpos.up(), null))
            {
                if (blockstate.isCollisionShapeOpaque(worldIn, blockpos))
                {
                    return RedstoneSide.UP;
                }

                return RedstoneSide.SIDE;
            }
        }

        return !canConnectTo(blockstate, worldIn, blockpos, face) && (blockstate.isNormalCube(worldIn, blockpos) || !canConnectTo(worldIn.getBlockState(blockpos.down()), worldIn, blockpos.down(), null)) ? RedstoneSide.NONE : RedstoneSide.SIDE;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        BlockPos blockpos = pos.down();
        BlockState blockstate = worldIn.getBlockState(blockpos);
        return blockstate.isSolidSide(worldIn, blockpos, Direction.UP) || blockstate.getBlock() == Blocks.HOPPER;
    }

    private BlockState updateSurroundingRedstone(World worldIn, BlockPos pos, BlockState state)
    {
        state = this.func_212568_b(worldIn, pos, state);
        List<BlockPos> list = Lists.newArrayList(this.blocksNeedingUpdate);
        this.blocksNeedingUpdate.clear();

        for (BlockPos blockpos : list)
        {
            worldIn.notifyNeighborsOfStateChange(blockpos, this);
        }

        return state;
    }

    private BlockState func_212568_b(World p_212568_1_, BlockPos p_212568_2_, BlockState p_212568_3_)
    {
        BlockState blockstate = p_212568_3_;
        int i = p_212568_3_.get(POWER);
        this.canProvidePower = false;
        int j = p_212568_1_.getRedstonePowerFromNeighbors(p_212568_2_);
        this.canProvidePower = true;
        int k = 0;
        if (j < 15)
        {
            for (Direction direction : Direction.Plane.HORIZONTAL)
            {
                BlockPos blockpos = p_212568_2_.offset(direction);
                BlockState blockstate1 = p_212568_1_.getBlockState(blockpos);
                k = this.maxSignal(k, blockstate1);
                BlockPos blockpos1 = p_212568_2_.up();
                if (blockstate1.isNormalCube(p_212568_1_, blockpos) && !p_212568_1_.getBlockState(blockpos1).isNormalCube(p_212568_1_, blockpos1))
                {
                    k = this.maxSignal(k, p_212568_1_.getBlockState(blockpos.up()));
                }
                else if (!blockstate1.isNormalCube(p_212568_1_, blockpos))
                {
                    k = this.maxSignal(k, p_212568_1_.getBlockState(blockpos.down()));
                }
            }
        }

        int l = k - 1;
        if (j > l)
        {
            l = j;
        }

        if (i != l)
        {
            p_212568_3_ = p_212568_3_.with(POWER, Integer.valueOf(l));
            if (p_212568_1_.getBlockState(p_212568_2_) == blockstate)
            {
                p_212568_1_.setBlockState(p_212568_2_, p_212568_3_, 2);
            }

            this.blocksNeedingUpdate.add(p_212568_2_);

            for (Direction direction1 : Direction.values())
            {
                this.blocksNeedingUpdate.add(p_212568_2_.offset(direction1));
            }
        }

        return p_212568_3_;
    }

    private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos)
    {
        if (worldIn.getBlockState(pos).getBlock() == this)
        {
            worldIn.notifyNeighborsOfStateChange(pos, this);

            for (Direction direction : Direction.values())
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(direction), this);
            }

        }
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        if (oldState.getBlock() != state.getBlock() && !worldIn.isRemote)
        {
            this.updateSurroundingRedstone(worldIn, pos, state);

            for (Direction direction : Direction.Plane.VERTICAL)
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(direction), this);
            }

            for (Direction direction1 : Direction.Plane.HORIZONTAL)
            {
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(direction1));
            }

            for (Direction direction2 : Direction.Plane.HORIZONTAL)
            {
                BlockPos blockpos = pos.offset(direction2);
                if (worldIn.getBlockState(blockpos).isNormalCube(worldIn, blockpos))
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
                }
                else
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
                }
            }

        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (!isMoving && state.getBlock() != newState.getBlock())
        {
            super.onReplaced(state, worldIn, pos, newState, isMoving);
            if (!worldIn.isRemote)
            {
                for (Direction direction : Direction.values())
                {
                    worldIn.notifyNeighborsOfStateChange(pos.offset(direction), this);
                }

                this.updateSurroundingRedstone(worldIn, pos, state);

                for (Direction direction1 : Direction.Plane.HORIZONTAL)
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(direction1));
                }

                for (Direction direction2 : Direction.Plane.HORIZONTAL)
                {
                    BlockPos blockpos = pos.offset(direction2);
                    if (worldIn.getBlockState(blockpos).isNormalCube(worldIn, blockpos))
                    {
                        this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
                    }
                    else
                    {
                        this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
                    }
                }

            }
        }
    }

    private int maxSignal(int existingSignal, BlockState neighbor)
    {
        if (neighbor.getBlock() != this)
        {
            return existingSignal;
        }
        else
        {
            int i = neighbor.get(POWER);
            return i > existingSignal ? i : existingSignal;
        }
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        if (!worldIn.isRemote)
        {
            if (state.isValidPosition(worldIn, pos))
            {
                this.updateSurroundingRedstone(worldIn, pos, state);
            }
            else
            {
                spawnDrops(state, worldIn, pos);
                worldIn.removeBlock(pos, false);
            }

        }
    }

    @Override
    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
    {
        return !this.canProvidePower ? 0 : blockState.getWeakPower(blockAccess, pos, side);
    }

    @Override
    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
    {
        if (!this.canProvidePower)
        {
            return 0;
        }
        else
        {
            int i = blockState.get(POWER);
            if (i == 0)
            {
                return 0;
            }
            else if (side == Direction.UP)
            {
                return i;
            }
            else
            {
                EnumSet<Direction> enumset = EnumSet.noneOf(Direction.class);

                for (Direction direction : Direction.Plane.HORIZONTAL)
                {
                    if (this.isPowerSourceAt(blockAccess, pos, direction))
                    {
                        enumset.add(direction);
                    }
                }

                if (side.getAxis().isHorizontal() && enumset.isEmpty())
                {
                    return i;
                }
                else if (enumset.contains(side) && !enumset.contains(side.rotateYCCW()) && !enumset.contains(side.rotateY()))
                {
                    return i;
                }
                else
                {
                    return 0;
                }
            }
        }
    }

    private boolean isPowerSourceAt(IBlockReader worldIn, BlockPos pos, Direction side)
    {
        BlockPos blockpos = pos.offset(side);
        BlockState blockstate = worldIn.getBlockState(blockpos);
        boolean flag = blockstate.isNormalCube(worldIn, blockpos);
        BlockPos blockpos1 = pos.up();
        boolean flag1 = worldIn.getBlockState(blockpos1).isNormalCube(worldIn, blockpos1);
        if (!flag1 && flag && canConnectTo(worldIn.getBlockState(blockpos.up()), worldIn, blockpos.up(), null))
        {
            return true;
        }
        else if (canConnectTo(blockstate, worldIn, blockpos, side))
        {
            return true;
        }
        else if (blockstate.getBlock() == Blocks.REPEATER && blockstate.get(RedstoneDiodeBlock.POWERED) && blockstate.get(RedstoneDiodeBlock.HORIZONTAL_FACING) == side)
        {
            return true;
        }
        else
        {
            return !flag && canConnectTo(worldIn.getBlockState(blockpos.down()), worldIn, blockpos.down(), null);
        }
    }

    @Override
    public boolean canProvidePower(BlockState state)
    {
        return this.canProvidePower;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        int i = stateIn.get(POWER);
        if (i != 0)
        {
            double d0 = (double) pos.getX() + 0.5D + ((double) rand.nextFloat() - 0.5D) * 0.2D;
            double d1 = (float) pos.getY() + 0.0625F;
            double d2 = (double) pos.getZ() + 0.5D + ((double) rand.nextFloat() - 0.5D) * 0.2D;
            float f = (float) i / 15.0F;
            float f1 = f * 0.6F + 0.4F;
            float f2 = Math.max(0.0F, f * f * 0.7F - 0.5F);
            float f3 = Math.max(0.0F, f * f * 0.6F - 0.7F);
            worldIn.addParticle(new RedstoneParticleData(f1, f2, f3, 1.0F), d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }

//    @Override
//    public BlockRenderLayer getRenderLayer()
//    {
//        return BlockRenderLayer.CUTOUT;
//    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.DECORATION;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(POWER);
    }
}