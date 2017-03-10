package codechicken.lib.util;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

/**
 * Created by covers1624 on 30/08/2016.
 */
@Deprecated
public class ContainerUtils {

    public static void bindPlayerInventory(Container container, InventoryPlayer inventoryPlayer, int xStart, int yStart) {
        // Main inventory
        for (int i = 0; i < 3; i++) {// Rows, 3 Rows
            for (int j = 0; j < 9; j++) {// Columns, 9 Slots per row
                int slot = j + i * 9 + 9;
                int x = xStart + j * 18;
                int y = yStart + i * 18;
                addSlotToContainer(container, new Slot(inventoryPlayer, slot, x, y));
            }
        }

        // Hotbar
        for (int i = 0; i < 9; i++) {
            addSlotToContainer(container, new Slot(inventoryPlayer, i, xStart + i * 18, yStart + 58));
        }
    }

    public static Slot addSlotToContainer(Container container, Slot slot) {
        slot.slotNumber = container.inventorySlots.size();
        //Covers1624Lib.logger.info(slot.slotNumber);
        container.inventorySlots.add(slot);
        container.inventoryItemStacks.add(null);
        return slot;
    }
}
