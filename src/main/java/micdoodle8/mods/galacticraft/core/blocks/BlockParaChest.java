//package micdoodle8.mods.galacticraft.core.blocks;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityParaChest;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.block.*;
//import net.minecraft.block.state.BlockFaceShape;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.item.ItemEntity;
//import net.minecraft.entity.passive.OcelotEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.player.ServerPlayerEntity;
//import net.minecraft.inventory.IInventory;
//import net.minecraft.item.DyeColor;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.item.ItemStack;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.Direction;
//import net.minecraft.util.Hand;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.World;
//
//import java.util.Iterator;
//import java.util.Random;
//
//public class BlockParaChest extends ContainerBlock implements ITileEntityProvider, IShiftDescription, ISortableBlock
//{
//    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
//    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
//    protected static final VoxelShape NOT_CONNECTED_AABB = Block.makeCuboidShape(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
//
//    public BlockParaChest(Properties builder)
//    {
//        super(builder);
//        this.setDefaultState(stateContainer.getBaseState().with(FACING, Direction.NORTH));
//    }
//
//    @Override
//    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
//    {
//        return NOT_CONNECTED_AABB;
//    }
//
//    @Override
//    public BlockRenderType getRenderType(BlockState state)
//    {
//        return BlockRenderType.MODEL;
//    }
//
//    @Override
//    public BlockState getStateForPlacement(BlockItemUseContext context)
//    {
//        return this.getDefaultState().with(FACING, placer.getHorizontalFacing());
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
//    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }
//
//    @Override
//    public void onBlockAdded(World worldIn, BlockPos pos, BlockState state)
//    {
//        super.onBlockAdded(worldIn, pos, state);
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
//            IInventory iinventory = this.getInventory(worldIn, pos);
//
//            if (iinventory != null && playerIn instanceof ServerPlayerEntity)
//            {
//                playerIn.openGui(GalacticraftCore.instance, -1, worldIn, pos.getX(), pos.getY(), pos.getZ());
//            }
//
//            return true;
//        }
//    }
//
//    @Override
//    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
//    {
//        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
//    }
//
//    @Override
//    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
//    {
//        TileEntityParaChest tileentitychest = (TileEntityParaChest) worldIn.getTileEntity(pos);
//
//        if (tileentitychest != null)
//        {
//            tileentitychest.updateContainingBlockInfo();
//        }
//    }
//
//    @Override
//    public void breakBlock(World worldIn, BlockPos pos, BlockState state)
//    {
//        TileEntityParaChest tileentitychest = (TileEntityParaChest) worldIn.getTileEntity(pos);
//
//        if (tileentitychest != null)
//        {
//            Random syncRandom = GCCoreUtil.getRandom(pos);
//            for (int j1 = 0; j1 < tileentitychest.getSizeInventory(); ++j1)
//            {
//                ItemStack itemstack = tileentitychest.getStackInSlot(j1);
//
//                if (itemstack != null)
//                {
//
//                    float f = syncRandom.nextFloat() * 0.8F + 0.1F;
//                    float f1 = syncRandom.nextFloat() * 0.8F + 0.1F;
//                    ItemEntity entityitem;
//
//                    for (float f2 = syncRandom.nextFloat() * 0.8F + 0.1F; !itemstack.isEmpty(); worldIn.addEntity(entityitem))
//                    {
//                        entityitem = new ItemEntity(worldIn, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2, itemstack.splitStack(syncRandom.nextInt(21) + 10));
//                        float f3 = 0.05F;
//                        entityitem.motionX = (float) syncRandom.nextGaussian() * f3;
//                        entityitem.motionY = (float) syncRandom.nextGaussian() * f3 + 0.2F;
//                        entityitem.motionZ = (float) syncRandom.nextGaussian() * f3;
//                    }
//                }
//            }
//
//            worldIn.updateComparatorOutputLevel(pos, null);
//        }
//
//        super.breakBlock(worldIn, pos, state);
//    }
//
//    public IInventory getInventory(World par1World, BlockPos pos)
//    {
//        Object object = par1World.getTileEntity(pos);
//
//        if (object == null)
//        {
//            return null;
//        }
//        else if (par1World.isSideSolid(pos.offset(Direction.UP), Direction.DOWN))
//        {
//            return null;
//        }
//        else if (BlockParaChest.isOcelotBlockingChest(par1World, pos))
//        {
//            return null;
//        }
//        else
//        {
//            return (IInventory) object;
//        }
//    }
//
//    public static boolean isOcelotBlockingChest(World par0World, BlockPos pos)
//    {
//        Iterator<?> iterator = par0World.getEntitiesWithinAABB(OcelotEntity.class, Block.makeCuboidShape(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1)).iterator();
//        OcelotEntity entityocelot;
//
//        do
//        {
//            if (!iterator.hasNext())
//            {
//                return false;
//            }
//
//            entityocelot = (OcelotEntity) iterator.next();
//        }
//        while (!entityocelot.isSitting());
//
//        return true;
//    }
//
//    @Override
//    public TileEntity createNewTileEntity(World par1World, int meta)
//    {
//        return new TileEntityParaChest();
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
//
//    @Override
//    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
//    {
//        builder.add(COLOR, FACING);
//    }
//
//    @Override
//    public BlockState getActualState(BlockState state, IBlockReader worldIn, BlockPos pos)
//    {
//        TileEntity tile = worldIn.getTileEntity(pos);
//        if (!(tile instanceof TileEntityParaChest))
//        {
//            return state;
//        }
//        TileEntityParaChest chest = (TileEntityParaChest) tile;
//        return state.with(COLOR, chest.color);
//    }
//
//    @Override
//    public EnumSortCategoryBlock getCategory(int meta)
//    {
//        return EnumSortCategoryBlock.GENERAL;
//    }
//}
