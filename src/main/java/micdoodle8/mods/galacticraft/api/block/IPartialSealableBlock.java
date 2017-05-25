package micdoodle8.mods.galacticraft.api.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IPartialSealableBlock
{
    boolean isSealed(World world, BlockPos pos, EnumFacing direction);
}
