package codechicken.nei.api;

import codechicken.nei.NEIChestGuiHandler;
import codechicken.nei.NEICreativeGuiHandler;
import codechicken.nei.NEIDummySlotHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class GuiInfo
{
    public static LinkedList<INEIGuiHandler> guiHandlers = new LinkedList<INEIGuiHandler>();
    public static HashSet<Class<? extends GuiContainer>> customSlotGuis = new HashSet<Class<? extends GuiContainer>>();

    public static void load() {
        API.registerNEIGuiHandler(new NEICreativeGuiHandler());
        API.registerNEIGuiHandler(new NEIChestGuiHandler());
        API.registerNEIGuiHandler(new NEIDummySlotHandler());
        customSlotGuis.add(GuiContainerCreative.class);
    }

    public static void clearGuiHandlers() {
        for (Iterator<INEIGuiHandler> iterator = guiHandlers.iterator(); iterator.hasNext(); )
            if (iterator.next() instanceof GuiContainer)
                iterator.remove();
    }

    public static boolean hasCustomSlots(GuiContainer gui) {
        return customSlotGuis.contains(gui.getClass());
    }
}
