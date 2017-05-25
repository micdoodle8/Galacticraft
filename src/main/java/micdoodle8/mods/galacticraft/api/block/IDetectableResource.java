package micdoodle8.mods.galacticraft.api.block;

import net.minecraft.block.state.IBlockState;
/**
 * Implement this interface to let Sensor Goggles see your block.
 */
public interface IDetectableResource
{
    /**
     * @return array of metadata values that are considered valueable.
     */
    boolean isValueable(IBlockState metadata);
}
