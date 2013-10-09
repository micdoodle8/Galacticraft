package codechicken.nei;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.nbt.NBTTagCompound;
import codechicken.lib.config.ConfigFile;

import static codechicken.core.gui.GuiDraw.*;

/**
 * The main data storage and manipulation class of the {@link DropDownWidget}
 * Manages user stored and runtime generated {@link MultiItemRange}s
 * Extends {@link ConfigFile} to make use of the CodeChicken advanced Configuration system.
 * Look at the static calls near the bottom for reference.
 *
 */
public class DropDownFile extends ConfigFile
{
    public DropDownFile(File file)
    {
        super(file);
    }    

    @Override
    public SubSetRangeTag getTag(String tagname)
    {
        return (SubSetRangeTag)super.getTag(tagname);
    }

    @Override
    public SubSetRangeTag getTag(String tagname, boolean createnew)
    {
        return (SubSetRangeTag) super.getTag(tagname, createnew);
    }

    @Override
    public SubSetRangeTag getNewTag(String name)
    {
        return new SubSetRangeTag(this, name);
    }

    @Override
    public void saveConfig()
    {
        super.saveConfig();
    }
    
    public boolean thisContains(int mousex, int mousey)
    {
        return mousex >= x && mousex < x + width &&
                mousey >= y && mousey <= y + height;
    }

    public boolean contains(int mousex, int mousey)
    {
        if(thisContains(mousex, mousey))
            return true;
        
        for(SubSetRangeTag tag : sortedtags)
            if(tag.contains(mousex, mousey))
                return true;
        return false;
    }

    public int getScrollBarWidth()
    {
        return 5;
    }

    public int getScrollBarHeight()
    {
        int sbarh = (int)((height / (float)contentheight) * height);
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

    public int getScrolledSlots()
    {
        int slots = childTagMap().size();
        int shownslots = height / slotheight;
        return (int) (percentscrolled * (slots - shownslots) + 0.5F);
    }

    private int getClickedSlot(int mousey)
    {
        return ((mousey - y) / slotheight) + getScrolledSlots();
    }
    
    public void calculatePercentScrolled()
    {
        int barempty = height - getScrollBarHeight();
        if(scrollclicky >= 0)
        {
            int scrolldiff = scrollmousey - scrollclicky;
            percentscrolled = scrolldiff / (float)barempty + scrollpercent;
        }
        if(percentscrolled < 0)
        {
            percentscrolled = 0;
        }
        if(percentscrolled > 1)
        {
            percentscrolled = 1;
        }
        int sbary = y + (int)(barempty * percentscrolled + 0.5);
        percentscrolled = (sbary - y) / (float) barempty;
    }

    public void processScrollMouse(int mousex, int mousey)
    {
        if(scrollclicky >= 0)
        {
            int scrolldiff = mousey - scrollclicky;
            int barupallowed = (int)((height - getScrollBarHeight()) * scrollpercent + 0.5);
            int bardownallowed = (height - getScrollBarHeight()) - barupallowed;
            
            if(-scrolldiff > barupallowed)
            {
                scrollmousey = scrollclicky - barupallowed;
            }
            else if(scrolldiff > bardownallowed)
            {
                scrollmousey = scrollclicky + bardownallowed;
            }
            else
            {
                scrollmousey = mousey;
            }
            
            calculatePercentScrolled();
        }
    }

    public String updateMouseOver(int mousex, int mousey, String mouseovername)
    {
        processScrollMouse(mousex, mousey);
        
        String currentmouseover = "";
    
        int sloty = y;
        int slotx = x + (hasscroll ? getScrollBarWidth() : 0);
        int slot = 0;    
        for(SubSetRangeTag tag : sortedtags)
        {
            slot++;
            if(slot <= getScrolledSlots())
                continue;
            
            if(slotx <= mousex && x+width > mousex && sloty <= mousey && sloty+slotheight > mousey)
                currentmouseover = tag.qualifiedname;
            
            String s = tag.updateMouseOver(mousex, mousey, mouseovername);
            if(!s.equals(""))
                currentmouseover = s;
            
            sloty+=slotheight;
        }
        return currentmouseover;
    }

    public boolean click(int mousex, int mousey, int button)
    {
        boolean inbounds = thisContains(mousex, mousey);
        if(!inbounds)
            for(SubSetRangeTag tag : sortedtags)
                tag.click(mousex, mousey, button);
        
        int barempty = height - getScrollBarHeight();
        int sbary = y + (int)(barempty * percentscrolled + 0.5);
        int contentx = x + getScrollBarWidth();
        
        if(hasscroll && button == 0 &&
                getScrollBarHeight() < height && //the scroll bar can move (not full length)
                mousex >= x && mousex <= x + getScrollBarWidth() &&
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
        else if(mousex >= contentx && mousex < x + width &&
                mousey >= y && mousey <= y + height)//in the box
        {
            int slot = getClickedSlot(mousey);
            if(slot == lastslotclicked && System.currentTimeMillis() - lastslotclicktime < 500 && button == 0)
            {
                slotClicked(slot, button, true);
            }
            else
            {
                slotClicked(slot, button, false);
            }
            
            if(button == 0)
            {
                lastslotclicked = slot;
                lastslotclicktime = System.currentTimeMillis();
            }
        }
        return true;
    }

    public void onMouseWheel(int i)
    {
        if(scrollclicky != -1)
        {
            return;
        }
        for(SubSetRangeTag tag : sortedtags)
        {
            if(tag.expanded)
            {
                tag.onMouseWheel(i);
                return;
            }
        }
        scrollpercent+=i/(float)contentheight * 10;
        if(scrollpercent > 1)
        {
            scrollpercent = 1;
        }
        else if(scrollpercent < 0)
        {
            scrollpercent = 0;
        }
    }
    
    private void slotClicked(int slot, int button, boolean doubleclick)
    {
        int i = 0;
        for(SubSetRangeTag tag : sortedtags)
        {
            if(slot == i)
            {
                tag.onClick(button, doubleclick);
                return;
            }
            i++;
        }
    }
    
    public void hideAllItems()
    {
        for(SubSetRangeTag tag : sortedtags)
            tag.hideAllItems();
    }
    
    public void showAllItems()
    {
        for(SubSetRangeTag tag : sortedtags)
            tag.showAllItems();
    }
    
    public void mouseUp(int mousex, int mousey, int button)
    {
        if(scrollclicky >= 0 && button == 0)//we were scrolling and we released mouse
        {
            scrollclicky = -1;
        }
        for(SubSetRangeTag tag : sortedtags)
            tag.mouseUp(mousex, mousey, button);
    }

    public void draw(int mousex, int mousey)
    {
        drawScrollBar();        
        
        int drawy = y;
        int drawx = x + (hasscroll ? getScrollBarWidth() : 0);
        int slot = 0;
        for(SubSetRangeTag tag : sortedtags)
        {
            slot++;
            if(slot <= getScrolledSlots())
            {
                continue;
            }
            
            if(!hidden)
            {
                if(!DropDownWidget.texturedButtons)
                {
                    boolean contains = mousex >= drawx && mousex < drawx + slotwidth &&
                            mousey >= drawy && mousey < drawy + slotheight;
                    drawRect(drawx, drawy, slotwidth, slotheight, contains ? 0xFF401008 : 0xFF000000);
                    drawStringC(tag.name, drawx, drawy, slotwidth, slotheight, tag.getColourFromState(), tag.state == 0);
                }
                else
                {
                    changeTexture("textures/gui/widgets.png");
                    if(tag.state == 1)
                        GL11.glColor4f(0.65F, 0.65F, 0.65F, 1.0F);
                    else
                        GL11.glColor4f(1, 1, 1, 1);
                    int tex = tag.state == 0 ? 0 : 1;
                    LayoutManager.drawButtonBackground(drawx, drawy, slotwidth, slotheight, false, tex);
                    
                    int textcolour = tag.state == 2 ? 0xFFE0E0E0 : 0xFFA0A0A0;
                    drawStringC(tag.name, drawx, drawy, slotwidth, slotheight, textcolour);
                }
            }
            
            tag.draw(mousex, mousey);
            
            drawy+=slotheight;
            if(drawy >= y + height)
            {
                break;
            }
        }
    }

    private void drawScrollBar()
    {
        if(hasscroll && !hidden)
        {
            int sbary = y + (int)((height - getScrollBarHeight()) * percentscrolled + 0.5);

            drawRect(x, y, 5, height, 0xFF202020);//background
            if(DropDownWidget.texturedButtons)
            {
                drawRect(x, sbary, 5, getScrollBarHeight(), 0xFF8B8B8B);//corners
                drawRect(x, sbary, 4, getScrollBarHeight() - 1, 0xFFF0F0F0);//topleft up
                drawRect(x + 1, sbary + 1, 4, getScrollBarHeight() - 1, 0xFF555555);//bottom right down
                drawRect(x + 1, sbary + 1, 3, getScrollBarHeight() - 2, 0xFFC6C6C6);//scrollbar
            }
            else
            {
                drawRect(x, sbary, 5, getScrollBarHeight(), 0xFFE0E0E0);
            }
        }
    }

    public void position(int px, int py)
    {
        this.x = px;
        this.y = py;
        recalcSize();
        
        int suby = y;
        int subx = x + width;
        int slot = 0;
        for(SubSetRangeTag tag : sortedtags)
        {
            slot++;
            if(slot <= getScrolledSlots())
            {
                continue;
            }
            
            tag.position(subx, suby);
            
            suby+=slotheight;
            if(suby >= y + height)
            {
                break;
            }
        }
    }
    
    public void recalcSize()
    {
        int maxheight = NEIClientUtils.getGuiContainer().height - y;
        contentheight = childTagMap().size() * slotheight;
        if(contentheight > maxheight)
        {
            height = (maxheight / slotheight)*slotheight;
            hasscroll = true;
        }
        else
            
        {
            hasscroll = false;
            height = contentheight;
        }
        
        slotwidth = 0;
        FontRenderer fontRenderer = NEIClientUtils.mc().fontRenderer;
        for(SubSetRangeTag tag : sortedtags)
        {
            int tagwidth = fontRenderer.getStringWidth(tag.name);
            if(tagwidth > slotwidth)
            {
                slotwidth = tagwidth;
            }
        }
        
        slotwidth +=2;
        width = slotwidth;
        if(hasscroll)
        {
            width+=5;
        }
        
        int totalwidth = x + width - LayoutManager.dropDown.x;
        
        hidden = totalwidth <= 0;
    }
    
    public void resetHashes()
    {
        sortedtags = getSortedTagList();
        for(SubSetRangeTag tag : sortedtags)
            tag.resetHashes();
    }
    
    public void updateState()
    {
        ItemVisibilityHash vis = NEIClientConfig.vishash;
        for(SubSetRangeTag tag : sortedtags)
            tag.updateState(vis);
    }
    
    public void addItemIfInRange(int item, int damage, NBTTagCompound compound)
    {
        for(SubSetRangeTag tag : sortedtags)
            tag.addItemIfInRange(item, damage, compound);
    }
    
    public int getWidthAtLevel(int hiddenlevel)
    {
        if(hiddenlevel == 0)
            return width;
        
        for(SubSetRangeTag tag : sortedtags)
        {
            int subwidth = tag.getWidthAtLevel(hiddenlevel-1);
            if(subwidth != 0)
                return subwidth;
        }
        return 0;
    }
    
    public Iterable<SubSetRangeTag> allTags()
    {
        LinkedList<SubSetRangeTag> tags = new LinkedList<SubSetRangeTag>();
        for(SubSetRangeTag tag : sortedtags)
            tag.addChildTags(tags);
        return tags;
    }

    public ArrayList<SubSetRangeTag> sortedtags = new ArrayList<SubSetRangeTag>();
    
    public int slotwidth = 0;
    public boolean hasscroll;
    
    protected int scrollclicky = -1;
    protected float scrollpercent;
    protected int scrollmousey;
    protected float percentscrolled;
    
    protected int lastslotclicked = -1;
    protected long lastslotclicktime;
    
    int x;
    int y;
    int height;
    int width;
    int contentheight;
    public boolean hidden;
    
    public static final int slotheight = 18;
    public static DropDownFile dropDownInstance;
    static
    {
        dropDownInstance = new DropDownFile(new File(Minecraft.getMinecraft().mcDataDir, "config/NEISubset.cfg"));
        dropDownInstance.setComment("You can put your own custom SubSet Ranges in here\n"+
                "Follow the following format (replace {something} with what you want.\n"+
                "{Parent}.{Name}=[{item1}],[{item2}],[{item3}-{item4}],[{item5}:{damage}],[{item6}:{damage1}-{damage2}]\n"+
                "Eg. Blocks.Nether = [87-89],[112-115]\n"+
                "Eg2. Birch = [17:2],[6:2]");
    }
}
