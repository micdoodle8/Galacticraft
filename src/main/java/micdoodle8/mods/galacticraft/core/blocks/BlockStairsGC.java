//package micdoodle8.mods.galacticraft.core.blocks;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.StairsBlock;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.util.Direction;
//import net.minecraft.util.Mirror;
//import net.minecraft.util.Rotation;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IBlockReader;
//
//public class BlockStairsGC extends StairsBlock implements ISortable
//{
//    public BlockStairsGC(String name, BlockState state)
//    {
//        super(state);
//        this.setUnlocalizedName(name);
//        this.useNeighborBrightness = true;
//    }
//
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }
//
//    @Override
//    public EnumSortCategory getCategory()
//    {
//        return EnumSortCategory.STAIRS;
//    }
//
//    //Override the interaction with StructuredComponent to do nothing here, otherwise messes up our Abandoned Base generation code merged from 1.8.9
//    @Override
//    public BlockState withRotation(BlockState state, Rotation rot)
//    {
//        return state;
//    }
//
//    //Override the interaction with StructuredComponent to do nothing here, otherwise messes up our Abandoned Base generation code merged from 1.8.9
//    @Override
//    public BlockState withMirror(BlockState state, Mirror mirrorIn)
//    {
//        return state;
//    }
//
//    @Override
//    public BlockState getActualState(BlockState state, IBlockReader worldIn, BlockPos pos)
//    {
//        return state.with(SHAPE, getStairsShape(state, worldIn, pos));
//    }
//
//    //Correct bugged version in vanilla Minecraft
//    private static StairsBlock.EnumShape getStairsShape(BlockState state, IBlockReader worldIn, BlockPos pos)
//    {
//        Direction enumfacing = (Direction)state.get(FACING);
//        BlockState iblockstate = worldIn.getBlockState(pos.offset(enumfacing));
//        boolean top = state.get(HALF) == EnumHalf.TOP;
//
//        if (isBlockStairs(iblockstate) && state.get(HALF) == iblockstate.get(HALF))
//        {
//            Direction enumfacing1 = (Direction)iblockstate.get(FACING);
//
//            if (enumfacing1.getAxis() != ((Direction)state.get(FACING)).getAxis() && isDifferentStairs(state, worldIn, pos, enumfacing1.getOpposite()))
//            {
//                if (enumfacing1 == (top ? enumfacing.rotateY() : enumfacing.rotateYCCW()))
//                {
//                    return StairsBlock.EnumShape.OUTER_LEFT;
//                }
//
//                return StairsBlock.EnumShape.OUTER_RIGHT;
//            }
//        }
//
//        BlockState iblockstate1 = worldIn.getBlockState(pos.offset(enumfacing.getOpposite()));
//
//        if (isBlockStairs(iblockstate1) && state.get(HALF) == iblockstate1.getValue(HALF))
//        {
//            Direction enumfacing2 = (Direction)iblockstate1.getValue(FACING);
//
//            if (enumfacing2.getAxis() != ((Direction)state.get(FACING)).getAxis() && isDifferentStairs(state, worldIn, pos, enumfacing2))
//            {
//                if (enumfacing2 == (top ? enumfacing.rotateY() : enumfacing.rotateYCCW()))
//                {
//                    return StairsBlock.EnumShape.INNER_LEFT;
//                }
//
//                return StairsBlock.EnumShape.INNER_RIGHT;
//            }
//        }
//
//        return StairsBlock.EnumShape.STRAIGHT;
//    }
//
//    private static boolean isDifferentStairs(BlockState state, IBlockReader worldIn, BlockPos pos, Direction side)
//    {
//        BlockState iblockstate = worldIn.getBlockState(pos.offset(LogicalSide));
//        return !isBlockStairs(iblockstate) || iblockstate.get(FACING) != state.get(FACING) || iblockstate.get(HALF) != state.get(HALF);
//    }
//}