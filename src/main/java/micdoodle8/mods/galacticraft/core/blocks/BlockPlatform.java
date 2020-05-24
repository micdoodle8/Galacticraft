//package micdoodle8.mods.galacticraft.core.blocks;
//
//import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
//import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
//import micdoodle8.mods.galacticraft.core.GCBlocks;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityPlatform;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockRenderType;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.state.BlockFaceShape;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.entity.Entity;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.BlockRenderLayer;
//import net.minecraft.util.Direction;
//import net.minecraft.util.IStringSerializable;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IBlockAccess;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import javax.annotation.Nullable;
//import java.util.List;
//
//public class BlockPlatform extends BlockAdvancedTile implements IPartialSealableBlock, IShiftDescription, ISortableBlock
//{
//    public static final EnumProperty<EnumCorner> CORNER = EnumProperty.create("type", EnumCorner.class);
//    public static final float HEIGHT = 0.875F;
//    protected static final VoxelShape BOUNDING_BOX = Block.makeCuboidShape(0.0D, 6 / 16.0D, 0.0D, 1.0D, HEIGHT, 1.0D);
//    protected static final VoxelShape BOUNDING_BOX_ZEROG = Block.makeCuboidShape(0.0D, 6 / 16.0D, 0.0D, 1.0D, 1.0D, 1.0D);;
//    public static boolean ignoreCollisionTests;
//
//    public enum EnumCorner implements IStringSerializable
//    {
//        NONE(0, "none"),
//        NW(1, "sw"),
//        SW(2, "nw"),
//        NE(3, "se"),
//        SE(4, "ne");
//        // Yes these labels are the wrong way round, n should be s!  But the BlockState model is hard-coded to work with this as it is.
//
//        private final int meta;
//        private final String name;
//
//        EnumCorner(int meta, String name)
//        {
//            this.meta = meta;
//            this.name = name;
//        }
//
//        public int getMeta()
//        {
//            return this.meta;
//        }
//
//        private final static EnumCorner[] values = values();
//        public static EnumCorner byMetadata(int meta)
//        {
//            return values[meta % values.length];
//        }
//
//        @Override
//        public String getName()
//        {
//            return this.name;
//        }
//    }
//
//    public BlockPlatform(Properties builder)
//    {
//        super(builder);
//        this.setDefaultState(stateContainer.getBaseState().with(CORNER, EnumCorner.NONE));
//    }
//
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }
//
//    private boolean checkAxis(World worldIn, BlockPos pos, Block block, Direction facing)
//    {
//        int sameCount = 0;
//        for (int i = 1; i <= 2; i++)
//        {
//            BlockState bs = worldIn.getBlockState(pos.offset(facing, i));
//            if (bs.getBlock() == block && bs.getValue(BlockPlatform.CORNER) == BlockPlatform.EnumCorner.NONE)
//            {
//                sameCount++;
//            }
//        }
//
//        return sameCount > 1;
//    }
//
//    @Override
//    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, Direction side)
//    {
//        final Block id = GCBlocks.platform;
//
//        if (checkAxis(worldIn, pos, id, Direction.EAST) ||
//                checkAxis(worldIn, pos, id, Direction.WEST) ||
//                checkAxis(worldIn, pos, id, Direction.NORTH) ||
//                checkAxis(worldIn, pos, id, Direction.SOUTH))
//        {
//            return false;
//        }
//
//        if (worldIn.getBlockState(pos.offset(Direction.DOWN)).getBlock() == GCBlocks.platform && side == Direction.UP)
//        {
//            return false;
//        }
//        else
//        {
//            return this.canPlaceBlockAt(worldIn, pos);
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
//    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public boolean shouldSideBeRendered(BlockState blockState, IBlockAccess blockAccess, BlockPos pos, Direction side)
//    {
//        return true;
//    }
//
//    @Override
//    public boolean doesSideBlockRendering(BlockState state, IBlockAccess world, BlockPos pos, Direction face)
//    {
//        return false;
//    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public BlockRenderLayer getBlockLayer()
//    {
//        return BlockRenderLayer.CUTOUT;
//    }
//
//    @Override
//    public BlockRenderType getRenderType(BlockState state)
//    {
//        return BlockRenderType.MODEL;
//    }
//
//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }
//
//    @Override
//    public TileEntity createTileEntity(BlockState state, IBlockReader world)
//    {
//        return new TileEntityPlatform(meta);
//    }
//
//    @Override
//    public void breakBlock(World worldIn, BlockPos pos, BlockState state)
//    {
//        final TileEntity var9 = worldIn.getTileEntity(pos);
//
//        if (var9 instanceof TileEntityPlatform)
//        {
//            ((TileEntityPlatform) var9).onDestroy(var9);
//        }
//
//        super.breakBlock(worldIn, pos, state);
//    }
//
//    @Override
//    public boolean isSealed(World worldIn, BlockPos pos, Direction direction)
//    {
//        return direction == Direction.UP;
//    }
//
//    @Override
//    public int damageDropped(BlockState state)
//    {
//        return 0;
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
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(CORNER, EnumCorner.byMetadata(meta));
//    }
//
//    @Override
//    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
//    {
//        builder.add(CORNER);
//    }
//
//    @Override
//    public EnumSortCategoryBlock getCategory(int meta)
//    {
//        return EnumSortCategoryBlock.DECORATION;
//    }
//
//    @Override
//    public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, @Nullable Entity entityIn, boolean p_185477_7_)
//    {
//        if (ignoreCollisionTests) return;
//        TileEntity te = worldIn.getTileEntity(pos);
//        if (te instanceof TileEntityPlatform)
//        {
//            if (((TileEntityPlatform) te).noCollide()) return;
//        }
//        AxisAlignedBB axisalignedbb = this.getCollisionBoundingBox(state, worldIn, pos).offset(pos);
//
//        if (axisalignedbb != null && mask.intersects(axisalignedbb))
//        {
//            list.add(axisalignedbb);
//        }
//    }
//
//    @Override
//    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess world, BlockPos pos)
//    {
//        if (world instanceof World && ((World) world).provider instanceof IZeroGDimension)
//            return BOUNDING_BOX_ZEROG;
//        return BOUNDING_BOX;
//    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public AxisAlignedBB getSelectedBoundingBox(BlockState bs, World worldIn, BlockPos pos)
//    {
//        TileEntity te = worldIn.getTileEntity(pos);
//        if (te instanceof TileEntityPlatform)
//        {
//            if (((TileEntityPlatform) te).noCollide())
//            {
//                if (bs.getBlock() == this && bs.getValue(BlockPlatform.CORNER) == BlockPlatform.EnumCorner.SE)
//                    return Block.makeCuboidShape((double)pos.getX() + 9/16D, (double)pos.getY(), (double)pos.getZ() + 9/16D, (double)pos.getX() + 1.0D, (double)pos.getY() + HEIGHT, (double)pos.getZ() + 1.0D);
//                else
//                    return Block.makeCuboidShape((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)pos.getX() + 7/16D, (double)pos.getY() + HEIGHT, (double)pos.getZ() + 7/16D);
//            }
//        }
//        return super.getSelectedBoundingBox(bs, worldIn, pos);
//    }
//}
