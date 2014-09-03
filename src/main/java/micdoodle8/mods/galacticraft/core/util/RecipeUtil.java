package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.inventory.InventoryBuggyBench;
import micdoodle8.mods.galacticraft.core.inventory.InventoryRocketBench;
import micdoodle8.mods.galacticraft.core.recipe.NasaWorkbenchRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.Arrays;
import java.util.HashMap;

public class RecipeUtil
{
    public static ItemStack findMatchingBuggy(InventoryBuggyBench benchStacks)
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

    public static ItemStack findMatchingSpaceshipRecipe(InventoryRocketBench inventoryRocketBench)
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
        GalacticraftRegistry.addT1RocketRecipe(new NasaWorkbenchRecipe(result, input));
    }

    public static void addBuggyBenchRecipe(ItemStack result, HashMap<Integer, ItemStack> input)
    {
        GalacticraftRegistry.addMoonBuggyRecipe(new NasaWorkbenchRecipe(result, input));
    }
}
