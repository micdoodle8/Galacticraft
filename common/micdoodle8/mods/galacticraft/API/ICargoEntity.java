package micdoodle8.mods.galacticraft.API;

import net.minecraft.item.ItemStack;

public interface ICargoEntity
{
    public static enum EnumCargoLoadingState
    {
        FULL, EMPTY, NOTARGET, NOINVENTORY, SUCCESS;
    }

    public static class RemovalResult
    {
        public final EnumCargoLoadingState resultState;
        public final ItemStack resultStack;

        public RemovalResult(EnumCargoLoadingState resultState, ItemStack resultStack)
        {
            this.resultState = resultState;
            this.resultStack = resultStack;
        }
    }

    public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd);

    public RemovalResult removeCargo(boolean doRemove);
}
