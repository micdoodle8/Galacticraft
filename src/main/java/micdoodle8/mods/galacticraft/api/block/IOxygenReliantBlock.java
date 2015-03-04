package micdoodle8.mods.galacticraft.api.block;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * If block requires updates when oxygen is added and removed, implement this
 * into your block class.
 */
public interface IOxygenReliantBlock
{
    public void onOxygenRemoved(World world, BlockPos pos);

    public void onOxygenAdded(World world, BlockPos pos);
}
