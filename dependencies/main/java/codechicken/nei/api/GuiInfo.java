package codechicken.nei.api;

import codechicken.nei.NEIChestGuiHandler;
import codechicken.nei.NEICreativeGuiHandler;
import codechicken.nei.NEIDummySlotHandler;
import net.minecraft.client.gui.inventory.GuiContainer;

import java.util.Iterator;
import java.util.LinkedList;

public class GuiInfo
{
    public static LinkedList<INEIGuiHandler> guiHandlers = new LinkedList<INEIGuiHandler>();

    public static void load() {
        API.registerNEIGuiHandler(new NEICreativeGuiHandler());
        API.registerNEIGuiHandler(new NEIChestGuiHandler());
        API.registerNEIGuiHandler(new NEIDummySlotHandler());
    }

    public static void clearGuiHandlers() {
        for (Iterator<INEIGuiHandler> iterator = guiHandlers.iterator(); iterator.hasNext(); )
            if (iterator.next() instanceof GuiContainer)
                iterator.remove();
    }
}
