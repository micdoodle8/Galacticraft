package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.inventory.InventoryBuggyBench;
import micdoodle8.mods.galacticraft.core.inventory.InventoryRocketBench;
import micdoodle8.mods.galacticraft.core.recipe.NasaWorkbenchRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.ShapedOreRecipe;

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
    }
    
    public static void addBlockRecipe(ItemStack result, String oreDictIngot, ItemStack gcIngot)
    {   
    	RecipeUtil.addRecipe(result, new Object[] { "XXX", "XXX", "XXI", 'X', oreDictIngot, 'I', gcIngot });
    	RecipeUtil.addRecipe(result, new Object[] { "XXX", "XXX", "XIX", 'X', oreDictIngot, 'I', gcIngot });
    	RecipeUtil.addRecipe(result, new Object[] { "XXX", "XXX", "IXX", 'X', oreDictIngot, 'I', gcIngot });
    	RecipeUtil.addRecipe(result, new Object[] { "XXX", "XXI", "XXX", 'X', oreDictIngot, 'I', gcIngot });
    	RecipeUtil.addRecipe(result, new Object[] { "XXX", "XIX", "XXX", 'X', oreDictIngot, 'I', gcIngot });
    	RecipeUtil.addRecipe(result, new Object[] { "XXX", "IXX", "XXX", 'X', oreDictIngot, 'I', gcIngot });
    	RecipeUtil.addRecipe(result, new Object[] { "XXI", "XXX", "XXX", 'X', oreDictIngot, 'I', gcIngot });
    	RecipeUtil.addRecipe(result, new Object[] { "XIX", "XXX", "XXX", 'X', oreDictIngot, 'I', gcIngot });
    	RecipeUtil.addRecipe(result, new Object[] { "IXX", "XXX", "XXX", 'X', oreDictIngot, 'I', gcIngot });
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
