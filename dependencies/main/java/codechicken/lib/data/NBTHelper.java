package codechicken.lib.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by covers1624 on 6/3/2016.
 */
public class NBTHelper {

    public static void writeFluidStack(NBTTagCompound tag, FluidStack fluid) {
        if (fluid == null || FluidRegistry.getFluidName(fluid) == null) {
            tag.setString("id", "");
        } else {
            tag.setString("id", FluidRegistry.getFluidName(fluid));
            tag.setInteger("amount", fluid.amount);
            tag.setTag("tag", fluid.tag);
        }
    }

    public static FluidStack readFluidStack(NBTTagCompound tag) {
        FluidStack fluid = null;
        String fluidName = tag.getString("id");

        if (fluidName.length() > 0) {
            fluid = new FluidStack(FluidRegistry.getFluid(fluidName), tag.getInteger("amount"), tag.getCompoundTag("tag"));
        }

        return fluid;
    }

}
