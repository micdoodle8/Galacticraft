package micdoodle8.mods.galacticraft.core.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTorchBase extends Block
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", facing -> facing != Direction.DOWN);
    protected static final AxisAlignedBB STANDING_AABB = new AxisAlignedBB(0.4000000059604645D, 0.0D, 0.4000000059604645D, 0.6000000238418579D, 0.6000000238418579D, 0.6000000238418579D);
    protected static final AxisAlignedBB TORCH_NORTH_AABB = new AxisAlignedBB(0.3499999940395355D, 0.20000000298023224D, 0.699999988079071D, 0.6499999761581421D, 0.800000011920929D, 1.0D);
    protected static final AxisAlignedBB TORCH_SOUTH_AABB = new AxisAlignedBB(0.3499999940395355D, 0.20000000298023224D, 0.0D, 0.6499999761581421D, 0.800000011920929D, 0.30000001192092896D);
    protected static final AxisAlignedBB TORCH_WEST_AABB = new AxisAlignedBB(0.699999988079071D, 0.20000000298023224D, 0.3499999940395355D, 1.0D, 0.800000011920929D, 0.6499999761581421D);
    protected static final AxisAlignedBB TORCH_EAST_AABB = new AxisAlignedBB(0.0D, 0.20000000298023224D, 0.3499999940395355D, 0.30000001192092896D, 0.800000011920929D, 0.6499999761581421D);

    public BlockTorchBase(Properties builder)
    {
        super(builder);
    }

    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos)
    {
        switch (state.getValue(FACING))
        {
        case EAST:
            return TORCH_EAST_AABB;
        case WEST:
            return TORCH_WEST_AABB;
        case SOUTH:
            return TORCH_SOUTH_AABB;
        case NORTH:
            return TORCH_NORTH_AABB;
        default:
            return STANDING_AABB;
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(BlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }

    @Override
    public BlockState getStateForPlacement(World worldIn, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer, Hand hand)
    {
        if (this.canPlaceAt(worldIn, pos, facing))
        {
            return this.getDefaultState().withProperty(FACING, facing);
        }
        else
        {
            for (Direction enumfacing : Direction.Plane.HORIZONTAL)
            {
                if (worldIn.isSideSolid(pos.offset(enumfacing.getOpposite()), enumfacing, true))
                {
                    return this.getDefaultState().withProperty(FACING, enumfacing);
                }
            }

            return this.getDefaultState();
        }
    }

    protected boolean canPlaceAt(World worldIn, BlockPos pos, Direction facing)
    {
        BlockPos blockpos = pos.offset(facing.getOpposite());
        boolean flag = facing.getAxis().isHorizontal();
        return flag && worldIn.isSideSolid(blockpos, facing, true) || facing.equals(Direction.UP) && this.canPlaceOn(worldIn, blockpos);
    }

    protected boolean canPlaceOn(World worldIn, BlockPos pos)
    {
        BlockState state = worldIn.getBlockState(pos);
        Block block = state.getBlock();
        if (block.isSideSolid(state, worldIn, pos, Direction.UP))
        {
            return true;
        }
        else
        {
            return block.canPlaceTorchOnTop(state, worldIn, pos);
        }
    }

    protected boolean checkForDrop(World worldIn, BlockPos pos, BlockState state)
    {
        if (state.getBlock() == this && this.canPlaceAt(worldIn, pos, (Direction) state.getValue(FACING)))
        {
            return true;
        }
        else
        {
            if (worldIn.getBlockState(pos).getBlock() == this)
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }

            return false;
        }
    }

    @Override
    public BlockState getStateFromMeta(int meta)
    {
        BlockState iblockstate = this.getDefaultState();

        switch (meta)
        {
        case 1:
            iblockstate = iblockstate.withProperty(FACING, Direction.EAST);
            break;
        case 2:
            iblockstate = iblockstate.withProperty(FACING, Direction.WEST);
            break;
        case 3:
            iblockstate = iblockstate.withProperty(FACING, Direction.SOUTH);
            break;
        case 4:
            iblockstate = iblockstate.withProperty(FACING, Direction.NORTH);
            break;
        case 5:
        default:
            iblockstate = iblockstate.withProperty(FACING, Direction.UP);
        }

        return iblockstate;
    }

    @Override
    public int getMetaFromState(BlockState state)
    {
        int i = 0;

        switch ((Direction) state.getValue(FACING))
        {
        case EAST:
            i = i | 1;
            break;
        case WEST:
            i = i | 2;
            break;
        case SOUTH:
            i = i | 3;
            break;
        case NORTH:
            i = i | 4;
            break;
        case DOWN:
        case UP:
        default:
            i = i | 5;
        }

        return i;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
    {
        return BlockFaceShape.UNDEFINED;
    }
}
