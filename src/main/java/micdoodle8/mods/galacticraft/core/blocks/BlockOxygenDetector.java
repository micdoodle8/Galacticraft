//package micdoodle8.mods.galacticraft.core.blocks;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenDetector;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.block.BlockRenderType;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.ContainerBlock;
//import net.minecraft.block.ITileEntityProvider;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.Direction;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IBlockAccess;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.World;
//
//public class BlockOxygenDetector extends ContainerBlock implements ITileEntityProvider, IShiftDescription, ISortableBlock
//{
//    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
//
//    public BlockOxygenDetector(Properties builder)
//    {
//        super(builder);
//    }
//
//    @Override
//    public BlockRenderType getRenderType(BlockState state)
//    {
//        return BlockRenderType.MODEL;
//    }
//
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }
//
//    @Override
//    public TileEntity createTileEntity(BlockState state, IBlockReader world)
//    {
//        return new TileEntityOxygenDetector();
//    }
//
//    public void updateOxygenState(World worldIn, BlockPos pos, boolean valid)
//    {
//        if (valid)
//        {
//            worldIn.setBlockState(pos, getStateFromMeta(1), 3);
//        }
//        else
//        {
//            worldIn.setBlockState(pos, getStateFromMeta(0), 3);
//        }
//    }
//
//    @Override
//    public boolean canProvidePower(BlockState state)
//    {
//        return true;
//    }
//
//    @Override
//    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
//    {
//        return getMetaFromState(blockAccess.getBlockState(pos)) == 1 ? 15 : 0;
//    }
//
//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(ACTIVE, meta > 0);
//    }
//
//    @Override
//    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
//    {
//        builder.add(ACTIVE);
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
//    public boolean isSideSolid(BlockState base_state, IBlockAccess world, BlockPos pos, Direction side)
//    {
//        return true;
//    }
//
//    @Override
//    public EnumSortCategoryBlock getCategory(int meta)
//    {
//        return EnumSortCategoryBlock.MACHINE;
//    }
//}
