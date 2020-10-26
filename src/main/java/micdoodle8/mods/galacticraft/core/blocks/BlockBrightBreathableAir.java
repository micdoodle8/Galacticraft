package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.fluid.OxygenPressureProtocol;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBrightBreathableAir extends BlockThermalAir
{
    public static final BooleanProperty THERMAL = BooleanProperty.create("thermal");

    public BlockBrightBreathableAir(Properties builder)
    {
        super(builder);
        this.setDefaultState(stateContainer.getBaseState().with(THERMAL, false));
    }

//    @Override
//    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
//    {
//        return true;
//    }
//
//    @Override
//    public PushReaction getMobilityFlag(BlockState state)
//    {
//        return PushReaction.DESTROY;
//    }
//
//    @Override
//    public Item getItemDropped(BlockState state, Random rand, int fortune)
//    {
//        return Item.getItemFromBlock(Blocks.AIR);
//    }
//
//    @Override
//    public boolean shouldSideBeRendered(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
//    {
//        final Block block = blockAccess.getBlockState(pos).getBlock();
//        if (block == this || block == GCBlocks.breatheableAir)
//        {
//            return false;
//        }
//        else
//        {
//            return block instanceof AirBlock;
//        }
//    }


    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        if (Blocks.AIR == blockIn)
        //Do no check if replacing breatheableAir with a solid block, although that could be dividing a sealed space
        {
            OxygenPressureProtocol.onEdgeBlockUpdated(worldIn, pos);
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(THERMAL);
    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(THERMAL, meta % 2 == 1);
//    }

//    @Override
//    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos)
//    {
//        return 15;
//    }

//    @Override
//    public int getLightOpacity(BlockState state)
//    {
//        return 0;
//    }

//    @Override
//    public void breakBlock(World worldIn, BlockPos vec, BlockState state)
//    {
//    }
}
