package codechicken.lib.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

/**
 * Creates a copy of an IInventory for extended simulation
 */
public class InventoryCopy implements IInventory {

    public boolean[] accessible;
    public ItemStack[] items;
    public IInventory inv;

    public InventoryCopy(IInventory inv) {
        items = new ItemStack[inv.getSizeInventory()];
        accessible = new boolean[inv.getSizeInventory()];
        this.inv = inv;
        update();
    }

    public void update() {
        for (int i = 0; i < items.length; i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != null) {
                items[i] = stack.copy();
            }
        }
    }

    public InventoryCopy open(InventoryRange access) {
        int lslot = access.lastSlot();
        if (lslot > accessible.length) {
            boolean[] l_accessible = new boolean[lslot];
            ItemStack[] l_items = new ItemStack[lslot];
            System.arraycopy(accessible, 0, l_accessible, 0, accessible.length);
            System.arraycopy(items, 0, l_items, 0, items.length);
            accessible = l_accessible;
            items = l_items;
        }

        for (int slot : access.slots) {
            accessible[slot] = true;
        }
        return this;
    }

    @Override
    public int getSizeInventory() {
        return items.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return items[slot];
    }

    public ItemStack decrStackSize(int slot, int amount) {
        return InventoryUtils.decrStackSize(this, slot, amount);
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        return InventoryUtils.removeStackFromSlot(this, slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        items[slot] = stack;
        markDirty();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return inv.isItemValidForSlot(i, itemstack);
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
    }

    @Override
    public String getName() {
        return "copy";
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(getName());
    }
}
