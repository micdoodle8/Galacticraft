package micdoodle8.mods.galacticraft.api.block;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPartialSealableBlock
{
    boolean isSealed(World world, BlockPos pos, Direction direction);
}
