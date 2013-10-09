package codechicken.nei.forge;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;

public class DefaultSlotClickHandler implements IContainerSlotClickHandler
{
    @Override
    public void beforeSlotClick(GuiContainer gui, int slotIndex, int button, Slot slot, int modifier)
    {
    }

    @Override
    public boolean handleSlotClick(GuiContainer gui, int slotIndex, int button, Slot slot, int modifier, boolean eventconsumed)
    {
        if(!eventconsumed)
            gui.sendMouseClick(slot, slotIndex, button, slotIndex != -999 ? modifier : 0);
        return true;
    }

    @Override
    public void afterSlotClick(GuiContainer gui, int slotIndex, int button, Slot slot, int modifier)
    {
    }
}
