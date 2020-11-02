package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockLaunchController extends BlockTileGC implements IShiftDescription, IPartialSealableBlock, ISortable
{
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BlockLaunchController(Properties builder)
    {
        super(builder);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
//        int metadata = getMetaFromState(state);

//        final int angle = MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
//        int change = Direction.byHorizontalIndex(angle).getOpposite().getHorizontalIndex();

//        worldIn.setBlockState(pos, state.with(FACING, placer.getHorizontalFacing().getOpposite()), 3);

        WorldUtil.markAdjacentPadForUpdate(worldIn, pos);
        TileEntity var8 = worldIn.getTileEntity(pos);
//        if (var8 instanceof IChunkLoader && !worldIn.isRemote && ConfigManagerPlanets.launchControllerChunkLoad && placer instanceof PlayerEntity)
//        {
//            ((IChunkLoader) var8).setOwnerName(placer.getName());
//            ((IChunkLoader) var8).onTicketLoaded(ForgeChunkManager.requestTicket(GalacticraftCore.instance, var8.getWorld(), Type.NORMAL), true);
//        } TODO Chunkloading
//        else if (var8 instanceof TileEntityLaunchController && placer instanceof PlayerEntity)
//        {
//            ((TileEntityLaunchController) var8).setOwnerName(placer.getName());
//        }
        ((TileEntityLaunchController) var8).setOwnerUUID(placer.getUniqueID());
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public ActionResultType onMachineActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
//        playerIn.openGui(GalacticraftPlanets.instance, GuiIdsPlanets.MACHINE_MARS, worldIn, pos.getX(), pos.getY(), pos.getZ()); TODO guis
        return ActionResultType.SUCCESS;
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

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityLaunchController();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, IFluidState fluid)
    {
        WorldUtil.markAdjacentPadForUpdate(world, pos);
        return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
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

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public boolean shouldSideBeRendered(BlockState blockState, IWorldReader blockAccess, BlockPos pos, Direction side)
//    {
//        return true;
//    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        Direction enumfacing = Direction.byHorizontalIndex(meta % 4);
//        EnumMachineType type = EnumMachineType.byMetadata((int) Math.floor(meta / 4.0));
//        return this.getDefaultState().with(FACING, enumfacing).with(TYPE, type);
//    }

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

    @Override
    public boolean isSealed(World world, BlockPos pos, Direction direction)
    {
        return true;
    }
}
