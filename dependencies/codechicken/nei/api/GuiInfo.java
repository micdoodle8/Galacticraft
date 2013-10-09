package codechicken.nei.api;

import java.util.Iterator;
import java.util.LinkedList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;

import codechicken.core.gui.GuiScreenWidget;
import codechicken.nei.NEIChestGuiHandler;
import codechicken.nei.NEICreativeGuiHandler;
import codechicken.nei.NEIDummySlotHandler;

public class GuiInfo
{    
    public static LinkedList<INEIGuiHandler> guiHandlers = new LinkedList<INEIGuiHandler>();
    
    public static void load()
    {
        API.registerNEIGuiHandler(new NEICreativeGuiHandler());
        API.registerNEIGuiHandler(new NEIChestGuiHandler());
        API.registerNEIGuiHandler(new NEIDummySlotHandler());
    }

    public static void clearGuiHandlers()
    {
        for(Iterator<INEIGuiHandler> iterator = guiHandlers.iterator(); iterator.hasNext();)
            if(iterator.next() instanceof GuiContainer)
                iterator.remove();
    }
    
    public static void switchGui(GuiScreen gui)
    {
        Minecraft mc = Minecraft.getMinecraft();
        if(mc.currentScreen instanceof GuiContainer)
            ((GuiContainer)mc.currentScreen).sendMouseClick(null, -999, 0, 0);
        
        mc.displayGuiScreen(gui);
        if(gui instanceof GuiContainer)
            ((GuiContainer)gui).refresh();
        else if(gui instanceof GuiScreenWidget)
            ((GuiScreenWidget)gui).reset();
    }
}
