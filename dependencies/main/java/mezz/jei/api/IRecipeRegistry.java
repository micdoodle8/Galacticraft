package mezz.jei.api;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fluids.FluidStack;

/**
 * The IRecipeManager offers several functions for retrieving and handling recipes.
 * The IRecipeManager instance is provided in JEIManager.
 * Get the instance from {@link IJeiRuntime#getRecipeRegistry()}.
 */
public interface IRecipeRegistry {

	/**
	 * Returns the IRecipeHandler associated with the recipeClass or null if there is none
	 */
	@Nullable
	<T> IRecipeHandler<T> getRecipeHandler(Class<? extends T> recipeClass);

	/**
	 * Returns an unmodifiable list of all Recipe Categories
	 */
	List<IRecipeCategory> getRecipeCategories();

	/**
	 * Returns an unmodifiable list of Recipe Categories
	 */
	List<IRecipeCategory> getRecipeCategories(List<String> recipeCategoryUids);

	/**
	 * Returns a new focus.
	 */
	<V> IFocus<V> createFocus(IFocus.Mode mode, @Nullable V ingredient);

	/**
	 * Returns a list of Recipe Categories for the focus.
	 *
	 * @since JEI 3.11.0
	 */
	<V> List<IRecipeCategory> getRecipeCategories(IFocus<V> focus);

	/**
	 * Returns a list of Recipe Wrappers in the recipeCategory that have the focus.
	 *
	 * @since JEI 3.12.0
	 */
	<T extends IRecipeWrapper, V> List<T> getRecipeWrappers(IRecipeCategory<T> recipeCategory, IFocus<V> focus);

	/**
	 * Returns a list of Recipe Wrappers in recipeCategory.
	 *
	 * @since JEI 3.12.0
	 */
	<T extends IRecipeWrapper> List<T> getRecipeWrappers(IRecipeCategory<T> recipeCategory);

	/**
	 * Returns an unmodifiable collection of ItemStacks that can craft the recipes from recipeCategory.
	 * For instance, the crafting table ItemStack is returned here for Crafting recipe category.
	 * These are registered with {@link IModRegistry#addRecipeCategoryCraftingItem(ItemStack, String...)}.
	 * <p>
	 * This takes the current focus into account, so that if the focus mode is set to Input
	 * and the focus is included in the craftingItems, it is the only one returned.
	 *
	 * @since JEI 3.11.0
	 */
	List<ItemStack> getCraftingItems(IRecipeCategory recipeCategory, IFocus focus);

	/**
	 * Returns the recipe transfer handler for the given container and category, if one exists.
	 *
	 * @param container      The container to transfer items in.
	 * @param recipeCategory The type of recipe that the recipe transfer handler acts on.
	 * @see IRecipeTransferRegistry
	 * @since JEI 3.13.2
	 */
	@Nullable
	IRecipeTransferHandler getRecipeTransferHandler(Container container, IRecipeCategory recipeCategory);

	/**
	 * Returns a drawable recipe layout, for addons that want to draw the layouts somewhere.
	 * Layouts created this way do not have recipe transfer buttons, they are not useful for this purpose.
	 *
	 * @param recipeCategory the recipe category that the recipe belongs to
	 * @param recipeWrapper  the specific recipe wrapper to draw.
	 * @param focus          the focus of the recipe layout.
	 * @since JEI 3.13.2
	 */
	@Nullable
	<T extends IRecipeWrapper> IRecipeLayoutDrawable createRecipeLayoutDrawable(IRecipeCategory<T> recipeCategory, T recipeWrapper, IFocus focus);

	/**
	 * Add a new recipe while the game is running.
	 * This is only for things like gated recipes becoming available, like the ones in Thaumcraft.
	 * Use your {@link IRecipeHandler#isRecipeValid(Object)} to determine which recipes are hidden, and when a recipe becomes valid you can add it here.
	 * (note that {@link IRecipeHandler#isRecipeValid(Object)} must be true when the recipe is added here for it to work)
	 */
	void addRecipe(Object recipe);

	/**
	 * Add a new smelting recipe while the game is running.
	 * By default, all smelting recipes from {@link FurnaceRecipes#smeltingList} are already added by JEI.
	 *
	 * @since JEI 3.14.2
	 */
	void addSmeltingRecipe(List<ItemStack> inputs, ItemStack output);

	/**
	 * Remove a recipe while the game is running.
	 *
	 * @since JEI 3.14.1
	 */
	void removeRecipe(Object recipe);

	// DEPRECATED METHODS BELOW


	/**
	 * Returns an unmodifiable list of Recipes in recipeCategory that have the focus.
	 *
	 * @since JEI 3.11.0
	 * @deprecated since JEI 3.12.0. Use {@link #getRecipeWrappers(IRecipeCategory, IFocus)}
	 */
	@Deprecated
	<V> List<Object> getRecipes(IRecipeCategory recipeCategory, IFocus<V> focus);

	/**
	 * Returns an unmodifiable list of Recipes in recipeCategory
	 *
	 * @deprecated since JEI 3.12.0. Use {@link #getRecipeWrappers(IRecipeCategory)}
	 */
	@Deprecated
	List<Object> getRecipes(IRecipeCategory recipeCategory);

	/**
	 * Returns an unmodifiable collection of ItemStacks that can craft recipes from recipeCategory.
	 * For instance, the crafting table ItemStack is returned here for Crafting recipe category.
	 * These are registered with {@link IModRegistry#addRecipeCategoryCraftingItem(ItemStack, String...)}.
	 *
	 * @since JEI 3.3.0
	 * @deprecated since JEI 3.11.0. Use {@link #getCraftingItems(IRecipeCategory, IFocus)}.
	 */
	@Deprecated
	Collection<ItemStack> getCraftingItems(IRecipeCategory recipeCategory);

	/**
	 * Returns an unmodifiable list of Recipe Categories that have the ItemStack as an input.
	 *
	 * @deprecated since JEI 3.11.0. Use {@link #getRecipeCategories(IFocus)}
	 */
	@Deprecated
	List<IRecipeCategory> getRecipeCategoriesWithInput(ItemStack input);

	/**
	 * Returns an unmodifiable list of Recipe Categories that have the Fluid as an input.
	 *
	 * @deprecated since JEI 3.11.0. Use {@link #getRecipeCategories(IFocus)}
	 */
	@Deprecated
	List<IRecipeCategory> getRecipeCategoriesWithInput(FluidStack input);

	/**
	 * Returns an unmodifiable list of Recipe Categories that have the ItemStack as an output.
	 *
	 * @deprecated since JEI 3.11.0. Use {@link #getRecipeCategories(IFocus)}
	 */
	@Deprecated
	List<IRecipeCategory> getRecipeCategoriesWithOutput(ItemStack output);

	/**
	 * Returns an unmodifiable list of Recipe Categories that have the Fluid as an output.
	 *
	 * @deprecated since JEI 3.11.0. Use {@link #getRecipeCategories(IFocus)}
	 */
	@Deprecated
	List<IRecipeCategory> getRecipeCategoriesWithOutput(FluidStack output);

	/**
	 * Returns an unmodifiable list of Recipes of recipeCategory that have the ItemStack as an input.
	 *
	 * @deprecated since JEI 3.11.0. Use {@link #getRecipeWrappers(IRecipeCategory, IFocus)}
	 */
	@Deprecated
	List<Object> getRecipesWithInput(IRecipeCategory recipeCategory, ItemStack input);

	/**
	 * Returns an unmodifiable list of Recipes of recipeCategory that have the Fluid as an input.
	 *
	 * @deprecated since JEI 3.11.0. Use {@link #getRecipeWrappers(IRecipeCategory, IFocus)}
	 */
	@Deprecated
	List<Object> getRecipesWithInput(IRecipeCategory recipeCategory, FluidStack input);

	/**
	 * Returns an unmodifiable list of Recipes of recipeCategory that have the ItemStack as an output.
	 *
	 * @deprecated since JEI 3.11.0. Use {@link #getRecipeWrappers(IRecipeCategory, IFocus)}
	 */
	@Deprecated
	List<Object> getRecipesWithOutput(IRecipeCategory recipeCategory, ItemStack output);

	/**
	 * Returns an unmodifiable list of Recipes of recipeCategory that have the Fluid as an output.
	 *
	 * @deprecated since JEI 3.11.0. Use {@link #getRecipeWrappers(IRecipeCategory, IFocus)}
	 */
	@Deprecated
	List<Object> getRecipesWithOutput(IRecipeCategory recipeCategory, FluidStack output);
}
