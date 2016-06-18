package micdoodle8.mods.galacticraft.core.util;

import net.minecraft.world.World;

public class RedstoneUtil
{

    /**
     * Returns the highest redstone signal strength powering the given block. Args: X, Y, Z.
     */
    public static boolean isBlockReceivingRedstone(World w, int x, int y, int z)
    {
    	if (w == null) return false;
    	if (isBlockProvidingPowerTo(w, x, y - 1, z, 0) > 0) return true;
    	if (isBlockProvidingPowerTo(w, x, y + 1, z, 1) > 0) return true;
    	if (isBlockProvidingPowerTo_NoChunkLoad(w, x, y, z - 1, 2) > 0) return true;
    	if (isBlockProvidingPowerTo_NoChunkLoad(w, x, y, z + 1, 3) > 0) return true;
    	if (isBlockProvidingPowerTo_NoChunkLoad(w, x - 1, y, z, 4) > 0) return true;
    	if (isBlockProvidingPowerTo_NoChunkLoad(w, x + 1, y, z, 5) > 0) return true;
    	return false;
    }

    /**
     * Is this block powering in the specified direction Args: x, y, z, direction
     */
    public static int isBlockProvidingPowerTo(World w, int x, int y, int z, int side)
    {
        return w.getBlock(x, y, z).isProvidingStrongPower(w, x, y, z, side);
    }

    public static int isBlockProvidingPowerTo_NoChunkLoad(World w, int x, int y, int z, int side)
    {
        if (!w.blockExists(x, y, z)) return 0;
    	return w.getBlock(x, y, z).isProvidingStrongPower(w, x, y, z, side);
    }
}
