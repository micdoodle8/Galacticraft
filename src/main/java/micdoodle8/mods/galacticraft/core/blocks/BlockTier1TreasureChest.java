package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTier1TreasureChest extends Block implements IShiftDescription, ISortable
{
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    protected static final VoxelShape AABB = VoxelShapes.create(0.0625, 0.0, 0.0625, 0.9375, 0.875, 0.9375);

    public BlockTier1TreasureChest(Properties builder)
    {
        super(builder);
        this.setDefaultState(stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return AABB;
    }

//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
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
//
//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

//    @Override
//    public void setBlockBoundsBasedOnState(IBlockReader worldIn, BlockPos pos)
//    {
//        if (worldIn.getBlockState(pos.north()).getBlock() == this)
//        {
//            this.setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
//        }
//        else if (worldIn.getBlockState(pos.south()).getBlock() == this)
//        {
//            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
//        }
//        else if (worldIn.getBlockState(pos.west()).getBlock() == this)
//        {
//            this.setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
//        }
//        else if (worldIn.getBlockState(pos.east()).getBlock() == this)
//        {
//            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
//        }
//        else
//        {
//            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
//        }
//    }

//    @Override
//    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
//    {
////        this.checkForSurroundingChests(worldIn, pos, state);
////        Iterator iterator = Direction.Plane.HORIZONTAL.iterator();
////
////        while (iterator.hasNext())
////        {
////            Direction enumfacing = (Direction) iterator.next();
////            BlockPos blockpos1 = pos.offset(enumfacing);
////            BlockState iblockstate1 = worldIn.getBlockState(blockpos1);
////
////            if (iblockstate1.getBlock() == this)
////            {
////                this.checkForSurroundingChests(worldIn, blockpos1, iblockstate1);
////            }
////        }
//    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing());
    }

//    @Override
//    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
//    {
//        Direction enumfacing = Direction.byHorizontalIndex(MathHelper.floor((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite();
//        state = state.with(FACING, enumfacing);
//        BlockPos blockpos1 = pos.north();
//        BlockPos blockpos2 = pos.south();
//        BlockPos blockpos3 = pos.west();
//        BlockPos blockpos4 = pos.east();
//        boolean flag = this == worldIn.getBlockState(blockpos1).getBlock();
//        boolean flag1 = this == worldIn.getBlockState(blockpos2).getBlock();
//        boolean flag2 = this == worldIn.getBlockState(blockpos3).getBlock();
//        boolean flag3 = this == worldIn.getBlockState(blockpos4).getBlock();
//
//        if (!flag && !flag1 && !flag2 && !flag3)
//        {
//            worldIn.setBlockState(pos, state, 3);
//        }
//        else if (enumfacing.getAxis() == Direction.Axis.X && (flag || flag1))
//        {
//            if (flag)
//            {
//                worldIn.setBlockState(blockpos1, state, 3);
//            }
//            else
//            {
//                worldIn.setBlockState(blockpos2, state, 3);
//            }
//
//            worldIn.setBlockState(pos, state, 3);
//        }
//        else if (enumfacing.getAxis() == Direction.Axis.Z && (flag2 || flag3))
//        {
//            if (flag2)
//            {
//                worldIn.setBlockState(blockpos3, state, 3);
//            }
//            else
//            {
//                worldIn.setBlockState(blockpos4, state, 3);
//            }
//
//            worldIn.setBlockState(pos, state, 3);
//        }
//    }

//    @Override
//    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
//    {
//        int i = 0;
//        BlockPos blockpos1 = pos.west();
//        BlockPos blockpos2 = pos.east();
//        BlockPos blockpos3 = pos.north();
//        BlockPos blockpos4 = pos.south();
//
//        if (worldIn.getBlockState(blockpos1).getBlock() == this)
//        {
//            ++i;
//        }
//
//        if (worldIn.getBlockState(blockpos2).getBlock() == this)
//        {
//            ++i;
//        }
//
//        if (worldIn.getBlockState(blockpos3).getBlock() == this)
//        {
//            ++i;
//        }
//
//        if (worldIn.getBlockState(blockpos4).getBlock() == this)
//        {
//            ++i;
//        }
//
//        return i <= 1;
//    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityTreasureChest)
        {
            tileentity.updateContainingBlockInfo();
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof IInventory)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit)
    {
        if (!worldIn.isRemote)
        {
//            TileEntity tile = worldIn.getTileEntity(pos);
            NetworkHooks.openGui((ServerPlayerEntity) playerIn, getContainer(state, worldIn, pos), buf -> buf.writeBlockPos(pos));
//            playerIn.displayGUIChest((IInventory) tile);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityTreasureChest();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    public int isProvidingWeakPower(IBlockReader worldIn, BlockPos pos, BlockState state, Direction side)
    {
        if (!this.canProvidePower(state))
        {
            return 0;
        }
        else
        {
            int i = 0;
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityTreasureChest)
            {
                i = ((TileEntityTreasureChest) tileentity).numPlayersUsing;
            }

            return MathHelper.clamp(i, 0, 15);
        }
    }

    public int isProvidingStrongPower(IBlockReader worldIn, BlockPos pos, BlockState state, Direction side)
    {
        return side == Direction.UP ? this.isProvidingWeakPower(worldIn, pos, state, side) : 0;
    }

//    private boolean isBlocked(World worldIn, BlockPos pos)
//    {
//        return this.isBelowSolidBlock(worldIn, pos) || this.isOcelotSittingOnChest(worldIn, pos);
//    }

//    private boolean isBelowSolidBlock(World worldIn, BlockPos pos)
//    {
//        return worldIn.isSideSolid(pos.up(), Direction.DOWN, false);
//    }

    private boolean isOcelotSittingOnChest(World worldIn, BlockPos pos)
    {
        List<CatEntity> list = worldIn.getEntitiesWithinAABB(CatEntity.class, new AxisAlignedBB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1));
        if (!list.isEmpty())
        {
            for (CatEntity catentity : list)
            {
                if (catentity.isSitting())
                {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state)
    {
        return true;
    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        Direction enumfacing = Direction.byIndex(meta);
//
//        if (enumfacing.getAxis() == Direction.Axis.Y)
//        {
//            enumfacing = Direction.NORTH;
//        }
//
//        return this.getDefaultState().with(FACING, enumfacing);
//    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Override
    public String getShiftDescription(ItemStack stack)
    {
        return GCCoreUtil.translate(this.getTranslationKey() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return true;
    }

//    @Override
//    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager)
//    {
//        return false;
//    }
//
    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.TREASURE;
    }

    @Override
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }

    @Override
    @Nullable
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity instanceof INamedContainerProvider ? (INamedContainerProvider) tileentity : null;
    }
}