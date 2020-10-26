//package micdoodle8.mods.galacticraft.core.blocks;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.dimension.WorldProviderSpaceStation;
//import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityThruster;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
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
//import net.minecraft.util.EnumParticleTypes;
//import net.minecraft.util.Hand;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import java.util.Random;
//
//public class BlockSpinThruster extends BlockAdvanced implements IShiftDescription, ITileEntityProvider, ISortable
//{
//    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
//    public static final BooleanProperty ORIENTATION = BooleanProperty.create("rev");
//
//    protected static final VoxelShape NORTH_AABB = VoxelShapes.create(0.2F, 0.2F, 0.4F, 0.8F, 0.8F, 1.0F);
//    protected static final VoxelShape SOUTH_AABB = VoxelShapes.create(0.2F, 0.2F, 0.0F, 0.8F, 0.8F, 0.6F);
//    protected static final VoxelShape WEST_AABB = VoxelShapes.create(0.4F, 0.2F, 0.2F, 1.0F, 0.8F, 0.8F);
//    protected static final VoxelShape EAST_AABB = VoxelShapes.create(0.0F, 0.2F, 0.2F, 0.6F, 0.8F, 0.8F);
//
//    public BlockSpinThruster(Properties builder)
//    {
//        super(builder);
//        this.setDefaultState(stateContainer.getBaseState().with(FACING, Direction.NORTH).with(ORIENTATION, false));
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
//        default:
//        case NORTH:
//            return NORTH_AABB;
//        }
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
//        return BlockFaceShape.UNDEFINED;
//    }
//
//    @Override
//    public boolean canPlaceBlockAt(World world, BlockPos pos)
//    {
//        return world.isSideSolid(pos.west(), Direction.EAST, true) || world.isSideSolid(pos.east(), Direction.WEST, true) || world.isSideSolid(pos.north(), Direction.SOUTH, true) || world.isSideSolid(pos.south(), Direction.NORTH, true);
//    }
//
//    @Override
//    public BlockState getStateForPlacement(BlockItemUseContext context)
//    {
//        if (facing.getAxis().isHorizontal() && this.canBlockStay(world, pos, facing))
//        {
//            return this.getDefaultState().with(FACING, facing);
//        }
//        else
//        {
//            for (Direction enumfacing : Direction.Plane.HORIZONTAL)
//            {
//                if (this.canBlockStay(world, pos, enumfacing))
//                {
//                    return this.getDefaultState().with(FACING, enumfacing);
//                }
//            }
//            return this.getDefaultState();
//        }
//    }
//
//    @Override
//    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
//    {
//        if (this.getMetaFromState(state) == 0)
//        {
//            this.onBlockAdded(worldIn, pos, state);
//        }
//    }
//
//    @Override
//    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
//    {
//        int metadata = this.getMetaFromState(state);
//
//        BlockPos baseBlock;
//        switch (metadata)
//        {
//        case 1:
//            baseBlock = pos.offset(Direction.WEST);
//            break;
//        case 2:
//            baseBlock = pos.offset(Direction.EAST);
//            break;
//        case 3:
//            baseBlock = pos.offset(Direction.NORTH);
//            break;
//        case 4:
//            baseBlock = pos.offset(Direction.SOUTH);
//            break;
//        default:
//            return;
//        }
//
//        if (!worldIn.isRemote)
//        {
//            if (worldIn.dimension instanceof WorldProviderSpaceStation)
//            {
//                ((WorldProviderSpaceStation) worldIn.dimension).getSpinManager().refresh(baseBlock, true);
//            }
//        }
//    }
//
//    @Override
//    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
//    {
//        Direction enumfacing = state.get(FACING);
//
//        if (!this.canBlockStay(worldIn, pos, enumfacing))
//        {
//            this.dropBlockAsItem(worldIn, pos, state, 0);
//            worldIn.removeBlock(pos, false);
//        }
//        if (!worldIn.isRemote)
//        {
//            if (worldIn.dimension instanceof WorldProviderSpaceStation)
//            {
//                ((WorldProviderSpaceStation) worldIn.dimension).getSpinManager().refresh(pos, true);
//            }
//        }
//    }
//
//    protected boolean canBlockStay(World world, BlockPos pos, Direction facing)
//    {
//        return world.isSideSolid(pos.offset(facing.getOpposite()), facing, true);
//    }
//
////    @Override
////    public RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end)
////    {
////        float var8 = 0.3F;
////
////        EnumFacing facing = worldIn.getBlockState(pos).getValue(BlockMachine.FACING);
////
////        switch (facing)
////        {
////        case NORTH:
////            this.setBlockBounds(0.5F - var8, 0.2F, 1.0F - var8 * 2.0F, 0.5F + var8, 0.8F, 1.0F);
////            break;
////        case EAST:
////            this.setBlockBounds(0.0F, 0.2F, 0.5F - var8, var8 * 2.0F, 0.8F, 0.5F + var8);
////            break;
////        case SOUTH:
////            this.setBlockBounds(0.5F - var8, 0.2F, 0.0F, 0.5F + var8, 0.8F, var8 * 2.0F);
////            break;
////        case WEST:
////            this.setBlockBounds(1.0F - var8 * 2.0F, 0.2F, 0.5F - var8, 1.0F, 0.8F, 0.5F + var8);
////            break;
////        }
////
////        return super.collisionRayTrace(worldIn, pos, start, end);
////    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
//    {
//        //TODO this is torch code as a placeholder, still need to adjust positioning and particle type
//        //Also make small thrust sounds
//        if (worldIn.dimension instanceof WorldProviderSpaceStation)
//        {
//            if (((WorldProviderSpaceStation) worldIn.dimension).getSpinManager().thrustersFiring || rand.nextInt(80) == 0)
//            {
//                final int var6 = this.getMetaFromState(stateIn) & 7;
//                final double var7 = pos.getX() + 0.5F;
//                final double var9 = pos.getY() + 0.7F;
//                final double var11 = pos.getZ() + 0.5F;
//                final double var13 = 0.2199999988079071D;
//                final double var15 = 0.27000001072883606D;
//
//                if (var6 == 1)
//                {
//                    worldIn.addParticle(EnumParticleTypes.SMOKE_NORMAL, var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
//                }
//                else if (var6 == 2)
//                {
//                    worldIn.addParticle(EnumParticleTypes.SMOKE_NORMAL, var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
//                }
//                else if (var6 == 3)
//                {
//                    worldIn.addParticle(EnumParticleTypes.SMOKE_NORMAL, var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
//                }
//                else if (var6 == 0)
//                {
//                    worldIn.addParticle(EnumParticleTypes.SMOKE_NORMAL, var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
//                }
//            }
//        }
//    }
//
//    @Override
//    public boolean onUseWrench(World world, BlockPos pos, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
//    {
//        BlockState state = world.getBlockState(pos);
//        boolean orientation = state.get(ORIENTATION);
//
//        Direction currentFacing = state.get(FACING);
////        if (this.canBlockStay(world, pos.offset(currentFacing.getOpposite()), currentFacing))
////        {
//            world.setBlockState(pos, state.with(ORIENTATION, !orientation), 2);
////        }
//        //TODO  else
//
//        if (world.getDimension() instanceof WorldProviderSpaceStation && !world.isRemote)
//        {
//            WorldProviderSpaceStation worldOrbital = (WorldProviderSpaceStation) world.getDimension();
//            worldOrbital.getSpinManager().refresh(pos, true);
//        }
//        return true;
//    }
//
//    @Override
//    public TileEntity createTileEntity(BlockState state, IBlockReader world)
//    {
//        return new TileEntityThruster();
//    }
//
//    @Override
//    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
//    {
//        if (!worldIn.isRemote)
//        {
//            final int facing = this.getMetaFromState(state) & 8;
//            if (worldIn.dimension instanceof WorldProviderSpaceStation)
//            {
//                WorldProviderSpaceStation worldOrbital = (WorldProviderSpaceStation) worldIn.dimension;
//                worldOrbital.getSpinManager().removeThruster(pos, facing == 0);
//                worldOrbital.getSpinManager().updateSpinSpeed();
//            }
//        }
//    }
//
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }
//
//    @Override
//    public String getShiftDescription(ItemStack stack)
//    {
//        return GCCoreUtil.translate(this.getTranslationKey() + ".description");
//    }
//
//    @Override
//    public boolean showDescription(ItemStack stack)
//    {
//        return true;
//    }
//
//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        Direction enumfacing = Direction.byHorizontalIndex(meta % 4);
//        return this.getDefaultState().with(FACING, enumfacing).with(ORIENTATION, meta >= 8);
//    }
//
//    @Override
//    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
//    {
//        builder.add(FACING, ORIENTATION);
//    }
//
//    @Override
//    public EnumSortCategory getCategory()
//    {
//        return EnumSortCategory.MACHINE;
//    }
//}
