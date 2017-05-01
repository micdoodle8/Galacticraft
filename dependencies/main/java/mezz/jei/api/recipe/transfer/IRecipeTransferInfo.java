package mezz.jei.api.recipe.transfer;

import java.util.List;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

/**
 * Gives JEI the information it needs to transfer recipes from a slotted inventory into the crafting area.
 *
 * Most plugins should create new IRecipeTransferInfo instances with IModRegistry.createRecipeTransferHelper.
 * Containers with slot ranges that contain gaps or other oddities can implement this interface directly.
 * Containers that need full control over the recipe transfer or do not use slots can implement IRecipeTransferHandler.
 */
public interface IRecipeTransferInfo {
	/**
	 * Return the container class that this recipe transfer helper supports
	 */
	Class<? extends Container> getContainerClass();

	/**
	 * Return the recipe category that this container can handle.
	 */
	String getRecipeCategoryUid();

	/**
	 * Return a list of slots for the recipe area.
	 */
	List<Slot> getRecipeSlots(Container container);

	/**
	 * Return a list of slots that the transfer can use to get items for crafting, or place leftover items.
	 */
	List<Slot> getInventorySlots(Container container);
}
