//package micdoodle8.mods.galacticraft.core.blocks;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.block.*;
//import net.minecraft.block.state.BlockFaceShape;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.client.particle.ParticleManager;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.passive.OcelotEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.inventory.IInventory;
//import net.minecraft.inventory.InventoryHelper;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.item.ItemStack;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.Direction;
//import net.minecraft.util.Hand;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.world.IBlockAccess;
//import net.minecraft.world.World;
//
//import java.util.Iterator;
//
//public class BlockTier1TreasureChest extends ContainerBlock implements ITileEntityProvider, IShiftDescription, ISortableBlock
//{
//    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
//    protected static final VoxelShape AABB = Block.makeCuboidShape(0.0625, 0.0, 0.0625, 0.9375, 0.875, 0.9375);
//
//    public BlockTier1TreasureChest(Properties builder)
//    {
//        super(builder);
//        this.setDefaultState(stateContainer.getBaseState().with(FACING, Direction.NORTH));
//    }
//
//    @Override
//    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
//    {
//        return AABB;
//    }
//
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
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
//    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }
//
//    @Override
//    public BlockRenderType getRenderType(BlockState state)
//    {
//        return BlockRenderType.ENTITYBLOCK_ANIMATED;
//    }
//
////    @Override
////    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
////    {
////        if (worldIn.getBlockState(pos.north()).getBlock() == this)
////        {
////            this.setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
////        }
////        else if (worldIn.getBlockState(pos.south()).getBlock() == this)
////        {
////            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
////        }
////        else if (worldIn.getBlockState(pos.west()).getBlock() == this)
////        {
////            this.setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
////        }
////        else if (worldIn.getBlockState(pos.east()).getBlock() == this)
////        {
////            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
////        }
////        else
////        {
////            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
////        }
////    }
//
//    @Override
//    public void onBlockAdded(World worldIn, BlockPos pos, BlockState state)
//    {
//        this.checkForSurroundingChests(worldIn, pos, state);
//        Iterator iterator = Direction.Plane.HORIZONTAL.iterator();
//
//        while (iterator.hasNext())
//        {
//            Direction enumfacing = (Direction) iterator.next();
//            BlockPos blockpos1 = pos.offset(enumfacing);
//            BlockState iblockstate1 = worldIn.getBlockState(blockpos1);
//
//            if (iblockstate1.getBlock() == this)
//            {
//                this.checkForSurroundingChests(worldIn, blockpos1, iblockstate1);
//            }
//        }
//    }
//
//    @Override
//    public BlockState getStateForPlacement(BlockItemUseContext context)
//    {
//        return this.getDefaultState().with(FACING, placer.getHorizontalFacing());
//    }
//
//    @Override
//    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
//    {
//        Direction enumfacing = Direction.getHorizontal(MathHelper.floor((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite();
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
//
//    public BlockState checkForSurroundingChests(World worldIn, BlockPos pos, BlockState state)
//    {
//        if (worldIn.isRemote)
//        {
//            return state;
//        }
//        else
//        {
//            BlockState iblockstate1 = worldIn.getBlockState(pos.north());
//            BlockState iblockstate2 = worldIn.getBlockState(pos.south());
//            BlockState iblockstate3 = worldIn.getBlockState(pos.west());
//            BlockState iblockstate4 = worldIn.getBlockState(pos.east());
//            Direction enumfacing = (Direction) state.get(FACING);
//            Block block = iblockstate1.getBlock();
//            Block block1 = iblockstate2.getBlock();
//            Block block2 = iblockstate3.getBlock();
//            Block block3 = iblockstate4.getBlock();
//
//            if (block != this && block1 != this)
//            {
//                boolean flag = block.isFullBlock(iblockstate1);
//                boolean flag1 = block1.isFullBlock(iblockstate2);
//
//                if (block2 == this || block3 == this)
//                {
//                    BlockPos blockpos2 = block2 == this ? pos.west() : pos.east();
//                    BlockState iblockstate7 = worldIn.getBlockState(blockpos2.north());
//                    BlockState iblockstate8 = worldIn.getBlockState(blockpos2.south());
//                    enumfacing = Direction.SOUTH;
//                    Direction enumfacing2;
//
//                    if (block2 == this)
//                    {
//                        enumfacing2 = (Direction) iblockstate3.getValue(FACING);
//                    }
//                    else
//                    {
//                        enumfacing2 = (Direction) iblockstate4.getValue(FACING);
//                    }
//
//                    if (enumfacing2 == Direction.NORTH)
//                    {
//                        enumfacing = Direction.NORTH;
//                    }
//
//                    Block block6 = iblockstate7.getBlock();
//                    Block block7 = iblockstate8.getBlock();
//
//                    if ((flag || block6.isFullBlock(iblockstate7)) && !flag1 && !block7.isFullBlock(iblockstate8))
//                    {
//                        enumfacing = Direction.SOUTH;
//                    }
//
//                    if ((flag1 || block7.isFullBlock(iblockstate8)) && !flag && !block6.isFullBlock(iblockstate7))
//                    {
//                        enumfacing = Direction.NORTH;
//                    }
//                }
//            }
//            else
//            {
//                BlockPos blockpos1 = block == this ? pos.north() : pos.south();
//                BlockState iblockstate5 = worldIn.getBlockState(blockpos1.west());
//                BlockState iblockstate6 = worldIn.getBlockState(blockpos1.east());
//                enumfacing = Direction.EAST;
//                Direction enumfacing1;
//
//                if (block == this)
//                {
//                    enumfacing1 = (Direction) iblockstate1.getValue(FACING);
//                }
//                else
//                {
//                    enumfacing1 = (Direction) iblockstate2.getValue(FACING);
//                }
//
//                if (enumfacing1 == Direction.WEST)
//                {
//                    enumfacing = Direction.WEST;
//                }
//
//                Block block4 = iblockstate5.getBlock();
//                Block block5 = iblockstate6.getBlock();
//
//                if ((block2.isFullBlock(iblockstate3) || block4.isFullBlock(iblockstate5)) && !block3.isFullBlock(iblockstate4) && !block5.isFullBlock(iblockstate6))
//                {
//                    enumfacing = Direction.EAST;
//                }
//
//                if ((block3.isFullBlock(iblockstate4) || block5.isFullBlock(iblockstate6)) && !block2.isFullBlock(iblockstate3) && !block4.isFullBlock(iblockstate5))
//                {
//                    enumfacing = Direction.WEST;
//                }
//            }
//
//            state = state.with(FACING, enumfacing);
//            worldIn.setBlockState(pos, state, 3);
//            return state;
//        }
//    }
//
//    public BlockState correctFacing(World worldIn, BlockPos pos, BlockState state)
//    {
//        Direction enumfacing = null;
//        Iterator iterator = Direction.Plane.HORIZONTAL.iterator();
//
//        while (iterator.hasNext())
//        {
//            Direction enumfacing1 = (Direction) iterator.next();
//            BlockState iblockstate1 = worldIn.getBlockState(pos.offset(enumfacing1));
//
//            if (iblockstate1.getBlock() == this)
//            {
//                return state;
//            }
//
//            if (iblockstate1.getBlock().isFullBlock(iblockstate1))
//            {
//                if (enumfacing != null)
//                {
//                    enumfacing = null;
//                    break;
//                }
//
//                enumfacing = enumfacing1;
//            }
//        }
//
//        if (enumfacing != null)
//        {
//            return state.with(FACING, enumfacing.getOpposite());
//        }
//        else
//        {
//            Direction enumfacing2 = (Direction) state.get(FACING);
//            BlockState state1 = worldIn.getBlockState(pos.offset(enumfacing2));
//
//            if (state1.getBlock().isFullBlock(state1))
//            {
//                enumfacing2 = enumfacing2.getOpposite();
//            }
//
//            state1 = worldIn.getBlockState(pos.offset(enumfacing2));
//            if (state1.getBlock().isFullBlock(state1))
//            {
//                enumfacing2 = enumfacing2.rotateY();
//            }
//
//            state1 = worldIn.getBlockState(pos.offset(enumfacing2));
//            if (state1.getBlock().isFullBlock(state1))
//            {
//                enumfacing2 = enumfacing2.getOpposite();
//            }
//
//            return state.with(FACING, enumfacing2);
//        }
//    }
//
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
//            if (this.isDoubleChest(worldIn, blockpos1))
//            {
//                return false;
//            }
//
//            ++i;
//        }
//
//        if (worldIn.getBlockState(blockpos2).getBlock() == this)
//        {
//            if (this.isDoubleChest(worldIn, blockpos2))
//            {
//                return false;
//            }
//
//            ++i;
//        }
//
//        if (worldIn.getBlockState(blockpos3).getBlock() == this)
//        {
//            if (this.isDoubleChest(worldIn, blockpos3))
//            {
//                return false;
//            }
//
//            ++i;
//        }
//
//        if (worldIn.getBlockState(blockpos4).getBlock() == this)
//        {
//            if (this.isDoubleChest(worldIn, blockpos4))
//            {
//                return false;
//            }
//
//            ++i;
//        }
//
//        return i <= 1;
//    }
//
//    private boolean isDoubleChest(World worldIn, BlockPos pos)
//    {
//        if (worldIn.getBlockState(pos).getBlock() != this)
//        {
//            return false;
//        }
//        else
//        {
//            Iterator iterator = Direction.Plane.HORIZONTAL.iterator();
//            Direction enumfacing;
//
//            do
//            {
//                if (!iterator.hasNext())
//                {
//                    return false;
//                }
//
//                enumfacing = (Direction) iterator.next();
//            }
//            while (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() != this);
//
//            return true;
//        }
//    }
//
//    @Override
//    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
//    {
//        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
//        TileEntity tileentity = worldIn.getTileEntity(pos);
//
//        if (tileentity instanceof TileEntityTreasureChest)
//        {
//            tileentity.updateContainingBlockInfo();
//        }
//    }
//
//    @Override
//    public void breakBlock(World worldIn, BlockPos pos, BlockState state)
//    {
//        TileEntity tileentity = worldIn.getTileEntity(pos);
//
//        if (tileentity instanceof IInventory)
//        {
//            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileentity);
//            worldIn.updateComparatorOutputLevel(pos, this);
//        }
//
//        super.breakBlock(worldIn, pos, state);
//    }
//
//    @Override
//    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit)
//    {
//        if (worldIn.isRemote)
//        {
//            return true;
//        }
//        else
//        {
//            TileEntity tile = worldIn.getTileEntity(pos);
//            playerIn.displayGUIChest((IInventory) tile);
//            return true;
//        }
//    }
//
//    @Override
//    public TileEntity createTileEntity(BlockState state, IBlockReader world)
//    {
//        return new TileEntityTreasureChest();
//    }
//
//    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, BlockState state, Direction side)
//    {
//        if (!this.canProvidePower(state))
//        {
//            return 0;
//        }
//        else
//        {
//            int i = 0;
//            TileEntity tileentity = worldIn.getTileEntity(pos);
//
//            if (tileentity instanceof TileEntityTreasureChest)
//            {
//                i = ((TileEntityTreasureChest) tileentity).numPlayersUsing;
//            }
//
//            return MathHelper.clamp(i, 0, 15);
//        }
//    }
//
//    public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, BlockState state, Direction side)
//    {
//        return side == Direction.UP ? this.isProvidingWeakPower(worldIn, pos, state, side) : 0;
//    }
//
//    private boolean isBlocked(World worldIn, BlockPos pos)
//    {
//        return this.isBelowSolidBlock(worldIn, pos) || this.isOcelotSittingOnChest(worldIn, pos);
//    }
//
//    private boolean isBelowSolidBlock(World worldIn, BlockPos pos)
//    {
//        return worldIn.isSideSolid(pos.up(), Direction.DOWN, false);
//    }
//
//    private boolean isOcelotSittingOnChest(World worldIn, BlockPos pos)
//    {
//        Iterator iterator = worldIn.getEntitiesWithinAABB(OcelotEntity.class, Block.makeCuboidShape((double) pos.getX(), (double) (pos.getY() + 1), (double) pos.getZ(), (double) (pos.getX() + 1), (double) (pos.getY() + 2), (double) (pos.getZ() + 1))).iterator();
//        OcelotEntity entityocelot;
//
//        do
//        {
//            if (!iterator.hasNext())
//            {
//                return false;
//            }
//
//            Entity entity = (Entity) iterator.next();
//            entityocelot = (OcelotEntity) entity;
//        }
//        while (!entityocelot.isSitting());
//
//        return true;
//    }
//
//    @Override
//    public boolean hasComparatorInputOverride(BlockState state)
//    {
//        return true;
//    }
//
//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        Direction enumfacing = Direction.getFront(meta);
//
//        if (enumfacing.getAxis() == Direction.Axis.Y)
//        {
//            enumfacing = Direction.NORTH;
//        }
//
//        return this.getDefaultState().with(FACING, enumfacing);
//    }
//
//    @Override
//    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
//    {
//        builder.add(FACING);
//    }
//
//    @Override
//    public String getShiftDescription(int meta)
//    {
//        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
//    }
//
//    @Override
//    public boolean showDescription(int meta)
//    {
//        return true;
//    }
//
//    @Override
//    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager)
//    {
//        return false;
//    }
//
//    @Override
//    public EnumSortCategoryBlock getCategory(int meta)
//    {
//        return EnumSortCategoryBlock.TREASURE;
//    }
//}