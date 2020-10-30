package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.api.transmission.tile.IConductor;
import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.energy.tile.EnergyStorageTile;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReceiver;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class BlockBeamReceiver extends BlockTileGC implements IShiftDescription, ISortable
{
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    protected static final VoxelShape UP_AABB = VoxelShapes.create(0.3F, 0.3F, 0.3F, 0.7F, 1.0F, 0.7F);
    protected static final VoxelShape DOWN_AABB = VoxelShapes.create(0.2F, 0.0F, 0.2F, 0.8F, 0.42F, 0.8F);
    protected static final VoxelShape EAST_AABB = VoxelShapes.create(0.58F, 0.2F, 0.2F, 1.0F, 0.8F, 0.8F);
    protected static final VoxelShape WEST_AABB = VoxelShapes.create(0.0F, 0.2F, 0.2F, 0.42F, 0.8F, 0.8F);
    protected static final VoxelShape NORTH_AABB = VoxelShapes.create(0.2F, 0.2F, 0.0F, 0.8F, 0.8F, 0.42F);
    protected static final VoxelShape SOUTH_AABB = VoxelShapes.create(0.2F, 0.2F, 0.58F, 0.8F, 0.8F, 1.0F);

    public BlockBeamReceiver(Properties builder)
    {
        super(builder);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        switch (state.get(FACING))
        {
        case UP:
            return UP_AABB;
        case DOWN:
            return DOWN_AABB;
        case EAST:
            return EAST_AABB;
        case WEST:
            return WEST_AABB;
        case SOUTH:
            return SOUTH_AABB;
        default:
        case NORTH:
            return NORTH_AABB;
        }
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

//    @Override
//    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
//    {
//        int oldMeta = getMetaFromState(worldIn.getBlockState(pos));
//        int meta = this.getMetadataFromAngle(worldIn, pos, Direction.byIndex(oldMeta).getOpposite());
//
//        if (meta == -1)
//        {
//            worldIn.destroyBlock(pos, true);
//        }
//        else if (meta != oldMeta)
//        {
//            worldIn.setBlockState(pos, getStateFromMeta(meta), 3);
//            TileEntity thisTile = worldIn.getTileEntity(pos);
//            if (thisTile instanceof TileEntityBeamReceiver)
//            {
//                TileEntityBeamReceiver thisReceiver = (TileEntityBeamReceiver) thisTile;
//                thisReceiver.setFacing(Direction.byIndex(meta));
//                thisReceiver.invalidateReflector();
//                thisReceiver.initiateReflector();
//            }
//        }
//
//        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
//    } TODO ?

//    @Override
//    public void onBlockAdded(World world, BlockPos pos, BlockState state)
//    {
//        TileEntity thisTile = world.getTileEntity(pos);
//        if (thisTile instanceof TileEntityBeamReceiver)
//        {
//            ((TileEntityBeamReceiver) thisTile).setFacing(Direction.byIndex(getMetaFromState(state)));
//        }
//    } TODO ?

//    @Override
//    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
//    {
//        int meta = getMetaFromState(world.getBlockState(pos));
//
//        if (meta != -1)
//        {
//            EnumFacing dir = EnumFacing.getFront(meta);
//
//            switch (dir)
//            {
//            case UP:
//                this.setBlockBounds(0.3F, 0.3F, 0.3F, 0.7F, 1.0F, 0.7F);
//                break;
//            case DOWN:
//                this.setBlockBounds(0.2F, 0.0F, 0.2F, 0.8F, 0.42F, 0.8F);
//                break;
//            case EAST:
//                this.setBlockBounds(0.58F, 0.2F, 0.2F, 1.0F, 0.8F, 0.8F);
//                break;
//            case WEST:
//                this.setBlockBounds(0.0F, 0.2F, 0.2F, 0.42F, 0.8F, 0.8F);
//                break;
//            case NORTH:
//                this.setBlockBounds(0.2F, 0.2F, 0.0F, 0.8F, 0.8F, 0.42F);
//                break;
//            case SOUTH:
//                this.setBlockBounds(0.2F, 0.2F, 0.58F, 0.8F, 0.8F, 1.0F);
//                break;
//            default:
//                break;
//            }
//        }
//    }
//
//    @Override
//    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//    }

    private int getMetadataFromAngle(World world, BlockPos pos, Direction side)
    {
        Direction direction = side.getOpposite();

        TileEntity tileAt = world.getTileEntity(pos.add(direction.getXOffset(), direction.getYOffset(), direction.getZOffset()));

        if (tileAt instanceof EnergyStorageTile)
        {
            if (((EnergyStorageTile) tileAt).getModeFromDirection(direction.getOpposite()) != null)
            {
                return direction.ordinal();
            }
            else
            {
                return -1;
            }
        }

        if (EnergyUtil.otherModCanReceive(tileAt, direction.getOpposite()))
        {
            return direction.ordinal();
        }

        for (Direction adjacentDir : Direction.values())
        {
            if (adjacentDir == direction)
            {
                continue;
            }
            tileAt = world.getTileEntity(pos.add(adjacentDir.getXOffset(), adjacentDir.getYOffset(), adjacentDir.getZOffset()));

            if (tileAt instanceof IConductor)
            {
                continue;
            }

            if (tileAt instanceof EnergyStorageTile && ((EnergyStorageTile) tileAt).getModeFromDirection(adjacentDir.getOpposite()) != null)
            {
                return adjacentDir.ordinal();
            }

            if (EnergyUtil.otherModCanReceive(tileAt, adjacentDir.getOpposite()))
            {
                return adjacentDir.ordinal();
            }
        }

        return -1;
    }

//    @Override
//    public BlockState getStateForPlacement(BlockItemUseContext context)
//    {
//        return getStateFromMeta(this.getMetadataFromAngle(context.getWorld(), context.getPos(), context.getFace()));
//    }

//    @Override
//    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, Direction side)
//    {
//        if (this.getMetadataFromAngle(worldIn, pos, LogicalSide) != -1)
//        {
//            return true;
//        }
//
//        if (worldIn.isRemote)
//        {
//            this.sendIncorrectSideMessage();
//        }
//
//        return false;
//    }

    @OnlyIn(Dist.CLIENT)
    private void sendIncorrectSideMessage()
    {
        Minecraft.getInstance().player.sendMessage(new StringTextComponent(EnumColor.RED + GCCoreUtil.translate("gui.receiver.cannot_attach")));
    }

//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }

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

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.INVISIBLE;
    }

//    @Override
//    public int damageDropped(BlockState metadata)
//    {
//        return 0;
//    }


    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityBeamReceiver();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        list.add(new ItemStack(this, 1));
//    }

    @Override
    public ActionResultType onMachineActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof TileEntityBeamReceiver)
        {
            return ((TileEntityBeamReceiver) tile).onMachineActivated(worldIn, pos, state, playerIn, hand, heldItem, hit);
        }

        return ActionResultType.PASS;
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

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.MACHINE;
    }
}
