package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.tile.TileEntityPlayerDetector;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.RedstoneUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockConcealedDetector extends Block implements ISortable
{
    public static final IntegerProperty VARIANT = IntegerProperty.create("var", 0, 1);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty DETECTED = BooleanProperty.create("det");

    public BlockConcealedDetector(Properties builder)
    {
        super(builder);
        this.setDefaultState(stateContainer.getBaseState().with(VARIANT, 0).with(DETECTED, false));
    }

//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public EnumSortCategory getCategory()
    {
         return EnumSortCategory.DECORATION;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        int facing = meta & 3;
//        int var = (meta >> 2) & 1;
//        return this.getDefaultState().with(FACING, Integer.valueOf(facing)).with(VARIANT, Integer.valueOf(var)).with(DETECTED, Boolean.valueOf(meta >= 8));
//    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, VARIANT, DETECTED);
    }

//    @Override
//    public int getLightOpacity(BlockState state)
//    {
//        return 0;
//    }
//
//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return true;
//    }
//
//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return true;
//    }
//
//    @Override
//    public boolean isSideSolid(BlockState state, IBlockReader world, BlockPos pos, Direction side)
//    {
//        return true;
//    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityPlayerDetector();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public boolean canProvidePower(BlockState state)
    {
        return true;
    }

    @Override
    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
    {
        if (blockAccess instanceof World && RedstoneUtil.isBlockReceivingDirectRedstone((World) blockAccess, pos))
        {
            return 0;
        }

        return blockAccess.getBlockState(pos).get(DETECTED) ? 0 : 15;
    }

    public void updateState(World worldObj, BlockPos pos, boolean result)
    {
        BlockState bs = worldObj.getBlockState(pos);
        if (result != bs.get(DETECTED))
        {
            worldObj.setBlockState(pos, bs.with(DETECTED, result), 3);
        }
    }

//    @Override
//    public int quantityDropped(Random random)
//    {
//        return 0;
//    }
//
//    @Override
//    protected boolean canSilkHarvest()
//    {
//        return false;
//    }
}
