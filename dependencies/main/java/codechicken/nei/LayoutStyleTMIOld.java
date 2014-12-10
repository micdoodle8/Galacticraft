package codechicken.nei;

import codechicken.nei.guihook.GuiContainerManager;

import static codechicken.lib.gui.GuiDraw.drawRect;
import static codechicken.lib.gui.GuiDraw.drawString;
import static codechicken.lib.gui.GuiDraw.drawStringC;
import static codechicken.nei.LayoutManager.*;

public class LayoutStyleTMIOld extends LayoutStyleDefault
{
    public static final Image stateOff = new Image(48, 0, 8, 12);
    public static final Image stateOn = new Image(56, 0, 8, 12);
    public static final Image stateDisabled = new Image(64, 0, 8, 12);
    
    int stateButtonCount;
    int clickButtonCount;

    @Override
    public String getName()
    {
        return "oldtmi";
    }
    
    @Override
    public void init()
    {
        delete.icon = new Image(24, 12, 12, 12);        
        gamemode.icons[0] = new Image(12, 12, 12, 12);
        gamemode.icons[1] = new Image(36, 12, 12, 12);
        gamemode.icons[2] = new Image(48, 12, 12, 12);
        rain.icon = new Image(0, 12, 12, 12);        
        magnet.icon = new Image(60, 24, 12, 12);        
        timeButtons[0].icon = new Image(12, 24, 12, 12);
        timeButtons[1].icon = new Image(0, 24, 12, 12);
        timeButtons[2].icon = new Image(24, 24, 12, 12);
        timeButtons[3].icon = new Image(36, 24, 12, 12);        
        heal.icon = new Image(48, 24, 12, 12);
        dropDown.x = 93;
    }
    
    @Override
    public void reset()
    {
        stateButtonCount = clickButtonCount = 0;
    }

    @Override
    public void layoutButton(Button button)
    {
        int offsetx = 2;
        int offsety = 2;
        
        if((button.state & 0x4) != 0)
        {
            button.x = offsetx + stateButtonCount*22;
            button.y = offsety;
            stateButtonCount++;
        }
        else
        {
            button.x = offsetx + (clickButtonCount%4)*22;
            button.y = offsety + (1+clickButtonCount/4)*17;
            clickButtonCount++;
        }
        
        button.h = 14;
        button.w = button.contentWidth() + 2;
        if((button.state & 0x4) != 0)
            button.w += stateOff.width;
    }
    
    @Override
    public void drawBackground(GuiContainerManager gui)
    {
        if(clickButtonCount == 0 && stateButtonCount == 0)
            return;
        
        int maxx = Math.max(stateButtonCount, clickButtonCount);
        if(maxx > 4)maxx = 4;
        int maxy = clickButtonCount == 0 ? 1 : (clickButtonCount / 4 + 2);
        
        drawRect(0, 0, 2+22*maxx, 1+maxy*17, 0xFF000000);
    }
    
    @Override
    public void drawButton(Button b, int mousex, int mousey)
    {
        int cwidth = b.contentWidth();
        if((b.state & 0x4) != 0)
            cwidth += stateOff.width;
        int textx = b.x + (b.w - cwidth) / 2;
        int texty = b.y + (b.h - 8) / 2;
        
        drawRect(b.x, b.y, b.w, b.h, b.contains(mousex, mousey) ? 0xee401008 : 0xee000000);
        
        Image icon = b.getRenderIcon();
        if(icon == null)
        {
            drawString(b.getRenderLabel(), textx, texty, -1);
        }
        else
        {
            int icony = b.y + (b.h - icon.height) / 2;
            LayoutManager.drawIcon(textx, icony, icon);
            if((b.state & 0x3) == 2)
                drawRect(textx, icony, icon.width, icon.height, 0x80000000);
        
            if((b.state & 0x4) != 0)
            {
                Image stateimage;
                if((b.state & 0x3) == 1)
                    stateimage = stateOn;
                else if((b.state & 0x3) == 2)
                    stateimage = stateDisabled;
                else
                    stateimage = stateOff;
                LayoutManager.drawIcon(textx+icon.width, icony, stateimage);
            }
        }
    }

    @Override
    public void drawSubsetTag(String text, int x, int y, int w, int h, int state, boolean mouseover) {
        drawRect(x, y, w, h, mouseover ? 0xFF401008 : 0xFF000000);
        if(text != null) {
            int colour = -1;
            if (state == 0)
                colour = 0xFF601010;
            else if (state == 1)
                colour = 0xFF807070;
            drawStringC(text, x, y, w, h, colour, state == 0);
        }
    }
}
