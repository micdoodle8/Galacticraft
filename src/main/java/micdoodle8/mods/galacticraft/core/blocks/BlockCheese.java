package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockCheese extends Block implements IShiftDescription, ISortable
{
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 6);
    protected static final VoxelShape[] CHEESE_AABB = new VoxelShape[]{
            VoxelShapes.create(0.0625, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
            VoxelShapes.create(0.1875, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
            VoxelShapes.create(0.3125, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
            VoxelShapes.create(0.4375, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
            VoxelShapes.create(0.5625, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
            VoxelShapes.create(0.6875, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
            VoxelShapes.create(0.8125, 0.0, 0.0625, 0.9375, 0.5, 0.9375)
    };

    public BlockCheese(Properties builder)
    {
        super(builder);
        this.setDefaultState(stateContainer.getBaseState().with(BITES, 0));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        int bites = 0;
        if (state.getBlock() instanceof BlockCheese)
        {
            bites = state.get(BITES);
        }
        return CHEESE_AABB[bites];
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
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit)
    {
        this.eatCheeseSlice(worldIn, pos, worldIn.getBlockState(pos), playerIn);
        return ActionResultType.SUCCESS;
    }

    private void eatCheeseSlice(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn)
    {
        if (playerIn.canEat(false))
        {
            playerIn.getFoodStats().addStats(2, 0.1F);
            int i = state.get(BITES);

            if (i < 6)
            {
                worldIn.setBlockState(pos, state.with(BITES, Integer.valueOf(i + 1)), 3);
            }
            else
            {
                worldIn.removeBlock(pos, false);
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        if (!this.canBlockStay(worldIn, pos))
        {
            worldIn.removeBlock(pos, false);
        }
    }

    private boolean canBlockStay(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).getMaterial().isSolid();
    }

//    @Override
//    public int quantityDropped(Random par1Random)
//    {
//        return 0;
//    }

//    @Override
//    public Item getItemDropped(BlockState state, Random rand, int fortune)
//    {
//        return Item.getItemFromBlock(Blocks.AIR);
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
//        return this.getDefaultState().with(BITES, Integer.valueOf(meta));
//    }
//
//    @Override
//    public ItemStack getItem(World worldIn, BlockPos pos, BlockState state)
//    {
//        return new ItemStack(Items.CAKE, 1, 0);
//    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public BlockRenderLayer getRenderLayer()
//    {
//        return BlockRenderLayer.CUTOUT;
//    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(BITES);
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)
    {
        return (7 - worldIn.getBlockState(pos).get(BITES)) * 2;
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state)
    {
        return true;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }
}
