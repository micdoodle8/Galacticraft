package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FluidUtil
{
    private static final boolean oldFluidIDMethod = true;
    private static final Class<?> fluidStackClass = null;
    private static final Method getFluidMethod = null;
    private static final Field fluidIdField = null;

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
            return var4.getItem() == GCItems.fuelCanister && var4.getDamage() < var4.getMaxDamage();
        }

//        if (var4.getItem() == GCItems.bucketFuel)
//        {
//        	return true;
//        } TODO Buckets

        LazyOptional<FluidStack> liquid = net.minecraftforge.fluids.FluidUtil.getFluidContained(var4);
        return liquid.isPresent() && FluidUtil.testFuel(liquid.orElse(FluidStack.EMPTY).getFluid().getRegistryName());
    }

    /**
     * Tests whether the named liquid is fuel / fuelgc
     * or rocket fuel from another mod, for compatibility (includes RotaryCraft Jet Fuel)
     *
     * @param name - which MUST be in lowercase, as always returned by FluidRegistry.getFluidName
     * @return true if a type of recognised fuel, false if not
     */
    public static boolean testFuel(ResourceLocation name)
    {
        if (name == null)
        {
            return false;
        }

//        if (name.equals("fuel") || name.equals("fuelgc")|| name.equals("fuel_light"))
//        {
//            return true;
//        }
//
//        if (name.contains("rocket") && name.contains("fuel"))    //from EnderIO
//        {
//            return true;
//        }
//
//        if (name.equals("rc jet fuel"))    //from RotaryCraft
//        {
//            return true;
//        }
//
//        if (name.startsWith("fuel"))
//        {
//            return !name.contains("dense") && !name.contains("heavy") && !name.contains("mixed") && !name.contains("gaseous");
//        }

        return name.getPath().contains("fuel");
//        return name.equals("refined_fuel");    //from Thermal Expansion
    }

    public static boolean testOil(ResourceLocation name)
    {
        if (name == null)
        {
            return false;
        }

//        if (name.equals("oil") || name.equals("oilgc"))
//        {
//            return true;
//        }
//
//        if (name.equals("crude_oil"))
//        {
//            return true;
//        }
//
//        if (name.startsWith("oil"))
//        {
//            return !name.contains("dense") && !name.contains("residue");
//        }
        return name.getPath().contains("oil");

//        return false;
    }

    public static boolean isFuel(FluidStack fluid)
    {
        return fluid != null && testFuel(fluid.getFluid().getRegistryName());
    }

    public static boolean isOil(FluidStack fluid)
    {
        return fluid != null && testOil(fluid.getFluid().getRegistryName());
    }

    public static boolean isFluidFuzzy(FluidStack fluid, String name)
    {
        return fluid != null && fluid != FluidStack.EMPTY && fluid.getFluid().getRegistryName().getPath().startsWith(name);
    }

    public static boolean isFluidStrict(FluidStack fluid, String name)
    {
        return fluid != null && fluid != FluidStack.EMPTY && fluid.getFluid().getRegistryName().getPath().equals(name);
    }

    /**
     * Fill Galacticraft entities (e.g. rockets, buggies) with Galacticraft fuel.
     * For legacy reasons, accepts either "fuel" or "fuelgc".
     * Auto-converts either one to the same type of fuel as is already contained.
     *
     * @param tank   The tank
     * @param liquid A FluidStack being the fuel offered
     * @param action True if this is not a simulation / tank capacity test
     * @return The amount filled
     */
    public static int fillWithGCFuel(FluidTank tank, FluidStack liquid, IFluidHandler.FluidAction action)
    {
        if (liquid != FluidStack.EMPTY && testFuel(liquid.getFluid().getRegistryName()))
        {
            final FluidStack liquidInTank = tank.getFluid();

            //If the tank is empty, fill it with the current type of GC fuel
            if (liquidInTank == FluidStack.EMPTY)
            {
                return tank.fill(new FluidStack(GCFluids.FUEL.getFluid(), liquid.getAmount()), action);
            }

            //If the tank already contains something, fill it with more of the same
            if (liquidInTank.getAmount() < tank.getCapacity())
            {
                return tank.fill(new FluidStack(liquidInTank, liquid.getAmount()), action);
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
            return var4.getItem() == GCItems.oilCanister && var4.getDamage() < var4.getMaxDamage();
        }

//        if (var4.getItem() == GCItems.bucketOil)
//        {
//        	return true;
//        } TODO Buckets

        LazyOptional<FluidStack> liquid = net.minecraftforge.fluids.FluidUtil.getFluidContained(var4);
        return liquid.isPresent() && isOil(liquid.orElse(null));
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
//            return var4.getItem() == AsteroidsItems.methaneCanister && var4.getDamage() < var4.getMaxDamage(); TODO Planets
        }

        LazyOptional<FluidStack> holder = net.minecraftforge.fluids.FluidUtil.getFluidContained(var4);
        if (holder.isPresent())
        {
            FluidStack stack = holder.orElse(null);
            return stack != FluidStack.EMPTY && stack.getFluid().getRegistryName().getPath().toLowerCase().contains("methane");
        }
        return false;
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
            return var4.getDamage() == 1;
        }

//        if (var4.getItem() instanceof ItemBucketGC)
//        {
//        	return true;
//        }

        LazyOptional<FluidStack> holder = net.minecraftforge.fluids.FluidUtil.getFluidContained(var4);
        return holder.isPresent();
    }

    /**
     * This tries to fill the given container (at inventory[slot]) with fluid from the specified tank
     * If successful, it places the resulting filled container in inventory[slot]
     * <p>
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
        if (liquid.getAmount() <= 0 || slotItem.isEmpty())
        {
            return;
        }

        if (slotItem.getItem() instanceof ItemCanisterGeneric)
        {
            if (slotItem.getCount() != 1 || slotItem.getItem() != canisterType && slotItem.getDamage() != ItemCanisterGeneric.EMPTY_CAPACITY)
            {
                return;
            }

            final int used = Math.min(liquid.getAmount(), slotItem.getDamage() - 1);
            if (used > 0)
            {
                ItemStack stack = new ItemStack(canisterType, 1);
                stack.setDamage(slotItem.getDamage() - used);
                inventory.set(slot, stack);
                tank.drain(used, IFluidHandler.FluidAction.EXECUTE);
            }
            return;
        }
//        else if (slotItem.getCount() == 1)
//        {
//        	ItemBucketGC bucketGC;
//        	if (slotItem.getItem() == Items.BUCKET && liquid.getAmount() >= 1000 && (bucketGC = ItemBucketGC.getBucketForFluid(liquid.getFluid())) != null)
//        	{
//        		inventory.set(slot, new ItemStack(bucketGC));
//        		tank.drain(1000, IFluidHandler.FluidAction.EXECUTE);
//        		return;
//        	}
//
//        	LazyOptional<IFluidHandlerItem> holder = net.minecraftforge.fluids.FluidUtil.getFluidHandler(inventory.get(slot));
//        	if (holder.isPresent())
//        	{
//                IFluidHandlerItem handler = holder.orElse(null);
//        		final int used = handler.fill(liquid, IFluidHandler.FluidAction.EXECUTE);
//        		if (used > 0)
//        		{
//        			inventory.set(slot, handler.getContainer());
//        			tank.drain(used, IFluidHandler.FluidAction.EXECUTE);
//        		}
//        	}
//        } TODO Bucket
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

            if (!liquid.isEmpty() && liquid.getAmount() > 0)
            {
                String liquidname = liquid.getFluid().getRegistryName().getPath();

                //Test for the GC fuels (though anything similarly named would also pass here)
                if (liquidname.startsWith("fuel"))
                {
                    //Make sure it is the current GC fuel
                    if (!liquidname.equals(GCFluids.FUEL.getFluid().getRegistryName().getPath()))
                    {
                        liquid = new FluidStack(GCFluids.FUEL.getFluid(), liquid.getAmount());
                    }

                    //But match any existing fuel fluid in the container
                    ItemStack stack = inventory.get(slot);
                    Item item = stack.isEmpty() ? null : stack.getItem();
                    if (!(item instanceof ItemCanisterGeneric /*|| item instanceof ItemBucketGC TODO Bucket */))
                    {
                        LazyOptional<IFluidHandlerItem> handler = net.minecraftforge.fluids.FluidUtil.getFluidHandler(stack);
                        if (handler.isPresent())
                        {
                            LazyOptional<FluidStack> holder = net.minecraftforge.fluids.FluidUtil.getFluidContained(stack);
                            FluidStack existingFluid = holder.orElse(FluidStack.EMPTY);
                            if (existingFluid != FluidStack.EMPTY && !existingFluid.getFluid().getRegistryName().getPath().equals(GCFluids.FUEL.getFluid().getRegistryName().getPath()))
                            {
                                liquid = new FluidStack(existingFluid, liquid.getAmount());
                            }
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
     * @param tank          The tank to fill with the fluid
     * @param desiredLiquid The type of liquid intended for that tank
     * @param stacks
     * @param slot
     * @param amountOffered The amount in the container being offered
     */
    public static void loadFromContainer(FluidTank tank, Fluid desiredLiquid, NonNullList<ItemStack> stacks, int slot, int amountOffered)
    {
        ItemStack slotItem = stacks.get(slot);

        if (slotItem.getItem() instanceof ItemCanisterGeneric)
        {
            int originalDamage = slotItem.getDamage();
            int used = tank.fill(new FluidStack(desiredLiquid, ItemCanisterGeneric.EMPTY_CAPACITY - originalDamage), IFluidHandler.FluidAction.EXECUTE);
            if (originalDamage + used >= ItemCanisterGeneric.EMPTY_CAPACITY)
            {
                ItemStack item = new ItemStack(GCItems.oilCanister, 1);
                item.setDamage(ItemCanisterGeneric.EMPTY_CAPACITY);
                stacks.set(slot, item);
            }
            else
            {
                ItemStack item = new ItemStack(slotItem.getItem(), 1);
                item.setDamage(originalDamage + used);
                stacks.set(slot, item);
            }
        }
//        else if (slotItem.getItem() instanceof ItemBucketGC && amountOffered == 1000)
//        {
//        	ItemBucketGC bucket = (ItemBucketGC) slotItem.getItem();
//        	if (fluidsSame(bucket.accepts, desiredLiquid, true))
//        	{
//                tank.fill(new FluidStack(desiredLiquid, amountOffered), IFluidHandler.FluidAction.EXECUTE);
//                stacks.set(slot, new ItemStack(Items.BUCKET));
//        	}
//        } TODO Bucket
        else
        {
            if (tank.getFluid() == FluidStack.EMPTY || amountOffered <= tank.getCapacity() - tank.getFluid().getAmount())
            {
                //Now deal with the resulting container
                if (FluidUtil.isFilledContainer(slotItem))
                {
                    final int itemCount = slotItem.getCount();
                    if (FluidUtil.isBucket(slotItem))
                    {
                        int used = tank.fill(new FluidStack(desiredLiquid, amountOffered), IFluidHandler.FluidAction.EXECUTE);
                        if (itemCount > 1)
                        {
                            tank.fill(new FluidStack(desiredLiquid, (itemCount - 1) * 1000), IFluidHandler.FluidAction.EXECUTE);
                        }
                        stacks.set(slot, new ItemStack(Items.BUCKET, itemCount));
                    }
                    else if (itemCount == 1)
                    {
                        LazyOptional<IFluidHandlerItem> holder = net.minecraftforge.fluids.FluidUtil.getFluidHandler(slotItem);
                        if (holder.isPresent())
                        {
                            IFluidHandlerItem handlerItem = holder.orElse(null);
                            int used = tank.fill(new FluidStack(desiredLiquid, amountOffered), IFluidHandler.FluidAction.SIMULATE);
                            FluidStack given = handlerItem.drain(used, IFluidHandler.FluidAction.EXECUTE);
                            if (given != FluidStack.EMPTY)
                            {
                                tank.fill(new FluidStack(desiredLiquid, given.getAmount()), IFluidHandler.FluidAction.EXECUTE);
                                stacks.set(slot, handlerItem.getContainer());
                            }
                            return;
                        }
                        //Fallback basic treatment
                        ItemStack emptyStack = FluidUtil.getUsedContainer(slotItem);
                        tank.fill(new FluidStack(desiredLiquid, FluidUtil.getContainerCapacity(slotItem)), IFluidHandler.FluidAction.EXECUTE);
                        stacks.set(slot, emptyStack);
                    }
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
            return var4.getDamage() == ItemCanisterGeneric.EMPTY_CAPACITY || (var4.getItem() == canisterType && var4.getDamage() > 1);
        }

        LazyOptional<IFluidHandlerItem> handlerHolder = net.minecraftforge.fluids.FluidUtil.getFluidHandler(var4);
        LazyOptional<FluidStack> fluidHolder = net.minecraftforge.fluids.FluidUtil.getFluidContained(var4);

        if (handlerHolder.isPresent())
        {
            if (!fluidHolder.isPresent())
            {
                return true;
            }

            // Check that there is at least some space in container
            FluidStack containedFluid = fluidHolder.orElse(null);
            containedFluid = containedFluid.copy();
            containedFluid.setAmount(1);
            return handlerHolder.orElse(null).fill(containedFluid, IFluidHandler.FluidAction.EXECUTE) > 0;
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
            if (var4.getDamage() == ItemCanisterGeneric.EMPTY_CAPACITY)
            {
                return true;
            }
            if (var4.getDamage() == 1)
            {
                return false;
            }

            return fluidsSame(FluidUtil.getFluidContained(var4), targetFluid);
        }

        if (isEmptyContainer(var4))
        {
            return true;
        }

        LazyOptional<FluidStack> holder = net.minecraftforge.fluids.FluidUtil.getFluidContained(var4);
        if (holder.isPresent())
        {
            return fluidsSame(holder.orElse(null), targetFluid);
        }
        return false;
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
            return container.getDamage() - 1;
        }
        if (!container.isEmpty())
        {
            container = container.copy();
            container.setCount(1);
            LazyOptional<IFluidHandlerItem> fluidHandler = net.minecraftforge.fluids.FluidUtil.getFluidHandler(container);
            if (fluidHandler.isPresent())
            {
                FluidStack drain = fluidHandler.orElse(null).drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
                return drain == null ? 0 : drain.getAmount();
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
        return fs1 == fs2; // TODO New way to do this? With registry resource locations
//        if (fs1 == null || fs2 == null)
//        {
//            return false;
//        }
//        Fluid f1 = fs1.getFluid();
//        Fluid f2 = fs2.getFluid();
//        if (f1 == null || f2 == null || f1.getName() == null)
//        {
//            return false;
//        }
//        return f1.getName().equals(f2.getName());
    }

    public static boolean fluidsSame(Fluid f1, Fluid f2, boolean fuzzy)
    {
        if (f1 == null || f2 == null)
        {
            return false;
        }
        if (fuzzy)
        {
            if (f1.getRegistryName().getPath().startsWith("fuel"))
            {
                return testFuel(f2.getRegistryName());
            }
            if (f1.getRegistryName().getPath().startsWith("oil"))
            {
                return f2.getRegistryName().getPath().startsWith("oil");
            }
            if (f1.getRegistryName().getPath().startsWith("water"))
            {
                return f2.getRegistryName().getPath().startsWith("water");
            }
        }
        return f1.getRegistryName().getPath().equals(f2.getRegistryName().getPath());
    }

//    public static boolean fluidsSame(Fluid f1, String name2, boolean fuzzy)
//    {
//        if (f1 == null || name2 == null || f1.getName() == null)
//        {
//            return false;
//        }
//        if (fuzzy)
//        {
//        	if (name2.equals("fuel"))
//        	{
//        		return testFuel(f1.getName());
//        	}
//
//        	return f1.getName().startsWith(name2);
//        }
//        return f1.getName().equals(name2);
//    }

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
            return var4.getDamage() == ItemCanisterGeneric.EMPTY_CAPACITY;
        }

        if (var4.getItem() == Items.BUCKET)
        {
            return true;
        }

        LazyOptional<IFluidHandlerItem> handler = net.minecraftforge.fluids.FluidUtil.getFluidHandler(var4);
        LazyOptional<FluidStack> containedFluid = net.minecraftforge.fluids.FluidUtil.getFluidContained(var4);
        return handler.isPresent() && (!containedFluid.isPresent() || containedFluid.orElse(null).getAmount() == 0);
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
            return var4.getDamage() < ItemCanisterGeneric.EMPTY_CAPACITY;
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
        LazyOptional<FluidStack> holder = net.minecraftforge.fluids.FluidUtil.getFluidContained(var4);
        if (holder.isPresent())
        {
            FluidStack liquid = holder.orElse(null);
            return liquid != FluidStack.EMPTY && liquid.getFluid().getRegistryName().getPath().equals("water");
        }
        return false;
    }


    public static FluidStack getFluidContained(ItemStack container)
    {
        if (container == null)
        {
            return null;
        }

        if (container.getItem() instanceof ItemCanisterGeneric)
        {
            ItemCanisterGeneric canister = (ItemCanisterGeneric) container.getItem();
            return canister.getFluid(container);
        }

//        if (container.getItem() instanceof ItemBucketGC)
//        {
//        	return new FluidStack(((ItemBucketGC)container.getItem()).accepts, 1000);
//        } TODO Bucket

        return net.minecraftforge.fluids.FluidUtil.getFluidContained(container).orElse(FluidStack.EMPTY);
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
        return slotItem != null && slotItem.getCount() == 1 && (slotItem.getItem() instanceof ItemCanisterGeneric /*|| slotItem.getItem() instanceof ItemBucketGC TODO Bucket*/ || net.minecraftforge.fluids.FluidUtil.getFluidHandler(slotItem) != null);
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
        LazyOptional<IFluidHandlerItem> holder = net.minecraftforge.fluids.FluidUtil.getFluidHandler(container);
        IFluidHandlerItem handler = holder.orElse(null);
        if (FluidUtil.isBucket(container) && handler != null)
        {
            return new ItemStack(Items.BUCKET, container.getCount());
        }
        else if (handler != null)
        {
            handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
            return handler.getContainer();
        }
        else
        {
            container.shrink(1);

            if (!container.isEmpty())
            {
                return ItemStack.EMPTY;
            }

            return container;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean isInsideOfFluid(Entity entity, Fluid fluid)
    {
        double d0 = entity.getPosY() + entity.getEyeHeight();
        int i = MathHelper.floor(entity.getPosX());
        int j = MathHelper.floor(MathHelper.floor(d0));
        int k = MathHelper.floor(entity.getPosZ());
        BlockPos pos = new BlockPos(i, j, k);
        Block block = entity.world.getBlockState(pos).getBlock();

        if (block != null && block instanceof IFluidBlock && ((IFluidBlock) block).getFluid().getRegistryName().equals(fluid.getRegistryName()))
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

        LazyOptional<IFluidHandlerItem> holder = net.minecraftforge.fluids.FluidUtil.getFluidHandler(container);
        IFluidHandlerItem handlerItem = holder.orElse(null);
        if (handlerItem != null)
        {
            if (container.getItem() == Items.BUCKET)
            {
                return true;
            }

            return handlerItem.getContainer().getItem() == Items.BUCKET;
        }

        return false;
    }

    @Nonnull
    public static FluidActionResult interactWithFluidHandler(@Nonnull ItemStack container, IFluidHandler fluidHandler, PlayerEntity player)
    {
        if (container.isEmpty() || fluidHandler == null || player == null)
        {
            return FluidActionResult.FAILURE;
        }

        //-----------Special code for canisters onto FluidTanks------------
        //This is NOT strictly necessary, as our 1.11 code for canisters implements IFluidHandler 100% correctly
        //
        //But our code offers three improvements over Forge:
        // 1: Performance :)
        // 2: Tries to empty partial canister BEFORE filling it, that seems more natural
        // 3: Forge's behaviour in Creative Mode with partial canisters is not a bug, but it still feels wrong
        //
        if (container.getItem() instanceof ItemCanisterGeneric)
        {
            ItemStack result;
            if ((result = FluidUtil.tryEmptyCanister(container, fluidHandler, player.abilities.isCreativeMode)) != ItemStack.EMPTY || (result = FluidUtil.tryFillCanister(container, fluidHandler, player.abilities.isCreativeMode)) != ItemStack.EMPTY)
            {
                // send inventory updates to client
                if (player.container != null)
                {
                    player.container.detectAndSendChanges();
                }
                return new FluidActionResult(result);
            }

            return FluidActionResult.FAILURE;
        }

        //Forge's UniversalBucket appears to be non-functional, currently, for FILLING modded buckets
        //Try to fill an empty vanilla bucket:
//        if (container.getItem() == Items.BUCKET)
//        {
//        	ItemStack result = ItemBucketGC.fillBucketFrom(fluidHandler);
//        	if (result != null)
//        	{
//        	    //Handle stacks of buckets: insert the filled bucket into another inventory slot or drop it
//        	    if (container.getCount() > 1)
//        	    {
//                    ItemStack remainder = ItemHandlerHelper.insertItemStacked(new PlayerMainInvWrapper(player.inventory), result, false);
//                    if (!remainder.isEmpty() && player != null)
//                    {
//                        ItemHandlerHelper.giveItemToPlayer(player, remainder);
//                    }
//                    result = container.copy();
//                    result.shrink(1);
//        	    }
//        		return new FluidActionResult(result);
//        	}
//        	//If failure, fall through to other Forge methods
//        } TODO Bucket

        //---------the rest of this is standard Forge code from interactWithFluidHandler()---------
        //-----------(but without the InvWrapper bug which could put fluids in armor slots!)-------
        IItemHandler playerInventory = new PlayerMainInvWrapper(player.inventory);

        FluidActionResult fillResult = net.minecraftforge.fluids.FluidUtil.tryFillContainerAndStow(container, fluidHandler, playerInventory, Integer.MAX_VALUE, player, true);
        if (fillResult.isSuccess())
        {
            return fillResult;
        }
        else
        {
            return net.minecraftforge.fluids.FluidUtil.tryEmptyContainerAndStow(container, fluidHandler, playerInventory, Integer.MAX_VALUE, player, true);
        }
    }

    private static ItemStack tryFillCanister(ItemStack canister, IFluidHandler tank, boolean isCreativeMode)
    {
        int currCapacity = canister.getDamage() - 1;
        if (currCapacity <= 0)
        {
            return ItemStack.EMPTY;
        }
        FluidStack liquid = tank.drain(currCapacity, IFluidHandler.FluidAction.SIMULATE);
        int transferred = ((ItemCanisterGeneric) canister.getItem()).fill(canister, liquid, isCreativeMode ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
        if (transferred > 0)
        {
            liquid = tank.drain(transferred, IFluidHandler.FluidAction.EXECUTE);
            return canister;
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack tryEmptyCanister(ItemStack canister, IFluidHandler tank, boolean isCreativeMode)
    {
        int currContents = ItemCanisterGeneric.EMPTY_CAPACITY - canister.getDamage();
        if (currContents <= 0)
        {
            return ItemStack.EMPTY;
        }
        FluidStack liquid = ((ItemCanisterGeneric) canister.getItem()).drain(canister, currContents, IFluidHandler.FluidAction.SIMULATE);
        int transferred = tank.fill(liquid, IFluidHandler.FluidAction.EXECUTE);
        if (transferred > 0)
        {
            ((ItemCanisterGeneric) canister.getItem()).drain(canister, transferred, isCreativeMode ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
            return canister;
        }
        return ItemStack.EMPTY;
    }
}
