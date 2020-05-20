package micdoodle8.mods.galacticraft.api.tile;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;

/**
 * Used for colored pipes to set/get colors
 */
public interface IColorable
{
    /**
     * Called when the color is changed
     */
    void onColorUpdate();

    /**
     * Gets the color of this tile from the Block State
     *
     * @return the color of the tile, equivalent to the dye colors in vanilla
     * minecraft
     */
    byte getColor(BlockState state);

    /**
     * Called when a tile adjacent to this one has it's color changed
     *
     * @param direction the direction (relative to this tile) that was updated.
     */
    void onAdjacentColorChanged(Direction direction);
}
