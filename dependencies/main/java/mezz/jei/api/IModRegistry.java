package mezz.jei.api;

import mezz.jei.api.gui.IAdvancedGuiHandler;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Passed to IModPlugins so they can register themselves.
 */
public interface IModRegistry {

	/**
	 * Get helpers and tools for addon mods.
	 * @since JEI 2.27.0
	 */
	@Nonnull
	IJeiHelpers getJeiHelpers();

	/**
	 * Get useful functions relating to items.
	 * @since JEI 2.27.0
	 */
	@Nonnull
	IItemRegistry getItemRegistry();

	/**
	 * Add the recipe categories provided by this plugin.
	 */
	void addRecipeCategories(IRecipeCategory... recipeCategories);

	/**
	 * Add the recipe handlers provided by this plugin.
	 */
	void addRecipeHandlers(IRecipeHandler... recipeHandlers);

	/**
	 * Add the recipes provided by the plugin.
	 * These can be regular recipes, they will get wrapped by the provided recipe handlers.
	 * Recipes that are already registered with minecraft's recipe managers don't need to be added here.
	 */
	void addRecipes(List recipes);

	/**
	 * Add a clickable area on a gui to jump to specific categories of recipes in JEI.
	 *
	 * @param guiContainerClass  the gui class for JEI to detect.
	 * @param xPos               left x position of the clickable area, relative to the left edge of the gui.
	 * @param yPos               top y position of the clickable area, relative to the top edge of the gui.
	 * @param width              the width of the clickable area.
	 * @param height             the height of the clickable area.
	 * @param recipeCategoryUids the recipe categories that JEI should display.
	 */
	void addRecipeClickArea(@Nonnull Class<? extends GuiContainer> guiContainerClass, int xPos, int yPos, int width, int height, @Nonnull String... recipeCategoryUids);

	/**
	 * Add a handler to give JEI extra information about how to layout the item list next to a specific type of GuiContainer.
	 * Used for guis with tabs on the side that would normally intersect with JEI's item list.
	 */
	void addAdvancedGuiHandlers(@Nonnull IAdvancedGuiHandler<?>... advancedGuiHandlers);

	/**
	 * Add a description page for an itemStack.
	 * Description pages show in the recipes for an itemStack and tell the player a little bit about it.
	 *
	 * @param itemStack       the itemStack(s) to describe
	 * @param descriptionKeys Localization keys for description text.
	 *                        New lines can be added with "\n" or by giving multiple descriptionKeys.
	 *                        Long lines are wrapped automatically.
	 *                        Very long entries will span multiple pages automatically.
	 */
	void addDescription(ItemStack itemStack, String... descriptionKeys);

	void addDescription(List<ItemStack> itemStacks, String... descriptionKeys);

	/**
	 * Get the registry for setting up recipe transfer.
	 */
	IRecipeTransferRegistry getRecipeTransferRegistry();
}
