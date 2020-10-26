package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFluidTank;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class BlockFluidTank extends Block implements IShiftDescription, ISortable
{
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    private static final VoxelShape BOUNDS = VoxelShapes.create(0.05F, 0.0F, 0.05F, 0.95F, 1.0F, 0.95F);

    public BlockFluidTank(Properties builder)
    {
        super(builder);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return BOUNDS;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileEntityFluidTank)
        {
            TileEntityFluidTank tank = (TileEntityFluidTank) tile;
//            tank.onBreak(); TODO Spill event needed?
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }
//
//    @Override
//    public boolean isOpaqueCube(BlockState state)
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
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
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
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(UP, DOWN);
    }

//    @Override
//    public BlockState getActualState(BlockState state, IBlockReader worldIn, BlockPos pos)
//    {
//        BlockState stateAbove = worldIn.getBlockState(pos.up());
//        BlockState stateBelow = worldIn.getBlockState(pos.down());
//        return state.with(UP, stateAbove.getBlock() == this).with(DOWN, stateBelow.getBlock() == this);
//    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityFluidTank();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

//    @Override
//    public void setBlockBoundsBasedOnState(IBlockReader worldIn, BlockPos pos)
//    {
//        this.setBlockBounds((float) BOUNDS.minX, (float) BOUNDS.minY, (float) BOUNDS.minZ, (float) BOUNDS.maxX, (float) BOUNDS.maxY, (float) BOUNDS.maxZ);
//    }
//
//    @Override
//    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//    }
//
//    @Override
//    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockReader worldIn, BlockPos pos)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        return super.getCollisionBoundingBox(worldIn, pos, state);
//    }
//
//    @Override
//    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        return super.getSelectedBoundingBox(worldIn, pos);
//    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit)
    {
        if (super.onBlockActivated(state, worldIn, pos, playerIn, hand, hit) == ActionResultType.SUCCESS)
        {
            return ActionResultType.SUCCESS;
        }

        if (hand == Hand.OFF_HAND)
        {
        	return ActionResultType.PASS;
        }

        ItemStack current = playerIn.inventory.getCurrentItem();
        int slot = playerIn.inventory.currentItem;

        if (!current.isEmpty())
        {
            TileEntity tile = worldIn.getTileEntity(pos);

            if (tile instanceof TileEntityFluidTank)
            {
                TileEntityFluidTank tank = (TileEntityFluidTank) tile;

                LazyOptional<IFluidHandler> holder = tank.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
                if (holder.isPresent())
                {
                    FluidActionResult forgeResult = FluidUtil.interactWithFluidHandler(current, holder.orElse(null), playerIn);
                    if (forgeResult.isSuccess())
                    {
                        playerIn.inventory.setInventorySlotContents(slot, forgeResult.result);
                        if (playerIn.container != null)
                        {
                            playerIn.container.detectAndSendChanges();
                        }
                        return ActionResultType.SUCCESS;
                    }
                }

                return ActionResultType.PASS;
            }
        }

        return ActionResultType.PASS;
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileEntityFluidTank)
        {
            TileEntityFluidTank tank = (TileEntityFluidTank) tile;
            return tank.fluidTank.getFluid() == FluidStack.EMPTY || tank.fluidTank.getFluid().getAmount() == 0 ? 0 : tank.fluidTank.getFluid().getFluid().getAttributes().getLuminosity(tank.fluidTank.getFluid());
        }

        return 0;
    }
}
