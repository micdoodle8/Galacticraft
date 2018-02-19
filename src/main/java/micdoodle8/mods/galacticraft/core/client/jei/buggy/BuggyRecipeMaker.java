package micdoodle8.mods.galacticraft.core.client.jei.buggy;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.GCItems;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class BuggyRecipeMaker
{
    public static List<INasaWorkbenchRecipe> getRecipesList()
    {
        List<INasaWorkbenchRecipe> recipes = new ArrayList<>();

        int chestCount = -1;
        for (INasaWorkbenchRecipe recipe : GalacticraftRegistry.getBuggyBenchRecipes())
        {
            int chests = BuggyRecipeMaker.countStorage(recipe); 
            if (chests == chestCount)
                continue;
            chestCount = chests;
            recipes.add(recipe);
        }

        return recipes;
    }

    public static int countStorage(INasaWorkbenchRecipe recipe)
    {
        int count = 0;
        ItemStack storage = new ItemStack(GCItems.partBuggy, 1, 2);
        for (Entry<Integer, ItemStack> e : recipe.getRecipeInput().entrySet())
        {
            if (ItemStack.areItemsEqual(storage, e.getValue()))
                count++;
        }
        return count;
    }
}
