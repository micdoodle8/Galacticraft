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

    /** Return the description in the UI. Note that this should NEVER be called directly, instead this acts as a bridge
     * for {@link #getTooltip()}. (As such this might return null or throw an exception) */
    @SideOnly(Side.CLIENT)
    String getDescription();

    /** @return The full tooltip for the UI. */
    @SideOnly(Side.CLIENT)
    default List<String> getTooltip() {
        String desc = getDescription();
        if (desc == null) {
            return ImmutableList.of();
        }
        return ImmutableList.of(desc);
    }

    /** @return A sprite to show in a GUI, or null if this should not render a sprite. */
    @SideOnly(Side.CLIENT)
    @Nullable
    ISprite getSprite();
}
