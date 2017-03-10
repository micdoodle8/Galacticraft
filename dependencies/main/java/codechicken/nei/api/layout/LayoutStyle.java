package codechicken.nei.api.layout;

import codechicken.nei.VisibilityData;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.widget.Button;
import net.minecraft.client.gui.inventory.GuiContainer;

public abstract class LayoutStyle {
    public abstract void init();

    public abstract void reset();

    public abstract void layout(GuiContainer gui, VisibilityData visibility);

    public abstract String getName();

    public void drawBackground(GuiContainerManager gui) {
    }

    public abstract void drawButton(Button button, int mouseX, int mouseY);

    public abstract void drawSubsetTag(String text, int x, int y, int w, int h, int state, boolean mouseover);
}
