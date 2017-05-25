package micdoodle8.mods.galacticraft.api.block;

import net.minecraft.block.state.IBlockState;

/**
 * Extend this block to allow planting of saplings on it. Requires
 */
public interface IPlantableBlock
{
    /**
     * @return amount of water blocks required for sapling to be growable. 4 is
     * default.
     */
    int requiredLiquidBlocksNearby();

    /**
     * @return is sapling can be placed on the provided metadata value, return true
     */
    boolean isPlantable(IBlockState state);
}
