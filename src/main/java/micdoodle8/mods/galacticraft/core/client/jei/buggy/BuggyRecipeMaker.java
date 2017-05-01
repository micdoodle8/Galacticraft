package micdoodle8.mods.galacticraft.core.client.jei.buggy;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;

import java.util.ArrayList;
import java.util.List;

public class BuggyRecipeMaker
{
    public static List<BuggyRecipeWrapper> getRecipesList()
    {
        List<BuggyRecipeWrapper> recipes = new ArrayList<>();

        for (INasaWorkbenchRecipe recipe : GalacticraftRegistry.getBuggyBenchRecipes())
        {
            BuggyRecipeWrapper wrapper = new BuggyRecipeWrapper(recipe);
            recipes.add(wrapper);
        }

        return recipes;
    }
}
