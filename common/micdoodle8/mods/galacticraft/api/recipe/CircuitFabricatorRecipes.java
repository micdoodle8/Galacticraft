package micdoodle8.mods.galacticraft.api.recipe;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;

public class CircuitFabricatorRecipes
{
	private static HashMap<ItemStack[], ItemStack> recipes = new HashMap<ItemStack[], ItemStack>();

	/**
	 * 
	 * Input list must be ItemStack array with 5 elements, contain null if no
	 * item is used in the slot.
	 * 
	 * 0 - Crystal slot 1 - Silicon slot 2 - Silicon slot 3 - Redstone slot 4 -
	 * Optional slot
	 * 
	 * @param output
	 * @param inputList
	 *            ItemStack array with length 5. Fill with stacks as explained
	 *            above
	 * @return
	 */
	public static void addRecipe(ItemStack output, ItemStack[] inputList)
	{
		if (inputList.length != 5)
		{
			throw new RuntimeException("Invalid circuit fabricator recipe!");
		}

		CircuitFabricatorRecipes.recipes.put(inputList, output);
	}

	/**
	 * Gets the output ItemStack for the passed input.
	 * 
	 * @param inputList
	 *            ItemStack array of input items
	 * @return The result ItemStack
	 */
	public static ItemStack getOutputForInput(ItemStack[] inputList)
	{
		if (inputList.length != 5)
		{
			return null;
		}

		for (Entry<ItemStack[], ItemStack> recipe : CircuitFabricatorRecipes.recipes.entrySet())
		{
			boolean found = true;

			for (int i = 0; i < 5; i++)
			{
				ItemStack recipeStack = recipe.getKey()[i];
				ItemStack inputStack = inputList[i];

				if (recipeStack == null || inputStack == null)
				{
					if (recipeStack != null || inputStack != null)
					{
						found = false;
						break;
					}
				}
				else if (recipeStack.itemID != inputStack.itemID || recipeStack.getItemDamage() != inputStack.getItemDamage())
				{
					found = false;
					break;
				}
			}

			if (!found)
			{
				continue;
			}

			return recipe.getValue();
		}

		return CircuitFabricatorRecipes.recipes.get(inputList);
	}
}
