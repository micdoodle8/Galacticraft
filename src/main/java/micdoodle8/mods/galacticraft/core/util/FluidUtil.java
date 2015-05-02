package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class FluidUtil
{
	
	public static boolean isFuelContainerAny(ItemStack var4)
	{
    	if (var4.getItem() instanceof ItemCanisterGeneric)
    		return var4.getItem() == GCItems.fuelCanister && var4.getItemDamage() < var4.getMaxDamage();

    	FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(var4);
        return liquid != null && FluidRegistry.getFluidName(liquid).equalsIgnoreCase("fuel");
	}

	public static boolean isOilContainerAny(ItemStack var4)
	{
    	if (var4.getItem() instanceof ItemCanisterGeneric)
    		return var4.getItem() == GCItems.oilCanister && var4.getItemDamage() < var4.getMaxDamage();

    	FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(var4);
        return liquid != null && (FluidRegistry.getFluidName(liquid).equalsIgnoreCase("oil") || FluidRegistry.getFluidName(liquid).equalsIgnoreCase("oilgc"));
	}

	public static boolean isMethaneContainerAny(ItemStack var4)
	{
    	if (var4.getItem() instanceof ItemCanisterGeneric)
    		return var4.getItem() == AsteroidsItems.methaneCanister && var4.getItemDamage() < var4.getMaxDamage();

    	FluidStack stack = FluidContainerRegistry.getFluidForFilledItem(var4);
        return stack != null && stack.getFluid() != null && stack.getFluid().getName().toLowerCase().contains("methane");
	}

	public static boolean isFullContainer(ItemStack var4)
	{
    	if (var4.getItem() instanceof ItemCanisterGeneric)
    		return var4.getItemDamage() == 1;

    	FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(var4);
        return liquid != null;
	}

    public static void tryFillContainer(FluidTank tank, FluidStack liquid, ItemStack[] inventory, int slot, Item canisterType)
	{
		ItemStack slotItem = inventory[slot];
		boolean isCanister = slotItem.getItem() instanceof ItemCanisterGeneric;
		final int amountToFill = Math.min(liquid.amount, isCanister ? slotItem.getItemDamage() - 1 : FluidContainerRegistry.BUCKET_VOLUME);

		if (amountToFill <= 0 || (isCanister && slotItem.getItem() != canisterType && slotItem.getItemDamage() != ItemCanisterGeneric.EMPTY))
			return;
		
		if (isCanister)
		{
			inventory[slot] = new ItemStack(canisterType, 1, slotItem.getItemDamage() - amountToFill);
			tank.drain(amountToFill, true);
		}
		else if (amountToFill == FluidContainerRegistry.BUCKET_VOLUME)
		{
			inventory[slot] = FluidContainerRegistry.fillFluidContainer(liquid, inventory[slot]);

			if (inventory[slot] == null)
			{
				inventory[slot] = slotItem;
			}
			else
			{
				tank.drain(amountToFill, true);
			}
		}
	}

	/**
	 * Tests for either an empty canister, or a canister of the appropriate type with some capacity remaining
	 * Will also return true for empty Forge fluid containers
	 * 
	 * @param var4
	 * @param canisterType
	 * @return
	 */
    public static boolean isEmptyContainer(ItemStack var4, Item canisterType)
	{
    	if (var4.getItem() instanceof ItemCanisterGeneric)
    		return var4.getItemDamage() == ItemCanisterGeneric.EMPTY || (var4.getItem() == canisterType && var4.getItemDamage() > 1);
				
		return FluidContainerRegistry.isEmptyContainer(var4);
	}

	/**
	 * Tests for either an empty canister, or a canister of the appropriate type with some capacity remaining
	 * Will also return true for empty Forge fluid containers
	 * 
	 * @param var4
	 * @param canisterType
	 * @return
	 */
    public static boolean isEmptyContainerFor(ItemStack var4, FluidStack targetFluid)
	{
    	if (var4.getItem() instanceof ItemCanisterGeneric)
    	{
    		if (var4.getItemDamage() == ItemCanisterGeneric.EMPTY) return true;
    		if (var4.getItemDamage() == 1) return false;
    		
    		return fluidsSame(((ItemCanisterGeneric)var4.getItem()).getFluid(var4), targetFluid);
    	}
				
		if (FluidContainerRegistry.isEmptyContainer(var4)) return true;
		
        return fluidsSame(FluidContainerRegistry.getFluidForFilledItem(var4), targetFluid);               
	}

    /**
     * 
     * @param fs1 First FluidStack to compare
     * @param fs2 Second FluidStack to compare
     * @return  True if the FluidStacks are both not null and the same fluid type
     * 		False otherwise
     */
    public static boolean fluidsSame(FluidStack fs1, FluidStack fs2)
    {
    	if (fs1 == null || fs2 == null) return false;
    	Fluid f1 = fs1.getFluid();
    	Fluid f2 = fs2.getFluid();
    	if (f1 == null || f2 == null || f1.getName() == null) return false;
		return f1.getName().equals(f2.getName());
	}

    public static boolean isEmptyContainer(ItemStack var4)
	{
    	if (var4.getItem() instanceof ItemCanisterGeneric)
    		return var4.getItemDamage() == ItemCanisterGeneric.EMPTY;
				
		return FluidContainerRegistry.isEmptyContainer(var4);
	}

	public static boolean isEmptyGasContainer(ItemStack var4)
	{
		return false;
	}

	public static boolean isFilledContainer(ItemStack var4)
	{
    	if (var4.getItem() instanceof ItemCanisterGeneric)
    		return var4.getItemDamage() < ItemCanisterGeneric.EMPTY;
        return FluidContainerRegistry.getFluidForFilledItem(var4) != null;
	}

	public static boolean isWaterContainer(ItemStack var4)
	{
        FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(var4);
        return liquid != null && liquid.getFluid() != null && liquid.getFluid().getName().equals("water");
	}

	public static boolean isValidContainer(ItemStack slotItem)
	{
		return slotItem != null && slotItem.stackSize == 1 && (slotItem.getItem() instanceof ItemCanisterGeneric || FluidContainerRegistry.isContainer(slotItem));
	}

	public static ItemStack getUsedContainer(ItemStack container)
	{
        if (FluidContainerRegistry.isBucket(container) && FluidContainerRegistry.isFilledContainer(container))
        {
            return new ItemStack(Items.bucket, container.stackSize);
        }
        else
        {
            container.stackSize--;

            if (container.stackSize == 0)
            {
                return null;
            }
            
            return container;
        }
	}
}
