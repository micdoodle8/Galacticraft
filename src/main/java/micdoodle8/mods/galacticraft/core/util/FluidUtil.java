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

	/**
	 * This looks for any type of filled or partly filled container of "fuel"
	 * @param var4  The ItemStack being examined
	 * @return     True if it's a container with "fuel"
	 */
	public static boolean isFuelContainerAny(ItemStack var4)
	{
    	if (var4.getItem() instanceof ItemCanisterGeneric)
    		return var4.getItem() == GCItems.fuelCanister && var4.getItemDamage() < var4.getMaxDamage();

    	FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(var4);
        return liquid != null && FluidRegistry.getFluidName(liquid).equalsIgnoreCase("fuel");
	}

	/**
	 * This looks for any type of filled or partly filled container of "oil"
	 * or the alternative fluid "oilgc" which may be present if Buildcraft was installed on top of a GC world
	 * @param var4  The ItemStack being examined
	 * @return     True if it's a container with "oil"
	 */
	public static boolean isOilContainerAny(ItemStack var4)
	{
    	if (var4.getItem() instanceof ItemCanisterGeneric)
    		return var4.getItem() == GCItems.oilCanister && var4.getItemDamage() < var4.getMaxDamage();

    	FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(var4);
        return liquid != null && (FluidRegistry.getFluidName(liquid).equalsIgnoreCase("oil") || FluidRegistry.getFluidName(liquid).equalsIgnoreCase("oilgc"));
	}

	/**
	 * This looks for any type of filled or partly filled container of "methane" gas
	 * @param var4  The ItemStack being examined
	 * @return     True if it's a container with "methane"
	 */
	public static boolean isMethaneContainerAny(ItemStack var4)
	{
    	if (var4.getItem() instanceof ItemCanisterGeneric)
    		return var4.getItem() == AsteroidsItems.methaneCanister && var4.getItemDamage() < var4.getMaxDamage();

    	FluidStack stack = FluidContainerRegistry.getFluidForFilledItem(var4);
        return stack != null && stack.getFluid() != null && stack.getFluid().getName().toLowerCase().contains("methane");
	}

	/**
	 * This looks for any type of completely full container
	 * Used, for example, in canExtractItem() logic
	 * @param var4  The ItemStack being examined
	 * @return     True if it's a full container
	 */
	public static boolean isFullContainer(ItemStack var4)
	{
    	if (var4.getItem() instanceof ItemCanisterGeneric)
    		return var4.getItemDamage() == 1;

    	FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(var4);
        return liquid != null;
	}

	/**
	 * This tries to fill the given container (at inventory[slot]) with fluid from the specified tank
	 * If successful, it places the resulting filled container in inventory[slot]
	 * 
	 * Note: this deals with the issue where FluidContainerRegistry.fillFluidContainer() returns null for failed fills
	 * 
	 * @param tank		The tank to take the fluid from
	 * @param liquid   The type of liquid in that tank (the calling method will normally have checked this already)
	 * @param inventory
	 * @param slot
	 * @param canisterType  The type of canister to return, if it's a canister being filled (pre-matched with the liquid type)
	 * 
	 */
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
	 * Tests for any type of container with some space in it
	 * It can be either an empty container, or a Galacticraft canister
	 * of the appropriate type, either empty or at least with some capacity remaining
	 * Will return true for any type of empty Forge fluid container including vanilla buckets
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
	 * Tests for any empty container which can accept the specified fluid
	 * Either Galacticraft canisters or Forge containers
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

    /**
     * Test for any completely empty container of either type
     * Used, for example, in canExtractItem() logic
     * @param var4  The ItemStack being tested
     * @return  True if the container is empty
     */
    public static boolean isEmptyContainer(ItemStack var4)
	{
    	if (var4.getItem() instanceof ItemCanisterGeneric)
    		return var4.getItemDamage() == ItemCanisterGeneric.EMPTY;
				
		return FluidContainerRegistry.isEmptyContainer(var4);
	}

    /**
     * Test for any empty specialist gas container
     * For future use, e.g. for compatibility with Mekanism tank items
     * @param var4  The ItemStack being tested
     * @return  True if the container is empty
     */
	public static boolean isEmptyGasContainer(ItemStack var4)
	{
		return false;
	}

    /**
     * Test for any container with some level of contents
     * Used, for example, in shift-clicking items into input item slots
     * @param var4  The ItemStack being tested
     * @return  True if the container contains something
     */
	public static boolean isFilledContainer(ItemStack var4)
	{
    	if (var4.getItem() instanceof ItemCanisterGeneric)
    		return var4.getItemDamage() < ItemCanisterGeneric.EMPTY;
        return FluidContainerRegistry.getFluidForFilledItem(var4) != null;
	}

    /**
     * Test for any container with water
     * (vanilla or Forge items only, Galacticraft does not have a water container)
     * @param var4  The ItemStack being tested
     * @return  True if the container contains water
     */
	public static boolean isWaterContainer(ItemStack var4)
	{
        FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(var4);
        return liquid != null && liquid.getFluid() != null && liquid.getFluid().getName().equals("water");
	}

	/**
	 * Test for any container type at all
	 * Used, for example, in isItemValidForSlot() logic
	 * @param slotItem
	 * @return  True if it is a container; False if it is null or not a container 
	 */
	public static boolean isValidContainer(ItemStack slotItem)
	{
		return slotItem != null && slotItem.stackSize == 1 && (slotItem.getItem() instanceof ItemCanisterGeneric || FluidContainerRegistry.isContainer(slotItem));
	}

	/**
	 * Returns the used (empty) container, for example an empty bucket
	 * Used, for example, in isItemValidForSlot() logic
	 * @param slotItem
	 * @return  True if it is a container; False if it is null or not a container 
	 */
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
