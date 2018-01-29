package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockStairsGC extends BlockStairs implements ISortableBlock
{
    public BlockStairsGC(String name, IBlockState state)
    {
        super(state);
        this.setUnlocalizedName(name);
        this.useNeighborBrightness = true;
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
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
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state;
    }

    //Override the interaction with StructuredComponent to do nothing here, otherwise messes up our Abandoned Base generation code merged from 1.8.9
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state;
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(SHAPE, getStairsShape(state, worldIn, pos));
    }

    //Correct bugged version in vanilla Minecraft
    private static BlockStairs.EnumShape getStairsShape(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
        IBlockState iblockstate = worldIn.getBlockState(pos.offset(enumfacing));
        boolean top = state.getValue(HALF) == EnumHalf.TOP;

        if (isBlockStairs(iblockstate) && state.getValue(HALF) == iblockstate.getValue(HALF))
        {
            EnumFacing enumfacing1 = (EnumFacing)iblockstate.getValue(FACING);

            if (enumfacing1.getAxis() != ((EnumFacing)state.getValue(FACING)).getAxis() && isDifferentStairs(state, worldIn, pos, enumfacing1.getOpposite()))
            {
                if (enumfacing1 == (top ? enumfacing.rotateY() : enumfacing.rotateYCCW()))
                {
                    return BlockStairs.EnumShape.OUTER_LEFT;
                }

                return BlockStairs.EnumShape.OUTER_RIGHT;
            }
        }

        IBlockState iblockstate1 = worldIn.getBlockState(pos.offset(enumfacing.getOpposite()));

        if (isBlockStairs(iblockstate1) && state.getValue(HALF) == iblockstate1.getValue(HALF))
        {
            EnumFacing enumfacing2 = (EnumFacing)iblockstate1.getValue(FACING);

            if (enumfacing2.getAxis() != ((EnumFacing)state.getValue(FACING)).getAxis() && isDifferentStairs(state, worldIn, pos, enumfacing2))
            {
                if (enumfacing2 == (top ? enumfacing.rotateY() : enumfacing.rotateYCCW()))
                {
                    return BlockStairs.EnumShape.INNER_LEFT;
                }

                return BlockStairs.EnumShape.INNER_RIGHT;
            }
        }

        return BlockStairs.EnumShape.STRAIGHT;
    }
    
    private static boolean isDifferentStairs(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos.offset(side));
        return !isBlockStairs(iblockstate) || iblockstate.getValue(FACING) != state.getValue(FACING) || iblockstate.getValue(HALF) != state.getValue(HALF);
    }
}