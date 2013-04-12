package micdoodle8.mods.galacticraft.API;

import net.minecraft.world.World;

public interface IOxygenReliantBlock
{
	public void onOxygenRemoved(World world, int x, int y, int z);

	public void onOxygenAdded(World world, int x, int y, int z);
}
