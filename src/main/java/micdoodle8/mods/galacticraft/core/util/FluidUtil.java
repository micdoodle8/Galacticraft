package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.core.GCFluids;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FluidUtil
{
    private static boolean oldFluidIDMethod = true;
    private static Class<?> fluidStackClass = null;
    private static Method getFluidMethod = null;
    private static Field fluidIdField = null;

    /**
     * This looks for any type of filled or partly filled container of "fuel"
     *
     * @param var4 The ItemStack being examined
     * @return True if it's a container with "fuel"
     */
    public static boolean isFuelContainerAny(ItemStack var4)
    {
        if (var4.getItem() instanceof ItemCanisterGeneric)
        {
            return var4.getItem() == GCItems.fuelCanister && var4.getItemDamage() < var4.getMaxDamage();
        }

        FluidStack liquid = net.minecraftforge.fluids.FluidUtil.getFluidContained(var4);
        return liquid != null && FluidUtil.testFuel(FluidRegistry.getFluidName(liquid));
    }

    /**
     * Tests whether the named liquid is fuel / fuelgc
     * or rocket fuel from another mod, for compatibility (includes RotaryCraft Jet Fuel)
     *
     * @param name - which MUST be in lowercase, as always returned by FluidRegistry.getFluidName
     * @return true if a type of recognised fuel, false if not
     */
    public static boolean testFuel(String name)
    {
        if (name.startsWith("fuel"))
        {
            return true;
        }

        if (name.contains("rocket") && name.contains("fuel"))
        {
            return true;
        }

        if (name.equals("rc jet fuel"))
        {
            return true;
        }

        return false;
    }

    /**
     * Fill Galacticraft entities (e.g. rockets, buggies) with Galacticraft fuel.
     * For legacy reasons, accepts either "fuel" or "fuelgc".
     * Auto-converts either one to the same type of fuel as is already contained.
     *
     * @param tank   The tank
     * @param liquid A FluidStack being the fuel offered
     * @param doFill True if this is not a simulation / tank capacity test
     * @return The amount filled
     */
    public static int fillWithGCFuel(FluidTank tank, FluidStack liquid, boolean doFill)
    {
        if (liquid != null && testFuel(FluidRegistry.getFluidName(liquid)))
        {
            final FluidStack liquidInTank = tank.getFluid();

            //If the tank is empty, fill it with the current type of GC fuel
            if (liquidInTank == null)
            {
                return tank.fill(new FluidStack(GCFluids.fluidFuel, liquid.amount), doFill);
            }

            //If the tank already contains something, fill it with more of the same
			if (liquidInTank.amount < tank.getCapacity())
            {
                return tank.fill(new FluidStack(liquidInTank, liquid.amount), doFill);
            }
        }

        return 0;
    }

    /**
     * This looks for any type of filled or partly filled container of "oil"
     * or the alternative fluid "oilgc" which may be present if Buildcraft was installed on top of a GC world
     *
     * @param var4 The ItemStack being examined
     * @return True if it's a container with "oil"
     */
    public static boolean isOilContainerAny(ItemStack var4)
    {
        if (var4.getItem() instanceof ItemCanisterGeneric)
        {
            return var4.getItem() == GCItems.oilCanister && var4.getItemDamage() < var4.getMaxDamage();
        }

        FluidStack liquid = net.minecraftforge.fluids.FluidUtil.getFluidContained(var4);
        return liquid != null && FluidRegistry.getFluidName(liquid).startsWith("oil");
    }

    /**
     * This looks for any type of filled or partly filled container of "methane" gas
     *
     * @param var4 The ItemStack being examined
     * @return True if it's a container with "methane"
     */
    public static boolean isMethaneContainerAny(ItemStack var4)
    {
        if (var4.getItem() instanceof ItemCanisterGeneric)
        {
            return var4.getItem() == AsteroidsItems.methaneCanister && var4.getItemDamage() < var4.getMaxDamage();
        }

        FluidStack stack = net.minecraftforge.fluids.FluidUtil.getFluidContained(var4);
        return stack != null && stack.getFluid() != null && stack.getFluid().getName().toLowerCase().contains("methane");
    }

    /**
     * This looks for any type of completely full container
     * Used, for example, in canExtractItem() logic
     *
     * @param var4 The ItemStack being examined
     * @return True if it's a full container
     */
    public static boolean isFullContainer(ItemStack var4)
    {
        if (var4.getItem() instanceof ItemCanisterGeneric)
        {
            return var4.getItemDamage() == 1;
        }

        FluidStack liquid = net.minecraftforge.fluids.FluidUtil.getFluidContained(var4);
        return liquid != null;
    }

    /**
     * This tries to fill the given container (at inventory[slot]) with fluid from the specified tank
     * If successful, it places the resulting filled container in inventory[slot]
     * <p>
     * Note: this deals with the issue where FluidContainerRegistry.fillFluidContainer() returns null for failed fills
     *
     * @param tank         The tank to take the fluid from
     * @param liquid       The type of liquid in that tank (the calling method will normally have checked this already)
     * @param inventory
     * @param slot
     * @param canisterType The type of canister to return, if it's a canister being filled (pre-matched with the liquid type)
     */
    public static void tryFillContainer(FluidTank tank, FluidStack liquid, NonNullList<ItemStack> inventory, int slot, Item canisterType)
    {
        ItemStack slotItem = inventory.get(slot);
        boolean isCanister = slotItem.getItem() instanceof ItemCanisterGeneric;
        final int amountToFill = Math.min(liquid.amount, isCanister ? slotItem.getItemDamage() - 1 : Fluid.BUCKET_VOLUME);

        if (amountToFill <= 0 || (isCanister && slotItem.getItem() != canisterType && slotItem.getItemDamage() != ItemCanisterGeneric.EMPTY))
        {
            return;
        }

        if (isCanister)
        {
            inventory.set(slot, new ItemStack(canisterType, 1, slotItem.getItemDamage() - amountToFill));
            tank.drain(amountToFill, true);
        }
        else if (amountToFill == Fluid.BUCKET_VOLUME)
        {
            IFluidHandlerItem handler = net.minecraftforge.fluids.FluidUtil.getFluidHandler(inventory.get(slot));

            if (handler != null)
            {
                int used = handler.fill(liquid, true);
                tank.drain(used, true);
            }
            else
            {
                //Failed to fill container: restore item that was there before
                inventory.set(slot, slotItem);
            }
        }
    }

    /**
     * @param tank
     * @param inventory
     * @param slot
     */
    public static void tryFillContainerFuel(FluidTank tank, NonNullList<ItemStack> inventory, int slot)
    {
        if (FluidUtil.isValidContainer(inventory.get(slot)))
        {
            FluidStack liquid = tank.getFluid();

            if (liquid != null && liquid.amount > 0)
            {
                String liquidname = liquid.getFluid().getName();

                //Test for the GC fuels (though anything similarly named would also pass here)
                if (liquidname.startsWith("fuel"))
                {
                    //Make sure it is the current GC fuel
                    if (!liquidname.equals(GCFluids.fluidFuel.getName()))
                    {
                        liquid = new FluidStack(GCFluids.fluidFuel, liquid.amount);
                    }

                    //But match any existing fuel fluid in the container
                    ItemStack stack = inventory.get(slot);
                    IFluidHandlerItem handler = net.minecraftforge.fluids.FluidUtil.getFluidHandler(stack);
                    if (handler != null)
                    {
                        FluidStack existingFluid = net.minecraftforge.fluids.FluidUtil.getFluidContained(stack);
                        if (existingFluid != null && !existingFluid.getFluid().getName().equals(GCFluids.fluidFuel.getName()))
                        {
                            liquid = new FluidStack(existingFluid, liquid.amount);
                        }
                    }

                    FluidUtil.tryFillContainer(tank, liquid, inventory, slot, GCItems.fuelCanister);
                }
            }
        }
    }

    /**
     * This tries to empty the container (at inventory[slot]) into the specified tank
     * forcing the tank contents to Fluid: desiredLiquid even if the container had
     * something different (useful for different types of fuel, for example). 
     * If successful, it replaces inventory[slot] with the corresponding empty container
     * <p>
     * 
     * @param tank         The tank to fill with the fluid
     * @param desiredLiquid       The type of liquid intended for that tank
     * @param stacks
     * @param slot
     * @param amountOffered  The amount in the container being offered
     */
    public static void loadFromContainer(FluidTank tank, Fluid desiredLiquid, NonNullList<ItemStack> stacks, int slot, int amountOffered)
    {
        ItemStack slotItem = stacks.get(1);

        if (slotItem.getItem() instanceof ItemCanisterGeneric)
        {
            int originalDamage = slotItem.getItemDamage();
            int used = tank.fill(new FluidStack(desiredLiquid, ItemCanisterGeneric.EMPTY - originalDamage), true);
            if (originalDamage + used >= ItemCanisterGeneric.EMPTY)
            {
            	stacks.set(1, new ItemStack(GCItems.oilCanister, 1, ItemCanisterGeneric.EMPTY));
            }
            else
            {
            	stacks.set(1, new ItemStack(slotItem.getItem(), 1, originalDamage + used));
            }
        }
        else
        {

        	if (tank.getFluid() == null || amountOffered <= tank.getCapacity() - tank.getFluid().amount)
            {
                tank.fill(new FluidStack(desiredLiquid, amountOffered), true);

                if (FluidUtil.isFilledContainer(slotItem))
                {
                    final int bucketCount = slotItem.getCount();
                    if (FluidUtil.isBucket(slotItem))
                    {
                        if (bucketCount > 1)
                        {
                            tank.fill(new FluidStack(desiredLiquid, (bucketCount - 1) * Fluid.BUCKET_VOLUME), true);
                        }
                        stacks.set(1, new ItemStack(Items.BUCKET, bucketCount));
                    }
                    else
                    {
                        ItemStack emptyStack = FluidUtil.getUsedContainer(slotItem);
                        if (bucketCount > 1)
                        {
                            tank.fill(new FluidStack(desiredLiquid, (bucketCount - 1) * FluidUtil.getContainerCapacity(slotItem)), true);
                            if (emptyStack != null)
                            {
                                emptyStack.setCount(bucketCount);
                            }
                        }
                        stacks.set(1, emptyStack);
                    }
                }
                else
                {
                    slotItem.shrink(1);
                }
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
    public static boolean isPartialContainer(ItemStack var4, Item canisterType)
    {
        if (var4.getItem() instanceof ItemCanisterGeneric)
        {
            return var4.getItemDamage() == ItemCanisterGeneric.EMPTY || (var4.getItem() == canisterType && var4.getItemDamage() > 1);
        }

        IFluidHandlerItem handler = net.minecraftforge.fluids.FluidUtil.getFluidHandler(var4);
        FluidStack containedFluid = net.minecraftforge.fluids.FluidUtil.getFluidContained(var4);

        if (handler != null)
        {
            if (containedFluid == null)
            {
                return true;
            }

            // Check that there is at least some space in container
            containedFluid = containedFluid.copy();
            containedFluid.amount = 1;
            if (handler.fill(containedFluid, false) > 0)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Tests for any empty container which can accept the specified fluid
     * Either Galacticraft canisters or Forge containers
     *
     * @param var4
     * @param targetFluid
     * @return
     */
    public static boolean isEmptyContainerFor(ItemStack var4, FluidStack targetFluid)
    {
        if (var4.getItem() instanceof ItemCanisterGeneric)
        {
            if (var4.getItemDamage() == ItemCanisterGeneric.EMPTY)
            {
                return true;
            }
            if (var4.getItemDamage() == 1)
            {
                return false;
            }

            return fluidsSame(FluidUtil.getFluidContained(var4), targetFluid);
        }

        if (isEmptyContainer(var4))
        {
            return true;
        }

        return fluidsSame(net.minecraftforge.fluids.FluidUtil.getFluidContained(var4), targetFluid);
    }

    /**
     * Returns the amount of fluid this container can provide in its current state
     * (Similar to Forge's FluidUtil.getFluidContained()) 
     * 
     * @param container
     * @return
     */
    public static int getContainerCapacity(ItemStack container)
    {
        if (container.getItem() instanceof ItemCanisterGeneric)
        {
            return container.getItemDamage() - 1;
        }
        if (!container.isEmpty())
        {
            container = container.copy();
            container.setCount(1);
            IFluidHandlerItem fluidHandler = net.minecraftforge.fluids.FluidUtil.getFluidHandler(container);
            if (fluidHandler != null)
            {
                FluidStack drain = fluidHandler.drain(Integer.MAX_VALUE, false);
                return drain == null ? 0 : drain.amount;
            }
        }
        return 0;
	}

    /**
     * @param fs1 First FluidStack to compare
     * @param fs2 Second FluidStack to compare
     * @return True if the FluidStacks are both not null and the same fluid type
     * False otherwise
     */
    public static boolean fluidsSame(FluidStack fs1, FluidStack fs2)
    {
        if (fs1 == null || fs2 == null)
        {
            return false;
        }
        Fluid f1 = fs1.getFluid();
        Fluid f2 = fs2.getFluid();
        if (f1 == null || f2 == null || f1.getName() == null)
        {
            return false;
        }
        return f1.getName().equals(f2.getName());
    }

    /**
     * Test for any completely empty container of either type
     * Used, for example, in canExtractItem() logic
     *
     * @param var4 The ItemStack being tested
     * @return True if the container is empty
     */
    public static boolean isEmptyContainer(ItemStack var4)
    {
        if (var4.getItem() instanceof ItemCanisterGeneric)
        {
            return var4.getItemDamage() == ItemCanisterGeneric.EMPTY;
        }

        IFluidHandlerItem handler = net.minecraftforge.fluids.FluidUtil.getFluidHandler(var4);
        FluidStack containedFluid = net.minecraftforge.fluids.FluidUtil.getFluidContained(var4);
        return handler != null && (containedFluid == null || containedFluid.amount == 0);
    }

    /**
     * Test for any empty specialist gas container
     * For future use, e.g. for compatibility with Mekanism tank items
     *
     * @param var4 The ItemStack being tested
     * @return True if the container is empty
     */
    public static boolean isEmptyGasContainer(ItemStack var4)
    {
        return false;
    }

    /**
     * Test for any container with some level of contents
     * Used, for example, in shift-clicking items into input item slots
     *
     * @param var4 The ItemStack being tested
     * @return True if the container contains something
     */
    public static boolean isFilledContainer(ItemStack var4)
    {
        if (net.minecraftforge.fluids.FluidUtil.getFluidHandler(var4) == null)
        {
            return false;
        }
        if (var4.getItem() instanceof ItemCanisterGeneric)
        {
            return var4.getItemDamage() < ItemCanisterGeneric.EMPTY;
        }
        return net.minecraftforge.fluids.FluidUtil.getFluidContained(var4) != null;
    }

    /**
     * Test for any container with water
     * (vanilla or Forge items only, Galacticraft does not have a water container)
     *
     * @param var4 The ItemStack being tested
     * @return True if the container contains water
     */
    public static boolean isWaterContainer(ItemStack var4)
    {
        FluidStack liquid = net.minecraftforge.fluids.FluidUtil.getFluidContained(var4);
        return liquid != null && liquid.getFluid() != null && liquid.getFluid().getName().equals("water");
    }


	public static FluidStack getFluidContained(ItemStack container)
	{
        if (container.getItem() instanceof ItemCanisterGeneric)
        {
        	ItemCanisterGeneric canister = (ItemCanisterGeneric) container.getItem(); 
        	return new FluidStack(FluidRegistry.getFluid(canister.getAllowedFluid()), ItemCanisterGeneric.EMPTY - container.getItemDamage());
        }

        return net.minecraftforge.fluids.FluidUtil.getFluidContained(container);
	}
	
    /**
     * Test for any container type at all
     * Used, for example, in isItemValidForSlot() logic
     *
     * @param slotItem
     * @return True if it is a container; False if it is null or not a container
     */
    public static boolean isValidContainer(ItemStack slotItem)
    {
        return slotItem != null && slotItem.getCount() == 1 && (slotItem.getItem() instanceof ItemCanisterGeneric || net.minecraftforge.fluids.FluidUtil.getFluidHandler(slotItem) != null);
    }

    /**
     * Returns the used (empty) container, for example an empty bucket
     * Used, for example, in isItemValidForSlot() logic
     *
     * @param container
     * @return True if it is a container; False if it is null or not a container
     */
    public static ItemStack getUsedContainer(ItemStack container)
    {
        if (FluidUtil.isBucket(container) && net.minecraftforge.fluids.FluidUtil.getFluidHandler(container) != null)
        {
            return new ItemStack(Items.BUCKET, container.getCount());
        }
        else
        {
            container.shrink(1);

            if (!container.isEmpty())
            {
                return null;
            }

            return container;
        }
    }

    @SideOnly(Side.CLIENT)
    public static boolean isInsideOfFluid(Entity entity, Fluid fluid)
    {
        double d0 = entity.posY + entity.getEyeHeight();
        int i = MathHelper.floor(entity.posX);
        int j = MathHelper.floor(MathHelper.floor(d0));
        int k = MathHelper.floor(entity.posZ);
        BlockPos pos = new BlockPos(i, j, k);
        Block block = entity.world.getBlockState(pos).getBlock();

        if (block != null && block instanceof IFluidBlock && ((IFluidBlock) block).getFluid() != null && ((IFluidBlock) block).getFluid().getName().equals(fluid.getName()))
        {
            double filled = ((IFluidBlock) block).getFilledPercentage(entity.world, pos);
            if (filled < 0)
            {
                filled *= -1;
                return d0 > j + (1 - filled);
            }
            else
            {
                return d0 < j + filled;
            }
        }
        else
        {
            return false;
        }
    }

    public static boolean isBucket(ItemStack container)
    {
        if (container == null)
        {
            return false;
        }

        IFluidHandlerItem handlerItem = net.minecraftforge.fluids.FluidUtil.getFluidHandler(container);
        if (handlerItem != null)
        {
            if (container.getItem() == Items.BUCKET)
            {
                return true;
            }

            if (handlerItem.getContainer().getItem() == Items.BUCKET)
            {
                return true;
            }
        }

        return false;
    }
}
