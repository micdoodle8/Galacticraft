package codechicken.lib.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

/**
 * Inventory wrapper for unified ISided/IInventory access
 */
public class InventoryRange {
    public IInventory inv;
    public EnumFacing face;
    public ISidedInventory sidedInv;
    public int[] slots;

    public InventoryRange(IInventory inv, int side) {
        this.inv = inv;
        this.face = EnumFacing.values()[side];
        if (inv instanceof ISidedInventory) {
            sidedInv = (ISidedInventory) inv;
            slots = sidedInv.getSlotsForFace(face);
        } else {
            slots = new int[inv.getSizeInventory()];
            for (int i = 0; i < slots.length; i++) {
                slots[i] = i;
            }
        }
    }

    public InventoryRange(IInventory inv) {
        this(inv, 0);
    }

    public InventoryRange(IInventory inv, int fslot, int lslot) {
        this.inv = inv;
        slots = new int[lslot - fslot];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = fslot + i;
        }
    }

    public InventoryRange(IInventory inv, InventoryRange access) {
        this.inv = inv;
        this.slots = access.slots;
        this.face = access.face;
        if (inv instanceof ISidedInventory) {
            sidedInv = (ISidedInventory) inv;
        }
    }

    public boolean canInsertItem(int slot, ItemStack item) {
        return sidedInv == null ? inv.isItemValidForSlot(slot, item) : sidedInv.canInsertItem(slot, item, face);
    }

    public boolean canExtractItem(int slot, ItemStack item) {
        return sidedInv == null ? inv.isItemValidForSlot(slot, item) : sidedInv.canExtractItem(slot, item, face);
    }

    public int lastSlot() {
        int last = 0;
        for (int slot : slots) {
            if (slot > last) {
                last = slot;
            }
        }
        return last;
    }
}
