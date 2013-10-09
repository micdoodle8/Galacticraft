package codechicken.nei.recipe;

import java.util.List;

import org.lwjgl.input.Keyboard;

import codechicken.nei.PositionedStack;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.api.IRecipeOverlayRenderer;

import net.minecraft.inventory.Container;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

/**
 * Do not implement this. Implement one of it's subinterfaces, either {@link ICraftingHandler} or {@link IUsageHandler}
 */
public interface IRecipeHandler
{
    /**
     * 
     * @return The name of this inventory. 
     * To be displayed at the top of the viewing container.
     */
    public String getRecipeName();
    /**
     * 
     * @return The number of recipes that this handler contains.
     */
    public int numRecipes();
    /**
     * Draw the background of this recipe handler (basically the slot layout image).
     * @param gui A GuiManager for drawing functions.
     * @param recipe The recipe index to draw at this position.
     */
    public void drawBackground(int recipe);
    /**
     * Draw the foreground of this recipe handler (for things like progress bars).
     * @param gui A GuiManager for drawing functions.
     * @param recipe The recipe index to draw at this position.
     */
    public void drawForeground(int recipe);
    /**
     * 
     * @param recipe The recipe index to get items for.
     * @return A list of the ingredient {@link PositionedStack}s in this recipe relative to the top left corner of your recipe drawing space.
     */
    public List<PositionedStack> getIngredientStacks(int recipe);
    /**
     * 
     * @param recipe The recipe index to get items for.
     * @return A list of the other {@link PositionedStack}s in this recipe relative to the top left corner of your recipe drawing space. For example fuel in furnaces.
     */
    public List<PositionedStack> getOtherStacks(int recipetype);
    /**
     * 
     * @param recipe The recipe index to get the result for.
     * @return The recipe result {@link PositionedStack} relative to the top left corner of your recipe drawing space.
     */
    public PositionedStack getResultStack(int recipe);
    /**
     * A tick function called for updating progress bars and cycling damage items.
     */
    public void onUpdate();
    /**
     * 
     * @param recipe The recipe index to check for.
     * @param gui The GUI to overlay.
     * @param container The container of the GUI.
     * @return true if this recipe can render an overlay for the specified type of inventory GUI.
     */
    public boolean hasOverlay(GuiContainer gui, Container container, int recipe);    
    /**
     * 
     * @param recipe The recipe index to get the overlay renderer for.
     * @return An instance of {@link IRecipeOverlayRenderer} to be used for rendering the overlay of this specific recipe.
     */
    public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui, int recipe);
    /**
     * 
     * @param recipe The recipe index to get the overlay renderer for.
     * @return An instance of {@link IOverlayHandler} to be used for rendering the overlay of this specific recipe.
     */
    public IOverlayHandler getOverlayHandler(GuiContainer gui, int recipe);
    /**
     * 
     * @return The number of recipes that can fit on a page in the viewer (1 or 2)
     */
    public int recipiesPerPage();
    /**
     * 
     * @param gui An instance of the currentscreen
     * @param currenttip The current tooltip, will contain item name and info
     * @param recipe The recipe index being handled
     * @return The modified tooltip. DO NOT return null
     */
    public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe);
    /**
     * 
     * @param gui An instance of the currentscreen
     * @param The itemstack currently under the mouse
     * @param The current tooltip, will contain item name and info
     * @param recipe The recipe index being handled
     * @return The modified tooltip. DO NOT return null
     */
    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe);
    
    /**
     * 
     * @param gui An instance of the currentscreen
     * @param keyChar The character representing the keyPress
     * @param keyCode The KeyCode as defined in {@link Keyboard}
     * @return true to terminate further processing of this event.
     */
    public boolean keyTyped(GuiRecipe gui, char keyChar, int keyCode, int recipe);
    /**
     * 
     * @param gui An instance of the currentscreen
     * @param button The button index being pressed, {0 = Left Click, 1 = Right Click, 2 = Middle Click}
     * @return true to terminate further processing of this event.
     */
    public boolean mouseClicked(GuiRecipe gui, int button, int recipe);
}
