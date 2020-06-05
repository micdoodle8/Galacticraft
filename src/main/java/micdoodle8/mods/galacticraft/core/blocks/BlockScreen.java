//package micdoodle8.mods.galacticraft.core.blocks;
//
//import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityScreen;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.ITileEntityProvider;
//import net.minecraft.block.state.BlockFaceShape;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.item.ItemStack;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.Direction;
//import net.minecraft.util.Hand;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.World;
//
//public class BlockScreen extends BlockAdvanced implements IShiftDescription, IPartialSealableBlock, ITileEntityProvider, ISortableBlock
//{
//    public static final DirectionProperty FACING = DirectionProperty.create("facing");
//    public static final BooleanProperty LEFT = BooleanProperty.create("left");
//    public static final BooleanProperty RIGHT = BooleanProperty.create("right");
//    public static final BooleanProperty UP = BooleanProperty.create("up");
//    public static final BooleanProperty DOWN = BooleanProperty.create("down");
//
//    protected static final float boundsFront = 0.094F;
//    protected static final float boundsBack = 1.0F - boundsFront;
//    protected static final VoxelShape DOWN_AABB = Block.makeCuboidShape(0F, 0F, 0F, 1.0F, boundsBack, 1.0F);
//    protected static final VoxelShape UP_AABB = Block.makeCuboidShape(0F, boundsFront, 0F, 1.0F, 1.0F, 1.0F);
//    protected static final VoxelShape NORTH_AABB = Block.makeCuboidShape(0F, 0F, boundsFront, 1.0F, 1.0F, 1.0F);
//    protected static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(0F, 0F, 0F, 1.0F, 1.0F, boundsBack);
//    protected static final VoxelShape WEST_AABB = Block.makeCuboidShape(boundsFront, 0F, 0F, 1.0F, 1.0F, 1.0F);
//    protected static final VoxelShape EAST_AABB = Block.makeCuboidShape(0F, 0F, 0F, boundsBack, 1.0F, 1.0F);
//
//    //Metadata: 0-5 = direction of screen back;  bit 3 = reserved for future use
//    public BlockScreen(Properties builder)
//    {
//        super(builder);
//        this.setDefaultState(stateContainer.getBaseState().with(FACING, Direction.NORTH).with(LEFT, false).with(RIGHT, false).with(UP, false).with(DOWN, false));
//    }
//
//    @Override
//    public boolean isSideSolid(BlockState base_state, IBlockReader world, BlockPos pos, Direction direction)
//    {
//        return direction.ordinal() != getMetaFromState(world.getBlockState(pos));
//    }
//
//    @Override
//    public void breakBlock(World worldIn, BlockPos pos, BlockState state)
//    {
//        ((TileEntityScreen) worldIn.getTileEntity(pos)).breakScreen(state);
//        super.breakBlock(worldIn, pos, state);
//    }
//
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
//        return face.ordinal() == getMetaFromState(state) ? BlockFaceShape.UNDEFINED : BlockFaceShape.BOWL;
//    }
//
//    @Override
//    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
//    {
//        final int angle = MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
//        int change = Direction.getHorizontal(angle).getOpposite().getIndex();
//        worldIn.setBlockState(pos, getStateFromMeta(change), 3);
//    }
//
//    @Override
//    public boolean onUseWrench(World world, BlockPos pos, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
//    {
//        int change = world.getBlockState(pos).getValue(FACING).rotateY().getIndex();
//        world.setBlockState(pos, this.getStateFromMeta(change), 3);
//        return true;
//    }
//
//    @Override
//    public TileEntity createTileEntity(BlockState state, IBlockReader world)
//    {
//        return new TileEntityScreen();
//    }
//
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }
//
//    @Override
//    public boolean onMachineActivated(World world, BlockPos pos, BlockState state, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
//    {
//        TileEntity tile = world.getTileEntity(pos);
//        if (tile instanceof TileEntityScreen)
//        {
//            ((TileEntityScreen) tile).changeChannel();
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
//    {
//        TileEntity tile = worldIn.getTileEntity(pos);
//        if (tile instanceof TileEntityScreen)
//        {
//            ((TileEntityScreen) tile).refreshConnections(true);
//        }
//    }
//
//    @Override
//    public String getShiftDescription(ItemStack stack)
//    {
//        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
//    }
//
//    @Override
//    public boolean showDescription(ItemStack stack)
//    {
//        return true;
//    }
//
//    @Override
//    public boolean isSealed(World worldIn, BlockPos pos, Direction direction)
//    {
//        return true;
//    }
//
//    @Override
//    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
//    {
//        switch (state.get(FACING))
//        {
//        case EAST:
//            return EAST_AABB;
//        case WEST:
//            return WEST_AABB;
//        case SOUTH:
//            return SOUTH_AABB;
//        case NORTH:
//            return NORTH_AABB;
//        case DOWN:
//            return DOWN_AABB;
//        case UP:
//        default:
//            return UP_AABB;
//        }
//    }
//
//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        Direction enumfacing = Direction.byIndex(meta);
//        return this.getDefaultState().with(FACING, enumfacing);
//    }
//
//    @Override
//    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
//    {
//        builder.add(FACING, LEFT, RIGHT, UP, DOWN);
//    }
//
//    @Override
//    public BlockState getActualState(BlockState state, IBlockReader worldIn, BlockPos pos)
//    {
//        TileEntityScreen screen = (TileEntityScreen) worldIn.getTileEntity(pos);
//        return state.with(LEFT, screen.connectedLeft)
//                .with(RIGHT, screen.connectedRight)
//                .with(UP, screen.connectedUp)
//                .with(DOWN, screen.connectedDown);
//    }
//
//    @Override
//    public EnumSortCategoryBlock getCategory(int meta)
//    {
//        return EnumSortCategoryBlock.MACHINE;
//    }
//}
