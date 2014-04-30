package codechicken.nei.recipe;

import codechicken.core.TaskProfiler;
import codechicken.nei.NEIClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;

import java.util.ArrayList;

public class GuiUsageRecipe extends GuiRecipe
{
    public static boolean openRecipeGui(String inputId, Object... ingredients) {
        Minecraft mc = NEIClientUtils.mc();
        GuiContainer prevscreen = mc.currentScreen instanceof GuiContainer ? (GuiContainer) mc.currentScreen : null;

        TaskProfiler profiler = ProfilerRecipeHandler.getProfiler();
        ArrayList<IUsageHandler> handlers = new ArrayList<IUsageHandler>();
        for (IUsageHandler usagehandler : usagehandlers) {
            profiler.start(usagehandler.getRecipeName());
            IUsageHandler handler = usagehandler.getUsageHandler(inputId, ingredients);
            if (handler.numRecipes() > 0)
                handlers.add(handler);
        }
        profiler.end();
        if (handlers.isEmpty())
            return false;

        mc.displayGuiScreen(new GuiUsageRecipe(prevscreen, handlers));
        return true;
    }

    private GuiUsageRecipe(GuiContainer prevgui, ArrayList<IUsageHandler> handlers) {
        super(prevgui);
        currenthandlers = handlers;
    }

    public static void registerUsageHandler(IUsageHandler handler) {
        for (IUsageHandler handler1 : usagehandlers) {
            if (handler1.getClass() == handler.getClass())
                return;
        }

        usagehandlers.add(handler);
    }

    public ArrayList<? extends IRecipeHandler> getCurrentRecipeHandlers() {
        return currenthandlers;
    }

    public ArrayList<IUsageHandler> currenthandlers;

    public static ArrayList<IUsageHandler> usagehandlers = new ArrayList<IUsageHandler>();
}
