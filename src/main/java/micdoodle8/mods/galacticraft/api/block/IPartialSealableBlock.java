package micdoodle8.mods.galacticraft.api.block;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IPartialSealableBlock
{
    public boolean isSealed(World world, int x, int y, int z, EnumFacing direction);
}
