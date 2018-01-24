package micdoodle8.mods.galacticraft.core.items;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class ItemCanisterGenericHandler implements IFluidHandlerItem, ICapabilityProvider
{
    @Nonnull
    protected ItemStack container;

    public ItemCanisterGenericHandler(@Nonnull ItemStack container)
    {
        this.container = container;
    }

    @Nonnull
    @Override
    public ItemStack getContainer()
    {
    	return container;  //never varies
    }

    @Nullable
    public FluidStack getFluid()
    {
        if (container.getCount() > 0 && container.getItem() instanceof ItemCanisterGeneric)
        {
        	return ((ItemCanisterGeneric)container.getItem()).getFluid(container);
        }
    	return null;
    }

    protected void setFluid(FluidStack fluid)
    {
        if (this.canFillFluidType(fluid))
        {
        	((ItemCanisterGeneric)container.getItem()).setDamage(container, ItemCanisterGeneric.EMPTY - fluid.amount);
        }
    }

    @Override
    public IFluidTankProperties[] getTankProperties()
    {
        if (container.getCount() > 0 && container.getItem() instanceof ItemCanisterGeneric)
        {
        	return new FluidTankProperties[] { new FluidTankProperties(getFluid(), ItemCanisterGeneric.EMPTY - 1) };
        }
        return new FluidTankProperties[0];
    }

    @Override
    public int fill(FluidStack resource, boolean doFill)
    {
        if (container.getCount() > 0 && container.getItem() instanceof ItemCanisterGeneric)
        {
        	return ((ItemCanisterGeneric)container.getItem()).fill(container, resource, doFill);
        }
        return 0;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain)
    {
        if (container.getCount() > 0 && container.getItem() instanceof ItemCanisterGeneric)
        {
        	return ((ItemCanisterGeneric)container.getItem()).drain(container, maxDrain, doDrain);
        }
        return null;
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain)
    {
        if (this.canDrainFluidType(resource))
        {
        	return ((ItemCanisterGeneric)container.getItem()).drain(container, resource.amount, doDrain);
        }
        return null;
    }

    public boolean canFillFluidType(FluidStack fluid)
    {
        if (container.getCount() > 0 && container.getItem() instanceof ItemCanisterGeneric && fluid != null && fluid.getFluid() != null)
        {
        	return ((ItemCanisterGeneric)container.getItem()).getAllowedFluid().equalsIgnoreCase(fluid.getFluid().getName());
        }
        return false;
    }

    public boolean canDrainFluidType(FluidStack fluid)
    {
        return this.canFillFluidType(fluid);
    }

    protected void setContainerToEmpty()
    {
        if (container.getCount() > 0 && container.getItem() instanceof ItemCanisterGeneric)
        {
        	((ItemCanisterGeneric)container.getItem()).setDamage(container, ItemCanisterGeneric.EMPTY);;
        }
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY ? (T) this : null;
    }
}

