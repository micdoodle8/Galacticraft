package codechicken.nei.api;

import codechicken.nei.Button;
import codechicken.nei.VisiblityData;
import codechicken.nei.guihook.GuiContainerManager;
import net.minecraft.client.gui.inventory.GuiContainer;

public abstract class LayoutStyle
{
    public abstract void init();
    public abstract void reset();
    public abstract void layout(GuiContainer gui, VisiblityData visibility);
    public abstract String getName();
    
    public void drawBackground(GuiContainerManager gui)
    {
    }
    public abstract void drawButton(Button button, int mousex, int mousey);
    public abstract boolean texturedButtons();
}
