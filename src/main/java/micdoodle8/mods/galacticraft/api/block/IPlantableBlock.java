package micdoodle8.mods.galacticraft.api.block;

/**
 * Extend this block to allow planting of saplings on it. Requires
 */
public interface IPlantableBlock
{
    /**
     * @return amount of water blocks required for sapling to be growable. 4 is
     * default.
     */
    public int requiredLiquidBlocksNearby();

    /**
     * @return is sapling can be placed on the provided metadata value, return true
     */
    public boolean isPlantable(int metadata);
}
