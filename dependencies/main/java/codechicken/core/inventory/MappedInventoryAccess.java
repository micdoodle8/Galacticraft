package codechicken.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MappedInventoryAccess implements IInventory {
    public interface InventoryAccessor {
        boolean canAccessSlot(int slot);
    }

    public static final InventoryAccessor fullAccess = new InventoryAccessor() {
        public boolean canAccessSlot(int slot) {
            return true;
        }
    };

    private ArrayList<Integer> slotMap = new ArrayList<Integer>();
    private IInventory inv;
    private ArrayList<InventoryAccessor> accessors = new ArrayList<MappedInventoryAccess.InventoryAccessor>();

    public MappedInventoryAccess(IInventory inv, InventoryAccessor... accessors) {
        this.inv = inv;
        Collections.addAll(this.accessors, accessors);
        reset();
    }

    public void reset() {
        slotMap.clear();
        nextslot:
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            for (InventoryAccessor a : accessors) {
                if (!a.canAccessSlot(i)) {
                    continue nextslot;
                }
            }

            slotMap.add(i);
        }
    }

    @Override
    public int getSizeInventory() {
        return slotMap.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inv.getStackInSlot(slotMap.get(slot));
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return inv.decrStackSize(slotMap.get(slot), amount);
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        return inv.removeStackFromSlot(slotMap.get(slot));
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inv.setInventorySlotContents(slotMap.get(slot), stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return inv.getInventoryStackLimit();
    }

    @Override
    public void markDirty() {
        inv.markDirty();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return inv.isUseableByPlayer(player);
    }

    public void addAccessor(InventoryAccessor accessor) {
        accessors.add(accessor);
        reset();
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return inv.isItemValidForSlot(slotMap.get(slot), stack);
    }

    @Override
    public void openInventory(EntityPlayer player) {
        inv.openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        inv.closeInventory(player);
    }

    @Override
    public int getField(int id) {
        return inv.getField(id);
    }

    @Override
    public void setField(int id, int value) {
        inv.setField(id, value);
    }

    @Override
    public int getFieldCount() {
        return inv.getFieldCount();
    }

    @Override
    public void clear() {
        inv.clear();
    }

    @Override
    public String getName() {
        return inv.getName();
    }

    @Override
    public boolean hasCustomName() {
        return inv.hasCustomName();
    }

    @Override
    public ITextComponent getDisplayName() {
        return inv.getDisplayName();
    }

    public List<InventoryAccessor> accessors() {
        return accessors;
    }
}
