package codechicken.nei;

import codechicken.lib.vec.Rectangle4i;
import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class Widget
{
    public Widget() {
    }

    public abstract void draw(int mx, int my);

    public void postDraw(int mx, int my) {
    }

    public boolean handleClick(int mx, int my, int button) {
        return true;
    }

    public void onGuiClick(int mx, int my) {
    }

    public void mouseUp(int mx, int my, int button) {

    }

    public boolean handleKeyPress(int keyID, char keyChar) {
        return false;
    }

    public void lastKeyTyped(int keyID, char keyChar) {
    }

    public boolean handleClickExt(int mx, int my, int button) {
        return false;
    }

    public boolean onMouseWheel(int i, int mx, int my) {
        return false;
    }

    public void update() {
    }

    public Rectangle4i bounds() {
        return new Rectangle4i(x, y, w, h);
    }

    public boolean contains(int px, int py) {
        return bounds().contains(px, py);
    }

    public ItemStack getStackMouseOver(int mx, int my) {
        return null;
    }

    public void mouseDragged(int mx, int my, int button, long heldTime) {
    }

    public List<String> handleTooltip(int mx, int my, List<String> tooltip) {
        return tooltip;
    }

    public void loseFocus() {
    }

    public void gainFocus() {
    }

    public int x;
    public int y;
    public int z;
    public int w;
    public int h;
}
