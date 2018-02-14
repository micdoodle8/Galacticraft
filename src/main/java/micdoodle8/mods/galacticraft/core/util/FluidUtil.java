package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.core.GCFluids;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFluidTank;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

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

        FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(var4);
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
    
    public static boolean testOil(String name)
    {
        if (name.startsWith("oil"))
        {
            return true;
        }

        if (name.equals("crude_oil"))
        {
            return true;
        }

        return false;
    }
    
    public static boolean isFuel(FluidStack fluid)
    {
        return fluid != null && testFuel(FluidRegistry.getFluidName(fluid));
    }

    public static boolean isOil(FluidStack fluid)
    {
        return fluid != null && testOil(FluidRegistry.getFluidName(fluid));
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

        FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(var4);
        return isOil(liquid);
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

        FluidStack stack = FluidContainerRegistry.getFluidForFilledItem(var4);
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
        boolean isCanister = slotItem.getItem() instanceof ItemCanisterGeneric;
        final int amountToFill = Math.min(liquid.amount, isCanister ? slotItem.getItemDamage() - 1 : FluidContainerRegistry.BUCKET_VOLUME);

        if (amountToFill <= 0 || (isCanister && slotItem.getItem() != canisterType && slotItem.getItemDamage() != ItemCanisterGeneric.EMPTY))
        {
            return;
        }

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
                //Failed to fill container: restore item that was there before
                inventory[slot] = slotItem;
            }
            else
            {
                tank.drain(amountToFill, true);
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
                tank.fill(new FluidStack(desiredLiquid, amountOffered), true);

                if (FluidContainerRegistry.isFilledContainer(slotItem))
                {
                    final int bucketCount = slotItem.stackSize;
                    if (FluidContainerRegistry.isBucket(slotItem))
                    {
                        if (bucketCount > 1)
                        {
                            tank.fill(new FluidStack(desiredLiquid, (bucketCount - 1) * FluidContainerRegistry.BUCKET_VOLUME), true);
                        }
                        inventory[slot] = new ItemStack(Items.bucket, bucketCount);
                    }
                    else
                    {
                        ItemStack emptyStack = FluidContainerRegistry.drainFluidContainer(slotItem); 
                        if (bucketCount > 1)
                        {
                            tank.fill(new FluidStack(desiredLiquid, (bucketCount - 1) * FluidContainerRegistry.getContainerCapacity(slotItem)), true);
                            if (emptyStack != null)
                            {
                                emptyStack.stackSize = bucketCount;
                            }
                        }
                        inventory[slot] = emptyStack;
                    }
                }
                else
                {
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

        if (FluidContainerRegistry.isEmptyContainer(var4))
        {
            return true;
        }

        return fluidsSame(FluidContainerRegistry.getFluidForFilledItem(var4), targetFluid);
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
        FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(var4);
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
        return slotItem != null && slotItem.stackSize == 1 && (slotItem.getItem() instanceof ItemCanisterGeneric || FluidContainerRegistry.isContainer(slotItem));
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

    public static boolean interactWithTank(ItemStack container, EntityPlayer playerIn, TileEntityFluidTank tank, EnumFacing side)
    {
        if (container == null || playerIn.worldObj.isRemote)
        {
            return true;
        }

        ItemStack result;
        if (container.getItem() instanceof ItemCanisterGeneric)
        {
            if ((result = FluidUtil.tryEmptyCanister(container, tank, side, playerIn.capabilities.isCreativeMode)) != null || (result = FluidUtil.tryFillCanister(container, tank, side, playerIn.capabilities.isCreativeMode)) != null)
            {
                // send inventory updates to client
                if (playerIn.inventoryContainer != null)
                {
                    playerIn.inventoryContainer.detectAndSendChanges();
                }
            }
            
            return true;
        }

        //The following code deals with a bug in 1.8.9 (filling a bucket from a tank with less than 1000mB returns an empty bucket, but clears out the tank)
        //This code may not be required in 1.10.2+
        int slot = playerIn.inventory.currentItem;
        if ((result = FluidUtil.tryFillBucket(container, tank, side)) != null || (result = net.minecraftforge.fluids.FluidUtil.tryEmptyBucket(container, tank, side)) != null)
        {
            if (!playerIn.capabilities.isCreativeMode)
            {
                playerIn.inventory.decrStackSize(slot, 1);
                ItemHandlerHelper.giveItemToPlayer(playerIn, result, slot);
            }
            if (playerIn.inventoryContainer != null)
            {
                playerIn.inventoryContainer.detectAndSendChanges();
            }
            return true;
        }
        else
        {
            // Code for IFluidContainerItems - unchanged from Forge
            ItemStack copy = container.copy();
            boolean changedBucket = false;
            if (ItemStack.areItemsEqual(container, FluidContainerRegistry.EMPTY_BUCKET) && FluidRegistry.isUniversalBucketEnabled())
            {
                container = new ItemStack(ForgeModContainer.getInstance().universalBucket, copy.stackSize);
                changedBucket = true;
            }

            if (net.minecraftforge.fluids.FluidUtil.tryFillFluidContainerItem(container, tank, side, playerIn) || net.minecraftforge.fluids.FluidUtil.tryEmptyFluidContainerItem(container, tank, side, playerIn))
            {
                if (playerIn.capabilities.isCreativeMode)
                {
                    playerIn.inventory.setInventorySlotContents(slot, copy);
                }
                else
                {
                    if (changedBucket && container.stackSize != copy.stackSize)
                    {
                        copy.stackSize = container.stackSize;
                        playerIn.inventory.setInventorySlotContents(slot, copy);
                    }
                    else
                    {
                        if (copy.stackSize > 1)
                        {
                            playerIn.inventory.setInventorySlotContents(slot, container);
                        }
                        else
                        {
                            playerIn.inventory.setInventorySlotContents(slot, null);
                            ItemHandlerHelper.giveItemToPlayer(playerIn, container, slot);
                        }
                    }
                }
                if (playerIn.inventoryContainer != null)
                {
                    playerIn.inventoryContainer.detectAndSendChanges();
                }
                return true;
            }
        }

        return false;
    }

    /**
     * The Forge original version of this is bugged!  (Drains tank even if less than 1000mB in tank)
     */
    private static ItemStack tryFillBucket(ItemStack bucket, TileEntityFluidTank tank, EnumFacing side)
    {
        if (!FluidContainerRegistry.isEmptyContainer(bucket))
        {
            return null;
        }
        FluidTankInfo[] info = tank.getTankInfo(side);
        if (info == null || info.length == 0)
        {
            return null;
        }
        int capacity = FluidContainerRegistry.getContainerCapacity(info[0].fluid, bucket);
        FluidStack liquid = tank.drain(side, capacity, false);
        if (liquid != null && liquid.amount == capacity)  //Only proceed and drain the tank if it can actually fill this bucket
        {
            tank.drain(side, capacity, true);
            return FluidContainerRegistry.fillFluidContainer(liquid, bucket);
        }

        return null;
    }

    /**
     * This is how to do it properly.  Nice and simple, and high performance.
     * @param isCreativeMode 
     */
    private static ItemStack tryFillCanister(ItemStack canister, TileEntityFluidTank tank, EnumFacing side, boolean isCreativeMode)
    {
        int currCapacity = canister.getItemDamage() - 1; 
        if (currCapacity <= 0)
        {
            return null;
        }
        FluidStack liquid = tank.drain(side, currCapacity, false);
        int transferred = ((ItemCanisterGeneric)canister.getItem()).fill(canister, liquid, !isCreativeMode);
        if (transferred > 0)
        {
            liquid = tank.drain(side, transferred, true);
            return canister;
        }
        return null;
    }

    private static ItemStack tryEmptyCanister(ItemStack canister, TileEntityFluidTank tank, EnumFacing side, boolean isCreativeMode)
    {
        int currContents = ItemCanisterGeneric.EMPTY - canister.getItemDamage(); 
        if (currContents <= 0)
        {
            return null;
        }
        FluidStack liquid = ((ItemFluidContainer)canister.getItem()).drain(canister, currContents, false); 
        int transferred = tank.fill(side, liquid, true);
        if (transferred > 0)
        {
            ((ItemFluidContainer)canister.getItem()).drain(canister, transferred, !isCreativeMode);
            return canister;
        }
        return null;
    }
}
