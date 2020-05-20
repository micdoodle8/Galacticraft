package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockStairsGC extends StairsBlock implements ISortableBlock
{
    public BlockStairsGC(String name, BlockState state)
    {
        super(state);
        this.setUnlocalizedName(name);
        this.useNeighborBrightness = true;
    }

    @Override
    public ItemGroup getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.STAIRS;
    }
    
    //Override the interaction with StructuredComponent to do nothing here, otherwise messes up our Abandoned Base generation code merged from 1.8.9
    @Override
    public BlockState withRotation(BlockState state, Rotation rot)
    {
        return state;
    }

    //Override the interaction with StructuredComponent to do nothing here, otherwise messes up our Abandoned Base generation code merged from 1.8.9
    @Override
    public BlockState withMirror(BlockState state, Mirror mirrorIn)
    {
        return state;
    }
    
    @Override
    public BlockState getActualState(BlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(SHAPE, getStairsShape(state, worldIn, pos));
    }

    //Correct bugged version in vanilla Minecraft
    private static StairsBlock.EnumShape getStairsShape(BlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        Direction enumfacing = (Direction)state.getValue(FACING);
        BlockState iblockstate = worldIn.getBlockState(pos.offset(enumfacing));
        boolean top = state.getValue(HALF) == EnumHalf.TOP;

        if (isBlockStairs(iblockstate) && state.getValue(HALF) == iblockstate.getValue(HALF))
        {
            Direction enumfacing1 = (Direction)iblockstate.getValue(FACING);

            if (enumfacing1.getAxis() != ((Direction)state.getValue(FACING)).getAxis() && isDifferentStairs(state, worldIn, pos, enumfacing1.getOpposite()))
            {
                if (enumfacing1 == (top ? enumfacing.rotateY() : enumfacing.rotateYCCW()))
                {
                    return StairsBlock.EnumShape.OUTER_LEFT;
                }

                return StairsBlock.EnumShape.OUTER_RIGHT;
            }
        }

        BlockState iblockstate1 = worldIn.getBlockState(pos.offset(enumfacing.getOpposite()));

        if (isBlockStairs(iblockstate1) && state.getValue(HALF) == iblockstate1.getValue(HALF))
        {
            Direction enumfacing2 = (Direction)iblockstate1.getValue(FACING);

            if (enumfacing2.getAxis() != ((Direction)state.getValue(FACING)).getAxis() && isDifferentStairs(state, worldIn, pos, enumfacing2))
            {
                if (enumfacing2 == (top ? enumfacing.rotateY() : enumfacing.rotateYCCW()))
                {
                    return StairsBlock.EnumShape.INNER_LEFT;
                }

                return StairsBlock.EnumShape.INNER_RIGHT;
            }
        }

        return StairsBlock.EnumShape.STRAIGHT;
    }
    
    private static boolean isDifferentStairs(BlockState state, IBlockAccess worldIn, BlockPos pos, Direction side)
    {
        BlockState iblockstate = worldIn.getBlockState(pos.offset(side));
        return !isBlockStairs(iblockstate) || iblockstate.getValue(FACING) != state.getValue(FACING) || iblockstate.getValue(HALF) != state.getValue(HALF);
    }
}