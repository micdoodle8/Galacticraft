package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.fluid.OxygenPressureProtocol;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBreathableAir extends BlockThermalAir
{
    public BlockBreathableAir(Properties builder)
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

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block oldBlock, BlockPos fromPos, boolean isMoving)
    {
        if (Blocks.AIR != oldBlock)
        //Do no check if replacing breatheableAir with a solid block, although that could be dividing a sealed space
        {
            // Check if replacing a passthrough breathable block, like a torch - if so replace with BreathableAir not Air
            if (Blocks.AIR == state.getBlock())
            {
                Direction side;
                if (pos.getX() != fromPos.getX()) side = pos.getX() > fromPos.getX() ? Direction.EAST : Direction.WEST;
                else if (pos.getY() != fromPos.getY()) side = pos.getY() > fromPos.getY() ? Direction.UP : Direction.DOWN;
                else side = pos.getZ() > fromPos.getZ() ? Direction.SOUTH : Direction.NORTH;
                if (OxygenPressureProtocol.canBlockPassAir(worldIn, state, fromPos, side))
                {
                    worldIn.setBlockState(fromPos, GCBlocks.breatheableAir.getDefaultState(), 6);
                }
            }
            // In all cases, trigger a leak check at this point
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
//    public int getLightOpacity(BlockState state)
//    {
//        return 0;
//    }

//    @Override
//    public void breakBlock(World worldIn, BlockPos vec, BlockState state)
//    {
//    }
}
