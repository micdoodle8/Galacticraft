package buildcraft.api.items;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import net.minecraftforge.fluids.FluidStack;

public interface IItemFluidShard {
    void addFluidDrops(NonNullList<ItemStack> toDrop, @Nullable FluidStack fluid);
}
