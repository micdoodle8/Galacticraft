package codechicken.lib.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

/**
 * IInventory implementation which saves and loads from an NBT tag
 */
public class InventoryNBT implements IInventory {
    protected ItemStack[] items;
    protected NBTTagCompound tag;

    public InventoryNBT(int size, NBTTagCompound tag) {
        this.tag = tag;
        items = new ItemStack[size];
        readNBT();
    }

    private void writeNBT() {
        tag.setTag("items", InventoryUtils.writeItemStacksToTag(items, getInventoryStackLimit()));
    }

    private void readNBT() {
        if (tag.hasKey("items")) {
            InventoryUtils.readItemStacksFromTag(items, tag.getTagList("items", 10));
        }
    }

    @Override
    public int getSizeInventory() {
        return items.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return items[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return InventoryUtils.decrStackSize(this, slot, amount);
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        return InventoryUtils.getStackInSlotOnClosing(this, slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        items[slot] = stack;
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
        writeNBT();
    }

    @Override
    public void clear() {
        for (int i = 0; i < items.length; i++) {
            items[i] = null;
        }
        markDirty();
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
    public boolean isUseableByPlayer(EntityPlayer var1) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public String getName() {
        return "NBT";
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    @Override
    public IChatComponent getDisplayName() {
        return new ChatComponentText(getName());
    }
}
