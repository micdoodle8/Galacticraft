package micdoodle8.mods.galacticraft.core.client.jei.tier1rocket;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class Tier1RocketRecipeMaker
{
    public static List<Tier1RocketRecipeWrapper> getRecipesList()
    {
        List<Tier1RocketRecipeWrapper> recipes = new ArrayList<>();

        int chestCount = -1;
        for (INasaWorkbenchRecipe recipe : GalacticraftRegistry.getRocketT1Recipes())
        {
            int chests = Tier1RocketRecipeMaker.countChests(recipe); 
            if (chests == chestCount)
                continue;
            chestCount = chests;
            Tier1RocketRecipeWrapper wrapper = new Tier1RocketRecipeWrapper(recipe);
            recipes.add(wrapper);
        }

        return recipes;
    }

    public static int countChests(INasaWorkbenchRecipe recipe)
    {
        int count = 0;
        ItemStack chest = new ItemStack(Blocks.CHEST);
        for (Entry<Integer, ItemStack> e : recipe.getRecipeInput().entrySet())
        {
            if (ItemStack.areItemsEqual(chest, e.getValue()))
                count++;
        }
        return count;
    }
}
