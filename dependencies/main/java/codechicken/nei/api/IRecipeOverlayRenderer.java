package codechicken.nei.api;

import codechicken.nei.guihook.GuiContainerManager;
import net.minecraft.inventory.Slot;

public interface IRecipeOverlayRenderer {
    void renderOverlay(GuiContainerManager gui, Slot slot);
}
