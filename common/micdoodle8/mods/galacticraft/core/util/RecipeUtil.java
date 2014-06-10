package micdoodle8.mods.galacticraft.core.util;

import gregtechmod.api.GregTech_API;
import ic2.api.item.Items;

import java.util.Arrays;
import java.util.HashMap;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.GCLog;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreInventoryBuggyBench;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreInventoryRocketBench;
import micdoodle8.mods.galacticraft.core.recipe.GCCoreNasaWorkbenchRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * RecipeUtil.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class RecipeUtil
{
	public static ItemStack findMatchingBuggy(GCCoreInventoryBuggyBench benchStacks)
	{
		for (INasaWorkbenchRecipe recipe : GalacticraftRegistry.getBuggyBenchRecipes())
		{
			if (recipe.matches(benchStacks))
			{
				return recipe.getRecipeOutput();
			}
		}

		return null;
	}

	public static ItemStack findMatchingSpaceshipRecipe(GCCoreInventoryRocketBench inventoryRocketBench)
	{
		for (INasaWorkbenchRecipe recipe : GalacticraftRegistry.getRocketT1Recipes())
		{
			if (recipe.matches(inventoryRocketBench))
			{
				return recipe.getRecipeOutput();
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static void addRecipe(ItemStack result, Object[] obj)
	{
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(result, obj));

		Object[] newIngredients = Arrays.copyOf(obj, obj.length);
		boolean changed = false;

		for (int i = 0; i < obj.length; i++)
		{
			if (newIngredients[i] instanceof String)
			{
				String ingred = (String) newIngredients[i];

				if (ingred.equals("ingotAluminum"))
				{
					newIngredients[i] = "ingotAluminium";
					changed = true;
				}
				else if (ingred.equals("compressedAluminum"))
				{
					newIngredients[i] = "compressedAluminium";
					changed = true;
				}
			}
		}

		if (changed)
		{
			CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(result, newIngredients));
		}

		newIngredients = Arrays.copyOf(obj, obj.length);
		changed = false;

		for (int i = 0; i < obj.length; i++)
		{
			if (newIngredients[i] instanceof String)
			{
				String ingred = (String) newIngredients[i];

				if (ingred.equals("ingotAluminum"))
				{
					newIngredients[i] = "ingotNaturalAluminum";
					changed = true;
				}
			}
		}

		if (changed)
		{
			CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(result, newIngredients));
		}
	}

	public static void addRocketBenchRecipe(ItemStack result, HashMap<Integer, ItemStack> input)
	{
		GalacticraftRegistry.addT1RocketRecipe(new GCCoreNasaWorkbenchRecipe(result, input));
	}

	public static void addBuggyBenchRecipe(ItemStack result, HashMap<Integer, ItemStack> input)
	{
		GalacticraftRegistry.addMoonBuggyRecipe(new GCCoreNasaWorkbenchRecipe(result, input));
	}

	public static ItemStack getGregtechBlock(int index, int amount, int metadata)
	{
		ItemStack stack = GregTech_API.getGregTechBlock(index, amount, metadata);

		if (stack != null)
		{
			return stack;
		}

		GCLog.severe("Failed to load Gregtech block for recipe, ensure Gregtech has loaded properly");
		return stack;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack getGregtechItem(int index, int amount, int metadata)
	{
		ItemStack stack = GregTech_API.getGregTechItem(index, index, metadata);

		if (stack != null)
		{
			return stack;
		}

		GCLog.severe("Failed to load Gregtech item for recipe, ensure Gregtech has loaded properly");
		return stack;
	}

	public static ItemStack getIndustrialCraftItem(String indentifier)
	{
		return Items.getItem(indentifier);
	}
}
