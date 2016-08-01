package codechicken.nei;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;

public class ExtendedCreativeInv implements IInventory {
    PlayerSave playerSave;
    Side side;

    public ExtendedCreativeInv(PlayerSave playerSave, Side side) {
        this.playerSave = playerSave;
        this.side = side;
    }

    @Override
    public int getSizeInventory() {
        return 54;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (side.isClient()) {
            return NEIClientConfig.creativeInv[slot];
        }
        return playerSave.creativeInv[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int size) {
        ItemStack item = getStackInSlot(slot);

        if (item != null) {
            if (item.stackSize <= size) {
                setInventorySlotContents(slot, null);
                markDirty();
                return item;
            }
            ItemStack itemstack1 = item.splitStack(size);
            if (item.stackSize == 0) {
                setInventorySlotContents(slot, null);
            }

            markDirty();
            return itemstack1;
        }
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        synchronized (this) {
            ItemStack stack = getStackInSlot(slot);
            setInventorySlotContents(slot, null);
            return stack;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (side.isClient()) {
            NEIClientConfig.creativeInv[slot] = stack;
        } else {
            playerSave.creativeInv[slot] = stack;
        }

        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
        if (side.isServer()) {
            playerSave.setCreativeDirty();
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1) {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return true;
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
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public IChatComponent getDisplayName() {
        return null;
    }
}
