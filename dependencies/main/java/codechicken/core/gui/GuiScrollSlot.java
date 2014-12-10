package codechicken.core.gui;

import org.lwjgl.input.Keyboard;

import java.awt.*;

public abstract class GuiScrollSlot extends GuiScrollPane
{
    protected String actionCommand;
    public boolean focused;
    protected ClickCounter<Integer> click = new ClickCounter<Integer>();
    public boolean smoothScroll = true;

    public GuiScrollSlot(int x, int y, int width, int height) {
        super(x, y, width, height);
        setMargins(3, 2, 3, 2);
    }

    public GuiScrollSlot setActionCommand(String s) {
        actionCommand = s;
        return this;
    }

    public void setSmoothScroll(boolean b) {
        smoothScroll = b;
    }

    public abstract int getSlotHeight(int slot);

    protected abstract int getNumSlots();

    public void selectNext() {}

    public void selectPrev() {}

    /**
     * Coordinates are relative to slot area
     */
    protected abstract void slotClicked(int slot, int button, int mx, int my, int count);

    protected abstract void drawSlot(int slot, int x, int y, int mx, int my, float frame);

    protected void unfocus() {
    }

    public void setFocused(boolean focus) {
        focused = focus;
        if (!focused)
            unfocus();
    }

    @Override
    public int contentHeight() {
        return getSlotY(getNumSlots());
    }

    public int getSlotY(int slot) {
        int h = 0;
        for(int i = 0; i < slot; i++)
            h+=getSlotHeight(i);
        return h;
    }

    public int getSlot(int my) {
        if (my < 0)
            return -1;

        int y = 0;
        for(int i = 0; i < getNumSlots(); i++) {
            int h = getSlotHeight(i);
            if(my >= y && my < y+h)
                return i;
            y+=h;
        }
        return -1;
    }

    public int getClickedSlot(int my) {
        return getSlot(my-windowBounds().y+scrolledPixels());
    }

    @Override
    public int scrolledPixels() {
        int scrolled = super.scrolledPixels();
        if(!smoothScroll) {
            int slot = getSlot(scrolled);
            int sloty = getSlotY(slot);
            int sloth = getSlotHeight(slot);
            scrolled = sloty+(int)((scrolled-sloty)/(double)sloth+0.5)*sloth;
        }
        return scrolled;
    }

    /**
     * @return -1 for left, 1 for right
     */
    public int scrollbarAlignment() {
        return 1;
    }

    @Override
    public int scrollbarGuideAlignment() {
        return scrollbarAlignment();
    }

    @Override
    public Rectangle scrollbarBounds() {
        Rectangle r = super.scrollbarBounds();
        if(scrollbarAlignment() == -1)
            r.x = x;
        return r;
    }

    public void showSlot(int slot) {
        showSlot(getSlotY(slot), getSlotHeight(slot));
    }

    @Override
    public void slotDown(int mx, int my, int button) {
        int slot = getSlot(my);
        click.mouseDown(slot >= 0 ? slot : null, button);
    }

    @Override
    public void slotUp(int mx, int my, int button) {
        int slot = getSlot(my);
        int c = click.mouseUp(slot >= 0 ? slot : null, button);
        if(c > 0 && slot >= 0)
            slotClicked(slot, button, mx, my-getSlotY(slot), c);
    }

    @Override
    public void keyTyped(char c, int keycode) {
        if (!focused)
            return;

        if (keycode == Keyboard.KEY_UP)
            selectPrev();
        if (keycode == Keyboard.KEY_DOWN)
            selectNext();
        if (keycode == Keyboard.KEY_RETURN && actionCommand != null)
            sendAction(actionCommand);
    }

    @Override
    public void drawContent(int mx, int my, float frame) {
        int scrolled = scrolledPixels();
        Rectangle w = windowBounds();
        int y = 0;
        for (int slot = 0; slot < getNumSlots(); slot++) {
            int h = getSlotHeight(slot);
            if (y+h > scrolled && y < scrolled+w.height)
                drawSlot(slot, w.x, w.y+y-scrolledPixels(), mx, my-y, frame);
            y+=h;
        }
    }
}
