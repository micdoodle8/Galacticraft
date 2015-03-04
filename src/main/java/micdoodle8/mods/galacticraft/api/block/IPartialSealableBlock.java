package micdoodle8.mods.galacticraft.api.block;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IPartialSealableBlock
{
    public boolean isSealed(World worldIn, BlockPos pos, EnumFacing direction);
}
