package codechicken.nei.guihook;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;

public class DefaultSlotClickHandler implements IContainerSlotClickHandler {
    @Override
    public void beforeSlotClick(GuiContainer gui, int slotIndex, int button, Slot slot, ClickType modifier) {
    }

    @Override
    public boolean handleSlotClick(GuiContainer gui, int slotIndex, int button, Slot slot, ClickType clickType, boolean eventConsumed) {
        if (!eventConsumed) {
            callHandleMouseClick(gui, slot, slotIndex, button, clickType);
        }

        return true;
    }

    private static void callHandleMouseClick(GuiContainer gui, Slot slot, int slotIndex, int button, ClickType modifiers) {
        //calls GuiContainer.mouseClicked using ASM generated forwarder
    }

    @Override
    public void afterSlotClick(GuiContainer gui, int slotIndex, int button, Slot slot, ClickType clickType) {
    }
}
