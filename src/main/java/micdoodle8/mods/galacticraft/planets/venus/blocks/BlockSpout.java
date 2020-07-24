package micdoodle8.mods.galacticraft.planets.venus.blocks;

import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntitySpout;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BlockSpout extends Block
{
    public BlockSpout(Properties builder)
    {
        super(builder);
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

//    @Override
//    public EnumSortCategoryBlock getCategory(int meta)
//    {
//        return EnumSortCategoryBlock.GENERAL;
//    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntitySpout();
    }

//    @Override
//    public Item getItemDropped(BlockState state, Random rand, int fortune)
//    {
//        return Item.getItemFromBlock(VenusBlocks.venusBlock);
//    } TODO Block drops

//    @Override
//    public int damageDropped(BlockState state)
//    {
//        return BlockVenusRock.EnumBlockBasicVenus.ROCK_SOFT.getMeta();
//    }

//    @Override
//    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, BlockState state, int fortune)
//    {
//        return super.getDrops(world, pos, state, fortune);
//    }


    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return super.getShape(state, worldIn, pos, context);
    }

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return (face == Direction.UP) ? BlockFaceShape.BOWL : BlockFaceShape.SOLID;
//    }
}
