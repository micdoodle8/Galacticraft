package micdoodle8.mods.galacticraft.api.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * If block requires updates when oxygen is added and removed, implement this
 * into your block class.
 * <p>
 * It is recommended that blocks implementing this should be set to tick
 * randomly, and should override @updateTick() also to carry out oxygen checks.
 */
public interface IOxygenReliantBlock
{
    void onOxygenRemoved(World world, BlockPos pos, BlockState currentState);

    void onOxygenAdded(World world, BlockPos pos, BlockState currentState);
}
