package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityArclamp;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.RedstoneUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBrightLamp extends BlockAdvanced implements IShiftDescription, ITileEntityProvider, ISortableBlock
{
    public static final DirectionProperty FACING = DirectionProperty.create("facing");
//    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.2F, 0.0F, 0.2F, 0.8F, 0.6F, 0.8F);
    protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.2F, 0.4F, 0.2F, 0.8F, 1.0F, 0.8F);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.2F, 0.2F, 0.0F, 0.8F, 0.8F, 0.6F);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.2F, 0.2F, 0.4F, 0.8F, 0.8F, 1.0F);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0F, 0.2F, 0.2F, 0.6F, 0.8F, 0.8F);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.4F, 0.2F, 0.2F, 1.0F, 0.8F, 0.8F);

    //Metadata: bits 0-2 are the side of the base plate using standard side convention (0-5)

    public BlockBrightLamp(Properties builder)
    {
        super(builder);
        this.setDefaultState(stateContainer.getBaseState().with(FACING, Direction.UP));  //.with(ACTIVE, true));
    }

    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos)
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
    public int getLightValue(BlockState state, IBlockAccess world, BlockPos pos)
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
    public int getLightOpacity(BlockState state, IBlockAccess world, BlockPos pos)
    {
        return 1;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(BlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        double boundsMin = 0.2D;
        double boundsMax = 0.8D;
        return new AxisAlignedBB(pos.getX() + boundsMin, pos.getY() + boundsMin, pos.getZ() + boundsMin, pos.getX() + boundsMax, pos.getY() + boundsMax, pos.getZ() + boundsMax);
    }

    @Override
    public boolean isOpaqueCube(BlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(BlockState state)
    {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        for (Direction side : Direction.VALUES)
        {
            BlockPos offsetPos = pos.offset(side);
            BlockState state = worldIn.getBlockState(offsetPos);
            if (state.getBlock().isSideSolid(state, worldIn, offsetPos, side.getOpposite()))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer, Hand hand)
    {
        Direction opposite = facing.getOpposite();
        BlockPos offsetPos = pos.offset(opposite);
        BlockState state = world.getBlockState(offsetPos);
        if (state.getBlock().isSideSolid(state, world, offsetPos, facing))
        {
            return this.getDefaultState().with(FACING, opposite);
        }

        return this.getDefaultState().with(FACING, facing);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which
     * neighbor changed (coordinates passed are their own) Args: x, y, z,
     * neighbor blockID
     */
    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        Direction side = state.get(FACING);

        BlockPos offsetPos = pos.offset(side);
        BlockState state1 = worldIn.getBlockState(offsetPos);
        if (state1.getBlock().isSideSolid(state1, worldIn, offsetPos, Direction.getFront(side.getIndex() ^ 1)))
        {
            return;
        }

        this.dropBlockAsItem(worldIn, pos, state, 0);
        worldIn.setBlockToAir(pos);
    }

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
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityArclamp();
    }

    @Override
    public ItemGroup getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    @Override
    public BlockState getStateFromMeta(int meta)
    {
        Direction enumfacing = Direction.getFront(meta);
        return this.getDefaultState().with(FACING, enumfacing);
    }

    @Override
    public int getMetaFromState(BlockState state)
    {
        return (state.get(FACING)).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);  //, ACTIVE });
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
