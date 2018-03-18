package buildcraft.api.statements;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import buildcraft.api.core.render.ISprite;

public interface IGuiSlot {
    /** Every statement needs a unique tag, it should be in the format of "&lt;modid&gt;:&lt;name&gt;".
     *
     * @return the unique id */
    String getUniqueTag();

    /** Return the description in the UI. */
    String getDescription();

    /** @return The full tooltip for the UI. */
    default List<String> getTooltip() {
        String desc = getDescription();
        if (desc == null) {
            return ImmutableList.of();
        }
        return ImmutableList.of(desc);
    }

    /** @return A sprite to show in a GUI or in-world (so this must be stitched into the block texture atlas), or null
     *         if this should not render a sprite. */
    @SideOnly(Side.CLIENT)
    @Nullable
    ISprite getSprite();
}
