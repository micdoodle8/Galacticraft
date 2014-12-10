package codechicken.nei.config;

import codechicken.nei.LayoutManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

import static codechicken.lib.gui.GuiDraw.*;

public abstract class OptionButton extends Option
{
    protected static ResourceLocation guiTex = new ResourceLocation("textures/gui/widgets.png");

    public final String prefix;
    public final String text;
    public final String tooltip;
    private boolean isEnabled = true;

    public OptionButton(String name, String prefix, String text, String tooltip) {
        super(name);
        this.prefix = prefix;
        this.text = text;
        this.tooltip = tooltip;
    }

    public OptionButton(String prefix, String text, String tooltip) {
        this(text, prefix, text, tooltip);
    }

    public OptionButton(String name) {
        this(null, name, name + ".tip");
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean b) {
        isEnabled = b;
    }

    @Override
    public void draw(int mx, int my, float frame) {
        changeTexture(guiTex);
        GL11.glColor4f(1, 1, 1, 1);
        drawPrefix();
        drawButton(mx, my);
    }

    public Rectangle buttonSize() {
        if (getPrefix() == null)
            return new Rectangle(0, 0, slot.slotWidth(), 20);

        int width = Math.max(60, getStringWidth(getButtonText()));
        return new Rectangle(slot.slotWidth() - width, 0, width, 20);
    }

    public String getPrefix() {
        if (prefix == null) return null;
        String s = translateN(prefix);
        if (s.equals(namespaced(prefix))) return null;
        return s;
    }

    public String getButtonText() {
        return translateN(name);
    }

    public String getTooltip() {
        if (tooltip == null) return null;
        String s = translateN(tooltip);
        if (s.equals(namespaced(tooltip))) return null;
        return s;
    }

    public void drawPrefix() {
        if (getPrefix() != null)
            drawString(getPrefix(), 10, 6, -1);
    }

    public void drawButton(int mx, int my) {
        Rectangle b = buttonSize();
        LayoutManager.drawButtonBackground(b.x, b.y, b.width, b.height, true, getButtonTex(mx, my));
        drawStringC(getButtonText(), b.x, b.y, b.width, b.height, getTextColour(mx, my));
    }

    public int getButtonTex(int mx, int my) {
        return !isEnabled() ? 0 : pointInside(mx, my) ? 2 : 1;
    }

    public int getTextColour(int mx, int my) {
        return !isEnabled() ? 0xFFA0A0A0 : pointInside(mx, my) ? 0xFFFFFFA0 : 0xFFE0E0E0;
    }

    public boolean pointInside(int mx, int my) {
        return buttonSize().contains(mx, my);
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        if (pointInside(x, y))
            if (onClick(button))
                playClickSound();
    }

    public boolean onClick(int button) {
        return false;
    }

    @Override
    public List<String> handleTooltip(int mx, int my, List<String> currenttip) {
        if (getTooltip() != null)
            currenttip.add(getTooltip());
        return currenttip;
    }
}
