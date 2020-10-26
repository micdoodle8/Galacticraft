package micdoodle8.mods.galacticraft.core.items;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import micdoodle8.mods.galacticraft.core.GCItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class ItemCanisterGenericHandler implements IFluidHandlerItem, ICapabilityProvider
{
    private final LazyOptional<IFluidHandlerItem> holder = LazyOptional.of(() -> this);
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

    @Nonnull
    public FluidStack getFluid()
    {
        if (container.getCount() > 0 && container.getItem() instanceof ItemCanisterGeneric)
        {
            return ((ItemCanisterGeneric) container.getItem()).getFluid(container);
        }
        return FluidStack.EMPTY;
    }

    protected void setFluid(FluidStack fluid)
    {
        if (this.canFillFluidType(fluid))
        {
            container.getItem().setDamage(container, ItemCanisterGeneric.EMPTY_CAPACITY - fluid.getAmount());
        }
    }

    @Override
    public int getTanks()
    {
        return 1;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank)
    {
        return getFluid();
    }

    @Override
    public int getTankCapacity(int tank)
    {
        return ItemCanisterGeneric.EMPTY_CAPACITY - 1;
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack)
    {
        int capacityPlusOne = container.getDamage();
        if (capacityPlusOne >= ItemCanisterGeneric.EMPTY_CAPACITY)
        {
            for (ItemCanisterGeneric i : GCItems.canisterTypes)
            {
                if (stack.getFluid().getRegistryName() == i.getAllowedFluid())
                {
                    return true;
                }
            }
        }

        return stack.getFluid().getRegistryName() == ((ItemCanisterGeneric) container.getItem()).getAllowedFluid();
    }

    //    @Override
//    public IFluidTankProperties[] getTankProperties()
//    {
//        if (container.getCount() > 0 && container.getItem() instanceof ItemCanisterGeneric)
//        {
//        	return new FluidTankProperties[] { new FluidTankProperties(getFluid(), ItemCanisterGeneric.EMPTY - 1) };
//        }
//        return new FluidTankProperties[0];
//    }

    @Override
    public int fill(FluidStack resource, IFluidHandler.FluidAction action)
    {
        if (container.getCount() > 0 && container.getItem() instanceof ItemCanisterGeneric)
        {
            return ((ItemCanisterGeneric) container.getItem()).fill(container, resource, action);
        }
        return 0;
    }

    @Override
    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action)
    {
        if (container.getCount() > 0 && container.getItem() instanceof ItemCanisterGeneric)
        {
            return ((ItemCanisterGeneric) container.getItem()).drain(container, maxDrain, action);
        }
        return null;
    }

    @Override
    public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action)
    {
        if (this.canDrainFluidType(resource))
        {
            return ((ItemCanisterGeneric) container.getItem()).drain(container, resource.getAmount(), action);
        }
        return null;
    }

    public boolean canFillFluidType(FluidStack fluid)
    {
        if (container.getCount() > 0 && container.getItem() instanceof ItemCanisterGeneric && fluid != null && fluid != FluidStack.EMPTY)
        {
            return ((ItemCanisterGeneric) container.getItem()).getAllowedFluid() == fluid.getFluid().getRegistryName();
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
            container.getItem().setDamage(container, ItemCanisterGeneric.EMPTY_CAPACITY);
        }
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
    {
        return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.orEmpty(capability, holder);
    }
}

