package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityArclamp;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.RedstoneUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;

public class BlockBrightLamp extends BlockAdvanced implements IShiftDescription, ISortableBlock
{
    public static final DirectionProperty FACING = DirectionProperty.create("facing");
//    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    protected static final VoxelShape DOWN_AABB = Block.makeCuboidShape(0.2F, 0.0F, 0.2F, 0.8F, 0.6F, 0.8F);
    protected static final VoxelShape UP_AABB = Block.makeCuboidShape(0.2F, 0.4F, 0.2F, 0.8F, 1.0F, 0.8F);
    protected static final VoxelShape NORTH_AABB = Block.makeCuboidShape(0.2F, 0.2F, 0.0F, 0.8F, 0.8F, 0.6F);
    protected static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(0.2F, 0.2F, 0.4F, 0.8F, 0.8F, 1.0F);
    protected static final VoxelShape WEST_AABB = Block.makeCuboidShape(0.0F, 0.2F, 0.2F, 0.6F, 0.8F, 0.8F);
    protected static final VoxelShape EAST_AABB = Block.makeCuboidShape(0.4F, 0.2F, 0.2F, 1.0F, 0.8F, 0.8F);

    //Metadata: bits 0-2 are the side of the base plate using standard side convention (0-5)

    public BlockBrightLamp(Properties builder)
    {
        super(builder);
        this.setDefaultState(stateContainer.getBaseState().with(FACING, Direction.UP));  //.with(ACTIVE, true));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        switch (state.get(FACING))
        {
        case EAST:
            return EAST_AABB;
        case WEST:
            return WEST_AABB;
        case SOUTH:
            return SOUTH_AABB;
        case NORTH:
            return NORTH_AABB;
        case DOWN:
            return DOWN_AABB;
        case UP:
        default:
            return UP_AABB;
        }
    }

    @Override
    public int getLightValue(BlockState state, IEnviromentBlockReader world, BlockPos pos)
    {
        Block block = state.getBlock();
        if (block != this)
        {
            return block.getLightValue(state);
        }
        /**
         * Gets the light value of the specified block coords. Args: x, y, z
         */

        if (world instanceof World)
        {
            return RedstoneUtil.isBlockReceivingRedstone((World) world, pos) ? 0 : this.lightValue;
        }

        return 0;
    }

    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return 1;
    }

//    @Override
//    public AxisAlignedBB getCollisionBoundingBox(BlockState blockState, IBlockAccess worldIn, BlockPos pos)
//    {
//        double boundsMin = 0.2D;
//        double boundsMax = 0.8D;
//        return Block.makeCuboidShape(pos.getX() + boundsMin, pos.getY() + boundsMin, pos.getZ() + boundsMin, pos.getX() + boundsMax, pos.getY() + boundsMax, pos.getZ() + boundsMax);
//    }

//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }
//
//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }

//    @Override
//    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
//    {
//        for (Direction side : Direction.VALUES)
//        {
//            BlockPos offsetPos = pos.offset(side);
//            BlockState state = worldIn.getBlockState(offsetPos);
//            if (state.getBlock().isSideSolid(state, worldIn, offsetPos, side.getOpposite()))
//            {
//                return true;
//            }
//        }
//        return false;
//    } TODO

//    @Override
//    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
//    {
//        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
//    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

//    @Override
//    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
//    {
//        Direction side = state.get(FACING);
//
//        BlockPos offsetPos = pos.offset(side);
//        BlockState state1 = worldIn.getBlockState(offsetPos);
//        if (state1.getBlock().isSideSolid(state1, worldIn, offsetPos, Direction.getFront(side.getIndex() ^ 1)))
//        {
//            return;
//        }
//
//        this.dropBlockAsItem(worldIn, pos, state, 0);
//        worldIn.removeBlock(pos, false);
//    } TODO

//    @Override
//    public RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end)
//    {
//        EnumFacing side = worldIn.getBlockState(pos).getValue(FACING);
//        float var8 = 0.3F;
//
//        if (side == EnumFacing.WEST)
//        {
//            this.setBlockBounds(0.0F, 0.2F, 0.5F - var8, var8 * 2.0F, 0.8F, 0.5F + var8);
//        }
//        else if (side == EnumFacing.EAST)
//        {
//            this.setBlockBounds(1.0F - var8 * 2.0F, 0.2F, 0.5F - var8, 1.0F, 0.8F, 0.5F + var8);
//        }
//        else if (side == EnumFacing.NORTH)
//        {
//            this.setBlockBounds(0.5F - var8, 0.2F, 0.0F, 0.5F + var8, 0.8F, var8 * 2.0F);
//        }
//        else if (side == EnumFacing.SOUTH)
//        {
//            this.setBlockBounds(0.5F - var8, 0.2F, 1.0F - var8 * 2.0F, 0.5F + var8, 0.8F, 1.0F);
//        }
//        else if (side == EnumFacing.DOWN)
//        {
//            this.setBlockBounds(0.5F - var8, 0.0F, 0.5F - var8, 0.5F + var8, 0.6F, 0.5F + var8);
//        }
//        else
//        {
//            this.setBlockBounds(0.5F - var8, 0.4F, 0.5F - var8, 0.5F + var8, 1.0F, 0.5F + var8);
//        }
//
//        return super.collisionRayTrace(worldIn, pos, start, end);
//    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        if (!world.isRemote)
        {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileEntityArclamp)
            {
                ((TileEntityArclamp) tile).facingChanged();
            }
        }
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityArclamp();
    }

    //    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public String getShiftDescription(int meta)
    {
        return GCCoreUtil.translate(this.getTranslationKey() + ".description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        Direction enumfacing = Direction.getFront(meta);
//        return this.getDefaultState().with(FACING, enumfacing);
//    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);  //, ACTIVE });
    }

//    @Override
//    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
//    {
//        return state.with(ACTIVE, ((TileEntityArclamp) worldIn.getTileEntity(pos)).getEnabled());
//    }
//
    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }
}
