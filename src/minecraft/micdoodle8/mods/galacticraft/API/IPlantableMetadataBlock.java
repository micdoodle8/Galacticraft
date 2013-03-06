package micdoodle8.mods.galacticraft.API;

/**
 * Extend this block to allow planting of saplings on it. Requires
 */
public interface IPlantableMetadataBlock
{
	/**
	 * @return amount of water blocks required for sapling to be growable. 4 is default.
	 */
	public int requiredLiquidBlocksNearby();
	
	/**
	 * @return array of metadata values that are considered valueable.
	 */
	public boolean isPlantable(int metadata);
}
