package codechicken.lib.util;

import codechicken.lib.inventory.InventoryUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

/**
 * Created by covers1624 on 6/30/2016.
 * TODO, InventoryUtils needs to have things merged to here and such.
 */
public class ItemUtils {

    public static boolean isPlayerHoldingSomething(EntityPlayer player) {
        return player.getHeldItemMainhand() != null || player.getHeldItemOffhand() != null;
    }

    public static ItemStack getHeldStack(EntityPlayer player) {
        ItemStack stack = player.getHeldItemMainhand();
        if (stack == null) {
            stack = player.getHeldItemOffhand();
        }
        return stack;
    }

    /**
     * Drops an item in the world at the given BlockPos
     *
     * @param world    World to drop the item.
     * @param pos      Location to drop item.
     * @param stack    ItemStack to drop.
     * @param velocity The velocity to add.
     */
    public static void dropItem(World world, BlockPos pos, ItemStack stack, double velocity) {
        double xVelocity = world.rand.nextFloat() * velocity + (1.0D - velocity) * 0.5D;
        double yVelocity = world.rand.nextFloat() * velocity + (1.0D - velocity) * 0.5D;
        double zVelocity = world.rand.nextFloat() * velocity + (1.0D - velocity) * 0.5D;
        EntityItem entityItem = new EntityItem(world, pos.getX() + xVelocity, pos.getY() + yVelocity, pos.getZ() + zVelocity, stack);
        entityItem.setPickupDelay(10);
        world.spawnEntityInWorld(entityItem);
    }

    /**
     * Drops an item in the world at the given BlockPos
     *
     * @param world World to drop the item.
     * @param pos   Location to drop item.
     * @param stack ItemStack to drop.
     */
    public static void dropItem(World world, BlockPos pos, ItemStack stack) {
        dropItem(world, pos, stack, 0.7D);
    }

    /**
     * Drops all the items in an IInventory on the ground.
     *
     * @param world     World to drop the item.
     * @param pos       Position to drop item.
     * @param inventory IInventory to drop.
     */
    public static void dropInventory(World world, BlockPos pos, IInventory inventory) {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack != null && stack.stackSize > 0) {
                dropItem(world, pos, stack);
            }
        }
    }

    /**
     * Copy's an ItemStack.
     *
     * @param stack     Stack to copy.
     * @param stackSize Size of the new stack.
     * @return The new stack.
     */
    public static ItemStack copyStack(ItemStack stack, int stackSize) {
        return InventoryUtils.copyStack(stack, stackSize);
    }

    /**
     * Ejects an item with .3 velocity in the given direction.
     *
     * @param world World to spawn the item.
     * @param pos   Location for item to spawn.
     * @param stack Stack to spawn.
     * @param dir   Direction to shoot.
     */
    public static void ejectItem(World world, BlockPos pos, ItemStack stack, EnumFacing dir) {
        pos.offset(dir);
        EntityItem entity = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
        entity.motionX = 0.0;
        entity.motionY = 0.0;
        entity.motionZ = 0.0;

        switch (dir) {
            case DOWN:
                entity.motionY = -0.3;
                break;
            case UP:
                entity.motionY = 0.3;
                break;
            case NORTH:
                entity.motionZ = -0.3;
                break;
            case SOUTH:
                entity.motionZ = 0.3;
                break;
            case WEST:
                entity.motionX = -0.3;
                break;
            case EAST:
                entity.motionX = 0.3;
                break;
            default:
        }

        entity.setPickupDelay(10);
        world.spawnEntityInWorld(entity);
    }

    /**
     * Ejects an list of items with .3 velocity in the given direction.
     *
     * @param world  World to spawn the item.
     * @param pos    Location for item to spawn.
     * @param stacks Stack to spawn.
     * @param dir    Direction to shoot.
     */
    public static void ejectItems(World world, BlockPos pos, List<ItemStack> stacks, EnumFacing dir) {
        for (ItemStack stack : stacks) {
            ejectItem(world, pos, stack, dir);
        }
    }

    /**
     * Gets the burn time for a given ItemStack.
     * Will return 0 if there is no burn time on the item.
     *
     * @param itemStack Stack to get Burn time on.
     * @return Burn time for the Stack.
     */
    public static int getBurnTime(ItemStack itemStack) {
        return TileEntityFurnace.getItemBurnTime(itemStack);
    }

    /**
     * Compares an ItemStack, Useful for comparators.
     *
     * @param stack1 First Stack.
     * @param stack2 Second Stack.
     * @return Returns the difference.
     */
    public static int compareItemStack(ItemStack stack1, ItemStack stack2) {
        int itemStack1ID = Item.getIdFromItem(stack1.getItem());
        int itemStack2ID = Item.getIdFromItem(stack1.getItem());
        return itemStack1ID != itemStack2ID ? itemStack1ID - itemStack2ID : (stack1.getItemDamage() == stack2.getItemDamage() ? 0 : (stack1.getItem().getHasSubtypes() ? stack1.getItemDamage() - stack2.getItemDamage() : 0));
    }

    /**
     * @param stack1 The {@link ItemStack} being compared.
     * @param stack2 The {@link ItemStack} to compare to.
     * @return whether the two items are the same in terms of damage and itemID.
     */
    public static boolean areStacksSameType(ItemStack stack1, ItemStack stack2) {
        return stack1 != null && stack2 != null && (stack1.getItem() == stack2.getItem() && (!stack2.getHasSubtypes() || stack2.getItemDamage() == stack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack2, stack1));
    }

    /**
     * {@link ItemStack}s with damage 32767 are wildcards allowing all damages. Eg all colours of wool are allowed to create Beds.
     *
     * @param stack1 The {@link ItemStack} being compared.
     * @param stack2 The {@link ItemStack} to compare to.
     * @return whether the two items are the same from the perspective of a crafting inventory.
     */
    public static boolean areStacksSameTypeCrafting(ItemStack stack1, ItemStack stack2) {
        return stack1 != null && stack2 != null && stack1.getItem() == stack2.getItem() && (stack1.getItemDamage() == stack2.getItemDamage() || stack1.getItemDamage() == OreDictionary.WILDCARD_VALUE || stack2.getItemDamage() == OreDictionary.WILDCARD_VALUE || stack1.getItem().isDamageable());
    }
}
