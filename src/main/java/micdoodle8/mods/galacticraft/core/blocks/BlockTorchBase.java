package micdoodle8.mods.galacticraft.core.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockTorchBase extends Block
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", facing -> facing != EnumFacing.DOWN);

    public BlockTorchBase(Material materialIn)
    {
        super(materialIn);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        if (this.canPlaceAt(worldIn, pos, facing))
        {
            return this.getDefaultState().withProperty(FACING, facing);
        }
        else
        {
            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
            {
                if (worldIn.isSideSolid(pos.offset(enumfacing.getOpposite()), enumfacing, true))
                {
                    return this.getDefaultState().withProperty(FACING, enumfacing);
                }
            }

            return this.getDefaultState();
        }
    }

    protected boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing)
    {
        BlockPos blockpos = pos.offset(facing.getOpposite());
        boolean flag = facing.getAxis().isHorizontal();
        return flag && worldIn.isSideSolid(blockpos, facing, true) || facing.equals(EnumFacing.UP) && this.canPlaceOn(worldIn, blockpos);
    }

    protected boolean canPlaceOn(World worldIn, BlockPos pos)
    {
        if (World.doesBlockHaveSolidTopSurface(worldIn, pos))
        {
            return true;
        }
        else
        {
            Block block = worldIn.getBlockState(pos).getBlock();
            return block.canPlaceTorchOnTop(worldIn, pos);
        }
    }

    protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getBlock() == this && this.canPlaceAt(worldIn, pos, (EnumFacing) state.getValue(FACING)))
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
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = this.getDefaultState();

        switch (meta)
        {
        case 1:
            iblockstate = iblockstate.withProperty(FACING, EnumFacing.EAST);
            break;
        case 2:
            iblockstate = iblockstate.withProperty(FACING, EnumFacing.WEST);
            break;
        case 3:
            iblockstate = iblockstate.withProperty(FACING, EnumFacing.SOUTH);
            break;
        case 4:
            iblockstate = iblockstate.withProperty(FACING, EnumFacing.NORTH);
            break;
        case 5:
        default:
            iblockstate = iblockstate.withProperty(FACING, EnumFacing.UP);
        }

        return iblockstate;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;

        switch ((EnumFacing) state.getValue(FACING))
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
}
