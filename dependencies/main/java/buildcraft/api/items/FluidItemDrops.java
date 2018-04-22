package buildcraft.api.items;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public class FluidItemDrops {

    public static IItemFluidShard item;

    public static void addFluidDrops(NonNullList<ItemStack> toDrop, FluidStack... fluids) {
        if (item != null) {
            for (FluidStack fluid : fluids) {
                item.addFluidDrops(toDrop, fluid);
            }
        }
    }

    public static void addFluidDrops(NonNullList<ItemStack> toDrop, IFluidTank... tanks) {
        if (item != null) {
            for (IFluidTank tank : tanks) {
                item.addFluidDrops(toDrop, tank.getFluid());
            }
        }
    }
}
