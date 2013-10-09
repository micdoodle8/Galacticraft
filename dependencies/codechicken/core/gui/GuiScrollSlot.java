package codechicken.core.gui;

import net.minecraft.client.gui.GuiButton;

import org.lwjgl.input.Keyboard;

import codechicken.lib.math.MathHelper;

public abstract class GuiScrollSlot extends GuiWidget
{    
    protected GuiButton scrollupbutton;
    protected GuiButton scrolldownbutton;
    protected String actionCommand;
    
    protected int scrollclicky = -1;
    protected float scrollpercent;
    protected int scrollmousey;
    protected float percentscrolled;

    protected int lastslotclicked = -1;
    protected int lastbuttonclicked = -1;
    protected long lastslotclicktime;
    
    public boolean focused;
    
    public int contentx;
    public int contenty;
    public int contentheight;
    public boolean smoothScroll = true;
    
    public GuiScrollSlot(int x, int y, int width, int height)
    {
        super(x, y, width, height);
        setContentSize(x+3, y+2, height-2);
    }
    
    public GuiScrollSlot setActionCommand(String s)
    {
        actionCommand = s;
        return this;
    }
    
    public void setSmoothScroll(boolean b)
    {
        smoothScroll = b;
    }
    
    public void setSize(int x, int y, int width, int height)
    {
        int cx = contentx-this.x;
        int cy = contenty-this.y;
        int ch = height-contentheight;
        super.setSize(x, y, width, height);
        
        setContentSize(x+cx, y+cy, height-ch);
    }
    
    public void setContentSize(int x, int y, int h)
    {
        this.contentx = x;
        this.contenty = y;
        this.contentheight = h;
    }
    
    public void registerButtons(GuiButton up, GuiButton down, String selectionCommand)
    {
        scrollupbutton = up;
        scrolldownbutton = down;
        actionCommand = selectionCommand;
    }

    public abstract int getSlotHeight();

    protected abstract int getNumSlots();

    public abstract void selectNext();
    
    public abstract void selectPrev();

    protected abstract void slotClicked(int slot, int button, int mousex, int mousey, boolean doubleclick);

    protected abstract boolean isSlotSelected(int slot);

    protected abstract void drawSlot(int slot, int x, int y, int mousex, int mousey, boolean selected, float frame);

    protected void unfocus()
    {
    }
    
    public void setFocused(boolean focus)
    {
        focused = focus;
        if(!focused)
            unfocus();
    }
    
    public void scrollUp()
    {
        scroll(-5);
    }
    
    public void scrollDown()
    {
        scroll(5);
    }
    
    public void scroll(int i)
    {
        percentscrolled += i/(float)contentheight * 100;
        calculatePercentScrolled();
    }
    
    public int totalContentHeight()
    {
        return getNumSlots() * getSlotHeight();
    }
    
    public int getSlotY(int slot)
    {
        int scrolledPixels = (int)((totalContentHeight() - contentheight) * percentscrolled + 0.5);
        if(!smoothScroll)
            scrolledPixels = (int)(scrolledPixels/(double)getSlotHeight()+0.5)*getSlotHeight();
        return contenty-scrolledPixels+slot*getSlotHeight();
    }

    public int getClickedSlot(int mousey)
    {
        if(mousey < contenty || mousey >= contenty+contentheight)
            return -1;
        
        for(int slot = 0; slot < getNumSlots(); slot++)
        {
            int sloty = getSlotY(slot);
            if(mousey >= sloty && mousey < sloty + getSlotHeight())
                return slot;
        }
        return -1;
    }
    
    public int getScrollBarWidth()
    {
        return 5;
    }
    
    public int getScrollBarHeight()
    {
        int sbarh = (int)((contentheight  / (float)totalContentHeight()) * height);
        if(sbarh > height)
        {
            return height;
        }
        else if(sbarh < height / 15)
        {
            return height / 15;
        }
        else
        {
            return sbarh;
        }
    }
    
    public void calculatePercentScrolled()
    {
        int barempty = height - getScrollBarHeight();
        if(scrollclicky >= 0)
        {
            int scrolldiff = scrollmousey - scrollclicky;
            percentscrolled = scrolldiff / (float)barempty + scrollpercent;
        }
        percentscrolled = (float) MathHelper.clip(percentscrolled, 0, 1);
    }
    
    public void showSlot(int slot)
    {
        int sloty = getSlotY(slot);
        if(sloty+getSlotHeight() > contenty+contentheight)
        {
            int diff = sloty-(contenty+contentheight-getSlotHeight());
            percentscrolled += diff/(double)(totalContentHeight() - contentheight);
            calculatePercentScrolled();
        }
        else if(sloty < contenty)
        {
            int diff = contenty-sloty;
            percentscrolled -= diff/(double)(totalContentHeight() - contentheight);
            calculatePercentScrolled();
        }
    }

    public void processMouse(int mousex, int mousey)
    {
        if(scrollclicky >= 0)
        {
            int scrolldiff = mousey - scrollclicky;
            int barupallowed = (int)((height - getScrollBarHeight()) * scrollpercent + 0.5);
            int bardownallowed = (height - getScrollBarHeight()) - barupallowed;
            
            if(-scrolldiff > barupallowed)
                scrollmousey = scrollclicky - barupallowed;
            else if(scrolldiff > bardownallowed)
                scrollmousey = scrollclicky + bardownallowed;
            else
                scrollmousey = mousey;
            
            calculatePercentScrolled();
        }
    }
    
    public void actionPerformed(GuiButton guibutton)
    {
        if(!guibutton.enabled)
        {
            return;
        }
        if(scrollupbutton != null && guibutton.id == scrollupbutton.id)
        {
            scrollUp();
        }
        else if(scrolldownbutton != null && guibutton.id == scrolldownbutton.id)
        {
            scrollDown();
        }
    }

    @Override
    public void mouseClicked(int mousex, int mousey, int button)
    {
        boolean needsfocus = (mousex >= x && mousex < x + width &&
                mousey >= y && mousey <= y + height);
        if(needsfocus != focused)
        {
            setFocused(needsfocus);
        }
        
        int barempty = height - getScrollBarHeight();
        int sbarx = x + width - getScrollBarWidth();
        int sbary = y + (int)(barempty * percentscrolled + 0.5);
        
        if(button == 0 &&
                getScrollBarHeight() < height && //the scroll bar can move (not full length)
                mousex >= sbarx && mousex <= x + width &&
                mousey >= y && mousey <= y + height)//in the scroll pane
        {
            if(mousey < sbary)
            {
                percentscrolled = (mousey - y) / (float)barempty;
                calculatePercentScrolled();
            }
            else if(mousey > sbary + getScrollBarHeight())
            {
                percentscrolled = (mousey - y - getScrollBarHeight() + 1) / (float)barempty;
                calculatePercentScrolled();
            }
            else
            {
                scrollclicky = mousey;
                scrollpercent = percentscrolled;
                scrollmousey = mousey;
            }
        }
        else if(mousex >= contentx && mousex < sbarx &&
                mousey >= contenty && mousey <= contenty + contentheight)//in the box
        {
            int slot = getClickedSlot(mousey);
            if(slot >= 0)
                slotClicked(slot, button, mousex-contentx, mousey-getSlotY(slot), 
                    slot == lastslotclicked && button == lastbuttonclicked && System.currentTimeMillis() - lastslotclicktime < 500);
            
            lastslotclicked = slot;
            lastbuttonclicked = button;
            lastslotclicktime = System.currentTimeMillis();
        }
    }

    @Override
    public void mouseMovedOrUp(int mousex, int mousey, int button)
    {
        if(scrollclicky >= 0 && button == 0)//we were scrolling and we released mouse
            scrollclicky = -1;
    }

    @Override
    public void keyTyped(char c, int keycode)
    {
        if(!focused)
            return;
        
        if(keycode == Keyboard.KEY_UP)
            selectPrev();
        if(keycode == Keyboard.KEY_DOWN)
            selectNext();
        if(keycode == Keyboard.KEY_RETURN && actionCommand != null)
            sendAction(actionCommand);
    }
    
    public void drawSlotBox(float frame)
    {
        drawRect(x, y, x + width, y + height, 0xff000000);
    }
    
    public void drawBackground(float frame)
    {
        
    }
    
    public void drawOverlay(float frame)
    {
        //outlines
        drawRect(x, y - 1, x + width, y, 0xffa0a0a0);//top
        drawRect(x, y + height, x + width, y + height + 1, 0xffa0a0a0);//bottom
        drawRect(x - 1, y - 1, x, y + height + 1, 0xffa0a0a0);//left
        drawRect(x + width, y - 1, x + width + 1, y + height + 1, 0xffa0a0a0);//right
    }
    
    public void drawScrollBar(float frame)
    {
        int sbarw = getScrollBarWidth();
        int sbarh = getScrollBarHeight();
        int sbarx = x + width - sbarw;
        int sbary = y + (int)((height - sbarh) * percentscrolled + 0.4999);
        
        drawRect(sbarx, sbary, sbarx+sbarw, sbary+sbarh, 0xFF8B8B8B);//corners
        drawRect(sbarx, sbary, sbarx+sbarw-1, sbary+sbarh - 1, 0xFFF0F0F0);//topleft up
        drawRect(sbarx + 1, sbary + 1, sbarx+sbarw, sbary+sbarh, 0xFF555555);//bottom right down
        drawRect(sbarx + 1, sbary + 1, sbarx+sbarw-1, sbary+sbarh - 1, 0xFFC6C6C6);//scrollbar
        
        if(drawLineGuide())
            drawRect(sbarx - 1, y, sbarx, y + height, 0xFF808080);//lineguide
    }
    
    public boolean drawLineGuide()
    {
        return true;
    }
    
    public void drawSlots(int mousex, int mousey, float frame)
    {
        for(int slot = 0; slot < getNumSlots(); slot++)
        {
            int sloty = getSlotY(slot);
            if(sloty > contenty - getSlotHeight() && sloty < contenty + contentheight)
                drawSlot(slot, contentx, sloty, mousex-contentx, mousey-sloty, isSlotSelected(slot), frame);
        }
    }
    
    @Override
    public void mouseDragged(int x, int y, int button, long time)
    {
        processMouse(x, y);
    }
    
    @Override
    public void draw(int mousex, int mousey, float frame)
    {
        drawBackground(frame);
        drawSlotBox(frame);
        drawSlots(mousex, mousey, frame);
        drawOverlay(frame);
        drawScrollBar(frame);
    }
}
