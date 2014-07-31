package codechicken.nei.config;

import codechicken.core.gui.GuiCCButton;
import codechicken.core.gui.GuiScreenWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.*;
import java.util.List;

import static codechicken.lib.gui.GuiDraw.drawMultilineTip;
import static codechicken.lib.gui.GuiDraw.fontRenderer;
import static codechicken.lib.gui.GuiDraw.getMousePosition;
import static net.minecraft.util.StatCollector.translateToLocal;

public abstract class GuiOptionPane extends GuiScreenWidget
{
    public class ScrollPane extends OptionScrollPane
    {
        @Override
        public int contentHeight() {
            return GuiOptionPane.this.contentHeight();
        }

        @Override
        public void drawContent(int mx, int my, float frame) {
            GuiOptionPane.this.drawContent(mx, my, frame);
        }
    }

    public abstract int contentHeight();
    public abstract void drawContent(int mx, int my, float frame);

    public ScrollPane pane;
    public GuiCCButton backButton;

    public Rectangle drawBounds() {
        Rectangle w = pane.windowBounds();
        w.y -= pane.scrolledPixels();
        return w;
    }

    @Override
    public void initGui() {
        xSize = width;
        ySize = height;
        super.initGui();

        if (pane != null) {
            pane.resize();
            backButton.width = Math.min(200, width - 40);
            backButton.x = (width - backButton.width) / 2;
            backButton.y = height - 25;
        }
    }

    @Override
    public void addWidgets() {
        add(pane = new ScrollPane());
        add(backButton = new GuiCCButton(0, 0, 0, 20, translateToLocal("nei.options.back")).setActionCommand("back"));
        initGui();
    }

    @Override
    public void actionPerformed(String ident, Object... params) {
        if (ident.equals("back"))
            Minecraft.getMinecraft().displayGuiScreen(getParentScreen());
    }

    @Override
    public void keyTyped(char c, int keycode) {
        if (keycode == Keyboard.KEY_ESCAPE || keycode == Keyboard.KEY_BACK) {
            Minecraft.getMinecraft().displayGuiScreen(getParentScreen());
            return;
        }
        super.keyTyped(c, keycode);
    }

    public abstract GuiScreen getParentScreen();

    @Override
    public void drawBackground() {
        drawDefaultBackground();
    }

    @Override
    public void drawForeground() {
        drawCenteredString(fontRenderer, getTitle(), width / 2, 6, -1);
        drawTooltip();
    }

    public void drawTooltip() {
        Point mouse = getMousePosition();
        drawMultilineTip(mouse.x + 12, mouse.y - 12, handleTooltip(mouse.x, mouse.y, new LinkedList<String>()));
    }

    public List<String> handleTooltip(int mx, int my, List<String> tooltip) {
        return tooltip;
    }

    public abstract String getTitle();

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
