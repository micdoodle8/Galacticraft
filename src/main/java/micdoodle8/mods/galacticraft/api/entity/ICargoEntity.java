package micdoodle8.mods.galacticraft.api.entity;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Implement into entities that can be loaded with cargo
 */
public interface ICargoEntity
{
    public static enum EnumCargoLoadingState
    {
        FULL,
        EMPTY,
        NOTARGET,
        NOINVENTORY,
        SUCCESS
    }

    public static class RemovalResult
    {
        public final EnumCargoLoadingState resultState;
        @Nonnull
        public final ItemStack resultStack;

        public RemovalResult(EnumCargoLoadingState resultState, @Nonnull ItemStack resultStack)
        {
            this.resultState = resultState;
            this.resultStack = resultStack;
        }
    }

    public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd);

    public RemovalResult removeCargo(boolean doRemove);
}
