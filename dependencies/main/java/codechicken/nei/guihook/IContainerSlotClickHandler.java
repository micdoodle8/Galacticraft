package codechicken.nei.guihook;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;

public interface IContainerSlotClickHandler {
    void beforeSlotClick(GuiContainer gui, int slotIndex, int button, Slot slot, ClickType clickType);

    boolean handleSlotClick(GuiContainer gui, int slotIndex, int button, Slot slot, ClickType clickType, boolean eventConsumed);

    void afterSlotClick(GuiContainer gui, int slotIndex, int button, Slot slot, ClickType clickType);
}
