package codechicken.core.inventory;

import codechicken.core.gui.GuiWidget;
import codechicken.core.gui.IGuiActionListener;
import codechicken.lib.gui.GuiDraw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GuiContainerWidget extends GuiContainer implements IGuiActionListener {
    public ArrayList<GuiWidget> widgets = new ArrayList<GuiWidget>();

    public GuiContainerWidget(Container inventorySlots) {
        this(inventorySlots, 176, 166);
    }

    public GuiContainerWidget(Container inventorySlots, int xSize, int ySize) {
        super(inventorySlots);
        this.xSize = xSize;
        this.ySize = ySize;
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int i, int j) {
        super.setWorldAndResolution(mc, i, j);
        if (widgets.isEmpty()) {
            addWidgets();
        }
    }

    public void add(GuiWidget widget) {
        widgets.add(widget);
        widget.onAdded(this);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mousex, int mousey) {
        GlStateManager.translate(guiLeft, guiTop, 0);
        drawBackground();
        for (GuiWidget widget : widgets) {
            widget.draw(mousex - guiLeft, mousey - guiTop, f);
        }

        GlStateManager.translate(-guiLeft, -guiTop, 0);
    }

    public void drawBackground() {
    }

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        super.mouseClicked(x, y, button);
        for (GuiWidget widget : widgets) {
            widget.mouseClicked(x - guiLeft, y - guiTop, button);
        }
    }

    @Override
    protected void mouseReleased(int x, int y, int button) {
        super.mouseReleased(x, y, button);
        for (GuiWidget widget : widgets) {
            widget.mouseReleased(x - guiLeft, y - guiTop, button);
        }
    }

    @Override
    protected void mouseClickMove(int x, int y, int button, long time) {
        super.mouseClickMove(x, y, button, time);
        for (GuiWidget widget : widgets) {
            widget.mouseDragged(x - guiLeft, y - guiTop, button, time);
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (mc.currentScreen == this) {
            for (GuiWidget widget : widgets) {
                widget.update();
            }
        }
    }

    @Override
    public void keyTyped(char c, int keycode) throws IOException {
        super.keyTyped(c, keycode);
        for (GuiWidget widget : widgets) {
            widget.keyTyped(c, keycode);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i != 0) {
            Point p = GuiDraw.getMousePosition();
            int scroll = i > 0 ? 1 : -1;
            for (GuiWidget widget : widgets) {
                widget.mouseScrolled(p.x, p.y, scroll);
            }
        }
    }

    @Override
    public void actionPerformed(String ident, Object... params) {
    }

    public void addWidgets() {
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        widgets.clear();
    }
}
