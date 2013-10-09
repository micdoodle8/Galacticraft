package codechicken.nei.forge;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

public interface IContainerSlotClickHandler
{
    public void beforeSlotClick(GuiContainer gui, int slotIndex, int button, Slot slot, int modifier);
    public boolean handleSlotClick(GuiContainer gui, int slotIndex, int button, Slot slot, int modifier, boolean eventconsumed);
    public void afterSlotClick(GuiContainer gui, int slotIndex, int button, Slot slot, int modifier);
}
