package micdoodle8.mods.galacticraft.api.block;

import net.minecraft.world.World;

/**
 * If block requires updates when oxygen is added and removed, implement this
 * into your block class.
 */
public interface IOxygenReliantBlock
{
	public void onOxygenRemoved(World world, int x, int y, int z);

	public void onOxygenAdded(World world, int x, int y, int z);
}
