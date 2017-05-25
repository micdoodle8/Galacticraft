package micdoodle8.mods.galacticraft.api.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITerraformableBlock
{
    /**
     * Determines if the block as this position is terraformable. This will only
     * be called for blocks inside the terraformer bubble. It is recommended
     * that you test whether the block can see the surface before returning
     * true.
     *
     * @param world World that the block is a part of
     * @param pos The position of the block
     * @return True if the block can be terraformed, false if not.
     */
    boolean isTerraformable(World world, BlockPos pos);
}
