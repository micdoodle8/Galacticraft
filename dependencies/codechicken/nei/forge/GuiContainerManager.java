package codechicken.nei.forge;

import java.awt.Point;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.inventory.Slot;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import static codechicken.core.gui.GuiDraw.*;

public class GuiContainerManager
{
    public GuiContainer window;

    public static RenderItem drawItems = new RenderItem();
    public static final LinkedList<IContainerTooltipHandler> tooltipHandlers = new LinkedList<IContainerTooltipHandler>();
    public static final LinkedList<IContainerInputHandler> inputHandlers = new LinkedList<IContainerInputHandler>();
    public static final LinkedList<IContainerDrawHandler> drawHandlers = new LinkedList<IContainerDrawHandler>();
    public static final LinkedList<IContainerObjectHandler> objectHandlers = new LinkedList<IContainerObjectHandler>();
    public static final LinkedList<IContainerSlotClickHandler> slotClickHandlers = new LinkedList<IContainerSlotClickHandler>();

    // Check the version of LWGJL, if it is 2.9.0, then it solves the multi input problem by itself
    private static boolean flag;
    
    static
    {
        try
        {
            flag = "2.9.0".equals(Sys.getVersion());
        }
        catch (Throwable t)
        {
            System.err.println(String.format("Error getting lwjgl version: %s", t.toString()));
            flag = false;
        }
    }
    
    static
    {
        addSlotClickHandler(new DefaultSlotClickHandler());
    }

    public GuiContainerManager(GuiContainer screen)
    {
        window = screen;
    }
    
    /**
     * For compile time access without overriding GuiContainer.
     */
    public static GuiContainerManager getManager(GuiContainer gui)
    {
        return gui.manager;
    }

    /**
     * Register a new Tooltip render handler;
     * @param handler The handler to register
     */
    public static void addTooltipHandler(IContainerTooltipHandler handler)
    {
        tooltipHandlers.add(handler);
    }

    /**
     * Register a new Input handler;
     * @param handler The handler to register
     */
    public static void addInputHandler(IContainerInputHandler handler)
    {
        inputHandlers.add(handler);
    }

    /**
     * Register a new Drawing handler;
     * @param handler The handler to register
     */
    public static void addDrawHandler(IContainerDrawHandler handler)
    {
        drawHandlers.add(handler);
    }

    /**
     * Register a new Object handler;
     * @param handler The handler to register
     */
    public static void addObjectHandler(IContainerObjectHandler handler)
    {
        objectHandlers.add(handler);
    }

    /**
     * Care needs to be taken with this method. It will insert your handler at the start of the list to be called first. You may need to simply edit the list yourself.
     * @param handler The handler to register.
     */
    public static void addSlotClickHandler(IContainerSlotClickHandler handler)
    {
        slotClickHandlers.addFirst(handler);
    }

    public ItemStack getStackMouseOver()
    {
        Point mousePos = getMousePosition();

        for(IContainerObjectHandler objectHandler : objectHandlers)
        {
            ItemStack item = objectHandler.getStackUnderMouse(window, mousePos.x, mousePos.y);
            if(item != null)
                return item;
        }
        
        Slot slot = getSlotMouseOver();
        if(slot != null)
            return slot.getStack();
        
        return null;
    }
    
    public Slot getSlotMouseOver()
    {
        Point mousePos = getMousePosition();
        if(objectUnderMouse(mousePos.x, mousePos.y))
            return null;
        
        return window.getSlotAtPosition(mousePos.x, mousePos.y);
    }

    /**
     * Extra lines are often used for more information. For example enchantments, potion effects and mob spawner contents.
     * @param itemstack The item to get the name for.
     * @param gui An instance of the currentscreen passed to tooltip handlers. If null, only gui inspecific handlers should respond
     * @param includeHandlers If true tooltip handlers will add to the item tip
     * @return A list of Strings representing the text to be displayed on each line of the tool tip.
     */
    public static List<String> itemDisplayNameMultiline(ItemStack itemstack, GuiContainer gui, boolean includeHandlers)
    {
        List<String> namelist = null;
        try
        {
            namelist = itemstack.getTooltip(Minecraft.getMinecraft().thePlayer, includeHandlers && Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
        }
        catch(Throwable t) {}

        if(namelist == null)
            namelist = new ArrayList<String>();

        if(namelist.size() == 0)
            namelist.add("Unnamed");

        if(namelist.get(0) == null || namelist.get(0).equals(""))
            namelist.set(0, "Unnamed");

        if(includeHandlers)
        {
            for(IContainerTooltipHandler handler : tooltipHandlers)
            {
                namelist = handler.handleItemTooltip(gui, itemstack, namelist);
            }
        }

        namelist.set(0, "\247"+Integer.toHexString(itemstack.getRarity().rarityColor)+namelist.get(0));
        for(int i = 1; i < namelist.size(); i++)
            namelist.set(i, "\u00a77"+namelist.get(i));

        return namelist;
    }

    /**
     * The general name of this item.
     * @param itemstack The {@link ItemStack} to get the name for.
     * @return The first line of the multiline display name.
     */
    public static String itemDisplayNameShort(ItemStack itemstack)
    {
        List<String> list = itemDisplayNameMultiline(itemstack, null, false);
        return list.get(0);
    }

    /**
     * Concatenates the multiline display name into one line for easy searching using string and {@link Pattern} functions.
     * @param itemstack The stack to get the name for
     * @return The multiline display name of this item separated by '#'
     */
    public static String concatenatedDisplayName(ItemStack itemstack, boolean includeHandlers)
    {
        List<String> list = itemDisplayNameMultiline(itemstack, null, includeHandlers);
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for(String name : list)
        {
            if(first)
            {
                first = false;
            }
            else
            {
                sb.append("#");
            }
            sb.append(name);
        }
        String s = sb.toString();
        while(true)
        {
            int pos = s.indexOf('\247');
            if(pos == -1)
                break;
            s = s.substring(0, pos)+s.substring(pos+2);
        }
        return s;
    }

    public static FontRenderer getFontRenderer(ItemStack stack)
    {
        if(stack != null && stack.getItem() != null)
        {
            FontRenderer f = stack.getItem().getFontRenderer(stack);
            if(f != null)
                return f;
        }
        return fontRenderer;
    }

    public static void drawItem(int i, int j, ItemStack itemstack)
    {
        drawItem(i, j, itemstack, getFontRenderer(itemstack));
    }
    
    private static HashSet<String> stackTraces = new HashSet<String>();
    public static void drawItem(int i, int j, ItemStack itemstack, FontRenderer fontRenderer)
    {
        enable3DRender();
        drawItems.zLevel += 100F;
        try
        {
            drawItems.renderItemAndEffectIntoGUI(fontRenderer, renderEngine, itemstack, i, j);
            drawItems.renderItemOverlayIntoGUI(fontRenderer, renderEngine, itemstack, i, j);
        }
        catch(Exception e)
        {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stackTrace = itemstack+sw.toString();
            if(!stackTraces.contains(stackTrace))
            {
                System.err.println("Error while rendering: "+itemstack);
                e.printStackTrace();
                stackTraces.add(stackTrace);
            }
            
            if(Tessellator.instance.isDrawing)
                Tessellator.instance.draw();
            drawItems.renderItemIntoGUI(fontRenderer, renderEngine, new ItemStack(51, 1, 0), i, j);
        }
        drawItems.zLevel -= 100F;
        enable2DRender();

        if(Tessellator.instance.isDrawing)
            Tessellator.instance.draw();
    }
    
    public static void setColouredItemRender(boolean enable)
    {
        drawItems.renderWithColor = !enable;
    }

    public static void enable3DRender()
    {
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    public static void enable2DRender()
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    public void load()
    {
        for(IContainerObjectHandler objectHandler : objectHandlers)
            objectHandler.load(window);
    }

    public void refresh()
    {
        for(IContainerObjectHandler objectHandler : objectHandlers)
            objectHandler.guiTick(window);
    }

    public void guiTick()
    {
        for(IContainerObjectHandler objectHandler : objectHandlers)
            objectHandler.guiTick(window);
    }

    public boolean lastKeyTyped(int keyID, char keyChar)
    {
        for(IContainerInputHandler inputhander : inputHandlers)
            if(inputhander.lastKeyTyped(window, keyChar, keyID))
                return true;
        
        return false;
    }

    public boolean firstKeyTyped(int keyID, char keyChar)
    {
        for(IContainerInputHandler inputhander : inputHandlers)
            inputhander.onKeyTyped(window, keyChar, keyID);

        for(IContainerInputHandler inputhander : inputHandlers)
            if(inputhander.keyTyped(window, keyChar, keyID))
                return true;

        return false;
    }

    public boolean mouseClicked(int mousex, int mousey, int button)
    {
        for(IContainerInputHandler inputhander : inputHandlers)
            inputhander.onMouseClicked(window, mousex, mousey, button);

        for(IContainerInputHandler inputhander : inputHandlers)
            if(inputhander.mouseClicked(window, mousex, mousey, button))
                return true;
        
        return false;
    }

    public boolean mouseScrolled(int scrolled)
    {
        Point mousepos = getMousePosition();

        for(IContainerInputHandler inputHandler : inputHandlers)
            inputHandler.onMouseScrolled(window, mousepos.x, mousepos.y, scrolled);

        for(IContainerInputHandler inputHandler : inputHandlers)
            if(inputHandler.mouseScrolled(window, mousepos.x, mousepos.y, scrolled))
                return true;
        
        return false;
    }

    public void mouseUp(int mousex, int mousey, int button)
    {
        for(IContainerInputHandler inputhander : inputHandlers)
            inputhander.onMouseUp(window, mousex, mousey, button);
    }

    public void mouseDragged(int mousex, int mousey, int button, long heldTime)
    {
        for(IContainerInputHandler inputhander : inputHandlers)
            inputhander.onMouseDragged(window, mousex, mousey, button, heldTime);
    }

    public void preDraw()
    {
        for(IContainerDrawHandler drawHandler : drawHandlers)
            drawHandler.onPreDraw(window);
    }

    public void renderObjects(int mousex, int mousey)
    {
        for(IContainerDrawHandler drawHandler : drawHandlers)
            drawHandler.renderObjects(window, mousex, mousey);

        for(IContainerDrawHandler drawHandler : drawHandlers)
            drawHandler.postRenderObjects(window, mousex, mousey);
    }

    public void renderToolTips(int mousex, int mousey)
    {
        List<String> tooltip = window.handleTooltip(mousex, mousey, new LinkedList<String>());

        for(IContainerTooltipHandler handler : tooltipHandlers)
            tooltip = handler.handleTooltipFirst(window, mousex, mousey, tooltip);

        if(tooltip.isEmpty() && shouldShowTooltip())//mouseover tip, not holding an item
        {
            ItemStack stack = getStackMouseOver();
            if(stack != null)
                tooltip = itemDisplayNameMultiline(stack, window, true);
            
            tooltip = window.handleItemTooltip(stack, mousex, mousey, tooltip);
        }

        if(tooltip.size() > 0)
            tooltip.set(0, tooltip.get(0)+"\247h");
        drawMultilineTip(mousex + 12, mousey - 12, tooltip);
    }

    public boolean shouldShowTooltip()
    {
        for(IContainerObjectHandler handler : objectHandlers)
            if(!handler.shouldShowTooltip(window))
                return false;

        return window.mc.thePlayer.inventory.getItemStack() == null;
    }

    public void renderSlotUnderlay(Slot slot)
    {
        for(IContainerDrawHandler drawHandler : drawHandlers)
            drawHandler.renderSlotUnderlay(window, slot);
    }

    public void renderSlotOverlay(Slot slot)
    {
        for(IContainerDrawHandler drawHandler : drawHandlers)
            drawHandler.renderSlotOverlay(window, slot);
    }

    public boolean objectUnderMouse(int mousex, int mousey)
    {
        for(IContainerObjectHandler objectHandler : objectHandlers)
            if(objectHandler.objectUnderMouse(window, mousex, mousey))
                return true;

        return false;
    }

    public void handleMouseClick(Slot slot, int slotIndex, int button, int modifier)
    {
        for(IContainerSlotClickHandler handler : slotClickHandlers)
            handler.beforeSlotClick(window, slotIndex, button, slot, modifier);

        boolean eventHandled = false;
        for(IContainerSlotClickHandler handler : slotClickHandlers)
            eventHandled = handler.handleSlotClick(window, slotIndex, button, slot, modifier, eventHandled);

        for(IContainerSlotClickHandler handler : slotClickHandlers)
            handler.afterSlotClick(window, slotIndex, button, slot, modifier);
    }
    
    // Enable inputting Chinese characters
    public void fixhandleKeyboardInput()
    {
        // if it's LWGJL 2.9.0
        if(flag)
        {
            int k = Keyboard.getEventKey();
            char c = Keyboard.getEventCharacter();
            if (Keyboard.getEventKeyState())
                window.keyPress(k, c);
        }
        // if it isn't LWJGL 2.9.0, then make the Chinese input method to split the characters
        else if (Keyboard.getEventKeyState())
        {
            int k = Keyboard.getEventKey();
            char c = Keyboard.getEventCharacter();

            if (c > 0x7F && c <= 0xFF && Keyboard.next())
            {
                int k2 = Keyboard.getEventKey();
                char c2 = Keyboard.getEventCharacter();
                try
                {
                    c2 = new String(new byte[] { (byte) c, (byte) c2 }).charAt(0);
                    window.keyPress(k, c2);
                }
                catch (Throwable t)
                {
                    window.keyPress(k, c);
                    window.keyPress(k2, c2);
                }
            }
            else
            {
                window.keyPress(k, c);
            }
        }
    }
}