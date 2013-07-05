package micdoodle8.mods.galacticraft.API.block;

import net.minecraft.world.World;

public interface IPartialSealedBlock
{
    public boolean isSealed(World world, int x, int y, int z);
}
