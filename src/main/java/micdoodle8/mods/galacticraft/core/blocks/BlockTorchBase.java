//package micdoodle8.mods.galacticraft.core.blocks;
//
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.Blocks;
//import net.minecraft.item.BlockItemUseContext;
//import net.minecraft.state.DirectionProperty;
//import net.minecraft.util.Direction;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.shapes.ISelectionContext;
//import net.minecraft.util.math.shapes.VoxelShape;
//import net.minecraft.util.math.shapes.VoxelShapes;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.IWorld;
//import net.minecraft.world.IWorldReader;
//
//import javax.annotation.Nullable;
//
//public class BlockTorchBase extends Block
//{
//    public static final DirectionProperty FACING = DirectionProperty.create("facing", facing -> facing != Direction.DOWN);
//    protected static final VoxelShape STANDING_AABB = VoxelShapes.create(0.4000000059604645D, 0.0D, 0.4000000059604645D, 0.6000000238418579D, 0.6000000238418579D, 0.6000000238418579D);
//    protected static final VoxelShape TORCH_NORTH_AABB = VoxelShapes.create(0.3499999940395355D, 0.20000000298023224D, 0.699999988079071D, 0.6499999761581421D, 0.800000011920929D, 1.0D);
//    protected static final VoxelShape TORCH_SOUTH_AABB = VoxelShapes.create(0.3499999940395355D, 0.20000000298023224D, 0.0D, 0.6499999761581421D, 0.800000011920929D, 0.30000001192092896D);
//    protected static final VoxelShape TORCH_WEST_AABB = VoxelShapes.create(0.699999988079071D, 0.20000000298023224D, 0.3499999940395355D, 1.0D, 0.800000011920929D, 0.6499999761581421D);
//    protected static final VoxelShape TORCH_EAST_AABB = VoxelShapes.create(0.0D, 0.20000000298023224D, 0.3499999940395355D, 0.30000001192092896D, 0.800000011920929D, 0.6499999761581421D);
//
//    public BlockTorchBase(Properties builder)
//    {
//        super(builder);
//    }
//
//    @Override
//    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
//    {
//        switch (state.get(FACING))
//        {
//        case EAST:
//            return TORCH_EAST_AABB;
//        case WEST:
//            return TORCH_WEST_AABB;
//        case SOUTH:
//            return TORCH_SOUTH_AABB;
//        case NORTH:
//            return TORCH_NORTH_AABB;
//        default:
//            return STANDING_AABB;
//        }
//    }
//
////    @Override
////    public AxisAlignedBB getCollisionBoundingBox(BlockState blockState, IBlockReader worldIn, BlockPos pos)
////    {
////        return NULL_AABB;
////    }
//
//    @Override
//    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
//    {
//        return facing == Direction.DOWN && !this.isValidPosition(stateIn, worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
//    }
//
//    @Override
//    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
//    {
//        return hasEnoughSolidSide(worldIn, pos.down(), Direction.UP);
//    }
//
//    @Override
//    @Nullable
//    public BlockState getStateForPlacement(BlockItemUseContext context) {
//        BlockState blockstate = this.getDefaultState();
//        IWorldReader iworldreader = context.getWorld();
//        BlockPos blockpos = context.getPos();
//        Direction[] adirection = context.getNearestLookingDirections();
//
//        for(Direction direction : adirection) {
//            if (direction.getAxis().isHorizontal()) {
//                Direction direction1 = direction.getOpposite();
//                blockstate = blockstate.with(FACING, direction1);
//                if (blockstate.isValidPosition(iworldreader, blockpos)) {
//                    return blockstate;
//                }
//            }
//        }
//
//        return null;
//    }
//}
