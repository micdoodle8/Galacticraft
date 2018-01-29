package mezz.jei.api.gui;

import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;

/**
 * Represents the layout of one recipe on-screen.
 * Plugins interpret a recipe wrapper to set the properties here.
 * It is passed to plugins in {@link IRecipeCategory#setRecipe(IRecipeLayout, IRecipeWrapper)}.
 * @see IRecipeLayoutDrawable
 */
public interface IRecipeLayout {
	/**
	 * Contains all the itemStacks displayed on this recipe layout.
	 * Init and set them in your recipe category.
	 */
	IGuiItemStackGroup getItemStacks();

	/**
	 * Contains all the fluidStacks displayed on this recipe layout.
	 * Init and set them in your recipe category.
	 */
	IGuiFluidStackGroup getFluidStacks();

	/**
	 * Get all the ingredients of one class that are displayed on this recipe layout.
	 * Init and set them in your recipe category.
	 * <p>
	 * This method is for handling custom item types, registered with {@link IModIngredientRegistration}.
	 *
	 * @see #getItemStacks()
	 * @see #getFluidStacks()
	 */
	<T> IGuiIngredientGroup<T> getIngredientsGroup(Class<T> ingredientClass);

	/**
	 * The current search focus. Set by the player when they look up the recipe. The object being looked up is the focus.
	 *
	 * @since JEI 3.11.0
	 */
	IFocus<?> getFocus();

	/**
	 * Moves the recipe transfer button's position relative to the recipe layout.
	 * By default the recipe transfer button is at the bottom, to the right of the recipe.
	 * If it doesn't fit there, you can use this to move it when you init the recipe layout.
	 */
	void setRecipeTransferButton(int posX, int posY);
}
