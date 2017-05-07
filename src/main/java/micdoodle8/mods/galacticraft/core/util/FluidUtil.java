package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.core.GCFluids;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

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

        FluidStack liquid = FluidUtil.getFluidContained(var4);
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
    
    public static boolean isFuel(FluidStack fluid)
    {
        return fluid != null && testFuel(FluidRegistry.getFluidName(fluid));
    }

    public static boolean isFluidFuzzy(FluidStack fluid, String name)
    {
        return fluid != null && fluid.getFluid() != null && fluid.getFluid().getName().startsWith(name);
    }

    public static boolean isFluidStrict(FluidStack fluid, String name)
    {
        return fluid != null && fluid.getFluid() != null && fluid.getFluid().getName().equals(name);
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

        FluidStack liquid = FluidUtil.getFluidContained(var4);
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

        FluidStack stack = FluidUtil.getFluidContained(var4);
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

        FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(var4);
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
    public static void tryFillContainer(FluidTank tank, FluidStack liquid, ItemStack[] inventory, int slot, Item canisterType)
    {
        ItemStack slotItem = inventory[slot];
        if (liquid.amount <= 0 || slotItem == null)
        {
            return;
        }

        if (slotItem.getItem() instanceof ItemCanisterGeneric)
        {
            if (slotItem.stackSize != 1 || slotItem.getItem() != canisterType && slotItem.getItemDamage() != ItemCanisterGeneric.EMPTY)
            {
                return;
            }

            final int used = Math.min(liquid.amount, slotItem.getItemDamage() - 1);
            if (used > 0)
            {
                inventory[slot] = new ItemStack(canisterType, 1, slotItem.getItemDamage() - used);
                tank.drain(used, true);
            }
            return;
        }
        else if (slotItem.stackSize == 1)
        {
            inventory[slot] = FluidContainerRegistry.fillFluidContainer(liquid, inventory[slot]);

            if (inventory[slot] != null)
            {
                int capacity = FluidContainerRegistry.getContainerCapacity(inventory[slot]);
                tank.drain(capacity, true);
                return;
            }

            //Failed to fill container (either because it's already full or it's not a registered FluidContainer): restore item that was there before
            inventory[slot] = slotItem;
            
            //Try the new way to fill container items
            IFluidHandler handler = net.minecraftforge.fluids.FluidUtil.getFluidHandler(inventory[slot]);
            if (handler != null)
            {
                final int used = handler.fill(liquid, true);
                if (used > 0)
                {
                    tank.drain(used, true);
                }
                return;
            }
        }
    }

    /**
     * @param tank
     * @param inventory
     * @param slot
     */
    public static void tryFillContainerFuel(FluidTank tank, ItemStack[] inventory, int slot)
    {
        if (FluidUtil.isValidContainer(inventory[slot]))
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
                    ItemStack stack = inventory[slot];
                    //(No null check necessary here: it cannot be a null ItemStack thanks to the .isValidContainer() check above
                    if (stack.getItem() instanceof IFluidContainerItem)
                    {
                        FluidStack existingFluid = ((IFluidContainerItem) stack.getItem()).getFluid(stack);
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
     * @param inventory
     * @param slot
     * @param amountOffered  The amount in the container being offered
     */
    public static void loadFromContainer(FluidTank tank, Fluid desiredLiquid, ItemStack[] inventory, int slot, int amountOffered)
    {
        ItemStack slotItem = inventory[slot];

        if (slotItem.getItem() instanceof ItemCanisterGeneric)
        {
            int originalDamage = slotItem.getItemDamage();
            int used = tank.fill(new FluidStack(desiredLiquid, ItemCanisterGeneric.EMPTY - originalDamage), true);
            if (originalDamage + used >= ItemCanisterGeneric.EMPTY)
            {
                inventory[slot] = new ItemStack(GCItems.oilCanister, 1, ItemCanisterGeneric.EMPTY);
            }
            else
            {
                inventory[slot] = new ItemStack(slotItem.getItem(), 1, originalDamage + used);
            }
        }
        else
        {
            if (tank.getFluid() == null || amountOffered <= tank.getCapacity() - tank.getFluid().amount)
            {
                if (FluidContainerRegistry.isFilledContainer(slotItem))
                {
                    int used = tank.fill(new FluidStack(desiredLiquid, amountOffered), true);
                    final int itemCount = slotItem.stackSize;

                    if (FluidContainerRegistry.isBucket(slotItem))
                    {
                        if (itemCount > 1)
                        {
                            tank.fill(new FluidStack(desiredLiquid, (itemCount - 1) * FluidContainerRegistry.BUCKET_VOLUME), true);
                        }
                        inventory[slot] = new ItemStack(Items.BUCKET, itemCount);
                    }
                    else
                    {
                        ItemStack emptyStack = FluidContainerRegistry.drainFluidContainer(slotItem); 
                        if (itemCount > 1)
                        {
                            tank.fill(new FluidStack(desiredLiquid, (itemCount - 1) * FluidContainerRegistry.getContainerCapacity(slotItem)), true);
                            if (emptyStack != null)
                            {
                                emptyStack.stackSize = itemCount;
                            }
                        }
                        inventory[slot] = emptyStack;
                    }
                }
                else
                {
                    IFluidHandler handlerItem = net.minecraftforge.fluids.FluidUtil.getFluidHandler(slotItem);
                    final int itemCount = slotItem.stackSize;
                    if (handlerItem != null && itemCount == 1)
                    {
                        int used = tank.fill(new FluidStack(desiredLiquid, amountOffered), false);
                        FluidStack given = handlerItem.drain(used, true); 
                        if (given != null)
                        {
                            tank.fill(new FluidStack(desiredLiquid, given.amount), true);
                        }
                        return;
                    }

                    slotItem.stackSize--;

                    if (slotItem.stackSize == 0)
                    {
                        inventory[slot] = null;
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
    public static boolean isEmptyContainer(ItemStack var4, Item canisterType)
    {
        if (var4.getItem() instanceof ItemCanisterGeneric)
        {
            return var4.getItemDamage() == ItemCanisterGeneric.EMPTY || (var4.getItem() == canisterType && var4.getItemDamage() > 1);
        }
        
        if (var4.getItem() == Items.BUCKET)
        {
            return true;
        }

        IFluidHandler handler = net.minecraftforge.fluids.FluidUtil.getFluidHandler(var4);
        if (handler != null)
        {
            FluidStack containedFluid = net.minecraftforge.fluids.FluidUtil.getFluidContained(var4);
            return (containedFluid == null || containedFluid.amount == 0);
        }
        
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
            if (var4.getItemDamage() == ItemCanisterGeneric.EMPTY)
            {
                return true;
            }
            if (var4.getItemDamage() == 1)
            {
                return false;
            }

            return fluidsSame(((ItemCanisterGeneric) var4.getItem()).getFluid(var4), targetFluid);
        }

        FluidStack liquid;
        IFluidHandler handler = net.minecraftforge.fluids.FluidUtil.getFluidHandler(var4);
        if (handler != null)
        {
            liquid = net.minecraftforge.fluids.FluidUtil.getFluidContained(var4);
            if (liquid != null && liquid.amount > 0) return false;
        }
        else
        {
            liquid = FluidContainerRegistry.getFluidForFilledItem(var4);
        }
        return fluidsSame(liquid, targetFluid);
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
        
        if (var4.getItem() == Items.BUCKET)
        {
            return true;
        }

        IFluidHandler handler = net.minecraftforge.fluids.FluidUtil.getFluidHandler(var4);
        if (handler != null)
        {
            FluidStack containedFluid = net.minecraftforge.fluids.FluidUtil.getFluidContained(var4);
            return (containedFluid == null || containedFluid.amount == 0);
        }

        return FluidContainerRegistry.isEmptyContainer(var4);
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
        if (var4.getItem() instanceof ItemCanisterGeneric)
        {
            return var4.getItemDamage() < ItemCanisterGeneric.EMPTY;
        }
        
        IFluidHandler handler = net.minecraftforge.fluids.FluidUtil.getFluidHandler(var4);
        if (handler != null)
        {
            FluidStack containedFluid = net.minecraftforge.fluids.FluidUtil.getFluidContained(var4);
            return containedFluid != null && containedFluid.amount > 0;
        }
        
        return FluidContainerRegistry.getFluidForFilledItem(var4) != null;
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
        FluidStack liquid;
        IFluidHandler handler = net.minecraftforge.fluids.FluidUtil.getFluidHandler(var4);
        if (handler != null)
        {
            FluidStack containedFluid = net.minecraftforge.fluids.FluidUtil.getFluidContained(var4);
            return containedFluid != null && containedFluid.amount > 0;
        }
        else
        {
            liquid = FluidContainerRegistry.getFluidForFilledItem(var4);
        }
        return liquid != null && liquid.getFluid() != null && liquid.getFluid().getName().equals("water");
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
        if (slotItem == null || slotItem.stackSize < 1)
            return false;
        
        if (slotItem.getItem() instanceof ItemCanisterGeneric || slotItem.getItem() instanceof ItemBucket)
            return true;

        IFluidHandler handler = net.minecraftforge.fluids.FluidUtil.getFluidHandler(slotItem);
        if (handler != null)
        {
            return true;
        }
        return FluidContainerRegistry.isContainer(slotItem);
    }

    /**
     * Returns the used (empty) container, for example an empty bucket
     * Used, for example, in isItemValidForSlot() logic
     *
     * @param slotItem
     * @return True if it is a container; False if it is null or not a container
     */
    public static ItemStack getUsedContainer(ItemStack container)
    {
        if (FluidContainerRegistry.isBucket(container) && FluidContainerRegistry.isFilledContainer(container))
        {
            return new ItemStack(Items.BUCKET, container.stackSize);
        }
        else if (net.minecraftforge.fluids.FluidUtil.getFluidHandler(container) != null)
        {
            //Somehow the container must handle its own emptying logic?!  Forge seems to offer no way to tell it, in 1.10
            return container;
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

    public static FluidStack getFluidContained(ItemStack container)
    {
        if (container == null)
        {
            return null;
        }
        
        if (container.getItem() instanceof ItemCanisterGeneric)
        {
            ItemCanisterGeneric canister = (ItemCanisterGeneric) container.getItem(); 
            return new FluidStack(FluidRegistry.getFluid(canister.getAllowedFluid()), ItemCanisterGeneric.EMPTY - container.getItemDamage());
        }

        FluidStack liquid = net.minecraftforge.fluids.FluidUtil.getFluidContained(container);
        if (liquid != null)
        {
            return liquid;
        }
        
        return FluidContainerRegistry.getFluidForFilledItem(container);
    }

    @SideOnly(Side.CLIENT)
    public static boolean isInsideOfFluid(Entity entity, Fluid fluid)
    {
        double d0 = entity.posY + entity.getEyeHeight();
        int i = MathHelper.floor_double(entity.posX);
        int j = MathHelper.floor_float(MathHelper.floor_double(d0));
        int k = MathHelper.floor_double(entity.posZ);
        BlockPos pos = new BlockPos(i, j, k);
        Block block = entity.worldObj.getBlockState(pos).getBlock();

        if (block != null && block instanceof IFluidBlock && ((IFluidBlock) block).getFluid() != null && ((IFluidBlock) block).getFluid().getName().equals(fluid.getName()))
        {
            double filled = ((IFluidBlock) block).getFilledPercentage(entity.worldObj, pos);
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

    public static boolean interactWithFluidHandler(ItemStack container, IFluidHandler fluidHandler, EntityPlayer player)
    {
        if (container == null || fluidHandler == null || player == null)
        {
            return false;
        }

        //Special code for canisters onto FluidTanks
        //This is NOT strictly necessary, as our 1.11 code for canisters implements IFluidHandler 100% correctly
        //
        //But our code offers three improvements over Forge:
        // 1: Performance :)
        // 2: Tries to empty partial canister BEFORE filling it, that seems more natural
        // 3: Forge's behaviour in Creative Mode with partial canisters is not a bug, but it still feels wrong
        //
        if (container.getItem() instanceof ItemCanisterGeneric)
        {
            if (FluidUtil.tryEmptyCanister(container, fluidHandler) != null || FluidUtil.tryFillCanister(container, fluidHandler) != null)
            {
                // send inventory updates to client
                if (player.inventoryContainer != null)
                {
                    player.inventoryContainer.detectAndSendChanges();
                }
                return true;
            }

            return false;
        }
        
        //Also try our buckets, and any registered containers from any other mods (Forge why don't you do this?)
        if (FluidContainerRegistry.isEmptyContainer(container))
        {
            FluidStack liquid = fluidHandler.drain(FluidContainerRegistry.getContainerCapacity(container), false);
            ItemStack result = FluidContainerRegistry.fillFluidContainer(liquid, container);
            if (result != null)
                return true;
        }

        IItemHandler playerInventory = new InvWrapper(player.inventory);
        boolean fillResult = net.minecraftforge.fluids.FluidUtil.tryFillContainerAndStow(container, fluidHandler, playerInventory, Integer.MAX_VALUE, player);
        if (fillResult)
        {
            return fillResult;
        }
        else
        {
            return net.minecraftforge.fluids.FluidUtil.tryEmptyContainerAndStow(container, fluidHandler, playerInventory, Integer.MAX_VALUE, player);
        }
     }

    /**
     * This is how to do it properly.  Nice and simple, and high performance.
     */
    private static ItemStack tryFillCanister(ItemStack canister, IFluidHandler tank)
    {
        int currCapacity = canister.getItemDamage() - 1; 
        if (currCapacity <= 0)
        {
            return null;
        }
        FluidStack liquid = tank.drain(currCapacity, false);
        int transferred = ((ItemCanisterGeneric)canister.getItem()).fill(canister, liquid, true);
        if (transferred > 0)
        {
            liquid = tank.drain(transferred, true);
            return canister;
        }
        return null;
    }

    private static ItemStack tryEmptyCanister(ItemStack canister, IFluidHandler tank)
    {
        int currContents = ItemCanisterGeneric.EMPTY - canister.getItemDamage(); 
        if (currContents <= 0)
        {
            return null;
        }
        FluidStack liquid = ((ItemCanisterGeneric)canister.getItem()).drain(canister, currContents, false); 
        int transferred = tank.fill(liquid, true);
        if (transferred > 0)
        {
            ((ItemCanisterGeneric)canister.getItem()).drain(canister, transferred, true);
            return canister;
        }
        return null;
    }
}
