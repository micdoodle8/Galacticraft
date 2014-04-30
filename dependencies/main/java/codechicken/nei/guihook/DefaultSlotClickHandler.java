package codechicken.nei.guihook;

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
            callHandleMouseClick(gui, slot, slotIndex, button, modifier);

        return true;
    }

    private static void callHandleMouseClick(GuiContainer gui, Slot slot, int slotIndex, int button, int modifiers) {
        //calls GuiContainer.mouseClicked using ASM generated forwarder
    }

    @Override
    public void afterSlotClick(GuiContainer gui, int slotIndex, int button, Slot slot, int modifier)
    {
    }
}
