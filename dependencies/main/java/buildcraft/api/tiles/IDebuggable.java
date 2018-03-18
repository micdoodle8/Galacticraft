package buildcraft.api.tiles;

import java.util.List;

import net.minecraft.util.EnumFacing;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IDebuggable {
    /** Get the debug information from a tile entity as a list of strings, used for the F3 debug menu. The left and
     * right parameters correspond to the sides of the F3 screen.
     * 
     * @param side The side the block was clicked on, may be null if we don't know, or is the "centre" side */
    void getDebugInfo(List<String> left, List<String> right, EnumFacing side);

    /** Same as {@link #getDebugInfo(List, List, EnumFacing)}, but only for client
     *
     * @param side same as for {@link #getDebugInfo(List, List, EnumFacing)} */
    @SideOnly(Side.CLIENT)
    default void getClientDebugInfo(List<String> left, List<String> right, EnumFacing side) {
    }
}
