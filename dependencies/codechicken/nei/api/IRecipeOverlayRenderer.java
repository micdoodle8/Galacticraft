package codechicken.nei.api;

import net.minecraft.inventory.Slot;
import codechicken.nei.forge.GuiContainerManager;

public interface IRecipeOverlayRenderer
{
    public void renderOverlay(GuiContainerManager gui, Slot slot);
}
