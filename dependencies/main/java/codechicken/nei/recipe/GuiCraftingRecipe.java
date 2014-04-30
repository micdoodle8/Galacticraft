package codechicken.nei.recipe;

import codechicken.core.TaskProfiler;
import codechicken.nei.NEIClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;

import java.util.ArrayList;

public class GuiCraftingRecipe extends GuiRecipe
{
    public static boolean openRecipeGui(String outputId, Object... results) {
        Minecraft mc = NEIClientUtils.mc();
        GuiContainer prevscreen = mc.currentScreen instanceof GuiContainer ? (GuiContainer) mc.currentScreen : null;

        TaskProfiler profiler = ProfilerRecipeHandler.getProfiler();
        ArrayList<ICraftingHandler> handlers = new ArrayList<ICraftingHandler>();
        for (ICraftingHandler craftinghandler : craftinghandlers) {
            profiler.start(craftinghandler.getRecipeName());
            ICraftingHandler handler = craftinghandler.getRecipeHandler(outputId, results);
            if (handler.numRecipes() > 0)
                handlers.add(handler);
        }
        profiler.end();
        if (handlers.isEmpty())
            return false;

        mc.displayGuiScreen(new GuiCraftingRecipe(prevscreen, handlers));
        return true;
    }

    private GuiCraftingRecipe(GuiContainer prevgui, ArrayList<ICraftingHandler> handlers) {
        super(prevgui);
        currenthandlers = handlers;
    }

    public static void registerRecipeHandler(ICraftingHandler handler) {
        for (ICraftingHandler handler1 : craftinghandlers)
            if (handler1.getClass() == handler.getClass())
                return;

        craftinghandlers.add(handler);
    }

    public ArrayList<? extends IRecipeHandler> getCurrentRecipeHandlers() {
        return currenthandlers;
    }

    public ArrayList<ICraftingHandler> currenthandlers;

    public static ArrayList<ICraftingHandler> craftinghandlers = new ArrayList<ICraftingHandler>();
}
