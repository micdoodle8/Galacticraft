package buildcraft.api.facades;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public interface IFacadeState {
    boolean isTransparent();

    IBlockState getBlockState();

    ItemStack getRequiredStack();
}
