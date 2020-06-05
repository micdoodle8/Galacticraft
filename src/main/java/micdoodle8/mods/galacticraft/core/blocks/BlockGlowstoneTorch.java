//package micdoodle8.mods.galacticraft.core.blocks;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.properties.IProperty;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.util.BlockRenderLayer;
//import net.minecraft.util.Direction;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import java.util.Random;
//
//public class BlockGlowstoneTorch extends BlockTorchBase implements IShiftDescription, ISortableBlock
//{
//
//    public BlockGlowstoneTorch(Properties builder)
//    {
//        super(builder);
//        this.setDefaultState(stateContainer.getBaseState().with(FACING, Direction.UP));
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
//    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
//    {
//        for (Direction enumfacing : FACING.getAllowedValues())
//        {
//            if (this.canPlaceAt(worldIn, pos, enumfacing))
//            {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    @Override
//    public void onBlockAdded(World worldIn, BlockPos pos, BlockState state)
//    {
//        this.checkForDrop(worldIn, pos, state);
//    }
//
//    @Override
//    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
//    {
//        if (!this.checkForDrop(worldIn, pos, state))
//        {
//            return;
//        }
//        else
//        {
//            Direction enumfacing = state.get(FACING);
//            Direction.Axis enumfacing$axis = enumfacing.getAxis();
//            Direction enumfacing1 = enumfacing.getOpposite();
//            boolean flag = false;
//
//            if (enumfacing$axis.isHorizontal() && !worldIn.isSideSolid(pos.offset(enumfacing1), enumfacing, true))
//            {
//                flag = true;
//            }
//            else if (enumfacing$axis.isVertical() && !this.canPlaceOn(worldIn, pos.offset(enumfacing1)))
//            {
//                flag = true;
//            }
//
//            if (flag)
//            {
//                this.dropBlockAsItem(worldIn, pos, state, 0);
//                worldIn.removeBlock(pos, false);
//            }
//        }
//    }
//
//    @Override
//    public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand)
//    {
//        super.updateTick(worldIn, pos, state, rand);
//
//        if (getMetaFromState(state) == 0)
//        {
//            this.onBlockAdded(worldIn, pos, state);
//        }
//    }
//
////    @Override
////    public RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end)
////    {
////        int l = getMetaFromState(worldIn.getBlockState(pos)) & 7;
////        float f = 0.15F;
////
////        if (l == 1)
////        {
////            this.setBlockBounds(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
////        }
////        else if (l == 2)
////        {
////            this.setBlockBounds(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
////        }
////        else if (l == 3)
////        {
////            this.setBlockBounds(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
////        }
////        else if (l == 4)
////        {
////            this.setBlockBounds(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
////        }
////        else
////        {
////            f = 0.1F;
////            this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
////        }
////
////        return super.collisionRayTrace(worldIn, pos, start, end);
////    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public BlockRenderLayer getBlockLayer()
//    {
//        return BlockRenderLayer.CUTOUT;
//    }
//
//    @Override
//    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
//    {
//        builder.add(new IProperty[] { FACING });
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
//    public EnumSortCategoryBlock getCategory(int meta)
//    {
//        return EnumSortCategoryBlock.GENERAL;
//    }
//}
