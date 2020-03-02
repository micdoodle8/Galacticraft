package micdoodle8.mods.galacticraft.core.client.jei.tier1rocket;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class Tier1RocketRecipeMaker
{
     public static List<INasaWorkbenchRecipe> getRecipesList()
    {
        List<INasaWorkbenchRecipe> recipes = new ArrayList<>();

        int chestCount = -1;
        for (INasaWorkbenchRecipe recipe : GalacticraftRegistry.getRocketT1Recipes())
        {
            int chests = Tier1RocketRecipeMaker.countChests(recipe); 
            if (chests == chestCount)
                continue;
            chestCount = chests;
            recipes.add(recipe);
        }

        return recipes;
    }

    public static int countChests(INasaWorkbenchRecipe recipe)
    {
        int count = 0;
        ItemStack chest = new ItemStack(Blocks.CHEST);
        for (Entry<Integer, Ingredient> e : recipe.getRecipeInput().entrySet())
        {
            if (e.getValue().apply(chest))
                count++;
        }
        return count;
    }
}
