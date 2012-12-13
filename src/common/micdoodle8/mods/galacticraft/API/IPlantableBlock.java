package micdoodle8.mods.galacticraft.API;

import net.minecraft.src.Block;
import net.minecraft.src.Material;

/**
 * Extend this block to allow planting of saplings on it. Requires 
 */
public interface IPlantableBlock
{
	/**
	 * @return amount of water blocks required for sapling to be growable. 4 is default.
	 */
	public int requiredLiquidBlocksNearby();
}
