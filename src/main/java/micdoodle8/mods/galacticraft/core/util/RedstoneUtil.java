package micdoodle8.mods.galacticraft.core.util;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RedstoneUtil
{

    /**
     * Returns true if the block is being directly powered with a redstone wire, lever, etc
     * (generally only used by actual redstone components for onward transmission of redstone signal)
     */
    public static boolean isBlockReceivingDirectRedstone(World w, BlockPos pos)
    {
        if (w == null)
        {
            return false;
        }

        // Check up/down without chunk load test first
        if (getStrongPower(w, pos.down(), Direction.DOWN) > 0)
        {
            return true;
        }
        if (getStrongPower(w, pos.up(), Direction.UP) > 0)
        {
            return true;
        }

        for (Direction facing : Direction.values())
        {
            if (facing.getAxis().isHorizontal())
            {
                if (getStrongPower_NoChunkLoad(w, pos.offset(facing, 1), facing) > 0)
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Is this block powering in the specified direction Args: x, y, z, direction
     */
    public static int getStrongPower(World w, BlockPos pos, Direction side)
    {
        BlockState bs = w.getBlockState(pos);
        return bs.getBlock().getStrongPower(bs, w, pos, side);
    }

    public static int getStrongPower_NoChunkLoad(World w, BlockPos pos, Direction side)
    {
        if (!w.isBlockLoaded(pos))
        {
            return 0;
        }
        BlockState bs = w.getBlockState(pos);
        return bs.getBlock().getStrongPower(bs, w, pos, side);
    }

    /**
     * Returns true if the block is being INDIRECTLY powered e.g by redstone passing through another block
     * (similar to how a vanilla piston or redstone lamp responds)
     * <p>
     * INEFFICIENT: (almost as bad as Vanilla!) examines redstone status of 30 nearby blocks if there is no redstone power
     * <p>
     * Note: if the calling block is itself a redstone emitter (!!) its redstone output to neighbours will be ignored
     */
    public static boolean isBlockReceivingRedstone(World w, BlockPos pos)
    {
        if (w == null)
        {
            return false;
        }

        // Check up/down without chunk load test first
        if (getRedstonePowerIndirect(w, pos.up(1), Direction.UP) > 0)
        {
            return true;
        }
        if (getRedstonePowerIndirect(w, pos.down(1), Direction.DOWN) > 0)
        {
            return true;
        }

        for (Direction facing : Direction.values())
        {
            if (facing.getAxis().isHorizontal())
            {
                if (getRedstonePowerIndirect_NoChunkLoad(w, pos.offset(facing, 1), facing) > 0)
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static int getRedstonePowerIndirect_NoChunkLoad(World w, BlockPos pos, Direction side)
    {
        if (!w.isBlockLoaded(pos))
        {
            return 0;
        }
        return getRedstonePowerIndirect(w, pos, side);
    }

    public static int getRedstonePowerIndirect(World w, BlockPos pos, Direction facing)
    {
        BlockState bs = w.getBlockState(pos);
        Block block = bs.getBlock();
        if (block instanceof AirBlock)
        {
            return 0;
        }
        return block.shouldCheckWeakPower(bs, w, pos, facing) ? getNeighbourPower_NoChunkLoad(w, pos, facing.getOpposite()) : bs.getWeakPower(w, pos, facing);
    }

    /**
     * Similar to the vanilla method getStrongPower(BlockPos pos) but more efficient - doesn't backtrack, so 15% faster
     * (also the low level code here is faster...)
     */
    public static int getNeighbourPower_NoChunkLoad(World w, BlockPos pos, Direction skip)
    {
        int i = 0;
        int p;
        BlockState bs;
        BlockPos sidePos;

        if (skip != Direction.DOWN)
        {
            sidePos = pos.add(0, -1, 0);
            bs = w.getBlockState(sidePos);
            i = bs.getBlock().getStrongPower(bs, w, sidePos, Direction.DOWN);
            if (i >= 15)
            {
                return i;
            }
        }

        if (skip != Direction.UP)
        {
            sidePos = pos.add(0, 1, 0);
            bs = w.getBlockState(sidePos);
            p = bs.getBlock().getStrongPower(bs, w, sidePos, Direction.UP);
            if (p >= 15)
            {
                return p;
            }
            if (p > i)
            {
                i = p;
            }
        }

        for (Direction side : Direction.values())
        {
            if (side.getAxis().isHorizontal())
            {
                if (side == skip)
                {
                    continue;
                }
                //This is a slightly faster pos.offset(side)
                sidePos = pos.add(side.getXOffset(), 0, side.getZOffset());
                if (!w.isBlockLoaded(sidePos))
                {
                    continue;
                }

                bs = w.getBlockState(sidePos);
                p = bs.getBlock().getStrongPower(bs, w, sidePos, side);
                if (p >= 15)
                {
                    return p;
                }
                if (p > i)
                {
                    i = p;
                }
            }
        }

        return i;
    }
}
