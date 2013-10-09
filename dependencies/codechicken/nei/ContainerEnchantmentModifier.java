package codechicken.nei;

import java.util.ArrayList;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.inventory.Slot;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import codechicken.lib.render.CCRenderState;

/**
 * This is crap code, don't ever do this.
 */
public class ContainerEnchantmentModifier extends ContainerEnchantment
{
    public static class EnchantmentHash
    {
        public EnchantmentHash(Enchantment e, int i, int l)
        {
            enchantment = e;
            state = i;
            level = l;
        }
        
        Enchantment enchantment;
        int state;
        int level;
    }
    
    public ArrayList<EnchantmentHash> slotEnchantment = new ArrayList<EnchantmentHash>();
    int level = 5;
    
    public int scrollclicky = -1;
    public float scrollpercent;
    public int scrollmousey;
    public float percentscrolled;
    public int relx = 60; 
    public int rely = 14;
    public int height = 57;
    public int cwidth = 101;
    public int slotheight = 19;
    public GuiEnchantmentModifier parentscreen;
    
    public ContainerEnchantmentModifier(InventoryPlayer inventoryplayer, World world, int i, int j, int k)
    {
        super(inventoryplayer, world, i, j, k);
    }
    
    public int getNumSlots()
    {
        return slotEnchantment.size();
    }
    
    public int getScrollBarHeight()
    {
        int sbarh = (int)((height / (float)getContentHeight()) * height);
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
    
    public int getScrollBarWidth()
    {
        return 7;
    }
    
    public int getContentHeight()
    {
        return slotheight * getNumSlots();
    }
    
    public int getScrolledSlots()
    {
        int slots = getNumSlots();
        int shownslots = height / slotheight;
        return (int) (percentscrolled * (slots - shownslots) + 0.5F);
    }
    
    private int getClickedSlot(int mousey)
    {
        return ((mousey - rely) / slotheight) + getScrolledSlots();
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
        int sbary = rely + (int)(barempty * percentscrolled + 0.5);
        percentscrolled = (sbary - rely) / (float) barempty;
    }
    
    public boolean clickScrollBar(int mousex, int mousey, int button)
    {
        mousex -= parentscreen.guiLeft;
        mousey -= parentscreen.guiTop;
        
        int barempty = height - getScrollBarHeight();
        int sbary = rely + (int)(barempty * percentscrolled + 0.5);
        
        if(button == 0 &&
                getScrollBarHeight() < height && //the scroll bar can move (not full length)
                mousex >= relx + cwidth && mousex < relx + cwidth + getScrollBarWidth() &&
                mousey >= rely && mousey < rely + height)//in the scroll pane
        {
            if(mousey < sbary)
            {
                percentscrolled = (mousey - rely) / (float)barempty;
                calculatePercentScrolled();
            }
            else if(mousey > sbary + getScrollBarHeight())
            {
                percentscrolled = (mousey - rely - getScrollBarHeight() + 1) / (float)barempty;
                calculatePercentScrolled();
            }
            else
            {
                scrollclicky = mousey;
                scrollpercent = percentscrolled;
                scrollmousey = mousey;
            }
            return true;
        }
        return false;
    }
    
    public void mouseUp(int mousex, int mousey, int button)
    {
        if(scrollclicky >= 0 && button == 0)//we were scrolling and we released mouse
        {
            scrollclicky = -1;
        }
    }
    
    public boolean clickButton(int mousex, int mousey, int button)
    {
        mousex -= parentscreen.guiLeft;
        mousey -= parentscreen.guiTop;
        if(mousex >= relx && mousex < relx + cwidth &&
                mousey >= rely && mousey <= rely + height)//in the box
        {
            int slot = getClickedSlot(mousey);
            if(slot >= getNumSlots())return false;
            toggleSlotEnchantment(slot);
            return true;
        }
        return false;
    }
    
    private void toggleSlotEnchantment(int slot)
    {
        EnchantmentHash e = slotEnchantment.get(slot);
        if(e.state == 2)
        {
            NEICPH.sendModifyEnchantment(e.enchantment.effectId, 0, false);
            e.state = 0;
        }
        else if(e.state == 1)
        {
            return;
        }
        else
        {
            NEICPH.sendModifyEnchantment(e.enchantment.effectId, level, true);
            e.state = 2;
        }
        updateEnchantmentOptions(GuiEnchantmentModifier.validateEnchantments());
    }
    
    public boolean addEnchantment(int e, int level)
    {
        if(e < Enchantment.enchantmentsList.length && Enchantment.enchantmentsList[e] != null)
        {
            ((Slot)inventorySlots.get(0)).getStack().addEnchantment(Enchantment.enchantmentsList[e], level);
            return true;
        }
        return false;
    }
    
    public void removeEnchantment(int e)
    {
        ItemStack stack = ((Slot)inventorySlots.get(0)).getStack();
        NBTTagList nbttaglist = stack.getEnchantmentTagList();
        if(nbttaglist != null)
        {
            for(int i = 0; i < nbttaglist.tagCount(); i++)
            {
                int ID = ((NBTTagCompound)nbttaglist.tagAt(i)).getShort("id");
                if(ID == e)
                {
                    nbttaglist.removeTag(i);
                    if(nbttaglist.tagCount() == 0)
                        stack.getTagCompound().removeTag("ench");
                    if(stack.getTagCompound().hasNoTags())
                        stack.setTagCompound(null);
                    return;
                }
            }
        }
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }
    
    public void onCraftMatrixChanged(IInventory iinventory)
    {
        if(parentscreen != null)
            updateEnchantmentOptions(GuiEnchantmentModifier.validateEnchantments());
    }

    public void updateEnchantmentOptions(boolean validate)
    {
        int numoptions = slotEnchantment.size();
        slotEnchantment.clear();
        
        ItemStack toolstack = getSlot(0).getStack();
        if(toolstack == null)
        {
            percentscrolled = 0;
            return;
        }
        
        Item item = toolstack.getItem();
        int enchantablity = item.getItemEnchantability();
        if(enchantablity == 0 && validate)
        {
            percentscrolled = 0;
            return;
        }
        
        for(Enchantment e : Enchantment.enchantmentsList)
        {
            if(e == null || e.type == null || (!e.type.canEnchantItem(item) && validate))
            {
                continue;
            }
            int state = 0;
            int level = -1;
            if(NEIServerUtils.stackHasEnchantment(toolstack, e.effectId))
            {
                state = 2;
                level = NEIServerUtils.getEnchantmentLevel(toolstack, e.effectId);
            }
            else if(NEIServerUtils.doesEnchantmentConflict(NEIServerUtils.getEnchantments(toolstack), e) && validate)
            {
                state = 1;
            }
            slotEnchantment.add(new EnchantmentHash(e, state, level));
        }
        if(numoptions != slotEnchantment.size())
        {
            percentscrolled = 0;
        }                
    }

    public void drawSlots(GuiEnchantmentModifier gui)
    {
        for(int slot = 0; slot < 3; slot++)
        {
            int shade = 0;
            String text = "";
            
            int containerslot = slot+getScrolledSlots();
            if(containerslot+1 > slotEnchantment.size())
            {
                shade = 1;
            }
            else
            {
                EnchantmentHash e = slotEnchantment.get(containerslot);
                shade = e.state;
                text = e.enchantment.getTranslatedName(e.level == -1 ? level : e.level);
                if(gui.mc.fontRenderer.getStringWidth(text) > 95 && text.contains("Projectile"))
                {
                    text = text.replace("Projectile", "Proj");
                }
                if(gui.mc.fontRenderer.getStringWidth(text) > 95 && text.contains("Protection"))
                {
                    text = text.replace("Protection", "Protect");
                }
                if(gui.mc.fontRenderer.getStringWidth(text) > 95 && text.contains("Bane of"))
                {
                    text = text.replace("Bane of ", "");
                }
            }

            CCRenderState.changeTexture("textures/gui/container/enchanting_table.png");
            GL11.glColor3f(1, 1, 1);
            if(hasScrollBar())
            {
                gui.drawTexturedModalRect(relx, rely+slot*slotheight, 0, gui.ySize+slotheight*shade, cwidth-30, slotheight);
                gui.drawTexturedModalRect(relx+cwidth-30, rely+slot*slotheight, cwidth-23, gui.ySize+slotheight*shade, 30, slotheight);
            }
            else
            {
                gui.drawTexturedModalRect(relx, rely+slot*slotheight, 0, gui.ySize+slotheight*shade, cwidth+7, slotheight);
            }
            
            gui.fontRenderer.drawString(text, relx + 4, rely+slot*slotheight+5, textColourFromState(shade));
        }
    }

    private boolean hasScrollBar()
    {
        return getNumSlots() > 3;
    }

    public void drawScrollBar(GuiEnchantmentModifier gui)
    {
        if(!hasScrollBar())return;
        
        int sbary = rely + (int)((height - getScrollBarHeight()) * percentscrolled + 0.5);
        int sbarx = relx + cwidth;
        
        Gui.drawRect(sbarx, rely, sbarx+getScrollBarWidth(), rely+height, 0xFF202020);//background
        Gui.drawRect(sbarx, sbary, sbarx+getScrollBarWidth(), sbary+getScrollBarHeight(), 0xFF8B8B8B);//corners
        Gui.drawRect(sbarx, sbary, sbarx+getScrollBarWidth()-1, sbary+getScrollBarHeight() - 1, 0xFFF0F0F0);//topleft up
        Gui.drawRect(sbarx + 1, sbary + 1, sbarx+getScrollBarWidth()-1, sbary+getScrollBarHeight() - 1, 0xFF555555);//bottom right down
        Gui.drawRect(sbarx + 1, sbary + 1, sbarx+getScrollBarWidth()-2, sbary+getScrollBarHeight() - 2, 0xFFC6C6C6);//scrollbar
    }
    
    private int textColourFromState(int shade)
    {
        if(shade == 0)
        {
            return 0x685e4a;
        }
        else if(shade == 1)
        {
            return 0x407f10;
        }
        else
        {
            return 0xffff80;
        }
    }
    
    public void onUpdate(int mousex, int mousey)
    {
        processScrollMouse(mousex, mousey);
    }
    
    public void processScrollMouse(int mousex, int mousey)
    {
        mousex -= parentscreen.guiLeft;
        mousey -= parentscreen.guiTop;
        
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
}
