package micdoodle8.mods.galacticraft.api.block;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public interface IPartialSealableBlock
{
	public boolean isSealed(World world, int x, int y, int z, ForgeDirection direction);
}
