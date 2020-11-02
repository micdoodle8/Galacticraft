package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.tile.TileEntityPlatform;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockPlatform extends BlockAdvancedTile implements IPartialSealableBlock, IShiftDescription, ISortable
{
    public static final EnumProperty<EnumCorner> CORNER = EnumProperty.create("type", EnumCorner.class);
    public static final float HEIGHT = 0.875F;
    protected static final VoxelShape BOUNDING_BOX = VoxelShapes.create(0.0D, 6 / 16.0D, 0.0D, 1.0D, HEIGHT, 1.0D);
    protected static final VoxelShape BOUNDING_BOX_ZEROG = VoxelShapes.create(0.0D, 6 / 16.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    public static boolean ignoreCollisionTests;

    public enum EnumCorner implements IStringSerializable
    {
        NONE(0, "none"),
        NW(1, "sw"),
        SW(2, "nw"),
        NE(3, "se"),
        SE(4, "ne");
        // Yes these labels are the wrong way round, n should be s!  But the BlockState model is hard-coded to work with this as it is.

        private final int id;
        private final String name;

        EnumCorner(int id, String name)
        {
            this.id = id;
            this.name = name;
        }

        public int getId()
        {
            return this.id;
        }

        private final static EnumCorner[] values = values();

        public static EnumCorner byId(int meta)
        {
            return values[meta % values.length];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockPlatform(Properties builder)
    {
        super(builder);
        this.setDefaultState(stateContainer.getBaseState().with(CORNER, EnumCorner.NONE));
    }

//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    private boolean checkAxis(World worldIn, BlockPos pos, Block block, Direction facing)
    {
        int sameCount = 0;
        for (int i = 1; i <= 2; i++)
        {
            BlockState bs = worldIn.getBlockState(pos.offset(facing, i));
            if (bs.getBlock() == block && bs.get(BlockPlatform.CORNER) == BlockPlatform.EnumCorner.NONE)
            {
                sameCount++;
            }
        }

        return sameCount > 1;
    }

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
//        if (worldIn.getBlockState(pos.offset(Direction.DOWN)).getBlock() == GCBlocks.platform && LogicalSide == Direction.UP)
//        {
//            return false;
//        }
//        else
//        {
//            return this.canPlaceBlockAt(worldIn, pos);
//        }
//    }

//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public boolean shouldSideBeRendered(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
//    {
//        return true;
//    }

//    @Override
//    public boolean doesSideBlockRendering(BlockState state, IBlockReader world, BlockPos pos, Direction face)
//    {
//        return false;
//    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public BlockRenderLayer getRenderLayer()
//    {
//        return BlockRenderLayer.CUTOUT;
//    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityPlatform(state.get(CORNER));
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        final TileEntity var9 = worldIn.getTileEntity(pos);

        if (state.getBlock() != this || newState.getBlock() != this)
        {
            if (var9 instanceof TileEntityPlatform)
            {
                ((TileEntityPlatform) var9).onDestroy(var9);
            }
        }

        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean isSealed(World worldIn, BlockPos pos, Direction direction)
    {
        return direction == Direction.UP;
    }

//    @Override
//    public int damageDropped(BlockState state)
//    {
//        return 0;
//    }

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
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(CORNER, EnumCorner.byMetadata(meta));
//    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(CORNER);
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.DECORATION;
    }

//    @Override
//    public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, @Nullable Entity entityIn, boolean p_185477_7_)
//    {
//        if (ignoreCollisionTests) return;
//        TileEntity te = worldIn.getTileEntity(pos);
//        if (te instanceof TileEntityPlatform)
//        {
//            if (((TileEntityPlatform) te).noCollide()) return;
//        }
//        AxisAlignedBB axisalignedbb = this.getShape(state, worldIn, pos).offset(pos);
//
//        if (axisalignedbb != null && mask.intersects(axisalignedbb))
//        {
//            list.add(axisalignedbb);
//        }
//    }
//
////    @Override
////    public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader world, BlockPos pos)
////    {
////        if (world instanceof World && ((World) world).dimension instanceof IZeroGDimension)
////            return BOUNDING_BOX_ZEROG;
////        return BOUNDING_BOX;
////    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        if (ignoreCollisionTests)
        {
            return VoxelShapes.empty();
        }

        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileEntityPlatform)
        {
            if (((TileEntityPlatform) tile).noCollide())
            {
                return VoxelShapes.empty();
            }
        }

        return super.getCollisionShape(state, worldIn, pos, context);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        if (worldIn instanceof World && ((World) worldIn).dimension instanceof IZeroGDimension)
        {
            return BOUNDING_BOX_ZEROG;
        }

        return BOUNDING_BOX;
    }

//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public AxisAlignedBB getSelectedBoundingBox(BlockState bs, World worldIn, BlockPos pos)
//    {
//        TileEntity te = worldIn.getTileEntity(pos);
//        if (te instanceof TileEntityPlatform)
//        {
//            if (((TileEntityPlatform) te).noCollide())
//            {
//                if (bs.getBlock() == this && bs.get(BlockPlatform.CORNER) == BlockPlatform.EnumCorner.SE)
//                    return VoxelShapes.create((double)pos.getX() + 9/16D, (double)pos.getY(), (double)pos.getZ() + 9/16D, (double)pos.getX() + 1.0D, (double)pos.getY() + HEIGHT, (double)pos.getZ() + 1.0D);
//                else
//                    return VoxelShapes.create((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)pos.getX() + 7/16D, (double)pos.getY() + HEIGHT, (double)pos.getZ() + 7/16D);
//            }
//        }
//        return super.getSelectedBoundingBox(bs, worldIn, pos);
//    }
}
