package codechicken.core.gui;

import codechicken.lib.math.MathHelper;
import codechicken.lib.vec.Rectangle4i;

import java.awt.*;

public abstract class GuiScrollPane extends GuiWidget
{
    protected int scrollclicky = -1;
    protected float scrollpercent;
    protected int scrollmousey;
    protected float percentscrolled;

    public int marginleft;
    public int margintop;
    public int marginright;
    public int marginbottom;

    public GuiScrollPane(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void setMargins(int left, int top, int right, int bottom) {
        marginleft = left;
        margintop = top;
        marginright = right;
        marginbottom = bottom;
    }

    public Rectangle windowBounds() {
        return new Rectangle(x+marginleft, y+margintop, width-marginleft-marginright, height-margintop-marginbottom);
    }

    public abstract int contentHeight();

    public Dimension scrollbarDim() {
        int h = (int) ((windowBounds().getHeight() / contentHeight()) * height);
        return new Dimension(5, h > height ? height : h < height / 15 ? height / 15 : h);
    }

    public Rectangle scrollbarBounds() {
        Dimension dim = scrollbarDim();
        return new Rectangle(
                x + width - dim.width, y + (int) ((height - dim.height) * percentscrolled + 0.4999),
                dim.width, dim.height);
    }

    public void scrollUp() {
        scroll(-5);
    }

    public void scrollDown() {
        scroll(5);
    }

    public void scroll(int i) {
        percentscrolled += i * height / (float)(2*contentHeight());
        calculatePercentScrolled();
    }

    public void calculatePercentScrolled() {
        int barempty = height - scrollbarDim().height;
        if (isScrolling()) {
            int scrolldiff = scrollmousey - scrollclicky;
            percentscrolled = scrolldiff / (float) barempty + scrollpercent;
        }
        percentscrolled = (float) MathHelper.clip(percentscrolled, 0, 1);
    }

    public boolean isScrolling() {
        return scrollclicky >= 0;
    }

    public boolean hasScrollbar() {
        return contentHeight() > height;
    }

    /**
     * @return -1 for left, 0 for none, 1 for right
     */
    public int scrollbarGuideAlignment() {
        return -1;
    }

    public void showSlot(int sloty, int slotHeight) {
        Rectangle w = windowBounds();
        if (sloty + slotHeight > w.y + w.height) {
            int diff = sloty - (w.y + w.height - slotHeight);
            percentscrolled += diff / (double) (contentHeight() - w.height);
            calculatePercentScrolled();
        } else if (sloty < w.y) {
            int diff = w.y - sloty;
            percentscrolled -= diff / (double) (contentHeight() - w.height);
            calculatePercentScrolled();
        }
    }

    @Override
    public void mouseClicked(int mx, int my, int button) {
        Rectangle sbar = scrollbarBounds();
        Rectangle w = windowBounds();
        int barempty = height - sbar.height;

        if (button == 0 &&
                sbar.height < height && //the scroll bar can move (not full length)
                mx >= sbar.x && mx <= sbar.x + sbar.width &&
                my >= y && my <= y + height)//in the scroll pane
        {
            if (my < sbar.y) {
                percentscrolled = (my - y) / (float) barempty;
                calculatePercentScrolled();
            } else if (my > sbar.y+sbar.height) {
                percentscrolled = (my - y - sbar.height + 1) / (float) barempty;
                calculatePercentScrolled();
            } else {
                scrollclicky = my;
                scrollpercent = percentscrolled;
                scrollmousey = my;
            }
        } else if (w.contains(mx, my))
            slotDown(mx - w.x, my - w.y + scrolledPixels(), button);
    }

    /**
     * Mouse down on slot area
     * Coordinates relative to slot content area
     */
    public void slotDown(int mx, int my, int button) {}

    /**
     * Mouse up on slot area
     * Coordinates relative to slot content area
     */
    public void slotUp(int mx, int my, int button) {}

    @Override
    public void mouseMovedOrUp(int mx, int my, int button) {
        Rectangle w = windowBounds();
        if (isScrolling() && button == 0)//we were scrolling and we released mouse
            scrollclicky = -1;
        else if (w.contains(mx, my))
            slotUp(mx - w.x, my - w.y + scrolledPixels(), button);
    }

    @Override
    public void mouseDragged(int mousex, int mousey, int button, long time) {
        if (isScrolling()) {
            int scrolldiff = mousey - scrollclicky;
            int sbarh = scrollbarDim().height;
            int barupallowed = (int) ((height - sbarh) * scrollpercent + 0.5);
            int bardownallowed = (height - sbarh) - barupallowed;

            if (-scrolldiff > barupallowed)
                scrollmousey = scrollclicky - barupallowed;
            else if (scrolldiff > bardownallowed)
                scrollmousey = scrollclicky + bardownallowed;
            else
                scrollmousey = mousey;

            calculatePercentScrolled();
        }
    }

    public int scrolledPixels() {
        int scrolled = (int) ((contentHeight() - windowBounds().height) * percentscrolled + 0.5);
        if (scrolled < 0) scrolled = 0;
        return scrolled;
    }

    public Rectangle4i bounds() {
        return new Rectangle4i(x, y, width, height);
    }

    public boolean contains(int mx, int my) {
        return bounds().contains(mx, my);
    }

    @Override
    public void draw(int mx, int my, float frame) {
        Rectangle w = windowBounds();
        drawBackground(frame);
        drawContent(mx-w.x, my+scrolledPixels()-w.y, frame);
        drawOverlay(frame);
        drawScrollbar(frame);
    }

    public void drawBackground(float frame) {
        drawRect(x, y, x + width, y + height, 0xff000000);
    }

    public abstract void drawContent(int mx, int my, float frame);

    public void drawOverlay(float frame) {
        //outlines
        drawRect(x, y - 1, x + width, y, 0xffa0a0a0);//top
        drawRect(x, y + height, x + width, y + height + 1, 0xffa0a0a0);//bottom
        drawRect(x - 1, y - 1, x, y + height + 1, 0xffa0a0a0);//left
        drawRect(x + width, y - 1, x + width + 1, y + height + 1, 0xffa0a0a0);//right
    }

    public void drawScrollbar(float frame) {
        Rectangle r = scrollbarBounds();

        drawRect(r.x, r.y, r.x + r.width, r.y + r.height, 0xFF8B8B8B);//corners
        drawRect(r.x, r.y, r.x + r.width - 1, r.y + r.height - 1, 0xFFF0F0F0);//topleft up
        drawRect(r.x + 1, r.y + 1, r.x + r.width, r.y + r.height, 0xFF555555);//bottom right down
        drawRect(r.x + 1, r.y + 1, r.x + r.width - 1, r.y + r.height - 1, 0xFFC6C6C6);//scrollbar

        int algn = scrollbarGuideAlignment();
        if (algn != 0)
            drawRect(algn > 0 ? r.x + r.width : r.x - 1, y, r.x, y + height, 0xFF808080);//lineguide
    }

}
