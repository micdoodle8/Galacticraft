package micdoodle8.mods.galacticraft.core.util;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class RedstoneUtil
{

    /**
     * Returns the highest redstone signal strength powering the given block. Args: X, Y, Z.
     */
    public static boolean isBlockReceivingRedstone(World w, BlockPos pos)
    {
        if (w == null)
        {
            return false;
        }

        // Check up/down without chunk load test first
        if (isBlockProvidingPowerTo(w, pos.offset(EnumFacing.DOWN), EnumFacing.DOWN) > 0)
        {
            return true;
        }
        if (isBlockProvidingPowerTo(w, pos.offset(EnumFacing.UP), EnumFacing.UP) > 0)
        {
            return true;
        }

        for (EnumFacing facing : EnumFacing.HORIZONTALS)
        {
            if (isBlockProvidingPowerTo_NoChunkLoad(w, pos.offset(facing), facing) > 0)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Is this block powering in the specified direction Args: x, y, z, direction
     */
    public static int isBlockProvidingPowerTo(World w, BlockPos pos, EnumFacing side)
    {
        return w.getBlockState(pos).getBlock().getStrongPower(w, pos, w.getBlockState(pos), side);
    }

    public static int isBlockProvidingPowerTo_NoChunkLoad(World w, BlockPos pos, EnumFacing side)
    {
        if (!w.isBlockLoaded(pos))
        {
            return 0;
        }
        return w.getBlockState(pos).getBlock().getStrongPower(w, pos, w.getBlockState(pos), side);
    }
}
