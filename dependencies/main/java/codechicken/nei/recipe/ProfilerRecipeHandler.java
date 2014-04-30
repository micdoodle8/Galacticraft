package codechicken.nei.recipe;

import codechicken.core.TaskProfiler;
import codechicken.core.TaskProfiler.ProfilerResult;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import java.text.DecimalFormat;
import java.util.*;

import static codechicken.lib.gui.GuiDraw.drawString;
import static codechicken.lib.gui.GuiDraw.getStringWidth;

public class ProfilerRecipeHandler implements ICraftingHandler, IUsageHandler
{
    private static TaskProfiler profiler = new TaskProfiler();

    public static TaskProfiler getProfiler()
    {
        profiler.clear();
        return profiler;
    }
    
    private boolean crafting;
    
    public ProfilerRecipeHandler(boolean crafting)
    {
        this.crafting = crafting;
    }
    
    @Override
    public String getRecipeName()
    {
        return NEIClientUtils.translate("recipe.profiler."+(crafting ? "crafting" : "usage"));
    }

    @Override
    public int numRecipes()
    {
        if(!NEIClientConfig.getBooleanSetting("inventory.profileRecipes"))
            return 0;
        
        return (int) Math.ceil(((crafting ? 
                GuiCraftingRecipe.craftinghandlers.size() : 
                GuiUsageRecipe.usagehandlers.size())-1)/6D);
    }

    @Override
    public void drawBackground(int recipe)
    {
    }

    @Override
    public void drawForeground(int recipe)
    {
        List<ProfilerResult> results = profiler.getResults();
        for(Iterator<ProfilerResult> it = results.iterator(); it.hasNext();)
            if(it.next().name.equals(getRecipeName()))
                it.remove();
        
        Collections.sort(results, new Comparator<ProfilerResult>()
        {
            @Override
            public int compare(ProfilerResult o1, ProfilerResult o2)
            {
                return o1.time < o2.time ? 1 : -1;
            }
        });
        
        for(int i = recipe*6; i < results.size() && i < (recipe+1)*6; i++)
        {
            ProfilerResult r = results.get(i);
            int y = (i%6)*20+6;
            drawString(r.name, 8, y, 0xFF808080, false);

            DecimalFormat format = new DecimalFormat("0.00");
            String s = format.format(r.fraction*100)+"%";
            if(r.time < 1000000L)
                s+= " ("+(r.time/1000)+"us)";
            else
                s+= " ("+(r.time/1000000)+"ms)";
            
            drawString(s, 156-getStringWidth(s), y+10, 0xFF404040, false);
        }
    }

    @Override
    public ArrayList<PositionedStack> getIngredientStacks(int recipe)
    {
        return new ArrayList<PositionedStack>();
    }

    @Override
    public ArrayList<PositionedStack> getOtherStacks(int recipetype)
    {
        return new ArrayList<PositionedStack>();
    }

    @Override
    public PositionedStack getResultStack(int recipe)
    {
        return null;
    }

    @Override
    public void onUpdate()
    {
    }

    @Override
    public boolean hasOverlay(GuiContainer gui, Container container, int recipe)
    {
        return false;
    }

    @Override
    public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui, int recipe)
    {
        return null;
    }

    @Override
    public IOverlayHandler getOverlayHandler(GuiContainer gui, int recipe)
    {
        return null;
    }

    @Override
    public int recipiesPerPage()
    {
        return 1;
    }

    @Override
    public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe)
    {
        return currenttip;
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe)
    {
        return currenttip;
    }

    @Override
    public boolean keyTyped(GuiRecipe gui, char keyChar, int keyCode, int recipe)
    {
        return false;
    }

    @Override
    public boolean mouseClicked(GuiRecipe gui, int button, int recipe)
    {
        return false;
    }

    @Override
    public IUsageHandler getUsageHandler(String inputId, Object... ingredients)
    {
        return this;
    }

    @Override
    public ICraftingHandler getRecipeHandler(String outputId, Object... results)
    {
        return this;
    }
}
