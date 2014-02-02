package micdoodle8.mods.galacticraft.mars.util;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.mars.inventory.GCMarsInventoryCargoRocketBench;
import micdoodle8.mods.galacticraft.mars.inventory.GCMarsInventoryRocketBenchT2;
import net.minecraft.item.ItemStack;

/**
 * RecipeUtilMars.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class RecipeUtilMars
{
	public static ItemStack findMatchingSpaceshipT2Recipe(GCMarsInventoryRocketBenchT2 inventoryRocketBench)
	{
		for (INasaWorkbenchRecipe recipe : GalacticraftRegistry.getRocketT2Recipes())
		{
			if (recipe.matches(inventoryRocketBench))
			{
				return recipe.getRecipeOutput();
			}
		}

		return null;
	}

	public static ItemStack findMatchingCargoRocketRecipe(GCMarsInventoryCargoRocketBench inventoryRocketBench)
	{
		for (INasaWorkbenchRecipe recipe : GalacticraftRegistry.getCargoRocketRecipes())
		{
			if (recipe.matches(inventoryRocketBench))
			{
				return recipe.getRecipeOutput();
			}
		}

		return null;
	}
}
