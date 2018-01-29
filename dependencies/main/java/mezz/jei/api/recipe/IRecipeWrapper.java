package mezz.jei.api.recipe;

import javax.annotation.Nullable;
import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IGuiIngredientGroup;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;

/**
 * A wrapper around a normal recipe with methods that allow JEI can make sense of it.
 * Plugins implement these to wrap each type of recipe they have.
 * <p>
 * Normal recipes are converted to wrapped recipes by {@link IRecipeHandler#getRecipeWrapper(Object)}.
 *
 * @see BlankRecipeWrapper
 */
public interface IRecipeWrapper {

	/**
	 * Gets all the recipe's ingredients by filling out an instance of {@link IIngredients}.
	 *
	 * @since JEI 3.11.0
	 */
	void getIngredients(IIngredients ingredients);

	/**
	 * Return a list of recipe inputs.
	 * Each element can be an ItemStack, null, or a List of ItemStacks.
	 *
	 * @deprecated since JEI 3.11.0. Use {@link #getIngredients(IIngredients)}
	 */
	@Deprecated
	List getInputs();

	/**
	 * Return a list of recipe inputs.
	 * Each element can be an ItemStack, null, or a List of ItemStacks.
	 *
	 * @deprecated since JEI 3.11.0. Use {@link #getIngredients(IIngredients)}
	 */
	@Deprecated
	List getOutputs();

	/**
	 * Return a list of recipe fluid inputs.
	 *
	 * @deprecated since JEI 3.11.0. Use {@link #getIngredients(IIngredients)}
	 */
	@Deprecated
	List<FluidStack> getFluidInputs();

	/**
	 * Return a list of recipe fluid outputs.
	 *
	 * @deprecated since JEI 3.11.0. Use {@link #getIngredients(IIngredients)}
	 */
	@Deprecated
	List<FluidStack> getFluidOutputs();

	/**
	 * Draw additional info about the recipe.
	 * Use the mouse position for things like button highlights.
	 * Tooltips are handled by {@link IRecipeWrapper#getTooltipStrings(int, int)}
	 *
	 * @param mouseX the X position of the mouse, relative to the recipe.
	 * @param mouseY the Y position of the mouse, relative to the recipe.
	 * @see IDrawable for a simple class for drawing things.
	 * @see IGuiHelper for useful functions.
	 * @since JEI 2.19.0
	 */
	void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY);

	/**
	 * Draw animations involving the recipe. Can be disabled in the config.
	 *
	 * @see IDrawableAnimated for a simple class for drawing animated things.
	 * @see IGuiHelper for useful functions.
	 * @deprecated since 3.13.1. Move animations into {@link #drawInfo(Minecraft, int, int, int, int)},
	 * these are being combined because nobody uses the config option to disable animations.
	 */
	@Deprecated
	void drawAnimations(Minecraft minecraft, int recipeWidth, int recipeHeight);

	/**
	 * Get the tooltip for whatever's under the mouse.
	 * ItemStack and fluid tooltips are already handled by JEI, this is for anything else.
	 *
	 * To add to ingredient tooltips, see {@link IGuiIngredientGroup#addTooltipCallback(ITooltipCallback)}
	 * To add tooltips for a recipe category, see {@link IRecipeCategory#getTooltipStrings(int, int)}
	 *
	 * @param mouseX the X position of the mouse, relative to the recipe.
	 * @param mouseY the Y position of the mouse, relative to the recipe.
	 * @return tooltip strings. If there is no tooltip at this position, return null or an empty list.
	 */
	@Nullable
	List<String> getTooltipStrings(int mouseX, int mouseY);

	/**
	 * Called when a player clicks the recipe.
	 * Useful for implementing buttons, hyperlinks, and other interactions to your recipe.
	 *
	 * @param mouseX      the X position of the mouse, relative to the recipe.
	 * @param mouseY      the Y position of the mouse, relative to the recipe.
	 * @param mouseButton the current mouse event button.
	 * @return true if the click was handled, false otherwise
	 * @since JEI 2.19.0
	 */
	boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton);
}
