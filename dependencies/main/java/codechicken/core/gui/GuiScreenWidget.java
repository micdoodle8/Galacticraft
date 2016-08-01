package codechicken.core.gui;

import codechicken.lib.gui.GuiDraw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GuiScreenWidget extends GuiScreen implements IGuiActionListener {
    public ArrayList<GuiWidget> widgets = new ArrayList<GuiWidget>();
    public int xSize, ySize, guiTop, guiLeft;

    public GuiScreenWidget() {
        this(176, 166);
    }

    public GuiScreenWidget(int xSize, int ySize) {
        super();
        this.xSize = xSize;
        this.ySize = ySize;
    }

    @Override
    public void initGui() {
        guiTop = (height - ySize) / 2;
        guiLeft = (width - xSize) / 2;
        if (!widgets.isEmpty()) {
            resize();
        }
    }

    public void reset() {
        widgets.clear();
        initGui();
        addWidgets();
        resize();
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int i, int j) {
        boolean init = this.mc == null;
        super.setWorldAndResolution(mc, i, j);
        if (init) {
            addWidgets();
            resize();
        }
    }

    public void add(GuiWidget widget) {
        widgets.add(widget);
        widget.onAdded(this);
    }

    @Override
    public void drawScreen(int mousex, int mousey, float f) {
        GlStateManager.translate(guiLeft, guiTop, 0);
        drawBackground();
        for (GuiWidget widget : widgets) {
            widget.draw(mousex - guiLeft, mousey - guiTop, f);
        }
        drawForeground();
        GlStateManager.translate(-guiLeft, -guiTop, 0);
    }

    public void drawBackground() {
    }

    public void drawForeground() {
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

    public void resize() {
    }

    public void addWidgets() {
    }
}
