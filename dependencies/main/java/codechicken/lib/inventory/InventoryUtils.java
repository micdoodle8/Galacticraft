package codechicken.lib.inventory;

import codechicken.lib.vec.Vector3;
import com.google.common.base.Objects;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Plane;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class InventoryUtils {

    public static boolean hasItemHandlerCap(TileEntity tileEntity, EnumFacing face) {
        return tileEntity != null && (tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face) || tileEntity instanceof ISidedInventory || tileEntity instanceof IInventory);
    }

    public static IItemHandler getItemHandlerCap(TileEntity tileEntity, EnumFacing face) {
        if (tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face)) {
            return tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face);
        } else if (tileEntity instanceof ISidedInventory) {
            return new SidedInvWrapper(((ISidedInventory) tileEntity), face);
        } else if (tileEntity instanceof IInventory) {
            return new InvWrapper(((IInventory) tileEntity));
        }
        return new EmptyHandler();
    }

    /**
     * Constructor for ItemStack with tag
     */
    public static ItemStack newItemStack(Item item, int size, int damage, NBTTagCompound tag) {
        ItemStack stack = new ItemStack(item, size, damage);
        stack.setTagCompound(tag);
        return stack;
    }

    /**
     * Gets the actual damage of an item without asking the Item
     */
    public static int actualDamage(ItemStack stack) {
        return Items.DIAMOND.getDamage(stack);
    }

    /**
     * Static default implementation for IInventory method
     */
    public static ItemStack decrStackSize(IInventory inv, int slot, int size) {
        ItemStack item = inv.getStackInSlot(slot);

        if (item != null) {
            if (item.stackSize <= size) {
                inv.setInventorySlotContents(slot, null);
                inv.markDirty();
                return item;
            }
            ItemStack itemstack1 = item.splitStack(size);
            if (item.stackSize == 0) {
                inv.setInventorySlotContents(slot, null);
            } else {
                inv.setInventorySlotContents(slot, item);
            }

            inv.markDirty();
            return itemstack1;
        }
        return null;
    }

    /**
     * Static default implementation for IInventory method
     */
    public static ItemStack removeStackFromSlot(IInventory inv, int slot) {
        ItemStack stack = inv.getStackInSlot(slot);
        inv.setInventorySlotContents(slot, null);
        return stack;
    }

    /**
     * @return The quantity of items from addition that can be added to base
     */
    public static int incrStackSize(ItemStack base, ItemStack addition) {
        if (canStack(base, addition)) {
            return incrStackSize(base, addition.stackSize);
        }

        return 0;
    }

    /**
     * @return The quantity of items from addition that can be added to base
     */
    public static int incrStackSize(ItemStack base, int addition) {
        int totalSize = base.stackSize + addition;

        if (totalSize <= base.getMaxStackSize()) {
            return addition;
        } else if (base.stackSize < base.getMaxStackSize()) {
            return base.getMaxStackSize() - base.stackSize;
        }

        return 0;
    }

    /**
     * NBT item saving function
     */
    public static NBTTagList writeItemStacksToTag(ItemStack[] items) {
        return writeItemStacksToTag(items, 64);
    }

    /**
     * NBT item saving function with support for stack sizes > 32K
     */
    public static NBTTagList writeItemStacksToTag(ItemStack[] items, int maxQuantity) {
        NBTTagList tagList = new NBTTagList();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setShort("Slot", (short) i);
                items[i].writeToNBT(tag);

                if (maxQuantity > Short.MAX_VALUE) {
                    tag.setInteger("Quantity", items[i].stackSize);
                } else if (maxQuantity > Byte.MAX_VALUE) {
                    tag.setShort("Quantity", (short) items[i].stackSize);
                }

                tagList.appendTag(tag);
            }
        }
        return tagList;
    }

    /**
     * NBT item loading function with support for stack sizes > 32K
     */
    public static void readItemStacksFromTag(ItemStack[] items, NBTTagList tagList) {
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tag = tagList.getCompoundTagAt(i);
            int b = tag.getShort("Slot");
            items[b] = ItemStack.loadItemStackFromNBT(tag);
            if (tag.hasKey("Quantity")) {
                items[b].stackSize = ((NBTPrimitive) tag.getTag("Quantity")).getInt();
            }
        }
    }

    /**
     * Spawns an itemstack in the world at a location
     */
    public static void dropItem(ItemStack stack, World world, Vector3 dropLocation) {
        EntityItem item = new EntityItem(world, dropLocation.x, dropLocation.y, dropLocation.z, stack);
        item.motionX = world.rand.nextGaussian() * 0.05;
        item.motionY = world.rand.nextGaussian() * 0.05 + 0.2F;
        item.motionZ = world.rand.nextGaussian() * 0.05;
        world.spawnEntityInWorld(item);
    }

    /**
     * Copies an itemstack with a new quantity
     */
    public static ItemStack copyStack(ItemStack stack, int quantity) {
        if (stack == null) {
            return null;
        }

        stack = stack.copy();
        stack.stackSize = quantity;
        return stack;
    }

    /**
     * Gets the maximum quantity of an item that can be inserted into inv
     */
    public static int getInsertibleQuantity(InventoryRange inv, ItemStack stack) {
        int quantity = 0;
        stack = copyStack(stack, Integer.MAX_VALUE);
        for (int slot : inv.slots) {
            quantity += fitStackInSlot(inv, slot, stack);
        }

        return quantity;
    }

    public static int getInsertibleQuantity(IInventory inv, ItemStack stack) {
        return getInsertibleQuantity(new InventoryRange(inv), stack);
    }

    public static int fitStackInSlot(InventoryRange inv, int slot, ItemStack stack) {
        ItemStack base = inv.inv.getStackInSlot(slot);
        if (!canStack(base, stack) || !inv.canInsertItem(slot, stack)) {
            return 0;
        }

        int fit = base != null ? incrStackSize(base, inv.inv.getInventoryStackLimit() - base.stackSize) : inv.inv.getInventoryStackLimit();
        return Math.min(fit, stack.stackSize);
    }

    public static int fitStackInSlot(IInventory inv, int slot, ItemStack stack) {
        return fitStackInSlot(new InventoryRange(inv), slot, stack);
    }

    /**
     * @param simulate If set to true, no items will actually be inserted
     * @return The number of items unable to be inserted
     */
    public static int insertItem(InventoryRange inv, ItemStack stack, boolean simulate) {
        stack = stack.copy();
        for (int pass = 0; pass < 2; pass++) {
            for (int slot : inv.slots) {
                ItemStack base = inv.inv.getStackInSlot(slot);
                if ((pass == 0) == (base == null)) {
                    continue;
                }
                int fit = fitStackInSlot(inv, slot, stack);
                if (fit == 0) {
                    continue;
                }

                if (base != null) {
                    stack.stackSize -= fit;
                    if (!simulate) {
                        base.stackSize += fit;
                        inv.inv.setInventorySlotContents(slot, base);
                    }
                } else {
                    if (!simulate) {
                        inv.inv.setInventorySlotContents(slot, copyStack(stack, fit));
                    }
                    stack.stackSize -= fit;
                }
                if (stack.stackSize == 0) {
                    return 0;
                }
            }
        }
        return stack.stackSize;
    }

    public static int insertItem(IInventory inv, ItemStack stack, boolean simulate) {
        return insertItem(new InventoryRange(inv), stack, simulate);
    }

    /**
     * Gets the stack in slot if it can be extracted
     */
    public static ItemStack getExtractableStack(InventoryRange inv, int slot) {
        ItemStack stack = inv.inv.getStackInSlot(slot);
        if (stack == null || !inv.canExtractItem(slot, stack)) {
            return null;
        }

        return stack;
    }

    public static ItemStack getExtractableStack(IInventory inv, int slot) {
        return getExtractableStack(new InventoryRange(inv), slot);
    }

    public static boolean areStacksIdentical(ItemStack stack1, ItemStack stack2) {
        if (stack1 == null || stack2 == null) {
            return stack1 == stack2;
        }

        return stack1.getItem() == stack2.getItem() && stack1.getItemDamage() == stack2.getItemDamage() && stack1.stackSize == stack2.stackSize && Objects.equal(stack1.getTagCompound(), stack2.getTagCompound());
    }

    /**
     * Gets an IInventory from a coordinate with support for double chests
     */
    public static IInventory getInventory(World world, BlockPos pos) {
        return getInventory(world.getTileEntity(pos));
    }

    public static IInventory getInventory(TileEntity tile) {
        if (!(tile instanceof IInventory)) {
            return null;
        }

        if (tile instanceof TileEntityChest) {
            return getChest((TileEntityChest) tile);
        }
        return (IInventory) tile;
    }

    public static IInventory getChest(TileEntityChest chest) {
        for (EnumFacing fside : Plane.HORIZONTAL) {
            if (chest.getWorld().getBlockState(chest.getPos().offset(fside)).getBlock() == chest.getBlockType()) {
                return new InventoryLargeChest("container.chestDouble", (TileEntityChest) chest.getWorld().getTileEntity(chest.getPos().offset(fside)), chest);
            }
        }
        return chest;
    }

    public static boolean canStack(ItemStack stack1, ItemStack stack2) {
        return stack1 == null || stack2 == null || (stack1.getItem() == stack2.getItem() && (!stack2.getHasSubtypes() || stack2.getItemDamage() == stack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack2, stack1)) && stack1.isStackable();
    }

    /**
     * Consumes one item from slot in inv with support for containers.
     */
    public static void consumeItem(IInventory inv, int slot) {
        ItemStack stack = inv.getStackInSlot(slot);
        Item item = stack.getItem();
        if (item.hasContainerItem(stack)) {
            ItemStack container = item.getContainerItem(stack);
            inv.setInventorySlotContents(slot, container);
        } else {
            inv.decrStackSize(slot, 1);
        }
    }

    /**
     * Gets the size of the stack in a slot. Returns 0 on null stacks
     */
    public static int stackSize(IInventory inv, int slot) {
        ItemStack stack = inv.getStackInSlot(slot);
        return stack == null ? 0 : stack.stackSize;
    }

    /**
     * Drops all items from inv using removeStackFromSlot
     */
    public static void dropOnClose(EntityPlayer player, IInventory inv) {
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.removeStackFromSlot(i);
            if (stack != null) {
                player.dropItem(stack, false);
            }
        }
    }

    public static NBTTagCompound savePersistant(ItemStack stack, NBTTagCompound tag) {
        stack.writeToNBT(tag);
        tag.removeTag("id");
        tag.setString("name", stack.getItem().getRegistryName().toString());
        return tag;
    }

    public static ItemStack loadPersistant(NBTTagCompound tag) {
        String name = tag.getString("name");
        Item item = Item.REGISTRY.getObject(new ResourceLocation(name));
        if (item == null) {
            return null;
        }
        int count = tag.hasKey("Count") ? tag.getByte("Count") : 1;
        int damage = tag.hasKey("Damage") ? tag.getShort("Damage") : 0;
        ItemStack stack = new ItemStack(item, count, damage);
        if (tag.hasKey("tag", 10)) {
            stack.setTagCompound(tag.getCompoundTag("tag"));
        }
        return stack;
    }
}
