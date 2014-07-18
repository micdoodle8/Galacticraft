package codechicken.nei.config;

import codechicken.core.gui.GuiCCButton;
import codechicken.core.gui.GuiScreenWidget;
import codechicken.lib.math.MathHelper;
import codechicken.lib.vec.Rectangle4i;
import codechicken.nei.HUDRenderer;
import codechicken.nei.NEIClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.Arrays;

import static codechicken.lib.gui.GuiDraw.displaySize;
import static codechicken.lib.gui.GuiDraw.getMousePosition;

public class GuiHighlightTips extends GuiScreenWidget
{
    private String name;
    private GuiCCButton toggleButton;
    private OptionHighlightTips opt;
    private Point dragDown;

    public GuiHighlightTips(OptionHighlightTips opt) {
        super(80, 20);
        this.opt = opt;
        name = opt.name;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    public void addWidgets() {
        add(toggleButton = new GuiCCButton(0, 0, 80, 20, "").setActionCommand("show"));
        updateNames();
    }

    @Override
    public void actionPerformed(String ident, Object... params) {
        if (ident.equals("show")) {
            opt.getTag(name).setBooleanValue(!show());
            updateNames();
        }
    }

    private void updateNames() {
        toggleButton.text = NEIClientUtils.translate("options." + name + "." + (show() ? "show" : "hide"));
    }

    private boolean show() {
        return opt.getTag(name).getBooleanValue();
    }

    @Override
    public void keyTyped(char c, int keycode) {
        if (keycode == Keyboard.KEY_ESCAPE || keycode == Keyboard.KEY_BACK) {
            Minecraft.getMinecraft().displayGuiScreen(opt.slot.getGui());
            return;
        }
        super.keyTyped(c, keycode);
    }

    @Override
    public void drawScreen(int mousex, int mousey, float f) {
        super.drawScreen(mousex, mousey, f);
        if (show())
            HUDRenderer.renderOverlay(new ItemStack(Blocks.redstone_block), Arrays.asList("RedstoneBlock", EnumChatFormatting.RED+"Sample"), renderPos());
    }

    public Point getPos() {
        return new Point(opt.getTag(name + ".x").getIntValue(), opt.getTag(name + ".y").getIntValue());
    }

    public Dimension sampleSize()//copied from HUDManager when running with the sample for this gui
    {
        return new Dimension(101, 30);
    }

    public Point getDrag() {
        Point mouse = getMousePosition();
        Point drag = new Point(mouse.x - dragDown.x, mouse.y - dragDown.y);
        Dimension size = displaySize();
        Dimension sample = sampleSize();
        drag.x *= 10000;
        drag.y *= 10000;
        drag.x /= (size.width - sample.width);
        drag.y /= (size.height - sample.height);
        Point pos = getPos();
        drag.x = (int) MathHelper.clip(drag.x, -pos.x, 10000 - pos.x);
        drag.y = (int) MathHelper.clip(drag.y, -pos.y, 10000 - pos.y);
        return drag;
    }

    public Point renderPos() {
        Point pos = getPos();
        if (dragDown != null) {
            Point drag = getDrag();
            pos.x += drag.x;
            pos.y += drag.y;
        }

        for (int i = 25; i < 100; i += 25)//snapping
        {
            if (pos.x / 100 == i)
                pos.x = i * 100;
            if (pos.y / 100 == i)
                pos.y = i * 100;
        }

        return pos;
    }

    @Override
    protected void mouseMovedOrUp(int x, int y, int button) {
        if (button == 0 && dragDown != null) {
            setPos(renderPos());
            dragDown = null;
        }
    }

    public Rectangle4i selectionBox() {
        Point pos = renderPos();
        Dimension size = displaySize();
        Dimension rect = sampleSize();
        return new Rectangle4i(
                (size.width - rect.width) * pos.x / 10000,
                (size.height - rect.height) * pos.y / 10000,
                rect.width, rect.height);
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        if (button == 0 && selectionBox().contains(x, y))
            dragDown = getMousePosition();
        else
            super.mouseClicked(x, y, button);
    }

    private void setPos(Point p) {
        opt.getTag(name + ".x").setIntValue(p.x);
        opt.getTag(name + ".y").setIntValue(p.y);
    }
}
