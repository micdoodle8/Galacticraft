package buildcraft.api.transport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

import buildcraft.api.transport.pipe.IPipeHolder;
import buildcraft.api.transport.pluggable.PipePluggable;

/** Designates an item that can be placed onto a pipe as a {@link PipePluggable}. */
public interface IItemPluggable {
    /** Called when this item is placed onto a pipe holder. This can return null if this item does not make a valid
     * pluggable. Note that if you return a non-null pluggable then it will *definitely* be added to the pipe, and you
     * are responsible for making all the effects yourself (like the sound effect).
     * 
     * @param stack The stack that holds this item
     * @param holder The pipe holder
     * @param side The side that the pluggable should be placed on
     * @return A pluggable to place onto the pipe */
    @Nullable
    PipePluggable onPlace(@Nonnull ItemStack stack, IPipeHolder holder, EnumFacing side, EntityPlayer player,
        EnumHand hand);
}
