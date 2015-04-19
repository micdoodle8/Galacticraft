package micdoodle8.mods.galacticraft.core.util;

import ic2.api.item.IC2Items;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.inventory.InventoryBuggyBench;
import micdoodle8.mods.galacticraft.core.inventory.InventoryRocketBench;
import micdoodle8.mods.galacticraft.core.recipe.NasaWorkbenchRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

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
    	if (OreDictionary.getOres(oreDictIngot).size() > 1)
    	{
    		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(result, new Object[] { gcIngot, oreDictIngot, oreDictIngot, oreDictIngot, oreDictIngot, oreDictIngot, oreDictIngot, oreDictIngot, oreDictIngot }));
    	}
    	else
	    	RecipeUtil.addRecipe(result, new Object[] { "XXX", "XXX", "XXX", 'X', gcIngot });
    }


    public static void addRocketBenchRecipe(ItemStack result, HashMap<Integer, ItemStack> input)
    {
        GalacticraftRegistry.addT1RocketRecipe(new NasaWorkbenchRecipe(result, input));
    }

    public static void addBuggyBenchRecipe(ItemStack result, HashMap<Integer, ItemStack> input)
    {
        GalacticraftRegistry.addMoonBuggyRecipe(new NasaWorkbenchRecipe(result, input));
    }


    public static ItemStack getIndustrialCraftItem(String indentifier)
	{
		return IC2Items.getItem(indentifier);
	}
}
